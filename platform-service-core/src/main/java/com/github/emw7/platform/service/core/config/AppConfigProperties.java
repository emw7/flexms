package com.github.emw7.platform.service.core.config;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

// TODO how to manage name to be fixed to spring.application.name: forse @ConfigurationProperties non è la soluzione giusta
@ConfigurationProperties(prefix = "app")
public record AppConfigProperties(@NonNull String name, @Nullable String instanceId) {

  public AppConfigProperties {
    instanceId= (StringUtils.isEmpty(instanceId) ) ? UUID.randomUUID().toString() : instanceId;
  }
}
