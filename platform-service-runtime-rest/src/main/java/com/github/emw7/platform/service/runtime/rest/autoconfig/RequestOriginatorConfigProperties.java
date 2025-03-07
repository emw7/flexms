package com.github.emw7.platform.service.runtime.rest.autoconfig;

import com.github.emw7.platform.rest.core.PlatformRestConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

// TODO rimuovere perché questo va bene per il servizio che riceve la richiesta, ma per il
//  client non va bene: il client NON ha la configurazione di runtime del service!
//  Attenzione che a questa classe ci sono riferimenti anche nella documentazione che va quindi
//  rivista!
@ConfigurationProperties(prefix = "com.github.emw7.platform.conf.rest.request.originator.header")
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
