package com.github.emw7.examples.updownstream.downstream.service.rest.config;

import com.github.emw7.platform.i18n.CompositeMessageSource;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  /**
   * See {@code extra/doc/Internationalization/README.md}.
   */
  @Bean
  public MessageSource messageSource(
      Map<String, MessageSource> messageSources) {
    return new CompositeMessageSource(messageSources);
  }

}
