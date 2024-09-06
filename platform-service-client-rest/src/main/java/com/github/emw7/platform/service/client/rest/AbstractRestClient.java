package com.github.emw7.platform.service.client.rest;

import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.ClientRestDependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.ServerRestDependencyErrorException;
import com.github.emw7.platform.service.client.api.AbstractClient;
import com.github.emw7.platform.service.core.error.model.RequestErrorResponse;
import java.util.function.Function;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The base class for all the REST client.
 * <p>
 * REST client is a <i>remote service</i> that request services to a remote agent through HTTP
 * requests.<br/> More details about <i>remote service</i> see {@link AbstractClient}.
 * <p>
 * This class provides final implementation of the
 * {@link #mapToRequestErrorException(DependencyErrorException, Function, String, String, String)}
 * declared by {@link AbstractClient} and refer to its documentation for the entire flow
 * description.
 * <p>
 * <b>IMPORTANT</b>: it depends on a RestProtocolOperation implementation in order to be tied to
 * REST (HTTP) communication.
 */
public abstract class AbstractRestClient extends AbstractClient {

  //region Constructors
  protected AbstractRestClient(@NonNull final ProtocolTemplate protocolTemplate, Authz authz,
      ServerRegistryDiscover serverRegistryDiscover, @NonNull final String serviceName,
      @NonNull final String serviceVersion, @NonNull final String callerId) {
    super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion, callerId);
  }
  //endregion Constructors

  //region Protected final methods
  @Override
  protected final @Nullable RequestErrorResponse mapDependencyErrorExceptionToRequestErrorResponse(
      @NonNull final DependencyErrorException e) throws UnknownDependencyErrorException {
    if (e instanceof ServerRestDependencyErrorException srdee) {
      return srdee.getErrorResponse()
          .getResponseBodyAs(RequestErrorResponse.class);
    } else if (e instanceof ClientRestDependencyErrorException crdee) {
      // there is no need to set the body conversion function as there is a default function that
      //  works.
      return crdee.getErrorResponse()
          .getResponseBodyAs(RequestErrorResponse.class);
    } else {
      // e should be instanceof RestUnknownDependencyErrorException
      // (see com.github.emw7.platform.protocol.rest.RestProtocolOperation.manageException)
      // but do not care... let's throw...
      throw new UnknownDependencyErrorException();
    }
  }
  //endregion Protected final methods

}
