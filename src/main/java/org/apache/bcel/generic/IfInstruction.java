/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.StackConsumer;

public abstract class IfInstruction
extends BranchInstruction
implements StackConsumer {
    IfInstruction() {
    }

    protected IfInstruction(short s2, InstructionHandle instructionHandle) {
        super(s2, instructionHandle);
    }

    public abstract IfInstruction negate();
}

