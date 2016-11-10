/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.entry;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public interface Entry {
    public static final Set<Type> FILE_TYPE_SET = Collections.unmodifiableSet(EnumSet.of(Type.FILE));
    public static final Set<Type> DIRECTORY_TYPE_SET = Collections.unmodifiableSet(EnumSet.of(Type.DIRECTORY));
    public static final Set<Type> SYMLINK_TYPE_SET = Collections.unmodifiableSet(EnumSet.of(Type.SYMLINK));
    public static final Set<Type> SPECIAL_TYPE_SET = Collections.unmodifiableSet(EnumSet.of(Type.SPECIAL));
    public static final Set<Type> ALL_TYPE_SET = Collections.unmodifiableSet(EnumSet.allOf(Type.class));
    public static final Set<Size> ALL_SIZE_SET = Collections.unmodifiableSet(EnumSet.allOf(Size.class));
    public static final Set<Access> ALL_ACCESS_SET = Collections.unmodifiableSet(EnumSet.allOf(Access.class));

    public long getSize(Size var1);

    public long getTime(Access var1);

    public static final class Access
    extends Enum<Access> {
        public static final /* enum */ Access WRITE = new Access();
        public static final /* enum */ Access READ = new Access();
        public static final /* enum */ Access CREATE = new Access();
        private static final /* synthetic */ Access[] $VALUES;

        public static Access[] values() {
            return (Access[])$VALUES.clone();
        }

        private Access() {
            super(string, n2);
        }

        static {
            $VALUES = new Access[]{WRITE, READ, CREATE};
        }
    }

    public static final class Size
    extends Enum<Size> {
        public static final /* enum */ Size DATA = new Size();
        public static final /* enum */ Size STORAGE = new Size();
        private static final /* synthetic */ Size[] $VALUES;

        public static Size[] values() {
            return (Size[])$VALUES.clone();
        }

        private Size() {
            super(string, n2);
        }

        static {
            $VALUES = new Size[]{DATA, STORAGE};
        }
    }

    public static final class Type
    extends Enum<Type> {
        public static final /* enum */ Type FILE = new Type();
        public static final /* enum */ Type DIRECTORY = new Type();
        public static final /* enum */ Type SYMLINK = new Type();
        public static final /* enum */ Type SPECIAL = new Type();
        private static final /* synthetic */ Type[] $VALUES;

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }

        private Type() {
            super(string, n2);
        }

        static {
            $VALUES = new Type[]{FILE, DIRECTORY, SYMLINK, SPECIAL};
        }
    }

}

