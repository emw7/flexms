package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.log.EventLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class RequestContextHolder {

  //region Private static properties
  private static final Logger log= LoggerFactory.getLogger(RequestContextHolder.class);
  private static final ThreadLocal<RequestContext> holder = new InheritableThreadLocal<>();
  //endregion Private static properties

  //region Public static methods
  /**
   * Sets the {@link RequestContext} to be wrapped.
   * <p>
   * Should not be {@code null}, but if {@code null} is provided it is accepted and a warn log is
   * emitted.
   *
   * @param requestContext the {@link RequestContext} to be wrapped
   */
  public static void set(@NonNull final RequestContext requestContext) {
    // TODO integrate with eventing service.
    if ( requestContext == null ) {
      EventLogger.notice(log, Level.WARN, "[ILLEGAL-ARGUMENT] provided request context is not valid: must be not null");
    }
    holder.set(requestContext);
  }

  /**
   * Returns the request contest set with {@link #set(RequestContext)}; {@code null} if {@link #set(RequestContext)} has not been called.
   *
   * @return the request contest set with {@link #set(RequestContext)}; {@code null} if {@link #set(RequestContext)} has not been called
   */
  public static @Nullable RequestContext get() {
    return holder.get();
  }
  //endregion Public static methods

  //region Constructors
  // prevents instantiation.
  private RequestContextHolder() {
  }
  //endregion Constructors

}
