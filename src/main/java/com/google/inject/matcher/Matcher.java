/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.matcher;

public interface Matcher<T> {
    public boolean matches(T var1);

    public Matcher<T> and(Matcher<? super T> var1);

    public Matcher<T> or(Matcher<? super T> var1);
}

