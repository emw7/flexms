package com.github.emw7.examples.serviceruntime.logic.impl;

import com.github.emw7.examples.serviceruntime.logic.SensorService;
import com.github.emw7.examples.serviceruntime.logic.error.client.MalformedInputBadRequestClientException;
import com.github.emw7.examples.serviceruntime.logic.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.serviceruntime.logic.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.serviceruntime.logic.error.client.TooManyRequestsClientException;
import com.github.emw7.examples.serviceruntime.logic.error.client.locked.NonRenewableLockedClientException;
import com.github.emw7.examples.serviceruntime.logic.error.server.ResourcesExhaustedServerRequestErrorException;
import com.github.emw7.examples.serviceruntime.logic.error.server.StorageNotAvailabledServerRequestErrorException;
import com.github.emw7.examples.serviceruntime.model.Sensor;
import com.github.emw7.platform.error.Id;
import io.micrometer.observation.annotation.Observed;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * The service implementation: it is API technology-agnostic.
 */
@Service
public class SensorServiceImpl implements SensorService {

  @Observed(name = "SensorService#create")
  @Override
  public @NonNull CreateSensorResponse create(@NonNull final CreateSensorRequest request)
      throws SensorAlreadyExistsClientException, MalformedInputBadRequestClientException, ResourcesExhaustedServerRequestErrorException,
      TooManyRequestsClientException {

    final Sensor requestedSensor= request.sensor();

    switch ( requestedSensor.code() ) {
      case "already-exists" :
        throw new SensorAlreadyExistsClientException(new Id("2NT02"), requestedSensor.code());
      case "malformed-input" :
        throw new MalformedInputBadRequestClientException(new Id("E55SK"), requestedSensor.code());
      case "resources-exhausted" :
        throw new ResourcesExhaustedServerRequestErrorException(new Id("MSV2S"), requestedSensor.code());
      case "dos" :
        throw new TooManyRequestsClientException(new Id("DBKD7"));
    }

    return new CreateSensorResponse(requestedSensor);
  }

  @Observed(name = "SensorSerrvice#delete")
  @Override
  public @NonNull DeleteSensorResponse delete(@NonNull final DeleteSensorRequest request)
      throws SensorNotFoundClientException, NonRenewableLockedClientException, StorageNotAvailabledServerRequestErrorException, TooManyRequestsClientException {

    final String sensorCode= request.code();

    switch ( sensorCode ) {
      case "sensor-not-found" :
        throw new SensorNotFoundClientException(new Id("DC0Y7"), sensorCode);
      case "non-renewable-locked" :
        throw new NonRenewableLockedClientException(new Id("MOK23"), "non-renewable-locked", "system", 10, 1);
      case "resources-exhausted" :
        throw new StorageNotAvailabledServerRequestErrorException(new Id("2J3Q5"), sensorCode);
      case "dos" :
        throw new TooManyRequestsClientException(new Id("N1CMG"));
    }

    return new DeleteSensorResponse();
  }
}
