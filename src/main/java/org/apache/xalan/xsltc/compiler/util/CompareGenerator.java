/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.Type;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Util;

public final class CompareGenerator
extends MethodGenerator {
    private static int DOM_INDEX = 1;
    private static int CURRENT_INDEX = 2;
    private static int LEVEL_INDEX = 3;
    private static int TRANSLET_INDEX = 4;
    private static int LAST_INDEX = 5;
    private int ITERATOR_INDEX = 6;
    private final Instruction _iloadCurrent = new ILOAD(CURRENT_INDEX);
    private final Instruction _istoreCurrent = new ISTORE(CURRENT_INDEX);
    private final Instruction _aloadDom = new ALOAD(DOM_INDEX);
    private final Instruction _iloadLast = new ILOAD(LAST_INDEX);
    private final Instruction _aloadIterator;
    private final Instruction _astoreIterator;

    public CompareGenerator(int n2, Type type, Type[] arrtype, String[] arrstring, String string, String string2, InstructionList instructionList, ConstantPoolGen constantPoolGen) {
        super(n2, type, arrtype, arrstring, string, string2, instructionList, constantPoolGen);
        LocalVariableGen localVariableGen = this.addLocalVariable("iterator", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        this.ITERATOR_INDEX = localVariableGen.getIndex();
        this._aloadIterator = new ALOAD(this.ITERATOR_INDEX);
        this._astoreIterator = new ASTORE(this.ITERATOR_INDEX);
        instructionList.append(new ACONST_NULL());
        instructionList.append(this.storeIterator());
    }

    public Instruction loadLastNode() {
        return this._iloadLast;
    }

    public Instruction loadCurrentNode() {
        return this._iloadCurrent;
    }

    public Instruction storeCurrentNode() {
        return this._istoreCurrent;
    }

    public Instruction loadDOM() {
        return this._aloadDom;
    }

    public int getHandlerIndex() {
        return -1;
    }

    public int getIteratorIndex() {
        return -1;
    }

    public Instruction storeIterator() {
        return this._astoreIterator;
    }

    public Instruction loadIterator() {
        return this._aloadIterator;
    }

    public int getLocalIndex(String string) {
        if (string.equals("current")) {
            return CURRENT_INDEX;
        }
        return super.getLocalIndex(string);
    }
}

