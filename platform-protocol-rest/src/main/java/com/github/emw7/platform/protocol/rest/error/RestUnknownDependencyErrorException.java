package com.github.emw7.platform.protocol.rest.error;

import com.github.emw7.platform.protocol.api.error.UnknownDependencyErrorException;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClientException;

/**
 * A specialization of UnknownDependencyErrorException for REST unknown error.
 */
public final class RestUnknownDependencyErrorException extends UnknownDependencyErrorException {

  /**
   *
   * @param errorResponse  the cause
   * @param caller
   * @param serviceName
   * @param serviceVersion
   */
  public RestUnknownDependencyErrorException(@NonNull final RestClientException errorResponse,
      @NonNull final String caller, @NonNull final String serviceName,
      @NonNull final String serviceVersion) {
    super(caller, serviceName, serviceVersion, errorResponse);
  }

}
