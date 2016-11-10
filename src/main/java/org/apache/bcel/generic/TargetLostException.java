/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.InstructionHandle;

public final class TargetLostException
extends Exception {
    private InstructionHandle[] targets;

    TargetLostException(InstructionHandle[] arrinstructionHandle, String string) {
        super(string);
        this.targets = arrinstructionHandle;
    }

    public InstructionHandle[] getTargets() {
        return this.targets;
    }
}

