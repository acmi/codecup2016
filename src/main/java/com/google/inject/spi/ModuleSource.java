/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.inject.internal.util.StackTraceElements;
import java.util.List;

final class ModuleSource {
    private final String moduleClassName;
    private final ModuleSource parent;
    private final StackTraceElements.InMemoryStackTraceElement[] partialCallStack;

    ModuleSource(Object object, StackTraceElement[] arrstackTraceElement) {
        this(null, object, arrstackTraceElement);
    }

    private ModuleSource(ModuleSource moduleSource, Object object, StackTraceElement[] arrstackTraceElement) {
        Preconditions.checkNotNull(object, "module cannot be null.");
        Preconditions.checkNotNull(arrstackTraceElement, "partialCallStack cannot be null.");
        this.parent = moduleSource;
        this.moduleClassName = object.getClass().getName();
        this.partialCallStack = StackTraceElements.convertToInMemoryStackTraceElement(arrstackTraceElement);
    }

    String getModuleClassName() {
        return this.moduleClassName;
    }

    StackTraceElement[] getPartialCallStack() {
        return StackTraceElements.convertToStackTraceElement(this.partialCallStack);
    }

    int getPartialCallStackSize() {
        return this.partialCallStack.length;
    }

    ModuleSource createChild(Object object, StackTraceElement[] arrstackTraceElement) {
        return new ModuleSource(this, object, arrstackTraceElement);
    }

    ModuleSource getParent() {
        return this.parent;
    }

    List<String> getModuleClassNames() {
        ImmutableList.Builder builder = ImmutableList.builder();
        ModuleSource moduleSource = this;
        while (moduleSource != null) {
            String string = moduleSource.moduleClassName;
            builder.add(string);
            moduleSource = moduleSource.parent;
        }
        return builder.build();
    }

    int size() {
        if (this.parent == null) {
            return 1;
        }
        return this.parent.size() + 1;
    }

    int getStackTraceSize() {
        if (this.parent == null) {
            return this.partialCallStack.length;
        }
        return this.parent.getStackTraceSize() + this.partialCallStack.length;
    }

    StackTraceElement[] getStackTrace() {
        int n2 = this.getStackTraceSize();
        StackTraceElement[] arrstackTraceElement = new StackTraceElement[n2];
        int n3 = 0;
        ModuleSource moduleSource = this;
        while (moduleSource != null) {
            StackTraceElement[] arrstackTraceElement2 = StackTraceElements.convertToStackTraceElement(moduleSource.partialCallStack);
            int n4 = arrstackTraceElement2.length;
            System.arraycopy(arrstackTraceElement2, 0, arrstackTraceElement, n3, n4);
            moduleSource = moduleSource.parent;
            n3 += n4;
        }
        return arrstackTraceElement;
    }
}

