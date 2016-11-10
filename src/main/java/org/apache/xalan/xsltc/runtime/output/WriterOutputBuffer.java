/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.xalan.xsltc.runtime.output.OutputBuffer;

class WriterOutputBuffer
implements OutputBuffer {
    private static final int KB = 1024;
    private static int BUFFER_SIZE = 4096;
    private Writer _writer;

    public WriterOutputBuffer(Writer writer) {
        this._writer = new BufferedWriter(writer, BUFFER_SIZE);
    }

    public String close() {
        try {
            this._writer.flush();
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException.toString());
        }
        return "";
    }

    public OutputBuffer append(String string) {
        try {
            this._writer.write(string);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException.toString());
        }
        return this;
    }

    public OutputBuffer append(char[] arrc, int n2, int n3) {
        try {
            this._writer.write(arrc, n2, n3);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException.toString());
        }
        return this;
    }

    public OutputBuffer append(char c2) {
        try {
            this._writer.write(c2);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException.toString());
        }
        return this;
    }

    static {
        String string = System.getProperty("os.name");
        if (string.equalsIgnoreCase("solaris")) {
            BUFFER_SIZE = 32768;
        }
    }
}

