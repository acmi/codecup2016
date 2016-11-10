/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.e.f;
import com.codeforces.commons.math.Math;
import com.google.common.base.Preconditions;
import java.lang.reflect.Field;

class g
implements f.b {
    final /* synthetic */ f a;

    g(f f2) {
        this.a = f2;
    }

    @Override
    public boolean a(Object object, Field field) throws IllegalAccessException {
        Class class_ = field.getType();
        if (class_ != Double.TYPE) {
            return false;
        }
        double d2 = field.getDouble(object);
        return !Double.isNaN(d2) && !Double.isInfinite(d2);
    }

    @Override
    public void a(Object object, Field field, StringBuilder stringBuilder) throws IllegalAccessException {
        String string = field.getName();
        double d2 = field.getDouble(object);
        switch (string) {
            case "width": 
            case "height": 
            case "attackRange": 
            case "castRange": 
            case "visionRange": 
            case "damage": 
            case "speedFactor": {
                this.a(d2, stringBuilder);
                break;
            }
            case "x": 
            case "y": {
                this.b(d2, stringBuilder);
                break;
            }
            case "speedX": 
            case "speedY": 
            case "radius": {
                this.c(d2, stringBuilder);
                break;
            }
            case "angle": {
                stringBuilder.append((double)Math.round(d2 * 100.0) / 100.0);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported double field name: '" + string + "'.");
            }
        }
    }

    private void a(double d2, StringBuilder stringBuilder) {
        long l2 = Math.round(d2);
        Preconditions.checkArgument(d2 == (double)l2);
        stringBuilder.append(l2);
    }

    private void b(double d2, StringBuilder stringBuilder) {
        stringBuilder.append(Math.round(d2));
    }

    private void c(double d2, StringBuilder stringBuilder) {
        long l2 = Math.round(d2);
        if (d2 == (double)l2) {
            stringBuilder.append(l2);
        } else {
            double d3 = (double)Math.round(d2 * f.a(this.a)) / f.a(this.a);
            long l3 = (long)d3;
            if ((double)l3 == d3) {
                stringBuilder.append(l3);
            } else {
                stringBuilder.append(d3);
            }
        }
    }
}

