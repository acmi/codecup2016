/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;

public class ValidationState
implements ValidationContext {
    private boolean fExtraChecking = true;
    private boolean fFacetChecking = true;
    private boolean fNormalize = true;
    private boolean fNamespaces = true;
    private EntityState fEntityState = null;
    private NamespaceContext fNamespaceContext = null;
    private SymbolTable fSymbolTable = null;
    private Locale fLocale = null;
    private final HashMap fIdTable = new HashMap();
    private final HashMap fIdRefTable = new HashMap();
    private static final Object fNullValue = new Object();

    public void setExtraChecking(boolean bl) {
        this.fExtraChecking = bl;
    }

    public void setFacetChecking(boolean bl) {
        this.fFacetChecking = bl;
    }

    public void setNormalizationRequired(boolean bl) {
        this.fNormalize = bl;
    }

    public void setUsingNamespaces(boolean bl) {
        this.fNamespaces = bl;
    }

    public void setEntityState(EntityState entityState) {
        this.fEntityState = entityState;
    }

    public void setNamespaceSupport(NamespaceContext namespaceContext) {
        this.fNamespaceContext = namespaceContext;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public String checkIDRefID() {
        Iterator iterator = this.fIdRefTable.keySet().iterator();
        while (iterator.hasNext()) {
            String string = (String)iterator.next();
            if (this.fIdTable.containsKey(string)) continue;
            return string;
        }
        return null;
    }

    public void reset() {
        this.fExtraChecking = true;
        this.fFacetChecking = true;
        this.fNamespaces = true;
        this.fIdTable.clear();
        this.fIdRefTable.clear();
        this.fEntityState = null;
        this.fNamespaceContext = null;
        this.fSymbolTable = null;
    }

    public void resetIDTables() {
        this.fIdTable.clear();
        this.fIdRefTable.clear();
    }

    public boolean needExtraChecking() {
        return this.fExtraChecking;
    }

    public boolean needFacetChecking() {
        return this.fFacetChecking;
    }

    public boolean needToNormalize() {
        return this.fNormalize;
    }

    public boolean useNamespaces() {
        return this.fNamespaces;
    }

    public boolean isEntityDeclared(String string) {
        if (this.fEntityState != null) {
            return this.fEntityState.isEntityDeclared(this.getSymbol(string));
        }
        return false;
    }

    public boolean isEntityUnparsed(String string) {
        if (this.fEntityState != null) {
            return this.fEntityState.isEntityUnparsed(this.getSymbol(string));
        }
        return false;
    }

    public boolean isIdDeclared(String string) {
        return this.fIdTable.containsKey(string);
    }

    public void addId(String string) {
        this.fIdTable.put(string, fNullValue);
    }

    public void addIdRef(String string) {
        this.fIdRefTable.put(string, fNullValue);
    }

    public String getSymbol(String string) {
        if (this.fSymbolTable != null) {
            return this.fSymbolTable.addSymbol(string);
        }
        return string.intern();
    }

    public String getURI(String string) {
        if (this.fNamespaceContext != null) {
            return this.fNamespaceContext.getURI(string);
        }
        return null;
    }

    public void setLocale(Locale locale) {
        this.fLocale = locale;
    }

    public Locale getLocale() {
        return this.fLocale;
    }
}

