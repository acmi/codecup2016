/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.io.OutputStream;
import java.io.Writer;
import org.w3c.dom.ls.LSOutput;

public class DOMOutputImpl
implements LSOutput {
    protected Writer fCharStream = null;
    protected OutputStream fByteStream = null;
    protected String fSystemId = null;
    protected String fEncoding = null;

    public Writer getCharacterStream() {
        return this.fCharStream;
    }

    public void setCharacterStream(Writer writer) {
        this.fCharStream = writer;
    }

    public OutputStream getByteStream() {
        return this.fByteStream;
    }

    public void setByteStream(OutputStream outputStream) {
        this.fByteStream = outputStream;
    }

    public String getSystemId() {
        return this.fSystemId;
    }

    public void setSystemId(String string) {
        this.fSystemId = string;
    }

    public String getEncoding() {
        return this.fEncoding;
    }

    public void setEncoding(String string) {
        this.fEncoding = string;
    }
}

