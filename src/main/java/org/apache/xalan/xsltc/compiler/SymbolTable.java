/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xalan.xsltc.compiler.AttributeSet;
import org.apache.xalan.xsltc.compiler.DecimalFormatting;
import org.apache.xalan.xsltc.compiler.Key;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.util.MethodType;

final class SymbolTable {
    private final Hashtable _stylesheets = new Hashtable();
    private final Hashtable _primops = new Hashtable();
    private Hashtable _variables = null;
    private Hashtable _templates = null;
    private Hashtable _attributeSets = null;
    private Hashtable _aliases = null;
    private Hashtable _excludedURI = null;
    private Stack _excludedURIStack = null;
    private Hashtable _decimalFormats = null;
    private Hashtable _keys = null;
    private int _nsCounter = 0;
    private SyntaxTreeNode _current = null;

    SymbolTable() {
    }

    public DecimalFormatting getDecimalFormatting(QName qName) {
        if (this._decimalFormats == null) {
            return null;
        }
        return (DecimalFormatting)this._decimalFormats.get(qName);
    }

    public void addDecimalFormatting(QName qName, DecimalFormatting decimalFormatting) {
        if (this._decimalFormats == null) {
            this._decimalFormats = new Hashtable();
        }
        this._decimalFormats.put(qName, decimalFormatting);
    }

    public Key getKey(QName qName) {
        if (this._keys == null) {
            return null;
        }
        return (Key)this._keys.get(qName);
    }

    public void addKey(QName qName, Key key) {
        if (this._keys == null) {
            this._keys = new Hashtable();
        }
        this._keys.put(qName, key);
    }

    public Stylesheet addStylesheet(QName qName, Stylesheet stylesheet) {
        return this._stylesheets.put(qName, stylesheet);
    }

    public Stylesheet lookupStylesheet(QName qName) {
        return (Stylesheet)this._stylesheets.get(qName);
    }

    public Template addTemplate(Template template) {
        QName qName = template.getName();
        if (this._templates == null) {
            this._templates = new Hashtable();
        }
        return this._templates.put(qName, template);
    }

    public Template lookupTemplate(QName qName) {
        if (this._templates == null) {
            return null;
        }
        return (Template)this._templates.get(qName);
    }

    public Variable addVariable(Variable variable) {
        if (this._variables == null) {
            this._variables = new Hashtable();
        }
        String string = variable.getName().getStringRep();
        return this._variables.put(string, variable);
    }

    public Param addParam(Param param) {
        if (this._variables == null) {
            this._variables = new Hashtable();
        }
        String string = param.getName().getStringRep();
        return this._variables.put(string, param);
    }

    public Variable lookupVariable(QName qName) {
        if (this._variables == null) {
            return null;
        }
        String string = qName.getStringRep();
        Object v2 = this._variables.get(string);
        return v2 instanceof Variable ? (Variable)v2 : null;
    }

    public Param lookupParam(QName qName) {
        if (this._variables == null) {
            return null;
        }
        String string = qName.getStringRep();
        Object v2 = this._variables.get(string);
        return v2 instanceof Param ? (Param)v2 : null;
    }

    public SyntaxTreeNode lookupName(QName qName) {
        if (this._variables == null) {
            return null;
        }
        String string = qName.getStringRep();
        return (SyntaxTreeNode)this._variables.get(string);
    }

    public AttributeSet addAttributeSet(AttributeSet attributeSet) {
        if (this._attributeSets == null) {
            this._attributeSets = new Hashtable();
        }
        return this._attributeSets.put(attributeSet.getName(), attributeSet);
    }

    public AttributeSet lookupAttributeSet(QName qName) {
        if (this._attributeSets == null) {
            return null;
        }
        return (AttributeSet)this._attributeSets.get(qName);
    }

    public void addPrimop(String string, MethodType methodType) {
        Vector<MethodType> vector = (Vector<MethodType>)this._primops.get(string);
        if (vector == null) {
            vector = new Vector<MethodType>();
            this._primops.put(string, vector);
        }
        vector.addElement(methodType);
    }

    public Vector lookupPrimop(String string) {
        return (Vector)this._primops.get(string);
    }

    public String generateNamespacePrefix() {
        return "ns" + this._nsCounter++;
    }

    public void setCurrentNode(SyntaxTreeNode syntaxTreeNode) {
        this._current = syntaxTreeNode;
    }

    public String lookupNamespace(String string) {
        if (this._current == null) {
            return "";
        }
        return this._current.lookupNamespace(string);
    }

    public void addPrefixAlias(String string, String string2) {
        if (this._aliases == null) {
            this._aliases = new Hashtable();
        }
        this._aliases.put(string, string2);
    }

    public String lookupPrefixAlias(String string) {
        if (this._aliases == null) {
            return null;
        }
        return (String)this._aliases.get(string);
    }

    public void excludeURI(String string) {
        Integer n2;
        if (string == null) {
            return;
        }
        if (this._excludedURI == null) {
            this._excludedURI = new Hashtable();
        }
        n2 = (n2 = (Integer)this._excludedURI.get(string)) == null ? new Integer(1) : new Integer(n2 + 1);
        this._excludedURI.put(string, n2);
    }

    public void excludeNamespaces(String string) {
        if (string != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(string);
            while (stringTokenizer.hasMoreTokens()) {
                String string2 = stringTokenizer.nextToken();
                String string3 = string2.equals("#default") ? this.lookupNamespace("") : this.lookupNamespace(string2);
                if (string3 == null) continue;
                this.excludeURI(string3);
            }
        }
    }

    public boolean isExcludedNamespace(String string) {
        if (string != null && this._excludedURI != null) {
            Integer n2 = (Integer)this._excludedURI.get(string);
            return n2 != null && n2 > 0;
        }
        return false;
    }

    public void unExcludeNamespaces(String string) {
        if (this._excludedURI == null) {
            return;
        }
        if (string != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(string);
            while (stringTokenizer.hasMoreTokens()) {
                String string2 = stringTokenizer.nextToken();
                String string3 = string2.equals("#default") ? this.lookupNamespace("") : this.lookupNamespace(string2);
                Integer n2 = (Integer)this._excludedURI.get(string3);
                if (n2 == null) continue;
                this._excludedURI.put(string3, new Integer(n2 - 1));
            }
        }
    }

    public void pushExcludedNamespacesContext() {
        if (this._excludedURIStack == null) {
            this._excludedURIStack = new Stack();
        }
        this._excludedURIStack.push(this._excludedURI);
        this._excludedURI = null;
    }

    public void popExcludedNamespacesContext() {
        this._excludedURI = (Hashtable)this._excludedURIStack.pop();
        if (this._excludedURIStack.isEmpty()) {
            this._excludedURIStack = null;
        }
    }
}

