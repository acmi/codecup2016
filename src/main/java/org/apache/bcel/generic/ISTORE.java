/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.StoreInstruction;
import org.apache.bcel.generic.Visitor;

public class ISTORE
extends StoreInstruction {
    ISTORE() {
        super(54, 59);
    }

    public ISTORE(int n2) {
        super(54, 59, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitISTORE(this);
    }
}

