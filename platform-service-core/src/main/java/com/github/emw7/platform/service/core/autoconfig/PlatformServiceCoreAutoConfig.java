package com.github.emw7.platform.service.core.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@AutoConfiguration
public class PlatformServiceCoreAutoConfig {

  /**
   * <a href="https://github.com/emw7/flexms/blob/5-transform-flexms-service-to-a-frontend-service/README.md#Internationalisation">README :: Internationalisation</a>
   */
  @Bean
  public MessageSource platformServiceCoreMessageSource () {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("com_github_emw7_platform_service_core_messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
