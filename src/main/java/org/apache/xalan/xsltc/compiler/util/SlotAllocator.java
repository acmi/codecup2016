/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.Type;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

final class SlotAllocator {
    private int _firstAvailableSlot;
    private int _size = 8;
    private int _free = 0;
    private int[] _slotsTaken = new int[this._size];

    SlotAllocator() {
    }

    public void initialize(LocalVariableGen[] arrlocalVariableGen) {
        int n2 = arrlocalVariableGen.length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = arrlocalVariableGen[i2].getType().getSize();
            int n5 = arrlocalVariableGen[i2].getIndex();
            n3 = Math.max(n3, n5 + n4);
        }
        this._firstAvailableSlot = n3;
    }

    public int allocateSlot(Type type) {
        int n2 = type.getSize();
        int n3 = this._free;
        int n4 = this._firstAvailableSlot;
        int n5 = 0;
        if (this._free + n2 > this._size) {
            int[] arrn = new int[this._size *= 2];
            for (int i2 = 0; i2 < n3; ++i2) {
                arrn[i2] = this._slotsTaken[i2];
            }
            this._slotsTaken = arrn;
        }
        while (n5 < n3) {
            if (n4 + n2 <= this._slotsTaken[n5]) {
                for (int i3 = n3 - 1; i3 >= n5; --i3) {
                    this._slotsTaken[i3 + n2] = this._slotsTaken[i3];
                }
                break;
            }
            n4 = this._slotsTaken[n5++] + 1;
        }
        for (int i4 = 0; i4 < n2; ++i4) {
            this._slotsTaken[n5 + i4] = n4 + i4;
        }
        this._free += n2;
        return n4;
    }

    public void releaseSlot(LocalVariableGen localVariableGen) {
        int n2 = localVariableGen.getType().getSize();
        int n3 = localVariableGen.getIndex();
        int n4 = this._free;
        for (int i2 = 0; i2 < n4; ++i2) {
            if (this._slotsTaken[i2] != n3) continue;
            int n5 = i2 + n2;
            while (n5 < n4) {
                this._slotsTaken[i2++] = this._slotsTaken[n5++];
            }
            this._free -= n2;
            return;
        }
        String string = "Variable slot allocation error(size=" + n2 + ", slot=" + n3 + ", limit=" + n4 + ")";
        ErrorMsg errorMsg = new ErrorMsg("INTERNAL_ERR", string);
        throw new Error(errorMsg.toString());
    }
}

