/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xpointer;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xpointer.XPointerPart;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

final class ShortHandPointer
implements XPointerPart {
    private String fShortHandPointer;
    private boolean fIsFragmentResolved = false;
    private SymbolTable fSymbolTable;
    int fMatchingChildCount = 0;

    public ShortHandPointer() {
    }

    public ShortHandPointer(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public void parseXPointer(String string) throws XNIException {
        this.fShortHandPointer = string;
        this.fIsFragmentResolved = false;
    }

    public boolean resolveXPointer(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations, int n2) throws XNIException {
        if (this.fMatchingChildCount == 0) {
            this.fIsFragmentResolved = false;
        }
        if (n2 == 0) {
            if (this.fMatchingChildCount == 0) {
                this.fIsFragmentResolved = this.hasMatchingIdentifier(qName, xMLAttributes, augmentations, n2);
            }
            if (this.fIsFragmentResolved) {
                ++this.fMatchingChildCount;
            }
        } else if (n2 == 2) {
            if (this.fMatchingChildCount == 0) {
                this.fIsFragmentResolved = this.hasMatchingIdentifier(qName, xMLAttributes, augmentations, n2);
            }
        } else if (this.fIsFragmentResolved) {
            --this.fMatchingChildCount;
        }
        return this.fIsFragmentResolved;
    }

    private boolean hasMatchingIdentifier(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations, int n2) throws XNIException {
        String string = null;
        if (xMLAttributes != null) {
            int n3 = 0;
            while (n3 < xMLAttributes.getLength()) {
                string = this.getSchemaDeterminedID(xMLAttributes, n3);
                if (string != null || (string = this.getChildrenSchemaDeterminedID(xMLAttributes, n3)) != null || (string = this.getDTDDeterminedID(xMLAttributes, n3)) != null) break;
                ++n3;
            }
        }
        if (string != null && string.equals(this.fShortHandPointer)) {
            return true;
        }
        return false;
    }

    public String getDTDDeterminedID(XMLAttributes xMLAttributes, int n2) throws XNIException {
        if (xMLAttributes.getType(n2).equals("ID")) {
            return xMLAttributes.getValue(n2);
        }
        return null;
    }

    public String getSchemaDeterminedID(XMLAttributes xMLAttributes, int n2) throws XNIException {
        Augmentations augmentations = xMLAttributes.getAugmentations(n2);
        AttributePSVI attributePSVI = (AttributePSVI)augmentations.getItem("ATTRIBUTE_PSVI");
        if (attributePSVI != null) {
            XSTypeDefinition xSTypeDefinition = attributePSVI.getMemberTypeDefinition();
            if (xSTypeDefinition != null) {
                xSTypeDefinition = attributePSVI.getTypeDefinition();
            }
            if (xSTypeDefinition != null && ((XSSimpleType)xSTypeDefinition).isIDType()) {
                return attributePSVI.getSchemaNormalizedValue();
            }
        }
        return null;
    }

    public String getChildrenSchemaDeterminedID(XMLAttributes xMLAttributes, int n2) throws XNIException {
        return null;
    }

    public boolean isFragmentResolved() {
        return this.fIsFragmentResolved;
    }

    public boolean isChildFragmentResolved() {
        return this.fIsFragmentResolved && this.fMatchingChildCount > 0;
    }

    public String getSchemeName() {
        return this.fShortHandPointer;
    }

    public String getSchemeData() {
        return null;
    }

    public void setSchemeName(String string) {
        this.fShortHandPointer = string;
    }

    public void setSchemeData(String string) {
    }
}

