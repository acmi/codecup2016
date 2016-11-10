/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import org.apache.xerces.stax.events.XMLEventImpl;
import org.apache.xerces.util.XMLChar;

public final class CharactersImpl
extends XMLEventImpl
implements Characters {
    private final String fData;

    public CharactersImpl(String string, int n2, Location location) {
        super(n2, location);
        this.fData = string != null ? string : "";
    }

    public String getData() {
        return this.fData;
    }

    public boolean isWhiteSpace() {
        int n2;
        int n3 = n2 = this.fData != null ? this.fData.length() : 0;
        if (n2 == 0) {
            return false;
        }
        int n4 = 0;
        while (n4 < n2) {
            if (!XMLChar.isSpace(this.fData.charAt(n4))) {
                return false;
            }
            ++n4;
        }
        return true;
    }

    public boolean isCData() {
        return 12 == this.getEventType();
    }

    public boolean isIgnorableWhiteSpace() {
        return 6 == this.getEventType();
    }

    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {
        try {
            writer.write(this.fData);
        }
        catch (IOException iOException) {
            throw new XMLStreamException(iOException);
        }
    }
}

