package com.github.emw7.flexms.platform.rest.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.flexms.platform.telemetry.Tracing;
import com.github.emw7.flexms.platform.rest.ClientExceptionHandler;
import java.util.UUID;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@AutoConfiguration
public class PlatformRestAutoConfig {

  // a useless tracing.
  @ConditionalOnMissingBean
  @Bean
  public Tracing tracing() {
    return new Tracing() {
      @Override
      public String traceId() {
        return UUID.randomUUID().toString();
      }

      @Override
      public String spanId() {
        return UUID.randomUUID().toString();
      }
    };
  }

}
