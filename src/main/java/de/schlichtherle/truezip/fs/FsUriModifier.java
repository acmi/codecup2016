/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.util.QuotedUriSyntaxException;
import de.schlichtherle.truezip.util.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class FsUriModifier
extends Enum<FsUriModifier> {
    public static final /* enum */ FsUriModifier NULL = new FsUriModifier("NULL", 0){

        @Override
        URI modify(URI uRI, PostFix postFix) throws URISyntaxException {
            if (uRI.normalize() != uRI) {
                throw new QuotedUriSyntaxException(uRI, "URI path not in normal form");
            }
            return uRI;
        }
    };
    public static final /* enum */ FsUriModifier CANONICALIZE = new FsUriModifier("CANONICALIZE", 1){

        @Override
        URI modify(URI uRI, PostFix postFix) throws URISyntaxException {
            return postFix.modify(uRI);
        }
    };
    private static final /* synthetic */ FsUriModifier[] $VALUES;

    public static FsUriModifier[] values() {
        return (FsUriModifier[])$VALUES.clone();
    }

    private FsUriModifier() {
        super(string, n2);
    }

    abstract URI modify(URI var1, PostFix var2) throws URISyntaxException;

    static {
        $VALUES = new FsUriModifier[]{NULL, CANONICALIZE};
    }

    public static abstract class PostFix
    extends Enum<PostFix> {
        public static final /* enum */ PostFix PATH = new PostFix("PATH", 0){

            @Override
            URI modify(URI uRI) throws URISyntaxException {
                int n2;
                String string;
                if (uRI.isOpaque() || !uRI.isAbsolute() || null != uRI.getRawFragment()) {
                    return uRI;
                }
                String string2 = uRI.getRawAuthority();
                String string3 = uRI.getRawPath();
                if (null == string2 && null != string3 && string3.startsWith("//")) {
                    int n3 = string3.indexOf(47, 2);
                    if (2 <= n3) {
                        string2 = string3.substring(2, n3);
                        string3 = string3.substring(n3);
                    }
                    uRI = new UriBuilder(uRI, true).authority(string2).path(string3).getUri();
                }
                uRI = uRI.normalize();
                string2 = uRI.getScheme();
                string3 = uRI.getRawAuthority();
                String string4 = string = uRI.getRawPath();
                while (string.endsWith("/") && (1 <= (n2 = string.length()) && null == string2 || 2 <= n2 && ':' != string.charAt(n2 - 2) || 3 <= n2 && !string.startsWith("/") || 4 < n2 && string.startsWith("/") || null != string3)) {
                    string = string.substring(0, n2 - 1);
                }
                return string != string4 ? new UriBuilder(uRI, true).path(string).getUri() : uRI;
            }
        };
        public static final /* enum */ PostFix MOUNT_POINT = new PostFix("MOUNT_POINT", 1){

            @Override
            URI modify(URI uRI) {
                return uRI.normalize();
            }
        };
        public static final /* enum */ PostFix ENTRY_NAME = new PostFix("ENTRY_NAME", 2){

            @Override
            URI modify(URI uRI) throws URISyntaxException {
                String string;
                if ((uRI = uRI.normalize()).isAbsolute() || null != uRI.getRawAuthority() || null != uRI.getRawFragment()) {
                    return uRI;
                }
                String string2 = string = uRI.getRawPath();
                while (string.startsWith("/")) {
                    string = string.substring(1);
                }
                while (string.endsWith("/")) {
                    string = string.substring(0, string.length() - 1);
                }
                return string == string2 ? uRI : new UriBuilder(uRI, true).path(string).getUri();
            }
        };
        private static final /* synthetic */ PostFix[] $VALUES;

        public static PostFix[] values() {
            return (PostFix[])$VALUES.clone();
        }

        private PostFix() {
            super(string, n2);
        }

        abstract URI modify(URI var1) throws URISyntaxException;

        static {
            $VALUES = new PostFix[]{PATH, MOUNT_POINT, ENTRY_NAME};
        }

    }

}

