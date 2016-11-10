/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b;

import com.a.b.a.a.b.b.e;
import com.a.b.a.a.b.b.h;
import com.a.b.a.a.b.b.j;
import com.a.b.a.a.b.b.m;
import com.a.b.a.a.b.d.b.b;
import com.a.b.a.a.b.d.b.c;
import com.a.b.a.a.b.e.d;
import com.a.b.a.a.b.e.k;
import com.a.b.a.a.b.e.l;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.b.e.q;
import com.a.b.a.a.b.e.r;
import com.a.b.a.a.b.e.u;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.b.e.z;
import com.a.b.a.a.c.A;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.i;
import com.a.b.a.a.c.n;
import com.a.b.a.a.c.p;
import com.a.b.a.a.c.s;
import com.a.b.a.a.c.w;
import com.a.b.a.a.c.y;
import com.a.b.f;
import com.a.b.g;
import com.codeforces.commons.collection.MapBuilder;
import com.codeforces.commons.compress.ZipUtil;
import com.codeforces.commons.geometry.Circle2D;
import com.codeforces.commons.geometry.Line2D;
import com.codeforces.commons.geometry.Point2D;
import com.codeforces.commons.geometry.Vector2D;
import com.codeforces.commons.holder.Mutable;
import com.codeforces.commons.holder.SimpleMutable;
import com.codeforces.commons.io.IoUtil;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.pair.SimplePair;
import com.codeforces.commons.text.StringUtil;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class a
implements com.a.b.a {
    private static final Logger a = LoggerFactory.getLogger(a.class);
    @Inject
    private g b;
    private com.a.b.a.a.a.b c = com.a.b.a.a.a.b.a;
    private final AtomicBoolean d = new AtomicBoolean(false);
    private final AtomicReference<String> e = new AtomicReference<Object>(null);
    private int f;
    private int g;
    private final List<com.a.b.a.a.d.j> h = new ArrayList<com.a.b.a.a.d.j>();
    private final Map<com.a.b.a.a.b.f, List<com.a.b.a.a.b.d.f.a>> i = new LinkedHashMap<com.a.b.a.a.b.f, List<com.a.b.a.a.b.d.f.a>>();
    private final List<com.a.b.a.a.b.f> j = new ArrayList<com.a.b.a.a.b.f>();
    private final List<com.a.b.b> k = new ArrayList<com.a.b.b>();
    private final List<com.a.b.b> l = new ArrayList<com.a.b.b>();
    private final Map<com.a.b.a.a.c.l, List<com.a.b.a.a.b.d.f.a>> m = new MapBuilder(new EnumMap(com.a.b.a.a.c.l.class)).put(com.a.b.a.a.c.l.ACADEMY, new ArrayList()).put(com.a.b.a.a.c.l.RENEGADES, new ArrayList()).buildUnmodifiable();
    private final d.a n = new d.a();
    private final d.a o = new d.a();
    private final ConcurrentMap<Long, SimplePair<Integer, p[]>> p = new ConcurrentHashMap<Long, SimplePair<Integer, p[]>>();
    private ExecutorService q;
    private long r;
    private boolean s;
    private BufferedReader t;
    private i u;
    private volatile List<com.a.b.a.a.e.a.e> v;

    @Override
    public void a(com.a.b.a.a.a.b b2) {
        a.info("Game has been started.");
        this.r = System.currentTimeMillis();
        this.c = b2;
        boolean bl = this.s = b2.g() != null;
        if (this.s) {
            try {
                this.t = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(b2.g()), StandardCharsets.UTF_8));
            }
            catch (IOException iOException) {
                this.a(String.format("Can't open log file: %s", ExceptionUtils.getStackTrace(iOException)));
                return;
            }
            this.u = this.w();
            if (this.u != null) {
                // empty if block
            }
        }
        this.g = b2.f();
        this.l();
        a.m();
        if (!this.s) {
            this.n();
            this.o();
            this.p();
            this.q();
            this.r();
            this.s();
            this.t();
        }
        a.info("Game has been initialized.");
    }

    @Override
    public void a() {
        this.f = 0;
        while (!(!this.s && this.f >= this.g || this.d.get() || !this.s && (this.u() || d.a(this.n.a()) || d.a(this.o.a())))) {
            this.a(false);
            if (this.s) {
                this.u = this.w();
                if (this.u == null || this.u.isLastTick()) break;
                this.g = this.u.getTickCount();
            } else {
                this.i();
                this.j();
                this.b.c();
                this.k();
                this.b.a(this.f);
            }
            if (this.f > 0 && this.f % 1000 == 0) {
                a.info("Processed " + this.f + " ticks.");
            }
            ++this.f;
        }
        this.v();
        this.a(true);
    }

    @Override
    public void b() {
        a.info("Game has been finished in " + (System.currentTimeMillis() - this.r) + " ms.");
        for (com.a.b.a.a.b.f f2 : this.i.keySet()) {
            a.info("Player '" + f2.b() + "' scored " + f2.j() + " point(s).");
        }
        if (!this.s) {
            this.c();
        }
        this.d();
        if (!this.s) {
            this.e();
            this.f();
            this.g();
        }
    }

    private void c() {
        (this.v == null ? (Stream)this.i.keySet().stream().map(com.a.b.a.a.b.f::d).parallel() : this.v.parallelStream()).forEach(IoUtil::closeQuietly);
    }

    private void d() {
        this.h.parallelStream().forEach(j2 -> {
            try {
                j2.close();
            }
            catch (IOException iOException) {
                a.error(String.format("Can't close renderer '%s'.", j2.getClass().getSimpleName()), iOException);
                this.a(String.format("Can't close renderer '%s': %s", j2.getClass().getSimpleName(), ExceptionUtils.getStackTrace(iOException)));
            }
        }
        );
    }

    private void e() {
        File file = this.c.i();
        if (file == null) {
            return;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<com.a.b.a.a.b.f, List<com.a.b.a.a.b.d.f.a>> entry : this.i.entrySet()) {
                boolean bl = true;
                for (com.a.b.a.a.b.d.f.a a2 : entry.getValue()) {
                    if (bl) {
                        bl = false;
                    } else {
                        stringBuilder.append(',');
                    }
                    y[] arry = (y[])a2.x().stream().filter(x.a::contains).toArray(n2 -> new y[n2]);
                    stringBuilder.append(arry.length == 0 ? "-" : StringUtils.join((Object[])arry, "->"));
                }
                stringBuilder.append('\n');
            }
            FileUtils.writeByteArrayToFile(file, stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException iOException) {
            a.error(String.format("Can't write strategy descriptions to file '%s'.", file.getPath()), iOException);
        }
    }

    private void f() {
        File file = this.c.j();
        if (file == null) {
            return;
        }
        try {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("skills", Boolean.toString(this.c.r()));
            byte[] arrby = new GsonBuilder().create().toJson(hashMap).getBytes(StandardCharsets.UTF_8);
            FileUtils.writeByteArrayToFile(file, arrby);
        }
        catch (IOException iOException) {
            a.error(String.format("Can't write attributes to file '%s'.", file.getPath()), iOException);
        }
    }

    private void g() {
        File file = this.c.h();
        if (file == null) {
            return;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.d.get()) {
                a.error("Game has failed with the message: " + this.e.get());
                stringBuilder.append("FAILED\n").append(this.e.get()).append('\n');
            } else {
                stringBuilder.append("OK\nSEED ").append(this.c.A()).append('\n');
                Map<Integer, Integer> map = this.h();
                for (com.a.b.a.a.b.f f2 : this.i.keySet()) {
                    String string;
                    stringBuilder.append(map.get(f2.j())).append(' ').append(f2.j()).append(f2.f() ? " CRASHED" : " OK");
                    if (!this.c.p() && StringUtil.isNotBlank(string = this.a(f2))) {
                        stringBuilder.append(' ').append(Base64.encodeBase64URLSafeString(ZipUtil.compress(string.getBytes(StandardCharsets.UTF_8), 9)));
                    }
                    stringBuilder.append('\n');
                }
            }
            FileUtils.writeByteArrayToFile(file, stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException iOException) {
            a.error(String.format("Can't write results to file '%s'.", file.getPath()), iOException);
        }
    }

    private String a(com.a.b.a.a.b.f f2) {
        File file;
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtil.isNotEmpty(f2.g())) {
            stringBuilder.append(f2.g());
        }
        if (f2.d() instanceof com.a.b.a.a.e.a.d && (file = ((com.a.b.a.a.e.a.d)f2.d()).b()) != null) {
            a.a(stringBuilder, "\u0412\u044b\u0432\u043e\u0434 runner'\u0430 \u0432 stdout:", file, "runexe.output");
            a.a(stringBuilder, "\u0412\u044b\u0432\u043e\u0434 runner'\u0430 \u0432 stderr:", file, "runexe.error");
            if (this.c.q()) {
                a.a(stringBuilder, "\u0412\u044b\u0432\u043e\u0434 \u0441\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u0438 \u0432 stdout:", file, "process.output");
                a.a(stringBuilder, "\u0412\u044b\u0432\u043e\u0434 \u0441\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u0438 \u0432 stderr:", file, "process.error");
            }
        }
        return stringBuilder.toString();
    }

    private static void a(StringBuilder stringBuilder, String string, File file, String string2) {
        String string3 = a.a(new File(file, string2));
        if (StringUtil.isNotBlank(string3)) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\r\n\r\n");
            }
            stringBuilder.append(string).append("\r\n").append(string3);
        }
    }

    private static String a(File file) {
        List<String> list;
        if (!file.isFile()) {
            return "";
        }
        try {
            list = FileUtils.readLines(file, StandardCharsets.UTF_8);
            while (!list.isEmpty() && StringUtil.isBlank(list.get(0))) {
                list.remove(0);
            }
            while (!list.isEmpty() && StringUtil.isBlank(list.get(list.size() - 1))) {
                list.remove(list.size() - 1);
            }
            if (list.isEmpty()) {
                return "";
            }
        }
        catch (IOException iOException) {
            a.error("Can't read file '" + file.getAbsolutePath() + "'.", iOException);
            return "";
        }
        return StringUtils.join(StringUtil.shrinkLinesTo(list, 100, 17), "\r\n");
    }

    private Map<Integer, Integer> h() {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        ArrayList<com.a.b.a.a.b.f> arrayList = new ArrayList<com.a.b.a.a.b.f>(this.i.keySet());
        Collections.sort(arrayList, q.a());
        for (int i2 = arrayList.size() - 1; i2 >= 0; --i2) {
            hashMap.put(arrayList.get(i2).j(), i2 + 1);
        }
        return hashMap;
    }

    private void a(boolean bl) {
        double d2 = 1.0 / (double)this.b.d();
        ArrayList<com.a.b.a.a.b.f> arrayList = new ArrayList<com.a.b.a.a.b.f>(this.i.keySet());
        List<com.a.b.d> list = this.b.a();
        List<com.a.b.a.a.b.a.a> list2 = this.b.b();
        i i2 = this.u == null ? z.a(z.a(this.f, this.g, d2, arrayList, this.c, list, null, this.m), this.c.A(), d2, bl, list2, arrayList, list) : this.u;
        for (com.a.b.a.a.d.j j2 : this.h) {
            try {
                j2.a(i2);
            }
            catch (IOException iOException) {
                a.error(String.format("Can't render world using renderer '%s' [tick=%d].", j2.getClass().getSimpleName(), this.f), iOException);
                this.a(String.format("Can't render world using renderer '%s': %s [tick=%d]", j2.getClass().getSimpleName(), ExceptionUtils.getStackTrace(iOException), this.f));
            }
        }
    }

    private void i() {
        double d2 = 1.0 / (double)this.b.d();
        ArrayList<com.a.b.a.a.b.f> arrayList = new ArrayList<com.a.b.a.a.b.f>(this.i.keySet());
        List<com.a.b.d> list = this.b.a();
        ArrayList<Map.Entry<com.a.b.a.a.b.f, List<com.a.b.a.a.b.d.f.a>>> arrayList2 = new ArrayList<Map.Entry<com.a.b.a.a.b.f, List<com.a.b.a.a.b.d.f.a>>>(this.i.entrySet());
        List<a<com.a.b.a.a.b.d.f.a>> list2 = Collections.synchronizedList(new ArrayList());
        (this.j.isEmpty() ? arrayList2.stream() : arrayList2.parallelStream()).forEach(entry -> {
            Future<s[]> future;
            SimpleMutable simpleMutable;
            com.a.b.a.a.b.f f2 = (com.a.b.a.a.b.f)entry.getKey();
            if (f2.f()) {
                return;
            }
            List list4 = (List)entry.getValue();
            if (list4.stream().allMatch(a2 -> x.a(a2) || a2.b(A.FROZEN))) {
                return;
            }
            int n2 = list4.size();
            D[] arrd = new D[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                future = (com.a.b.a.a.b.d.f.a)list4.get(i2);
                long l2 = future.a();
                future.B();
                SimplePair<Integer, p[]> simplePair = this.p.get(l2);
                if (simplePair != null && Integer.valueOf(this.f).equals(simplePair.getFirst())) {
                    future.a(Preconditions.checkNotNull(simplePair.getSecond()));
                    this.p.remove(l2);
                }
                arrd[i2] = x.a((com.a.b.a.a.b.d.f.a)((Object)future), this.f, f2, this.m.get((Object)f2.k()));
            }
            E e2 = z.a(this.f, this.g, d2, arrayList, this.c, list, f2, this.m);
            future = this.q.submit(() -> f2.d().a(arrd, e2));
            if (!this.a(f2, future, simpleMutable = new SimpleMutable())) {
                return;
            }
            s[] arrs = (s[])simpleMutable.get();
            if (arrs == null || arrs.length != n2) {
                Object[] arrobject = new Object[5];
                arrobject[0] = f2.d().getClass().getSimpleName();
                arrobject[1] = f2;
                arrobject[2] = arrs == null ? 0 : arrs.length;
                arrobject[3] = n2;
                arrobject[4] = this.f;
                throw new RuntimeException(String.format("Strategy adapter '%s' of %s returned %d moves for %d wizards at tick %d.", arrobject));
            }
            for (int i3 = 0; i3 < n2; ++i3) {
                com.a.b.a.a.b.d.f.a a3;
                s s2 = arrs[i3];
                if (s2 == null || x.a(a3 = (com.a.b.a.a.b.d.f.a)list4.get(i3)) || a3.b(A.FROZEN)) continue;
                list2.add(new a(a3, s2, null));
            }
        }
        );
        this.a(list2);
        com.a.a.a.a.c.a(list2);
        list2.forEach(a2 -> {
            this.a((com.a.b.a.a.b.d.f.a)a2.a(), a2.b());
        }
        );
        com.a.a.a.a.c.a(list2);
        this.b(list2);
    }

    private <R> boolean a(com.a.b.a.a.b.f f2, Future<R> future, Mutable<R> mutable) {
        long l2 = System.currentTimeMillis();
        try {
            if (this.c.l()) {
                mutable.set(future.get(30, TimeUnit.MINUTES));
            } else {
                mutable.set(future.get(5000, TimeUnit.MILLISECONDS));
            }
        }
        catch (InterruptedException interruptedException) {
            a.error(String.format("Strategy adapter '%s' of %s has been interrupted at a tick %d.", f2.d().getClass().getSimpleName(), f2, this.f), interruptedException);
            future.cancel(true);
            f2.a("\u041e\u0436\u0438\u0434\u0430\u043d\u0438\u0435 \u043e\u0442\u043a\u043b\u0438\u043a\u0430 \u043e\u0442 \u0441\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u0438 \u0431\u044b\u043b\u043e \u043f\u0440\u0435\u0440\u0432\u0430\u043d\u043e.");
            return false;
        }
        catch (ExecutionException executionException) {
            a.warn(String.format("Strategy adapter '%s' of %s has failed at a tick %d.", f2.d().getClass().getSimpleName(), f2, this.f), executionException);
            future.cancel(true);
            f2.a("\u041f\u0440\u043e\u0446\u0435\u0441\u0441 \u0441\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u0438 \u043d\u0435\u043f\u0440\u0435\u0434\u0432\u0438\u0434\u0435\u043d\u043d\u043e \u0437\u0430\u0432\u0435\u0440\u0448\u0438\u043b\u0441\u044f \u043d\u0430 \u0442\u0438\u043a\u0435 " + this.f + '.');
            return false;
        }
        catch (TimeoutException timeoutException) {
            a.warn(String.format("Strategy adapter '%s' of %s has timed out at a tick %d.", f2.d().getClass().getSimpleName(), f2, this.f), timeoutException);
            future.cancel(true);
            Object[] arrobject = new Object[2];
            arrobject[0] = this.c.l() ? 30 : 5000;
            arrobject[1] = this.c.l() ? "\u043c\u0438\u043d" : "\u043c\u0441";
            f2.a(String.format("\u041f\u0440\u043e\u0446\u0435\u0441\u0441 \u0441\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u0438 \u043f\u0440\u0435\u0432\u044b\u0441\u0438\u043b \u043e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u0438\u0435 \u043f\u043e \u0432\u0440\u0435\u043c\u0435\u043d\u0438 \u043d\u0430 \u0442\u0438\u043a: %d %s.", arrobject));
            return false;
        }
        if (!this.c.l()) {
            long l3 = System.currentTimeMillis() - l2;
            long l4 = f2.h();
            if (l4 < l3) {
                a.warn(String.format("Strategy adapter '%s' of %s has consumed all available game time at a tick %d.", f2.d().getClass().getSimpleName(), f2, this.f));
                f2.a(String.format("\u041f\u0440\u043e\u0446\u0435\u0441\u0441 \u0441\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u0438 \u043f\u0440\u0435\u0432\u044b\u0441\u0438\u043b \u043e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u0438\u0435 \u043f\u043e \u0432\u0440\u0435\u043c\u0435\u043d\u0438 \u043d\u0430 \u0438\u0433\u0440\u0443: %d \u043c\u0441.", f2.i()));
                return false;
            }
            f2.b(l3);
        }
        return true;
    }

    private void j() {
        for (com.a.b.b b2 : this.k) {
            b2.a(this.b, this.f);
        }
    }

    private void k() {
        for (com.a.b.b b2 : this.l) {
            b2.a(this.b, this.f);
        }
    }

    private void a(List<a<com.a.b.a.a.b.d.f.a>> list) {
        list.forEach(a4 -> {
            int n3;
            com.a.b.a.a.b.d.f.a a5 = (com.a.b.a.a.b.d.f.a)a4.a();
            s s2 = a4.b();
            if (s2.getSkillToLearn() != null && a5.b(s2.getSkillToLearn())) {
                a5.c(s2.getSkillToLearn());
            }
            if (!a5.s().l()) {
                return;
            }
            p[] arrp = s2.getMessages();
            if (arrp == null || (n3 = arrp.length) == 0) {
                return;
            }
            com.a.b.a.a.b.d.f.a[] arra = (com.a.b.a.a.b.d.f.a[])this.m.get((Object)a5.c()).stream().filter(a3 -> !a5.equals(a3)).sorted((a2, a3) -> Long.compare(a2.a(), a3.a())).toArray(n2 -> new com.a.b.a.a.b.d.f.a[n2]);
            if (n3 != arra.length) {
                return;
            }
            for (int i2 = 0; i2 < n3; ++i2) {
                y y2;
                SimplePair<Integer, p[]> simplePair;
                com.a.b.a.a.b.d.f.a a6;
                byte[] arrby;
                p p2 = arrp[i2];
                if (p2 == null || (simplePair = this.p.get((a6 = arra[i2]).a())) != null && Preconditions.checkNotNull(simplePair.getFirst()) >= this.f) continue;
                n n4 = p2.getLane();
                y y3 = y2 = this.c.r() ? p2.getSkillToLearn() : null;
                if (this.c.s()) {
                    arrby = p2.getRawMessage();
                    if (arrby.length > 1024) {
                        arrby = ArrayUtils.EMPTY_BYTE_ARRAY;
                    }
                } else {
                    arrby = ArrayUtils.EMPTY_BYTE_ARRAY;
                }
                this.p.put(a6.a(), new SimplePair<Integer, p[]>(this.f + 1 + NumberUtil.toInt(Math.ceil((double)arrby.length / 2.0)), new p[]{new p(n4, y2, arrby)}));
            }
        }
        );
    }

    private void a(com.a.b.a.a.b.d.f.a a2, s s2) {
        com.a.b.a.a.c.a a3 = s2.getAction();
        if (a3 == null || a3 == com.a.b.a.a.c.a.NONE) {
            return;
        }
        if (x.a(a2) && a3 != com.a.b.a.a.c.a.STAFF || a2.b(A.FROZEN) || a2.c(this.f) > 0 || a2.a(a3, this.f) > 0) {
            return;
        }
        switch (a3) {
            case STAFF: {
                this.a(a2);
                break;
            }
            case MAGIC_MISSILE: {
                this.a(a2, s2, w.MAGIC_MISSILE);
                break;
            }
            case FROST_BOLT: {
                if (!a2.a(y.FROST_BOLT)) break;
                this.a(a2, s2, w.FROST_BOLT);
                break;
            }
            case FIREBALL: {
                if (!a2.a(y.FIREBALL)) break;
                this.a(a2, s2, w.FIREBALL);
                break;
            }
            case HASTE: {
                if (!a2.a(y.HASTE)) break;
                this.a(a2, s2, y.HASTE);
                break;
            }
            case SHIELD: {
                if (!a2.a(y.SHIELD)) break;
                this.a(a2, s2, y.SHIELD);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported action: " + (Object)((Object)a3) + '.');
            }
        }
    }

    private void a(com.a.b.a.a.b.d.f.a a2) {
        List<com.a.b.a.a.b.d.f.a> list = this.m.get((Object)a2.c());
        for (com.a.b.d d2 : x.a(a2, this.b)) {
            if (!(d2 instanceof com.a.b.a.a.b.d.b)) continue;
            double d3 = x.a(a2, (com.a.b.a.a.b.d.b)d2, list);
            if (d2 instanceof com.a.b.a.a.b.d.f.a) {
                x.a(this.c, this.b, (com.a.b.a.a.b.d.f.a)d2, a2.s(), list, this.m.get((Object)v.b(d2)), d3);
                continue;
            }
            if (d2 instanceof com.a.b.a.a.b.d.c.b) {
                o.a(this.b, (com.a.b.a.a.b.d.c.b)d2, a2.s(), list, d3);
                continue;
            }
            if (d2 instanceof com.a.b.a.a.b.d.b.a) {
                d.a(this.b, (com.a.b.a.a.b.d.b.a)d2, a2.s(), list, d3, d2.c() == com.a.b.a.a.c.l.ACADEMY ? this.n : this.o);
                continue;
            }
            if (d2 instanceof com.a.b.a.a.b.d.e.a) {
                u.a(this.b, (com.a.b.a.a.b.d.e.a)d2, d3);
                continue;
            }
            throw new IllegalArgumentException("Unsupported unit class: " + d2.getClass() + '.');
        }
        a2.b(com.a.b.a.a.c.a.STAFF, this.f);
    }

    private void a(com.a.b.a.a.b.d.f.a a2, s s2, w w2) {
        int n2 = r.a(a2, w2);
        if (a2.h() < (double)n2) {
            return;
        }
        a2.a(a2.h() - (double)n2);
        List<com.a.b.a.a.b.d.f.a> list = this.m.get((Object)a2.c());
        double d2 = x.b(a2, list);
        double d3 = s2.getMaxCastDistance();
        if (Double.isNaN(d3) || Double.isInfinite(d3)) {
            a.warn(String.format("Received unexpected 'maxCastDistance' (%s) for %s.", d3, a2));
            return;
        }
        d3 = Math.max(Math.min(d3, d2), 0.0);
        double d4 = s2.getMinCastDistance();
        if (Double.isNaN(d4) || Double.isInfinite(d4)) {
            a.warn(String.format("Received unexpected 'minCastDistance' (%s) for %s.", d4, a2));
            return;
        }
        d4 = Math.max(Math.min(d4, d3), 0.0);
        double d5 = s2.getCastAngle();
        if (Double.isNaN(d5) || Double.isInfinite(d5)) {
            a.warn(String.format("Received unexpected 'castAngle' (%s) for %s.", d5, a2));
            return;
        }
        d5 = Math.max(Math.min(d5, 0.2617993877991494), -0.2617993877991494);
        switch (w2) {
            case MAGIC_MISSILE: {
                this.b.a(new com.a.b.a.a.b.d.d.d(a2, d4, d3, d5));
                break;
            }
            case FROST_BOLT: {
                this.b.a(new com.a.b.a.a.b.d.d.c(a2, d4, d3, d5));
                break;
            }
            case FIREBALL: {
                this.b.a(new com.a.b.a.a.b.d.d.b(a2, d4, d3, d5));
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported projectile type: " + (Object)((Object)w2) + '.');
            }
        }
        a2.b(Preconditions.checkNotNull(s2.getAction()), this.f);
    }

    private void a(com.a.b.a.a.b.d.f.a a2, s s2, y y2) {
        int n2;
        switch (y2) {
            case HASTE: {
                n2 = 48;
                break;
            }
            case SHIELD: {
                n2 = 48;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported status spell skill type: " + (Object)((Object)y2) + '.');
            }
        }
        if (a2.h() < (double)n2) {
            return;
        }
        a2.a(a2.h() - (double)n2);
        List<com.a.b.a.a.b.d.f.a> list = this.m.get((Object)a2.c());
        double d2 = x.b(a2, list);
        com.a.b.a.a.b.d.f.a a3 = null;
        for (com.a.b.a.a.b.d.f.a a4 : list) {
            if (a4.a() != s2.getStatusTargetId()) continue;
            a3 = a4;
            break;
        }
        if (a3 == null) {
            a3 = a2;
        }
        if (!(a3.equals(a2) || a2.b(a3) <= d2 && Math.abs(a2.a(a3)) <= 0.2617993877991494)) {
            return;
        }
        switch (y2) {
            case HASTE: {
                a3.a(A.HASTENED, a2, 600);
                break;
            }
            case SHIELD: {
                a3.a(A.SHIELDED, a2, 600);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported status spell skill type: " + (Object)((Object)y2) + '.');
            }
        }
        a2.b(Preconditions.checkNotNull(s2.getAction()), this.f);
    }

    private void b(List<a<com.a.b.a.a.b.d.f.a>> list) {
        Vector2D[] arrvector2D = this.c(list);
        this.a(list, arrvector2D);
        for (a<com.a.b.a.a.b.d.f.a> a2 : list) {
            a.b(a2.a(), a2.b());
        }
    }

    private Vector2D[] c(List<a<com.a.b.a.a.b.d.f.a>> list) {
        int n2 = list.size();
        Vector2D[] arrvector2D = new Vector2D[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            a<com.a.b.a.a.b.d.f.a> a2 = list.get(i2);
            com.a.b.a.a.b.d.f.a a3 = a2.a();
            s s2 = a2.b();
            double d2 = s2.getSpeed();
            if (Double.isNaN(d2) || Double.isInfinite(d2)) {
                a.warn(String.format("Received unexpected 'speed' (%s) for %s.", d2, a3));
                continue;
            }
            double d3 = s2.getStrafeSpeed();
            if (Double.isNaN(d3) || Double.isInfinite(d3)) {
                a.warn(String.format("Received unexpected 'strafeSpeed' (%s) for %s.", d3, a3));
                continue;
            }
            if (d2 == 0.0 && d3 == 0.0 || x.a(a3) || a3.b(A.FROZEN)) continue;
            double d4 = x.a(a3, this.m.get((Object)a3.c()));
            double d5 = 4.0 * d4;
            double d6 = 3.0 * d4;
            double d7 = 3.0 * d4;
            double d8 = Math.hypot((d2 = Math.max(Math.min(d2, d5), - d6)) / (d2 >= 0.0 ? d5 : d6), (d3 = Math.max(Math.min(d3, d7), - d7)) / d7);
            if (d8 > 1.0) {
                d2 /= d8;
                d3 /= d8;
            }
            com.a.c.c c2 = a3.b();
            Vector2D vector2D = new Vector2D(d2, d3).rotate(c2.e());
            if (x.a(c2.c() + vector2D.getX(), c2.d() + vector2D.getY())) continue;
            arrvector2D[i2] = vector2D;
        }
        return arrvector2D;
    }

    private void a(List<a<com.a.b.a.a.b.d.f.a>> list, Vector2D[] arrvector2D) {
        int n2;
        int n3 = list.size();
        do {
            n2 = 0;
            for (int i2 = 0; i2 < n3; ++i2) {
                Vector2D vector2D = arrvector2D[i2];
                if (vector2D == null) continue;
                com.a.b.a.a.b.d.f.a a2 = list.get(i2).a();
                com.a.c.c c2 = a2.b();
                double d2 = c2.c();
                double d3 = c2.d();
                c2.a(d2 + vector2D.getX());
                c2.b(d3 + vector2D.getY());
                this.b.c(a2);
                if (x.a(a2, this.b, true)) {
                    ++n2;
                    arrvector2D[i2] = null;
                    continue;
                }
                c2.a(d2);
                c2.b(d3);
                this.b.c(a2);
            }
        } while (n2 > 0);
    }

    private static void b(com.a.b.a.a.b.d.f.a a2, s s2) {
        double d2 = s2.getTurn();
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            a.warn(String.format("Received unexpected 'turn' (%s) for %s.", d2, a2));
            return;
        }
        if (d2 == 0.0) {
            return;
        }
        if (x.a(a2) || a2.b(A.FROZEN)) {
            return;
        }
        double d3 = a2.b(A.HASTENED) ? 0.15707963267948966 : 0.10471975511965978;
        d2 = Math.max(Math.min(d2, d3), - d3);
        com.a.c.c c2 = a2.b();
        c2.c(l.a(c2.e() + d2));
    }

    private void l() {
        File file;
        String string;
        a.debug("Started to add renderers.");
        if (this.c.m()) {
            a.debug("Adding " + com.a.b.a.a.d.a.class.getSimpleName() + '.');
            this.h.add(new com.a.b.a.a.d.a(this.c));
        }
        if ((file = this.c.v()) != null) {
            try {
                a.debug("Adding " + com.a.b.a.a.d.l.class.getSimpleName() + '.');
                this.h.add(new com.a.b.a.a.d.l(file));
            }
            catch (IOException iOException) {
                a.error(String.format("Can't create renderer '%s'.", com.a.b.a.a.d.l.class.getSimpleName()), iOException);
                this.a(String.format("Can't create renderer '%s': %s", com.a.b.a.a.d.l.class.getSimpleName(), ExceptionUtils.getStackTrace(iOException)));
            }
        }
        if (StringUtils.isNotBlank(string = this.c.w())) {
            try {
                a.debug("Adding " + com.a.b.a.a.d.h.class.getSimpleName() + '.');
                this.h.add(new com.a.b.a.a.d.h(string, this.c));
            }
            catch (RuntimeException runtimeException) {
                a.error(String.format("Can't create renderer '%s'.", com.a.b.a.a.d.h.class.getSimpleName()), runtimeException);
                this.a(String.format("Can't create renderer '%s': %s", com.a.b.a.a.d.h.class.getSimpleName(), ExceptionUtils.getStackTrace(runtimeException)));
            }
        }
        a.debug("Finished to add renderers.");
    }

    private static void m() {
        a.debug("Started to create static objects.");
        a.debug("No objects to create.");
        a.debug("Finished to create static objects.");
    }

    private void n() {
        a.debug("Started to add players.");
        List<String> list = this.c.a();
        int n2 = list.size();
        if (n2 != 10) {
            throw new IllegalArgumentException("Unexpected player count: " + n2 + '.');
        }
        if (this.q != null) {
            this.q.shutdown();
        }
        this.q = Executors.newFixedThreadPool(n2, new com.a.b.a.a.b.b(this));
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = this.c.b(i2);
            com.a.b.a.a.b.f f2 = q.a(this.c, i2, this.c.a(i2), list.get(i2), n3, this.h);
            Preconditions.checkArgument((f2.k() == com.a.b.a.a.c.l.ACADEMY && f2.a() == this.c.t() || f2.k() == com.a.b.a.a.c.l.RENEGADES && f2.a() == this.c.u()) == f2.l());
            f2.a((long)(n3 * (this.g + 1)) * (f2.l() ? 30 : 20) + 5000);
            if (f2.e()) {
                if (this.j.size() >= 1) {
                    throw new IllegalArgumentException(String.format("Can only add %d keyboard player(s).", 1));
                }
                this.j.add(f2);
            }
            this.i.put(f2, new ArrayList());
        }
        a.debug("Finished to add players.");
    }

    private void o() {
        a.debug("Sending game contexts.");
        for (com.a.b.a.a.b.f f2 : this.i.keySet()) {
            SimpleMutable simpleMutable;
            com.a.b.a.a.c.m m2 = k.a(f2.c(), this.g, this.c);
            Future<Integer> future = this.q.submit(() -> {
                com.a.b.a.a.e.a.e e2 = f2.d();
                int n2 = e2.a();
                e2.a(m2);
                return n2;
            }
            );
            if (!this.a(f2, future, simpleMutable = new SimpleMutable())) continue;
            Integer n2 = (Integer)simpleMutable.get();
            if (f2.f() || com.a.b.a.a.a.c.a(n2)) continue;
            a.warn(String.format("Strategy adapter '%s' returned unsupported protocol version %d.", f2.d().getClass().getSimpleName(), n2));
            f2.a("\u041f\u0440\u043e\u0446\u0435\u0441\u0441 \u0441\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u0438 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0435\u0442 \u0443\u0441\u0442\u0430\u0440\u0435\u0432\u0448\u0443\u044e \u0432\u0435\u0440\u0441\u0438\u044e \u043f\u0440\u043e\u0442\u043e\u043a\u043e\u043b\u0430.");
        }
    }

    private void p() {
        a.debug("Adding player units.");
        int n2 = this.i.size();
        Preconditions.checkState(n2 == 10);
        int n3 = 0;
        for (Map.Entry<com.a.b.a.a.b.f, List<com.a.b.a.a.b.d.f.a>> entry : this.i.entrySet()) {
            com.a.b.a.a.b.f f2 = entry.getKey();
            int n4 = this.c.b(n3);
            for (int i2 = 0; i2 < n4; ++i2) {
                Point2D point2D = x.a(n3, n2, i2, n4);
                double d2 = x.b(n3, n2, i2, n4);
                com.a.b.a.a.b.d.f.a a2 = new com.a.b.a.a.b.d.f.a(f2, i2, point2D.getX(), point2D.getY(), d2, n3 < 5 ? com.a.b.a.a.c.l.ACADEMY : com.a.b.a.a.c.l.RENEGADES, this.c);
                entry.getValue().add(this.b.a(a2));
                this.m.get((Object)a2.c()).add(a2);
            }
            ++n3;
        }
    }

    private void q() {
        a.debug("Adding buildings.");
        this.n.a(this.b.a(new b(400.0, 3600.0, com.a.b.a.a.c.l.ACADEMY, null, this.c)));
        this.o.a(this.b.a(new b(3600.0, 400.0, com.a.b.a.a.c.l.RENEGADES, null, this.c)));
        Line2D line2D = Line2D.getLineByTwoPoints(0.0, 3600.0, 3600.0, 0.0);
        Line2D line2D2 = Line2D.getLineByTwoPoints(400.0, 4000.0, 4000.0, 400.0);
        Line2D line2D3 = Line2D.getLineByTwoPoints(400.0, 0.0, 4000.0, 3600.0);
        Line2D line2D4 = Line2D.getLineByTwoPoints(0.0, 400.0, 3600.0, 4000.0);
        Point2D point2D = Preconditions.checkNotNull(line2D2.getParallelLines(50.0)[1].getIntersectionPoint(line2D4.getParallelLines(50.0)[0], 1.0E-7));
        this.n.a(this.b.a(new c(point2D, com.a.b.a.a.c.l.ACADEMY, n.MIDDLE, this.c)));
        Point2D point2D2 = Preconditions.checkNotNull(line2D.getParallelLines(50.0)[0].getIntersectionPoint(line2D3.getParallelLines(50.0)[1], 1.0E-7));
        this.o.a(this.b.a(new c(point2D2, com.a.b.a.a.c.l.RENEGADES, n.MIDDLE, this.c)));
        double d2 = this.n.a().a(point2D);
        double d3 = d2 / 2.0;
        Point2D point2D3 = Preconditions.checkNotNull(line2D.getParallelLines(50.0)[0].getIntersectionPoints(new Circle2D(this.n.a().g(), d3), 1.0E-7)[1]);
        this.n.b(this.b.a(new c(point2D3, com.a.b.a.a.c.l.ACADEMY, n.MIDDLE, this.c)));
        Point2D point2D4 = Preconditions.checkNotNull(line2D2.getParallelLines(50.0)[1].getIntersectionPoints(new Circle2D(this.o.a().g(), d3), 1.0E-7)[0]);
        this.o.b(this.b.a(new c(point2D4, com.a.b.a.a.c.l.RENEGADES, n.MIDDLE, this.c)));
        Line2D line2D5 = Line2D.getLineByTwoPoints(0.0, 0.0, 0.0, 4000.0);
        Line2D line2D6 = Line2D.getLineByTwoPoints(0.0, 0.0, 4000.0, 0.0);
        Line2D line2D7 = Line2D.getLineByTwoPoints(4000.0, 0.0, 4000.0, 4000.0);
        Line2D line2D8 = Line2D.getLineByTwoPoints(0.0, 4000.0, 4000.0, 4000.0);
        double d4 = 350.0;
        Point2D point2D5 = Preconditions.checkNotNull(line2D5.getParallelLines(d4)[1].getIntersectionPoints(new Circle2D(this.n.a().g(), d2), 1.0E-7)[0]);
        this.n.c(this.b.a(new c(point2D5, com.a.b.a.a.c.l.ACADEMY, n.TOP, this.c)));
        Point2D point2D6 = Preconditions.checkNotNull(line2D5.getParallelLines(50.0)[1].getIntersectionPoints(new Circle2D(this.n.a().g(), d3), 1.0E-7)[0]);
        this.n.d(this.b.a(new c(point2D6, com.a.b.a.a.c.l.ACADEMY, n.TOP, this.c)));
        Point2D point2D7 = Preconditions.checkNotNull(line2D8.getParallelLines(50.0)[1].getIntersectionPoints(new Circle2D(this.n.a().g(), d2), 1.0E-7)[1]);
        this.n.e(this.b.a(new c(point2D7, com.a.b.a.a.c.l.ACADEMY, n.BOTTOM, this.c)));
        Point2D point2D8 = Preconditions.checkNotNull(line2D8.getParallelLines(d4)[1].getIntersectionPoints(new Circle2D(this.n.a().g(), d3), 1.0E-7)[1]);
        this.n.f(this.b.a(new c(point2D8, com.a.b.a.a.c.l.ACADEMY, n.BOTTOM, this.c)));
        Point2D point2D9 = Preconditions.checkNotNull(line2D6.getParallelLines(50.0)[0].getIntersectionPoints(new Circle2D(this.o.a().g(), d2), 1.0E-7)[0]);
        this.o.c(this.b.a(new c(point2D9, com.a.b.a.a.c.l.RENEGADES, n.TOP, this.c)));
        Point2D point2D10 = Preconditions.checkNotNull(line2D6.getParallelLines(d4)[0].getIntersectionPoints(new Circle2D(this.o.a().g(), d3), 1.0E-7)[0]);
        this.o.d(this.b.a(new c(point2D10, com.a.b.a.a.c.l.RENEGADES, n.TOP, this.c)));
        Point2D point2D11 = Preconditions.checkNotNull(line2D7.getParallelLines(d4)[0].getIntersectionPoints(new Circle2D(this.o.a().g(), d2), 1.0E-7)[1]);
        this.o.e(this.b.a(new c(point2D11, com.a.b.a.a.c.l.RENEGADES, n.BOTTOM, this.c)));
        Point2D point2D12 = Preconditions.checkNotNull(line2D7.getParallelLines(50.0)[0].getIntersectionPoints(new Circle2D(this.o.a().g(), d3), 1.0E-7)[1]);
        this.o.f(this.b.a(new c(point2D12, com.a.b.a.a.c.l.RENEGADES, n.BOTTOM, this.c)));
    }

    private void r() {
        a.debug("Adding trees.");
        List<com.a.b.d> list = this.b.a();
        ArrayList<com.a.b.d> arrayList = new ArrayList<com.a.b.d>(list.size() + 340);
        arrayList.addAll(list);
        for (int i2 = 0; i2 < 4; ++i2) {
            for (int i3 = 0; i3 < 85; ++i3) {
                j.a(this.b, i2, arrayList);
            }
        }
    }

    private void s() {
        a.debug("Adding collision listeners.");
        this.b.a(com.a.b.d.class, new com.a.b.a.a.b.c.a());
        this.b.a(com.a.b.a.a.b.d.d.e.class, com.a.b.a.a.b.d.b.class, new com.a.b.a.a.b.c.b(this.c, this.b, this.m, this.n, this.o));
        this.b.a(com.a.b.a.a.b.d.f.a.class, com.a.b.a.a.b.d.a.a.class, new com.a.b.a.a.b.c.c());
    }

    private void t() {
        a.debug("Adding game events.");
        this.k.add(new com.a.b.a.a.b.b.l());
        this.k.add(new com.a.b.a.a.b.b.f(this.c, this.m, this.n, this.o));
        this.k.add(new com.a.b.a.a.b.b.b(this.c, this.m, this.n, this.o));
        this.l.add(new com.a.b.a.a.b.b.l());
        this.l.add(new com.a.b.a.a.b.b.k());
        this.l.add(new com.a.b.a.a.b.b.c(this.c, this.m, this.n, this.o));
        this.l.add(new com.a.b.a.a.b.b.a());
        this.l.add(new j());
        this.l.add(new h(this.n, this.o));
        this.l.add(new m(this.i));
        this.l.add(new e());
        this.l.add(new com.a.b.a.a.b.b.d(this.c, this.m, this.n, this.o));
    }

    private boolean u() {
        return this.i.keySet().stream().allMatch(com.a.b.a.a.b.f::f);
    }

    private void v() {
        if (this.s) {
            return;
        }
        Set<com.a.b.a.a.b.f> set = this.i.keySet();
        if (d.a(this.n.a())) {
            set.stream().filter(f2 -> f2.k() == com.a.b.a.a.c.l.RENEGADES).forEach(f2 -> {
                f2.a(1000);
            }
            );
        }
        if (d.a(this.o.a())) {
            set.stream().filter(f2 -> f2.k() == com.a.b.a.a.c.l.ACADEMY).forEach(f2 -> {
                f2.a(1000);
            }
            );
        }
        if (this.c.s() && set.size() == 10) {
            this.v = set.stream().map(com.a.b.a.a.b.f::d).collect(Collectors.toList());
            Iterator<com.a.b.a.a.b.f> iterator = set.iterator();
            com.a.b.a.a.b.f f3 = iterator.next();
            iterator.next();
            iterator.next();
            iterator.next();
            iterator.next();
            com.a.b.a.a.b.f f4 = iterator.next();
            List<com.a.b.a.a.b.d.f.a> list = this.i.get(f3);
            List<com.a.b.a.a.b.d.f.a> list2 = this.i.get(f4);
            this.a(f3, set, list);
            this.a(f4, set, list2);
            this.i.clear();
            this.i.put(f3, list);
            this.i.put(f4, list2);
        }
    }

    private void a(com.a.b.a.a.b.f f2, Set<com.a.b.a.a.b.f> set, List<com.a.b.a.a.b.d.f.a> list) {
        set.stream().filter(f3 -> f3.k() == f2.k() && !f3.equals(f2)).forEach(f3 -> {
            f2.a(f3.j());
            list.addAll((Collection)this.i.get(f3));
            if (f3.f() && !f2.f()) {
                f2.a(f3.g());
            }
        }
        );
    }

    private void a(String string) {
        if (!this.d.getAndSet(true)) {
            this.e.set(string);
        }
    }

    private i w() {
        try {
            Map<String, ArrayList<com.a.b.a.a.b.f>> map = new MapBuilder<String, List<com.a.b.d>>().put("units", this.b.a()).put("players", new ArrayList<com.a.b.a.a.b.f>(this.i.keySet())).buildUnmodifiable();
            return z.a(this.t.readLine(), this.u, map);
        }
        catch (IOException iOException) {
            this.a(String.format("Can't read world from log file: %s", ExceptionUtils.getStackTrace(iOException)));
            return null;
        }
    }

    private static final class a<U extends com.a.b.d> {
        private final U a;
        private final s b;

        private a(U u2, s s2) {
            this.a = u2;
            this.b = s2;
        }

        public U a() {
            return this.a;
        }

        private s b() {
            return this.b;
        }

        public String toString() {
            return StringUtil.toString((Object)this, false, new String[0]);
        }

        /* synthetic */ a(com.a.b.d d2, s s2, com.a.b.a.a.b.b b2) {
            this(d2, s2);
        }
    }

}

