/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.util.EncodingMap;
import org.apache.xml.serialize.EncodingInfo;

public class Encodings {
    static final String[] UNICODE_ENCODINGS = new String[]{"Unicode", "UnicodeBig", "UnicodeLittle", "GB2312", "UTF8", "UTF-16"};
    static Hashtable _encodings = new Hashtable();

    static EncodingInfo getEncodingInfo(String string, boolean bl) throws UnsupportedEncodingException {
        EncodingInfo encodingInfo = null;
        if (string == null) {
            encodingInfo = (EncodingInfo)_encodings.get("UTF8");
            if (encodingInfo != null) {
                return encodingInfo;
            }
            encodingInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping("UTF8"), "UTF8", 65535);
            _encodings.put("UTF8", encodingInfo);
            return encodingInfo;
        }
        String string2 = EncodingMap.getIANA2JavaMapping(string = string.toUpperCase(Locale.ENGLISH));
        if (string2 == null) {
            if (bl) {
                EncodingInfo.testJavaEncodingName(string);
                encodingInfo = (EncodingInfo)_encodings.get(string);
                if (encodingInfo != null) {
                    return encodingInfo;
                }
                int n2 = 0;
                while (n2 < UNICODE_ENCODINGS.length) {
                    if (UNICODE_ENCODINGS[n2].equalsIgnoreCase(string)) {
                        encodingInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping(string), string, 65535);
                        break;
                    }
                    ++n2;
                }
                if (n2 == UNICODE_ENCODINGS.length) {
                    encodingInfo = new EncodingInfo(EncodingMap.getJava2IANAMapping(string), string, 127);
                }
                _encodings.put(string, encodingInfo);
                return encodingInfo;
            }
            throw new UnsupportedEncodingException(string);
        }
        encodingInfo = (EncodingInfo)_encodings.get(string2);
        if (encodingInfo != null) {
            return encodingInfo;
        }
        int n3 = 0;
        while (n3 < UNICODE_ENCODINGS.length) {
            if (UNICODE_ENCODINGS[n3].equalsIgnoreCase(string2)) {
                encodingInfo = new EncodingInfo(string, string2, 65535);
                break;
            }
            ++n3;
        }
        if (n3 == UNICODE_ENCODINGS.length) {
            encodingInfo = new EncodingInfo(string, string2, 127);
        }
        _encodings.put(string2, encodingInfo);
        return encodingInfo;
    }
}

