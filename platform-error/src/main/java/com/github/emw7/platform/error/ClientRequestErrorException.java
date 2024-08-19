package com.github.emw7.platform.error;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract non-sealed class ClientRequestErrorException extends RequestErrorException {

  private static final String TYPE = "CLIENT";

  public static boolean is (@Nullable final String type)
  {
    return TYPE.equals(type);
  }

  public static boolean is (@Nullable final RequestErrorException requestErrorException)
  {
    return requestErrorException != null && is(requestErrorException.getType());
  }

  protected ClientRequestErrorException(@Nullable final Throwable cause, @NonNull final Code code, @NonNull final Id id,
      @Nullable List<Error> errors) {
    super(cause, TYPE, code, id, errors);
  }

  protected ClientRequestErrorException(@NonNull final Code code, @NonNull final Id id,
      @Nullable List<Error> errors) {
    this(null, code, id, errors);
  }

  protected ClientRequestErrorException(@Nullable final Throwable cause, @NonNull final Code code, @NonNull final Id id,
      @NonNull Error error) {
    this(cause, code, id, List.of(error));
  }

  protected ClientRequestErrorException(@NonNull final Code code, @NonNull final Id id,
      @NonNull Error error) {
    this(null, code, id, error);
  }

}
