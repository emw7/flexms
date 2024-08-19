package com.github.emw7.platform.protocol.rest.error;

import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClientException;

/**
 * A specialization of DependencyErrorException for REST unknown error (could not have http status).
 */
public final class RestUnknownDependencyErrorException extends AbstractRestDependencyErrorException {

  public RestUnknownDependencyErrorException(@NonNull final RestClientException restClientException) {
    super(restClientException, restClientException.getMessage(), restClientException);
  }

}
