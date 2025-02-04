package com.github.emw7.platform.service.core.common.request.error;

import com.github.emw7.platform.i18n.I18nLabel;

public final class ServiceCoreCommonRequestErrorConstants {

  //public static final String I18N_LABEL_REQUEST_PREFIX= I18nLabelPrefixes.PLATFORM_PREFIX + "request.";

  // TODO doc
  @I18nLabel(params = {})
  public static final String DEFAULT_ERROR_LABEL = "com.github.emw7.platform.i18n.error.request.generic-error";

  /**
   * TODO doc
   * <a href="https://en.wikipedia.org/wiki/Hyper_Text_Coffee_Pot_Control_Protocol">...</a>
   */
  public static final int DEFAULT_CLIENT_ERROR_CODE = 418;

  /**
   * TODO doc
   */
  public static final int SERVER_ERROR_CODE = 500;

  @I18nLabel(params= {})
  public static final String SERVER_ERROR_LABEL = "com.github.emw7.platform.i18n.error.request.server.internal-server-error";

}
