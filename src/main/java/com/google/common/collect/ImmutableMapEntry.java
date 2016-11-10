/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableEntry;

class ImmutableMapEntry<K, V>
extends ImmutableEntry<K, V> {
    static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int n2) {
        return new ImmutableMapEntry[n2];
    }

    ImmutableMapEntry(K k2, V v2) {
        super(k2, v2);
        CollectPreconditions.checkEntryNotNull(k2, v2);
    }

    ImmutableMapEntry<K, V> getNextInKeyBucket() {
        return null;
    }

    ImmutableMapEntry<K, V> getNextInValueBucket() {
        return null;
    }

    boolean isReusable() {
        return true;
    }

    static class NonTerminalImmutableMapEntry<K, V>
    extends ImmutableMapEntry<K, V> {
        private final transient ImmutableMapEntry<K, V> nextInKeyBucket;

        NonTerminalImmutableMapEntry(K k2, V v2, ImmutableMapEntry<K, V> immutableMapEntry) {
            super(k2, v2);
            this.nextInKeyBucket = immutableMapEntry;
        }

        @Override
        final ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }

        @Override
        final boolean isReusable() {
            return false;
        }
    }

}

