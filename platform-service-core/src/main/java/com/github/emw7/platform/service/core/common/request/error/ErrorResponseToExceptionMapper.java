package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.util.function.Function;
import org.springframework.lang.NonNull;

@FunctionalInterface
public interface ErrorResponseToExceptionMapper<T extends RequestErrorException>  {

  final class CannotMapException extends Exception {

    public CannotMapException(final String message) {
      super(message);
    }

    public CannotMapException(final String message, Throwable cause) {
      super(message, cause);
    }

  }

  @NonNull
  T map(RequestErrorResponse requestErrorResponse) throws CannotMapException;

}
