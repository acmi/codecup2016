/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.ObjectStack;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;

public class StackGuard {
    private int m_recursionLimit = -1;
    TransformerImpl m_transformer;

    public int getRecursionLimit() {
        return this.m_recursionLimit;
    }

    public void setRecursionLimit(int n2) {
        this.m_recursionLimit = n2;
    }

    public StackGuard(TransformerImpl transformerImpl) {
        this.m_transformer = transformerImpl;
    }

    public int countLikeTemplates(ElemTemplate elemTemplate, int n2) {
        ObjectStack objectStack = this.m_transformer.getCurrentTemplateElements();
        int n3 = 1;
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            if ((ElemTemplateElement)objectStack.elementAt(i2) != elemTemplate) continue;
            ++n3;
        }
        return n3;
    }

    private ElemTemplate getNextMatchOrNamedTemplate(int n2) {
        ObjectStack objectStack = this.m_transformer.getCurrentTemplateElements();
        for (int i2 = n2; i2 >= 0; --i2) {
            ElemTemplateElement elemTemplateElement = (ElemTemplateElement)objectStack.elementAt(i2);
            if (null == elemTemplateElement || elemTemplateElement.getXSLToken() != 19) continue;
            return (ElemTemplate)elemTemplateElement;
        }
        return null;
    }

    public void checkForInfinateLoop() throws TransformerException {
        ElemTemplate elemTemplate;
        int n2 = this.m_transformer.getCurrentTemplateElementsCount();
        if (n2 < this.m_recursionLimit) {
            return;
        }
        if (this.m_recursionLimit <= 0) {
            return;
        }
        for (int i2 = n2 - 1; i2 >= this.m_recursionLimit && null != (elemTemplate = this.getNextMatchOrNamedTemplate(i2)); --i2) {
            int n3 = this.countLikeTemplates(elemTemplate, i2);
            if (n3 < this.m_recursionLimit) continue;
            String string = XSLMessages.createMessage(null != elemTemplate.getName() ? "nameIs" : "matchPatternIs", null);
            Object[] arrobject = new Object[3];
            arrobject[0] = new Integer(n3);
            arrobject[1] = string;
            arrobject[2] = null != elemTemplate.getName() ? elemTemplate.getName().toString() : elemTemplate.getMatch().getPatternString();
            Object[] arrobject2 = arrobject;
            String string2 = XSLMessages.createMessage("recursionTooDeep", arrobject2);
            throw new TransformerException(string2);
        }
    }
}

