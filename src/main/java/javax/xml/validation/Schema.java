/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.validation;

import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

public abstract class Schema {
    protected Schema() {
    }

    public abstract Validator newValidator();

    public abstract ValidatorHandler newValidatorHandler();
}
