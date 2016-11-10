/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BitArray
implements Externalizable {
    static final long serialVersionUID = -4876019880708377663L;
    private int[] _bits;
    private int _bitSize;
    private int _intSize;
    private int _mask;
    private static final int[] _masks = new int[]{Integer.MIN_VALUE, 1073741824, 536870912, 268435456, 134217728, 67108864, 33554432, 16777216, 8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072, 65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1};
    private static final boolean DEBUG_ASSERTIONS = false;
    private int _pos = Integer.MAX_VALUE;
    private int _node = 0;
    private int _int = 0;
    private int _bit = 0;
    int _first = Integer.MAX_VALUE;
    int _last = Integer.MIN_VALUE;

    public BitArray() {
        this(32);
    }

    public BitArray(int n2) {
        if (n2 < 32) {
            n2 = 32;
        }
        this._bitSize = n2;
        this._intSize = (this._bitSize >>> 5) + 1;
        this._bits = new int[this._intSize + 1];
    }

    public BitArray(int n2, int[] arrn) {
        if (n2 < 32) {
            n2 = 32;
        }
        this._bitSize = n2;
        this._intSize = (this._bitSize >>> 5) + 1;
        this._bits = arrn;
    }

    public void setMask(int n2) {
        this._mask = n2;
    }

    public int getMask() {
        return this._mask;
    }

    public final int size() {
        return this._bitSize;
    }

    public final boolean getBit(int n2) {
        return (this._bits[n2 >>> 5] & _masks[n2 % 32]) != 0;
    }

    public final int getNextBit(int n2) {
        for (int i2 = n2 >>> 5; i2 <= this._intSize; ++i2) {
            int n3 = this._bits[i2];
            if (n3 != 0) {
                for (int i3 = n2 % 32; i3 < 32; ++i3) {
                    if ((n3 & _masks[i3]) == 0) continue;
                    return (i2 << 5) + i3;
                }
            }
            n2 = 0;
        }
        return -1;
    }

    public final int getBitNumber(int n2) {
        if (n2 == this._pos) {
            return this._node;
        }
        if (n2 < this._pos) {
            this._pos = 0;
            this._bit = 0;
            this._int = 0;
        }
        while (this._int <= this._intSize) {
            int n3 = this._bits[this._int];
            if (n3 != 0) {
                while (this._bit < 32) {
                    if ((n3 & _masks[this._bit]) != 0 && ++this._pos == n2) {
                        this._node = (this._int << 5) + this._bit - 1;
                        return this._node;
                    }
                    ++this._bit;
                }
                this._bit = 0;
            }
            ++this._int;
        }
        return 0;
    }

    public final int[] data() {
        return this._bits;
    }

    public final void setBit(int n2) {
        if (n2 >= this._bitSize) {
            return;
        }
        int n3 = n2 >>> 5;
        if (n3 < this._first) {
            this._first = n3;
        }
        if (n3 > this._last) {
            this._last = n3;
        }
        int[] arrn = this._bits;
        int n4 = n3;
        arrn[n4] = arrn[n4] | _masks[n2 % 32];
    }

    public final BitArray merge(BitArray bitArray) {
        if (this._last == -1) {
            this._bits = bitArray._bits;
        } else if (bitArray._last != -1) {
            int n2;
            int n3 = this._first < bitArray._first ? this._first : bitArray._first;
            int n4 = n2 = this._last > bitArray._last ? this._last : bitArray._last;
            if (bitArray._intSize > this._intSize) {
                if (n2 > this._intSize) {
                    n2 = this._intSize;
                }
                for (int i2 = n3; i2 <= n2; ++i2) {
                    int[] arrn = bitArray._bits;
                    int n5 = i2;
                    arrn[n5] = arrn[n5] | this._bits[i2];
                }
                this._bits = bitArray._bits;
            } else {
                if (n2 > bitArray._intSize) {
                    n2 = bitArray._intSize;
                }
                for (int i3 = n3; i3 <= n2; ++i3) {
                    int[] arrn = this._bits;
                    int n6 = i3;
                    arrn[n6] = arrn[n6] | bitArray._bits[i3];
                }
            }
        }
        return this;
    }

    public final void resize(int n2) {
        if (n2 > this._bitSize) {
            this._intSize = (n2 >>> 5) + 1;
            int[] arrn = new int[this._intSize + 1];
            System.arraycopy(this._bits, 0, arrn, 0, (this._bitSize >>> 5) + 1);
            this._bits = arrn;
            this._bitSize = n2;
        }
    }

    public BitArray cloneArray() {
        return new BitArray(this._intSize, this._bits);
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeInt(this._bitSize);
        objectOutput.writeInt(this._mask);
        objectOutput.writeObject(this._bits);
        objectOutput.flush();
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this._bitSize = objectInput.readInt();
        this._intSize = (this._bitSize >>> 5) + 1;
        this._mask = objectInput.readInt();
        this._bits = (int[])objectInput.readObject();
    }
}

