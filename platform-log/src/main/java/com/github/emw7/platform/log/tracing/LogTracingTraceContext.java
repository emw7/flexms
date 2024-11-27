package com.github.emw7.platform.log.tracing;

import io.micrometer.tracing.TraceContext;
import org.springframework.lang.NonNull;

/**
 * A basic {@link TraceContext} implementation.
 */
public final class LogTracingTraceContext implements TraceContext {

  //region Private final properties
  private final String traceId;
  private final String spanId;
  //endregion Private final properties

  //region Constructors
  LogTracingTraceContext(@NonNull final String traceId, @NonNull final String spanId) {
    this.traceId = traceId;
    this.spanId = spanId;
  }
  //endregion Constructors

  //region API
  /**
   * @return ""
   */
  @Override
  public @NonNull String parentId() {
    return "";
  }

  @Override
  public @NonNull String traceId() {
    return this.traceId;
  }

  @Override
  public @NonNull String spanId() {
    return this.spanId;
  }

  /**
   * @return {@code false}
   */
  @Override
  public @NonNull Boolean sampled() {
    return false;
  }
  //endregion API

}
