package com.github.emw7.platform.service.runtime.rest.request;

import com.github.emw7.platform.i18n.util.I18nUtil;
import com.github.emw7.platform.service.runtime.rest.autoconfig.RequestCallerConfigProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

public class RequestCallerLocaleResolver extends AcceptHeaderLocaleResolver {

  private final RequestCallerConfigProperties requestCallerConfigProperties;

  public RequestCallerLocaleResolver(final RequestCallerConfigProperties requestCallerConfigProperties) {
    this.requestCallerConfigProperties = requestCallerConfigProperties;
  }

  @Override
  public Locale resolveLocale(final HttpServletRequest request) {
    if (request.getHeader(requestCallerConfigProperties.lang()) == null) {
      return super.resolveLocale(request);
    } else {
      Locale requestLocale = I18nUtil.locale(request.getHeader(requestCallerConfigProperties.lang()));
      if (requestLocale == null) {
        requestLocale = super.resolveLocale(request);
      }
      return requestLocale;
    }
  }
}
