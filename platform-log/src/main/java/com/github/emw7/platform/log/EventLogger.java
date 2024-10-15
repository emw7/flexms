package com.github.emw7.platform.log;

import static com.github.emw7.platform.log.LogEvent.UUID_ON;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Provides methods for getting log events builders.
 * <p>
 * <b>Suggestion</b>: use static import {@code import static com.github.emw7.platform.log.EventLogger.*}.
 */
public final class EventLogger {

  //region Package-private static final properties
  private static final Marker MARKER_ENTRY = MarkerFactory.getMarker("entry");
  private static final Marker MARKER_EXIT = MarkerFactory.getMarker("exit");
  static final Marker MARKER_DOING = MarkerFactory.getMarker("doing");
  static final Marker MARKER_DONE = MarkerFactory.getMarker("done");
  static final Marker MARKER_CAUGHT = MarkerFactory.getMarker("caught");
  static final Marker MARKER_THROWING = MarkerFactory.getMarker("throwing");
  static final Marker MARKER_NOTICE = MarkerFactory.getMarker("notice");
  //endregion Package-private static final properties

  //region Public static methods

  /**
   * Returns a builder for a {@code doing} log event.
   * <p>
   * A {@code doing} log event is used to stress the starting of an operation; conclusion of such an operation
   * must be stressed with a {@link #done(DoingLogEvent, Object)} log event.
   * <p>
   * The {@code doing} log event:
   * <ul>
   * <li>Enables uuid printing (that is generated randomly).</li>
   * <li>Event is {@code doing}.</li>
   * <li>Uses {@link #MARKER_DOING}.</li>
   * <li>Level is {@link Level#INFO}.</li>
   * <li>Allows all levels.</li>
   * <li>Allows adding args.</li>
   * </ul>
   *
   * @param log SLF4J logger through to print the log messages
   * @param pattern pattern of the {@code what} key
   * @param params actual values for the pattern placeholders
   *
   * @return a builder for a {@code doing} log event
   *
   * @see #done(DoingLogEvent, Object)
   * @see #done(DoingLogEvent)
   */
  public static @NonNull DoingLogEventBuilder doing(@NonNull final Logger log,
      @NonNull final String pattern, @NonNull final Object... params) {
    return new DoingLogEventBuilder(log, MARKER_DOING, Level.INFO, UUID_ON, pattern, params);
  }

  /**
   * Returns a builder for a {@code done} log event.
   * <p>
   * A {@code done} log event is used to stress the conclusion of an operation; starting of such an operation
   * must be stressed with a {@link #doing(Logger, String, Object...)} log event.
   * <p>
   * A {@code done} log event is paired with a {@link #doing(Logger, String, Object...)} log event it inherits all the information (but marker) from the
   * paired doing event:
   * <pre>
   * LogEvent doingSomethingEvt= doing(log, "do something: {}", "now").args("a", 1).log();
   * // do something...
   * done(doingSomethingEvt).log();
   *
   * logs something like:
   * [#event:doing][#uuid:123-xyz][#what:do something: now][#arg:a=1]
   * ...
   * [#event:done][#uuid:123-xyz][#what:do something: now][#result:void][#arg:a=1]
   * </pre>
   * <p>
   * The {@code done} log event:
   * <ul>
   * <li>Inherits the uuid from related doing log event, and prints it.</li>
   * <li>Enables uuid printing that is set to equals to the one of the paired doing log event.</li>
   * <li>Event is {@code done}.</li>
   * <li>Uses {@link #MARKER_DONE}.</li>
   * <li>Disallows overriding of level, that is prints at the same level of the paired {@code doing} log event</li>
   * <li>Allows adding args.</li>
   * <li>Adds the {@code result} key by which value is the result of the related operation[*].</li>
   * </ul>
   * <p>
   * [*]: The {@link #done(DoingLogEvent)} overload, that is the version used in the example, must be used for
   * {@code void} operation, while this overload can be used for
   * operations that return a value. As result, event if is a key, is managed as an {@link Arg},
   * refer to {@link Arg#asString()} to the details on how it is printed.
   *
   * @param logEvent the doing log event with this {@code done} log event is paired to.
   * @param result the result of the related operation
   * @param <R> type of the result
   *
   * @return a builder for a {@code done} log event
   */
  public static <R> @NonNull DoneLogEventBuilder<R> done(@NonNull final DoingLogEvent logEvent,
      @Nullable R result) {
    return new DoneLogEventBuilder<>(logEvent.getLog(), MARKER_DONE, logEvent.getLevel(),
        logEvent.getArgs(), logEvent.getUuid(), result, logEvent.getPattern(),
        logEvent.getParams());
  }

