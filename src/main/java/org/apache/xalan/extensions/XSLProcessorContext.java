/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.SerializerUtils;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.transformer.ClonerToResultTree;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.QName;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.DescendantIterator;
import org.apache.xpath.axes.OneStepIterator;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XString;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class XSLProcessorContext {
    private TransformerImpl transformer;
    private Stylesheet stylesheetTree;
    private DTM sourceTree;
    private int sourceNode;
    private QName mode;

    public XSLProcessorContext(TransformerImpl transformerImpl, Stylesheet stylesheet) {
        this.transformer = transformerImpl;
        this.stylesheetTree = stylesheet;
        XPathContext xPathContext = transformerImpl.getXPathContext();
        this.mode = transformerImpl.getMode();
        this.sourceNode = xPathContext.getCurrentNode();
        this.sourceTree = xPathContext.getDTM(this.sourceNode);
    }

    public TransformerImpl getTransformer() {
        return this.transformer;
    }

    public Stylesheet getStylesheet() {
        return this.stylesheetTree;
    }

    public Node getSourceTree() {
        return this.sourceTree.getNode(this.sourceTree.getDocumentRoot(this.sourceNode));
    }

    public Node getContextNode() {
        return this.sourceTree.getNode(this.sourceNode);
    }

    public QName getMode() {
        return this.mode;
    }

    public void outputToResultTree(Stylesheet stylesheet, Object object) throws TransformerException, MalformedURLException, FileNotFoundException, IOException {
        try {
            int n2;
            Object object2;
            XObject xObject;
            SerializationHandler serializationHandler = this.transformer.getResultTreeHandler();
            XPathContext xPathContext = this.transformer.getXPathContext();
            if (object instanceof XObject) {
                xObject = (XObject)object;
            } else if (object instanceof String) {
                xObject = new XString((String)object);
            } else if (object instanceof Boolean) {
                xObject = new XBoolean((boolean)((Boolean)object));
            } else if (object instanceof Double) {
                xObject = new XNumber((Double)object);
            } else if (object instanceof DocumentFragment) {
                n2 = xPathContext.getDTMHandleFromNode((DocumentFragment)object);
                xObject = new XRTreeFrag(n2, xPathContext);
            } else if (object instanceof DTM) {
                DTM dTM = (DTM)object;
                object2 = new DescendantIterator();
                object2.setRoot(dTM.getDocument(), xPathContext);
                xObject = new XNodeSet((DTMIterator)object2);
            } else if (object instanceof DTMAxisIterator) {
                DTMAxisIterator dTMAxisIterator = (DTMAxisIterator)object;
                object2 = new OneStepIterator(dTMAxisIterator, -1);
                xObject = new XNodeSet((DTMIterator)object2);
            } else {
                xObject = object instanceof DTMIterator ? new XNodeSet((DTMIterator)object) : (object instanceof NodeIterator ? new XNodeSet(new NodeSetDTM((NodeIterator)object, xPathContext)) : (object instanceof Node ? new XNodeSet(xPathContext.getDTMHandleFromNode((Node)object), xPathContext.getDTMManager()) : new XString(object.toString())));
            }
            n2 = xObject.getType();
            switch (n2) {
                case 1: 
                case 2: 
                case 3: {
                    object2 = xObject.str();
                    serializationHandler.characters(object2.toCharArray(), 0, object2.length());
                    break;
                }
                case 4: {
                    int n3;
                    DTMIterator dTMIterator = xObject.iter();
                    while (-1 != (n3 = dTMIterator.nextNode())) {
                        DTM dTM = dTMIterator.getDTM(n3);
                        int n4 = n3;
                        while (-1 != n3) {
                            serializationHandler.flushPending();
                            ClonerToResultTree.cloneToResultTree(n3, dTM.getNodeType(n3), dTM, serializationHandler, true);
                            int n5 = dTM.getFirstChild(n3);
                            while (-1 == n5) {
                                if (1 == dTM.getNodeType(n3)) {
                                    serializationHandler.endElement("", "", dTM.getNodeName(n3));
                                }
                                if (n4 == n3) break;
                                n5 = dTM.getNextSibling(n3);
                                if (-1 != n5 || n4 != (n3 = dTM.getParent(n3))) continue;
                                if (1 == dTM.getNodeType(n3)) {
                                    serializationHandler.endElement("", "", dTM.getNodeName(n3));
                                }
                                n5 = -1;
                                break;
                            }
                            n3 = n5;
                        }
                    }
                    break;
                }
                case 5: {
                    SerializerUtils.outputResultTreeFragment(serializationHandler, xObject, this.transformer.getXPathContext());
                }
            }
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
    }
}

