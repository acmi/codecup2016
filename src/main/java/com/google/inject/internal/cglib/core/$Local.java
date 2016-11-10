/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$Type;

public class $Local {
    private $Type type;
    private int index;

    public $Local(int n2, $Type $Type) {
        this.type = $Type;
        this.index = n2;
    }

    public int getIndex() {
        return this.index;
    }

    public $Type getType() {
        return this.type;
    }
}

