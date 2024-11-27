package com.github.emw7.platform.log.tracing;

import org.springframework.lang.NonNull;

/**
 * Interface to be implemented by projects that wants to use platform-observability::tracing
 * without using
 * <a href="https://docs.spring.io/spring-boot/reference/actuator/tracing.html">Spring (Boot)
 * Tracing</a>.
 */
public interface LogTracingMDCFacade {

  /**
   * Put the {@code traceId} in the MDC.
   *
   * @param id the {@code traceId} to put in the MDC
   */
  void traceId (@NonNull final String id);

  /**
   * Put the {@code spanId} in the MDC.
   *
   * @param id the {@code spanId} to put in the MDC
   */
  void spanId (@NonNull final String id);

  /**
   * @return whether the {@code traceId} has been set.
   */
  boolean containsTraceId ();

  /**
   * @return whether the {@code traceId} has been set.
   */
  boolean containsSpanId ();

  /**
   * Put a random {@code traceId} in the MDC.
   */
  default void traceId () {
    traceId(LogTracingUtil.generateTraceId());
  }

  /**
   * Put a random {@code spanId} in the MDC.
   */
  default void spanId () {
    spanId(LogTracingUtil.generateSpanId());
  }

}
