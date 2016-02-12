package com.thoughtworks.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;

public class Functional {
    public static <T, U> Optional<U> flatMap(Optional<T> maybeValue, final Function<T, Optional<U>> transformation) {
        return maybeValue.isPresent() ? transformation.apply(maybeValue.get()) : Optional.<U>absent();
    }
}
