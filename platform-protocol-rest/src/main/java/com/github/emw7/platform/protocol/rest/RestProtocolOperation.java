package com.github.emw7.platform.protocol.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.emw7.platform.auth.api.token.AuthToken;
import com.github.emw7.platform.discovery.api.model.Server;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.protocol.api.ProtocolRequest;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.ServerRestDependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.ClientRestDependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.RestUnknownDependencyErrorException;
import com.github.emw7.platform.protocol.rest.request.RestProtocolRequest;
import com.github.emw7.platform.rest.core.PlatformRestConstants;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Implementations of {@link ProtocolTemplate} that speaks REST (HTTP) language (protocol) through
 * {@link RestTemplate}.
 */
public final class RestProtocolOperation implements ProtocolTemplate {

  //region Private static final properties
  private static final Logger log = LoggerFactory.getLogger(RestProtocolOperation.class);
  //endregion Private static final properties

  // FIXME EM phonebook: keep here or move or hardcoding? Remember that these are spring
  //  dependant so it should be done something like SpringPageableQueryParamsExpander but
  //  Pageable itself is a spring artefact... so it should be removed the dependency from
  //  Pageable too... what a mess... is it worth? I think it isn't!
  private static final class PageableQueryParameterNames {

    private static final String PAGE_NUMBER = "page";
    private static final String PAGE_SIZE = "size";
    private static final String SORT = "sort";
    private static final String SORT_PROPERTY_DIRETION_SEP = ",";
  }

  //region Private properties
  private final RestTemplate restTemplate;

  private final ObjectReader objectReader;
  //endregion Private properties

  //region Constructors

  /**
   * Constructs the object by setting {@code this.restTemplate= restTemplateBuilder.build()}.
   *
   * @param restTemplateBuilder builder for {@link #restTemplate}
   * @see #restTemplate
   */
  public RestProtocolOperation(@NonNull final RestTemplateBuilder restTemplateBuilder,
      @NonNull final ObjectMapper objectMapper) {
    this.restTemplate = restTemplateBuilder.build();
    this.objectReader = objectMapper.reader()
        .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }
  //endregion Constructors

  //region Private types

  //region Exchanger

  //region Base exchanger
  private static abstract sealed class Exchanger<T, B> implements
      Function<RequestEntity<B>, T> permits ClassExchanger, ParameterizedTypeReferenceExchanger {

    protected final RestTemplate restTemplate;
    //protected final ProtocolRequest<B> protocolRequest;

    private Exchanger(final RestTemplate restTemplate/*,
        final ProtocolRequest<B> protocolRequest*/) {
      this.restTemplate = restTemplate;
      //this.protocolRequest = protocolRequest;
    }

    @Override
    public final T apply(final RequestEntity<B> requestEntity) throws RestClientException {
      return _exchange(requestEntity);
    }

    protected abstract T _exchange(final RequestEntity<B> requestEntity) throws RestClientException;
  }
  //endregion Base exchanger

  //region ClassExchanger
  private final class ClassExchanger<T, B> extends Exchanger<T, B> {

    private final Class<T> responseType;

    private ClassExchanger(final RestTemplate restTemplate,
        /*final ProtocolRequest<B> protocolRequest,*/
        final Class<T> responseType) {
      super(restTemplate/*, protocolRequest*/);
      this.responseType = responseType;
    }

    @Override
    protected final T _exchange(final RequestEntity<B> requestEntity) throws RestClientException {
      ResponseEntity<T> response = restTemplate.exchange(requestEntity, responseType);
      return response.getBody();
    }
  }
  //endregion ClassExchanger

  //region ParameterizedTypeReferenceExchanger
  private final class ParameterizedTypeReferenceExchanger<T, B> extends Exchanger<T, B> {

    private final ParameterizedTypeReference<T> responseType;

    private ParameterizedTypeReferenceExchanger(final RestTemplate restTemplate,
        /*final ProtocolRequest<B> protocolRequest,*/
        final ParameterizedTypeReference<T> responseType) {
      super(restTemplate/*, protocolRequest*/);
      this.responseType = responseType;
    }

    @Override
    protected final T _exchange(final RequestEntity<B> requestEntity) throws RestClientException {
      ResponseEntity<T> response = restTemplate.exchange(requestEntity, responseType);
      return response.getBody();
    }
  }
  //endregion ParameterizedTypeReferenceExchanger

  //endregion Exchanger

  //endregion Private types

  //region API

  // TODO [DOC]

