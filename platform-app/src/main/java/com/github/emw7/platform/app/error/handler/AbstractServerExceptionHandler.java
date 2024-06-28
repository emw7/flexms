package com.github.emw7.platform.app.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.error.Constants;
import com.github.emw7.platform.error.RequestError;
import com.github.emw7.platform.i18n.Translator;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract non-sealed class AbstractServerExceptionHandler extends AbstractExceptionHandler {

  protected AbstractServerExceptionHandler(
      @NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }

  @NonNull
  @Override
  protected String label(@NonNull final Map<String, Object> annotationProperties,
      @Nullable final RequestError requestError) {
    return Constants.SERVER_ERROR_LABEL;
  }

  @Override
  protected int retrieveStatus(@Nullable final RequestError requestError) {
    return Constants.SERVER_ERROR_CODE;
  }

  @Override
  protected int defaultStatus() {
    return Constants.SERVER_ERROR_CODE;
  }

}
