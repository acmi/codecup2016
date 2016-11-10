/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.Instruction;
import org.apache.xalan.xsltc.compiler.util.MarkerInstruction;

class OutlineableChunkStart
extends MarkerInstruction {
    public static final Instruction OUTLINEABLECHUNKSTART = new OutlineableChunkStart();
    static Class class$org$apache$xalan$xsltc$compiler$util$OutlineableChunkStart;

    private OutlineableChunkStart() {
    }

    public String getName() {
        Class class_ = class$org$apache$xalan$xsltc$compiler$util$OutlineableChunkStart == null ? (OutlineableChunkStart.class$org$apache$xalan$xsltc$compiler$util$OutlineableChunkStart = OutlineableChunkStart.class$("org.apache.xalan.xsltc.compiler.util.OutlineableChunkStart")) : class$org$apache$xalan$xsltc$compiler$util$OutlineableChunkStart;
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

