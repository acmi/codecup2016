/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.entry;

import de.schlichtherle.truezip.util.QuotedUriSyntaxException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

public class EntryName
implements Serializable,
Comparable<EntryName> {
    private URI uri;

    public EntryName(URI uRI) throws URISyntaxException {
        this.parse(uRI);
    }

    private void parse(URI uRI) throws URISyntaxException {
        if (uRI.isAbsolute()) {
            throw new QuotedUriSyntaxException(uRI, "Scheme component defined.");
        }
        if (null != uRI.getRawAuthority()) {
            throw new QuotedUriSyntaxException(uRI, "Authority component defined.");
        }
        if (null == uRI.getRawPath()) {
            throw new QuotedUriSyntaxException(uRI, "Path component undefined.");
        }
        if (null != uRI.getRawFragment()) {
            throw new QuotedUriSyntaxException(uRI, "Fragment component defined.");
        }
        this.uri = uRI;
        assert (this.invariants());
    }

    private boolean invariants() {
        assert (null != this.toUri());
        assert (!this.toUri().isAbsolute());
        assert (null != this.toUri().getRawPath());
        assert (null == this.toUri().getRawFragment());
        return true;
    }

    public final URI toUri() {
        return this.uri;
    }

    public final String getPath() {
        return this.uri.getPath();
    }

    public final boolean equals(Object object) {
        return this == object || object instanceof EntryName && this.uri.equals(((EntryName)object).uri);
    }

    @Override
    public final int compareTo(EntryName entryName) {
        return this.uri.compareTo(entryName.uri);
    }

    public final int hashCode() {
        return this.uri.hashCode();
    }

    public final String toString() {
        return this.uri.toString();
    }
}

