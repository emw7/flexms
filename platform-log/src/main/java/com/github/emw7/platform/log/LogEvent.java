package com.github.emw7.platform.log;

import com.github.emw7.platform.core.mapper.BooleanMapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LoggingEventBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The plain log event.
 * <p>
 * This class serves as parent for other log events, and so this documentation is valid for children
 * too.
 * <p>
 * This class cannot be instantiated directly, but must be instantiated by related builder that in
 * turn must be got from {@link EventLogger} static methods that define specific log events. Refers
 * to that class for details of log events. A log event is something that can be logged.<br/> To
 * produce the log the {@link #log()} method must be invoked.<br/>
 * <p>
 * A log event is characterized by the following properties:
 * <ul>
 * <li>The <a href="https://slf4j.org/api/org/slf4j/Marker.html">marker</a> with which mark the
 *     log event; it is defined by the {@link EventLogger} method related to the log event it self and cannot be changed.</li>
 * <li>The event name; it defined by the {@link EventLogger} method related to the log event it self and cannot be changed.</li>
 * <li>The uuid of the event; some events have the uuid equals to the one of the log event they are paired to ({@code done} log event is paired with {@code doing} log event and so {@code done} has the same uuid of the {@code doing})</li>
 * <li>The message pattern and an array of params with actual value for placeholders in pattern[*].</li>
 * <li>The level at which the log event is logged.</li>
 * <li>The args, (name, key) pairs that characterises the specific event.</li>
 * <li></li>
 * </ul>
 * Log event uuid is auto-generated if supplied {@code uuid} is {@code null}.
 * <p>
 * <b>[*] Pattern and params</b><br/>
 * {@code pattern} is the actual message of the log.
 * It is logged within the {@code what} key.
 * It
 * can contain placeholders {@code {}} that are replaced by actual values specified in
 * {@code params}.
 * Placeholders are positional placeholders, that is first {@code {}} is replaced
 * by first param, second {@code {}} is replaced by second param, and so on.
 *
 * @param log     the {@link Logger} through which log
 * @param marker  the marker with which mark the log
 * @param pattern the {@code what} message to log
 * @param params  the actual values for pattern placeholders
 * @param level   level at which log
 * @param event   name of the logged event
 * @param args    arguments to be logged together with the message
 * @param uuid    uuid for the log event
 * @param uuidOn  whether add the uuid to the log message or not; is overridden by
 *                {@link #alwaysPrintUuid}
 */
public class LogEvent {

  //region Package-private static final properties
  static final boolean UUID_ON = true;
  //endregion Package-private static final properties

  //region Private static final properties

  /**
   * The format for the log message; each {@code {}} is a placeholder for a key:
   * <ol>
   * <li>event</li>
   * <li>uuid</li>
   * <li>what</li>
   * <li>custom-keys</li>
   * <li>ctx-args</li>
   * <li>args</li>
   * </ol>
   *
   * @see #log()
   */
  private static final String LOG_FORMAT = "{}{}{}{}{}{}";

  /**
   * If {@code true} then uuid is always printed regardless of {@code uuidOn} value.
   * <p>
   * Example-A
   * <pre>
   * alwaysPrintUuid : true
   * uuidOn : any
   * => uuid is printed with log message
   * </pre>
   * Example-B
   * <pre>
   * alwaysPrintUuid : false
   * uuidOn : false
   * => uuid is not printed with log message
   * </pre>
   * Example-C
   * <pre>
   * alwaysPrintUuid : false
   * uuidOn : true
   * => uuid is printed with log message
   * </pre>
   */
  private static final boolean alwaysPrintUuid;

  /**
   * If {@code true} the log events are submitted to be logged in a different thread.
   * <p>
   * That should improve performance given that thread creation and submission take less
   * than log event actual logging!
   */
  private static final boolean logOnThread;

  private static final ExecutorService EXECUTOR;

  private static final ThreadFactory tf;

  public static final CountDownLatch terminated;
  //endregion Private static final properties

  //region Static initialization
  static {

    terminated = new CountDownLatch(1);

    final Environment environment = new StandardEnvironment();

    final String envAlwaysPrintUuid = environment.getProperty(
        "com.github.emw7.platform.log.always-print-uuid", "false");
    alwaysPrintUuid = BooleanMapper.fromString(envAlwaysPrintUuid);

    final String envLogOnThread = environment.getProperty(
        "com.github.emw7.platform.log.log-on-thread", "false");
    logOnThread = BooleanMapper.fromString(envLogOnThread);

    if (logOnThread) {

      tf= Thread.ofPlatform().name("emw7-platform-log-", 1).daemon(true).factory();

      EXECUTOR = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public @Nullable Thread newThread(@NonNull final Runnable r) {

          // why this?
          //  because from https://logback.qos.ch/manual/mdc.html:
          //  Please note that MDC as implemented by logback-classic assumes that values are placed into the MDC with moderate frequency. Also note that a child thread does not automatically inherit a copy of the mapped diagnostic context of its parent.
          //  and from https://logback.qos.ch/manual/mdc.html#managedThreads:
          //  A copy of the mapped diagnostic context can not always be inherited by worker threads from the initiating thread. This is the case when java.util.concurrent.Executors is used for thread management. For instance, newCachedThreadPool method creates a ThreadPoolExecutor and like other thread pooling code, it has intricate thread creation logic.
          //  In such cases, it is recommended that MDC.getCopyOfContextMap() is invoked on the original (master) thread before submitting a task to the executor. When the task runs, as its first action, it should invoke MDC.setContextMap() to associate the stored copy of the original MDC values with the new Executor managed thread.
          final Map<String, String> parentContextMap = MDC.getCopyOfContextMap();
          Runnable wrapingRunnable = () -> {
            MDC.setContextMap(parentContextMap);
            r.run();
          };
          // disabled warning as it is not possible that tf is null as it is null only if logOn>thread
          //  if false and in such a case we do not drop in this branch.
          //noinspection DataFlowIssue
          return tf.newThread(wrapingRunnable);
        }
      });

      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("shutting down platform-log");
        EXECUTOR.shutdown();
        try {
          // Wait a while for existing tasks to terminate
          if (!EXECUTOR.awaitTermination(60, TimeUnit.SECONDS)) {
            // Cancel currently executing tasks forcefully
            EXECUTOR.shutdownNow();
            // Wait a while for tasks to respond to being cancelled
            //noinspection ResultOfMethodCallIgnored
            EXECUTOR.awaitTermination(60, TimeUnit.SECONDS);
            System.out.println("platform-log did not terminate properly");
          } else {
            System.out.println("platform-log terminated properly");
          }
        } catch (InterruptedException ex) {
          // (Re-)Cancel if current thread also interrupted
          EXECUTOR.shutdownNow();
          // Preserve interrupt status
          Thread.currentThread().interrupt();
        } finally {
          terminated.countDown();
        }
      }));
    }
    else {
      EXECUTOR= null;
      tf= null;
    }
  }
  //endregion Static initialization

  //region Private final properties
  private final Logger log;

  private final Marker marker;
  private final String pattern;
  // these are actual values for pattern placeholders:
  //  pattern: "this is a {} day, and everybody is {}";
  //  params: ["beautiful", "happy"]
  //  => "this ia a beautiful day, and everybody is happy"
  private final Object[] params;

  private final Level level;
  // strictly tied with marker; in fact initialized as market.getName().
  private final String event;

  // these are [#arg:name=value].
  private final Set<Arg<?>> args;

  private final String uuid;
  // whether print or nor the uuid.
  private final boolean uuidOn;
  //endregion Private final properties

  //region Constructors

  /**
   * Creates a log event.
   * <p>
   * Log event uuid is auto-generated if supplied {@code uuid} is {@code null}.
   * <p>
   * <b>Pattern and params</b><br/>
   * {@code pattern} is the actual message of the log. It is logged within the {@code what} key. It
   * can contain placeholders {@code {}} that are replaced by actual values specified in
   * {@code params}. Placeholders are positional placeholders, that is first {@code {}} is replaced
   * by first param, second {@code {}} is replaced by second param, and so on.
   *
   * @param log     the {@link Logger} through which log
   * @param marker  the marker with which mark the log
   * @param pattern the {@code what} message to log
   * @param params  the actual values for pattern placeholders
   * @param level   level at which log
   * @param event   name of the logged event
   * @param args    arguments to be logged together with the message
   * @param uuid    uuid for the log event
   * @param uuidOn  whether add the uuid to the log message or not; is overridden by
   *                {@link #alwaysPrintUuid}
   */
  LogEvent(@NonNull final Logger log, @NonNull final Marker marker, @NonNull final String pattern,
      @NonNull final Object[] params, @NonNull final Level level, @NonNull final String event,
      @NonNull final Set<Arg<?>> args, @NonNull final String uuid, final boolean uuidOn) {
    this.log = log;

    this.marker = marker;

    this.pattern = pattern;
    // these are actual values for pattern placeholders.
    this.params = params;

    this.level = level;
    this.event = event;

    this.args = args;

    this.uuid = uuid;
    this.uuidOn = uuidOn;
  }
  //endregion Constructors

  //region API

  /**
   * TODO
   * <p>
   * <b>Note</b>: while can be safely invoked many times, different invocations can produce
   * different outputs as arguments are retrieved from {@code MDC} too.
   */
  public final void log() {
    if (logOnThread) {
      try {
        EXECUTOR.submit(this::_log);
      } catch (RejectedExecutionException e) {
        System.out.printf("rejected log %s%n", this);
      }
    } else {
      _log();
    }
  }

  //endregion API

  //region Template method: log

  /**
   * Allows children to customise the SLF4J LoggingEventBuilder.
   */
  protected @NonNull LoggingEventBuilder customiseLoggingEventBuilder(
      @NonNull final LoggingEventBuilder loggingEventBuilder) {
    return loggingEventBuilder;
  }

  /**
   * Allows children to add custom keys as a set of {@link Arg}.
   */
  protected @Nullable Set<Arg<?>> customKeys() {
    return null;
  }
  //endregion Template method: log

  //region Getters & Setters
  @NonNull
  Logger getLog() {
    return log;
  }

  // maker getter is not needed.

  @NonNull
  String getPattern() {
    return pattern;
  }

  @NonNull
  Object[] getParams() {
    return params;
  }

  @NonNull
  Level getLevel() {
    return level;
  }

  /**
   * Returns the args set.
   * <p>
   * <b>Note</b>: returns the set itself and not a copy!
   *
   * @return the args set
   */
  @NonNull
  Set<Arg<?>> getArgs() {
    return args;
  }

  @NonNull
  String getUuid() {
    return uuid;
  }
  //endregion Getters & setters

  //region Private methods

  // key with name and value (arg:name=value).
  // key with value only (arg:value).
  private @NonNull String key(@NonNull final String tag, @NonNull final String value) {
    return "[#" + tag + ':' + value + ']';
  }

  private @NonNull String keyArg(@NonNull final String tag, @NonNull final String name,
      @NonNull final String value) {
    return "[#" + tag + ':' + name + '=' + value + ']';
  }

  private @NonNull String keyUuid() {
    return alwaysPrintUuid | uuidOn ? key("uuid", uuid) : "";
  }

  // creates the string for the args (obtained by joining all the keys for the args)
  private @NonNull String buildArgsString() {
    return args.stream().map(arg -> keyArg("arg", arg.name(), arg.asString()))
        .collect(Collectors.joining());
  }

  // creates the string for the context args (obtained by joining all the keys for the context args)
  private @NonNull String buildCtxArgsString() {
    return Optional.ofNullable(MDC.getCopyOfContextMap()).orElseGet(HashMap::new).entrySet()
        .stream().map(arg -> keyArg("arg", arg.getKey(), arg.getValue()))
        .collect(Collectors.joining());
  }

  // creates the string for the custom keys (obtained by joining all the keys obtained invoking
  //  key(arg.name(), arg.asString()))
  private @NonNull String buildCustomKeysString(@Nullable Set<Arg<?>> args) {
    return Optional.ofNullable(args).orElseGet(HashSet::new).stream()
        .map(arg -> key(arg.name(), arg.asString())).collect(Collectors.joining());
  }

  public @NonNull String toString ()
  {
    final String what = MessageFormatter.arrayFormat(pattern, params).getMessage();
    return String.format("%s%s%s%s%s%s",
        key("event", event),
        keyUuid(),
        key("what", what),
        buildCustomKeysString(customKeys()),
        buildCtxArgsString(),
        buildArgsString());
  }

  /**
   * Actual logs.
   */
  private void _log () {
    LoggingEventBuilder loggingEventBuilder = log.atLevel(level).addMarker(marker);
    loggingEventBuilder = customiseLoggingEventBuilder(loggingEventBuilder);

    final String what = MessageFormatter.arrayFormat(pattern, params).getMessage();

    loggingEventBuilder.log(LOG_FORMAT,
        key("event", event),
        keyUuid(),
        key("what", what),
        buildCustomKeysString(customKeys()),
        buildCtxArgsString(),
        buildArgsString());
  }

  //endregion Private methods
}
