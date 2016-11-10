/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.StoreInstruction;
import org.apache.bcel.generic.Visitor;

public class LSTORE
extends StoreInstruction {
    LSTORE() {
        super(55, 63);
    }

    public LSTORE(int n2) {
        super(55, 63, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitLSTORE(this);
    }
}

