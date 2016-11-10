/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.datatype;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.apache.xerces.jaxp.datatype.DurationImpl;
import org.apache.xerces.jaxp.datatype.SerializedXMLGregorianCalendar;
import org.apache.xerces.util.DatatypeMessageFormatter;

class XMLGregorianCalendarImpl
extends XMLGregorianCalendar
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 3905403108073447394L;
    private BigInteger orig_eon;
    private int orig_year = Integer.MIN_VALUE;
    private int orig_month = Integer.MIN_VALUE;
    private int orig_day = Integer.MIN_VALUE;
    private int orig_hour = Integer.MIN_VALUE;
    private int orig_minute = Integer.MIN_VALUE;
    private int orig_second = Integer.MIN_VALUE;
    private BigDecimal orig_fracSeconds;
    private int orig_timezone = Integer.MIN_VALUE;
    private BigInteger eon = null;
    private int year = Integer.MIN_VALUE;
    private int month = Integer.MIN_VALUE;
    private int day = Integer.MIN_VALUE;
    private int timezone = Integer.MIN_VALUE;
    private int hour = Integer.MIN_VALUE;
    private int minute = Integer.MIN_VALUE;
    private int second = Integer.MIN_VALUE;
    private BigDecimal fractionalSecond = null;
    private static final BigInteger BILLION_B = BigInteger.valueOf(1000000000);
    private static final int BILLION_I = 1000000000;
    private static final Date PURE_GREGORIAN_CHANGE = new Date(Long.MIN_VALUE);
    private static final int YEAR = 0;
    private static final int MONTH = 1;
    private static final int DAY = 2;
    private static final int HOUR = 3;
    private static final int MINUTE = 4;
    private static final int SECOND = 5;
    private static final int MILLISECOND = 6;
    private static final int TIMEZONE = 7;
    private static final int[] MIN_FIELD_VALUE = new int[]{Integer.MIN_VALUE, 1, 1, 0, 0, 0, 0, -840};
    private static final int[] MAX_FIELD_VALUE = new int[]{Integer.MAX_VALUE, 12, 31, 24, 59, 60, 999, 840};
    private static final String[] FIELD_NAME = new String[]{"Year", "Month", "Day", "Hour", "Minute", "Second", "Millisecond", "Timezone"};
    public static final XMLGregorianCalendar LEAP_YEAR_DEFAULT = XMLGregorianCalendarImpl.createDateTime(400, 1, 1, 0, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private static final BigInteger FOUR = BigInteger.valueOf(4);
    private static final BigInteger HUNDRED = BigInteger.valueOf(100);
    private static final BigInteger FOUR_HUNDRED = BigInteger.valueOf(400);
    private static final BigInteger SIXTY = BigInteger.valueOf(60);
    private static final BigInteger TWENTY_FOUR = BigInteger.valueOf(24);
    private static final BigInteger TWELVE = BigInteger.valueOf(12);
    private static final BigDecimal DECIMAL_ZERO = BigDecimal.valueOf(0);
    private static final BigDecimal DECIMAL_ONE = BigDecimal.valueOf(1);
    private static final BigDecimal DECIMAL_SIXTY = BigDecimal.valueOf(60);

    protected XMLGregorianCalendarImpl(String string) throws IllegalArgumentException {
        String string2 = null;
        String string3 = string;
        int n2 = string3.length();
        if (string3.indexOf(84) != -1) {
            string2 = "%Y-%M-%DT%h:%m:%s%z";
        } else if (n2 >= 3 && string3.charAt(2) == ':') {
            string2 = "%h:%m:%s%z";
        } else if (string3.startsWith("--")) {
            if (n2 >= 3 && string3.charAt(2) == '-') {
                string2 = "---%D%z";
            } else if (n2 == 4 || n2 >= 6 && (string3.charAt(4) == '+' || string3.charAt(4) == '-' && (string3.charAt(5) == '-' || n2 == 10))) {
                string2 = "--%M--%z";
                Parser parser = new Parser(this, string2, string3, null);
                try {
                    parser.parse();
                    if (!this.isValid()) {
                        throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCRepresentation", new Object[]{string}));
                    }
                    this.save();
                    return;
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    string2 = "--%M%z";
                }
            } else {
                string2 = "--%M-%D%z";
            }
        } else {
            int n3 = 0;
            int n4 = string3.indexOf(58);
            if (n4 != -1) {
                n2 -= 6;
            }
            int n5 = 1;
            while (n5 < n2) {
                if (string3.charAt(n5) == '-') {
                    ++n3;
                }
                ++n5;
            }
            string2 = n3 == 0 ? "%Y%z" : (n3 == 1 ? "%Y-%M%z" : "%Y-%M-%D%z");
        }
        Parser parser = new Parser(this, string2, string3, null);
        parser.parse();
        if (!this.isValid()) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCRepresentation", new Object[]{string}));
        }
        this.save();
    }

    private void save() {
        this.orig_eon = this.eon;
        this.orig_year = this.year;
        this.orig_month = this.month;
        this.orig_day = this.day;
        this.orig_hour = this.hour;
        this.orig_minute = this.minute;
        this.orig_second = this.second;
        this.orig_fracSeconds = this.fractionalSecond;
        this.orig_timezone = this.timezone;
    }

    public XMLGregorianCalendarImpl() {
    }

    protected XMLGregorianCalendarImpl(BigInteger bigInteger, int n2, int n3, int n4, int n5, int n6, BigDecimal bigDecimal, int n7) {
        this.setYear(bigInteger);
        this.setMonth(n2);
        this.setDay(n3);
        this.setTime(n4, n5, n6, bigDecimal);
        this.setTimezone(n7);
        if (!this.isValid()) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCValue-fractional", new Object[]{bigInteger, new Integer(n2), new Integer(n3), new Integer(n4), new Integer(n5), new Integer(n6), bigDecimal, new Integer(n7)}));
        }
        this.save();
    }

    private XMLGregorianCalendarImpl(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        this.setYear(n2);
        this.setMonth(n3);
        this.setDay(n4);
        this.setTime(n5, n6, n7);
        this.setTimezone(n9);
        BigDecimal bigDecimal = null;
        if (n8 != Integer.MIN_VALUE) {
            bigDecimal = BigDecimal.valueOf(n8, 3);
        }
        this.setFractionalSecond(bigDecimal);
        if (!this.isValid()) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCValue-milli", new Object[]{new Integer(n2), new Integer(n3), new Integer(n4), new Integer(n5), new Integer(n6), new Integer(n7), new Integer(n8), new Integer(n9)}));
        }
        this.save();
    }

    public XMLGregorianCalendarImpl(GregorianCalendar gregorianCalendar) {
        int n2 = gregorianCalendar.get(1);
        if (gregorianCalendar.get(0) == 0) {
            n2 = - n2;
        }
        this.setYear(n2);
        this.setMonth(gregorianCalendar.get(2) + 1);
        this.setDay(gregorianCalendar.get(5));
        this.setTime(gregorianCalendar.get(11), gregorianCalendar.get(12), gregorianCalendar.get(13), gregorianCalendar.get(14));
        int n3 = (gregorianCalendar.get(15) + gregorianCalendar.get(16)) / 60000;
        this.setTimezone(n3);
        this.save();
    }

    public static XMLGregorianCalendar createDateTime(BigInteger bigInteger, int n2, int n3, int n4, int n5, int n6, BigDecimal bigDecimal, int n7) {
        return new XMLGregorianCalendarImpl(bigInteger, n2, n3, n4, n5, n6, bigDecimal, n7);
    }

    public static XMLGregorianCalendar createDateTime(int n2, int n3, int n4, int n5, int n6, int n7) {
        return new XMLGregorianCalendarImpl(n2, n3, n4, n5, n6, n7, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public static XMLGregorianCalendar createDateTime(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        return new XMLGregorianCalendarImpl(n2, n3, n4, n5, n6, n7, n8, n9);
    }

    public static XMLGregorianCalendar createDate(int n2, int n3, int n4, int n5) {
        return new XMLGregorianCalendarImpl(n2, n3, n4, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, n5);
    }

    public static XMLGregorianCalendar createTime(int n2, int n3, int n4, int n5) {
        return new XMLGregorianCalendarImpl(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, n2, n3, n4, Integer.MIN_VALUE, n5);
    }

    public static XMLGregorianCalendar createTime(int n2, int n3, int n4, BigDecimal bigDecimal, int n5) {
        return new XMLGregorianCalendarImpl(null, Integer.MIN_VALUE, Integer.MIN_VALUE, n2, n3, n4, bigDecimal, n5);
    }

    public static XMLGregorianCalendar createTime(int n2, int n3, int n4, int n5, int n6) {
        return new XMLGregorianCalendarImpl(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, n2, n3, n4, n5, n6);
    }

    public BigInteger getEon() {
        return this.eon;
    }

    public int getYear() {
        return this.year;
    }

    public BigInteger getEonAndYear() {
        if (this.year != Integer.MIN_VALUE && this.eon != null) {
            return this.eon.add(BigInteger.valueOf(this.year));
        }
        if (this.year != Integer.MIN_VALUE && this.eon == null) {
            return BigInteger.valueOf(this.year);
        }
        return null;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public int getTimezone() {
        return this.timezone;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getSecond() {
        return this.second;
    }

    private BigDecimal getSeconds() {
        if (this.second == Integer.MIN_VALUE) {
            return DECIMAL_ZERO;
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(this.second);
        if (this.fractionalSecond != null) {
            return bigDecimal.add(this.fractionalSecond);
        }
        return bigDecimal;
    }

    public int getMillisecond() {
        if (this.fractionalSecond == null) {
            return Integer.MIN_VALUE;
        }
        return this.fractionalSecond.movePointRight(3).intValue();
    }

    public BigDecimal getFractionalSecond() {
        return this.fractionalSecond;
    }

    public void setYear(BigInteger bigInteger) {
        if (bigInteger == null) {
            this.eon = null;
            this.year = Integer.MIN_VALUE;
        } else {
            BigInteger bigInteger2 = bigInteger.remainder(BILLION_B);
            this.year = bigInteger2.intValue();
            this.setEon(bigInteger.subtract(bigInteger2));
        }
    }

    public void setYear(int n2) {
        if (n2 == Integer.MIN_VALUE) {
            this.year = Integer.MIN_VALUE;
            this.eon = null;
        } else if (Math.abs(n2) < 1000000000) {
            this.year = n2;
            this.eon = null;
        } else {
            BigInteger bigInteger = BigInteger.valueOf(n2);
            BigInteger bigInteger2 = bigInteger.remainder(BILLION_B);
            this.year = bigInteger2.intValue();
            this.setEon(bigInteger.subtract(bigInteger2));
        }
    }

    private void setEon(BigInteger bigInteger) {
        this.eon = bigInteger != null && bigInteger.compareTo(BigInteger.ZERO) == 0 ? null : bigInteger;
    }

    public void setMonth(int n2) {
        this.checkFieldValueConstraint(1, n2);
        this.month = n2;
    }

    public void setDay(int n2) {
        this.checkFieldValueConstraint(2, n2);
        this.day = n2;
    }

    public void setTimezone(int n2) {
        this.checkFieldValueConstraint(7, n2);
        this.timezone = n2;
    }

    public void setTime(int n2, int n3, int n4) {
        this.setTime(n2, n3, n4, null);
    }

    private void checkFieldValueConstraint(int n2, int n3) throws IllegalArgumentException {
        if (n3 < MIN_FIELD_VALUE[n2] && n3 != Integer.MIN_VALUE || n3 > MAX_FIELD_VALUE[n2]) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidFieldValue", new Object[]{new Integer(n3), FIELD_NAME[n2]}));
        }
    }

    public void setHour(int n2) {
        this.checkFieldValueConstraint(3, n2);
        this.hour = n2;
    }

    public void setMinute(int n2) {
        this.checkFieldValueConstraint(4, n2);
        this.minute = n2;
    }

    public void setSecond(int n2) {
        this.checkFieldValueConstraint(5, n2);
        this.second = n2;
    }

    public void setTime(int n2, int n3, int n4, BigDecimal bigDecimal) {
        this.setHour(n2);
        this.setMinute(n3);
        this.setSecond(n4);
        this.setFractionalSecond(bigDecimal);
    }

    public void setTime(int n2, int n3, int n4, int n5) {
        this.setHour(n2);
        this.setMinute(n3);
        this.setSecond(n4);
        this.setMillisecond(n5);
    }

    public int compare(XMLGregorianCalendar xMLGregorianCalendar) {
        XMLGregorianCalendar xMLGregorianCalendar2;
        int n2 = 2;
        XMLGregorianCalendarImpl xMLGregorianCalendarImpl = this;
        XMLGregorianCalendar xMLGregorianCalendar3 = xMLGregorianCalendar;
        if (xMLGregorianCalendarImpl.getTimezone() == xMLGregorianCalendar3.getTimezone()) {
            return XMLGregorianCalendarImpl.internalCompare(xMLGregorianCalendarImpl, xMLGregorianCalendar3);
        }
        if (xMLGregorianCalendarImpl.getTimezone() != Integer.MIN_VALUE && xMLGregorianCalendar3.getTimezone() != Integer.MIN_VALUE) {
            xMLGregorianCalendarImpl = (XMLGregorianCalendarImpl)xMLGregorianCalendarImpl.normalize();
            xMLGregorianCalendar3 = (XMLGregorianCalendarImpl)xMLGregorianCalendar3.normalize();
            return XMLGregorianCalendarImpl.internalCompare(xMLGregorianCalendarImpl, xMLGregorianCalendar3);
        }
        if (xMLGregorianCalendarImpl.getTimezone() != Integer.MIN_VALUE) {
            XMLGregorianCalendar xMLGregorianCalendar4;
            if (xMLGregorianCalendarImpl.getTimezone() != 0) {
                xMLGregorianCalendarImpl = (XMLGregorianCalendarImpl)xMLGregorianCalendarImpl.normalize();
            }
            if ((n2 = XMLGregorianCalendarImpl.internalCompare(xMLGregorianCalendarImpl, xMLGregorianCalendar4 = this.normalizeToTimezone(xMLGregorianCalendar3, 840))) == -1) {
                return n2;
            }
            XMLGregorianCalendar xMLGregorianCalendar5 = this.normalizeToTimezone(xMLGregorianCalendar3, -840);
            n2 = XMLGregorianCalendarImpl.internalCompare(xMLGregorianCalendarImpl, xMLGregorianCalendar5);
            if (n2 == 1) {
                return n2;
            }
            return 2;
        }
        if (xMLGregorianCalendar3.getTimezone() != 0) {
            xMLGregorianCalendar3 = (XMLGregorianCalendarImpl)this.normalizeToTimezone(xMLGregorianCalendar3, xMLGregorianCalendar3.getTimezone());
        }
        if ((n2 = XMLGregorianCalendarImpl.internalCompare(xMLGregorianCalendar2 = this.normalizeToTimezone(xMLGregorianCalendarImpl, -840), xMLGregorianCalendar3)) == -1) {
            return n2;
        }
        XMLGregorianCalendar xMLGregorianCalendar6 = this.normalizeToTimezone(xMLGregorianCalendarImpl, 840);
        n2 = XMLGregorianCalendarImpl.internalCompare(xMLGregorianCalendar6, xMLGregorianCalendar3);
        if (n2 == 1) {
            return n2;
        }
        return 2;
    }

    public XMLGregorianCalendar normalize() {
        XMLGregorianCalendar xMLGregorianCalendar = this.normalizeToTimezone(this, this.timezone);
        if (this.getTimezone() == Integer.MIN_VALUE) {
            xMLGregorianCalendar.setTimezone(Integer.MIN_VALUE);
        }
        if (this.getMillisecond() == Integer.MIN_VALUE) {
            xMLGregorianCalendar.setMillisecond(Integer.MIN_VALUE);
        }
        return xMLGregorianCalendar;
    }

    private XMLGregorianCalendar normalizeToTimezone(XMLGregorianCalendar xMLGregorianCalendar, int n2) {
        int n3 = n2;
        XMLGregorianCalendar xMLGregorianCalendar2 = (XMLGregorianCalendar)xMLGregorianCalendar.clone();
        DurationImpl durationImpl = new DurationImpl((n3 = - n3) >= 0, 0, 0, 0, 0, n3 < 0 ? - n3 : n3, 0);
        xMLGregorianCalendar2.add(durationImpl);
        xMLGregorianCalendar2.setTimezone(0);
        return xMLGregorianCalendar2;
    }

    private static int internalCompare(XMLGregorianCalendar xMLGregorianCalendar, XMLGregorianCalendar xMLGregorianCalendar2) {
        int n2;
        if (xMLGregorianCalendar.getEon() == xMLGregorianCalendar2.getEon() ? (n2 = XMLGregorianCalendarImpl.compareField(xMLGregorianCalendar.getYear(), xMLGregorianCalendar2.getYear())) != 0 : (n2 = XMLGregorianCalendarImpl.compareField(xMLGregorianCalendar.getEonAndYear(), xMLGregorianCalendar2.getEonAndYear())) != 0) {
            return n2;
        }
        n2 = XMLGregorianCalendarImpl.compareField(xMLGregorianCalendar.getMonth(), xMLGregorianCalendar2.getMonth());
        if (n2 != 0) {
            return n2;
        }
        n2 = XMLGregorianCalendarImpl.compareField(xMLGregorianCalendar.getDay(), xMLGregorianCalendar2.getDay());
        if (n2 != 0) {
            return n2;
        }
        n2 = XMLGregorianCalendarImpl.compareField(xMLGregorianCalendar.getHour(), xMLGregorianCalendar2.getHour());
        if (n2 != 0) {
            return n2;
        }
        n2 = XMLGregorianCalendarImpl.compareField(xMLGregorianCalendar.getMinute(), xMLGregorianCalendar2.getMinute());
        if (n2 != 0) {
            return n2;
        }
        n2 = XMLGregorianCalendarImpl.compareField(xMLGregorianCalendar.getSecond(), xMLGregorianCalendar2.getSecond());
        if (n2 != 0) {
            return n2;
        }
        n2 = XMLGregorianCalendarImpl.compareField(xMLGregorianCalendar.getFractionalSecond(), xMLGregorianCalendar2.getFractionalSecond());
        return n2;
    }

    private static int compareField(int n2, int n3) {
        if (n2 == n3) {
            return 0;
        }
        if (n2 == Integer.MIN_VALUE || n3 == Integer.MIN_VALUE) {
            return 2;
        }
        return n2 < n3 ? -1 : 1;
    }

    private static int compareField(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger == null) {
            return bigInteger2 == null ? 0 : 2;
        }
        if (bigInteger2 == null) {
            return 2;
        }
        return bigInteger.compareTo(bigInteger2);
    }

    private static int compareField(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        if (bigDecimal == bigDecimal2) {
            return 0;
        }
        if (bigDecimal == null) {
            bigDecimal = DECIMAL_ZERO;
        }
        if (bigDecimal2 == null) {
            bigDecimal2 = DECIMAL_ZERO;
        }
        return bigDecimal.compareTo(bigDecimal2);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof XMLGregorianCalendar) {
            return this.compare((XMLGregorianCalendar)object) == 0;
        }
        return false;
    }

    public int hashCode() {
        int n2 = this.getTimezone();
        if (n2 == Integer.MIN_VALUE) {
            n2 = 0;
        }
        XMLGregorianCalendar xMLGregorianCalendar = this;
        if (n2 != 0) {
            xMLGregorianCalendar = this.normalizeToTimezone(this, this.getTimezone());
        }
        return xMLGregorianCalendar.getYear() + xMLGregorianCalendar.getMonth() + xMLGregorianCalendar.getDay() + xMLGregorianCalendar.getHour() + xMLGregorianCalendar.getMinute() + xMLGregorianCalendar.getSecond();
    }

    public static XMLGregorianCalendar parse(String string) {
        return new XMLGregorianCalendarImpl(string);
    }

    public String toXMLFormat() {
        QName qName = this.getXMLSchemaType();
        String string = null;
        if (qName == DatatypeConstants.DATETIME) {
            string = "%Y-%M-%DT%h:%m:%s%z";
        } else if (qName == DatatypeConstants.DATE) {
            string = "%Y-%M-%D%z";
        } else if (qName == DatatypeConstants.TIME) {
            string = "%h:%m:%s%z";
        } else if (qName == DatatypeConstants.GMONTH) {
            string = "--%M--%z";
        } else if (qName == DatatypeConstants.GDAY) {
            string = "---%D%z";
        } else if (qName == DatatypeConstants.GYEAR) {
            string = "%Y%z";
        } else if (qName == DatatypeConstants.GYEARMONTH) {
            string = "%Y-%M%z";
        } else if (qName == DatatypeConstants.GMONTHDAY) {
            string = "--%M-%D%z";
        }
        return this.format(string);
    }

    public QName getXMLSchemaType() {
        if (this.year != Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day != Integer.MIN_VALUE && this.hour != Integer.MIN_VALUE && this.minute != Integer.MIN_VALUE && this.second != Integer.MIN_VALUE) {
            return DatatypeConstants.DATETIME;
        }
        if (this.year != Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day != Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
            return DatatypeConstants.DATE;
        }
        if (this.year == Integer.MIN_VALUE && this.month == Integer.MIN_VALUE && this.day == Integer.MIN_VALUE && this.hour != Integer.MIN_VALUE && this.minute != Integer.MIN_VALUE && this.second != Integer.MIN_VALUE) {
            return DatatypeConstants.TIME;
        }
        if (this.year != Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day == Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
            return DatatypeConstants.GYEARMONTH;
        }
        if (this.year == Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day != Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
            return DatatypeConstants.GMONTHDAY;
        }
        if (this.year != Integer.MIN_VALUE && this.month == Integer.MIN_VALUE && this.day == Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
            return DatatypeConstants.GYEAR;
        }
        if (this.year == Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day == Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
            return DatatypeConstants.GMONTH;
        }
        if (this.year == Integer.MIN_VALUE && this.month == Integer.MIN_VALUE && this.day != Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
            return DatatypeConstants.GDAY;
        }
        throw new IllegalStateException(this.getClass().getName() + "#getXMLSchemaType() :" + DatatypeMessageFormatter.formatMessage(null, "InvalidXGCFields", null));
    }

    public boolean isValid() {
        if (this.month != Integer.MIN_VALUE && this.day != Integer.MIN_VALUE && (this.year != Integer.MIN_VALUE ? (this.eon == null ? this.day > XMLGregorianCalendarImpl.maximumDayInMonthFor(this.year, this.month) : this.day > XMLGregorianCalendarImpl.maximumDayInMonthFor(this.getEonAndYear(), this.month)) : this.day > XMLGregorianCalendarImpl.maximumDayInMonthFor(2000, this.month))) {
            return false;
        }
        if (this.hour == 24 && (this.minute != 0 || this.second != 0 || this.fractionalSecond != null && this.fractionalSecond.compareTo(DECIMAL_ZERO) != 0)) {
            return false;
        }
        if (this.eon == null && this.year == 0) {
            return false;
        }
        return true;
    }

    public void add(Duration duration) {
        int n2;
        BigDecimal bigDecimal;
        boolean[] arrbl = new boolean[]{false, false, false, false, false, false};
        int n3 = duration.getSign();
        int n4 = this.getMonth();
        if (n4 == Integer.MIN_VALUE) {
            n4 = MIN_FIELD_VALUE[1];
            arrbl[1] = true;
        }
        BigInteger bigInteger = XMLGregorianCalendarImpl.sanitize(duration.getField(DatatypeConstants.MONTHS), n3);
        BigInteger bigInteger2 = BigInteger.valueOf(n4).add(bigInteger);
        this.setMonth(bigInteger2.subtract(BigInteger.ONE).mod(TWELVE).intValue() + 1);
        BigInteger bigInteger3 = new BigDecimal(bigInteger2.subtract(BigInteger.ONE)).divide(new BigDecimal(TWELVE), 3).toBigInteger();
        BigInteger bigInteger4 = this.getEonAndYear();
        if (bigInteger4 == null) {
            arrbl[0] = true;
            bigInteger4 = BigInteger.ZERO;
        }
        BigInteger bigInteger5 = XMLGregorianCalendarImpl.sanitize(duration.getField(DatatypeConstants.YEARS), n3);
        BigInteger bigInteger6 = bigInteger4.add(bigInteger5).add(bigInteger3);
        this.setYear(bigInteger6);
        if (this.getSecond() == Integer.MIN_VALUE) {
            arrbl[5] = true;
            bigDecimal = DECIMAL_ZERO;
        } else {
            bigDecimal = this.getSeconds();
        }
        BigDecimal bigDecimal2 = DurationImpl.sanitize((BigDecimal)duration.getField(DatatypeConstants.SECONDS), n3);
        BigDecimal bigDecimal3 = bigDecimal.add(bigDecimal2);
        BigDecimal bigDecimal4 = new BigDecimal(new BigDecimal(bigDecimal3.toBigInteger()).divide(DECIMAL_SIXTY, 3).toBigInteger());
        BigDecimal bigDecimal5 = bigDecimal3.subtract(bigDecimal4.multiply(DECIMAL_SIXTY));
        bigInteger3 = bigDecimal4.toBigInteger();
        this.setSecond(bigDecimal5.intValue());
        BigDecimal bigDecimal6 = bigDecimal5.subtract(new BigDecimal(BigInteger.valueOf(this.getSecond())));
        if (bigDecimal6.compareTo(DECIMAL_ZERO) < 0) {
            this.setFractionalSecond(DECIMAL_ONE.add(bigDecimal6));
            if (this.getSecond() == 0) {
                this.setSecond(59);
                bigInteger3 = bigInteger3.subtract(BigInteger.ONE);
            } else {
                this.setSecond(this.getSecond() - 1);
            }
        } else {
            this.setFractionalSecond(bigDecimal6);
        }
        int n5 = this.getMinute();
        if (n5 == Integer.MIN_VALUE) {
            arrbl[4] = true;
            n5 = MIN_FIELD_VALUE[4];
        }
        BigInteger bigInteger7 = XMLGregorianCalendarImpl.sanitize(duration.getField(DatatypeConstants.MINUTES), n3);
        bigInteger2 = BigInteger.valueOf(n5).add(bigInteger7).add(bigInteger3);
        this.setMinute(bigInteger2.mod(SIXTY).intValue());
        bigInteger3 = new BigDecimal(bigInteger2).divide(DECIMAL_SIXTY, 3).toBigInteger();
        int n6 = this.getHour();
        if (n6 == Integer.MIN_VALUE) {
            arrbl[3] = true;
            n6 = MIN_FIELD_VALUE[3];
        }
        BigInteger bigInteger8 = XMLGregorianCalendarImpl.sanitize(duration.getField(DatatypeConstants.HOURS), n3);
        bigInteger2 = BigInteger.valueOf(n6).add(bigInteger8).add(bigInteger3);
        this.setHour(bigInteger2.mod(TWENTY_FOUR).intValue());
        bigInteger3 = new BigDecimal(bigInteger2).divide(new BigDecimal(TWENTY_FOUR), 3).toBigInteger();
        int n7 = this.getDay();
        if (n7 == Integer.MIN_VALUE) {
            arrbl[2] = true;
            n7 = MIN_FIELD_VALUE[2];
        }
        BigInteger bigInteger9 = XMLGregorianCalendarImpl.sanitize(duration.getField(DatatypeConstants.DAYS), n3);
        int n8 = XMLGregorianCalendarImpl.maximumDayInMonthFor(this.getEonAndYear(), this.getMonth());
        BigInteger bigInteger10 = n7 > n8 ? BigInteger.valueOf(n8) : (n7 < 1 ? BigInteger.ONE : BigInteger.valueOf(n7));
        BigInteger bigInteger11 = bigInteger10.add(bigInteger9).add(bigInteger3);
        do {
            int n9;
            int n10;
            if (bigInteger11.compareTo(BigInteger.ONE) < 0) {
                BigInteger bigInteger12 = null;
                bigInteger12 = this.month >= 2 ? BigInteger.valueOf(XMLGregorianCalendarImpl.maximumDayInMonthFor(this.getEonAndYear(), this.getMonth() - 1)) : BigInteger.valueOf(XMLGregorianCalendarImpl.maximumDayInMonthFor(this.getEonAndYear().subtract(BigInteger.valueOf(1)), 12));
                bigInteger11 = bigInteger11.add(bigInteger12);
                n9 = -1;
            } else {
                if (bigInteger11.compareTo(BigInteger.valueOf(XMLGregorianCalendarImpl.maximumDayInMonthFor(this.getEonAndYear(), this.getMonth()))) <= 0) break;
                bigInteger11 = bigInteger11.add(BigInteger.valueOf(- XMLGregorianCalendarImpl.maximumDayInMonthFor(this.getEonAndYear(), this.getMonth())));
                n9 = 1;
            }
            int n11 = this.getMonth() + n9;
            n2 = (n11 - 1) % 12;
            if (n2 < 0) {
                n2 = 12 + n2 + 1;
                n10 = BigDecimal.valueOf(n11 - 1).divide(new BigDecimal(TWELVE), 0).intValue();
            } else {
                n10 = (n11 - 1) / 12;
                ++n2;
            }
            this.setMonth(n2);
            if (n10 == 0) continue;
            this.setYear(this.getEonAndYear().add(BigInteger.valueOf(n10)));
        } while (true);
        this.setDay(bigInteger11.intValue());
        n2 = 0;
        while (n2 <= 5) {
            if (arrbl[n2]) {
                switch (n2) {
                    case 0: {
                        this.setYear(Integer.MIN_VALUE);
                        break;
                    }
                    case 1: {
                        this.setMonth(Integer.MIN_VALUE);
                        break;
                    }
                    case 2: {
                        this.setDay(Integer.MIN_VALUE);
                        break;
                    }
                    case 3: {
                        this.setHour(Integer.MIN_VALUE);
                        break;
                    }
                    case 4: {
                        this.setMinute(Integer.MIN_VALUE);
                        break;
                    }
                    case 5: {
                        this.setSecond(Integer.MIN_VALUE);
                        this.setFractionalSecond(null);
                    }
                }
            }
            ++n2;
        }
    }

    private static int maximumDayInMonthFor(BigInteger bigInteger, int n2) {
        if (n2 != 2) {
            return DaysInMonth.access$100()[n2];
        }
        if (bigInteger.mod(FOUR_HUNDRED).equals(BigInteger.ZERO) || !bigInteger.mod(HUNDRED).equals(BigInteger.ZERO) && bigInteger.mod(FOUR).equals(BigInteger.ZERO)) {
            return 29;
        }
        return DaysInMonth.access$100()[n2];
    }

    private static int maximumDayInMonthFor(int n2, int n3) {
        if (n3 != 2) {
            return DaysInMonth.access$100()[n3];
        }
        if (n2 % 400 == 0 || n2 % 100 != 0 && n2 % 4 == 0) {
            return 29;
        }
        return DaysInMonth.access$100()[2];
    }

    public GregorianCalendar toGregorianCalendar() {
        GregorianCalendar gregorianCalendar = null;
        TimeZone timeZone = this.getTimeZone(Integer.MIN_VALUE);
        Locale locale = Locale.getDefault();
        gregorianCalendar = new GregorianCalendar(timeZone, locale);
        gregorianCalendar.clear();
        gregorianCalendar.setGregorianChange(PURE_GREGORIAN_CHANGE);
        if (this.year != Integer.MIN_VALUE) {
            if (this.eon == null) {
                gregorianCalendar.set(0, this.year < 0 ? 0 : 1);
                gregorianCalendar.set(1, Math.abs(this.year));
            } else {
                BigInteger bigInteger = this.getEonAndYear();
                gregorianCalendar.set(0, bigInteger.signum() == -1 ? 0 : 1);
                gregorianCalendar.set(1, bigInteger.abs().intValue());
            }
        }
        if (this.month != Integer.MIN_VALUE) {
            gregorianCalendar.set(2, this.month - 1);
        }
        if (this.day != Integer.MIN_VALUE) {
            gregorianCalendar.set(5, this.day);
        }
        if (this.hour != Integer.MIN_VALUE) {
            gregorianCalendar.set(11, this.hour);
        }
        if (this.minute != Integer.MIN_VALUE) {
            gregorianCalendar.set(12, this.minute);
        }
        if (this.second != Integer.MIN_VALUE) {
            gregorianCalendar.set(13, this.second);
        }
        if (this.fractionalSecond != null) {
            gregorianCalendar.set(14, this.getMillisecond());
        }
        return gregorianCalendar;
    }

    public GregorianCalendar toGregorianCalendar(TimeZone timeZone, Locale locale, XMLGregorianCalendar xMLGregorianCalendar) {
        int n2;
        GregorianCalendar gregorianCalendar = null;
        TimeZone timeZone2 = timeZone;
        if (timeZone2 == null) {
            n2 = Integer.MIN_VALUE;
            if (xMLGregorianCalendar != null) {
                n2 = xMLGregorianCalendar.getTimezone();
            }
            timeZone2 = this.getTimeZone(n2);
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        gregorianCalendar = new GregorianCalendar(timeZone2, locale);
        gregorianCalendar.clear();
        gregorianCalendar.setGregorianChange(PURE_GREGORIAN_CHANGE);
        if (this.year != Integer.MIN_VALUE) {
            if (this.eon == null) {
                gregorianCalendar.set(0, this.year < 0 ? 0 : 1);
                gregorianCalendar.set(1, Math.abs(this.year));
            } else {
                BigInteger bigInteger = this.getEonAndYear();
                gregorianCalendar.set(0, bigInteger.signum() == -1 ? 0 : 1);
                gregorianCalendar.set(1, bigInteger.abs().intValue());
            }
        } else if (xMLGregorianCalendar != null && (n2 = xMLGregorianCalendar.getYear()) != Integer.MIN_VALUE) {
            if (xMLGregorianCalendar.getEon() == null) {
                gregorianCalendar.set(0, n2 < 0 ? 0 : 1);
                gregorianCalendar.set(1, Math.abs(n2));
            } else {
                BigInteger bigInteger = xMLGregorianCalendar.getEonAndYear();
                gregorianCalendar.set(0, bigInteger.signum() == -1 ? 0 : 1);
                gregorianCalendar.set(1, bigInteger.abs().intValue());
            }
        }
        if (this.month != Integer.MIN_VALUE) {
            gregorianCalendar.set(2, this.month - 1);
        } else {
            int n3 = n2 = xMLGregorianCalendar != null ? xMLGregorianCalendar.getMonth() : Integer.MIN_VALUE;
            if (n2 != Integer.MIN_VALUE) {
                gregorianCalendar.set(2, n2 - 1);
            }
        }
        if (this.day != Integer.MIN_VALUE) {
            gregorianCalendar.set(5, this.day);
        } else {
            int n4 = n2 = xMLGregorianCalendar != null ? xMLGregorianCalendar.getDay() : Integer.MIN_VALUE;
            if (n2 != Integer.MIN_VALUE) {
                gregorianCalendar.set(5, n2);
            }
        }
        if (this.hour != Integer.MIN_VALUE) {
            gregorianCalendar.set(11, this.hour);
        } else {
            int n5 = n2 = xMLGregorianCalendar != null ? xMLGregorianCalendar.getHour() : Integer.MIN_VALUE;
            if (n2 != Integer.MIN_VALUE) {
                gregorianCalendar.set(11, n2);
            }
        }
        if (this.minute != Integer.MIN_VALUE) {
            gregorianCalendar.set(12, this.minute);
        } else {
            int n6 = n2 = xMLGregorianCalendar != null ? xMLGregorianCalendar.getMinute() : Integer.MIN_VALUE;
            if (n2 != Integer.MIN_VALUE) {
                gregorianCalendar.set(12, n2);
            }
        }
        if (this.second != Integer.MIN_VALUE) {
            gregorianCalendar.set(13, this.second);
        } else {
            int n7 = n2 = xMLGregorianCalendar != null ? xMLGregorianCalendar.getSecond() : Integer.MIN_VALUE;
            if (n2 != Integer.MIN_VALUE) {
                gregorianCalendar.set(13, n2);
            }
        }
        if (this.fractionalSecond != null) {
            gregorianCalendar.set(14, this.getMillisecond());
        } else {
            BigDecimal bigDecimal;
            BigDecimal bigDecimal2 = bigDecimal = xMLGregorianCalendar != null ? xMLGregorianCalendar.getFractionalSecond() : null;
            if (bigDecimal != null) {
                gregorianCalendar.set(14, xMLGregorianCalendar.getMillisecond());
            }
        }
        return gregorianCalendar;
    }

    public TimeZone getTimeZone(int n2) {
        TimeZone timeZone = null;
        int n3 = this.getTimezone();
        if (n3 == Integer.MIN_VALUE) {
            n3 = n2;
        }
        if (n3 == Integer.MIN_VALUE) {
            timeZone = TimeZone.getDefault();
        } else {
            char c2;
            char c3 = c2 = n3 < 0 ? '-' : '+';
            if (c2 == '-') {
                n3 = - n3;
            }
            int n4 = n3 / 60;
            int n5 = n3 - n4 * 60;
            StringBuffer stringBuffer = new StringBuffer(8);
            stringBuffer.append("GMT");
            stringBuffer.append(c2);
            stringBuffer.append(n4);
            if (n5 != 0) {
                if (n5 < 10) {
                    stringBuffer.append('0');
                }
                stringBuffer.append(n5);
            }
            timeZone = TimeZone.getTimeZone(stringBuffer.toString());
        }
        return timeZone;
    }

    public Object clone() {
        return new XMLGregorianCalendarImpl(this.getEonAndYear(), this.month, this.day, this.hour, this.minute, this.second, this.fractionalSecond, this.timezone);
    }

    public void clear() {
        this.eon = null;
        this.year = Integer.MIN_VALUE;
        this.month = Integer.MIN_VALUE;
        this.day = Integer.MIN_VALUE;
        this.timezone = Integer.MIN_VALUE;
        this.hour = Integer.MIN_VALUE;
        this.minute = Integer.MIN_VALUE;
        this.second = Integer.MIN_VALUE;
        this.fractionalSecond = null;
    }

    public void setMillisecond(int n2) {
        if (n2 == Integer.MIN_VALUE) {
            this.fractionalSecond = null;
        } else {
            this.checkFieldValueConstraint(6, n2);
            this.fractionalSecond = BigDecimal.valueOf(n2, 3);
        }
    }

    public void setFractionalSecond(BigDecimal bigDecimal) {
        if (bigDecimal != null && (bigDecimal.compareTo(DECIMAL_ZERO) < 0 || bigDecimal.compareTo(DECIMAL_ONE) > 0)) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidFractional", new Object[]{bigDecimal}));
        }
        this.fractionalSecond = bigDecimal;
    }

    private static boolean isDigit(char c2) {
        return '0' <= c2 && c2 <= '9';
    }

    private String format(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        int n3 = string.length();
        block9 : while (n2 < n3) {
            char c2;
            if ((c2 = string.charAt(n2++)) != '%') {
                stringBuffer.append(c2);
                continue;
            }
            switch (string.charAt(n2++)) {
                int n4;
                case 'Y': {
                    if (this.eon == null) {
                        n4 = this.year;
                        if (n4 < 0) {
                            stringBuffer.append('-');
                            n4 = - this.year;
                        }
                        this.printNumber(stringBuffer, n4, 4);
                        break;
                    }
                    this.printNumber(stringBuffer, this.getEonAndYear(), 4);
                    break;
                }
                case 'M': {
                    this.printNumber(stringBuffer, this.getMonth(), 2);
                    break;
                }
                case 'D': {
                    this.printNumber(stringBuffer, this.getDay(), 2);
                    break;
                }
                case 'h': {
                    this.printNumber(stringBuffer, this.getHour(), 2);
                    break;
                }
                case 'm': {
                    this.printNumber(stringBuffer, this.getMinute(), 2);
                    break;
                }
                case 's': {
                    this.printNumber(stringBuffer, this.getSecond(), 2);
                    if (this.getFractionalSecond() == null) continue block9;
                    String string2 = this.toString(this.getFractionalSecond());
                    stringBuffer.append(string2.substring(1, string2.length()));
                    break;
                }
                case 'z': {
                    n4 = this.getTimezone();
                    if (n4 == 0) {
                        stringBuffer.append('Z');
                        break;
                    }
                    if (n4 == Integer.MIN_VALUE) continue block9;
                    if (n4 < 0) {
                        stringBuffer.append('-');
                        n4 *= -1;
                    } else {
                        stringBuffer.append('+');
                    }
                    this.printNumber(stringBuffer, n4 / 60, 2);
                    stringBuffer.append(':');
                    this.printNumber(stringBuffer, n4 % 60, 2);
                    break;
                }
                default: {
                    throw new InternalError();
                }
            }
        }
        return stringBuffer.toString();
    }

    private void printNumber(StringBuffer stringBuffer, int n2, int n3) {
        String string = String.valueOf(n2);
        int n4 = string.length();
        while (n4 < n3) {
            stringBuffer.append('0');
            ++n4;
        }
        stringBuffer.append(string);
    }

    private void printNumber(StringBuffer stringBuffer, BigInteger bigInteger, int n2) {
        String string = bigInteger.toString();
        int n3 = string.length();
        while (n3 < n2) {
            stringBuffer.append('0');
            ++n3;
        }
        stringBuffer.append(string);
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

    static BigInteger sanitize(Number number, int n2) {
        if (n2 == 0 || number == null) {
            return BigInteger.ZERO;
        }
        return n2 < 0 ? ((BigInteger)number).negate() : (BigInteger)number;
    }

    public void reset() {
        this.eon = this.orig_eon;
        this.year = this.orig_year;
        this.month = this.orig_month;
        this.day = this.orig_day;
        this.hour = this.orig_hour;
        this.minute = this.orig_minute;
        this.second = this.orig_second;
        this.fractionalSecond = this.orig_fracSeconds;
        this.timezone = this.orig_timezone;
    }

    private Object writeReplace() throws IOException {
        return new SerializedXMLGregorianCalendar(this.toXMLFormat());
    }

    static boolean access$200(char c2) {
        return XMLGregorianCalendarImpl.isDigit(c2);
    }

    class 1 {
    }

    private final class Parser {
        private final String format;
        private final String value;
        private final int flen;
        private final int vlen;
        private int fidx;
        private int vidx;
        private final XMLGregorianCalendarImpl this$0;

        private Parser(XMLGregorianCalendarImpl xMLGregorianCalendarImpl, String string, String string2) {
            this.this$0 = xMLGregorianCalendarImpl;
            this.format = string;
            this.value = string2;
            this.flen = string.length();
            this.vlen = string2.length();
        }

        public void parse() throws IllegalArgumentException {
            block9 : while (this.fidx < this.flen) {
                char c2;
                if ((c2 = this.format.charAt(this.fidx++)) != '%') {
                    this.skip(c2);
                    continue;
                }
                switch (this.format.charAt(this.fidx++)) {
                    case 'Y': {
                        this.parseYear();
                        break;
                    }
                    case 'M': {
                        this.this$0.setMonth(this.parseInt(2, 2));
                        break;
                    }
                    case 'D': {
                        this.this$0.setDay(this.parseInt(2, 2));
                        break;
                    }
                    case 'h': {
                        this.this$0.setHour(this.parseInt(2, 2));
                        break;
                    }
                    case 'm': {
                        this.this$0.setMinute(this.parseInt(2, 2));
                        break;
                    }
                    case 's': {
                        this.this$0.setSecond(this.parseInt(2, 2));
                        if (this.peek() != '.') continue block9;
                        this.this$0.setFractionalSecond(this.parseBigDecimal());
                        break;
                    }
                    case 'z': {
                        char c3 = this.peek();
                        if (c3 == 'Z') {
                            ++this.vidx;
                            this.this$0.setTimezone(0);
                            break;
                        }
                        if (c3 != '+' && c3 != '-') continue block9;
                        ++this.vidx;
                        int n2 = this.parseInt(2, 2);
                        this.skip(':');
                        int n3 = this.parseInt(2, 2);
                        this.this$0.setTimezone((n2 * 60 + n3) * (c3 == '+' ? 1 : -1));
                        break;
                    }
                    default: {
                        throw new InternalError();
                    }
                }
            }
            if (this.vidx != this.vlen) {
                throw new IllegalArgumentException(this.value);
            }
        }

        private char peek() throws IllegalArgumentException {
            if (this.vidx == this.vlen) {
                return '\uffff';
            }
            return this.value.charAt(this.vidx);
        }

        private char read() throws IllegalArgumentException {
            if (this.vidx == this.vlen) {
                throw new IllegalArgumentException(this.value);
            }
            return this.value.charAt(this.vidx++);
        }

        private void skip(char c2) throws IllegalArgumentException {
            if (this.read() != c2) {
                throw new IllegalArgumentException(this.value);
            }
        }

        private void parseYear() throws IllegalArgumentException {
            int n2 = this.vidx++;
            int n3 = 0;
            if (this.peek() == '-') {
                n3 = 1;
            }
            while (XMLGregorianCalendarImpl.access$200(this.peek())) {
                ++this.vidx;
            }
            int n4 = this.vidx - n2 - n3;
            if (n4 < 4) {
                throw new IllegalArgumentException(this.value);
            }
            String string = this.value.substring(n2, this.vidx);
            if (n4 < 10) {
                this.this$0.setYear(Integer.parseInt(string));
            } else {
                this.this$0.setYear(new BigInteger(string));
            }
        }

        private int parseInt(int n2, int n3) throws IllegalArgumentException {
            int n4 = this.vidx;
            while (XMLGregorianCalendarImpl.access$200(this.peek()) && this.vidx - n4 < n3) {
                ++this.vidx;
            }
            if (this.vidx - n4 < n2) {
                throw new IllegalArgumentException(this.value);
            }
            return Integer.parseInt(this.value.substring(n4, this.vidx));
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        private BigDecimal parseBigDecimal() throws IllegalArgumentException {
            int n2 = this.vidx++;
            if (this.peek() != '.') throw new IllegalArgumentException(this.value);
            while (XMLGregorianCalendarImpl.access$200(this.peek())) {
                ++this.vidx;
            }
            return new BigDecimal(this.value.substring(n2, this.vidx));
        }

        Parser(XMLGregorianCalendarImpl xMLGregorianCalendarImpl, String string, String string2, 1 var4_4) {
            this(xMLGregorianCalendarImpl, string, string2);
        }
    }

    private static class DaysInMonth {
        private static final int[] table = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        private DaysInMonth() {
        }

        static int[] access$100() {
            return table;
        }
    }

}

