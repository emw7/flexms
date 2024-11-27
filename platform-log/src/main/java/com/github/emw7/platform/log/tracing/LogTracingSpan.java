package com.github.emw7.platform.log.tracing;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import java.util.concurrent.TimeUnit;
import org.springframework.lang.NonNull;

/**
 * A very basic {@link Span} implementation that does very few:
 * <ul>
 * <li>manages a {@link LogTracingTraceContext} to store {@code traceId} and {@code spanId}</li>
 * <li>all methods (but {@link #isNoop()} and {@link #context()}) delegate to
 *    {@link io.micrometer.tracing.Tracer#NOOP}</li>
 * </ul>
 */
public final class LogTracingSpan implements Span {

  //region Private static final properties
  private static final Span NOOP= Span.NOOP;
  //endregion Private static final properties

  //region Private final properties
  private final TraceContext context;
  //endregion Private final properties

  //region Constructors
  public LogTracingSpan() {
    this(LogTracingUtil.generateTraceId());
  }

  public LogTracingSpan(@NonNull final String traceId) {
    this(traceId, LogTracingUtil.generateSpanId());
  }

  public LogTracingSpan(@NonNull final String traceId, @NonNull final String spanId) {
    this.context= new LogTracingTraceContext(traceId, spanId);
  }
  //endregion Constructors

  //region API
  /**
   * @return {@code false}
   */
  @Override
  public  boolean isNoop() {
    return false;
  }

  /**
   * @return the span context
   */
  @Override
  public @NonNull TraceContext context() {
    return context;
  }

  //region Plain delegation
  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span start() {
    return NOOP.start();
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span name(final String s) {
    return NOOP.name(s);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span event(final String s) {
    return NOOP.event(s);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span event(final String s, final long l, final TimeUnit timeUnit) {
    return NOOP.event(s, l, timeUnit);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span tag(final String s, final String s1) {
    return NOOP.tag(s, s1);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span tag(final String key, final long value) {
    return NOOP.tag(key, value);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span tag(final String key, final double value) {
    return NOOP.tag(key, value);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span tag(final String key, final boolean value) {
    return NOOP.tag(key, value);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span error(final Throwable throwable) {
    return NOOP.error(throwable);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull void end() {
    NOOP.end();
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull void end(final long l, final TimeUnit timeUnit) {
    NOOP.end(l, timeUnit);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull void abandon() {
    NOOP.abandon();
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span remoteServiceName(final String s) {
    return NOOP.remoteServiceName(s);
  }

  /**
   * @return the result of delegation to {@link Span#NOOP}
   */
  @Override
  public @NonNull Span remoteIpAndPort(final String s, final int i) {
    return NOOP.remoteIpAndPort(s, i);
  }
  //endregion Plain delegation

  //endregion API

}
