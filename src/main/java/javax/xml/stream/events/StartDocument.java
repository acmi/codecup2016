/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream.events;

import javax.xml.stream.events.XMLEvent;

public interface StartDocument
extends XMLEvent {
    public String getCharacterEncodingScheme();

    public String getVersion();
}

