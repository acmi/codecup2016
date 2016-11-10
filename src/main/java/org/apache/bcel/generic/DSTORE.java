/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.StoreInstruction;
import org.apache.bcel.generic.Visitor;

public class DSTORE
extends StoreInstruction {
    DSTORE() {
        super(57, 71);
    }

    public DSTORE(int n2) {
        super(57, 71, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitDSTORE(this);
    }
}

