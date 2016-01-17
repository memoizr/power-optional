package com.memoizrlabs.poweroptional;

import com.memoizrlabs.functions.Action0;
import com.memoizrlabs.functions.Action1;
import com.memoizrlabs.functions.Func0;
import com.memoizrlabs.functions.Func1;
import com.memoizrlabs.functions.Predicate;

import java.io.Serializable;

/**
 * A wrapper class that wraps non-null values that may or may not be there. As this wrapper acts as a
 * monad, it is possible to safely perform chained operations on wrapped values that may or may not exist.
 *
 * As this is a value-based class, equals and hashcode operations are simply forwarded to the wrapped object.
 * This may cause unexpected behavior for some identity-sensitive operations, therefore equals and hascode operations
 * should generally be avoided on wrappers.
 *
 * @param <T> the type optionOf the wrapped value.
 */
public abstract class Optional<T> implements Serializable {

    private static final Empty<?> EMPTY = new Empty<>();

    /**
     * Wraps a value in a non-empty wrapper if the value is non-null and returns the wrapped value,
     * returns an empty wrapper in case the value is null.
     *
     * @param value the nullable value to wrap.
     * @param <T>   the type optionOf the value to be wrapped.
     * @return the wrapped value or an empty wrapper if the value is null.
     */
    public static <T> Optional<T> optionOf(T value) {
        if (value == null) {
            return empty();
        } else {
            return new Some<>(value);
        }
    }

    /**
     * Returns an empty instance to wrap missing values.
     *
     * @param <T> the type optionOf the missing value.
     * @return the empty wrapper.
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    /**
     * Extracts the value from the wrapper. Throws a NoSuchElementException if
     * performed on an empty wrapper.
     *
     * @return the unwrapped value.
     */
    public abstract T get();

    /**
     * Returns a wrapped object obtained from a function if performed on a non-empty wrapper.
     *
     * @param function the factory function that generates the object.
     * @param <S>      the type optionOf the transformed object;
     * @return the transformed object or an empty wrapper if executed on an empty-wrapper.
     */
    public abstract <S> Optional<S> map(Func1<? super T, S> function);

    /**
     * If the predicate evaluates to true, returns the instance, otherwise returns an empty
     * wrapper.
     *
     * @param predicate the predicate function to evaluated.
     * @return the same instance optionOf the wrapped object.
     */
    public abstract Optional<T> filter(Predicate<? super T> predicate);

    /**
     * If performed on a non-empty wrapper, returns the wrapped object returned by the specified
     * function. Otherwise returns an empty wrapper.
     *
     * @param function the function that returns another wrapped object.
     * @param <S>      the type optionOf the new returned object.
     * @return the wrapped object returned by the function.
     */
    public abstract <S> Optional<S> flatMap(Func1<? super T, Optional<S>> function);

    /**
     * Tells whether this wrapper is non-empty or empty.
     *
     * @return true if the wrapper is non-empty, false if empty.
     */
    public abstract boolean isPresent();

    /**
     * Tells whether this wrapper is non-empty or empty.
     *
     * @return true if the wrapper is empty, false if non-empty.
     */
    public final boolean isEmpty() {
        return !isPresent();
    }

    /**
     * Calls a function if called on a non-empty wrapper.
     *
     * @param action1 the function to call.
     */
    public abstract Optional<T> doIfPresent(Action1<T> action1);

    /**
     * Calls a function if called on an empty wrapper.
     *
     * @param action the function to call.
     */
    public abstract Optional<T> doIfEmpty(Action0 action);

    /**
     * Returns the unwrapped value if called on a non-empty wrapper, or returns the specified value if called
     * on an empty wrapper.
     *
     * @param alternative the value to return in case the wrapper is empty.
     * @return either the unwrapped value or the specified one.
     */
    public abstract T orElse(T alternative);

    /**
     * Returns the unwrapped value if called on a non-empty wrapper, or returns the value returned by the
     * specified function if called on an empty wrapper.
     *
     * @param function the unwrapped value or the specified one.
     * @return either the unwrapped value or the value obtained from the specified function.
     */
    public abstract T orElseGet(Func0<T> function);

    /**
     * Returns the unwrapped valued if called on a non-empty wrapper, or throws the exeception returned by the
     * specified function if called on an empty wrapper.
     *
     * @param function the function that returns the exception in case the wrapper is empty.
     * @param <X>      the type optionOf the exception
     * @return the exception to throw
     * @throws X the thrown exception.
     */
    public abstract <X extends Throwable> T orElseThrow(Func0<X> function) throws X;

}
