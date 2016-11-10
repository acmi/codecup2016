/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.a.a.a.e;
import com.a.b.a.a.b.d.b.a;
import com.a.b.a.a.b.d.c.b;
import com.a.b.a.a.b.e.c;
import com.a.b.a.a.b.e.d;
import com.a.b.a.a.b.e.j;
import com.a.b.a.a.b.e.o;
import com.a.b.a.a.b.e.q;
import com.a.b.a.a.b.e.r;
import com.a.b.a.a.b.e.u;
import com.a.b.a.a.b.e.x;
import com.a.b.a.a.b.f;
import com.a.b.a.a.c.B;
import com.a.b.a.a.c.C;
import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.g;
import com.a.b.a.a.c.h;
import com.a.b.a.a.c.i;
import com.a.b.a.a.c.l;
import com.a.b.a.a.c.t;
import com.a.b.a.a.c.v;
import com.codeforces.commons.collection.MapBuilder;
import com.codeforces.commons.holder.Readable;
import com.codeforces.commons.pair.Pair;
import com.codeforces.commons.reflection.ReflectionUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class z {
    private static final Logger a = LoggerFactory.getLogger(z.class);
    private static final Gson b = new GsonBuilder().serializeSpecialFloatingPointValues().create();
    private static final Lock c = new ReentrantLock();
    private static final ConcurrentMap<Pair<Integer, l>, List<com.a.b.d>> d = new ConcurrentHashMap<Pair<Integer, l>, List<com.a.b.d>>();
    private static final ConcurrentMap<Long, C> e = new ConcurrentHashMap<Long, C>();

    private z() {
        throw new UnsupportedOperationException();
    }

    public static E a(int n2, int n3, double d2, List<f> list, com.a.b.a.a.a.b b2, List<com.a.b.d> list2, f f2, Map<l, List<com.a.b.a.a.b.d.f.a>> map) {
        ArrayList<D> arrayList = new ArrayList<D>();
        ArrayList<com.a.b.a.a.c.q> arrayList2 = new ArrayList<com.a.b.a.a.c.q>();
        ArrayList<v> arrayList3 = new ArrayList<v>();
        ArrayList<com.a.b.a.a.c.b> arrayList4 = new ArrayList<com.a.b.a.a.c.b>();
        ArrayList<com.a.b.a.a.c.d> arrayList5 = new ArrayList<com.a.b.a.a.c.d>();
        ArrayList<B> arrayList6 = new ArrayList<B>();
        boolean bl = f2 != null && f2.k() == l.RENEGADES;
        for (com.a.b.d d3 : z.a(n2, list2, f2)) {
            if (d3 instanceof com.a.b.a.a.b.d.f.a) {
                arrayList.add(x.a((com.a.b.a.a.b.d.f.a)d3, n2, f2, map.get((Object)d3.c())));
                continue;
            }
            if (d3 instanceof b) {
                arrayList2.add(o.a((b)d3, n2, bl));
                continue;
            }
            if (d3 instanceof com.a.b.a.a.b.d.d.e) {
                arrayList3.add(r.a((com.a.b.a.a.b.d.d.e)d3, d2, bl));
                continue;
            }
            if (d3 instanceof com.a.b.a.a.b.d.a.a) {
                arrayList4.add(c.a((com.a.b.a.a.b.d.a.a)d3, bl));
                continue;
            }
            if (d3 instanceof a) {
                arrayList5.add(d.a((a)d3, n2, bl));
                continue;
            }
            if (d3 instanceof com.a.b.a.a.b.d.e.a) {
                arrayList6.add(u.a((com.a.b.a.a.b.d.e.a)d3, bl));
                continue;
            }
            throw new IllegalArgumentException("Unsupported unit class: " + d3.getClass() + '.');
        }
        if (f2 != null) {
            com.a.a.a.a.c.a(arrayList);
            com.a.a.a.a.c.a(arrayList2);
            com.a.a.a.a.c.a(arrayList3);
            com.a.a.a.a.c.a(arrayList4);
            com.a.a.a.a.c.a(arrayList5);
            com.a.a.a.a.c.a(arrayList6);
        }
        return new E(n2, n3, b2.d(), b2.e(), q.a(list, f2), arrayList.toArray(new D[arrayList.size()]), arrayList2.toArray(new com.a.b.a.a.c.q[arrayList2.size()]), arrayList3.toArray(new v[arrayList3.size()]), arrayList4.toArray(new com.a.b.a.a.c.b[arrayList4.size()]), arrayList5.toArray(new com.a.b.a.a.c.d[arrayList5.size()]), arrayList6.toArray(new B[arrayList6.size()]));
    }

    private static List<com.a.b.d> a(int n3, List<com.a.b.d> list, f f2) {
        if (f2 == null) {
            return list;
        }
        l l2 = f2.k();
        Pair<Integer, l> pair = new Pair<Integer, l>(n3, l2);
        List<com.a.b.d> list2 = d.get(pair);
        if (list2 == null) {
            list2 = new ArrayList<com.a.b.d>();
            com.a.b.a.a.b.d.d[] arrd = (com.a.b.a.a.b.d.d[])list.stream().filter(d2 -> d2.c() == l2 && d2 instanceof com.a.b.a.a.b.d.d).map(d2 -> (com.a.b.a.a.b.d.d)d2).toArray(n2 -> new com.a.b.a.a.b.d.d[n2]);
            for (com.a.b.d d3 : list) {
                if (d3.c() != l2 && !com.a.b.a.a.b.d.d.a(d3, arrd)) continue;
                list2.add(d3);
            }
            d.put(pair, Collections.unmodifiableList(list2));
            d.remove(new Pair<Integer, l>(n3 - 1, l2));
        }
        return list2;
    }

    public static i a(E e2, Long l2, double d2, boolean bl, List<com.a.b.a.a.b.a.a> list, List<f> list2, List<com.a.b.d> list3) {
        int n2 = list.size();
        com.a.b.a.a.c.j[] arrj = new com.a.b.a.a.c.j[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrj[i2] = j.a(list.get(i2), e2.getTickIndex());
        }
        HashMap<Long, h> hashMap = new HashMap<Long, h>();
        for (com.a.b.d object : list3) {
            com.a.b.a.a.b.d.f.a a2;
            if (!(object instanceof com.a.b.a.a.b.d.f.a)) continue;
            hashMap.put(a2.a(), new h(ObjectUtils.defaultIfNull(a2.f(), 0.0), (a2 = (com.a.b.a.a.b.d.f.a)object).o() > 0 ? Integer.valueOf(a2.o()) : null, a2.y(), a2.z()));
        }
        Object object = null;
        for (f f2 : list2) {
            if (!f2.e()) continue;
            if (object == null) {
                object = new HashMap();
            }
            object.put(f2.a(), new g(true));
        }
        return new i(e2, l2, d2, bl, arrj, (Map<Long, h>)hashMap, (Map<Long, g>)object, new MapBuilder<String, List<com.a.b.d>>().put("units", list3).put("players", list2).buildUnmodifiable());
    }

    public static i a(String string, i i2, Object object) {
        c.lock();
        try {
            if (i2 == null) {
                i i3 = b.fromJson(string, i.class);
                return i3;
            }
            JsonObject jsonObject = b.fromJson(string, JsonObject.class);
            z.a(b, jsonObject, i2);
            z.b(b, jsonObject, i2);
            z.a(b, jsonObject, i2::getWizardsUnsafe);
            z.a(b, jsonObject, i2::getMinionsUnsafe);
            z.a(b, jsonObject, i2::getProjectilesUnsafe);
            z.a(b, jsonObject, i2::getBonusesUnsafe);
            z.a(b, jsonObject, i2::getBuildingsUnsafe);
            z.a(b, jsonObject, i2::getTreesUnsafe);
            z.c(b, jsonObject, i2);
            i i4 = b.fromJson((JsonElement)jsonObject, i.class);
            z.a(i4, object);
            e.putAll(z.a((C[])i4.getWizardsUnsafe()));
            i i5 = i4;
            return i5;
        }
        catch (RuntimeException runtimeException) {
            a.error(String.format("Can't de-serialize decorated world. Input string: %s.", string));
            throw runtimeException;
        }
        finally {
            c.unlock();
        }
    }

    private static void a(Gson gson, JsonObject jsonObject, i i2) {
        Map<String, List<Field>> map = ReflectionUtil.getFieldsByNameMap(i.class);
        for (Map.Entry<String, List<Field>> entry : map.entrySet()) {
            String string = entry.getKey();
            if (jsonObject.get(string) != null || "systemData".equals(string)) continue;
            z.a(gson, jsonObject, i2, entry.getValue().get(0));
        }
    }

    private static void b(Gson gson, JsonObject jsonObject, i i2) {
        Map<Long, t> map = z.a(i2);
        Map<String, List<Field>> map2 = ReflectionUtil.getFieldsByNameMap(t.class);
        for (JsonElement jsonElement : jsonObject.getAsJsonArray("players")) {
            JsonObject jsonObject2 = jsonElement.getAsJsonObject();
            long l2 = jsonObject2.get("id").getAsLong();
            t t2 = map.get(l2);
            if (t2 == null) continue;
            for (Map.Entry<String, List<Field>> entry : map2.entrySet()) {
                if (jsonObject2.get(entry.getKey()) != null) continue;
                z.a(gson, jsonObject2, t2, entry.getValue().get(0));
            }
        }
    }

    private static <U extends C> void a(Gson gson, JsonObject jsonObject, Readable<U[]> readable, String string) {
        C[] arrc = (C[])readable.get();
        if (arrc == null || arrc.length == 0) {
            return;
        }
        Map map = z.a((C[])arrc);
        Map<String, List<Field>> map2 = ReflectionUtil.getFieldsByNameMap(arrc[0].getClass());
        for (JsonElement jsonElement : jsonObject.getAsJsonArray(string)) {
            JsonObject jsonObject2 = jsonElement.getAsJsonObject();
            long l2 = jsonObject2.get("id").getAsLong();
            C c2 = (C)map.get(l2);
            if (c2 == null && (c2 = e.get(l2)) == null) continue;
            for (Map.Entry<String, List<Field>> entry : map2.entrySet()) {
                JsonElement jsonElement2 = jsonObject2.get(entry.getKey());
                if (jsonElement2 == null) {
                    z.a(gson, jsonObject2, c2, entry.getValue().get(0));
                    continue;
                }
                if (!"statuses".equals(entry.getKey()) || !(c2 instanceof com.a.b.a.a.c.o)) continue;
                z.a(gson, jsonElement2, (com.a.b.a.a.c.o)c2);
            }
        }
    }

    private static <U extends C> void a(Gson gson, JsonObject jsonObject, Readable<U[]> readable) {
        C[] arrc = (C[])readable.get();
        if (arrc == null || arrc.length == 0) {
            return;
        }
        z.a(gson, jsonObject, () -> arrc, StringUtils.uncapitalize(e.a(arrc[0].getClass().getSimpleName().toLowerCase())));
    }

    private static void c(Gson gson, JsonObject jsonObject, i i2) {
        Map<Long, com.a.b.a.a.c.j> map = z.b(i2);
        Map<String, List<Field>> map2 = ReflectionUtil.getFieldsByNameMap(com.a.b.a.a.c.j.class);
        for (JsonElement jsonElement : jsonObject.getAsJsonArray("effects")) {
            JsonObject jsonObject2 = jsonElement.getAsJsonObject();
            long l2 = jsonObject2.get("id").getAsLong();
            com.a.b.a.a.c.j j2 = map.get(l2);
            if (j2 == null) continue;
            for (Map.Entry<String, List<Field>> entry : map2.entrySet()) {
                if (jsonObject2.get(entry.getKey()) != null) continue;
                z.a(gson, jsonObject2, j2, entry.getValue().get(0));
            }
        }
    }

    private static void a(Gson gson, JsonElement jsonElement, com.a.b.a.a.c.o o2) {
        Map<Long, com.a.b.a.a.c.z> map = z.a(o2);
        Map<String, List<Field>> map2 = ReflectionUtil.getFieldsByNameMap(com.a.b.a.a.c.z.class);
        if (jsonElement.isJsonArray()) {
            for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                JsonObject jsonObject = jsonElement2.getAsJsonObject();
                long l2 = jsonObject.get("id").getAsLong();
                com.a.b.a.a.c.z z2 = map.get(l2);
                if (z2 == null) continue;
                for (Map.Entry<String, List<Field>> entry : map2.entrySet()) {
                    if (jsonObject.get(entry.getKey()) != null) continue;
                    z.a(gson, jsonObject, z2, entry.getValue().get(0));
                }
            }
        }
    }

    private static void a(Gson gson, JsonObject jsonObject, Object object, Field field) {
        try {
            jsonObject.add(field.getName(), gson.toJsonTree(field.get(object)));
        }
        catch (IllegalAccessException illegalAccessException) {
            a.error(String.format("Can't read field '%s' of previous %s.", field.getName(), object.getClass().getSimpleName()), illegalAccessException);
        }
    }

    public static Map<Long, t> a(i i2) {
        t[] arrt = i2.getPlayersUnsafe();
        int n2 = arrt.length;
        HashMap<Long, t> hashMap = new HashMap<Long, t>(n2);
        int n3 = n2;
        while (--n3 >= 0) {
            t t2 = arrt[n3];
            hashMap.put(t2.getId(), t2);
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public static Map<Long, C> a(E e2) {
        HashMap<Long, C> hashMap = new HashMap<Long, C>();
        z.a(hashMap, e2.getWizardsUnsafe());
        z.a(hashMap, e2.getMinionsUnsafe());
        z.a(hashMap, e2.getProjectilesUnsafe());
        z.a(hashMap, e2.getBonusesUnsafe());
        z.a(hashMap, e2.getBuildingsUnsafe());
        z.a(hashMap, e2.getTreesUnsafe());
        return Collections.unmodifiableMap(hashMap);
    }

    private static void a(Map<Long, C> map, C[] arrc) {
        int n2 = arrc.length;
        while (--n2 >= 0) {
            C c2 = arrc[n2];
            map.put(c2.getId(), c2);
        }
    }

    private static <U extends C> Map<Long, U> a(U[] arrU) {
        int n2 = arrU.length;
        HashMap<Long, U> hashMap = new HashMap<Long, U>(n2);
        int n3 = n2;
        while (--n3 >= 0) {
            U u2 = arrU[n3];
            hashMap.put(u2.getId(), u2);
        }
        return Collections.unmodifiableMap(hashMap);
    }

    private static Map<Long, com.a.b.a.a.c.j> b(i i2) {
        com.a.b.a.a.c.j[] arrj = i2.getEffectsUnsafe();
        int n2 = arrj.length;
        HashMap<Long, com.a.b.a.a.c.j> hashMap = new HashMap<Long, com.a.b.a.a.c.j>(n2);
        int n3 = n2;
        while (--n3 >= 0) {
            com.a.b.a.a.c.j j2 = arrj[n3];
            hashMap.put(j2.getId(), j2);
        }
        return Collections.unmodifiableMap(hashMap);
    }

    private static Map<Long, com.a.b.a.a.c.z> a(com.a.b.a.a.c.o o2) {
        com.a.b.a.a.c.z[] arrz = o2.getStatuses();
        int n2 = arrz.length;
        HashMap<Long, com.a.b.a.a.c.z> hashMap = new HashMap<Long, com.a.b.a.a.c.z>(n2);
        int n3 = n2;
        while (--n3 >= 0) {
            com.a.b.a.a.c.z z2 = arrz[n3];
            hashMap.put(z2.getId(), z2);
        }
        return Collections.unmodifiableMap(hashMap);
    }

    private static void a(i i2, Object object) {
        if (object != null) {
            try {
                ReflectionUtil.getFieldsByNameMap(i.class).get("systemData").get(0).set(i2, object);
            }
            catch (IllegalAccessException illegalAccessException) {
                a.error("Can't set field 'systemData' of decorated world.", illegalAccessException);
            }
        }
    }
}

