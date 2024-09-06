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
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServiceNotFoundServerException;
import com.github.emw7.platform.error.category.NotFound;
import com.github.emw7.platform.protocol.api.ProtocolRequest;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.ClientRestDependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.RestUnknownDependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.ServerRestDependencyErrorException;
import com.github.emw7.platform.protocol.rest.request.GetRestProtocolRequest;
import com.github.emw7.platform.service.client.api.error.DownstreamStackDependencyErrorServerException;
import com.github.emw7.platform.service.client.api.error.UnknownDependencyErrorServerException;
import com.github.emw7.platform.service.client.api.error.UnmappedDependencyErrorServerException;
import com.github.emw7.platform.service.core.error.model.RequestErrorResponse;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ExtendWith(MockitoExtension.class)
public class AbstractRestClientTest {

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
  public void givenServiceIsNotRegisteredInServiceRegistry_WhenCall_thenServiceNotFoundServerException() throws ServerNotFoundException {
    // data...
    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {
        ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();
        return call("/bar", pr, Object.class, null);
      }

    };

    // given...
    doThrow(new ServerNotFoundException(new Id("00000"), serviceName, serviceVersion))
        .when(serverRegistryDiscover).discover(serviceName, serviceVersion);

    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .as("given service is not registered in service registry when call then service not found server exception")
        .isInstanceOf(ServiceNotFoundServerException.class);

