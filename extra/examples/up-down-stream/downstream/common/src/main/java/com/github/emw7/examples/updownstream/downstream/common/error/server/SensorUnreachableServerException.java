package com.github.emw7.examples.updownstream.downstream.common.error.server;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.AbstractConstructorResponseToExceptionMapper;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class SensorUnreachableServerException extends ServerRequestErrorException {

  public static SensorUnreachableServerException.Mapper mapper() {
    return new SensorUnreachableServerException.Mapper();
  }

  public static class Mapper extends
      AbstractConstructorResponseToExceptionMapper<SensorUnreachableServerException> {

    private Mapper() {
      super(CODE, SensorUnreachableServerException.class, Throwable.class, Id.class,
          Error.class);
    }

    @Override
    protected Object[] retrieveArgsForConstructor(
        @NonNull final RequestErrorResponse requestErrorResponse) {
      final Object[] argsForConstructor = new Object[3];
      argsForConstructor[0] = null;
      argsForConstructor[1] = new Id("0");
      final List<Error> errors = requestErrorResponse.errors();
      argsForConstructor[2] = errors.isEmpty() ? new Error("", "", null) : errors.getFirst();
      return argsForConstructor;
    }
  }

  public static final Code CODE = new Code("TWC0J");

  public SensorUnreachableServerException(@Nullable final Throwable cause, @NonNull final Id id,
      @NonNull final Error error) {
    super(cause, CODE, id, error);
  }

}