  /**
   * See {@link #done(DoingLogEvent, Object)}.
   */
  public static @NonNull DoneLogEventBuilder<Void> done(@NonNull final DoingLogEvent logEvent) {
    return new DoneLogEventBuilder<>(logEvent.getLog(), MARKER_DONE, logEvent.getLevel(),
        logEvent.getArgs(), logEvent.getUuid(), logEvent.getPattern(), logEvent.getParams());
  }

  /**
   * Returns a builder for a {@code caught} log event.
   * <p>
   * The {@code caught} log event:
   * <ul>
   * <li>Disables uuid printing (the uuid is generated randomly, anyway).</li>
   * <li>Event is {@code caught}.</li>
   * <li>Uses {@link #MARKER_CAUGHT}.</li>
   * <li>Level is {@link Level#ERROR}.</li>
   * <li>Allows only {@code error} and {@code warn} levels.</li>
   * <li>Allows adding args.</li>
   * <li>Enables printing of exception stack trace.</li>
   * <li>Adds the {@code ex-type}, {@code ex-msg}, {@code ex-cause-type} and {@code ex-cause-msg} key which values are:
   *     type of the exception, exception's message, type of the cause and cause's message (last two get {@code "N/A"} value if the exception has not the cause</li>
   * </ul>
   *
   * @param log       SLF4J logger through to print the log messages
   * @param throwable the exception this event is for
   * @param pattern   pattern of the {@code what} key
   * @param params    actual values for the pattern placeholders
   * @param <T>       type of the exception
   *
   * @return a builder for a {@code caught} log event
   */
  public static <T extends Throwable> @NonNull ExceptionLogEventBuilder caught(
      @NonNull final Logger log, @NonNull final T throwable, @NonNull final String pattern,
      @NonNull final Object... params) {
    return new ExceptionLogEventBuilder(log, MARKER_CAUGHT, Level.ERROR, true, throwable, pattern,
        params);
  }

  /**
   * Returns a builder for a {@code throwing} log event.
   * <p>
   * The {@code throwing} log event:
   * <ul>
   * <li>Disables uuid printing (the uuid is generated randomly, anyway).</li>
   * <li>Event is {@code throwing}.</li>
   * <li>Uses {@link #MARKER_THROWING}.</li>
   * <li>Level is {@link Level#ERROR}.</li>
   * <li>Allows only {@code error} and {@code warn} levels.</li>
   * <li>Allows adding args.</li>
   * <li>Disables printing of exception stack trace.</li>
   * <li>Adds the {@code ex-type}, {@code ex-msg}, {@code ex-cause-type} and {@code ex-cause-msg} key which values are:
   *     type of the exception, exception's message, type of the cause and cause's message (last two get {@code "N/A"} value if the exception has not the cause</li>
   * </ul>
   *
   * @param log       SLF4J logger through to print the log messages
   * @param throwable the exception this event is for
   * @param pattern   pattern of the {@code what} key
   * @param params    actual values for the pattern placeholders
   * @param <T>       type of the exception
   *
   * @return a builder for a {@code throwing} log event
   */
  public static <T extends Throwable> @NonNull ExceptionLogEventBuilder throwing(
      @NonNull final Logger log, @NonNull final T throwable, @NonNull final String pattern,
      @NonNull final Object... params) {
    return new ExceptionLogEventBuilder(log, MARKER_THROWING, Level.ERROR, false, throwable,
        pattern, params);
  }

  /**
   * Returns a builder for a {@code notice} log event.
   * <p>
   * The {@code notice} log event:
   * <ul>
   * <li>Disables uuid printing (the uuid is generated randomly, anyway).</li>
   * <li>Event is {@code notice}.</li>
   * <li>Uses {@link #MARKER_NOTICE}.</li>
   * <li>Level is {@link Level#INFO}.</li>
   * <li>Allows all levels.</li>
   * <li>Allows adding args.</li>
   * </ul>
   *
   * @param log SLF4J logger through to print the log messages
   * @param pattern pattern of the {@code what} key
   * @param params actual values for the pattern placeholders
   *
   * @return a builder for a {@code notice} log event
   */
  public static @NonNull LogEventBuilder notice(@NonNull final Logger log,
      @NonNull final String pattern, @NonNull final Object... params) {
    return new LogEventBuilder(log, MARKER_NOTICE, Level.INFO, !UUID_ON, pattern, params);
  }

  /**
   * Returns a logging context in which context arguments can be added and removed.
   *
   * @param ctxArgs starting list of context arguments
   *
   * @return a logging context in which context arguments can be added and removed
   */
  public static @NonNull LogContext context(@NonNull final Arg<?>... ctxArgs) {
    return new LogContext(ctxArgs);
  }
  //region Public static methods

  //region Constructors
  // prevents instantiation.
  private EventLogger() {
  }
  //endregion Constructors

}
