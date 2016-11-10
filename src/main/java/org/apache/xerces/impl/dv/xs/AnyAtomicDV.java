/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.TypeValidator;

class AnyAtomicDV
extends TypeValidator {
    AnyAtomicDV() {
    }

    public short getAllowedFacets() {
        return 0;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        return string;
    }
}

