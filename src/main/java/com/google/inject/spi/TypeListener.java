/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;

public interface TypeListener {
    public <I> void hear(TypeLiteral<I> var1, TypeEncounter<I> var2);
}

