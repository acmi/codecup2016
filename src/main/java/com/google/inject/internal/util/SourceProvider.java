/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public final class SourceProvider {
    public static final Object UNKNOWN_SOURCE = "[unknown source]";
    private final SourceProvider parent;
    private final ImmutableSet<String> classNamesToSkip;
    public static final SourceProvider DEFAULT_INSTANCE = new SourceProvider(ImmutableSet.of(SourceProvider.class.getName()));

    private SourceProvider(Iterable<String> iterable) {
        this(null, iterable);
    }

    private SourceProvider(SourceProvider sourceProvider, Iterable<String> iterable) {
        this.parent = sourceProvider;
        ImmutableSet.Builder builder = ImmutableSet.builder();
        for (String string : iterable) {
            if (sourceProvider != null && sourceProvider.shouldBeSkipped(string)) continue;
            builder.add(string);
        }
        this.classNamesToSkip = builder.build();
    }

    public /* varargs */ SourceProvider plusSkippedClasses(Class ... arrclass) {
        return new SourceProvider(this, SourceProvider.asStrings(arrclass));
    }

    private boolean shouldBeSkipped(String string) {
        return this.parent != null && this.parent.shouldBeSkipped(string) || this.classNamesToSkip.contains(string);
    }

    private static /* varargs */ List<String> asStrings(Class ... arrclass) {
        ArrayList<String> arrayList = Lists.newArrayList();
        for (Class class_ : arrclass) {
            arrayList.add(class_.getName());
        }
        return arrayList;
    }

    public StackTraceElement get(StackTraceElement[] arrstackTraceElement) {
        Preconditions.checkNotNull(arrstackTraceElement, "The stack trace elements cannot be null.");
        for (StackTraceElement stackTraceElement : arrstackTraceElement) {
            String string = stackTraceElement.getClassName();
            if (this.shouldBeSkipped(string)) continue;
            return stackTraceElement;
        }
        throw new AssertionError();
    }

    public Object getFromClassNames(List<String> list) {
        Preconditions.checkNotNull(list, "The list of module class names cannot be null.");
        for (String string : list) {
            if (this.shouldBeSkipped(string)) continue;
            return new StackTraceElement(string, "configure", null, -1);
        }
        return UNKNOWN_SOURCE;
    }
}

