package com.github.emw7.bar.client.rest;

import com.github.emw7.bar.client.BarClient;
import com.github.emw7.bar.logic.api.ApiDefinition;
import com.github.emw7.bar.logic.api.ApiDefinition.Create;
import com.github.emw7.bar.logic.api.ApiDefinition.Retrieve;
import com.github.emw7.bar.logic.api.error.BarNotFoundException;
import com.github.emw7.bar.model.Bar;
import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.discovery.api.error.ServerNotFoundException;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import com.github.emw7.platform.protocol.api.ProtocolRequest;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.rest.request.GetRestProtocolRequest;
import com.github.emw7.platform.protocol.rest.request.PostRestProtocolRequest;
import com.github.emw7.platform.protocol.rest.request.RestProtocolRequest;
import com.github.emw7.platform.service.client.rest.AbstractRestClient;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;

public class BarRestClient extends AbstractRestClient implements BarClient {

  public BarRestClient(@NonNull final ProtocolTemplate protocolTemplate, final Authz authz,
      final ServerRegistryDiscover serverRegistryDiscover, @NonNull final String serviceName,
      @NonNull final String serviceVersion, @NonNull final String callerId) {
    super(protocolTemplate, authz, serverRegistryDiscover, serviceName, serviceVersion, callerId);
  }

  @Override
  public @NonNull Bar create(@NonNull final Bar bar) throws RequestErrorException {
    PostRestProtocolRequest<Bar> pr= PostRestProtocolRequest.<Bar>builder()
        .body(bar).contentType(MediaType.APPLICATION_JSON)
        .acceptableMediaType(MediaType.APPLICATION_JSON)
        .build();
    return call(Create.endpoint, pr, Bar.class , null);
  }

  @Override
  public @NonNull Bar retrieve(@NonNull final String id) throws BarNotFoundException {
    GetRestProtocolRequest pr= GetRestProtocolRequest.<Bar>builder()
        .acceptableMediaType(MediaType.APPLICATION_JSON)
        // TODO non mi piace così, perché se cambia nome? deve essere messo in Endpoint
        .pathParams(Map.of("id",id))
        .build();
    // TODO... vedi che serve il mapping delle eccezioni!!!???
    try {
      return call(Retrieve.endpoint, pr, Bar.class , null);
    } catch (RequestErrorException e) {
      throw new BarNotFoundException(new Id("XFWPM"), id, null);
    }
  }

  @NonNull
  @Override
  public List<Bar> list(@NonNull final ListFilter listFilter, @NonNull final Pageable pageable)
      throws ServerRequestErrorException {
    return List.of();
  }

  @NonNull
  @Override
  public Bar update(@NonNull final String id, @NonNull final Bar bar)
      throws NotFoundClientException, ServerRequestErrorException {
    return null;
  }

  @NonNull
  @Override
  public Bar patch(@NonNull final String id, @NonNull final BarPatcher barPatcher)
      throws NotFoundClientException, ServerRequestErrorException {
    return null;
  }

  @Override
  public void delete(@NonNull final String id) throws ServerRequestErrorException {

  }

  @Override
  public void delete(@NonNull final Set<String> ids) throws ServerRequestErrorException {

  }
}
