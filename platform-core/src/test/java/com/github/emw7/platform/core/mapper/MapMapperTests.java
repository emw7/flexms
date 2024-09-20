package com.github.emw7.platform.core.mapper;

import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapMapperTests {

  //region Private constants
  private static final String MAP_NULL = "null";
  private static final String MAP_EMPTY = "{}";
  //endregion Private constants

  //region Tests

  //region NON-Sorted
  @Test
  public void givenNull_whenToString_thenStringNull() {
    final String s = MapMapper.mapToString(null);
    Assertions.assertThat(s).isEqualTo(MAP_NULL);
  }

  @Test
  public void givenEmpty_whenToString_thenStringEmpty() {
    final String s = MapMapper.mapToString(java.util.Map.of());
    Assertions.assertThat(s).isEqualTo(MAP_EMPTY);
  }

  @Test
  public void givenOneEntry_whenToString_thenStringOneEntry() {
    final String s = MapMapper.mapToString(java.util.Map.of("a", "s"));
    Assertions.assertThat(s).isEqualTo("{a=s}");
  }
  //endregion NON-Sorted

  //region SortedByKey
  // must test as NON-sorted as code is duplicated.
  @Test
  public void givenNull_whenToStringSortedByKey_thenStringNull() {
    final String s = MapMapper.mapToStringSortedByKey(null);
    Assertions.assertThat(s).isEqualTo(MAP_NULL);
  }

  @Test
  public void givenEmpty_whenToStringSortedByKey_thenStringEmpty() {
    final Map<String, ?> sut= java.util.Map.of();
    final String s = MapMapper.mapToStringSortedByKey(sut);
    Assertions.assertThat(s).isEqualTo(MAP_EMPTY);
  }

  @Test
  public void givenOneEntry_whenToStringSortedByKey_thenStringOneEntry() {
    final String s = MapMapper.mapToStringSortedByKey(java.util.Map.of("a", "s"));
    Assertions.assertThat(s).isEqualTo("{a=s}");
  }

  // cannot test with NON-sorted version as order is unpredictable.
  @Test
  public void givenManyEntriesWithIntegerKey_whenToStringSortedByKey_thenStringManyEntries() {
    final String s = MapMapper.mapToStringSortedByKey(
        java.util.Map.of(1, false, 2, 0L, 3, "a", 4, 2.71828182845D, 5, 1, 6, "b", 7, 3.14F, 8, true));
    Assertions.assertThat(s)
        .isEqualTo("{1=false, 2=0, 3=a, 4=2.71828182845, 5=1, 6=b, 7=3.14, 8=true}");
  }

  // cannot test with NON-sorted version as order is unpredictable.
  @Test
  public void givenManyEntriesWithStringKey_whenToStringSortedByKey_thenStringManyEntries() {
    final String s = MapMapper.mapToStringSortedByKey(
        java.util.Map.of("h", false, "a", 0L, "b", "a", "c", 2.71828182845D, "d", 1, "e", "b", "f", 3.14F, "g", true));
    Assertions.assertThat(s)
        .isEqualTo("{a=0, b=a, c=2.71828182845, d=1, e=b, f=3.14, g=true, h=false}");
  }

  // cannot test with NON-sorted version as order is unpredictable.
  // null key and value
  @Test
  public void givenNullKeysAndValues_whenToStringSortedByKey_thenSStringManyEntries() {
    final Map<String,String> sut= new HashMap<>();
    sut.put(null, "va");
    sut.put("kb", null);
    sut.put("kc", null);
    sut.put("kd", "vd");
    final String s = MapMapper.mapToStringSortedByKey(sut);
    Assertions.assertThat(s).isEqualTo("{null=va, kb=null, kc=null, kd=vd}");
  }

  //endregion SortedByKey
  //endregion Tests

}
