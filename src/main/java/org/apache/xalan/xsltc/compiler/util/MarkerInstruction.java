/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.Visitor;

abstract class MarkerInstruction
extends Instruction {
    public MarkerInstruction() {
        super(-1, 0);
    }

    public void accept(Visitor visitor) {
    }

    public final int consumeStack(ConstantPoolGen constantPoolGen) {
        return 0;
    }

    public final int produceStack(ConstantPoolGen constantPoolGen) {
        return 0;
    }

    public Instruction copy() {
        return this;
    }

    public final void dump(DataOutputStream dataOutputStream) throws IOException {
    }
}

