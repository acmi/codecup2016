/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.l;
import com.codeforces.commons.reflection.Name;
import com.codeforces.commons.text.StringUtil;
import com.google.gson.annotations.Until;

public class t {
    private final long id;
    @Until(value=1.0)
    private final boolean me;
    @Until(value=1.0)
    private final String name;
    private final boolean strategyCrashed;
    private final int score;
    @Until(value=1.0)
    private final l faction;

    public t(@Name(value="id") long l2, @Name(value="me") boolean bl, @Name(value="name") String string, @Name(value="strategyCrashed") boolean bl2, @Name(value="score") int n2, @Name(value="faction") l l3) {
        this.id = l2;
        this.me = bl;
        this.name = string;
        this.strategyCrashed = bl2;
        this.score = n2;
        this.faction = l3;
    }

    public long getId() {
        return this.id;
    }

    public boolean isMe() {
        return this.me;
    }

    public String getName() {
        return this.name;
    }

    public boolean isStrategyCrashed() {
        return this.strategyCrashed;
    }

    public int getScore() {
        return this.score;
    }

    public l getFaction() {
        return this.faction;
    }

    public boolean equals(Object object) {
        return this == object || object instanceof t && t.areFieldEquals(this, (t)object);
    }

    public static boolean areFieldEquals(t t2, t t3) {
        return t2 == t3 || t2 != null && t3 != null && t2.id == t3.id && t2.me == t3.me && StringUtil.equals(t2.name, t3.name) && t2.strategyCrashed == t3.strategyCrashed && t2.score == t3.score && t2.faction == t3.faction;
    }
}

