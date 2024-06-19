package com.github.emw7.platform.log;

import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;

public final class EventLogger {

  //region Static
  private static final Logger logger = LoggerFactory.getLogger(EventLogger.class);

//  private static final ThreadLocal<Stack<LogEvent>> events;
//
//  private static void set() {
//    events.set(new Stack<>());
//  }
//
//  private static Stack<LogEvent> get() {
//    if (events.get() == null) {
//      set();
//    }
//    return events.get();
//  }
//
//  private static LogEvent pop(@NonNull final String eventUuid) {
//    while (!events.get().empty()) {
//      final LogEvent event = events.get().pop();
//      if (event.getUuid().equals(eventUuid)) {
//        return event;
//      }
//    }
//    return null;
//  }
//
//  static {
//    events = new ThreadLocal<>();
//  }
  //endregion Static

  private static final Marker MARKER_ENTRY = MarkerFactory.getMarker("entry");
  private static final Marker MARKER_EXIT = MarkerFactory.getMarker("exit");
  private static final Marker MARKER_DOING = MarkerFactory.getMarker("doing");
  private static final Marker MARKER_DONE = MarkerFactory.getMarker("done");
  private static final Marker MARKER_CAUGHT = MarkerFactory.getMarker("caught");
  private static final Marker MARKER_THROWING = MarkerFactory.getMarker("throwing");
  private static final Marker MARKER_NOTICE = MarkerFactory.getMarker("notice");

//  public static void reset() {
//    if (!get().empty()) {
//      logger.error("[LOGGING] stack for thread '{}' was not empty",
//          Thread.currentThread().getName());
//    }
//    set();
//  }
  /*

     methodA
       final event;
       try {
         event= doing(...)
         do...
         done(event) [pop event]
       } catch ( Exception e ) {
           caught(event) [pop event]
           throwing(new Exception(e), event, ...)
       }

     methodB
       final event
       try {
         event= doing(...)
         methodA(...)
         done(event) [pop event]
       } catch ( Exception e ) {
           caught(e, event, ...) [pop event]
       }

     methodC
       try {
         methodB(...)
       } catch ( Exception e ) {
           caught(...) [NO pop... there isn't event]
           throwing(...)
       }
   */

  public static LogEvent doing(@NonNull final Logger log, @NonNull final String pattern,
      Object... params) {
    final LogEvent logEvent = new LogEvent(log, MARKER_DOING, pattern, params);
    //noinspection StringConcatenationArgumentToLogCall
    log.info(MARKER_DOING, "[   DOING] " + pattern, params);
//    get().push(logEvent);
    return logEvent;
  }

  public static void done(@NonNull final LogEvent logEvent) {
    done(logEvent.getLog(), logEvent);
  }

  public static void done(@NonNull final Logger log, @NonNull final LogEvent logEvent) {
//    final LogEvent doingEvent = pop(logEvent.getUuid());
//    if (doingEvent != null) {
//      log.info(MARKER_DONE, "[    DONE] " + doingEvent.getPattern(), doingEvent.getParams());
//    } else {
//      log.atWarn().log(
//          "[LOGGING] done event called for event with uuid '{}' that cannot be found in the event stack",
//          logEvent.getUuid());
      log.atInfo().addMarker(MARKER_DONE)
          .log("[    DONE] " + logEvent.getPattern(), logEvent.getParams());
//    }
  }

  public static <E extends Throwable> void caught(@NonNull final E e,
      @NonNull final LogEvent logEvent) {
    caught(logEvent.getLog(), e, logEvent);
  }

  public static <E extends Throwable> void caught(@NonNull final Logger log, @NonNull final E e,
      @NonNull final LogEvent logEvent) {
//    final LogEvent doingEvent = pop(logEvent.getUuid());
//    if (doingEvent != null) {
//      log.atError().addMarker(MARKER_CAUGHT).setCause(e).addArgument(e.getMessage())
//          .log("[  CAUGHT] error '{}' while " + doingEvent.getPattern(), doingEvent.getParams());
//    } else {
//      log.atWarn().log(
//          "[LOGGING] caught event called for event with uuid '{}' that cannot be found in the event stack",
//          logEvent.getUuid());
      log.atError().addMarker(MARKER_CAUGHT).setCause(e)
          .log("[  CAUGHT] " + logEvent.getPattern(), logEvent.getParams());
//    }
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
//    final LogEvent doingEvent = pop(logEvent.getUuid());
//    if (doingEvent != null) {
//      log.atError().addMarker(MARKER_THROWING).addArgument(e.getMessage()).addArgument(e.getCause())
//          .log("[THROWING] error '{}' caused by '{}' while " + doingEvent.getPattern(),
//              doingEvent.getParams());
//    } else {
//      log.atWarn().log(
//          "[LOGGING] throwing event called for event with uuid '{}' that cannot be found in the event stack",
//          logEvent.getUuid());
      log.atError().addMarker(MARKER_THROWING).setCause(e).addArgument(e.getMessage())
          .addArgument(e.getCause())
          .log("[THROWING] error '{}' caused by by '{}' while " + logEvent.getPattern(),
              logEvent.getParams());
//    }
    return e;
  }

  public static <E extends Throwable> @NonNull E throwing(@NonNull final Logger log,
      @NonNull final E e, @NonNull final String pattern, Object... params) {
    log.atError().addMarker(MARKER_THROWING).log("[THROWING] " + pattern, params);
    return e;
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
