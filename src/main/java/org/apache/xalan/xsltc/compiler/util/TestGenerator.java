/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.Type;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

public final class TestGenerator
extends MethodGenerator {
    private static int CONTEXT_NODE_INDEX = 1;
    private static int CURRENT_NODE_INDEX = 4;
    private static int ITERATOR_INDEX = 6;
    private Instruction _aloadDom;
    private final Instruction _iloadCurrent = new ILOAD(CURRENT_NODE_INDEX);
    private final Instruction _iloadContext = new ILOAD(CONTEXT_NODE_INDEX);
    private final Instruction _istoreCurrent = new ISTORE(CURRENT_NODE_INDEX);
    private final Instruction _istoreContext = new ILOAD(CONTEXT_NODE_INDEX);
    private final Instruction _astoreIterator = new ASTORE(ITERATOR_INDEX);
    private final Instruction _aloadIterator = new ALOAD(ITERATOR_INDEX);

    public TestGenerator(int n2, Type type, Type[] arrtype, String[] arrstring, String string, String string2, InstructionList instructionList, ConstantPoolGen constantPoolGen) {
        super(n2, type, arrtype, arrstring, string, string2, instructionList, constantPoolGen);
    }

    public int getHandlerIndex() {
        return -1;
    }

    public int getIteratorIndex() {
        return ITERATOR_INDEX;
    }

    public void setDomIndex(int n2) {
        this._aloadDom = new ALOAD(n2);
    }

    public Instruction loadDOM() {
        return this._aloadDom;
    }

    public Instruction loadCurrentNode() {
        return this._iloadCurrent;
    }

    public Instruction loadContextNode() {
        return this._iloadContext;
    }

    public Instruction storeContextNode() {
        return this._istoreContext;
    }

    public Instruction storeCurrentNode() {
        return this._istoreCurrent;
    }

    public Instruction storeIterator() {
        return this._astoreIterator;
    }

    public Instruction loadIterator() {
        return this._aloadIterator;
    }

    public int getLocalIndex(String string) {
        if (string.equals("current")) {
            return CURRENT_NODE_INDEX;
        }
        return super.getLocalIndex(string);
    }
}

