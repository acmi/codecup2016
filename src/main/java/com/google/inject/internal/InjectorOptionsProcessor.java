/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Stage;
import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.spi.DisableCircularProxiesOption;
import com.google.inject.spi.RequireAtInjectOnConstructorsOption;
import com.google.inject.spi.RequireExactBindingAnnotationsOption;
import com.google.inject.spi.RequireExplicitBindingsOption;

class InjectorOptionsProcessor
extends AbstractProcessor {
    private boolean disableCircularProxies = false;
    private boolean jitDisabled = false;
    private boolean atInjectRequired = false;
    private boolean exactBindingAnnotationsRequired = false;

    InjectorOptionsProcessor(Errors errors) {
        super(errors);
    }

    @Override
    public Boolean visit(DisableCircularProxiesOption disableCircularProxiesOption) {
        this.disableCircularProxies = true;
        return true;
    }

    @Override
    public Boolean visit(RequireExplicitBindingsOption requireExplicitBindingsOption) {
        this.jitDisabled = true;
        return true;
    }

    @Override
    public Boolean visit(RequireAtInjectOnConstructorsOption requireAtInjectOnConstructorsOption) {
        this.atInjectRequired = true;
        return true;
    }

    @Override
    public Boolean visit(RequireExactBindingAnnotationsOption requireExactBindingAnnotationsOption) {
        this.exactBindingAnnotationsRequired = true;
        return true;
    }

    InjectorImpl.InjectorOptions getOptions(Stage stage, InjectorImpl.InjectorOptions injectorOptions) {
        Preconditions.checkNotNull(stage, "stage must be set");
        if (injectorOptions == null) {
            return new InjectorImpl.InjectorOptions(stage, this.jitDisabled, this.disableCircularProxies, this.atInjectRequired, this.exactBindingAnnotationsRequired);
        }
        Preconditions.checkState(stage == injectorOptions.stage, "child & parent stage don't match");
        return new InjectorImpl.InjectorOptions(stage, this.jitDisabled || injectorOptions.jitDisabled, this.disableCircularProxies || injectorOptions.disableCircularProxies, this.atInjectRequired || injectorOptions.atInjectRequired, this.exactBindingAnnotationsRequired || injectorOptions.exactBindingAnnotationsRequired);
    }
}

