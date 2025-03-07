package com.github.emw7.platform.error;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

public final class Code {

  private static final byte LENGTH = 5;

  private final String code;

  // TODO copy doc from id

  /**
   * @param code to be generated with
   *             <a href="https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new">...</a>.
   */
  public Code(@NonNull final String code) {
    // id == null it is a guard for bad developers.
    this.code = StringUtils.leftPad(code, LENGTH, '0');
  }

  @Override
  public String toString() {
    return code;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Code code1 = (Code) o;
    return Objects.equals(code, code1.code);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(code);
  }
}
