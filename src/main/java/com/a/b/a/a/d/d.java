/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.d;

import com.a.b.a.a.d.a;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.ImageObserver;

class d
extends Panel {
    final /* synthetic */ a a;

    d(a a2) {
        this.a = a2;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawImage(a.e(this.a), 0, 0, null);
    }
}

