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
  @SafeVarargs
  public static <K, V> @Nullable Map<K, V> remove(@Nullable final Map<K, V> src,
      @NonNull K... keys) {
    if (src == null) {
      return null;
    } else if (ArrayUtils.isEmpty(keys)) {
      // if the keys array is empty, then keep all that is remove nothing.
      return Map.copyOf(src);
    } else {
      final Map<K, V> ret = new HashMap<>(src.size());
      Arrays.stream(keys).forEach(key -> {
        if (!src.containsKey(key)) {
          ret.put(key, src.get(key));
        }
      });
      return Collections.unmodifiableMap(ret);
    }
  }

  @SafeVarargs
  public static <K, V> @Nullable Map<K, V> keep(@Nullable final Map<K, V> src, @NonNull K... keys) {
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
