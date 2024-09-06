package com.github.emw7.platform.service.runtime.rest.aop;

import com.github.emw7.platform.service.core.request.context.Caller;
import com.github.emw7.platform.service.core.request.context.RequestContext;
import com.github.emw7.platform.service.core.request.context.DefaultRequestContext;
import com.github.emw7.platform.service.runtime.rest.request.context.RestRequestContextRetriever;
import com.github.emw7.platform.telemetry.tracing.Trace;
import com.github.emw7.platform.telemetry.tracing.TracingContainer;
import com.github.emw7.platform.telemetry.tracing.TracingFactory;
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
public class RestControllerAspect {

  private final TracingFactory tracingFactory;
  private final RestRequestContextRetriever requestContextRetriever;

  public RestControllerAspect(@NonNull final TracingFactory tracingFactory,
      @NonNull final RestRequestContextRetriever requestContextRetriever) {
    this.tracingFactory = tracingFactory;
    this.requestContextRetriever = requestContextRetriever;
  }

  @Before(value = "@within(org.springframework.web.bind.annotation.RestController)", argNames = "joinPoint")
  public void tracing(@NonNull final JoinPoint joinPoint) {

    final HttpServletRequest httpServletRequest= retrieveHttpServletRequest();

    final Trace trace= requestContextRetriever.retrieveTrace(httpServletRequest);
    TracingContainer.set(tracingFactory.of(trace.getId()));

    final Caller originator= requestContextRetriever.retrieveOriginator(httpServletRequest);
    final Caller caller = requestContextRetriever.retrieveCaller(httpServletRequest);
    final RequestContext requestContext= new DefaultRequestContext(Locale.getDefault(),
        // true that TracingContainer.get() can be null that it has been set it only some rows above...
        TracingContainer.get(), originator, caller);
    com.github.emw7.platform.service.core.request.context.RequestContextHolder.set(requestContext);
    // TODO lanciare eccezione IllegalState o cosa? quando caller e vuoto? Se si come forzare in ogni protocollo?
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
