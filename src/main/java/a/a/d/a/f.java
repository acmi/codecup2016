/*
 * Decompiled with CFR 0_119.
 */
package a.a.d.a;

import a.a.a.b;
import a.a.b.e;
import a.a.d.a.g;
import a.a.d.a.h;
import a.a.d.c;
import a.a.e.d;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class f<V>
extends a.a.a.a.e
implements c<V>,
Externalizable {
    private final d<V> n;
    protected transient V[] k;
    protected long m;

    public f() {
        this.n = new g(this);
    }

    public f(int n2, float f2, long l2) {
        super(n2, f2);
        this.n = new g(this);
        this.m = l2;
    }

    @Override
    protected int c(int n2) {
        int n3 = super.c(n2);
        this.k = new Object[n3];
        return n3;
    }

    @Override
    protected void d(int n2) {
        int n3 = this.h.length;
        long[] arrl = this.h;
        V[] arrV = this.k;
        byte[] arrby = this.l;
        this.h = new long[n2];
        this.k = new Object[n2];
        this.l = new byte[n2];
        int n4 = n3;
        while (n4-- > 0) {
            if (arrby[n4] != 1) continue;
            long l2 = arrl[n4];
            int n5 = this.c(l2);
            this.k[n5] = arrV[n4];
        }
    }

    @Override
    public boolean b_(long l2) {
        return this.a(l2);
    }

    @Override
    public V b(long l2) {
        int n2 = this.c_(l2);
        return n2 < 0 ? null : (V)this.k[n2];
    }

    public V a(long l2, V v2) {
        int n2 = this.c(l2);
        return this.a(v2, n2);
    }

    private V a(V v2, int n2) {
        V v3 = null;
        boolean bl = true;
        if (n2 < 0) {
            n2 = - n2 - 1;
            v3 = this.k[n2];
            bl = false;
        }
        this.k[n2] = v2;
        if (bl) {
            this.b(this.j);
        }
        return v3;
    }

    @Override
    protected void b(int n2) {
        this.k[n2] = null;
        super.b(n2);
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(this.h, 0, this.h.length, this.m);
        Arrays.fill(this.l, 0, this.l.length, 0);
        Arrays.fill(this.k, 0, this.k.length, null);
    }

    public e<V> d() {
        return new a(this);
    }

    public boolean a(d<? super V> d2) {
        byte[] arrby = this.l;
        long[] arrl = this.h;
        V[] arrV = this.k;
        int n2 = arrl.length;
        while (n2-- > 0) {
            if (arrby[n2] != 1 || d2.a(arrl[n2], arrV[n2])) continue;
            return false;
        }
        return true;
    }

    public boolean equals(Object object) {
        if (!(object instanceof c)) {
            return false;
        }
        c c2 = (c)object;
        if (c2.size() != this.size()) {
            return false;
        }
        try {
            e<V> e2 = this.d();
            while (e2.hasNext()) {
                e2.a();
                long l2 = e2.b();
                V v2 = e2.c();
                if (!(v2 == null ? c2.b(l2) != null || !c2.b_(l2) : !v2.equals(c2.b(l2)))) continue;
                return false;
            }
        }
        catch (ClassCastException classCastException) {
            // empty catch block
        }
        return true;
    }

    public int hashCode() {
        int n2 = 0;
        V[] arrV = this.k;
        byte[] arrby = this.l;
        int n3 = arrV.length;
        while (n3-- > 0) {
            if (arrby[n3] != 1) continue;
            n2 += b.a(this.h[n3]) ^ (arrV[n3] == null ? 0 : arrV[n3].hashCode());
        }
        return n2;
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(0);
        super.writeExternal(objectOutput);
        objectOutput.writeLong(this.m);
        objectOutput.writeInt(this.a);
        int n2 = this.l.length;
        while (n2-- > 0) {
            if (this.l[n2] != 1) continue;
            objectOutput.writeLong(this.h[n2]);
            objectOutput.writeObject(this.k[n2]);
        }
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        objectInput.readByte();
        super.readExternal(objectInput);
        this.m = objectInput.readLong();
        int n2 = objectInput.readInt();
        this.c(n2);
        while (n2-- > 0) {
            long l2 = objectInput.readLong();
            Object object = objectInput.readObject();
            this.a(l2, object);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        this.a(new h(this, stringBuilder));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    class a<V>
    extends a.a.a.a.c
    implements e<V> {
        private final f<V> e;

        public a(f<V> f3) {
            super(f3);
            this.e = f3;
        }

        @Override
        public void a() {
            this.b_();
        }

        @Override
        public long b() {
            return this.e.h[this.c];
        }

        @Override
        public V c() {
            return this.e.k[this.c];
        }
    }

}

