package com.github.emw7.platform.error;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract non-sealed class ServerRequestErrorException extends RequestErrorException {

  private static final String TYPE = "SERVER";

  public static boolean is (@Nullable final String type)
  {
    return TYPE.equals(type);
  }

  public static boolean is (@Nullable final RequestErrorException requestErrorException)
  {
    return requestErrorException != null && is(requestErrorException.getType());
  }

  protected ServerRequestErrorException(@NonNull final Code code, @NonNull final Id id,
      @NonNull Error error) {
    super(TYPE, code, id, error);
  }

  protected ServerRequestErrorException(@Nullable final Throwable cause, @NonNull final Code code, @NonNull final Id id,
      @NonNull Error error) {
    super(cause, TYPE, code, id, error);
  }

}
