/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream;

public class FactoryConfigurationError
extends Error {
    private Exception nested;

    public FactoryConfigurationError() {
    }

    public FactoryConfigurationError(Exception exception, String string) {
        super(string);
        this.nested = exception;
    }

    public String getMessage() {
        String string = Throwable.super.getMessage();
        if (string != null) {
            return string;
        }
        if (this.nested != null && (string = this.nested.getMessage()) == null) {
            string = this.nested.getClass().toString();
        }
        return string;
    }
}

