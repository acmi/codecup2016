/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.b;

import com.a.a.a.a.c;
import com.a.b.a.a.b.d.e.a;
import com.a.b.a.a.b.e.l;
import com.a.b.a.a.b.e.u;
import com.a.b.b;
import com.a.b.d;
import com.a.b.g;
import com.codeforces.commons.geometry.Point2D;
import java.util.List;

public class j
implements b {
    @Override
    public void a(g g2, int n2) {
        int[] arrn = new int[4];
        List<d> list = g2.a();
        int n3 = list.size();
        while (--n3 >= 0) {
            d d2 = list.get(n3);
            if (!(d2 instanceof a)) continue;
            int[] arrn2 = arrn;
            int n4 = u.a((a)d2);
            arrn2[n4] = arrn2[n4] + 1;
        }
        n3 = 4;
        while (--n3 >= 0) {
            if (arrn[n3] >= 85 || c.d() >= 0.005) continue;
            j.a(g2, n3, null);
        }
    }

    public static boolean a(g g2, int n2, List<d> list) {
        int n3 = 100;
        while (--n3 >= 0) {
            Point2D point2D = u.a(n2);
            if (point2D == null) continue;
            double d2 = u.a();
            a a2 = new a(point2D, d2);
            if (list == null) {
                if (!j.a(a2, d2, g2.a())) continue;
                g2.a(a2);
                return true;
            }
            if (!j.a(a2, d2, list)) continue;
            g2.a(a2);
            list.add(a2);
            return true;
        }
        return false;
    }

    private static boolean a(a a2, double d2, List<d> list) {
        int n2 = list.size();
        while (--n2 >= 0) {
            d d3 = list.get(n2);
            if (d3.b(a2) > d2 + l.a(d3)) continue;
            return false;
        }
        return true;
    }
}

