/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.transform.SourceLocator;

public class TransformerException
extends Exception {
    SourceLocator locator;
    Throwable containedException;
    static Class class$java$lang$Throwable;

    public SourceLocator getLocator() {
        return this.locator;
    }

    public void setLocator(SourceLocator sourceLocator) {
        this.locator = sourceLocator;
    }

    public Throwable getException() {
        return this.containedException;
    }

    public Throwable getCause() {
        return this.containedException == this ? null : this.containedException;
    }

    public synchronized Throwable initCause(Throwable throwable) {
        if (this.containedException != null) {
            throw new IllegalStateException("Can't overwrite cause");
        }
        if (throwable == this) {
            throw new IllegalArgumentException("Self-causation not permitted");
        }
        this.containedException = throwable;
        return this;
    }

    public TransformerException(String string) {
        super(string);
        this.containedException = null;
        this.locator = null;
    }

    public TransformerException(Throwable throwable) {
        super(throwable.toString());
        this.containedException = throwable;
        this.locator = null;
    }

    public TransformerException(String string, Throwable throwable) {
        super(string == null || string.length() == 0 ? throwable.toString() : string);
        this.containedException = throwable;
        this.locator = null;
    }

    public TransformerException(String string, SourceLocator sourceLocator) {
        super(string);
        this.containedException = null;
        this.locator = sourceLocator;
    }

    public TransformerException(String string, SourceLocator sourceLocator, Throwable throwable) {
        super(string);
        this.containedException = throwable;
        this.locator = sourceLocator;
    }

    public String getMessageAndLocation() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = Throwable.super.getMessage();
        if (null != string) {
            stringBuffer.append(string);
        }
        if (null != this.locator) {
            String string2 = this.locator.getSystemId();
            int n2 = this.locator.getLineNumber();
            int n3 = this.locator.getColumnNumber();
            if (null != string2) {
                stringBuffer.append("; SystemID: ");
                stringBuffer.append(string2);
            }
            if (0 != n2) {
                stringBuffer.append("; Line#: ");
                stringBuffer.append(n2);
            }
            if (0 != n3) {
                stringBuffer.append("; Column#: ");
                stringBuffer.append(n3);
            }
        }
        return stringBuffer.toString();
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
            Throwable.super.printStackTrace(printWriter);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        boolean bl = false;
        try {
            Class class_ = class$java$lang$Throwable == null ? (TransformerException.class$java$lang$Throwable = TransformerException.class$("java.lang.Throwable")) : class$java$lang$Throwable;
            class_.getMethod("getCause", null);
            bl = true;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        if (!bl) {
            Throwable throwable = this.getException();
            int n2 = 0;
            while (n2 < 10 && null != throwable) {
                Object object;
                printWriter.println("---------");
                try {
                    if (throwable instanceof TransformerException && null != (object = ((TransformerException)throwable).getLocationAsString())) {
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
                        if (throwable3 == (throwable = (Throwable)object.invoke(throwable, null))) {
                            break;
                        }
                    } else {
                        throwable = null;
                    }
                }
                catch (InvocationTargetException invocationTargetException) {
                    throwable = null;
                }
                catch (IllegalAccessException illegalAccessException) {
                    throwable = null;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    throwable = null;
                }
                ++n2;
            }
        }
        printWriter.flush();
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

