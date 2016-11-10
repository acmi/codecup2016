/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax;

import java.io.IOException;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public interface Parser {
    public void setEntityResolver(EntityResolver var1);

    public void setDTDHandler(DTDHandler var1);

    public void setDocumentHandler(DocumentHandler var1);

    public void setErrorHandler(ErrorHandler var1);

    public void parse(InputSource var1) throws SAXException, IOException;
}
