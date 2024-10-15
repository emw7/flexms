package com.github.emw7.platform.log;

import static com.github.emw7.platform.log.LogEvent.UUID_ON;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Builder for a {@link DoneLogEvent}.
 * <p>
 * <b>Note</b>: This is not thread-safe as inherits from LogEventBuilder that is not thread-safe.
 * 
 * @param <R> type of the result
 * 
 * @see EventLogger#done(DoingLogEvent, Object)
 * @see EventLogger#done(DoingLogEvent)
 */
public final class DoneLogEventBuilder<R> extends LogEventBuilder {

  //region Package-private static final properties
  static final boolean IS_VOID = true;
  //endregion Package-private static final properties

  //region Private (final) properties
  private final R result;
  //private final String uuid;

  // this is not a characteristic of the event, but is supporting void methods.
  private final boolean isVoid;
  //endregion Private (final) properties

  //region Constructors

  //region Package-private
  // following are package-private constructors; ideally only EventLogger construct this class.

  /**
   * Constructor for non-void result.
   */
  DoneLogEventBuilder(@NonNull final Logger log,
      @NonNull final Marker marker,
      @NonNull final Level level,
      @NonNull final Set<Arg<?>> args,
      @NonNull final String uuid,
      @Nullable final R result,
      @NonNull final String pattern, @NonNull final Object... params) {
    this(log, marker, level, args, uuid, result, !IS_VOID, pattern, params);
  }

  /**
   * Constructor for void result.
   */
  DoneLogEventBuilder(@NonNull final Logger log,
      @NonNull final Marker marker,
      @NonNull final Level level,
      @NonNull final Set<Arg<?>> args,
      @NonNull final String uuid,
      @NonNull final String pattern, @NonNull final Object... params) {
    this(log, marker, level, args, uuid, null, IS_VOID, pattern, params);
  }
  //endregion Package-private

  //region Private
  private DoneLogEventBuilder(@NonNull final Logger log,
      @NonNull final Marker marker,
      @NonNull final Level level,
      @NonNull final Set<Arg<?>> args,
      @NonNull final String uuid,
      @Nullable final R result, final boolean isVoid,
      @NonNull final String pattern, @NonNull final Object... params) {
    // the uuidOn argument is then completely ignored as it is the DoneLogEvent that forces
    //  its value; in fact, the DoneLogEvent constructors do not have it among the arguments.
    super(log, marker, level, uuid, UUID_ON, pattern, params);

    this.result = result;
    this.isVoid = isVoid;
    //this.uuid= uuid;

    // args are set here and not in LogEventBuilder constructors as right now this is the only
    //  event that initializes args; if in the future others need to initialize them, then it will
    //  be evaluated whether adding args as LogEventBuilder argument and left to it the due of its
    //  initialization.
    this.args = args;
  }
  //endregion Private

  //endregion Constructors

  //region API

  /**
   * See {@link LogEventBuilder#log()}
   *
   * @return the built log event
   */
  public DoneLogEvent<R> log() {
    //noinspection unchecked
    return (DoneLogEvent<R>)super.log();
  }

  //region level

  /**
   * Do nothing as doing inherit level from done and cannot change it.
   *
   * @return {@code this}
   */
  @NonNull
  public DoneLogEventBuilder<R> trace() {
    return this;
  }

  /**
   * Do nothing as doing inherit level from done and cannot change it.
   *
   * @return {@code this}
   */
  @NonNull
  public DoneLogEventBuilder<R> debug() {
    return this;
  }

  /**
   * Do nothing as doing inherit level from done and cannot change it.
   *
   * @return {@code this}
   */
  @NonNull
  public DoneLogEventBuilder<R> info() {
    return this;
  }

  /**
   * Do nothing as doing inherit level from done and cannot change it.
   *
   * @return {@code this}
   */
  @NonNull
  public DoneLogEventBuilder<R> warn() {
    return this;
  }

  /**
   * Do nothing as doing inherit level from done and cannot change it.
   *
   * @return {@code this}
   */
  @NonNull
  public DoneLogEventBuilder<R> error() {
    return this;
  }
  //endregion level

  /**
   * See {@link LogEventBuilder#arg(String, Object)}
   *
   * @return {@code this}
   */
  public @NonNull <T> DoneLogEventBuilder<R> arg(@NonNull final String name,
      @Nullable final T value) {
    //noinspection unchecked
    return (DoneLogEventBuilder<R>)super.arg(name,value);
  }

  /**
   * See {@link LogEventBuilder#arg(Arg)}
   *
   * @return {@code this}
   */
  public @NonNull <T> DoneLogEventBuilder<R> arg(@NonNull final Arg<T> arg) {
    //noinspection unchecked
    return (DoneLogEventBuilder<R>)super.arg(arg);
  }
  //endregion API

  //region Private methods
  protected @NonNull DoneLogEvent<R> build() {
    return new DoneLogEvent<>(log, marker, pattern, params, level, event, args, uuid, uuidOn, result, isVoid);
  }
  //endregion Private methods

}
