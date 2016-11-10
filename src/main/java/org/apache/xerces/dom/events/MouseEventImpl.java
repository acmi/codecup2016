/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom.events;

import org.apache.xerces.dom.events.UIEventImpl;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

public class MouseEventImpl
extends UIEventImpl
implements MouseEvent {
    private int fScreenX;
    private int fScreenY;
    private int fClientX;
    private int fClientY;
    private boolean fCtrlKey;
    private boolean fAltKey;
    private boolean fShiftKey;
    private boolean fMetaKey;
    private short fButton;
    private EventTarget fRelatedTarget;

    public int getScreenX() {
        return this.fScreenX;
    }

    public int getScreenY() {
        return this.fScreenY;
    }

    public int getClientX() {
        return this.fClientX;
    }

    public int getClientY() {
        return this.fClientY;
    }

    public boolean getCtrlKey() {
        return this.fCtrlKey;
    }

    public boolean getAltKey() {
        return this.fAltKey;
    }

    public boolean getShiftKey() {
        return this.fShiftKey;
    }

    public boolean getMetaKey() {
        return this.fMetaKey;
    }

    public short getButton() {
        return this.fButton;
    }

    public EventTarget getRelatedTarget() {
        return this.fRelatedTarget;
    }

    public void initMouseEvent(String string, boolean bl, boolean bl2, AbstractView abstractView, int n2, int n3, int n4, int n5, int n6, boolean bl3, boolean bl4, boolean bl5, boolean bl6, short s2, EventTarget eventTarget) {
        this.fScreenX = n3;
        this.fScreenY = n4;
        this.fClientX = n5;
        this.fClientY = n6;
        this.fCtrlKey = bl3;
        this.fAltKey = bl4;
        this.fShiftKey = bl5;
        this.fMetaKey = bl6;
        this.fButton = s2;
        this.fRelatedTarget = eventTarget;
        super.initUIEvent(string, bl, bl2, abstractView, n2);
    }
}

