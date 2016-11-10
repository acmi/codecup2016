/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform.sax;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ext.LexicalHandler;

public interface TransformerHandler
extends ContentHandler,
DTDHandler,
LexicalHandler {
    public void setResult(Result var1) throws IllegalArgumentException;

    public Transformer getTransformer();
}

