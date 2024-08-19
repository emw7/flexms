package com.github.emw7.platform.protocol.rest.request;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.MultiValueMap;

public abstract non-sealed class DeleteRestProtocolRequest extends AbstractRestProtocolRequest<Void> {

  //region Private static final properties
  private static final HttpMethod HTTP_METHOD = HttpMethod.DELETE;
  //endregion Private static final properties

  //region Builder
  public static class Builder {

    private HttpHeaders httpHeaders;

    private List<MediaType> acceptableMediaTypes= List.of(MediaType.APPLICATION_JSON);

    private Map<String, Object> pathParams;
    // PUT has not query parameters.
    private final MultiValueMap<String, String> queryParams = null;
    // PUT cannot be pageable.
    private final Pageable pageable = null;

    public DeleteRestProtocolRequest.Builder httpHeaders(HttpHeaders httpHeaders) {
      this.httpHeaders = httpHeaders;
      return this;
    }

    public DeleteRestProtocolRequest.Builder acceptableMediaType (@NonNull final MediaType... mediaTypes) {
      this.acceptableMediaTypes= List.of(mediaTypes);
      return this;
    }

    public DeleteRestProtocolRequest.Builder pathParams(Map<String, Object> pathParams) {
      this.pathParams = pathParams;
      return this;
    }

    public DeleteRestProtocolRequest build() {
      //noinspection ConstantConditions
      return new DeleteRestProtocolRequest(httpHeaders, acceptableMediaTypes, pathParams) {};
    }
  }

  public static  DeleteRestProtocolRequest.Builder builder() {
    return new DeleteRestProtocolRequest.Builder();
  }
  //endregion Builder

  //region Constructors
  private DeleteRestProtocolRequest(final HttpHeaders httpHeaders, @NonNull final List<MediaType> acceptableMediaTypes,
      final Map<String, Object> pathParams) {
    super(HTTP_METHOD, httpHeaders, acceptableMediaTypes, null, pathParams, null, null, null);
  }
  //endregion Constructors

}
