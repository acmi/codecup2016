/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.extensions.ExtensionsTable;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemAttributeSet;
import org.apache.xalan.templates.ElemForEach;
import org.apache.xalan.templates.ElemSort;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.WhiteSpaceInfo;
import org.apache.xalan.templates.XUnresolvedVariable;
import org.apache.xalan.trace.GenerateEvent;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.ClonerToResultTree;
import org.apache.xalan.transformer.CountersTable;
import org.apache.xalan.transformer.KeyManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.NodeSortKey;
import org.apache.xalan.transformer.StackGuard;
import org.apache.xalan.transformer.TransformSnapshot;
import org.apache.xalan.transformer.TransformSnapshotImpl;
import org.apache.xalan.transformer.TransformState;
import org.apache.xalan.transformer.TransformerClient;
import org.apache.xalan.transformer.TransformerHandlerImpl;
import org.apache.xalan.transformer.XalanTransformState;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.ToSAXHandler;
import org.apache.xml.serializer.ToTextStream;
import org.apache.xml.serializer.ToXMLSAXHandler;
import org.apache.xml.serializer.TransformStateSetter;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.DOMBuilder;
import org.apache.xml.utils.DOMHelper;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.ObjectPool;
import org.apache.xml.utils.ObjectStack;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.ThreadControllerWrapper;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Arg;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.SourceTreeManager;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.SelfIteratorNoPredicate;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class TransformerImpl
extends Transformer
implements Runnable,
DTMWSFilter,
SerializerTrace,
ExtensionsProvider {
    private Boolean m_reentryGuard = new Boolean(true);
    private FileOutputStream m_outputStream = null;
    private boolean m_parserEventsOnMain = true;
    private Thread m_transformThread;
    private String m_urlOfSource = null;
    private Result m_outputTarget = null;
    private OutputProperties m_outputFormat;
    ContentHandler m_inputContentHandler;
    private ContentHandler m_outputContentHandler = null;
    DocumentBuilder m_docBuilder = null;
    private ObjectPool m_textResultHandlerObjectPool;
    private ObjectPool m_stringWriterObjectPool;
    private OutputProperties m_textformat;
    ObjectStack m_currentTemplateElements;
    Stack m_currentMatchTemplates;
    NodeVector m_currentMatchedNodes;
    private StylesheetRoot m_stylesheetRoot;
    private boolean m_quietConflictWarnings;
    private XPathContext m_xcontext;
    private StackGuard m_stackGuard;
    private SerializationHandler m_serializationHandler;
    private KeyManager m_keyManager;
    Stack m_attrSetStack;
    CountersTable m_countersTable;
    BoolStack m_currentTemplateRuleIsNull;
    ObjectStack m_currentFuncResult;
    private MsgMgr m_msgMgr;
    private boolean m_optimizer;
    private boolean m_incremental;
    private boolean m_source_location;
    private boolean m_debug;
    private ErrorListener m_errorHandler;
    private TraceManager m_traceManager;
    private Exception m_exceptionThrown;
    private Source m_xmlSource;
    private int m_doc;
    private boolean m_isTransformDone;
    private boolean m_hasBeenReset;
    private boolean m_shouldReset;
    private Stack m_modes;
    private ExtensionsTable m_extensionsTable;
    private boolean m_hasTransformThreadErrorCatcher;
    Vector m_userParams;
    static Class class$org$apache$xml$serializer$ToTextStream;
    static Class class$java$io$StringWriter;

    public void setShouldReset(boolean bl) {
        this.m_shouldReset = bl;
    }

    public TransformerImpl(StylesheetRoot stylesheetRoot) {
        Class class_ = class$org$apache$xml$serializer$ToTextStream == null ? (TransformerImpl.class$org$apache$xml$serializer$ToTextStream = TransformerImpl.class$("org.apache.xml.serializer.ToTextStream")) : class$org$apache$xml$serializer$ToTextStream;
        this.m_textResultHandlerObjectPool = new ObjectPool(class_);
        Class class_2 = class$java$io$StringWriter == null ? (TransformerImpl.class$java$io$StringWriter = TransformerImpl.class$("java.io.StringWriter")) : class$java$io$StringWriter;
        this.m_stringWriterObjectPool = new ObjectPool(class_2);
        this.m_textformat = new OutputProperties("text");
        this.m_currentTemplateElements = new ObjectStack(4096);
        this.m_currentMatchTemplates = new Stack();
        this.m_currentMatchedNodes = new NodeVector();
        this.m_stylesheetRoot = null;
        this.m_quietConflictWarnings = true;
        this.m_keyManager = new KeyManager();
        this.m_attrSetStack = null;
        this.m_countersTable = null;
        this.m_currentTemplateRuleIsNull = new BoolStack();
        this.m_currentFuncResult = new ObjectStack();
        this.m_optimizer = true;
        this.m_incremental = false;
        this.m_source_location = false;
        this.m_debug = false;
        this.m_errorHandler = new DefaultErrorHandler(false);
        this.m_traceManager = new TraceManager(this);
        this.m_exceptionThrown = null;
        this.m_isTransformDone = false;
        this.m_hasBeenReset = false;
        this.m_shouldReset = true;
        this.m_modes = new Stack();
        this.m_extensionsTable = null;
        this.m_hasTransformThreadErrorCatcher = false;
        this.m_optimizer = stylesheetRoot.getOptimizer();
        this.m_incremental = stylesheetRoot.getIncremental();
        this.m_source_location = stylesheetRoot.getSource_location();
        this.setStylesheet(stylesheetRoot);
        XPathContext xPathContext = new XPathContext(this);
        xPathContext.setIncremental(this.m_incremental);
        xPathContext.getDTMManager().setIncremental(this.m_incremental);
        xPathContext.setSource_location(this.m_source_location);
        xPathContext.getDTMManager().setSource_location(this.m_source_location);
        if (stylesheetRoot.isSecureProcessing()) {
            xPathContext.setSecureProcessing(true);
        }
        this.setXPathContext(xPathContext);
        this.getXPathContext().setNamespaceContext(stylesheetRoot);
        this.m_stackGuard = new StackGuard(this);
    }

    public ExtensionsTable getExtensionsTable() {
        return this.m_extensionsTable;
    }

    void setExtensionsTable(StylesheetRoot stylesheetRoot) throws TransformerException {
        try {
            if (stylesheetRoot.getExtensions() != null && !stylesheetRoot.isSecureProcessing()) {
                this.m_extensionsTable = new ExtensionsTable(stylesheetRoot);
            }
        }
        catch (TransformerException transformerException) {
            transformerException.printStackTrace();
        }
    }

    public boolean functionAvailable(String string, String string2) throws TransformerException {
        return this.getExtensionsTable().functionAvailable(string, string2);
    }

    public boolean elementAvailable(String string, String string2) throws TransformerException {
        return this.getExtensionsTable().elementAvailable(string, string2);
    }

    public Object extFunction(String string, String string2, Vector vector, Object object) throws TransformerException {
        return this.getExtensionsTable().extFunction(string, string2, vector, object, this.getXPathContext().getExpressionContext());
    }

    public Object extFunction(FuncExtFunction funcExtFunction, Vector vector) throws TransformerException {
        return this.getExtensionsTable().extFunction(funcExtFunction, vector, this.getXPathContext().getExpressionContext());
    }

    public void reset() {
        if (!this.m_hasBeenReset && this.m_shouldReset) {
            this.m_hasBeenReset = true;
            if (this.m_outputStream != null) {
                try {
                    this.m_outputStream.close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
            this.m_outputStream = null;
            this.m_countersTable = null;
            this.m_xcontext.reset();
            this.m_xcontext.getVarStack().reset();
            this.resetUserParameters();
            this.m_currentTemplateElements.removeAllElements();
            this.m_currentMatchTemplates.removeAllElements();
            this.m_currentMatchedNodes.removeAllElements();
            this.m_serializationHandler = null;
            this.m_outputTarget = null;
            this.m_keyManager = new KeyManager();
            this.m_attrSetStack = null;
            this.m_countersTable = null;
            this.m_currentTemplateRuleIsNull = new BoolStack();
            this.m_xmlSource = null;
            this.m_doc = -1;
            this.m_isTransformDone = false;
            this.m_transformThread = null;
            this.m_xcontext.getSourceTreeManager().reset();
        }
    }

    public boolean getProperty(String string) {
        return false;
    }

    public void setProperty(String string, Object object) {
    }

    public boolean isParserEventsOnMain() {
        return this.m_parserEventsOnMain;
    }

    public Thread getTransformThread() {
        return this.m_transformThread;
    }

    public void setTransformThread(Thread thread) {
        this.m_transformThread = thread;
    }

    public boolean hasTransformThreadErrorCatcher() {
        return this.m_hasTransformThreadErrorCatcher;
    }

    public void transform(Source source) throws TransformerException {
        this.transform(source, true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void transform(Source source, boolean bl) throws TransformerException {
        try {
            String string;
            Object object;
            Object object2;
            Object object3;
            if (this.getXPathContext().getNamespaceContext() == null) {
                this.getXPathContext().setNamespaceContext(this.getStylesheet());
            }
            if (null == (string = source.getSystemId())) {
                string = this.m_stylesheetRoot.getBaseIdentifier();
            }
            if (null == string) {
                object3 = "";
                try {
                    object3 = System.getProperty("user.dir");
                }
                catch (SecurityException securityException) {
                    // empty catch block
                }
                string = object3.startsWith(File.separator) ? "file://" + (String)object3 : "file:///" + (String)object3;
                string = string + File.separatorChar + source.getClass().getName();
            }
            this.setBaseURLOfSource(string);
            object3 = this.m_xcontext.getDTMManager();
            if (source instanceof StreamSource && source.getSystemId() == null && ((StreamSource)source).getInputStream() == null && ((StreamSource)source).getReader() == null || source instanceof SAXSource && ((SAXSource)source).getInputSource() == null && ((SAXSource)source).getXMLReader() == null || source instanceof DOMSource && ((DOMSource)source).getNode() == null) {
                try {
                    object2 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = object2.newDocumentBuilder();
                    object = source.getSystemId();
                    source = new DOMSource(documentBuilder.newDocument());
                    if (object != null) {
                        source.setSystemId((String)object);
                    }
                }
                catch (ParserConfigurationException parserConfigurationException) {
                    this.fatalError(parserConfigurationException);
                }
            }
            object2 = object3.getDTM(source, false, this, true, true);
            object2.setDocumentBaseURI(string);
            boolean bl2 = true;
            try {
                this.transformNode(object2.getDocument());
            }
            finally {
                if (bl) {
                    object3.release((DTM)object2, bl2);
                }
            }
            object = this.getExceptionThrown();
            if (null != object) {
                if (object instanceof TransformerException) {
                    throw (TransformerException)object;
                }
                if (!(object instanceof WrappedRuntimeException)) throw new TransformerException((Throwable)object);
                this.fatalError(((WrappedRuntimeException)object).getException());
                return;
            }
            if (null == this.m_serializationHandler) return;
            this.m_serializationHandler.endDocument();
        }
        catch (WrappedRuntimeException wrappedRuntimeException) {
            Exception exception = wrappedRuntimeException.getException();
            while (exception instanceof WrappedRuntimeException) {
                exception = ((WrappedRuntimeException)exception).getException();
            }
            this.fatalError(exception);
        }
        catch (SAXParseException sAXParseException) {
            this.fatalError(sAXParseException);
        }
        catch (SAXException sAXException) {
            this.m_errorHandler.fatalError(new TransformerException(sAXException));
        }
        finally {
            this.m_hasTransformThreadErrorCatcher = false;
            this.reset();
        }
    }

    private void fatalError(Throwable throwable) throws TransformerException {
        if (throwable instanceof SAXParseException) {
            this.m_errorHandler.fatalError(new TransformerException(throwable.getMessage(), new SAXSourceLocator((SAXParseException)throwable)));
        } else {
            this.m_errorHandler.fatalError(new TransformerException(throwable));
        }
    }

    public String getBaseURLOfSource() {
        return this.m_urlOfSource;
    }

    public void setBaseURLOfSource(String string) {
        this.m_urlOfSource = string;
    }

    public Result getOutputTarget() {
        return this.m_outputTarget;
    }

    public void setOutputTarget(Result result) {
        this.m_outputTarget = result;
    }

    public String getOutputProperty(String string) throws IllegalArgumentException {
        String string2 = null;
        OutputProperties outputProperties = this.getOutputFormat();
        string2 = outputProperties.getProperty(string);
        if (null == string2 && !OutputProperties.isLegalPropertyKey(string)) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{string}));
        }
        return string2;
    }

    public String getOutputPropertyNoDefault(String string) throws IllegalArgumentException {
        String string2 = null;
        OutputProperties outputProperties = this.getOutputFormat();
        string2 = (String)outputProperties.getProperties().get(string);
        if (null == string2 && !OutputProperties.isLegalPropertyKey(string)) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{string}));
        }
        return string2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setOutputProperty(String string, String string2) throws IllegalArgumentException {
        Boolean bl = this.m_reentryGuard;
        synchronized (bl) {
            if (null == this.m_outputFormat) {
                this.m_outputFormat = (OutputProperties)this.getStylesheet().getOutputComposed().clone();
            }
            if (!OutputProperties.isLegalPropertyKey(string)) {
                throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{string}));
            }
            this.m_outputFormat.setProperty(string, string2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setOutputProperties(Properties properties) throws IllegalArgumentException {
        Boolean bl = this.m_reentryGuard;
        synchronized (bl) {
            if (null != properties) {
                String string = (String)properties.get("method");
                if (null != string) {
                    this.m_outputFormat = new OutputProperties(string);
                } else if (this.m_outputFormat == null) {
                    this.m_outputFormat = new OutputProperties();
                }
                this.m_outputFormat.copyFrom(properties);
                this.m_outputFormat.copyFrom(this.m_stylesheetRoot.getOutputProperties());
            } else {
                this.m_outputFormat = null;
            }
        }
    }

    public Properties getOutputProperties() {
        return (Properties)this.getOutputFormat().getProperties().clone();
    }

    public SerializationHandler createSerializationHandler(Result result) throws TransformerException {
        SerializationHandler serializationHandler = this.createSerializationHandler(result, this.getOutputFormat());
        return serializationHandler;
    }

    public SerializationHandler createSerializationHandler(Result result, OutputProperties outputProperties) throws TransformerException {
        SerializationHandler serializationHandler;
        Object object;
        Node node = null;
        if (result instanceof DOMResult) {
            Document document;
            short s2;
            DOMBuilder dOMBuilder;
            node = ((DOMResult)result).getNode();
            object = ((DOMResult)result).getNextSibling();
            if (null != node) {
                s2 = node.getNodeType();
                document = 9 == s2 ? (Document)node : node.getOwnerDocument();
            } else {
                boolean bl = this.m_stylesheetRoot.isSecureProcessing();
                document = DOMHelper.createDocument(bl);
                node = document;
                s2 = node.getNodeType();
                ((DOMResult)result).setNode(node);
            }
            DOMBuilder dOMBuilder2 = dOMBuilder = 11 == s2 ? new DOMBuilder(document, (DocumentFragment)node) : new DOMBuilder(document, node);
            if (object != null) {
                dOMBuilder.setNextSibling((Node)object);
            }
            String string = outputProperties.getProperty("encoding");
            serializationHandler = new ToXMLSAXHandler(dOMBuilder, dOMBuilder, string);
        } else if (result instanceof SAXResult) {
            object = ((SAXResult)result).getHandler();
            if (null == object) {
                throw new IllegalArgumentException("handler can not be null for a SAXResult");
            }
            LexicalHandler lexicalHandler = object instanceof LexicalHandler ? (LexicalHandler)object : null;
            String string = outputProperties.getProperty("encoding");
            String string2 = outputProperties.getProperty("method");
            ToXMLSAXHandler toXMLSAXHandler = new ToXMLSAXHandler((ContentHandler)object, lexicalHandler, string);
            toXMLSAXHandler.setShouldOutputNSAttr(false);
            serializationHandler = toXMLSAXHandler;
            String string3 = outputProperties.getProperty("doctype-public");
            String string4 = outputProperties.getProperty("doctype-system");
            if (string4 != null) {
                serializationHandler.setDoctypeSystem(string4);
            }
            if (string3 != null) {
                serializationHandler.setDoctypePublic(string3);
            }
            if (object instanceof TransformerClient) {
                XalanTransformState xalanTransformState = new XalanTransformState();
                ((TransformerClient)object).setTransformState(xalanTransformState);
                ((ToSAXHandler)serializationHandler).setTransformState(xalanTransformState);
            }
        } else if (result instanceof StreamResult) {
            object = (StreamResult)result;
            try {
                SerializationHandler serializationHandler2 = (SerializationHandler)SerializerFactory.getSerializer(outputProperties.getProperties());
                if (null != object.getWriter()) {
                    serializationHandler2.setWriter(object.getWriter());
                } else if (null != object.getOutputStream()) {
                    serializationHandler2.setOutputStream(object.getOutputStream());
                } else if (null != object.getSystemId()) {
                    String string = object.getSystemId();
                    if (string.startsWith("file:///")) {
                        string = string.substring(8).indexOf(":") > 0 ? string.substring(8) : string.substring(7);
                    } else if (string.startsWith("file:/")) {
                        string = string.substring(6).indexOf(":") > 0 ? string.substring(6) : string.substring(5);
                    }
                    this.m_outputStream = new FileOutputStream(string);
                    serializationHandler2.setOutputStream(this.m_outputStream);
                    serializationHandler = serializationHandler2;
                } else {
                    throw new TransformerException(XSLMessages.createMessage("ER_NO_OUTPUT_SPECIFIED", null));
                }
                serializationHandler = serializationHandler2;
            }
            catch (IOException iOException) {
                throw new TransformerException(iOException);
            }
        } else {
            throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_TRANSFORM_TO_RESULT_TYPE", new Object[]{result.getClass().getName()}));
        }
        serializationHandler.setTransformer(this);
        object = this.getStylesheet();
        serializationHandler.setSourceLocator((SourceLocator)object);
        return serializationHandler;
    }

    public void transform(Source source, Result result) throws TransformerException {
        this.transform(source, result, true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void transform(Source source, Result result, boolean bl) throws TransformerException {
        Boolean bl2 = this.m_reentryGuard;
        synchronized (bl2) {
            SerializationHandler serializationHandler = this.createSerializationHandler(result);
            this.setSerializationHandler(serializationHandler);
            this.m_outputTarget = result;
            this.transform(source, bl);
        }
    }

    public void transformNode(int n2, Result result) throws TransformerException {
        SerializationHandler serializationHandler = this.createSerializationHandler(result);
        this.setSerializationHandler(serializationHandler);
        this.m_outputTarget = result;
        this.transformNode(n2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void transformNode(int n2) throws TransformerException {
        this.setExtensionsTable(this.getStylesheet());
        SerializationHandler serializationHandler = this.m_serializationHandler;
        synchronized (serializationHandler) {
            block25 : {
                this.m_hasBeenReset = false;
                XPathContext xPathContext = this.getXPathContext();
                DTM dTM = xPathContext.getDTM(n2);
                try {
                    this.pushGlobalVars(n2);
                    StylesheetRoot stylesheetRoot = this.getStylesheet();
                    int n3 = stylesheetRoot.getGlobalImportCount();
                    for (int i2 = 0; i2 < n3; ++i2) {
                        StylesheetComposed stylesheetComposed = stylesheetRoot.getGlobalImport(i2);
                        int n4 = stylesheetComposed.getIncludeCountComposed();
                        for (int i3 = -1; i3 < n4; ++i3) {
                            Stylesheet stylesheet = stylesheetComposed.getIncludeComposed(i3);
                            stylesheet.runtimeInit(this);
                            for (ElemTemplateElement elemTemplateElement = stylesheet.getFirstChildElem(); elemTemplateElement != null; elemTemplateElement = elemTemplateElement.getNextSiblingElem()) {
                                elemTemplateElement.runtimeInit(this);
                            }
                        }
                    }
                    SelfIteratorNoPredicate selfIteratorNoPredicate = new SelfIteratorNoPredicate();
                    selfIteratorNoPredicate.setRoot(n2, xPathContext);
                    xPathContext.pushContextNodeList(selfIteratorNoPredicate);
                    try {
                        this.applyTemplateToNode(null, null, n2);
                    }
                    finally {
                        xPathContext.popContextNodeList();
                    }
                    if (null != this.m_serializationHandler) {
                        this.m_serializationHandler.endDocument();
                    }
                }
                catch (Exception exception) {
                    Exception exception2;
                    Exception exception3;
                    while (exception2 instanceof WrappedRuntimeException) {
                        exception3 = ((WrappedRuntimeException)exception2).getException();
                        if (null == exception3) continue;
                        exception2 = exception3;
                    }
                    if (null != this.m_serializationHandler) {
                        try {
                            if (exception2 instanceof SAXParseException) {
                                this.m_serializationHandler.fatalError((SAXParseException)exception2);
                            } else if (exception2 instanceof TransformerException) {
                                exception3 = (TransformerException)exception2;
                                SAXSourceLocator sAXSourceLocator = new SAXSourceLocator(exception3.getLocator());
                                this.m_serializationHandler.fatalError(new SAXParseException(exception3.getMessage(), sAXSourceLocator, exception3));
                            } else {
                                this.m_serializationHandler.fatalError(new SAXParseException(exception2.getMessage(), new SAXSourceLocator(), exception2));
                            }
                        }
                        catch (Exception exception4) {
                            // empty catch block
                        }
                    }
                    if (exception2 instanceof TransformerException) {
                        this.m_errorHandler.fatalError((TransformerException)exception2);
                        break block25;
                    }
                    if (exception2 instanceof SAXParseException) {
                        this.m_errorHandler.fatalError(new TransformerException(exception2.getMessage(), new SAXSourceLocator((SAXParseException)exception2), exception2));
                        break block25;
                    }
                    this.m_errorHandler.fatalError(new TransformerException(exception2));
                }
                finally {
                    this.reset();
                }
            }
        }
    }

    public ContentHandler getInputContentHandler() {
        return this.getInputContentHandler(false);
    }

    public ContentHandler getInputContentHandler(boolean bl) {
        if (null == this.m_inputContentHandler) {
            this.m_inputContentHandler = new TransformerHandlerImpl(this, bl, this.m_urlOfSource);
        }
        return this.m_inputContentHandler;
    }

    public DeclHandler getInputDeclHandler() {
        if (this.m_inputContentHandler instanceof DeclHandler) {
            return (DeclHandler)((Object)this.m_inputContentHandler);
        }
        return null;
    }

    public LexicalHandler getInputLexicalHandler() {
        if (this.m_inputContentHandler instanceof LexicalHandler) {
            return (LexicalHandler)((Object)this.m_inputContentHandler);
        }
        return null;
    }

    public void setOutputFormat(OutputProperties outputProperties) {
        this.m_outputFormat = outputProperties;
    }

    public OutputProperties getOutputFormat() {
        OutputProperties outputProperties = null == this.m_outputFormat ? this.getStylesheet().getOutputComposed() : this.m_outputFormat;
        return outputProperties;
    }

    public void setParameter(String string, String string2, Object object) {
        VariableStack variableStack = this.getXPathContext().getVarStack();
        QName qName = new QName(string2, string);
        XObject xObject = XObject.create(object, this.getXPathContext());
        StylesheetRoot stylesheetRoot = this.m_stylesheetRoot;
        Vector vector = stylesheetRoot.getVariablesAndParamsComposed();
        int n2 = vector.size();
        while (--n2 >= 0) {
            ElemVariable elemVariable = (ElemVariable)vector.elementAt(n2);
            if (elemVariable.getXSLToken() != 41 || !elemVariable.getName().equals(qName)) continue;
            variableStack.setGlobalVariable(n2, xObject);
        }
    }

    public void setParameter(String string, Object object) {
        if (object == null) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_SET_PARAM_VALUE", new Object[]{string}));
        }
        StringTokenizer stringTokenizer = new StringTokenizer(string, "{}", false);
        try {
            String string2;
            String string3 = stringTokenizer.nextToken();
            String string4 = string2 = stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : null;
            if (null == this.m_userParams) {
                this.m_userParams = new Vector();
            }
            if (null == string2) {
                this.replaceOrPushUserParam(new QName(string3), XObject.create(object, this.getXPathContext()));
                this.setParameter(string3, null, object);
            } else {
                this.replaceOrPushUserParam(new QName(string3, string2), XObject.create(object, this.getXPathContext()));
                this.setParameter(string2, string3, object);
            }
        }
        catch (NoSuchElementException noSuchElementException) {
            // empty catch block
        }
    }

    private void replaceOrPushUserParam(QName qName, XObject xObject) {
        int n2 = this.m_userParams.size();
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            Arg arg = (Arg)this.m_userParams.elementAt(i2);
            if (!arg.getQName().equals(qName)) continue;
            this.m_userParams.setElementAt(new Arg(qName, xObject, true), i2);
            return;
        }
        this.m_userParams.addElement(new Arg(qName, xObject, true));
    }

    public Object getParameter(String string) {
        try {
            QName qName = QName.getQNameFromString(string);
            if (null == this.m_userParams) {
                return null;
            }
            int n2 = this.m_userParams.size();
            for (int i2 = n2 - 1; i2 >= 0; --i2) {
                Arg arg = (Arg)this.m_userParams.elementAt(i2);
                if (!arg.getQName().equals(qName)) continue;
                return arg.getVal().object();
            }
            return null;
        }
        catch (NoSuchElementException noSuchElementException) {
            return null;
        }
    }

    private void resetUserParameters() {
        try {
            if (null == this.m_userParams) {
                return;
            }
            int n2 = this.m_userParams.size();
            for (int i2 = n2 - 1; i2 >= 0; --i2) {
                Arg arg = (Arg)this.m_userParams.elementAt(i2);
                QName qName = arg.getQName();
                String string = qName.getNamespace();
                String string2 = qName.getLocalPart();
                this.setParameter(string2, string, arg.getVal().object());
            }
        }
        catch (NoSuchElementException noSuchElementException) {
            // empty catch block
        }
    }

    public void setParameters(Properties properties) {
        this.clearParameters();
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String string = properties.getProperty((String)enumeration.nextElement());
            StringTokenizer stringTokenizer = new StringTokenizer(string, "{}", false);
            try {
                String string2;
                String string3 = stringTokenizer.nextToken();
                String string4 = string2 = stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : null;
                if (null == string2) {
                    this.setParameter(string3, null, properties.getProperty(string));
                    continue;
                }
                this.setParameter(string2, string3, properties.getProperty(string));
            }
            catch (NoSuchElementException noSuchElementException) {}
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearParameters() {
        Boolean bl = this.m_reentryGuard;
        synchronized (bl) {
            VariableStack variableStack = new VariableStack();
            this.m_xcontext.setVarStack(variableStack);
            this.m_userParams = null;
        }
    }

    protected void pushGlobalVars(int n2) throws TransformerException {
        XPathContext xPathContext = this.m_xcontext;
        VariableStack variableStack = xPathContext.getVarStack();
        StylesheetRoot stylesheetRoot = this.getStylesheet();
        Vector vector = stylesheetRoot.getVariablesAndParamsComposed();
        int n3 = vector.size();
        variableStack.link(n3);
        while (--n3 >= 0) {
            ElemVariable elemVariable = (ElemVariable)vector.elementAt(n3);
            XUnresolvedVariable xUnresolvedVariable = new XUnresolvedVariable(elemVariable, n2, this, variableStack.getStackFrame(), 0, true);
            if (null != variableStack.elementAt(n3)) continue;
            variableStack.setGlobalVariable(n3, xUnresolvedVariable);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setURIResolver(URIResolver uRIResolver) {
        Boolean bl = this.m_reentryGuard;
        synchronized (bl) {
            this.m_xcontext.getSourceTreeManager().setURIResolver(uRIResolver);
        }
    }

    public URIResolver getURIResolver() {
        return this.m_xcontext.getSourceTreeManager().getURIResolver();
    }

    public void setContentHandler(ContentHandler contentHandler) {
        if (contentHandler == null) {
            throw new NullPointerException(XSLMessages.createMessage("ER_NULL_CONTENT_HANDLER", null));
        }
        this.m_outputContentHandler = contentHandler;
        if (null == this.m_serializationHandler) {
            ToXMLSAXHandler toXMLSAXHandler = new ToXMLSAXHandler();
            toXMLSAXHandler.setContentHandler(contentHandler);
            toXMLSAXHandler.setTransformer(this);
            this.m_serializationHandler = toXMLSAXHandler;
        } else {
            this.m_serializationHandler.setContentHandler(contentHandler);
        }
    }

    public ContentHandler getContentHandler() {
        return this.m_outputContentHandler;
    }

    public int transformToRTF(ElemTemplateElement elemTemplateElement) throws TransformerException {
        DTM dTM = this.m_xcontext.getRTFDTM();
        return this.transformToRTF(elemTemplateElement, dTM);
    }

    public int transformToGlobalRTF(ElemTemplateElement elemTemplateElement) throws TransformerException {
        DTM dTM = this.m_xcontext.getGlobalRTFDTM();
        return this.transformToRTF(elemTemplateElement, dTM);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int transformToRTF(ElemTemplateElement elemTemplateElement, DTM dTM) throws TransformerException {
        int n2;
        XPathContext xPathContext = this.m_xcontext;
        ContentHandler contentHandler = dTM.getContentHandler();
        SerializationHandler serializationHandler = this.m_serializationHandler;
        ToXMLSAXHandler toXMLSAXHandler = new ToXMLSAXHandler();
        toXMLSAXHandler.setContentHandler(contentHandler);
        toXMLSAXHandler.setTransformer(this);
        SerializationHandler serializationHandler2 = this.m_serializationHandler = toXMLSAXHandler;
        try {
            serializationHandler2.startDocument();
            serializationHandler2.flushPending();
            try {
                this.executeChildTemplates(elemTemplateElement, true);
                serializationHandler2.flushPending();
                n2 = dTM.getDocument();
            }
            finally {
                serializationHandler2.endDocument();
            }
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
        finally {
            this.m_serializationHandler = serializationHandler;
        }
        return n2;
    }

    public ObjectPool getStringWriterPool() {
        return this.m_stringWriterObjectPool;
    }

    public String transformToString(ElemTemplateElement elemTemplateElement) throws TransformerException {
        Object object;
        ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getFirstChildElem();
        if (null == elemTemplateElement2) {
            return "";
        }
        if (elemTemplateElement.hasTextLitOnly() && this.m_optimizer) {
            return ((ElemTextLiteral)elemTemplateElement2).getNodeValue();
        }
        SerializationHandler serializationHandler = this.m_serializationHandler;
        StringWriter stringWriter = (StringWriter)this.m_stringWriterObjectPool.getInstance();
        this.m_serializationHandler = (ToTextStream)this.m_textResultHandlerObjectPool.getInstance();
        if (null == this.m_serializationHandler) {
            object = SerializerFactory.getSerializer(this.m_textformat.getProperties());
            this.m_serializationHandler = (SerializationHandler)object;
        }
        this.m_serializationHandler.setTransformer(this);
        this.m_serializationHandler.setWriter(stringWriter);
        try {
            this.executeChildTemplates(elemTemplateElement, true);
            this.m_serializationHandler.endDocument();
            object = stringWriter.toString();
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
        finally {
            stringWriter.getBuffer().setLength(0);
            try {
                stringWriter.close();
            }
            catch (Exception exception) {}
            this.m_stringWriterObjectPool.freeInstance(stringWriter);
            this.m_serializationHandler.reset();
            this.m_textResultHandlerObjectPool.freeInstance(this.m_serializationHandler);
            this.m_serializationHandler = serializationHandler;
        }
        return object;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean applyTemplateToNode(ElemTemplateElement elemTemplateElement, ElemTemplate elemTemplate, int n2) throws TransformerException {
        DTM dTM = this.m_xcontext.getDTM(n2);
        short s2 = dTM.getNodeType(n2);
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = elemTemplateElement == null ? false : (bl2 = elemTemplateElement.getXSLToken() == 72);
        if (null == elemTemplate || bl2) {
            int n3;
            int n4 = 0;
            if (bl2) {
                n3 = elemTemplate.getStylesheetComposed().getImportCountComposed() - 1;
                n4 = elemTemplate.getStylesheetComposed().getEndImportCountComposed();
            } else {
                n3 = -1;
            }
            if (bl2 && n3 == -1) {
                elemTemplate = null;
            } else {
                XPathContext xPathContext = this.m_xcontext;
                try {
                    xPathContext.pushNamespaceContext(elemTemplateElement);
                    QName qName = this.getMode();
                    elemTemplate = bl2 ? this.m_stylesheetRoot.getTemplateComposed(xPathContext, n2, qName, n3, n4, this.m_quietConflictWarnings, dTM) : this.m_stylesheetRoot.getTemplateComposed(xPathContext, n2, qName, this.m_quietConflictWarnings, dTM);
                }
                finally {
                    xPathContext.popNamespaceContext();
                }
            }
            if (null == elemTemplate) {
                switch (s2) {
                    case 1: 
                    case 11: {
                        elemTemplate = this.m_stylesheetRoot.getDefaultRule();
                        break;
                    }
                    case 2: 
                    case 3: 
                    case 4: {
                        elemTemplate = this.m_stylesheetRoot.getDefaultTextRule();
                        bl = true;
                        break;
                    }
                    case 9: {
                        elemTemplate = this.m_stylesheetRoot.getDefaultRootRule();
                        break;
                    }
                    default: {
                        return false;
                    }
                }
            }
        }
        try {
            this.pushElemTemplateElement(elemTemplate);
            this.m_xcontext.pushCurrentNode(n2);
            this.pushPairCurrentMatched(elemTemplate, n2);
            if (!bl2) {
                NodeSetDTM nodeSetDTM = new NodeSetDTM(n2, this.m_xcontext.getDTMManager());
                this.m_xcontext.pushContextNodeList(nodeSetDTM);
            }
            if (bl) {
                switch (s2) {
                    case 3: 
                    case 4: {
                        ClonerToResultTree.cloneToResultTree(n2, s2, dTM, this.getResultTreeHandler(), false);
                        break;
                    }
                    case 2: {
                        dTM.dispatchCharactersEvents(n2, this.getResultTreeHandler(), false);
                    }
                }
            } else {
                if (this.m_debug) {
                    this.getTraceManager().fireTraceEvent(elemTemplate);
                }
                this.m_xcontext.setSAXLocator(elemTemplate);
                this.m_xcontext.getVarStack().link(elemTemplate.m_frameSize);
                this.executeChildTemplates((ElemTemplateElement)elemTemplate, true);
                if (this.m_debug) {
                    this.getTraceManager().fireTraceEndEvent(elemTemplate);
                }
            }
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
        finally {
            if (!bl) {
                this.m_xcontext.getVarStack().unlink();
            }
            this.m_xcontext.popCurrentNode();
            if (!bl2) {
                this.m_xcontext.popContextNodeList();
            }
            this.popCurrentMatched();
            this.popElemTemplateElement();
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void executeChildTemplates(ElemTemplateElement elemTemplateElement, Node node, QName qName, ContentHandler contentHandler) throws TransformerException {
        XPathContext xPathContext = this.m_xcontext;
        try {
            if (null != qName) {
                this.pushMode(qName);
            }
            xPathContext.pushCurrentNode(xPathContext.getDTMHandleFromNode(node));
            this.executeChildTemplates(elemTemplateElement, contentHandler);
        }
        finally {
            xPathContext.popCurrentNode();
            if (null != qName) {
                this.popMode();
            }
        }
    }

    public void executeChildTemplates(ElemTemplateElement elemTemplateElement, boolean bl) throws TransformerException {
        ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getFirstChildElem();
        if (null == elemTemplateElement2) {
            return;
        }
        if (elemTemplateElement.hasTextLitOnly() && this.m_optimizer) {
            char[] arrc = ((ElemTextLiteral)elemTemplateElement2).getChars();
            try {
                this.pushElemTemplateElement(elemTemplateElement2);
                this.m_serializationHandler.characters(arrc, 0, arrc.length);
            }
            catch (SAXException sAXException) {
                throw new TransformerException(sAXException);
            }
            finally {
                this.popElemTemplateElement();
            }
            return;
        }
        XPathContext xPathContext = this.m_xcontext;
        xPathContext.pushSAXLocatorNull();
        int n2 = this.m_currentTemplateElements.size();
        this.m_currentTemplateElements.push(null);
        try {
            while (elemTemplateElement2 != null) {
                if (bl || elemTemplateElement2.getXSLToken() != 48) {
                    xPathContext.setSAXLocator(elemTemplateElement2);
                    this.m_currentTemplateElements.setElementAt(elemTemplateElement2, n2);
                    elemTemplateElement2.execute(this);
                }
                elemTemplateElement2 = elemTemplateElement2.getNextSiblingElem();
            }
        }
        catch (RuntimeException runtimeException) {
            TransformerException transformerException = new TransformerException(runtimeException);
            transformerException.setLocator(elemTemplateElement2);
            throw transformerException;
        }
        finally {
            this.m_currentTemplateElements.pop();
            xPathContext.popSAXLocator();
        }
    }

    public void executeChildTemplates(ElemTemplateElement elemTemplateElement, ContentHandler contentHandler) throws TransformerException {
        SerializationHandler serializationHandler;
        SerializationHandler serializationHandler2 = serializationHandler = this.getSerializationHandler();
        try {
            serializationHandler.flushPending();
            LexicalHandler lexicalHandler = null;
            if (contentHandler instanceof LexicalHandler) {
                lexicalHandler = (LexicalHandler)((Object)contentHandler);
            }
            this.m_serializationHandler = new ToXMLSAXHandler(contentHandler, lexicalHandler, serializationHandler2.getEncoding());
            this.m_serializationHandler.setTransformer(this);
            this.executeChildTemplates(elemTemplateElement, true);
        }
        catch (TransformerException transformerException) {
            throw transformerException;
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
        finally {
            this.m_serializationHandler = serializationHandler2;
        }
    }

    public Vector processSortKeys(ElemForEach elemForEach, int n2) throws TransformerException {
        Vector<NodeSortKey> vector = null;
        XPathContext xPathContext = this.m_xcontext;
        int n3 = elemForEach.getSortElemCount();
        if (n3 > 0) {
            vector = new Vector<NodeSortKey>();
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            boolean bl;
            ElemSort elemSort = elemForEach.getSortElem(i2);
            if (this.m_debug) {
                this.getTraceManager().fireTraceEvent(elemSort);
            }
            String string = null != elemSort.getLang() ? elemSort.getLang().evaluate(xPathContext, n2, elemForEach) : null;
            String string2 = elemSort.getDataType().evaluate(xPathContext, n2, elemForEach);
            if (string2.indexOf(":") >= 0) {
                System.out.println("TODO: Need to write the hooks for QNAME sort data type");
            } else if (!string2.equalsIgnoreCase("text") && !string2.equalsIgnoreCase("number")) {
                elemForEach.error("ER_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"data-type", string2});
            }
            boolean bl2 = null != string2 && string2.equals("number");
            String string3 = elemSort.getOrder().evaluate(xPathContext, n2, elemForEach);
            if (!string3.equalsIgnoreCase("ascending") && !string3.equalsIgnoreCase("descending")) {
                elemForEach.error("ER_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"order", string3});
            }
            boolean bl3 = null != string3 && string3.equals("descending");
            AVT aVT = elemSort.getCaseOrder();
            if (null != aVT) {
                String string4 = aVT.evaluate(xPathContext, n2, elemForEach);
                if (!string4.equalsIgnoreCase("upper-first") && !string4.equalsIgnoreCase("lower-first")) {
                    elemForEach.error("ER_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"case-order", string4});
                }
                bl = null != string4 && string4.equals("upper-first");
            } else {
                bl = false;
            }
            vector.addElement(new NodeSortKey(this, elemSort.getSelect(), bl2, bl3, string, bl, elemForEach));
            if (!this.m_debug) continue;
            this.getTraceManager().fireTraceEndEvent(elemSort);
        }
        return vector;
    }

    public Vector getElementCallstack() {
        Vector<ElemTemplateElement> vector = new Vector<ElemTemplateElement>();
        int n2 = this.m_currentTemplateElements.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            ElemTemplateElement elemTemplateElement = (ElemTemplateElement)this.m_currentTemplateElements.elementAt(i2);
            if (null == elemTemplateElement) continue;
            vector.addElement(elemTemplateElement);
        }
        return vector;
    }

    public int getCurrentTemplateElementsCount() {
        return this.m_currentTemplateElements.size();
    }

    public ObjectStack getCurrentTemplateElements() {
        return this.m_currentTemplateElements;
    }

    public void pushElemTemplateElement(ElemTemplateElement elemTemplateElement) {
        this.m_currentTemplateElements.push(elemTemplateElement);
    }

    public void popElemTemplateElement() {
        this.m_currentTemplateElements.pop();
    }

    public void setCurrentElement(ElemTemplateElement elemTemplateElement) {
        this.m_currentTemplateElements.setTop(elemTemplateElement);
    }

    public ElemTemplateElement getCurrentElement() {
        return this.m_currentTemplateElements.size() > 0 ? (ElemTemplateElement)this.m_currentTemplateElements.peek() : null;
    }

    public int getCurrentNode() {
        return this.m_xcontext.getCurrentNode();
    }

    public Vector getTemplateCallstack() {
        Vector<ElemTemplateElement> vector = new Vector<ElemTemplateElement>();
        int n2 = this.m_currentTemplateElements.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            ElemTemplateElement elemTemplateElement = (ElemTemplateElement)this.m_currentTemplateElements.elementAt(i2);
            if (null == elemTemplateElement || elemTemplateElement.getXSLToken() == 19) continue;
            vector.addElement(elemTemplateElement);
        }
        return vector;
    }

    public ElemTemplate getCurrentTemplate() {
        ElemTemplateElement elemTemplateElement;
        for (elemTemplateElement = this.getCurrentElement(); null != elemTemplateElement && elemTemplateElement.getXSLToken() != 19; elemTemplateElement = elemTemplateElement.getParentElem()) {
        }
        return (ElemTemplate)elemTemplateElement;
    }

    public void pushPairCurrentMatched(ElemTemplateElement elemTemplateElement, int n2) {
        this.m_currentMatchTemplates.push(elemTemplateElement);
        this.m_currentMatchedNodes.push(n2);
    }

    public void popCurrentMatched() {
        this.m_currentMatchTemplates.pop();
        this.m_currentMatchedNodes.pop();
    }

    public ElemTemplate getMatchedTemplate() {
        return (ElemTemplate)this.m_currentMatchTemplates.peek();
    }

    public int getMatchedNode() {
        return this.m_currentMatchedNodes.peepTail();
    }

    public DTMIterator getContextNodeList() {
        try {
            DTMIterator dTMIterator = this.m_xcontext.getContextNodeList();
            return dTMIterator == null ? null : dTMIterator.cloneWithReset();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public Transformer getTransformer() {
        return this;
    }

    public void setStylesheet(StylesheetRoot stylesheetRoot) {
        this.m_stylesheetRoot = stylesheetRoot;
    }

    public final StylesheetRoot getStylesheet() {
        return this.m_stylesheetRoot;
    }

    public boolean getQuietConflictWarnings() {
        return this.m_quietConflictWarnings;
    }

    public void setQuietConflictWarnings(boolean bl) {
        this.m_quietConflictWarnings = bl;
    }

    public void setXPathContext(XPathContext xPathContext) {
        this.m_xcontext = xPathContext;
    }

    public final XPathContext getXPathContext() {
        return this.m_xcontext;
    }

    public StackGuard getStackGuard() {
        return this.m_stackGuard;
    }

    public int getRecursionLimit() {
        return this.m_stackGuard.getRecursionLimit();
    }

    public void setRecursionLimit(int n2) {
        this.m_stackGuard.setRecursionLimit(n2);
    }

    public SerializationHandler getResultTreeHandler() {
        return this.m_serializationHandler;
    }

    public SerializationHandler getSerializationHandler() {
        return this.m_serializationHandler;
    }

    public KeyManager getKeyManager() {
        return this.m_keyManager;
    }

    public boolean isRecursiveAttrSet(ElemAttributeSet elemAttributeSet) {
        int n2;
        if (null == this.m_attrSetStack) {
            this.m_attrSetStack = new Stack();
        }
        if (!this.m_attrSetStack.empty() && (n2 = this.m_attrSetStack.search(elemAttributeSet)) > -1) {
            return true;
        }
        return false;
    }

    public void pushElemAttributeSet(ElemAttributeSet elemAttributeSet) {
        this.m_attrSetStack.push(elemAttributeSet);
    }

    public void popElemAttributeSet() {
        this.m_attrSetStack.pop();
    }

    public CountersTable getCountersTable() {
        if (null == this.m_countersTable) {
            this.m_countersTable = new CountersTable();
        }
        return this.m_countersTable;
    }

    public boolean currentTemplateRuleIsNull() {
        return !this.m_currentTemplateRuleIsNull.isEmpty() && this.m_currentTemplateRuleIsNull.peek();
    }

    public void pushCurrentTemplateRuleIsNull(boolean bl) {
        this.m_currentTemplateRuleIsNull.push(bl);
    }

    public void popCurrentTemplateRuleIsNull() {
        this.m_currentTemplateRuleIsNull.pop();
    }

    public void pushCurrentFuncResult(Object object) {
        this.m_currentFuncResult.push(object);
    }

    public Object popCurrentFuncResult() {
        return this.m_currentFuncResult.pop();
    }

    public boolean currentFuncResultSeen() {
        return !this.m_currentFuncResult.empty() && this.m_currentFuncResult.peek() != null;
    }

    public MsgMgr getMsgMgr() {
        if (null == this.m_msgMgr) {
            this.m_msgMgr = new MsgMgr(this);
        }
        return this.m_msgMgr;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException {
        Boolean bl = this.m_reentryGuard;
        synchronized (bl) {
            if (errorListener == null) {
                throw new IllegalArgumentException(XSLMessages.createMessage("ER_NULL_ERROR_HANDLER", null));
            }
            this.m_errorHandler = errorListener;
        }
    }

    public ErrorListener getErrorListener() {
        return this.m_errorHandler;
    }

    public TraceManager getTraceManager() {
        return this.m_traceManager;
    }

    public boolean getFeature(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ("http://xml.org/trax/features/sax/input".equals(string)) {
            return true;
        }
        if ("http://xml.org/trax/features/dom/input".equals(string)) {
            return true;
        }
        throw new SAXNotRecognizedException(string);
    }

    public QName getMode() {
        return this.m_modes.isEmpty() ? null : (QName)this.m_modes.peek();
    }

    public void pushMode(QName qName) {
        this.m_modes.push(qName);
    }

    public void popMode() {
        this.m_modes.pop();
    }

    public void runTransformThread(int n2) {
        Thread thread = ThreadControllerWrapper.runThread(this, n2);
        this.setTransformThread(thread);
    }

    public void runTransformThread() {
        ThreadControllerWrapper.runThread(this, -1);
    }

    public static void runTransformThread(Runnable runnable) {
        ThreadControllerWrapper.runThread(runnable, -1);
    }

    public void waitTransformThread() throws SAXException {
        Thread thread = this.getTransformThread();
        if (null != thread) {
            try {
                Exception exception;
                ThreadControllerWrapper.waitThread(thread, this);
                if (!this.hasTransformThreadErrorCatcher() && null != (exception = this.getExceptionThrown())) {
                    exception.printStackTrace();
                    throw new SAXException(exception);
                }
                this.setTransformThread(null);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    public Exception getExceptionThrown() {
        return this.m_exceptionThrown;
    }

    public void setExceptionThrown(Exception exception) {
        this.m_exceptionThrown = exception;
    }

    public void setSourceTreeDocForThread(int n2) {
        this.m_doc = n2;
    }

    public void setXMLSource(Source source) {
        this.m_xmlSource = source;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isTransformDone() {
        TransformerImpl transformerImpl = this;
        synchronized (transformerImpl) {
            return this.m_isTransformDone;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setIsTransformDone(boolean bl) {
        TransformerImpl transformerImpl = this;
        synchronized (transformerImpl) {
            this.m_isTransformDone = bl;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void postExceptionFromThread(Exception exception) {
        this.m_isTransformDone = true;
        this.m_exceptionThrown = exception;
        TransformerImpl transformerImpl = this;
        synchronized (transformerImpl) {
            this.notifyAll();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void run() {
        block10 : {
            this.m_hasBeenReset = false;
            try {
                try {
                    this.m_isTransformDone = false;
                    this.transformNode(this.m_doc);
                }
                catch (Exception exception) {
                    if (null != this.m_transformThread) {
                        this.postExceptionFromThread(exception);
                        break block10;
                    }
                    throw new RuntimeException(exception.getMessage());
                }
                finally {
                    this.m_isTransformDone = true;
                    if (this.m_inputContentHandler instanceof TransformerHandlerImpl) {
                        ((TransformerHandlerImpl)this.m_inputContentHandler).clearCoRoutine();
                    }
                }
            }
            catch (Exception exception) {
                if (null != this.m_transformThread) {
                    this.postExceptionFromThread(exception);
                }
                throw new RuntimeException(exception.getMessage());
            }
        }
    }

    public TransformSnapshot getSnapshot() {
        return new TransformSnapshotImpl(this);
    }

    public void executeFromSnapshot(TransformSnapshot transformSnapshot) throws TransformerException {
        ElemTemplate elemTemplate = this.getMatchedTemplate();
        int n2 = this.getMatchedNode();
        this.pushElemTemplateElement(elemTemplate);
        this.m_xcontext.pushCurrentNode(n2);
        this.executeChildTemplates((ElemTemplateElement)elemTemplate, true);
    }

    public void resetToStylesheet(TransformSnapshot transformSnapshot) {
        ((TransformSnapshotImpl)transformSnapshot).apply(this);
    }

    public void stopTransformation() {
    }

    public short getShouldStripSpace(int n2, DTM dTM) {
        try {
            WhiteSpaceInfo whiteSpaceInfo = this.m_stylesheetRoot.getWhiteSpaceInfo(this.m_xcontext, n2, dTM);
            if (null == whiteSpaceInfo) {
                return 3;
            }
            return whiteSpaceInfo.getShouldStripSpace() ? 2 : 1;
        }
        catch (TransformerException transformerException) {
            return 3;
        }
    }

    public void init(ToXMLSAXHandler toXMLSAXHandler, Transformer transformer, ContentHandler contentHandler) {
        toXMLSAXHandler.setTransformer(transformer);
        toXMLSAXHandler.setContentHandler(contentHandler);
    }

    public void setSerializationHandler(SerializationHandler serializationHandler) {
        this.m_serializationHandler = serializationHandler;
    }

    public void fireGenerateEvent(int n2, char[] arrc, int n3, int n4) {
        GenerateEvent generateEvent = new GenerateEvent(this, n2, arrc, n3, n4);
        this.m_traceManager.fireGenerateEvent(generateEvent);
    }

    public void fireGenerateEvent(int n2, String string, Attributes attributes) {
        GenerateEvent generateEvent = new GenerateEvent(this, n2, string, attributes);
        this.m_traceManager.fireGenerateEvent(generateEvent);
    }

    public void fireGenerateEvent(int n2, String string, String string2) {
        GenerateEvent generateEvent = new GenerateEvent(this, n2, string, string2);
        this.m_traceManager.fireGenerateEvent(generateEvent);
    }

    public void fireGenerateEvent(int n2, String string) {
        GenerateEvent generateEvent = new GenerateEvent(this, n2, string);
        this.m_traceManager.fireGenerateEvent(generateEvent);
    }

    public void fireGenerateEvent(int n2) {
        GenerateEvent generateEvent = new GenerateEvent(this, n2);
        this.m_traceManager.fireGenerateEvent(generateEvent);
    }

    public boolean hasTraceListeners() {
        return this.m_traceManager.hasTraceListeners();
    }

    public boolean getDebug() {
        return this.m_debug;
    }

    public void setDebug(boolean bl) {
        this.m_debug = bl;
    }

    public boolean getIncremental() {
        return this.m_incremental;
    }

    public boolean getOptimize() {
        return this.m_optimizer;
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

