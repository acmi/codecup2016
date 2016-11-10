/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.spi.ElementVisitor;

public interface Element {
    public Object getSource();

    public <T> T acceptVisitor(ElementVisitor<T> var1);

    public void applyTo(Binder var1);
}

