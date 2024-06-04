package com.github.emw7.flexms.server.acme.logic;

import com.github.emw7.flexms.commons.acme.Foo;
import com.github.emw7.flexms.platform.error.core.BadPropertiesClientException;
import com.github.emw7.flexms.platform.error.core.DependencyUnavailableServerException;
import com.github.emw7.flexms.platform.error.core.ServerRequestErrorException;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AcmeServiceDefault implements AcmeService {

  @Override
  public Foo create(@NonNull final Foo foo) throws BadPropertiesClientException, ServerRequestErrorException {
    if ( foo.uuid() != null ) {
      //throw new BadPropertiesBadRequestException("en:app.error.acme.create", "app.error.acme.create", Map.of());
      throw new DependencyUnavailableServerException("a", "b", null);
      }
    return new Foo(UUID.randomUUID().toString(), foo.name(), foo.i());
  }

}
