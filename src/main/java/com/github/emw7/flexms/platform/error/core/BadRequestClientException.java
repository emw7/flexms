package com.github.emw7.flexms.platform.error.core;

import com.github.emw7.flexms.platform.error.core.category.BadRequest;
import java.util.Map;
import org.springframework.lang.NonNull;

@BadRequest
public abstract non-sealed class BadRequestClientException extends ClientRequestErrorException {

  protected BadRequestClientException(@NonNull final String code, final String message,
      final String label, Map<String, String> params) {
    super(code, message, label, params);
  }
}
