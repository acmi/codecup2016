/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.StoreInstruction;
import org.apache.bcel.generic.Visitor;

public class ASTORE
extends StoreInstruction {
    ASTORE() {
        super(58, 75);
    }

    public ASTORE(int n2) {
        super(58, 75, n2);
    }

    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitASTORE(this);
    }
}

