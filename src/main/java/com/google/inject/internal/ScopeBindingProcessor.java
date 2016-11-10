/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Scope;
import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.State;
import com.google.inject.spi.ScopeBinding;
import java.lang.annotation.Annotation;

final class ScopeBindingProcessor
extends AbstractProcessor {
    ScopeBindingProcessor(Errors errors) {
        super(errors);
    }

    @Override
    public Boolean visit(ScopeBinding scopeBinding) {
        ScopeBinding scopeBinding2;
        Scope scope = Preconditions.checkNotNull(scopeBinding.getScope(), "scope");
        Class<? extends Annotation> class_ = Preconditions.checkNotNull(scopeBinding.getAnnotationType(), "annotation type");
        if (!Annotations.isScopeAnnotation(class_)) {
            this.errors.missingScopeAnnotation(class_);
        }
        if (!Annotations.isRetainedAtRuntime(class_)) {
            this.errors.missingRuntimeRetention(class_);
        }
        if ((scopeBinding2 = this.injector.state.getScopeBinding(class_)) != null) {
            if (!scope.equals(scopeBinding2.getScope())) {
                this.errors.duplicateScopes(scopeBinding2, class_, scope);
            }
        } else {
            this.injector.state.putScopeBinding(class_, scopeBinding);
        }
        return true;
    }
}

