/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.inject.ScopeAnnotation;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE, ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface Singleton {
}
