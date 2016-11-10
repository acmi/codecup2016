/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream.events;

import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public interface XMLEvent {
    public Characters asCharacters();

    public EndElement asEndElement();

    public StartElement asStartElement();

    public int getEventType();

    public Location getLocation();

    public boolean isStartDocument();

    public void writeAsEncodedUnicode(Writer var1) throws XMLStreamException;
}

