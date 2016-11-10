/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.socket;

import de.schlichtherle.truezip.entry.Entry;
import de.schlichtherle.truezip.socket.IOSocket;
import de.schlichtherle.truezip.socket.InputSocket;
import java.io.IOException;
import java.io.OutputStream;

public abstract class OutputSocket<E extends Entry>
extends IOSocket<E, Entry> {
    private InputSocket<?> peer;

    @Override
    public final Entry getPeerTarget() throws IOException {
        return null == this.peer ? null : (Entry)this.peer.getLocalTarget();
    }

    final OutputSocket<E> connect(InputSocket<?> inputSocket) {
        InputSocket inputSocket2 = this.peer;
        if (inputSocket2 != inputSocket) {
            if (null != inputSocket2) {
                this.peer = null;
                inputSocket2.connect(null);
            }
            if (null != inputSocket) {
                this.peer = inputSocket;
                inputSocket.connect(this);
            }
        }
        return this;
    }

    public abstract OutputStream newOutputStream() throws IOException;
}

