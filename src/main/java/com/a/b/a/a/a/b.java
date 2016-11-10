/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.a;

import com.a.a.a.a.c;
import com.codeforces.commons.collection.ListBuilder;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.pair.Pair;
import com.codeforces.commons.text.StringUtil;
import com.google.common.base.Preconditions;
import java.io.File;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class b {
    private static final Logger b = LoggerFactory.getLogger(b.class);
    public static final b a = new b();
    private static final Pattern c = Pattern.compile("[1-5][\\d]{4}");
    private static final Pattern d = Pattern.compile("[1-9][0-9]{0,7}");
    private static final Pattern e = Pattern.compile("1");
    private static final Pattern f = Pattern.compile("[1-9][0-9]{0,3}x[1-9][0-9]{0,3}");
    private final Map<String, String> g;
    private final List<String> h;
    private final boolean i;
    private final boolean j;
    private final long k;
    private final long l;
    private int m;
    private int n;
    private int o;

    private b() {
        this.g = Collections.emptyMap();
        this.h = Collections.emptyList();
        this.i = false;
        this.j = false;
        this.k = 1;
        this.l = 6;
    }

    public b(String[] arrstring) {
        this(b.a(arrstring), b.b(arrstring));
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public b(Map<String, String> var1_1, List<String> var2_2) {
        super();
        this.g = new HashMap<String, String>(var1_1);
        if (var2_2.size() != 2) ** GOTO lbl-1000
        if (var2_2.stream().noneMatch((Predicate<String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, equals(java.lang.Object ), (Ljava/lang/String;)Z)((String)"#KeyboardPlayer"))) {
            this.h = new ListBuilder<String>().add(var2_2.get(0)).add(var2_2.get(0)).add(var2_2.get(0)).add(var2_2.get(0)).add(var2_2.get(0)).add(var2_2.get(1)).add(var2_2.get(1)).add(var2_2.get(1)).add(var2_2.get(1)).add(var2_2.get(1)).build();
            var3_3 = this.a(0);
            var4_4 = this.a(1);
            if (StringUtil.isNotBlank(var3_3)) {
                this.g.put("p1-name", var3_3);
                this.g.put("p2-name", var3_3);
                this.g.put("p3-name", var3_3);
                this.g.put("p4-name", var3_3);
                this.g.put("p5-name", var3_3);
            }
            if (StringUtil.isNotBlank(var4_4)) {
                this.g.put("p6-name", var4_4);
                this.g.put("p7-name", var4_4);
                this.g.put("p8-name", var4_4);
                this.g.put("p9-name", var4_4);
                this.g.put("p10-name", var4_4);
            }
            for (var5_5 = 1; var5_5 <= 10; ++var5_5) {
                this.g.put("p" + var5_5 + "-team-size", Integer.toString(1));
            }
            this.j = true;
            this.k = 1;
            this.l = 6;
            this.i = true;
        } else lbl-1000: // 2 sources:
        {
            this.h = new ArrayList<String>(var2_2);
            this.j = false;
            this.k = b.a(this.g.get("academy-master-player-id"), 1);
            this.l = b.a(this.g.get("renegades-master-player-id"), 6);
            this.i = b.a(var1_1.get("skills")) != false || b.a(var1_1.get("skills-enabled")) != false;
        }
        this.D();
        this.m = b.a(this.g, true);
        this.n = b.a(this.g, false);
        this.o = b.a(this.g);
    }

    public List<String> a() {
        return Collections.unmodifiableList(this.h);
    }

    public int b() {
        return this.m;
    }

    public int c() {
        return this.n;
    }

    public double d() {
        return 4000.0;
    }

    public double e() {
        return 4000.0;
    }

    public int f() {
        return this.o;
    }

    public final String a(int n2) {
        String string = StringUtil.trimToNull(this.g.get("p" + (n2 + 1) + "-name"));
        return string == null ? "Player #" + (n2 + 1) : string;
    }

    public int b(int n2) {
        b.debug("Parsing team size for player #" + (n2 + 1) + '.');
        String string = StringUtil.trimToNull(this.g.get("p" + (n2 + 1) + "-team-size"));
        if (string == null) {
            return 1;
        }
        if (!e.matcher(string).matches()) {
            throw new IllegalArgumentException("Illegal team size value: '" + string + "'.");
        }
        return Integer.parseInt(string);
    }

    public final String c(int n2) {
        return StringUtil.trimToNull(this.g.get("p" + (n2 + 1) + "-startup-command"));
    }

    public File g() {
        String string = this.g.get("replay-file");
        return StringUtil.isBlank(string) ? null : new File(string);
    }

    public File h() {
        String string = this.g.get("results-file");
        return StringUtil.isBlank(string) ? null : new File(string);
    }

    public File i() {
        String string = this.g.get("strategy-description-file");
        return StringUtil.isBlank(string) ? null : new File(string);
    }

    public File j() {
        String string = this.g.get("attributes-file");
        return StringUtil.isBlank(string) ? null : new File(string);
    }

    public File k() {
        String string = this.g.get("plugins-directory");
        return StringUtil.isBlank(string) ? null : new File(string);
    }

    public boolean l() {
        return b.a(this.g.get("debug"));
    }

    public boolean m() {
        return b.a(this.g.get("render-to-screen"));
    }

    public boolean n() {
        return b.a(this.g.get("render-to-screen-sync"));
    }

    public int o() {
        try {
            String string = this.g.get("render-to-screen-tick");
            return string == null ? 0 : Math.max(Math.min(Integer.parseInt(string), 10000000), 0);
        }
        catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    public boolean p() {
        return b.a(this.g.get("local-test"));
    }

    public boolean q() {
        return b.a(this.g.get("verification-game"));
    }

    public boolean r() {
        return this.i;
    }

    public boolean s() {
        return this.j;
    }

    public long t() {
        return this.k;
    }

    public long u() {
        return this.l;
    }

    public File v() {
        String string = this.g.get("write-to-text-file");
        return StringUtil.isBlank(string) ? null : new File(string);
    }

    public String w() {
        return this.g.get("write-to-remote-storage");
    }

    public String x() {
        return this.g.get("system-user-login");
    }

    public String y() {
        return this.g.get("system-user-password");
    }

    public int z() {
        String string = this.g.get("base-adapter-port");
        Preconditions.checkArgument(StringUtil.isNotBlank(string) && c.matcher(string).matches(), "Argument 'base-adapter-port' is expected to be an integer between 10000 and 59999 inclusive.");
        return Integer.parseInt(string);
    }

    public File d(int n2) {
        boolean bl = b.a(this.g.get("dump-tcp-data"));
        return bl ? new File("p" + (n2 + 1) + "-tcp-dump.bin") : null;
    }

    public Long A() {
        String string = this.g.get("seed");
        return StringUtil.isBlank(string) ? null : Long.valueOf(Long.parseLong(string));
    }

    public File B() {
        String string = this.g.get("cache-directory");
        return StringUtil.isBlank(string) ? null : new File(string);
    }

    public int C() {
        try {
            return Math.max(Math.min(Integer.parseInt(this.g.get("psycho-level")), 255), 0);
        }
        catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    private static int a(Map<String, String> map, boolean bl) {
        int n2;
        String string = StringUtil.trimToNull(map.get("render-to-screen-size"));
        if (string == null) {
            string = "1280x800";
        }
        if ((n2 = string.indexOf(120)) <= 0 || n2 == string.length() - 1 || !f.matcher(string).matches()) {
            throw new IllegalArgumentException("Illegal screen size value: '" + string + "'.");
        }
        int n3 = Integer.parseInt(bl ? string.substring(0, n2) : string.substring(n2 + 1));
        if (n3 < 100 || n3 > 100000) {
            Object[] arrobject = new Object[2];
            arrobject[0] = bl ? "first" : "second";
            arrobject[1] = n3;
            throw new IllegalArgumentException(String.format("Illegal screen size dimension (%s): '%d'.", arrobject));
        }
        return n3;
    }

    private static int a(Map<String, String> map) {
        b.debug("Parsing duration.");
        String string = StringUtil.trimToNull(map.get("duration"));
        if (string == null || "0".equals(string) || string.startsWith("-")) {
            string = Integer.toString(20000);
        }
        if (!d.matcher(string).matches()) {
            throw new IllegalArgumentException("Illegal duration value: '" + string + "'.");
        }
        return Integer.parseInt(string);
    }

    private static boolean a(String string) {
        return Boolean.parseBoolean(string) || BooleanUtils.toBoolean(string) || "1".equals(string);
    }

    private static long a(String string, long l2) {
        if (string != null) {
            try {
                return Long.parseLong(string);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return l2;
    }

    private static Map<String, String> a(String[] arrstring) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String string : arrstring) {
            if (!string.startsWith("-")) continue;
            Pair<String, String> pair = b.b(string.substring("-".length()));
            hashMap.put(pair.getFirst(), pair.getSecond());
        }
        return hashMap;
    }

    private static List<String> b(String[] arrstring) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string : arrstring) {
            if (string.startsWith("-")) continue;
            arrayList.add(string);
        }
        return arrayList;
    }

    private static Pair<String, String> b(String string) {
        int n2 = string.indexOf(61);
        if (n2 <= 0) {
            throw new IllegalArgumentException("Illegal property string: '" + string + "'.");
        }
        return new Pair<String, String>(string.substring(0, n2), string.substring(n2 + 1));
    }

    private void D() {
        for (String string : this.h) {
            if ("#KeyboardPlayer".equals(string)) {
                this.g.put("debug", "true");
                this.g.put("keyboard-player", "true");
                this.g.put("render-to-screen", "true");
                this.g.put("render-to-screen-sync", "true");
                continue;
            }
            if (!"#LocalTestPlayer".equals(string)) continue;
            this.g.put("debug", "true");
            this.g.put("local-test", "true");
        }
        if (StringUtil.isBlank(this.g.get("map"))) {
            this.g.put("map", "default.map");
        }
    }

    public static void a(b b2) {
        Long l2 = b2.A();
        if (l2 == null) {
            b2.g.put("seed", String.valueOf(c.a()));
            l2 = b2.A();
        }
        c.a(true, l2);
        b.info("Starting game with seed '" + l2 + "'.");
    }
}

