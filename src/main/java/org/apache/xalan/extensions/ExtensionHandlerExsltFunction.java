/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.io.IOException;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.extensions.ExtensionHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemExsltFuncResult;
import org.apache.xalan.templates.ElemExsltFunction;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class ExtensionHandlerExsltFunction
extends ExtensionHandler {
    private String m_namespace;
    private StylesheetRoot m_stylesheet;
    private static final QName RESULTQNAME = new QName("http://exslt.org/functions", "result");

    public ExtensionHandlerExsltFunction(String string, StylesheetRoot stylesheetRoot) {
        super(string, "xml");
        this.m_namespace = string;
        this.m_stylesheet = stylesheetRoot;
    }

    public void processElement(String string, ElemTemplateElement elemTemplateElement, TransformerImpl transformerImpl, Stylesheet stylesheet, Object object) throws TransformerException, IOException {
    }

    public ElemExsltFunction getFunction(String string) {
        QName qName = new QName(this.m_namespace, string);
        ElemTemplate elemTemplate = this.m_stylesheet.getTemplateComposed(qName);
        if (elemTemplate != null && elemTemplate instanceof ElemExsltFunction) {
            return (ElemExsltFunction)elemTemplate;
        }
        return null;
    }

    public boolean isFunctionAvailable(String string) {
        return this.getFunction(string) != null;
    }

    public boolean isElementAvailable(String string) {
        if (!new QName(this.m_namespace, string).equals(RESULTQNAME)) {
            return false;
        }
        ElemTemplateElement elemTemplateElement = this.m_stylesheet.getFirstChildElem();
        while (elemTemplateElement != null && elemTemplateElement != this.m_stylesheet) {
            if (elemTemplateElement instanceof ElemExsltFuncResult && this.ancestorIsFunction(elemTemplateElement)) {
                return true;
            }
            ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getFirstChildElem();
            if (elemTemplateElement2 == null) {
                elemTemplateElement2 = elemTemplateElement.getNextSiblingElem();
            }
            if (elemTemplateElement2 == null) {
                elemTemplateElement2 = elemTemplateElement.getParentElem();
            }
            elemTemplateElement = elemTemplateElement2;
        }
        return false;
    }

    private boolean ancestorIsFunction(ElemTemplateElement elemTemplateElement) {
        while (elemTemplateElement.getParentElem() != null && !(elemTemplateElement.getParentElem() instanceof StylesheetRoot)) {
            if (elemTemplateElement.getParentElem() instanceof ElemExsltFunction) {
                return true;
            }
            elemTemplateElement = elemTemplateElement.getParentElem();
        }
        return false;
    }

    public Object callFunction(String string, Vector vector, Object object, ExpressionContext expressionContext) throws TransformerException {
        throw new TransformerException("This method should not be called.");
    }

    public Object callFunction(FuncExtFunction funcExtFunction, Vector vector, ExpressionContext expressionContext) throws TransformerException {
        ExpressionNode expressionNode;
        for (expressionNode = funcExtFunction.exprGetParent(); expressionNode != null && !(expressionNode instanceof ElemTemplate); expressionNode = expressionNode.exprGetParent()) {
        }
        ElemTemplate elemTemplate = expressionNode != null ? (ElemTemplate)expressionNode : null;
        XObject[] arrxObject = new XObject[vector.size()];
        try {
            for (int i2 = 0; i2 < arrxObject.length; ++i2) {
                arrxObject[i2] = XObject.create(vector.get(i2));
            }
            ElemExsltFunction elemExsltFunction = this.getFunction(funcExtFunction.getFunctionName());
            if (null != elemExsltFunction) {
                XPathContext xPathContext = expressionContext.getXPathContext();
                TransformerImpl transformerImpl = (TransformerImpl)xPathContext.getOwnerObject();
                transformerImpl.pushCurrentFuncResult(null);
                elemExsltFunction.execute(transformerImpl, arrxObject);
                XObject xObject = (XObject)transformerImpl.popCurrentFuncResult();
                return xObject == null ? new XString("") : xObject;
            }
            throw new TransformerException(XSLMessages.createMessage("ER_FUNCTION_NOT_FOUND", new Object[]{funcExtFunction.getFunctionName()}));
        }
        catch (TransformerException transformerException) {
            throw transformerException;
        }
        catch (Exception exception) {
            throw new TransformerException(exception);
        }
    }
}

