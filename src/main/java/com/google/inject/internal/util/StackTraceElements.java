/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.internal.util.Classes;
import com.google.inject.internal.util.LineNumbers;
import com.google.inject.internal.util.SourceProvider;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StackTraceElements {
    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
    private static final InMemoryStackTraceElement[] EMPTY_INMEMORY_STACK_TRACE = new InMemoryStackTraceElement[0];
    static final LoadingCache<Class<?>, LineNumbers> lineNumbersCache = CacheBuilder.newBuilder().weakKeys().softValues().build(new CacheLoader<Class<?>, LineNumbers>(){

        @Override
        public LineNumbers load(Class<?> class_) {
            try {
                return new LineNumbers(class_);
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
    });
    private static final ConcurrentMap<InMemoryStackTraceElement, InMemoryStackTraceElement> elementCache = new ConcurrentHashMap<InMemoryStackTraceElement, InMemoryStackTraceElement>();
    private static final ConcurrentMap<String, String> stringCache = new ConcurrentHashMap<String, String>();
    private static final String UNKNOWN_SOURCE = "Unknown Source";

    public static Object forMember(Member member) {
        if (member == null) {
            return SourceProvider.UNKNOWN_SOURCE;
        }
        Class class_ = member.getDeclaringClass();
        LineNumbers lineNumbers = lineNumbersCache.getUnchecked(class_);
        String string = lineNumbers.getSource();
        Integer n2 = lineNumbers.getLineNumber(member);
        int n3 = n2 == null ? lineNumbers.getFirstLine() : n2.intValue();
        Class<? extends Member> class_2 = Classes.memberType(member);
        String string2 = class_2 == Constructor.class ? "<init>" : member.getName();
        return new StackTraceElement(class_.getName(), string2, string, n3);
    }

    public static Object forType(Class<?> class_) {
        LineNumbers lineNumbers = lineNumbersCache.getUnchecked(class_);
        int n2 = lineNumbers.getFirstLine();
        String string = lineNumbers.getSource();
        return new StackTraceElement(class_.getName(), "class", string, n2);
    }

    public static void clearCache() {
        elementCache.clear();
        stringCache.clear();
    }

    public static InMemoryStackTraceElement[] convertToInMemoryStackTraceElement(StackTraceElement[] arrstackTraceElement) {
        if (arrstackTraceElement.length == 0) {
            return EMPTY_INMEMORY_STACK_TRACE;
        }
        InMemoryStackTraceElement[] arrinMemoryStackTraceElement = new InMemoryStackTraceElement[arrstackTraceElement.length];
        for (int i2 = 0; i2 < arrstackTraceElement.length; ++i2) {
            arrinMemoryStackTraceElement[i2] = StackTraceElements.weakIntern(new InMemoryStackTraceElement(arrstackTraceElement[i2]));
        }
        return arrinMemoryStackTraceElement;
    }

    public static StackTraceElement[] convertToStackTraceElement(InMemoryStackTraceElement[] arrinMemoryStackTraceElement) {
        if (arrinMemoryStackTraceElement.length == 0) {
            return EMPTY_STACK_TRACE;
        }
        StackTraceElement[] arrstackTraceElement = new StackTraceElement[arrinMemoryStackTraceElement.length];
        for (int i2 = 0; i2 < arrinMemoryStackTraceElement.length; ++i2) {
            String string = arrinMemoryStackTraceElement[i2].getClassName();
            String string2 = arrinMemoryStackTraceElement[i2].getMethodName();
            int n2 = arrinMemoryStackTraceElement[i2].getLineNumber();
            arrstackTraceElement[i2] = new StackTraceElement(string, string2, "Unknown Source", n2);
        }
        return arrstackTraceElement;
    }

    private static InMemoryStackTraceElement weakIntern(InMemoryStackTraceElement inMemoryStackTraceElement) {
        InMemoryStackTraceElement inMemoryStackTraceElement2 = elementCache.get(inMemoryStackTraceElement);
        if (inMemoryStackTraceElement2 != null) {
            return inMemoryStackTraceElement2;
        }
        inMemoryStackTraceElement = new InMemoryStackTraceElement(StackTraceElements.weakIntern(inMemoryStackTraceElement.getClassName()), StackTraceElements.weakIntern(inMemoryStackTraceElement.getMethodName()), inMemoryStackTraceElement.getLineNumber());
        elementCache.put(inMemoryStackTraceElement, inMemoryStackTraceElement);
        return inMemoryStackTraceElement;
    }

    private static String weakIntern(String string) {
        String string2 = stringCache.get(string);
        if (string2 != null) {
            return string2;
        }
        stringCache.put(string, string);
        return string;
    }

    public static class InMemoryStackTraceElement {
        private String declaringClass;
        private String methodName;
        private int lineNumber;

        InMemoryStackTraceElement(StackTraceElement stackTraceElement) {
            this(stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getLineNumber());
        }

        InMemoryStackTraceElement(String string, String string2, int n2) {
            this.declaringClass = string;
            this.methodName = string2;
            this.lineNumber = n2;
        }

        String getClassName() {
            return this.declaringClass;
        }

        String getMethodName() {
            return this.methodName;
        }

        int getLineNumber() {
            return this.lineNumber;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof InMemoryStackTraceElement)) {
                return false;
            }
            InMemoryStackTraceElement inMemoryStackTraceElement = (InMemoryStackTraceElement)object;
            return inMemoryStackTraceElement.declaringClass.equals(this.declaringClass) && inMemoryStackTraceElement.lineNumber == this.lineNumber && this.methodName.equals(inMemoryStackTraceElement.methodName);
        }

        public int hashCode() {
            int n2 = 31 * this.declaringClass.hashCode() + this.methodName.hashCode();
            n2 = 31 * n2 + this.lineNumber;
            return n2;
        }

        public String toString() {
            return this.declaringClass + "." + this.methodName + "(" + this.lineNumber + ")";
        }
    }

}

