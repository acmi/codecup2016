/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.c.y;

public final class a {
    public static int a(com.a.b.a.a.b.d.f.a a2, com.a.b.a.a.c.a a3) {
        switch (a3) {
            case STAFF: {
                return 60;
            }
            case MAGIC_MISSILE: {
                return a2.a(y.ADVANCED_MAGIC_MISSILE) ? 0 : 60;
            }
            case FROST_BOLT: {
                return 90;
            }
            case FIREBALL: {
                return 120;
            }
            case HASTE: {
                return 120;
            }
            case SHIELD: {
                return 120;
            }
        }
        throw new IllegalArgumentException("Unsupported action type: " + (Object)((Object)a3) + '.');
    }
}

