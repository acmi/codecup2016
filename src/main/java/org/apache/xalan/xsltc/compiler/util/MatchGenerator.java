/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.Type;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

public final class MatchGenerator
extends MethodGenerator {
    private static int CURRENT_INDEX = 1;
    private int _iteratorIndex = -1;
    private final Instruction _iloadCurrent = new ILOAD(CURRENT_INDEX);
    private final Instruction _istoreCurrent = new ISTORE(CURRENT_INDEX);
    private Instruction _aloadDom;

    public MatchGenerator(int n2, Type type, Type[] arrtype, String[] arrstring, String string, String string2, InstructionList instructionList, ConstantPoolGen constantPoolGen) {
        super(n2, type, arrtype, arrstring, string, string2, instructionList, constantPoolGen);
    }

    public Instruction loadCurrentNode() {
        return this._iloadCurrent;
    }

    public Instruction storeCurrentNode() {
        return this._istoreCurrent;
    }

    public int getHandlerIndex() {
        return -1;
    }

    public Instruction loadDOM() {
        return this._aloadDom;
    }

    public void setDomIndex(int n2) {
        this._aloadDom = new ALOAD(n2);
    }

    public int getIteratorIndex() {
        return this._iteratorIndex;
    }

    public void setIteratorIndex(int n2) {
        this._iteratorIndex = n2;
    }

    public int getLocalIndex(String string) {
        if (string.equals("current")) {
            return CURRENT_INDEX;
        }
        return super.getLocalIndex(string);
    }
}

