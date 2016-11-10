/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform;

public interface SourceLocator {
    public String getPublicId();

    public String getSystemId();

    public int getLineNumber();

    public int getColumnNumber();
}

