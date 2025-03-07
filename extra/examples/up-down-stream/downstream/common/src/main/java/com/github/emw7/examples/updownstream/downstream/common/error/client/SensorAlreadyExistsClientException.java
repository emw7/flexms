package com.github.emw7.examples.updownstream.downstream.common.error.client;

import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.AlreadyExistsClientException;
import org.springframework.lang.NonNull;

public final class SensorAlreadyExistsClientException extends AlreadyExistsClientException {

  public SensorAlreadyExistsClientException(@NonNull final String sensorCode) {
    super(new Id("0"), Sensor.RESOURCE_NAME, sensorCode, null);
  }

}
