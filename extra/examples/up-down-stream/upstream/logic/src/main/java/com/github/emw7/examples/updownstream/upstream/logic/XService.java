package com.github.emw7.examples.updownstream.upstream.logic;

import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.examples.updownstream.upstream.common.error.client.XAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.client.XNotFoundClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XSystemErrorServerException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XUnreachableServerException;
import io.micrometer.observation.annotation.Observed;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The service implementation: it is API technology-agnostic.
 */
public interface XService {

  //region Create
  record XCreateRequest(@NonNull Sensor sensor) {

  }

  record XCreateResponse(@NonNull Sensor sensor) {

  }

  @Observed(name = "XService#create")
  @NonNull
  XCreateResponse create(@NonNull XService.XCreateRequest request)
      throws XAlreadyExistsClientException, XSystemErrorServerException;
  //endregion Create

  //region Delete
  record XDeleteRequest(@NonNull String code) {

  }

  record XDeleteResponse() {

  }

  @Observed(name = "XService#delete")
  @NonNull
  XDeleteResponse delete(@NonNull XDeleteRequest request)
      throws XNotFoundClientException, XSystemErrorServerException;
  //endregion Delete

  //region Read
  record XReadRequest(@NonNull String code, boolean respondWithError) {

  }

  record XReadResponse(@NonNull String code, @Nullable Object value, int err) {

  }

  @Observed(name = "XService#read")
  @NonNull
  XReadResponse read(@NonNull final XReadRequest request)
      throws XNotFoundClientException, XUnreachableServerException, XSystemErrorServerException;
  //endregion Read

}
