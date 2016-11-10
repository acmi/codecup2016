/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.extensions.XSLProcessorContext;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class PipeDocument {
    public void pipeDocument(XSLProcessorContext xSLProcessorContext, ElemExtensionCall elemExtensionCall) throws TransformerException, TransformerConfigurationException, SAXException, IOException, FileNotFoundException {
        SAXTransformerFactory sAXTransformerFactory = (SAXTransformerFactory)TransformerFactory.newInstance();
        String string = elemExtensionCall.getAttribute("source", xSLProcessorContext.getContextNode(), xSLProcessorContext.getTransformer());
        TransformerImpl transformerImpl = xSLProcessorContext.getTransformer();
        String string2 = transformerImpl.getBaseURLOfSource();
        String string3 = SystemIDResolver.getAbsoluteURI(string, string2);
        String string4 = elemExtensionCall.getAttribute("target", xSLProcessorContext.getContextNode(), xSLProcessorContext.getTransformer());
        XPathContext xPathContext = xSLProcessorContext.getTransformer().getXPathContext();
        int n2 = xPathContext.getDTMHandleFromNode(xSLProcessorContext.getContextNode());
        String string5 = elemExtensionCall.getSystemId();
        NodeList nodeList = null;
        NodeList nodeList2 = null;
        Node node = null;
        Node node2 = null;
        if (elemExtensionCall.hasChildNodes()) {
            nodeList = elemExtensionCall.getChildNodes();
            Vector<TransformerHandler> vector = new Vector<TransformerHandler>(nodeList.getLength());
            for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
                node = nodeList.item(i2);
                if (node.getNodeType() != 1 || !((Element)node).getTagName().equals("stylesheet") || !(node instanceof ElemLiteralResult)) continue;
                AVT aVT = ((ElemLiteralResult)node).getLiteralResultAttribute("href");
                String string6 = aVT.evaluate(xPathContext, n2, elemExtensionCall);
                String string7 = SystemIDResolver.getAbsoluteURI(string6, string5);
                Templates templates = sAXTransformerFactory.newTemplates(new StreamSource(string7));
                TransformerHandler transformerHandler = sAXTransformerFactory.newTransformerHandler(templates);
                Transformer transformer = transformerHandler.getTransformer();
                vector.addElement(transformerHandler);
                nodeList2 = node.getChildNodes();
                for (int i3 = 0; i3 < nodeList2.getLength(); ++i3) {
                    node2 = nodeList2.item(i3);
                    if (node2.getNodeType() != 1 || !((Element)node2).getTagName().equals("param") || !(node2 instanceof ElemLiteralResult)) continue;
                    aVT = ((ElemLiteralResult)node2).getLiteralResultAttribute("name");
                    String string8 = aVT.evaluate(xPathContext, n2, elemExtensionCall);
                    aVT = ((ElemLiteralResult)node2).getLiteralResultAttribute("value");
                    String string9 = aVT.evaluate(xPathContext, n2, elemExtensionCall);
                    transformer.setParameter(string8, string9);
                }
            }
            this.usePipe(vector, string3, string4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void usePipe(Vector vector, String string, String string2) throws TransformerException, TransformerConfigurationException, FileNotFoundException, IOException, SAXException, SAXNotRecognizedException {
        Object object;
        Object object2;
        XMLReader xMLReader = XMLReaderFactory.createXMLReader();
        TransformerHandler transformerHandler = (TransformerHandler)vector.firstElement();
        xMLReader.setContentHandler(transformerHandler);
        xMLReader.setProperty("http://xml.org/sax/properties/lexical-handler", transformerHandler);
        for (int i2 = 1; i2 < vector.size(); ++i2) {
            object2 = (TransformerHandler)vector.elementAt(i2 - 1);
            object = (TransformerHandler)vector.elementAt(i2);
            object2.setResult(new SAXResult((ContentHandler)object));
        }
        TransformerHandler transformerHandler2 = (TransformerHandler)vector.lastElement();
        object2 = transformerHandler2.getTransformer();
        object = object2.getOutputProperties();
        Serializer serializer = SerializerFactory.getSerializer((Properties)object);
        FileOutputStream fileOutputStream = new FileOutputStream(string2);
        try {
            serializer.setOutputStream(fileOutputStream);
            transformerHandler2.setResult(new SAXResult(serializer.asContentHandler()));
            xMLReader.parse(string);
        }
        finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
}

