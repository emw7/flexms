package com.github.emw7.platform.protocol.rest.request;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

public abstract sealed class AbstractRestProtocolRequest<B> implements
    RestProtocolRequest<B> permits GetRestProtocolRequest, PostRestProtocolRequest,
    PutRestProtocolRequest, PatchRestProtocolRequest, DeleteRestProtocolRequest {

  private final HttpMethod httpMethod;

  private final HttpHeaders httpHeaders;

  private final List<MediaType> acceptableMediaTypes;

  private final MediaType contentType;

  private final Map<String, Object> pathParams;
  private final MultiValueMap<String, String> queryParams;

  private final Pageable pageable;

  private final B body;

  protected AbstractRestProtocolRequest(@NonNull final HttpMethod httpMethod,
      @Nullable final HttpHeaders httpHeaders, @NonNull final List<MediaType> acceptableMediaTypes,
      @Nullable final MediaType contentType, @Nullable final Map<String, Object> pathParams,
      @Nullable final MultiValueMap<String, String> queryParams, @Nullable final Pageable pageable,
      @Nullable final B body) {

    this.httpMethod = httpMethod;

    this.httpHeaders = Optional.ofNullable(httpHeaders).orElse(HttpHeaders.EMPTY);

    this.acceptableMediaTypes = acceptableMediaTypes;

    this.contentType = contentType;

    // Using Map.of() that is unmodifiable instead of new HashMap<>() because unmodifiable what is
    //  wanted! [A].
    this.pathParams = Optional.ofNullable(pathParams).orElse(Map.of());
    // [A].
    this.queryParams = Optional.ofNullable(queryParams)
        .orElse(new MultiValueMapAdapter<>(Map.of()));

    this.pageable = Optional.ofNullable(pageable).orElse(Pageable.unpaged());

    this.body = body;
  }

  @Override
  public final @NonNull HttpMethod getHttpMethod() {
    return httpMethod;
  }

  @Override
  public final @NonNull HttpHeaders getHttpHeaders() {
    return httpHeaders;
  }

  @Override
  public final @NonNull List<MediaType> acceptableMediaTypes() {
    return acceptableMediaTypes;
  }

  @Override
  public final @Nullable MediaType contentType() {
    return contentType;
  }

  @Override
  public final @NonNull Map<String, Object> getPathParams() {
    return pathParams;
  }

  @Override
  public final @NonNull MultiValueMap<String, String> getQueryParams() {
    return queryParams;
  }

  @Override
  public final @NonNull Pageable getPageable() {
    return pageable;
  }

  @Override
  public final @Nullable B getBody() {
    return body;
  }

}
