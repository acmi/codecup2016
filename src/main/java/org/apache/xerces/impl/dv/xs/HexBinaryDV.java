/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.util.ByteListImpl;
import org.apache.xerces.impl.dv.util.HexBin;
import org.apache.xerces.impl.dv.xs.TypeValidator;

public class HexBinaryDV
extends TypeValidator {
    public short getAllowedFacets() {
        return 2079;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        byte[] arrby = HexBin.decode(string);
        if (arrby == null) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "hexBinary"});
        }
        return new XHex(arrby);
    }

    public int getDataLength(Object object) {
        return ((XHex)object).getLength();
    }

    private static final class XHex
    extends ByteListImpl {
        public XHex(byte[] arrby) {
            super(arrby);
        }

        public synchronized String toString() {
            if (this.canonical == null) {
                this.canonical = HexBin.encode(this.data);
            }
            return this.canonical;
        }

        public boolean equals(Object object) {
            if (!(object instanceof XHex)) {
                return false;
            }
            int n2 = this.data.length;
            byte[] arrby = ((XHex)object).data;
            if (n2 != arrby.length) {
                return false;
            }
            int n3 = 0;
            while (n3 < n2) {
                if (this.data[n3] != arrby[n3]) {
                    return false;
                }
                ++n3;
            }
            return true;
        }

        public int hashCode() {
            int n2 = 0;
            int n3 = 0;
            while (n3 < this.data.length) {
                n2 = n2 * 37 + (this.data[n3] & 255);
                ++n3;
            }
            return n2;
        }
    }

}

