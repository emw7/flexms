package com.github.emw7.platform.discovery.api.autoconfig;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

public class PlatformDiscoveryApiAutoConfig {

  /**
   * <a href="https://github.com/emw7/flexms/blob/5-transform-flexms-service-to-a-frontend-service/README.md#Internationalisation">README :: Internationalisation</a>
   */
  @Bean
  public MessageSource platformDiscoveryApiMessageSource () {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("com_github_emw7_platform_discovery_api_messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

}
