package com.github.emw7.platform.service.runtime.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.service.core.common.request.error.ServiceCoreCommonRequestErrorConstants;
import com.github.emw7.platform.i18n.Translator;
import org.springframework.lang.NonNull;

/**
 * Base for client exception handler.
 */
public abstract sealed class AbstractClientExceptionHandler extends AbstractExceptionHandler
permits BasicClientExceptionHandler {

  protected AbstractClientExceptionHandler(
      @NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }

  @Override
  protected final int defaultStatus() {
    return ServiceCoreCommonRequestErrorConstants.DEFAULT_CLIENT_ERROR_CODE;
  }

}
