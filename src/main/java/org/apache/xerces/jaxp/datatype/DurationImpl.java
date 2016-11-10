/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.datatype;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.jaxp.datatype.SerializedDuration;
import org.apache.xerces.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.xerces.util.DatatypeMessageFormatter;

class DurationImpl
extends Duration
implements Serializable {
    private static final long serialVersionUID = -2650025807136350131L;
    private static final DatatypeConstants.Field[] FIELDS = new DatatypeConstants.Field[]{DatatypeConstants.YEARS, DatatypeConstants.MONTHS, DatatypeConstants.DAYS, DatatypeConstants.HOURS, DatatypeConstants.MINUTES, DatatypeConstants.SECONDS};
    private static final BigDecimal ZERO = BigDecimal.valueOf(0);
    private final int signum;
    private final BigInteger years;
    private final BigInteger months;
    private final BigInteger days;
    private final BigInteger hours;
    private final BigInteger minutes;
    private final BigDecimal seconds;
    private static final XMLGregorianCalendar[] TEST_POINTS = new XMLGregorianCalendar[]{XMLGregorianCalendarImpl.parse("1696-09-01T00:00:00Z"), XMLGregorianCalendarImpl.parse("1697-02-01T00:00:00Z"), XMLGregorianCalendarImpl.parse("1903-03-01T00:00:00Z"), XMLGregorianCalendarImpl.parse("1903-07-01T00:00:00Z")};
    private static final BigDecimal[] FACTORS = new BigDecimal[]{BigDecimal.valueOf(12), null, BigDecimal.valueOf(24), BigDecimal.valueOf(60), BigDecimal.valueOf(60)};

    public int getSign() {
        return this.signum;
    }

    private int calcSignum(boolean bl) {
        if (!(this.years != null && this.years.signum() != 0 || this.months != null && this.months.signum() != 0 || this.days != null && this.days.signum() != 0 || this.hours != null && this.hours.signum() != 0 || this.minutes != null && this.minutes.signum() != 0 || this.seconds != null && this.seconds.signum() != 0)) {
            return 0;
        }
        if (bl) {
            return 1;
        }
        return -1;
    }

    protected DurationImpl(boolean bl, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigDecimal bigDecimal) {
        this.years = bigInteger;
        this.months = bigInteger2;
        this.days = bigInteger3;
        this.hours = bigInteger4;
        this.minutes = bigInteger5;
        this.seconds = bigDecimal;
        this.signum = this.calcSignum(bl);
        if (bigInteger == null && bigInteger2 == null && bigInteger3 == null && bigInteger4 == null && bigInteger5 == null && bigDecimal == null) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "AllFieldsNull", null));
        }
        DurationImpl.testNonNegative(bigInteger, DatatypeConstants.YEARS);
        DurationImpl.testNonNegative(bigInteger2, DatatypeConstants.MONTHS);
        DurationImpl.testNonNegative(bigInteger3, DatatypeConstants.DAYS);
        DurationImpl.testNonNegative(bigInteger4, DatatypeConstants.HOURS);
        DurationImpl.testNonNegative(bigInteger5, DatatypeConstants.MINUTES);
        DurationImpl.testNonNegative(bigDecimal, DatatypeConstants.SECONDS);
    }

    private static void testNonNegative(BigInteger bigInteger, DatatypeConstants.Field field) {
        if (bigInteger != null && bigInteger.signum() < 0) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "NegativeField", new Object[]{field.toString()}));
        }
    }

    private static void testNonNegative(BigDecimal bigDecimal, DatatypeConstants.Field field) {
        if (bigDecimal != null && bigDecimal.signum() < 0) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "NegativeField", new Object[]{field.toString()}));
        }
    }

    protected DurationImpl(boolean bl, int n2, int n3, int n4, int n5, int n6, int n7) {
        this(bl, DurationImpl.wrap(n2), DurationImpl.wrap(n3), DurationImpl.wrap(n4), DurationImpl.wrap(n5), DurationImpl.wrap(n6), n7 != 0 ? BigDecimal.valueOf(n7) : null);
    }

    private static BigInteger wrap(int n2) {
        if (n2 == Integer.MIN_VALUE) {
            return null;
        }
        return BigInteger.valueOf(n2);
    }

    protected DurationImpl(long l2) {
        boolean bl = false;
        long l3 = l2;
        if (l3 > 0) {
            this.signum = 1;
        } else if (l3 < 0) {
            this.signum = -1;
            if (l3 == Long.MIN_VALUE) {
                ++l3;
                bl = true;
            }
            l3 *= -1;
        } else {
            this.signum = 0;
        }
        this.years = null;
        this.months = null;
        this.seconds = BigDecimal.valueOf(l3 % 60000 + (bl ? 1 : 0), 3);
        this.minutes = (l3 /= 60000) == 0 ? null : BigInteger.valueOf(l3 % 60);
        this.hours = (l3 /= 60) == 0 ? null : BigInteger.valueOf(l3 % 24);
        this.days = (l3 /= 24) == 0 ? null : BigInteger.valueOf(l3);
    }

    protected DurationImpl(String string) throws IllegalArgumentException {
        boolean bl;
        if (string == null) {
            throw new NullPointerException();
        }
        String string2 = string;
        int[] arrn = new int[1];
        int n2 = string2.length();
        boolean bl2 = false;
        arrn[0] = 0;
        if (n2 != arrn[0] && string2.charAt(arrn[0]) == '-') {
            int[] arrn2 = arrn;
            arrn2[0] = arrn2[0] + 1;
            bl = false;
        } else {
            bl = true;
        }
        if (n2 != arrn[0]) {
            int[] arrn3 = arrn;
            int n3 = arrn3[0];
            arrn3[0] = n3 + 1;
            if (string2.charAt(n3) != 'P') {
                throw new IllegalArgumentException(string2);
            }
        }
        int n4 = 0;
        String[] arrstring = new String[3];
        int[] arrn4 = new int[3];
        while (n2 != arrn[0] && DurationImpl.isDigit(string2.charAt(arrn[0])) && n4 < 3) {
            arrn4[n4] = arrn[0];
            arrstring[n4++] = DurationImpl.parsePiece(string2, arrn);
        }
        if (n2 != arrn[0]) {
            int[] arrn5 = arrn;
            int n5 = arrn5[0];
            arrn5[0] = n5 + 1;
            if (string2.charAt(n5) == 'T') {
                bl2 = true;
            } else {
                throw new IllegalArgumentException(string2);
            }
        }
        int n6 = 0;
        String[] arrstring2 = new String[3];
        int[] arrn6 = new int[3];
        while (n2 != arrn[0] && DurationImpl.isDigitOrPeriod(string2.charAt(arrn[0])) && n6 < 3) {
            arrn6[n6] = arrn[0];
            arrstring2[n6++] = DurationImpl.parsePiece(string2, arrn);
        }
        if (bl2 && n6 == 0) {
            throw new IllegalArgumentException(string2);
        }
        if (n2 != arrn[0]) {
            throw new IllegalArgumentException(string2);
        }
        if (n4 == 0 && n6 == 0) {
            throw new IllegalArgumentException(string2);
        }
        DurationImpl.organizeParts(string2, arrstring, arrn4, n4, "YMD");
        DurationImpl.organizeParts(string2, arrstring2, arrn6, n6, "HMS");
        this.years = DurationImpl.parseBigInteger(string2, arrstring[0], arrn4[0]);
        this.months = DurationImpl.parseBigInteger(string2, arrstring[1], arrn4[1]);
        this.days = DurationImpl.parseBigInteger(string2, arrstring[2], arrn4[2]);
        this.hours = DurationImpl.parseBigInteger(string2, arrstring2[0], arrn6[0]);
        this.minutes = DurationImpl.parseBigInteger(string2, arrstring2[1], arrn6[1]);
        this.seconds = DurationImpl.parseBigDecimal(string2, arrstring2[2], arrn6[2]);
        this.signum = this.calcSignum(bl);
    }

    private static boolean isDigit(char c2) {
        return '0' <= c2 && c2 <= '9';
    }

    private static boolean isDigitOrPeriod(char c2) {
        return DurationImpl.isDigit(c2) || c2 == '.';
    }

    private static String parsePiece(String string, int[] arrn) throws IllegalArgumentException {
        int n2 = arrn[0];
        while (arrn[0] < string.length() && DurationImpl.isDigitOrPeriod(string.charAt(arrn[0]))) {
            int[] arrn2 = arrn;
            arrn2[0] = arrn2[0] + 1;
        }
        if (arrn[0] == string.length()) {
            throw new IllegalArgumentException(string);
        }
        int[] arrn3 = arrn;
        arrn3[0] = arrn3[0] + 1;
        return string.substring(n2, arrn[0]);
    }

    private static void organizeParts(String string, String[] arrstring, int[] arrn, int n2, String string2) throws IllegalArgumentException {
        int n3 = string2.length();
        int n4 = n2 - 1;
        while (n4 >= 0) {
            if (arrstring[n4] == null) {
                throw new IllegalArgumentException(string);
            }
            int n5 = string2.lastIndexOf(arrstring[n4].charAt(arrstring[n4].length() - 1), n3 - 1);
            if (n5 == -1) {
                throw new IllegalArgumentException(string);
            }
            int n6 = n5 + 1;
            while (n6 < n3) {
                arrstring[n6] = null;
                ++n6;
            }
            n3 = n5;
            arrstring[n3] = arrstring[n4];
            arrn[n3] = arrn[n4];
            --n4;
        }
        while (--n3 >= 0) {
            arrstring[n3] = null;
            --n3;
        }
    }

    private static BigInteger parseBigInteger(String string, String string2, int n2) throws IllegalArgumentException {
        if (string2 == null) {
            return null;
        }
        string2 = string2.substring(0, string2.length() - 1);
        return new BigInteger(string2);
    }

    private static BigDecimal parseBigDecimal(String string, String string2, int n2) throws IllegalArgumentException {
        if (string2 == null) {
            return null;
        }
        string2 = string2.substring(0, string2.length() - 1);
        return new BigDecimal(string2);
    }

    public int compare(Duration duration) {
        BigInteger bigInteger = BigInteger.valueOf(Integer.MAX_VALUE);
        if (this.years != null && this.years.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.YEARS.toString(), this.years.toString()}));
        }
        if (this.months != null && this.months.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MONTHS.toString(), this.months.toString()}));
        }
        if (this.days != null && this.days.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.DAYS.toString(), this.days.toString()}));
        }
        if (this.hours != null && this.hours.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.HOURS.toString(), this.hours.toString()}));
        }
        if (this.minutes != null && this.minutes.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MINUTES.toString(), this.minutes.toString()}));
        }
        if (this.seconds != null && this.seconds.toBigInteger().compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.SECONDS.toString(), this.toString(this.seconds)}));
        }
        BigInteger bigInteger2 = (BigInteger)duration.getField(DatatypeConstants.YEARS);
        if (bigInteger2 != null && bigInteger2.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.YEARS.toString(), bigInteger2.toString()}));
        }
        BigInteger bigInteger3 = (BigInteger)duration.getField(DatatypeConstants.MONTHS);
        if (bigInteger3 != null && bigInteger3.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MONTHS.toString(), bigInteger3.toString()}));
        }
        BigInteger bigInteger4 = (BigInteger)duration.getField(DatatypeConstants.DAYS);
        if (bigInteger4 != null && bigInteger4.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.DAYS.toString(), bigInteger4.toString()}));
        }
        BigInteger bigInteger5 = (BigInteger)duration.getField(DatatypeConstants.HOURS);
        if (bigInteger5 != null && bigInteger5.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.HOURS.toString(), bigInteger5.toString()}));
        }
        BigInteger bigInteger6 = (BigInteger)duration.getField(DatatypeConstants.MINUTES);
        if (bigInteger6 != null && bigInteger6.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MINUTES.toString(), bigInteger6.toString()}));
        }
        BigDecimal bigDecimal = (BigDecimal)duration.getField(DatatypeConstants.SECONDS);
        BigInteger bigInteger7 = null;
        if (bigDecimal != null) {
            bigInteger7 = bigDecimal.toBigInteger();
        }
        if (bigInteger7 != null && bigInteger7.compareTo(bigInteger) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.SECONDS.toString(), bigInteger7.toString()}));
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar(1970, 1, 1, 0, 0, 0);
        gregorianCalendar.add(1, this.getYears() * this.getSign());
        gregorianCalendar.add(2, this.getMonths() * this.getSign());
        gregorianCalendar.add(6, this.getDays() * this.getSign());
        gregorianCalendar.add(11, this.getHours() * this.getSign());
        gregorianCalendar.add(12, this.getMinutes() * this.getSign());
        gregorianCalendar.add(13, this.getSeconds() * this.getSign());
        GregorianCalendar gregorianCalendar2 = new GregorianCalendar(1970, 1, 1, 0, 0, 0);
        gregorianCalendar2.add(1, duration.getYears() * duration.getSign());
        gregorianCalendar2.add(2, duration.getMonths() * duration.getSign());
        gregorianCalendar2.add(6, duration.getDays() * duration.getSign());
        gregorianCalendar2.add(11, duration.getHours() * duration.getSign());
        gregorianCalendar2.add(12, duration.getMinutes() * duration.getSign());
        gregorianCalendar2.add(13, duration.getSeconds() * duration.getSign());
        if (gregorianCalendar.equals(gregorianCalendar2)) {
            return 0;
        }
        return this.compareDates(this, duration);
    }

    private int compareDates(Duration duration, Duration duration2) {
        int n2 = 2;
        int n3 = 2;
        XMLGregorianCalendar xMLGregorianCalendar = (XMLGregorianCalendar)TEST_POINTS[0].clone();
        XMLGregorianCalendar xMLGregorianCalendar2 = (XMLGregorianCalendar)TEST_POINTS[0].clone();
        xMLGregorianCalendar.add(duration);
        xMLGregorianCalendar2.add(duration2);
        n2 = xMLGregorianCalendar.compare(xMLGregorianCalendar2);
        if (n2 == 2) {
            return 2;
        }
        xMLGregorianCalendar = (XMLGregorianCalendar)TEST_POINTS[1].clone();
        xMLGregorianCalendar2 = (XMLGregorianCalendar)TEST_POINTS[1].clone();
        xMLGregorianCalendar.add(duration);
        xMLGregorianCalendar2.add(duration2);
        n3 = xMLGregorianCalendar.compare(xMLGregorianCalendar2);
        n2 = this.compareResults(n2, n3);
        if (n2 == 2) {
            return 2;
        }
        xMLGregorianCalendar = (XMLGregorianCalendar)TEST_POINTS[2].clone();
        xMLGregorianCalendar2 = (XMLGregorianCalendar)TEST_POINTS[2].clone();
        xMLGregorianCalendar.add(duration);
        xMLGregorianCalendar2.add(duration2);
        n3 = xMLGregorianCalendar.compare(xMLGregorianCalendar2);
        n2 = this.compareResults(n2, n3);
        if (n2 == 2) {
            return 2;
        }
        xMLGregorianCalendar = (XMLGregorianCalendar)TEST_POINTS[3].clone();
        xMLGregorianCalendar2 = (XMLGregorianCalendar)TEST_POINTS[3].clone();
        xMLGregorianCalendar.add(duration);
        xMLGregorianCalendar2.add(duration2);
        n3 = xMLGregorianCalendar.compare(xMLGregorianCalendar2);
        n2 = this.compareResults(n2, n3);
        return n2;
    }

    private int compareResults(int n2, int n3) {
        if (n3 == 2) {
            return 2;
        }
        if (n2 != n3) {
            return 2;
        }
        return n2;
    }

    public int hashCode() {
        GregorianCalendar gregorianCalendar = TEST_POINTS[0].toGregorianCalendar();
        this.addTo(gregorianCalendar);
        return (int)DurationImpl.getCalendarTimeInMillis(gregorianCalendar);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.signum < 0) {
            stringBuffer.append('-');
        }
        stringBuffer.append('P');
        if (this.years != null) {
            stringBuffer.append(this.years).append('Y');
        }
        if (this.months != null) {
            stringBuffer.append(this.months).append('M');
        }
        if (this.days != null) {
            stringBuffer.append(this.days).append('D');
        }
        if (this.hours != null || this.minutes != null || this.seconds != null) {
            stringBuffer.append('T');
            if (this.hours != null) {
                stringBuffer.append(this.hours).append('H');
            }
            if (this.minutes != null) {
                stringBuffer.append(this.minutes).append('M');
            }
            if (this.seconds != null) {
                stringBuffer.append(this.toString(this.seconds)).append('S');
            }
        }
        return stringBuffer.toString();
    }

    private String toString(BigDecimal bigDecimal) {
        StringBuffer stringBuffer;
        String string = bigDecimal.unscaledValue().toString();
        int n2 = bigDecimal.scale();
        if (n2 == 0) {
            return string;
        }
        int n3 = string.length() - n2;
        if (n3 == 0) {
            return "0." + string;
        }
        if (n3 > 0) {
            stringBuffer = new StringBuffer(string);
            stringBuffer.insert(n3, '.');
        } else {
            stringBuffer = new StringBuffer(3 - n3 + string.length());
            stringBuffer.append("0.");
            int n4 = 0;
            while (n4 < - n3) {
                stringBuffer.append('0');
                ++n4;
            }
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }

    public boolean isSet(DatatypeConstants.Field field) {
        if (field == null) {
            String string = "javax.xml.datatype.Duration#isSet(DatatypeConstants.Field field)";
            throw new NullPointerException(DatatypeMessageFormatter.formatMessage(null, "FieldCannotBeNull", new Object[]{string}));
        }
        if (field == DatatypeConstants.YEARS) {
            return this.years != null;
        }
        if (field == DatatypeConstants.MONTHS) {
            return this.months != null;
        }
        if (field == DatatypeConstants.DAYS) {
            return this.days != null;
        }
        if (field == DatatypeConstants.HOURS) {
            return this.hours != null;
        }
        if (field == DatatypeConstants.MINUTES) {
            return this.minutes != null;
        }
        if (field == DatatypeConstants.SECONDS) {
            return this.seconds != null;
        }
        String string = "javax.xml.datatype.Duration#isSet(DatatypeConstants.Field field)";
        throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "UnknownField", new Object[]{string, field.toString()}));
    }

    public Number getField(DatatypeConstants.Field field) {
        if (field == null) {
            String string = "javax.xml.datatype.Duration#isSet(DatatypeConstants.Field field) ";
            throw new NullPointerException(DatatypeMessageFormatter.formatMessage(null, "FieldCannotBeNull", new Object[]{string}));
        }
        if (field == DatatypeConstants.YEARS) {
            return this.years;
        }
        if (field == DatatypeConstants.MONTHS) {
            return this.months;
        }
        if (field == DatatypeConstants.DAYS) {
            return this.days;
        }
        if (field == DatatypeConstants.HOURS) {
            return this.hours;
        }
        if (field == DatatypeConstants.MINUTES) {
            return this.minutes;
        }
        if (field == DatatypeConstants.SECONDS) {
            return this.seconds;
        }
        String string = "javax.xml.datatype.Duration#(getSet(DatatypeConstants.Field field)";
        throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "UnknownField", new Object[]{string, field.toString()}));
    }

    public int getYears() {
        return this.getInt(DatatypeConstants.YEARS);
    }

    public int getMonths() {
        return this.getInt(DatatypeConstants.MONTHS);
    }

    public int getDays() {
        return this.getInt(DatatypeConstants.DAYS);
    }

    public int getHours() {
        return this.getInt(DatatypeConstants.HOURS);
    }

    public int getMinutes() {
        return this.getInt(DatatypeConstants.MINUTES);
    }

    public int getSeconds() {
        return this.getInt(DatatypeConstants.SECONDS);
    }

    private int getInt(DatatypeConstants.Field field) {
        Number number = this.getField(field);
        if (number == null) {
            return 0;
        }
        return number.intValue();
    }

    public long getTimeInMillis(Calendar calendar) {
        Calendar calendar2 = (Calendar)calendar.clone();
        this.addTo(calendar2);
        return DurationImpl.getCalendarTimeInMillis(calendar2) - DurationImpl.getCalendarTimeInMillis(calendar);
    }

    public long getTimeInMillis(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        this.addTo(gregorianCalendar);
        return DurationImpl.getCalendarTimeInMillis(gregorianCalendar) - date.getTime();
    }

    public Duration normalizeWith(Calendar calendar) {
        Calendar calendar2 = (Calendar)calendar.clone();
        calendar2.add(1, this.getYears() * this.signum);
        calendar2.add(2, this.getMonths() * this.signum);
        calendar2.add(5, this.getDays() * this.signum);
        long l2 = DurationImpl.getCalendarTimeInMillis(calendar2) - DurationImpl.getCalendarTimeInMillis(calendar);
        int n2 = (int)(l2 / 86400000);
        return new DurationImpl(n2 >= 0, null, null, DurationImpl.wrap(Math.abs(n2)), (BigInteger)this.getField(DatatypeConstants.HOURS), (BigInteger)this.getField(DatatypeConstants.MINUTES), (BigDecimal)this.getField(DatatypeConstants.SECONDS));
    }

    public Duration multiply(int n2) {
        return this.multiply(BigDecimal.valueOf(n2));
    }

    public Duration multiply(BigDecimal bigDecimal) {
        BigDecimal bigDecimal2 = ZERO;
        int n2 = bigDecimal.signum();
        bigDecimal = bigDecimal.abs();
        BigDecimal[] arrbigDecimal = new BigDecimal[6];
        int n3 = 0;
        while (n3 < 5) {
            BigDecimal bigDecimal3 = this.getFieldAsBigDecimal(FIELDS[n3]);
            bigDecimal3 = bigDecimal3.multiply(bigDecimal).add(bigDecimal2);
            arrbigDecimal[n3] = bigDecimal3.setScale(0, 1);
            bigDecimal3 = bigDecimal3.subtract(arrbigDecimal[n3]);
            if (n3 == 1) {
                if (bigDecimal3.signum() != 0) {
                    throw new IllegalStateException();
                }
                bigDecimal2 = ZERO;
            } else {
                bigDecimal2 = bigDecimal3.multiply(FACTORS[n3]);
            }
            ++n3;
        }
        arrbigDecimal[5] = this.seconds != null ? this.seconds.multiply(bigDecimal).add(bigDecimal2) : bigDecimal2;
        return new DurationImpl(this.signum * n2 >= 0, DurationImpl.toBigInteger(arrbigDecimal[0], null == this.years), DurationImpl.toBigInteger(arrbigDecimal[1], null == this.months), DurationImpl.toBigInteger(arrbigDecimal[2], null == this.days), DurationImpl.toBigInteger(arrbigDecimal[3], null == this.hours), DurationImpl.toBigInteger(arrbigDecimal[4], null == this.minutes), arrbigDecimal[5].signum() == 0 && this.seconds == null ? null : arrbigDecimal[5]);
    }

    private BigDecimal getFieldAsBigDecimal(DatatypeConstants.Field field) {
        if (field == DatatypeConstants.SECONDS) {
            if (this.seconds != null) {
                return this.seconds;
            }
            return ZERO;
        }
        BigInteger bigInteger = (BigInteger)this.getField(field);
        if (bigInteger == null) {
            return ZERO;
        }
        return new BigDecimal(bigInteger);
    }

    private static BigInteger toBigInteger(BigDecimal bigDecimal, boolean bl) {
        if (bl && bigDecimal.signum() == 0) {
            return null;
        }
        return bigDecimal.unscaledValue();
    }

    public Duration add(Duration duration) {
        DurationImpl durationImpl = this;
        BigDecimal[] arrbigDecimal = new BigDecimal[]{DurationImpl.sanitize((BigInteger)durationImpl.getField(DatatypeConstants.YEARS), durationImpl.getSign()).add(DurationImpl.sanitize((BigInteger)duration.getField(DatatypeConstants.YEARS), duration.getSign())), DurationImpl.sanitize((BigInteger)durationImpl.getField(DatatypeConstants.MONTHS), durationImpl.getSign()).add(DurationImpl.sanitize((BigInteger)duration.getField(DatatypeConstants.MONTHS), duration.getSign())), DurationImpl.sanitize((BigInteger)durationImpl.getField(DatatypeConstants.DAYS), durationImpl.getSign()).add(DurationImpl.sanitize((BigInteger)duration.getField(DatatypeConstants.DAYS), duration.getSign())), DurationImpl.sanitize((BigInteger)durationImpl.getField(DatatypeConstants.HOURS), durationImpl.getSign()).add(DurationImpl.sanitize((BigInteger)duration.getField(DatatypeConstants.HOURS), duration.getSign())), DurationImpl.sanitize((BigInteger)durationImpl.getField(DatatypeConstants.MINUTES), durationImpl.getSign()).add(DurationImpl.sanitize((BigInteger)duration.getField(DatatypeConstants.MINUTES), duration.getSign())), DurationImpl.sanitize((BigDecimal)durationImpl.getField(DatatypeConstants.SECONDS), durationImpl.getSign()).add(DurationImpl.sanitize((BigDecimal)duration.getField(DatatypeConstants.SECONDS), duration.getSign()))};
        DurationImpl.alignSigns(arrbigDecimal, 0, 2);
        DurationImpl.alignSigns(arrbigDecimal, 2, 6);
        int n2 = 0;
        int n3 = 0;
        while (n3 < 6) {
            if (n2 * arrbigDecimal[n3].signum() < 0) {
                throw new IllegalStateException();
            }
            if (n2 == 0) {
                n2 = arrbigDecimal[n3].signum();
            }
            ++n3;
        }
        return new DurationImpl(n2 >= 0, DurationImpl.toBigInteger(DurationImpl.sanitize(arrbigDecimal[0], n2), durationImpl.getField(DatatypeConstants.YEARS) == null && duration.getField(DatatypeConstants.YEARS) == null), DurationImpl.toBigInteger(DurationImpl.sanitize(arrbigDecimal[1], n2), durationImpl.getField(DatatypeConstants.MONTHS) == null && duration.getField(DatatypeConstants.MONTHS) == null), DurationImpl.toBigInteger(DurationImpl.sanitize(arrbigDecimal[2], n2), durationImpl.getField(DatatypeConstants.DAYS) == null && duration.getField(DatatypeConstants.DAYS) == null), DurationImpl.toBigInteger(DurationImpl.sanitize(arrbigDecimal[3], n2), durationImpl.getField(DatatypeConstants.HOURS) == null && duration.getField(DatatypeConstants.HOURS) == null), DurationImpl.toBigInteger(DurationImpl.sanitize(arrbigDecimal[4], n2), durationImpl.getField(DatatypeConstants.MINUTES) == null && duration.getField(DatatypeConstants.MINUTES) == null), arrbigDecimal[5].signum() == 0 && durationImpl.getField(DatatypeConstants.SECONDS) == null && duration.getField(DatatypeConstants.SECONDS) == null ? null : DurationImpl.sanitize(arrbigDecimal[5], n2));
    }

    private static void alignSigns(BigDecimal[] arrbigDecimal, int n2, int n3) {
        boolean bl;
        do {
            bl = false;
            int n4 = 0;
            int n5 = n2;
            while (n5 < n3) {
                if (n4 * arrbigDecimal[n5].signum() < 0) {
                    bl = true;
                    BigDecimal bigDecimal = arrbigDecimal[n5].abs().divide(FACTORS[n5 - 1], 0);
                    if (arrbigDecimal[n5].signum() > 0) {
                        bigDecimal = bigDecimal.negate();
                    }
                    arrbigDecimal[n5 - 1] = arrbigDecimal[n5 - 1].subtract(bigDecimal);
                    arrbigDecimal[n5] = arrbigDecimal[n5].add(bigDecimal.multiply(FACTORS[n5 - 1]));
                }
                if (arrbigDecimal[n5].signum() != 0) {
                    n4 = arrbigDecimal[n5].signum();
                }
                ++n5;
            }
        } while (bl);
    }

    private static BigDecimal sanitize(BigInteger bigInteger, int n2) {
        if (n2 == 0 || bigInteger == null) {
            return ZERO;
        }
        if (n2 > 0) {
            return new BigDecimal(bigInteger);
        }
        return new BigDecimal(bigInteger.negate());
    }

    static BigDecimal sanitize(BigDecimal bigDecimal, int n2) {
        if (n2 == 0 || bigDecimal == null) {
            return ZERO;
        }
        if (n2 > 0) {
            return bigDecimal;
        }
        return bigDecimal.negate();
    }

    public Duration subtract(Duration duration) {
        return this.add(duration.negate());
    }

    public Duration negate() {
        return new DurationImpl(this.signum <= 0, this.years, this.months, this.days, this.hours, this.minutes, this.seconds);
    }

    public int signum() {
        return this.signum;
    }

    public void addTo(Calendar calendar) {
        calendar.add(1, this.getYears() * this.signum);
        calendar.add(2, this.getMonths() * this.signum);
        calendar.add(5, this.getDays() * this.signum);
        calendar.add(10, this.getHours() * this.signum);
        calendar.add(12, this.getMinutes() * this.signum);
        calendar.add(13, this.getSeconds() * this.signum);
        if (this.seconds != null) {
            BigDecimal bigDecimal = this.seconds.subtract(this.seconds.setScale(0, 1));
            int n2 = bigDecimal.movePointRight(3).intValue();
            calendar.add(14, n2 * this.signum);
        }
    }

    public void addTo(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        this.addTo(gregorianCalendar);
        date.setTime(DurationImpl.getCalendarTimeInMillis(gregorianCalendar));
    }

    private static long getCalendarTimeInMillis(Calendar calendar) {
        return calendar.getTime().getTime();
    }

    private Object writeReplace() throws IOException {
        return new SerializedDuration(this.toString());
    }
}

