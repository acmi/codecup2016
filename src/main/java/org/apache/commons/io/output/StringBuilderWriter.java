/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

public class StringBuilderWriter
extends Writer
implements Serializable {
    private final StringBuilder builder;

    public StringBuilderWriter() {
        this.builder = new StringBuilder();
    }

    public StringBuilderWriter(int n2) {
        this.builder = new StringBuilder(n2);
    }

    @Override
    public Writer append(char c2) {
        this.builder.append(c2);
        return this;
    }

    @Override
    public Writer append(CharSequence charSequence) {
        this.builder.append(charSequence);
        return this;
    }

    @Override
    public Writer append(CharSequence charSequence, int n2, int n3) {
        this.builder.append(charSequence, n2, n3);
        return this;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void write(String string) {
        if (string != null) {
            this.builder.append(string);
        }
    }

    @Override
    public void write(char[] arrc, int n2, int n3) {
        if (arrc != null) {
            this.builder.append(arrc, n2, n3);
        }
    }

    public String toString() {
        return this.builder.toString();
    }
}

