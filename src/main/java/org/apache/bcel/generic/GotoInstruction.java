/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.UnconditionalBranch;

public abstract class GotoInstruction
extends BranchInstruction
implements UnconditionalBranch {
    GotoInstruction(short s2, InstructionHandle instructionHandle) {
        super(s2, instructionHandle);
    }

    GotoInstruction() {
    }
}

