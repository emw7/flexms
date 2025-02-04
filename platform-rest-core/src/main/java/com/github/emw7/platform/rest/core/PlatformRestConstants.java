package com.github.emw7.platform.rest.core;

import org.springframework.lang.NonNull;

public final class PlatformRestConstants {

  private static final String HEADER_NAME_PREFIX = "X-COM-GITHUB-EMW7";
  private static final char HEADER_NAME_PARTS_SEP = '-';

  /**
   * Constructs EMW7 platform header name by prefixing {@code headerName} with standard prefix.
   * <p>
   * Result is {@value HEADER_NAME_PREFIX}{@value HEADER_NAME_PARTS_SEP}{@code headerName}.
   *
   * @param headerName header name for which construct EMW7 platform header name.
   *
   * @return the constructed header name
   */
  private static String headerName(@NonNull final String headerName) {
    return HEADER_NAME_PREFIX + HEADER_NAME_PARTS_SEP + headerName;
  }


  //region Headers
  public static final String ORIGINATOR_TENANT_HEADER_NAME = headerName("ORIGINATOR-TENANT");
  public static final String ORIGINATOR_ID_HEADER_NAME = headerName("ORIGINATOR-ID");
  public static final String ORIGINATOR_LANG_HEADER_NAME = headerName("ORIGINATOR-LANG");
  public static final String ORIGINATOR_IS_SERVICE_HEADER_NAME = headerName("ORIGINATOR-IS-SERVICE");


  public static final String CALLER_TENANT_HEADER_NAME = headerName("CALLER-TENANT");
  public static final String CALLER_ID_HEADER_NAME = headerName("CALLER-ID");
  public static final String CALLER_LANG_HEADER_NAME = headerName("CALLER-LANG");
  public static final String CALLER_IS_SERVICE_HEADER_NAME = headerName("CALLER-IS-SERVICE");
  //endregion Headers

  //region Constructors
  // prevents instantiation.
  private PlatformRestConstants() {
  }
  //endregion Constructors

}
