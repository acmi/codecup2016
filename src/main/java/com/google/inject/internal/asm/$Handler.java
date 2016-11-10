/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.$Label;

class $Handler {
    $Label a;
    $Label b;
    $Label c;
    String d;
    int e;
    $Handler f;

    $Handler() {
    }

    static $Handler a($Handler $Handler, $Label $Label, $Label $Label2) {
        int n2;
        if ($Handler == null) {
            return null;
        }
        $Handler.f = $Handler.a($Handler.f, $Label, $Label2);
        int n3 = $Handler.a.c;
        int n4 = $Handler.b.c;
        int n5 = $Label.c;
        int n6 = n2 = $Label2 == null ? Integer.MAX_VALUE : $Label2.c;
        if (n5 < n4 && n2 > n3) {
            if (n5 <= n3) {
                if (n2 >= n4) {
                    $Handler = $Handler.f;
                } else {
                    $Handler.a = $Label2;
                }
            } else if (n2 >= n4) {
                $Handler.b = $Label;
            } else {
                $Handler $Handler2 = new $Handler();
                $Handler2.a = $Label2;
                $Handler2.b = $Handler.b;
                $Handler2.c = $Handler.c;
                $Handler2.d = $Handler.d;
                $Handler2.e = $Handler.e;
                $Handler2.f = $Handler.f;
                $Handler.b = $Label;
                $Handler.f = $Handler2;
            }
        }
        return $Handler;
    }
}

