package com.github.emw7.platform.service.runtime.rest.request.context;

import com.github.emw7.platform.service.core.request.context.AbstractRequestContextRetriever;
import com.github.emw7.platform.service.core.request.context.Caller;
import com.github.emw7.platform.core.mapper.BooleanMapper;
import com.github.emw7.platform.service.runtime.rest.autoconfig.PlatformServiceRuntimeRestAutoConfig;
import com.github.emw7.platform.service.runtime.rest.autoconfig.RequestCallerConfigProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The default (autoconfigured, see
 * {@link com.github.emw7.platform.service.runtime.rest.autoconfig.PlatformServiceRuntimeRestAutoConfig})
 * request caller.
 * <p>
 * Information is retrieved from http headers defined in {@link RequestCallerConfigProperties},
 * except for {@code locale} that is retrieved by calling
 * {@code LocaleContextHolder.getLocale()} that in turn uses the {@code localeResolver} bean
 * configured in
 * {@link PlatformServiceRuntimeRestAutoConfig#localeResolver(RequestCallerConfigProperties)}.
 *
 * @see com.github.emw7.platform.service.runtime.rest.autoconfig.PlatformServiceRuntimeRestAutoConfig
 * @see DefaultHttpServletHeadersRequestContextRetriever
 * @see AbstractRequestContextRetriever
 */
public class HttpServletRequestHeaderCallerRetriever implements RestCallerRetriever {

  //region Private properties
  private final RequestCallerConfigProperties requestCallerConfigProperties;
  //endregion Private properties

  //region Constructors
  public HttpServletRequestHeaderCallerRetriever(
      @NonNull final RequestCallerConfigProperties requestCallerConfigProperties) {
    this.requestCallerConfigProperties = requestCallerConfigProperties;
  }
  //endregion Constructors

  //region API
  @Override
  public @Nullable Caller retrieve(@NonNull final Object context) {

    final HttpServletRequest httpServletRequest = (HttpServletRequest)context;

    final Caller.Builder callerBuilder= new Caller.Builder();
    // tenant
    final String tenant= httpServletRequest.getHeader(
        getRequestCallerConfigProperties().tenant());
    callerBuilder.tenant(tenant);
    // ==========

    // id
    final String id= httpServletRequest.getHeader(getRequestCallerConfigProperties().id());
    callerBuilder.id(id);
    // ==========

    // locale
    final Locale locale= LocaleContextHolder.getLocale();
    callerBuilder.locale(locale);
    // ==========

    // isService
    final boolean isService= BooleanMapper.fromString(httpServletRequest.getHeader(getRequestCallerConfigProperties().isService()));
    callerBuilder.isService(isService);
    // ==========

    return callerBuilder.build();

  }
  //endregion API

  //region Getters & Setters
  private RequestCallerConfigProperties getRequestCallerConfigProperties() {
    return requestCallerConfigProperties;
  }
  //endregion Getters & Setters

}
