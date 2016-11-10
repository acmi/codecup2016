/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.io;

import de.schlichtherle.truezip.io.InputException;
import de.schlichtherle.truezip.util.ThreadGroups;
import de.schlichtherle.truezip.util.Throwables;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Queue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Streams {
    private static final ExecutorService executor = Executors.newCachedThreadPool(new ReaderThreadFactory());

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            Streams.cat(inputStream, outputStream);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException iOException) {
                throw new InputException(iOException);
            }
            finally {
                outputStream.close();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static void cat(InputStream var0, OutputStream var1_1) throws IOException {
        if (null == var0) throw new NullPointerException();
        if (null == var1_1) {
            throw new NullPointerException();
        }
        var2_2 = new ReentrantLock();
        var3_3 = var2_2.newCondition();
        var4_4 = Buffer.allocate();
        var5_5 = false;
        try {
            block23 : {
                var6_6 = new ReaderTask(var0, var4_4, (Lock)var2_2, var3_3);
                var7_7 = Streams.executor.submit(var6_6);
                var8_8 = var4_4.length;
                do lbl-1000: // 3 sources:
                {
                    var2_2.lock();
                    try {
                        while (0 >= var6_6.size) {
                            try {
                                var3_3.await();
                            }
                            catch (InterruptedException var12_14) {
                                var5_5 = true;
                            }
                        }
                        var10_10 = var6_6.off;
                        var11_12 = var4_4[var10_10];
                    }
                    finally {
                        var2_2.unlock();
                    }
                    var9_9 = var11_12.read;
                    if (var9_9 == -1) {
                        var1_1.flush();
                        var10_11 = var6_6.exception;
                        if (null == var10_11) return;
                        if (var10_11 instanceof InputException) {
                            throw (InputException)var10_11;
                        }
                        break block23;
                    }
                    try {
                        var12_13 = var11_12.buf;
                        var1_1.write(var12_13, 0, var9_9);
                    }
                    catch (IOException var12_15) {
                        Streams.cancel(var7_7);
                        throw var12_15;
                    }
                    catch (RuntimeException var12_16) {
                        Streams.cancel(var7_7);
                        throw var12_16;
                    }
                    catch (Error var12_17) {
                        Streams.cancel(var7_7);
                        throw var12_17;
                    }
                    var2_2.lock();
                    try {
                        var6_6.off = (var10_10 + 1) % var8_8;
                        --var6_6.size;
                        var3_3.signal();
                    }
                    finally {
                        var2_2.unlock();
                        continue;
                    }
                    break;
                } while (true);
                ** GOTO lbl-1000
            }
            if (var10_11 instanceof IOException) {
                throw new InputException((IOException)var10_11);
            }
            if (var10_11 instanceof RuntimeException == false) throw (Error)Throwables.wrap(var10_11);
            throw (RuntimeException)Throwables.wrap(var10_11);
        }
        finally {
            if (var5_5) {
                Thread.currentThread().interrupt();
            }
            Buffer.release(var4_4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void cancel(Future<?> future) {
        future.cancel(true);
        boolean bl = false;
        try {
            do {
                try {
                    future.get();
                    break;
                }
                catch (CancellationException cancellationException) {
                    break;
                }
                catch (ExecutionException executionException) {
                    throw new AssertionError(executionException);
                }
                catch (InterruptedException interruptedException) {
                    bl = true;
                    continue;
                }
                break;
            } while (true);
        }
        finally {
            if (bl) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static final class ReaderThread
    extends Thread {
        ReaderThread(Runnable runnable) {
            super(ThreadGroups.getServerThreadGroup(), runnable, ReaderThread.class.getName());
            this.setDaemon(true);
        }
    }

    private static final class ReaderThreadFactory
    implements ThreadFactory {
        private ReaderThreadFactory() {
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return new ReaderThread(runnable);
        }
    }

    private static final class Buffer {
        static final Queue<Reference<Buffer[]>> queue = new ConcurrentLinkedQueue<Reference<Buffer[]>>();
        final byte[] buf = new byte[8192];
        int read;

        private Buffer() {
        }

        static Buffer[] allocate() {
            Buffer[] arrbuffer;
            while (null != (arrbuffer = queue.poll())) {
                Buffer[] arrbuffer2 = arrbuffer.get();
                if (null == arrbuffer2) continue;
                return arrbuffer2;
            }
            arrbuffer = new Buffer[4];
            int n2 = arrbuffer.length;
            while (0 <= --n2) {
                arrbuffer[n2] = new Buffer();
            }
            return arrbuffer;
        }

        static void release(Buffer[] arrbuffer) {
            queue.add(new SoftReference<Buffer[]>(arrbuffer));
        }
    }

}

