package com.github.emw7.platform.service.core.request.context;

import java.util.Locale;
import org.springframework.lang.NonNull;

/**
 * This class represents the user that does the request.
 * <p>
 * The class could have been called user, but it was too generic so it has been decided to tie the
 * name to the role/context that is mapping an user that does a request, that is a requestor.
 */
public sealed interface Requestor permits Caller, Originator {

  @NonNull
  String tenant();

  @NonNull
  String id();

  @NonNull
  Locale locale();

  boolean isService();
}
