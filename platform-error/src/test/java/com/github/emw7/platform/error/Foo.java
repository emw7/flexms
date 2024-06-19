package com.github.emw7.platform.error;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class Foo {

  private static @NonNull String mapToString(@Nullable final Map<String, Object> params) {

    class EntryMapper {

      public void map(StringBuilder sb, @Nullable final String key) {
        sb.append((key == null) ? "null" : key).append('=')
            .append(Optional.ofNullable(params.get(key)).map(Object::toString).orElse("null"))
            .append(',').append(' ');
      }
    }

    class EntryMapper2 {

      public void map2(StringBuilder sb, @NonNull final Entry<String, Object> e) {
        String key = e.getKey();
        Object value = e.getValue();
        sb.append((key == null) ? "null" : key).append('=')
            .append(Optional.ofNullable(value).map(Object::toString).orElse("null"))
            .append(',').append(' ');
      }
    }

    if (params == null) {
      return "null";
    } else if (params.isEmpty()) {
      return "{}";
    }
    // else... params has elements.
    StringBuilder s = new StringBuilder(params.size() * 64);
    s.append('{');

    boolean t = false;

    return ((t) ? params.entrySet().stream().sorted(Entry.comparingByKey())
        : params.entrySet().stream().unordered()).collect(() -> s, new EntryMapper2()::map2,
        (a, b) -> a.append(b.toString())).replace(s.length() - 2, s.length(), "}").toString();

//    return params.keySet().stream()/*.sorted()*/
//        .collect(() -> s, new EntryMapper()::map, (a,b) -> a.append(b.toString())).replace(s.length()-2,s.length(),"}").toString();
  }

  @Test
  public void testFoo() {
    Map<String, Object> map = new HashMap<>();
    Random random = new Random(System.currentTimeMillis());
    for (int i = 1; i <= 10000000; i++) {
      map.put(StringUtils.leftPad(String.valueOf(i), 10, '0'), random.nextDouble());
    }
    long t;

    for (int it = 0; it < 10; it++) {
      t = System.nanoTime();
      mapToString(map);
      t = System.nanoTime() - t;
      System.out.println("stream t: " + Duration.ofNanos(t).toMillis());

      t = System.nanoTime();
      Collections.unmodifiableMap(map).toString();
      t = System.nanoTime() - t;
      System.out.println("toString t: " + Duration.ofNanos(t).toMillis());
    }

    //System.out.println(s);
  }

}
