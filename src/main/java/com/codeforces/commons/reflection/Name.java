/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.reflection;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
public @interface Name {
    public String value();
}

