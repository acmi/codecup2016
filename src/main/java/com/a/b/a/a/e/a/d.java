/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a;

import com.a.b.a.a.a.b;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.m;
import com.a.b.a.a.c.s;
import com.a.b.a.a.c.u;
import com.a.b.a.a.e.a.a.a;
import com.a.b.a.a.e.a.a.c;
import com.a.b.a.a.e.a.e;
import com.a.b.a.a.e.a.f;
import com.codeforces.commons.io.FileUtil;
import com.codeforces.commons.process.ThreadUtil;
import com.codeforces.commons.system.EnvironmentUtil;
import com.codeforces.commons.text.StringUtil;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class d
implements e {
    private static final Logger a = LoggerFactory.getLogger(d.class);
    private static final AtomicInteger b = new AtomicInteger();
    private final b c;
    private final int d;
    private final String e;
    private final int f;
    private final boolean g;
    private final boolean h;
    private a i;
    private c j;
    private final AtomicBoolean k = new AtomicBoolean(true);

    public static d a(b b2, int n2, String string, int n3, boolean bl) {
        d.a(n2, n3);
        Preconditions.checkArgument(new File(string).isFile(), "Argument 'playerDefinition' is expected to be a file.");
        return new d(b2, n2, string, n3, false, bl);
    }

    public static d b(b b2, int n2, String string, int n3, boolean bl) {
        d.a(n2, n3);
        Preconditions.checkArgument("#LocalTestPlayer".equals(string), "Argument 'playerDefinition' is not '#LocalTestPlayer'.");
        return new d(b2, n2, string, n3, true, bl);
    }

    private static void a(int n2, int n3) {
        Preconditions.checkArgument(n2 >= 0 && n2 <= 9, "Unexpected argument 'playerIndex': " + n2 + '.');
        Preconditions.checkArgument(n3 >= 1 && n3 <= 9, "Unexpected argument 'teamSize': " + n3 + '.');
    }

    private d(b b2, int n2, String string, int n3, boolean bl, boolean bl2) {
        this.c = b2;
        this.d = n2;
        this.e = string;
        this.f = n3;
        this.g = bl;
        this.h = bl2;
    }

    public File b() {
        return this.i == null ? null : this.i.a();
    }

    public void c() {
        int n2;
        String string;
        File file = this.c.d(this.d);
        this.j = new com.a.b.a.a.e.a.a.f(this.c, file);
        int n3 = this.c.z();
        int n4 = n3 + 49;
        while ((n2 = n3 + b.getAndIncrement()) <= n4) {
            try {
                this.j.a(n2);
                break;
            }
            catch (f f2) {
                continue;
            }
        }
        if (n2 > n4) {
            throw new f(String.format("Can't start %s, because all ports in the allowed range of %d to %d are in use.", this.j.getClass().getSimpleName(), n3, n4));
        }
        String string2 = string = this.c.l() ? StringUtils.repeat("0", 16) : RandomStringUtils.randomAlphanumeric(16);
        if (this.g) {
            String string3 = this.c.c(this.d);
            if (string3 != null) {
                string3 = StringUtil.replace(string3, "${port}", Integer.toString(n2));
                try {
                    Runtime.getRuntime().exec(EnvironmentUtil.expandSystemVariablesQuietly(string3));
                }
                catch (IOException iOException) {
                    throw new f(String.format("Can't execute local test player startup command '%s'.", string3), iOException);
                }
            }
        } else {
            this.a(Integer.toString(n2), string);
        }
        this.a(string);
    }

    private void a(String string, String string2) {
        String string3;
        long l2 = TimeUnit.MILLISECONDS.toSeconds((long)(this.f * this.c.f()) * (this.h ? 20 : 10) + 5000 + TimeUnit.SECONDS.toMillis(1) - 1);
        StringBuilder stringBuilder = new StringBuilder();
        String string4 = this.c.x();
        if (StringUtil.isNotBlank(string4)) {
            stringBuilder.append(" -l ").append(string4);
        }
        if (StringUtil.isNotBlank(string3 = this.c.y())) {
            stringBuilder.append(" -p ").append(string3);
        }
        String string5 = d.d();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("remote-process.port", string);
        hashMap.put("time-limit-seconds", Long.toString(l2));
        hashMap.put("system-user-credentials", stringBuilder.toString());
        hashMap.put("jruby-home", string5);
        hashMap.put("jruby-home.double-backslashed", StringUtil.replace(string5, "/", "\\\\"));
        try {
            File file = this.c.B();
            this.i = FileUtil.executeIoOperation(() -> a.a(this.e, hashMap, file, "127.0.0.1", string, string2), 3, 1000, ThreadUtil.ExecutionStrategy.Type.CONSTANT);
        }
        catch (IOException iOException) {
            throw new f(String.format("Failed to start process for player '%s'.", this.e), iOException);
        }
    }

    private void a(String string) {
        for (int i2 = 2; i2 >= 0; --i2) {
            String string2;
            try {
                this.j.a();
                string2 = this.j.b();
                if (string.equals(string2)) {
                    this.j.f();
                    break;
                }
            }
            catch (f f2) {
                a.error("Got unexpected exception while authenticating strategy '" + this.e + "'.", f2);
                if (i2 != 0) continue;
                throw f2;
            }
            String string3 = String.format("Player '%s' has returned unexpected token: '%s' expected, but '%s' found.", this.e, string, string2);
            a.error(string3);
            if (i2 != 0) continue;
            throw new f(string3);
        }
    }

    @Override
    public int a() {
        int n2 = this.j.c();
        this.j.b(this.f);
        return n2;
    }

    @Override
    public void a(m m2) {
        this.j.a(m2);
    }

    @Override
    public s[] a(D[] arrd, E e2) {
        if (arrd.length != this.f) {
            throw new IllegalArgumentException(String.format("Strategy adapter '%s' got %d wizards while team size is %d.", this.getClass().getSimpleName(), arrd.length, this.f));
        }
        this.j.a(new u(arrd, e2), this.k.getAndSet(false));
        return this.j.d();
    }

    @Override
    public void close() {
        if (this.j != null) {
            this.j.e();
        }
        new Thread(() -> {
            if (this.i != null) {
                if (this.c.l()) {
                    this.i.a(TimeUnit.MINUTES.toMillis(30));
                } else {
                    this.i.a(TimeUnit.SECONDS.toMillis(5));
                }
            }
            if (this.j != null) {
                this.j.g();
            }
            if (this.i != null) {
                this.i.b();
            }
        }
        ).start();
    }

    private static String d() {
        String[] arrstring;
        String string = System.getenv("JRUBY_HOME");
        if (StringUtil.isNotBlank(string) && (arrstring = new String[](string)).isDirectory() && new File((File)arrstring, "bin").isDirectory()) {
            string = arrstring.getAbsolutePath().replace('\\', '/');
            while (string.contains("//")) {
                string = StringUtil.replace(string, "//", "/");
            }
            while (string.endsWith("/")) {
                string = string.substring(0, string.length() - 1);
            }
            return string;
        }
        arrstring = new String[]{"C:/Programs/", "C:/", "C:/Program Files/", "C:/Program Files (x86)/"};
        String[] arrstring2 = new String[]{"jruby-9.1.5.0", "jruby", "jruby-9.0.3.0", "jruby-9.0.1.0", "jruby-1.7.13"};
        int n2 = arrstring.length;
        int n3 = arrstring2.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            for (int i3 = 0; i3 < n3; ++i3) {
                String string2 = arrstring[i2] + arrstring2[i3];
                if (!new File(string2).isDirectory() || !new File(string2, "bin").isDirectory()) continue;
                return string2;
            }
        }
        throw new f("Can't find JRuby home directory.");
    }
}

