/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BIPUSH;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.SIPUSH;
import org.apache.bcel.generic.VariableLengthInstruction;

public final class PUSH
implements CompoundInstruction,
InstructionConstants,
VariableLengthInstruction {
    private Instruction instruction;

    public PUSH(ConstantPoolGen constantPoolGen, int n2) {
        this.instruction = n2 >= -1 && n2 <= 5 ? InstructionConstants.INSTRUCTIONS[3 + n2] : (n2 >= -128 && n2 <= 127 ? new BIPUSH((byte)n2) : (n2 >= -32768 && n2 <= 32767 ? new SIPUSH((short)n2) : new LDC(constantPoolGen.addInteger(n2))));
    }

    public PUSH(ConstantPoolGen constantPoolGen, boolean bl) {
        this.instruction = InstructionConstants.INSTRUCTIONS[3 + (bl ? 1 : 0)];
    }

    public PUSH(ConstantPoolGen constantPoolGen, double d2) {
        this.instruction = d2 == 0.0 ? InstructionConstants.DCONST_0 : (d2 == 1.0 ? InstructionConstants.DCONST_1 : new LDC2_W(constantPoolGen.addDouble(d2)));
    }

    public PUSH(ConstantPoolGen constantPoolGen, String string) {
        this.instruction = string == null ? InstructionConstants.ACONST_NULL : new LDC(constantPoolGen.addString(string));
    }

    public PUSH(ConstantPoolGen constantPoolGen, Boolean bl) {
        this(constantPoolGen, (boolean)bl);
    }

    public final InstructionList getInstructionList() {
        return new InstructionList(this.instruction);
    }

    public String toString() {
        return this.instruction.toString() + " (PUSH)";
    }
}

