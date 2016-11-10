/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InjectorShell;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import java.util.Iterator;
import java.util.List;

abstract class AbstractProcessor
extends DefaultElementVisitor<Boolean> {
    protected Errors errors;
    protected InjectorImpl injector;

    protected AbstractProcessor(Errors errors) {
        this.errors = errors;
    }

    public void process(Iterable<InjectorShell> iterable) {
        for (InjectorShell injectorShell : iterable) {
            this.process(injectorShell.getInjector(), injectorShell.getElements());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void process(InjectorImpl injectorImpl, List<Element> list) {
        Errors errors = this.errors;
        this.injector = injectorImpl;
        try {
            Iterator<Element> iterator = list.iterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                this.errors = errors.withSource(element.getSource());
                Boolean bl = (Boolean)element.acceptVisitor(this);
                if (!bl.booleanValue()) continue;
                iterator.remove();
            }
        }
        finally {
            this.errors = errors;
            this.injector = null;
        }
    }

    @Override
    protected Boolean visitOther(Element element) {
        return false;
    }
}

