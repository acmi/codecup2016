/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.internal.util.StackTraceElements;
import com.google.inject.spi.ModuleSource;
import java.util.List;

public final class ElementSource {
    final ElementSource originalElementSource;
    final ModuleSource moduleSource;
    final StackTraceElements.InMemoryStackTraceElement[] partialCallStack;
    final Object declaringSource;

    ElementSource(ElementSource elementSource, Object object, ModuleSource moduleSource, StackTraceElement[] arrstackTraceElement) {
        Preconditions.checkNotNull(object, "declaringSource cannot be null.");
        Preconditions.checkNotNull(moduleSource, "moduleSource cannot be null.");
        Preconditions.checkNotNull(arrstackTraceElement, "partialCallStack cannot be null.");
        this.originalElementSource = elementSource;
        this.declaringSource = object;
        this.moduleSource = moduleSource;
        this.partialCallStack = StackTraceElements.convertToInMemoryStackTraceElement(arrstackTraceElement);
    }

    public ElementSource getOriginalElementSource() {
        return this.originalElementSource;
    }

    public Object getDeclaringSource() {
        return this.declaringSource;
    }

    public List<String> getModuleClassNames() {
        return this.moduleSource.getModuleClassNames();
    }

    public List<Integer> getModuleConfigurePositionsInStackTrace() {
        int n2 = this.moduleSource.size();
        Integer[] arrinteger = new Integer[n2];
        int n3 = this.partialCallStack.length;
        arrinteger[0] = n3 - 1;
        ModuleSource moduleSource = this.moduleSource;
        for (int i2 = 1; i2 < n2; ++i2) {
            n3 = moduleSource.getPartialCallStackSize();
            arrinteger[i2] = arrinteger[i2 - 1] + n3;
            moduleSource = moduleSource.getParent();
        }
        return ImmutableList.copyOf(arrinteger);
    }

    public StackTraceElement[] getStackTrace() {
        int n2 = this.moduleSource.getStackTraceSize();
        int n3 = this.partialCallStack.length;
        int n4 = this.moduleSource.getStackTraceSize() + n3;
        StackTraceElement[] arrstackTraceElement = new StackTraceElement[n4];
        System.arraycopy(StackTraceElements.convertToStackTraceElement(this.partialCallStack), 0, arrstackTraceElement, 0, n3);
        System.arraycopy(this.moduleSource.getStackTrace(), 0, arrstackTraceElement, n3, n2);
        return arrstackTraceElement;
    }

    public String toString() {
        return this.getDeclaringSource().toString();
    }
}

