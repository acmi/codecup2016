/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.e.f;
import com.a.b.a.a.c.C;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.i;
import com.a.b.a.a.c.t;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

class h
implements f.a {
    final /* synthetic */ i a;
    final /* synthetic */ Map b;
    final /* synthetic */ Map c;
    final /* synthetic */ f d;

    h(f f2, i i2, Map map, Map map2) {
        this.d = f2;
        this.a = i2;
        this.b = map;
        this.c = map2;
    }

    @Override
    public boolean a(Object object, Field field, StringBuilder stringBuilder, boolean bl) {
        C c2;
        C c3;
        if ("id".equals(field.getName())) {
            return false;
        }
        Class class_ = object.getClass();
        if (E.class.isInstance(object)) {
            return this.a(this.a, f.b(this.d), field);
        }
        if (class_ == t.class) {
            t t2 = (t)object;
            t t3 = (t)this.b.get(t2.getId());
            if (t3 != null) {
                return this.a(t2, t3, field);
            }
        } else if (C.class.isAssignableFrom(class_) && (c2 = (C)this.c.get((c3 = (C)object).getId())) != null) {
            return this.a(c3, c2, field);
        }
        return false;
    }

    private boolean a(Object object, Object object2, Field field) {
        try {
            return Objects.deepEquals(field.get(object), field.get(object2));
        }
        catch (IllegalAccessException illegalAccessException) {
            return false;
        }
    }
}

