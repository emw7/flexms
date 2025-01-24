package com.github.emw7.examples.serviceruntime.api;

import com.github.emw7.examples.serviceruntime.error.ClientExHandler;
import com.github.emw7.examples.serviceruntime.error.ServerExHandler;
import com.github.emw7.examples.serviceruntime.logic.SensorService;
import com.github.emw7.examples.serviceruntime.model.Sensor;
import com.github.emw7.platform.service.core.common.request.error.ClientRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import io.micrometer.observation.annotation.Observed;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SensorControllerImpl implements SensorController {

  //region Private final properties
  private final SensorService serviceSensor;
  private final ClientExHandler clientExHandler;
  private final ServerExHandler serverExHandler;
  //endregion Private final properties



  //region Constructors
  public SensorControllerImpl(@NonNull final SensorService serviceSensor,
      @NonNull final ClientExHandler clientExHandler,
      @NonNull final ServerExHandler serverExHandler) {
    this.serviceSensor = serviceSensor;
    this.clientExHandler = clientExHandler;
    this.serverExHandler = serverExHandler;
  }
  //endregion Constructors



  //region API

  //region Create
  @Observed(name = "SensorController#create")
  @Override
  public @NonNull CreateSensorResponse create(@NonNull final CreateSensorRequest request) {
    final SensorService.CreateSensorRequest serviceRequest = new SensorService.CreateSensorRequest(
        request.sensor());

    try {
      final SensorService.CreateSensorResponse serviceResponse = getServiceSensor().create(
          serviceRequest);
      return new CreateSensorResponse(
          new Sensor(serviceResponse.sensor().code(), serviceResponse.sensor().name(),
              serviceResponse.sensor().type()), null);
    } catch (ClientRequestErrorException e) {
      return new CreateSensorResponse(null, getClientExHandler().handle(e));
    } catch (ServerRequestErrorException e) {
      return new CreateSensorResponse(null, getServerExHandler().handle(e));
    }
  }

  /**
   * This is not part of the interface but is it implementeted to simulate a client that cannot
   * send a {@code DeleteSensorRequest} instance (a browser for example in REST context).
   */
  @Observed(name = "SensorController#create")
  public @NonNull CreateSensorResponse create(@NonNull String code, @NonNull String name, int type) {
    return create(new CreateSensorRequest(new Sensor(code, name, type)));
  }
  //endregion Create


  //region Delete
  @Observed(name = "SensorController#delete")
  @Override
  public @NonNull DeleteSensorResponse delete(@NonNull final DeleteSensorRequest request) {
    final SensorService.DeleteSensorRequest serviceRequest = new SensorService.DeleteSensorRequest(
        request.code());

    try {
      /*final ServiceSensor.DeleteSensorResponse serviceResponse = */
      getServiceSensor().delete(serviceRequest);
      return new DeleteSensorResponse(null);
    } catch (ClientRequestErrorException e) {
      return new DeleteSensorResponse(getClientExHandler().handle(e));
    } catch (ServerRequestErrorException e) {
      return new DeleteSensorResponse(getServerExHandler().handle(e));
    }

  }

  /**
   * This is not part of the interface but is it implementeted to simulate a client that cannot
   * send a {@code DeleteSensorRequest} instance (a browser for example in REST context).
   */
  @Observed(name = "SensorController#delete")
  public @NonNull DeleteSensorResponse delete(@NonNull final String code) {
    return delete(new DeleteSensorRequest(code));
  }
  //endregion Delete

  //endregion API


  //region Getters & Setters
  private SensorService getServiceSensor() {
    return serviceSensor;
  }

  private ClientExHandler getClientExHandler() {
    return clientExHandler;
  }

  private ServerExHandler getServerExHandler() {
    return serverExHandler;
  }
  //endregion Getters & Setters

}
