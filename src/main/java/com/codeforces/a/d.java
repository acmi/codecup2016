/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.a;

import com.codeforces.a.b;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

static final class d
extends Thread {
    final /* synthetic */ Process a;
    final /* synthetic */ b b;
    final /* synthetic */ List c;

    d(Process process, b b2, List list) {
        this.a = process;
        this.b = b2;
        this.c = list;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(this.a.getOutputStream());
        InputStream inputStream = null;
        try {
            int n2;
            inputStream = new BufferedInputStream(new FileInputStream(this.b.c()));
            byte[] arrby = new byte[1048576];
            while ((n2 = inputStream.read(arrby)) != -1) {
                bufferedOutputStream.write(arrby, 0, n2);
                bufferedOutputStream.flush();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            this.c.add("Can't find input file " + this.b.c() + '.');
        }
        catch (IOException iOException) {
            this.c.add("Can't read input file " + this.b.c() + '.');
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException iOException) {}
            try {
                bufferedOutputStream.close();
            }
            catch (IOException iOException) {}
        }
    }
}

