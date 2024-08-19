package com.github.emw7.platform.protocol.rest.request;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public abstract non-sealed class PutRestProtocolRequest<B> extends AbstractRestProtocolRequest<B> {

  //region Private static final properties
  private static final HttpMethod HTTP_METHOD = HttpMethod.PUT;
  //endregion Private static final properties

  //region Builder
  public static class Builder<B> {

    private HttpHeaders httpHeaders;

    private List<MediaType> acceptableMediaTypes= List.of(MediaType.APPLICATION_JSON);
    private MediaType contentType;

    private Map<String, Object> pathParams;
    // PUT has not query parameters.
    private final MultiValueMap<String, String> queryParams = null;
    // PUT cannot be pageable.
    private final Pageable pageable = null;
    private B body;

    public PutRestProtocolRequest.Builder<B> httpHeaders(HttpHeaders httpHeaders) {
      this.httpHeaders = httpHeaders;
      return this;
    }

    public PutRestProtocolRequest.Builder<B> acceptableMediaType (@NonNull final MediaType... mediaTypes) {
      this.acceptableMediaTypes= List.of(mediaTypes);
      return this;
    }

    public PutRestProtocolRequest.Builder<B> contentType (@NonNull final MediaType contentType) {
      this.contentType= contentType;
      return this;
    }

    public PutRestProtocolRequest.Builder<B> pathParams(Map<String, Object> pathParams) {
      this.pathParams = pathParams;
      return this;
    }

    public PutRestProtocolRequest.Builder<B> body(B body) {
      this.body = body;
      return this;
    }

    public PutRestProtocolRequest<B> build() {
      //noinspection ConstantConditions
      return new PutRestProtocolRequest<>(httpHeaders, acceptableMediaTypes, contentType, pathParams, body) {};
    }
  }

  public static <B> PutRestProtocolRequest.Builder<B> builder() {
    return new PutRestProtocolRequest.Builder<B>();
  }
  //endregion Builder

  //region Constructors
  private PutRestProtocolRequest(final HttpHeaders httpHeaders, @NonNull final List<MediaType> acceptableMediaTypes,
      @Nullable final MediaType contentType,
      final Map<String, Object> pathParams,
      final B body) {
    super(HTTP_METHOD, httpHeaders, acceptableMediaTypes, contentType, pathParams, null, null, body);
  }
  //endregion Constructors

}
