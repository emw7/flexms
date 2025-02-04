package com.github.emw7.platform.service.core.runtime.config;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Collects application information defined in system properties (or related environment variables)
 * prefixed with `app`.
 */
@ConfigurationProperties(prefix = "app")
public record AppConfigProperties(@NonNull String name, @Nullable String instanceId) {

  public AppConfigProperties {
    instanceId= (StringUtils.isEmpty(instanceId) ) ? UUID.randomUUID().toString() : instanceId;
  }
}
