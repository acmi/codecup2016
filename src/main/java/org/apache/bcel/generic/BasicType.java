/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Type;

public final class BasicType
extends Type {
    BasicType(byte by) {
        super(by, Constants.SHORT_TYPE_NAMES[by]);
        if (by < 4 || by > 12) {
            throw new ClassGenException("Invalid type: " + by);
        }
    }

    public static final BasicType getType(byte by) {
        switch (by) {
            case 12: {
                return Type.VOID;
            }
            case 4: {
                return Type.BOOLEAN;
            }
            case 8: {
                return Type.BYTE;
            }
            case 9: {
                return Type.SHORT;
            }
            case 5: {
                return Type.CHAR;
            }
            case 10: {
                return Type.INT;
            }
            case 11: {
                return Type.LONG;
            }
            case 7: {
                return Type.DOUBLE;
            }
            case 6: {
                return Type.FLOAT;
            }
        }
        throw new ClassGenException("Invalid type: " + by);
    }

    public boolean equals(Object object) {
        return object instanceof BasicType ? ((BasicType)object).type == this.type : false;
    }
}

