/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExtensionHandler;
import org.apache.xalan.extensions.ExtensionNamespacesManager;
import org.apache.xalan.extensions.ExtensionsTable;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemExtensionDecl;
import org.apache.xalan.templates.ElemFallback;
import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ElemExtensionCall
extends ElemLiteralResult {
    static final long serialVersionUID = 3171339708500216920L;
    String m_extns;
    String m_lang;
    String m_srcURL;
    String m_scriptSrc;
    ElemExtensionDecl m_decl = null;

    public int getXSLToken() {
        return 79;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        this.m_extns = this.getNamespace();
        this.m_decl = this.getElemExtensionDecl(stylesheetRoot, this.m_extns);
        if (this.m_decl == null) {
            stylesheetRoot.getExtensionNamespacesManager().registerExtension(this.m_extns);
        }
    }

    private ElemExtensionDecl getElemExtensionDecl(StylesheetRoot stylesheetRoot, String string) {
        ElemExtensionDecl elemExtensionDecl = null;
        int n2 = stylesheetRoot.getGlobalImportCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            StylesheetComposed stylesheetComposed = stylesheetRoot.getGlobalImport(i2);
            for (ElemTemplateElement elemTemplateElement = stylesheetComposed.getFirstChildElem(); elemTemplateElement != null; elemTemplateElement = elemTemplateElement.getNextSiblingElem()) {
                String string2;
                String string3;
                if (85 != elemTemplateElement.getXSLToken() || !string.equals(string2 = elemTemplateElement.getNamespaceForPrefix(string3 = (elemExtensionDecl = (ElemExtensionDecl)elemTemplateElement).getPrefix()))) continue;
                return elemExtensionDecl;
            }
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void executeFallbacks(TransformerImpl transformerImpl) throws TransformerException {
        ElemTemplateElement elemTemplateElement = this.m_firstChild;
        while (elemTemplateElement != null) {
            if (elemTemplateElement.getXSLToken() == 57) {
                try {
                    transformerImpl.pushElemTemplateElement(elemTemplateElement);
                    ((ElemFallback)elemTemplateElement).executeFallback(transformerImpl);
                }
                finally {
                    transformerImpl.popElemTemplateElement();
                }
            }
            elemTemplateElement = elemTemplateElement.m_nextSibling;
        }
    }

    private boolean hasFallbackChildren() {
        ElemTemplateElement elemTemplateElement = this.m_firstChild;
        while (elemTemplateElement != null) {
            if (elemTemplateElement.getXSLToken() == 57) {
                return true;
            }
            elemTemplateElement = elemTemplateElement.m_nextSibling;
        }
        return false;
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        block15 : {
            if (transformerImpl.getStylesheet().isSecureProcessing()) {
                throw new TransformerException(XSLMessages.createMessage("ER_EXTENSION_ELEMENT_NOT_ALLOWED_IN_SECURE_PROCESSING", new Object[]{this.getRawName()}));
            }
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEvent(this);
            }
            try {
                transformerImpl.getResultTreeHandler().flushPending();
                ExtensionsTable extensionsTable = transformerImpl.getExtensionsTable();
                ExtensionHandler extensionHandler = extensionsTable.get(this.m_extns);
                if (null == extensionHandler) {
                    if (this.hasFallbackChildren()) {
                        this.executeFallbacks(transformerImpl);
                    } else {
                        TransformerException transformerException = new TransformerException(XSLMessages.createMessage("ER_CALL_TO_EXT_FAILED", new Object[]{this.getNodeName()}));
                        transformerImpl.getErrorListener().fatalError(transformerException);
                    }
                    return;
                }
                try {
                    extensionHandler.processElement(this.getLocalName(), this, transformerImpl, this.getStylesheet(), this);
                }
                catch (Exception exception) {
                    if (this.hasFallbackChildren()) {
                        this.executeFallbacks(transformerImpl);
                        break block15;
                    }
                    if (exception instanceof TransformerException) {
                        TransformerException transformerException = (TransformerException)exception;
                        if (null == transformerException.getLocator()) {
                            transformerException.setLocator(this);
                        }
                        transformerImpl.getErrorListener().fatalError(transformerException);
                        break block15;
                    }
                    if (exception instanceof RuntimeException) {
                        transformerImpl.getErrorListener().fatalError(new TransformerException(exception));
                        break block15;
                    }
                    transformerImpl.getErrorListener().warning(new TransformerException(exception));
                }
            }
            catch (TransformerException transformerException) {
                transformerImpl.getErrorListener().fatalError(transformerException);
            }
            catch (SAXException sAXException) {
                throw new TransformerException(sAXException);
            }
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    public String getAttribute(String string, Node node, TransformerImpl transformerImpl) throws TransformerException {
        AVT aVT = this.getLiteralResultAttribute(string);
        if (null != aVT && aVT.getRawName().equals(string)) {
            XPathContext xPathContext = transformerImpl.getXPathContext();
            return aVT.evaluate(xPathContext, xPathContext.getDTMHandleFromNode(node), this);
        }
        return null;
    }

    protected boolean accept(XSLTVisitor xSLTVisitor) {
        return xSLTVisitor.visitExtensionElement(this);
    }
}

