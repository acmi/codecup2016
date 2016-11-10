/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import org.apache.xerces.util.EncodingMap;

public class EncodingInfo {
    private Object[] fArgsForMethod = null;
    String ianaName;
    String javaName;
    int lastPrintable;
    Object fCharsetEncoder = null;
    Object fCharToByteConverter = null;
    boolean fHaveTriedCToB = false;
    boolean fHaveTriedCharsetEncoder = false;

    public EncodingInfo(String string, String string2, int n2) {
        this.ianaName = string;
        this.javaName = EncodingMap.getIANA2JavaMapping(string);
        this.lastPrintable = n2;
    }

    public Writer getWriter(OutputStream outputStream) throws UnsupportedEncodingException {
        if (this.javaName != null) {
            return new OutputStreamWriter(outputStream, this.javaName);
        }
        this.javaName = EncodingMap.getIANA2JavaMapping(this.ianaName);
        if (this.javaName == null) {
            return new OutputStreamWriter(outputStream, "UTF8");
        }
        return new OutputStreamWriter(outputStream, this.javaName);
    }

    public boolean isPrintable(char c2) {
        if (c2 <= this.lastPrintable) {
            return true;
        }
        return this.isPrintable0(c2);
    }

    private boolean isPrintable0(char c2) {
        if (this.fCharsetEncoder == null && CharsetMethods.access$000() && !this.fHaveTriedCharsetEncoder) {
            if (this.fArgsForMethod == null) {
                this.fArgsForMethod = new Object[1];
            }
            try {
                this.fArgsForMethod[0] = this.javaName;
                Object object = CharsetMethods.access$100().invoke(null, this.fArgsForMethod);
                if (((Boolean)CharsetMethods.access$200().invoke(object, null)).booleanValue()) {
                    this.fCharsetEncoder = CharsetMethods.access$300().invoke(object, null);
                } else {
                    this.fHaveTriedCharsetEncoder = true;
                }
            }
            catch (Exception exception) {
                this.fHaveTriedCharsetEncoder = true;
            }
        }
        if (this.fCharsetEncoder != null) {
            try {
                this.fArgsForMethod[0] = new Character(c2);
                return (Boolean)CharsetMethods.access$400().invoke(this.fCharsetEncoder, this.fArgsForMethod);
            }
            catch (Exception exception) {
                this.fCharsetEncoder = null;
                this.fHaveTriedCharsetEncoder = false;
            }
        }
        if (this.fCharToByteConverter == null) {
            if (this.fHaveTriedCToB || !CharToByteConverterMethods.access$500()) {
                return false;
            }
            if (this.fArgsForMethod == null) {
                this.fArgsForMethod = new Object[1];
            }
            try {
                this.fArgsForMethod[0] = this.javaName;
                this.fCharToByteConverter = CharToByteConverterMethods.access$600().invoke(null, this.fArgsForMethod);
            }
            catch (Exception exception) {
                this.fHaveTriedCToB = true;
                return false;
            }
        }
        try {
            this.fArgsForMethod[0] = new Character(c2);
            return (Boolean)CharToByteConverterMethods.access$700().invoke(this.fCharToByteConverter, this.fArgsForMethod);
        }
        catch (Exception exception) {
            this.fCharToByteConverter = null;
            this.fHaveTriedCToB = false;
            return false;
        }
    }

    public static void testJavaEncodingName(String string) throws UnsupportedEncodingException {
        byte[] arrby = new byte[]{118, 97, 108, 105, 100};
        String string2 = new String(arrby, string);
    }

    static class CharToByteConverterMethods {
        private static Method fgGetConverterMethod = null;
        private static Method fgCanConvertMethod = null;
        private static boolean fgConvertersAvailable = false;
        static Class class$java$lang$String;

        static boolean access$500() {
            return fgConvertersAvailable;
        }

        static Method access$600() {
            return fgGetConverterMethod;
        }

        static Method access$700() {
            return fgCanConvertMethod;
        }

        static Class class$(String string) {
            try {
                return Class.forName(string);
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }

        static {
            try {
                Class class_ = Class.forName("sun.io.CharToByteConverter");
                Class[] arrclass = new Class[1];
                Class class_2 = class$java$lang$String == null ? (CharToByteConverterMethods.class$java$lang$String = CharToByteConverterMethods.class$("java.lang.String")) : class$java$lang$String;
                arrclass[0] = class_2;
                fgGetConverterMethod = class_.getMethod("getConverter", arrclass);
                fgCanConvertMethod = class_.getMethod("canConvert", Character.TYPE);
                fgConvertersAvailable = true;
            }
            catch (Exception exception) {
                fgGetConverterMethod = null;
                fgCanConvertMethod = null;
                fgConvertersAvailable = false;
            }
        }
    }

    static class CharsetMethods {
        private static Method fgCharsetForNameMethod = null;
        private static Method fgCharsetCanEncodeMethod = null;
        private static Method fgCharsetNewEncoderMethod = null;
        private static Method fgCharsetEncoderCanEncodeMethod = null;
        private static boolean fgNIOCharsetAvailable = false;
        static Class class$java$lang$String;

        static boolean access$000() {
            return fgNIOCharsetAvailable;
        }

        static Method access$100() {
            return fgCharsetForNameMethod;
        }

        static Method access$200() {
            return fgCharsetCanEncodeMethod;
        }

        static Method access$300() {
            return fgCharsetNewEncoderMethod;
        }

        static Method access$400() {
            return fgCharsetEncoderCanEncodeMethod;
        }

        static Class class$(String string) {
            try {
                return Class.forName(string);
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }

        static {
            try {
                Class class_ = Class.forName("java.nio.charset.Charset");
                Class class_2 = Class.forName("java.nio.charset.CharsetEncoder");
                Class[] arrclass = new Class[1];
                Class class_3 = class$java$lang$String == null ? (CharsetMethods.class$java$lang$String = CharsetMethods.class$("java.lang.String")) : class$java$lang$String;
                arrclass[0] = class_3;
                fgCharsetForNameMethod = class_.getMethod("forName", arrclass);
                fgCharsetCanEncodeMethod = class_.getMethod("canEncode", new Class[0]);
                fgCharsetNewEncoderMethod = class_.getMethod("newEncoder", new Class[0]);
                fgCharsetEncoderCanEncodeMethod = class_2.getMethod("canEncode", Character.TYPE);
                fgNIOCharsetAvailable = true;
            }
            catch (Exception exception) {
                fgCharsetForNameMethod = null;
                fgCharsetCanEncodeMethod = null;
                fgCharsetEncoderCanEncodeMethod = null;
                fgCharsetNewEncoderMethod = null;
                fgNIOCharsetAvailable = false;
            }
        }
    }

}

