/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.entry.Entry;
import de.schlichtherle.truezip.fs.FsEntry;
import de.schlichtherle.truezip.fs.FsEntryName;
import de.schlichtherle.truezip.fs.FsInputOption;
import de.schlichtherle.truezip.fs.FsModel;
import de.schlichtherle.truezip.fs.FsOutputOption;
import de.schlichtherle.truezip.fs.FsSyncException;
import de.schlichtherle.truezip.fs.FsSyncOption;
import de.schlichtherle.truezip.fs.FsSyncWarningException;
import de.schlichtherle.truezip.socket.InputSocket;
import de.schlichtherle.truezip.socket.OutputSocket;
import de.schlichtherle.truezip.util.BitField;
import java.io.IOException;

public abstract class FsController<M extends FsModel> {
    public abstract M getModel();

    public abstract FsEntry getEntry(FsEntryName var1) throws IOException;

    public abstract boolean isReadable(FsEntryName var1) throws IOException;

    public abstract boolean isWritable(FsEntryName var1) throws IOException;

    public boolean isExecutable(FsEntryName fsEntryName) throws IOException {
        return false;
    }

    public abstract void setReadOnly(FsEntryName var1) throws IOException;

    public abstract boolean setTime(FsEntryName var1, BitField<Entry.Access> var2, long var3, BitField<FsOutputOption> var5) throws IOException;

    public abstract InputSocket<?> getInputSocket(FsEntryName var1, BitField<FsInputOption> var2);

    public abstract OutputSocket<?> getOutputSocket(FsEntryName var1, BitField<FsOutputOption> var2, Entry var3);

    public abstract void mknod(FsEntryName var1, Entry.Type var2, BitField<FsOutputOption> var3, Entry var4) throws IOException;

    public abstract void unlink(FsEntryName var1, BitField<FsOutputOption> var2) throws IOException;

    public abstract void sync(BitField<FsSyncOption> var1) throws FsSyncWarningException, FsSyncException;

    public final boolean equals(Object object) {
        return this == object;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return String.format("%s[model=%s]", this.getClass().getName(), this.getModel());
    }
}

