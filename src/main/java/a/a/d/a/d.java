/*
 * Decompiled with CFR 0_119.
 */
package a.a.d.a;

import a.a.a.a.f;
import a.a.d.a.e;
import a.a.d.b;
import a.a.e.c;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class d
extends f
implements b,
Externalizable {
    protected transient int[] m;

    public d() {
    }

    public d(int n2, float f2, long l2, int n3) {
        super(n2, f2, l2, n3);
    }

    protected int c(int n2) {
        int n3 = super.c(n2);
        this.m = new int[n3];
        return n3;
    }

    protected void d(int n2) {
        int n3 = this.h.length;
        long[] arrl = this.h;
        int[] arrn = this.m;
        byte[] arrby = this.l;
        this.h = new long[n2];
        this.m = new int[n2];
        this.l = new byte[n2];
        int n4 = n3;
        while (n4-- > 0) {
            if (arrby[n4] != 1) continue;
            long l2 = arrl[n4];
            int n5 = this.b(l2);
            this.m[n5] = arrn[n4];
        }
    }

    public int a(long l2, int n2) {
        int n3 = this.b(l2);
        return this.a(l2, n2, n3);
    }

    private int a(long l2, int n2, int n3) {
        int n4 = this.j;
        boolean bl = true;
        if (n3 < 0) {
            n3 = - n3 - 1;
            n4 = this.m[n3];
            bl = false;
        }
        this.m[n3] = n2;
        if (bl) {
            this.b(this.k);
        }
        return n4;
    }

    public int a(long l2) {
        int n2 = this.a_(l2);
        return n2 < 0 ? this.j : this.m[n2];
    }

    public void clear() {
        super.clear();
        Arrays.fill(this.h, 0, this.h.length, this.i);
        Arrays.fill(this.m, 0, this.m.length, this.j);
        Arrays.fill(this.l, 0, this.l.length, 0);
    }

    public boolean isEmpty() {
        return 0 == this.a;
    }

    protected void b(int n2) {
        this.m[n2] = this.j;
        super.b(n2);
    }

    public boolean a(c c2) {
        byte[] arrby = this.l;
        long[] arrl = this.h;
        int[] arrn = this.m;
        int n2 = arrl.length;
        while (n2-- > 0) {
            if (arrby[n2] != 1 || c2.a(arrl[n2], arrn[n2])) continue;
            return false;
        }
        return true;
    }

    public boolean equals(Object object) {
        if (!(object instanceof b)) {
            return false;
        }
        b b2 = (b)object;
        if (b2.size() != this.size()) {
            return false;
        }
        int[] arrn = this.m;
        byte[] arrby = this.l;
        int n2 = this.d();
        int n3 = b2.d();
        int n4 = arrn.length;
        while (n4-- > 0) {
            int n5;
            int n6;
            long l2;
            if (arrby[n4] != 1 || (n5 = arrn[n4]) == (n6 = b2.a(l2 = this.h[n4])) || n5 == n2 || n6 == n3) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        int n2 = 0;
        byte[] arrby = this.l;
        int n3 = this.m.length;
        while (n3-- > 0) {
            if (arrby[n3] != 1) continue;
            n2 += a.a.a.b.a(this.h[n3]) ^ a.a.a.b.a(this.m[n3]);
        }
        return n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        this.a(new e(this, stringBuilder));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(0);
        super.writeExternal(objectOutput);
        objectOutput.writeInt(this.a);
        int n2 = this.l.length;
        while (n2-- > 0) {
            if (this.l[n2] != 1) continue;
            objectOutput.writeLong(this.h[n2]);
            objectOutput.writeInt(this.m[n2]);
        }
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        objectInput.readByte();
        super.readExternal(objectInput);
        int n2 = objectInput.readInt();
        this.c(n2);
        while (n2-- > 0) {
            long l2 = objectInput.readLong();
            int n3 = objectInput.readInt();
            this.a(l2, n3);
        }
    }
}

