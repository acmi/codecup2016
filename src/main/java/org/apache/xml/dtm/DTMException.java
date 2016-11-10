/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.transform.SourceLocator;
import org.apache.xml.res.XMLMessages;

public class DTMException
extends RuntimeException {
    SourceLocator locator;
    Throwable containedException;
    static Class class$java$lang$Throwable;

    public Throwable getException() {
        return this.containedException;
    }

    public Throwable getCause() {
        return this.containedException == this ? null : this.containedException;
    }

    public synchronized Throwable initCause(Throwable throwable) {
        if (this.containedException == null && throwable != null) {
            throw new IllegalStateException(XMLMessages.createXMLMessage("ER_CANNOT_OVERWRITE_CAUSE", null));
        }
        if (throwable == this) {
            throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_SELF_CAUSATION_NOT_PERMITTED", null));
        }
        this.containedException = throwable;
        return this;
    }

    public DTMException(String string) {
        super(string);
        this.containedException = null;
        this.locator = null;
    }

    public DTMException(Throwable throwable) {
        super(throwable.getMessage());
        this.containedException = throwable;
        this.locator = null;
    }

    public DTMException(String string, Throwable throwable) {
        super(string == null || string.length() == 0 ? throwable.getMessage() : string);
        this.containedException = throwable;
        this.locator = null;
    }

    public String getLocationAsString() {
        if (null != this.locator) {
            StringBuffer stringBuffer = new StringBuffer();
            String string = this.locator.getSystemId();
            int n2 = this.locator.getLineNumber();
            int n3 = this.locator.getColumnNumber();
            if (null != string) {
                stringBuffer.append("; SystemID: ");
                stringBuffer.append(string);
            }
            if (0 != n2) {
                stringBuffer.append("; Line#: ");
                stringBuffer.append(n2);
            }
            if (0 != n3) {
                stringBuffer.append("; Column#: ");
                stringBuffer.append(n3);
            }
            return stringBuffer.toString();
        }
        return null;
    }

    public void printStackTrace() {
        this.printStackTrace(new PrintWriter(System.err, true));
    }

    public void printStackTrace(PrintStream printStream) {
        this.printStackTrace(new PrintWriter(printStream));
    }

    public void printStackTrace(PrintWriter printWriter) {
        if (printWriter == null) {
            printWriter = new PrintWriter(System.err, true);
        }
        try {
            String string = this.getLocationAsString();
            if (null != string) {
                printWriter.println(string);
            }
            super.printStackTrace(printWriter);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        boolean bl = false;
        try {
            Class class_ = class$java$lang$Throwable == null ? (DTMException.class$java$lang$Throwable = DTMException.class$("java.lang.Throwable")) : class$java$lang$Throwable;
            class_.getMethod("getCause", null);
            bl = true;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        if (!bl) {
            Throwable throwable = this.getException();
            for (int i2 = 0; i2 < 10 && null != throwable; ++i2) {
                Object object;
                printWriter.println("---------");
                try {
                    if (throwable instanceof DTMException && null != (object = ((DTMException)throwable).getLocationAsString())) {
                        printWriter.println((String)object);
                    }
                    throwable.printStackTrace(printWriter);
                }
                catch (Throwable throwable2) {
                    printWriter.println("Could not print stack trace...");
                }
                try {
                    object = throwable.getClass().getMethod("getException", null);
                    if (null != object) {
                        Throwable throwable3 = throwable;
                        if (throwable3 != (throwable = (Throwable)object.invoke(throwable, null))) continue;
                        break;
                    }
                    throwable = null;
                    continue;
                }
                catch (InvocationTargetException invocationTargetException) {
                    throwable = null;
                    continue;
                }
                catch (IllegalAccessException illegalAccessException) {
                    throwable = null;
                    continue;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    throwable = null;
                }
            }
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

