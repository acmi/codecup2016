/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class ExsltDatetime {
    static final String dt = "yyyy-MM-dd'T'HH:mm:ss";
    static final String d = "yyyy-MM-dd";
    static final String gym = "yyyy-MM";
    static final String gy = "yyyy";
    static final String gmd = "--MM-dd";
    static final String gm = "--MM--";
    static final String gd = "---dd";
    static final String t = "HH:mm:ss";
    static final String EMPTY_STR = "";

    public static String dateTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        StringBuffer stringBuffer = new StringBuffer(simpleDateFormat.format(date));
        int n2 = calendar.get(15) + calendar.get(16);
        if (n2 == 0) {
            stringBuffer.append('Z');
        } else {
            int n3 = n2 / 3600000;
            int n4 = n2 % 3600000;
            char c2 = n3 < 0 ? '-' : '+';
            stringBuffer.append("" + c2 + ExsltDatetime.formatDigits(n3) + ':' + ExsltDatetime.formatDigits(n4));
        }
        return stringBuffer.toString();
    }

    private static String formatDigits(int n2) {
        String string = String.valueOf(Math.abs(n2));
        return string.length() == 1 ? "" + '0' + string : string;
    }

    public static String date(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[0];
        String string3 = arrstring[1];
        String string4 = arrstring[2];
        if (string3 == null || string4 == null) {
            return "";
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
        String string5 = "yyyy-MM-dd";
        Date date = ExsltDatetime.testFormats(string3, arrstring2);
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(string5);
        simpleDateFormat.setLenient(false);
        String string6 = simpleDateFormat.format(date);
        if (string6.length() == 0) {
            return "";
        }
        return string2 + string6 + string4;
    }

    public static String date() {
        String string = ExsltDatetime.dateTime();
        String string2 = string.substring(0, string.indexOf("T"));
        String string3 = string.substring(ExsltDatetime.getZoneStart(string));
        return string2 + string3;
    }

    public static String time(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        String string3 = arrstring[2];
        if (string2 == null || string3 == null) {
            return "";
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss"};
        String string4 = "HH:mm:ss";
        Date date = ExsltDatetime.testFormats(string2, arrstring2);
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(string4);
        String string5 = simpleDateFormat.format(date);
        return string5 + string3;
    }

    public static String time() {
        String string = ExsltDatetime.dateTime();
        String string2 = string.substring(string.indexOf("T") + 1);
        return string2;
    }

    public static double year(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        boolean bl = arrstring[0].length() == 0;
        String string2 = arrstring[1];
        if (string2 == null) {
            return Double.NaN;
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "yyyy"};
        double d2 = ExsltDatetime.getNumber(string2, arrstring2, 1);
        if (bl || d2 == Double.NaN) {
            return d2;
        }
        return - d2;
    }

    public static double year() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(1);
    }

    public static double monthInYear(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return Double.NaN;
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "--MM--", "--MM-dd"};
        return ExsltDatetime.getNumber(string2, arrstring2, 2) + 1.0;
    }

    public static double monthInYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(2) + 1;
    }

    public static double weekInYear(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return Double.NaN;
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
        return ExsltDatetime.getNumber(string2, arrstring2, 3);
    }

    public static double weekInYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(3);
    }

    public static double dayInYear(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return Double.NaN;
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
        return ExsltDatetime.getNumber(string2, arrstring2, 6);
    }

    public static double dayInYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(6);
    }

    public static double dayInMonth(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "--MM-dd", "---dd"};
        double d2 = ExsltDatetime.getNumber(string2, arrstring2, 5);
        return d2;
    }

    public static double dayInMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(5);
    }

    public static double dayOfWeekInMonth(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return Double.NaN;
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
        return ExsltDatetime.getNumber(string2, arrstring2, 8);
    }

    public static double dayOfWeekInMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(8);
    }

    public static double dayInWeek(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return Double.NaN;
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
        return ExsltDatetime.getNumber(string2, arrstring2, 7);
    }

    public static double dayInWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(7);
    }

    public static double hourInDay(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return Double.NaN;
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss"};
        return ExsltDatetime.getNumber(string2, arrstring2, 11);
    }

    public static double hourInDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(11);
    }

    public static double minuteInHour(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return Double.NaN;
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss"};
        return ExsltDatetime.getNumber(string2, arrstring2, 12);
    }

    public static double minuteInHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(12);
    }

    public static double secondInMinute(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return Double.NaN;
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss"};
        return ExsltDatetime.getNumber(string2, arrstring2, 13);
    }

    public static double secondInMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(13);
    }

    public static XObject leapYear(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return new XNumber(Double.NaN);
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "yyyy"};
        double d2 = ExsltDatetime.getNumber(string2, arrstring2, 1);
        if (d2 == Double.NaN) {
            return new XNumber(Double.NaN);
        }
        int n2 = (int)d2;
        return new XBoolean(n2 % 400 == 0 || n2 % 100 != 0 && n2 % 4 == 0);
    }

    public static boolean leapYear() {
        Calendar calendar = Calendar.getInstance();
        int n2 = calendar.get(1);
        return n2 % 400 == 0 || n2 % 100 != 0 && n2 % 4 == 0;
    }

    public static String monthName(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return "";
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "--MM--"};
        String string3 = "MMMM";
        return ExsltDatetime.getNameOrAbbrev(string, arrstring2, string3);
    }

    public static String monthName() {
        String string = "MMMM";
        return ExsltDatetime.getNameOrAbbrev(string);
    }

    public static String monthAbbreviation(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return "";
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "--MM--"};
        String string3 = "MMM";
        return ExsltDatetime.getNameOrAbbrev(string, arrstring2, string3);
    }

    public static String monthAbbreviation() {
        String string = "MMM";
        return ExsltDatetime.getNameOrAbbrev(string);
    }

    public static String dayName(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return "";
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
        String string3 = "EEEE";
        return ExsltDatetime.getNameOrAbbrev(string, arrstring2, string3);
    }

    public static String dayName() {
        String string = "EEEE";
        return ExsltDatetime.getNameOrAbbrev(string);
    }

    public static String dayAbbreviation(String string) throws ParseException {
        String[] arrstring = ExsltDatetime.getEraDatetimeZone(string);
        String string2 = arrstring[1];
        if (string2 == null) {
            return "";
        }
        String[] arrstring2 = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
        String string3 = "EEE";
        return ExsltDatetime.getNameOrAbbrev(string, arrstring2, string3);
    }

    public static String dayAbbreviation() {
        String string = "EEE";
        return ExsltDatetime.getNameOrAbbrev(string);
    }

    private static String[] getEraDatetimeZone(String string) {
        int n2;
        String string2 = "";
        String string3 = string;
        String string4 = "";
        if (string.charAt(0) == '-' && !string.startsWith("--")) {
            string2 = "-";
            string3 = string.substring(1);
        }
        if ((n2 = ExsltDatetime.getZoneStart(string3)) > 0) {
            string4 = string3.substring(n2);
            string3 = string3.substring(0, n2);
        } else if (n2 == -2) {
            string4 = null;
        }
        return new String[]{string2, string3, string4};
    }

    private static int getZoneStart(String string) {
        if (string.indexOf(90) == string.length() - 1) {
            return string.length() - 1;
        }
        if (string.length() >= 6 && string.charAt(string.length() - 3) == ':' && (string.charAt(string.length() - 6) == '+' || string.charAt(string.length() - 6) == '-')) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                simpleDateFormat.setLenient(false);
                Date date = simpleDateFormat.parse(string.substring(string.length() - 5));
                return string.length() - 6;
            }
            catch (ParseException parseException) {
                System.out.println("ParseException " + parseException.getErrorOffset());
                return -2;
            }
        }
        return -1;
    }

    private static Date testFormats(String string, String[] arrstring) throws ParseException {
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(arrstring[i2]);
                simpleDateFormat.setLenient(false);
                return simpleDateFormat.parse(string);
            }
            catch (ParseException parseException) {
                continue;
            }
        }
        return null;
    }

    private static double getNumber(String string, String[] arrstring, int n2) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        Date date = ExsltDatetime.testFormats(string, arrstring);
        if (date == null) {
            return Double.NaN;
        }
        calendar.setTime(date);
        return calendar.get(n2);
    }

    private static String getNameOrAbbrev(String string, String[] arrstring, String string2) throws ParseException {
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(arrstring[i2], Locale.ENGLISH);
                simpleDateFormat.setLenient(false);
                Date date = simpleDateFormat.parse(string);
                simpleDateFormat.applyPattern(string2);
                return simpleDateFormat.format(date);
            }
            catch (ParseException parseException) {
                continue;
            }
        }
        return "";
    }

    private static String getNameOrAbbrev(String string) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(string, Locale.ENGLISH);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String formatDate(String string, String string2) {
        String string3;
        String[] arrstring;
        TimeZone timeZone;
        String string4 = "Gy";
        String string5 = "M";
        String string6 = "dDEFwW";
        if (string.endsWith("Z") || string.endsWith("z")) {
            timeZone = TimeZone.getTimeZone("GMT");
            string = string.substring(0, string.length() - 1) + "GMT";
            string3 = "z";
        } else if (string.length() >= 6 && string.charAt(string.length() - 3) == ':' && (string.charAt(string.length() - 6) == '+' || string.charAt(string.length() - 6) == '-')) {
            arrstring = string.substring(string.length() - 6);
            timeZone = "+00:00".equals(arrstring) || "-00:00".equals(arrstring) ? TimeZone.getTimeZone("GMT") : TimeZone.getTimeZone("GMT" + (String)arrstring);
            string3 = "z";
            string = string.substring(0, string.length() - 6) + "GMT" + (String)arrstring;
        } else {
            timeZone = TimeZone.getDefault();
            string3 = "";
        }
        arrstring = new String[]{"yyyy-MM-dd'T'HH:mm:ss" + string3, "yyyy-MM-dd", "yyyy-MM", "yyyy"};
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss" + string3);
            simpleDateFormat.setLenient(false);
            Date date = simpleDateFormat.parse(string);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(ExsltDatetime.strip("GyMdDEFwW", string2));
            simpleDateFormat2.setTimeZone(timeZone);
            return simpleDateFormat2.format(date);
        }
        catch (ParseException parseException) {
            for (int i2 = 0; i2 < arrstring.length; ++i2) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(arrstring[i2]);
                    simpleDateFormat.setLenient(false);
                    Date date = simpleDateFormat.parse(string);
                    SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(string2);
                    simpleDateFormat3.setTimeZone(timeZone);
                    return simpleDateFormat3.format(date);
                }
                catch (ParseException parseException2) {
                    continue;
                }
            }
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("--MM-dd");
                simpleDateFormat.setLenient(false);
                Date date = simpleDateFormat.parse(string);
                SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat(ExsltDatetime.strip("Gy", string2));
                simpleDateFormat4.setTimeZone(timeZone);
                return simpleDateFormat4.format(date);
            }
            catch (ParseException parseException3) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("--MM--");
                    simpleDateFormat.setLenient(false);
                    Date date = simpleDateFormat.parse(string);
                    SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat(ExsltDatetime.strip("Gy", string2));
                    simpleDateFormat5.setTimeZone(timeZone);
                    return simpleDateFormat5.format(date);
                }
                catch (ParseException parseException4) {
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("---dd");
                        simpleDateFormat.setLenient(false);
                        Date date = simpleDateFormat.parse(string);
                        SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat(ExsltDatetime.strip("GyM", string2));
                        simpleDateFormat6.setTimeZone(timeZone);
                        return simpleDateFormat6.format(date);
                    }
                    catch (ParseException parseException5) {
                        return "";
                    }
                }
            }
        }
    }

    private static String strip(String string, String string2) {
        int n2 = 0;
        StringBuffer stringBuffer = new StringBuffer(string2.length());
        while (n2 < string2.length()) {
            char c2 = string2.charAt(n2);
            if (c2 == '\'') {
                int n3 = string2.indexOf(39, n2 + 1);
                if (n3 == -1) {
                    n3 = string2.length();
                }
                stringBuffer.append(string2.substring(n2, n3));
                n2 = n3++;
                continue;
            }
            if (string.indexOf(c2) > -1) {
                ++n2;
                continue;
            }
            stringBuffer.append(c2);
            ++n2;
        }
        return stringBuffer.toString();
    }
}

