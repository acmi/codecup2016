/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.reflect;

import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.reflect.$FastClass;
import com.google.inject.internal.cglib.reflect.$FastMember;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class $FastMethod
extends $FastMember {
    $FastMethod($FastClass $FastClass, Method method) {
        super($FastClass, method, $FastMethod.helper($FastClass, method));
    }

    private static int helper($FastClass $FastClass, Method method) {
        int n2 = $FastClass.getIndex(new $Signature(method.getName(), $Type.getMethodDescriptor(method)));
        if (n2 < 0) {
            Class<?>[] arrclass = method.getParameterTypes();
            int n3 = method.getName().hashCode();
            int n4 = arrclass.length;
            System.err.println(new StringBuilder(33).append("hash=").append(n3).append(" size=").append(n4).toString());
            for (n3 = 0; n3 < arrclass.length; ++n3) {
                n4 = n3;
                String string = String.valueOf(arrclass[n3].getName());
                System.err.println(new StringBuilder(21 + String.valueOf(string).length()).append("  types[").append(n4).append("]=").append(string).toString());
            }
            String string = String.valueOf(method);
            throw new IllegalArgumentException(new StringBuilder(19 + String.valueOf(string).length()).append("Cannot find method ").append(string).toString());
        }
        return n2;
    }

    public Class getReturnType() {
        return ((Method)this.member).getReturnType();
    }

    public Class[] getParameterTypes() {
        return ((Method)this.member).getParameterTypes();
    }

    public Class[] getExceptionTypes() {
        return ((Method)this.member).getExceptionTypes();
    }

    public Object invoke(Object object, Object[] arrobject) throws InvocationTargetException {
        return this.fc.invoke(this.index, object, arrobject);
    }

    public Method getJavaMethod() {
        return (Method)this.member;
    }
}

