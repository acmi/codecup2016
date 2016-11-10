/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.a.a;

import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import java.awt.Color;

public final class a {
    private static final Color a = new Color(255, 3, 3);
    private static final Color b = new Color(0, 66, 255);
    private static final Color c = new Color(28, 230, 185);
    private static final Color d = new Color(83, 0, 129);
    private static final Color e = new Color(255, 252, 1);
    private static final Color f = new Color(254, 186, 14);
    private static final Color g = new Color(32, 192, 0);
    private static final Color h = new Color(229, 91, 176);
    private static final Color i = new Color(149, 150, 151);
    private static final Color j = new Color(126, 191, 241);
    private static final Color k = new Color(16, 98, 70);
    private static final Color l = new Color(78, 42, 4);
    private static final Color m = new Color(254, 14, 0);
    private static final Color n = new Color(254, 121, 3);
    private static final Color o = new Color(248, 244, 0);
    private static final Color p = new Color(96, 255, 0);
    private static final Color q = new Color(2, 201, 179);
    private static final Color r = new Color(208, 66, 243);
    private static final Color[] s = new Color[]{a, b, c, d, e, f, g, h, i, j, k, l};

    public static Color a() {
        return new Color(a.getRGB());
    }

    public static Color b() {
        return new Color(b.getRGB());
    }

    public static Color c() {
        return new Color(c.getRGB());
    }

    public static Color d() {
        return new Color(d.getRGB());
    }

    public static Color e() {
        return new Color(e.getRGB());
    }

    public static Color f() {
        return new Color(f.getRGB());
    }

    public static Color g() {
        return new Color(h.getRGB());
    }

    public static Color h() {
        return new Color(i.getRGB());
    }

    public static Color i() {
        return new Color(j.getRGB());
    }

    public static Color j() {
        return new Color(l.getRGB());
    }

    public static Color k() {
        return new Color(n.getRGB());
    }

    public static Color l() {
        return new Color(q.getRGB());
    }

    public static Color m() {
        return new Color(r.getRGB());
    }

    public static Color a(Color color, int n2) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n2);
    }

    public static /* varargs */ Color a(Color ... arrcolor) {
        int n2 = arrcolor.length;
        if (n2 == 0) {
            return null;
        }
        if (n2 == 1) {
            return arrcolor[0];
        }
        Color color = arrcolor[0];
        int n3 = color.getAlpha();
        int n4 = color.getRed();
        int n5 = color.getGreen();
        int n6 = color.getBlue();
        int n7 = n2;
        while (--n7 > 0) {
            Color color2 = arrcolor[n7];
            n3 += color2.getAlpha();
            n4 += color2.getRed();
            n5 += color2.getGreen();
            n6 += color2.getBlue();
        }
        return new Color(NumberUtil.toInt(Math.round((double)n4 / (double)n2)), NumberUtil.toInt(Math.round((double)n5 / (double)n2)), NumberUtil.toInt(Math.round((double)n6 / (double)n2)), NumberUtil.toInt(Math.round((double)n3 / (double)n2)));
    }
}

