/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd.models;

public class CMStateSet {
    int fBitCount;
    int fByteCount;
    int fBits1;
    int fBits2;
    byte[] fByteArray;

    public CMStateSet(int n2) {
        this.fBitCount = n2;
        if (this.fBitCount < 0) {
            throw new RuntimeException("ImplementationMessages.VAL_CMSI");
        }
        if (this.fBitCount > 64) {
            this.fByteCount = this.fBitCount / 8;
            if (this.fBitCount % 8 != 0) {
                ++this.fByteCount;
            }
            this.fByteArray = new byte[this.fByteCount];
        }
        this.zeroBits();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append('{');
            int n2 = 0;
            while (n2 < this.fBitCount) {
                if (this.getBit(n2)) {
                    stringBuffer.append(' ').append(n2);
                }
                ++n2;
            }
            stringBuffer.append(" }");
        }
        catch (RuntimeException runtimeException) {
            // empty catch block
        }
        return stringBuffer.toString();
    }

    public final void intersection(CMStateSet cMStateSet) {
        if (this.fBitCount < 65) {
            this.fBits1 &= cMStateSet.fBits1;
            this.fBits2 &= cMStateSet.fBits2;
        } else {
            int n2 = this.fByteCount - 1;
            while (n2 >= 0) {
                byte[] arrby = this.fByteArray;
                int n3 = n2;
                arrby[n3] = (byte)(arrby[n3] & cMStateSet.fByteArray[n2]);
                --n2;
            }
        }
    }

    public final boolean getBit(int n2) {
        if (n2 >= this.fBitCount) {
            throw new RuntimeException("ImplementationMessages.VAL_CMSI");
        }
        if (this.fBitCount < 65) {
            int n3 = 1 << n2 % 32;
            if (n2 < 32) {
                return (this.fBits1 & n3) != 0;
            }
            return (this.fBits2 & n3) != 0;
        }
        int n4 = n2 >> 3;
        byte by = (byte)(1 << n2 % 8);
        return (this.fByteArray[n4] & by) != 0;
    }

    public final boolean isEmpty() {
        if (this.fBitCount < 65) {
            return this.fBits1 == 0 && this.fBits2 == 0;
        }
        int n2 = this.fByteCount - 1;
        while (n2 >= 0) {
            if (this.fByteArray[n2] != 0) {
                return false;
            }
            --n2;
        }
        return true;
    }

    final boolean isSameSet(CMStateSet cMStateSet) {
        if (this.fBitCount != cMStateSet.fBitCount) {
            return false;
        }
        if (this.fBitCount < 65) {
            return this.fBits1 == cMStateSet.fBits1 && this.fBits2 == cMStateSet.fBits2;
        }
        int n2 = this.fByteCount - 1;
        while (n2 >= 0) {
            if (this.fByteArray[n2] != cMStateSet.fByteArray[n2]) {
                return false;
            }
            --n2;
        }
        return true;
    }

    public final void union(CMStateSet cMStateSet) {
        if (this.fBitCount < 65) {
            this.fBits1 |= cMStateSet.fBits1;
            this.fBits2 |= cMStateSet.fBits2;
        } else {
            int n2 = this.fByteCount - 1;
            while (n2 >= 0) {
                byte[] arrby = this.fByteArray;
                int n3 = n2;
                arrby[n3] = (byte)(arrby[n3] | cMStateSet.fByteArray[n2]);
                --n2;
            }
        }
    }

    public final void setBit(int n2) {
        if (n2 >= this.fBitCount) {
            throw new RuntimeException("ImplementationMessages.VAL_CMSI");
        }
        if (this.fBitCount < 65) {
            int n3 = 1 << n2 % 32;
            if (n2 < 32) {
                this.fBits1 &= ~ n3;
                this.fBits1 |= n3;
            } else {
                this.fBits2 &= ~ n3;
                this.fBits2 |= n3;
            }
        } else {
            byte by = (byte)(1 << n2 % 8);
            int n4 = n2 >> 3;
            byte[] arrby = this.fByteArray;
            int n5 = n4;
            arrby[n5] = (byte)(arrby[n5] & ~ by);
            byte[] arrby2 = this.fByteArray;
            int n6 = n4;
            arrby2[n6] = (byte)(arrby2[n6] | by);
        }
    }

    public final void setTo(CMStateSet cMStateSet) {
        if (this.fBitCount != cMStateSet.fBitCount) {
            throw new RuntimeException("ImplementationMessages.VAL_CMSI");
        }
        if (this.fBitCount < 65) {
            this.fBits1 = cMStateSet.fBits1;
            this.fBits2 = cMStateSet.fBits2;
        } else {
            int n2 = this.fByteCount - 1;
            while (n2 >= 0) {
                this.fByteArray[n2] = cMStateSet.fByteArray[n2];
                --n2;
            }
        }
    }

    public final void zeroBits() {
        if (this.fBitCount < 65) {
            this.fBits1 = 0;
            this.fBits2 = 0;
        } else {
            int n2 = this.fByteCount - 1;
            while (n2 >= 0) {
                this.fByteArray[n2] = 0;
                --n2;
            }
        }
    }

    public boolean equals(Object object) {
        if (!(object instanceof CMStateSet)) {
            return false;
        }
        return this.isSameSet((CMStateSet)object);
    }

    public int hashCode() {
        if (this.fBitCount < 65) {
            return this.fBits1 + this.fBits2 * 31;
        }
        int n2 = 0;
        int n3 = this.fByteCount - 1;
        while (n3 >= 0) {
            n2 = this.fByteArray[n3] + n2 * 31;
            --n3;
        }
        return n2;
    }
}

