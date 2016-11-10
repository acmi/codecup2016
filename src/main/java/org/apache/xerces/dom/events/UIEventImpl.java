/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom.events;

import org.apache.xerces.dom.events.EventImpl;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

public class UIEventImpl
extends EventImpl
implements UIEvent {
    private AbstractView fView;
    private int fDetail;

    public AbstractView getView() {
        return this.fView;
    }

    public int getDetail() {
        return this.fDetail;
    }

    public void initUIEvent(String string, boolean bl, boolean bl2, AbstractView abstractView, int n2) {
        this.fView = abstractView;
        this.fDetail = n2;
        super.initEvent(string, bl, bl2);
    }
}

