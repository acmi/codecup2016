/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.codec.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

public class DigestUtils {
    public static MessageDigest getDigest(String string) {
        try {
            return MessageDigest.getInstance(string);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new IllegalArgumentException(noSuchAlgorithmException);
        }
    }

    public static MessageDigest getSha1Digest() {
        return DigestUtils.getDigest("SHA-1");
    }

    public static byte[] sha1(byte[] arrby) {
        return DigestUtils.getSha1Digest().digest(arrby);
    }

    public static byte[] sha1(String string) {
        return DigestUtils.sha1(StringUtils.getBytesUtf8(string));
    }

    public static String sha1Hex(byte[] arrby) {
        return Hex.encodeHexString(DigestUtils.sha1(arrby));
    }

    public static String sha1Hex(String string) {
        return Hex.encodeHexString(DigestUtils.sha1(string));
    }
}

