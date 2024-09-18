package com.github.emw7.platform.core.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class MapUtil {

  //region API

  /**
   * Returns a new unmodifiable map obtained by removing the specified key from the provided map.
   * <p>
   * Returns {@code null} if provided the map is {@code null}.
   *
   * @param src the map from which remove the specified keys
   * @param keys the list of keys to be removed from the provided map
   * @return a new unmodifiable map obtained by removing the specified key from the provided map
   * @param <K> the type of keys maintained by the provided map
   * @param <V> the type of mapped values
   */
  @SafeVarargs
  public static <K, V> @Nullable /*unmodifiable*/ Map<K, V> remove(@Nullable final Map<K, V> src,
      @NonNull K... keys) {
    if (src == null) {
      return null;
    } else if (ArrayUtils.isEmpty(keys)) {
      // if the keys array is empty, then keep all that is remove nothing.
      return Map.copyOf(src);
    } else {
      final Map<K, V> ret = new HashMap<>(src);
      Arrays.stream(keys).forEach(ret::remove);
      return Collections.unmodifiableMap(ret);
    }
  }

  /**
   * Returns a new unmodifiable map obtained by keeping only the specified key from the provided map.
   * <p>
   * Returns {@code null} if the provided map is {@code null}.
   *
   * @param src the map from which keep the specified keys
   * @param keys the list of keys to be kept from the provided map
   * @return a new unmodifiable map obtained by keeping only the specified key from the provided map
   * @param <K> the type of keys maintained by the provided map
   * @param <V> the type of mapped values
   */
  @SafeVarargs
  public static <K, V> @Nullable /*unmodifiable*/ Map<K, V> keep(@Nullable final Map<K, V> src, @NonNull K... keys) {
    if (src == null) {
      return null;
    } else if (ArrayUtils.isEmpty(keys)) {
      // if the key array is empty, then remove all that is keep nothing.
      return Map.of();
    } else {
      final Map<K, V> ret = new HashMap<>(src.size());
      Arrays.stream(keys).forEach(key -> {
        if (src.containsKey(key)) {
          ret.put(key, src.get(key));
        }
      });
      return Collections.unmodifiableMap(ret);
    }
  }
  //endregion API

  //region Constructors
  // prevents instantiation.
  private MapUtil() {
  }
  //endregion Constructors

}
