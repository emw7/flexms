package com.github.emw7.flexms.platform.error.core;

import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;

public abstract non-sealed class ServerRequestErrorException extends RequestErrorException {

  private static final String TYPE= "1";
  private static final String TYPE_STRING= "SERVER";

  protected ServerRequestErrorException(@NonNull final String code, final String message, final String label,
      Map<String, String> params) {
    super(TYPE, TYPE_STRING, code, List.of(new Error(
        message, label, params)));
  }

}
