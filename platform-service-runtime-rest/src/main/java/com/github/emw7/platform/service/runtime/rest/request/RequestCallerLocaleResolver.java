package com.github.emw7.platform.service.runtime.rest.request;

import com.github.emw7.platform.i18n.util.I18nUtil;
import com.github.emw7.platform.service.runtime.rest.autoconfig.RequestCallerConfigProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * Recupera il locale dalla lang specificata nell'http header
 * {@link com.github.emw7.platform.rest.core.PlatformRestConstants#CALLER_LANG_HEADER_NAME}
 * (which value can be overridden by property
 * {@code om.github.emw7.platform.conf.rest.request.caller.header} e se l'header non è presente o
 * ha un valore per il quale non si può recuperare un locale allora delega alla classe padre.
 */
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
