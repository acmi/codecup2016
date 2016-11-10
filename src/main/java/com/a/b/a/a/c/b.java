/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.l;
import com.a.b.a.a.c.o;
import com.a.b.a.a.c.z;
import com.codeforces.commons.reflection.Name;

public class B
extends o {
    public B(@Name(value="id") long l2, @Name(value="x") double d2, @Name(value="y") double d3, @Name(value="speedX") double d4, @Name(value="speedY") double d5, @Name(value="angle") double d6, @Name(value="faction") l l3, @Name(value="radius") double d7, @Name(value="life") int n2, @Name(value="maxLife") int n3, @Name(value="statuses") z[] arrz) {
        super(l2, d2, d3, d4, d5, d6, l3, d7, n2, n3, arrz);
    }

    public boolean equals(Object object) {
        return this == object || object instanceof B && B.areFieldEquals(this, (B)object);
    }

    public static boolean areFieldEquals(B b2, B b3) {
        return b2 == b3 || b2 != null && b3 != null && o.areFieldEquals(b2, b3);
    }
}

