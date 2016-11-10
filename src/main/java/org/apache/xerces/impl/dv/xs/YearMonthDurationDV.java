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
import org.apache.xerces.impl.dv.xs.DurationDV;

class YearMonthDurationDV
extends DurationDV {
    YearMonthDurationDV() {
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        try {
            return this.parse(string, 1);
        }
        catch (Exception exception) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "yearMonthDuration"});
        }
    }

    protected Duration getDuration(AbstractDateTimeDV.DateTimeData dateTimeData) {
        int n2 = 1;
        if (dateTimeData.year < 0 || dateTimeData.month < 0) {
            n2 = -1;
        }
        return AbstractDateTimeDV.datatypeFactory.newDuration(n2 == 1, dateTimeData.year != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.year) : null, dateTimeData.month != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.month) : null, null, null, null, null);
    }
}

