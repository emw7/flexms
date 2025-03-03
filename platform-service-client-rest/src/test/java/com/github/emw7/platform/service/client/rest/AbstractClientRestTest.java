package com.github.emw7.platform.service.client.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.auth.api.token.SimpleAuthToken;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.discovery.api.error.ServerNotFoundException;
import com.github.emw7.platform.discovery.api.model.Server;
import com.github.emw7.platform.error.Code;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.protocol.api.ProtocolRequest;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.RestClientDependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.RestServerDependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.RestUnknownDependencyErrorException;
import com.github.emw7.platform.protocol.rest.request.GetRestProtocolRequest;
import com.github.emw7.platform.service.client.api.error.DependencyErrorServerException;
import com.github.emw7.platform.service.client.api.error.ServiceNotFoundServerException;
import com.github.emw7.platform.service.core.common.request.error.NotFoundClientException;
import com.github.emw7.platform.service.core.common.request.error.RequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.ServerRequestErrorException;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

@ExtendWith(MockitoExtension.class)
public class AbstractClientRestTest {

  @Mock
  private ProtocolTemplate protocolTemplate;
  @Mock
  private Authz authz;
  @Mock
  private ServerRegistryDiscover serverRegistryDiscover;

  private final String serviceName = "bar-rest";
  private final String serviceVersion = "v1";
  private final String serviceUrl = "https//" + serviceName + ".local";

  private final String endPoint = "/bar";

  private final String callerId = "AbstractRestClientTest";

  //region ServiceNotFoundServerException
  // -> 1
  @Test
  public void givenServiceIsNotRegisteredInServiceRegistry_WhenCall_thenServiceNotFoundServerException()
      throws ServerNotFoundException {
    // data...
    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {
        ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();
        return call("/bar", pr, Object.class, null);
      }

    }

    // given...
    doThrow(new ServerNotFoundException(serviceName, serviceVersion)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .as("given service is not registered in service registry when call then service not found server exception")
        .isInstanceOf(ServiceNotFoundServerException.class);

    //verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
  }
  //endregion ServiceNotFoundServerException

