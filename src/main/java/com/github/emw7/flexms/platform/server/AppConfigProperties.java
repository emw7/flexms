package com.github.emw7.flexms.platform.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppConfigProperties (String name, String instanceId) {}
