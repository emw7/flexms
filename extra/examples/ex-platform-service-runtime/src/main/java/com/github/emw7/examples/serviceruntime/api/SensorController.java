package com.github.emw7.examples.serviceruntime.api;

import com.github.emw7.examples.serviceruntime.model.Sensor;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The controller interface that defines the API request and response payloads and the available
 * endpoints. It is technology-agnostic.
 * <p>
 * The response classes bring both correct answer and error answer. This is a design solution of
 * this example. In the README.md of the project there is an alternative implementation.
 */
public interface SensorController {

  //region Create
  record CreateSensorRequest (@NonNull Sensor sensor) {}
  record CreateSensorResponse (@Nullable Sensor sensor, @Nullable RequestErrorResponse error) {}

  @NonNull CreateSensorResponse create (@NonNull final CreateSensorRequest request);
  //endregion Create

  //region Delete
  record DeleteSensorRequest (@NonNull String code) {}
  record DeleteSensorResponse (@Nullable RequestErrorResponse error) {}

  @NonNull DeleteSensorResponse delete (@NonNull final DeleteSensorRequest request);
  //endregion Delete
}
