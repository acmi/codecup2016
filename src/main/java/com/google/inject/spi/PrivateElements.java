/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.spi.Element;
import java.util.List;
import java.util.Set;

public interface PrivateElements
extends Element {
    public List<Element> getElements();

    public Injector getInjector();

    public Set<Key<?>> getExposedKeys();

    public Object getExposedSource(Key<?> var1);
}

