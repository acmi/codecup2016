/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.internal.Errors;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.ModuleAnnotatedMethodScanner;
import java.lang.annotation.Annotation;
import java.util.Set;

public final class ModuleAnnotatedMethodScannerBinding
implements Element {
    private final Object source;
    private final ModuleAnnotatedMethodScanner scanner;

    public ModuleAnnotatedMethodScannerBinding(Object object, ModuleAnnotatedMethodScanner moduleAnnotatedMethodScanner) {
        this.source = Preconditions.checkNotNull(object, "source");
        this.scanner = Preconditions.checkNotNull(moduleAnnotatedMethodScanner, "scanner");
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    public ModuleAnnotatedMethodScanner getScanner() {
        return this.scanner;
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).scanModulesForAnnotatedMethods(this.scanner);
    }

    public String toString() {
        return this.scanner + " which scans for " + this.scanner.annotationClasses() + " (bound at " + Errors.convert(this.source) + ")";
    }
}

