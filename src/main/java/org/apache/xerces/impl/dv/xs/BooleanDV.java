/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.TypeValidator;

public class BooleanDV
extends TypeValidator {
    public short getAllowedFacets() {
        return 24;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        if ("false".equals(string) || "0".equals(string)) {
            return Boolean.FALSE;
        }
        if ("true".equals(string) || "1".equals(string)) {
            return Boolean.TRUE;
        }
        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "boolean"});
    }
}

