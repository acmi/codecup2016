/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.Type;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

public final class RtMethodGenerator
extends MethodGenerator {
    private static final int HANDLER_INDEX = 2;
    private final Instruction _astoreHandler = new ASTORE(2);
    private final Instruction _aloadHandler = new ALOAD(2);

    public RtMethodGenerator(int n2, Type type, Type[] arrtype, String[] arrstring, String string, String string2, InstructionList instructionList, ConstantPoolGen constantPoolGen) {
        super(n2, type, arrtype, arrstring, string, string2, instructionList, constantPoolGen);
    }

    public int getIteratorIndex() {
        return -1;
    }

    public final Instruction storeHandler() {
        return this._astoreHandler;
    }

    public final Instruction loadHandler() {
        return this._aloadHandler;
    }

    public int getLocalIndex(String string) {
        return -1;
    }
}

