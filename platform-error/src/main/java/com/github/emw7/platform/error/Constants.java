package com.github.emw7.platform.error;

import com.github.emw7.platform.i18n.I18nLabel;
import com.github.emw7.platform.i18n.I18nLabelPrefixes;

public final class Constants {

  public static final String I18N_LABEL_REQUEST_PREFIX= I18nLabelPrefixes.PLATFORM_PREFIX + "request.";

  // TODO doc
  @I18nLabel(label = "com.github.emw7.platform.generic-error")
  public static final String DEFAULT_ERROR_LABEL =  I18N_LABEL_REQUEST_PREFIX
    + "generic-error";

  /**
   * TODO doc
   * https://en.wikipedia.org/wiki/Hyper_Text_Coffee_Pot_Control_Protocol
   */
  public static final int DEFAULT_CLIENT_ERROR_CODE = 418;

  /**
   * TODO doc
   */
  public static final int SERVER_ERROR_CODE = 500;

  @I18nLabel(label = "com.github.emw7.platform.request.server-error")
  public static final String SERVER_ERROR_LABEL = I18N_LABEL_REQUEST_PREFIX
      + "server-error";

}
