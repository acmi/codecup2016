/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.extensions.ExtensionHandler;
import org.apache.xalan.extensions.ExtensionNamespaceSupport;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xpath.XPathProcessorException;
import org.apache.xpath.functions.FuncExtFunction;

public class ExtensionsTable {
    public Hashtable m_extensionFunctionNamespaces = new Hashtable();
    private StylesheetRoot m_sroot;

    public ExtensionsTable(StylesheetRoot stylesheetRoot) throws TransformerException {
        this.m_sroot = stylesheetRoot;
        Vector vector = this.m_sroot.getExtensions();
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            ExtensionNamespaceSupport extensionNamespaceSupport = (ExtensionNamespaceSupport)vector.get(i2);
            ExtensionHandler extensionHandler = extensionNamespaceSupport.launch();
            if (extensionHandler == null) continue;
            this.addExtensionNamespace(extensionNamespaceSupport.getNamespace(), extensionHandler);
        }
    }

    public ExtensionHandler get(String string) {
        return (ExtensionHandler)this.m_extensionFunctionNamespaces.get(string);
    }

    public void addExtensionNamespace(String string, ExtensionHandler extensionHandler) {
        this.m_extensionFunctionNamespaces.put(string, extensionHandler);
    }

    public boolean functionAvailable(String string, String string2) throws TransformerException {
        ExtensionHandler extensionHandler;
        boolean bl = false;
        if (null != string && (extensionHandler = (ExtensionHandler)this.m_extensionFunctionNamespaces.get(string)) != null) {
            bl = extensionHandler.isFunctionAvailable(string2);
        }
        return bl;
    }

    public boolean elementAvailable(String string, String string2) throws TransformerException {
        ExtensionHandler extensionHandler;
        boolean bl = false;
        if (null != string && (extensionHandler = (ExtensionHandler)this.m_extensionFunctionNamespaces.get(string)) != null) {
            bl = extensionHandler.isElementAvailable(string2);
        }
        return bl;
    }

    public Object extFunction(String string, String string2, Vector vector, Object object, ExpressionContext expressionContext) throws TransformerException {
        Object object2 = null;
        if (null != string) {
            ExtensionHandler extensionHandler = (ExtensionHandler)this.m_extensionFunctionNamespaces.get(string);
            if (null != extensionHandler) {
                try {
                    object2 = extensionHandler.callFunction(string2, vector, object, expressionContext);
                }
                catch (TransformerException transformerException) {
                    throw transformerException;
                }
                catch (Exception exception) {
                    throw new TransformerException(exception);
                }
            } else {
                throw new XPathProcessorException(XSLMessages.createMessage("ER_EXTENSION_FUNC_UNKNOWN", new Object[]{string, string2}));
            }
        }
        return object2;
    }

    public Object extFunction(FuncExtFunction funcExtFunction, Vector vector, ExpressionContext expressionContext) throws TransformerException {
        Object object = null;
        String string = funcExtFunction.getNamespace();
        if (null != string) {
            ExtensionHandler extensionHandler = (ExtensionHandler)this.m_extensionFunctionNamespaces.get(string);
            if (null != extensionHandler) {
                try {
                    object = extensionHandler.callFunction(funcExtFunction, vector, expressionContext);
                }
                catch (TransformerException transformerException) {
                    throw transformerException;
                }
                catch (Exception exception) {
                    throw new TransformerException(exception);
                }
            } else {
                throw new XPathProcessorException(XSLMessages.createMessage("ER_EXTENSION_FUNC_UNKNOWN", new Object[]{string, funcExtFunction.getFunctionName()}));
            }
        }
        return object;
    }
}

