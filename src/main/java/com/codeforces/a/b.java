/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.a;

import java.io.File;

public class b {
    private final File a;
    private final long b;
    private final File c;
    private final File d;
    private final File e;

    private b(File file, long l2, File file2, File file3, File file4) {
        this.a = file;
        this.b = l2;
        this.c = file2;
        this.d = file3;
        this.e = file4;
    }

    public File a() {
        return this.a;
    }

    public long b() {
        return this.b;
    }

    public File c() {
        return this.c;
    }

    public File d() {
        return this.d;
    }

    public File e() {
        return this.e;
    }

    public static final class a {
        private File a;
        private long b;
        private File c;
        private File d;
        private File e;

        public a a(long l2) {
            this.b = l2;
            return this;
        }

        public b a() {
            return new b(this.a, this.b, this.c, this.d, this.e);
        }
    }

}

