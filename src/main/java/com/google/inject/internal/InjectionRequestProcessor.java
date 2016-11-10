/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Stage;
import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.ContextualCallable;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.Initializable;
import com.google.inject.internal.Initializer;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.MembersInjectorStore;
import com.google.inject.internal.SingleMemberInjector;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.InjectionRequest;
import com.google.inject.spi.Message;
import com.google.inject.spi.StaticInjectionRequest;
import java.util.Collection;
import java.util.List;
import java.util.Set;

final class InjectionRequestProcessor
extends AbstractProcessor {
    private final List<StaticInjection> staticInjections = Lists.newArrayList();
    private final Initializer initializer;

    InjectionRequestProcessor(Errors errors, Initializer initializer) {
        super(errors);
        this.initializer = initializer;
    }

    @Override
    public Boolean visit(StaticInjectionRequest staticInjectionRequest) {
        this.staticInjections.add(new StaticInjection(this.injector, staticInjectionRequest));
        return true;
    }

    @Override
    public Boolean visit(InjectionRequest<?> injectionRequest) {
        Set set;
        try {
            set = injectionRequest.getInjectionPoints();
        }
        catch (ConfigurationException configurationException) {
            this.errors.merge(configurationException.getErrorMessages());
            set = (Set)configurationException.getPartialValue();
        }
        this.initializer.requestInjection(this.injector, injectionRequest.getInstance(), null, injectionRequest.getSource(), set);
        return true;
    }

    void validate() {
        for (StaticInjection staticInjection : this.staticInjections) {
            staticInjection.validate();
        }
    }

    void injectMembers() {
        for (StaticInjection staticInjection : this.staticInjections) {
            staticInjection.injectMembers();
        }
    }

    private class StaticInjection {
        final InjectorImpl injector;
        final Object source;
        final StaticInjectionRequest request;
        ImmutableList<SingleMemberInjector> memberInjectors;

        public StaticInjection(InjectorImpl injectorImpl, StaticInjectionRequest staticInjectionRequest) {
            this.injector = injectorImpl;
            this.source = staticInjectionRequest.getSource();
            this.request = staticInjectionRequest;
        }

        void validate() {
            Set set;
            Errors errors = InjectionRequestProcessor.this.errors.withSource(this.source);
            try {
                set = this.request.getInjectionPoints();
            }
            catch (ConfigurationException configurationException) {
                errors.merge(configurationException.getErrorMessages());
                set = (Set)configurationException.getPartialValue();
            }
            this.memberInjectors = set != null ? this.injector.membersInjectorStore.getInjectors(set, errors) : ImmutableList.of();
            InjectionRequestProcessor.this.errors.merge(errors);
        }

        void injectMembers() {
            try {
                this.injector.callInContext(new ContextualCallable<Void>(){

                    @Override
                    public Void call(InternalContext internalContext) {
                        for (SingleMemberInjector singleMemberInjector : StaticInjection.this.memberInjectors) {
                            if (StaticInjection.this.injector.options.stage == Stage.TOOL && !singleMemberInjector.getInjectionPoint().isToolable()) continue;
                            singleMemberInjector.inject(InjectionRequestProcessor.this.errors, internalContext, null);
                        }
                        return null;
                    }
                });
            }
            catch (ErrorsException errorsException) {
                throw new AssertionError();
            }
        }

    }

}

