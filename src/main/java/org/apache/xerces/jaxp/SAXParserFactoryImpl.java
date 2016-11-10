/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp;

import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import org.apache.xerces.jaxp.SAXParserImpl;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class SAXParserFactoryImpl
extends SAXParserFactory {
    private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
    private Hashtable features;
    private Schema grammar;
    private boolean isXIncludeAware;
    private boolean fSecureProcess = false;

    public SAXParser newSAXParser() throws ParserConfigurationException {
        SAXParserImpl sAXParserImpl;
        try {
            sAXParserImpl = new SAXParserImpl(this, this.features, this.fSecureProcess);
        }
        catch (SAXException sAXException) {
            throw new ParserConfigurationException(sAXException.getMessage());
        }
        return sAXParserImpl;
    }

    private SAXParserImpl newSAXParserImpl() throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        SAXParserImpl sAXParserImpl;
        try {
            sAXParserImpl = new SAXParserImpl(this, this.features);
        }
        catch (SAXNotSupportedException sAXNotSupportedException) {
            throw sAXNotSupportedException;
        }
        catch (SAXNotRecognizedException sAXNotRecognizedException) {
            throw sAXNotRecognizedException;
        }
        catch (SAXException sAXException) {
            throw new ParserConfigurationException(sAXException.getMessage());
        }
        return sAXParserImpl;
    }

    public void setFeature(String string, boolean bl) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        if (string == null) {
            throw new NullPointerException();
        }
        if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            this.fSecureProcess = bl;
            return;
        }
        if (string.equals("http://xml.org/sax/features/namespaces")) {
            this.setNamespaceAware(bl);
            return;
        }
        if (string.equals("http://xml.org/sax/features/validation")) {
            this.setValidating(bl);
            return;
        }
        if (string.equals("http://apache.org/xml/features/xinclude")) {
            this.setXIncludeAware(bl);
            return;
        }
        if (this.features == null) {
            this.features = new Hashtable();
        }
        this.features.put(string, bl ? Boolean.TRUE : Boolean.FALSE);
        try {
            this.newSAXParserImpl();
        }
        catch (SAXNotSupportedException sAXNotSupportedException) {
            this.features.remove(string);
            throw sAXNotSupportedException;
        }
        catch (SAXNotRecognizedException sAXNotRecognizedException) {
            this.features.remove(string);
            throw sAXNotRecognizedException;
        }
    }

    public boolean getFeature(String string) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        if (string == null) {
            throw new NullPointerException();
        }
        if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return this.fSecureProcess;
        }
        if (string.equals("http://xml.org/sax/features/namespaces")) {
            return this.isNamespaceAware();
        }
        if (string.equals("http://xml.org/sax/features/validation")) {
            return this.isValidating();
        }
        if (string.equals("http://apache.org/xml/features/xinclude")) {
            return this.isXIncludeAware();
        }
        return this.newSAXParserImpl().getXMLReader().getFeature(string);
    }

    public Schema getSchema() {
        return this.grammar;
    }

    public void setSchema(Schema schema) {
        this.grammar = schema;
    }

    public boolean isXIncludeAware() {
        return this.isXIncludeAware;
    }

    public void setXIncludeAware(boolean bl) {
        this.isXIncludeAware = bl;
    }
}

