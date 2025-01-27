package com.github.emw7.platform.error;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


// Foo is the name of the tests that does not concern the project itself but are related to
//  something to try and investigate.
@Disabled
public class Foo {

  private static @NonNull String mapToString(@Nullable final Map<String, Object> params) {

    class EntryMapper {

      public void map(StringBuilder sb, @NonNull final Entry<String, Object> e) {
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
                : params.entrySet().stream())
        .collect(() -> s, new EntryMapper()::map,
                 (a, b) -> a.append(b.toString())).replace(s.length() - 2, s.length(), "}").toString();
}

  /**
   * Tests the performance of converting a map to string via toString or via collecting its
   * elements through a stream.
   */
  @Test
  public void testFoo() {
    Map<String, Object> map = new HashMap<>();
    Random random = new Random(System.currentTimeMillis());
    for (int i = 1; i <= 10_000_000; i++) {
      map.put(StringUtils.leftPad(String.valueOf(i), 10, '0'), random.nextDouble());
    }

    long tstre=0;
    long tstri=0;
    long t;

    for (int it = 0; it < 10; it++) {
      t = System.nanoTime();
      mapToString(map);
      t = System.nanoTime() - t;
      tstre+= t;
      System.out.println("stream t: " + Duration.ofNanos(t).toMillis());

      t = System.nanoTime();
      map.toString();
      t = System.nanoTime() - t;
      tstri+= t;
      System.out.println("toString t: " + Duration.ofNanos(t).toMillis());
    }

    System.out.printf("stream tt: %d, avg: %d%n", Duration.ofNanos(tstre).toMillis(),  Duration.ofNanos(tstre/10).toMillis());
    System.out.printf("toString tt: %d, avg: %d%n", Duration.ofNanos(tstri).toMillis(),  Duration.ofNanos(tstri/10).toMillis());

    //System.out.println(s);
  }

}
