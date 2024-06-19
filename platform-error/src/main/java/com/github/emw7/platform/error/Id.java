package com.github.emw7.platform.error;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Id {

  private static final Logger logger= LoggerFactory.getLogger(Id.class);

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
   * @param id the id
   */
  public Id(@NonNull final String id) {
    if ( StringUtils.isEmpty(id) || id.length() > LENGTH )  {
      logger.warn("[ILLEGAL-ARGUMENT] provided id '{}' is not valid: must be not null and with length in 1..5", id);
    }
    // it's true that id should not be null because of @NonNull, but
    this.id = StringUtils.leftPad(Optional.ofNullable(id).orElse(""), LENGTH,'0');
  }

  @Override
  public @NonNull String toString() {
    return id;
  }

}
