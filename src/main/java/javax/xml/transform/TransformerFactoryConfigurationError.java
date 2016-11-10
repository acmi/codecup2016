/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform;

public class TransformerFactoryConfigurationError
extends Error {
    private Exception exception;

    public TransformerFactoryConfigurationError() {
        this.exception = null;
    }

    public TransformerFactoryConfigurationError(String string) {
        super(string);
        this.exception = null;
    }

    public TransformerFactoryConfigurationError(Exception exception, String string) {
        super(string);
        this.exception = exception;
    }

    public String getMessage() {
        String string = Throwable.super.getMessage();
        if (string == null && this.exception != null) {
            return this.exception.getMessage();
        }
        return string;
    }
}

