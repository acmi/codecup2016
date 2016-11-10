/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.util;

import com.google.common.base.Preconditions;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class Classes {
    public static boolean isInnerClass(Class<?> class_) {
        return !Modifier.isStatic(class_.getModifiers()) && class_.getEnclosingClass() != null;
    }

    public static boolean isConcrete(Class<?> class_) {
        int n2 = class_.getModifiers();
        return !class_.isInterface() && !Modifier.isAbstract(n2);
    }

    public static String toString(Member member) {
        Class<? extends Member> class_ = Classes.memberType(member);
        if (class_ == Method.class) {
            return member.getDeclaringClass().getName() + "." + member.getName() + "()";
        }
        if (class_ == Field.class) {
            return member.getDeclaringClass().getName() + "." + member.getName();
        }
        if (class_ == Constructor.class) {
            return member.getDeclaringClass().getName() + ".<init>()";
        }
        throw new AssertionError();
    }

    public static Class<? extends Member> memberType(Member member) {
        Preconditions.checkNotNull(member, "member");
        if (member instanceof Field) {
            return Field.class;
        }
        if (member instanceof Method) {
            return Method.class;
        }
        if (member instanceof Constructor) {
            return Constructor.class;
        }
        throw new IllegalArgumentException("Unsupported implementation class for Member, " + member.getClass());
    }
}

