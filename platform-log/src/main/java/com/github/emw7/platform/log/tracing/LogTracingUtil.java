package com.github.emw7.platform.log.tracing;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import java.util.Objects;
import java.util.UUID;
import org.springframework.lang.NonNull;

public final class LogTracingUtil {

  //region API static

  /**
   * Generates a random 16 byte long {@code traceId} returning its hexadecimal string
   * representation.
   * <p>
   * <i>Internals</i>
   * <pre>
   * The code is the following:
   * final UUID traceIdUuid = UUID.randomUUID();
   * return Long.toHexString(traceIdUuid.getMostSignificantBits()) +
   *        Long.toHexString(traceIdUuid.getLeastSignificantBits());
   * </pre>
   *
   * @return the hexadecimal string representation of the generated {@code traceId}
   */
  public static String generateTraceId() {
    final UUID traceIdUuid = UUID.randomUUID();
    return Long.toHexString(traceIdUuid.getMostSignificantBits()) +
           Long.toHexString(traceIdUuid.getLeastSignificantBits());
  }

  /**
   * Generates a random 8 byte long {@code spanId} returning its hexadecimal string
   * representation.
   * <p>
   * <i>Internals</i>
   * <pre>
   * The code is the following:
   * final UUID spanIdUuid = UUID.randomUUID();
   * return Long.toHexString(spanIdUuid.getLeastSignificantBits());
   * </pre>
   *
   * @return the hexadecimal string representation of the generated {@code spanId}
   */
  public static String generateSpanId() {
    final UUID spanIdUuid = UUID.randomUUID();
    return Long.toHexString(spanIdUuid.getLeastSignificantBits());
  }

  /**
   * Returns the {@code traceId} retrieved by the current span of the provided tracer.
   * <p>
   * If the current span is {@code null} then fallback to {@link Span#NOOP}.
   *
   * @param tracer the tracer from with retrieved the {@code traceId}
   *
   * @return the {@code traceId} retrieved by the current span of the provided tracer
   */
  public static @NonNull String traceId(@NonNull final Tracer tracer) {
    final TraceContext tracingContext= Objects.requireNonNullElse(tracer.currentSpan(), Span.NOOP).context();
    return tracingContext.traceId();
  }

  /**
   * Returns the {@code spanId} retrieved by the current span of the provided tracer.
   * <p>
   * If the current span is {@code null} then fallback to {@link Span#NOOP}.
   *
   * @param tracer the tracer from with retrieved the {@code spanId}
   *
   * @return the {@code spanId} retrieved by the current span of the provided tracer
   */
  public static @NonNull String spanId(@NonNull final Tracer tracer) {
    final TraceContext tracingContext= Objects.requireNonNullElse(tracer.currentSpan(), Span.NOOP).context();
    return tracingContext.spanId();
  }

  //endregion API static

  //region Constructors

  // prevents instantiation.
  private LogTracingUtil() {}

  //endregion Constructors
}
