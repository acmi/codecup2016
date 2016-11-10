/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.math3.geometry;

import java.text.FieldPosition;
import java.text.NumberFormat;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.util.CompositeFormat;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class VectorFormat<S> {
    private final String prefix;
    private final String suffix;
    private final String separator;
    private final String trimmedPrefix;
    private final String trimmedSuffix;
    private final String trimmedSeparator;
    private final NumberFormat format;

    protected VectorFormat(String string, String string2, String string3, NumberFormat numberFormat) {
        this.prefix = string;
        this.suffix = string2;
        this.separator = string3;
        this.trimmedPrefix = string.trim();
        this.trimmedSuffix = string2.trim();
        this.trimmedSeparator = string3.trim();
        this.format = numberFormat;
    }

    public String format(Vector<S> vector) {
        return this.format(vector, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public abstract StringBuffer format(Vector<S> var1, StringBuffer var2, FieldPosition var3);

    protected /* varargs */ StringBuffer format(StringBuffer stringBuffer, FieldPosition fieldPosition, double ... arrd) {
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        stringBuffer.append(this.prefix);
        for (int i2 = 0; i2 < arrd.length; ++i2) {
            if (i2 > 0) {
                stringBuffer.append(this.separator);
            }
            CompositeFormat.formatDouble(arrd[i2], this.format, stringBuffer, fieldPosition);
        }
        stringBuffer.append(this.suffix);
        return stringBuffer;
    }
}

