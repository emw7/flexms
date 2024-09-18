package com.github.emw7.platform.i18n;

import java.util.Map;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CompositeMessageSourceTestConfig {

  @Bean
  public MessageSource messageSource(Map<String, MessageSource> messageSources) {
    return new CompositeMessageSource(messageSources);
  }

}
