/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.ReferenceType;

public final class ObjectType
extends ReferenceType {
    private String class_name;

    public ObjectType(String string) {
        super(14, "L" + string.replace('.', '/') + ";");
        this.class_name = string.replace('/', '.');
    }

    public String getClassName() {
        return this.class_name;
    }

    public int hashCode() {
        return this.class_name.hashCode();
    }

    public boolean equals(Object object) {
        return object instanceof ObjectType ? ((ObjectType)object).class_name.equals(this.class_name) : false;
    }
}

