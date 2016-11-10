/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsEntryName;
import de.schlichtherle.truezip.fs.FsMountPoint;
import de.schlichtherle.truezip.fs.FsUriModifier;
import de.schlichtherle.truezip.util.QuotedUriSyntaxException;
import de.schlichtherle.truezip.util.UriBuilder;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

public final class FsPath
implements Serializable,
Comparable<FsPath> {
    private static final URI DOT = URI.create(".");
    private URI uri;
    private transient FsMountPoint mountPoint;
    private transient FsEntryName entryName;
    private volatile transient URI hierarchical;

    public FsPath(File file) {
        try {
            this.parse(file.toURI(), FsUriModifier.CANONICALIZE);
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new AssertionError(uRISyntaxException);
        }
    }

    public FsPath(URI uRI, FsUriModifier fsUriModifier) throws URISyntaxException {
        this.parse(uRI, fsUriModifier);
    }

    public FsPath(FsMountPoint fsMountPoint, FsEntryName fsEntryName) {
        if (null == fsMountPoint) {
            this.uri = fsEntryName.toUri();
        } else if (fsEntryName.isRoot()) {
            this.uri = fsMountPoint.toUri();
        } else {
            URI uRI = fsMountPoint.toUri();
            if (uRI.isOpaque()) {
                try {
                    String string = uRI.getRawSchemeSpecificPart();
                    int n2 = string.length();
                    URI uRI2 = fsEntryName.toUri();
                    String string2 = uRI2.getRawPath();
                    int n3 = string2.length();
                    String string3 = uRI2.getRawQuery();
                    int n4 = null == string3 ? 0 : string3.length() + 1;
                    StringBuilder stringBuilder = new StringBuilder(n2 + n3 + n4).append(string).append(string2);
                    if (null != string3) {
                        stringBuilder.append('?').append(string3);
                    }
                    this.uri = new UriBuilder(true).scheme(uRI.getScheme()).path(stringBuilder.toString()).fragment(uRI2.getRawFragment()).getUri();
                }
                catch (URISyntaxException uRISyntaxException) {
                    throw new AssertionError(uRISyntaxException);
                }
            } else {
                this.uri = uRI.resolve(fsEntryName.toUri());
            }
        }
        this.mountPoint = fsMountPoint;
        this.entryName = fsEntryName;
        assert (this.invariants());
    }

    private void parse(URI uRI, FsUriModifier fsUriModifier) throws URISyntaxException {
        if (null != (uRI = fsUriModifier.modify(uRI, FsUriModifier.PostFix.PATH)).getRawFragment()) {
            throw new QuotedUriSyntaxException(uRI, "Fragment not allowed");
        }
        if (uRI.isOpaque()) {
            URI uRI2;
            URI uRI3;
            String string = uRI.getRawSchemeSpecificPart();
            int n2 = string.lastIndexOf("!/");
            if (0 > n2) {
                throw new QuotedUriSyntaxException(uRI, "Missing mount point separator \"!/\"");
            }
            UriBuilder uriBuilder = new UriBuilder(true);
            this.mountPoint = new FsMountPoint(uriBuilder.scheme(uRI.getScheme()).path(string.substring(0, n2 + 2)).toUri(), fsUriModifier);
            this.entryName = new FsEntryName(uriBuilder.clear().pathQuery(string.substring(n2 + 2)).fragment(uRI.getRawFragment()).toUri(), fsUriModifier);
            if (FsUriModifier.NULL != fsUriModifier && !uRI.equals(uRI3 = new URI((uRI2 = this.mountPoint.toUri()).getScheme() + ':' + uRI2.getRawSchemeSpecificPart() + this.entryName.toUri()))) {
                uRI = uRI3;
            }
        } else if (uRI.isAbsolute()) {
            this.mountPoint = new FsMountPoint(uRI.resolve(DOT), fsUriModifier);
            this.entryName = new FsEntryName(this.mountPoint.toUri().relativize(uRI), fsUriModifier);
        } else {
            this.mountPoint = null;
            this.entryName = new FsEntryName(uRI, fsUriModifier);
            if (FsUriModifier.NULL != fsUriModifier) {
                uRI = this.entryName.toUri();
            }
        }
        this.uri = uRI;
        assert (this.invariants());
    }

    private boolean invariants() {
        assert (null != this.toUri());
        assert (null == this.toUri().getRawFragment());
        assert (null != this.getMountPoint() == this.toUri().isAbsolute());
        assert (null != this.getEntryName());
        if (this.toUri().isOpaque()) {
            assert (this.toUri().getRawSchemeSpecificPart().contains("!/"));
        } else if (this.toUri().isAbsolute()) {
            assert (this.toUri().normalize() == this.toUri());
            assert (this.toUri().equals(this.getMountPoint().toUri().resolve(this.getEntryName().toUri())));
        } else {
            assert (this.toUri().normalize() == this.toUri());
            assert (this.getEntryName().toUri() == this.toUri());
        }
        return true;
    }

    public URI toUri() {
        return this.uri;
    }

    public URI toHierarchicalUri() {
        URI uRI = this.hierarchical;
        if (null != uRI) {
            return uRI;
        }
        if (this.uri.isOpaque()) {
            URI uRI2 = this.mountPoint.toHierarchicalUri();
            URI uRI3 = this.entryName.toUri();
            try {
                this.hierarchical = uRI3.toString().isEmpty() ? uRI2 : new UriBuilder(uRI2, true).path(uRI2.getRawPath() + "/").getUri().resolve(uRI3);
                return this.hierarchical;
            }
            catch (URISyntaxException uRISyntaxException) {
                throw new AssertionError(uRISyntaxException);
            }
        }
        this.hierarchical = this.uri;
        return this.hierarchical;
    }

    public FsMountPoint getMountPoint() {
        return this.mountPoint;
    }

    public FsEntryName getEntryName() {
        return this.entryName;
    }

    public boolean equals(Object object) {
        return this == object || object instanceof FsPath && this.uri.equals(((FsPath)object).uri);
    }

    @Override
    public int compareTo(FsPath fsPath) {
        return this.uri.compareTo(fsPath.uri);
    }

    public int hashCode() {
        return this.uri.hashCode();
    }

    public String toString() {
        return this.uri.toString();
    }
}

