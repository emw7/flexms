package com.github.emw7.platform.service.runtime.rest.aop;

import com.github.emw7.platform.service.core.request.context.Caller;
import com.github.emw7.platform.service.core.request.context.Originator;
import com.github.emw7.platform.service.core.request.context.RequestContext;
import com.github.emw7.platform.service.core.request.context.DefaultRequestContext;
import com.github.emw7.platform.service.runtime.rest.request.context.RestRequestContextRetriever;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
public final class RestControllerAspect {

  private final RestRequestContextRetriever requestContextRetriever;

  public RestControllerAspect(@NonNull final RestRequestContextRetriever requestContextRetriever) {
    this.requestContextRetriever = requestContextRetriever;
  }

  /**
   * Retrieves the {@link RequestContext} information and put it into the
   * {@link com.github.emw7.platform.service.core.request.context.RequestContextHolder}.
   * <p>
   * Application can get {@link RequestContext} information in any place by invoking
   * {@link com.github.emw7.platform.service.core.request.context.RequestContextHolder#get()}.
   *
   * @param joinPoint
   */
  @Before(value = "@within(org.springframework.web.bind.annotation.RestController)", argNames = "joinPoint")
  public void requestContext(@NonNull final JoinPoint joinPoint) {

    final HttpServletRequest httpServletRequest= retrieveHttpServletRequest();

    final Originator originator= requestContextRetriever.retrieveOriginator(httpServletRequest);
    final Caller caller = requestContextRetriever.retrieveCaller(httpServletRequest);
    final RequestContext requestContext= new DefaultRequestContext(Locale.getDefault(),
        originator, caller);
    com.github.emw7.platform.service.core.request.context.RequestContextHolder.set(requestContext);
    // TODO lanciare eccezione IllegalState o cosa? Quando caller è vuoto? Se sì come forzare in ogni protocollo?
  }

  private @NonNull HttpServletRequest retrieveHttpServletRequest () {
    final RequestAttributes requestAttributes= RequestContextHolder.getRequestAttributes();
    if ( requestAttributes instanceof ServletRequestAttributes servletRequestAttributes ) {
      final HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
      if ( httpServletRequest != null ) {
        return httpServletRequest;
      }
      else {
        throw new IllegalStateException("HttpServletRequest is null");
      }
    }
    throw new IllegalStateException(
        String.format("RequestContextHolder.getRequestAttributes() is not an instance of '%s' but it is '%s'",
            ServletRequestAttributes.class.getName(), ( requestAttributes == null) ? "null" : requestAttributes.getClass().getName()));
  }
}
