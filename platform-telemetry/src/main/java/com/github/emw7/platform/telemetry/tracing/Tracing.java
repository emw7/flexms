package com.github.emw7.platform.telemetry.tracing;

import org.springframework.lang.NonNull;

/**
 * The tracing interface.
 * <p>
 * Tracing consists of trace-id and span-id.
 */
public interface Tracing {

  @NonNull
  String traceId();

  @NonNull
  Trace trace();

  @NonNull
  String spanId();

  Span addSpan();

}
