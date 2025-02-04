package com.github.emw7.platform.service.core.request.context;

import com.github.emw7.platform.i18n.util.I18nUtil;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * REV:v The holder of the request context.
 * <p>
 * Being a utility class, it can be used anywhere in the application.
 * <p>
 * The request context must be put using {@link #set(RequestContext)} that save it in an
 * {@link InheritableThreadLocal}.<br/>
 * The request context can be retrieved using {@link #get()}.
 */
public final class RequestContextHolder {

  //region Private static properties
  private static final Logger log = LoggerFactory.getLogger(RequestContextHolder.class);

  private static final ThreadLocal<RequestContext> holder = new InheritableThreadLocal<>() {
    @Override
    protected RequestContext initialValue() {
      return new DefaultRequestContext(Locale.getDefault(), Originator.DEFAULT, Caller.DEFAULT);
    }
  };
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
    holder.set(requestContext);
  }

  /**
   * Returns the request context.
   * <p>
   * It cannot be {@code null} as it is forced to a default value if not set.
   *
   * @return the request context
   */
  public static @NonNull RequestContext get() {
    return holder.get();
  }
  //endregion Public static methods

  //region Constructors
  // prevents instantiation.
  private RequestContextHolder() {
  }
  //endregion Constructors

}
