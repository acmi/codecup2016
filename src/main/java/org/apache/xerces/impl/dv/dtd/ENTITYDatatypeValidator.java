/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.dtd;

import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class ENTITYDatatypeValidator
implements DatatypeValidator {
    public void validate(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        if (!validationContext.isEntityUnparsed(string)) {
            throw new InvalidDatatypeValueException("ENTITYNotUnparsed", new Object[]{string});
        }
    }
}

