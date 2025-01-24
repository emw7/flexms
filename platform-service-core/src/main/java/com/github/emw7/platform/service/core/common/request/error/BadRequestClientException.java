package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.core.common.request.error.category.BadRequest;
import java.util.List;
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
