package com.github.emw7.examples.updownstream.upstream.rest.common;

import com.github.emw7.platform.protocol.rest.request.Parameter;
import java.util.Arrays;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

public final class XApiRest {

  private static final String BASE_ENDPOINT = "/x";

  //region Create
  public static final class Create {

    public static final String ENDPOINT = BASE_ENDPOINT;

  }
  //endregion Create

  //region Delete
  public static final class Delete {

    public static final String ENDPOINT = BASE_ENDPOINT + '/' + "{code}";

    //region Path parameters
    public static final String P_N_CODE = "code";
    //endregion Path parameters

  }
  //endregion Delete

  //region Read
  public static final class Read {

    public static final String ENDPOINT = BASE_ENDPOINT + "/{code}:read";

    public static final String PRODUCES = MediaType.APPLICATION_JSON_VALUE;

    //region Path parameters
    public static final String P_N_CODE = "code";
    //endregion Path parameters

    //region Query parameters
    public static final String Q_N_RESPOND_WITH_ERROR = "respondWithError";
    public static final String Q_V_RESPOND_WITH_ERROR = "false";
    //endregion Query parameters

  }
  //endregion Read
}
