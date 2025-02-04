package com.github.emw7.examples.updownstream.downstream.logic.autoconfig;

import com.github.emw7.examples.updownstream.downstream.logic.DefaultSensorService;
import com.github.emw7.examples.updownstream.downstream.logic.SensorService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class DownstreamLogicAutoConfig {

  @ConditionalOnMissingBean
  @Bean
  public SensorService sensorService () {
    return new DefaultSensorService();
  }
}
