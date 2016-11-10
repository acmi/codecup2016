/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;

public abstract class BasicParserConfiguration
extends ParserConfigurationSettings
implements XMLParserConfiguration {
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
    protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected SymbolTable fSymbolTable;
    protected Locale fLocale;
    protected ArrayList fComponents = new ArrayList();
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDTDHandler fDTDHandler;
    protected XMLDTDContentModelHandler fDTDContentModelHandler;
    protected XMLDocumentSource fLastComponent;

    protected BasicParserConfiguration() {
        this(null, null);
    }

    protected BasicParserConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null);
    }

    protected BasicParserConfiguration(SymbolTable symbolTable, XMLComponentManager xMLComponentManager) {
        super(xMLComponentManager);
        this.fRecognizedFeatures = new ArrayList();
        this.fRecognizedProperties = new ArrayList();
        this.fFeatures = new HashMap();
        this.fProperties = new HashMap();
        String[] arrstring = new String[]{"http://apache.org/xml/features/internal/parser-settings", "http://xml.org/sax/features/validation", "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities"};
        this.addRecognizedFeatures(arrstring);
        this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
        this.fFeatures.put("http://xml.org/sax/features/validation", Boolean.FALSE);
        this.fFeatures.put("http://xml.org/sax/features/namespaces", Boolean.TRUE);
        this.fFeatures.put("http://xml.org/sax/features/external-general-entities", Boolean.TRUE);
        this.fFeatures.put("http://xml.org/sax/features/external-parameter-entities", Boolean.TRUE);
        String[] arrstring2 = new String[]{"http://xml.org/sax/properties/xml-string", "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver"};
        this.addRecognizedProperties(arrstring2);
        if (symbolTable == null) {
            symbolTable = new SymbolTable();
        }
        this.fSymbolTable = symbolTable;
        this.fProperties.put("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
    }

    protected void addComponent(XMLComponent xMLComponent) {
        int n2;
        Object object;
        String string;
        if (this.fComponents.contains(xMLComponent)) {
            return;
        }
        this.fComponents.add(xMLComponent);
        String[] arrstring = xMLComponent.getRecognizedFeatures();
        this.addRecognizedFeatures(arrstring);
        String[] arrstring2 = xMLComponent.getRecognizedProperties();
        this.addRecognizedProperties(arrstring2);
        if (arrstring != null) {
            n2 = 0;
            while (n2 < arrstring.length) {
                string = arrstring[n2];
                object = xMLComponent.getFeatureDefault(string);
                if (object != null) {
                    super.setFeature(string, object.booleanValue());
                }
                ++n2;
            }
        }
        if (arrstring2 != null) {
            n2 = 0;
            while (n2 < arrstring2.length) {
                string = arrstring2[n2];
                object = xMLComponent.getPropertyDefault(string);
                if (object != null) {
                    super.setProperty(string, object);
                }
                ++n2;
            }
        }
    }

    public abstract void parse(XMLInputSource var1) throws XNIException, IOException;

    public void setDocumentHandler(XMLDocumentHandler xMLDocumentHandler) {
        this.fDocumentHandler = xMLDocumentHandler;
        if (this.fLastComponent != null) {
            this.fLastComponent.setDocumentHandler(this.fDocumentHandler);
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.setDocumentSource(this.fLastComponent);
            }
        }
    }

    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    public void setDTDHandler(XMLDTDHandler xMLDTDHandler) {
        this.fDTDHandler = xMLDTDHandler;
    }

    public XMLDTDHandler getDTDHandler() {
        return this.fDTDHandler;
    }

    public void setDTDContentModelHandler(XMLDTDContentModelHandler xMLDTDContentModelHandler) {
        this.fDTDContentModelHandler = xMLDTDContentModelHandler;
    }

    public XMLDTDContentModelHandler getDTDContentModelHandler() {
        return this.fDTDContentModelHandler;
    }

    public void setEntityResolver(XMLEntityResolver xMLEntityResolver) {
        this.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", xMLEntityResolver);
    }

    public XMLEntityResolver getEntityResolver() {
        return (XMLEntityResolver)this.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
    }

    public void setErrorHandler(XMLErrorHandler xMLErrorHandler) {
        this.fProperties.put("http://apache.org/xml/properties/internal/error-handler", xMLErrorHandler);
    }

    public XMLErrorHandler getErrorHandler() {
        return (XMLErrorHandler)this.fProperties.get("http://apache.org/xml/properties/internal/error-handler");
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
        int n2 = this.fComponents.size();
        int n3 = 0;
        while (n3 < n2) {
            XMLComponent xMLComponent = (XMLComponent)this.fComponents.get(n3);
            xMLComponent.setFeature(string, bl);
            ++n3;
        }
        super.setFeature(string, bl);
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        int n2 = this.fComponents.size();
        int n3 = 0;
        while (n3 < n2) {
            XMLComponent xMLComponent = (XMLComponent)this.fComponents.get(n3);
            xMLComponent.setProperty(string, object);
            ++n3;
        }
        super.setProperty(string, object);
    }

    public void setLocale(Locale locale) throws XNIException {
        this.fLocale = locale;
    }

    public Locale getLocale() {
        return this.fLocale;
    }

    protected void reset() throws XNIException {
        int n2 = this.fComponents.size();
        int n3 = 0;
        while (n3 < n2) {
            XMLComponent xMLComponent = (XMLComponent)this.fComponents.get(n3);
            xMLComponent.reset(this);
            ++n3;
        }
    }

    protected void checkProperty(String string) throws XMLConfigurationException {
        int n2;
        if (string.startsWith("http://xml.org/sax/properties/") && (n2 = string.length() - "http://xml.org/sax/properties/".length()) == "xml-string".length() && string.endsWith("xml-string")) {
            short s2 = 1;
            throw new XMLConfigurationException(s2, string);
        }
        super.checkProperty(string);
    }

    protected void checkFeature(String string) throws XMLConfigurationException {
        int n2;
        if (string.startsWith("http://apache.org/xml/features/") && (n2 = string.length() - "http://apache.org/xml/features/".length()) == "internal/parser-settings".length() && string.endsWith("internal/parser-settings")) {
            short s2 = 1;
            throw new XMLConfigurationException(s2, string);
        }
        super.checkFeature(string);
    }
}

