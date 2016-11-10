/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.a;

import com.codeforces.a.a;
import com.codeforces.a.b;
import com.codeforces.a.d;
import com.codeforces.a.e;
import com.codeforces.a.f;
import com.codeforces.a.g;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class c {
    private static String[] a(File file, String string) {
        if (new File(file, string).exists()) {
            return new String[]{string};
        }
        string = string + " ";
        boolean bl = false;
        boolean bl2 = false;
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 == '\\') {
                if (bl ^= true) continue;
                stringBuilder.append('\\');
                continue;
            }
            if (c2 == '\"') {
                if (!bl) {
                    bl2 = !bl2;
                } else {
                    stringBuilder.append('\"');
                }
            } else {
                if (bl) {
                    stringBuilder.append('\\');
                }
                if (c2 <= ' ' && !bl2) {
                    if (stringBuilder.length() > 0) {
                        arrayList.add(stringBuilder.toString());
                        stringBuilder.setLength(0);
                    }
                } else {
                    stringBuilder.append(c2);
                }
            }
            bl = false;
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static <T> T a(Callable<T> callable, long l2, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            FutureTask<T> futureTask = new FutureTask<T>(callable);
            executorService.execute(futureTask);
            T t2 = futureTask.get(l2, timeUnit);
            return t2;
        }
        finally {
            executorService.shutdown();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static a a(String string, b b2) {
        String[] arrstring = c.a(b2.a(), string);
        ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
        processBuilder.directory(b2.a());
        processBuilder.command(arrstring);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        ArrayList<String> arrayList = new ArrayList<String>();
        int n2 = -1;
        try {
            Process process = processBuilder.start();
            d d2 = null;
            if (b2.c() != null) {
                d2 = new d(process, b2, arrayList);
            } else {
                process.getOutputStream().close();
            }
            e e2 = new e(process, b2, arrayList, stringBuilder);
            f f2 = new f(process, b2, arrayList, stringBuilder2);
            if (d2 != null) {
                d2.start();
            }
            e2.start();
            f2.start();
            try {
                n2 = (Integer)c.a(new g(process), b2.b() == 0 ? Integer.MAX_VALUE : b2.b(), TimeUnit.MILLISECONDS);
            }
            catch (TimeoutException timeoutException) {
                arrayList.add("Process timed out [timeLimit=" + b2.b() + ']');
            }
            catch (ExecutionException executionException) {
                arrayList.add("Process failed [commandLine=" + string + ']');
            }
            finally {
                if (d2 != null) {
                    d2.join(TimeUnit.MINUTES.toMillis(1));
                }
                e2.join(TimeUnit.MINUTES.toMillis(1));
                f2.join(TimeUnit.MINUTES.toMillis(1));
                process.destroy();
            }
            return new a(n2, stringBuilder.toString(), stringBuilder2.toString(), arrayList);
        }
        catch (Exception exception) {
            return new a(-1, stringBuilder.toString(), stringBuilder2.toString(), Arrays.asList(exception.getMessage()));
        }
    }
}

