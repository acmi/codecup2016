/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.impl.dv.xs.DurationDV;
import org.apache.xerces.impl.dv.xs.TypeValidator;
import org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl;
import org.apache.xerces.xs.datatypes.XSDateTime;

public abstract class AbstractDateTimeDV
extends TypeValidator {
    private static final boolean DEBUG = false;
    protected static final int YEAR = 2000;
    protected static final int MONTH = 1;
    protected static final int DAY = 1;
    protected static final DatatypeFactory datatypeFactory = new DatatypeFactoryImpl();

    public short getAllowedFacets() {
        return 2552;
    }

    public boolean isIdentical(Object object, Object object2) {
        if (!(object instanceof DateTimeData) || !(object2 instanceof DateTimeData)) {
            return false;
        }
        DateTimeData dateTimeData = (DateTimeData)object;
        DateTimeData dateTimeData2 = (DateTimeData)object2;
        if (dateTimeData.timezoneHr == dateTimeData2.timezoneHr && dateTimeData.timezoneMin == dateTimeData2.timezoneMin) {
            return dateTimeData.equals(dateTimeData2);
        }
        return false;
    }

    public int compare(Object object, Object object2) {
        return this.compareDates((DateTimeData)object, (DateTimeData)object2, true);
    }

    protected short compareDates(DateTimeData dateTimeData, DateTimeData dateTimeData2, boolean bl) {
        if (dateTimeData.utc == dateTimeData2.utc) {
            return this.compareOrder(dateTimeData, dateTimeData2);
        }
        DateTimeData dateTimeData3 = new DateTimeData(null, this);
        if (dateTimeData.utc == 90) {
            this.cloneDate(dateTimeData2, dateTimeData3);
            dateTimeData3.timezoneHr = 14;
            dateTimeData3.timezoneMin = 0;
            dateTimeData3.utc = 43;
            this.normalize(dateTimeData3);
            short s2 = this.compareOrder(dateTimeData, dateTimeData3);
            if (s2 == -1) {
                return s2;
            }
            this.cloneDate(dateTimeData2, dateTimeData3);
            dateTimeData3.timezoneHr = -14;
            dateTimeData3.timezoneMin = 0;
            dateTimeData3.utc = 45;
            this.normalize(dateTimeData3);
            short s3 = this.compareOrder(dateTimeData, dateTimeData3);
            if (s3 == 1) {
                return s3;
            }
            return 2;
        }
        if (dateTimeData2.utc == 90) {
            this.cloneDate(dateTimeData, dateTimeData3);
            dateTimeData3.timezoneHr = -14;
            dateTimeData3.timezoneMin = 0;
            dateTimeData3.utc = 45;
            this.normalize(dateTimeData3);
            short s4 = this.compareOrder(dateTimeData3, dateTimeData2);
            if (s4 == -1) {
                return s4;
            }
            this.cloneDate(dateTimeData, dateTimeData3);
            dateTimeData3.timezoneHr = 14;
            dateTimeData3.timezoneMin = 0;
            dateTimeData3.utc = 43;
            this.normalize(dateTimeData3);
            short s5 = this.compareOrder(dateTimeData3, dateTimeData2);
            if (s5 == 1) {
                return s5;
            }
            return 2;
        }
        return 2;
    }

    protected short compareOrder(DateTimeData dateTimeData, DateTimeData dateTimeData2) {
        if (dateTimeData.position < 1) {
            if (dateTimeData.year < dateTimeData2.year) {
                return -1;
            }
            if (dateTimeData.year > dateTimeData2.year) {
                return 1;
            }
        }
        if (dateTimeData.position < 2) {
            if (dateTimeData.month < dateTimeData2.month) {
                return -1;
            }
            if (dateTimeData.month > dateTimeData2.month) {
                return 1;
            }
        }
        if (dateTimeData.day < dateTimeData2.day) {
            return -1;
        }
        if (dateTimeData.day > dateTimeData2.day) {
            return 1;
        }
        if (dateTimeData.hour < dateTimeData2.hour) {
            return -1;
        }
        if (dateTimeData.hour > dateTimeData2.hour) {
            return 1;
        }
        if (dateTimeData.minute < dateTimeData2.minute) {
            return -1;
        }
        if (dateTimeData.minute > dateTimeData2.minute) {
            return 1;
        }
        if (dateTimeData.second < dateTimeData2.second) {
            return -1;
        }
        if (dateTimeData.second > dateTimeData2.second) {
            return 1;
        }
        if (dateTimeData.utc < dateTimeData2.utc) {
            return -1;
        }
        if (dateTimeData.utc > dateTimeData2.utc) {
            return 1;
        }
        return 0;
    }

    protected void getTime(String string, int n2, int n3, DateTimeData dateTimeData) throws RuntimeException {
        int n4 = n2 + 2;
        dateTimeData.hour = this.parseInt(string, n2, n4);
        if (string.charAt(n4++) != ':') {
            throw new RuntimeException("Error in parsing time zone");
        }
        n2 = n4;
        dateTimeData.minute = this.parseInt(string, n2, n4 += 2);
        if (string.charAt(n4++) != ':') {
            throw new RuntimeException("Error in parsing time zone");
        }
        int n5 = this.findUTCSign(string, n2, n3);
        n2 = n4;
        n4 = n5 < 0 ? n3 : n5;
        dateTimeData.second = this.parseSecond(string, n2, n4);
        if (n5 > 0) {
            this.getTimeZone(string, dateTimeData, n5, n3);
        }
    }

    protected int getDate(String string, int n2, int n3, DateTimeData dateTimeData) throws RuntimeException {
        n2 = this.getYearMonth(string, n2, n3, dateTimeData);
        if (string.charAt(n2++) != '-') {
            throw new RuntimeException("CCYY-MM must be followed by '-' sign");
        }
        int n4 = n2 + 2;
        dateTimeData.day = this.parseInt(string, n2, n4);
        return n4;
    }

    protected int getYearMonth(String string, int n2, int n3, DateTimeData dateTimeData) throws RuntimeException {
        int n4;
        if (string.charAt(0) == '-') {
            ++n2;
        }
        if ((n4 = this.indexOf(string, n2, n3, '-')) == -1) {
            throw new RuntimeException("Year separator is missing or misplaced");
        }
        int n5 = n4 - n2;
        if (n5 < 4) {
            throw new RuntimeException("Year must have 'CCYY' format");
        }
        if (n5 > 4 && string.charAt(n2) == '0') {
            throw new RuntimeException("Leading zeros are required if the year value would otherwise have fewer than four digits; otherwise they are forbidden");
        }
        dateTimeData.year = this.parseIntYear(string, n4);
        if (string.charAt(n4) != '-') {
            throw new RuntimeException("CCYY must be followed by '-' sign");
        }
        n2 = ++n4;
        n4 = n2 + 2;
        dateTimeData.month = this.parseInt(string, n2, n4);
        return n4;
    }

    protected void parseTimeZone(String string, int n2, int n3, DateTimeData dateTimeData) throws RuntimeException {
        if (n2 < n3) {
            if (!this.isNextCharUTCSign(string, n2, n3)) {
                throw new RuntimeException("Error in month parsing");
            }
            this.getTimeZone(string, dateTimeData, n2, n3);
        }
    }

    protected void getTimeZone(String string, DateTimeData dateTimeData, int n2, int n3) throws RuntimeException {
        dateTimeData.utc = string.charAt(n2);
        if (string.charAt(n2) == 'Z') {
            if (n3 > ++n2) {
                throw new RuntimeException("Error in parsing time zone");
            }
            return;
        }
        if (n2 <= n3 - 6) {
            int n4 = string.charAt(n2) == '-' ? -1 : 1;
            int n5 = ++n2 + 2;
            dateTimeData.timezoneHr = n4 * this.parseInt(string, n2, n5);
            if (string.charAt(n5++) != ':') {
                throw new RuntimeException("Error in parsing time zone");
            }
            dateTimeData.timezoneMin = n4 * this.parseInt(string, n5, n5 + 2);
            if (n5 + 2 != n3) {
                throw new RuntimeException("Error in parsing time zone");
            }
            if (dateTimeData.timezoneHr != 0 || dateTimeData.timezoneMin != 0) {
                dateTimeData.normalized = false;
            }
        } else {
            throw new RuntimeException("Error in parsing time zone");
        }
    }

    protected int indexOf(String string, int n2, int n3, char c2) {
        int n4 = n2;
        while (n4 < n3) {
            if (string.charAt(n4) == c2) {
                return n4;
            }
            ++n4;
        }
        return -1;
    }

    protected void validateDateTime(DateTimeData dateTimeData) {
        if (dateTimeData.year == 0) {
            throw new RuntimeException("The year \"0000\" is an illegal year value");
        }
        if (dateTimeData.month < 1 || dateTimeData.month > 12) {
            throw new RuntimeException("The month must have values 1 to 12");
        }
        if (dateTimeData.day > this.maxDayInMonthFor(dateTimeData.year, dateTimeData.month) || dateTimeData.day < 1) {
            throw new RuntimeException("The day must have values 1 to 31");
        }
        if (dateTimeData.hour > 23 || dateTimeData.hour < 0) {
            if (dateTimeData.hour == 24 && dateTimeData.minute == 0 && dateTimeData.second == 0.0) {
                dateTimeData.hour = 0;
                if (++dateTimeData.day > this.maxDayInMonthFor(dateTimeData.year, dateTimeData.month)) {
                    dateTimeData.day = 1;
                    if (++dateTimeData.month > 12) {
                        dateTimeData.month = 1;
                        if (++dateTimeData.year == 0) {
                            dateTimeData.year = 1;
                        }
                    }
                }
            } else {
                throw new RuntimeException("Hour must have values 0-23, unless 24:00:00");
            }
        }
        if (dateTimeData.minute > 59 || dateTimeData.minute < 0) {
            throw new RuntimeException("Minute must have values 0-59");
        }
        if (dateTimeData.second >= 60.0 || dateTimeData.second < 0.0) {
            throw new RuntimeException("Second must have values 0-59");
        }
        if (dateTimeData.timezoneHr > 14 || dateTimeData.timezoneHr < -14) {
            throw new RuntimeException("Time zone should have range -14:00 to +14:00");
        }
        if ((dateTimeData.timezoneHr == 14 || dateTimeData.timezoneHr == -14) && dateTimeData.timezoneMin != 0) {
            throw new RuntimeException("Time zone should have range -14:00 to +14:00");
        }
        if (dateTimeData.timezoneMin > 59 || dateTimeData.timezoneMin < -59) {
            throw new RuntimeException("Minute must have values 0-59");
        }
    }

    protected int findUTCSign(String string, int n2, int n3) {
        int n4 = n2;
        while (n4 < n3) {
            char c2 = string.charAt(n4);
            if (c2 == 'Z' || c2 == '+' || c2 == '-') {
                return n4;
            }
            ++n4;
        }
        return -1;
    }

    protected final boolean isNextCharUTCSign(String string, int n2, int n3) {
        if (n2 < n3) {
            char c2 = string.charAt(n2);
            return c2 == 'Z' || c2 == '+' || c2 == '-';
        }
        return false;
    }

    protected int parseInt(String string, int n2, int n3) throws NumberFormatException {
        int n4 = 10;
        int n5 = 0;
        int n6 = 0;
        int n7 = -2147483647;
        int n8 = n7 / n4;
        int n9 = n2;
        do {
            if ((n6 = TypeValidator.getDigit(string.charAt(n9))) < 0) {
                throw new NumberFormatException("'" + string + "' has wrong format");
            }
            if (n5 < n8) {
                throw new NumberFormatException("'" + string + "' has wrong format");
            }
            if ((n5 *= n4) < n7 + n6) {
                throw new NumberFormatException("'" + string + "' has wrong format");
            }
            n5 -= n6;
        } while (++n9 < n3);
        return - n5;
    }

    protected int parseIntYear(String string, int n2) {
        int n3;
        int n4 = 10;
        int n5 = 0;
        boolean bl = false;
        int n6 = 0;
        int n7 = 0;
        if (string.charAt(0) == '-') {
            bl = true;
            n3 = Integer.MIN_VALUE;
            ++n6;
        } else {
            n3 = -2147483647;
        }
        int n8 = n3 / n4;
        while (n6 < n2) {
            if ((n7 = TypeValidator.getDigit(string.charAt(n6++))) < 0) {
                throw new NumberFormatException("'" + string + "' has wrong format");
            }
            if (n5 < n8) {
                throw new NumberFormatException("'" + string + "' has wrong format");
            }
            if ((n5 *= n4) < n3 + n7) {
                throw new NumberFormatException("'" + string + "' has wrong format");
            }
            n5 -= n7;
        }
        if (bl) {
            if (n6 > 1) {
                return n5;
            }
            throw new NumberFormatException("'" + string + "' has wrong format");
        }
        return - n5;
    }

    protected void normalize(DateTimeData dateTimeData) {
        int n2 = -1;
        int n3 = dateTimeData.minute + n2 * dateTimeData.timezoneMin;
        int n4 = this.fQuotient(n3, 60);
        dateTimeData.minute = this.mod(n3, 60, n4);
        n3 = dateTimeData.hour + n2 * dateTimeData.timezoneHr + n4;
        n4 = this.fQuotient(n3, 24);
        dateTimeData.hour = this.mod(n3, 24, n4);
        dateTimeData.day += n4;
        do {
            n3 = this.maxDayInMonthFor(dateTimeData.year, dateTimeData.month);
            if (dateTimeData.day < 1) {
                dateTimeData.day += this.maxDayInMonthFor(dateTimeData.year, dateTimeData.month - 1);
                n4 = -1;
            } else {
                if (dateTimeData.day <= n3) break;
                dateTimeData.day -= n3;
                n4 = 1;
            }
            n3 = dateTimeData.month + n4;
            dateTimeData.month = this.modulo(n3, 1, 13);
            dateTimeData.year += this.fQuotient(n3, 1, 13);
            if (dateTimeData.year != 0) continue;
            dateTimeData.year = dateTimeData.timezoneHr < 0 || dateTimeData.timezoneMin < 0 ? 1 : -1;
        } while (true);
        dateTimeData.utc = 90;
    }

    protected void saveUnnormalized(DateTimeData dateTimeData) {
        dateTimeData.unNormYear = dateTimeData.year;
        dateTimeData.unNormMonth = dateTimeData.month;
        dateTimeData.unNormDay = dateTimeData.day;
        dateTimeData.unNormHour = dateTimeData.hour;
        dateTimeData.unNormMinute = dateTimeData.minute;
        dateTimeData.unNormSecond = dateTimeData.second;
    }

    protected void resetDateObj(DateTimeData dateTimeData) {
        dateTimeData.year = 0;
        dateTimeData.month = 0;
        dateTimeData.day = 0;
        dateTimeData.hour = 0;
        dateTimeData.minute = 0;
        dateTimeData.second = 0.0;
        dateTimeData.utc = 0;
        dateTimeData.timezoneHr = 0;
        dateTimeData.timezoneMin = 0;
    }

    protected int maxDayInMonthFor(int n2, int n3) {
        if (n3 == 4 || n3 == 6 || n3 == 9 || n3 == 11) {
            return 30;
        }
        if (n3 == 2) {
            if (this.isLeapYear(n2)) {
                return 29;
            }
            return 28;
        }
        return 31;
    }

    private boolean isLeapYear(int n2) {
        return n2 % 4 == 0 && (n2 % 100 != 0 || n2 % 400 == 0);
    }

    protected int mod(int n2, int n3, int n4) {
        return n2 - n4 * n3;
    }

    protected int fQuotient(int n2, int n3) {
        return (int)Math.floor((float)n2 / (float)n3);
    }

    protected int modulo(int n2, int n3, int n4) {
        int n5 = n2 - n3;
        int n6 = n4 - n3;
        return this.mod(n5, n6, this.fQuotient(n5, n6)) + n3;
    }

    protected int fQuotient(int n2, int n3, int n4) {
        return this.fQuotient(n2 - n3, n4 - n3);
    }

    protected String dateToString(DateTimeData dateTimeData) {
        StringBuffer stringBuffer = new StringBuffer(25);
        this.append(stringBuffer, dateTimeData.year, 4);
        stringBuffer.append('-');
        this.append(stringBuffer, dateTimeData.month, 2);
        stringBuffer.append('-');
        this.append(stringBuffer, dateTimeData.day, 2);
        stringBuffer.append('T');
        this.append(stringBuffer, dateTimeData.hour, 2);
        stringBuffer.append(':');
        this.append(stringBuffer, dateTimeData.minute, 2);
        stringBuffer.append(':');
        this.append(stringBuffer, dateTimeData.second);
        this.append(stringBuffer, (char)dateTimeData.utc, 0);
        return stringBuffer.toString();
    }

    protected final void append(StringBuffer stringBuffer, int n2, int n3) {
        if (n2 == Integer.MIN_VALUE) {
            stringBuffer.append(n2);
            return;
        }
        if (n2 < 0) {
            stringBuffer.append('-');
            n2 = - n2;
        }
        if (n3 == 4) {
            if (n2 < 10) {
                stringBuffer.append("000");
            } else if (n2 < 100) {
                stringBuffer.append("00");
            } else if (n2 < 1000) {
                stringBuffer.append("0");
            }
            stringBuffer.append(n2);
        } else if (n3 == 2) {
            if (n2 < 10) {
                stringBuffer.append('0');
            }
            stringBuffer.append(n2);
        } else if (n2 != 0) {
            stringBuffer.append((char)n2);
        }
    }

    protected final void append(StringBuffer stringBuffer, double d2) {
        if (d2 < 0.0) {
            stringBuffer.append('-');
            d2 = - d2;
        }
        if (d2 < 10.0) {
            stringBuffer.append('0');
        }
        this.append2(stringBuffer, d2);
    }

    protected final void append2(StringBuffer stringBuffer, double d2) {
        int n2 = (int)d2;
        if (d2 == (double)n2) {
            stringBuffer.append(n2);
        } else {
            this.append3(stringBuffer, d2);
        }
    }

    private void append3(StringBuffer stringBuffer, double d2) {
        String string = String.valueOf(d2);
        int n2 = string.indexOf(69);
        if (n2 == -1) {
            stringBuffer.append(string);
            return;
        }
        if (d2 < 1.0) {
            int n3;
            int n4;
            try {
                n3 = this.parseInt(string, n2 + 2, string.length());
            }
            catch (Exception exception) {
                stringBuffer.append(string);
                return;
            }
            stringBuffer.append("0.");
            int n5 = 1;
            while (n5 < n3) {
                stringBuffer.append('0');
                ++n5;
            }
            int n6 = n2 - 1;
            while (n6 > 0) {
                n4 = string.charAt(n6);
                if (n4 != 48) break;
                --n6;
            }
            n4 = 0;
            while (n4 <= n6) {
                char c2 = string.charAt(n4);
                if (c2 != '.') {
                    stringBuffer.append(c2);
                }
                ++n4;
            }
        } else {
            int n7;
            char c3;
            try {
                n7 = this.parseInt(string, n2 + 1, string.length());
            }
            catch (Exception exception) {
                stringBuffer.append(string);
                return;
            }
            int n8 = n7 + 2;
            int n9 = 0;
            while (n9 < n2) {
                c3 = string.charAt(n9);
                if (c3 != '.') {
                    if (n9 == n8) {
                        stringBuffer.append('.');
                    }
                    stringBuffer.append(c3);
                }
                ++n9;
            }
            c3 = n8 - n2;
            while (c3 > '\u0000') {
                stringBuffer.append('0');
                --c3;
            }
        }
    }

    protected double parseSecond(String string, int n2, int n3) throws NumberFormatException {
        int n4 = -1;
        int n5 = n2;
        while (n5 < n3) {
            char c2 = string.charAt(n5);
            if (c2 == '.') {
                n4 = n5;
            } else if (c2 > '9' || c2 < '0') {
                throw new NumberFormatException("'" + string + "' has wrong format");
            }
            ++n5;
        }
        if (n4 == -1 ? n2 + 2 != n3 : n2 + 2 != n4 || n4 + 1 == n3) {
            throw new NumberFormatException("'" + string + "' has wrong format");
        }
        return Double.parseDouble(string.substring(n2, n3));
    }

    private void cloneDate(DateTimeData dateTimeData, DateTimeData dateTimeData2) {
        dateTimeData2.year = dateTimeData.year;
        dateTimeData2.month = dateTimeData.month;
        dateTimeData2.day = dateTimeData.day;
        dateTimeData2.hour = dateTimeData.hour;
        dateTimeData2.minute = dateTimeData.minute;
        dateTimeData2.second = dateTimeData.second;
        dateTimeData2.utc = dateTimeData.utc;
        dateTimeData2.timezoneHr = dateTimeData.timezoneHr;
        dateTimeData2.timezoneMin = dateTimeData.timezoneMin;
    }

    protected XMLGregorianCalendar getXMLGregorianCalendar(DateTimeData dateTimeData) {
        return null;
    }

    protected Duration getDuration(DateTimeData dateTimeData) {
        return null;
    }

    protected final BigDecimal getFractionalSecondsAsBigDecimal(DateTimeData dateTimeData) {
        StringBuffer stringBuffer = new StringBuffer();
        this.append3(stringBuffer, dateTimeData.unNormSecond);
        String string = stringBuffer.toString();
        int n2 = string.indexOf(46);
        if (n2 == -1) {
            return null;
        }
        BigDecimal bigDecimal = new BigDecimal(string = string.substring(n2));
        if (bigDecimal.compareTo(BigDecimal.valueOf(0)) == 0) {
            return null;
        }
        return bigDecimal;
    }

    static final class DateTimeData
    implements XSDateTime {
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int utc;
        double second;
        int timezoneHr;
        int timezoneMin;
        private String originalValue;
        boolean normalized = true;
        int unNormYear;
        int unNormMonth;
        int unNormDay;
        int unNormHour;
        int unNormMinute;
        double unNormSecond;
        int position;
        final AbstractDateTimeDV type;
        private String canonical;

        public DateTimeData(String string, AbstractDateTimeDV abstractDateTimeDV) {
            this.originalValue = string;
            this.type = abstractDateTimeDV;
        }

        public DateTimeData(int n2, int n3, int n4, int n5, int n6, double d2, int n7, String string, boolean bl, AbstractDateTimeDV abstractDateTimeDV) {
            this.year = n2;
            this.month = n3;
            this.day = n4;
            this.hour = n5;
            this.minute = n6;
            this.second = d2;
            this.utc = n7;
            this.type = abstractDateTimeDV;
            this.originalValue = string;
        }

        public boolean equals(Object object) {
            if (!(object instanceof DateTimeData)) {
                return false;
            }
            return this.type.compareDates(this, (DateTimeData)object, true) == 0;
        }

        public synchronized String toString() {
            if (this.canonical == null) {
                this.canonical = this.type.dateToString(this);
            }
            return this.canonical;
        }

        public int getYears() {
            if (this.type instanceof DurationDV) {
                return 0;
            }
            return this.normalized ? this.year : this.unNormYear;
        }

        public int getMonths() {
            if (this.type instanceof DurationDV) {
                return this.year * 12 + this.month;
            }
            return this.normalized ? this.month : this.unNormMonth;
        }

        public int getDays() {
            if (this.type instanceof DurationDV) {
                return 0;
            }
            return this.normalized ? this.day : this.unNormDay;
        }

        public int getHours() {
            if (this.type instanceof DurationDV) {
                return 0;
            }
            return this.normalized ? this.hour : this.unNormHour;
        }

        public int getMinutes() {
            if (this.type instanceof DurationDV) {
                return 0;
            }
            return this.normalized ? this.minute : this.unNormMinute;
        }

        public double getSeconds() {
            if (this.type instanceof DurationDV) {
                return (double)(this.day * 24 * 60 * 60 + this.hour * 60 * 60 + this.minute * 60) + this.second;
            }
            return this.normalized ? this.second : this.unNormSecond;
        }

        public boolean hasTimeZone() {
            return this.utc != 0;
        }

        public int getTimeZoneHours() {
            return this.timezoneHr;
        }

        public int getTimeZoneMinutes() {
            return this.timezoneMin;
        }

        public String getLexicalValue() {
            return this.originalValue;
        }

        public XSDateTime normalize() {
            if (!this.normalized) {
                DateTimeData dateTimeData = (DateTimeData)this.clone();
                dateTimeData.normalized = true;
                return dateTimeData;
            }
            return this;
        }

        public boolean isNormalized() {
            return this.normalized;
        }

        public Object clone() {
            DateTimeData dateTimeData = new DateTimeData(this.year, this.month, this.day, this.hour, this.minute, this.second, this.utc, this.originalValue, this.normalized, this.type);
            dateTimeData.canonical = this.canonical;
            dateTimeData.position = this.position;
            dateTimeData.timezoneHr = this.timezoneHr;
            dateTimeData.timezoneMin = this.timezoneMin;
            dateTimeData.unNormYear = this.unNormYear;
            dateTimeData.unNormMonth = this.unNormMonth;
            dateTimeData.unNormDay = this.unNormDay;
            dateTimeData.unNormHour = this.unNormHour;
            dateTimeData.unNormMinute = this.unNormMinute;
            dateTimeData.unNormSecond = this.unNormSecond;
            return dateTimeData;
        }

        public XMLGregorianCalendar getXMLGregorianCalendar() {
            return this.type.getXMLGregorianCalendar(this);
        }

        public Duration getDuration() {
            return this.type.getDuration(this);
        }
    }

}

