package com.github.emw7.platform.log.tracing;

import io.micrometer.common.lang.Nullable;
import io.micrometer.tracing.Baggage;
import io.micrometer.tracing.BaggageInScope;
import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Span.Builder;
import io.micrometer.tracing.SpanCustomizer;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import java.util.Map;
import org.springframework.lang.NonNull;

/**
 * A basic {@link Tracer} that actually does very few:
 * <ul>
 * <li>delegates to provided {@link LogTracingMDCFacade} the storage of {@code traceId} and
 *     {@code spanId} in {@link #nextSpan()} and {@link #nextSpan(Span)}</li>
 * <li>all methods (included the ones at the previous point) eventually delegate to
 *    {@link Tracer#NOOP}</li>
 * </ul>
 */
public class LogTracingTracer implements Tracer {

  private final static Tracer NOOP= Tracer.NOOP;

  private final LogTracingMDCFacade mdcWrapper;

  public LogTracingTracer(@NonNull final LogTracingMDCFacade mdcWrapper) {
    this.mdcWrapper= mdcWrapper;
  }

  //region API

  /**
   * Does:
   * <ol>
   * <li>if mdcWrapper does not contain {@code traceId} then
   *     {@code mdcWrapper.traceId(span.context().traceId())}</li>
   * <li>if mdcWrapper does not contain {@code spanId} then
   *     {@code mdcWrapper.spanId(span.context().spanId())}</li>
   * </ol>
   * @return {@code NOOP.nextSpan(span)}
   */
  @Override
  public @NonNull Span nextSpan() {
    if ( !mdcWrapper.containsTraceId()) {
      mdcWrapper.traceId(LogTracingUtil.generateTraceId());
    }
    if ( !mdcWrapper.containsSpanId() ) {
      mdcWrapper.spanId(LogTracingUtil.generateSpanId());
    }
    return NOOP.nextSpan();
  }

  /**
   * If {@code span} is {@code null} calls {@link #nextSpan()}, otherwise does:
   * <ol>
   * <li>if mdcWrapper does not contain {@code traceId} then
   *     {@code mdcWrapper.traceId(span.context().traceId())}</li>
   * <li>if mdcWrapper does not contain {@code spanId} then
   *     {@code mdcWrapper.spanId(span.context().spanId())}</li>
   * </ol>
   * @param span span from which get {@code traceId} and {@code spanId}.
   * @return {@code NOOP.nextSpan(span)}
   */
  @Override
  public @NonNull Span nextSpan(final Span span) {
    if ( span == null ) {
      return nextSpan();
    }
    else {
      mdcWrapper.traceId(span.context().traceId());
      mdcWrapper.spanId(span.context().spanId());
    }
    return NOOP.nextSpan(span);
  }

  //region Plain delegation
  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull SpanInScope withSpan(final Span span) {
    return NOOP.withSpan(span);
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull ScopedSpan startScopedSpan(final String s) {
    return NOOP.startScopedSpan(s);
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull Builder spanBuilder() {
    return NOOP.spanBuilder();
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull TraceContext.Builder traceContextBuilder() {
    return NOOP.traceContextBuilder();
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull CurrentTraceContext currentTraceContext() {
    return NOOP.currentTraceContext();
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  @Nullable
  public @NonNull SpanCustomizer currentSpanCustomizer() {
    return NOOP.currentSpanCustomizer();
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  @Nullable
  public @NonNull Span currentSpan() {
    return NOOP.currentSpan();
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull Map<String, String> getAllBaggage() {
    return NOOP.getAllBaggage();
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull Map<String, String> getAllBaggage(final TraceContext traceContext) {
    return NOOP.getAllBaggage(traceContext);
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull Baggage getBaggage(final String s) {
    return NOOP.getBaggage(s);
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  @Nullable
  public @NonNull Baggage getBaggage(final TraceContext traceContext, final String s) {
    return NOOP.getBaggage(traceContext, s);
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  @Deprecated
  public @NonNull Baggage createBaggage(final String s) {
    return NOOP.createBaggage(s);
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  @Deprecated
  public @NonNull Baggage createBaggage(final String s, final String s1) {
    return NOOP.createBaggage(s, s1);
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull BaggageInScope createBaggageInScope(final String name, final String value) {
    return NOOP.createBaggageInScope(name, value);
  }

  /**
   * @return the result of delegation to {@code NOOP}
   *
   * @see Tracer#NOOP
   */
  @Override
  public @NonNull BaggageInScope createBaggageInScope(@NonNull final TraceContext traceContext,
      @NonNull final String name, @NonNull final String value) {
    return NOOP.createBaggageInScope(traceContext, name, value);
  }
  //endregion Plain delegation

  //endregion API

}
