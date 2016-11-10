/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.$ByteVector;
import com.google.inject.internal.asm.$Edge;
import com.google.inject.internal.asm.$Frame;
import com.google.inject.internal.asm.$MethodWriter;

public class $Label {
    public Object info;
    int a;
    int b;
    int c;
    private int d;
    private int[] e;
    int f;
    int g;
    $Frame h;
    $Label i;
    $Edge j;
    $Label k;

    public int getOffset() {
        if ((this.a & 2) == 0) {
            throw new IllegalStateException("Label offset position has not been resolved yet");
        }
        return this.c;
    }

    void a($MethodWriter $MethodWriter, $ByteVector $ByteVector, int n2, boolean bl) {
        if ((this.a & 2) == 0) {
            if (bl) {
                this.a(-1 - n2, $ByteVector.b);
                $ByteVector.putInt(-1);
            } else {
                this.a(n2, $ByteVector.b);
                $ByteVector.putShort(-1);
            }
        } else if (bl) {
            $ByteVector.putInt(this.c - n2);
        } else {
            $ByteVector.putShort(this.c - n2);
        }
    }

    private void a(int n2, int n3) {
        if (this.e == null) {
            this.e = new int[6];
        }
        if (this.d >= this.e.length) {
            int[] arrn = new int[this.e.length + 6];
            System.arraycopy(this.e, 0, arrn, 0, this.e.length);
            this.e = arrn;
        }
        this.e[this.d++] = n2;
        this.e[this.d++] = n3;
    }

    boolean a($MethodWriter $MethodWriter, int n2, byte[] arrby) {
        boolean bl = false;
        this.a |= 2;
        this.c = n2;
        int n3 = 0;
        while (n3 < this.d) {
            int n4;
            int n5 = this.e[n3++];
            int n6 = this.e[n3++];
            if (n5 >= 0) {
                n4 = n2 - n5;
                if (n4 < -32768 || n4 > 32767) {
                    int n7 = arrby[n6 - 1] & 255;
                    arrby[n6 - 1] = n7 <= 168 ? (byte)(n7 + 49) : (byte)(n7 + 20);
                    bl = true;
                }
                arrby[n6++] = (byte)(n4 >>> 8);
                arrby[n6] = (byte)n4;
                continue;
            }
            n4 = n2 + n5 + 1;
            arrby[n6++] = (byte)(n4 >>> 24);
            arrby[n6++] = (byte)(n4 >>> 16);
            arrby[n6++] = (byte)(n4 >>> 8);
            arrby[n6] = (byte)n4;
        }
        return bl;
    }

    $Label a() {
        return this.h == null ? this : this.h.b;
    }

    boolean a(long l2) {
        if ((this.a & 1024) != 0) {
            return (this.e[(int)(l2 >>> 32)] & (int)l2) != 0;
        }
        return false;
    }

    boolean a($Label $Label) {
        if ((this.a & 1024) == 0 || ($Label.a & 1024) == 0) {
            return false;
        }
        for (int i2 = 0; i2 < this.e.length; ++i2) {
            if ((this.e[i2] & $Label.e[i2]) == 0) continue;
            return true;
        }
        return false;
    }

    void a(long l2, int n2) {
        if ((this.a & 1024) == 0) {
            this.a |= 1024;
            this.e = new int[n2 / 32 + 1];
        }
        int[] arrn = this.e;
        int n3 = (int)(l2 >>> 32);
        arrn[n3] = arrn[n3] | (int)l2;
    }

    void b($Label $Label, long l2, int n2) {
        $Label $Label2 = this;
        while ($Label2 != null) {
            $Edge $Edge;
            $Label $Label3 = $Label2;
            $Label2 = $Label3.k;
            $Label3.k = null;
            if ($Label != null) {
                if (($Label3.a & 2048) != 0) continue;
                $Label3.a |= 2048;
                if (($Label3.a & 256) != 0 && !$Label3.a($Label)) {
                    $Edge = new $Edge();
                    $Edge.a = $Label3.f;
                    $Edge.b = $Label.j.b;
                    $Edge.c = $Label3.j;
                    $Label3.j = $Edge;
                }
            } else {
                if ($Label3.a(l2)) continue;
                $Label3.a(l2, n2);
            }
            $Edge = $Label3.j;
            while ($Edge != null) {
                if ((($Label3.a & 128) == 0 || $Edge != $Label3.j.c) && $Edge.b.k == null) {
                    $Edge.b.k = $Label2;
                    $Label2 = $Edge.b;
                }
                $Edge = $Edge.c;
            }
        }
    }

    public String toString() {
        return "L" + System.identityHashCode(this);
    }
}

