package com.github.emw7.examples.updownstream.upstream.api.controller;

import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreachableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.examples.updownstream.upstream.common.error.client.XAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.client.XNotFoundClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XSystemErrorServerException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XUnreachableServerException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface XApi {

  //region Create
  record CreateXRequest(@NonNull Sensor sensor) {

  }

  record CreateXResponse(@NonNull Sensor sensor) {

  }


  @NonNull
  CreateXResponse create(@NonNull final CreateXRequest request)
      throws XAlreadyExistsClientException, XSystemErrorServerException;

  //endregion Create

  //region Delete
  record DeleteXRequest(@NonNull String code) {

  }

  record DeleteXResponse() {

  }

  @NonNull
  DeleteXResponse delete(@NonNull final DeleteXRequest request)
      throws XNotFoundClientException, XSystemErrorServerException;

  //endregion Delete

  //region Read
  record ReadXRequest(@NonNull String code, boolean respondWithError) {

  }

  record ReadXResponse(@NonNull String code, @Nullable Object value, int err) {

  }

  @NonNull
  ReadXResponse read(@NonNull final ReadXRequest request)
      throws XNotFoundClientException, XUnreachableServerException;

  //endregion Read

}