  //region Client error
  @Test
  public void clientDoesNotPassResponseToExceptionMapperSoExpectADependencyErrorServerExceptionServer()
      throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class TestNotFoundClientException extends NotFoundClientException {

      private TestNotFoundClientException() {
        super(new Id(""), "test", "0");
      }
    }

    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    }

    final HttpClientErrorException httpErrorException = new HttpClientErrorException(
        HttpStatus.NOT_FOUND);
    httpErrorException.setBodyConvertFunction(resolvableType -> new RequestErrorResponse(null, null,
        httpErrorException.getStatusCode().value(), NotFoundClientException.CODE + "-00000", "", "",
        "", "", null));
    final RestClientDependencyErrorException e = new RestClientDependencyErrorException(
        httpErrorException, callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate)
        .exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DependencyErrorServerException.class);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  @Test
  public void clientDoesNotPassResponseToExceptionMapperSoExpectADependencyErrorServerExceptionClient()
      throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class TestNotFoundClientException extends NotFoundClientException {

      private TestNotFoundClientException() {
        super(new Id(""), "test", "0");
      }
    }

    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    }

    final HttpClientErrorException httpErrorException = new HttpClientErrorException(
        HttpStatus.NOT_FOUND);
    httpErrorException.setBodyConvertFunction(resolvableType -> new RequestErrorResponse(null, null,
        httpErrorException.getStatusCode().value(), NotFoundClientException.CODE + "-00000", "", "",
        "", "", null));
    final RestClientDependencyErrorException e = new RestClientDependencyErrorException(
        httpErrorException, callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate)
        .exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DependencyErrorServerException.class);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  public static class TestNotFoundClientException extends NotFoundClientException {

    public TestNotFoundClientException(Object resourceId) {
      super(new Id(""), "test", resourceId);
    }
  }

  @Test
  public void protocolThrowsAnExceptionThatResolvesInASpecificClientException()
      throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();



    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, /*this::_mapRequestErrorResponseToRequestErrorException*/
        NotFoundClientException.mapper(TestNotFoundClientException.class, Object.class));
      }

      private @NonNull RequestErrorException _mapRequestErrorResponseToRequestErrorException(
          @NonNull final RequestErrorResponse e) {
        return new TestNotFoundClientException(0);
      }
    }

    final HttpClientErrorException httpErrorException = new HttpClientErrorException(
        HttpStatus.NOT_FOUND);
    httpErrorException.setBodyConvertFunction(resolvableType -> new RequestErrorResponse(null, null,
        httpErrorException.getStatusCode().value(), NotFoundClientException.CODE + "-00000", "", "",
        "", "", List.of(new RequestErrorException.Error("", Map.of("resourceName","test", "resourceId",0)))));
    final RestClientDependencyErrorException e = new RestClientDependencyErrorException(
        httpErrorException, callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate)
        .exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(TestNotFoundClientException.class);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  // ThatConcreteClientDoesNotKnowHowToConvertToTiedException see _mapRequestErrorResponseToRequestErrorException
  //  returns null... that mean that the arrived error response cannot be mapped to a tued exception,
  //  EntityNotFound for example.
  @Test
  public void protocolThrowsAnExceptionThatResolvesInRestClientDependencyErrorExceptionThatConcreteClientDoesNotKnowHowToConvertToTiedException()
      throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class TestNotFoundClientException extends NotFoundClientException {

      private TestNotFoundClientException() {
        super(new Id(""), "test", "0");
      }
    }

    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    }

    final HttpClientErrorException httpErrorException = new HttpClientErrorException(
        HttpStatus.NOT_FOUND);
    httpErrorException.setBodyConvertFunction(resolvableType -> new RequestErrorResponse(null, null,
        httpErrorException.getStatusCode().value(), NotFoundClientException.CODE + "-00000", "", "",
        "", "", null));
    final RestClientDependencyErrorException e = new RestClientDependencyErrorException(
        httpErrorException, callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate)
        .exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DependencyErrorServerException.class).cause().isSameAs(e);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  @Test
  public void protocolThrowsAnExceptionThatResolvesInRestClientDependencyErrorExceptionWhichBodyIsConvertedToNull()
      throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class TestNotFoundClientException extends NotFoundClientException {

      private TestNotFoundClientException() {
        super(new Id(""), "test", "0");
      }
    }

    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    }

    final HttpClientErrorException httpErrorException = new HttpClientErrorException(
        HttpStatus.NOT_FOUND);
    httpErrorException.setBodyConvertFunction(resolvableType -> null);
    final RestClientDependencyErrorException e = new RestClientDependencyErrorException(
        httpErrorException, callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate)
        .exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DependencyErrorServerException.class).cause().isSameAs(e);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }
  //endregion Client error

  //region Server error
  @Test
  public void protocolThrowsAnExceptionThatResolvesInASpecificServerException()
      throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class TestServerException extends ServerRequestErrorException {

      private TestServerException() {
        super(new Code("1234"), new Id("1111"), new Error("msg", "i18nLabel", Map.of()));
      }
    }

    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, this::_mapRequestErrorResponseToRequestErrorException);
      }

      final @NonNull RequestErrorException _mapRequestErrorResponseToRequestErrorException(
          @NonNull final RequestErrorResponse e) {
        return new TestServerException();
      }
    }

    final HttpServerErrorException httpErrorException = new HttpServerErrorException(
        HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorException.setBodyConvertFunction(resolvableType -> new RequestErrorResponse(null, null,
        httpErrorException.getStatusCode().value(), "1234-1111", "", "", "", "", null));
    final RestServerDependencyErrorException e = new RestServerDependencyErrorException(
        httpErrorException, callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate)
        .exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call).isInstanceOf(TestServerException.class);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  // ThatConcreteClientDoesNotKnowHowToConvertToTiedException see _mapRequestErrorResponseToRequestErrorException
  //  returns null... that mean that the arrived error response cannot be mapped to a tued exception,
  //  DbConnectionError for example.
  @Test
  public void protocolThrowsAnExceptionThatResolvesInRestServerDependencyErrorExceptionThatConcreteClientDoesNotKnowHowToConvertToTiedException()
      throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class TestServerException extends ServerRequestErrorException {

      private TestServerException() {
        super(new Code("1234"), new Id("1111"), new Error("msg", "i18nLabel", Map.of()));
      }
    }

    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    }

    final HttpServerErrorException httpErrorException = new HttpServerErrorException(
        HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorException.setBodyConvertFunction(resolvableType -> new RequestErrorResponse(null, null,
        httpErrorException.getStatusCode().value(), "1234-1111", "", "", "", "", null));
    final RestServerDependencyErrorException e = new RestServerDependencyErrorException(
        httpErrorException, callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate)
        .exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DependencyErrorServerException.class).cause().isSameAs(e);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  @Test
  public void protocolThrowsAnExceptionThatResolvesInRestServerDependencyErrorExceptionWhichBodyIsConvertedToNull()
      throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class TestServerException extends ServerRequestErrorException {

      private TestServerException() {
        super(new Code("1234"), new Id("1111"), new Error("msg", "i18nLabel", Map.of()));
      }
    }

    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    }

    final HttpServerErrorException httpErrorException = new HttpServerErrorException(
        HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorException.setBodyConvertFunction(resolvableType -> null);
    final RestServerDependencyErrorException e = new RestServerDependencyErrorException(
        httpErrorException, callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate)
        .exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DependencyErrorServerException.class).cause().isSameAs(e);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  @Test
  public void protocolThrowsAnExceptionThatResolvesInRestUnknownDependencyErrorException()
      throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class Client extends AbstractClientRest {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    }

    final RestClientException httpErrorException = new RestClientException("error");
    final RestUnknownDependencyErrorException e = new RestUnknownDependencyErrorException(
        httpErrorException, callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover)
        .discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate)
        .exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DependencyErrorServerException.class).cause().isSameAs(e);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }
  //endregion Server error

}
