/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;

public final class ArrayType
extends ReferenceType {
    private int dimensions;
    private Type basic_type;

    public ArrayType(Type type, int n2) {
        super(13, "<dummy>");
        if (n2 < 1 || n2 > 255) {
            throw new ClassGenException("Invalid number of dimensions: " + n2);
        }
        switch (type.getType()) {
            case 13: {
                ArrayType arrayType = (ArrayType)type;
                this.dimensions = n2 + arrayType.dimensions;
                this.basic_type = arrayType.basic_type;
                break;
            }
            case 12: {
                throw new ClassGenException("Invalid type: void[]");
            }
            default: {
                this.dimensions = n2;
                this.basic_type = type;
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        int n3 = 0;
        while (n3 < this.dimensions) {
            stringBuffer.append('[');
            ++n3;
        }
        stringBuffer.append(this.basic_type.getSignature());
        this.signature = stringBuffer.toString();
    }

    public boolean equals(Object object) {
        if (object instanceof ArrayType) {
            ArrayType arrayType = (ArrayType)object;
            return arrayType.dimensions == this.dimensions && arrayType.basic_type.equals(this.basic_type);
        }
        return false;
    }
}

