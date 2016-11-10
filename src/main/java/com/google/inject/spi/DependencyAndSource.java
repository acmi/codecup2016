/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.internal.util.StackTraceElements;
import com.google.inject.spi.Dependency;
import java.lang.reflect.Member;

public final class DependencyAndSource {
    private final Dependency<?> dependency;
    private final Object source;

    public DependencyAndSource(Dependency<?> dependency, Object object) {
        this.dependency = dependency;
        this.source = object;
    }

    public Dependency<?> getDependency() {
        return this.dependency;
    }

    public String getBindingSource() {
        if (this.source instanceof Class) {
            return StackTraceElements.forType((Class)this.source).toString();
        }
        if (this.source instanceof Member) {
            return StackTraceElements.forMember((Member)this.source).toString();
        }
        return this.source.toString();
    }

    public String toString() {
        Dependency dependency = this.getDependency();
        String string = this.getBindingSource();
        if (dependency != null) {
            return "Dependency: " + dependency + ", source: " + string;
        }
        return "Source: " + string;
    }
}

