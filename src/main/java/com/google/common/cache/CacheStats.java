/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class CacheStats {
    private final long hitCount;
    private final long missCount;
    private final long loadSuccessCount;
    private final long loadExceptionCount;
    private final long totalLoadTime;
    private final long evictionCount;

    public CacheStats(long l2, long l3, long l4, long l5, long l6, long l7) {
        Preconditions.checkArgument(l2 >= 0);
        Preconditions.checkArgument(l3 >= 0);
        Preconditions.checkArgument(l4 >= 0);
        Preconditions.checkArgument(l5 >= 0);
        Preconditions.checkArgument(l6 >= 0);
        Preconditions.checkArgument(l7 >= 0);
        this.hitCount = l2;
        this.missCount = l3;
        this.loadSuccessCount = l4;
        this.loadExceptionCount = l5;
        this.totalLoadTime = l6;
        this.evictionCount = l7;
    }

    public int hashCode() {
        return Objects.hashCode(this.hitCount, this.missCount, this.loadSuccessCount, this.loadExceptionCount, this.totalLoadTime, this.evictionCount);
    }

    public boolean equals(Object object) {
        if (object instanceof CacheStats) {
            CacheStats cacheStats = (CacheStats)object;
            return this.hitCount == cacheStats.hitCount && this.missCount == cacheStats.missCount && this.loadSuccessCount == cacheStats.loadSuccessCount && this.loadExceptionCount == cacheStats.loadExceptionCount && this.totalLoadTime == cacheStats.totalLoadTime && this.evictionCount == cacheStats.evictionCount;
        }
        return false;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("hitCount", this.hitCount).add("missCount", this.missCount).add("loadSuccessCount", this.loadSuccessCount).add("loadExceptionCount", this.loadExceptionCount).add("totalLoadTime", this.totalLoadTime).add("evictionCount", this.evictionCount).toString();
    }
}

