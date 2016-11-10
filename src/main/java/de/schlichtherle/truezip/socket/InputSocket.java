/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.socket;

import de.schlichtherle.truezip.entry.Entry;
import de.schlichtherle.truezip.rof.ReadOnlyFile;
import de.schlichtherle.truezip.rof.ReadOnlyFileInputStream;
import de.schlichtherle.truezip.socket.IOSocket;
import de.schlichtherle.truezip.socket.OutputSocket;
import java.io.IOException;
import java.io.InputStream;

public abstract class InputSocket<E extends Entry>
extends IOSocket<E, Entry> {
    private OutputSocket<?> peer;

    @Override
    public final Entry getPeerTarget() throws IOException {
        return null == this.peer ? null : (Entry)this.peer.getLocalTarget();
    }

    final InputSocket<E> connect(OutputSocket<?> outputSocket) {
        OutputSocket outputSocket2 = this.peer;
        if (outputSocket2 != outputSocket) {
            if (null != outputSocket2) {
                this.peer = null;
                outputSocket2.connect(null);
            }
            if (null != outputSocket) {
                this.peer = outputSocket;
                outputSocket.connect(this);
            }
        }
        return this;
    }

    public abstract ReadOnlyFile newReadOnlyFile() throws IOException;

    public InputStream newInputStream() throws IOException {
        return new ReadOnlyFileInputStream(this.newReadOnlyFile());
    }
}

