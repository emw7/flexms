package com.github.emw7.examples.updownstream.downstream.common.error.server;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class SystemErrorServerException extends ServerRequestErrorException {

  public SystemErrorServerException(@Nullable final Throwable cause,
      @NonNull final Code code,
      @NonNull final Id id,
      @NonNull final Error error) {
    super(cause, code, id, error);
  }

}
