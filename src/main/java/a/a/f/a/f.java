/*
 * Decompiled with CFR 0_119.
 */
package a.a.f.a;

import a.a.a.a.e;
import a.a.a.b;
import a.a.f.a;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class f
extends e
implements a,
Externalizable {
    public f() {
    }

    public f(int n2) {
        super(n2);
    }

    public boolean b(long l2) {
        int n2 = this.c(l2);
        if (n2 < 0) {
            return false;
        }
        this.b(this.j);
        return true;
    }

    public void clear() {
        super.clear();
        long[] arrl = this.h;
        byte[] arrby = this.l;
        int n2 = arrl.length;
        while (n2-- > 0) {
            arrl[n2] = this.i;
            arrby[n2] = 0;
        }
    }

    protected void d(int n2) {
        int n3 = this.h.length;
        long[] arrl = this.h;
        byte[] arrby = this.l;
        this.h = new long[n2];
        this.l = new byte[n2];
        int n4 = n3;
        while (n4-- > 0) {
            if (arrby[n4] != 1) continue;
            long l2 = arrl[n4];
            int n5 = this.c(l2);
        }
    }

    public boolean equals(Object object) {
        if (!(object instanceof a)) {
            return false;
        }
        a a2 = (a)object;
        if (a2.size() != this.size()) {
            return false;
        }
        int n2 = this.l.length;
        while (n2-- > 0) {
            if (this.l[n2] != 1 || a2.a(this.h[n2])) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        int n2 = 0;
        int n3 = this.l.length;
        while (n3-- > 0) {
            if (this.l[n3] != 1) continue;
            n2 += b.a(this.h[n3]);
        }
        return n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.a * 2 + 2);
        stringBuilder.append("{");
        int n2 = this.l.length;
        int n3 = 1;
        while (n2-- > 0) {
            if (this.l[n2] != 1) continue;
            stringBuilder.append(this.h[n2]);
            if (n3++ >= this.a) continue;
            stringBuilder.append(",");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(1);
        super.writeExternal(objectOutput);
        objectOutput.writeInt(this.a);
        objectOutput.writeFloat(this.c);
        objectOutput.writeLong(this.i);
        int n2 = this.l.length;
        while (n2-- > 0) {
            if (this.l[n2] != 1) continue;
            objectOutput.writeLong(this.h[n2]);
        }
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        byte by = objectInput.readByte();
        super.readExternal(objectInput);
        int n2 = objectInput.readInt();
        if (by >= 1) {
            this.c = objectInput.readFloat();
            this.i = objectInput.readLong();
            if (this.i != 0) {
                Arrays.fill(this.h, this.i);
            }
        }
        this.c(n2);
        while (n2-- > 0) {
            long l2 = objectInput.readLong();
            this.b(l2);
        }
    }
}

