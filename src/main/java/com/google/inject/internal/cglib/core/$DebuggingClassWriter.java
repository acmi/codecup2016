/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$ClassReader;
import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$ClassWriter;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class $DebuggingClassWriter
extends $ClassVisitor {
    public static final String DEBUG_LOCATION_PROPERTY = "cglib.debugLocation";
    private static String debugLocation = System.getProperty("cglib.debugLocation");
    private static Constructor traceCtor;
    private String className;
    private String superName;

    public $DebuggingClassWriter(int n2) {
        super(327680, new $ClassWriter(n2));
    }

    public void visit(int n2, int n3, String string, String string2, String string3, String[] arrstring) {
        this.className = string.replace('/', '.');
        this.superName = string3.replace('/', '.');
        super.visit(n2, n3, string, string2, string3, arrstring);
    }

    public String getClassName() {
        return this.className;
    }

    public String getSuperName() {
        return this.superName;
    }

    public byte[] toByteArray() {
        return (byte[])AccessController.doPrivileged(new PrivilegedAction(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public Object run() {
                byte[] arrby = (($ClassWriter)$DebuggingClassWriter.this.cv).toByteArray();
                if (debugLocation != null) {
                    String string = $DebuggingClassWriter.this.className.replace('.', File.separatorChar);
                    try {
                        Object object = debugLocation;
                        char c2 = File.separatorChar;
                        new File(new StringBuilder(1 + String.valueOf(object).length() + String.valueOf(string).length()).append((String)object).append(c2).append(string).toString()).getParentFile().mkdirs();
                        object = new File(new File(debugLocation), String.valueOf(string).concat(".class"));
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream((File)object));
                        try {
                            bufferedOutputStream.write(arrby);
                        }
                        finally {
                            bufferedOutputStream.close();
                        }
                        if (traceCtor != null) {
                            object = new File(new File(debugLocation), String.valueOf(string).concat(".asm"));
                            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream((File)object));
                            try {
                                $ClassReader $ClassReader = new $ClassReader(arrby);
                                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(bufferedOutputStream));
                                $ClassVisitor $ClassVisitor = ($ClassVisitor)traceCtor.newInstance(null, printWriter);
                                $ClassReader.accept($ClassVisitor, 0);
                                printWriter.flush();
                            }
                            finally {
                                bufferedOutputStream.close();
                            }
                        }
                    }
                    catch (Exception exception) {
                        throw new $CodeGenerationException(exception);
                    }
                }
                return arrby;
            }
        });
    }

    static {
        if (debugLocation != null) {
            Object object = debugLocation;
            System.err.println(new StringBuilder(38 + String.valueOf(object).length()).append("CGLIB debugging enabled, writing to '").append((String)object).append("'").toString());
            try {
                object = Class.forName("com.google.inject.internal.asm.util.$TraceClassVisitor");
                traceCtor = object.getConstructor($ClassVisitor.class, PrintWriter.class);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

}

