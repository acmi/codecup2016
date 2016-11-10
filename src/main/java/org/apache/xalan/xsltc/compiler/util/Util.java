/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import java.io.PrintStream;
import java.util.StringTokenizer;
import org.apache.bcel.generic.Type;
import org.apache.xml.utils.XML11Char;

public final class Util {
    private static char filesep;

    public static String noExtName(String string) {
        int n2 = string.lastIndexOf(46);
        return string.substring(0, n2 >= 0 ? n2 : string.length());
    }

    public static String baseName(String string) {
        int n2 = string.lastIndexOf(92);
        if (n2 < 0) {
            n2 = string.lastIndexOf(47);
        }
        if (n2 >= 0) {
            return string.substring(n2 + 1);
        }
        int n3 = string.lastIndexOf(58);
        if (n3 > 0) {
            return string.substring(n3 + 1);
        }
        return string;
    }

    public static String pathName(String string) {
        int n2 = string.lastIndexOf(47);
        if (n2 < 0) {
            n2 = string.lastIndexOf(92);
        }
        return string.substring(0, n2 + 1);
    }

    public static String toJavaName(String string) {
        if (string.length() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            char c2 = string.charAt(0);
            stringBuffer.append(Character.isJavaIdentifierStart(c2) ? c2 : '_');
            int n2 = string.length();
            for (int i2 = 1; i2 < n2; ++i2) {
                c2 = string.charAt(i2);
                stringBuffer.append(Character.isJavaIdentifierPart(c2) ? c2 : '_');
            }
            return stringBuffer.toString();
        }
        return string;
    }

    public static Type getJCRefType(String string) {
        return Type.getType(string);
    }

    public static String internalName(String string) {
        return string.replace('.', filesep);
    }

    public static void println(String string) {
        System.out.println(string);
    }

    public static void println(char c2) {
        System.out.println(c2);
    }

    public static void TRACE1() {
        System.out.println("TRACE1");
    }

    public static void TRACE2() {
        System.out.println("TRACE2");
    }

    public static void TRACE3() {
        System.out.println("TRACE3");
    }

    public static String replace(String string, char c2, String string2) {
        return string.indexOf(c2) < 0 ? string : Util.replace(string, String.valueOf(c2), new String[]{string2});
    }

    public static String replace(String string, String string2, String[] arrstring) {
        int n2 = string.length();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            int n3 = string2.indexOf(c2);
            if (n3 >= 0) {
                stringBuffer.append(arrstring[n3]);
                continue;
            }
            stringBuffer.append(c2);
        }
        return stringBuffer.toString();
    }

    public static String escape(String string) {
        return Util.replace(string, ".-/:", new String[]{"$dot$", "$dash$", "$slash$", "$colon$"});
    }

    public static String getLocalName(String string) {
        int n2 = string.lastIndexOf(58);
        return n2 > 0 ? string.substring(n2 + 1) : string;
    }

    public static String getPrefix(String string) {
        int n2 = string.lastIndexOf(58);
        return n2 > 0 ? string.substring(0, n2) : "";
    }

    public static boolean isLiteral(String string) {
        int n2 = string.length();
        for (int i2 = 0; i2 < n2 - 1; ++i2) {
            if (string.charAt(i2) != '{' || string.charAt(i2 + 1) == '{') continue;
            return false;
        }
        return true;
    }

    public static boolean isValidQNames(String string) {
        if (string != null && !string.equals("")) {
            StringTokenizer stringTokenizer = new StringTokenizer(string);
            while (stringTokenizer.hasMoreTokens()) {
                if (XML11Char.isXML11ValidQName(stringTokenizer.nextToken())) continue;
                return false;
            }
        }
        return true;
    }

    static {
        String string = System.getProperty("file.separator", "/");
        filesep = string.charAt(0);
    }
}

