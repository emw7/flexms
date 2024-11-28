package com.github.emw7.platform.log.tracing;

import static org.junit.jupiter.api.Assertions.*;

import net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step.ForDelegation.OffsetMapping.ForFieldHandle.Access;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

class LogTracingTraceContextTest {

  @Test
  void parentId() {
    final LogTracingTraceContext sut=
        new LogTracingTraceContext(LogTracingUtil.generateTraceId(), LogTracingUtil.generateSpanId());
    final String actual= sut.parentId();
    Assertions.assertThat(actual).isEmpty();
  }

  @Test
  void traceId() {
    final String expectedTraceId= LogTracingUtil.generateTraceId();
    final String expectedSpanId= LogTracingUtil.generateSpanId();
    final LogTracingTraceContext sut=
        new LogTracingTraceContext(expectedTraceId, expectedSpanId);
    final String actualTraceId= sut.traceId();
    final String actualSpanId= sut.spanId();

    Assertions.assertThat(actualTraceId).as("trace-id").isSameAs(expectedTraceId);
    Assertions.assertThat(actualSpanId).as("trace-id").isSameAs(expectedSpanId);
  }

  @Test
  void spanId() {
  }

  @Test
  void sampled() {
    final LogTracingTraceContext sut=
        new LogTracingTraceContext(LogTracingUtil.generateTraceId(), LogTracingUtil.generateSpanId());
    final boolean actual= sut.sampled();
    Assertions.assertThat(actual).isFalse();
  }
}