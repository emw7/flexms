package com.github.emw7.platform.service.runtime.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emw7.platform.service.core.common.request.error.ServiceCoreCommonRequestErrorConstants;
import com.github.emw7.platform.i18n.Translator;
import com.github.emw7.platform.service.core.common.request.error.RequestError;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base for server exception handler.
 */
public abstract sealed class AbstractServerExceptionHandler extends AbstractExceptionHandler
permits BasicServerExceptionHandler {

  protected AbstractServerExceptionHandler(@NonNull final ObjectMapper objectMapper,
      @NonNull final Translator translator) {
    super(objectMapper, translator);
  }

  /**
   * Returns {@link ServiceCoreCommonRequestErrorConstants#SERVER_ERROR_CODE}.
   *
   * @param annotationProperties ignored
   * @param requestError         ignored: always returns {@link ServiceCoreCommonRequestErrorConstants#SERVER_ERROR_CODE}
   * @return {@link ServiceCoreCommonRequestErrorConstants#SERVER_ERROR_CODE}
   */
  @NonNull
  @Override
  protected final String label(@NonNull final Map<String, Object> annotationProperties,
      @Nullable final RequestError requestError) {
    return ServiceCoreCommonRequestErrorConstants.SERVER_ERROR_LABEL;
  }

  /**
   * Returns {@link ServiceCoreCommonRequestErrorConstants#SERVER_ERROR_CODE}.
   *
   * @param requestError ignored: always returns {@link ServiceCoreCommonRequestErrorConstants#SERVER_ERROR_CODE}
   * @return {@link ServiceCoreCommonRequestErrorConstants#SERVER_ERROR_CODE}
   */
  @Override
  protected final int retrieveStatus(@Nullable final RequestError requestError) {
    return defaultStatus();
  }

  /**
   * Returns {@link ServiceCoreCommonRequestErrorConstants#SERVER_ERROR_CODE}.
   *
   * @return {@link ServiceCoreCommonRequestErrorConstants#SERVER_ERROR_CODE}
   */
  @Override
  protected final int defaultStatus() {
    return ServiceCoreCommonRequestErrorConstants.SERVER_ERROR_CODE;
  }

}
