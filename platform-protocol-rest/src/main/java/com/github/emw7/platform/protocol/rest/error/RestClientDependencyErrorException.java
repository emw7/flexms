package com.github.emw7.platform.protocol.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * A specialization of DependencyErrorException for REST client error that is not BAD REQUEST (http
 * status != 400) (has http status).
 */
@Deprecated
public final class RestClientDependencyErrorException /*extends AbstractRestDependencyErrorException*/ {

  //private final List<ErrorInfo> errorInfo;


  public RestClientDependencyErrorException(String message, Throwable cause,
      @NonNull final HttpStatus httpStatus/*, @Nullable final List<ErrorInfo> errorInfo*/) {
    //super(message,cause);
    //this.errorInfo = Optional.ofNullable(errorInfo).orElse(Collections.emptyList());
    //this.httpStatus = httpStatus;
  }

}
