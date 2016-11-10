/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.f;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.t;
import com.a.b.a.a.d.j;
import com.a.b.a.a.e.a.a;
import com.a.b.a.a.e.a.a.g;
import com.a.b.a.a.e.a.b;
import com.a.b.a.a.e.a.c;
import com.a.b.a.a.e.a.d;
import com.a.b.a.a.e.a.e;
import com.codeforces.commons.text.StringUtil;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class q {
    private static final Logger a = LoggerFactory.getLogger(q.class);

    private q() {
        throw new UnsupportedOperationException();
    }

    public static f a(com.a.b.a.a.a.b b2, int n2, String string, String string2, int n3, List<j> list) {
        q.a(string, string2);
        l l2 = n2 < 5 ? l.ACADEMY : l.RENEGADES;
        boolean bl = l2 == l.ACADEMY && (long)n2 + 1 == b2.t() || l2 == l.RENEGADES && (long)n2 + 1 == b2.u();
        try {
            if (string2.endsWith(".class")) {
                return new f(string, new a(string2, n3), l2, bl);
            }
            if ("#KeyboardPlayer".equals(string2)) {
                for (j j2 : list) {
                    if (!(j2 instanceof com.a.b.a.a.d.a)) continue;
                    return new f(string, new c(n3, ((com.a.b.a.a.d.a)j2).a()), l2, bl);
                }
            } else {
                if ("#LocalTestPlayer".equals(string2)) {
                    d d2 = d.b(b2, n2, string2, n3, bl);
                    return q.a(b2, n2, string, d2);
                }
                Preconditions.checkNotNull(g.a(string2));
                d d3 = d.a(b2, n2, string2, n3, bl);
                return q.a(b2, n2, string, d3);
            }
            String string3 = String.format("Unsupported player definition: '%s'.", string2);
            a.error(string3);
            throw new IllegalArgumentException(string3);
        }
        catch (RuntimeException runtimeException) {
            String string4 = String.format("Can't load player defined by '%s'.", string2);
            a.error(string4, runtimeException);
            f f2 = new f(string, new b(n3), l2, bl);
            f2.a("\u041f\u0440\u0438 \u0438\u043d\u0438\u0446\u0438\u0430\u043b\u0438\u0437\u0430\u0446\u0438\u0438 \u0438\u0433\u0440\u043e\u043a\u0430 \u0432\u043e\u0437\u043d\u0438\u043a\u043b\u043e \u043d\u0435\u043f\u0440\u0435\u0434\u0432\u0438\u0434\u0435\u043d\u043d\u043e\u0435 \u0438\u0441\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435.");
            return f2;
        }
    }

    private static f a(com.a.b.a.a.a.b b2, int n2, String string, d d2) {
        l l2 = n2 < 5 ? l.ACADEMY : l.RENEGADES;
        boolean bl = l2 == l.ACADEMY && (long)n2 + 1 == b2.t() || l2 == l.RENEGADES && (long)n2 + 1 == b2.u();
        try {
            d2.c();
            return new f(string, d2, l2, bl);
        }
        catch (RuntimeException runtimeException) {
            runtimeException.printStackTrace();
            d2.close();
            throw runtimeException;
        }
    }

    private static void a(String string, String string2) {
        if (StringUtil.isBlank(string)) {
            throw new IllegalArgumentException("Argument 'name' is blank.");
        }
        if (StringUtil.isBlank(string2)) {
            throw new IllegalArgumentException("Argument 'playerDefinition' is blank.");
        }
    }

    private static t a(f f2, f f3) {
        return new t(f2.a(), f2.equals(f3), f2.b(), f2.f(), f2.j(), f2.k());
    }

    public static t[] a(List<f> list, f f2) {
        int n2 = list.size();
        t[] arrt = new t[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrt[i2] = q.a(list.get(i2), f2);
        }
        return arrt;
    }

    public static Comparator<f> a() {
        return (f2, f3) -> {
            if (f3.j() > f2.j()) {
                return 1;
            }
            if (f3.j() < f2.j()) {
                return -1;
            }
            if (f3.a() > f2.a()) {
                return 1;
            }
            if (f3.a() < f2.a()) {
                return -1;
            }
            return 0;
        }
        ;
    }

    public static Comparator<t> b() {
        return (t2, t3) -> {
            if (t3.getScore() > t2.getScore()) {
                return 1;
            }
            if (t3.getScore() < t2.getScore()) {
                return -1;
            }
            if (t3.getId() > t2.getId()) {
                return 1;
            }
            if (t3.getId() < t2.getId()) {
                return -1;
            }
            return 0;
        }
        ;
    }
}

