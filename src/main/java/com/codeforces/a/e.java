/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.a;

import com.codeforces.a.b;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

static final class e
extends Thread {
    final /* synthetic */ Process a;
    final /* synthetic */ b b;
    final /* synthetic */ List c;
    final /* synthetic */ StringBuilder d;

    e(Process process, b b2, List list, StringBuilder stringBuilder) {
        this.a = process;
        this.b = b2;
        this.c = list;
        this.d = stringBuilder;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        FileOutputStream fileOutputStream;
        InputStream inputStream = this.a.getInputStream();
        BufferedReader bufferedReader = null;
        if (this.b.d() == null) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        }
        try {
            fileOutputStream = this.b.d() == null ? null : new FileOutputStream(this.b.d());
        }
        catch (IOException iOException) {
            this.c.add("Can't write output file " + this.b.d() + '.');
            return;
        }
        try {
            char[] arrc = new char[1048576];
            byte[] arrby = new byte[1048576];
            do {
                int n2;
                if (this.b.d() == null) {
                    n2 = bufferedReader.read(arrc);
                    if (n2 != -1) {
                        if (this.d.length() >= 5242880) continue;
                        n2 = Math.min(n2, 5242880 - this.d.length());
                        this.d.append(arrc, 0, n2);
                        continue;
                    }
                } else {
                    n2 = inputStream.read(arrby);
                    if (n2 != -1) {
                        fileOutputStream.write(arrby, 0, n2);
                        continue;
                    }
                }
                break;
                break;
            } while (true);
        }
        catch (IOException iOException) {
            this.c.add("Can't handle output of the process.");
        }
        finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
            catch (IOException iOException) {}
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
            catch (IOException iOException) {}
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException iOException) {}
        }
    }
}

