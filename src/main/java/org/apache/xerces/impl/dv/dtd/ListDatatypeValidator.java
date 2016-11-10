/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.dtd;

import java.util.StringTokenizer;
import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class ListDatatypeValidator
implements DatatypeValidator {
    final DatatypeValidator fItemValidator;

    public ListDatatypeValidator(DatatypeValidator datatypeValidator) {
        this.fItemValidator = datatypeValidator;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void validate(String var1_1, ValidationContext var2_2) throws InvalidDatatypeValueException {
        var3_3 = new StringTokenizer(var1_1, " ");
        var4_4 = var3_3.countTokens();
        if (var4_4 != 0) ** GOTO lbl6
        throw new InvalidDatatypeValueException("EmptyList", null);
lbl-1000: // 1 sources:
        {
            this.fItemValidator.validate(var3_3.nextToken(), var2_2);
lbl6: // 2 sources:
            ** while (var3_3.hasMoreTokens())
        }
lbl7: // 1 sources:
    }
}

