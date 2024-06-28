package com.github.emw7.platform.log;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;

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

    public void log() {
      log.atLevel(level).addMarker(MARKER_NOTICE).log(event + pattern, params);
    }

    public LogEventBuilder level(Level level) {this.level= level; return this;}
    public LogEventBuilder pattern(String pattern) {this.pattern= pattern; return this;}
    public LogEventBuilder params(Object... params) {this.params= params; return this;}
  }

  public static LogEvent doing(@NonNull final Logger log, @NonNull final String pattern,
      Object... params) {
    final LogEvent logEvent = new LogEvent(log, MARKER_DOING, pattern, params);
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

  public static LogEventBuilder noticeLogEvent(@NonNull final Logger log) {
    LogEventBuilder logEvent= new LogEventBuilder();
    logEvent.log= log;
    logEvent.pattern="";
    logEvent.params= new Object[0];
    logEvent.level= Level.INFO;
    logEvent.marker= MARKER_NOTICE;
    logEvent.event= "[  NOTICE]";
    return logEvent;
  }

  public static void notice(@NonNull final Logger log, @NonNull Level level,
      @NonNull final String pattern, Object... params) {
    log.atLevel(level).addMarker(MARKER_NOTICE).log("[  NOTICE] " + pattern, params);
  }

  public static void notice(@NonNull final Logger log,
      @NonNull final String pattern, Object... params) {
    notice(log, Level.INFO, pattern, params);
  }

  //region Constructors
  // prevents instantiation.
  private EventLogger() {
  }
  //endregion Constructors

}
