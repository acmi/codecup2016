/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp;

import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.apache.xerces.jaxp.DocumentBuilderImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.util.SAXMessageFormatter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class DocumentBuilderFactoryImpl
extends DocumentBuilderFactory {
    private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
    private static final String INCLUDE_IGNORABLE_WHITESPACE = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
    private static final String CREATE_ENTITY_REF_NODES_FEATURE = "http://apache.org/xml/features/dom/create-entity-ref-nodes";
    private static final String INCLUDE_COMMENTS_FEATURE = "http://apache.org/xml/features/include-comments";
    private static final String CREATE_CDATA_NODES_FEATURE = "http://apache.org/xml/features/create-cdata-nodes";
    private Hashtable attributes;
    private Hashtable features;
    private Schema grammar;
    private boolean isXIncludeAware;
    private boolean fSecureProcess = false;

    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        if (this.grammar != null && this.attributes != null) {
            if (this.attributes.containsKey("http://java.sun.com/xml/jaxp/properties/schemaLanguage")) {
                throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[]{"http://java.sun.com/xml/jaxp/properties/schemaLanguage"}));
            }
            if (this.attributes.containsKey("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
                throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[]{"http://java.sun.com/xml/jaxp/properties/schemaSource"}));
            }
        }
        try {
            return new DocumentBuilderImpl(this, this.attributes, this.features, this.fSecureProcess);
        }
        catch (SAXException sAXException) {
            throw new ParserConfigurationException(sAXException.getMessage());
        }
    }

    public void setAttribute(String string, Object object) throws IllegalArgumentException {
        if (object == null) {
            if (this.attributes != null) {
                this.attributes.remove(string);
            }
            return;
        }
        if (this.attributes == null) {
            this.attributes = new Hashtable();
        }
        this.attributes.put(string, object);
        try {
            new DocumentBuilderImpl(this, this.attributes, this.features);
        }
        catch (Exception exception) {
            this.attributes.remove(string);
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    public Object getAttribute(String string) throws IllegalArgumentException {
        DOMParser dOMParser;
        if (this.attributes != null && (dOMParser = (DOMParser)this.attributes.get(string)) != null) {
            return dOMParser;
        }
        dOMParser = null;
        try {
            dOMParser = new DocumentBuilderImpl(this, this.attributes, this.features).getDOMParser();
            return dOMParser.getProperty(string);
        }
        catch (SAXException sAXException) {
            try {
                boolean bl = dOMParser.getFeature(string);
                return bl ? Boolean.TRUE : Boolean.FALSE;
            }
            catch (SAXException sAXException2) {
                throw new IllegalArgumentException(sAXException.getMessage());
            }
        }
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

    public boolean getFeature(String string) throws ParserConfigurationException {
        Object object;
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
        if (string.equals("http://apache.org/xml/features/dom/include-ignorable-whitespace")) {
            return !this.isIgnoringElementContentWhitespace();
        }
        if (string.equals("http://apache.org/xml/features/dom/create-entity-ref-nodes")) {
            return !this.isExpandEntityReferences();
        }
        if (string.equals("http://apache.org/xml/features/include-comments")) {
            return !this.isIgnoringComments();
        }
        if (string.equals("http://apache.org/xml/features/create-cdata-nodes")) {
            return !this.isCoalescing();
        }
        if (this.features != null && (object = this.features.get(string)) != null) {
            return (Boolean)object;
        }
        try {
            object = new DocumentBuilderImpl(this, this.attributes, this.features).getDOMParser();
            return object.getFeature(string);
        }
        catch (SAXException sAXException) {
            throw new ParserConfigurationException(sAXException.getMessage());
        }
    }

    public void setFeature(String string, boolean bl) throws ParserConfigurationException {
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
        if (string.equals("http://apache.org/xml/features/dom/include-ignorable-whitespace")) {
            this.setIgnoringElementContentWhitespace(!bl);
            return;
        }
        if (string.equals("http://apache.org/xml/features/dom/create-entity-ref-nodes")) {
            this.setExpandEntityReferences(!bl);
            return;
        }
        if (string.equals("http://apache.org/xml/features/include-comments")) {
            this.setIgnoringComments(!bl);
            return;
        }
        if (string.equals("http://apache.org/xml/features/create-cdata-nodes")) {
            this.setCoalescing(!bl);
            return;
        }
        if (this.features == null) {
            this.features = new Hashtable();
        }
        this.features.put(string, bl ? Boolean.TRUE : Boolean.FALSE);
        try {
            new DocumentBuilderImpl(this, this.attributes, this.features);
        }
        catch (SAXNotSupportedException sAXNotSupportedException) {
            this.features.remove(string);
            throw new ParserConfigurationException(sAXNotSupportedException.getMessage());
        }
        catch (SAXNotRecognizedException sAXNotRecognizedException) {
            this.features.remove(string);
            throw new ParserConfigurationException(sAXNotRecognizedException.getMessage());
        }
    }
}

