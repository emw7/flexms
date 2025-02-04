package com.github.emw7.platform.service.runtime.rest.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.service.runtime.rest.aop.RestControllerAspect;
import com.github.emw7.platform.service.runtime.rest.error.ClientExceptionHandler;
import com.github.emw7.platform.service.runtime.rest.error.ServerExceptionHandler;
import com.github.emw7.platform.service.runtime.rest.request.RequestCallerLocaleResolver;
import com.github.emw7.platform.service.runtime.rest.request.context.HttpServletRequestHeaderCallerRetriever;
import com.github.emw7.platform.service.runtime.rest.request.context.RestRequestContextRetriever;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.service.runtime.rest.request.context.DefaultHttpServletHeadersRequestContextRetriever;
import com.github.emw7.platform.service.runtime.rest.request.context.HttpServletRequestHeaderOriginatorRetriever;
import com.github.emw7.platform.service.runtime.rest.request.context.RestCallerRetriever;
import com.github.emw7.platform.service.runtime.rest.request.context.RestOriginatorRetriever;
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
public class PlatformServiceRuntimeRestAutoConfig {

  @Bean
  public RestControllerAspect restControllerAspect(
      @NonNull final RestRequestContextRetriever restRequestContextRetriever) {
    return new RestControllerAspect(restRequestContextRetriever);
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
  public RestRequestContextRetriever restRequestContextRetriever(@Nullable final RestOriginatorRetriever originatorRetriever,
      @Nullable final RestCallerRetriever callerRetriever) {
    return new DefaultHttpServletHeadersRequestContextRetriever(originatorRetriever, callerRetriever);
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

  /**
   * Returns a locale resolver that first searches for CALLER (YES caller!) lang header and then delegates
   * to standard locale resolver flow.
   *
   * @param requestCallerConfigProperties the set of properties in which is specified the name
   *                                      of the caller lang header
   *
   * @return an instance of {@link RequestCallerLocaleResolver}
   *
   * @see RequestCallerLocaleResolver
   */
  @Bean
  public LocaleResolver localeResolver (RequestCallerConfigProperties requestCallerConfigProperties){
    return new RequestCallerLocaleResolver(requestCallerConfigProperties);
  }

}
