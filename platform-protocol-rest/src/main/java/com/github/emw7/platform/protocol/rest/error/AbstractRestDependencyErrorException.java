package com.github.emw7.platform.protocol.rest.error;

import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

/**
 * An "empty" class creating a layer between protocol agnostic dependency errors and rest errors
 */
public abstract sealed class AbstractRestDependencyErrorException extends
    DependencyErrorException permits RestDependencyErrorException, RestUnknownDependencyErrorException {

  public AbstractRestDependencyErrorException (
      @NonNull final RestClientException errorResponse, @NonNull final String message, @NonNull final Throwable cause) {
    super(errorResponse, message, cause);
  }

  @Override
  public @NonNull RestClientException getErrorResponse() {
    // TODO throw Programming error in case of class cast exception o rin case getErrorresponse is null (?)
    return (RestClientException)super.getErrorResponse();
  }

  @Override
  public synchronized @NonNull Throwable getCause() {
    return super.getCause();
  }
}
