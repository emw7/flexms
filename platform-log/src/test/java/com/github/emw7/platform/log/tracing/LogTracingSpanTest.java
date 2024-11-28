package com.github.emw7.platform.log.tracing;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LogTracingSpanTest {

  @Test
  void isNoop() {
    final LogTracingSpan sut = createSut();
    final boolean actual = sut.isNoop();
    Assertions.assertThat(actual).isFalse();
  }

  @Test
  void context() {
    final String expectedTraceId = LogTracingUtil.generateTraceId();
    final String expectedSpanId = LogTracingUtil.generateSpanId();

    final LogTracingSpan sut =
        new LogTracingSpan(expectedTraceId, expectedSpanId);

    final TraceContext actual = sut.context();

    final String actualTraceId = actual.traceId();
    final String actualSpanId = actual.spanId();

    Assertions.assertThat(actualTraceId).isSameAs(expectedTraceId);
    Assertions.assertThat(actualSpanId).isSameAs(expectedSpanId);
  }

  @Test
  void start() {
    final LogTracingSpan sut = createSut();
    final Span actual = sut.start();
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
  }

  @Test
  void name() {
    final LogTracingSpan sut = createSut();
    final Span actual = sut.name("");
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
  }

  @Test
  void event() {
    final LogTracingSpan sut = createSut();
    final Span actual = sut.event("");
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
  }

  @Test
  void tag() {
    final LogTracingSpan sut = createSut();
    final Span actual = sut.tag("", "");
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
  }


  @Test
  void error() {
    final LogTracingSpan sut = createSut();
    final Span actual = sut.error(new Exception());
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
  }

  @Test
  void end() {
    // void... non need to test
  }


  @Test
  void abandon() {
    // void... non need to test
  }

  @Test
  void remoteServiceName() {
    final LogTracingSpan sut = createSut();
    final Span actual = sut.remoteServiceName("");
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
  }

  @Test
  void remoteIpAndPort() {
    final LogTracingSpan sut = createSut();
    final Span actual = sut.remoteIpAndPort("", 0);
    Assertions.assertThat(actual).isSameAs(Span.NOOP);
  }

  //region Private methods
  private LogTracingSpan createSut() {
    final String expectedTraceId = LogTracingUtil.generateTraceId();
    final String expectedSpanId = LogTracingUtil.generateSpanId();

    return new LogTracingSpan(expectedTraceId, expectedSpanId);
  }
  //endregion Private methods
}