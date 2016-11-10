/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.util.UriBuilder;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Locale;

public final class FsScheme
implements Serializable,
Comparable<FsScheme> {
    private final String scheme;

    public static FsScheme create(String string) {
        try {
            return new FsScheme(string);
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new IllegalArgumentException(uRISyntaxException);
        }
    }

    public FsScheme(String string) throws URISyntaxException {
        UriBuilder.validateScheme(string);
        this.scheme = string;
    }

    public boolean equals(Object object) {
        return this == object || object instanceof FsScheme && this.scheme.equalsIgnoreCase(((FsScheme)object).scheme);
    }

    @Override
    public int compareTo(FsScheme fsScheme) {
        return this.scheme.compareToIgnoreCase(fsScheme.scheme);
    }

    public int hashCode() {
        return this.scheme.toLowerCase(Locale.ROOT).hashCode();
    }

    public String toString() {
        return this.scheme;
    }
}

