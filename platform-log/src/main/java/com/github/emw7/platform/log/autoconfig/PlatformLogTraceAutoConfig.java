package com.github.emw7.platform.log.autoconfig;

import com.github.emw7.platform.log.tracing.LogTracingMDCFacade;
import com.github.emw7.platform.log.tracing.LogTracingTracer;
import com.github.emw7.platform.log.tracing.TracerContainer;
import io.micrometer.tracing.Tracer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@AutoConfiguration
@AutoConfigureOrder(1) // value 1 found with trial and error.
public class PlatformLogTraceAutoConfig {

  @ConditionalOnMissingBean(Tracer.class)
  @Bean
  public LogTracingTracer logTracingTracer (@NonNull final LogTracingMDCFacade mdcWrapper) {
    return new LogTracingTracer(mdcWrapper);
  }

  @ConditionalOnBean(Tracer.class)
  @Bean
  public TracerContainer tracingContainer (@NonNull final Tracer tracer) {
    //noinspection InstantiationOfUtilityClass
    return new TracerContainer(tracer);
  }

}
