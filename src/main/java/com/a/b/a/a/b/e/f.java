/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.e;

import com.a.b.a.a.b.e.g;
import com.a.b.a.a.b.e.h;
import com.a.b.a.a.b.e.v;
import com.a.b.a.a.b.e.z;
import com.a.b.a.a.c.C;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.i;
import com.a.b.a.a.c.j;
import com.a.b.a.a.c.t;
import com.codeforces.commons.io.IoUtil;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.reflection.ReflectionUtil;
import com.codeforces.commons.text.StringUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Until;
import com.google.inject.ConfigurationException;
import com.google.inject.spi.Message;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class f {
    private static final Logger a = LoggerFactory.getLogger(f.class);
    private final Map<String, c> b = new HashMap<String, c>();
    private final SortedSet<c> c = new TreeSet<c>(c.a());
    private final Set<Long> d = new HashSet<Long>();
    private final Set<Long> e = new HashSet<Long>();
    private final Set<Long> f = new HashSet<Long>();
    private final Set<Long> g = new HashSet<Long>();
    private final StringBuilder h = new StringBuilder(IoUtil.BUFFER_SIZE);
    private final boolean i;
    private final f j;
    private final int k;
    private final int l;
    private double m;
    private float n;
    private i o;

    private f(f f2, int n2, int n3, boolean bl) {
        if (f2 == null) {
            throw new IllegalArgumentException("Argument 'specialFloatingPointValueSerializationStrategy' is null");
        }
        if (n2 < 0 || n2 > 15) {
            throw new IllegalArgumentException(String.format("Illegal value %d of argument 'maxDoubleFractionalPartDigitCount'.", n2));
        }
        if (n3 < 0 || n3 > 5) {
            throw new IllegalArgumentException(String.format("Illegal value %d of argument 'maxFloatFractionalPartDigitCount'.", n3));
        }
        this.j = f2;
        this.k = n2;
        this.l = n3;
        this.i = bl;
        this.m = 1.0;
        int n4 = n2;
        while (--n4 >= 0) {
            this.m *= 10.0;
        }
        this.n = 1.0f;
        n4 = n3;
        while (--n4 >= 0) {
            this.n *= 10.0f;
        }
    }

    public f() {
        this(f.b, 1, 1, false);
        this.a(new g(this), "DoubleFieldValueAppender");
    }

    public String a(i i2) {
        this.h.setLength(0);
        try {
            if (this.o == null) {
                this.a(i2, null, null);
            } else {
                if (i2.getTickIndex() != this.o.getTickIndex() + 1) {
                    a.warn(String.format("Unexpected tick %d of previous world. Current tick is %d.", this.o.getTickIndex(), i2.getTickIndex()));
                }
                Map<Long, t> map = z.a(this.o);
                Map<Long, C> map2 = z.a((E)this.o);
                this.a(i2, (object, stringBuilder) -> {
                    C c2;
                    Class class_ = object.getClass();
                    if (class_ == t.class) {
                        t t2 = (t)object;
                        if (t.areFieldEquals(t2, (t)map.get(t2.getId()))) {
                            stringBuilder.append("{\"id\":").append(t2.getId()).append('}');
                            return true;
                        }
                    } else if (C.class.isAssignableFrom(class_) && v.a(c2 = (C)object, (C)map2.get(c2.getId()))) {
                        stringBuilder.append("{\"id\":").append(c2.getId()).append('}');
                        return true;
                    }
                    return false;
                }
                , new h(this, i2, map, map2));
            }
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new ConfigurationException(Collections.singletonList(new Message("Got unexpected exception while serializing world.", illegalAccessException)));
        }
        this.o = i2;
        return this.h.toString();
    }

    private void a(Object object, d d2, a a2) throws IllegalAccessException {
        if (d2 != null && d2.intercept(object, this.h)) {
            return;
        }
        Class class_ = object.getClass();
        this.h.append('{');
        boolean bl = this.a(object, class_);
        boolean bl2 = false;
        for (Map.Entry<String, List<Field>> entry : ReflectionUtil.getFieldsByNameMap(class_).entrySet()) {
            Until until;
            String string = entry.getKey();
            List<Field> list = entry.getValue();
            if (list.size() != 1) {
                throw new ConfigurationException(Collections.singletonList(new Message(String.format("Zero or multiple fields with name '%s' in class '%s'.", string, class_.getSimpleName()))));
            }
            Field field = list.get(0);
            Expose expose = field.getAnnotation(Expose.class);
            if (expose != null && !expose.serialize()) continue;
            if (!bl && (until = field.getAnnotation(Until.class)) != null) {
                if (NumberUtil.equals(until.value(), 1.0)) continue;
                throw new ConfigurationException(Collections.singletonList(new Message(String.format("Unsupported value %s of @Until annotation on field '%s' of class '%s'.", until.value(), string, class_.getSimpleName()))));
            }
            int n2 = this.h.length();
            this.a(object, field, bl2, d2, a2);
            bl2 |= this.h.length() > n2;
        }
        this.h.append('}');
    }

    private boolean a(Object object, Class<?> class_) {
        if (class_ == i.class) {
            return ((E)object).getTickIndex() == 0;
        }
        if (class_ == t.class) {
            return this.d.add(((t)object).getId());
        }
        if (C.class.isAssignableFrom(class_)) {
            return this.e.add(((C)object).getId());
        }
        if (class_ == j.class) {
            return this.f.add(((j)object).getId());
        }
        if (class_ == com.a.b.a.a.c.z.class) {
            return this.g.add(((com.a.b.a.a.c.z)object).getId());
        }
        if (class_ == com.a.b.a.a.c.h.class || class_ == com.a.b.a.a.c.g.class) {
            return true;
        }
        throw new IllegalArgumentException("Unsupported class: " + class_ + '.');
    }

    private void a(Object object, Field field, boolean bl, d d2, a a2) throws IllegalAccessException {
        if (a2 != null && a2.a(object, field, this.h, bl)) {
            return;
        }
        for (c object22 : this.c) {
            if (!object22.b.a(object, field)) continue;
            object22.b.a(object, field, this.a(field, bl));
            return;
        }
        Class class_ = field.getType();
        if (class_ == Boolean.class) {
            Boolean bl2 = (Boolean)field.get(object);
            if (this.i || bl2 != null) {
                this.a(field, bl).append(bl2);
            }
        } else if (class_ == Boolean.TYPE) {
            this.a(field, bl).append(field.getBoolean(object));
        } else if (class_ == Integer.class) {
            Integer n2 = (Integer)field.get(object);
            if (this.i || n2 != null) {
                this.a(field, bl).append(n2);
            }
        } else if (class_ == Integer.TYPE) {
            this.a(field, bl).append(field.getInt(object));
        } else if (class_ == Long.class) {
            Long l2 = (Long)field.get(object);
            if (this.i || l2 != null) {
                this.a(field, bl).append(l2);
            }
        } else if (class_ == Long.TYPE) {
            this.a(field, bl).append(field.getLong(object));
        } else if (class_ == Float.class || class_ == Float.TYPE) {
            Float f2 = (Float)field.get(object);
            if (f2 == null) {
                if (this.i) {
                    this.a(field, bl).append("null");
                }
            } else if (Float.isNaN(f2.floatValue())) {
                this.a(object, field, f2, bl, "NaN");
            } else if (Float.isInfinite(f2.floatValue())) {
                String string = f2.floatValue() == Float.NEGATIVE_INFINITY ? "-Infinity" : "Infinity";
                this.a(object, field, f2, bl, string);
            } else {
                this.a(field, bl).append((float)Math.round(f2.floatValue() * this.n) / this.n);
            }
        } else if (class_ == Double.class || class_ == Double.TYPE) {
            Double d3 = (Double)field.get(object);
            if (d3 == null) {
                if (this.i) {
                    this.a(field, bl).append("null");
                }
            } else if (Double.isNaN(d3)) {
                this.a(object, field, d3, bl, "NaN");
            } else if (Double.isInfinite(d3)) {
                String string = d3 == Double.NEGATIVE_INFINITY ? "-Infinity" : "Infinity";
                this.a(object, field, d3, bl, string);
            } else {
                this.a(field, bl).append((double)Math.round(d3 * this.m) / this.m);
            }
        } else if (class_ == String.class) {
            String string = (String)field.get(object);
            if (string == null) {
                if (this.i) {
                    this.a(field, bl).append("null");
                }
            } else {
                this.a(field, bl).append('\"').append(StringEscapeUtils.escapeJson(string)).append('\"');
            }
        } else if (Enum.class.isAssignableFrom(class_)) {
            Enum enum_ = (Enum)field.get(object);
            if (enum_ == null) {
                if (this.i) {
                    this.a(field, bl).append("null");
                }
            } else {
                this.a(field, bl).append('\"').append(StringEscapeUtils.escapeJson(enum_.name())).append('\"');
            }
        } else if (Map.class.isAssignableFrom(class_)) {
            Map map = (Map)field.get(object);
            if (map == null) {
                if (this.i) {
                    this.a(field, bl).append("null");
                }
            } else {
                this.a(field, bl).append('{');
                boolean bl3 = true;
                for (Map.Entry entry : map.entrySet()) {
                    String string;
                    Map.Entry entry2 = entry;
                    if (entry2.getKey() == null || StringUtil.isBlank(string = entry2.getKey().toString()) || StringUtil.trim(string).length() != string.length()) {
                        throw new IllegalArgumentException(String.format("Illegal map key '%s' of field '%s.%s'.", entry2.getKey(), object.getClass().getSimpleName(), field.getName()));
                    }
                    if (!this.i && entry2.getValue() == null) continue;
                    if (bl3) {
                        bl3 = false;
                    } else {
                        this.h.append(',');
                    }
                    this.h.append('\"').append(StringEscapeUtils.escapeJson(string)).append("\":");
                    this.a(entry2.getValue(), field, d2, a2);
                }
                this.h.append('}');
            }
        } else {
            Object object2 = field.get(object);
            if (object2 == null) {
                if (this.i) {
                    this.a(field, bl).append("null");
                }
            } else if (class_.isArray()) {
                this.a(field, bl).append('[');
                this.b(object2, field, d2, a2);
                this.h.append(']');
            } else {
                this.a(field, bl);
                this.a(object2, d2, a2);
            }
        }
    }

    private void a(Object object, Field field, d d2, a a2) throws IllegalAccessException {
        if (object == null) {
            this.h.append("null");
            return;
        }
        Class class_ = object.getClass();
        if (class_ == Boolean.class || class_ == Boolean.TYPE || class_ == Byte.class || class_ == Byte.TYPE || class_ == Short.class || class_ == Short.TYPE || class_ == Integer.class || class_ == Integer.TYPE || class_ == Long.class || class_ == Long.TYPE) {
            this.h.append(object);
        } else if (class_ == Float.class || class_ == Float.TYPE) {
            if (Float.isNaN(((Float)object).floatValue())) {
                this.a(field, object, "NaN");
            } else if (Float.isInfinite(((Float)object).floatValue())) {
                String string = object.equals(Float.valueOf(Float.NEGATIVE_INFINITY)) ? "-Infinity" : "Infinity";
                this.a(field, object, string);
            } else {
                this.h.append((float)Math.round(((Float)object).floatValue() * this.n) / this.n);
            }
        } else if (class_ == Double.class || class_ == Double.TYPE) {
            if (Double.isNaN((Double)object)) {
                this.a(field, object, "NaN");
            } else if (Double.isInfinite((Double)object)) {
                String string = object.equals(Double.NEGATIVE_INFINITY) ? "-Infinity" : "Infinity";
                this.a(field, object, string);
            } else {
                this.h.append((double)Math.round((Double)object * this.m) / this.m);
            }
        } else if (Enum.class.isAssignableFrom(class_)) {
            this.h.append('\"').append(StringEscapeUtils.escapeJson(((Enum)object).name())).append('\"');
        } else if (class_.isArray()) {
            this.h.append('[');
            this.b(object, field, d2, a2);
            this.h.append(']');
        } else {
            this.a(object, d2, a2);
        }
    }

    private void b(Object object, Field field, d d2, a a2) throws IllegalAccessException {
        int n2 = Array.getLength(object);
        if (n2 <= 0) {
            return;
        }
        this.a(Array.get(object, 0), field, d2, a2);
        for (int i2 = 1; i2 < n2; ++i2) {
            this.h.append(',');
            this.a(Array.get(object, i2), field, d2, a2);
        }
    }

    private void a(Object object, Field field, Object object2, boolean bl, String string) {
        switch (this.j) {
            case a: {
                this.a(field, bl).append(string);
                break;
            }
            case b: {
                break;
            }
            case c: {
                throw new IllegalArgumentException(String.format("Can't serialize special floating point value '%s' of field '%s.%s'.", object2, object.getClass().getSimpleName(), field.getName()));
            }
            case d: {
                this.a(field, bl).append("0.0");
                break;
            }
            default: {
                throw new IllegalArgumentException(String.format("Unsupported special floating point value serialization strategy: %s.", new Object[]{this.j}));
            }
        }
    }

    private void a(Field field, Object object, String string) {
        switch (this.j) {
            case a: {
                this.h.append(string);
                break;
            }
            case b: 
            case c: {
                throw new IllegalArgumentException(String.format("Can't serialize special floating point value '%s' of item of array/collection field '%s.%s'.", object, field.getDeclaringClass().getSimpleName(), field.getName()));
            }
            case d: {
                this.h.append("0.0");
                break;
            }
            default: {
                throw new IllegalArgumentException(String.format("Unsupported special floating point value serialization strategy: %s.", new Object[]{this.j}));
            }
        }
    }

    private StringBuilder a(Field field, boolean bl) {
        if (bl) {
            this.h.append(',');
        }
        return this.h.append('\"').append(field.getName()).append("\":");
    }

    public void a(b b2, String string, double d2) {
        e.b(string);
        if (this.b.containsKey(string)) {
            throw new IllegalArgumentException("Listener '" + string + "' is already registered.");
        }
        c c2 = new c(string, d2, b2, null);
        this.b.put(string, c2);
        this.c.add(c2);
    }

    public void a(b b2, String string) {
        this.a(b2, string, 0.0);
    }

    static /* synthetic */ double a(f f2) {
        return f2.m;
    }

    static /* synthetic */ i b(f f2) {
        return f2.o;
    }

    private static class e {
        public final String c;

        private e(String string) {
            this.c = string;
        }

        public int hashCode() {
            return this.c.hashCode();
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            e e2 = (e)object;
            return this.c.equals(e2.c);
        }

        private static void b(String string) {
            if (StringUtil.isBlank(string)) {
                throw new IllegalArgumentException("Argument 'name' is blank.");
            }
            if (!StringUtil.trim(string).equals(string)) {
                throw new IllegalArgumentException("Argument 'name' should not contain neither leading nor trailing whitespace characters.");
            }
        }

        /* synthetic */ e(String string, g g2) {
            this(string);
        }
    }

    private static final class c
    extends e {
        private static final Comparator<c> d = (c2, c3) -> {
            int n2 = Double.compare(c3.a, c2.a);
            if (n2 != 0) {
                return n2;
            }
            return c2.c.compareTo(c3.c);
        };
        public final double a;
        public final b b;

        private c(String string, double d2, b b2) {
            super(string, null);
            this.a = d2;
            this.b = b2;
        }

        static /* synthetic */ Comparator a() {
            return d;
        }

        /* synthetic */ c(String string, double d2, b b2, g g2) {
            this(string, d2, b2);
        }
    }

    public static interface b {
        public boolean a(Object var1, Field var2) throws IllegalAccessException;

        public void a(Object var1, Field var2, StringBuilder var3) throws IllegalAccessException;
    }

    private static interface a {
        public boolean a(Object var1, Field var2, StringBuilder var3, boolean var4);
    }

    private static interface d {
        public boolean intercept(Object var1, StringBuilder var2);
    }

    public static final class f
    extends Enum<f> {
        public static final /* enum */ f a = new f();
        public static final /* enum */ f b = new f();
        public static final /* enum */ f c = new f();
        public static final /* enum */ f d = new f();
        private static final /* synthetic */ f[] $VALUES;

        public static f[] values() {
            return (f[])$VALUES.clone();
        }

        private f() {
            super(string, n2);
        }

        static {
            $VALUES = new f[]{a, b, c, d};
        }
    }

}

