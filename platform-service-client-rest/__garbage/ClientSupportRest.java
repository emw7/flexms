package com.github.emw7.platform.service.client.rest;

import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.RestClientDependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.RestServerDependencyErrorException;
import com.github.emw7.platform.service.client.api.AbstractClient;
import com.github.emw7.platform.service.core.common.request.error.model.RequestErrorResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

///**
// * The base class for all the REST client.
// * <p>
// * REST client is a <i>remote service</i> that request services to a remote agent through HTTP
// * requests.<br/> More details about <i>remote service</i> see {@link AbstractClient}.
// * <p>
// * This class provides final implementation of the
// * {@link #mapDependencyErrorExceptionToRequestErrorResponse(DependencyErrorException, Function, String, String, String)}
// * declared by {@link AbstractClient} and refer to its documentation for the entire flow
// * description.
// * <p>
// * <b>IMPORTANT</b>: it depends on a RestProtocolOperation implementation in order to be tied to
// * REST (HTTP) communication.
// */
public abstract class ClientSupportRest extends AbstractClient {

  //region Constructors
  protected ClientSupportRest(@NonNull final ProtocolTemplate protocolTemplate, Authz authz,
      ServerRegistryDiscover serverRegistryDiscover, @NonNull final String serviceName,
      @NonNull final String serviceVersion, @NonNull final String callerId) {
    super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion, callerId);
  }
  //endregion Constructors

  //region Protected final methods
  @Override
  protected final @Nullable RequestErrorResponse _mapToRequestErrorResponse(
      @NonNull final DependencyErrorException e) {
    RequestErrorResponse requestErrorResponse = null;
    if (e instanceof RestServerDependencyErrorException rxdee) {
      requestErrorResponse = rxdee.getCause().getResponseBodyAs(RequestErrorResponse.class);

    } else if (e instanceof RestClientDependencyErrorException rxdee) {
      // there is no need to set the body conversion function as there is a default function that
      //  works.
      requestErrorResponse = rxdee.getCause().getResponseBodyAs(RequestErrorResponse.class);

    }
    return requestErrorResponse;
  }
  //endregion Protected final methods

}
