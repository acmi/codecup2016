/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.file;

import de.schlichtherle.truezip.fs.FsAbstractCompositeDriver;
import de.schlichtherle.truezip.fs.FsDriver;
import de.schlichtherle.truezip.fs.FsDriverProvider;
import de.schlichtherle.truezip.fs.FsScheme;
import de.schlichtherle.truezip.fs.sl.FsDriverLocator;
import de.schlichtherle.truezip.util.CanonicalStringSet;
import de.schlichtherle.truezip.util.SuffixSet;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class TArchiveDetector
extends FsAbstractCompositeDriver {
    public static final TArchiveDetector NULL = new TArchiveDetector("");
    public static final TArchiveDetector ALL = new TArchiveDetector(null);
    private final SuffixSet suffixes;
    private final Map<FsScheme, FsDriver> drivers;

    private static SuffixSet extensions(FsDriverProvider fsDriverProvider) {
        if (fsDriverProvider instanceof TArchiveDetector) {
            return new SuffixSet(((TArchiveDetector)fsDriverProvider).suffixes);
        }
        Map<FsScheme, FsDriver> map = fsDriverProvider.get();
        SuffixSet suffixSet = new SuffixSet();
        for (Map.Entry<FsScheme, FsDriver> entry : map.entrySet()) {
            if (!entry.getValue().isFederated()) continue;
            suffixSet.add(entry.getKey().toString());
        }
        return suffixSet;
    }

    public TArchiveDetector(String string) {
        this(FsDriverLocator.SINGLETON, string);
    }

    public TArchiveDetector(FsDriverProvider fsDriverProvider, String string) {
        SuffixSet suffixSet;
        SuffixSet suffixSet2 = TArchiveDetector.extensions(fsDriverProvider);
        if (null == string) {
            suffixSet = suffixSet2;
        } else {
            suffixSet = new SuffixSet(string);
            if (suffixSet.retainAll(suffixSet2)) {
                suffixSet = new SuffixSet(string);
                suffixSet.removeAll(suffixSet2);
                assert (!suffixSet.isEmpty());
                throw new IllegalArgumentException("\"" + suffixSet + "\" (no archive driver installed for these extensions)");
            }
        }
        this.suffixes = suffixSet;
        this.drivers = fsDriverProvider.get();
    }

    @Override
    public Map<FsScheme, FsDriver> get() {
        return this.drivers;
    }

    public FsScheme getScheme(String string) {
        string = string.replace('/', File.separatorChar);
        int n2 = string.lastIndexOf(File.separatorChar) + 1;
        string = string.substring(n2);
        int n3 = string.length();
        n2 = 0;
        while (0 < (n2 = string.indexOf(46, n2) + 1) && n2 < n3) {
            String string2 = string.substring(n2);
            if (!this.suffixes.contains(string2)) continue;
            try {
                return new FsScheme(string2);
            }
            catch (URISyntaxException uRISyntaxException) {
                continue;
            }
        }
        return null;
    }

    public String toString() {
        return this.suffixes.toString();
    }
}

