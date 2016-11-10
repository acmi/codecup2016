/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.util.SymbolTable;

public final class ShadowedSymbolTable
extends SymbolTable {
    protected SymbolTable fSymbolTable;

    public ShadowedSymbolTable(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public String addSymbol(String string) {
        if (this.fSymbolTable.containsSymbol(string)) {
            return this.fSymbolTable.addSymbol(string);
        }
        return super.addSymbol(string);
    }

    public String addSymbol(char[] arrc, int n2, int n3) {
        if (this.fSymbolTable.containsSymbol(arrc, n2, n3)) {
            return this.fSymbolTable.addSymbol(arrc, n2, n3);
        }
        return super.addSymbol(arrc, n2, n3);
    }

    public int hash(String string) {
        return this.fSymbolTable.hash(string);
    }

    public int hash(char[] arrc, int n2, int n3) {
        return this.fSymbolTable.hash(arrc, n2, n3);
    }
}

