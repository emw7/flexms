package com.github.emw7.platform.app.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.error.Constants;
import com.github.emw7.platform.i18n.Translator;
import org.springframework.lang.NonNull;

public abstract non-sealed class AbstractClientExceptionHandler extends AbstractExceptionHandler {

  protected AbstractClientExceptionHandler(
      @NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }

  @Override
  protected int defaultStatus() {
    return Constants.DEFAULT_CLIENT_ERROR_CODE;
  }

}
