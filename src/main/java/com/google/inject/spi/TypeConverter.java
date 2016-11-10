/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.TypeLiteral;

public interface TypeConverter {
    public Object convert(String var1, TypeLiteral<?> var2);
}

