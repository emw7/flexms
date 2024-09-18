package com.github.emw7.platform.core.map;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

class MapUtilTest {

  @Test
  void removeNoneFromNullGivesNull() {
    final Map<Integer, String> sut= MapUtil.remove(null);
    Assertions.assertThat(sut).as("remove none from null gives null").isNull();
  }

  @Test
  void removeAnyFromNullGivesNull() {
    final Map<Integer, String> sut= MapUtil.remove(null, 1);
    Assertions.assertThat(sut).as("remove any from null gives null").isNull();
  }

  @Test
  void removeFirst() {
    final Map<Integer, String> sut= MapUtil.remove(Map.of(1, "one", 2, "two"),1);
    Assertions.assertThat(sut).as("remove first").containsExactly(MapEntry.entry(2,"two"));
  }

  @Test
  void removeLast() {
    final Map<Integer, String> sut= MapUtil.remove(Map.of(1, "one", 2, "two"),2);
    Assertions.assertThat(sut).as("remove last").containsExactly(MapEntry.entry(1,"one"));
  }

  @Test
  void removeNoneGivesSameOfSourceInAnyOrder() {
    final Map<Integer, String> sut= MapUtil.remove(Map.of(1, "one", 2, "two"));
    Assertions.assertThat(sut).as("remove nome gives same of source in any order").containsExactlyInAnyOrderEntriesOf(Map.of(1, "one", 2, "two"));
  }

  @Test
  void removeAllGivesEmpty() {
    final Map<Integer, String> sut= MapUtil.remove(Map.of(1, "one", 2, "two"), 2, 1);
    Assertions.assertThat(sut).as("only 2 entry left").isEmpty();
  }

  @Test
  void keepNoneFromNullGivesNull() {
    final Map<Integer, String> sut= MapUtil.keep(null);
    Assertions.assertThat(sut).as("keep none from null gives null").isNull();
  }

  @Test
  void keepAnyFromNullGivesNull() {
    final Map<Integer, String> sut= MapUtil.keep(null, 1);
    Assertions.assertThat(sut).as("keep any from null gives null").isNull();
  }

  @Test
  void keepFirst() {
    final Map<Integer, String> sut= MapUtil.keep(Map.of(1, "one", 2, "two"),1);
    Assertions.assertThat(sut).as("keep first").containsExactly(MapEntry.entry(1,"one"));
  }

  @Test
  void keepLast() {
    final Map<Integer, String> sut= MapUtil.keep(Map.of(1, "one", 2, "two"),2);
    Assertions.assertThat(sut).as("keep last").containsExactly(MapEntry.entry(2,"two"));
  }

  @Test
  void keepNoneGivesEmpty() {
    final Map<Integer, String> sut= MapUtil.keep(Map.of(1, "one", 2, "two"));
    Assertions.assertThat(sut).as("keep none gives empty").isEmpty();
  }

  @Test
  void keepAllGivesSameOfSourceInAnyOrder() {
    final Map<Integer, String> sut= MapUtil.keep(Map.of(1, "one", 2, "two"), 2, 1);
    Assertions.assertThat(sut).as("keep all gives same of source in any order").containsExactlyInAnyOrderEntriesOf(Map.of(1, "one", 2, "two"));
  }
}