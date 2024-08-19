package com.github.emw7.platform.protocol.rest.request;

import com.github.emw7.platform.protocol.rest.request.PostRestProtocolRequest.Builder;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public abstract non-sealed class PostRestProtocolRequest<B> extends AbstractRestProtocolRequest<B> {

  //region Private static final properties
  private static final HttpMethod HTTP_METHOD = HttpMethod.POST;
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

    public PostRestProtocolRequest.Builder<B> httpHeaders(HttpHeaders httpHeaders) {
      this.httpHeaders = httpHeaders;
      return this;
    }

    public PostRestProtocolRequest.Builder<B> acceptableMediaType (@NonNull final MediaType... mediaTypes) {
      this.acceptableMediaTypes= List.of(mediaTypes);
      return this;
    }

    public PostRestProtocolRequest.Builder<B> contentType (@NonNull final MediaType contentType) {
      this.contentType= contentType;
      return this;
    }

    public PostRestProtocolRequest.Builder<B> pathParams(Map<String, Object> pathParams) {
      this.pathParams = pathParams;
      return this;
    }

    public PostRestProtocolRequest.Builder<B> body(B body) {
      this.body = body;
      return this;
    }

    public PostRestProtocolRequest<B> build() {
      //noinspection ConstantConditions
      return new PostRestProtocolRequest<>(httpHeaders, acceptableMediaTypes, contentType, pathParams, body) {};
    }
  }

  public static <B> PostRestProtocolRequest.Builder<B> builder() {
    return new PostRestProtocolRequest.Builder<B>();
  }
  //endregion Builder

  //region Constructors
  private PostRestProtocolRequest(final HttpHeaders httpHeaders, @NonNull final List<MediaType> acceptableMediaTypes,
      @Nullable final MediaType contentType,
      final Map<String, Object> pathParams,
      final B body) {
    super(HTTP_METHOD, httpHeaders, acceptableMediaTypes, contentType, pathParams, null, null, body);
  }
  //endregion Constructors

}
