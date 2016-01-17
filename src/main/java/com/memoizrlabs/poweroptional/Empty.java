package com.memoizrlabs.poweroptional;

import com.memoizrlabs.functions.Action0;
import com.memoizrlabs.functions.Action1;
import com.memoizrlabs.functions.Func0;
import com.memoizrlabs.functions.Func1;
import com.memoizrlabs.functions.Predicate;

import java.util.NoSuchElementException;

final class Empty<T> extends Optional<T> {

    Empty() {
    }

    @Override
    public T get() {
        throw new NoSuchElementException();
    }

    @Override
    public <S> Optional<S> map(Func1<? super T, S> function) {
        return empty();
    }

    @Override
    public <S> Optional<S> flatMap(Func1<? super T, Optional<S>> function) {
        return empty();
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public Optional<T> filter(Predicate<? super T> predicate) {
        return empty();
    }

    @Override
    public Optional<T> doIfPresent(Action1<T> action1) {
        return this;
    }

    @Override
    public Optional<T> doIfEmpty(Action0 action) {
        action.call();
        return this;
    }

    @Override
    public T orElse(T alternative) {
        return alternative;
    }

    @Override
    public T orElseGet(Func0<T> function) {
        return function.call();
    }

    @Override
    public <X extends Throwable> T orElseThrow(Func0<X> function) throws X {
        throw function.call();
    }

    @Override
    public String toString() {
        return "Empty option";
    }
}
