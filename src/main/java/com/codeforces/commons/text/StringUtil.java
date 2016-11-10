/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.text;

import com.codeforces.commons.holder.Holders;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.pair.BooleanPair;
import com.codeforces.commons.pair.BytePair;
import com.codeforces.commons.pair.DoublePair;
import com.codeforces.commons.pair.FloatPair;
import com.codeforces.commons.pair.IntPair;
import com.codeforces.commons.pair.LongPair;
import com.codeforces.commons.pair.Pair;
import com.codeforces.commons.pair.ShortPair;
import com.codeforces.commons.reflection.ReflectionUtil;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;
import org.apache.commons.lang3.mutable.MutableBoolean;

public final class StringUtil {
    private static final Pattern FORMAT_COMMENTS_COMMENT_SPLIT_PATTERN = Pattern.compile("\\[pre\\]|\\[/pre\\]");
    private static final Pattern FORMAT_COMMENTS_LINE_BREAK_REPLACE_PATTERN = Pattern.compile("[\n\r][\n\r]+");
    private static final Map<Class, ToStringConverter> toStringConverterByClass = new HashMap<Class, ToStringConverter>();
    private static final ReadWriteLock toStringConverterByClassMapLock = new ReentrantReadWriteLock();

    public static boolean isWhitespace(char c2) {
        return Character.isWhitespace(c2) || c2 == '\u00a0' || c2 == '\u200b';
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNotEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static boolean isBlank(String string) {
        if (string == null || string.isEmpty()) {
            return true;
        }
        for (int i2 = string.length() - 1; i2 >= 0; --i2) {
            if (StringUtil.isWhitespace(string.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isNotBlank(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }
        for (int i2 = string.length() - 1; i2 >= 0; --i2) {
            if (StringUtil.isWhitespace(string.charAt(i2))) continue;
            return true;
        }
        return false;
    }

    public static boolean equals(String string, String string2) {
        return string == null ? string2 == null : string.equals(string2);
    }

    public static String trim(String string) {
        int n2;
        if (string == null) {
            return null;
        }
        int n3 = string.length() - 1;
        int n4 = n3;
        for (n2 = 0; n2 <= n3 && StringUtil.isWhitespace(string.charAt(n2)); ++n2) {
        }
        while (n4 > n2 && StringUtil.isWhitespace(string.charAt(n4))) {
            --n4;
        }
        return n2 == 0 && n4 == n3 ? string : string.substring(n2, n4 + 1);
    }

    public static String trimToNull(String string) {
        return string == null ? null : ((string = StringUtil.trim(string)).isEmpty() ? null : string);
    }

    public static String trimToEmpty(String string) {
        return string == null ? "" : StringUtil.trim(string);
    }

    public static String[] split(String string, char c2) {
        String[] arrstring;
        int n2 = string.length();
        int n3 = 0;
        int n4 = 0;
        String[] arrstring2 = null;
        int n5 = 0;
        while (n4 < n2) {
            if (string.charAt(n4) == c2) {
                if (arrstring2 == null) {
                    arrstring2 = new String[8];
                } else if (n5 == arrstring2.length) {
                    arrstring = new String[n5 << 1];
                    System.arraycopy(arrstring2, 0, arrstring, 0, n5);
                    arrstring2 = arrstring;
                }
                arrstring2[n5++] = string.substring(n3, n4);
                n3 = ++n4;
                continue;
            }
            ++n4;
        }
        if (arrstring2 == null) {
            return new String[]{string};
        }
        if (n5 == arrstring2.length) {
            arrstring = new String[n5 + 1];
            System.arraycopy(arrstring2, 0, arrstring, 0, n5);
            arrstring2 = arrstring;
        }
        arrstring2[n5++] = string.substring(n3, n4);
        if (n5 == arrstring2.length) {
            return arrstring2;
        }
        arrstring = new String[n5];
        System.arraycopy(arrstring2, 0, arrstring, 0, n5);
        return arrstring;
    }

    public static String replace(String string, String string2, String string3) {
        if (StringUtil.isEmpty(string) || StringUtil.isEmpty(string2) || string3 == null) {
            return string;
        }
        int n2 = string.indexOf(string2);
        if (n2 == -1) {
            return string;
        }
        int n3 = 0;
        int n4 = string2.length();
        StringBuilder stringBuilder = new StringBuilder(string.length() + (Math.max(string3.length() - n4, 0) << 4));
        do {
            if (n2 > n3) {
                stringBuilder.append(string.substring(n3, n2));
            }
            stringBuilder.append(string3);
            n3 = n2 + n4;
        } while ((n2 = string.indexOf(string2, n3)) != -1);
        return stringBuilder.append(string.substring(n3)).toString();
    }

    public static /* varargs */ <T> String toString(Class<? extends T> class_, T t2, boolean bl, String ... arrstring) {
        ToStringOptions toStringOptions = new ToStringOptions();
        toStringOptions.skipNulls = bl;
        return StringUtil.toString(class_, t2, toStringOptions, arrstring);
    }

    public static /* varargs */ <T> String toString(Class<? extends T> class_, T t2, ToStringOptions toStringOptions, String ... arrstring) {
        if (t2 == null) {
            return StringUtil.getSimpleName(class_, toStringOptions.addEnclosingClassNames) + " {null}";
        }
        return StringUtil.toString(t2, toStringOptions, arrstring);
    }

    public static /* varargs */ String toString(Object object, boolean bl, String ... arrstring) {
        ToStringOptions toStringOptions = new ToStringOptions();
        toStringOptions.skipNulls = bl;
        return StringUtil.toString(object, toStringOptions, arrstring);
    }

    public static /* varargs */ String toString(Object object, ToStringOptions toStringOptions, String ... arrstring) {
        Object object2;
        Class class_ = object.getClass();
        if (arrstring.length == 0) {
            object2 = ReflectionUtil.getFieldsByNameMap(class_).keySet();
            arrstring = object2.toArray(new String[object2.size()]);
        }
        object2 = new StringBuilder(StringUtil.getSimpleName(class_, toStringOptions.addEnclosingClassNames)).append(" {");
        boolean bl = true;
        for (String string : arrstring) {
            String string2;
            if (StringUtil.isBlank(string)) {
                throw new IllegalArgumentException("Field name can not be neither 'null' nor blank.");
            }
            Object object3 = ReflectionUtil.getDeepValue(object, string);
            if (object3 == null) {
                if (toStringOptions.skipNulls || toStringOptions.skipEmptyStrings || toStringOptions.skipBlankStrings) continue;
                string2 = string + "=null";
            } else {
                string2 = StringUtil.fieldToString(object3, string, toStringOptions);
                if (string2 == null) continue;
            }
            if (bl) {
                bl = false;
            } else {
                object2.append(", ");
            }
            object2.append(string2);
        }
        return object2.append('}').toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static <T> ToStringConverter<? super T> getToStringConverter(Class<T> class_, boolean bl) {
        Lock lock = toStringConverterByClassMapLock.readLock();
        lock.lock();
        try {
            if (bl) {
                ToStringConverter toStringConverter;
                for (Class<T> class_2 = class_; class_2 != null; class_2 = class_2.getSuperclass()) {
                    toStringConverter = toStringConverterByClass.get(class_2);
                    if (toStringConverter == null) continue;
                    ToStringConverter toStringConverter2 = toStringConverter;
                    return toStringConverter2;
                }
                toStringConverter = null;
                return toStringConverter;
            }
            ToStringConverter toStringConverter = toStringConverterByClass.get(class_);
            return toStringConverter;
        }
        finally {
            lock.unlock();
        }
    }

    private static String getSimpleName(Class class_, boolean bl) {
        String string = class_.getSimpleName();
        if (bl) {
            while ((class_ = class_.getEnclosingClass()) != null) {
                string = String.format("%s.%s", class_.getSimpleName(), string);
            }
        }
        return string;
    }

    private static String fieldToString(Object object, String string, ToStringOptions toStringOptions) {
        if (object.getClass() == Boolean.class || object.getClass() == Boolean.TYPE) {
            return (Boolean)object != false ? string : "" + '!' + string;
        }
        MutableBoolean mutableBoolean = new MutableBoolean();
        String string2 = StringUtil.valueToString(object, mutableBoolean);
        if (StringUtil.shouldSkipField(string2, toStringOptions, mutableBoolean)) {
            return null;
        }
        return string + '=' + string2;
    }

    private static boolean shouldSkipField(String string, ToStringOptions toStringOptions, MutableBoolean mutableBoolean) {
        if (toStringOptions.skipNulls && string == null) {
            return true;
        }
        if (toStringOptions.skipEmptyStrings && (mutableBoolean != null && mutableBoolean.booleanValue() ? "''".equals(string) || "\"\"".equals(string) : StringUtil.isEmpty(string))) {
            return true;
        }
        if (toStringOptions.skipBlankStrings && (mutableBoolean != null && mutableBoolean.booleanValue() ? StringUtil.isBlank(string) || StringUtil.isBlank(string.substring(1, string.length() - 1)) : StringUtil.isBlank(string))) {
            return true;
        }
        return false;
    }

    private static String valueToString(Object object, MutableBoolean mutableBoolean) {
        if (object == null) {
            return null;
        }
        Class class_ = object.getClass();
        if (class_.isArray()) {
            return StringUtil.arrayToString(object);
        }
        ToStringConverter toStringConverter = StringUtil.getToStringConverter(class_, true);
        if (toStringConverter != null) {
            return toStringConverter.convert((Object)object);
        }
        if (object instanceof Collection) {
            return StringUtil.collectionToString((Collection)object);
        }
        if (object instanceof Map) {
            return StringUtil.mapToString((Map)object);
        }
        if (object instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry)object;
            return StringUtil.valueToString(entry.getKey(), null) + ": " + StringUtil.valueToString(entry.getValue(), null);
        }
        if (object instanceof Pair) {
            Pair pair = (Pair)object;
            return "" + '(' + StringUtil.valueToString(pair.getFirst(), null) + ", " + StringUtil.valueToString(pair.getSecond(), null) + ')';
        }
        if (object instanceof BooleanPair) {
            BooleanPair booleanPair = (BooleanPair)object;
            return "" + '(' + StringUtil.valueToString(booleanPair.getFirst(), null) + ", " + StringUtil.valueToString(booleanPair.getSecond(), null) + ')';
        }
        if (object instanceof BytePair) {
            BytePair bytePair = (BytePair)object;
            return "" + '(' + StringUtil.valueToString(Byte.valueOf(bytePair.getFirst()), null) + ", " + StringUtil.valueToString(Byte.valueOf(bytePair.getSecond()), null) + ')';
        }
        if (object instanceof ShortPair) {
            ShortPair shortPair = (ShortPair)object;
            return "" + '(' + StringUtil.valueToString(shortPair.getFirst(), null) + ", " + StringUtil.valueToString(shortPair.getSecond(), null) + ')';
        }
        if (object instanceof IntPair) {
            IntPair intPair = (IntPair)object;
            return "" + '(' + StringUtil.valueToString(intPair.getFirst(), null) + ", " + StringUtil.valueToString(intPair.getSecond(), null) + ')';
        }
        if (object instanceof LongPair) {
            LongPair longPair = (LongPair)object;
            return "" + '(' + StringUtil.valueToString(longPair.getFirst(), null) + ", " + StringUtil.valueToString(longPair.getSecond(), null) + ')';
        }
        if (object instanceof FloatPair) {
            FloatPair floatPair = (FloatPair)object;
            return "" + '(' + StringUtil.valueToString(Float.valueOf(floatPair.getFirst()), null) + ", " + StringUtil.valueToString(Float.valueOf(floatPair.getSecond()), null) + ')';
        }
        if (object instanceof DoublePair) {
            DoublePair doublePair = (DoublePair)object;
            return "" + '(' + StringUtil.valueToString(doublePair.getFirst(), null) + ", " + StringUtil.valueToString(doublePair.getSecond(), null) + ')';
        }
        if (class_ == Character.class) {
            Holders.setQuietly(mutableBoolean, true);
            return "'" + object + '\'';
        }
        if (class_ == Boolean.class || class_ == Byte.class || class_ == Short.class || class_ == Integer.class || class_ == Long.class || class_ == Float.class || class_ == Double.class) {
            return object.toString();
        }
        if (class_.isEnum()) {
            return ((Enum)object).name();
        }
        if (class_ == String.class) {
            Holders.setQuietly(mutableBoolean, true);
            return "" + '\'' + (String)object + '\'';
        }
        Holders.setQuietly(mutableBoolean, true);
        return "" + '\'' + String.valueOf(object) + '\'';
    }

    private static String arrayToString(Object object) {
        StringBuilder stringBuilder = new StringBuilder("[");
        int n2 = Array.getLength(object);
        if (n2 > 0) {
            stringBuilder.append(StringUtil.valueToString(Array.get(object, 0), null));
            for (int i2 = 1; i2 < n2; ++i2) {
                stringBuilder.append(", ").append(StringUtil.valueToString(Array.get(object, i2), null));
            }
        }
        return stringBuilder.append(']').toString();
    }

    private static String collectionToString(Collection collection) {
        StringBuilder stringBuilder = new StringBuilder("[");
        Iterator iterator = collection.iterator();
        if (iterator.hasNext()) {
            stringBuilder.append(StringUtil.valueToString(iterator.next(), null));
            while (iterator.hasNext()) {
                stringBuilder.append(", ").append(StringUtil.valueToString(iterator.next(), null));
            }
        }
        return stringBuilder.append(']').toString();
    }

    private static String mapToString(Map map) {
        StringBuilder stringBuilder = new StringBuilder("{");
        Iterator iterator = map.entrySet().iterator();
        if (iterator.hasNext()) {
            stringBuilder.append(StringUtil.valueToString(iterator.next(), null));
            while (iterator.hasNext()) {
                stringBuilder.append(", ").append(StringUtil.valueToString(iterator.next(), null));
            }
        }
        return stringBuilder.append('}').toString();
    }

    public static String shrinkTo(String string, int n2) {
        if (n2 < 8) {
            throw new IllegalArgumentException("Argument maxLength is expected to be at least 8.");
        }
        if (string == null || string.length() <= n2) {
            return string;
        }
        int n3 = n2 / 2;
        int n4 = n2 - n3 - 3;
        return string.substring(0, n3) + "..." + string.substring(string.length() - n4);
    }

    public static List<String> shrinkLinesTo(List<String> list, int n2, int n3) {
        if (n3 < 3) {
            throw new IllegalArgumentException("Argument 'maxLineCount' is expected to be at least 3.");
        }
        if (list == null) {
            return null;
        }
        int n4 = list.size();
        ArrayList<String> arrayList = new ArrayList<String>(Math.min(n3, n4));
        if (n4 <= n3) {
            for (String string : list) {
                arrayList.add(StringUtil.shrinkTo(string, n2));
            }
        } else {
            int n5;
            int n6 = n3 / 2;
            int n7 = n3 - n6 - 1;
            for (n5 = 0; n5 < n6; ++n5) {
                arrayList.add(StringUtil.shrinkTo(list.get(n5), n2));
            }
            arrayList.add("...");
            for (n5 = n4 - n7; n5 < n4; ++n5) {
                arrayList.add(StringUtil.shrinkTo(list.get(n5), n2));
            }
        }
        return arrayList;
    }

    public static final class ToStringOptions {
        private boolean skipNulls;
        private boolean skipEmptyStrings;
        private boolean skipBlankStrings;
        private boolean addEnclosingClassNames;
    }

    public static interface ToStringConverter<T> {
        public String convert(T var1);
    }

}

