/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.entry.EntryName;
import de.schlichtherle.truezip.fs.FsUriModifier;
import de.schlichtherle.truezip.util.QuotedUriSyntaxException;
import java.net.URI;
import java.net.URISyntaxException;

public final class FsEntryName
extends EntryName {
    public static final FsEntryName ROOT;

    public FsEntryName(URI uRI) throws URISyntaxException {
        this(uRI, FsUriModifier.NULL);
    }

    public FsEntryName(URI uRI, FsUriModifier fsUriModifier) throws URISyntaxException {
        uRI = fsUriModifier.modify(uRI, FsUriModifier.PostFix.ENTRY_NAME);
        super(uRI);
        this.parse(uRI);
    }

    private void parse(URI uRI) throws URISyntaxException {
        String string = uRI.getRawPath();
        if (string.startsWith("/")) {
            throw new QuotedUriSyntaxException(uRI, "Illegal start of URI path component");
        }
        if (!string.isEmpty() && "../".startsWith(string.substring(0, Math.min(string.length(), "../".length())))) {
            throw new QuotedUriSyntaxException(uRI, "Illegal start of URI path component");
        }
        if (string.endsWith("/")) {
            throw new QuotedUriSyntaxException(uRI, "Illegal separator \"/\" at end of URI path");
        }
        assert (this.invariants());
    }

    private boolean invariants() {
        assert (null != this.toUri());
        assert (this.toUri().normalize() == this.toUri());
        String string = this.toUri().getRawPath();
        assert (!"..".equals(string));
        assert (!string.startsWith("/"));
        assert (!string.startsWith("./"));
        assert (!string.startsWith("../"));
        assert (!string.endsWith("/"));
        return true;
    }

    public boolean isRoot() {
        URI uRI = this.toUri();
        String string = uRI.getRawPath();
        if (null != string && !string.isEmpty()) {
            return false;
        }
        String string2 = uRI.getRawQuery();
        if (null != string2) {
            return false;
        }
        return true;
    }

    static {
        try {
            ROOT = new FsEntryName(new URI(""));
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new AssertionError(uRISyntaxException);
        }
    }
}

