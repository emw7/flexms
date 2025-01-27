package com.github.emw7.platform.service.runtime.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.service.core.common.request.error.Constants;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.service.core.common.request.error.RequestError;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract non-sealed class AbstractServerExceptionHandler extends AbstractExceptionHandler {

  protected AbstractServerExceptionHandler(@NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }

  /**
   * Returns {@link Constants#SERVER_ERROR_CODE}.
   *
   * @param annotationProperties ignored
   * @param requestError         ignored: always returns {@link Constants#SERVER_ERROR_CODE}
   * @return {@link Constants#SERVER_ERROR_CODE}
   */
  @NonNull
  @Override
  protected final String label(@NonNull final Map<String, Object> annotationProperties,
      @Nullable final RequestError requestError) {
    return Constants.SERVER_ERROR_LABEL;
  }

  /**
   * Returns {@link Constants#SERVER_ERROR_CODE}.
   *
   * @param requestError ignored: always returns {@link Constants#SERVER_ERROR_CODE}
   * @return {@link Constants#SERVER_ERROR_CODE}
   */
  @Override
  protected final int retrieveStatus(@Nullable final RequestError requestError) {
    return defaultStatus();
  }

  /**
   * Returns {@link Constants#SERVER_ERROR_CODE}.
   *
   * @return {@link Constants#SERVER_ERROR_CODE}
   */
  @Override
  protected final int defaultStatus() {
    return Constants.SERVER_ERROR_CODE;
  }

}
