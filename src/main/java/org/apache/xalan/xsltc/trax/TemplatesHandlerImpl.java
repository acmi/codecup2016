/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.TemplatesHandler;
import org.apache.xalan.xsltc.compiler.CompilerException;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.trax.TemplatesImpl;
import org.apache.xalan.xsltc.trax.TransformerFactoryImpl;
import org.apache.xalan.xsltc.trax.Util;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class TemplatesHandlerImpl
implements TemplatesHandler,
SourceLoader,
ContentHandler {
    private String _systemId;
    private int _indentNumber;
    private URIResolver _uriResolver = null;
    private TransformerFactoryImpl _tfactory = null;
    private Parser _parser = null;
    private TemplatesImpl _templates = null;

    protected TemplatesHandlerImpl(int n2, TransformerFactoryImpl transformerFactoryImpl) {
        this._indentNumber = n2;
        this._tfactory = transformerFactoryImpl;
        XSLTC xSLTC = new XSLTC();
        if (transformerFactoryImpl.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
            xSLTC.setSecureProcessing(true);
        }
        if ("true".equals(transformerFactoryImpl.getAttribute("enable-inlining"))) {
            xSLTC.setTemplateInlining(true);
        } else {
            xSLTC.setTemplateInlining(false);
        }
        this._parser = xSLTC.getParser();
    }

    public String getSystemId() {
        return this._systemId;
    }

    public void setSystemId(String string) {
        this._systemId = string;
    }

    public void setURIResolver(URIResolver uRIResolver) {
        this._uriResolver = uRIResolver;
    }

    public Templates getTemplates() {
        return this._templates;
    }

    public InputSource loadSource(String string, String string2, XSLTC xSLTC) {
        try {
            Source source = this._uriResolver.resolve(string, string2);
            if (source != null) {
                return Util.getInputSource(xSLTC, source);
            }
        }
        catch (TransformerException transformerException) {
            // empty catch block
        }
        return null;
    }

    public void startDocument() {
        XSLTC xSLTC = this._parser.getXSLTC();
        xSLTC.init();
        xSLTC.setOutputType(2);
        this._parser.startDocument();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void endDocument() throws SAXException {
        block15 : {
            this._parser.endDocument();
            try {
                byte[][] arrby;
                XSLTC xSLTC = this._parser.getXSLTC();
                String string = this._systemId != null ? Util.baseName(this._systemId) : (String)this._tfactory.getAttribute("translet-name");
                xSLTC.setClassName(string);
                string = xSLTC.getClassName();
                Stylesheet stylesheet = null;
                SyntaxTreeNode syntaxTreeNode = this._parser.getDocumentRoot();
                if (!this._parser.errorsFound() && syntaxTreeNode != null) {
                    stylesheet = this._parser.makeStylesheet(syntaxTreeNode);
                    stylesheet.setSystemId(this._systemId);
                    stylesheet.setParentStylesheet(null);
                    if (xSLTC.getTemplateInlining()) {
                        stylesheet.setTemplateInlining(true);
                    } else {
                        stylesheet.setTemplateInlining(false);
                    }
                    if (this._uriResolver != null) {
                        stylesheet.setSourceLoader(this);
                    }
                    this._parser.setCurrentStylesheet(stylesheet);
                    xSLTC.setStylesheet(stylesheet);
                    this._parser.createAST(stylesheet);
                }
                if (!this._parser.errorsFound() && stylesheet != null) {
                    stylesheet.setMultiDocument(xSLTC.isMultiDocument());
                    stylesheet.setHasIdCall(xSLTC.hasIdCall());
                    arrby = xSLTC.getClass();
                    synchronized (arrby) {
                        stylesheet.translate();
                    }
                }
                if (!this._parser.errorsFound()) {
                    arrby = xSLTC.getBytecodes();
                    if (arrby != null) {
                        this._templates = new TemplatesImpl(xSLTC.getBytecodes(), string, this._parser.getOutputProperties(), this._indentNumber, this._tfactory);
                        if (this._uriResolver != null) {
                            this._templates.setURIResolver(this._uriResolver);
                        }
                    }
                    break block15;
                }
                arrby = new StringBuffer();
                Vector vector = this._parser.getErrors();
                int n2 = vector.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    if (arrby.length() > 0) {
                        arrby.append('\n');
                    }
                    arrby.append(vector.elementAt(i2).toString());
                }
                throw new SAXException("JAXP_COMPILE_ERR", new TransformerException(arrby.toString()));
            }
            catch (CompilerException compilerException) {
                throw new SAXException("JAXP_COMPILE_ERR", compilerException);
            }
        }
    }

    public void startPrefixMapping(String string, String string2) {
        this._parser.startPrefixMapping(string, string2);
    }

    public void endPrefixMapping(String string) {
        this._parser.endPrefixMapping(string);
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        this._parser.startElement(string, string2, string3, attributes);
    }

    public void endElement(String string, String string2, String string3) {
        this._parser.endElement(string, string2, string3);
    }

    public void characters(char[] arrc, int n2, int n3) {
        this._parser.characters(arrc, n2, n3);
    }

    public void processingInstruction(String string, String string2) {
        this._parser.processingInstruction(string, string2);
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) {
        this._parser.ignorableWhitespace(arrc, n2, n3);
    }

    public void skippedEntity(String string) {
        this._parser.skippedEntity(string);
    }

    public void setDocumentLocator(Locator locator) {
        this.setSystemId(locator.getSystemId());
        this._parser.setDocumentLocator(locator);
    }
}

