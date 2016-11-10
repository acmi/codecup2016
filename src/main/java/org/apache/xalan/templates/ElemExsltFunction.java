/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExtensionNamespaceSupport;
import org.apache.xalan.extensions.ExtensionNamespacesManager;
import org.apache.xalan.templates.ElemParam;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ElemExsltFunction
extends ElemTemplate {
    static final long serialVersionUID = 272154954793534771L;

    public int getXSLToken() {
        return 88;
    }

    public String getNodeName() {
        return "function";
    }

    public void execute(TransformerImpl transformerImpl, XObject[] arrxObject) throws TransformerException {
        XPathContext xPathContext = transformerImpl.getXPathContext();
        VariableStack variableStack = xPathContext.getVarStack();
        int n2 = variableStack.getStackFrame();
        int n3 = variableStack.link(this.m_frameSize);
        if (this.m_inArgsSize < arrxObject.length) {
            throw new TransformerException("function called with too many args");
        }
        if (this.m_inArgsSize > 0) {
            variableStack.clearLocalSlots(0, this.m_inArgsSize);
            if (arrxObject.length > 0) {
                variableStack.setStackFrame(n2);
                NodeList nodeList = this.getChildNodes();
                for (int i2 = 0; i2 < arrxObject.length; ++i2) {
                    Node node = nodeList.item(i2);
                    if (!(nodeList.item(i2) instanceof ElemParam)) continue;
                    ElemParam elemParam = (ElemParam)nodeList.item(i2);
                    variableStack.setLocalVariable(elemParam.getIndex(), arrxObject[i2], n3);
                }
                variableStack.setStackFrame(n3);
            }
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        variableStack.setStackFrame(n3);
        transformerImpl.executeChildTemplates((ElemTemplateElement)this, true);
        variableStack.unlink(n2);
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        String string = this.getName().getNamespace();
        String string2 = stylesheetRoot.getExtensionHandlerClass();
        Object[] arrobject = new Object[]{string, stylesheetRoot};
        ExtensionNamespaceSupport extensionNamespaceSupport = new ExtensionNamespaceSupport(string, string2, arrobject);
        stylesheetRoot.getExtensionNamespacesManager().registerExtension(extensionNamespaceSupport);
        if (!string.equals("http://exslt.org/functions")) {
            string = "http://exslt.org/functions";
            arrobject = new Object[]{string, stylesheetRoot};
            extensionNamespaceSupport = new ExtensionNamespaceSupport(string, string2, arrobject);
            stylesheetRoot.getExtensionNamespacesManager().registerExtension(extensionNamespaceSupport);
        }
    }
}

