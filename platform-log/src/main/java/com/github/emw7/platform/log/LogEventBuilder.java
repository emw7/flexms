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
 * Builder for a {@link LogEvent}.
 * <p>
 * <b>Note</b>: This is not thread-safe as used {@link Set} implementation is not thread-safe.
 */
public class LogEventBuilder {

  //region Protected (final) properties
  protected final Logger log;

  protected final String event;
  protected final Marker marker;

  protected final String pattern;
  protected final Object[] params;

  protected final String uuid;
  protected final boolean uuidOn;

  //region Customisable properties

  protected Level level;

  protected Set<Arg<?>> args;
  //endregion Customisable properties

  //endregion Protected (final) properties

  //region Constructors
  // this is a package-private constructor; ideally only EventLogger construct this class.
  LogEventBuilder(@NonNull final Logger log, @NonNull final Marker marker,
      @NonNull final Level level, final boolean uuidOn, @NonNull final String pattern,
      @NonNull final Object... params) {
    this(log, marker, level, null, uuidOn, pattern, params);
  }

  LogEventBuilder(@NonNull final Logger log, @NonNull final Marker marker,
      @NonNull final Level level, @Nullable final String uuid, final boolean uuidOn,
      @NonNull final String pattern, @NonNull final Object... params) {
    this.log = log;

    this.event = marker.getName();
    this.marker = marker;

    this.pattern = pattern;
    this.params = params;

    this.uuid = Optional.ofNullable(uuid).orElse(UUID.randomUUID().toString());
    this.uuidOn = uuidOn;

    // customisable
    this.level = level;

    this.args = HashSet.newHashSet(3);
  }

  //endregion Constructors

  //region API

  /**
   * Builds and log the log event.
   * <p>
   * As most of the time log event is built to be logged and not for other scopes, this method has
   * been designed as build+log to allow using a more compact statement like
   * {@code new LogEventBuilder(log, ...).<builder-methods>.log()} in place of
   * {@code new LogEventBuilder(log, ...).<builder-methods>.build().log()}.
   * <p>
   * <b>Note<b>: the methods could have been designed as {@code void}, but the returned log event
   * can be used by other log events, like {@link EventLogger#done(DoingLogEvent)} that uses the
   * {@link EventLogger#doing(Logger, String, Object...)} log event.
   *
   * @return the built log event
   */
  public LogEvent log() {
    final LogEvent logEvent = build();
    logEvent.log();
    return logEvent;
  }

  //region Customisable properties

  //region level
  public @NonNull LogEventBuilder trace() {
    this.level = Level.TRACE;
    return this;
  }

  public @NonNull LogEventBuilder debug() {
    this.level = Level.DEBUG;
    return this;
  }

  public @NonNull LogEventBuilder info() {
    this.level = Level.INFO;
    return this;
  }

  public @NonNull LogEventBuilder warn() {
    this.level = Level.WARN;
    return this;
  }

  public @NonNull LogEventBuilder error() {
    this.level = Level.ERROR;
    return this;
  }
  //endregion level

  /**
   * Adds an arg that is valid for this event only.
   * <p>
   * For args that spread over multiple log events see {@link LogContext#addArg(String, Object)}
   *
   * @param name  name of the arg
   * @param value value of the arg
   * @param <T>   type of the arg's value
   * @return {@code this}
   */
  public <T> @NonNull LogEventBuilder arg(@NonNull final String name, @Nullable final T value) {
    return arg(Arg.of(name, value));
  }

  /**
   * See {@link #arg(String, Object)}
   */
  public <T> @NonNull LogEventBuilder arg(@NonNull final Arg<T> arg) {
    this.args.add(arg);
    return this;
  }

  //endregion Customisable properties

  //endregion API

  //region Private methods
  protected @NonNull LogEvent build() {
    return new LogEvent(log, marker, pattern, params, level, event, args, uuid, uuidOn);
  }
  //endregion Private methods

}
