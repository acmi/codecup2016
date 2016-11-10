/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.BaseSchemaDVFactory;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.util.SymbolHash;

public class SchemaDVFactoryImpl
extends BaseSchemaDVFactory {
    static final SymbolHash fBuiltInTypes = new SymbolHash();

    static void createBuiltInTypes() {
        BaseSchemaDVFactory.createBuiltInTypes(fBuiltInTypes, XSSimpleTypeDecl.fAnySimpleType);
    }

    public XSSimpleType getBuiltInType(String string) {
        return (XSSimpleType)fBuiltInTypes.get(string);
    }

    public SymbolHash getBuiltInTypes() {
        return fBuiltInTypes.makeClone();
    }

    static {
        SchemaDVFactoryImpl.createBuiltInTypes();
    }
}

