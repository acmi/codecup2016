/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.State;
import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;

final class ModuleAnnotatedMethodScannerProcessor
extends AbstractProcessor {
    ModuleAnnotatedMethodScannerProcessor(Errors errors) {
        super(errors);
    }

    @Override
    public Boolean visit(ModuleAnnotatedMethodScannerBinding moduleAnnotatedMethodScannerBinding) {
        this.injector.state.addScanner(moduleAnnotatedMethodScannerBinding);
        return true;
    }
}

