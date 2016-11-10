/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a.a;

import com.a.b.a.a.e.a.a.c;
import com.a.b.a.a.e.a.a.e;
import com.a.b.a.a.e.a.f;
import com.codeforces.commons.io.FileUtil;
import com.codeforces.commons.io.IoUtil;
import com.codeforces.commons.math.Math;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.process.ThreadUtil;
import com.codeforces.commons.text.StringUtil;
import com.codeforces.jrun.Outcome;
import com.codeforces.jrun.Params;
import com.codeforces.jrun.ProcessRunner;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class d
implements c {
    private static final Logger a = LoggerFactory.getLogger(d.class);
    private static final int b = IoUtil.BUFFER_SIZE;
    private static final ByteOrder c = ByteOrder.LITTLE_ENDIAN;
    private final AtomicBoolean d = new AtomicBoolean();
    private final int e;
    private ServerSocket f;
    private Socket g;
    private InputStream h;
    private OutputStream i;
    private final ByteArrayOutputStream j;
    private final File k;
    private OutputStream l;

    protected d(com.a.b.a.a.a.b b2, File file) {
        this.e = NumberUtil.toInt(b2.l() ? TimeUnit.MINUTES.toMillis(30) : Math.max(TimeUnit.SECONDS.toMillis(10), 10000));
        this.j = new ByteArrayOutputStream(b);
        this.k = file;
    }

    @Override
    public void a(int n2) {
        try {
            FileUtil.executeIoOperation(new e(this, n2), 2, 1000, ThreadUtil.ExecutionStrategy.Type.SQUARE);
        }
        catch (IOException iOException) {
            String string = String.format("Can't start %s on %d.", this.getClass().getSimpleName(), n2);
            a.error(string, iOException);
            Outcome a2 = ProcessRunner.run("netstat -n -a -b -o", new Params.Builder().setTimeLimit(10000).newInstance());
            if (StringUtil.isNotBlank(a2.getOutput())) {
                a.error("'netstat -n -a -b -o' outputs: " + a2.getOutput());
            }
            throw new f(string, iOException);
        }
    }

    @Override
    public void a() {
        IoUtil.closeQuietly((AutoCloseable)this.g);
        try {
            this.g = this.f.accept();
            this.g.setSoTimeout(this.e);
            this.g.setSendBufferSize(b);
            this.g.setReceiveBufferSize(b);
            this.g.setTcpNoDelay(true);
            this.h = this.g.getInputStream();
            this.i = this.g.getOutputStream();
            if (this.k != null) {
                this.l = new FileOutputStream(this.k);
            }
        }
        catch (IOException iOException) {
            throw new f("Can't accept remote process connection.", iOException);
        }
    }

    protected final <E> void a(E[] arrE, a<E> a2) {
        if (arrE == null) {
            this.c(-1);
        } else {
            int n2 = arrE.length;
            this.c(n2);
            for (int i2 = 0; i2 < n2; ++i2) {
                a2.write(arrE[i2]);
            }
        }
    }

    protected final void a(Object object, boolean bl) {
        if (object == null) {
            if (bl) {
                this.c(-1);
            }
        } else {
            int n2 = Array.getLength(object);
            if (bl) {
                this.c(n2);
            }
            Class class_ = object.getClass().getComponentType();
            for (int i2 = 0; i2 < n2; ++i2) {
                Object object2 = Array.get(object, i2);
                if (class_.isArray()) {
                    this.a(object2, bl);
                    continue;
                }
                if (class_.isEnum()) {
                    this.a((Enum)object2);
                    continue;
                }
                if (class_ == String.class) {
                    this.a((String)object2);
                    continue;
                }
                if (object2 == null) {
                    this.b(false);
                    continue;
                }
                if (class_ == Boolean.class || class_ == Boolean.TYPE) {
                    this.b((Boolean)object2);
                    continue;
                }
                if (class_ == Integer.class || class_ == Integer.TYPE) {
                    this.c((Integer)object2);
                    continue;
                }
                if (class_ == Long.class || class_ == Long.TYPE) {
                    this.a((Long)object2);
                    continue;
                }
                if (class_ == Double.class || class_ == Double.TYPE) {
                    this.a((Double)object2);
                    continue;
                }
                throw new IllegalArgumentException("Unsupported array item class: " + class_ + '.');
            }
        }
    }

    protected final byte[] a(boolean bl) {
        int n2 = this.j();
        if (bl) {
            if (n2 < 0) {
                return null;
            }
        } else if (n2 <= 0) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
        return this.d(n2);
    }

    protected final <E extends Enum> E a(Class<E> class_) {
        byte by = this.d(1)[0];
        for (Enum enum_ : (Enum[])class_.getEnumConstants()) {
            if (enum_.ordinal() != by) continue;
            return (E)enum_;
        }
        return null;
    }

    protected final <E extends Enum> void a(E e2) {
        this.e(e2 == null ? -1 : e2.ordinal());
    }

    protected final String h() {
        int n2 = this.j();
        if (n2 < 0) {
            return null;
        }
        return new String(this.d(n2), StandardCharsets.UTF_8);
    }

    protected final void a(String string) {
        if (string == null) {
            this.c(-1);
            return;
        }
        byte[] arrby = string.getBytes(StandardCharsets.UTF_8);
        this.c(arrby.length);
        this.a(arrby);
    }

    protected final boolean i() {
        return this.d(1)[0] != 0;
    }

    protected final void b(boolean bl) {
        this.e(bl ? 1 : 0);
    }

    protected final int j() {
        return ByteBuffer.wrap(this.d(4)).order(c).getInt();
    }

    protected final void c(int n2) {
        this.a(ByteBuffer.allocate(4).order(c).putInt(n2).array());
    }

    protected final long k() {
        return ByteBuffer.wrap(this.d(8)).order(c).getLong();
    }

    protected final void a(long l2) {
        this.a(ByteBuffer.allocate(8).order(c).putLong(l2).array());
    }

    protected final double l() {
        return Double.longBitsToDouble(this.k());
    }

    protected final void a(double d2) {
        this.a(Double.doubleToLongBits(d2));
    }

    protected final byte[] d(int n2) {
        this.o();
        try {
            return IOUtils.toByteArray(this.h, n2);
        }
        catch (IOException iOException) {
            throw new f(String.format("Can't read %d bytes from input stream.", n2), iOException);
        }
    }

    protected final void a(byte[] arrby) {
        this.o();
        try {
            this.j.write(arrby);
        }
        catch (IOException iOException) {
            throw new f(String.format("Can't write %d bytes into output stream.", arrby.length), iOException);
        }
    }

    private void e(int n2) {
        this.o();
        try {
            this.j.write(n2);
        }
        catch (RuntimeException runtimeException) {
            throw new f("Can't write a byte into output stream.", runtimeException);
        }
    }

    protected final void m() {
        this.o();
        try {
            byte[] arrby = this.j.toByteArray();
            this.j.reset();
            this.i.write(arrby);
            this.i.flush();
            if (this.l != null) {
                this.l.write(arrby);
            }
        }
        catch (IOException iOException) {
            throw new f("Can't flush output stream.", iOException);
        }
    }

    private void o() {
        if (this.d.get()) {
            throw new IllegalStateException(String.format("%s is stopped.", this.getClass()));
        }
    }

    @Override
    public void f() {
        IoUtil.closeQuietly((AutoCloseable)this.f);
        this.f = null;
    }

    @Override
    public void g() {
        if (!this.d.compareAndSet(false, true)) {
            return;
        }
        IoUtil.closeQuietly(this.l, this.g, this.f);
    }

    protected void finalize() throws Throwable {
        this.g();
        super.finalize();
    }

    static /* synthetic */ ServerSocket a(d d2, ServerSocket serverSocket) {
        d2.f = serverSocket;
        return d2.f;
    }

    static /* synthetic */ int a(d d2) {
        return d2.e;
    }

    static /* synthetic */ ServerSocket b(d d2) {
        return d2.f;
    }

    static /* synthetic */ int n() {
        return b;
    }

    protected static interface a<E> {
        public void write(E var1);
    }

}

