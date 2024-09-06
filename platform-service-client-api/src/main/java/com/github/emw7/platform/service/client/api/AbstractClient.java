package com.github.emw7.platform.service.client.api;

import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.auth.api.token.AuthToken;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.discovery.api.error.ServerNotFoundException;
import com.github.emw7.platform.discovery.api.model.Server;
import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Constants;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import com.github.emw7.platform.error.ServiceNotFoundServerException;
import com.github.emw7.platform.error.category.NotFound;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.log.LogEvent;
import com.github.emw7.platform.protocol.api.ProtocolRequest;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.service.client.api.error.DownstreamStackDependencyErrorServerException;
import com.github.emw7.platform.service.client.api.error.UnknownDependencyErrorServerException;
import com.github.emw7.platform.service.client.api.error.UnmappedDependencyErrorServerException;
import com.github.emw7.platform.service.core.error.model.RequestErrorResponse;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base class for a service client. //region TO BE REVIEWED
 * <p>
 * A service client is a bean at service layer that serves the caller by sending the request to a
 * remote(1)(2) service.
 * <p>
 * Children must implement {@link #mapToRequestErrorException(DependencyErrorException, Map)} that
 * creates {@link RequestErrorException} according to occurred {@link DependencyErrorException} and
 * delegates creation of {@link RequestErrorException} to error manager provided by client. At this
 * level it is not possible creating client exception, only client can, so it must provide
 * dependency error manager that creates the right client exception according to occurred
 * {@link DependencyErrorException}.
 * <p>
 * The remote is not contact by this class by the dialog with the remote party is delegate to the
 * instance of the {@link ProtocolTemplate}.
 * <p>
 * <b>Notes</b>:
 *
 * <pre>
 * [1] remote means 'not in the same process' and does not mean 'in a different machine'; it could
 * be in the same machine actually but running in a different process.
 * [2] A good design would allow to be agnostic among either remote or not (that is 'the same
 * process'); the current design allow that but only following complicated solutions as proxying.
 * [-] Even if this class aims to be as generic as possible (i.e. allowing to be base for a gRPC
 * client for example) has some references to the REST (HTTP) world likely that is only a naming
 * issue (i.e. RestRequest can be rename to RemoteRequest) but it could be that some refactoring
 * must be done.
 * </pre>
 * // endregion TO BE REVIEWED
 */
public abstract class AbstractClient {

  private static final Logger log = LoggerFactory.getLogger(AbstractClient.class);

  private final ProtocolTemplate protocolTemplate;

  private final Authz authz;

  private final ServerRegistryDiscover serverRegistry;

  /**
   * The server <b>name</b> the client is for.
   */
  private final String serviceName;
  /**
   * The server <b>version</b> the client is for.
   */
  private final String serviceVersion;

  /**
   * The name of the server that is doing the call.
   */
  private final String callerId;

  /**
   * @param protocolTemplate the implementation that contacts the remote
   * @param serviceName      TODO
   * @param serviceVersion   TODO
   * @param callerId         TODO
   */
  public AbstractClient(@NonNull final ProtocolTemplate protocolTemplate,
      @NonNull final Authz authz, @NonNull final ServerRegistryDiscover serverRegistry,
      @NonNull final String serviceName, @NonNull final String serviceVersion,
      @NonNull final String callerId) {
    this.protocolTemplate = protocolTemplate;
    this.authz = authz;
    this.serverRegistry = serverRegistry;
    this.serviceName = serviceName;
    this.serviceVersion = serviceVersion;
    this.callerId = callerId;
  }

  //  /**
//   * Delegates to the instance of ProtocolOperations the execution of the requested operation and
//   * returns the response.
//   * <p>
//   * In case of {@link DependencyErrorException} thrown by
//   * {@link ProtocolOperations#call(String, String, String, String, RestRequest, Class)} calls
//   * {@link #createServiceException(DependencyErrorException, Function)} and throws the returned
//   * exception.
//   *
//   * @param endPoint the operation to invoke on the remote
//   * @param protocolRequest the parameters of the operation
//   * @param responseType
//   * @param dependencyErrorManager a function that (can) translate the occurred error on the remote
//   *        to a business exception.
//   *
//   * @return the response answered by the remote
//   *
//   * @param <T> type of the response payload
//   * @param <B> type of the request payload (Body)
//   * @param <E> type of the exception thrown in case of errors
//   *
//   * @throws RequestErrorException in case of RequestErrorResponse from service
//   * @throws ServerNotFoundException in case service cannot be found in service registry
//   */
  protected final <T, B> T call(@NonNull final String endpoint, ProtocolRequest<B> protocolRequest,
      Class<T> responseType,
      @Nullable final Map<Class<? extends Annotation>, Function<RequestErrorResponse, ? extends RequestErrorException>> requestErrorResponseToExceptionMappers)
      throws RequestErrorException {

    return call(endpoint, requestErrorResponseToExceptionMappers,
        new ClassExchanger<>(endpoint, protocolRequest, responseType));
  }

  @SuppressWarnings("unused")
  protected final <T, B> T call(@NonNull final String endpoint, ProtocolRequest<B> protocolRequest,
      ParameterizedTypeReference<T> responseType,
      @Nullable final Map<Class<? extends Annotation>, Function<RequestErrorResponse, ? extends RequestErrorException>> requestErrorResponseToExceptionMappers)
      throws RequestErrorException {

    return call(endpoint, requestErrorResponseToExceptionMappers,
        new ParameterizedTypeReferenceExchanger<>(endpoint, protocolRequest, responseType));

  }

  //region Template methods
  public static final class UnknownDependencyErrorException extends Exception {
    private final String causeRef;
    public UnknownDependencyErrorException()
    {
      this.causeRef= null;
    }
    public UnknownDependencyErrorException (@NonNull final String causeRef) {
      this.causeRef= causeRef;
    }

    public @Nullable String getCauseRef() {
      return causeRef;
    }
  }

//  private static final class UnmappedDependencyErrorException extends Exception {
//    private final String causeRef;
//    private UnmappedDependencyErrorException (@NonNull final String causeRef) {
//      this.causeRef= causeRef;
//    }
//
//    private @NonNull String getCauseRef() {
//      return causeRef;
//    }
//  }

  protected abstract @Nullable RequestErrorResponse mapDependencyErrorExceptionToRequestErrorResponse(
      @NonNull final DependencyErrorException e) throws UnknownDependencyErrorException;

  /**
   * Maps the request error response to a standard server exception.
   * <p>
   * <b>Note</b>: some responses are not mapped to the related serve exception but are mapped to
   * {@link DownstreamStackDependencyErrorServerException}.<br/> These exceptions are:
   * <ul>
   *   <li>{@link ServiceNotFoundServerException}</li>
   *   <li>{@link UnknownDependencyErrorServerException}</li>
   *   <li>{@link UnmappedDependencyErrorServerException}</li>
   * </ul>
   * </p>
   *
   * @param requestErrorResponse     TODO
   * @param dependencyErrorException TODO
   * @return the mapping of the request error response to a standard server exception
   */
  private @Nullable ServerRequestErrorException mapToStandardServerException(
      @NonNull final RequestErrorResponse requestErrorResponse,
      @NonNull final DependencyErrorException dependencyErrorException) throws UnknownDependencyErrorException{
    if (requestErrorResponse.status() != Constants.SERVER_ERROR_CODE) {
      return null;
    }
    // else...

    return switch (requestErrorResponse.ref()) {
      case String ref when codeMatches(ref, ServiceNotFoundServerException.CODE) ->
          new DownstreamStackDependencyErrorServerException(dependencyErrorException,
              new Id("IE45T"), requestErrorResponse.ref());
      case String ref when codeMatches(ref, UnknownDependencyErrorServerException.CODE) ->
          new DownstreamStackDependencyErrorServerException(dependencyErrorException,
              new Id("8YS9O"), requestErrorResponse.ref());
      case String ref when codeMatches(ref, UnmappedDependencyErrorServerException.CODE) ->
          new DownstreamStackDependencyErrorServerException(dependencyErrorException,
              new Id("ZHRWW"), requestErrorResponse.ref());
      case null -> throw new UnknownDependencyErrorException();
      default -> null;
    };
  }

  /**
   * Maps the {@link DependencyErrorException} got from called service (aka remote service) to a
   * {@link RequestErrorException} that is the exception that will receive who used the client to
   * contact the remote service.
   * <p>
   * The result of the mapping is the same exception that has reached the controller of the called
   * service with some exception because in this case (the client calls the remote service) some
   * errors in the communication can occur that, of course, cannot occur when the call is local
   * (that is all internal to the remote service itself): - {@link ServiceNotFoundServerException}:
   * the service registry could not find the remote service -
   * {@link UnknownDependencyErrorServerException}: an unknown error occurred in the calling of the
   * remote service (for example, the remote service did not respond because it is down) -
   * {@link UnmappedDependencyErrorServerException}: received either a client or a server error
   * that, for some reason, cannot be mapped to known exception (that is an exception that is known
   * to be thrown by service layer of the remote service, that is, again, an exception that is known
   * that can reach the controller of the called service. - ??? Dependency internal error: in case
   * the dependency
   *
   * @param e                                      TODO
   * @param requestErrorResponseToExceptionMappers TODO
   * @return TODO
   */
  protected final @NonNull RequestErrorException mapToRequestErrorException(
      @NonNull final DependencyErrorException e,
      @Nullable final Map<Class<? extends Annotation>, Function<RequestErrorResponse, ? extends RequestErrorException>> requestErrorResponseToExceptionMappers) {
    try {

      final RequestErrorResponse requestErrorResponse = Optional.ofNullable(
              mapDependencyErrorExceptionToRequestErrorResponse(e))
          .orElseThrow(UnknownDependencyErrorException::new);
      // from now on, requestErrorResponse is *NOT* null.

      //region standard server exception
      ServerRequestErrorException standardServerRequestErrorException = mapToStandardServerException(
          requestErrorResponse, e);
      if (standardServerRequestErrorException != null) {
        return standardServerRequestErrorException;
      }
      //endregion standard server exception

      final Class<? extends Annotation> category = retrieveRequestErrorResponseCategory(
          requestErrorResponse);
      // from now on, category is *NON* null.

      Function<RequestErrorResponse, ? extends RequestErrorException> requestErrorResponseToExceptionMapper = function(
          e, category, requestErrorResponseToExceptionMappers);
      RequestErrorException ree = requestErrorResponseToExceptionMapper.apply(requestErrorResponse);
      if (ree != null) {
        return ree;
      }
      else {
        return new UnmappedDependencyErrorServerException(e, new Id("E5A88"), category,
            requestErrorResponse.ref());
      }
    } catch (UnknownDependencyErrorException udee) {
      // yes... udee is ignored,
      //  and it is not the cause of UnknownDependencyErrorServerException,
      //  but e is the cause.
      // TODO Id is useless... because... UnknownDependencyErrorException is thrown in 2 different
      //  places but cachted here, so Id does not identify where exception arise: remove Id.
      return new UnknownDependencyErrorServerException(e, new Id("1QURD"),udee.getCauseRef());
    }

  }

  private @NonNull Class<? extends Annotation> retrieveRequestErrorResponseCategory(
      @NonNull final RequestErrorResponse requestErrorResponse)
      throws UnknownDependencyErrorException {
    return switch (requestErrorResponse.ref()) {
      case String ref when codeMatches(ref, NotFoundClientException.CODE) -> NotFound.class;
      case null -> throw new UnknownDependencyErrorException();
      default -> throw new UnknownDependencyErrorException(requestErrorResponse.ref());
      //default -> throw new UnmappedDependencyErrorException(requestErrorResponse.ref());
    };
  }

  private @NonNull Function<RequestErrorResponse, ? extends RequestErrorException> function(
      @NonNull final DependencyErrorException e,
      @NonNull final Class<? extends Annotation> category,
      @Nullable final Map<Class<? extends Annotation>, Function<RequestErrorResponse, ? extends RequestErrorException>> requestErrorResponseToExceptionMappers) {
    if (requestErrorResponseToExceptionMappers == null) {
      return requestErrorResponse -> new UnmappedDependencyErrorServerException(e, new Id("E5A88"),
          category, requestErrorResponse.ref());
    } else {
      if (requestErrorResponseToExceptionMappers.get(category) == null) {
        return p -> new UnmappedDependencyErrorServerException(e, new Id("E5A88"), category,
            p.ref());
      } else {
        return requestErrorResponseToExceptionMappers.get(category);
      }
    }
  }

  private boolean codeMatches(@Nullable final String ref, @NonNull final Code code) {
    if (ref == null) {
      return false;
    } else {
      return ref.startsWith(code.toString() + '-');
    }
  }
  //endregion Template methods

  //region Private
  private static final class ExchangerNestedRuntimeException extends NestedRuntimeException {

    private ExchangerNestedRuntimeException(@NonNull final Throwable cause) {
      super(cause.getMessage(), cause);
    }
  }

  private abstract static sealed class Exchanger<T, B> implements
      BiFunction<Server, AuthToken, T> permits ParameterizedTypeReferenceExchanger, ClassExchanger {

    protected final String endpoint;
    protected final ProtocolRequest<B> protocolRequest;

    private Exchanger(final String endpoint, final ProtocolRequest<B> protocolRequest) {
      this.endpoint = endpoint;
      this.protocolRequest = protocolRequest;
    }

    public final T apply(Server server, AuthToken token) throws ExchangerNestedRuntimeException {
      try {
        return exchange(server, token);
      } catch (Exception e) {
        throw new ExchangerNestedRuntimeException(e);
      }
    }

    protected abstract T exchange(Server server, AuthToken token) throws DependencyErrorException;

  }

  private final class ClassExchanger<T, B> extends Exchanger<T, B> {

    private final Class<T> responseType;

    private ClassExchanger(final String endpoint, final ProtocolRequest<B> protocolRequest,
        final Class<T> responseType) {
      super(endpoint, protocolRequest);
      this.responseType = responseType;
    }

    protected T exchange(Server server, AuthToken token) throws DependencyErrorException {
      return protocolTemplate.exchange(server, endpoint, callerId, protocolRequest, responseType,
          token);
    }
  }

  private final class ParameterizedTypeReferenceExchanger<T, B> extends Exchanger<T, B> {

    private final ParameterizedTypeReference<T> responseType;

    private ParameterizedTypeReferenceExchanger(final String endpoint,
        final ProtocolRequest<B> protocolRequest,
        final ParameterizedTypeReference<T> responseType) {
      super(endpoint, protocolRequest);
      this.responseType = responseType;
    }

    protected T exchange(Server server, AuthToken token) throws DependencyErrorException {
      return protocolTemplate.exchange(server, endpoint, callerId, protocolRequest, responseType,
          token);

    }
  }

  private <T, B> T call(@NonNull final String endpoint,
      @Nullable final Map<Class<? extends Annotation>, Function<RequestErrorResponse, ? extends RequestErrorException>> requestErrorResponseToExceptionMappers,
      @NonNull Exchanger<T, B> exchanger) throws RequestErrorException {

    try (final LogEvent logEvent = EventLogger.doing(log).level(Level.DEBUG)
        .ctxArg("service-name", serviceName).ctxArg("service-version", serviceVersion)
        .ctxArg("caller", callerId).ctxArg("endpoint", endpoint)
        .pattern("call endpoint {} of service {}@{} ").params(endpoint, serviceName, serviceVersion)
        .log()) {

      final AuthToken token = authz.authorize();
      // can throw ServerNotFoundException
      final Server server = serverRegistry.discover(serviceName, serviceVersion);
      final String url = server.url();
      logEvent.ctxArg("service-url", url);

      // can throw ExchangerNestedRuntimeException
      T response = exchanger.apply(server, token);
      EventLogger.done(logEvent);
      return response;

    } catch (ServerNotFoundException e) {
      throw new ServiceNotFoundServerException(e, new Id("SQBRA"), callerId, e.getServerName(),
          e.getServerVersion(), null);
    } catch (ExchangerNestedRuntimeException e) {
      DependencyErrorException dee = (DependencyErrorException) e.getCause();
      throw mapToRequestErrorException(dee, requestErrorResponseToExceptionMappers);
    }
  }
  //endregion Private

  //region Getters & Setters
  // TODO
  //endregion Getters & Setters
}
