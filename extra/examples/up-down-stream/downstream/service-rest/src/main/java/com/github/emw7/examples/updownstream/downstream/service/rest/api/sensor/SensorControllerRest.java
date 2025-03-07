package com.github.emw7.examples.updownstream.downstream.service.rest.api.sensor;

import com.github.emw7.examples.updownstream.downstream.api.SensorApi;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreachableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.examples.updownstream.downstream.logic.SensorService;
import com.github.emw7.examples.updownstream.downstream.rest.common.SensorApiRest.Create;
import com.github.emw7.examples.updownstream.downstream.rest.common.SensorApiRest.Delete;
import com.github.emw7.examples.updownstream.downstream.rest.common.SensorApiRest.Read;
import com.github.emw7.platform.log.EventLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorControllerRest {

  //region Private static final properties
  private static final Logger log = LoggerFactory.getLogger(SensorControllerRest.class);
  //endregion Private static final properties

  //region Private final properties
  private final SensorService sensorService;
  //endregion Private final properties


  //region Constructors
  public SensorControllerRest(@NonNull final SensorService sensorService) {
    this.sensorService = sensorService;
  }
  //endregion Constructors

  //region API

  //region Create
  @PostMapping(Create.ENDPOINT)
  public @NonNull SensorApi.CreateSensorResponse create(@NonNull @RequestBody final Sensor sensor)
      throws SensorAlreadyExistsClientException, SystemErrorServerException {

    EventLogger.notice(log, "inside the {}#create for {}", this.getClass().getSimpleName(), sensor)
        .log();

    // Service request.
    final SensorService.CreateSensorRequest serviceRequest = new SensorService.CreateSensorRequest(
        sensor);

    // Invokes operation.
    final SensorService.CreateSensorResponse serviceResponse = getSensorService().create(
        serviceRequest);

    // Returns.
    return new SensorApi.CreateSensorResponse(serviceResponse.sensor());
  }
  //endregion Create


  //region Delete
  @DeleteMapping(Delete.ENDPOINT)
  public @NonNull SensorApi.DeleteSensorResponse delete(@NonNull @PathVariable final String code)
      throws SensorNotFoundClientException, SystemErrorServerException {

    EventLogger.notice(log, "inside the {}#delete for {}", this.getClass().getSimpleName(), code)
        .log();

    // Service request.
    final SensorService.DeleteSensorRequest serviceRequest = new SensorService.DeleteSensorRequest(
        code);

    // Invokes operation
    final SensorService.DeleteSensorResponse serviceResponse = getSensorService().delete(
        serviceRequest);

    // Returns
    return new SensorApi.DeleteSensorResponse();
  }
  //endregion Delete


  //region Read
  @GetMapping(Read.ENDPOINT)
  public @NonNull SensorApi.ReadSensorResponse read(@NonNull @PathVariable(name= Read.P_N_CODE) final String code,
      @NonNull @RequestParam(name = Read.Q_N_RESPOND_WITH_ERROR, defaultValue = Read.Q_V_RESPOND_WITH_ERROR) final
  boolean respondWithError)
      throws SensorNotFoundClientException, SensorUnreachableServerException {
    EventLogger.notice(log, "inside the {}#read for {}", this.getClass().getSimpleName(), code)
        .log();

    // Service request.
    final SensorService.ReadSensorRequest serviceRequest = new SensorService.ReadSensorRequest(
        code, respondWithError);

    // Invokes operation
    final SensorService.ReadSensorResponse serviceResponse = getSensorService().read(
        serviceRequest);

    // Returns
    return new SensorApi.ReadSensorResponse(serviceResponse.code(), serviceResponse.value(),
        serviceResponse.err());
  }
  //endregion Read

  //endregion API


  //region Getters & Setters
  private SensorService getSensorService() {
    return this.sensorService;
  }
  //endregion
}
