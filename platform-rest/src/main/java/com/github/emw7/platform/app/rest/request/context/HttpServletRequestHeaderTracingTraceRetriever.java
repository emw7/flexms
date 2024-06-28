package com.github.emw7.platform.app.rest.request.context;

import com.github.emw7.platform.telemetry.tracing.Trace;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpServletRequestHeaderTracingTraceRetriever implements RestTracingTraceRetriever {

  private final String traceIdHeaderName;

  public HttpServletRequestHeaderTracingTraceRetriever(@NonNull final String traceIdHeaderName) {
    this.traceIdHeaderName = traceIdHeaderName;
  }

  @Override
  public @Nullable Trace retrieve() {
    final RequestAttributes requestAttributes= RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes ) {
      HttpServletRequest httpServletRequest= servletRequestAttributes.getRequest();
      String traceId= httpServletRequest.getHeader(traceIdHeaderName);
      if (!StringUtils.isEmpty(traceId)) {
        return new Trace(traceId);
      }
    }
    return null;
  }

}
