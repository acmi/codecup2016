/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.f;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.w;
import com.codeforces.commons.reflection.Name;
import com.google.gson.annotations.Until;

public class v
extends f {
    @Until(value=1.0)
    private final w type;
    @Until(value=1.0)
    private final long ownerUnitId;
    @Until(value=1.0)
    private final long ownerPlayerId;

    public v(@Name(value="id") long l2, @Name(value="x") double d2, @Name(value="y") double d3, @Name(value="speedX") double d4, @Name(value="speedY") double d5, @Name(value="angle") double d6, @Name(value="faction") l l3, @Name(value="radius") double d7, @Name(value="type") w w2, @Name(value="ownerUnitId") long l4, @Name(value="ownerPlayerId") long l5) {
        super(l2, d2, d3, d4, d5, d6, l3, d7);
        this.type = w2;
        this.ownerUnitId = l4;
        this.ownerPlayerId = l5;
    }

    public w getType() {
        return this.type;
    }

    public long getOwnerUnitId() {
        return this.ownerUnitId;
    }

    public long getOwnerPlayerId() {
        return this.ownerPlayerId;
    }

    public static boolean areFieldEquals(v v2, v v3) {
        return v2 == v3 || v2 != null && v3 != null && f.areFieldEquals(v2, v3) && v2.type == v3.type && v2.ownerUnitId == v3.ownerUnitId && v2.ownerPlayerId == v3.ownerPlayerId;
    }
}

