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

public final class DeleteRestProtocolRequest extends AbstractRestProtocolRequest<Void> {

  //region Private static final properties
  private static final HttpMethod HTTP_METHOD = HttpMethod.DELETE;
  //endregion Private static final properties

  //region Builder
  public static class Builder extends
      AbstractRestProtocolRequest.Builder<Void, DeleteRestProtocolRequest> {

    private final HttpHeaders httpHeaders;

    private final List<MediaType> acceptableMediaTypes;

    private Builder() {
      this(null, null);
    }

    private Builder(@Nullable final Map<String, Parameter<?>> expectedPathParameters,
        @Nullable final Map<String, Parameter<?>> expectedQueryParameters) {
      super(expectedPathParameters, expectedQueryParameters);

      this.httpHeaders = new HttpHeaders();
      this.acceptableMediaTypes = new ArrayList<>();
    }

    public DeleteRestProtocolRequest.Builder httpHeader(@NonNull final String name,
        @NonNull final String... values) {
      this.httpHeaders.addAll(name, Arrays.stream(values).toList());
      return this;
    }

    public DeleteRestProtocolRequest.Builder acceptableMediaTypes(
        @NonNull final MediaType... mediaType) {
      this.acceptableMediaTypes.addAll(Arrays.stream(mediaType).toList());
      return this;
    }

    @Override
    protected DeleteRestProtocolRequest _build() {
      return new DeleteRestProtocolRequest(httpHeaders, acceptableMediaTypes, pathParams,
          queryParams);
    }
  }

  public static DeleteRestProtocolRequest.Builder builder() {
    return builder(null, null);
  }

  public static DeleteRestProtocolRequest.Builder builder(
      @Nullable final Map<String, Parameter<?>> expectedPathParameters,
      @Nullable final Map<String, Parameter<?>> expectedQueryParameters) {
    return new DeleteRestProtocolRequest.Builder(expectedPathParameters, expectedQueryParameters);
  }
  //endregion Builder

  //region Constructors
  private DeleteRestProtocolRequest(@NonNull final HttpHeaders httpHeaders,
      @NonNull final List<MediaType> acceptableMediaTypes,
      @NonNull final Map<String, Object> pathParams,
      @NonNull final MultiValueMap<String, String> queryParams) {
    super(HTTP_METHOD, httpHeaders, acceptableMediaTypes, null, pathParams, queryParams, null,
        null);
  }
  //endregion Constructors

}
