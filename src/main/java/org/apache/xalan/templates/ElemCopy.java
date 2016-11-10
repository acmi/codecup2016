/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.SerializerUtils;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemUse;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.ClonerToResultTree;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;

public class ElemCopy
extends ElemUse {
    static final long serialVersionUID = 5478580783896941384L;

    public int getXSLToken() {
        return 9;
    }

    public String getNodeName() {
        return "copy";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        XPathContext xPathContext = transformerImpl.getXPathContext();
        try {
            block9 : {
                try {
                    int n2 = xPathContext.getCurrentNode();
                    xPathContext.pushCurrentNode(n2);
                    DTM dTM = xPathContext.getDTM(n2);
                    short s2 = dTM.getNodeType(n2);
                    if (9 != s2 && 11 != s2) {
                        SerializationHandler serializationHandler = transformerImpl.getSerializationHandler();
                        if (transformerImpl.getDebug()) {
                            transformerImpl.getTraceManager().fireTraceEvent(this);
                        }
                        ClonerToResultTree.cloneToResultTree(n2, s2, dTM, serializationHandler, false);
                        if (1 == s2) {
                            super.execute(transformerImpl);
                            SerializerUtils.processNSDecls(serializationHandler, n2, s2, dTM);
                            transformerImpl.executeChildTemplates((ElemTemplateElement)this, true);
                            String string = dTM.getNamespaceURI(n2);
                            String string2 = dTM.getLocalName(n2);
                            transformerImpl.getResultTreeHandler().endElement(string, string2, dTM.getNodeName(n2));
                        }
                        if (transformerImpl.getDebug()) {
                            transformerImpl.getTraceManager().fireTraceEndEvent(this);
                        }
                        break block9;
                    }
                    if (transformerImpl.getDebug()) {
                        transformerImpl.getTraceManager().fireTraceEvent(this);
                    }
                    super.execute(transformerImpl);
                    transformerImpl.executeChildTemplates((ElemTemplateElement)this, true);
                    if (!transformerImpl.getDebug()) break block9;
                    transformerImpl.getTraceManager().fireTraceEndEvent(this);
                }
                catch (SAXException sAXException) {
                    throw new TransformerException(sAXException);
                }
            }
            java.lang.Object var10_10 = null;
            xPathContext.popCurrentNode();
        }
        catch (Throwable throwable) {
            java.lang.Object var10_11 = null;
            xPathContext.popCurrentNode();
            throw throwable;
        }
    }
}

