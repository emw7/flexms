package com.github.emw7.platform.core.function;

import java.util.function.BiPredicate;

/**
 * A {@link BiPredicate} on the same type.
 *
 * @param <T> the type of the objects to be checked for equality
 */
@FunctionalInterface
public interface Equality<T> extends BiPredicate<T, T> {

}
