/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.SerializerUtils;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.DTMTreeWalker;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.XPathContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TreeWalker2Result
extends DTMTreeWalker {
    TransformerImpl m_transformer;
    SerializationHandler m_handler;
    int m_startNode;

    public TreeWalker2Result(TransformerImpl transformerImpl, SerializationHandler serializationHandler) {
        super(serializationHandler, null);
        this.m_transformer = transformerImpl;
        this.m_handler = serializationHandler;
    }

    public void traverse(int n2) throws SAXException {
        this.m_dtm = this.m_transformer.getXPathContext().getDTM(n2);
        this.m_startNode = n2;
        super.traverse(n2);
    }

    protected void endNode(int n2) throws SAXException {
        super.endNode(n2);
        if (1 == this.m_dtm.getNodeType(n2)) {
            this.m_transformer.getXPathContext().popCurrentNode();
        }
    }

    protected void startNode(int n2) throws SAXException {
        XPathContext xPathContext = this.m_transformer.getXPathContext();
        try {
            if (1 == this.m_dtm.getNodeType(n2)) {
                xPathContext.pushCurrentNode(n2);
                if (this.m_startNode != n2) {
                    super.startNode(n2);
                } else {
                    String string = this.m_dtm.getNodeName(n2);
                    String string2 = this.m_dtm.getLocalName(n2);
                    String string3 = this.m_dtm.getNamespaceURI(n2);
                    this.m_handler.startElement(string3, string2, string);
                    boolean bl = false;
                    DTM dTM = this.m_dtm;
                    int n3 = dTM.getFirstNamespaceNode(n2, true);
                    while (-1 != n3) {
                        SerializerUtils.ensureNamespaceDeclDeclared(this.m_handler, dTM, n3);
                        n3 = dTM.getNextNamespaceNode(n2, n3, true);
                    }
                    n3 = dTM.getFirstAttribute(n2);
                    while (-1 != n3) {
                        SerializerUtils.addAttribute(this.m_handler, n3);
                        n3 = dTM.getNextAttribute(n3);
                    }
                }
            } else {
                xPathContext.pushCurrentNode(n2);
                super.startNode(n2);
                xPathContext.popCurrentNode();
            }
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }
}

