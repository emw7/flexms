package com.github.emw7.platform.i18n;

import java.util.Map;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@TestConfiguration
public class CustomAppMessageSourceTestConfig {

  @Bean
  public MessageSource appMessageSource () {
    ResourceBundleMessageSource messageSource= new ResourceBundleMessageSource();
    messageSource.setBasename("app_messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

}
