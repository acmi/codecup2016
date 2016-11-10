/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xinclude;

import java.io.IOException;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xinclude.XIncludeTextReader;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XInclude11TextReader
extends XIncludeTextReader {
    public XInclude11TextReader(XMLInputSource xMLInputSource, XIncludeHandler xIncludeHandler, int n2) throws IOException {
        super(xMLInputSource, xIncludeHandler, n2);
    }

    protected boolean isValid(int n2) {
        return XML11Char.isXML11Valid(n2);
    }
}

