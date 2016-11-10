/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.Instruction;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;

public final class NodeCounterGenerator
extends ClassGenerator {
    private Instruction _aloadTranslet;

    public NodeCounterGenerator(String string, String string2, String string3, int n2, String[] arrstring, Stylesheet stylesheet) {
        super(string, string2, string3, n2, arrstring, stylesheet);
    }

    public void setTransletIndex(int n2) {
        this._aloadTranslet = new ALOAD(n2);
    }

    public Instruction loadTranslet() {
        return this._aloadTranslet;
    }

    public boolean isExternal() {
        return true;
    }
}

