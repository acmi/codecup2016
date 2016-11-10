/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson.internal.bind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601Utils {
    private static final String UTC_ID = "UTC";
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");

    public static String format(Date date) {
        return ISO8601Utils.format(date, false, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean bl) {
        return ISO8601Utils.format(date, bl, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean bl, TimeZone timeZone) {
        int n2;
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime(date);
        int n3 = "yyyy-MM-ddThh:mm:ss".length();
        n3 += bl ? ".sss".length() : 0;
        StringBuilder stringBuilder = new StringBuilder(n3 += timeZone.getRawOffset() == 0 ? "Z".length() : "+hh:mm".length());
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(1), "yyyy".length());
        stringBuilder.append('-');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(2) + 1, "MM".length());
        stringBuilder.append('-');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(5), "dd".length());
        stringBuilder.append('T');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(11), "hh".length());
        stringBuilder.append(':');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(12), "mm".length());
        stringBuilder.append(':');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(13), "ss".length());
        if (bl) {
            stringBuilder.append('.');
            ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(14), "sss".length());
        }
        if ((n2 = timeZone.getOffset(gregorianCalendar.getTimeInMillis())) != 0) {
            int n4 = Math.abs(n2 / 60000 / 60);
            int n5 = Math.abs(n2 / 60000 % 60);
            stringBuilder.append(n2 < 0 ? '-' : '+');
            ISO8601Utils.padInt(stringBuilder, n4, "hh".length());
            stringBuilder.append(':');
            ISO8601Utils.padInt(stringBuilder, n5, "mm".length());
        } else {
            stringBuilder.append('Z');
        }
        return stringBuilder.toString();
    }

    public static Date parse(String string, ParsePosition parsePosition) throws ParseException {
        Throwable throwable = null;
        try {
            int n2;
            int n3 = parsePosition.getIndex();
            int n4 = ISO8601Utils.parseInt(string, n3, n3 += 4);
            if (ISO8601Utils.checkOffset(string, n3, '-')) {
                ++n3;
            }
            int n5 = ISO8601Utils.parseInt(string, n3, n3 += 2);
            if (ISO8601Utils.checkOffset(string, n3, '-')) {
                ++n3;
            }
            int n6 = ISO8601Utils.parseInt(string, n3, n3 += 2);
            int n7 = 0;
            int n8 = 0;
            int n9 = 0;
            int n10 = 0;
            boolean bl = ISO8601Utils.checkOffset(string, n3, 'T');
            if (!bl && string.length() <= n3) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(n4, n5 - 1, n6);
                parsePosition.setIndex(n3);
                return gregorianCalendar.getTime();
            }
            if (bl) {
                char c2;
                n7 = ISO8601Utils.parseInt(string, ++n3, n3 += 2);
                if (ISO8601Utils.checkOffset(string, n3, ':')) {
                    ++n3;
                }
                n8 = ISO8601Utils.parseInt(string, n3, n3 += 2);
                if (ISO8601Utils.checkOffset(string, n3, ':')) {
                    ++n3;
                }
                if (string.length() > n3 && (c2 = string.charAt(n3)) != 'Z' && c2 != '+' && c2 != '-') {
                    int n11 = n3;
                    n9 = ISO8601Utils.parseInt(string, n11, n3 += 2);
                    if (n9 > 59 && n9 < 63) {
                        n9 = 59;
                    }
                    if (ISO8601Utils.checkOffset(string, n3, '.')) {
                        n2 = ISO8601Utils.indexOfNonDigit(string, ++n3 + 1);
                        int n12 = Math.min(n2, n3 + 3);
                        int n13 = ISO8601Utils.parseInt(string, n3, n12);
                        switch (n12 - n3) {
                            case 2: {
                                n10 = n13 * 10;
                                break;
                            }
                            case 1: {
                                n10 = n13 * 100;
                                break;
                            }
                            default: {
                                n10 = n13;
                            }
                        }
                        n3 = n2;
                    }
                }
            }
            if (string.length() <= n3) {
                throw new IllegalArgumentException("No time zone indicator");
            }
            TimeZone timeZone = null;
            n2 = string.charAt(n3);
            if (n2 == 90) {
                timeZone = TIMEZONE_UTC;
                ++n3;
            } else if (n2 == 43 || n2 == 45) {
                String string2 = string.substring(n3);
                string2 = string2.length() >= 5 ? string2 : string2 + "00";
                n3 += string2.length();
                if ("+0000".equals(string2) || "+00:00".equals(string2)) {
                    timeZone = TIMEZONE_UTC;
                } else {
                    String string3;
                    String string4 = "GMT" + string2;
                    timeZone = TimeZone.getTimeZone(string4);
                    String string5 = timeZone.getID();
                    if (!string5.equals(string4) && !(string3 = string5.replace(":", "")).equals(string4)) {
                        throw new IndexOutOfBoundsException("Mismatching time zone indicator: " + string4 + " given, resolves to " + timeZone.getID());
                    }
                }
            } else {
                throw new IndexOutOfBoundsException("Invalid time zone indicator '" + (char)n2 + "'");
            }
            GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);
            gregorianCalendar.setLenient(false);
            gregorianCalendar.set(1, n4);
            gregorianCalendar.set(2, n5 - 1);
            gregorianCalendar.set(5, n6);
            gregorianCalendar.set(11, n7);
            gregorianCalendar.set(12, n8);
            gregorianCalendar.set(13, n9);
            gregorianCalendar.set(14, n10);
            parsePosition.setIndex(n3);
            return gregorianCalendar.getTime();
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throwable = indexOutOfBoundsException;
        }
        catch (NumberFormatException numberFormatException) {
            throwable = numberFormatException;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throwable = illegalArgumentException;
        }
        String string6 = string == null ? null : "" + '\"' + string + "'";
        String string7 = throwable.getMessage();
        if (string7 == null || string7.isEmpty()) {
            string7 = "(" + throwable.getClass().getName() + ")";
        }
        ParseException parseException = new ParseException("Failed to parse date [" + string6 + "]: " + string7, parsePosition.getIndex());
        parseException.initCause(throwable);
        throw parseException;
    }

    private static boolean checkOffset(String string, int n2, char c2) {
        return n2 < string.length() && string.charAt(n2) == c2;
    }

    private static int parseInt(String string, int n2, int n3) throws NumberFormatException {
        int n4;
        if (n2 < 0 || n3 > string.length() || n2 > n3) {
            throw new NumberFormatException(string);
        }
        int n5 = n2;
        int n6 = 0;
        if (n5 < n3) {
            if ((n4 = Character.digit(string.charAt(n5++), 10)) < 0) {
                throw new NumberFormatException("Invalid number: " + string.substring(n2, n3));
            }
            n6 = - n4;
        }
        while (n5 < n3) {
            if ((n4 = Character.digit(string.charAt(n5++), 10)) < 0) {
                throw new NumberFormatException("Invalid number: " + string.substring(n2, n3));
            }
            n6 *= 10;
            n6 -= n4;
        }
        return - n6;
    }

    private static void padInt(StringBuilder stringBuilder, int n2, int n3) {
        String string = Integer.toString(n2);
        for (int i2 = n3 - string.length(); i2 > 0; --i2) {
            stringBuilder.append('0');
        }
        stringBuilder.append(string);
    }

    private static int indexOfNonDigit(String string, int n2) {
        for (int i2 = n2; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 >= '0' && c2 <= '9') continue;
            return i2;
        }
        return string.length();
    }
}

