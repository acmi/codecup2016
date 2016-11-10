/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.file;

import de.schlichtherle.truezip.file.TArchiveDetector;
import de.schlichtherle.truezip.fs.FsInputOption;
import de.schlichtherle.truezip.fs.FsInputOptions;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsOutputOption;
import de.schlichtherle.truezip.fs.FsOutputOptions;
import de.schlichtherle.truezip.fs.sl.FsManagerLocator;
import de.schlichtherle.truezip.util.BitField;
import de.schlichtherle.truezip.util.InheritableThreadLocalStack;
import de.schlichtherle.truezip.util.Resource;
import java.io.Closeable;

public final class TConfig
extends Resource<RuntimeException>
implements Closeable {
    public static final BitField<FsInputOption> DEFAULT_INPUT_PREFERENCES = FsInputOptions.NONE;
    private static final BitField<FsInputOption> INPUT_PREFERENCES_COMPLEMENT_MASK = FsInputOptions.INPUT_PREFERENCES_MASK.not();
    public static final BitField<FsOutputOption> DEFAULT_OUTPUT_PREFERENCES = BitField.of(FsOutputOption.CREATE_PARENTS);
    private static final BitField<FsOutputOption> OUTPUT_PREFERENCES_COMPLEMENT_MASK = FsOutputOptions.OUTPUT_PREFERENCES_MASK.not();
    private static final InheritableThreadLocalStack<TConfig> configs = new InheritableThreadLocalStack();
    private static final TConfig GLOBAL = new TConfig();
    private FsManager manager = FsManagerLocator.SINGLETON.get();
    private TArchiveDetector detector = TArchiveDetector.ALL;
    private BitField<FsInputOption> inputPreferences = DEFAULT_INPUT_PREFERENCES;
    private BitField<FsOutputOption> outputPreferences = DEFAULT_OUTPUT_PREFERENCES;

    public static TConfig get() {
        return configs.peekOrElse(GLOBAL);
    }

    private TConfig() {
    }

    @Deprecated
    public FsManager getFsManager() {
        return this.manager;
    }

    public boolean isLenient() {
        return this.outputPreferences.get(FsOutputOption.CREATE_PARENTS);
    }

    public TArchiveDetector getArchiveDetector() {
        return this.detector;
    }

    public BitField<FsInputOption> getInputPreferences() {
        return this.inputPreferences;
    }

    public BitField<FsOutputOption> getOutputPreferences() {
        return this.outputPreferences;
    }

    @Override
    protected void onClose() {
        configs.popIf(this);
    }
}

