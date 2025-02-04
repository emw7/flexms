package com.github.emw7.platform.service.core.request.context;

import io.micrometer.tracing.Span;
import org.springframework.lang.NonNull;

/**
 * REV:V Interface to be implemented by a request context retriever, that is, an object that retrieve
 * information and create with it a {@link RequestContext} object.
 */
public interface RequestContextRetriever {

  /**
   * Returns the originator of the request chain.
   *
   * @param context
   *
   * @return the originator of the request chain
   */
  @NonNull
  Originator retrieveOriginator(@NonNull final Object context);

  /**
   * Returns the caller of this request chain ring.
   *
   * @param context
   *
   * @return the caller of this request chain ring
   */

  @NonNull
  Caller retrieveCaller(@NonNull final Object context);

}
