package com.jonassigel;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utility class for something Java should have itself: List-Composable
 * functions. Multi-Type would be nice, but not very feasible tbh
 */
public class Util {
    public static <T> Function<T, T> compose(List<Function<T, T>> funcs) {
        return funcs.stream().reduce(Function.identity(), Function::andThen);
    }

    public static <T> Function<T, T> compose(Stream<Function<T, T>> funcs) {
        return funcs.reduce(Function.identity(), Function::andThen);
    }
}
