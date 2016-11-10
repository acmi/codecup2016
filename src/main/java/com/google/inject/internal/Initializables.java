/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.Initializable;

final class Initializables {
    Initializables() {
    }

    static <T> Initializable<T> of(final T t2) {
        return new Initializable<T>(){

            @Override
            public T get(Errors errors) throws ErrorsException {
                return (T)t2;
            }

            public String toString() {
                return String.valueOf(t2);
            }
        };
    }

}

