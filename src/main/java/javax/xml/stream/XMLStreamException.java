/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream;

import javax.xml.stream.Location;

public class XMLStreamException
extends Exception {
    protected Throwable nested;
    protected Location location;

    public XMLStreamException() {
    }

    public XMLStreamException(Throwable throwable) {
        this.nested = throwable;
    }

    public Location getLocation() {
        return this.location;
    }
}

