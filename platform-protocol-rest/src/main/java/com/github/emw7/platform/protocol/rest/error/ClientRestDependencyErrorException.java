package com.github.emw7.platform.protocol.rest.error;

import com.github.emw7.platform.protocol.api.error.ClientDependencyErrorException;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

/**
 * An "empty" class creating a layer between protocol agnostic dependency errors and rest errors
 */
public final class ClientRestDependencyErrorException extends
    ClientDependencyErrorException {

  public ClientRestDependencyErrorException(
      @NonNull final HttpClientErrorException errorResponse,
      @NonNull final String caller, @NonNull final String serviceName, @NonNull final String serviceVersion) {
    super(errorResponse, caller, serviceName, serviceVersion, errorResponse.getMessage(), errorResponse);
  }

  /**
   * Returns the error responses as specified to the constructor.
   *
   * @return the error response
   */
  @Override
  public @NonNull HttpClientErrorException getErrorResponse() {
    // TODO throw Programming error in case of class cast exception o rin case getErrorresponse is null (?)
    return (HttpClientErrorException)super.getErrorResponse();
  }

}
