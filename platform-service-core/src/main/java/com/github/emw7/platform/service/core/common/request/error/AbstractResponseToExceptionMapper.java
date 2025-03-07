package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract class AbstractResponseToExceptionMapper<T extends RequestErrorException> implements
    ErrorResponseToExceptionMapper<T> {

  private static final Logger log = LoggerFactory.getLogger(
      AbstractResponseToExceptionMapper.class);

  private final Code code;

  public AbstractResponseToExceptionMapper(@NonNull final Code code) {
    this.code = code;
  }

  @SuppressWarnings("ConstantValue")
  @Override
  public final @NonNull T map(@NonNull final RequestErrorResponse requestErrorResponse)
      throws CannotMapException {

    if ((requestErrorResponse.ref() == null) || !requestErrorResponse.ref()
        .startsWith(code.toString() + '-')) {
      throw new CannotMapException(requestErrorResponse.ref() + " is not compatible with " + code);
    } else {
      return _map(requestErrorResponse);
    }

  }

  /**
   * Returns the {@link RequestErrorException} tied to the specified {@code requestErrorResponse}.
   * <p>
   * Receives a {@code requestErrorResponse} which {@code ref} starts with {@link #code} so the
   * mapping from {@code requestErrorResponse} to {@link RequestErrorException} should be successful
   * but if there is any error in the conversion then must return {@code null}.
   *
   * @param requestErrorResponse the {@link RequestErrorResponse} to be mapped in the tied
   *                             {@link RequestErrorException}.
   * @return the {@link RequestErrorException} tied to the specified {@code requestErrorResponse},
   * {@code null} in case of any error during conversion.
   */
  protected abstract @NonNull T _map(@NonNull final RequestErrorResponse requestErrorResponse) throws CannotMapException;

}
