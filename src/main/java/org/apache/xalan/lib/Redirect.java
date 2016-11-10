/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.xml.transform.Result;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import org.apache.xalan.extensions.XSLProcessorContext;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class Redirect {
    protected Hashtable m_formatterListeners = new Hashtable();
    protected Hashtable m_outputStreams = new Hashtable();
    public static final boolean DEFAULT_APPEND_OPEN = false;
    public static final boolean DEFAULT_APPEND_WRITE = false;

    public void open(XSLProcessorContext xSLProcessorContext, ElemExtensionCall elemExtensionCall) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
        String string = this.getFilename(xSLProcessorContext, elemExtensionCall);
        Object v2 = this.m_formatterListeners.get(string);
        if (null == v2) {
            String string2 = elemExtensionCall.getAttribute("mkdirs", xSLProcessorContext.getContextNode(), xSLProcessorContext.getTransformer());
            boolean bl = string2 != null ? string2.equals("true") || string2.equals("yes") : true;
            String string3 = elemExtensionCall.getAttribute("append", xSLProcessorContext.getContextNode(), xSLProcessorContext.getTransformer());
            boolean bl2 = string3 != null ? string3.equals("true") || string3.equals("yes") : false;
            ContentHandler contentHandler = this.makeFormatterListener(xSLProcessorContext, elemExtensionCall, string, true, bl, bl2);
        }
    }

    public void write(XSLProcessorContext xSLProcessorContext, ElemExtensionCall elemExtensionCall) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
        OutputStream outputStream;
        ContentHandler contentHandler;
        Object object;
        String string = this.getFilename(xSLProcessorContext, elemExtensionCall);
        Object v2 = this.m_formatterListeners.get(string);
        boolean bl = false;
        if (null == v2) {
            object = elemExtensionCall.getAttribute("mkdirs", xSLProcessorContext.getContextNode(), xSLProcessorContext.getTransformer());
            boolean bl2 = object != null ? object.equals("true") || object.equals("yes") : true;
            String string2 = elemExtensionCall.getAttribute("append", xSLProcessorContext.getContextNode(), xSLProcessorContext.getTransformer());
            boolean bl3 = string2 != null ? string2.equals("true") || string2.equals("yes") : false;
            contentHandler = this.makeFormatterListener(xSLProcessorContext, elemExtensionCall, string, true, bl2, bl3);
        } else {
            bl = true;
            contentHandler = (ContentHandler)v2;
        }
        object = xSLProcessorContext.getTransformer();
        this.startRedirection((TransformerImpl)object, contentHandler);
        object.executeChildTemplates(elemExtensionCall, xSLProcessorContext.getContextNode(), xSLProcessorContext.getMode(), contentHandler);
        this.endRedirection((TransformerImpl)object);
        if (!bl && null != (outputStream = (OutputStream)this.m_outputStreams.get(string))) {
            try {
                contentHandler.endDocument();
            }
            catch (SAXException sAXException) {
                throw new TransformerException(sAXException);
            }
            outputStream.close();
            this.m_outputStreams.remove(string);
            this.m_formatterListeners.remove(string);
        }
    }

    public void close(XSLProcessorContext xSLProcessorContext, ElemExtensionCall elemExtensionCall) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
        String string = this.getFilename(xSLProcessorContext, elemExtensionCall);
        Object v2 = this.m_formatterListeners.get(string);
        if (null != v2) {
            ContentHandler contentHandler = (ContentHandler)v2;
            try {
                contentHandler.endDocument();
            }
            catch (SAXException sAXException) {
                throw new TransformerException(sAXException);
            }
            OutputStream outputStream = (OutputStream)this.m_outputStreams.get(string);
            if (null != outputStream) {
                outputStream.close();
                this.m_outputStreams.remove(string);
            }
            this.m_formatterListeners.remove(string);
        }
    }

    private String getFilename(XSLProcessorContext xSLProcessorContext, ElemExtensionCall elemExtensionCall) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
        String string;
        String string2 = elemExtensionCall.getAttribute("select", xSLProcessorContext.getContextNode(), xSLProcessorContext.getTransformer());
        if (null != string2) {
            XPathContext xPathContext = xSLProcessorContext.getTransformer().getXPathContext();
            XPath xPath = new XPath(string2, elemExtensionCall, xPathContext.getNamespaceContext(), 0);
            XObject xObject = xPath.execute(xPathContext, xSLProcessorContext.getContextNode(), (PrefixResolver)elemExtensionCall);
            string = xObject.str();
            if (null == string || string.length() == 0) {
                string = elemExtensionCall.getAttribute("file", xSLProcessorContext.getContextNode(), xSLProcessorContext.getTransformer());
            }
        } else {
            string = elemExtensionCall.getAttribute("file", xSLProcessorContext.getContextNode(), xSLProcessorContext.getTransformer());
        }
        if (null == string) {
            xSLProcessorContext.getTransformer().getMsgMgr().error((SourceLocator)elemExtensionCall, elemExtensionCall, xSLProcessorContext.getContextNode(), "ER_REDIRECT_COULDNT_GET_FILENAME");
        }
        return string;
    }

    private String urlToFileName(String string) {
        if (null != string) {
            if (string.startsWith("file:////")) {
                string = string.substring(7);
            } else if (string.startsWith("file:///")) {
                string = string.substring(6);
            } else if (string.startsWith("file://")) {
                string = string.substring(5);
            } else if (string.startsWith("file:/")) {
                string = string.substring(5);
            } else if (string.startsWith("file:")) {
                string = string.substring(4);
            }
        }
        return string;
    }

    private ContentHandler makeFormatterListener(XSLProcessorContext xSLProcessorContext, ElemExtensionCall elemExtensionCall, String string, boolean bl, boolean bl2, boolean bl3) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
        Object object;
        Object object2;
        File file = new File(string);
        TransformerImpl transformerImpl = xSLProcessorContext.getTransformer();
        if (!file.isAbsolute()) {
            String string2;
            object = transformerImpl.getOutputTarget();
            string2 = null != object && (string2 = object.getSystemId()) != null ? this.urlToFileName(string2) : this.urlToFileName(transformerImpl.getBaseURLOfSource());
            if (null != string2) {
                object2 = new File(string2);
                file = new File(object2.getParent(), string);
            }
        }
        if (bl2 && null != (object = file.getParent()) && object.length() > 0) {
            object2 = new File((String)object);
            object2.mkdirs();
        }
        object = transformerImpl.getOutputFormat();
        object2 = new FileOutputStream(file.getPath(), bl3);
        try {
            SerializationHandler serializationHandler = this.createSerializationHandler(transformerImpl, (FileOutputStream)object2, file, (OutputProperties)object);
            try {
                serializationHandler.startDocument();
            }
            catch (SAXException sAXException) {
                throw new TransformerException(sAXException);
            }
            if (bl) {
                this.m_outputStreams.put(string, object2);
                this.m_formatterListeners.put(string, serializationHandler);
            }
            return serializationHandler;
        }
        catch (TransformerException transformerException) {
            throw new TransformerException(transformerException);
        }
    }

    public void startRedirection(TransformerImpl transformerImpl, ContentHandler contentHandler) {
    }

    public void endRedirection(TransformerImpl transformerImpl) {
    }

    public SerializationHandler createSerializationHandler(TransformerImpl transformerImpl, FileOutputStream fileOutputStream, File file, OutputProperties outputProperties) throws IOException, TransformerException {
        SerializationHandler serializationHandler = transformerImpl.createSerializationHandler(new StreamResult(fileOutputStream), outputProperties);
        return serializationHandler;
    }
}

