/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$ClassReader;
import com.google.inject.internal.asm.$ClassVisitor;
import java.util.ArrayList;
import java.util.List;

public class $ClassNameReader {
    private static final EarlyExitException EARLY_EXIT = new EarlyExitException();

    private $ClassNameReader() {
    }

    public static String getClassName($ClassReader $ClassReader) {
        return $ClassNameReader.getClassInfo($ClassReader)[0];
    }

    public static String[] getClassInfo($ClassReader $ClassReader) {
        final ArrayList arrayList = new ArrayList();
        try {
            $ClassReader.accept(new $ClassVisitor(327680, null){

                public void visit(int n2, int n3, String string, String string2, String string3, String[] arrstring) {
                    arrayList.add(string.replace('/', '.'));
                    if (string3 != null) {
                        arrayList.add(string3.replace('/', '.'));
                    }
                    for (int i2 = 0; i2 < arrstring.length; ++i2) {
                        arrayList.add(arrstring[i2].replace('/', '.'));
                    }
                    throw EARLY_EXIT;
                }
            }, 6);
        }
        catch (EarlyExitException earlyExitException) {
            // empty catch block
        }
        return arrayList.toArray(new String[0]);
    }

    private static class EarlyExitException
    extends RuntimeException {
        private EarlyExitException() {
        }
    }

}

