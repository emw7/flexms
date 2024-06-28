package com.github.emw7.platform.app.rest.request.context;

import com.github.emw7.platform.app.request.context.Caller;
import com.github.emw7.platform.core.i18n.I18nUtil;
import com.github.emw7.platform.core.mapper.BooleanMapper;
import com.github.emw7.platform.app.rest.autoconfig.RequestOriginatorConfigProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
   * <li>locale: {@link com.github.emw7.platform.app.PlatformAppConstants#SYSTEM_LOCALE}</li>
   * <li>isService: {@link com.github.emw7.platform.app.PlatformAppConstants#SYSTEM_IS_SERVICE}</li>
   * </ul>
   *
   * @param context the context from which can be retrieved the needed information.
   * @return the retrieved originator or {@code null} if originator was not supplied.
   */
  @Override
  public @Nullable Caller retrieve(@NonNull final Object context) {

    final HttpServletRequest httpServletRequest = (HttpServletRequest) context;

    final Caller.Builder originatorBuilder = new Caller.Builder();
    // tenant
    final String tenant = httpServletRequest.getHeader(
        getRequestOriginatorConfigProperties().tenant());
    if (tenant == null) {
      return null;
    }
    originatorBuilder.tenant(tenant);
    // ==========

    // id
    final String id = httpServletRequest.getHeader(getRequestOriginatorConfigProperties().id());
    if (id == null) {
      return null;
    }
    originatorBuilder.id(id);
    // ==========

    // locale
    final String lang = httpServletRequest.getHeader(getRequestOriginatorConfigProperties().lang());
    final Locale locale = I18nUtil.locale(lang);
    originatorBuilder.locale(locale);
    // ==========

    // isService
    final boolean isService = BooleanMapper.fromString(
        httpServletRequest.getHeader(getRequestOriginatorConfigProperties().isService()));
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
