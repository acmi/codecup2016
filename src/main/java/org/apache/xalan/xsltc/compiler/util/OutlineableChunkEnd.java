/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.Instruction;
import org.apache.xalan.xsltc.compiler.util.MarkerInstruction;

class OutlineableChunkEnd
extends MarkerInstruction {
    public static final Instruction OUTLINEABLECHUNKEND = new OutlineableChunkEnd();
    static Class class$org$apache$xalan$xsltc$compiler$util$OutlineableChunkEnd;

    private OutlineableChunkEnd() {
    }

    public String getName() {
        Class class_ = class$org$apache$xalan$xsltc$compiler$util$OutlineableChunkEnd == null ? (OutlineableChunkEnd.class$org$apache$xalan$xsltc$compiler$util$OutlineableChunkEnd = OutlineableChunkEnd.class$("org.apache.xalan.xsltc.compiler.util.OutlineableChunkEnd")) : class$org$apache$xalan$xsltc$compiler$util$OutlineableChunkEnd;
        return class_.getName();
    }

    public String toString() {
        return this.getName();
    }

    public String toString(boolean bl) {
        return this.getName();
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

