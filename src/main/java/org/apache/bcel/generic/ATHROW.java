/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.UnconditionalBranch;
import org.apache.bcel.generic.Visitor;

public class ATHROW
extends Instruction
implements ExceptionThrower,
UnconditionalBranch {
    public ATHROW() {
        super(191, 1);
    }

    public void accept(Visitor visitor) {
        visitor.visitUnconditionalBranch(this);
        visitor.visitExceptionThrower(this);
        visitor.visitATHROW(this);
    }
}

