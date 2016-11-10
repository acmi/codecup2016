/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.xml.serializer.EncodingInfo;
import org.apache.xml.serializer.ObjectFactory;
import org.apache.xml.serializer.SecuritySupport;
import org.apache.xml.serializer.SerializerBase;
import org.apache.xml.serializer.utils.WrappedRuntimeException;

public final class Encodings {
    private static final String ENCODINGS_FILE = SerializerBase.PKG_PATH + "/Encodings.properties";
    private static final Hashtable _encodingTableKeyJava = new Hashtable();
    private static final Hashtable _encodingTableKeyMime = new Hashtable();
    private static final EncodingInfo[] _encodings = Encodings.loadEncodingInfo();

    static Writer getWriter(OutputStream outputStream, String string) throws UnsupportedEncodingException {
        for (int i2 = 0; i2 < _encodings.length; ++i2) {
            if (!Encodings._encodings[i2].name.equalsIgnoreCase(string)) continue;
            try {
                String string2 = Encodings._encodings[i2].javaName;
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, string2);
                return outputStreamWriter;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                continue;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
        }
        try {
            return new OutputStreamWriter(outputStream, string);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new UnsupportedEncodingException(string);
        }
    }

    static EncodingInfo getEncodingInfo(String string) {
        String string2 = Encodings.toUpperCaseFast(string);
        EncodingInfo encodingInfo = (EncodingInfo)_encodingTableKeyJava.get(string2);
        if (encodingInfo == null) {
            encodingInfo = (EncodingInfo)_encodingTableKeyMime.get(string2);
        }
        if (encodingInfo == null) {
            encodingInfo = new EncodingInfo(null, null, '\u0000');
        }
        return encodingInfo;
    }

    private static String toUpperCaseFast(String string) {
        boolean bl = false;
        int n2 = string.length();
        char[] arrc = new char[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            if ('a' <= c2 && c2 <= 'z') {
                c2 = (char)(c2 + -32);
                bl = true;
            }
            arrc[i2] = c2;
        }
        String string2 = bl ? String.valueOf(arrc) : string;
        return string2;
    }

    static String getMimeEncoding(String string) {
        block5 : {
            if (null == string) {
                try {
                    string = System.getProperty("file.encoding", "UTF8");
                    if (null != string) {
                        String string2 = string.equalsIgnoreCase("Cp1252") || string.equalsIgnoreCase("ISO8859_1") || string.equalsIgnoreCase("8859_1") || string.equalsIgnoreCase("UTF8") ? "UTF-8" : Encodings.convertJava2MimeEncoding(string);
                        string = null != string2 ? string2 : "UTF-8";
                        break block5;
                    }
                    string = "UTF-8";
                }
                catch (SecurityException securityException) {
                    string = "UTF-8";
                }
            } else {
                string = Encodings.convertJava2MimeEncoding(string);
            }
        }
        return string;
    }

    private static String convertJava2MimeEncoding(String string) {
        EncodingInfo encodingInfo = (EncodingInfo)_encodingTableKeyJava.get(Encodings.toUpperCaseFast(string));
        if (null != encodingInfo) {
            return encodingInfo.name;
        }
        return string;
    }

    public static String convertMime2JavaEncoding(String string) {
        for (int i2 = 0; i2 < _encodings.length; ++i2) {
            if (!Encodings._encodings[i2].name.equalsIgnoreCase(string)) continue;
            return Encodings._encodings[i2].javaName;
        }
        return string;
    }

    private static EncodingInfo[] loadEncodingInfo() {
        try {
            InputStream inputStream = SecuritySupport.getResourceAsStream(ObjectFactory.findClassLoader(), ENCODINGS_FILE);
            Properties properties = new Properties();
            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            }
            int n2 = properties.size();
            ArrayList<EncodingInfo> arrayList = new ArrayList<EncodingInfo>();
            Enumeration enumeration = properties.keys();
            for (int i2 = 0; i2 < n2; ++i2) {
                char c2;
                String string;
                String string2;
                String string3 = (String)enumeration.nextElement();
                String string4 = properties.getProperty(string3);
                int n3 = Encodings.lengthOfMimeNames(string4);
                if (n3 == 0) {
                    string2 = string3;
                    c2 = '\u0000';
                    continue;
                }
                try {
                    string = string4.substring(n3).trim();
                    c2 = (char)Integer.decode(string).intValue();
                }
                catch (NumberFormatException numberFormatException) {
                    c2 = '\u0000';
                }
                string = string4.substring(0, n3);
                StringTokenizer stringTokenizer = new StringTokenizer(string, ",");
                boolean bl = true;
                while (stringTokenizer.hasMoreTokens()) {
                    string2 = stringTokenizer.nextToken();
                    EncodingInfo encodingInfo = new EncodingInfo(string2, string3, c2);
                    arrayList.add(encodingInfo);
                    _encodingTableKeyMime.put(string2.toUpperCase(), encodingInfo);
                    if (bl) {
                        _encodingTableKeyJava.put(string3.toUpperCase(), encodingInfo);
                    }
                    bl = false;
                }
            }
            EncodingInfo[] arrencodingInfo = new EncodingInfo[arrayList.size()];
            arrayList.toArray(arrencodingInfo);
            return arrencodingInfo;
        }
        catch (MalformedURLException malformedURLException) {
            throw new WrappedRuntimeException(malformedURLException);
        }
        catch (IOException iOException) {
            throw new WrappedRuntimeException(iOException);
        }
    }

    private static int lengthOfMimeNames(String string) {
        int n2 = string.indexOf(32);
        if (n2 < 0) {
            n2 = string.length();
        }
        return n2;
    }

    static boolean isHighUTF16Surrogate(char c2) {
        return '\ud800' <= c2 && c2 <= '\udbff';
    }

    static boolean isLowUTF16Surrogate(char c2) {
        return '\udc00' <= c2 && c2 <= '\udfff';
    }

    static int toCodePoint(char c2, char c3) {
        int n2 = (c2 - 55296 << 10) + (c3 - 56320) + 65536;
        return n2;
    }

    static int toCodePoint(char c2) {
        char c3 = c2;
        return c3;
    }
}

