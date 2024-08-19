package com.github.emw7.platform.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class I18nLabelPrefixes {

  //region Public static properties
  public static final String PLATFORM_PREFIX = "com.github.emw7.platform.";
  //endregion Public static properties

  //region Private properties
  private final String platformPrefix;
  private final String customPrefix;
  //endregion Private properties

  //region Constructors
  public I18nLabelPrefixes(@Nullable final String customPrefix) {
    this.platformPrefix = PLATFORM_PREFIX;
    if (StringUtils.isEmpty(customPrefix) ) {
      this.customPrefix= "";
    }
    else {
      this.customPrefix = customPrefix + (customPrefix.endsWith(".'") ? "" : '.');
    }
  }
  //endregion Constructors

  //region Getters & Setters
  public String getPlatformPrefix() {
    return platformPrefix;
  }

  public String getCustomPrefix() {
    return customPrefix;
  }
  //endregion Getters & Setters

}
