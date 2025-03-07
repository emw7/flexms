package com.github.emw7.examples.updownstream.downstream.client.rest;

import com.github.emw7.examples.updownstream.downstream.client.common.SensorClient;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreachableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.examples.updownstream.downstream.rest.common.SensorApiRest.Create;
import com.github.emw7.examples.updownstream.downstream.rest.common.SensorApiRest.Delete;
import com.github.emw7.examples.updownstream.downstream.rest.common.SensorApiRest.Read;
import com.github.emw7.platform.core.mapper.BooleanMapper;
import com.github.emw7.platform.protocol.api.ProtocolRequest;
import com.github.emw7.platform.service.client.api.error.DependencyErrorServerException;
import com.github.emw7.platform.service.client.api.error.ServiceNotFoundServerException;
import com.github.emw7.platform.service.client.rest.ClientRest;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import java.util.Map;
import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.NonNull;

public final class SensorClientRest extends SensorClient {

  private static final String SERVICE_NAME = "downstream";
  private static final String SERVICE_VERSION = "v1";

  private final ClientRest clientRest;

  public SensorClientRest(@NonNull final ClientRest clientRest) {
    this.clientRest = clientRest;
  }


  @Override
  protected @NonNull CreateSensorResponse _create(@NonNull final CreateSensorRequest request)
      throws SensorAlreadyExistsClientException, SystemErrorServerException, ServiceNotFoundServerException, DependencyErrorServerException, NestedRuntimeException {

    final ProtocolRequest<Sensor> protocolRequest = Create.protocolRequestBuilder()
        .body(request.sensor()).build();
    try {
      return clientRest.call(SERVICE_NAME, SERVICE_VERSION, Create.ENDPOINT, protocolRequest,
          Create.responseClazz, CREATE_EX_MAPPER);
    } catch (SensorAlreadyExistsClientException | SystemErrorServerException |
             ServiceNotFoundServerException | DependencyErrorServerException e) {
      throw e;
    } catch (RequestErrorException e) {
      // this covers all the other cases that for any reason are not covered by EX_MAPPER.
      // TODO lanciare una programming error o una unmapped error?
      throw new NestedRuntimeException(e.getClass().getName(), e) {
      };
    }
  }


  @Override
  protected @NonNull DeleteSensorResponse _delete(@NonNull final DeleteSensorRequest request)
      throws SensorNotFoundClientException, SystemErrorServerException, ServiceNotFoundServerException, DependencyErrorServerException, NestedRuntimeException {

    final ProtocolRequest<Void> protocolRequest = Delete.protocolRequestBuilder()
        .pathParam(Delete.P_N_CODE,request.code()).build();
    try {
      return clientRest.call(SERVICE_NAME, SERVICE_VERSION, Delete.ENDPOINT, protocolRequest,
          DeleteSensorResponse.class, DELETE_EX_MAPPER);
    } catch (SensorNotFoundClientException | SystemErrorServerException |
             ServiceNotFoundServerException | DependencyErrorServerException e) {
      throw e;
    } catch (RequestErrorException e) {
      // this covers all the other cases that for any reason are not covered by EX_MAPPER.
      // TODO lanciare una programming error o una unmapped error?
      throw new NestedRuntimeException(e.getClass().getName(), e) {
      };
    }
  }

  @Override
  protected @NonNull ReadSensorResponse _read(@NonNull final ReadSensorRequest request)
      throws SensorNotFoundClientException, SensorUnreachableServerException, ServiceNotFoundServerException, DependencyErrorServerException {
  final ProtocolRequest<Void> protocolRequest = Read.protocolRequestBuilder()
      .pathParam(Read.P_N_CODE, request.code())
      .queryParam(Read.Q_N_RESPOND_WITH_ERROR, BooleanMapper.toString(request.respondWithError()))
      .build();
    try {
      return clientRest.call(SERVICE_NAME, SERVICE_VERSION, Read.ENDPOINT, protocolRequest,
          ReadSensorResponse.class, READ_EX_MAPPER);
    } catch (SensorNotFoundClientException | SensorUnreachableServerException |
             ServiceNotFoundServerException | DependencyErrorServerException e) {
      throw e;
    } catch (RequestErrorException e) {
      // this covers all the other cases that for any reason are not covered by EX_MAPPER.
      // TODO lanciare una programming error o una unmapped error?
      throw new NestedRuntimeException(e.getClass().getName(), e) {
      };
    }
  }

}
