package com.github.emw7.platform.error.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@AutoConfiguration
public class PlatformErrorAutoConfig {

  @Bean
  public MessageSource platformErrorMessageSource () {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("com_github_emw7_platform_error_messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
