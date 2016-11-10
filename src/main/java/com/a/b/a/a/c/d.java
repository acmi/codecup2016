/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.b.e.n;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.o;
import com.a.b.a.a.c.p;
import com.a.b.a.a.c.y;
import com.a.b.a.a.c.z;
import com.codeforces.commons.reflection.Name;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Until;
import java.util.Arrays;

public class D
extends o {
    @Until(value=1.0)
    private final long ownerPlayerId;
    @Until(value=1.0)
    private final boolean me;
    private final int mana;
    private final int maxMana;
    @Until(value=1.0)
    private final double visionRange;
    private final double castRange;
    private final int xp;
    private final int level;
    private final y[] skills;
    @Expose(serialize=0, deserialize=0)
    private final int remainingActionCooldownTicks;
    @Expose(serialize=0, deserialize=0)
    private final int[] remainingCooldownTicksByAction;
    @Expose(serialize=0, deserialize=0)
    private final boolean master;
    @Expose(serialize=0, deserialize=0)
    private final p[] messages;

    public D(@Name(value="id") long l2, @Name(value="x") double d2, @Name(value="y") double d3, @Name(value="speedX") double d4, @Name(value="speedY") double d5, @Name(value="angle") double d6, @Name(value="faction") l l3, @Name(value="radius") double d7, @Name(value="life") int n2, @Name(value="maxLife") int n3, @Name(value="statuses") z[] arrz, @Name(value="ownerPlayerId") long l4, @Name(value="me") boolean bl, @Name(value="mana") int n4, @Name(value="maxMana") int n5, @Name(value="visionRange") double d8, @Name(value="castRange") double d9, @Name(value="xp") int n6, @Name(value="level") int n7, @Name(value="skills") y[] arry, @Name(value="remainingActionCooldownTicks") int n8, @Name(value="remainingCooldownTicksByAction") int[] arrn, @Name(value="master") boolean bl2, @Name(value="messages") p[] arrp) {
        super(l2, d2, d3, d4, d5, d6, l3, d7, n2, n3, arrz);
        this.ownerPlayerId = l4;
        this.me = bl;
        this.mana = n4;
        this.maxMana = n5;
        this.visionRange = d8;
        this.castRange = d9;
        this.xp = n6;
        this.level = n7;
        this.skills = Arrays.copyOf(arry, arry.length);
        this.remainingActionCooldownTicks = n8;
        this.remainingCooldownTicksByAction = Arrays.copyOf(arrn, arrn.length);
        this.master = bl2;
        this.messages = Arrays.copyOf(arrp, arrp.length);
    }

    public long getOwnerPlayerId() {
        return this.ownerPlayerId;
    }

    public boolean isMe() {
        return this.me;
    }

    public int getMana() {
        return this.mana;
    }

    public int getMaxMana() {
        return this.maxMana;
    }

    public double getVisionRange() {
        return this.visionRange;
    }

    public double getCastRange() {
        return this.castRange;
    }

    public int getXp() {
        return this.xp;
    }

    public int getLevel() {
        return this.level;
    }

    public y[] getSkills() {
        return Arrays.copyOf(this.skills, this.skills.length);
    }

    public int getRemainingActionCooldownTicks() {
        return this.remainingActionCooldownTicks;
    }

    public int[] getRemainingCooldownTicksByAction() {
        return Arrays.copyOf(this.remainingCooldownTicksByAction, this.remainingCooldownTicksByAction.length);
    }

    public boolean isMaster() {
        return this.master;
    }

    public p[] getMessages() {
        return Arrays.copyOf(this.messages, this.messages.length);
    }

    public y[] getSkillsUnsafe() {
        return this.skills;
    }

    public static boolean areFieldEquals(D d2, D d3) {
        return d2 == d3 || d2 != null && d3 != null && o.areFieldEquals(d2, d3) && d2.ownerPlayerId == d3.ownerPlayerId && d2.me == d3.me && d2.mana == d3.mana && d2.maxMana == d3.maxMana && Double.compare(d2.visionRange, d3.visionRange) == 0 && Double.compare(d2.castRange, d3.castRange) == 0 && Arrays.equals((Object[])d2.skills, (Object[])d3.skills) && d2.remainingActionCooldownTicks == d3.remainingActionCooldownTicks && Arrays.equals(d2.remainingCooldownTicksByAction, d3.remainingCooldownTicksByAction) && d2.master == d3.master && n.a(d2.messages, d3.messages);
    }
}

