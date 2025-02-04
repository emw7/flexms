package com.github.emw7.platform.service.core.request.context;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * REV:V The {@link Originator} retriever.
 * <p>
 * How to retrieve the originator is left to the implementations.</br>
 * The {@code context} object that is passed to the {@link #retrieve(Object)} method is thought to
 * be an object from which extract {@code originator} information.<br/>
 * See
 * {@code com.github.emw7.platform.service.runtime.rest.request.context.HttpServletRequestHeaderOriginatorRetriever#retrieve(java.lang.Object)}
 * for example.
 */
public interface OriginatorRetriever {

  @Nullable
  Originator retrieve(@NonNull final Object context);

}
