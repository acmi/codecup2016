/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import de.schlichtherle.truezip.util.QuotedUriSyntaxException;
import de.schlichtherle.truezip.util.UriEncoder;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public final class UriBuilder {
    private final UriEncoder encoder;
    private StringBuilder builder;
    private String scheme;
    private String authority;
    private String path;
    private String query;
    private String fragment;

    public UriBuilder() {
        this(false);
    }

    public UriBuilder(boolean bl) {
        this.encoder = new UriEncoder(null, bl);
    }

    public UriBuilder(URI uRI, boolean bl) {
        this.encoder = new UriEncoder(null, bl);
        this.setUri(uRI);
    }

    public UriBuilder clear() {
        this.scheme = null;
        this.authority = null;
        this.path = null;
        this.query = null;
        this.fragment = null;
        return this;
    }

    public String toString() {
        try {
            return this.getString();
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new IllegalStateException(uRISyntaxException);
        }
    }

    public String getString() throws URISyntaxException {
        boolean bl;
        boolean bl2;
        StringBuilder stringBuilder = this.resetBuilder();
        int n2 = -1;
        String string = null;
        String string2 = this.scheme;
        String string3 = this.authority;
        String string4 = this.path;
        String string5 = this.query;
        String string6 = this.fragment;
        boolean bl3 = bl = null != string2;
        if (bl) {
            stringBuilder.append(string2).append(':');
        }
        int n3 = stringBuilder.length();
        boolean bl4 = bl2 = null != string3;
        if (bl2) {
            this.encoder.encode(string3, UriEncoder.Encoding.AUTHORITY, stringBuilder.append("//"));
        }
        boolean bl5 = false;
        if (null != string4 && !string4.isEmpty()) {
            if (string4.startsWith("/")) {
                bl5 = true;
                this.encoder.encode(string4, UriEncoder.Encoding.ABSOLUTE_PATH, stringBuilder);
            } else if (bl2) {
                bl5 = true;
                n2 = stringBuilder.length();
                string = "Relative path with " + (string3.isEmpty() ? "" : "non-") + "empty authority";
                this.encoder.encode(string4, UriEncoder.Encoding.ABSOLUTE_PATH, stringBuilder);
            } else if (bl) {
                this.encoder.encode(string4, UriEncoder.Encoding.QUERY, stringBuilder);
            } else {
                this.encoder.encode(string4, UriEncoder.Encoding.PATH, stringBuilder);
            }
        }
        if (null != string5) {
            stringBuilder.append('?');
            if (bl && !bl5) {
                n2 = stringBuilder.length();
                string = "Query in opaque URI";
            }
            this.encoder.encode(string5, UriEncoder.Encoding.QUERY, stringBuilder);
        }
        assert (bl == 0 < n3);
        if (bl && n3 >= stringBuilder.length()) {
            n2 = stringBuilder.length();
            string = "Empty scheme specific part in absolute URI";
        }
        if (null != string6) {
            this.encoder.encode(string6, UriEncoder.Encoding.FRAGMENT, stringBuilder.append('#'));
        }
        if (bl) {
            UriBuilder.validateScheme((CharBuffer)CharBuffer.wrap(stringBuilder).limit(string2.length()));
        }
        String string7 = stringBuilder.toString();
        if (0 <= n2) {
            throw new QuotedUriSyntaxException(string7, string, n2);
        }
        return string7;
    }

    private StringBuilder resetBuilder() {
        StringBuilder stringBuilder = this.builder;
        if (null == stringBuilder) {
            this.builder = stringBuilder = new StringBuilder();
        } else {
            stringBuilder.setLength(0);
        }
        return stringBuilder;
    }

    public static void validateScheme(String string) throws URISyntaxException {
        UriBuilder.validateScheme(CharBuffer.wrap(string));
    }

    private static void validateScheme(CharBuffer charBuffer) throws URISyntaxException {
        if (!charBuffer.hasRemaining()) {
            throw UriBuilder.newURISyntaxException(charBuffer, "Empty URI scheme");
        }
        char c2 = charBuffer.get();
        if (!(c2 >= 'a' && 'z' >= c2 || c2 >= 'A' && 'Z' >= c2)) {
            throw UriBuilder.newURISyntaxException(charBuffer, "Illegal character in URI scheme");
        }
        while (charBuffer.hasRemaining()) {
            c2 = charBuffer.get();
            if (c2 >= 'a' && 'z' >= c2 || c2 >= 'A' && 'Z' >= c2 || c2 >= '0' && '9' >= c2 || c2 == '+' || c2 == '-' || c2 == '.') continue;
            throw UriBuilder.newURISyntaxException(charBuffer, "Illegal character in URI scheme");
        }
    }

    private static URISyntaxException newURISyntaxException(CharBuffer charBuffer, String string) {
        int n2 = charBuffer.position() - 1;
        return new QuotedUriSyntaxException(charBuffer.rewind().limit(charBuffer.capacity()), string, n2);
    }

    public URI toUri() {
        try {
            return this.getUri();
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new IllegalStateException(uRISyntaxException);
        }
    }

    public URI getUri() throws URISyntaxException {
        String string = this.getString();
        try {
            return new URI(string);
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new AssertionError(uRISyntaxException);
        }
    }

    public void setUri(URI uRI) {
        if (this.encoder.isRaw()) {
            this.setScheme(uRI.getScheme());
            this.setAuthority(uRI.getRawAuthority());
            this.setPath(uRI.isOpaque() ? uRI.getRawSchemeSpecificPart() : uRI.getRawPath());
            this.setQuery(uRI.getRawQuery());
            this.setFragment(uRI.getRawFragment());
        } else {
            this.setScheme(uRI.getScheme());
            this.setAuthority(uRI.getAuthority());
            this.setPath(uRI.isOpaque() ? uRI.getSchemeSpecificPart() : uRI.getPath());
            this.setQuery(uRI.getQuery());
            this.setFragment(uRI.getFragment());
        }
    }

    public void setScheme(String string) {
        this.scheme = string;
    }

    public UriBuilder scheme(String string) {
        this.setScheme(string);
        return this;
    }

    public void setAuthority(String string) {
        this.authority = string;
    }

    public UriBuilder authority(String string) {
        this.setAuthority(string);
        return this;
    }

    public void setPath(String string) {
        this.path = string;
    }

    public UriBuilder path(String string) {
        this.setPath(string);
        return this;
    }

    public void setQuery(String string) {
        this.query = string;
    }

    public void setPathQuery(String string) {
        int n2;
        if (null != string && 0 <= (n2 = string.indexOf(63))) {
            this.path = string.substring(0, n2);
            this.query = string.substring(n2 + 1);
        } else {
            this.path = string;
            this.query = null;
        }
    }

    public UriBuilder pathQuery(String string) {
        this.setPathQuery(string);
        return this;
    }

    public void setFragment(String string) {
        this.fragment = string;
    }

    public UriBuilder fragment(String string) {
        this.setFragment(string);
        return this;
    }
}

