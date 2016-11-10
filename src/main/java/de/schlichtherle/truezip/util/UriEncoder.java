/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import de.schlichtherle.truezip.util.QuotedUriSyntaxException;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public final class UriEncoder {
    public static final Charset UTF8 = Charset.forName("UTF-8");
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final CharsetEncoder encoder;
    private final boolean encode;
    private final boolean raw;
    private StringBuilder stringBuilder;

    public UriEncoder() {
        this(UTF8, false);
    }

    public UriEncoder(Charset charset, boolean bl) {
        this.encode = null != charset;
        if (!this.encode) {
            charset = UTF8;
        }
        this.encoder = charset.newEncoder();
        this.raw = bl;
    }

    boolean isRaw() {
        return this.raw;
    }

    private static void quote(char c2, StringBuilder stringBuilder) {
        UriEncoder.quote(UTF8.encode(CharBuffer.wrap(Character.toString(c2))), stringBuilder);
    }

    private static void quote(ByteBuffer byteBuffer, StringBuilder stringBuilder) {
        while (byteBuffer.hasRemaining()) {
            byte by = byteBuffer.get();
            stringBuilder.append('%');
            stringBuilder.append(HEX[by >> 4 & 15]);
            stringBuilder.append(HEX[by & 15]);
        }
    }

    public StringBuilder encode(String string, Encoding encoding, StringBuilder stringBuilder) throws URISyntaxException {
        String[] arrstring = encoding.escapes;
        CharBuffer charBuffer = CharBuffer.wrap(string);
        ByteBuffer byteBuffer = null;
        CharsetEncoder charsetEncoder = this.encoder;
        boolean bl = this.encode;
        while (charBuffer.hasRemaining()) {
            charBuffer.mark();
            char c2 = charBuffer.get();
            if (c2 < '') {
                String string2 = arrstring[c2];
                if (!(null == string2 || '%' == c2 && this.raw)) {
                    if (null == byteBuffer) {
                        if (null == stringBuilder) {
                            stringBuilder = this.stringBuilder;
                            if (null == stringBuilder) {
                                stringBuilder = this.stringBuilder = new StringBuilder();
                            } else {
                                stringBuilder.setLength(0);
                            }
                            stringBuilder.append(string, 0, charBuffer.position() - 1);
                        }
                        byteBuffer = ByteBuffer.allocate(3);
                    }
                    stringBuilder.append(string2);
                    continue;
                }
                if (null == stringBuilder) continue;
                stringBuilder.append(c2);
                continue;
            }
            if (Character.isISOControl(c2) || Character.isSpaceChar(c2) || bl) {
                if (null == byteBuffer) {
                    if (null == stringBuilder) {
                        stringBuilder = this.stringBuilder;
                        if (null == stringBuilder) {
                            stringBuilder = this.stringBuilder = new StringBuilder();
                        } else {
                            stringBuilder.setLength(0);
                        }
                        stringBuilder.append(string, 0, charBuffer.position() - 1);
                    }
                    byteBuffer = ByteBuffer.allocate(3);
                }
                int n2 = charBuffer.position();
                charBuffer.reset();
                charBuffer.limit(n2);
                CoderResult coderResult = charsetEncoder.reset().encode(charBuffer, byteBuffer, true);
                if (CoderResult.UNDERFLOW != coderResult || CoderResult.UNDERFLOW != (coderResult = charsetEncoder.flush(byteBuffer))) {
                    assert (CoderResult.OVERFLOW != coderResult);
                    throw new QuotedUriSyntaxException(string, coderResult.toString());
                }
                byteBuffer.flip();
                UriEncoder.quote(byteBuffer, stringBuilder);
                byteBuffer.clear();
                charBuffer.limit(charBuffer.capacity());
                continue;
            }
            if (null == stringBuilder) continue;
            stringBuilder.append(c2);
        }
        return null == byteBuffer ? null : stringBuilder;
    }

    public static final class Encoding
    extends Enum<Encoding> {
        public static final /* enum */ Encoding ANY = new Encoding("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'(),;$&+=@");
        public static final /* enum */ Encoding AUTHORITY = new Encoding("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'(),;$&+=@:[]");
        public static final /* enum */ Encoding PATH = new Encoding("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'(),;$&+=@/");
        public static final /* enum */ Encoding ABSOLUTE_PATH = new Encoding("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'(),;$&+=@:/");
        public static final /* enum */ Encoding QUERY = new Encoding("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'(),;$&+=@:/?");
        public static final /* enum */ Encoding FRAGMENT = new Encoding("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'(),;$&+=@:/?");
        private final String[] escapes = new String[128];
        private static final /* synthetic */ Encoding[] $VALUES;

        public static Encoding[] values() {
            return (Encoding[])$VALUES.clone();
        }

        private Encoding(String string2) {
            super(string, n2);
            StringBuilder stringBuilder = new StringBuilder();
            for (char c2 = '\u0000'; c2 < ''; c2 = (char)((char)(c2 + 1))) {
                if (string2.indexOf(c2) >= 0) continue;
                stringBuilder.setLength(0);
                UriEncoder.quote(c2, stringBuilder);
                this.escapes[c2] = stringBuilder.toString();
            }
        }

        static {
            $VALUES = new Encoding[]{ANY, AUTHORITY, PATH, ABSOLUTE_PATH, QUERY, FRAGMENT};
        }
    }

}

