/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class VariableStack
implements Cloneable {
    XObject[] _stackFrames;
    int _frameTop;
    private int _currentFrameBottom;
    int[] _links;
    int _linksTop;
    private static XObject[] m_nulls = new XObject[1024];

    public VariableStack() {
        this.reset();
    }

    public VariableStack(int n2) {
        this.reset(n2, n2 * 2);
    }

    public synchronized Object clone() throws CloneNotSupportedException {
        VariableStack variableStack = (VariableStack)super.clone();
        variableStack._stackFrames = (XObject[])this._stackFrames.clone();
        variableStack._links = (int[])this._links.clone();
        return variableStack;
    }

    public XObject elementAt(int n2) {
        return this._stackFrames[n2];
    }

    public void reset() {
        int n2 = this._links == null ? 4096 : this._links.length;
        int n3 = this._stackFrames == null ? 8192 : this._stackFrames.length;
        this.reset(n2, n3);
    }

    protected void reset(int n2, int n3) {
        this._frameTop = 0;
        this._linksTop = 0;
        if (this._links == null) {
            this._links = new int[n2];
        }
        this._links[this._linksTop++] = 0;
        this._stackFrames = new XObject[n3];
    }

    public void setStackFrame(int n2) {
        this._currentFrameBottom = n2;
    }

    public int getStackFrame() {
        return this._currentFrameBottom;
    }

    public int link(int n2) {
        XObject[] arrxObject;
        this._currentFrameBottom = this._frameTop;
        this._frameTop += n2;
        if (this._frameTop >= this._stackFrames.length) {
            arrxObject = new XObject[this._stackFrames.length + 4096 + n2];
            System.arraycopy(this._stackFrames, 0, arrxObject, 0, this._stackFrames.length);
            this._stackFrames = arrxObject;
        }
        if (this._linksTop + 1 >= this._links.length) {
            arrxObject = new int[this._links.length + 2048];
            System.arraycopy(this._links, 0, arrxObject, 0, this._links.length);
            this._links = arrxObject;
        }
        this._links[this._linksTop++] = this._currentFrameBottom;
        return this._currentFrameBottom;
    }

    public void unlink() {
        this._frameTop = this._links[--this._linksTop];
        this._currentFrameBottom = this._links[this._linksTop - 1];
    }

    public void unlink(int n2) {
        this._frameTop = this._links[--this._linksTop];
        this._currentFrameBottom = n2;
    }

    public void setLocalVariable(int n2, XObject xObject) {
        this._stackFrames[n2 + this._currentFrameBottom] = xObject;
    }

    public void setLocalVariable(int n2, XObject xObject, int n3) {
        this._stackFrames[n2 + n3] = xObject;
    }

    public XObject getLocalVariable(XPathContext xPathContext, int n2) throws TransformerException {
        XObject xObject = this._stackFrames[n2 += this._currentFrameBottom];
        if (null == xObject) {
            throw new TransformerException(XSLMessages.createXPATHMessage("ER_VARIABLE_ACCESSED_BEFORE_BIND", null), xPathContext.getSAXLocator());
        }
        if (xObject.getType() == 600) {
            this._stackFrames[n2] = xObject.execute(xPathContext);
            return this._stackFrames[n2];
        }
        return xObject;
    }

    public XObject getLocalVariable(int n2, int n3) throws TransformerException {
        XObject xObject = this._stackFrames[n2 += n3];
        return xObject;
    }

    public XObject getLocalVariable(XPathContext xPathContext, int n2, boolean bl) throws TransformerException {
        XObject xObject = this._stackFrames[n2 += this._currentFrameBottom];
        if (null == xObject) {
            throw new TransformerException(XSLMessages.createXPATHMessage("ER_VARIABLE_ACCESSED_BEFORE_BIND", null), xPathContext.getSAXLocator());
        }
        if (xObject.getType() == 600) {
            this._stackFrames[n2] = xObject.execute(xPathContext);
            return this._stackFrames[n2];
        }
        return bl ? xObject : xObject.getFresh();
    }

    public boolean isLocalSet(int n2) throws TransformerException {
        return this._stackFrames[n2 + this._currentFrameBottom] != null;
    }

    public void clearLocalSlots(int n2, int n3) {
        System.arraycopy(m_nulls, 0, this._stackFrames, n2 += this._currentFrameBottom, n3);
    }

    public void setGlobalVariable(int n2, XObject xObject) {
        this._stackFrames[n2] = xObject;
    }

    public XObject getGlobalVariable(XPathContext xPathContext, int n2) throws TransformerException {
        XObject xObject = this._stackFrames[n2];
        if (xObject.getType() == 600) {
            this._stackFrames[n2] = xObject.execute(xPathContext);
            return this._stackFrames[n2];
        }
        return xObject;
    }

    public XObject getGlobalVariable(XPathContext xPathContext, int n2, boolean bl) throws TransformerException {
        XObject xObject = this._stackFrames[n2];
        if (xObject.getType() == 600) {
            this._stackFrames[n2] = xObject.execute(xPathContext);
            return this._stackFrames[n2];
        }
        return bl ? xObject : xObject.getFresh();
    }

    public XObject getVariableOrParam(XPathContext xPathContext, QName qName) throws TransformerException {
        PrefixResolver prefixResolver = xPathContext.getNamespaceContext();
        if (prefixResolver instanceof ElemTemplateElement) {
            ElemVariable elemVariable;
            ElemTemplateElement elemTemplateElement = (ElemTemplateElement)prefixResolver;
            if (!(elemTemplateElement instanceof Stylesheet)) {
                while (!(elemTemplateElement.getParentNode() instanceof Stylesheet)) {
                    ElemTemplateElement elemTemplateElement2 = elemTemplateElement;
                    while (null != (elemTemplateElement = elemTemplateElement.getPreviousSiblingElem())) {
                        if (!(elemTemplateElement instanceof ElemVariable) || !(elemVariable = (ElemVariable)elemTemplateElement).getName().equals(qName)) continue;
                        return this.getLocalVariable(xPathContext, elemVariable.getIndex());
                    }
                    elemTemplateElement = elemTemplateElement2.getParentElem();
                }
            }
            if (null != (elemVariable = elemTemplateElement.getStylesheetRoot().getVariableOrParamComposed(qName))) {
                return this.getGlobalVariable(xPathContext, elemVariable.getIndex());
            }
        }
        throw new TransformerException(XSLMessages.createXPATHMessage("ER_VAR_NOT_RESOLVABLE", new Object[]{qName.toString()}));
    }
}

