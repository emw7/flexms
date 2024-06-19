package com.github.emw7.platform.error;

import com.github.emw7.platform.error.category.BadRequest;
import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@BadRequest
public abstract class BadRequestClientException extends ClientRequestErrorException {

  protected BadRequestClientException(@Nullable final Throwable cause, @NonNull final Code code,
      @NonNull final Id id, @NonNull final Error error) {
    this(cause, code, id, List.of(error));
  }

  protected BadRequestClientException(@Nullable final Throwable cause, @NonNull final Code code,
      @NonNull final Id id, @NonNull final List<Error> errors) {
    super(cause, code, id, errors);
  }

}
