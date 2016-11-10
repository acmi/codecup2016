/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

interface CycleDetectingLock<ID> {
    public ListMultimap<Long, ID> lockOrDetectPotentialLocksCycle();

    public void unlock();

    public static class CycleDetectingLockFactory<ID> {
        private static Map<Long, ReentrantCycleDetectingLock<?>> lockThreadIsWaitingOn = Maps.newHashMap();
        private static final Multimap<Long, ReentrantCycleDetectingLock<?>> locksOwnedByThread = LinkedHashMultimap.create();

        CycleDetectingLock<ID> create(ID ID) {
            return new ReentrantCycleDetectingLock<ID>(this, ID, new ReentrantLock());
        }

        static class ReentrantCycleDetectingLock<ID>
        implements CycleDetectingLock<ID> {
            private final Lock lockImplementation;
            private final ID userLockId;
            private final CycleDetectingLockFactory<ID> lockFactory;
            private Long lockOwnerThreadId = null;
            private int lockReentranceCount = 0;

            ReentrantCycleDetectingLock(CycleDetectingLockFactory<ID> cycleDetectingLockFactory, ID ID, Lock lock) {
                this.lockFactory = cycleDetectingLockFactory;
                this.userLockId = Preconditions.checkNotNull(ID, "userLockId");
                this.lockImplementation = Preconditions.checkNotNull(lock, "lockImplementation");
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public ListMultimap<Long, ID> lockOrDetectPotentialLocksCycle() {
                long l2 = Thread.currentThread().getId();
                Class<CycleDetectingLockFactory> class_ = CycleDetectingLockFactory.class;
                synchronized (CycleDetectingLockFactory.class) {
                    this.checkState();
                    ListMultimap<Long, ID> listMultimap = this.detectPotentialLocksCycle();
                    if (!listMultimap.isEmpty()) {
                        // ** MonitorExit[var3_2] (shouldn't be in output)
                        return listMultimap;
                    }
                    lockThreadIsWaitingOn.put(l2, this);
                    // ** MonitorExit[var3_2] (shouldn't be in output)
                    this.lockImplementation.lock();
                    class_ = CycleDetectingLockFactory.class;
                    synchronized (CycleDetectingLockFactory.class) {
                        lockThreadIsWaitingOn.remove(l2);
                        this.checkState();
                        this.lockOwnerThreadId = l2;
                        ++this.lockReentranceCount;
                        locksOwnedByThread.put(l2, this);
                        // ** MonitorExit[var3_2] (shouldn't be in output)
                        return ImmutableListMultimap.of();
                    }
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void unlock() {
                long l2 = Thread.currentThread().getId();
                Class<CycleDetectingLockFactory> class_ = CycleDetectingLockFactory.class;
                synchronized (CycleDetectingLockFactory.class) {
                    this.checkState();
                    Preconditions.checkState(this.lockOwnerThreadId != null, "Thread is trying to unlock a lock that is not locked");
                    Preconditions.checkState(this.lockOwnerThreadId == l2, "Thread is trying to unlock a lock owned by another thread");
                    this.lockImplementation.unlock();
                    --this.lockReentranceCount;
                    if (this.lockReentranceCount == 0) {
                        this.lockOwnerThreadId = null;
                        Preconditions.checkState(locksOwnedByThread.remove(l2, this), "Internal error: Can not find this lock in locks owned by a current thread");
                        if (locksOwnedByThread.get(l2).isEmpty()) {
                            locksOwnedByThread.removeAll(l2);
                        }
                    }
                    // ** MonitorExit[var3_2] (shouldn't be in output)
                    return;
                }
            }

            void checkState() throws IllegalStateException {
                long l2 = Thread.currentThread().getId();
                Preconditions.checkState(!lockThreadIsWaitingOn.containsKey(l2), "Internal error: Thread should not be in a waiting thread on a lock now");
                if (this.lockOwnerThreadId != null) {
                    Preconditions.checkState(this.lockReentranceCount >= 0, "Internal error: Lock ownership and reentrance count internal states do not match");
                    Preconditions.checkState(locksOwnedByThread.get(this.lockOwnerThreadId).contains(this), "Internal error: Set of locks owned by a current thread and lock ownership status do not match");
                } else {
                    Preconditions.checkState(this.lockReentranceCount == 0, "Internal error: Reentrance count of a non locked lock is expect to be zero");
                    Preconditions.checkState(!locksOwnedByThread.values().contains(this), "Internal error: Non locked lock should not be owned by any thread");
                }
            }

            private ListMultimap<Long, ID> detectPotentialLocksCycle() {
                long l2 = Thread.currentThread().getId();
                if (this.lockOwnerThreadId == null || this.lockOwnerThreadId == l2) {
                    return ImmutableListMultimap.of();
                }
                ListMultimap<Long, ID> listMultimap = Multimaps.newListMultimap(new LinkedHashMap(), new Supplier<List<ID>>(){

                    @Override
                    public List<ID> get() {
                        return Lists.newArrayList();
                    }
                });
                ReentrantCycleDetectingLock reentrantCycleDetectingLock = this;
                while (reentrantCycleDetectingLock != null && reentrantCycleDetectingLock.lockOwnerThreadId != null) {
                    Long l3 = reentrantCycleDetectingLock.lockOwnerThreadId;
                    listMultimap.putAll(l3, this.getAllLockIdsAfter(l3, reentrantCycleDetectingLock));
                    if (l3 == l2) {
                        return listMultimap;
                    }
                    reentrantCycleDetectingLock = (ReentrantCycleDetectingLock)lockThreadIsWaitingOn.get(l3);
                }
                return ImmutableListMultimap.of();
            }

            private List<ID> getAllLockIdsAfter(long l2, ReentrantCycleDetectingLock reentrantCycleDetectingLock) {
                ArrayList<ID> arrayList = Lists.newArrayList();
                boolean bl = false;
                Collection collection = locksOwnedByThread.get(l2);
                Preconditions.checkNotNull(collection, "Internal error: No locks were found taken by a thread");
                for (ReentrantCycleDetectingLock reentrantCycleDetectingLock2 : collection) {
                    if (reentrantCycleDetectingLock2 == reentrantCycleDetectingLock) {
                        bl = true;
                    }
                    if (!bl || reentrantCycleDetectingLock2.lockFactory != this.lockFactory) continue;
                    ID ID = reentrantCycleDetectingLock2.userLockId;
                    arrayList.add(ID);
                }
                Preconditions.checkState(bl, "Internal error: We can not find locks that created a cycle that we detected");
                return arrayList;
            }

            public String toString() {
                Long l2 = this.lockOwnerThreadId;
                if (l2 != null) {
                    return String.format("%s[%s][locked by Id=%d]", super.toString(), this.userLockId, l2);
                }
                return String.format("%s[%s][unlocked]", super.toString(), this.userLockId);
            }

        }

    }

}

