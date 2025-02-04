package com.github.emw7.examples.updownstream.downstream.service.rest.api.sensor;

import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreacheableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.examples.updownstream.downstream.logic.SensorService;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.service.core.request.context.RequestContext;
import com.github.emw7.platform.service.core.request.context.RequestContextHolder;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

public class SensorControllerImpl implements SensorController {

  //region Private static final properties
  private static final Logger log= LoggerFactory.getLogger(SensorControllerImpl.class);
  //endregion Private static final properties

  //region Private final properties
  private final SensorService sensorService;
  //endregion Private final properties


  //region Constructors
  public SensorControllerImpl(final SensorService sensorService) {
    this.sensorService = sensorService;
  }
  //endregion Constructors


  //region API
  @Observed(name = "SensorController#create")
  @Override
  public @NonNull CreateSensorResponse create(@NonNull final CreateSensorRequest request)
      throws SensorAlreadyExistsClientException, SystemErrorServerException {

    // [DEBUG]
    EventLogger.notice(log, "request originator").arg("originator", RequestContextHolder.get().originator()).log();
    EventLogger.notice(log, "request caller").arg("caller", RequestContextHolder.get().caller()).log();
    EventLogger.notice(log, "locale").arg("locale", RequestContextHolder.get().locale()).log();

    // Service request.
    final SensorService.CreateSensorRequest serviceRequest = new SensorService.CreateSensorRequest(
        new Sensor(request.sensor().code(), request.sensor().name(), request.sensor().type()));

    // Invokes operation
    final SensorService.CreateSensorResponse serviceResponse = getSensorService().create(
        serviceRequest);

    // Returns
    return new CreateSensorResponse(
        new Sensor(serviceResponse.sensor().code(), serviceResponse.sensor().name(),
            serviceResponse.sensor().type()));
  }

  @Observed(name = "SensorController#delete")
  @Override
  public @NonNull DeleteSensorResponse delete(@NonNull final DeleteSensorRequest request)
      throws SensorNotFoundClientException, SystemErrorServerException {
    // Service request.
    final SensorService.DeleteSensorRequest serviceRequest= new SensorService.DeleteSensorRequest(
        request.code());

    // Invokes operation
    final SensorService.DeleteSensorResponse serviceResponse= getSensorService().delete(serviceRequest);

    // Returns
    return new DeleteSensorResponse();
  }

  @Override
  public @NonNull ReadSensorResponse read(@NonNull final ReadSensorRequest request)
      throws SensorNotFoundClientException, SensorUnreacheableServerException {
    // Service request.
    final SensorService.ReadSensorRequest serviceRequest= new SensorService.ReadSensorRequest(
        request.code(), request.respondWithError());

    // Invokes operation
    final SensorService.ReadSensorResponse serviceResponse= getSensorService().read(serviceRequest);

    // Returns
    return new ReadSensorResponse(serviceResponse.code(), serviceResponse.value(), serviceResponse.err());
  }
  //endregion API


  //region Getters & Setters
  private SensorService getSensorService() {
    return this.sensorService;
  }
  //endregion
}
