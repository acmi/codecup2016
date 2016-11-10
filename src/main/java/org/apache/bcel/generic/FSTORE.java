/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.StoreInstruction;
import org.apache.bcel.generic.Visitor;

public class FSTORE
extends StoreInstruction {
    FSTORE() {
        super(56, 67);
    }

    public FSTORE(int n2) {
        super(56, 67, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitFSTORE(this);
    }
}

