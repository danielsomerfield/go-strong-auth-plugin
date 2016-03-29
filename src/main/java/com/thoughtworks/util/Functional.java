package com.thoughtworks.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;

public class Functional {
    public static <T, U> Optional<U> flatMap(Optional<? extends T> maybeValue, final Function<T, Optional<U>> transformation) {
        return maybeValue.isPresent() ? transformation.apply(maybeValue.get()) : Optional.<U>absent();
    }

    public static <T, U, V> Optional<V> flatMap(Optional<T> maybeValue,
                                                final Function<T, Optional<U>> transformation1,
                                                final Function<U, Optional<V>> transformation2
    ) {
        return flatMap(flatMap(maybeValue, transformation1), transformation2);
    }
}
