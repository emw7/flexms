package com.github.emw7.platform.protocol.rest.error;

import com.github.emw7.platform.protocol.api.error.UnknownDependencyErrorException;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClientException;

/**
 * A specialization of DependencyErrorException for REST unknown error (could not have http status).
 */
public final class RestUnknownDependencyErrorException extends UnknownDependencyErrorException {

  public RestUnknownDependencyErrorException(@NonNull final RestClientException restClientException,
      @NonNull final String caller, @NonNull final String serviceName, @NonNull final String serviceVersion) {
    super(restClientException,
        caller, serviceName, serviceVersion,
        restClientException.getMessage(), restClientException);
  }

}
