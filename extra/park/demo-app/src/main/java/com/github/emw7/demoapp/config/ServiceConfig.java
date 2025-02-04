package com.github.emw7.demoapp.config;

import com.github.emw7.platform.i18n.CompositeMessageSource;
import jakarta.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ServiceConfig {

//  @Bean
//  public MessageSource appMessageSource ()
//  {
//    ReloadableResourceBundleMessageSource appMessageSource = new ReloadableResourceBundleMessageSource();
//    appMessageSource.setBasename("messages");
//    appMessageSource.setDefaultEncoding("UTF-8");
//    return appMessageSource;
//  }

  @Bean
  public MessageSource messageSource(
      Map<String, MessageSource> messageSources) {
    return new CompositeMessageSource(messageSources);
  }

}
