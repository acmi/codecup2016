/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.dtd.XMLSimpleType;
import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.xni.QName;

public class XMLElementDecl {
    public static final short TYPE_ANY = 0;
    public static final short TYPE_EMPTY = 1;
    public static final short TYPE_MIXED = 2;
    public static final short TYPE_CHILDREN = 3;
    public static final short TYPE_SIMPLE = 4;
    public final QName name = new QName();
    public int scope = -1;
    public short type = -1;
    public ContentModelValidator contentModelValidator;
    public final XMLSimpleType simpleType = new XMLSimpleType();

    public void setValues(QName qName, int n2, short s2, ContentModelValidator contentModelValidator, XMLSimpleType xMLSimpleType) {
        this.name.setValues(qName);
        this.scope = n2;
        this.type = s2;
        this.contentModelValidator = contentModelValidator;
        this.simpleType.setValues(xMLSimpleType);
    }

    public void clear() {
        this.name.clear();
        this.type = -1;
        this.scope = -1;
        this.contentModelValidator = null;
        this.simpleType.clear();
    }
}

