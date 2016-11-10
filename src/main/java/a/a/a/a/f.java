/*
 * Decompiled with CFR 0_119.
 */
package a.a.a.a;

import a.a.a.a.h;
import a.a.a.b;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class f
extends h {
    public transient long[] h;
    protected long i;
    protected int j;
    protected boolean k;

    public f() {
        this.i = 0;
        this.j = 0;
    }

    public f(int n2, float f2, long l2, int n3) {
        super(n2, f2);
        this.i = l2;
        this.j = n3;
    }

    public int d() {
        return this.j;
    }

    protected int c(int n2) {
        int n3 = super.c(n2);
        this.h = new long[n3];
        return n3;
    }

    protected void b(int n2) {
        this.h[n2] = this.i;
        super.b(n2);
    }

    protected int a_(long l2) {
        byte[] arrby = this.l;
        long[] arrl = this.h;
        int n2 = arrby.length;
        int n3 = b.a(l2) & Integer.MAX_VALUE;
        int n4 = n3 % n2;
        byte by = arrby[n4];
        if (by == 0) {
            return -1;
        }
        if (by == 1 && arrl[n4] == l2) {
            return n4;
        }
        return this.a(l2, n4, n3, by);
    }

    int a(long l2, int n2, int n3, byte by) {
        int n4 = this.h.length;
        int n5 = 1 + n3 % (n4 - 2);
        int n6 = n2;
        do {
            if ((n2 -= n5) < 0) {
                n2 += n4;
            }
            if ((by = this.l[n2]) == 0) {
                return -1;
            }
            if (l2 != this.h[n2] || by == 2) continue;
            return n2;
        } while (n2 != n6);
        return -1;
    }

    protected int b(long l2) {
        int n2 = b.a(l2) & Integer.MAX_VALUE;
        int n3 = n2 % this.l.length;
        byte by = this.l[n3];
        this.k = false;
        if (by == 0) {
            this.k = true;
            this.a(n3, l2);
            return n3;
        }
        if (by == 1 && this.h[n3] == l2) {
            return - n3 - 1;
        }
        return this.b(l2, n3, n2, by);
    }

    int b(long l2, int n2, int n3, byte by) {
        int n4 = this.h.length;
        int n5 = 1 + n3 % (n4 - 2);
        int n6 = n2;
        int n7 = -1;
        do {
            if (by == 2 && n7 == -1) {
                n7 = n2;
            }
            if ((n2 -= n5) < 0) {
                n2 += n4;
            }
            if ((by = this.l[n2]) == 0) {
                if (n7 != -1) {
                    this.a(n7, l2);
                    return n7;
                }
                this.k = true;
                this.a(n2, l2);
                return n2;
            }
            if (by != 1 || this.h[n2] != l2) continue;
            return - n2 - 1;
        } while (n2 != n6);
        if (n7 != -1) {
            this.a(n7, l2);
            return n7;
        }
        throw new IllegalStateException("No free or removed slots available. Key set full?!!");
    }

    void a(int n2, long l2) {
        this.h[n2] = l2;
        this.l[n2] = 1;
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(0);
        super.writeExternal(objectOutput);
        objectOutput.writeLong(this.i);
        objectOutput.writeInt(this.j);
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        objectInput.readByte();
        super.readExternal(objectInput);
        this.i = objectInput.readLong();
        this.j = objectInput.readInt();
    }
}

