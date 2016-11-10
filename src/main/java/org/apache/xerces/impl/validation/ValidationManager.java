/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.validation;

import java.util.ArrayList;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationState;

public class ValidationManager {
    protected final ArrayList fVSs = new ArrayList();
    protected boolean fGrammarFound = false;
    protected boolean fCachedDTD = false;

    public final void addValidationState(ValidationState validationState) {
        this.fVSs.add(validationState);
    }

    public final void setEntityState(EntityState entityState) {
        int n2 = this.fVSs.size() - 1;
        while (n2 >= 0) {
            ((ValidationState)this.fVSs.get(n2)).setEntityState(entityState);
            --n2;
        }
    }

    public final void setGrammarFound(boolean bl) {
        this.fGrammarFound = bl;
    }

    public final boolean isGrammarFound() {
        return this.fGrammarFound;
    }

    public final void setCachedDTD(boolean bl) {
        this.fCachedDTD = bl;
    }

    public final boolean isCachedDTD() {
        return this.fCachedDTD;
    }

    public final void reset() {
        this.fVSs.clear();
        this.fGrammarFound = false;
        this.fCachedDTD = false;
    }
}

