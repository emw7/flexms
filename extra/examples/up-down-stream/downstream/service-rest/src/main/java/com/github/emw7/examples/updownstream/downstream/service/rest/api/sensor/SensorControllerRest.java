package com.github.emw7.examples.updownstream.downstream.service.rest.api.sensor;

import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController;
import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController.CreateSensorRequest;
import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController.CreateSensorResponse;
import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController.DeleteSensorRequest;
import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController.DeleteSensorResponse;
import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController.ReadSensorRequest;
import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController.ReadSensorResponse;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreacheableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.examples.updownstream.downstream.logic.SensorService;
import com.github.emw7.examples.updownstream.downstream.service.rest.api.sensor.SensorApiDefinition.Create;
import com.github.emw7.examples.updownstream.downstream.service.rest.api.sensor.SensorApiDefinition.Delete;
import com.github.emw7.examples.updownstream.downstream.service.rest.api.sensor.SensorApiDefinition.Read;
import com.github.emw7.platform.log.EventLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorControllerRest {

  //region Private final properties
  private final Logger log = LoggerFactory.getLogger(SensorControllerRest.class);
  private final SensorController sensorController;
  //endregion Private final properties


  //region Constructors
  public SensorControllerRest(@NonNull final SensorController sensorController) {
    this.sensorController = sensorController;
  }
  //endregion Constructors


  //region API
  @PostMapping(Create.ENDPOINT)
  public @NonNull CreateSensorResponse create(@NonNull @RequestBody final Sensor sensor)
      throws SensorAlreadyExistsClientException, SystemErrorServerException {

    EventLogger.notice(log, "inside the SensorControllerRest#create for {}", sensor).log();

    // Controller request.
    final CreateSensorRequest request = new CreateSensorRequest(sensor);

    // Invokes operation and returns.
    return getSensorController().create(request);
  }

  @DeleteMapping(Delete.ENDPOINT)
  public @NonNull DeleteSensorResponse create(@NonNull @PathVariable final String code)
      throws SensorNotFoundClientException, SystemErrorServerException {
    // Controller request.
    final DeleteSensorRequest request = new DeleteSensorRequest(code);

    // Invokes operation and returns.
    return getSensorController().delete(request);
  }


  @PostMapping(Read.ENDPOINT)
  public @NonNull ReadSensorResponse read(@NonNull @RequestBody final ReadSensorRequest request)
      throws SensorNotFoundClientException, SensorUnreacheableServerException {
    // Invokes operation and returns
    return getSensorController().read(request);
}
  //endregion API


  //region Getters & Setters
  private SensorController getSensorController() {
    return this.sensorController;
  }
  //endregion
}
