/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public interface DatatypeValidator {
    public void validate(String var1, ValidationContext var2) throws InvalidDatatypeValueException;
}