  /**
   * Contacts the specified server and returns the response.
   *
   * <pre>
   * Flow:
   *   -> exchange(...,ClassExchanger)
   * </pre>
   *
   * @param server          server to contact
   * @param callerId        the id of who is doing the call
   * @param protocolRequest the request payload
   * @param responseType    class of the type of the response
   * @param token
   * @return the response
   * @throws DependencyErrorException if error is returned by contacted server of in case error
   *                                  occurs in contacting the server
   */
  @Override
  public final <T, B> T exchange(final Server server, final String endpoint, final String callerId,
      final ProtocolRequest<B> protocolRequest, final Class<T> responseType, final AuthToken token)
      throws DependencyErrorException {
//
//    // TODO throw programming error if cast fails.
//    // cast to protocol specific request type.
//    final RestProtocolRequest<B> restProtocolRequest;
//    try {
//      restProtocolRequest = (RestProtocolRequest<B>) protocolRequest;
//    } catch ( ClassCastException e ) {
//      throw new RuntimeException(e);
//    }
//
//    final RequestEntity<B> requestEntity = buildRequestEntity(server, endpoint, callerId,
//        protocolRequest);
//    T response = exchange(() -> restTemplate.exchange(requestEntity, responseType),
//        restProtocolRequest);
//    return response;
    return exchange(server, endpoint, callerId, protocolRequest, token,
        new RestProtocolOperation.ClassExchanger<>(restTemplate/*, protocolRequest*/,
            responseType));
  }

  // TODO [DOC]

  /**
   * Contacts the specified server and returns the response.
   *
   * <pre>
   * Flow:
   *   -> exchange(...,ParameterizedTypeReferenceExchanger)
   * </pre>
   *
   * @param server          server to contact
   * @param callerId        the id of who is doing the call
   * @param protocolRequest the request payload
   * @param responseType    type of the response
   * @param token
   * @return the response
   * @throws DependencyErrorException if error is returned by contacted server of in case error
   *                                  occurs in contacting the server
   */
  @Override
  public final <T, B> T exchange(final Server server, final String endpoint, final String callerId,
      final ProtocolRequest<B> protocolRequest, final ParameterizedTypeReference<T> responseType,
      final AuthToken token) throws DependencyErrorException {
//
//    // cast to protocol specific request type.
//    final RestProtocolRequest<B> restProtocolRequest;
//    try {
//      restProtocolRequest = (RestProtocolRequest<B>) protocolRequest;
//    } catch ( ClassCastException e ) {
//      throw new RuntimeException(e);
//    }
//
//    final RequestEntity<B> requestEntity = buildRequestEntity(server, endpoint, callerId,
//        restProtocolRequest);
//
//    T response = exchange(() -> restTemplate.exchange(requestEntity, responseType),
//        restProtocolRequest);
//    return response;
    return exchange(server, endpoint, callerId, protocolRequest, token,
        new RestProtocolOperation.ParameterizedTypeReferenceExchanger<>(
            restTemplate/*, protocolRequest*/, responseType));
  }
  //endregion API

  //region Private methods
  // TODO [DOC]

  /**
   * <pre>
   * Flow:
   *   cast protocolRequest to RestProtocolRequest => error throw ProgrammingErrorException
   *   requestEntity <- build (spring) request entity (buildRequestEntity)
   *   return exchanger(requestEntity) => error throw error mapping to AbstractRestDependencyErrorException (manageException)
   *
   *   buildRequestEntity
   *     url <- build url to call using server, endpoint and RestProtocolRequest
   *     request entity is built by:
   *       setting method and url;
   *       setting headers (buildHttpHeaders);
   *       setting request body (that is null in case method does not accept body);
   *
   *     buildHttpHeaders
   *       (build) headers from request
   *       (build) headers for method
   *       (build) standard headers
   *
   * // FIXME EM phonebook: verify how body (restProtocolRequest.getBody()) is serialized;
   * //  if serialization is fine the removed the objectWriter and all its references (including
   * //  the commented out too); [B] moreover MediaType.APPLICATION_JSON hard-coded here should be
   * //  passed by caller as it could be that the request is not a json, but a file, for example:
   * //  tutto molto bello... ma qui stiamo assumendo che tutti parlino json... ma è davvero così?
   * //  forse deve essere il chiamante a definirlo...
   * //final String bodyAsString= serializeBody(restProtocolRequest.getBody());
   * final RequestEntity<B> requestEntity = customizeRequestEntityForHttpMethod(
   * RequestEntity.method(restProtocolRequest.getHttpMethod(), url)
   * .headers(buildHttpHeaders(restProtocolRequest.getHttpHeaders(), callerId))
   * .accept(MediaType.APPLICATION_JSON), restProtocolRequest).body(
   * restProtocolRequest.getBody());
   *
   * return requestEntity;
   *
   * </pre>
   *
   * @param server
   * @param endpoint
   * @param callerId
   * @param protocolRequest
   * @param token
   * @param exchanger
   * @param <T>
   * @param <B>
   * @return
   * @throws DependencyErrorException
   */
  private final <T, B> T exchange(final Server server, final String endpoint, final String callerId,
      final ProtocolRequest<B> protocolRequest, final AuthToken token,
      @NonNull final Exchanger<T, B> exchanger) throws DependencyErrorException {

    // TODO throw programming error if cast fails.
    // cast to protocol specific request type.
    final RestProtocolRequest<B> restProtocolRequest;
    try {
      restProtocolRequest = (RestProtocolRequest<B>) protocolRequest;
    } catch (ClassCastException e) {
      // TODO programming error exception?
      throw new RuntimeException(e);
    }

    try {
      final RequestEntity<B> requestEntity = buildRequestEntity(server, endpoint, callerId,
          restProtocolRequest);
      return exchanger.apply(requestEntity);
    } catch (RestClientException e) {
      //noinspection resource
      EventLogger.notice(log).level(Level.ERROR)
          .pattern("rest request failed; method: {}, error: {}")
          .params(restProtocolRequest.getHttpMethod(), e.getMessage()).log();
      throw manageException(e, callerId, server);
    }
  }

