/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.a;

import java.util.List;

public class a {
    private final int a;
    private final String b;
    private final String c;
    private final String d;

    a(int n2, String string, String string2, List<String> list) {
        this.a = list.isEmpty() ? n2 : -1;
        this.b = string;
        this.c = string2;
        StringBuilder stringBuilder = new StringBuilder();
        for (String string3 : list) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(string3);
        }
        this.d = stringBuilder.toString();
    }

    public String a() {
        return this.b;
    }

    public String toString() {
        return String.format("Outcome {exitCode=%d, output='%s', error='%s', comment='%s'}", this.a, this.b, this.c, this.d);
    }
}

