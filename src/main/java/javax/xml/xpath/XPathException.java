/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.xpath;

import java.io.PrintStream;
import java.io.PrintWriter;

public class XPathException
extends Exception {
    private final Throwable cause;

    public XPathException(Throwable throwable) {
        super(throwable == null ? null : throwable.toString());
        this.cause = throwable;
        if (throwable == null) {
            throw new NullPointerException("cause can't be null");
        }
    }

    public Throwable getCause() {
        return this.cause;
    }

    public void printStackTrace(PrintStream printStream) {
        if (this.getCause() != null) {
            this.getCause().printStackTrace(printStream);
            printStream.println("--------------- linked to ------------------");
        }
        Throwable.super.printStackTrace(printStream);
    }

    public void printStackTrace() {
        this.printStackTrace(System.err);
    }

    public void printStackTrace(PrintWriter printWriter) {
        if (this.getCause() != null) {
            this.getCause().printStackTrace(printWriter);
            printWriter.println("--------------- linked to ------------------");
        }
        Throwable.super.printStackTrace(printWriter);
    }
}

