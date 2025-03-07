package com.github.emw7.platform.service.client.api;

import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.auth.api.token.AuthToken;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.discovery.api.error.ServerNotFoundException;
import com.github.emw7.platform.discovery.api.model.Server;
import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.log.DoingLogEvent;
import com.github.emw7.platform.log.EventLogger;
import com.github.emw7.platform.protocol.api.ProtocolRequest;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.service.client.api.error.DependencyErrorServerException;
import com.github.emw7.platform.service.client.api.error.ServiceNotFoundServerException;
import com.github.emw7.platform.service.core.common.request.error.ErrorResponseToExceptionMapper;
import com.github.emw7.platform.service.core.common.request.error.ErrorResponseToExceptionMapper.CannotMapException;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.ServiceCoreCommonRequestErrorConstants;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

///**
// * Base class for a service client. //region TO BE REVIEWED
// * <p>
// * A service client is a bean at service layer that serves the caller by sending the request to a
// * remote(1)(2) service.
// * <p>
// * Children must implement {@link #_mapToRequestErrorException(DependencyErrorException)} that
// * creates {@link RequestErrorException} according to occurred {@link DependencyErrorException} and
// * delegates creation of {@link RequestErrorException} to error manager provided by client. At this
// * level it is not possible creating client exception, only client can, so it must provide
// * dependency error manager that creates the right client exception according to occurred
// * {@link DependencyErrorException}.
// * <p>
// * The remote is not contact by this class by the dialog with the remote party is delegate to the
// * instance of the {@link ProtocolTemplate}.
// * <p>
// * <b>Notes</b>:
// *
// * <pre>
// * [1] remote means 'not in the same process' and does not mean 'in a different machine'; it could
// * be in the same machine actually but running in a different process.
// * [2] A good design would allow to be agnostic among either remote or not (that is 'the same
// * process'); the current design allow that but only following complicated solutions as proxying.
// * [-] Even if this class aims to be as generic as possible (i.e. allowing to be base for a gRPC
// * client for example) has some references to the REST (HTTP) world likely that is only a naming
// * issue (i.e. RestRequest can be rename to RemoteRequest) but it could be that some refactoring
// * must be done.
// * </pre>
// * // endregion TO BE REVIEWED
// */
public abstract class AbstractClient2 {

  private static final Logger log = LoggerFactory.getLogger(AbstractClient2.class);

  private final ProtocolTemplate protocolTemplate;

  private final Authz authz;

  private final ServerRegistryDiscover serverRegistry;

//  /**
//   * The server <b>name</b> the client is for.
//   */
//  private final String serviceName;
//  /**
//   * The server <b>version</b> the client is for.
//   */
//  private final String serviceVersion;

  /**
   * The name of the server that is doing the call.
   */
  private final String callerId;

