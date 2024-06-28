package com.github.emw7.platform.core.mapper;

import java.util.Map.Entry;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class MapMapper {

  //region Private static methods

  /**
   * Appends to the provided string builder the string representation of the specified map's entry.
   * <p>
   * The string representation of an entry is the {@code "key::toString=value::toString"} string. If
   * any is {@code null} then it is represented by the {@code "null"} string.
   *
   * @param sb  the string builder to which append the string presentation of the entry
   * @param e   the entry which string representation must be appended to the string builder
   * @param <K>
   * @param <V>
   */
  private static <K, V> void map(@NonNull final StringBuilder sb,
      @NonNull final java.util.Map.Entry<K, V> e) {
    K key = e.getKey();
    V value = e.getValue();
    sb.append((key == null) ? "null" : key.toString()).append('=')
        .append(Optional.ofNullable(value).map(V::toString).orElse("null"))
        // using 2 appends instead of single append(", ") because I saw doing like this in
        //  java.util.AbstractMap.toString.
        .append(',').append(' ');
  }
  //endregion Private static methods

  //region API
  //region Sorted

  /**
   * Returns the string representation of the specified map where keys are sorted.
   * <p>
   * The method behaves as {@link #mapToString(java.util.Map)} but key ares sorted.
   *
   * @param map the map for which string representation is wanted
   * @param <K> the key type
   * @param <V> the value type
   * @return the string representation of the specified map
   */
  public static @NonNull <K extends Comparable<K>, V> String mapToStringSortedByKey(
      @Nullable final java.util.Map<K, V> map) {
    if (map == null) {
      return "null";
    } else if (map.isEmpty()) {
      return "{}";
    }
    // else... map has elements.
    final StringBuilder sb = new StringBuilder(map.size() * 64);
    sb.append('{');

    return map.entrySet().stream().sorted(Entry.comparingByKey())
        .collect(() -> sb, MapMapper::map, (a, b) -> a.append(b.toString()))
        .replace(sb.length() - 2, sb.length(), "}").toString();
  }
  //endregion Sorted

  //region Unsorted

  /**
   * Returns the string representation of the specified map.
   * <p>
   * Output:
   * <pre>
   *   map = null => "null"
   *   map is empty => "{}"
   *   otherwise => "{" &lt;entry&gt; { "," &lt;entry&gt; } "}"
   *                &lt;entry&gt; ::= &lt;key&gt; "=" &lt;value&gt;
   *                &lt;key&gt; ::= entry.key::toString | "null"
   *                &lt;value&gt; ::= entry.value::toString | "null"
   * </pre>
   * Example:
   * <pre>
   * The following code
   *
   * final Map<String, Object> map= Map.of("a","string", "b",3.14F, "c",1, "d",true);
   * System.out.println(MapToString.mapToString(map))
   *
   * outputs
   * {a=string, b=3.14, c=1, d=true}
   * </pre>
   *
   * @param map the map for which string representation is wanted
   * @param <K> the key type
   * @param <V> the value type
   * @return the string representation of the specified map
   */
  public static @NonNull <K, V> String mapToString(@Nullable final java.util.Map<K, V> map) {
    if (map == null) {
      return "null";
    } else if (map.isEmpty()) {
      return "{}";
    }
    // else... map has elements.
    final StringBuilder sb = new StringBuilder(map.size() * 64);
    sb.append('{');

    return map.entrySet().stream()
        .collect(() -> sb, MapMapper::map, (a, b) -> a.append(b.toString()))
        .replace(sb.length() - 2, sb.length(), "}").toString();
  }
  //endregion Unsorted
  //endregion API

  //region Constructors
  // prevents instantiation.
  private MapMapper() {
  }
  //endregion Constructors

}
