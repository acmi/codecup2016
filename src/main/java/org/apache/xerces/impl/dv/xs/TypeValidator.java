/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public abstract class TypeValidator {
    public static final short LESS_THAN = -1;
    public static final short EQUAL = 0;
    public static final short GREATER_THAN = 1;
    public static final short INDETERMINATE = 2;

    public abstract short getAllowedFacets();

    public abstract Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException;

    public void checkExtraRules(Object object, ValidationContext validationContext) throws InvalidDatatypeValueException {
    }

    public boolean isIdentical(Object object, Object object2) {
        return object.equals(object2);
    }

    public int compare(Object object, Object object2) {
        return -1;
    }

    public int getDataLength(Object object) {
        return object instanceof String ? ((String)object).length() : -1;
    }

    public int getTotalDigits(Object object) {
        return -1;
    }

    public int getFractionDigits(Object object) {
        return -1;
    }

    public static final boolean isDigit(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    public static final int getDigit(char c2) {
        return TypeValidator.isDigit(c2) ? c2 - 48 : -1;
    }
}

