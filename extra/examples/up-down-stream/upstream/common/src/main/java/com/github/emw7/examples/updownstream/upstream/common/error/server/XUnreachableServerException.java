package com.github.emw7.examples.updownstream.upstream.common.error.server;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class XUnreachableServerException extends ServerRequestErrorException {

  public static final Code CODE= new Code("V0BTW");
  public XUnreachableServerException(@Nullable final Throwable cause,
      @NonNull final Id id,
      @NonNull final Error error) {
    super(cause, CODE, id, error);
  }

}
