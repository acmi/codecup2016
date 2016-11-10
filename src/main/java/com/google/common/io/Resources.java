/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class Resources {
    public static ByteSource asByteSource(URL uRL) {
        return new UrlByteSource(uRL);
    }

    private static final class UrlByteSource
    extends ByteSource {
        private final URL url;

        private UrlByteSource(URL uRL) {
            this.url = Preconditions.checkNotNull(uRL);
        }

        @Override
        public InputStream openStream() throws IOException {
            return this.url.openStream();
        }

        public String toString() {
            return "Resources.asByteSource(" + this.url + ")";
        }
    }

}

