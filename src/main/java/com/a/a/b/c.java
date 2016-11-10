/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b;

import com.codeforces.commons.text.StringUtil;

class c {
    public final String a;

    c(String string) {
        this.a = string;
    }

    public int hashCode() {
        return this.a.hashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        c c2 = (c)object;
        return this.a.equals(c2.a);
    }

    static void a(String string) {
        if (StringUtil.isBlank(string)) {
            throw new IllegalArgumentException("Argument 'name' is blank.");
        }
        if (!StringUtil.trim(string).equals(string)) {
            throw new IllegalArgumentException("Argument 'name' should not contain neither leading nor trailing whitespace characters.");
        }
    }
}

