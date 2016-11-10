/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xalan.xsltc.trax.SAX2DOM;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.ToHTMLStream;
import org.apache.xml.serializer.ToTextStream;
import org.apache.xml.serializer.ToUnknownStream;
import org.apache.xml.serializer.ToXMLSAXHandler;
import org.apache.xml.serializer.ToXMLStream;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

public class TransletOutputHandlerFactory {
    public static final int STREAM = 0;
    public static final int SAX = 1;
    public static final int DOM = 2;
    private String _encoding = "utf-8";
    private String _method = null;
    private int _outputType = 0;
    private OutputStream _ostream = System.out;
    private Writer _writer = null;
    private Node _node = null;
    private Node _nextSibling = null;
    private int _indentNumber = -1;
    private ContentHandler _handler = null;
    private LexicalHandler _lexHandler = null;

    public static TransletOutputHandlerFactory newInstance() {
        return new TransletOutputHandlerFactory();
    }

    public void setOutputType(int n2) {
        this._outputType = n2;
    }

    public void setEncoding(String string) {
        if (string != null) {
            this._encoding = string;
        }
    }

    public void setOutputMethod(String string) {
        this._method = string;
    }

    public void setOutputStream(OutputStream outputStream) {
        this._ostream = outputStream;
    }

    public void setWriter(Writer writer) {
        this._writer = writer;
    }

    public void setHandler(ContentHandler contentHandler) {
        this._handler = contentHandler;
    }

    public void setLexicalHandler(LexicalHandler lexicalHandler) {
        this._lexHandler = lexicalHandler;
    }

    public void setNode(Node node) {
        this._node = node;
    }

    public Node getNode() {
        return this._handler instanceof SAX2DOM ? ((SAX2DOM)this._handler).getDOM() : null;
    }

    public void setNextSibling(Node node) {
        this._nextSibling = node;
    }

    public void setIndentNumber(int n2) {
        this._indentNumber = n2;
    }

    public SerializationHandler getSerializationHandler() throws IOException, ParserConfigurationException {
        Serializer serializer = null;
        switch (this._outputType) {
            case 0: {
                if (this._method == null) {
                    serializer = new ToUnknownStream();
                } else if (this._method.equalsIgnoreCase("xml")) {
                    serializer = new ToXMLStream();
                } else if (this._method.equalsIgnoreCase("html")) {
                    serializer = new ToHTMLStream();
                } else if (this._method.equalsIgnoreCase("text")) {
                    serializer = new ToTextStream();
                }
                if (serializer != null && this._indentNumber >= 0) {
                    serializer.setIndentAmount(this._indentNumber);
                }
                serializer.setEncoding(this._encoding);
                if (this._writer != null) {
                    serializer.setWriter(this._writer);
                } else {
                    serializer.setOutputStream(this._ostream);
                }
                return serializer;
            }
            case 2: {
                this._handler = this._node != null ? new SAX2DOM(this._node, this._nextSibling) : new SAX2DOM();
                this._lexHandler = (LexicalHandler)((Object)this._handler);
            }
            case 1: {
                if (this._method == null) {
                    this._method = "xml";
                }
                serializer = this._lexHandler == null ? new ToXMLSAXHandler(this._handler, this._encoding) : new ToXMLSAXHandler(this._handler, this._lexHandler, this._encoding);
                return serializer;
            }
        }
        return null;
    }
}

