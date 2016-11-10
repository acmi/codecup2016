/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.binder;

public interface ConstantBindingBuilder {
    public void to(String var1);

    public void to(int var1);

    public void to(long var1);

    public void to(boolean var1);

    public void to(double var1);

    public void to(float var1);

    public void to(short var1);

    public void to(char var1);

    public void to(byte var1);

    public void to(Class<?> var1);

    public <E extends Enum<E>> void to(E var1);
}

