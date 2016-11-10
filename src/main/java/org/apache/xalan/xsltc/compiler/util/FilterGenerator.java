/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.Instruction;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;

public final class FilterGenerator
extends ClassGenerator {
    private static int TRANSLET_INDEX = 5;
    private final Instruction _aloadTranslet = new ALOAD(TRANSLET_INDEX);

    public FilterGenerator(String string, String string2, String string3, int n2, String[] arrstring, Stylesheet stylesheet) {
        super(string, string2, string3, n2, arrstring, stylesheet);
    }

    public final Instruction loadTranslet() {
        return this._aloadTranslet;
    }

    public boolean isExternal() {
        return true;
    }
}

