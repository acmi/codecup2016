/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binding;
import com.google.inject.Key;

public interface LinkedKeyBinding<T>
extends Binding<T> {
    public Key<? extends T> getLinkedKey();
}

