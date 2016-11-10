/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

import java.io.PrintStream;
import java.text.CharacterIterator;
import org.apache.xerces.impl.xpath.regex.Match;
import org.apache.xerces.impl.xpath.regex.ParseException;
import org.apache.xerces.impl.xpath.regex.RegularExpression;

public final class REUtil {
    static final int CACHESIZE = 20;
    static final RegularExpression[] regexCache = new RegularExpression[20];

    private REUtil() {
    }

    static final int composeFromSurrogates(int n2, int n3) {
        return 65536 + (n2 - 55296 << 10) + n3 - 56320;
    }

    static final boolean isLowSurrogate(int n2) {
        return (n2 & 64512) == 56320;
    }

    static final boolean isHighSurrogate(int n2) {
        return (n2 & 64512) == 55296;
    }

    static final String decomposeToSurrogates(int n2) {
        char[] arrc = new char[]{(char)(((n2 -= 65536) >> 10) + 55296), (char)((n2 & 1023) + 56320)};
        return new String(arrc);
    }

    static final String substring(CharacterIterator characterIterator, int n2, int n3) {
        char[] arrc = new char[n3 - n2];
        int n4 = 0;
        while (n4 < arrc.length) {
            arrc[n4] = characterIterator.setIndex(n4 + n2);
            ++n4;
        }
        return new String(arrc);
    }

    static final int getOptionValue(int n2) {
        int n3 = 0;
        switch (n2) {
            case 105: {
                n3 = 2;
                break;
            }
            case 109: {
                n3 = 8;
                break;
            }
            case 115: {
                n3 = 4;
                break;
            }
            case 120: {
                n3 = 16;
                break;
            }
            case 117: {
                n3 = 32;
                break;
            }
            case 119: {
                n3 = 64;
                break;
            }
            case 70: {
                n3 = 256;
                break;
            }
            case 72: {
                n3 = 128;
                break;
            }
            case 88: {
                n3 = 512;
                break;
            }
            case 44: {
                n3 = 1024;
                break;
            }
        }
        return n3;
    }

    static final int parseOptions(String string) throws ParseException {
        if (string == null) {
            return 0;
        }
        int n2 = 0;
        int n3 = 0;
        while (n3 < string.length()) {
            int n4 = REUtil.getOptionValue(string.charAt(n3));
            if (n4 == 0) {
                throw new ParseException("Unknown Option: " + string.substring(n3), -1);
            }
            n2 |= n4;
            ++n3;
        }
        return n2;
    }

    static final String createOptionString(int n2) {
        StringBuffer stringBuffer = new StringBuffer(9);
        if ((n2 & 256) != 0) {
            stringBuffer.append('F');
        }
        if ((n2 & 128) != 0) {
            stringBuffer.append('H');
        }
        if ((n2 & 512) != 0) {
            stringBuffer.append('X');
        }
        if ((n2 & 2) != 0) {
            stringBuffer.append('i');
        }
        if ((n2 & 8) != 0) {
            stringBuffer.append('m');
        }
        if ((n2 & 4) != 0) {
            stringBuffer.append('s');
        }
        if ((n2 & 32) != 0) {
            stringBuffer.append('u');
        }
        if ((n2 & 64) != 0) {
            stringBuffer.append('w');
        }
        if ((n2 & 16) != 0) {
            stringBuffer.append('x');
        }
        if ((n2 & 1024) != 0) {
            stringBuffer.append(',');
        }
        return stringBuffer.toString().intern();
    }

