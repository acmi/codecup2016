/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.cglib.core.$Predicate;
import java.lang.reflect.Member;

public class $RejectModifierPredicate
implements $Predicate {
    private int rejectMask;

    public $RejectModifierPredicate(int n2) {
        this.rejectMask = n2;
    }

    public boolean evaluate(Object object) {
        return (((Member)object).getModifiers() & this.rejectMask) == 0;
    }
}

