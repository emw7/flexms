package com.github.emw7.flexms.platform.server.config;

import com.github.emw7.flexms.platform.server.AppConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppConfigProperties.class)
public class PlatformConfig {

}