  private <T, B> RequestEntity<B> buildRequestEntity(@NonNull final Server server,
      @NonNull final String endpoint, @NonNull final String callerId,
      @NonNull final RestProtocolRequest<B> restProtocolRequest) {

    final String url = buildUrl(server, endpoint, restProtocolRequest);

    // FIXME EM phonebook: verify how body (restProtocolRequest.getBody()) is serialized;
    //  if serialization is fine the removed the objectWriter and all its references (including
    //  the commented out too); [B] moreover MediaType.APPLICATION_JSON hard-coded here should be
    //  passed by caller as it could be that the request is not a json, but a file, for example:
    //  tutto molto bello... ma qui stiamo assumendo che tutti parlino json... ma è davvero così?
    //  forse deve essere il chiamante a definirlo...
    //final String bodyAsString= serializeBody(restProtocolRequest.getBody());
//    final RequestEntity<B> requestEntity = RequestEntity.method(restProtocolRequest.getHttpMethod(),
//            url).headers(buildHttpHeaders(restProtocolRequest))
//        .body(restProtocolRequest.getBody());

    // DataFlowIssue to avoid warning about .getBody() could be null, as it seems that
    //  .body(...) manages  it gracefully.
    //noinspection DataFlowIssue
    return RequestEntity.method(restProtocolRequest.getHttpMethod(), url)
        .headers(buildHttpHeaders(restProtocolRequest)).body(restProtocolRequest.getBody());
  }


  //region Build url
  private final @NonNull <B> String buildUrl(@NonNull final Server server,
      @NonNull final String endpoint, @NonNull final RestProtocolRequest<B> request) {

    Pageable p = request.getPageable();
    // pageable must be added to query parameters
    Map<String, Object> pp = request.getPathParams();
    MultiValueMap<String, String> qp = request.getQueryParams();

    UriComponentsBuilder ucb = UriComponentsBuilder
        // the url.
        .fromHttpUrl(server.url())
        // first path component is server name.
        .pathSegment(server.name())
        // second path component is server version.
        .pathSegment(server.version())
        // next path components are then end point.
        .path(endpoint)
        // next path components are the one of the custom ones in the request; here are put as
        //  placeholders ({<path-component-name>}) that will be replaced later (A).
        .pathSegment(pp.keySet().stream().map(k -> '{' + k + '}').toArray(String[]::new))
        // now it's the turn of query parameters
        .queryParams(qp);

    addPageAndSortQueryParameters(ucb, p);

    UriComponents uc = ucb.buildAndExpand(pp);
    return uc.encode().toUriString();
  }

  private void addPageAndSortQueryParameters(@NonNull final UriComponentsBuilder ucb,
      @Nullable final Pageable pageable) {
    if (pageable == null) {
      return;
    }
    // else...
    if (pageable.isPaged()) {
      ucb.queryParam(PageableQueryParameterNames.PAGE_NUMBER,
          String.valueOf(pageable.getPageNumber()));
      ucb.queryParam(PageableQueryParameterNames.PAGE_SIZE, String.valueOf(pageable.getPageSize()));
    }
    if (pageable.getSort() == null || !pageable.getSort().isSorted()) {
      return;
    }
    // else... sorted!
    // reusing StringBuilder: https://www.baeldung.com/java-reuse-stringbuilder-for-efficiency
    StringBuilder sb = new StringBuilder();
    //queryParameters.remove(PageableQueryParameterNames.SORT);
    for (Order order : pageable.getSort()) {
      sb.setLength(0);
      ucb.queryParam(PageableQueryParameterNames.SORT, sb.append(order.getProperty())
          .append(PageableQueryParameterNames.SORT_PROPERTY_DIRETION_SEP)
          .append(order.getDirection()).toString());
    }
  }
  //endregion Build url

