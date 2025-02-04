package com.github.emw7.platform.service.core.request.context;

import java.util.Locale;
import org.springframework.lang.NonNull;

/**
 * REV:V This class represents the user that does the request.
 * <p>
 * <ul>
 * <li>{@link #tenant()} is the tenant unique code of the requester; refer to the implementation
 * for more details on how it is retrieved.</li>
 * <li>{@link #id()} is the unique identifier of the requester; refer to the implementation
 * for more details on how it is retrieved.</li>
 * <li>{@link #locale()} is the locale of the requester; refer to the implementation
 * for more details on how it is retrieved.</li>
 * <li>{@link #isService()} specify whether the requester is a service; refer to the implementation
 * for more details on how it is retrieved.</li>
 * </ul>
 * <p>
 * The class could have been called user, but it was too generic, so it has been decided to tie the
 * name to the role/context that is mapping a user that does a request, that is a requester.
 */
public sealed interface Requester permits Caller, Originator {

  @NonNull
  String tenant();

  @NonNull
  String id();

  @NonNull
  Locale locale();

  boolean isService();
}
