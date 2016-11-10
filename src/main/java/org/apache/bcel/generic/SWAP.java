/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Visitor;

public class SWAP
extends StackInstruction
implements StackConsumer,
StackProducer {
    public SWAP() {
        super(95);
    }

    public void accept(Visitor visitor) {
        visitor.visitStackConsumer(this);
        visitor.visitStackProducer(this);
        visitor.visitStackInstruction(this);
        visitor.visitSWAP(this);
    }
}

