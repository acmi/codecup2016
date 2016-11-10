/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TemplatesHandler;
import org.apache.xalan.extensions.ExpressionVisitor;
import org.apache.xalan.processor.ProcessorStylesheetDoc;
import org.apache.xalan.processor.ProcessorStylesheetElement;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xalan.processor.XSLTElementDef;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.processor.XSLTSchema;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemForEach;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.NamespaceSupport2;
import org.apache.xml.utils.NodeConsumer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.compiler.FunctionTable;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;

public class StylesheetHandler
extends DefaultHandler
implements TemplatesHandler,
NodeConsumer,
PrefixResolver {
    private FunctionTable m_funcTable = new FunctionTable();
    private boolean m_optimize = true;
    private boolean m_incremental = false;
    private boolean m_source_location = false;
    private int m_stylesheetLevel = -1;
    private boolean m_parsingComplete = false;
    private Vector m_prefixMappings = new Vector();
    private boolean m_shouldProcess = true;
    private String m_fragmentIDString;
    private int m_elementID = 0;
    private int m_fragmentID = 0;
    private TransformerFactoryImpl m_stylesheetProcessor;
    public static final int STYPE_ROOT = 1;
    public static final int STYPE_INCLUDE = 2;
    public static final int STYPE_IMPORT = 3;
    private int m_stylesheetType = 1;
    private Stack m_stylesheets = new Stack();
    StylesheetRoot m_stylesheetRoot;
    Stylesheet m_lastPoppedStylesheet;
    private Stack m_processors = new Stack();
    private XSLTSchema m_schema = new XSLTSchema();
    private Stack m_elems = new Stack();
    private int m_docOrderCount = 0;
    Stack m_baseIdentifiers = new Stack();
    private Stack m_stylesheetLocatorStack = new Stack();
    private Stack m_importStack = new Stack();
    private Stack m_importSourceStack = new Stack();
    private boolean warnedAboutOldXSLTNamespace = false;
    Stack m_nsSupportStack = new Stack();
    private Node m_originatingNode;
    private BoolStack m_spacePreserveStack = new BoolStack();
    static Class class$org$apache$xalan$templates$FuncDocument;
    static Class class$org$apache$xalan$templates$FuncFormatNumb;

    public StylesheetHandler(TransformerFactoryImpl transformerFactoryImpl) throws TransformerConfigurationException {
        Class class_ = class$org$apache$xalan$templates$FuncDocument == null ? (StylesheetHandler.class$org$apache$xalan$templates$FuncDocument = StylesheetHandler.class$("org.apache.xalan.templates.FuncDocument")) : class$org$apache$xalan$templates$FuncDocument;
        Class class_2 = class_;
        this.m_funcTable.installFunction("document", class_2);
        Class class_3 = class$org$apache$xalan$templates$FuncFormatNumb == null ? (StylesheetHandler.class$org$apache$xalan$templates$FuncFormatNumb = StylesheetHandler.class$("org.apache.xalan.templates.FuncFormatNumb")) : class$org$apache$xalan$templates$FuncFormatNumb;
        class_2 = class_3;
        this.m_funcTable.installFunction("format-number", class_2);
        this.m_optimize = (Boolean)transformerFactoryImpl.getAttribute("http://xml.apache.org/xalan/features/optimize");
        this.m_incremental = (Boolean)transformerFactoryImpl.getAttribute("http://xml.apache.org/xalan/features/incremental");
        this.m_source_location = (Boolean)transformerFactoryImpl.getAttribute("http://xml.apache.org/xalan/properties/source-location");
        this.init(transformerFactoryImpl);
    }

    void init(TransformerFactoryImpl transformerFactoryImpl) {
        this.m_stylesheetProcessor = transformerFactoryImpl;
        this.m_processors.push(this.m_schema.getElementProcessor());
        this.pushNewNamespaceSupport();
    }

    public XPath createXPath(String string, ElemTemplateElement elemTemplateElement) throws TransformerException {
        ErrorListener errorListener = this.m_stylesheetProcessor.getErrorListener();
        XPath xPath = new XPath(string, elemTemplateElement, this, 0, errorListener, this.m_funcTable);
        xPath.callVisitors(xPath, new ExpressionVisitor(this.getStylesheetRoot()));
        return xPath;
    }

    XPath createMatchPatternXPath(String string, ElemTemplateElement elemTemplateElement) throws TransformerException {
        ErrorListener errorListener = this.m_stylesheetProcessor.getErrorListener();
        XPath xPath = new XPath(string, elemTemplateElement, this, 1, errorListener, this.m_funcTable);
        xPath.callVisitors(xPath, new ExpressionVisitor(this.getStylesheetRoot()));
        return xPath;
    }

    public String getNamespaceForPrefix(String string) {
        return this.getNamespaceSupport().getURI(string);
    }

    public String getNamespaceForPrefix(String string, Node node) {
        this.assertion(true, "can't process a context node in StylesheetHandler!");
        return null;
    }

    private boolean stackContains(Stack stack, String string) {
        int n2 = stack.size();
        boolean bl = false;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string2 = (String)stack.elementAt(i2);
            if (!string2.equals(string)) continue;
            bl = true;
            break;
        }
        return bl;
    }

    public Templates getTemplates() {
        return this.getStylesheetRoot();
    }

    public void setSystemId(String string) {
        this.pushBaseIndentifier(string);
    }

    public String getSystemId() {
        return this.getBaseIdentifier();
    }

    public InputSource resolveEntity(String string, String string2) throws SAXException {
        return this.getCurrentProcessor().resolveEntity(this, string, string2);
    }

    public void notationDecl(String string, String string2, String string3) {
        this.getCurrentProcessor().notationDecl(this, string, string2, string3);
    }

    public void unparsedEntityDecl(String string, String string2, String string3, String string4) {
        this.getCurrentProcessor().unparsedEntityDecl(this, string, string2, string3, string4);
    }

    XSLTElementProcessor getProcessorFor(String string, String string2, String string3) throws SAXException {
        XSLTElementProcessor xSLTElementProcessor = this.getCurrentProcessor();
        XSLTElementDef xSLTElementDef = xSLTElementProcessor.getElemDef();
        XSLTElementProcessor xSLTElementProcessor2 = xSLTElementDef.getProcessorFor(string, string2);
        if (null == xSLTElementProcessor2 && !(xSLTElementProcessor instanceof ProcessorStylesheetDoc) && (null == this.getStylesheet() || Double.valueOf(this.getStylesheet().getVersion()) > 1.0 || !string.equals("http://www.w3.org/1999/XSL/Transform") && xSLTElementProcessor instanceof ProcessorStylesheetElement || this.getElemVersion() > 1.0)) {
            xSLTElementProcessor2 = xSLTElementDef.getProcessorForUnknown(string, string2);
        }
        if (null == xSLTElementProcessor2) {
            this.error(XSLMessages.createMessage("ER_NOT_ALLOWED_IN_POSITION", new Object[]{string3}), null);
        }
        return xSLTElementProcessor2;
    }

    public void setDocumentLocator(Locator locator) {
        this.m_stylesheetLocatorStack.push(new SAXSourceLocator(locator));
    }

    public void startDocument() throws SAXException {
        ++this.m_stylesheetLevel;
        this.pushSpaceHandling(false);
    }

    public boolean isStylesheetParsingComplete() {
        return this.m_parsingComplete;
    }

    public void endDocument() throws SAXException {
        try {
            if (null != this.getStylesheetRoot()) {
                if (0 == this.m_stylesheetLevel) {
                    this.getStylesheetRoot().recompose();
                }
            } else {
                throw new TransformerException(XSLMessages.createMessage("ER_NO_STYLESHEETROOT", null));
            }
            XSLTElementProcessor xSLTElementProcessor = this.getCurrentProcessor();
            if (null != xSLTElementProcessor) {
                xSLTElementProcessor.startNonText(this);
            }
            --this.m_stylesheetLevel;
            this.popSpaceHandling();
            this.m_parsingComplete = this.m_stylesheetLevel < 0;
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        this.m_prefixMappings.addElement(string);
        this.m_prefixMappings.addElement(string2);
    }

    public void endPrefixMapping(String string) throws SAXException {
    }

    private void flushCharacters() throws SAXException {
        XSLTElementProcessor xSLTElementProcessor = this.getCurrentProcessor();
        if (null != xSLTElementProcessor) {
            xSLTElementProcessor.startNonText(this);
        }
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        NamespaceSupport namespaceSupport = this.getNamespaceSupport();
        namespaceSupport.pushContext();
        int n2 = this.m_prefixMappings.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string4 = (String)this.m_prefixMappings.elementAt(i2++);
            String string5 = (String)this.m_prefixMappings.elementAt(i2);
            namespaceSupport.declarePrefix(string4, string5);
        }
        this.m_prefixMappings.removeAllElements();
        ++this.m_elementID;
        this.checkForFragmentID(attributes);
        if (!this.m_shouldProcess) {
            return;
        }
        this.flushCharacters();
        this.pushSpaceHandling(attributes);
        XSLTElementProcessor xSLTElementProcessor = this.getProcessorFor(string, string2, string3);
        if (null != xSLTElementProcessor) {
            this.pushProcessor(xSLTElementProcessor);
            xSLTElementProcessor.startElement(this, string, string2, string3, attributes);
        } else {
            this.m_shouldProcess = false;
            this.popSpaceHandling();
        }
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        --this.m_elementID;
        if (!this.m_shouldProcess) {
            return;
        }
        if (this.m_elementID + 1 == this.m_fragmentID) {
            this.m_shouldProcess = false;
        }
        this.flushCharacters();
        this.popSpaceHandling();
        XSLTElementProcessor xSLTElementProcessor = this.getCurrentProcessor();
        xSLTElementProcessor.endElement(this, string, string2, string3);
        this.popProcessor();
        this.getNamespaceSupport().popContext();
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (!this.m_shouldProcess) {
            return;
        }
        XSLTElementProcessor xSLTElementProcessor = this.getCurrentProcessor();
        XSLTElementDef xSLTElementDef = xSLTElementProcessor.getElemDef();
        if (xSLTElementDef.getType() != 2) {
            xSLTElementProcessor = xSLTElementDef.getProcessorFor(null, "text()");
        }
        if (null == xSLTElementProcessor) {
            if (!XMLCharacterRecognizer.isWhiteSpace(arrc, n2, n3)) {
                this.error(XSLMessages.createMessage("ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION", null), null);
            }
        } else {
            xSLTElementProcessor.characters(this, arrc, n2, n3);
        }
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        if (!this.m_shouldProcess) {
            return;
        }
        this.getCurrentProcessor().ignorableWhitespace(this, arrc, n2, n3);
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        if (!this.m_shouldProcess) {
            return;
        }
        String string3 = "";
        String string4 = "";
        String string5 = string;
        int n2 = string.indexOf(58);
        if (n2 >= 0) {
            string3 = string.substring(0, n2);
            string4 = this.getNamespaceForPrefix(string3);
            string5 = string.substring(n2 + 1);
        }
        try {
            if ("xalan-doc-cache-off".equals(string) || "xalan:doc-cache-off".equals(string) || "doc-cache-off".equals(string5) && string4.equals("org.apache.xalan.xslt.extensions.Redirect")) {
                if (!(this.m_elems.peek() instanceof ElemForEach)) {
                    throw new TransformerException("xalan:doc-cache-off not allowed here!", this.getLocator());
                }
                ElemForEach elemForEach = (ElemForEach)this.m_elems.peek();
                elemForEach.m_doc_cache_off = true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.flushCharacters();
        this.getCurrentProcessor().processingInstruction(this, string, string2);
    }

    public void skippedEntity(String string) throws SAXException {
        if (!this.m_shouldProcess) {
            return;
        }
        this.getCurrentProcessor().skippedEntity(this, string);
    }

    public void warn(String string, Object[] arrobject) throws SAXException {
        String string2 = XSLMessages.createWarning(string, arrobject);
        SAXSourceLocator sAXSourceLocator = this.getLocator();
        ErrorListener errorListener = this.m_stylesheetProcessor.getErrorListener();
        try {
            if (null != errorListener) {
                errorListener.warning(new TransformerException(string2, sAXSourceLocator));
            }
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    private void assertion(boolean bl, String string) throws RuntimeException {
        if (!bl) {
            throw new RuntimeException(string);
        }
    }

    protected void error(String string, Exception exception) throws SAXException {
        SAXSourceLocator sAXSourceLocator = this.getLocator();
        ErrorListener errorListener = this.m_stylesheetProcessor.getErrorListener();
        TransformerException transformerException = !(exception instanceof TransformerException) ? (null == exception ? new TransformerException(string, sAXSourceLocator) : new TransformerException(string, sAXSourceLocator, exception)) : (TransformerException)exception;
        if (null != errorListener) {
            try {
                errorListener.error(transformerException);
            }
            catch (TransformerException transformerException2) {
                throw new SAXException(transformerException2);
            }
        } else {
            throw new SAXException(transformerException);
        }
    }

    protected void error(String string, Object[] arrobject, Exception exception) throws SAXException {
        String string2 = XSLMessages.createMessage(string, arrobject);
        this.error(string2, exception);
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
        String string = sAXParseException.getMessage();
        SAXSourceLocator sAXSourceLocator = this.getLocator();
        ErrorListener errorListener = this.m_stylesheetProcessor.getErrorListener();
        try {
            errorListener.warning(new TransformerException(string, sAXSourceLocator));
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    public void error(SAXParseException sAXParseException) throws SAXException {
        String string = sAXParseException.getMessage();
        SAXSourceLocator sAXSourceLocator = this.getLocator();
        ErrorListener errorListener = this.m_stylesheetProcessor.getErrorListener();
        try {
            errorListener.error(new TransformerException(string, sAXSourceLocator));
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        String string = sAXParseException.getMessage();
        SAXSourceLocator sAXSourceLocator = this.getLocator();
        ErrorListener errorListener = this.m_stylesheetProcessor.getErrorListener();
        try {
            errorListener.fatalError(new TransformerException(string, sAXSourceLocator));
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    private void checkForFragmentID(Attributes attributes) {
        if (!this.m_shouldProcess && null != attributes && null != this.m_fragmentIDString) {
            int n2 = attributes.getLength();
            for (int i2 = 0; i2 < n2; ++i2) {
                String string;
                String string2 = attributes.getQName(i2);
                if (!string2.equals("id") || !(string = attributes.getValue(i2)).equalsIgnoreCase(this.m_fragmentIDString)) continue;
                this.m_shouldProcess = true;
                this.m_fragmentID = this.m_elementID;
            }
        }
    }

    public TransformerFactoryImpl getStylesheetProcessor() {
        return this.m_stylesheetProcessor;
    }

    int getStylesheetType() {
        return this.m_stylesheetType;
    }

    void setStylesheetType(int n2) {
        this.m_stylesheetType = n2;
    }

    Stylesheet getStylesheet() {
        return this.m_stylesheets.size() == 0 ? null : (Stylesheet)this.m_stylesheets.peek();
    }

    Stylesheet getLastPoppedStylesheet() {
        return this.m_lastPoppedStylesheet;
    }

    public StylesheetRoot getStylesheetRoot() {
        if (this.m_stylesheetRoot != null) {
            this.m_stylesheetRoot.setOptimizer(this.m_optimize);
            this.m_stylesheetRoot.setIncremental(this.m_incremental);
            this.m_stylesheetRoot.setSource_location(this.m_source_location);
        }
        return this.m_stylesheetRoot;
    }

    public void pushStylesheet(Stylesheet stylesheet) {
        if (this.m_stylesheets.size() == 0) {
            this.m_stylesheetRoot = (StylesheetRoot)stylesheet;
        }
        this.m_stylesheets.push(stylesheet);
    }

    Stylesheet popStylesheet() {
        if (!this.m_stylesheetLocatorStack.isEmpty()) {
            this.m_stylesheetLocatorStack.pop();
        }
        if (!this.m_stylesheets.isEmpty()) {
            this.m_lastPoppedStylesheet = (Stylesheet)this.m_stylesheets.pop();
        }
        return this.m_lastPoppedStylesheet;
    }

    XSLTElementProcessor getCurrentProcessor() {
        return (XSLTElementProcessor)this.m_processors.peek();
    }

    void pushProcessor(XSLTElementProcessor xSLTElementProcessor) {
        this.m_processors.push(xSLTElementProcessor);
    }

    XSLTElementProcessor popProcessor() {
        return (XSLTElementProcessor)this.m_processors.pop();
    }

    public XSLTSchema getSchema() {
        return this.m_schema;
    }

    ElemTemplateElement getElemTemplateElement() {
        try {
            return (ElemTemplateElement)this.m_elems.peek();
        }
        catch (EmptyStackException emptyStackException) {
            return null;
        }
    }

    int nextUid() {
        return this.m_docOrderCount++;
    }

    void pushElemTemplateElement(ElemTemplateElement elemTemplateElement) {
        if (elemTemplateElement.getUid() == -1) {
            elemTemplateElement.setUid(this.nextUid());
        }
        this.m_elems.push(elemTemplateElement);
    }

    ElemTemplateElement popElemTemplateElement() {
        return (ElemTemplateElement)this.m_elems.pop();
    }

    void pushBaseIndentifier(String string) {
        if (null != string) {
            int n2 = string.indexOf(35);
            if (n2 > -1) {
                this.m_fragmentIDString = string.substring(n2 + 1);
                this.m_shouldProcess = false;
            } else {
                this.m_shouldProcess = true;
            }
        } else {
            this.m_shouldProcess = true;
        }
        this.m_baseIdentifiers.push(string);
    }

    String popBaseIndentifier() {
        return (String)this.m_baseIdentifiers.pop();
    }

    public String getBaseIdentifier() {
        String string = this.m_baseIdentifiers.isEmpty() ? null : this.m_baseIdentifiers.peek();
        if (null == string) {
            SAXSourceLocator sAXSourceLocator = this.getLocator();
            string = null == sAXSourceLocator ? "" : sAXSourceLocator.getSystemId();
        }
        return string;
    }

    public SAXSourceLocator getLocator() {
        if (this.m_stylesheetLocatorStack.isEmpty()) {
            SAXSourceLocator sAXSourceLocator = new SAXSourceLocator();
            sAXSourceLocator.setSystemId(this.getStylesheetProcessor().getDOMsystemID());
            return sAXSourceLocator;
        }
        return (SAXSourceLocator)this.m_stylesheetLocatorStack.peek();
    }

    void pushImportURL(String string) {
        this.m_importStack.push(string);
    }

    void pushImportSource(Source source) {
        this.m_importSourceStack.push(source);
    }

    boolean importStackContains(String string) {
        return this.stackContains(this.m_importStack, string);
    }

    String popImportURL() {
        return (String)this.m_importStack.pop();
    }

    String peekImportURL() {
        return (String)this.m_importStack.peek();
    }

    Source peekSourceFromURIResolver() {
        return (Source)this.m_importSourceStack.peek();
    }

    Source popImportSource() {
        return (Source)this.m_importSourceStack.pop();
    }

    void pushNewNamespaceSupport() {
        this.m_nsSupportStack.push(new NamespaceSupport2());
    }

    void popNamespaceSupport() {
        this.m_nsSupportStack.pop();
    }

    NamespaceSupport getNamespaceSupport() {
        return (NamespaceSupport)this.m_nsSupportStack.peek();
    }

    public void setOriginatingNode(Node node) {
        this.m_originatingNode = node;
    }

    public Node getOriginatingNode() {
        return this.m_originatingNode;
    }

    boolean isSpacePreserve() {
        return this.m_spacePreserveStack.peek();
    }

    void popSpaceHandling() {
        this.m_spacePreserveStack.pop();
    }

    void pushSpaceHandling(boolean bl) throws SAXParseException {
        this.m_spacePreserveStack.push(bl);
    }

    void pushSpaceHandling(Attributes attributes) throws SAXParseException {
        String string = attributes.getValue("xml:space");
        if (null == string) {
            this.m_spacePreserveStack.push(this.m_spacePreserveStack.peekOrFalse());
        } else if (string.equals("preserve")) {
            this.m_spacePreserveStack.push(true);
        } else if (string.equals("default")) {
            this.m_spacePreserveStack.push(false);
        } else {
            SAXSourceLocator sAXSourceLocator = this.getLocator();
            ErrorListener errorListener = this.m_stylesheetProcessor.getErrorListener();
            try {
                errorListener.error(new TransformerException(XSLMessages.createMessage("ER_ILLEGAL_XMLSPACE_VALUE", null), sAXSourceLocator));
            }
            catch (TransformerException transformerException) {
                throw new SAXParseException(transformerException.getMessage(), sAXSourceLocator, transformerException);
            }
            this.m_spacePreserveStack.push(this.m_spacePreserveStack.peek());
        }
    }

    private double getElemVersion() {
        double d2 = -1.0;
        for (ElemTemplateElement elemTemplateElement = this.getElemTemplateElement(); (d2 == -1.0 || d2 == 1.0) && elemTemplateElement != null; elemTemplateElement = elemTemplateElement.getParentElem()) {
            try {
                d2 = Double.valueOf(elemTemplateElement.getXmlVersion());
                continue;
            }
            catch (Exception exception) {
                d2 = -1.0;
            }
        }
        return d2 == -1.0 ? 1.0 : d2;
    }

    public boolean handlesNullPrefixes() {
        return false;
    }

    public boolean getOptimize() {
        return this.m_optimize;
    }

    public boolean getIncremental() {
        return this.m_incremental;
    }

    public boolean getSource_location() {
        return this.m_source_location;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

