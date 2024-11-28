package com.github.emw7.platform.log.tracing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.micrometer.tracing.Baggage;
import io.micrometer.tracing.BaggageInScope;
import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Span.Builder;
import io.micrometer.tracing.SpanCustomizer;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer.SpanInScope;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LogTracingTracerTest {

  @Mock
  private LogTracingMDCFacade logTracingMDCFacade;

  // SUT: System Under Test.
  @InjectMocks
  private LogTracingTracer sut;

  @Test
  void nextSpanFalse() {
    doReturn(false).when(logTracingMDCFacade).containsTraceId();
    doNothing().when(logTracingMDCFacade).traceId(anyString());
    doReturn(false).when(logTracingMDCFacade).containsSpanId();
    doNothing().when(logTracingMDCFacade).spanId(anyString());
    final Span actual= sut.nextSpan();
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
    verify(logTracingMDCFacade, times(1)).containsTraceId();
    verify(logTracingMDCFacade, times(1)).traceId(anyString());
    verify(logTracingMDCFacade, times(1)).containsSpanId();
    verify(logTracingMDCFacade, times(1)).spanId(anyString());
  }

  @Test
  void nextSpanTrue() {
    doReturn(true).when(logTracingMDCFacade).containsTraceId();
    doReturn(true).when(logTracingMDCFacade).containsSpanId();
    final Span actual= sut.nextSpan();
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
    verify(logTracingMDCFacade, times(1)).containsTraceId();
    verify(logTracingMDCFacade, never()).traceId(anyString());
    verify(logTracingMDCFacade, times(1)).containsSpanId();
    verify(logTracingMDCFacade, never()).spanId(anyString());
  }


  @Test
  void withSpan() {
    final SpanInScope actual = sut.withSpan(Span.NOOP);
    Assertions.assertThat(actual).isNotNull();
  }

  @Test
  void startScopedSpan() {
    final ScopedSpan actual = sut.startScopedSpan("");
    Assertions.assertThat(actual).isSameAs(ScopedSpan.NOOP);
  }

  @Test
  void spanBuilder() {
    final Builder actual = sut.spanBuilder();
    Assertions.assertThat(actual).isSameAs(Builder.NOOP);
  }

  @Test
  void traceContextBuilder() {
    final TraceContext.Builder actual = sut.traceContextBuilder();
    Assertions.assertThat(actual).isSameAs(TraceContext.Builder.NOOP);
  }

  @Test
  void currentTraceContext() {
    final CurrentTraceContext actual = sut.currentTraceContext();
    Assertions.assertThat(actual).isSameAs(CurrentTraceContext.NOOP);
  }

  @Test
  void currentSpanCustomizer() {
    final SpanCustomizer actual = sut.currentSpanCustomizer();
    Assertions.assertThat(actual).isSameAs(SpanCustomizer.NOOP);
  }

  @Test
  void currentSpan() {
    final Span actual = sut.currentSpan();
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
  }

  @Test
  void getAllBaggage() {
    final Map<String, String> actual = sut.getAllBaggage();
    Assertions.assertThat(actual).isNullOrEmpty();
  }

  @Test
  void getBaggage() {
    final Baggage actual = sut.getBaggage("");
    Assertions.assertThat(actual).isSameAs(Baggage.NOOP);
  }

  @Test
  void createBaggage() {
    // not tested as method is deprecated.
  }


  @Test
  void createBaggageInScopeA() {
    final BaggageInScope actual = sut.createBaggageInScope("", "");
    Assertions.assertThat(actual).isSameAs(BaggageInScope.NOOP);
  }

  @Test
  void createBaggageInScopeB() {
    final BaggageInScope actual = sut.createBaggageInScope(TraceContext.NOOP, "", "");
    Assertions.assertThat(actual).isSameAs(BaggageInScope.NOOP);
  }

}