package com.github.emw7.examples.updownstream.downstream.service.rest.config;

import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController;
import com.github.emw7.examples.updownstream.downstream.logic.SensorService;
import com.github.emw7.examples.updownstream.downstream.service.rest.api.sensor.SensorControllerImpl;
import com.github.emw7.platform.i18n.CompositeMessageSource;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
public class Config {

  @Bean
  public SensorController sensorController (@NonNull final SensorService sensorService) {
    return new SensorControllerImpl(sensorService);
  }

  /**
   * See {@code extra/doc/Internationalization/README.md}.
   */
  @Bean
  public MessageSource messageSource(
      Map<String, MessageSource> messageSources) {
    return new CompositeMessageSource(messageSources);
  }

}