    //verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
  }
  //endregion ServiceNotFoundServerException

  //region UnknownDependencyErrorServerException
  // 1 -> 2
  @Test
  public void givenExchangeRaisesRestUnknownDependencyErrorException_WhenCall_thenUnknownDependencyErrorServerException() throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    };

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover).discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(RestUnknownDependencyErrorException.class).when(protocolTemplate).exchange(any(), eq(
            endPoint),
        eq(callerId), eq(pr), eq(Object.class), any());


    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(UnknownDependencyErrorServerException.class);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  // 2.2 -> 2
  @Test
  public void givenExchangeRaisesClientRestDependencyErrorExceptionWithNullRef_WhenCall_thenUnknownDependencyErrorServerException() throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    };

    final HttpClientErrorException httpErrorException= new HttpClientErrorException(HttpStatus.BAD_REQUEST);
    httpErrorException.setBodyConvertFunction( resolvableType -> new RequestErrorResponse(
        null, null, httpErrorException.getStatusCode().value(), null, "", "", "", "", null,
        null, null));
    final ClientRestDependencyErrorException e= new ClientRestDependencyErrorException(
        httpErrorException,callerId, serviceName, serviceVersion);

    // given...

    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover).discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate).exchange(any(), eq(
            endPoint),
        eq(callerId), eq(pr), eq(Object.class), any());


    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(UnknownDependencyErrorServerException.class);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  // 3.2 -> 2
  @Test
  public void givenExchangeRaisesServerRestDependencyErrorExceptionWithNullRef_WhenCall_thenUnknownDependencyErrorServerException() throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    };

    final HttpServerErrorException httpErrorException= new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorException.setBodyConvertFunction( resolvableType -> new RequestErrorResponse(
        null, null, httpErrorException.getStatusCode().value(), null, "", "", "", "", null,
        null, null));
    final ServerRestDependencyErrorException e= new ServerRestDependencyErrorException(
        httpErrorException,callerId, serviceName, serviceVersion);

    // given...

    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover).discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate).exchange(any(), eq(
            endPoint),
        eq(callerId), eq(pr), eq(Object.class), any());


    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(UnknownDependencyErrorServerException.class);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }
  //endregion UnknownDependencyErrorServerException

  //region The same exception that controller B gets from service B
  // 2.1.1 -> 3
  @Test
  public void givenExchangeRaisesQazClientException_WhenCall_thenSameClientException() throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class TestNotFoundException extends NotFoundClientException {

      private TestNotFoundException() {
        super(new Id(""), "test", "0", null);
      }
    }

    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, Map.of(
            NotFound.class, requestErrorResponse -> new TestNotFoundException()));
      }

    };

    final HttpClientErrorException httpErrorException= new HttpClientErrorException(HttpStatus.NOT_FOUND);
    httpErrorException.setBodyConvertFunction( resolvableType -> new RequestErrorResponse(
        null, null, httpErrorException.getStatusCode().value(), NotFoundClientException.CODE + "-00000", "", "", "", "", null,
        null, null));
    final ClientRestDependencyErrorException e= new ClientRestDependencyErrorException(
        httpErrorException,callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover).discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate).exchange(any(), eq(
            endPoint),
        eq(callerId), eq(pr), eq(Object.class), any());


    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(TestNotFoundException.class);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }
  //endregion same exception that controller B gets from service B

  //region UnmappedDependencyErrorServerException
  // 2.1.2 -> 2
  @Test
  public void givenExchangeRaisesWithUnmappedClientCategory_WhenCall_thenUnknownDependencyErrorServerException() throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        // the mapper will not be used at all.
        return call(endPoint, pr, Object.class, Map.of(
            NotFound.class, requestErrorResponse -> new NotFoundClientException(new Id("00000"),"test", "0", null) {}));
      }

    };

    final HttpClientErrorException httpErrorException= new HttpClientErrorException(HttpStatus.BAD_REQUEST);
    httpErrorException.setBodyConvertFunction( resolvableType -> new RequestErrorResponse(
        null, null, httpErrorException.getStatusCode().value(), "?????" + "-00000", "", "", "", "", null,
        null, null));
    final ClientRestDependencyErrorException e= new ClientRestDependencyErrorException(
        httpErrorException,callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover).discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate).exchange(any(), eq(
            endPoint),
        eq(callerId), eq(pr), eq(Object.class), any());


    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(UnknownDependencyErrorServerException.class)
        .hasFieldOrPropertyWithValue("causeRef","?????-00000");

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  // 3.1.3 -> 2
  @Test
  public void givenExchangeRaisesWithUnmappedServerCategory_WhenCall_thenUnknownDependencyErrorServerException() throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, Map.of(
            NotFound.class, requestErrorResponse -> new NotFoundClientException(new Id("00000"),"test", "0", null) {}));
      }

    };

    final HttpServerErrorException httpErrorException= new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorException.setBodyConvertFunction( resolvableType -> new RequestErrorResponse(
        null, null, httpErrorException.getStatusCode().value(), "?????" + "-00000", "", "", "", "", null,
        null, null));
    final ServerRestDependencyErrorException e= new ServerRestDependencyErrorException(
        httpErrorException,callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover).discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate).exchange(any(), eq(
            endPoint),
        eq(callerId), eq(pr), eq(Object.class), any());


    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(UnknownDependencyErrorServerException.class);

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }
  //endregion UnmappedDependencyErrorServerException

  //region DownstreamStackDependecyErrorServerException
  // 3.1.2 -> 5
  @Test
  public void givenExchangeRaisesWsxServerException_WhenCall_thenDownstreamStackDependencyErrorServerException() throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    };

    final HttpServerErrorException httpErrorException= new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorException.setBodyConvertFunction( resolvableType -> new RequestErrorResponse(
        null, null, httpErrorException.getStatusCode().value(), ServiceNotFoundServerException.CODE + "-00000", "", "", "", "", null,
        null, null));
    final ServerRestDependencyErrorException e= new ServerRestDependencyErrorException(
        httpErrorException,callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover).discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate).exchange(any(), eq(
            endPoint),
        eq(callerId), eq(pr), eq(Object.class), any());


    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DownstreamStackDependencyErrorServerException.class)
        .hasFieldOrPropertyWithValue("causeRef", ServiceNotFoundServerException.CODE + "-00000");

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  // 3.1.1_Unknown -> 5
  @Test
  public void givenExchangeRaisesUnknownDependencyErrorServerException_WhenCall_thenDownstreamStackDependecyErrorServerException() throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    };

    final HttpServerErrorException httpErrorException= new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorException.setBodyConvertFunction( resolvableType -> new RequestErrorResponse(
        null, null, httpErrorException.getStatusCode().value(), UnknownDependencyErrorServerException.CODE + "-00000", "", "", "", "", null,
        null, null));
    final ServerRestDependencyErrorException e= new ServerRestDependencyErrorException(
        httpErrorException,callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover).discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate).exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());


    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DownstreamStackDependencyErrorServerException.class)
        .hasFieldOrPropertyWithValue("causeRef",UnknownDependencyErrorServerException.CODE + "-00000");

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }

  // 3.1.1_Unmapped -> 5
  @Test
  public void givenExchangeRaisesUnmappedErrorServerException_WhenCall_thenDownstreamStackDependecyErrorServerException() throws ServerNotFoundException, DependencyErrorException {

    // data...
    ProtocolRequest<Void> pr = GetRestProtocolRequest.builder().build();

    class Client extends AbstractRestClient {

      Client() {
        super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion,
            callerId);
      }

      public Object call() throws RequestErrorException {

        return call(endPoint, pr, Object.class, null);
      }

    };

    final HttpServerErrorException httpErrorException= new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorException.setBodyConvertFunction( resolvableType -> new RequestErrorResponse(
        null, null, httpErrorException.getStatusCode().value(), UnmappedDependencyErrorServerException.CODE + "-00000", "", "", "", "", null,
        null, null));
    final ServerRestDependencyErrorException e= new ServerRestDependencyErrorException(
        httpErrorException,callerId, serviceName, serviceVersion);

    // given...
    doReturn(new Server(serviceName, serviceVersion, serviceUrl)).when(serverRegistryDiscover).discover(serviceName, serviceVersion);
    doReturn(new SimpleAuthToken("", System.currentTimeMillis())).when(authz).authorize();
    doThrow(e).when(protocolTemplate).exchange(any(), eq(endPoint), eq(callerId), eq(pr), eq(Object.class), any());


    // execute test...
    Assertions.assertThatThrownBy(new Client()::call)
        .isInstanceOf(DownstreamStackDependencyErrorServerException.class)
        .hasFieldOrPropertyWithValue("causeRef",UnmappedDependencyErrorServerException.CODE + "-00000");

    // verify...
    verify(serverRegistryDiscover, times(1)).discover(serviceName, serviceVersion);
    verify(authz, times(1)).authorize();
  }
  //endregion DownstreamStackDependecyErrorServerException
}
