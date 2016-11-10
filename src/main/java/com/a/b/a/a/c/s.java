/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.c;

import com.a.b.a.a.c.a;
import com.a.b.a.a.c.p;
import com.a.b.a.a.c.y;
import java.util.Arrays;

public class s {
    private double speed;
    private double strafeSpeed;
    private double turn;
    private a action;
    private double castAngle;
    private double minCastDistance;
    private double maxCastDistance = 10000.0;
    private long statusTargetId = -1;
    private y skillToLearn;
    private p[] messages;

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double d2) {
        this.speed = d2;
    }

    public double getStrafeSpeed() {
        return this.strafeSpeed;
    }

    public void setStrafeSpeed(double d2) {
        this.strafeSpeed = d2;
    }

    public double getTurn() {
        return this.turn;
    }

    public void setTurn(double d2) {
        this.turn = d2;
    }

    public a getAction() {
        return this.action;
    }

    public void setAction(a a2) {
        this.action = a2;
    }

    public double getCastAngle() {
        return this.castAngle;
    }

    public void setCastAngle(double d2) {
        this.castAngle = d2;
    }

    public double getMinCastDistance() {
        return this.minCastDistance;
    }

    public void setMinCastDistance(double d2) {
        this.minCastDistance = d2;
    }

    public double getMaxCastDistance() {
        return this.maxCastDistance;
    }

    public void setMaxCastDistance(double d2) {
        this.maxCastDistance = d2;
    }

    public long getStatusTargetId() {
        return this.statusTargetId;
    }

    public void setStatusTargetId(long l2) {
        this.statusTargetId = l2;
    }

    public y getSkillToLearn() {
        return this.skillToLearn;
    }

    public void setSkillToLearn(y y2) {
        this.skillToLearn = y2;
    }

    public p[] getMessages() {
        return this.messages == null ? null : Arrays.copyOf(this.messages, this.messages.length);
    }

    public void setMessages(p[] arrp) {
        this.messages = arrp == null ? null : Arrays.copyOf(arrp, arrp.length);
    }
}

