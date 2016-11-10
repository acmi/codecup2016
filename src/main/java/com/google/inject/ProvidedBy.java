/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Provider;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface ProvidedBy {
    public Class<? extends Provider<?>> value();
}

