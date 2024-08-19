package com.github.emw7.platform.protocol.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * A specialization of DependencyErrorException for REST server error (has http status).
 */
@Deprecated
public final class RestServerDependencyErrorException /*extends AbstractRestDependencyErrorException*/ {

  // TODO ?
  //private final ErrorInfo errorInfo;
  private String errorCode;
  private HttpStatus httpStatus;

  public RestServerDependencyErrorException(String message, Throwable cause,
      @NonNull HttpStatus httpStatus, /*ErrorInfo errorInfo, */String errorCode) {
    //super(message,cause);
    //this.errorInfo = errorInfo;
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }

}
