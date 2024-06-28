package com.github.emw7.platform.app.rest.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.app.rest.PlatformRestConstants;
import com.github.emw7.platform.app.rest.aop.RestControllerAspect;
import com.github.emw7.platform.app.rest.error.ClientExceptionHandler;
import com.github.emw7.platform.app.rest.error.ServerExceptionHandler;
import com.github.emw7.platform.app.rest.request.RequestCallerLocaleResolver;
import com.github.emw7.platform.app.rest.request.context.HttpServletRequestHeaderCallerRetriever;
import com.github.emw7.platform.app.rest.request.context.HttpServletRequestHeaderTracingTraceRetriever;
import com.github.emw7.platform.app.rest.request.context.RestRequestContextRetriever;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.app.rest.request.context.DefaultHttpServletHeadersRequestContextRetriever;
import com.github.emw7.platform.app.rest.request.context.HttpServletRequestHeaderOriginatorRetriever;
import com.github.emw7.platform.app.rest.request.context.RestCallerRetriever;
import com.github.emw7.platform.app.rest.request.context.RestOriginatorRetriever;
import com.github.emw7.platform.app.rest.request.context.RestTracingTraceRetriever;
import com.github.emw7.platform.telemetry.tracing.TracingFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.LocaleResolver;

@AutoConfiguration
// https://github.com/spring-projects/spring-boot/issues/24209#issuecomment-2053566339
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 9)
@EnableConfigurationProperties({RequestOriginatorConfigProperties.class, RequestCallerConfigProperties.class})
public class PlatformRestAutoConfig {

  @Bean
  public RestControllerAspect restControllerAspect(@NonNull final TracingFactory tracingFactory/*,
      @NonNull final TracingTraceRetriever tracingTraceRetriever*/,
      @NonNull final RestRequestContextRetriever restRequestContextRetriever) {
    return new RestControllerAspect(tracingFactory/*, tracingTraceRetriever*/,
        restRequestContextRetriever);
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

  @ConditionalOnMissingBean
  @Bean
  public RestRequestContextRetriever restRequestContextRetriever(@Nullable final
      RestTracingTraceRetriever tracingTraceRetriever, @Nullable final RestOriginatorRetriever originatorRetriever,
      @Nullable final RestCallerRetriever callerRetriever) {
    return new DefaultHttpServletHeadersRequestContextRetriever(tracingTraceRetriever, originatorRetriever, callerRetriever);
  }

  @ConditionalOnMissingBean
  @Bean
  public RestTracingTraceRetriever restTracingTraceRetriever (@Value("${com.github.emw7.platform.app.rest.trace-id-header-name:}") @Nullable final String traceIdHeaderName)
  {
    final String traceIdHeaderNameActual= (StringUtils.isEmpty(traceIdHeaderName)) ? PlatformRestConstants.TRACE_ID_HEADER_NAME: traceIdHeaderName;
    return new HttpServletRequestHeaderTracingTraceRetriever(traceIdHeaderNameActual);
  }

  @ConditionalOnMissingBean
  @Bean
  public RestOriginatorRetriever restOriginatorRetriever (@NonNull final RequestOriginatorConfigProperties requestOriginatorConfigProperties)
  {
    return new HttpServletRequestHeaderOriginatorRetriever(requestOriginatorConfigProperties);
  }

  @ConditionalOnMissingBean
  @Bean
  public RestCallerRetriever restCallerRetriever (@NonNull final RequestCallerConfigProperties requestCallerConfigProperties)
  {
    return new HttpServletRequestHeaderCallerRetriever(requestCallerConfigProperties);
  }

  @Bean
  LocaleResolver localeResolver (RequestCallerConfigProperties requestCallerConfigProperties){
    return new RequestCallerLocaleResolver(requestCallerConfigProperties);
  }

}
