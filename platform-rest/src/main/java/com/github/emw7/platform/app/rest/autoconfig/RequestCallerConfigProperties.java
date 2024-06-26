package com.github.emw7.platform.app.rest.autoconfig;

import com.github.emw7.platform.app.rest.PlatformRestConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

@ConfigurationProperties(prefix = "com.github.emw7.platform.app.rest.request.caller.header")
public record RequestCallerConfigProperties(@Nullable String tenant, @Nullable String id,
                                            @Nullable String lang, @Nullable String isService) {

  public RequestCallerConfigProperties {
    tenant = (StringUtils.isEmpty(tenant)) ? PlatformRestConstants.CALLER_TENANT_HEADER_NAME
        : tenant;
    id = (StringUtils.isEmpty(id)) ? PlatformRestConstants.CALLER_ID_HEADER_NAME : id;
    lang = (StringUtils.isEmpty(lang)) ? PlatformRestConstants.CALLER_LANG_HEADER_NAME : lang;
    isService = (StringUtils.isEmpty(isService)) ? PlatformRestConstants.CALLER_IS_SERVICE_HEADER_NAME : isService;
  }

}
