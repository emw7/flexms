package com.github.emw7.platform.core.array;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayUtilTests {

  @Test
  public void givenBothNull_WhenJoin_thenNull() {
    final Object[] join = ArrayUtil.join(null, null);
    Assertions.assertThat(join).isNull();
  }

  @Test
  public void givenBNull_WhenJoin_thenA() {
    final Object[] join = ArrayUtil.join(new Object[]{false, 0L, "a", 2, 71828182845904523536D},
        null);
    Assertions.assertThat(join).containsOnly(false, 0L, "a", 2, 71828182845904523536D);
  }

  @Test
  public void givenANull_WhenJoin_thenB() {
    final Object[] join = ArrayUtil.join(null, new Object[]{1, "b", 3.14F, true});
    Assertions.assertThat(join).containsOnly(1, "b", 3.14F, true);
  }

  @Test
  public void givenBothNotNullNull_WhenJoin_thenAB() {
    final Object[] join = ArrayUtil.join(new Object[]{false, 0L, "a", 2.71828182845904523536D},
        new Object[]{1, "b", 3.14F, true});
    Assertions.assertThat(join)
        .containsOnly(false, 0L, "a", 2.71828182845904523536D, 1, "b", 3.14F, true);
  }

}
