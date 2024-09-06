package com.github.emw7.platform.service.runtime.autoconfig;

import com.github.emw7.platform.service.core.config.AppConfigProperties;
import com.github.emw7.platform.service.runtime.event.AppReadyEventListener;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@AutoConfiguration
@EnableConfigurationProperties(AppConfigProperties.class)
public class PlatformServiceRuntimeAutoConfig {

  @Bean
  public AppReadyEventListener applicationReadyEventListener(
      @NonNull final AppConfigProperties appConfigProperties) {
    return new AppReadyEventListener(appConfigProperties);
  }

//  @Bean
//  public ObjectMapper objectMapper() {
//    ObjectMapper objectMapper = new ObjectMapper();
//    JavaTimeModule javaTimeModule = new JavaTimeModule();
//
//    // Optionally, customize the DateTimeFormatter for ZonedDateTime
//    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//    javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(formatter));
//    javaTimeModule.addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);
//
//    objectMapper.registerModule(javaTimeModule);
//    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//    return objectMapper;
//  }

}
