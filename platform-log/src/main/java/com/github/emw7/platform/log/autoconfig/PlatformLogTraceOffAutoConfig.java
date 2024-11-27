package com.github.emw7.platform.log.autoconfig;

import com.github.emw7.platform.log.tracing.LogTracingTracer;
import com.github.emw7.platform.log.tracing.LogTracingMDCFacade;
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
@ConditionalOnProperty(name = "com.github.emw7.platform.log.trace-enabled", havingValue = "false")
public class PlatformLogTraceOffAutoConfig {

  /**
   * Returns the default fallback MDCWrapper that does nothing, disabling tracing actually.
   * <p>
   * Application can either use Spring Observability / Tracing (see TODO:examples) or define its
   * own (see TODO:examples) that does something more useful to enable basic tracing.
   * information.
   *
   * @return the default fallback MDCWrapper
   */
  @Bean
  public LogTracingMDCFacade logTracingMDCFacade ()
  {
    return new LogTracingMDCFacade() {
      @Override
      public void traceId(@NonNull final String id) {
        // do nothing!
      }

      @Override
      public void spanId(@NonNull final String id) {
        // do nothing!
      }

      @Override
      public boolean containsTraceId() {
        return false;
      }

      @Override
      public boolean containsSpanId() {
        return false;
      }
    };
  }

}