    static String stripExtendedComment(String string) {
        int n2 = string.length();
        StringBuffer stringBuffer = new StringBuffer(n2);
        int n3 = 0;
        int n4 = 0;
        block0 : while (n3 < n2) {
            char c2;
            char c3;
            if ((c2 = string.charAt(n3++)) == '\t' || c2 == '\n' || c2 == '\f' || c2 == '\r' || c2 == ' ') {
                if (n4 <= 0) continue;
                stringBuffer.append(c2);
                continue;
            }
            if (c2 == '#') {
                while (n3 < n2) {
                    if ((c2 = string.charAt(n3++)) == '\r' || c2 == '\n') continue block0;
                }
                continue;
            }
            if (c2 == '\\' && n3 < n2) {
                c3 = string.charAt(n3);
                if (c3 == '#' || c3 == '\t' || c3 == '\n' || c3 == '\f' || c3 == '\r' || c3 == ' ') {
                    stringBuffer.append(c3);
                    ++n3;
                    continue;
                }
                stringBuffer.append('\\');
                stringBuffer.append(c3);
                ++n3;
                continue;
            }
            if (c2 == '[') {
                ++n4;
                stringBuffer.append(c2);
                if (n3 >= n2) continue;
                c3 = string.charAt(n3);
                if (c3 == '[' || c3 == ']') {
                    stringBuffer.append(c3);
                    ++n3;
                    continue;
                }
                if (c3 != '^' || n3 + 1 >= n2 || (c3 = string.charAt(n3 + 1)) != '[' && c3 != ']') continue;
                stringBuffer.append('^');
                stringBuffer.append(c3);
                n3 += 2;
                continue;
            }
            if (n4 > 0 && c2 == ']') {
                --n4;
            }
            stringBuffer.append(c2);
        }
        return stringBuffer.toString();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static void main(String[] var0) {
        var1_1 = null;
        try {
            var2_2 = "";
            var3_4 = null;
            if (var0.length == 0) {
                System.out.println("Error:Usage: java REUtil -i|-m|-s|-u|-w|-X regularExpression String");
                System.exit(0);
            }
            var4_7 = 0;
            do {
                if (var4_7 < var0.length) ** GOTO lbl17
                var5_9 = new RegularExpression(var1_1, var2_2);
                System.out.println("RegularExpression: " + var5_9);
                var6_11 = new Match();
                var5_9.matches(var3_4, var6_11);
                var7_12 = 0;
                ** GOTO lbl69
lbl17: // 1 sources:
                if (var0[var4_7].length() == 0 || var0[var4_7].charAt(0) != '-') {
                    if (var1_1 == null) {
                        var1_1 = var0[var4_7];
                    } else if (var3_4 == null) {
                        var3_4 = var0[var4_7];
                    } else {
                        System.err.println("Unnecessary: " + var0[var4_7]);
                    }
                } else if (var0[var4_7].equals("-i")) {
                    var2_2 = var2_2 + "i";
                } else if (var0[var4_7].equals("-m")) {
                    var2_2 = var2_2 + "m";
                } else if (var0[var4_7].equals("-s")) {
                    var2_2 = var2_2 + "s";
                } else if (var0[var4_7].equals("-u")) {
                    var2_2 = var2_2 + "u";
                } else if (var0[var4_7].equals("-w")) {
                    var2_2 = var2_2 + "w";
                } else if (var0[var4_7].equals("-X")) {
                    var2_2 = var2_2 + "X";
                } else {
                    System.err.println("Unknown option: " + var0[var4_7]);
                }
                ++var4_7;
            } while (true);
        }
        catch (ParseException var2_3) {
            if (var1_1 == null) {
                var2_3.printStackTrace();
                return;
            }
            System.err.println("org.apache.xerces.utils.regex.ParseException: " + var2_3.getMessage());
            var3_5 = "        ";
            System.err.println(var3_5 + var1_1);
            var4_8 = var2_3.getLocation();
            if (var4_8 < 0) return;
            System.err.print(var3_5);
            var5_10 = 0;
            ** GOTO lbl73
        }
        catch (Exception var3_6) {
            var3_6.printStackTrace();
        }
        return;
lbl-1000: // 1 sources:
        {
            System.out.print("Matched range for the whole pattern: ");
            if (var6_11.getBeginning(var7_12) < 0) {
                System.out.println("-1");
            } else {
                System.out.print("" + var6_11.getBeginning(var7_12) + ", " + var6_11.getEnd(var7_12) + ", ");
                System.out.println("\"" + var6_11.getCapturedText(var7_12) + "\"");
            }
            ++var7_12;
lbl69: // 2 sources:
            ** while (var7_12 < var6_11.getNumberOfGroups())
        }
lbl70: // 1 sources:
        return;
lbl-1000: // 1 sources:
        {
            System.err.print("-");
            ++var5_10;
lbl73: // 2 sources:
            ** while (var5_10 < var4_8)
        }
lbl74: // 1 sources:
        System.err.println("^");
    }

    public static RegularExpression createRegex(String string, String string2) throws ParseException {
        RegularExpression regularExpression = null;
        int n2 = REUtil.parseOptions(string2);
        RegularExpression[] arrregularExpression = regexCache;
        synchronized (arrregularExpression) {
            int n3 = 0;
            while (n3 < 20) {
                RegularExpression regularExpression2 = regexCache[n3];
                if (regularExpression2 == null) {
                    n3 = -1;
                    break;
                }
                if (regularExpression2.equals(string, n2)) {
                    regularExpression = regularExpression2;
                    break;
                }
                ++n3;
            }
            if (regularExpression != null) {
                if (n3 != 0) {
                    System.arraycopy(regexCache, 0, regexCache, 1, n3);
                    REUtil.regexCache[0] = regularExpression;
                }
            } else {
                regularExpression = new RegularExpression(string, string2);
                System.arraycopy(regexCache, 0, regexCache, 1, 19);
                REUtil.regexCache[0] = regularExpression;
            }
        }
        return regularExpression;
    }

    public static boolean matches(String string, String string2) throws ParseException {
        return REUtil.createRegex(string, null).matches(string2);
    }

    public static boolean matches(String string, String string2, String string3) throws ParseException {
        return REUtil.createRegex(string, string2).matches(string3);
    }

    public static String quoteMeta(String string) {
        int n2 = string.length();
        StringBuffer stringBuffer = null;
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (".*+?{[()|\\^$".indexOf(c2) >= 0) {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer(n3 + (n2 - n3) * 2);
                    if (n3 > 0) {
                        stringBuffer.append(string.substring(0, n3));
                    }
                }
                stringBuffer.append('\\');
                stringBuffer.append(c2);
            } else if (stringBuffer != null) {
                stringBuffer.append(c2);
            }
            ++n3;
        }
        return stringBuffer != null ? stringBuffer.toString() : string;
    }

    static void dumpString(String string) {
        int n2 = 0;
        while (n2 < string.length()) {
            System.out.print(Integer.toHexString(string.charAt(n2)));
            System.out.print(" ");
            ++n2;
        }
        System.out.println();
    }
}

