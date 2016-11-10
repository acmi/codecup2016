/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.datatype;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class DatatypeConfigurationException
extends Exception {
    private Throwable causeOnJDK13OrBelow;
    private transient boolean isJDK14OrAbove = false;
    static Class class$java$lang$Throwable;

    public DatatypeConfigurationException() {
    }

    public DatatypeConfigurationException(String string) {
        super(string);
    }

    public DatatypeConfigurationException(String string, Throwable throwable) {
        super(string);
        this.initCauseByReflection(throwable);
    }

    public void printStackTrace() {
        if (!this.isJDK14OrAbove && this.causeOnJDK13OrBelow != null) {
            this.printStackTrace0(new PrintWriter(System.err, true));
        } else {
            Throwable.super.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream printStream) {
        if (!this.isJDK14OrAbove && this.causeOnJDK13OrBelow != null) {
            this.printStackTrace0(new PrintWriter(printStream));
        } else {
            Throwable.super.printStackTrace(printStream);
        }
    }

    public void printStackTrace(PrintWriter printWriter) {
        if (!this.isJDK14OrAbove && this.causeOnJDK13OrBelow != null) {
            this.printStackTrace0(printWriter);
        } else {
            Throwable.super.printStackTrace(printWriter);
        }
    }

    private void printStackTrace0(PrintWriter printWriter) {
        this.causeOnJDK13OrBelow.printStackTrace(printWriter);
        printWriter.println("------------------------------------------");
        Throwable.super.printStackTrace(printWriter);
    }

    private void initCauseByReflection(Throwable throwable) {
        this.causeOnJDK13OrBelow = throwable;
        try {
            Class[] arrclass = new Class[1];
            Class class_ = class$java$lang$Throwable == null ? (DatatypeConfigurationException.class$java$lang$Throwable = DatatypeConfigurationException.class$("java.lang.Throwable")) : class$java$lang$Throwable;
            arrclass[0] = class_;
            Method method = this.getClass().getMethod("initCause", arrclass);
            method.invoke(this, throwable);
            this.isJDK14OrAbove = true;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

