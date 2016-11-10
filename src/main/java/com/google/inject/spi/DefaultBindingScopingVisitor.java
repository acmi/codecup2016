/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Scope;
import com.google.inject.spi.BindingScopingVisitor;
import java.lang.annotation.Annotation;

public class DefaultBindingScopingVisitor<V>
implements BindingScopingVisitor<V> {
    protected V visitOther() {
        return null;
    }

    @Override
    public V visitEagerSingleton() {
        return this.visitOther();
    }

    @Override
    public V visitScope(Scope scope) {
        return this.visitOther();
    }

    @Override
    public V visitScopeAnnotation(Class<? extends Annotation> class_) {
        return this.visitOther();
    }

    @Override
    public V visitNoScoping() {
        return this.visitOther();
    }
}