  /**
   * @param protocolTemplate the implementation that contacts the remote
   * @param callerId         TODO
   */
  public AbstractClient2(@NonNull final ProtocolTemplate protocolTemplate,
      @NonNull final Authz authz, @NonNull final ServerRegistryDiscover serverRegistry,
      @NonNull final String callerId) {
    this.protocolTemplate = protocolTemplate;
    this.authz = authz;
    this.serverRegistry = serverRegistry;
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
  public final <T, B> T call(@NonNull final String serviceName, @NonNull final String serviceVersion,
      @NonNull final String endpoint, ProtocolRequest<B> protocolRequest,
      Class<T> responseType,
      @Nullable final ErrorResponseToExceptionMapper<?> responseToExceptionMapper)
      throws RequestErrorException {

    return call(serviceName, serviceVersion, endpoint, responseToExceptionMapper,
        new ClassExchanger<>(endpoint, protocolRequest, responseType));
  }

  @SuppressWarnings("unused")
  public final <T, B> T call(@NonNull final String serviceName, @NonNull final String serviceVersion,
      @NonNull final String endpoint, ProtocolRequest<B> protocolRequest,
      ParameterizedTypeReference<T> responseType,
      @Nullable final ErrorResponseToExceptionMapper<?> responseToExceptionMapper)
      throws RequestErrorException {

    return call(serviceName, serviceVersion, endpoint, responseToExceptionMapper,
        new ParameterizedTypeReferenceExchanger<>(endpoint, protocolRequest, responseType));

  }

  protected abstract @Nullable RequestErrorResponse _mapToRequestErrorResponse(
      @NonNull final DependencyErrorException e);

  private @Nullable ServerRequestErrorException mapToStandardServerException(
      @NonNull final RequestErrorResponse requestErrorResponse,
      @NonNull final DependencyErrorException dependencyErrorException) {

    // A quick check to verify whether proceed.
    if (requestErrorResponse.status() != ServiceCoreCommonRequestErrorConstants.SERVER_ERROR_CODE) {
      return null;
    }
    // else...

    return switch (requestErrorResponse.ref()) {
      case String ref when codeMatches(ref, ServiceNotFoundServerException.CODE) || codeMatches(ref,
          DependencyErrorServerException.CODE)
//          || codeMatches(ref, UnknownDependencyErrorServerException.CODE)
          -> new DependencyErrorServerException(dependencyErrorException, new Id("TLSB8"),
          requestErrorResponse);
//      case String ref when codeMatches(ref, UnknownDependencyErrorServerException.CODE) ->
//          new DownstreamStackDependencyErrorServerException(dependencyErrorException,
//              new Id("8YS9O"), requestErrorResponse.ref());
//      case String ref when codeMatches(ref, UnmappedDependencyErrorServerException.CODE) ->
//          new DownstreamStackDependencyErrorServerException(dependencyErrorException,
//              new Id("ZHRWW"), requestErrorResponse.ref());
//      case null -> throw new UnknownDependencyErrorException(dependencyErrorException);
      default -> null;
    };
  }

  protected final @NonNull RequestErrorException mapToRequestErrorException(
      @NonNull final DependencyErrorException dee,
      @Nullable final ErrorResponseToExceptionMapper<?> responseToExceptionMapper) {

    final RequestErrorResponse requestErrorResponse = _mapToRequestErrorResponse(dee);

    if (requestErrorResponse == null) {
      return new DependencyErrorServerException(dee, new Id("2INEA"), null);
    }
    // else...

    // Here we have requestErrorResponse not null, but could be invalid with invalid ref ant that
    // must be managed.

    RequestErrorException requestErrorException = null;
    requestErrorException = mapToStandardServerException(requestErrorResponse, dee);
    if (requestErrorException != null) {
      return requestErrorException;
    }

    if (responseToExceptionMapper != null) {
      try {
        return responseToExceptionMapper.map(requestErrorResponse);
      } catch ( CannotMapException ignored) {}
    }
    return new DependencyErrorServerException(dee, new Id("LY22Y"), requestErrorResponse);
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

  /**
   * @param endpoint
   * @param responseToExceptionMapper
   * @param exchanger
   * @param <T>
   * @param <B>
   * @return
   * @throws RequestErrorException
   * @throws ServiceNotFoundServerException if the dependency service cannot be found.
   */
  private <T, B> T call(
      @NonNull final String serviceName, @NonNull final String serviceVersion,
      @NonNull final String endpoint,
      @Nullable final ErrorResponseToExceptionMapper responseToExceptionMapper,
      @NonNull Exchanger<T, B> exchanger) throws RequestErrorException {

    try {
      // TODO fix for new log.
      final DoingLogEvent logEvent = EventLogger.doing(log, "call endpoint {} of service {}@{} ",
          endpoint, serviceName, serviceVersion).debug()
          /*.ctxArg("service-name", serviceName).ctxArg("service-version", serviceVersion)
          .ctxArg("caller", callerId).ctxArg("endpoint", endpoint)*/.log();

      final AuthToken token = authz.authorize();
      // can throw ServerNotFoundException
      final Server server = serverRegistry.discover(serviceName, serviceVersion);
      final String url = server.url();
      // TODO fix for new log.
      /*logEvent.ctxArg("service-url", url);*/

      // can throw ExchangerNestedRuntimeException
      T response = exchanger.apply(server, token);
      EventLogger.done(logEvent).log();
      return response;

    } catch (ServerNotFoundException e) {
      throw new ServiceNotFoundServerException(e, new Id("CK4ZV"), callerId, e.getServerName(),
          e.getServerVersion(), null);
    } catch (ExchangerNestedRuntimeException e) {
      DependencyErrorException dee = (DependencyErrorException) e.getCause();
      throw mapToRequestErrorException(dee, responseToExceptionMapper);
    }
  }
  //endregion Private

  //region Getters & Setters
  //endregion Getters & Setters
}
