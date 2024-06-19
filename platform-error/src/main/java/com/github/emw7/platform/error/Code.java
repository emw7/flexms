package com.github.emw7.platform.error;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

public final class Code {

  private static final byte LENGTH = 5;

  private final String code;

  public Code(@NonNull final String code) {
    // id == null it is a guard for bad developers.
    this.code = StringUtils.leftPad(code == null ? "" : code, LENGTH, '0');
  }

  @Override
  public String toString() {
    return code;
  }

}
