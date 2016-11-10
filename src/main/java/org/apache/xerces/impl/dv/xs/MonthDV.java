/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.AbstractDateTimeDV;
import org.apache.xerces.impl.dv.xs.SchemaDateTimeException;

public class MonthDV
extends AbstractDateTimeDV {
    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        try {
            return this.parse(string);
        }
        catch (Exception exception) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "gMonth"});
        }
    }

    protected AbstractDateTimeDV.DateTimeData parse(String string) throws SchemaDateTimeException {
        AbstractDateTimeDV.DateTimeData dateTimeData = new AbstractDateTimeDV.DateTimeData(string, this);
        int n2 = string.length();
        dateTimeData.year = 2000;
        dateTimeData.day = 1;
        if (string.charAt(0) != '-' || string.charAt(1) != '-') {
            throw new SchemaDateTimeException("Invalid format for gMonth: " + string);
        }
        int n3 = 4;
        dateTimeData.month = this.parseInt(string, 2, n3);
        if (string.length() >= n3 + 2 && string.charAt(n3) == '-' && string.charAt(n3 + 1) == '-') {
            n3 += 2;
        }
        if (n3 < n2) {
            if (!this.isNextCharUTCSign(string, n3, n2)) {
                throw new SchemaDateTimeException("Error in month parsing: " + string);
            }
            this.getTimeZone(string, dateTimeData, n3, n2);
        }
        this.validateDateTime(dateTimeData);
        this.saveUnnormalized(dateTimeData);
        if (dateTimeData.utc != 0 && dateTimeData.utc != 90) {
            this.normalize(dateTimeData);
        }
        dateTimeData.position = 1;
        return dateTimeData;
    }

    protected String dateToString(AbstractDateTimeDV.DateTimeData dateTimeData) {
        StringBuffer stringBuffer = new StringBuffer(5);
        stringBuffer.append('-');
        stringBuffer.append('-');
        this.append(stringBuffer, dateTimeData.month, 2);
        this.append(stringBuffer, (char)dateTimeData.utc, 0);
        return stringBuffer.toString();
    }

    protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData dateTimeData) {
        return AbstractDateTimeDV.datatypeFactory.newXMLGregorianCalendar(Integer.MIN_VALUE, dateTimeData.unNormMonth, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, dateTimeData.hasTimeZone() ? dateTimeData.timezoneHr * 60 + dateTimeData.timezoneMin : Integer.MIN_VALUE);
    }
}

