/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime.output;

import org.apache.xalan.xsltc.runtime.output.OutputBuffer;

class StringOutputBuffer
implements OutputBuffer {
    private StringBuffer _buffer = new StringBuffer();

    public String close() {
        return this._buffer.toString();
    }

    public OutputBuffer append(String string) {
        this._buffer.append(string);
        return this;
    }

    public OutputBuffer append(char[] arrc, int n2, int n3) {
        this._buffer.append(arrc, n2, n3);
        return this;
    }

    public OutputBuffer append(char c2) {
        this._buffer.append(c2);
        return this;
    }
}