  //region Build http headers
  private HttpHeaders buildHttpHeaders(@NonNull final RestProtocolRequest<?> restProtocolRequest) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    addHeaderFromRequest(httpHeaders, restProtocolRequest);
    addHeaderForRequestMethod(httpHeaders, restProtocolRequest);
    addStandardHeaders(httpHeaders);
    return httpHeaders;
  }

  private void addHeaderFromRequest(@NonNull final HttpHeaders httpHeaders,
      @NonNull final RestProtocolRequest<?> restProtocolRequest) {
    final HttpHeaders requestHttpHeaders = restProtocolRequest.getHttpHeaders();
    httpHeaders.putAll(requestHttpHeaders);
    httpHeaders.setAccept(restProtocolRequest.acceptableMediaTypes());
  }

  private void addHeaderForRequestMethod(@NonNull final HttpHeaders httpHeaders,
      @NonNull final RestProtocolRequest<?> restProtocolRequest) {

    if (restProtocolRequest.getHttpMethod() == HttpMethod.POST) {
      addHeaderForPost(httpHeaders, restProtocolRequest);
    }

  }

  // FIXME EM phonebook: content type for POST and other methods that have body? and moreover
  //  allow to use something different from JSON; this is linked to [B].
  private void addHeaderForPost(@NonNull final HttpHeaders httpHeaders,
      @NonNull final RestProtocolRequest<?> restProtocolRequest) {
    httpHeaders.setContentType(restProtocolRequest.contentType());
  }

  private void addStandardHeaders(@NonNull final HttpHeaders httpHeaders) {
    // TODO fix
//    ret.set(RestClientConstants.H_TRACEID, log.getId());
//    ret.set(RestClientConstants.H_CALLERID, callerId);
    httpHeaders.set(PlatformRestConstants.CALLER_IS_SERVICE_HEADER_NAME, "true");

//    ret.set(PlatformRestConstants.
    //RestClientConstants.H_USERID, SecurityContextUtil.getKeyCloakUserIdFromToken());
    // TODO FIXME EM phonebook: for sure H_TENANTNAME and H_CLIENTID are missing, but right now there
    //  is not a way to get them here, so they will be put in a different moment.

  }
//endregion Build http headers

  //region Manage exception
  // FIXME [DOC]

  /**
   * Maps spring rest client error to an implementation of
   * {@link ServerRestDependencyErrorException}.
   */
  private @NonNull DependencyErrorException manageException(
      @NonNull final RestClientException restClientException, @NonNull final String callerId,
      @NonNull final Server server) {

    return switch (restClientException) {
      case HttpClientErrorException e ->
          new ClientRestDependencyErrorException(e, callerId, server.name(), server.version());
      case HttpServerErrorException e ->
          new ServerRestDependencyErrorException(e, callerId, server.name(), server.version());
      default -> new RestUnknownDependencyErrorException(restClientException, callerId, server.name(), server.version());
    };
  }
  //endregion Manage exception

//  private <B> @Nullable String serializeBody (@Nullable final B body) {
//    if ( body == null ) {
//      return null;
//    }
//    // else...
//    return objectWriter.writeValueAsString(body);
//  }
  //  // FIXME EM phonebook: content type for POST and other methods that have body? and moreover
//  //  allow to use something different from JSON; this is linked to [B].
//  private RequestEntity.BodyBuilder customizeRequestEntityForHttpMethod(
//      @NonNull final RequestEntity.BodyBuilder rebb,
//      @NonNull final RestProtocolRequest<?> restProtocolRequest) {
//    if (restProtocolRequest.getHttpMethod() == HttpMethod.POST) {
//      return rebb.contentType(MediaType.APPLICATION_JSON);
//    } else {
//      return rebb;
//    }
//  }

  //  private <T, B> T exchange(Supplier<ResponseEntity<T>> exchanger,
//      final RestProtocolRequest<B> restProtocolRequest) throws DependencyErrorException {
//    try {
//      ResponseEntity<T> response = exchanger.get();
//      return response.getBody();
//    } catch (RestClientException e) {
//      //noinspection resource
//      EventLogger.notice(log)
//          .level(Level.ERROR)
//          .pattern("rest request failed; methods: {}, error: {}")
//          .params(restProtocolRequest.getHttpMethod(), e.getMessage())
//          .log();
//      throw manageException(e);
//    }
//  }

  //endregion Private methods

}
