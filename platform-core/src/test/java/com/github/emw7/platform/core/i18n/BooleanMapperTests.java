package com.github.emw7.platform.core.i18n;

import com.github.emw7.platform.core.mapper.BooleanMapper;
import java.util.Locale;
import java.util.Random;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class BooleanMapperTests {

  @Test
  public void givenNull_whenFromString_thenFalse() {
    Assertions.assertThat(BooleanMapper.fromString(null))
        .as("when mapping boolean from null string then expect false").isFalse();
  }

  @Test
  public void givenEmpty_whenFromString_thenFalse() {
    Assertions.assertThat(BooleanMapper.fromString(""))
        .as("when mapping boolean from empty string then expect false").isFalse();
  }

  @Test
  public void givenAnyNotInTheTrueSet_whenFromString_thenFalse() {
    final int len = new Random().nextInt(1, 10);
    // https://www.ascii-code.com/ASCII: ASCII printable characters (character code 32-127)
    //  excluding 127 = delete.
    String b = null;
    while (b == null || isTrue(b)) {
      b = new Random().ints(len, 32, 127).
          collect(
              StringBuilder::new,
              StringBuilder::appendCodePoint,
              StringBuilder::append).toString();
  }
    Assertions.assertThat(BooleanMapper.fromString(b))
        .as("when mapping boolean from not-true string then expect false").isFalse();
  }

  // "true", "t", "1", "on", "yes", "y"
  @Test
  public void givenTRUE_whenFromString_thenTrue() {
    Assertions.assertThat(BooleanMapper.fromString("TRUE"))
        .as("when mapping boolean from TRUE string then expect false").isTrue();
  }

  @Test
  public void givent_whenFromString_thenTrue() {
    Assertions.assertThat(BooleanMapper.fromString("t"))
        .as("when mapping boolean from t string then expect false").isTrue();
  }

  @Test
  public void given1_whenFromString_thenTrue() {
    Assertions.assertThat(BooleanMapper.fromString("1"))
        .as("when mapping boolean from 1 string then expect false").isTrue();
  }

  @Test
  public void givenoN_whenFromString_thenTrue() {
    Assertions.assertThat(BooleanMapper.fromString("oN"))
        .as("when mapping boolean from oN string then expect false").isTrue();
  }

  @Test
  public void givenYes_whenFromString_thenTrue() {
    Assertions.assertThat(BooleanMapper.fromString("Yes"))
        .as("when mapping boolean from Yes string then expect false").isTrue();
  }

  @Test
  public void givenY_whenFromString_thenTrue() {
    Assertions.assertThat(BooleanMapper.fromString("Y"))
        .as("when mapping boolean from Y string then expect false").isTrue();
  }

  //region Private methods
  private boolean isTrue (@NonNull final String b) {
    return switch (b.toLowerCase(Locale.ROOT)) {
      case "true", "t", "1", "on", "yes", "y" -> true;
      default -> false;
    };
  }
  //endregion Private methods

}
