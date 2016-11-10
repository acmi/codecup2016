/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.util.SymbolTable;

public final class SynchronizedSymbolTable
extends SymbolTable {
    protected SymbolTable fSymbolTable;

    public SynchronizedSymbolTable(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public SynchronizedSymbolTable() {
        this.fSymbolTable = new SymbolTable();
    }

    public SynchronizedSymbolTable(int n2) {
        this.fSymbolTable = new SymbolTable(n2);
    }

    public String addSymbol(String string) {
        SymbolTable symbolTable = this.fSymbolTable;
        synchronized (symbolTable) {
            String string2 = this.fSymbolTable.addSymbol(string);
            return string2;
        }
    }

    public String addSymbol(char[] arrc, int n2, int n3) {
        SymbolTable symbolTable = this.fSymbolTable;
        synchronized (symbolTable) {
            String string = this.fSymbolTable.addSymbol(arrc, n2, n3);
            return string;
        }
    }

    public boolean containsSymbol(String string) {
        SymbolTable symbolTable = this.fSymbolTable;
        synchronized (symbolTable) {
            boolean bl = this.fSymbolTable.containsSymbol(string);
            return bl;
        }
    }

    public boolean containsSymbol(char[] arrc, int n2, int n3) {
        SymbolTable symbolTable = this.fSymbolTable;
        synchronized (symbolTable) {
            boolean bl = this.fSymbolTable.containsSymbol(arrc, n2, n3);
            return bl;
        }
    }
}

