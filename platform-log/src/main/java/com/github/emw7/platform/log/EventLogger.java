package com.github.emw7.platform.log;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class EventLogger {

  private static final Marker MARKER_ENTRY = MarkerFactory.getMarker("entry");
  private static final Marker MARKER_EXIT = MarkerFactory.getMarker("exit");
  private static final Marker MARKER_DOING = MarkerFactory.getMarker("doing");
  private static final Marker MARKER_DONE = MarkerFactory.getMarker("done");
  private static final Marker MARKER_CAUGHT = MarkerFactory.getMarker("caught");
  private static final Marker MARKER_THROWING = MarkerFactory.getMarker("throwing");
  private static final Marker MARKER_NOTICE = MarkerFactory.getMarker("notice");

  public static class LogEventBuilder {

    private Logger log;
    private String pattern;
    private Object[] params;
    private Level level;
    private Marker marker;
    private String event;
    // this is a Map and not a list of MDCAutoclosable because TODO: ho interrotto così la frase...
    //  cosa volevo dire?
    private Map<String, String> contextArguments;

    // TODO aggiunto in un secondo momento, verificare se funziona!
    private LogEventBuilder () {}

    public LogEvent log() {
      if (contextArguments != null) {
        contextArguments.forEach(MDC::put);
      }
      log.atLevel(level).addMarker(marker).log(event + ' ' + pattern, params);
      return new LogEvent(log, MARKER_DOING, pattern, params, contextArguments);
    }

    /**
     * Deprecation note: use {@link #trace()}, {@link #debug()}, {@link #info()}, {@link #warn()}, {@link #error()}
     */
    @Deprecated
    public LogEventBuilder level(Level level) {
      this.level = level;
      return this;
    }

    public LogEventBuilder trace() {
      this.level = Level.TRACE;
      return this;
    }

    public LogEventBuilder debug() {
      this.level = Level.DEBUG;
      return this;
    }

    public LogEventBuilder info() {
      this.level = Level.INFO;
      return this;
    }

    public LogEventBuilder warn() {
      this.level = Level.WARN;
      return this;
    }

    public LogEventBuilder error() {
      this.level = Level.ERROR;
      return this;
    }

    public LogEventBuilder pattern(String pattern) {
      this.pattern = pattern;
      return this;
    }

    public EventLogger.LogEventBuilder params(Object... params) {
      this.params = params;
      return this;
    }

    public LogEventBuilder ctxArg(@NonNull final String key, @Nullable final Object val) {
      if (contextArguments == null) {
        contextArguments = new HashMap<>();
      }
      contextArguments.put(key, (val == null) ? "null" : val.toString());
      return this;
    }
  }

  public static LogEventBuilder doing(@NonNull final Logger log) {
    LogEventBuilder logEventBuilder = new LogEventBuilder();
    logEventBuilder.log = log;
    logEventBuilder.pattern = "";
    logEventBuilder.params = new Object[0];
    logEventBuilder.level = Level.INFO;
    logEventBuilder.marker = MARKER_DOING;
    logEventBuilder.event = "[   DOING]";
    return logEventBuilder;
  }

  /**
   * <b>Deprecation note</b>: Use {@link #doing(Logger)}.
   */
  public static LogEvent doing(@NonNull final Logger log, @NonNull final String pattern,
      Object... params) {
    final LogEvent logEvent = new LogEvent(log, MARKER_DOING, pattern, params, null);
    //noinspection StringConcatenationArgumentToLogCall
    log.info(MARKER_DOING, "[   DOING] " + pattern, params);
    return logEvent;
  }

  public static void done(@NonNull final LogEvent logEvent) {
    done(logEvent.getLog(), logEvent);
  }

  public static void done(@NonNull final Logger log, @NonNull final LogEvent logEvent) {
    log.atInfo().addMarker(MARKER_DONE)
        .log("[    DONE] " + logEvent.getPattern(), logEvent.getParams());
  }

  public static <E extends Throwable> void caught(@NonNull final E e,
      @NonNull final LogEvent logEvent) {
    caught(logEvent.getLog(), e, logEvent);
  }

  public static <E extends Throwable> void caught(@NonNull final Logger log, @NonNull final E e,
      @NonNull final LogEvent logEvent) {
    log.atError().addMarker(MARKER_CAUGHT).setCause(e)
        .log("[  CAUGHT] " + logEvent.getPattern(), logEvent.getParams());
  }

  public static <E extends Throwable> void caught(@NonNull final Logger log, @NonNull final E e,
      @NonNull final String pattern, Object... params) {
    log.atError().addMarker(MARKER_CAUGHT).setCause(e).log("[  CAUGHT] " + pattern, params);
  }

  public static <E extends Throwable> @NonNull E throwing(@NonNull final E e,
      @NonNull final LogEvent logEvent) {
    return throwing(logEvent.getLog(), e, logEvent);
  }

  public static <E extends Throwable> @NonNull E throwing(@NonNull final Logger log,
      @NonNull final E e, @NonNull final LogEvent logEvent) {
    log.atError().addMarker(MARKER_THROWING).setCause(e).addArgument(e.getMessage())
        .addArgument(e.getCause())
        .log("[THROWING] error '{}' caused by by '{}' while " + logEvent.getPattern(),
            logEvent.getParams());
    return e;
  }

  public static <E extends Throwable> @NonNull E throwing(@NonNull final Logger log,
      @NonNull final E e, @NonNull final String pattern, Object... params) {
    log.atError().addMarker(MARKER_THROWING).log("[THROWING] " + pattern, params);
    return e;
  }

  public static LogEventBuilder notice(@NonNull final Logger log) {
    LogEventBuilder logEventBuilder = new LogEventBuilder();
    logEventBuilder.log = log;
    logEventBuilder.pattern = "";
    logEventBuilder.params = new Object[0];
    logEventBuilder.level = Level.INFO;
    logEventBuilder.marker = MARKER_NOTICE;
    logEventBuilder.event = "[  NOTICE]";
    return logEventBuilder;
  }

  /**
   * <b>Deprecation note</b>: Use {@link #notice(Logger)}.
   */
  @Deprecated
  public static void notice(@NonNull final Logger log, @NonNull Level level,
      @NonNull final String pattern, Object... params) {
    log.atLevel(level).addMarker(MARKER_NOTICE).log("[  NOTICE] " + pattern, params);
  }

  /**
   * <b>Deprecation note</b>: Use {@link #notice(Logger)}.
   */
  @Deprecated
  public static void notice(@NonNull final Logger log, @NonNull final String pattern,
      Object... params) {
    notice(log, Level.INFO, pattern, params);
  }

  //region Constructors
  // prevents instantiation.
  private EventLogger() {
  }
  //endregion Constructors

}
