package com.github.emw7.platform.protocol.rest.request;

import com.github.emw7.platform.protocol.api.ProtocolRequest;
import java.util.List;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public interface RestProtocolRequest<B> extends ProtocolRequest<B> {

  @NonNull
  HttpMethod getHttpMethod();

  @NonNull
  HttpHeaders getHttpHeaders();

  @NonNull
  List<MediaType> acceptableMediaTypes ();

  /**
   * {@code @Nullable} because not all the methods have the body and thus the content type.
   *
   * @return the type of the body; {@code null} is the method does not accept body
   */
  @Nullable
  MediaType contentType ();

  @NonNull
  Map<String, Object> getPathParams();

  @NonNull
  MultiValueMap<String, String> getQueryParams();

  @NonNull
  Pageable getPageable();

  @Nullable
  B getBody();

}
