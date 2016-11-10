/*
 * Decompiled with CFR 0_119.
 */
package com.a.c;

import com.a.c.c;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.reflection.Name;

public final class f
extends c {
    @Name(value="x")
    private double a;
    @Name(value="y")
    private double b;
    @Name(value="angle")
    private double c;
    @Name(value="speed")
    private Vector2D d = new Vector2D(0.0, 0.0);
    @Name(value="medianSpeed")
    private Vector2D e = new Vector2D(0.0, 0.0);
    @Name(value="angularSpeed")
    private double f;
    @Name(value="angularSpeed")
    private double g;
    @Name(value="force")
    private Vector2D h = new Vector2D(0.0, 0.0);
    @Name(value="torque")
    private double i;
    @Name(value="movementAirFrictionFactor")
    private double j;
    @Name(value="rotationAirFrictionFactor")
    private double k;
    @Name(value="movementFrictionFactor")
    private double l;
    @Name(value="crosswiseMovementFrictionFactor")
    private Double m;
    @Name(value="rotationFrictionFactor")
    private double n;
    @Name(value="momentumTransferFactor")
    private double o = 1.0;
    @Name(value="surfaceFriction")
    private double p;

    public f(long l2) {
        super(l2);
    }

    @Override
    public double c() {
        return this.a;
    }

    @Override
    public void a(double d2) {
        this.a = d2;
    }

    @Override
    public double d() {
        return this.b;
    }

    @Override
    public void b(double d2) {
        this.b = d2;
    }

    @Override
    public double e() {
        return this.c;
    }

    @Override
    public void c(double d2) {
        while (d2 > 3.141592653589793) {
            d2 -= 6.283185307179586;
        }
        while (d2 < -3.141592653589793) {
            d2 += 6.283185307179586;
        }
        this.c = d2;
    }

    @Override
    public Vector2D f() {
        return this.d;
    }

    @Override
    public void a(Vector2D vector2D) {
        this.d = vector2D;
    }

    @Override
    public Vector2D g() {
        return this.e;
    }

    @Override
    public double h() {
        return this.f;
    }

    @Override
    public double i() {
        return this.g;
    }

    @Override
    public Vector2D j() {
        return this.h;
    }

    @Override
    public double k() {
        return this.i;
    }

    @Override
    public double l() {
        return this.j;
    }

    @Override
    public double m() {
        return this.k;
    }

    @Override
    public double n() {
        return this.l;
    }

    @Override
    public Double o() {
        return this.m;
    }

    @Override
    public double p() {
        return this.n;
    }

    @Override
    public double q() {
        return this.o;
    }

    @Override
    public double r() {
        return this.p;
    }
}

