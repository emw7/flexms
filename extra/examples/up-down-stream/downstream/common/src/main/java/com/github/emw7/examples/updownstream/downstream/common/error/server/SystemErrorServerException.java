package com.github.emw7.examples.updownstream.downstream.common.error.server;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.AbstractConstructorResponseToExceptionMapper;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class SystemErrorServerException extends ServerRequestErrorException {

  public static Mapper mapper () {
    return new Mapper();
  }

  public static class Mapper extends
      AbstractConstructorResponseToExceptionMapper<SystemErrorServerException> {

    private Mapper() {
      super(new Code("6GEYW"), SystemErrorServerException.class, Throwable.class, Code.class,
          Id.class, Error.class);
    }

    @Override
    protected Object[] retrieveArgsForConstructor(
        @NonNull final RequestErrorResponse requestErrorResponse) {
      final Object[] argsForConstructor = new Object[4];
      argsForConstructor[0] = null;
      argsForConstructor[1] = new Code("6GEYW");
      argsForConstructor[2] = new Id("0");
      final List<Error> errors = requestErrorResponse.errors();
      argsForConstructor[3] = errors.isEmpty() ? new Error("", "", null) : errors.getFirst();
      return argsForConstructor;
    }
  }

  public SystemErrorServerException(@Nullable final Throwable cause, @NonNull final Code code,
      @NonNull final Id id, @NonNull final Error error) {
    super(cause, code, id, error);
  }

}
