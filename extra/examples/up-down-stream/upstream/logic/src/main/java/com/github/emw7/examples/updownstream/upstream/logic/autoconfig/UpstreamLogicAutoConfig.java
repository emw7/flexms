package com.github.emw7.examples.updownstream.upstream.logic.autoconfig;

import com.github.emw7.examples.updownstream.downstream.client.common.SensorClient;
import com.github.emw7.examples.updownstream.upstream.logic.DefaultXService;
import com.github.emw7.examples.updownstream.upstream.logic.XService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@AutoConfiguration
public class UpstreamLogicAutoConfig {

  @ConditionalOnMissingBean
  @Bean
  public XService xService(@NonNull final SensorClient sensorClient) {
    return new DefaultXService(sensorClient);
  }
}
