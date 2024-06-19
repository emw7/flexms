package com.github.emw7.platform.rest.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.rest.aop.RestControllerAspect;
import com.github.emw7.platform.rest.error.ClientExceptionHandler;
import com.github.emw7.platform.rest.error.ServerExceptionHandler;
import com.github.emw7.platform.telemetry.tracing.Tracing;
import com.github.emw7.platform.telemetry.tracing.TracingFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.web.context.annotation.RequestScope;

@AutoConfiguration
public class PlatformRestAutoConfig {

  @Bean
  public RestControllerAspect restControllerAspect(@NonNull final TracingFactory tracingFactory) {
    return new RestControllerAspect(tracingFactory);
  }

  @Bean
  public ClientExceptionHandler clientExceptionHandler(@NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    return new ClientExceptionHandler(objectMapper, translator);
  }

  @Bean
  public ServerExceptionHandler serverExceptionHandler(@NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    return new ServerExceptionHandler(objectMapper, translator);
  }

}
