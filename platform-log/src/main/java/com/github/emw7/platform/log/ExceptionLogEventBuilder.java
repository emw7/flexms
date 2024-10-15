package com.github.emw7.platform.log;

import static com.github.emw7.platform.log.LogEvent.UUID_ON;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;

/**
 * Builder for a {@link ExceptionLogEvent}.
 * <p>
 * <b>Note</b>: This is not thread-safe as inherits from LogEventBuilder that is not thread-safe.
 */
public final class ExceptionLogEventBuilder extends LogEventBuilder {

  //region Private (final) properties

  private final Throwable throwable;

  //region Customisable properties

  private boolean stackTraceOn;
  //endregion Customisable properties

  //endregion Private (final) properties

  //region Constructors
  // this is a package-private constructor; ideally only EventLogger construct this class.
  ExceptionLogEventBuilder(@NonNull final Logger log, @NonNull final Marker marker,
      @NonNull final Level level, final boolean stackTraceOn, @NonNull final Throwable throwable,
      @NonNull final String pattern, @NonNull final Object... params) {
    // the uuidOn argument is then completely ignored as it is the ExceptionLogEvent that forces 
    //  its value; in fact, the ExceptionLogEvent constructors do not have it among the arguments. 
    super(log, marker, level, !UUID_ON, pattern, params);

    this.throwable = throwable;

    // customisable
    this.stackTraceOn = stackTraceOn;
  }
  //endregion Constructors

  //region API

  /**
   * See {@link LogEventBuilder#log()}
   *
   * @return the built log event
   */
  public @NonNull ExceptionLogEvent log() {
    final ExceptionLogEvent logEvent = build();
    logEvent.log();
    return logEvent;
  }

  //region level

  /**
   * Do nothing as exception event must be logged as either error or warn only.
   *
   * @return {@code this}
   */
  public @NonNull ExceptionLogEventBuilder trace() {
    return this;
  }

  /**
   * Do nothing as exception event must be logged as either error or warn only.
   *
   * @return {@code this}
   */
  public @NonNull ExceptionLogEventBuilder debug() {
    return this;
  }

  /**
   * Do nothing as exception event must be logged as either error or warn only.
   *
   * @return {@code this}
   */
  public @NonNull ExceptionLogEventBuilder info() {
    return this;
  }

  /**
   * Log at {@code warn} level.
   *
   * @return {@code this}
   */
  public @NonNull ExceptionLogEventBuilder warn() {
    return (ExceptionLogEventBuilder) super.warn();
  }

  /**
   * Log at {@code error} level.
   *
   * @return {@code this}
   */
  public @NonNull ExceptionLogEventBuilder error() {
    return (ExceptionLogEventBuilder) super.error();
  }
  //endregion level

  /**
   * See {@link LogEventBuilder#arg(String, Object)}
   *
   * @return {@code this}
   */
  @SuppressWarnings("NullableProblems")
  @Override
  public <T> @NonNull ExceptionLogEventBuilder arg(@NonNull final String name,
      @NonNull final T value) {
    return (ExceptionLogEventBuilder) super.arg(name, value);
  }

  /**
   * See {@link LogEventBuilder#arg(String, Object)}
   *
   * @return {@code this}
   */
  @Override
  public <T> @NonNull ExceptionLogEventBuilder arg(@NonNull final Arg<T> arg) {
    return (ExceptionLogEventBuilder) super.arg(arg);
  }

  /**
   * Enables printing of exception stack trace.
   *
   * @return {@code this}
   */
  public @NonNull ExceptionLogEventBuilder stackTraceOn() {
    this.stackTraceOn = true;
    return this;
  }

  /**
   * Disables printing of exception stack trace.
   *
   * @return {@code this}
   */
  public @NonNull ExceptionLogEventBuilder stackTraceOff() {
    this.stackTraceOn = false;
    return this;
  }
  //endregion API

  //region Private methods
  protected @NonNull ExceptionLogEvent build() {
    return new ExceptionLogEvent(log, marker, pattern, params, level, event, args, uuid, uuidOn, throwable,
        stackTraceOn);
  }
  //endregion Private methods

}
