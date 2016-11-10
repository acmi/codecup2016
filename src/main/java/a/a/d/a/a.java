/*
 * Decompiled with CFR 0_119.
 */
package a.a.d.a;

import a.a.a.a.d;
import a.a.a.a.h;
import a.a.d.a.b;
import a.a.d.a.c;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class a<V>
extends d
implements a.a.d.a<V>,
Externalizable {
    private final a.a.e.a<V> n;
    protected transient V[] k;
    protected int m;

    public a() {
        this.n = new b(this);
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
        int[] arrn = this.h;
        V[] arrV = this.k;
        byte[] arrby = this.l;
        this.h = new int[n2];
        this.k = new Object[n2];
        this.l = new byte[n2];
        int n4 = n3;
        while (n4-- > 0) {
            if (arrby[n4] != 1) continue;
            int n5 = arrn[n4];
            int n6 = this.i(n5);
            this.k[n6] = arrV[n4];
        }
    }

    @Override
    public boolean a(int n2) {
        return this.g(n2);
    }

    @Override
    public V a_(int n2) {
        int n3 = this.h(n2);
        return n3 < 0 ? null : (V)this.k[n3];
    }

    public V a(int n2, V v2) {
        int n3 = this.i(n2);
        return this.a(v2, n3);
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

    public a.a.b.c<V> d() {
        return new a(this);
    }

    public boolean a(a.a.e.a<? super V> a2) {
        byte[] arrby = this.l;
        int[] arrn = this.h;
        V[] arrV = this.k;
        int n2 = arrn.length;
        while (n2-- > 0) {
            if (arrby[n2] != 1 || a2.a(arrn[n2], arrV[n2])) continue;
            return false;
        }
        return true;
    }

    public boolean equals(Object object) {
        if (!(object instanceof a.a.d.a)) {
            return false;
        }
        a.a.d.a a2 = (a.a.d.a)object;
        if (a2.size() != this.size()) {
            return false;
        }
        try {
            a.a.b.c<V> c2 = this.d();
            while (c2.hasNext()) {
                c2.a();
                int n2 = c2.b();
                V v2 = c2.c();
                if (!(v2 == null ? a2.a_(n2) != null || !a2.a(n2) : !v2.equals(a2.a_(n2)))) continue;
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
            n2 += a.a.a.b.a(this.h[n3]) ^ (arrV[n3] == null ? 0 : arrV[n3].hashCode());
        }
        return n2;
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(0);
        super.writeExternal(objectOutput);
        objectOutput.writeInt(this.m);
        objectOutput.writeInt(this.a);
        int n2 = this.l.length;
        while (n2-- > 0) {
            if (this.l[n2] != 1) continue;
            objectOutput.writeInt(this.h[n2]);
            objectOutput.writeObject(this.k[n2]);
        }
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        objectInput.readByte();
        super.readExternal(objectInput);
        this.m = objectInput.readInt();
        int n2 = objectInput.readInt();
        this.c(n2);
        while (n2-- > 0) {
            int n3 = objectInput.readInt();
            Object object = objectInput.readObject();
            this.a(n3, object);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        this.a(new c(this, stringBuilder));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    class a<V>
    extends a.a.a.a.c
    implements a.a.b.c<V> {
        private final a<V> e;

        public a(a<V> a3) {
            super(a3);
            this.e = a3;
        }

        @Override
        public void a() {
            this.b_();
        }

        @Override
        public int b() {
            return this.e.h[this.c];
        }

        @Override
        public V c() {
            return this.e.k[this.c];
        }
    }

}

