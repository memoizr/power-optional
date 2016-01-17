package com.memoizrlabs.poweroptional;

import com.memoizrlabs.functions.Action0;
import com.memoizrlabs.functions.Action1;
import com.memoizrlabs.functions.Func0;
import com.memoizrlabs.functions.Func1;
import com.memoizrlabs.functions.Predicate;

final class Some<T> extends Optional<T> {

    private final T value;

    Some(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public <S> Optional<S> map(Func1<? super T, S> function) {
        return optionOf(function.call(value));
    }

    @Override
    public Optional<T> filter(Predicate<? super T> predicate) {
        if (predicate.verify(value)) {
            return this;
        } else {
            return empty();
        }
    }

    @Override
    public <S> Optional<S> flatMap(Func1<? super T, Optional<S>> function) {
        return function.call(value);
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public Optional<T> doIfPresent(Action1<T> action1) {
        action1.call(value);
        return this;
    }

    @Override
    public Optional<T> doIfEmpty(Action0 action) {
        return this;
    }

    @Override
    public T orElse(T alternative) {
        return value;
    }

    @Override
    public T orElseGet(Func0<T> function) {
        return value;
    }

    @Override
    public <X extends Throwable> T orElseThrow(Func0<X> function) throws X {
        return value;
    }

    @Override
    public String toString() {
        return "Some {" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Some)) {
            return false;
        }

        final Some some = (Some) object;

        return value.equals(some.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
