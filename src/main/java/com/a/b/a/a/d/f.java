/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.d;

import com.a.b.a.a.d.a;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class f
extends WindowAdapter {
    final /* synthetic */ a a;

    f(a a2) {
        this.a = a2;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        a.c(this.a);
        System.exit(0);
    }
}

