/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.d;

import com.a.b.a.a.b.e.f;
import com.a.b.a.a.c.i;
import com.a.b.a.a.d.j;
import com.a.b.a.a.d.k;
import com.codeforces.commons.io.FileUtil;
import com.codeforces.commons.io.IoUtil;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class l
implements j {
    private final ExecutorService a = Executors.newSingleThreadExecutor();
    private final AtomicReference<Throwable> b = new AtomicReference();
    private final Writer c;
    private final f d;

    public l(File file) throws IOException {
        FileUtil.ensureParentDirectoryExists(file);
        this.c = new OutputStreamWriter((OutputStream)new BufferedOutputStream(new FileOutputStream(file, false), IoUtil.BUFFER_SIZE), StandardCharsets.UTF_8);
        this.d = new f();
    }

    @Override
    public void a(i i2) throws IOException {
        this.a();
        this.a.execute(() -> {
            if (this.b.get() != null) {
                return;
            }
            try {
                this.c.write(this.d.a(i2));
                this.c.write(10);
            }
            catch (IOException | Error | RuntimeException throwable) {
                this.a(throwable);
            }
        }
        );
    }

    @Override
    public void close() throws IOException {
        try {
            this.a.shutdown();
            try {
                if (!this.a.awaitTermination(2, TimeUnit.MINUTES)) {
                    this.a(new IOException("Can't write game log file in the allotted time."));
                }
            }
            catch (InterruptedException interruptedException) {
                this.a(new IOException("Unexpectedly interrupted while writing game log file.", interruptedException));
            }
            this.a();
            this.c.close();
        }
        catch (IOException | Error | RuntimeException throwable) {
            IoUtil.closeQuietly((AutoCloseable)this.c);
            throw throwable;
        }
    }

    private void a(Throwable throwable) {
        this.b.compareAndSet((Throwable)null, throwable);
    }

    private void a() throws IOException {
        k.a(this.b.get());
    }
}

