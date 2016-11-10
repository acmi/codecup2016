/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.file;

import de.schlichtherle.truezip.file.TBIO;
import de.schlichtherle.truezip.file.TConfig;
import de.schlichtherle.truezip.fs.FsInputOption;
import de.schlichtherle.truezip.io.DecoratingInputStream;
import de.schlichtherle.truezip.socket.InputSocket;
import de.schlichtherle.truezip.util.BitField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class TFileInputStream
extends DecoratingInputStream {
    public TFileInputStream(File file) throws FileNotFoundException {
        super(TFileInputStream.newInputStream(file));
    }

    private static InputStream newInputStream(File file) throws FileNotFoundException {
        InputSocket inputSocket = TBIO.getInputSocket(file, TConfig.get().getInputPreferences());
        try {
            return inputSocket.newInputStream();
        }
        catch (FileNotFoundException fileNotFoundException) {
            throw fileNotFoundException;
        }
        catch (IOException iOException) {
            throw (FileNotFoundException)new FileNotFoundException(file.toString()).initCause(iOException);
        }
    }
}

