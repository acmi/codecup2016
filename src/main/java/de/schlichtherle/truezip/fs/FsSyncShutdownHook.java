/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsSyncOption;
import de.schlichtherle.truezip.fs.FsSyncOptions;
import de.schlichtherle.truezip.util.BitField;

final class FsSyncShutdownHook {
    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static final Hook hook = new Hook();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static void cancel() {
        Hook hook = FsSyncShutdownHook.hook;
        if (hook.manager != null) {
            Hook hook2 = hook;
            synchronized (hook2) {
                if (hook.manager != null) {
                    RUNTIME.removeShutdownHook(hook);
                    hook.manager = null;
                }
            }
        }
    }

    private static final class Hook
    extends Thread {
        volatile FsManager manager;

        Hook() {
            this.setPriority(10);
        }

        @Override
        public void run() {
            FsManager fsManager = this.manager;
            if (fsManager != null) {
                this.manager = null;
                try {
                    fsManager.sync(FsSyncOptions.UMOUNT);
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

}

