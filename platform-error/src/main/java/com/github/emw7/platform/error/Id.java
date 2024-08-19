package com.github.emw7.platform.error;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

public final class Id {

  private static final Logger logger = LoggerFactory.getLogger(Id.class);

  private static final byte LENGTH = 5;

  private final String id;

  /**
   * Represents an id.
   * <p>
   * {@code id} should be not null and with length in 1..{@value LENGTH}; it is left padded with
   * {@code 0}. If it does not respect those constraints, it is accepted and a warn log is emitted.
   * <pre>
   *   id = null => forced to 0..0
   *   id = "" => forced to 0..0
   *   id = abc => forced to 0..0abc
   *   length(id) > 5 (example abcdefghij) => kept as it is (abcdefghij)
   * </pre>
   *
   * @param id the id; to be generated with
   *           https://www.random.org/strings/?num=1&len=5&digits=on&upperalpha=on&unique=on&format=html&rnd=new
   */
  public Id(@NonNull final String id) {
    this.id = StringUtils.leftPad(id == null ? "" : id, LENGTH, '0');
  }

  @Override
  public @NonNull String toString() {
    return id;
  }

}
