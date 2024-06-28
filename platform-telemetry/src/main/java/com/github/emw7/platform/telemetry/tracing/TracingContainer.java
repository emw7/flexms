package com.github.emw7.platform.telemetry.tracing;

import com.github.emw7.platform.log.EventLogger;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class TracingContainer {

  //region Private static properties
  private static final Logger log = LoggerFactory.getLogger(TracingContainer.class);

  private static final ThreadLocal<Tracing> tracingContainer = new InheritableThreadLocal<>();
  //endregion Private static properties

  //region Public static methods

  /**
   * Sets the {@link Tracing} to be wrapped.
   * <p>
   * Should not be {@code null}, but if {@code null} is provided it is accepted and a warn log is
   * emitted.
   *
   * @param tracing the {@link Tracing} to be wrapped
   */
  public static void set(@NonNull final Tracing tracing) {
    // TODO integrate with eventing service.
    if ( tracing == null ) {
      EventLogger.notice(log, Level.WARN, "[ILLEGAL-ARGUMENT] provided tracing is not valid: must be not null");
    }
    tracingContainer.set(tracing);
  }

  /**
   * Returns the wrapped {@link Tracing}.
   * <p>
   * Returns {@code null} if {@link #set(Tracing)} has not yes called.<br/> Returns {@code null}
   * event if {@link #set(Tracing)} has been called with {@code null} parameter but that is another
   * story.
   *
   * @return the wrapped {@link Tracing}
   */
  public static @Nullable Tracing get() {
    final Tracing tracing = tracingContainer.get();
    if (tracing == null) {
      log.warn("returning null tracing: either set not called or called with null parameter");
    }
    return tracingContainer.get();
  }

  public static @Nullable String traceId() {
    final Tracing tracing = tracingContainer.get();
    if (tracing == null) {
      log.warn(MarkerFactory.getMarker("ciao"),
          "returning null traceId: either set not called or called with null parameter");
    }
    return Optional.ofNullable(get()).map(Tracing::traceId).orElse(null);
  }

  public static @Nullable String spanId() {
    final Tracing tracing = tracingContainer.get();
    if (tracing == null) {
      log.warn("returning null spanId: either set not called or called with null parameter");
    }
    return Optional.ofNullable(get()).map(Tracing::spanId).orElse(null);
  }
  //endregion Public static methods

  //region Constructors
  // prevents instantiation.
  private TracingContainer() {
  }
  //endregion Constructors

}
