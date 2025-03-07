package com.github.emw7.examples.updownstream.upstream.service.rest.api.x;

import com.github.emw7.examples.updownstream.downstream.client.common.SensorClient.ClientErrorException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.examples.updownstream.upstream.api.controller.XApi;
import com.github.emw7.examples.updownstream.upstream.common.error.client.XAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.client.XNotFoundClientException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XSystemErrorServerException;
import com.github.emw7.examples.updownstream.upstream.common.error.server.XUnreachableServerException;
import com.github.emw7.examples.updownstream.upstream.logic.XService;
import com.github.emw7.examples.updownstream.upstream.logic.XService.XReadRequest;
import com.github.emw7.examples.updownstream.upstream.rest.common.XApiRest.Create;
import com.github.emw7.examples.updownstream.upstream.rest.common.XApiRest.Delete;
import com.github.emw7.examples.updownstream.upstream.rest.common.XApiRest.Read;
import com.github.emw7.platform.log.EventLogger;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class XControllerRest {

  //region Private static final properties
  private static final Logger log = LoggerFactory.getLogger(XControllerRest.class);
  //endregion Private final properties

  //region Private final properties
  private final XService xService;
  //endregion Private final properties


  //region Constructors
  public XControllerRest(@NonNull final XService xService) {
    this.xService = xService;
  }
  //endregion Constructors

  //region API

  //region Create
  @PostMapping(value = Create.ENDPOINT )
  public @NonNull XApi.CreateXResponse create(@NonNull @RequestBody final Sensor sensor)
      throws XAlreadyExistsClientException, XSystemErrorServerException {

    EventLogger.notice(log, "inside the {}#create for {}", this.getClass().getSimpleName(), sensor)
        .log();

    // Service request.
    final XService.XCreateRequest request = new XService.XCreateRequest(sensor);

    // Invokes operation.
    final XService.XCreateResponse serviceResponse = getxService().create(request);

    // Returns.
    return new XApi.CreateXResponse(serviceResponse.sensor());
  }
  //endregion Create

  //region Delete
  @DeleteMapping(Delete.ENDPOINT)
  public @NonNull XApi.DeleteXResponse delete (@NonNull @PathVariable(name = Delete.P_N_CODE) final String code)
      throws XSystemErrorServerException, XNotFoundClientException {
    EventLogger.notice(log, "inside the {}#delete for {}", this.getClass().getSimpleName(), code)
        .log();
    final XService.XDeleteRequest serviceRequest= new XService.XDeleteRequest(code);
    final XService.XDeleteResponse serviceResponse= getxService().delete(serviceRequest);
    return new XApi.DeleteXResponse();
  }
  //endregion Delete

  //region Read
  //endregion Read
  @GetMapping(path = Read.ENDPOINT, produces = Read.PRODUCES)
  public @NonNull XApi.ReadXResponse read (@NonNull @PathVariable(name = Read.P_N_CODE) final String code,
      @NonNull @RequestParam(name = Read.Q_N_RESPOND_WITH_ERROR, defaultValue = Read.Q_V_RESPOND_WITH_ERROR) final  boolean respondWithError)
      throws XSystemErrorServerException, XNotFoundClientException, XUnreachableServerException {
    EventLogger.notice(log, "inside the {}#read for {}", this.getClass().getSimpleName(), code)
        .log();

    final XService.XReadRequest serviceRequest= new XService.XReadRequest(code, respondWithError);
    final XService.XReadResponse serviceResponse= getxService().read(serviceRequest);
    return new XApi.ReadXResponse(serviceResponse.code(), serviceResponse.value(), serviceResponse.err());
  }

  //endregion API


  //region Getters & Setters
  private XService getxService() {
    return this.xService;
  }
  //endregion
}
