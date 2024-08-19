package com.github.emw7.platform.auth.api.autoconfig;

import com.github.emw7.platform.auth.api.token.AuthTokenFactory;
import com.github.emw7.platform.auth.api.token.SimpleAuthTokenFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class PlatformAuthApiAutoConfig
{

  /**
   * Returns a default auth token factory if none is provided.
   * @return a default auth token factory if none is provided
   */
  @Bean
  @ConditionalOnMissingBean
  AuthTokenFactory simpleAuthTokenFactory () {
    return new SimpleAuthTokenFactory();
  }

}
