/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream.events;

import javax.xml.stream.events.XMLEvent;

public interface ProcessingInstruction
extends XMLEvent {
    public String getData();

    public String getTarget();
}
