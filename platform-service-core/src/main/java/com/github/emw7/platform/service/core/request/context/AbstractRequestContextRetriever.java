package com.github.emw7.platform.service.core.request.context;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * REV:V Abstract request context retriever that provides a basic template for retrieving request
 * context information.
 * <p>
 * See method descriptions for more information.
 */
public abstract class AbstractRequestContextRetriever implements RequestContextRetriever {

  //region API
  /**
   * Returns the originator of this request chain.
   * <p>
   * Template flow:
   * <pre>
   * 1. Gets originator ({@code o}) by calling abstract _retrieveOriginator(context)
   * 2. If {@code o} is {@code null} then returns Originator.DEFAULT, otherwise returns {@code o}.
   * </pre>
   *
   * @see Originator
   * @see Originator#DEFAULT
   */
  @Override
  public final @NonNull Originator retrieveOriginator(@NonNull final Object context) {
    final Originator originator = _retrieveOriginator(context);
    if (originator != null) {
      return originator;
    } else {
      return Originator.DEFAULT;
    }
  }

  /**
   * Returns the caller of this request chain ring.
   * <p>
   * Template flow:
   * <pre>
   * 1. Gets caller ({@code c}) by calling abstract _retrieveCaller(context)
   * 2. If {@code c} is {@code null} then returns Caller.DEFAULT, otherwise returns {@code c}.
   * </pre>
   *
   * @see Caller
   * @see Caller#DEFAULT
   */
  @Override
  public final @NonNull Caller retrieveCaller(@NonNull final Object context) {
    final Caller caller = _retrieveCaller(context);
    if (caller != null) {
      return caller;
    } else {
      return Caller.DEFAULT;
    }
  }
  //endregion API

  //region Template methods
//  protected abstract @Nullable Span _retrieveTrace(@NonNull final Object context);/* {
//    return null;
//  }*/

  protected abstract @Nullable Originator _retrieveOriginator(@NonNull final Object context);/* {
    return null;
  }*/

  protected abstract @Nullable Caller _retrieveCaller(@NonNull final Object context);/* {
    return null;
  }*/
  //endregion Template methods

}
