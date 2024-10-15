package com.github.emw7.platform.log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;
import org.springframework.lang.NonNull;

/**
 * The log event for events with exception.
 * <p>
 * <b>Configuration</b>
 * <ul>
 * <li>Disables uuid printing (see {@link ExceptionLogEvent#ExceptionLogEvent(Logger, Marker, String, Object[], Level, String, Set, String, boolean, Throwable, boolean)}.</li>
 * <li>If stack trace is on then enables stack trace printinga (see {@link #customiseLoggingEventBuilder(LoggingEventBuilder)}).</li>
 * <li>Adds the args related to the throwable tied to the log event and, if any, to the related cause (see {@link #customKeys()}).</li>
 * </ul>
 *
 * @see EventLogger#caught(Logger, Throwable, String, Object...)
 * @see EventLogger#throwing(Logger, Throwable, String, Object...)
 */
public class ExceptionLogEvent extends LogEvent {

  //region Private final properties
  private final Throwable throwable;
  private final boolean stackTraceOn;
  //endregion Private final properties

  //region Constructors
  ExceptionLogEvent(@NonNull final Logger log, @NonNull final Marker marker,
      @NonNull final String pattern, @NonNull final Object[] params, @NonNull final Level level,
      @NonNull final String event, @NonNull final Set<Arg<?>> args,
      @NonNull final String uuid, final boolean uuidOn,
      @NonNull final Throwable throwable, final boolean stackTraceOn) {
    super(log, marker, pattern, params, level, event, args, uuid, uuidOn);
    this.throwable = throwable;
    this.stackTraceOn = stackTraceOn;
  }
  //endregion Constructors

  //region Template method: log

  /**
   * Customises the {@link LoggingEventBuilder} by adding the exception if stack trace is on.
   *
   * @param loggingEventBuilder the {@link LoggingEventBuilder} to customise
   * @return the provided {@link LoggingEventBuilder}
   */
  @Override
  protected @NonNull LoggingEventBuilder customiseLoggingEventBuilder(
      @NonNull final LoggingEventBuilder loggingEventBuilder) {
    if (stackTraceOn) {
      return loggingEventBuilder.setCause(throwable);
    } else {
      return loggingEventBuilder;
    }
  }

  /**
   * Returns an unmodifiable set containing args for the throwable tied to this log event and, if
   * any, for the related cause.
   * <p>
   * The added args are the following:
   * <ul>
   * <li>{@code ex-type}: the name of the class of the provided throwable.</li>
   * <li>{@code ex-msg}: the localised message of the provided throwable.</li>
   * <li>{@code ex-cause-type}: the name of the class of the cause of the provided throwable[*].</li>
   * <li>{@code ex-cause-msg}: the localised message of the cause of the provided throwable[*].</li>
   * </ul>
   * </ul>
   * <p>
   * [*]: {@code "N/A"} if cause is not available.
   *
   * @return an unmodifiable set containing args for the throwable tied to this log event and, if *
   * * any, for the related cause
   */
  @Override
  protected @NonNull /*unmodifiable*/ Set<Arg<?>> customKeys() {
    Set<Arg<?>> keysSet = new HashSet<>(4);
    keysSet.add(Arg.of("ex-type", throwable.getClass().getName()));
    keysSet.add(Arg.of("ex-msg", throwable.getLocalizedMessage()));

    if (throwable.getCause() != null) {
      final Throwable cause = throwable.getCause();
      keysSet.add(Arg.of("ex-cause-type", cause.getClass().getName()));
      keysSet.add(Arg.of("ex-cause-msg", cause.getLocalizedMessage()));
    } else {
      keysSet.add(Arg.of("ex-cause-type", "N/A"));
      keysSet.add(Arg.of("ex-cause-msg", "N/A"));
    }
    return Collections.unmodifiableSet(keysSet);
  }

  //endregion Template method: log

}
