package com.github.emw7.examples.updownstream.downstream.logic;

import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException.Error;
import io.micrometer.observation.annotation.Observed;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorAlreadyExistsClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.client.SensorNotFoundClientException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SensorUnreachableServerException;
import com.github.emw7.examples.updownstream.downstream.common.error.server.SystemErrorServerException;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;


/**
 * The service implementation: it is API technology-agnostic.
 */
public class DefaultSensorService implements SensorService {

  private final static Logger log = LoggerFactory.getLogger(DefaultSensorService.class);

  //region Create
  @Observed(name = "SensorService#create")
  @Override
  public @NonNull CreateSensorResponse create(@NonNull final CreateSensorRequest request)
      throws SensorAlreadyExistsClientException, SystemErrorServerException {

    final Sensor requestedSensor = request.sensor();
    final String sensorCode = requestedSensor.code();

    EventLogger.notice(log, "requested to create sensor with code {}", sensorCode).info().log();

    switch (sensorCode) {
      case "already-exists":
        throw new SensorAlreadyExistsClientException(sensorCode);
      case "system-error":
        throw new SystemErrorServerException(new RuntimeException("create-system-error"),
            new Code("6GEYW"), new Id("YOCBD"),
            new Error("app.i18n.error.create.system-error", Map.of("code", sensorCode)));
    }

    return new CreateSensorResponse(requestedSensor);
  }
  //endregion Create

  //region Delete
  @Observed(name = "SensorService#delete")
  @Override
  public @NonNull DeleteSensorResponse delete(@NonNull final DeleteSensorRequest request)
      throws SensorNotFoundClientException, SystemErrorServerException {

    final String sensorCode = request.code();

    switch (sensorCode) {
      case "sensor-not-found":
        throw new SensorNotFoundClientException(sensorCode);
      case "system-error":
        throw new SystemErrorServerException(new RuntimeException("delete-system-error"),
            new Code("LWYI9"), new Id("GHF8E"),
            new Error("app.i18n.error.delete.system-error", Map.of("code", sensorCode)));
    }

    return new DeleteSensorResponse();
  }
  //endregion Delete


  //region Read
  @Observed(name = "SensorService#read")
  @Override
  public @NonNull ReadSensorResponse read(@NonNull final ReadSensorRequest request)
      throws SensorNotFoundClientException, SensorUnreachableServerException {

    final String sensorCode = request.code();

    try {
      switch (sensorCode) {
        case "sensor-not-found":
          throw new SensorNotFoundClientException(sensorCode);
        case "unreachable-sensor":
          throw new SensorUnreachableServerException(new RuntimeException("unreachable-sensor"),
              new Id("ZWWHD"),
              new Error("app.i18n.error.read.unreachable-sensor", Map.of("code", sensorCode)));
      }
      return new ReadSensorResponse(sensorCode, 1.0, 0);
    } catch (SensorNotFoundClientException | SensorUnreachableServerException e) {
      if (request.respondWithError()) {
        EventLogger.notice(log, "error occurred while reading sensor {}: {}", sensorCode,
            e.getMessage()).error().log();
        return new ReadSensorResponse(sensorCode, null, 1);
      } else {
        throw e;
      }
    }
  }
  //endregion Read

}
