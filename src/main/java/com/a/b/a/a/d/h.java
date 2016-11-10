/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.d;

import com.a.b.a.a.a.b;
import com.a.b.a.a.b.e.f;
import com.a.b.a.a.d.i;
import com.a.b.a.a.d.j;
import com.a.b.a.a.d.k;
import com.codeforces.commons.io.FileUtil;
import com.codeforces.commons.io.http.HttpMethod;
import com.codeforces.commons.io.http.HttpRequest;
import com.codeforces.commons.io.http.HttpResponse;
import com.codeforces.commons.io.http.HttpUtil;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.process.ThreadUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.Logger;

public class h
implements j {
    private static final Logger a = Logger.getLogger(h.class);
    private final ExecutorService b = Executors.newSingleThreadExecutor();
    private final AtomicReference<Throwable> c = new AtomicReference();
    private final String d;
    private final b e;
    private final f f;
    private int g;
    private boolean h;
    private final StringBuilder i = new StringBuilder(NumberUtil.toInt(0x10000000));
    private StringBuilder j = h.b();

    public h(String string, b b2) {
        this.d = string;
        this.e = b2;
        this.f = new f();
        this.a("");
    }

    @Override
    public void a(com.a.b.a.a.c.i i2) throws IOException {
        this.a();
        this.b.execute(() -> {
            if (this.c.get() != null) {
                return;
            }
            try {
                String string = this.f.a(i2);
                ++this.g;
                this.i.append(string).append('\n');
                this.j.append(string).append('\n');
                if (h.b(i2)) {
                    this.a("", false);
                }
            }
            catch (Error | RuntimeException throwable) {
                this.a(throwable);
            }
        }
        );
    }

    @Override
    public void close() throws IOException {
        this.b.shutdown();
        try {
            if (!this.b.awaitTermination(10, TimeUnit.MINUTES)) {
                this.a(new IOException("Can't write game log file in the allotted time."));
            }
        }
        catch (InterruptedException interruptedException) {
            this.a(new IOException("Unexpectedly interrupted while writing game log file.", interruptedException));
        }
        this.a();
        this.a("", true);
        FileUtil.executeIoOperation(() -> {
            if (this.h) {
                this.h = false;
                this.a("");
                this.a(this.i.toString(), this.d, "", true);
                if (this.h) {
                    throw new IOException("Can't save complete document '" + this.d + "'.");
                }
            }
            return null;
        }
        , 4, 60000, ThreadUtil.ExecutionStrategy.Type.LINEAR);
        try {
            this.h = false;
            this.a("-meta");
            this.a(new GsonBuilder().create().toJson(new a(InetAddress.getLocalHost().getHostName(), new Date(), this.g, this.e.d(), this.e.e(), this.e.r(), this.e.s(), null)), this.d, "-meta", true);
        }
        catch (RuntimeException runtimeException) {
            // empty catch block
        }
    }

    private static boolean b(com.a.b.a.a.c.i i2) {
        return (i2.getTickIndex() + 1) % 500 == 0;
    }

    private void a(String string) {
        String string2 = this.d + string + "/begin";
        try {
            HttpResponse httpResponse = HttpUtil.executePostRequestAndReturnResponse(30000, string2, new Object[0]);
            if (httpResponse.hasIoException()) {
                throw httpResponse.getIoException();
            }
            if (httpResponse.getCode() != 200) {
                throw new IOException(String.format("Got unexpected %s from remote storage '%s' while creating new document.", httpResponse, string2));
            }
        }
        catch (IOException iOException) {
            a.error("Got I/O-exception while starting document '" + string2 + "'.", iOException);
            this.h = true;
        }
    }

    private void a(String string, boolean bl) {
        this.a(this.j.toString(), this.d, string, bl);
        this.j = h.b();
    }

    private void a(String string, String string2, String string3, boolean bl) {
        if (this.h) {
            return;
        }
        String string4 = string2 + string3 + '/' + (bl ? "end" : "append");
        try {
            h.a(string, string4);
        }
        catch (IOException iOException) {
            a.error("Got I/O-exception while appending document '" + string4 + "'.", iOException);
            this.h = true;
        }
    }

    private void a(Throwable throwable) {
        this.c.compareAndSet((Throwable)null, throwable);
    }

    private void a() throws IOException {
        k.a(this.c.get());
    }

    private static void a(String string, String string2) throws IOException {
        byte[] arrby = string.getBytes(StandardCharsets.UTF_8);
        String string3 = FileUtil.formatSize(arrby.length);
        a.info("Started to send a batch (" + string3 + ").");
        HttpResponse httpResponse = HttpRequest.create(string2, new Object[0]).setMethod(HttpMethod.POST).setBinaryEntity(arrby).setGzip(true).setTimeoutMillis(120000).executeAndReturnResponse();
        if (httpResponse.getCode() != 200) {
            throw new IOException(String.format("Got unexpected %s from remote storage '%s' while appending document.", httpResponse, string2));
        }
        a.info("Finished to send a batch (" + string3 + ").");
    }

    private static StringBuilder b() {
        return new StringBuilder(NumberUtil.toInt(0x2000000));
    }

    private static final class a {
        private final String a;
        private final Date b;
        private final int c;
        private final double d;
        private final double e;
        private final boolean f;
        private final boolean g;

        private a(String string, Date date, int n2, double d2, double d3, boolean bl, boolean bl2) {
            this.a = string;
            this.b = date;
            this.c = n2;
            this.d = d2;
            this.e = d3;
            this.f = bl;
            this.g = bl2;
        }

        /* synthetic */ a(String string, Date date, int n2, double d2, double d3, boolean bl, boolean bl2, i i2) {
            this(string, date, n2, d2, d3, bl, bl2);
        }
    }

}

