/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.stax;

import javax.xml.stream.Location;

public class ImmutableLocation
implements Location {
    private final int fCharacterOffset;
    private final int fColumnNumber;
    private final int fLineNumber;
    private final String fPublicId;
    private final String fSystemId;

    public ImmutableLocation(Location location) {
        this(location.getCharacterOffset(), location.getColumnNumber(), location.getLineNumber(), location.getPublicId(), location.getSystemId());
    }

    public ImmutableLocation(int n2, int n3, int n4, String string, String string2) {
        this.fCharacterOffset = n2;
        this.fColumnNumber = n3;
        this.fLineNumber = n4;
        this.fPublicId = string;
        this.fSystemId = string2;
    }

    public int getCharacterOffset() {
        return this.fCharacterOffset;
    }

    public int getColumnNumber() {
        return this.fColumnNumber;
    }

    public int getLineNumber() {
        return this.fLineNumber;
    }

    public String getPublicId() {
        return this.fPublicId;
    }

    public String getSystemId() {
        return this.fSystemId;
    }
}

