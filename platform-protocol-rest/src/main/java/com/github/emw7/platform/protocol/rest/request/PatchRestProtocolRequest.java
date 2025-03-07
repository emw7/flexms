package com.github.emw7.platform.protocol.rest.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public final class PatchRestProtocolRequest<B> extends AbstractRestProtocolRequest<B> {

  //region Private static final properties
  private static final HttpMethod HTTP_METHOD = HttpMethod.PATCH;
  //endregion Private static final properties

  //region Builder
  public static class Builder<B> extends AbstractRestProtocolRequest.Builder<B, PatchRestProtocolRequest<B>> {

    private final HttpHeaders httpHeaders;

    private final List<MediaType> acceptableMediaTypes;

    private MediaType contentType;
    private B body;

    private Builder() {
      this(null, null);
    }

    private Builder(@Nullable final Map<String, Parameter<?>> expectedPathParameters,
        @Nullable final Map<String, Parameter<?>> expectedQueryParameters) {
      super(expectedPathParameters, expectedQueryParameters);

      this.httpHeaders = new HttpHeaders();
      this.acceptableMediaTypes = new ArrayList<>();

      this.contentType= null;
      this.body= null;
    }

    public PatchRestProtocolRequest.Builder<B> httpHeader(@NonNull final String name,
        @NonNull final String... values) {
      this.httpHeaders.addAll(name, Arrays.stream(values).toList());
      return this;
    }

    public PatchRestProtocolRequest.Builder<B> acceptableMediaTypes(
        @NonNull final MediaType... mediaType) {
      this.acceptableMediaTypes.addAll(Arrays.stream(mediaType).toList());
      return this;
    }

    public PatchRestProtocolRequest.Builder<B> contentType (@NonNull final MediaType contentType) {
      this.contentType= contentType;
      return this;
    }

    public PatchRestProtocolRequest.Builder<B> body(B body) {
      this.body = body;
      return this;
    }

    @Override
    protected PatchRestProtocolRequest<B> _build() {
      return new PatchRestProtocolRequest<>(httpHeaders, acceptableMediaTypes, pathParams,
          queryParams, contentType, body);
    }

    @Override
    protected void _check() throws RuntimeException {
      if ( body != null && contentType == null ) {
        // TODO I18nRuntimeException???
        throw new RuntimeException("Content type must be set when body is set");
      }
    }

  }

  public static <B> PatchRestProtocolRequest.Builder<B> builder() {
    return builder(null, null);
  }

  public static <B> PatchRestProtocolRequest.Builder<B> builder(
      @Nullable final Map<String, Parameter<?>> expectedPathParameters,
      @Nullable final Map<String, Parameter<?>> expectedQueryParameters) {
    return new PatchRestProtocolRequest.Builder<>(expectedPathParameters, expectedQueryParameters);
  }
  //endregion Builder

  //region Constructors
  private PatchRestProtocolRequest(@NonNull final HttpHeaders httpHeaders,
      @NonNull final List<MediaType> acceptableMediaTypes,
      @NonNull final Map<String, Object> pathParams,
      @NonNull final MultiValueMap<String, String> queryParams,
      @Nullable final MediaType contentType,
      @Nullable final B body) {
    super(HTTP_METHOD, httpHeaders, acceptableMediaTypes, contentType, pathParams, queryParams, null, body);
  }
  //endregion Constructors

}
