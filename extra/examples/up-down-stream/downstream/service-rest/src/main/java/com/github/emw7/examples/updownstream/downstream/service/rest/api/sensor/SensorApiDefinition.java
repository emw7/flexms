package com.github.emw7.examples.updownstream.downstream.service.rest.api.sensor;

import com.github.emw7.examples.updownstream.downstream.api.controller.SensorController.ReadSensorRequest;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.platform.service.core.runtime.ApiDefinition;
import com.github.emw7.platform.service.core.runtime.ApiDefinition.ApiSemantic;
import java.nio.file.Path;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class SensorApiDefinition {

  private static final String SENSOR_ENDPOINT = "/sensor";

  //region Create
  public static final class Create implements ApiDefinition {
    public static final ApiSemantic SEMANTIC = ApiSemantic.POST;
    public static final String ENDPOINT = SENSOR_ENDPOINT;
    public static final Class<Sensor> requestBodyClazz = Sensor.class;
    public static final java.util.List<String> pathParameters = List.of();
    public static final java.util.List<String> queryParameters = List.of();

    @NonNull
    @Override
    public ApiSemantic semantic() {
      return SEMANTIC;
    }

    @NonNull
    @Override
    public String endpoint() {
      return ENDPOINT;
    }

    @Nullable
    @Override
    public Class<?> requestBodyClazz() {
      return requestBodyClazz;
    }

    @NonNull
    @Override
    public List<String> pathParameters() {
      return pathParameters;
    }

    @NonNull
    @Override
    public List<String> queryParameters() {
      return queryParameters;
    }
  }
  //endregion Create

  //region Delete
  public static final class Delete implements ApiDefinition{
    public static final ApiSemantic SEMANTIC = ApiSemantic.DELETE;
    public static final String ENDPOINT = SENSOR_ENDPOINT + '/' + "{code}";
    public static final Class<Sensor> requestBodyClazz = null;
    public static final java.util.List<String> pathParameters = List.of("code");
    public static final java.util.List<String> queryParameters = List.of();

    @NonNull
    @Override
    public ApiSemantic semantic() {
      return SEMANTIC;
    }

    @NonNull
    @Override
    public String endpoint() {
      return ENDPOINT;
    }

    @Nullable
    @Override
    public Class<?> requestBodyClazz() {
      return requestBodyClazz;
    }

    @NonNull
    @Override
    public List<String> pathParameters() {
      return pathParameters;
    }

    @NonNull
    @Override
    public List<String> queryParameters() {
      return queryParameters;
    }
  }
  //endregion Delete

  //region Read
  public static final class Read implements ApiDefinition {
    public static final ApiSemantic SEMANTIC = ApiSemantic.GET;
    public static final String ENDPOINT = SENSOR_ENDPOINT + ":read";
    public static final Class<ReadSensorRequest> requestBodyClazz = ReadSensorRequest.class;
    public static final List<String> pathParameters = List.of();
    public static final List<String> queryParameters = List.of();

    @NonNull
    @Override
    public ApiSemantic semantic() {
      return SEMANTIC;
    }

    @NonNull
    @Override
    public String endpoint() {
      return ENDPOINT;
    }

    @Nullable
    @Override
    public Class<?> requestBodyClazz() {
      return requestBodyClazz;
    }

    @NonNull
    @Override
    public List<String> pathParameters() {
      return pathParameters;
    }

    @NonNull
    @Override
    public List<String> queryParameters() {
      return queryParameters;
    }
  }
  //endregion Read
}
