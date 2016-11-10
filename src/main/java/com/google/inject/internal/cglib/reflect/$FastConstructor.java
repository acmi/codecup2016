/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.reflect;

import com.google.inject.internal.cglib.reflect.$FastClass;
import com.google.inject.internal.cglib.reflect.$FastMember;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;

public class $FastConstructor
extends $FastMember {
    $FastConstructor($FastClass $FastClass, Constructor constructor) {
        super($FastClass, constructor, $FastClass.getIndex(constructor.getParameterTypes()));
    }

    public Class[] getParameterTypes() {
        return ((Constructor)this.member).getParameterTypes();
    }

    public Class[] getExceptionTypes() {
        return ((Constructor)this.member).getExceptionTypes();
    }

    public Object newInstance() throws InvocationTargetException {
        return this.fc.newInstance(this.index, null);
    }

    public Object newInstance(Object[] arrobject) throws InvocationTargetException {
        return this.fc.newInstance(this.index, arrobject);
    }

    public Constructor getJavaConstructor() {
        return (Constructor)this.member;
    }
}

