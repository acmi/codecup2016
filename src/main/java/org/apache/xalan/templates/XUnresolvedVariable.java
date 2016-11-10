/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class XUnresolvedVariable
extends XObject {
    static final long serialVersionUID = -256779804767950188L;
    private transient int m_context;
    private transient TransformerImpl m_transformer;
    private transient int m_varStackPos = -1;
    private transient int m_varStackContext;
    private boolean m_isGlobal;
    private transient boolean m_doneEval = true;

    public XUnresolvedVariable(ElemVariable elemVariable, int n2, TransformerImpl transformerImpl, int n3, int n4, boolean bl) {
        super(elemVariable);
        this.m_context = n2;
        this.m_transformer = transformerImpl;
        this.m_varStackPos = n3;
        this.m_varStackContext = n4;
        this.m_isGlobal = bl;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        if (!this.m_doneEval) {
            this.m_transformer.getMsgMgr().error(xPathContext.getSAXLocator(), "ER_REFERENCING_ITSELF", new Object[]{((ElemVariable)this.object()).getName().getLocalName()});
        }
        VariableStack variableStack = xPathContext.getVarStack();
        int n2 = variableStack.getStackFrame();
        ElemVariable elemVariable = (ElemVariable)this.m_obj;
        try {
            this.m_doneEval = false;
            if (-1 != elemVariable.m_frameSize) {
                variableStack.link(elemVariable.m_frameSize);
            }
            XObject xObject = elemVariable.getValue(this.m_transformer, this.m_context);
            this.m_doneEval = true;
            XObject xObject2 = xObject;
            return xObject2;
        }
        finally {
            if (-1 != elemVariable.m_frameSize) {
                variableStack.unlink(n2);
            }
        }
    }

    public void setVarStackPos(int n2) {
        this.m_varStackPos = n2;
    }

    public void setVarStackContext(int n2) {
        this.m_varStackContext = n2;
    }

    public int getType() {
        return 600;
    }

    public String getTypeString() {
        return "XUnresolvedVariable (" + this.object().getClass().getName() + ")";
    }
}

