/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform.sax;

import javax.xml.transform.Result;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

public class SAXResult
implements Result {
    private ContentHandler handler;
    private LexicalHandler lexhandler;
    private String systemId;

    public SAXResult() {
    }

    public SAXResult(ContentHandler contentHandler) {
        this.setHandler(contentHandler);
    }

    public void setHandler(ContentHandler contentHandler) {
        this.handler = contentHandler;
    }

    public ContentHandler getHandler() {
        return this.handler;
    }

    public LexicalHandler getLexicalHandler() {
        return this.lexhandler;
    }

    public String getSystemId() {
        return this.systemId;
    }
}

