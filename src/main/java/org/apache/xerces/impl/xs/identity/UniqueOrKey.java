/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xs.identity.IdentityConstraint;

public class UniqueOrKey
extends IdentityConstraint {
    public UniqueOrKey(String string, String string2, String string3, short s2) {
        super(string, string2, string3);
        this.type = s2;
    }
}

