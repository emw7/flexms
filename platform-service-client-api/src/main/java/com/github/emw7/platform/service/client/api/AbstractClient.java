package com.github.emw7.platform.service.client.api;

import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.auth.api.token.AuthToken;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.discovery.api.error.ServerNotFoundException;
import com.github.emw7.platform.discovery.api.model.Server;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServiceNotFoundServerException;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.log.LogEvent;
import com.github.emw7.platform.protocol.api.ProtocolRequest;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
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
 * Children must implement {@link #mapToRequestErrorException(DependencyErrorException, Function)}
 * that creates {@link RequestErrorException} according to occurred {@link DependencyErrorException}
 * and delegates creation of {@link ClientException} to error manager provided by client. At this
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
   * @param serviceName
   * @param serviceVersion
   * @param callerId
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
  protected final <T, B, E extends RequestErrorException> T call(@NonNull final String endpoint,
      ProtocolRequest<B> protocolRequest, Class<T> responseType,
      @Nullable final Function<DependencyErrorException, E> dependencyErrorManager)
      throws RequestErrorException {

//    try (final LogEvent logEvent = EventLogger.doing(log).level(Level.DEBUG)
//        .ctxArg("service-name", serviceName).ctxArg("service-version", serviceVersion)
//        .ctxArg("caller", callerId).ctxArg("endpoint", endpoint)
//        .pattern("call endpoint {} of service {}@{} ").params(endpoint, serviceName, serviceVersion)
//        .log()) {
//
//      final AuthToken token = authz.authorize();
//      final Server server = serverRegistry.discover(serviceName, serviceVersion);
//      final String url = server.url();
//      logEvent.ctxArg("service-url", url);
//
//      return protocolTemplate.exchange(server, endpoint, callerId, protocolRequest, responseType,
//          token);
//
//    } catch (DependencyErrorException e) {
//      RequestErrorException ree = mapToRequestErrorException(e, dependencyErrorManager);
//      throw ree;
//    }
    return call(endpoint, dependencyErrorManager,
        new ClassExchanger<>(endpoint, protocolRequest, responseType));
  }

  //  /**
//   * Exactly as {@link #call(String, RestRequest, Class, Function)} but allows to specify the
//   * response type through {@link ParameterizedTypeReference} instead of a {@link Class}.
//   *
//   * @see #call(String, RestRequest, Class, Function)
//   */
  protected final <T, B, E extends RequestErrorException> T call(@NonNull final String endpoint,
      ProtocolRequest<B> protocolRequest, ParameterizedTypeReference<T> responseType,
      @Nullable final Function<DependencyErrorException, E> dependencyErrorManager)
      throws RequestErrorException {

    return call(endpoint, dependencyErrorManager,
        new ParameterizedTypeReferenceExchanger<>(endpoint, protocolRequest, responseType));

  }

  //  /**
//   * Creates the service exception that must be thrown by caller.
//   * <p>
//   * Called by either {@link #call(String, RestRequest, Class, Function)} or
//   * {@link #call(String, RestRequest, Class, Function)} in case of {@link DependencyErrorException}
//   * thrown by {@link ProtocolOperations}
//   *
//   * @param e the exception occurred in either {@link #call(String, RestRequest, Class, Function)}
//   *        of {@link #call(String, RestRequest, ParameterizedTypeReference, Function)}
//   * @param dependencyErrorManager exactly the dependency error manager passed by caller to either
//   *        {@link #call(String, RestRequest, Class, Function)} or
//   *        {@link #call(String, RestRequest, Class, Function)}
//   *
//   * @return the service exception that must be returned to the caller
//   */
  protected abstract <E extends RequestErrorException> RequestErrorException mapToRequestErrorException(
      @NonNull final DependencyErrorException e,
      @Nullable final Function<DependencyErrorException, E> dependencyErrorManager,
  @NonNull final String callerId, @NonNull final String serviceName, @NonNull final String serviceVersion);

  //region Private
  private static final class ExchangerNestedRuntimeException extends NestedRuntimeException {
    private ExchangerNestedRuntimeException(@NonNull final Throwable cause) {
      super(cause.getMessage(), cause);
    }
  }

  private abstract static sealed class Exchanger<T, B, E extends RequestErrorException> implements
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

  private final class ClassExchanger<T, B, E extends RequestErrorException> extends
      Exchanger<T, B, E> {

    private final Class<T> responseType;

    private ClassExchanger(final String endpoint, final ProtocolRequest<B> protocolRequest,
        final Class<T> responseType) {
      super(endpoint, protocolRequest);
      this.responseType = responseType;
    }

    protected final T exchange(Server server, AuthToken token) throws DependencyErrorException {
      return protocolTemplate.exchange(server, endpoint, callerId, protocolRequest, responseType,
          token);
    }
  }

  private final class ParameterizedTypeReferenceExchanger<T, B, E extends RequestErrorException> extends
      Exchanger<T, B, E> {

    private final ParameterizedTypeReference<T> responseType;

    private ParameterizedTypeReferenceExchanger(final String endpoint,
        final ProtocolRequest<B> protocolRequest,
        final ParameterizedTypeReference<T> responseType) {
      super(endpoint, protocolRequest);
      this.responseType = responseType;
    }

    protected final T exchange(Server server, AuthToken token) throws DependencyErrorException {
      return protocolTemplate.exchange(server, endpoint, callerId, protocolRequest, responseType,
          token);

    }
  }

  private final <T, B, E extends RequestErrorException> T call(@NonNull final String endpoint,
      @Nullable final Function<DependencyErrorException, E> dependencyErrorManager,
      @NonNull Exchanger<T, B, E> exchanger)
      throws RequestErrorException {

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
      T response= exchanger.apply(server, token);
      EventLogger.done(logEvent);
      return response;

    } catch ( ServerNotFoundException e ) {
      throw new ServiceNotFoundServerException(e, new Id("SQBRA"), callerId, e.getServerName(), e.getServerVersion(), null);
    } catch (ExchangerNestedRuntimeException e) {
      DependencyErrorException dee = (DependencyErrorException) e.getCause();
      RequestErrorException ree = mapToRequestErrorException(dee, dependencyErrorManager,
          callerId, serviceName, serviceVersion);
      throw ree;
    }
  }
  //endregion Private

  //region Getters & Setters
  // TODO
  //endregion Getters & Setters
}
