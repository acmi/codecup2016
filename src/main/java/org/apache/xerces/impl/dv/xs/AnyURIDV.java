/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import java.io.UnsupportedEncodingException;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.TypeValidator;
import org.apache.xerces.util.URI;

public class AnyURIDV
extends TypeValidator {
    private static final URI BASE_URI;
    private static boolean[] gNeedEscaping;
    private static char[] gAfterEscaping1;
    private static char[] gAfterEscaping2;
    private static char[] gHexChs;

    public short getAllowedFacets() {
        return 2079;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        try {
            if (string.length() != 0) {
                String string2 = AnyURIDV.encode(string);
                new URI(BASE_URI, string2);
            }
        }
        catch (URI.MalformedURIException malformedURIException) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "anyURI"});
        }
        return string;
    }

    private static String encode(String string) {
        int n2;
        int n3 = string.length();
        StringBuffer stringBuffer = new StringBuffer(n3 * 3);
        int n4 = 0;
        while (n4 < n3) {
            n2 = string.charAt(n4);
            if (n2 >= 128) break;
            if (gNeedEscaping[n2]) {
                stringBuffer.append('%');
                stringBuffer.append(gAfterEscaping1[n2]);
                stringBuffer.append(gAfterEscaping2[n2]);
            } else {
                stringBuffer.append((char)n2);
            }
            ++n4;
        }
        if (n4 < n3) {
            byte[] arrby = null;
            try {
                arrby = string.substring(n4).getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                return string;
            }
            n3 = arrby.length;
            n4 = 0;
            while (n4 < n3) {
                char c2 = arrby[n4];
                if (c2 < '\u0000') {
                    n2 = c2 + 256;
                    stringBuffer.append('%');
                    stringBuffer.append(gHexChs[n2 >> 4]);
                    stringBuffer.append(gHexChs[n2 & 15]);
                } else if (gNeedEscaping[c2]) {
                    stringBuffer.append('%');
                    stringBuffer.append(gAfterEscaping1[c2]);
                    stringBuffer.append(gAfterEscaping2[c2]);
                } else {
                    stringBuffer.append(c2);
                }
                ++n4;
            }
        }
        if (stringBuffer.length() != n3) {
            return stringBuffer.toString();
        }
        return string;
    }

    static {
        URI uRI = null;
        try {
            uRI = new URI("abc://def.ghi.jkl");
        }
        catch (URI.MalformedURIException malformedURIException) {
            // empty catch block
        }
        BASE_URI = uRI;
        gNeedEscaping = new boolean[128];
        gAfterEscaping1 = new char[128];
        gAfterEscaping2 = new char[128];
        gHexChs = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int n2 = 0;
        while (n2 <= 31) {
            AnyURIDV.gNeedEscaping[n2] = true;
            AnyURIDV.gAfterEscaping1[n2] = gHexChs[n2 >> 4];
            AnyURIDV.gAfterEscaping2[n2] = gHexChs[n2 & 15];
            ++n2;
        }
        AnyURIDV.gNeedEscaping[127] = true;
        AnyURIDV.gAfterEscaping1[127] = 55;
        AnyURIDV.gAfterEscaping2[127] = 70;
        char[] arrc = new char[]{' ', '<', '>', '\"', '{', '}', '|', '\\', '^', '~', '`'};
        int n3 = arrc.length;
        int n4 = 0;
        while (n4 < n3) {
            char c2 = arrc[n4];
            AnyURIDV.gNeedEscaping[c2] = true;
            AnyURIDV.gAfterEscaping1[c2] = gHexChs[c2 >> 4];
            AnyURIDV.gAfterEscaping2[c2] = gHexChs[c2 & 15];
            ++n4;
        }
    }
}

