/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.identity.Field;
import org.apache.xerces.impl.xs.identity.Selector;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSIDCDefinition;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

public abstract class IdentityConstraint
implements XSIDCDefinition {
    protected short type;
    protected final String fNamespace;
    protected final String fIdentityConstraintName;
    protected final String fElementName;
    protected Selector fSelector;
    protected int fFieldCount;
    protected Field[] fFields;
    protected XSAnnotationImpl[] fAnnotations = null;
    protected int fNumAnnotations;

    protected IdentityConstraint(String string, String string2, String string3) {
        this.fNamespace = string;
        this.fIdentityConstraintName = string2;
        this.fElementName = string3;
    }

    public String getIdentityConstraintName() {
        return this.fIdentityConstraintName;
    }

    public void setSelector(Selector selector) {
        this.fSelector = selector;
    }

    public Selector getSelector() {
        return this.fSelector;
    }

    public void addField(Field field) {
        if (this.fFields == null) {
            this.fFields = new Field[4];
        } else if (this.fFieldCount == this.fFields.length) {
            this.fFields = IdentityConstraint.resize(this.fFields, this.fFieldCount * 2);
        }
        this.fFields[this.fFieldCount++] = field;
    }

    public int getFieldCount() {
        return this.fFieldCount;
    }

    public Field getFieldAt(int n2) {
        return this.fFields[n2];
    }

    public String getElementName() {
        return this.fElementName;
    }

    public String toString() {
        String string = super.toString();
        int n2 = string.lastIndexOf(36);
        if (n2 != -1) {
            return string.substring(n2 + 1);
        }
        int n3 = string.lastIndexOf(46);
        if (n3 != -1) {
            return string.substring(n3 + 1);
        }
        return string;
    }

    public boolean equals(IdentityConstraint identityConstraint) {
        boolean bl = this.fIdentityConstraintName.equals(identityConstraint.fIdentityConstraintName);
        if (!bl) {
            return false;
        }
        bl = this.fSelector.toString().equals(identityConstraint.fSelector.toString());
        if (!bl) {
            return false;
        }
        boolean bl2 = bl = this.fFieldCount == identityConstraint.fFieldCount;
        if (!bl) {
            return false;
        }
        int n2 = 0;
        while (n2 < this.fFieldCount) {
            if (!this.fFields[n2].toString().equals(identityConstraint.fFields[n2].toString())) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    static final Field[] resize(Field[] arrfield, int n2) {
        Field[] arrfield2 = new Field[n2];
        System.arraycopy(arrfield, 0, arrfield2, 0, arrfield.length);
        return arrfield2;
    }

    public short getType() {
        return 10;
    }

    public String getName() {
        return this.fIdentityConstraintName;
    }

    public String getNamespace() {
        return this.fNamespace;
    }

    public short getCategory() {
        return this.type;
    }

    public String getSelectorStr() {
        return this.fSelector != null ? this.fSelector.toString() : null;
    }

    public StringList getFieldStrs() {
        String[] arrstring = new String[this.fFieldCount];
        int n2 = 0;
        while (n2 < this.fFieldCount) {
            arrstring[n2] = this.fFields[n2].toString();
            ++n2;
        }
        return new StringListImpl(arrstring, this.fFieldCount);
    }

    public XSIDCDefinition getRefKey() {
        return null;
    }

    public XSObjectList getAnnotations() {
        return new XSObjectListImpl(this.fAnnotations, this.fNumAnnotations);
    }

    public XSNamespaceItem getNamespaceItem() {
        return null;
    }

    public void addAnnotation(XSAnnotationImpl xSAnnotationImpl) {
        if (xSAnnotationImpl == null) {
            return;
        }
        if (this.fAnnotations == null) {
            this.fAnnotations = new XSAnnotationImpl[2];
        } else if (this.fNumAnnotations == this.fAnnotations.length) {
            XSAnnotationImpl[] arrxSAnnotationImpl = new XSAnnotationImpl[this.fNumAnnotations << 1];
            System.arraycopy(this.fAnnotations, 0, arrxSAnnotationImpl, 0, this.fNumAnnotations);
            this.fAnnotations = arrxSAnnotationImpl;
        }
        this.fAnnotations[this.fNumAnnotations++] = xSAnnotationImpl;
    }
}

