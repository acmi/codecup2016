/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.a;

import com.a.a.b.a.c;
import com.a.b.a.a.b.d;
import com.a.b.g;
import com.a.c.b.a.b;
import com.a.c.e;
import com.google.inject.AbstractModule;
import com.google.inject.binder.AnnotatedBindingBuilder;

public final class a
extends AbstractModule {
    @Override
    protected void configure() {
        this.bind(com.a.b.a.class).toInstance(new com.a.b.a.a.b.a());
        this.bind(e.class).toInstance(new b(10, 1, 1.0E-7, new c(100.0, 100.0)));
        this.bind(g.class).toInstance(new d(false));
    }
}

