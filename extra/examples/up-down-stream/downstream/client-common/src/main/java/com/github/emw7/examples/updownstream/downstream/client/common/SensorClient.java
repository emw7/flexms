package com.github.emw7.examples.updownstream.downstream.client.common;

import com.github.emw7.examples.updownstream.downstream.api.SensorApi;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.CreateSensorRequest;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.CreateSensorResponse;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.DeleteSensorRequest;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.DeleteSensorResponse;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.ReadSensorRequest;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.ReadSensorResponse;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreachableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.service.client.api.error.DependencyErrorServerException;
import com.github.emw7.platform.service.client.api.error.ServiceNotFoundServerException;
import com.github.emw7.platform.service.core.common.request.error.AlreadyExistsClientException;
import com.github.emw7.platform.service.core.common.request.error.CompositeErrorResponseToExceptionMapper;
import com.github.emw7.platform.service.core.common.request.error.ErrorResponseToExceptionMapper;
import com.github.emw7.platform.service.core.common.request.error.NotFoundClientException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract class /*interface*/ SensorClient implements SensorApi {

  //region Public static types

  /**
   * Exception throws in case of client encounters errors that (local) service cannot encounter,
   * that is:
   * <ul>
   *   <li>ServiceNotFoundServerException - when the client cannot found the related service.</li>
   *   <li>DependencyErrorServerException - when the related service answered with non-standard error</li>
   * </ul>
   */
  public static final class ClientErrorException extends NestedRuntimeException {

    public ClientErrorException(@NonNull final Throwable cause) {
      super(cause.getMessage(), cause);
    }
  }
  //endregion Public static types

  //region Create

  public static final ErrorResponseToExceptionMapper<?> CREATE_EX_MAPPER= CompositeErrorResponseToExceptionMapper.builder()
      .add(AlreadyExistsClientException.mapper(SensorAlreadyExistsClientException.class,
          String.class))
      .add(SystemErrorServerException.mapper()).build();
  /**
   *
   * @param request
   * @return
   * @throws SensorAlreadyExistsClientException
   * @throws SystemErrorServerException
   * @throws ClientErrorException in case the client encounters either ServiceNotFoundServerException or DependencyErrorServerException
   */
  @Override
  public final @NonNull CreateSensorResponse create(@NonNull final CreateSensorRequest request)
      throws SensorAlreadyExistsClientException, SystemErrorServerException, ClientErrorException {
    try {
      return _create(request);
    } catch ( ServiceNotFoundServerException | DependencyErrorServerException e ) {
      throw new ClientErrorException(e);
    }
  }

  protected abstract @NonNull CreateSensorResponse _create(@NonNull final CreateSensorRequest request)
      throws SensorAlreadyExistsClientException, SystemErrorServerException, ServiceNotFoundServerException, DependencyErrorServerException;
  //endregion Create

  //region Delete
  public static final ErrorResponseToExceptionMapper<?> DELETE_EX_MAPPER= CompositeErrorResponseToExceptionMapper.builder()
      .add(NotFoundClientException.mapper(SensorNotFoundClientException.class,
          String.class))
      .add(SystemErrorServerException.mapper()).build();

  /**
   *
   * @param request
   * @return
   * @throws SensorNotFoundClientException
   * @throws SystemErrorServerException
   * @throws ClientErrorException in case the client encounters either ServiceNotFoundServerException or DependencyErrorServerException
   */
  @Override
  public final @NonNull DeleteSensorResponse delete(@NonNull final DeleteSensorRequest request)
      throws SensorNotFoundClientException, SystemErrorServerException, ClientErrorException {
    try {
      return _delete(request);
    } catch ( ServiceNotFoundServerException | DependencyErrorServerException e ) {
      throw new ClientErrorException(e);
    }
  }

  protected abstract @NonNull DeleteSensorResponse _delete(@NonNull final DeleteSensorRequest request)
      throws SensorNotFoundClientException, SystemErrorServerException, ServiceNotFoundServerException, DependencyErrorServerException;
  //endregion Delete

  //region Read
  public static final ErrorResponseToExceptionMapper<?> READ_EX_MAPPER= CompositeErrorResponseToExceptionMapper.builder()
      .add(NotFoundClientException.mapper(SensorNotFoundClientException.class,
          String.class))
      .add(SensorUnreachableServerException.mapper())
      .build();

  /**
   *
   * @param request
   * @return
   * @throws SensorNotFoundClientException
   * @throws SensorUnreachableServerException
   * @throws ClientErrorException in case the client encounters either ServiceNotFoundServerException or DependencyErrorServerException

   */
  @Override
  public final @NonNull ReadSensorResponse read(@NonNull final ReadSensorRequest request)
      throws SensorNotFoundClientException, SensorUnreachableServerException, ClientErrorException {
    try {
      return _read(request);
    } catch ( ServiceNotFoundServerException | DependencyErrorServerException e ) {
      throw new ClientErrorException(e);
    }
  }

  protected abstract @NonNull ReadSensorResponse _read(@NonNull final ReadSensorRequest request)
      throws SensorNotFoundClientException, SensorUnreachableServerException, ServiceNotFoundServerException, DependencyErrorServerException;

  //endregion Read
}
