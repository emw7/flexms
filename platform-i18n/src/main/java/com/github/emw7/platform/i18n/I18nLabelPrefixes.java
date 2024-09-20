package com.github.emw7.platform.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * A class that stores the prefix for the standard EMW7 platform's labels and a custom prefix
 * that the application can configure.
 * <p>
 * The idea behind this class is that:
 * <ul>
 * <li>All the labels of EMW7 platform are prefixed by a non-modifiable prefix specified by
 * {@link #PLATFORM_PREFIX} that is returned by {@link #getPlatformPrefix()} as well.</li>
 * <li>Application can define a custom prefix that is is used by EMW7 internationalisation framework
 * to override messages provided by EMW7 platform.</li>
 * </ul>
 * <p>
 * A bean of this class is instantiated by
 * {@link com.github.emw7.platform.i18n.autoconfig.PlatformI18nAutoConfig#i18nLabelPrefixes(String)}
 * to which you are referred for further details.<br/>
 * For a detailed description of EMW7 internationalisation framework, please refer to
 * <a href="https://github.com/emw7/flexms/blob/main/README.md">EMW7 reference
 * documentation</a>.
 */
public final class I18nLabelPrefixes {

  //region Public static properties
  /**
   * The standard EMW7 platform's label prefix.
   * <p>
   * Please note the trailing dot (.).
   */
  public static final String PLATFORM_PREFIX = "com.github.emw7.platform.";
  //endregion Public static properties

  //region Private properties
  private final String platformPrefix;
  private final String customPrefix;
  //endregion Private properties

  //region Constructors
  /**
   * Constructs a class with the supplied custom prefix.
   * <p>
   * <b>Note</b>: a trailing dot (.) is added in case if it is not present.
   *
   * @param customPrefix the custom prefix with which application can override the EMW7 platform
   *                     messages.
   */
  public I18nLabelPrefixes(@Nullable final String customPrefix) {
    this.platformPrefix = PLATFORM_PREFIX;
    if (StringUtils.isEmpty(customPrefix) ) {
      this.customPrefix= "";
    }
    else {
      this.customPrefix = customPrefix + (customPrefix.endsWith(".") ? "" : '.');
    }
  }
  //endregion Constructors

  //region Getters & Setters
  /**
   * Returns the standard EMW7 platform's label prefix.
   *
   * @return the standard EMW7 platform's label prefix
   */
  public String getPlatformPrefix() {
    return platformPrefix;
  }

  /**
   * Returns the custom prefix as supplied by the application.
   * <p>
   * <b>Note</b>: constructor added a trailing dot (.) if it was not already present.
   *
   * @return the custom prefix as supplied by the application
   */
  public String getCustomPrefix() {
    return customPrefix;
  }
  //endregion Getters & Setters

}
