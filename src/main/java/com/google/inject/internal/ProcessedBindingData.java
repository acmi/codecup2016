/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.Lists;
import com.google.inject.internal.CreationListener;
import com.google.inject.internal.Errors;
import java.util.List;

class ProcessedBindingData {
    private final List<CreationListener> creationListeners = Lists.newArrayList();
    private final List<Runnable> uninitializedBindings = Lists.newArrayList();

    ProcessedBindingData() {
    }

    void addCreationListener(CreationListener creationListener) {
        this.creationListeners.add(creationListener);
    }

    void addUninitializedBinding(Runnable runnable) {
        this.uninitializedBindings.add(runnable);
    }

    void initializeBindings() {
        for (Runnable runnable : this.uninitializedBindings) {
            runnable.run();
        }
    }

    void runCreationListeners(Errors errors) {
        for (CreationListener creationListener : this.creationListeners) {
            creationListener.notify(errors);
        }
    }
}

