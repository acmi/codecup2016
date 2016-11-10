/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.n;
import com.a.b.a.a.c.y;
import com.codeforces.commons.reflection.Name;
import java.util.Arrays;

public class p {
    private final n lane;
    private final y skillToLearn;
    private final byte[] rawMessage;

    public p(@Name(value="lane") n n2, @Name(value="skillToLearn") y y2, @Name(value="rawMessage") byte[] arrby) {
        this.lane = n2;
        this.skillToLearn = y2;
        this.rawMessage = Arrays.copyOf(arrby, arrby.length);
    }

    public n getLane() {
        return this.lane;
    }

    public y getSkillToLearn() {
        return this.skillToLearn;
    }

    public byte[] getRawMessage() {
        return Arrays.copyOf(this.rawMessage, this.rawMessage.length);
    }

    public static boolean areFieldEquals(p p2, p p3) {
        return p2 == p3 || p2 != null && p3 != null && p2.lane == p3.lane && p2.skillToLearn == p3.skillToLearn && Arrays.equals(p2.rawMessage, p3.rawMessage);
    }
}

