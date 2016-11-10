/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform.stream;

import java.io.OutputStream;
import java.io.Writer;
import javax.xml.transform.Result;

public class StreamResult
implements Result {
    private String systemId;
    private OutputStream outputStream;
    private Writer writer;

    public StreamResult() {
    }

    public StreamResult(OutputStream outputStream) {
        this.setOutputStream(outputStream);
    }

    public StreamResult(Writer writer) {
        this.setWriter(writer);
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public Writer getWriter() {
        return this.writer;
    }

    public void setSystemId(String string) {
        this.systemId = string;
    }

    public String getSystemId() {
        return this.systemId;
    }
}

