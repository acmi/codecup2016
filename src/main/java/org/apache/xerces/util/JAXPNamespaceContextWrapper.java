/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;

public final class JAXPNamespaceContextWrapper
implements NamespaceContext {
    private javax.xml.namespace.NamespaceContext fNamespaceContext;
    private SymbolTable fSymbolTable;
    private List fPrefixes;
    private final Vector fAllPrefixes = new Vector();
    private int[] fContext = new int[8];
    private int fCurrentContext;

    public JAXPNamespaceContextWrapper(SymbolTable symbolTable) {
        this.setSymbolTable(symbolTable);
    }

    public void setNamespaceContext(javax.xml.namespace.NamespaceContext namespaceContext) {
        this.fNamespaceContext = namespaceContext;
    }

    public javax.xml.namespace.NamespaceContext getNamespaceContext() {
        return this.fNamespaceContext;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public SymbolTable getSymbolTable() {
        return this.fSymbolTable;
    }

    public void setDeclaredPrefixes(List list) {
        this.fPrefixes = list;
    }

    public List getDeclaredPrefixes() {
        return this.fPrefixes;
    }

    public String getURI(String string) {
        String string2;
        if (this.fNamespaceContext != null && (string2 = this.fNamespaceContext.getNamespaceURI(string)) != null && !"".equals(string2)) {
            return this.fSymbolTable != null ? this.fSymbolTable.addSymbol(string2) : string2.intern();
        }
        return null;
    }

    public String getPrefix(String string) {
        if (this.fNamespaceContext != null) {
            String string2;
            if (string == null) {
                string = "";
            }
            if ((string2 = this.fNamespaceContext.getPrefix(string)) == null) {
                string2 = "";
            }
            return this.fSymbolTable != null ? this.fSymbolTable.addSymbol(string2) : string2.intern();
        }
        return null;
    }

    public Enumeration getAllPrefixes() {
        return Collections.enumeration(new TreeSet(this.fAllPrefixes));
    }

    public void pushContext() {
        if (this.fCurrentContext + 1 == this.fContext.length) {
            int[] arrn = new int[this.fContext.length * 2];
            System.arraycopy(this.fContext, 0, arrn, 0, this.fContext.length);
            this.fContext = arrn;
        }
        this.fContext[++this.fCurrentContext] = this.fAllPrefixes.size();
        if (this.fPrefixes != null) {
            this.fAllPrefixes.addAll(this.fPrefixes);
        }
    }

    public void popContext() {
        this.fAllPrefixes.setSize(this.fContext[this.fCurrentContext--]);
    }

    public boolean declarePrefix(String string, String string2) {
        return true;
    }

    public int getDeclaredPrefixCount() {
        return this.fPrefixes != null ? this.fPrefixes.size() : 0;
    }

    public String getDeclaredPrefixAt(int n2) {
        return (String)this.fPrefixes.get(n2);
    }

    public void reset() {
        this.fCurrentContext = 0;
        this.fContext[this.fCurrentContext] = 0;
        this.fAllPrefixes.clear();
    }
}

