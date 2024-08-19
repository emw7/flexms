package com.github.emw7.platform.error;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

public final class Code {

  private static final byte LENGTH = 5;

  private final String code;

  // TODO copy doc from id

  /**
   * @param code to be generated with
   *             https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new.
   */
  public Code(@NonNull final String code) {
    // id == null it is a guard for bad developers.
    this.code = StringUtils.leftPad(code == null ? "" : code, LENGTH, '0');
  }

  @Override
  public String toString() {
    return code;
  }

}
