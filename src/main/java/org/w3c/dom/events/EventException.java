/*
 * Decompiled with CFR 0_119.
 */
package org.w3c.dom.events;

public class EventException
extends RuntimeException {
    public short code;
    public static final short UNSPECIFIED_EVENT_TYPE_ERR = 0;

    public EventException(short s2, String string) {
        super(string);
        this.code = s2;
    }
}

