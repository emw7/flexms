package com.github.emw7.platform.core.collection;

import java.util.Map;
import java.util.Map.Entry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContainsExactlyInAnyOrderTest {

  @Test
  public void testContainsExactlyInAnyOrder ()
  {
    final Map<String, Object> actual = Map.of("a", 1, "b", 2);
    final Map<String, Object> e1 = Map.of("b", 2, "a", 1);
    final Map<String, Object> e2 = Map.of("b", 1, "a", 2);
    final Map<String, Object> e3 = Map.of("a", 2, "b", 1);
    final Map<String, Object> e4 = Map.of("a", 1, "b", 2, "c", false);
    final Map<String, Object> e5 = Map.of("a", 1);

    final ContainsExactlyInAnyOrder<Map.Entry<String, Object>> test= new ContainsExactlyInAnyOrder<>(actual.entrySet(),
        Entry::equals);

    Assertions.assertThat(test.test(e1.entrySet())).as("true if same with different order")
        .isTrue();
    Assertions.assertThat(test.test(e2.entrySet())).as("false if keys differ")
        .isFalse();
    Assertions.assertThat(test.test(e3.entrySet())).as("false if values differ")
        .isFalse();
    Assertions.assertThat(test.test(e4.entrySet())).as("false actual has less elements")
        .isFalse();
    Assertions.assertThat(test.test(e5.entrySet())).as("false actual has more elements")
        .isFalse();
  }
}
