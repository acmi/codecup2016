/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax.ext;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public interface EntityResolver2
extends EntityResolver {
    public InputSource getExternalSubset(String var1, String var2) throws SAXException, IOException;

    public InputSource resolveEntity(String var1, String var2, String var3, String var4) throws SAXException, IOException;
}

