package com.github.emw7.examples.updownstream.downstream.rest.common;

import com.github.emw7.examples.updownstream.downstream.api.SensorApi;
import com.github.emw7.examples.updownstream.downstream.common.model.Sensor;
import com.github.emw7.platform.protocol.rest.request.DeleteRestProtocolRequest;
import com.github.emw7.platform.protocol.rest.request.GetRestProtocolRequest;
import com.github.emw7.platform.protocol.rest.request.Parameter;
import com.github.emw7.platform.protocol.rest.request.PostRestProtocolRequest;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public final class SensorApiRest {

  private static final String SENSOR_ENDPOINT = "/sensor";

  //region Create
  public static final class Create {

    public static final HttpMethod METHOD = HttpMethod.POST;
    public static final String ENDPOINT = SENSOR_ENDPOINT;

    public static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON;
    public static final MediaType[] ACCEPTABLE_MEDIA_TYPES = new MediaType[]{MediaType.APPLICATION_JSON};
//    public static final Class<Sensor> requestBodyClazz = Sensor.class;
//    public static final java.util.List<String> pathParameters = List.of();
//    public static final java.util.List<String> queryParameters = List.of();

    public static final Class<SensorApi.CreateSensorResponse> responseClazz = SensorApi.CreateSensorResponse.class;

    public static PostRestProtocolRequest.Builder<Sensor> protocolRequestBuilder() {
      return PostRestProtocolRequest.<Sensor>builder().contentType(CONTENT_TYPE)
          .acceptableMediaTypes(ACCEPTABLE_MEDIA_TYPES);
    }
  }
  //endregion Create

  //region Delete
  public static final class Delete {

    public static final HttpMethod METHOD = HttpMethod.DELETE;
    public static final String ENDPOINT = SENSOR_ENDPOINT + '/' + "{code}";
    public static final MediaType[] ACCEPTABLE_MEDIA_TYPES = new MediaType[]{MediaType.APPLICATION_JSON};

    //region Path parameters
    public static final String P_N_CODE= "code";
    private static final Map<String, Parameter<?>> PATH_PARAMETERS= Map.of(
        P_N_CODE, Parameter.of(P_N_CODE)
    );
    //endregion Path paramters
//    public enum PathParameterDefinitions implements ParameterDefinition {
//
//      CODE(Parameter.<String>of("code"));
//
//      public static final Map<String, Parameter<?>> PARAMETERS;
//
//      static {
//        PARAMETERS = ParameterDefinition.mapValues(PathParameterDefinitions.values());
//      }
//
//      private final Parameter<?> parameter;
//
//      PathParameterDefinitions(@NonNull final Parameter<?> parameter) {
//        this.parameter= parameter;
//      }
//
//      @Override
//      public <T> Parameter<T> parameter () {
//        //noinspection unchecked
//        return (Parameter<T>)parameter;
//      }
//    }

    public static DeleteRestProtocolRequest.Builder protocolRequestBuilder() {
      return DeleteRestProtocolRequest.builder(PATH_PARAMETERS, null)
          .acceptableMediaTypes(ACCEPTABLE_MEDIA_TYPES);
    }

  }


  //endregion Delete

  //region Read
  public static final class Read {

    public static final HttpMethod METHOD = HttpMethod.GET;
    public static final String ENDPOINT = SENSOR_ENDPOINT + "/{code}:read";

    private static final MediaType[] ACCEPTABLE_MEDIA = new MediaType[]{MediaType.APPLICATION_JSON};

    //region Path parameters
    public static final String P_N_CODE = "code";
    private static final Map<String, Parameter<?>> PATH_PARAMETERS= Map.of(
        P_N_CODE, Parameter.of(P_N_CODE)
    );
    //endregion Path parameters

    //region Query parameters
    public static final String Q_N_RESPOND_WITH_ERROR = "respondWithError";
    public static final String Q_V_RESPOND_WITH_ERROR = "false";
    private static final Map<String, Parameter<?>> QUERY_PARAMETERS= Map.of(
        Q_N_RESPOND_WITH_ERROR, Parameter.of(Q_N_RESPOND_WITH_ERROR, Q_V_RESPOND_WITH_ERROR)
    );
    //endregion Query parameters

    // Request builder
    public static GetRestProtocolRequest.Builder protocolRequestBuilder() {
      return GetRestProtocolRequest.builder(PATH_PARAMETERS, QUERY_PARAMETERS)
          .acceptableMediaTypes(ACCEPTABLE_MEDIA);
    }

  }
  //endregion Read
}
