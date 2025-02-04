package com.github.emw7.platform.service.core.request.context;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * REV:V The {@link Caller} retriever.
 * <p>
 * How to retrieve the caller is left to the implementations.</br>
 * The {@code context} object that is passed to the {@link #retrieve(Object)} method is thought to
 * be an object from which extract {@code caller} information.<br/>
 * See
 * {@code com.github.emw7.platform.service.runtime.rest.request.context.HttpServletRequestHeaderCallerRetriever#retrieve(java.lang.Object)}
 * for example.
 */
public interface CallerRetriever {

  @Nullable
  Caller retrieve(@NonNull final Object context);

}
