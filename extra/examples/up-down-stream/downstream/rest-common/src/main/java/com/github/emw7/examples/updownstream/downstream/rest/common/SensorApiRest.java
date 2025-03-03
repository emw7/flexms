package com.github.emw7.examples.updownstream.downstream.rest.common;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public final class SensorApiRest {

  private static final String SENSOR_ENDPOINT = "/sensor";

  //region Create
  public static final class Create {

    public static final HttpMethod METHOD = HttpMethod.POST;
    public static final String ENDPOINT = SENSOR_ENDPOINT;

    public static final MediaType CONTENT_TYPE= MediaType.APPLICATION_JSON;
//    public static final Class<Sensor> requestBodyClazz = Sensor.class;
//    public static final java.util.List<String> pathParameters = List.of();
//    public static final java.util.List<String> queryParameters = List.of();
  }
  //endregion Create

  //region Delete
  public static final class Delete {

    public static final HttpMethod METHOD = HttpMethod.DELETE;
    public static final String ENDPOINT = SENSOR_ENDPOINT + '/' + "{code}";
//    public static final Class<?> requestBodyClazz = null;
//    public static final java.util.List<String> pathParameters = List.of("code");
//    public static final java.util.List<String> queryParameters = List.of();

  }
  //endregion Delete

  //region Read
  public static final class Read {

    public static final HttpMethod METHOD = HttpMethod.GET;
    public static final String ENDPOINT = SENSOR_ENDPOINT + ":read";
//    public static final Class<?> requestBodyClazz = null;
//    public static final List<String> pathParameters = List.of();
//    public static final List<String> queryParameters = List.of();

  }
  //endregion Read
}
