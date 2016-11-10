/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.PushInstruction;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Visitor;

public class DUP
extends StackInstruction
implements PushInstruction {
    public DUP() {
        super(89);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackProducer(this);
        visitor.visitPushInstruction(this);
        visitor.visitStackInstruction(this);
        visitor.visitDUP(this);
    }
}

