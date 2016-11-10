/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.math3.util;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Locale;

public class CompositeFormat {
    public static NumberFormat getDefaultNumberFormat() {
        return CompositeFormat.getDefaultNumberFormat(Locale.getDefault());
    }

    public static NumberFormat getDefaultNumberFormat(Locale locale) {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(10);
        return numberFormat;
    }

    public static StringBuffer formatDouble(double d2, NumberFormat numberFormat, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            stringBuffer.append('(');
            stringBuffer.append(d2);
            stringBuffer.append(')');
        } else {
            numberFormat.format(d2, stringBuffer, fieldPosition);
        }
        return stringBuffer;
    }
}

