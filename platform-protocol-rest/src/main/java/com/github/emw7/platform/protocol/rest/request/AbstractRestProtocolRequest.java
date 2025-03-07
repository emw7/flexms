package com.github.emw7.platform.protocol.rest.request;

import com.github.emw7.platform.core.CoreConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

public abstract sealed class AbstractRestProtocolRequest<B> implements
    RestProtocolRequest<B> permits GetRestProtocolRequest, PostRestProtocolRequest,
    PutRestProtocolRequest, PatchRestProtocolRequest, DeleteRestProtocolRequest {

  //region Builder
  public static abstract class Builder<B, I extends AbstractRestProtocolRequest<B>> {

    //region Private static final properties
    private static final Logger log = LoggerFactory.getLogger(
        AbstractRestProtocolRequest.Builder.class);
    //endregion Private static final properties

    //region Private final properties
    //region Support
    private final Map<String, Parameter<?>> expectedPathParameters;
    private final Map<String, Parameter<?>> expectedQueryParameters;
    //endregion Support
    //endregion Private final properties

    //region Protected final properties
    protected final Map<String, Object> pathParams;
    protected final MultiValueMap<String, String> queryParams;
    //region Protected final properties

    private Builder() {
      this(null, null);
    }

    protected Builder(@Nullable final Map<String, Parameter<?>> expectedPathParameters,
        @Nullable final Map<String, Parameter<?>> expectedQueryParameters) {
      this.expectedPathParameters = Optional.ofNullable(expectedPathParameters).orElseGet(Map::of);
      this.expectedQueryParameters = Optional.ofNullable(expectedQueryParameters)
          .orElseGet(Map::of);

      this.pathParams = new HashMap<>(this.expectedPathParameters.size());
      this.queryParams = new LinkedMultiValueMap<>(this.expectedQueryParameters.size());
    }

    //region API
    public Builder<B, I> pathParam(@NonNull final String name, @NonNull final String value) {
      this.pathParams.put(name, value);
      return this;
    }

    public Builder<B, I> queryParam(@NonNull final String name, @NonNull final String... values) {
      // Not thread safe because who known whether addAll is thread safe!
      queryParams.addAll(name, Arrays.stream(values).toList());
      return this;
    }
    //endregion API

    public final I build() {
      check();
      return _build();
    }

    protected abstract I _build();

    /**
     * To be implemented in case child builder must do check in addition the one {@link #check()}
     * does.
     * <p>
     * Called only if {@link #check()} passes.
     *
     * @throws RuntimeException in case of checks do not pass
     */
    protected void _check() throws RuntimeException {
    }

    /**
     * Checks if request can be built consistently.
     * <p>
     * Checks are performed only if {@code log.isDebugEnabled()} is {@code true}.
     * <p>
     * If checks passes then {@link #_check()} is invoked: there is where children can do theirs
     * addition checks.
     * <p>
     * Checks are:
     * <ul>
     * <li>{@code path parameters} and {@code query parameters} are consistent with
     * the expected and mandatory ones; that is: mandatory parameters are all specified and no
     * extra parameters are present</li>
     * </ul>
     */
    private void check() {
      if (!log.isDebugEnabled()) {
        return;
      }
      //else...

      // Checks path parameters.
      final List<String> missingPathParameters = new ArrayList<>(expectedPathParameters.size());
      final Set<String> extraPathParameters = new HashSet<>(pathParams.keySet());
      expectedPathParameters.values().forEach(parameter -> {
        if ( !pathParams.containsKey(parameter.getName()) ) {
          if ( parameter.isMandatory() ) {
            missingPathParameters.add(parameter.getName());
          }
          else {
            // has default
            pathParam(parameter.getName(), parameter.getDefaultValue() == null ? CoreConstants.NULL_STRING_REPRESENTATION : parameter.getDefaultValue().toString());
          }
        }
//        // missing
//        if (parameter.isMandatory() && !pathParams.containsKey(parameter.getName())) {
//          missingPathParameters.add(parameter.getName());
//        }
        extraPathParameters.remove(parameter.getName());
      });

      // Checks query parameters.
      final List<String> missingQueryParameters = new ArrayList<>(expectedQueryParameters.size());
      final Set<String> extraQueryParameters = new HashSet<>(queryParams.keySet());
      expectedQueryParameters.values().forEach(parameter -> {
        if ( !queryParams.containsKey(parameter.getName()) ) {
          if ( parameter.isMandatory() ) {
            missingQueryParameters.add(parameter.getName());
          }
          else {
            // has default
            queryParam(parameter.getName(), parameter.getDefaultValue() == null ? CoreConstants.NULL_STRING_REPRESENTATION : parameter.getDefaultValue().toString());
          }
        }
//        // missing
//        if (parameter.isMandatory() && !queryParams.containsKey(parameter.getName())) {
//          missingQueryParameters.add(parameter.getName());
//        }
        extraQueryParameters.remove(parameter.getName());
      });

      if (!missingPathParameters.isEmpty() || !extraPathParameters.isEmpty()
          || !missingQueryParameters.isEmpty() || !extraQueryParameters.isEmpty()) {
        // TODO I18nRuntimeException???
        throw new RuntimeException(String.format(
            "Missing mandatory path parameters: %s, extra path parameters are present: %s;"
                + "Missing mandatory query parameters: %s, extra query parameters are present: %s",
            String.join(", ", missingPathParameters), String.join(", ", extraPathParameters),
            String.join(", ", missingQueryParameters), String.join(", ", extraQueryParameters)));
      } else {
        _check();
      }
    }
  }
  //endregion Builder

  private final HttpMethod httpMethod;

  private final HttpHeaders httpHeaders;

  private final List<MediaType> acceptableMediaTypes;

  private final MediaType contentType;

  private final Map<String, Object> pathParams;
  private final MultiValueMap<String, String> queryParams;

  private final Pagination pagination;

  private final B body;

  protected AbstractRestProtocolRequest(@NonNull final HttpMethod httpMethod,
      @Nullable final HttpHeaders httpHeaders,
      @NonNull final List<MediaType> acceptableMediaTypes,
      @Nullable final MediaType contentType,
      @Nullable final Map<String, Object> pathParams,
      @Nullable final MultiValueMap<String, String> queryParams,
      @Nullable final Pagination pagination,
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

    this.pagination = Optional.ofNullable(pagination).orElse(Pagination.builder().build());

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

  //  @Override
//  public final @NonNull Pageable getPageable() {
//    return pageable;
//  }
  @Override
  public final @NonNull Pagination getPagination() {
    return pagination;
  }

  @Override
  public final @Nullable B getBody() {
    return body;
  }

}
