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

public final class NamedMethodGenerator
extends MethodGenerator {
    protected static final int CURRENT_INDEX = 4;
    private static final int PARAM_START_INDEX = 5;

    public NamedMethodGenerator(int n2, Type type, Type[] arrtype, String[] arrstring, String string, String string2, InstructionList instructionList, ConstantPoolGen constantPoolGen) {
        super(n2, type, arrtype, arrstring, string, string2, instructionList, constantPoolGen);
    }

    public int getLocalIndex(String string) {
        if (string.equals("current")) {
            return 4;
        }
        return super.getLocalIndex(string);
    }

    public Instruction loadParameter(int n2) {
        return new ALOAD(n2 + 5);
    }

    public Instruction storeParameter(int n2) {
        return new ASTORE(n2 + 5);
    }
}

