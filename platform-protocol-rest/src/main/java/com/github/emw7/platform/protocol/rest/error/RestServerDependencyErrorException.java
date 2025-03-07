package com.github.emw7.platform.protocol.rest.error;

import com.github.emw7.platform.protocol.api.error.ServerDependencyErrorException;
import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpServerErrorException;

/**
 * A specialization of ServerDependencyErrorException for REST server error.
 */
public final class RestServerDependencyErrorException extends ServerDependencyErrorException {

  /**
   *
   * @param errorResponse the cause
   * @param caller
   * @param serviceName
   * @param serviceVersion
   */
  public RestServerDependencyErrorException(
      @NonNull final HttpServerErrorException errorResponse,
      @NonNull final String caller, @NonNull final String serviceName,
      @NonNull final String serviceVersion) {
    super(caller, serviceName, serviceVersion, errorResponse);
  }

  public @NonNull HttpServerErrorException getCause () {
    return (HttpServerErrorException)super.getCause();
  }

}
