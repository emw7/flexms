package com.github.emw7.platform.service.runtime.rest.autoconfig;

import com.github.emw7.platform.rest.core.PlatformRestConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

@ConfigurationProperties(prefix = "com.github.emw7.platform.app.rest.request.originator.header")
public record RequestOriginatorConfigProperties(@Nullable String tenant, @Nullable String id,
                                                @Nullable String lang, @Nullable String isService) {

  public RequestOriginatorConfigProperties {
    tenant = (StringUtils.isEmpty(tenant)) ? PlatformRestConstants.ORIGINATOR_TENANT_HEADER_NAME
        : tenant;
    id = (StringUtils.isEmpty(id)) ? PlatformRestConstants.ORIGINATOR_ID_HEADER_NAME : id;
    lang = (StringUtils.isEmpty(lang)) ? PlatformRestConstants.ORIGINATOR_LANG_HEADER_NAME : lang;
    isService = (StringUtils.isEmpty(isService)) ? PlatformRestConstants.ORIGINATOR_IS_SERVICE_HEADER_NAME
        : isService;
  }

}
