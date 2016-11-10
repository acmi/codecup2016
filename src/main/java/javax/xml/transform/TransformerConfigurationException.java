/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform;

import javax.xml.transform.TransformerException;

public class TransformerConfigurationException
extends TransformerException {
    public TransformerConfigurationException() {
        super("Configuration Error");
    }

    public TransformerConfigurationException(String string) {
        super(string);
    }

    public TransformerConfigurationException(Throwable throwable) {
        super(throwable);
    }

    public TransformerConfigurationException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

