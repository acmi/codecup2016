/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExtensionNamespaceSupport;
import org.apache.xalan.extensions.ExtensionNamespacesManager;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemExtensionScript;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.StringVector;

public class ElemExtensionDecl
extends ElemTemplateElement {
    static final long serialVersionUID = -4692738885172766789L;
    private String m_prefix = null;
    private StringVector m_functions = new StringVector();
    private StringVector m_elements = null;

    public void setPrefix(String string) {
        this.m_prefix = string;
    }

    public String getPrefix() {
        return this.m_prefix;
    }

    public void setFunctions(StringVector stringVector) {
        this.m_functions = stringVector;
    }

    public StringVector getFunctions() {
        return this.m_functions;
    }

    public String getFunction(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_functions) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.m_functions.elementAt(n2);
    }

    public int getFunctionCount() {
        return null != this.m_functions ? this.m_functions.size() : 0;
    }

    public void setElements(StringVector stringVector) {
        this.m_elements = stringVector;
    }

    public StringVector getElements() {
        return this.m_elements;
    }

    public String getElement(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_elements) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.m_elements.elementAt(n2);
    }

    public int getElementCount() {
        return null != this.m_elements ? this.m_elements.size() : 0;
    }

    public int getXSLToken() {
        return 85;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        Object object;
        Object[] arrobject;
        Object object2;
        Object object3;
        super.compose(stylesheetRoot);
        String string = this.getPrefix();
        String string2 = this.getNamespaceForPrefix(string);
        String string3 = null;
        String string4 = null;
        String string5 = null;
        if (null == string2) {
            throw new TransformerException(XSLMessages.createMessage("ER_NO_NAMESPACE_DECL", new Object[]{string}));
        }
        for (object = this.getFirstChildElem(); object != null; object = object.getNextSiblingElem()) {
            char[] arrc;
            if (86 != object.getXSLToken()) continue;
            object3 = (ElemExtensionScript)object;
            string3 = object3.getLang();
            string4 = object3.getSrc();
            object2 = object3.getFirstChildElem();
            if (null == object2 || 78 != object2.getXSLToken() || (string5 = new String(arrc = (arrobject = (Object[])object2).getChars())).trim().length() != 0) continue;
            string5 = null;
        }
        if (null == string3) {
            string3 = "javaclass";
        }
        if (string3.equals("javaclass") && string5 != null) {
            throw new TransformerException(XSLMessages.createMessage("ER_ELEM_CONTENT_NOT_ALLOWED", new Object[]{string5}));
        }
        object = null;
        object3 = stylesheetRoot.getExtensionNamespacesManager();
        if (object3.namespaceIndex(string2, object3.getExtensions()) == -1) {
            if (string3.equals("javaclass")) {
                if (null == string4) {
                    object = object3.defineJavaNamespace(string2);
                } else if (object3.namespaceIndex(string4, object3.getExtensions()) == -1) {
                    object = object3.defineJavaNamespace(string2, string4);
                }
            } else {
                object2 = "org.apache.xalan.extensions.ExtensionHandlerGeneral";
                arrobject = new Object[]{string2, this.m_elements, this.m_functions, string3, string4, string5, this.getSystemId()};
                object = new ExtensionNamespaceSupport(string2, (String)object2, arrobject);
            }
        }
        if (object != null) {
            object3.registerExtension((ExtensionNamespaceSupport)object);
        }
    }

    public void runtimeInit(TransformerImpl transformerImpl) throws TransformerException {
    }
}

