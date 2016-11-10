/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.socket;

import de.schlichtherle.truezip.io.InputException;
import de.schlichtherle.truezip.io.Streams;
import de.schlichtherle.truezip.socket.InputSocket;
import de.schlichtherle.truezip.socket.OutputSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class IOSocket<LT, PT> {
    public abstract LT getLocalTarget() throws IOException;

    public abstract PT getPeerTarget() throws IOException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copy(InputSocket<?> inputSocket, OutputSocket<?> outputSocket) throws IOException {
        InputStream inputStream;
        if (null == outputSocket) {
            throw new NullPointerException();
        }
        OutputStream outputStream = null;
        try {
            inputStream = inputSocket.connect(outputSocket).newInputStream();
        }
        catch (IOException iOException) {
            throw new InputException(iOException);
        }
        try {
            outputStream = outputSocket.newOutputStream();
        }
        finally {
            if (null == outputStream) {
                try {
                    inputStream.close();
                }
                catch (IOException iOException) {
                    throw new InputException(iOException);
                }
            }
        }
        Streams.copy(inputStream, outputStream);
        inputSocket.connect(null);
    }

    public String toString() {
        Object object;
        Object object2;
        try {
            object = this.getLocalTarget();
        }
        catch (IOException iOException) {
            object = iOException;
        }
        try {
            object2 = this.getPeerTarget();
        }
        catch (IOException iOException) {
            object2 = iOException;
        }
        return String.format("%s[localTarget=%s, peerTarget=%s]", this.getClass().getName(), object, object2);
    }

    public final boolean equals(Object object) {
        return this == object;
    }

    public final int hashCode() {
        return super.hashCode();
    }
}

