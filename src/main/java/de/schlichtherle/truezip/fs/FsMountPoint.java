/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsEntryName;
import de.schlichtherle.truezip.fs.FsPath;
import de.schlichtherle.truezip.fs.FsScheme;
import de.schlichtherle.truezip.fs.FsUriModifier;
import de.schlichtherle.truezip.util.QuotedUriSyntaxException;
import de.schlichtherle.truezip.util.UriBuilder;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

public final class FsMountPoint
implements Serializable,
Comparable<FsMountPoint> {
    private URI uri;
    private transient FsPath path;
    private volatile transient FsScheme scheme;
    private volatile transient URI hierarchical;

    public FsMountPoint(URI uRI, FsUriModifier fsUriModifier) throws URISyntaxException {
        this.parse(uRI, fsUriModifier);
    }

    public FsMountPoint(FsScheme fsScheme, FsPath fsPath) throws URISyntaxException {
        URI uRI = fsPath.toUri();
        if (!uRI.isAbsolute()) {
            throw new QuotedUriSyntaxException(uRI, "Path not absolute");
        }
        String string = fsPath.getEntryName().toUri().getPath();
        if (0 == string.length()) {
            throw new QuotedUriSyntaxException(uRI, "Empty entry name");
        }
        this.uri = new UriBuilder(true).scheme(fsScheme.toString()).path(uRI.getScheme() + ':' + uRI.getRawSchemeSpecificPart() + "!/").toUri();
        this.scheme = fsScheme;
        this.path = fsPath;
        assert (this.invariants());
    }

    private void parse(URI uRI, FsUriModifier fsUriModifier) throws URISyntaxException {
        if (null != (uRI = fsUriModifier.modify(uRI, FsUriModifier.PostFix.MOUNT_POINT)).getRawQuery()) {
            throw new QuotedUriSyntaxException(uRI, "Query not allowed");
        }
        if (null != uRI.getRawFragment()) {
            throw new QuotedUriSyntaxException(uRI, "Fragment not allowed");
        }
        if (uRI.isOpaque()) {
            URI uRI2;
            String string = uRI.getRawSchemeSpecificPart();
            int n2 = string.lastIndexOf("!/");
            if (string.length() - 2 != n2) {
                throw new QuotedUriSyntaxException(uRI, "Doesn't end with mount point separator \"!/\"");
            }
            this.path = new FsPath(new URI(string.substring(0, n2)), fsUriModifier);
            URI uRI3 = this.path.toUri();
            if (!uRI3.isAbsolute()) {
                throw new QuotedUriSyntaxException(uRI, "Path not absolute");
            }
            if (0 == this.path.getEntryName().getPath().length()) {
                throw new QuotedUriSyntaxException(uRI, "Empty URI path of entry name of path");
            }
            if (FsUriModifier.NULL != fsUriModifier && !uRI.equals(uRI2 = new UriBuilder(true).scheme(uRI.getScheme()).path(uRI3.getScheme() + ':' + uRI3.getRawSchemeSpecificPart() + "!/").toUri())) {
                uRI = uRI2;
            }
        } else {
            if (!uRI.isAbsolute()) {
                throw new QuotedUriSyntaxException(uRI, "Not absolute");
            }
            if (!uRI.getRawPath().endsWith("/")) {
                throw new QuotedUriSyntaxException(uRI, "URI path doesn't end with separator \"/\"");
            }
            this.path = null;
        }
        this.uri = uRI;
        assert (this.invariants());
    }

    private boolean invariants() {
        assert (null != this.toUri());
        assert (this.toUri().isAbsolute());
        assert (null == this.toUri().getRawQuery());
        assert (null == this.toUri().getRawFragment());
        if (this.toUri().isOpaque()) {
            assert (this.toUri().getRawSchemeSpecificPart().endsWith("!/"));
            assert (null != this.getPath());
            assert (this.getPath().toUri().isAbsolute());
            assert (null == this.getPath().toUri().getRawFragment());
            assert (0 != this.getPath().getEntryName().toUri().getRawPath().length());
        } else {
            assert (this.toUri().normalize() == this.toUri());
            assert (this.toUri().getRawPath().endsWith("/"));
            assert (null == this.getPath());
        }
        return true;
    }

    public URI toUri() {
        return this.uri;
    }

    public URI toHierarchicalUri() {
        URI uRI = this.hierarchical;
        URI uRI2 = null != uRI ? uRI : (this.hierarchical = this.uri.isOpaque() ? this.path.toHierarchicalUri() : this.uri);
        return uRI2;
    }

    public FsScheme getScheme() {
        FsScheme fsScheme = this.scheme;
        FsScheme fsScheme2 = null != fsScheme ? fsScheme : (this.scheme = FsScheme.create(this.uri.getScheme()));
        return fsScheme2;
    }

    public FsPath getPath() {
        return this.path;
    }

    public FsMountPoint getParent() {
        assert (null == this.path || null != this.path.getMountPoint());
        return null == this.path ? null : this.path.getMountPoint();
    }

    public boolean equals(Object object) {
        return this == object || object instanceof FsMountPoint && this.uri.equals(((FsMountPoint)object).uri);
    }

    @Override
    public int compareTo(FsMountPoint fsMountPoint) {
        return this.uri.compareTo(fsMountPoint.uri);
    }

    public int hashCode() {
        return this.uri.hashCode();
    }

    public String toString() {
        return this.uri.toString();
    }
}

