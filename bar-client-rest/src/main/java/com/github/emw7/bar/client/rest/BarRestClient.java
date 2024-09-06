package com.github.emw7.bar.client.rest;

import com.github.emw7.bar.client.BarClient;
import com.github.emw7.bar.logic.api.ApiDefinition.Create;
import com.github.emw7.bar.logic.api.ApiDefinition.Retrieve;
import com.github.emw7.bar.logic.api.error.BarNotFoundException;
import com.github.emw7.bar.model.Bar;
import com.github.emw7.platform.auth.api.authz.Authz;
import com.github.emw7.platform.core.map.MapUtil;
import com.github.emw7.platform.discovery.api.ServerRegistryDiscover;
import com.github.emw7.platform.error.Id;
import com.github.emw7.platform.error.NotFoundClientException;
import com.github.emw7.platform.error.RequestErrorException;
import com.github.emw7.platform.error.ResourceIdClientException;
import com.github.emw7.platform.error.ServerRequestErrorException;
import com.github.emw7.platform.error.category.NotFound;
import com.github.emw7.platform.protocol.api.ProtocolTemplate;
import com.github.emw7.platform.protocol.rest.request.GetRestProtocolRequest;
import com.github.emw7.platform.protocol.rest.request.PostRestProtocolRequest;
import com.github.emw7.platform.service.client.rest.AbstractRestClient;
import com.github.emw7.platform.service.core.error.model.RequestErrorResponse;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
  public @NonNull Bar create(@NonNull final Bar bar)
      throws BarNotFoundException, RequestErrorException {
    PostRestProtocolRequest<Bar> pr = PostRestProtocolRequest.<Bar>builder().body(bar)
        .contentType(MediaType.APPLICATION_JSON).acceptableMediaType(MediaType.APPLICATION_JSON)
        .build();

//    Map<Class<? extends Annotation>, Function<RequestErrorResponse, ? extends RequestErrorException>> requestErrorResponseToExceptionMappers =
//        Map.of(
//            NotFound.class,
//              rer -> new BarNotFoundException(
//                           new Id("00000"),
//                           rer.errors().getFirst().params().get(ResourceIdClientException.RESOURCE_ID_KEY),
//                           MapUtil.remove(rer.errors().getFirst().params(), ResourceIdClientException.RESOURCE_ID_KEY)
//                         )
//        );
    return call(Create.endpoint, pr, Bar.class, Create.requestErrorResponseToExceptionMappers);
  }

//  private void mapAndThrowCreateException(@NonNull final DependencyErrorException dee)
//      throws BarNotFoundException, UnmappedDependencyErrorServerException {
//
//    // assuming cast works!
//    final RestDependencyErrorException e= (RestDependencyErrorException)dee;
//
//    // there is no need to set the body conversion function as there is a default function that
//    //  works.
//    final RequestErrorResponse requestErrorResponse = e.getErrorResponse()
//        .getResponseBodyAs(RequestErrorResponse.class);
//    if (requestErrorResponse == null) {
//      throw new UnmappedDependencyErrorServerException(e, new Id("E5A88"), "callerId",
//          "serviceName", "serviceVersion");
//    }
//    // TODO match on type... what about check for res, traceid, spanid...?
//    // TODO NullPointerException?
//    if (codeMatches(requestErrorResponse.ref(), NotFoundClientException.CODE)) {
//      throw new BarNotFoundException(new Id("00000"), Bar.RESOURCE_NAME,
//          requestErrorResponse.errors().getFirst().params());
//    }
//    // else...
//    throw new UnmappedDependencyErrorServerException(e, new Id("E5A88"), "callerId", "serviceName",
//        "serviceVersion");
//
//  }


  @Override
  public @NonNull Bar retrieve(@NonNull final String id) throws BarNotFoundException {
    GetRestProtocolRequest pr = GetRestProtocolRequest.<Bar>builder()
        .acceptableMediaType(MediaType.APPLICATION_JSON)
        // TODO non mi piace così, perché se cambia nome? deve essere messo in Endpoint
        .pathParams(Map.of("id", id)).build();
    // TODO... vedi che serve il mapping delle eccezioni!!!???
    try {
      return call(Retrieve.endpoint, pr, Bar.class, null);
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
