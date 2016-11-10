/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.xs.ShortList;

public class ValidatedInfo {
    public String normalizedValue;
    public Object actualValue;
    public short actualValueType;
    public XSSimpleType memberType;
    public XSSimpleType[] memberTypes;
    public ShortList itemValueTypes;

    public void reset() {
        this.normalizedValue = null;
        this.actualValue = null;
        this.actualValueType = 45;
        this.memberType = null;
        this.memberTypes = null;
        this.itemValueTypes = null;
    }

    public String stringValue() {
        if (this.actualValue == null) {
            return this.normalizedValue;
        }
        return this.actualValue.toString();
    }

    public static boolean isComparable(ValidatedInfo validatedInfo, ValidatedInfo validatedInfo2) {
        short s2;
        short s3 = ValidatedInfo.convertToPrimitiveKind(validatedInfo.actualValueType);
        if (s3 != (s2 = ValidatedInfo.convertToPrimitiveKind(validatedInfo2.actualValueType))) {
            return s3 == 1 && s2 == 2 || s3 == 2 && s2 == 1;
        }
        if (s3 == 44 || s3 == 43) {
            int n2;
            ShortList shortList = validatedInfo.itemValueTypes;
            ShortList shortList2 = validatedInfo2.itemValueTypes;
            int n3 = shortList != null ? shortList.getLength() : 0;
            int n4 = n2 = shortList2 != null ? shortList2.getLength() : 0;
            if (n3 != n2) {
                return false;
            }
            int n5 = 0;
            while (n5 < n3) {
                short s4;
                short s5 = ValidatedInfo.convertToPrimitiveKind(shortList.item(n5));
                if (!(s5 == (s4 = ValidatedInfo.convertToPrimitiveKind(shortList2.item(n5))) || s5 == 1 && s4 == 2 || s5 == 2 && s4 == 1)) {
                    return false;
                }
                ++n5;
            }
        }
        return true;
    }

    private static short convertToPrimitiveKind(short s2) {
        if (s2 <= 20) {
            return s2;
        }
        if (s2 <= 29) {
            return 2;
        }
        if (s2 <= 42) {
            return 4;
        }
        return s2;
    }
}

