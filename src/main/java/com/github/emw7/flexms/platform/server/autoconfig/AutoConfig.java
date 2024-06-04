package com.github.emw7.flexms.platform.server.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.github.emw7.flexms.platform.server.AppConfigProperties;
import com.github.emw7.flexms.platform.server.config.PlatformConfig;
import com.github.emw7.flexms.platform.server.event.ApplicationReadyEventListener;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;

@AutoConfiguration
@Import(PlatformConfig.class)
public class AutoConfig {

  @Bean
  public ApplicationReadyEventListener applicationReadyEventListener (@NonNull final AppConfigProperties appConfigProperties) {
    return new ApplicationReadyEventListener(appConfigProperties);
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaTimeModule javaTimeModule = new JavaTimeModule();

    // Optionally, customize the DateTimeFormatter for ZonedDateTime
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(formatter));
    javaTimeModule.addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);

    objectMapper.registerModule(javaTimeModule);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return objectMapper;
  }

}
