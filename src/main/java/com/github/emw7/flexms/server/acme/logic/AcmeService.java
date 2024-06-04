package com.github.emw7.flexms.server.acme.logic;

import com.github.emw7.flexms.commons.acme.Foo;
import com.github.emw7.flexms.platform.error.core.BadPropertiesClientException;
import com.github.emw7.flexms.platform.error.core.ServerRequestErrorException;
import org.springframework.lang.NonNull;

public interface AcmeService {

  public Foo create (@NonNull final Foo foo) throws BadPropertiesClientException, ServerRequestErrorException;
}
