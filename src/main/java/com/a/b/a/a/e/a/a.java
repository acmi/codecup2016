/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a;

import com.a.b.a.a.c.D;
import com.a.b.a.a.c.E;
import com.a.b.a.a.c.m;
import com.a.b.a.a.c.s;
import com.a.b.a.a.e.a.e;
import java.lang.reflect.Constructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class a
implements e {
    private static final Logger a = LoggerFactory.getLogger(a.class);
    private static final String b = com.a.b.a.a.e.a.class.getPackage().getName();
    private final com.a.b.a.a.e.e[] c;
    private final int d;
    private m e;

    public a(String string, int n2) {
        Constructor constructor;
        if (!string.endsWith(".class")) {
            throw new IllegalArgumentException(String.format("Illegal player definition: '%s'.", string));
        }
        if ((string = string.substring(0, string.length() - ".class".length())).indexOf(46) == -1) {
            string = b + '.' + string;
        }
        try {
            constructor = Class.forName(string).getConstructor(new Class[0]);
        }
        catch (ClassNotFoundException classNotFoundException) {
            String string2 = String.format("Class '%s' does not exist.", string);
            a.error(string2, classNotFoundException);
            throw new IllegalArgumentException(string2, classNotFoundException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            String string3 = String.format("Class '%s' hasn't default constructor.", string);
            a.error(string3, noSuchMethodException);
            throw new IllegalArgumentException(string3, noSuchMethodException);
        }
        this.c = new com.a.b.a.a.e.e[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            com.a.b.a.a.e.e e2;
            try {
                Object obj = constructor.newInstance(new Object[0]);
                if (obj instanceof com.a.b.a.a.e.e) {
                    e2 = (com.a.b.a.a.e.e)obj;
                } else {
                    a.error(String.format("Instance of class '%s' is not a strategy.", string));
                    e2 = new com.a.b.a.a.e.a();
                }
            }
            catch (Exception exception) {
                a.error(String.format("Can't create instance of class '%s'.", string), exception);
                e2 = new com.a.b.a.a.e.a();
            }
            this.c[i2] = e2;
        }
        this.d = n2;
    }

    @Override
    public int a() {
        return 1;
    }

    @Override
    public void a(m m2) {
        this.e = m2;
    }

    @Override
    public s[] a(D[] arrd, E e2) {
        if (arrd.length != this.d) {
            throw new IllegalArgumentException(String.format("Strategy adapter '%s' got %d wizards while team size is %d.", this.getClass().getSimpleName(), arrd.length, this.d));
        }
        s[] arrs = new s[this.d];
        for (int i2 = 0; i2 < this.d; ++i2) {
            arrs[i2] = new s();
            this.c[i2].a(arrd[i2], e2, this.e, arrs[i2]);
        }
        return arrs;
    }

    @Override
    public void close() {
    }
}

