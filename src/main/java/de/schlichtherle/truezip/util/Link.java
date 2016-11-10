/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public interface Link<T> {
    public T getTarget();

    public static abstract class Type
    extends Enum<Type> {
        public static final /* enum */ Type STRONG = new Type("STRONG", 0){

            @Override
            <T> Link<T> newLink(T t2, ReferenceQueue<? super T> referenceQueue) {
                return new Strong<T>(t2);
            }
        };
        public static final /* enum */ Type SOFT = new Type("SOFT", 1){

            @Override
            <T> Link<T> newLink(T t2, ReferenceQueue<? super T> referenceQueue) {
                return new Soft<T>((T)t2, referenceQueue);
            }
        };
        public static final /* enum */ Type WEAK = new Type("WEAK", 2){

            @Override
            <T> Link<T> newLink(T t2, ReferenceQueue<? super T> referenceQueue) {
                return new Weak<T>((T)t2, referenceQueue);
            }
        };
        public static final /* enum */ Type PHANTOM = new Type("PHANTOM", 3){

            @Override
            public <T> Link<T> newLink(T t2, ReferenceQueue<? super T> referenceQueue) {
                return new Phantom<T>((T)t2, referenceQueue);
            }
        };
        private static final /* synthetic */ Type[] $VALUES;

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }

        private Type() {
            super(string, n2);
        }

        public <T> Link<T> newLink(T t2) {
            return this.newLink(t2, null);
        }

        abstract <T> Link<T> newLink(T var1, ReferenceQueue<? super T> var2);

        static {
            $VALUES = new Type[]{STRONG, SOFT, WEAK, PHANTOM};
        }

        private static final class Phantom<T>
        extends PhantomReference<T>
        implements Link<T> {
            Phantom(T t2, ReferenceQueue<? super T> referenceQueue) {
                super(t2, referenceQueue);
            }

            @Override
            public T getTarget() {
                return super.get();
            }

            public String toString() {
                return String.format("%s[target=%s]", this.getClass().getName(), this.getTarget());
            }
        }

        private static final class Weak<T>
        extends WeakReference<T>
        implements Link<T> {
            Weak(T t2, ReferenceQueue<? super T> referenceQueue) {
                super(t2, referenceQueue);
            }

            @Override
            public T getTarget() {
                return super.get();
            }

            public String toString() {
                return String.format("%s[target=%s]", this.getClass().getName(), this.getTarget());
            }
        }

        private static final class Soft<T>
        extends SoftReference<T>
        implements Link<T> {
            Soft(T t2, ReferenceQueue<? super T> referenceQueue) {
                super(t2, referenceQueue);
            }

            @Override
            public T getTarget() {
                return super.get();
            }

            public String toString() {
                return String.format("%s[target=%s]", this.getClass().getName(), this.getTarget());
            }
        }

        private static final class Strong<T>
        implements Link<T> {
            private final T target;

            Strong(T t2) {
                this.target = t2;
            }

            @Override
            public T getTarget() {
                return this.target;
            }

            public String toString() {
                return String.format("%s[target=%s]", this.getClass().getName(), this.getTarget());
            }
        }

    }

}

