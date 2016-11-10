/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import org.apache.xalan.extensions.ExtensionNamespacesManager;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.functions.FuncExtFunctionAvailable;
import org.apache.xpath.functions.Function;

public class ExpressionVisitor
extends XPathVisitor {
    private StylesheetRoot m_sroot;

    public ExpressionVisitor(StylesheetRoot stylesheetRoot) {
        this.m_sroot = stylesheetRoot;
    }

    public boolean visitFunction(ExpressionOwner expressionOwner, Function function) {
        String string;
        if (function instanceof FuncExtFunction) {
            String string2 = ((FuncExtFunction)function).getNamespace();
            this.m_sroot.getExtensionNamespacesManager().registerExtension(string2);
        } else if (function instanceof FuncExtFunctionAvailable && (string = ((FuncExtFunctionAvailable)function).getArg0().toString()).indexOf(":") > 0) {
            String string3 = string.substring(0, string.indexOf(":"));
            String string4 = this.m_sroot.getNamespaceForPrefix(string3);
            this.m_sroot.getExtensionNamespacesManager().registerExtension(string4);
        }
        return true;
    }
}

