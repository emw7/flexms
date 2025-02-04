package com.github.emw7.platform.service.runtime.rest.request.context;

import com.github.emw7.platform.service.core.request.context.Caller;
import com.github.emw7.platform.service.core.request.context.CallerRetriever;
import com.github.emw7.platform.service.core.request.context.AbstractRequestContextRetriever;
import com.github.emw7.platform.service.core.request.context.Originator;
import com.github.emw7.platform.service.core.request.context.OriginatorRetriever;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The default (autoconfigured, see
 * {@link com.github.emw7.platform.service.runtime.rest.autoconfig.PlatformServiceRuntimeRestAutoConfig})
 * request context information retriever that delegates retrieval of originator and caller to
 * their respective objects: {@link RestOriginatorRetriever} and {@link RestCallerRetriever}.
 * <p>
 * Application can customize originator and caller retrieval, still using this class, by defining
 * its own {@code restOriginatorRetriever} and {@code restCallerRetriever} beans.
 *
 * @see com.github.emw7.platform.service.runtime.rest.autoconfig.PlatformServiceRuntimeRestAutoConfig
 * @see AbstractRequestContextRetriever
 */
public final class DefaultHttpServletHeadersRequestContextRetriever extends
    AbstractRequestContextRetriever implements RestRequestContextRetriever {

  //region Private properties
  private final RestOriginatorRetriever originatorRetriever;
  private final RestCallerRetriever callerRetriever;
  //endregion Provate properties

  //region Constructors
  public DefaultHttpServletHeadersRequestContextRetriever(
      @Nullable final RestOriginatorRetriever originatorRetriever,
      @Nullable final RestCallerRetriever callerRetriever) {

    this.originatorRetriever = originatorRetriever;
    this.callerRetriever = callerRetriever;
  }
  //endregion Constructors

  //region Template methods
  @Override
  protected final @Nullable Originator _retrieveOriginator(@NonNull final Object context) {
    if (getOriginatorRetriever() != null) {
      return getOriginatorRetriever().retrieve(context);
    } else {
      return null;
    }
  }


  @Override
  protected final @Nullable Caller _retrieveCaller(@NonNull final Object context) {
    if (getCallerRetriever() != null) {
      return getCallerRetriever().retrieve(context);
    } else {
      return null;
    }
  }
  //endregion Template methods

  //region Getters & Setters
  /*private @Nullable TracingTraceRetriever getTracingTraceRetriever() {
    return tracingTraceRetriever;
  }*/

  private @Nullable OriginatorRetriever getOriginatorRetriever() {
    return originatorRetriever;
  }

  private @Nullable CallerRetriever getCallerRetriever() {
    return callerRetriever;
  }
  //endregion Getters & Setters
}
