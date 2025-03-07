package com.github.emw7.examples.updownstream.downstream.api;

import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreachableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import java.util.HashMap;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface SensorApi {

  //region Create
  record CreateSensorRequest(@NonNull Sensor sensor) {

  }

  record CreateSensorResponse(@NonNull Sensor sensor) {

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
      throws SensorNotFoundClientException, SensorUnreachableServerException;

  //endregion Read

}
