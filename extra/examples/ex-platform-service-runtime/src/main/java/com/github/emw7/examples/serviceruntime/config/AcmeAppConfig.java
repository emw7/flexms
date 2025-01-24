package com.github.emw7.examples.serviceruntime.config;

import com.github.emw7.platform.i18n.CompositeMessageSource;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AcmeAppConfig {

  @Bean
  public MessageSource messageSource(
      Map<String, MessageSource> messageSources) {
    return new CompositeMessageSource(messageSources);
  }

}
