/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public interface ExtendedLexicalHandler
extends LexicalHandler {
    public void comment(String var1) throws SAXException;
}

