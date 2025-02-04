package com.github.emw7.examples.updownstream.downstream.api.controller;

import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreacheableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The controller interface that defines the API request and response payloads and the available
 * endpoints. It is technology-agnostic.
 * <p>
 * The response classes bring both correct answer and error answer. This is a design solution of
 * this example. In the README.md of the project there is an alternative implementation.
 */
public interface SensorController {

  //region Create
  record CreateSensorRequest(@NonNull Sensor sensor) {

  }

  record CreateSensorResponse(@Nullable Sensor sensor) {

  }


  @NonNull
  CreateSensorResponse create(@NonNull final CreateSensorRequest request)
      throws SensorAlreadyExistsClientException, SystemErrorServerException;
  //endregion Create

  //region Delete
  record DeleteSensorRequest(@NonNull String code) {

  }

  record DeleteSensorResponse() {

  }

  @NonNull
  DeleteSensorResponse delete(@NonNull final DeleteSensorRequest request)
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

  //region Write
  //endregion Write
}
