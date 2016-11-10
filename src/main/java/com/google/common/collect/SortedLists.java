/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

final class SortedLists {
    public static <E> int binarySearch(List<? extends E> list, E e2, Comparator<? super E> comparator, KeyPresentBehavior keyPresentBehavior, KeyAbsentBehavior keyAbsentBehavior) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(list);
        Preconditions.checkNotNull(keyPresentBehavior);
        Preconditions.checkNotNull(keyAbsentBehavior);
        if (!(list instanceof RandomAccess)) {
            list = Lists.newArrayList(list);
        }
        int n2 = 0;
        int n3 = list.size() - 1;
        while (n2 <= n3) {
            int n4 = n2 + n3 >>> 1;
            int n5 = comparator.compare(e2, list.get(n4));
            if (n5 < 0) {
                n3 = n4 - 1;
                continue;
            }
            if (n5 > 0) {
                n2 = n4 + 1;
                continue;
            }
            return n2 + keyPresentBehavior.resultIndex(comparator, e2, list.subList(n2, n3 + 1), n4 - n2);
        }
        return keyAbsentBehavior.resultIndex(n2);
    }

    public static abstract class KeyAbsentBehavior
    extends Enum<KeyAbsentBehavior> {
        public static final /* enum */ KeyAbsentBehavior NEXT_LOWER = new KeyAbsentBehavior("NEXT_LOWER", 0){

            @Override
            int resultIndex(int n2) {
                return n2 - 1;
            }
        };
        public static final /* enum */ KeyAbsentBehavior NEXT_HIGHER = new KeyAbsentBehavior("NEXT_HIGHER", 1){

            @Override
            public int resultIndex(int n2) {
                return n2;
            }
        };
        public static final /* enum */ KeyAbsentBehavior INVERTED_INSERTION_INDEX = new KeyAbsentBehavior("INVERTED_INSERTION_INDEX", 2){

            @Override
            public int resultIndex(int n2) {
                return ~ n2;
            }
        };
        private static final /* synthetic */ KeyAbsentBehavior[] $VALUES;

        public static KeyAbsentBehavior[] values() {
            return (KeyAbsentBehavior[])$VALUES.clone();
        }

        private KeyAbsentBehavior() {
            super(string, n2);
        }

        abstract int resultIndex(int var1);

        static {
            $VALUES = new KeyAbsentBehavior[]{NEXT_LOWER, NEXT_HIGHER, INVERTED_INSERTION_INDEX};
        }

    }

    public static abstract class KeyPresentBehavior
    extends Enum<KeyPresentBehavior> {
        public static final /* enum */ KeyPresentBehavior ANY_PRESENT = new KeyPresentBehavior("ANY_PRESENT", 0){

            @Override
            <E> int resultIndex(Comparator<? super E> comparator, E e2, List<? extends E> list, int n2) {
                return n2;
            }
        };
        public static final /* enum */ KeyPresentBehavior LAST_PRESENT = new KeyPresentBehavior("LAST_PRESENT", 1){

            @Override
            <E> int resultIndex(Comparator<? super E> comparator, E e2, List<? extends E> list, int n2) {
                int n3 = n2;
                int n4 = list.size() - 1;
                while (n3 < n4) {
                    int n5 = n3 + n4 + 1 >>> 1;
                    int n6 = comparator.compare(list.get(n5), e2);
                    if (n6 > 0) {
                        n4 = n5 - 1;
                        continue;
                    }
                    n3 = n5;
                }
                return n3;
            }
        };
        public static final /* enum */ KeyPresentBehavior FIRST_PRESENT = new KeyPresentBehavior("FIRST_PRESENT", 2){

            @Override
            <E> int resultIndex(Comparator<? super E> comparator, E e2, List<? extends E> list, int n2) {
                int n3 = 0;
                int n4 = n2;
                while (n3 < n4) {
                    int n5 = n3 + n4 >>> 1;
                    int n6 = comparator.compare(list.get(n5), e2);
                    if (n6 < 0) {
                        n3 = n5 + 1;
                        continue;
                    }
                    n4 = n5;
                }
                return n3;
            }
        };
        public static final /* enum */ KeyPresentBehavior FIRST_AFTER = new KeyPresentBehavior("FIRST_AFTER", 3){

            @Override
            public <E> int resultIndex(Comparator<? super E> comparator, E e2, List<? extends E> list, int n2) {
                return LAST_PRESENT.resultIndex(comparator, e2, list, n2) + 1;
            }
        };
        public static final /* enum */ KeyPresentBehavior LAST_BEFORE = new KeyPresentBehavior("LAST_BEFORE", 4){

            @Override
            public <E> int resultIndex(Comparator<? super E> comparator, E e2, List<? extends E> list, int n2) {
                return FIRST_PRESENT.resultIndex(comparator, e2, list, n2) - 1;
            }
        };
        private static final /* synthetic */ KeyPresentBehavior[] $VALUES;

        public static KeyPresentBehavior[] values() {
            return (KeyPresentBehavior[])$VALUES.clone();
        }

        private KeyPresentBehavior() {
            super(string, n2);
        }

        abstract <E> int resultIndex(Comparator<? super E> var1, E var2, List<? extends E> var3, int var4);

        static {
            $VALUES = new KeyPresentBehavior[]{ANY_PRESENT, LAST_PRESENT, FIRST_PRESENT, FIRST_AFTER, LAST_BEFORE};
        }

    }

}

