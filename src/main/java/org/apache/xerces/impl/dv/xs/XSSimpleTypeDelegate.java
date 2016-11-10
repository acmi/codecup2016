/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.DatatypeException;
import org.apache.xerces.impl.dv.InvalidDatatypeFacetException;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class XSSimpleTypeDelegate
implements XSSimpleType {
    protected final XSSimpleType type;

    public XSSimpleTypeDelegate(XSSimpleType xSSimpleType) {
        if (xSSimpleType == null) {
            throw new NullPointerException();
        }
        this.type = xSSimpleType;
    }

    public XSSimpleType getWrappedXSSimpleType() {
        return this.type;
    }

    public XSObjectList getAnnotations() {
        return this.type.getAnnotations();
    }

    public boolean getBounded() {
        return this.type.getBounded();
    }

    public short getBuiltInKind() {
        return this.type.getBuiltInKind();
    }

    public short getDefinedFacets() {
        return this.type.getDefinedFacets();
    }

    public XSObjectList getFacets() {
        return this.type.getFacets();
    }

    public boolean getFinite() {
        return this.type.getFinite();
    }

    public short getFixedFacets() {
        return this.type.getFixedFacets();
    }

    public XSSimpleTypeDefinition getItemType() {
        return this.type.getItemType();
    }

    public StringList getLexicalEnumeration() {
        return this.type.getLexicalEnumeration();
    }

    public String getLexicalFacetValue(short s2) {
        return this.type.getLexicalFacetValue(s2);
    }

    public StringList getLexicalPattern() {
        return this.type.getLexicalPattern();
    }

    public XSObjectList getMemberTypes() {
        return this.type.getMemberTypes();
    }

    public XSObjectList getMultiValueFacets() {
        return this.type.getMultiValueFacets();
    }

    public boolean getNumeric() {
        return this.type.getNumeric();
    }

    public short getOrdered() {
        return this.type.getOrdered();
    }

    public XSSimpleTypeDefinition getPrimitiveType() {
        return this.type.getPrimitiveType();
    }

    public short getVariety() {
        return this.type.getVariety();
    }

    public boolean isDefinedFacet(short s2) {
        return this.type.isDefinedFacet(s2);
    }

    public boolean isFixedFacet(short s2) {
        return this.type.isFixedFacet(s2);
    }

    public boolean derivedFrom(String string, String string2, short s2) {
        return this.type.derivedFrom(string, string2, s2);
    }

    public boolean derivedFromType(XSTypeDefinition xSTypeDefinition, short s2) {
        return this.type.derivedFromType(xSTypeDefinition, s2);
    }

    public boolean getAnonymous() {
        return this.type.getAnonymous();
    }

    public XSTypeDefinition getBaseType() {
        return this.type.getBaseType();
    }

    public short getFinal() {
        return this.type.getFinal();
    }

    public short getTypeCategory() {
        return this.type.getTypeCategory();
    }

    public boolean isFinal(short s2) {
        return this.type.isFinal(s2);
    }

    public String getName() {
        return this.type.getName();
    }

    public String getNamespace() {
        return this.type.getNamespace();
    }

    public XSNamespaceItem getNamespaceItem() {
        return this.type.getNamespaceItem();
    }

    public short getType() {
        return this.type.getType();
    }

    public void applyFacets(XSFacets xSFacets, short s2, short s3, ValidationContext validationContext) throws InvalidDatatypeFacetException {
        this.type.applyFacets(xSFacets, s2, s3, validationContext);
    }

    public short getPrimitiveKind() {
        return this.type.getPrimitiveKind();
    }

    public short getWhitespace() throws DatatypeException {
        return this.type.getWhitespace();
    }

    public boolean isEqual(Object object, Object object2) {
        return this.type.isEqual(object, object2);
    }

    public boolean isIDType() {
        return this.type.isIDType();
    }

    public void validate(ValidationContext validationContext, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        this.type.validate(validationContext, validatedInfo);
    }

    public Object validate(String string, ValidationContext validationContext, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        return this.type.validate(string, validationContext, validatedInfo);
    }

    public Object validate(Object object, ValidationContext validationContext, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        return this.type.validate(object, validationContext, validatedInfo);
    }

    public String toString() {
        return this.type.toString();
    }
}

