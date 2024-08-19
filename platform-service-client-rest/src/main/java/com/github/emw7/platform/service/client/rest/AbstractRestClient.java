package com.github.emw7.platform.service.client.rest;

import com.github.emw7.platform.app.error.model.RequestErrorResponse;
import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.error.ClientRequestErrorException;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.api.error.DependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.RestDependencyErrorException;
import com.github.emw7.platform.protocol.rest.error.RestUnknownDependencyErrorException;
import com.github.emw7.platform.service.client.api.AbstractClient;
import com.github.emw7.platform.service.client.api.error.UnknownDependencyErrorServerException;
import com.github.emw7.platform.service.client.api.error.UnmappedDependencyErrorServerException;
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
 * {@link #mapToRequestErrorException(DependencyErrorException, Function)} declared by
 * {@link AbstractClient} and refer to its documentation for the entire flow description.
 * <p>
 * <b>IMPORTANT</b>: it depends on a RestProtocolOperation implementation in order to be tied to
 * REST (HTTP) communication.
 */
public abstract class AbstractRestClient extends AbstractClient {

  public AbstractRestClient(@NonNull final ProtocolTemplate protocolTemplate, Authz authz,
      ServerRegistryDiscover serverRegistryDiscover, @NonNull final String serviceName,
      @NonNull final String serviceVersion, @NonNull final String callerId) {
    super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion, callerId);
  }

  @Override
  protected final <E extends RequestErrorException> @NonNull RequestErrorException mapToRequestErrorException(
      @NonNull final DependencyErrorException e,
      @Nullable final Function<DependencyErrorException, E> dependencyErrorManager,
      @NonNull final String callerId, @NonNull final String serviceName, @NonNull final String serviceVersion) {
    if (e instanceof RestUnknownDependencyErrorException) {
      return new UnknownDependencyErrorServerException(e, new Id("1QURD"),callerId, serviceName, serviceVersion);
    } else {
      // instance of RestDependencyErrorException
      final RestDependencyErrorException rdee = (RestDependencyErrorException) e;
      // there is no need to set the body conversion function as there is a default function that
      //  works.
      final RequestErrorResponse requestErrorResponse = rdee.getErrorResponse()
          .getResponseBodyAs(RequestErrorResponse.class);
      // TODO match on type... what about check for res, traceid, spanid...?
      // TODO NullPointerException?
      if (ServerRequestErrorException.is(requestErrorResponse.type())) {
        return null;
      } else if (ClientRequestErrorException.is(requestErrorResponse.type())) {
        return null;
      } else {
        return new UnmappedDependencyErrorServerException(e, new Id("E5A88"),callerId, serviceName, serviceVersion);
      }
    }


  }

}
