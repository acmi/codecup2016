/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;

public class ReturnaddressType
extends Type {
    public static final ReturnaddressType NO_TARGET = new ReturnaddressType();
    private InstructionHandle returnTarget;

    private ReturnaddressType() {
        super(16, "<return address>");
    }

    public boolean equals(Object object) {
        if (!(object instanceof ReturnaddressType)) {
            return false;
        }
        return ((ReturnaddressType)object).returnTarget.equals(this.returnTarget);
    }
}

