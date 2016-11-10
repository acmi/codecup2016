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

class DayTimeDurationDV
extends DurationDV {
    DayTimeDurationDV() {
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        try {
            return this.parse(string, 2);
        }
        catch (Exception exception) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "dayTimeDuration"});
        }
    }

    protected Duration getDuration(AbstractDateTimeDV.DateTimeData dateTimeData) {
        int n2 = 1;
        if (dateTimeData.day < 0 || dateTimeData.hour < 0 || dateTimeData.minute < 0 || dateTimeData.second < 0.0) {
            n2 = -1;
        }
        return AbstractDateTimeDV.datatypeFactory.newDuration(n2 == 1, null, null, dateTimeData.day != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.day) : null, dateTimeData.hour != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.hour) : null, dateTimeData.minute != Integer.MIN_VALUE ? BigInteger.valueOf(n2 * dateTimeData.minute) : null, dateTimeData.second != -2.147483648E9 ? new BigDecimal(String.valueOf((double)n2 * dateTimeData.second)) : null);
    }
}

