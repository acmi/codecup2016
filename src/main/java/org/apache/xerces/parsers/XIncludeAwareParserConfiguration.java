/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.parsers;

import java.util.HashMap;
import org.apache.xerces.impl.XML11DTDScannerImpl;
import org.apache.xerces.impl.dtd.XML11DTDProcessor;
import org.apache.xerces.impl.dtd.XMLDTDProcessor;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.parsers.XML11Configuration;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xinclude.XIncludeNamespaceSupport;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLDTDSource;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XIncludeAwareParserConfiguration
extends XML11Configuration {
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
    protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
    protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
    protected static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
    protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
    protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
    protected XIncludeHandler fXIncludeHandler;
    protected NamespaceSupport fNonXIncludeNSContext;
    protected XIncludeNamespaceSupport fXIncludeNSContext;
    protected NamespaceContext fCurrentNSContext;
    protected boolean fXIncludeEnabled = false;

    public XIncludeAwareParserConfiguration() {
        this(null, null, null);
    }

    public XIncludeAwareParserConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null, null);
    }

    public XIncludeAwareParserConfiguration(SymbolTable symbolTable, XMLGrammarPool xMLGrammarPool) {
        this(symbolTable, xMLGrammarPool, null);
    }

    public XIncludeAwareParserConfiguration(SymbolTable symbolTable, XMLGrammarPool xMLGrammarPool, XMLComponentManager xMLComponentManager) {
        super(symbolTable, xMLGrammarPool, xMLComponentManager);
        String[] arrstring = new String[]{"http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language"};
        this.addRecognizedFeatures(arrstring);
        String[] arrstring2 = new String[]{"http://apache.org/xml/properties/internal/xinclude-handler", "http://apache.org/xml/properties/internal/namespace-context"};
        this.addRecognizedProperties(arrstring2);
        this.setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", true);
        this.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
        this.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
        this.fNonXIncludeNSContext = new NamespaceSupport();
        this.fCurrentNSContext = this.fNonXIncludeNSContext;
        this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNonXIncludeNSContext);
    }

    protected void configurePipeline() {
        super.configurePipeline();
        if (this.fXIncludeEnabled) {
            if (this.fXIncludeHandler == null) {
                this.fXIncludeHandler = new XIncludeHandler();
                this.setProperty("http://apache.org/xml/properties/internal/xinclude-handler", this.fXIncludeHandler);
                this.addCommonComponent(this.fXIncludeHandler);
                this.fXIncludeHandler.reset(this);
            }
            if (this.fCurrentNSContext != this.fXIncludeNSContext) {
                if (this.fXIncludeNSContext == null) {
                    this.fXIncludeNSContext = new XIncludeNamespaceSupport();
                }
                this.fCurrentNSContext = this.fXIncludeNSContext;
                this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fXIncludeNSContext);
            }
            this.fDTDScanner.setDTDHandler(this.fDTDProcessor);
            this.fDTDProcessor.setDTDSource(this.fDTDScanner);
            this.fDTDProcessor.setDTDHandler(this.fXIncludeHandler);
            this.fXIncludeHandler.setDTDSource(this.fDTDProcessor);
            this.fXIncludeHandler.setDTDHandler(this.fDTDHandler);
            if (this.fDTDHandler != null) {
                this.fDTDHandler.setDTDSource(this.fXIncludeHandler);
            }
            XMLDocumentSource xMLDocumentSource = null;
            if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE) {
                xMLDocumentSource = this.fSchemaValidator.getDocumentSource();
            } else {
                xMLDocumentSource = this.fLastComponent;
                this.fLastComponent = this.fXIncludeHandler;
            }
            XMLDocumentHandler xMLDocumentHandler = xMLDocumentSource.getDocumentHandler();
            xMLDocumentSource.setDocumentHandler(this.fXIncludeHandler);
            this.fXIncludeHandler.setDocumentSource(xMLDocumentSource);
            if (xMLDocumentHandler != null) {
                this.fXIncludeHandler.setDocumentHandler(xMLDocumentHandler);
                xMLDocumentHandler.setDocumentSource(this.fXIncludeHandler);
            }
        } else if (this.fCurrentNSContext != this.fNonXIncludeNSContext) {
            this.fCurrentNSContext = this.fNonXIncludeNSContext;
            this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNonXIncludeNSContext);
        }
    }

    protected void configureXML11Pipeline() {
        super.configureXML11Pipeline();
        if (this.fXIncludeEnabled) {
            if (this.fXIncludeHandler == null) {
                this.fXIncludeHandler = new XIncludeHandler();
                this.setProperty("http://apache.org/xml/properties/internal/xinclude-handler", this.fXIncludeHandler);
                this.addCommonComponent(this.fXIncludeHandler);
                this.fXIncludeHandler.reset(this);
            }
            if (this.fCurrentNSContext != this.fXIncludeNSContext) {
                if (this.fXIncludeNSContext == null) {
                    this.fXIncludeNSContext = new XIncludeNamespaceSupport();
                }
                this.fCurrentNSContext = this.fXIncludeNSContext;
                this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fXIncludeNSContext);
            }
            this.fXML11DTDScanner.setDTDHandler(this.fXML11DTDProcessor);
            this.fXML11DTDProcessor.setDTDSource(this.fXML11DTDScanner);
            this.fXML11DTDProcessor.setDTDHandler(this.fXIncludeHandler);
            this.fXIncludeHandler.setDTDSource(this.fXML11DTDProcessor);
            this.fXIncludeHandler.setDTDHandler(this.fDTDHandler);
            if (this.fDTDHandler != null) {
                this.fDTDHandler.setDTDSource(this.fXIncludeHandler);
            }
            XMLDocumentSource xMLDocumentSource = null;
            if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE) {
                xMLDocumentSource = this.fSchemaValidator.getDocumentSource();
            } else {
                xMLDocumentSource = this.fLastComponent;
                this.fLastComponent = this.fXIncludeHandler;
            }
            XMLDocumentHandler xMLDocumentHandler = xMLDocumentSource.getDocumentHandler();
            xMLDocumentSource.setDocumentHandler(this.fXIncludeHandler);
            this.fXIncludeHandler.setDocumentSource(xMLDocumentSource);
            if (xMLDocumentHandler != null) {
                this.fXIncludeHandler.setDocumentHandler(xMLDocumentHandler);
                xMLDocumentHandler.setDocumentSource(this.fXIncludeHandler);
            }
        } else if (this.fCurrentNSContext != this.fNonXIncludeNSContext) {
            this.fCurrentNSContext = this.fNonXIncludeNSContext;
            this.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNonXIncludeNSContext);
        }
    }

    public boolean getFeature(String string) throws XMLConfigurationException {
        if (string.equals("http://apache.org/xml/features/internal/parser-settings")) {
            return this.fConfigUpdated;
        }
        if (string.equals("http://apache.org/xml/features/xinclude")) {
            return this.fXIncludeEnabled;
        }
        return super.getFeature0(string);
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
        if (string.equals("http://apache.org/xml/features/xinclude")) {
            this.fXIncludeEnabled = bl;
            this.fConfigUpdated = true;
            return;
        }
        super.setFeature(string, bl);
    }
}

