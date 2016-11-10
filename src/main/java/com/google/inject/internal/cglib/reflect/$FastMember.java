/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.reflect;

import com.google.inject.internal.cglib.reflect.$FastClass;
import java.lang.reflect.Member;

public abstract class $FastMember {
    protected $FastClass fc;
    protected Member member;
    protected int index;

    protected $FastMember($FastClass $FastClass, Member member, int n2) {
        this.fc = $FastClass;
        this.member = member;
        this.index = n2;
    }

    public abstract Class[] getParameterTypes();

    public abstract Class[] getExceptionTypes();

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.member.getName();
    }

    public Class getDeclaringClass() {
        return this.fc.getJavaClass();
    }

    public int getModifiers() {
        return this.member.getModifiers();
    }

    public String toString() {
        return this.member.toString();
    }

    public int hashCode() {
        return this.member.hashCode();
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof $FastMember)) {
            return false;
        }
        return this.member.equals((($FastMember)object).member);
    }
}

