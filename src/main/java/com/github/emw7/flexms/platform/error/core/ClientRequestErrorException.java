package com.github.emw7.flexms.platform.error.core;

import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;

public abstract sealed class ClientRequestErrorException extends RequestErrorException permits
    BadRequestClientException, NotFoundClientException {

  private static final String TYPE = "2";
  private static final String TYPE_STRING= "CLIENT";

  protected ClientRequestErrorException(@NonNull final String code, final String message, final String label,
      Map<String, String> params) {
    super(TYPE, TYPE_STRING, code, List.of(new Error(message, label, params)));
  }

}
