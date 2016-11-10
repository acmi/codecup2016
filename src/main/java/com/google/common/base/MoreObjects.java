/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Preconditions;
import java.util.Arrays;

public final class MoreObjects {
    public static <T> T firstNonNull(T t2, T t3) {
        return t2 != null ? t2 : Preconditions.checkNotNull(t3);
    }

    public static ToStringHelper toStringHelper(Object object) {
        return new ToStringHelper(object.getClass().getSimpleName());
    }

    public static ToStringHelper toStringHelper(Class<?> class_) {
        return new ToStringHelper(class_.getSimpleName());
    }

    public static final class ToStringHelper {
        private final String className;
        private ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;

        private ToStringHelper(String string) {
            this.holderTail = this.holderHead = new ValueHolder();
            this.omitNullValues = false;
            this.className = Preconditions.checkNotNull(string);
        }

        public ToStringHelper add(String string, Object object) {
            return this.addHolder(string, object);
        }

        public ToStringHelper add(String string, boolean bl) {
            return this.addHolder(string, String.valueOf(bl));
        }

        public ToStringHelper add(String string, int n2) {
            return this.addHolder(string, String.valueOf(n2));
        }

        public ToStringHelper add(String string, long l2) {
            return this.addHolder(string, String.valueOf(l2));
        }

        public ToStringHelper addValue(Object object) {
            return this.addHolder(object);
        }

        public String toString() {
            boolean bl = this.omitNullValues;
            String string = "";
            StringBuilder stringBuilder = new StringBuilder(32).append(this.className).append('{');
            ValueHolder valueHolder = this.holderHead.next;
            while (valueHolder != null) {
                Object object = valueHolder.value;
                if (!bl || object != null) {
                    stringBuilder.append(string);
                    string = ", ";
                    if (valueHolder.name != null) {
                        stringBuilder.append(valueHolder.name).append('=');
                    }
                    if (object != null && object.getClass().isArray()) {
                        Object[] arrobject = new Object[]{object};
                        String string2 = Arrays.deepToString(arrobject);
                        stringBuilder.append(string2.substring(1, string2.length() - 1));
                    } else {
                        stringBuilder.append(object);
                    }
                }
                valueHolder = valueHolder.next;
            }
            return stringBuilder.append('}').toString();
        }

        private ValueHolder addHolder() {
            ValueHolder valueHolder;
            this.holderTail = this.holderTail.next = (valueHolder = new ValueHolder());
            return valueHolder;
        }

        private ToStringHelper addHolder(Object object) {
            ValueHolder valueHolder = this.addHolder();
            valueHolder.value = object;
            return this;
        }

        private ToStringHelper addHolder(String string, Object object) {
            ValueHolder valueHolder = this.addHolder();
            valueHolder.value = object;
            valueHolder.name = Preconditions.checkNotNull(string);
            return this;
        }

        private static final class ValueHolder {
            String name;
            Object value;
            ValueHolder next;

            private ValueHolder() {
            }
        }

    }

}

