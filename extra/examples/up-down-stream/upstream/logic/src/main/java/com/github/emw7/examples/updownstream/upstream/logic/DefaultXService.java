package com.github.emw7.examples.updownstream.upstream.logic;

import com.github.emw7.examples.updownstream.downstream.api.SensorApi.CreateSensorRequest;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.CreateSensorResponse;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.DeleteSensorRequest;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.DeleteSensorResponse;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.ReadSensorRequest;
import com.github.emw7.examples.updownstream.downstream.api.SensorApi.ReadSensorResponse;
import com.github.emw7.examples.updownstream.downstream.client.common.SensorClient;
import com.github.emw7.examples.updownstream.downstream.client.common.SensorClient.ClientErrorException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreachableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.upstream.common.error.client.XAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.client.XNotFoundClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XSystemErrorServerException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XUnreachableServerException;
import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException.Error;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;


/**
 * The service implementation: it is API technology-agnostic.
 */
public class DefaultXService implements XService {

  //region Private static final properties
  private final static Logger log = LoggerFactory.getLogger(DefaultXService.class);
  //region Private static final properties

  //region Private final properties
  private final SensorClient sensorClient;
  //endregion Private final properties

  //region Constructors
  public DefaultXService(@NonNull final SensorClient sensorClient) {
    this.sensorClient = sensorClient;
  }

  //endregion Constructors

  //region Create
  // TODO forse mapper non è il migliore dei nomi.
  private static class CreateMapper {

    // map from service to client request
    static CreateSensorRequest map(@NonNull final XCreateRequest serviceRequest) {
      return new CreateSensorRequest(serviceRequest.sensor());
    }

    // map from client response to service (app) response
    static XCreateResponse map(@NonNull final CreateSensorResponse clientResponse) {
      return new XCreateResponse(clientResponse.sensor());
    }
  }

  @NonNull
  @Override
  public XCreateResponse create(@NonNull final XCreateRequest request)
      throws XAlreadyExistsClientException, XSystemErrorServerException {

    EventLogger.notice(log, "inside the DefaultXService#create for {}", request).log();

    final CreateSensorRequest createReq = CreateMapper.map(request);

    try {
      final CreateSensorResponse createRes = getSensorClient().create(createReq);
      return CreateMapper.map(createRes);
    } catch (SensorAlreadyExistsClientException e) {
      //noinspection DataFlowIssue as Error forces parms to be non-null.
      throw new XAlreadyExistsClientException(
          (String) e.getErrors().getFirst().params().get("resourceId"));
    } catch (SystemErrorServerException | ClientErrorException e) {
      throw new XSystemErrorServerException(e, new Code("04JQD"), new Id("KMFHU"),
          new Error("app.i18n.error.create.system-error",
              Map.of("code", request.sensor().code(), "error", e.getMessage())));
    }

  }
  //endregion Create

  //region Delete
  private static class DeleteMapper {

    // map from service to client request
    static DeleteSensorRequest map(@NonNull final XDeleteRequest serviceRequest) {
      return new DeleteSensorRequest(serviceRequest.code());
    }

    // map from client response to service (app) response
    static XDeleteResponse map(@NonNull final DeleteSensorResponse ignored) {
      return new XDeleteResponse();
    }
  }

  @NonNull
  @Override
  public XDeleteResponse delete(@NonNull final XDeleteRequest request)
      throws XNotFoundClientException, XSystemErrorServerException {

    EventLogger.notice(log, "inside the DefaultXService#delete for {}", request).log();

    final DeleteSensorRequest req = DeleteMapper.map(request);

    try {
      final DeleteSensorResponse res = getSensorClient().delete(req);
      return DeleteMapper.map(res);
    } catch (SensorNotFoundClientException e) {
      //noinspection DataFlowIssue as Error forces parmas to be non-null.
      throw new XNotFoundClientException(
          (String) e.getErrors().getFirst().params().get("resourceId"));
    } catch (SystemErrorServerException | ClientErrorException e) {
      throw new XSystemErrorServerException(e, new Code("04JQD"), new Id("KMFHU"),
          new Error("app.i18n.error.create.system-error",
              Map.of("code", request.code(), "error", e.getMessage())));
    }

  }
  //endregion Delete

  //region Read
  private static class ReadMapper {

    // map from service to client request
    static ReadSensorRequest map(@NonNull final XReadRequest serviceRequest) {
      return new ReadSensorRequest(serviceRequest.code(), serviceRequest.respondWithError());
    }

    // map from client response to service (app) response
    static XReadResponse map(@NonNull final ReadSensorResponse serviceResponse) {
      return new XReadResponse(serviceResponse.code(), serviceResponse.value(),
          serviceResponse.err());
    }
  }

  @NonNull
  @Override
  public XReadResponse read(@NonNull final XReadRequest request)
      throws XNotFoundClientException, XUnreachableServerException, XSystemErrorServerException {

    EventLogger.notice(log, "inside the DefaultXService#read for {}", request).log();

    final ReadSensorRequest req = ReadMapper.map(request);

    try {

      final ReadSensorResponse res = getSensorClient().read(req);
      return ReadMapper.map(res);

    } catch (SensorNotFoundClientException e) {
      throw new XNotFoundClientException(request.code());
    } catch (SensorUnreachableServerException e) {
      throw new XUnreachableServerException(e, new Id("4WIKR"), e.getErrors().getFirst());
    } catch (ClientErrorException e) {
      throw new XSystemErrorServerException(e, new Code("C33YQ"), new Id("1D7T0"),
          new Error("app.i18n.error.create.system-error",
              Map.of("code", request.code(), "error", e.getMessage())));
    }
  }
  //endregion Read

  //region Getters & Setters
  private SensorClient getSensorClient() {
    return sensorClient;
  }
  //endregion Getters & Setters

}
