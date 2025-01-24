package com.github.emw7.examples.serviceruntime.logic;

import com.github.emw7.examples.serviceruntime.logic.error.client.MalformedInputBadRequestClientException;
import com.github.emw7.examples.serviceruntime.logic.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.serviceruntime.logic.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.serviceruntime.logic.error.client.TooManyRequestsClientException;
import com.github.emw7.examples.serviceruntime.logic.error.client.locked.NonRenewableLockedClientException;
import com.github.emw7.examples.serviceruntime.logic.error.server.ResourcesExhaustedServerRequestErrorException;
import com.github.emw7.examples.serviceruntime.logic.error.server.StorageNotAvailabledServerRequestErrorException;
import com.github.emw7.examples.serviceruntime.model.Sensor;
import io.micrometer.observation.annotation.Observed;
import org.springframework.lang.NonNull;

/**
 * The service implementation: it is API technology-agnostic.
 */
public interface SensorService {

  //region Create
  record CreateSensorRequest (@NonNull Sensor sensor) {}

  record CreateSensorResponse (@NonNull Sensor sensor) {}

  @NonNull CreateSensorResponse create(@NonNull CreateSensorRequest request) throws
      SensorAlreadyExistsClientException, MalformedInputBadRequestClientException, ResourcesExhaustedServerRequestErrorException, TooManyRequestsClientException;
  //endregion Create

  //region Delete
  record DeleteSensorRequest (@NonNull String code) {}

  record DeleteSensorResponse () {}

  @Observed(name = "ServiceSensor#delete")
  @NonNull DeleteSensorResponse delete(@NonNull DeleteSensorRequest request) throws
      SensorNotFoundClientException, NonRenewableLockedClientException, StorageNotAvailabledServerRequestErrorException, TooManyRequestsClientException;
  //endregion Delete

}
