/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xni.parser;

import java.io.IOException;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;

public interface XMLEntityResolver {
    public XMLInputSource resolveEntity(XMLResourceIdentifier var1) throws XNIException, IOException;
}

