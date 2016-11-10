/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a.a;

import com.a.b.a.a.e.a.a.b;
import com.a.b.a.a.e.a.a.g;
import com.codeforces.commons.compress.ZipUtil;
import com.codeforces.commons.io.FileUtil;
import com.codeforces.commons.io.IoUtil;
import com.codeforces.commons.resource.ResourceUtil;
import com.codeforces.commons.text.StringUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class a {
    private static final Logger a = LoggerFactory.getLogger(a.class);
    private final String b;
    private final Process c;
    private final File d;
    private final AtomicBoolean e = new AtomicBoolean();

    private a(String string, Process process, File file) {
        this.b = string;
        this.c = process;
        this.d = file;
    }

    public File a() {
        return this.d;
    }

    public void a(long l2) {
        Thread thread = new Thread(() -> {
            try {
                this.c.waitFor();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        );
        thread.start();
        try {
            thread.join(l2);
        }
        catch (InterruptedException interruptedException) {
            thread.interrupt();
        }
    }

    public void b() {
        if (!this.e.compareAndSet(false, true)) {
            return;
        }
        this.c.destroy();
        try {
            this.c.waitFor();
            a.info("Process finished with exit code '" + this.c.exitValue() + "'.");
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    protected void finalize() throws Throwable {
        if (!this.e.get()) {
            a.error(String.format("Process '%s' in directory '%s' has not been destroyed.", this.b, this.d.getAbsolutePath()));
        }
        this.b();
        super.finalize();
    }

    public static /* varargs */ a a(String string, Map<String, String> map, File file, String ... arrstring) throws IOException {
        b b2;
        Object object;
        File file2 = new File(string);
        File file3 = a.a(file2.getParentFile());
        String string2 = FilenameUtils.getExtension(file2.getName());
        if ("zip".equalsIgnoreCase(string2)) {
            b2 = g.a(file2.getName().substring(0, file2.getName().length() - ".zip".length()));
            ZipUtil.unzip(file2, file3);
        } else {
            b2 = g.a(string);
            object = new File(file3, file2.getName());
            FileUtil.createSymbolicLinkOrCopy(file2, (File)object);
        }
        a.a(b2, file3, file, map);
        object = b2.a(FilenameUtils.getName(string), map);
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(a.a(file3, (String)object)));
        Collections.addAll(arrayList, arrstring);
        if (!new File(arrayList.get(0)).isAbsolute()) {
            arrayList.set(0, new File(file3, arrayList.get(0)).getAbsolutePath());
        }
        String string3 = a.a(arrayList);
        File file4 = new File(file3, "run.bat");
        if (!FileUtil.isFile(file4)) {
            FileUtil.writeFile(file4, string3.getBytes(StandardCharsets.UTF_8));
        }
        Process process = new ProcessBuilder(arrayList).directory(file3).start();
        a.a(process, process.getInputStream(), new File(file3, "runexe.output"));
        a.a(process, process.getErrorStream(), new File(file3, "runexe.error"));
        a.info("Running '" + string3 + "' in the '" + file3 + "'.");
        return new a((String)object, process, file3);
    }

    private static void a(Process process, InputStream inputStream, File file) {
        new Thread(() -> {
            try {
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
            catch (IOException iOException) {
                // empty catch block
            }
            a.debug("Completed to write stream from " + process + " to '" + file + "'.");
        }
        ).start();
    }

    private static String a(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : list) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(' ');
            }
            stringBuilder.append('\"').append(string).append('\"');
        }
        return stringBuilder.toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void a(b b2, File file, File file2, Map<String, String> map) throws IOException {
        for (String string : b2.a()) {
            if (map != null && b2.a(string)) {
                String string2;
                InputStream inputStream = null;
                try {
                    inputStream = a.class.getResourceAsStream(string);
                    string2 = new String(IoUtil.toByteArray(inputStream), StandardCharsets.UTF_8);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        string2 = StringUtil.replace(string2, "${" + entry.getKey() + '}', entry.getValue());
                    }
                }
                finally {
                    IoUtil.closeQuietly((AutoCloseable)inputStream);
                }
                File file3 = new File(file, new File(string).getName());
                ResourceUtil.saveResourceToFile(file3, null, string2.getBytes(StandardCharsets.UTF_8), null);
                continue;
            }
            ResourceUtil.copyResourceToDir(file, file2, string, null, a.class, false);
        }
    }

    private static File a(File file) throws IOException {
        File file2;
        while ((file2 = new File(file, String.format("%s-%s", new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(new Date()), RandomStringUtils.randomAlphanumeric(4)))).exists()) {
        }
        if (!file2.mkdirs()) {
            throw new IOException("Can't create temporary directory '" + file2 + "'.");
        }
        return file2;
    }

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
}

