package com.github.emw7.examples.serviceruntime.logic.error.client;

import com.github.emw7.examples.serviceruntime.model.Sensor;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.NotFoundClientException;
import org.springframework.lang.NonNull;

public final class SensorNotFoundClientException extends NotFoundClientException {

  public SensorNotFoundClientException(@NonNull final Id id, @NonNull final String sensorCode) {
    super(id, Sensor.RESOURCE_NAME, sensorCode);
  }

}
