package com.github.emw7.platform.protocol.rest.error;

import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.protocol.api.error.ServerDependencyErrorException;
import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

/**
 * An "empty" class creating a layer between protocol agnostic dependency errors and rest errors
 */
public final class ServerRestDependencyErrorException extends ServerDependencyErrorException {

  public ServerRestDependencyErrorException(
      @NonNull final HttpServerErrorException errorResponse,
      @NonNull final String caller, @NonNull final String serviceName, @NonNull final String serviceVersion) {
    super(errorResponse, caller, serviceName, serviceVersion, errorResponse.getMessage(), errorResponse);
  }

  /**
   * Returns the error responses as specified to the constructor.
   *
   * @return the error response
   */
  @Override
  public @NonNull HttpServerErrorException getErrorResponse() {
    // TODO throw Programming error in case of class cast exception o rin case getErrorresponse is null (?)
    return (HttpServerErrorException)super.getErrorResponse();
  }

}
