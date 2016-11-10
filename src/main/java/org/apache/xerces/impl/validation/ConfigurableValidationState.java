/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.validation;

import org.apache.xerces.impl.validation.ValidationState;

public final class ConfigurableValidationState
extends ValidationState {
    private boolean fIdIdrefChecking = true;
    private boolean fUnparsedEntityChecking = true;

    public void setIdIdrefChecking(boolean bl) {
        this.fIdIdrefChecking = bl;
    }

    public void setUnparsedEntityChecking(boolean bl) {
        this.fUnparsedEntityChecking = bl;
    }

    public String checkIDRefID() {
        return this.fIdIdrefChecking ? super.checkIDRefID() : null;
    }

    public boolean isIdDeclared(String string) {
        return this.fIdIdrefChecking ? super.isIdDeclared(string) : false;
    }

    public boolean isEntityDeclared(String string) {
        return this.fUnparsedEntityChecking ? super.isEntityDeclared(string) : true;
    }

    public boolean isEntityUnparsed(String string) {
        return this.fUnparsedEntityChecking ? super.isEntityUnparsed(string) : true;
    }

    public void addId(String string) {
        if (this.fIdIdrefChecking) {
            super.addId(string);
        }
    }

    public void addIdRef(String string) {
        if (this.fIdIdrefChecking) {
            super.addIdRef(string);
        }
    }
}

