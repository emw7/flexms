package com.github.emw7.platform.service.client.api.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@AutoConfiguration
public class PlatformServiceClientApiAutoConfig {

  @Bean
  public MessageSource platformServiceClientApiMessageSource () {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("com_github_emw7_platform_service_client_api_messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
