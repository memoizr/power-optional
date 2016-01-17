package com.memoizrlabs.poweroptional;

import com.memoizrlabs.functions.Action0;
import com.memoizrlabs.functions.Action1;
import com.memoizrlabs.functions.Func0;
import com.memoizrlabs.functions.Func1;
import com.memoizrlabs.functions.Predicate;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.memoizrlabs.poweroptional.Optional.optionOf;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OptionalTest {

    private static final String CONTENT_TO_STRING = "Content description";

    @Test
    public void of_withNullValue_returnsEmpty() throws Exception {
        assertFalse(optionOf(null).isPresent());
    }

    @Test
    public void of_withValue_returnsWrappedValue() throws Exception {
        assertTrue(optionOf(new A())
            .isPresent());
    }

    @Test
    public void get_withValue_returnsValue() throws Exception {
        final A value = new A();

        assertEquals(value, optionOf(value)
            .get());
    }

    @Test(expected = NoSuchElementException.class)
    public void get_withoutValue_throwsException() throws Exception {
        Optional.empty().get();
    }

    @Test
    public void map_withValue_continuesTheChain() throws Exception {
        final A value = new A();
        final B other = new B();

        Func1<A, B> function = new Func1<A, B>() {
            @Override
            public B call(A a) {
                return other;
            }
        };

        Optional<B> optionalB = optionOf(value).map(function);

        assertEquals(other, optionalB.get());
    }

    @Test
    public void map_withoutValue_doesNotContinueTheChain() throws Exception {
        Optional.empty().map(new Func1<Object, Object>() {
            @Override
            public Object call(Object a) {
                fail();
                return null;
            }
        });
    }

    @Test
    public void flatMap_withValue_continuesTheChain() throws Exception {
        final A value = new A();
        final B other = new B();

        Func1<A, Optional<B>> monadicFunc = new Func1<A, Optional<B>>() {
            @Override
            public Optional<B> call(A a) {
                return optionOf(other);
            }
        };

        Optional<B> optionalB = optionOf(value).flatMap(monadicFunc);

        assertEquals(other, optionalB.get());
    }

    @Test
    public void flatMap_withNoValue_doesNotContinue() throws Exception {
        Optional.empty().flatMap(new Func1<Object, Optional<Object>>() {
            @Override
            public Optional<Object> call(Object a) {
                fail();
                return null;
            }
        });
    }

    @Test
    public void filterFilters_withValue_ifMatches_returnsWrappedValue() throws Exception {
        A value = new A();

        Optional<A> filteredOption = optionOf(value).filter(new Predicate<A>() {
            @Override
            public boolean verify(A a) {
                return true;
            }
        });

        assertEquals(value, filteredOption.get());
    }


    @Test
    public void filterFilters_withValue_ifNoMatch_returnsEmpty() throws Exception {
        Optional<A> filteredOption = optionOf(new A()).filter(new Predicate<A>() {
            @Override
            public boolean verify(A a) {
                return false;
            }
        });

        assertFalse(filteredOption.isPresent());
    }

    @Test
    public void filterFilters_withoutValue_returnsEmpty() throws Exception {

        Optional<A> filteredOption = Optional.<A>empty().filter(new Predicate<A>() {
            @Override
            public boolean verify(A a) {
                return false;
            }
        });

        assertFalse(filteredOption.isPresent());
    }

    @Test
    public void isPresent_withValue_returnsTrue() throws Exception {
        assertTrue(optionOf(new A())
            .isPresent());
    }

    @Test
    public void isPresent_withoutValue_returnsFalse() throws Exception {
        assertFalse(Optional.empty()
            .isPresent());
    }

    @Test
    public void isEmpty_withValue_returnsFalse() throws Exception {
        assertFalse(optionOf(new A())
                .isEmpty());
    }

    @Test
    public void isEmpty_withoutValue_returnsTrue() throws Exception {
        assertTrue(Optional.empty()
                .isEmpty());
    }

    @Test
    public void doIfPresent_withValue_executesFunc() throws Exception {
        A value = new A();

        final AtomicInteger integer = new AtomicInteger();

        Optional<A> optional = optionOf(value)
            .doIfPresent(new Action1<A>() {
                @Override
                public void call(A x) {
                    integer.incrementAndGet();
                }
            });

        assertEquals(1, integer.get());
        assertTrue(optional.isPresent());
    }

    @Test
    public void doIfPresentDoesnt_withoutValue_doesNotExecuteFunc() throws Exception {
        final Optional<Object> optional = Optional.empty()
                .doIfPresent(new Action1<Object>() {
                    @Override
                    public void call(Object x) {
                        fail();
                    }
                });
        assertFalse(optional.isPresent());
    }

    @Test
    public void doIfEmpty_withValue_doesNotExecuteFunc() throws Exception {
        A value = new A();

        final AtomicInteger integer = new AtomicInteger();

        Optional<A> optional = optionOf(value)
                .doIfEmpty(new Action0() {
                    @Override
                    public void call() {
                        integer.incrementAndGet();
                    }
                });

        assertEquals(0, integer.get());
        assertTrue(optional.isPresent());
    }

    @Test
    public void doIfEmpty_withoutValue_executesFunc() throws Exception {
        final AtomicInteger integer = new AtomicInteger();

        final Optional<Object> optional = Optional.empty()
                .doIfEmpty(new Action0() {
                    @Override
                    public void call() {
                        integer.incrementAndGet();
                    }
                });

        assertEquals(1, integer.get());
        assertFalse(optional.isPresent());
    }

    @Test
    public void orElse_withValue_returnsValue() throws Exception {
        A value = new A();
        A alternative = new A();

        assertEquals(value, optionOf(value)
            .orElse(alternative));
    }

    @Test
    public void orElse_withoutValue_returnsAlternative() throws Exception {
        A alternative = new A();

        assertEquals(alternative, Optional.empty()
            .orElse(alternative));
    }

    @Test
    public void orElseGet_withValue_returnsValue() throws Exception {
        final A value = new A();
        final A alternative = new A();

        assertEquals(value, optionOf(value)
            .orElseGet(new Func0<A>() {
                @Override
                public A call() {
                    return alternative;
                }
            }));
    }

    @Test
    public void orElseGet_withoutValue_returnsAlternative() throws Exception {
        final A alternative = new A();

        assertEquals(alternative, Optional.empty()
            .orElseGet(new Func0<Object>() {
                @Override
                public Object call() {
                    return alternative;
                }
            }));
    }

    @Test
    public void orElseThrow_withValue_returnsValue() throws Exception, AnException {
        A value = new A();

        assertEquals(value, optionOf(value)
            .orElseThrow(new Func0<AnException>() {
                @Override
                public AnException call() {
                    return new AnException();
                }
            }));
    }

    @Test(expected = AnException.class)
    public void orElseThrow_withoutValue_throwsException() throws Exception, AnException {
        Optional.empty()
            .orElseThrow(new Func0<AnException>() {
                @Override
                public AnException call() {
                    return new AnException();
                }
            });
    }

    @Test
    public void toString_withValue_printsTheValue() throws Exception {
        String s = optionOf(new A())
            .toString();

        assertTrue(s.contains(CONTENT_TO_STRING));
    }

    @Test
    public void toString_withoutValue_printsEmptyDescription() throws Exception {
        String s = Optional.empty()
            .toString();

        assertTrue(s.toLowerCase()
            .contains("empty"));
    }

    @Test
    public void equals_withValue_returnsTrueIfValueIsEqual() throws Exception {
        A value = new A();
        assertTrue(optionOf(value)
            .equals(optionOf(value)));
    }

    @Test
    public void hashcode_withValue_returnsValuesHashcode() throws Exception {
        A value = new A();
        assertEquals(value.hashCode(), optionOf(value).hashCode());
    }

    private static final class A {
        @Override
        public String toString() {
            return CONTENT_TO_STRING;
        }
    }

    private static final class B {
    }

    private static final class AnException extends Throwable {
    }
}

