/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.internal.AbstractBindingProcessor;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.ProcessedBindingData;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.UntargettedBindingImpl;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.UntargettedBinding;
import java.lang.annotation.Annotation;

class UntargettedBindingProcessor
extends AbstractBindingProcessor {
    UntargettedBindingProcessor(Errors errors, ProcessedBindingData processedBindingData) {
        super(errors, processedBindingData);
    }

    @Override
    public <T> Boolean visit(Binding<T> binding) {
        return (Boolean)binding.acceptTargetVisitor(new AbstractBindingProcessor.Processor<T, Boolean>((BindingImpl)binding){

            @Override
            public Boolean visit(UntargettedBinding<? extends T> untargettedBinding) {
                this.prepareBinding();
                if (this.key.getAnnotationType() != null) {
                    UntargettedBindingProcessor.this.errors.missingImplementation(this.key);
                    UntargettedBindingProcessor.this.putBinding(UntargettedBindingProcessor.this.invalidBinding(UntargettedBindingProcessor.this.injector, this.key, this.source));
                    return true;
                }
                try {
                    BindingImpl bindingImpl = UntargettedBindingProcessor.this.injector.createUninitializedBinding(this.key, this.scoping, this.source, UntargettedBindingProcessor.this.errors, false);
                    this.scheduleInitialization(bindingImpl);
                    UntargettedBindingProcessor.this.putBinding(bindingImpl);
                }
                catch (ErrorsException errorsException) {
                    UntargettedBindingProcessor.this.errors.merge(errorsException.getErrors());
                    UntargettedBindingProcessor.this.putBinding(UntargettedBindingProcessor.this.invalidBinding(UntargettedBindingProcessor.this.injector, this.key, this.source));
                }
                return true;
            }

            @Override
            protected Boolean visitOther(Binding<? extends T> binding) {
                return false;
            }
        });
    }

}

