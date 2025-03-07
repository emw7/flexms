package com.github.emw7.examples.updownstream.upstream.api.controller;

import com.github.emw7.examples.updownstream.upstream.common.error.client.XAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.client.XNotFoundClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XUnreachableServerException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XSystemErrorServerException;
import org.springframework.lang.NonNull;

/**
 * The controller interface that defines the API request and response payloads and the available
 * endpoints. It is technology-agnostic.
 * <p>
 * The response classes bring both correct answer and error answer. This is a design solution of
 * this example. In the README.md of the project there is an alternative implementation.
 */
public interface XController {

  //region Create

  @NonNull
  XApi.Create.Response create(@NonNull final XApi.Create.Request request)
      throws XAlreadyExistsClientException, XSystemErrorServerException;
  //endregion Create

  //region Delete
  @NonNull
  XApi.Delete.Response delete(@NonNull final XApi.Delete.Request request)
      throws XNotFoundClientException, XSystemErrorServerException;
  //endregion Delete

  //region Read
  @NonNull
  XApi.Read.Response read(@NonNull final XApi.Read.Response request)
      throws XNotFoundClientException, XUnreachableServerException;
  //endregion Read

  //region Write
  //endregion Write
}
