/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.cglib.core.$NamingPolicy;
import com.google.inject.internal.cglib.core.$Predicate;

public class $DefaultNamingPolicy
implements $NamingPolicy {
    public static final $DefaultNamingPolicy INSTANCE = new $DefaultNamingPolicy();

    public String getClassName(String string, String string2, Object object, $Predicate $Predicate) {
        String string3;
        if (string == null) {
            string = "com.google.inject.internal.cglib.empty.$Object";
        } else if (string.startsWith("java")) {
            String string4 = String.valueOf(string);
            string = string4.length() != 0 ? "$".concat(string4) : new String("$");
        }
        String string5 = string;
        String string6 = String.valueOf(string2.substring(string2.lastIndexOf(46) + 1));
        String string7 = this.getTag();
        String string8 = String.valueOf(Integer.toHexString(object.hashCode()));
        string5 = string3 = new StringBuilder(4 + String.valueOf(string5).length() + String.valueOf(string6).length() + String.valueOf(string7).length() + String.valueOf(string8).length()).append(string5).append("$$").append(string6).append(string7).append("$$").append(string8).toString();
        int n2 = 2;
        while ($Predicate.evaluate(string5)) {
            int n3 = n2++;
            string5 = new StringBuilder(12 + String.valueOf(string3).length()).append(string3).append("_").append(n3).toString();
        }
        return string5;
    }

    protected String getTag() {
        return "ByCGLIB";
    }

    public int hashCode() {
        return this.getTag().hashCode();
    }

    public boolean equals(Object object) {
        return object instanceof $DefaultNamingPolicy && (($DefaultNamingPolicy)object).getTag().equals(this.getTag());
    }
}

