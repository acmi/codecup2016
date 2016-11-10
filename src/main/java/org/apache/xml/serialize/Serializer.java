/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.xml.sax.ContentHandler;

public interface Serializer {
    public void setOutputByteStream(OutputStream var1);

    public void setOutputCharStream(Writer var1);

    public ContentHandler asContentHandler() throws IOException;
}

