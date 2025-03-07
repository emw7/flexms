package com.github.emw7.platform.protocol.rest.error;

import com.github.emw7.platform.protocol.api.error.ClientDependencyErrorException;
import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpClientErrorException;

/**
 * A specialization of ClientDependencyErrorException for REST client error.
 */
public final class RestClientDependencyErrorException extends ClientDependencyErrorException {

  /**
   *
   * @param errorResponse the cause
   * @param caller
   * @param serviceName
   * @param serviceVersion
   */
  public RestClientDependencyErrorException(@NonNull final HttpClientErrorException errorResponse,
      @NonNull final String caller, @NonNull final String serviceName,
      @NonNull final String serviceVersion) {
    super(caller, serviceName, serviceVersion, errorResponse);
  }

  public @NonNull HttpClientErrorException getCause () {
    return (HttpClientErrorException)super.getCause();
  }

}
