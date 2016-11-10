/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import javax.xml.transform.Transformer;
import org.apache.xml.serializer.DOMSerializer;
import org.apache.xml.serializer.ExtendedContentHandler;
import org.apache.xml.serializer.ExtendedLexicalHandler;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.XSLOutputAttributes;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;

public interface SerializationHandler
extends DOMSerializer,
ExtendedContentHandler,
ExtendedLexicalHandler,
Serializer,
XSLOutputAttributes,
DTDHandler,
ErrorHandler,
DeclHandler {
    public void setContentHandler(ContentHandler var1);

    public void close();

    public void serialize(Node var1) throws IOException;

    public boolean setEscaping(boolean var1) throws SAXException;

    public void setIndentAmount(int var1);

    public void setTransformer(Transformer var1);

    public Transformer getTransformer();

    public void setNamespaceMappings(NamespaceMappings var1);

    public void flushPending() throws SAXException;
}

