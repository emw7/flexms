package com.github.emw7.platform.protocol.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * A specialization of DependencyErrorException for REST client error that is BAD REQUEST
 * (http status = 400) (has http status).
 */
@Deprecated
public final class RestValidationDependencyErrorException /*extends AbstractRestDependencyErrorException*/ {

  private final String response;

  private final HttpStatus httpStatus;

  public RestValidationDependencyErrorException(String message, Throwable cause,
      @NonNull HttpStatus httpStatus, String response) {
    //super(null, message, cause);
    this.response= response;
    this.httpStatus = httpStatus;
  }

}
