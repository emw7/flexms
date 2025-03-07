package com.github.emw7.examples.updownstream.downstream.client.common;

import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.platform.service.core.common.request.error.AlreadyExistsClientException;
import com.github.emw7.platform.service.core.common.request.error.CompositeErrorResponseToExceptionMapper;
import com.github.emw7.platform.service.core.common.request.error.ErrorResponseToExceptionMapper;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import org.springframework.lang.NonNull;

public final class SensorExceptionMapper<T extends RequestErrorException> implements ErrorResponseToExceptionMapper<T> {

  private static final ErrorResponseToExceptionMapper<?> INTERNAL_MAPPER= CompositeErrorResponseToExceptionMapper.builder()
      .add(AlreadyExistsClientException.mapper(SensorAlreadyExistsClientException.class,
          String.class))
      .add(SystemErrorServerException.mapper()).build();

  private static final SensorExceptionMapper<?> INSTANCE= new SensorExceptionMapper<>();

  public static SensorExceptionMapper<?> get () {
    return INSTANCE;
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NonNull T map(final RequestErrorResponse requestErrorResponse)
      throws CannotMapException {
    return (T)INTERNAL_MAPPER.map(requestErrorResponse);
  }

  //region Constructors
  // prevents instantiation.
  private SensorExceptionMapper() {
  }
  //endregion Constructors

}
