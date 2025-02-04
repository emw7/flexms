package com.github.emw7.examples.updownstream.downstream.logic;

import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreacheableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import io.micrometer.observation.annotation.Observed;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The service implementation: it is API technology-agnostic.
 */
public interface SensorService {

  //region Create
  record CreateSensorRequest(@NonNull Sensor sensor) {

  }

  record CreateSensorResponse(@NonNull Sensor sensor) {

  }

  @Observed(name = "SensorService#create")
  @NonNull
  CreateSensorResponse create(@NonNull CreateSensorRequest request)
      throws SensorAlreadyExistsClientException, SystemErrorServerException;
  //endregion Create

  //region Delete
  record DeleteSensorRequest(@NonNull String code) {

  }

  record DeleteSensorResponse() {

  }

  @Observed(name = "ServiceSensor#delete")
  @NonNull
  DeleteSensorResponse delete(@NonNull DeleteSensorRequest request)
      throws SensorNotFoundClientException, SystemErrorServerException;
  //endregion Delete

  //region Read
  record ReadSensorRequest(@NonNull String code, boolean respondWithError) {

  }

  record ReadSensorResponse(@NonNull String code, @Nullable Object value, int err) {

  }

  @NonNull
  ReadSensorResponse read(@NonNull final ReadSensorRequest request)
      throws SensorNotFoundClientException, SensorUnreacheableServerException;
  //endregion Read

}
