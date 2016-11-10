/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.AbstractDateTimeDV;
import org.apache.xerces.impl.dv.xs.SchemaDateTimeException;

public class DurationDV
extends AbstractDateTimeDV {
    public static final int DURATION_TYPE = 0;
    public static final int YEARMONTHDURATION_TYPE = 1;
    public static final int DAYTIMEDURATION_TYPE = 2;
    private static final AbstractDateTimeDV.DateTimeData[] DATETIMES = new AbstractDateTimeDV.DateTimeData[]{new AbstractDateTimeDV.DateTimeData(1696, 9, 1, 0, 0, 0.0, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1697, 2, 1, 0, 0, 0.0, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1903, 3, 1, 0, 0, 0.0, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1903, 7, 1, 0, 0, 0.0, 90, null, true, null)};

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        try {
            return this.parse(string, 0);
        }
        catch (Exception exception) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "duration"});
        }
    }

    protected AbstractDateTimeDV.DateTimeData parse(String string, int n2) throws SchemaDateTimeException {
        char c2;
        int n3 = string.length();
        AbstractDateTimeDV.DateTimeData dateTimeData = new AbstractDateTimeDV.DateTimeData(string, this);
        int n4 = 0;
        if ((c2 = string.charAt(n4++)) != 'P' && c2 != '-') {
            throw new SchemaDateTimeException();
        }
        int n5 = dateTimeData.utc = c2 == '-' ? 45 : 0;
        if (c2 == '-' && string.charAt(n4++) != 'P') {
            throw new SchemaDateTimeException();
        }
        int n6 = 1;
        if (dateTimeData.utc == 45) {
            n6 = -1;
        }
        boolean bl = false;
        int n7 = this.indexOf(string, n4, n3, 'T');
        if (n7 == -1) {
            n7 = n3;
        } else if (n2 == 1) {
            throw new SchemaDateTimeException();
        }
        int n8 = this.indexOf(string, n4, n7, 'Y');
        if (n8 != -1) {
            if (n2 == 2) {
                throw new SchemaDateTimeException();
            }
            dateTimeData.year = n6 * this.parseInt(string, n4, n8);
            n4 = n8 + 1;
            bl = true;
        }
        if ((n8 = this.indexOf(string, n4, n7, 'M')) != -1) {
            if (n2 == 2) {
                throw new SchemaDateTimeException();
            }
            dateTimeData.month = n6 * this.parseInt(string, n4, n8);
            n4 = n8 + 1;
            bl = true;
        }
        if ((n8 = this.indexOf(string, n4, n7, 'D')) != -1) {
            if (n2 == 1) {
                throw new SchemaDateTimeException();
            }
            dateTimeData.day = n6 * this.parseInt(string, n4, n8);
            n4 = n8 + 1;
            bl = true;
        }
        if (n3 == n7 && n4 != n3) {
            throw new SchemaDateTimeException();
        }
        if (n3 != n7) {
            if ((n8 = this.indexOf(string, ++n4, n3, 'H')) != -1) {
                dateTimeData.hour = n6 * this.parseInt(string, n4, n8);
                n4 = n8 + 1;
                bl = true;
            }
            if ((n8 = this.indexOf(string, n4, n3, 'M')) != -1) {
                dateTimeData.minute = n6 * this.parseInt(string, n4, n8);
                n4 = n8 + 1;
                bl = true;
            }
            if ((n8 = this.indexOf(string, n4, n3, 'S')) != -1) {
                dateTimeData.second = (double)n6 * this.parseSecond(string, n4, n8);
                n4 = n8 + 1;
                bl = true;
            }
            if (n4 != n3 || string.charAt(--n4) == 'T') {
                throw new SchemaDateTimeException();
            }
        }
        if (!bl) {
            throw new SchemaDateTimeException();
        }
        return dateTimeData;
    }

    protected short compareDates(AbstractDateTimeDV.DateTimeData dateTimeData, AbstractDateTimeDV.DateTimeData dateTimeData2, boolean bl) {
        AbstractDateTimeDV.DateTimeData dateTimeData3;
        short s2 = 2;
        short s3 = this.compareOrder(dateTimeData, dateTimeData2);
        if (s3 == 0) {
            return 0;
        }
        AbstractDateTimeDV.DateTimeData[] arrdateTimeData = new AbstractDateTimeDV.DateTimeData[]{new AbstractDateTimeDV.DateTimeData(null, this), new AbstractDateTimeDV.DateTimeData(null, this)};
        AbstractDateTimeDV.DateTimeData dateTimeData4 = this.addDuration(dateTimeData, DATETIMES[0], arrdateTimeData[0]);
        s3 = this.compareOrder(dateTimeData4, dateTimeData3 = this.addDuration(dateTimeData2, DATETIMES[0], arrdateTimeData[1]));
        if (s3 == 2) {
            return 2;
        }
        dateTimeData4 = this.addDuration(dateTimeData, DATETIMES[1], arrdateTimeData[0]);
        s2 = this.compareOrder(dateTimeData4, dateTimeData3 = this.addDuration(dateTimeData2, DATETIMES[1], arrdateTimeData[1]));
        if ((s3 = this.compareResults(s3, s2, bl)) == 2) {
            return 2;
        }
        dateTimeData4 = this.addDuration(dateTimeData, DATETIMES[2], arrdateTimeData[0]);
        s2 = this.compareOrder(dateTimeData4, dateTimeData3 = this.addDuration(dateTimeData2, DATETIMES[2], arrdateTimeData[1]));
        if ((s3 = this.compareResults(s3, s2, bl)) == 2) {
            return 2;
        }
        dateTimeData4 = this.addDuration(dateTimeData, DATETIMES[3], arrdateTimeData[0]);
        dateTimeData3 = this.addDuration(dateTimeData2, DATETIMES[3], arrdateTimeData[1]);
        s2 = this.compareOrder(dateTimeData4, dateTimeData3);
        s3 = this.compareResults(s3, s2, bl);
        return s3;
    }

    private short compareResults(short s2, short s3, boolean bl) {
        if (s3 == 2) {
            return 2;
        }
        if (s2 != s3 && bl) {
            return 2;
        }
        if (s2 != s3 && !bl) {
            if (s2 != 0 && s3 != 0) {
                return 2;
            }
            return s2 != 0 ? s2 : s3;
        }
        return s2;
    }

    private AbstractDateTimeDV.DateTimeData addDuration(AbstractDateTimeDV.DateTimeData dateTimeData, AbstractDateTimeDV.DateTimeData dateTimeData2, AbstractDateTimeDV.DateTimeData dateTimeData3) {
        this.resetDateObj(dateTimeData3);
        int n2 = dateTimeData2.month + dateTimeData.month;
        dateTimeData3.month = this.modulo(n2, 1, 13);
        int n3 = this.fQuotient(n2, 1, 13);
        dateTimeData3.year = dateTimeData2.year + dateTimeData.year + n3;
        double d2 = dateTimeData2.second + dateTimeData.second;
        n3 = (int)Math.floor(d2 / 60.0);
        dateTimeData3.second = d2 - (double)(n3 * 60);
        n2 = dateTimeData2.minute + dateTimeData.minute + n3;
        n3 = this.fQuotient(n2, 60);
        dateTimeData3.minute = this.mod(n2, 60, n3);
        n2 = dateTimeData2.hour + dateTimeData.hour + n3;
        n3 = this.fQuotient(n2, 24);
        dateTimeData3.hour = this.mod(n2, 24, n3);
        dateTimeData3.day = dateTimeData2.day + dateTimeData.day + n3;
        do {
            n2 = this.maxDayInMonthFor(dateTimeData3.year, dateTimeData3.month);
            if (dateTimeData3.day < 1) {
                dateTimeData3.day += this.maxDayInMonthFor(dateTimeData3.year, dateTimeData3.month - 1);
                n3 = -1;
            } else {
                if (dateTimeData3.day <= n2) break;
                dateTimeData3.day -= n2;
                n3 = 1;
            }
            n2 = dateTimeData3.month + n3;
            dateTimeData3.month = this.modulo(n2, 1, 13);
            dateTimeData3.year += this.fQuotient(n2, 1, 13);
        } while (true);
        dateTimeData3.utc = 90;
        return dateTimeData3;
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
        if (n4 + 1 == n3) {
            throw new NumberFormatException("'" + string + "' has wrong format");
        }
        double d2 = Double.parseDouble(string.substring(n2, n3));
        if (d2 == Double.POSITIVE_INFINITY) {
            throw new NumberFormatException("'" + string + "' has wrong format");
        }
        return d2;
    }

    protected String dateToString(AbstractDateTimeDV.DateTimeData dateTimeData) {
        StringBuffer stringBuffer = new StringBuffer(30);
        if (dateTimeData.year < 0 || dateTimeData.month < 0 || dateTimeData.day < 0 || dateTimeData.hour < 0 || dateTimeData.minute < 0 || dateTimeData.second < 0.0) {
            stringBuffer.append('-');
        }
        stringBuffer.append('P');
        stringBuffer.append((dateTimeData.year < 0 ? -1 : 1) * dateTimeData.year);
        stringBuffer.append('Y');
        stringBuffer.append((dateTimeData.month < 0 ? -1 : 1) * dateTimeData.month);
        stringBuffer.append('M');
        stringBuffer.append((dateTimeData.day < 0 ? -1 : 1) * dateTimeData.day);
        stringBuffer.append('D');
        stringBuffer.append('T');
        stringBuffer.append((dateTimeData.hour < 0 ? -1 : 1) * dateTimeData.hour);
        stringBuffer.append('H');
        stringBuffer.append((dateTimeData.minute < 0 ? -1 : 1) * dateTimeData.minute);
        stringBuffer.append('M');
        this.append2(stringBuffer, (dateTimeData.second < 0.0 ? -1.0 : 1.0) * dateTimeData.second);
        stringBuffer.append('S');
        return stringBuffer.toString();
    }

    protected Duration getDuration(AbstractDateTimeDV.DateTimeData dateTimeData) {
        int n2 = 1;
        if (dateTimeData.year < 0 || dateTimeData.month < 0 || dateTimeData.day < 0 || dateTimeData.hour < 0 || dateTimeData.minute < 0 || dateTimeData.second < 0.0) {
            n2 = -1;
        }
        return AbstractDateTimeDV.datatypeFactory.newDuration(n2 == 1, dateTimeData.year != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.year) : null, dateTimeData.month != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.month) : null, dateTimeData.day != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.day) : null, dateTimeData.hour != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.hour) : null, dateTimeData.minute != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.minute) : null, dateTimeData.second != -2.147483648E9 ? new BigDecimal(String.valueOf((double)n2 * dateTimeData.second)) : null);
    }
}

