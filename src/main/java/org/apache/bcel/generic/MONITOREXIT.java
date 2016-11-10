/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Visitor;

public class MONITOREXIT
extends Instruction
implements ExceptionThrower,
StackConsumer {
    public MONITOREXIT() {
        super(195, 1);
    }

    public void accept(Visitor visitor) {
        visitor.visitExceptionThrower(this);
        visitor.visitStackConsumer(this);
        visitor.visitMONITOREXIT(this);
    }
}

