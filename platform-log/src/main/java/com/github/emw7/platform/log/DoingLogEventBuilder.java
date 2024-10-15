package com.github.emw7.platform.log;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Builder for a {@link DoingLogEvent}.
 * <p>
 * <b>Note</b>: This is not thread-safe as inherits from LogEventBuilder that is not thread-safe.
 *
 * @see EventLogger#doing(Logger, String, Object...)
 */
public class DoingLogEventBuilder extends LogEventBuilder {

  //region Protected (final) properties
  //endregion Protected (final) properties

  //region Constructors
  // this is a package-private constructor; ideally only EventLogger construct this class.
  DoingLogEventBuilder(@NonNull final Logger log, @NonNull final Marker marker,
      @NonNull final Level level, final boolean uuidOn, @NonNull final String pattern,
      @NonNull final Object... params) {
    super(log, marker, level,  uuidOn, pattern, params);
  }


  //endregion Constructors

  //region API

  /**
   * See {@link LogEventBuilder#log()}
   *
   * @return the built log event
   */
  public DoingLogEvent log() {
    return (DoingLogEvent)super.log();
  }

  //region Customisable properties

  //region level
  public @NonNull DoingLogEventBuilder trace() {
    return (DoingLogEventBuilder)super.trace();
  }

  public @NonNull DoingLogEventBuilder debug() {
    return (DoingLogEventBuilder)super.debug();
  }

  public @NonNull DoingLogEventBuilder info() {
    return (DoingLogEventBuilder)super.info();
  }

  public @NonNull DoingLogEventBuilder warn() {
    return (DoingLogEventBuilder)super.warn();
  }

  public @NonNull DoingLogEventBuilder error() {
    return (DoingLogEventBuilder)super.error();
  }
  //endregion level

  /**
   * See {@link LogEventBuilder#arg(String, Object)}
   *
   * @return {@code this}
   */
  public <T> @NonNull DoingLogEventBuilder arg(@NonNull final String name, @Nullable final T value) {
    return (DoingLogEventBuilder)super.arg(name,value);
  }

  /**
   * See {@link LogEventBuilder#arg(Arg)}
   *
   * @return {@code this}
   */
  public <T> @NonNull DoingLogEventBuilder arg(@NonNull final Arg<T> arg) {
    return (DoingLogEventBuilder)super.arg(arg);
  }

  //endregion Customisable properties

  //endregion API

  //region Private methods
  protected @NonNull DoingLogEvent build() {
    return new DoingLogEvent(log, marker, pattern, params, level, event, args, uuid, uuidOn);
  }
  //endregion Private methods

}
