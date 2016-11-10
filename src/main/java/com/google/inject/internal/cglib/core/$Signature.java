/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$Type;

public class $Signature {
    private String name;
    private String desc;

    public $Signature(String string, String string2) {
        if (string.indexOf(40) >= 0) {
            throw new IllegalArgumentException(new StringBuilder(18 + String.valueOf(string).length()).append("Name '").append(string).append("' is invalid").toString());
        }
        this.name = string;
        this.desc = string2;
    }

    public $Signature(String string, $Type $Type, $Type[] arr$Type) {
        this(string, $Type.getMethodDescriptor($Type, arr$Type));
    }

    public String getName() {
        return this.name;
    }

    public String getDescriptor() {
        return this.desc;
    }

    public $Type getReturnType() {
        return $Type.getReturnType(this.desc);
    }

    public $Type[] getArgumentTypes() {
        return $Type.getArgumentTypes(this.desc);
    }

    public String toString() {
        String string = String.valueOf(this.name);
        String string2 = String.valueOf(this.desc);
        return string2.length() != 0 ? string.concat(string2) : new String(string);
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof $Signature)) {
            return false;
        }
        $Signature $Signature = ($Signature)object;
        return this.name.equals($Signature.name) && this.desc.equals($Signature.desc);
    }

    public int hashCode() {
        return this.name.hashCode() ^ this.desc.hashCode();
    }
}

