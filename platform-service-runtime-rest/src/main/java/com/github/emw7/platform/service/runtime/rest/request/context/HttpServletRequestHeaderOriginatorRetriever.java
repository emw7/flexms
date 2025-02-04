package com.github.emw7.platform.service.runtime.rest.request.context;

import com.github.emw7.platform.service.core.ServiceCoreConstants;
import com.github.emw7.platform.service.core.request.context.AbstractRequestContextRetriever;
import com.github.emw7.platform.core.mapper.BooleanMapper;
import com.github.emw7.platform.service.core.request.context.Originator;
import com.github.emw7.platform.service.runtime.rest.autoconfig.PlatformServiceRuntimeRestAutoConfig;
import com.github.emw7.platform.service.runtime.rest.autoconfig.RequestCallerConfigProperties;
import com.github.emw7.platform.service.runtime.rest.autoconfig.RequestOriginatorConfigProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The default (autoconfigured, see
 * {@link com.github.emw7.platform.service.runtime.rest.autoconfig.PlatformServiceRuntimeRestAutoConfig})
 * request originator.
 * <p>
 * Information is retrieved from http headers defined in {@link RequestOriginatorConfigProperties},
 * except for {@code locale} that is retrieved by calling
 * {@code LocaleContextHolder.getLocale()} that in turn uses the {@code localeResolver} bean
 * configured in
 * {@link PlatformServiceRuntimeRestAutoConfig#localeResolver(RequestCallerConfigProperties)}.
 *
 * @see com.github.emw7.platform.service.runtime.rest.autoconfig.PlatformServiceRuntimeRestAutoConfig
 * @see DefaultHttpServletHeadersRequestContextRetriever
 * @see AbstractRequestContextRetriever
 */
public class HttpServletRequestHeaderOriginatorRetriever implements RestOriginatorRetriever {

  //region Private properties
  private final RequestOriginatorConfigProperties requestOriginatorConfigProperties;
  //endregion Private properties

  //region Constructors
  public HttpServletRequestHeaderOriginatorRetriever(
      @NonNull final RequestOriginatorConfigProperties requestOriginatorConfigProperties) {
    this.requestOriginatorConfigProperties = requestOriginatorConfigProperties;
  }
  //endregion Constructors

  //region API

  /**
   * Returns retrieved originator or {@code null} if originator was not supplied.
   * <p>
   * Originator is considered as not supplied if either tenant or id cannot be retrieved, in such a case {@code  null} is returned.
   * <p>
   * Other information is optional anda, if not supplied, it gets default values:
   * <ul>
   * <li>locale: {@link ServiceCoreConstants#SYSTEM_LOCALE}</li>
   * <li>isService: {@link ServiceCoreConstants#SYSTEM_IS_SERVICE}</li>
   * </ul>
   *
   * @param context the context from which can be retrieved the needed information.
   * @return the retrieved originator or {@code null} if originator was not supplied.
   */
  @Override
  public @Nullable Originator retrieve(@NonNull final Object context) {

    final HttpServletRequest httpServletRequest = (HttpServletRequest)context;

    final Originator.Builder originatorBuilder= new Originator.Builder();
    // tenant
    final String tenant= httpServletRequest.getHeader(
        getRequestOriginatorConfigProperties().tenant());
    originatorBuilder.tenant(tenant);
    // ==========

    // id
    final String id= httpServletRequest.getHeader(getRequestOriginatorConfigProperties().id());
    originatorBuilder.id(id);
    // ==========

    // locale
    // TODO NON VA BENE perché NON usa l'header LANG di ORIGINATOR!!!
    final Locale locale= LocaleContextHolder.getLocale();
    originatorBuilder.locale(locale);
    // ==========

    // isService
    final boolean isService= BooleanMapper.fromString(httpServletRequest.getHeader(getRequestOriginatorConfigProperties().isService()));
    originatorBuilder.isService(isService);
    // ==========

    return originatorBuilder.build();

  }
  //endregion API

  //region Getters & Setters
  private RequestOriginatorConfigProperties getRequestOriginatorConfigProperties() {
    return requestOriginatorConfigProperties;
  }
  //endregion Getters & Setters

}
