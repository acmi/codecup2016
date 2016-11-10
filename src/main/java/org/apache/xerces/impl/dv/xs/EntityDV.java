/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.TypeValidator;
import org.apache.xerces.util.XMLChar;

public class EntityDV
extends TypeValidator {
    public short getAllowedFacets() {
        return 2079;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        if (!XMLChar.isValidNCName(string)) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "NCName"});
        }
        return string;
    }

    public void checkExtraRules(Object object, ValidationContext validationContext) throws InvalidDatatypeValueException {
        if (!validationContext.isEntityUnparsed((String)object)) {
            throw new InvalidDatatypeValueException("UndeclaredEntity", new Object[]{object});
        }
    }
}
