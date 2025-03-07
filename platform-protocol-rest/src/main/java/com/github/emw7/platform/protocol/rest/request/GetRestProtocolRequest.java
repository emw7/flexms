package com.github.emw7.platform.protocol.rest.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public final class GetRestProtocolRequest extends AbstractRestProtocolRequest<Void> {

  //region Private static final properties
  private static final HttpMethod HTTP_METHOD = HttpMethod.GET;
  //endregion Private static final properties

  //region Builder

  // TODO builder forse deve essere gerarchico!!!

  /**
   * It is not thread safe!
   */
  public static final class Builder extends
      AbstractRestProtocolRequest.Builder<Void, GetRestProtocolRequest> {

//    //region Private static final properties
//    private static final Logger log = LoggerFactory.getLogger(
//        DeleteRestProtocolRequest.Builder.class);
//    //endregion Private static final properties
//
//    //region Support
//    private final Map<String, Parameter<?>> expectedPathParameters;
//    private final Map<String, Parameter<?>> expectedQueryParameters;
//    //endregion Support

    private final HttpHeaders httpHeaders;

    private final List<MediaType> acceptableMediaTypes;

    //    private final Map<String, Object> pathParams;
//    private final MultiValueMap<String, String> queryParams;
    private Pagination pagination;

    private Builder() {
      this(null, null);
    }

    private Builder(@Nullable final Map<String, Parameter<?>> expectedPathParameters,
        @Nullable final Map<String, Parameter<?>> expectedQueryParameters) {
      super(expectedPathParameters, expectedQueryParameters);

      this.httpHeaders = new HttpHeaders();
      this.acceptableMediaTypes = new ArrayList<>();
      this.pagination = Pagination.builder().build();
    }

    public GetRestProtocolRequest.Builder httpHeader(@NonNull final String name,
        @NonNull final String... values) {
      this.httpHeaders.addAll(name, Arrays.stream(values).toList());
      return this;
    }

    public GetRestProtocolRequest.Builder acceptableMediaTypes(
        @NonNull final MediaType... mediaType) {
      this.acceptableMediaTypes.addAll(Arrays.stream(mediaType).toList());
      return this;
    }

//    public GetRestProtocolRequest.Builder httpHeaders(HttpHeaders httpHeaders) {
//      this.httpHeaders = httpHeaders;
//      return this;
//    }
//
//    public GetRestProtocolRequest.Builder acceptableMediaType(
//        @NonNull final MediaType... mediaTypes) {
//      this.acceptableMediaTypes = List.of(mediaTypes);
//      return this;
//    }

    @Override
    protected GetRestProtocolRequest _build() {
      return new GetRestProtocolRequest(httpHeaders, acceptableMediaTypes, pathParams, queryParams);
    }

    //    public GetRestProtocolRequest.Builder pathParams(Map<String, Object> pathParams) {
//      this.pathParams.putAll(pathParams);
//      return this;
//    }
//
//    public GetRestProtocolRequest.Builder pathParam(@NonNull final String name,
//        @NonNull final String value) {
//      this.pathParams.put(name, value);
//      return this;
//    }

//    /**
//     * Not thread safe!.
//     *
//     * @param name
//     * @param values
//     * @return
//     */
//    public GetRestProtocolRequest.Builder queryParam(@NonNull final String name,
//        @NonNull final String... values) {
//      // Not thread safe because who known whether addAll is thread safe!
//      queryParams.addAll(name, Arrays.stream(values).toList());
//      return this;
//    }

//    public GetRestProtocolRequest build() {
//      check();
//      //noinspection ConstantConditions
//      return new GetRestProtocolRequest(httpHeaders, acceptableMediaTypes, pathParams,
//          queryParams) {
//      };
//    }

//    private void check() {
//      if (!log.isDebugEnabled()) {
//        return;
//      }
//      //else...
//      // Checks path parameters.
//      final List<String> missingPathParameters = new ArrayList<>(expectedPathParameters.size());
//      final Set<String> extraPathParameters = new HashSet<>(pathParams.keySet());
//      expectedPathParameters.values().forEach(parameter -> {
//        // missing
//        if (parameter.isMandatory() && !pathParams.containsKey(parameter.getName())) {
//          missingPathParameters.add(parameter.getName());
//        }
//        extraPathParameters.remove(parameter.getName());
//      });
//
//
//      // Checks query parameters.
//      final List<String> missingQueryParameters = new ArrayList<>(expectedQueryParameters.size());
//      final Set<String> extraQueryParameters = new HashSet<>(queryParams.keySet());
//      expectedQueryParameters.values().forEach(parameter -> {
//        // missing
//        if (parameter.isMandatory() && !queryParams.containsKey(parameter.getName())) {
//          missingQueryParameters.add(parameter.getName());
//        }
//        extraQueryParameters.remove(parameter.getName());
//      });
//      if (!missingPathParameters.isEmpty() || !extraPathParameters.isEmpty()
//          || !missingQueryParameters.isEmpty() || !extraQueryParameters.isEmpty()) {
//        // TODO move toward checked exception thrown by check and build in turn.
//        throw new RuntimeException(String.format(
//            "Missing mandatory path parameters: %s, extra path parameters are present: %s;"
//                + "Missing mandatory query parameters: %s, extra query parameters are present: %s",
//            String.join(", ", missingPathParameters), String.join(", ", extraPathParameters),
//            String.join(", ", missingQueryParameters), String.join(", ", extraQueryParameters)));
//      }
//    }
  }

  public static GetRestProtocolRequest.Builder builder() {
    return builder(null, null);
  }

  public static GetRestProtocolRequest.Builder builder(
      @Nullable final Map<String, Parameter<?>> expectedPathParameters,
      @Nullable final Map<String, Parameter<?>> expectedQueryParameters) {
    return new GetRestProtocolRequest.Builder(expectedPathParameters, expectedQueryParameters);
  }
  //endregion Builder

  //region Constructors
  private GetRestProtocolRequest(final HttpHeaders httpHeaders,
      @NonNull final List<MediaType> acceptableMediaTypes, final Map<String, Object> pathParams,
      final MultiValueMap<String, String> queryParams) {
    // TODO mancano un sacco di parametri!!!
    super(HTTP_METHOD, httpHeaders, acceptableMediaTypes, null, pathParams, queryParams, null,
        null);
  }
  //endregion Constructors

}
