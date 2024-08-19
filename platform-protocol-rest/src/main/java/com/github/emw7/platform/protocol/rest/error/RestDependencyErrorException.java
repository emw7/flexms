package com.github.emw7.platform.protocol.rest.error;

import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClientResponseException;

/**
 * The error that occurred when calling a dependency through REST.
 */
public final class RestDependencyErrorException extends AbstractRestDependencyErrorException {

  public RestDependencyErrorException(
      @NonNull final RestClientResponseException restClientResponseException) {
    super(restClientResponseException, restClientResponseException.getMessage(), restClientResponseException);
  }

  // TODO [DOC] warns about Programming error (inherited by super).
  @Override
  public @NonNull RestClientResponseException getErrorResponse() {
    return (RestClientResponseException) super.getErrorResponse();
  }
}
