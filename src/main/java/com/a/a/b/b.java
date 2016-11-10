/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b;

import com.a.a.b.d;
import com.codeforces.commons.geometry.Vector2D;

public class b
extends d {
    private Vector2D a;
    private Vector2D b;
    private Vector2D c;
    private double d;
    private double e;
    private double f;

    public b() {
        this.a = new Vector2D(0.0, 0.0);
        this.b = new Vector2D(0.0, 0.0);
        this.c = new Vector2D(0.0, 0.0);
    }

    public b(b b2) {
        super(b2);
        this.a = b2.a.copy();
        this.c = b2.c.copy();
        this.d = b2.d;
        this.f = b2.f;
    }

    public Vector2D a() {
        return this.a;
    }

    public void a(Vector2D vector2D) {
        this.a = vector2D.copy();
    }

    public Vector2D b() {
        return this.b;
    }

    public void b(Vector2D vector2D) {
        this.b = vector2D;
    }

    public Vector2D c() {
        return this.c;
    }

    public void c(Vector2D vector2D) {
        this.c = vector2D.copy();
    }

    public double d() {
        return this.d;
    }

    public void a(double d2) {
        this.d = d2;
    }

    public double e() {
        return this.e;
    }

    public void b(double d2) {
        this.e = d2;
    }

    public double f() {
        return this.f;
    }

    public void c(double d2) {
        this.f = d2;
    }
}

