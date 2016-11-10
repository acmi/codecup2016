/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.stax.events;

import java.io.StringWriter;
import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.xerces.stax.EmptyLocation;
import org.apache.xerces.stax.ImmutableLocation;

public abstract class XMLEventImpl
implements XMLEvent {
    private int fEventType;
    private Location fLocation;

    XMLEventImpl(int n2, Location location) {
        this.fEventType = n2;
        this.fLocation = location != null ? new ImmutableLocation(location) : EmptyLocation.getInstance();
    }

    public final int getEventType() {
        return this.fEventType;
    }

    public final Location getLocation() {
        return this.fLocation;
    }

    public final boolean isStartElement() {
        return 1 == this.fEventType;
    }

    public final boolean isAttribute() {
        return 10 == this.fEventType;
    }

    public final boolean isNamespace() {
        return 13 == this.fEventType;
    }

    public final boolean isEndElement() {
        return 2 == this.fEventType;
    }

    public final boolean isEntityReference() {
        return 9 == this.fEventType;
    }

    public final boolean isProcessingInstruction() {
        return 3 == this.fEventType;
    }

    public final boolean isCharacters() {
        return 4 == this.fEventType || 12 == this.fEventType || 6 == this.fEventType;
    }

    public final boolean isStartDocument() {
        return 7 == this.fEventType;
    }

    public final boolean isEndDocument() {
        return 8 == this.fEventType;
    }

    public final StartElement asStartElement() {
        return (StartElement)((Object)this);
    }

    public final EndElement asEndElement() {
        return (EndElement)((Object)this);
    }

    public final Characters asCharacters() {
        return (Characters)((Object)this);
    }

    public final QName getSchemaType() {
        return null;
    }

    public final String toString() {
        StringWriter stringWriter = new StringWriter();
        try {
            this.writeAsEncodedUnicode(stringWriter);
        }
        catch (XMLStreamException xMLStreamException) {
            // empty catch block
        }
        return stringWriter.toString();
    }

    public abstract void writeAsEncodedUnicode(Writer var1) throws XMLStreamException;
}

