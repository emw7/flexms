package com.github.emw7.platform.log;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The log event for the done log events, stressing the conclusion of an operation that has a
 * result that can be void, null or any.
 *
 * @see EventLogger#done(DoingLogEvent)
 * @see EventLogger#done(DoingLogEvent, Object)
 */
public final class DoneLogEvent<R> extends LogEvent {

  //region Private final properties
  private final R result;

  // this is not a characteristic of the event, but is supporting void methods.
  private final boolean isVoid;
  //region Private final properties

  //region Constructors
  DoneLogEvent(@NonNull final Logger log, @NonNull final Marker marker,
      @NonNull final String pattern, @NonNull final Object[] params, @NonNull final Level level,
      @NonNull final String event, @NonNull final Set<Arg<?>> args,
      @NonNull final String uuid, final boolean uuidOn,
      @Nullable final R result, final boolean isVoid) {
    super(log, marker, pattern, params, level, event, args, uuid, uuidOn);

    this.result = result;

    this.isVoid = isVoid;
  }
  //endregion Constructors

  //region Template method: log

  /**
   * The returned args are the following:
   * <ul>
   * <li>{@code result}: the result tied to this log event[*].</li>
   * </ul>
   * <p>
   * [*]: {@code "void"} if #isVoid is {@code true}.
   */
  @Override
  protected @NonNull /*unmodifiable*/ Set<Arg<?>> customKeys() {
    if (this.isVoid) {
      return Set.of(Arg.of("result", "void"));
    } else {
      return Set.of(Arg.of("result", this.result));
    }
  }

  //endregion Template method: log

}
