package com.github.emw7.platform.protocol.rest.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.protocol.rest.RestProtocolTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@AutoConfiguration
public class PlatformProtocolRestAutoConfig {

  @ConditionalOnProperty(name = "com.github.emw7.platform.protocol.rest.enabled.rest-template", havingValue = "true")
  @Bean
  public RestProtocolTemplate restProtocolTemplate(
      @NonNull final RestTemplateBuilder restTemplateBuilder/*,
      @NonNull final ObjectMapper objectMapper*/) {
    return new RestProtocolTemplate(restTemplateBuilder/*, objectMapper*/);
  }

}
