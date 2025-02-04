package com.github.emw7.platform.core.collection;

import com.github.emw7.platform.core.function.Equality;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class ContainsExactlyInAnyOrder<T> {

  final Collection<T> actual;
  final Equality<T> equals;

  public ContainsExactlyInAnyOrder(@NonNull final Collection<T> actual, Equality<T> equality) {
    this.actual = actual;
    this.equals = equality;
  }

  private ArrayList<T> toArrayList(@NonNull final Collection<T> collection) {
    return new ArrayList<>(collection);
  }

  private T[] toArray(@NonNull final Collection<T> collection) {
    final ArrayList<T> arrayList = toArrayList(collection);
    return (T[]) arrayList.toArray();
  }


  public boolean test(@NonNull final Collection<T> expected) {
    if (actual.size() != expected.size()) {
      // If sizes are different, they cannot contain the same elements.
      return false;
    }
    // else... actual and expected have the same size.

    // This is used to save which expected have been already found to be equal to the ones in
    //  expected.
    // All elements are initialized to 0.
    final int[] consumedExpected = new int[actual.size()];

    // It is a copy org.assertj.core.util.IterableUtil.toArray(java.lang.Iterable<? extends T>)
    final T[] expectedArray = toArray(expected);
    try {
      actual.forEach(
          a -> {
            int p = containedIn(a, expectedArray, consumedExpected, equals);
            if (p >= 0) {
              consumedExpected[p] = 1;
            } else {
              throw new Different();
            }
          }
      );
    } catch (Different e) {
      return false;
    }

    return true;
  }

  // Returns the index of the expected element that is equals (by the equality criteria) to the
  //  actual element or -1 is such an element does not exist.
  //  Only not already consumed expected elements are visited.
  //  An expected element is consumed when it is found to be equal to an element of actual by
  //  equality criteria.
  //  Consumed elements have value 1 in the consumed array in their index.
  private int containedIn(@Nullable T actual, @NonNull final T[] expected, @NonNull final int[] e,
      @NonNull final Equality<T> equality) {
    int i = 0;
    // There are returns in the loop!
    while (i < expected.length) {
      if (e[i] == 0 && equality.test(actual, expected[i])) {
        return i;
      }
      i += 1;
    }
    return -1;
  }

  private static class Different extends RuntimeException {

  }
}
