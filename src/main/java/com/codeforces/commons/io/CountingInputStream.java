/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public final class CountingInputStream
extends InputStream {
    private static final ReadEvent EMPTY_READ_EVENT = new ReadEvent(){

        @Override
        public void onRead(long l2, long l3) {
        }
    };
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicLong totalReadByteCount = new AtomicLong();
    private final InputStream inputStream;
    private final ReadEvent readEvent;

    public CountingInputStream(InputStream inputStream, ReadEvent readEvent) {
        this.inputStream = inputStream;
        this.readEvent = readEvent;
    }

    @Override
    public int read() throws IOException {
        if (this.lock.isHeldByCurrentThread()) {
            return this.inputStream.read();
        }
        this.lock.lock();
        try {
            int n2 = this.inputStream.read();
            if (n2 != -1) {
                this.readEvent.onRead(1, this.totalReadByteCount.incrementAndGet());
            }
            int n3 = n2;
            return n3;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int read(byte[] arrby) throws IOException {
        if (this.lock.isHeldByCurrentThread()) {
            return this.inputStream.read(arrby);
        }
        this.lock.lock();
        try {
            int n2 = this.inputStream.read(arrby);
            if (n2 > 0) {
                this.readEvent.onRead(n2, this.totalReadByteCount.addAndGet(n2));
            }
            int n3 = n2;
            return n3;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int read(byte[] arrby, int n2, int n3) throws IOException {
        if (this.lock.isHeldByCurrentThread()) {
            return this.inputStream.read(arrby, n2, n3);
        }
        this.lock.lock();
        try {
            int n4 = this.inputStream.read(arrby, n2, n3);
            if (n4 > 0) {
                this.readEvent.onRead(n4, this.totalReadByteCount.addAndGet(n4));
            }
            int n5 = n4;
            return n5;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long skip(long l2) throws IOException {
        if (this.lock.isHeldByCurrentThread()) {
            return this.inputStream.skip(l2);
        }
        this.lock.lock();
        try {
            long l3 = this.inputStream.skip(l2);
            if (l3 > 0) {
                this.readEvent.onRead(l3, this.totalReadByteCount.addAndGet(l2));
            }
            long l4 = l3;
            return l4;
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public int available() throws IOException {
        this.lock.lock();
        try {
            int n2 = this.inputStream.available();
            return n2;
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        this.lock.lock();
        try {
            this.inputStream.close();
        }
        finally {
            this.lock.unlock();
        }
    }

    public static interface ReadEvent {
        public void onRead(long var1, long var3) throws IOException;
    }

}

