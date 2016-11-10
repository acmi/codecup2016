/*
 * Decompiled with CFR 0_119.
 */
package a.a.c.b;

import a.a.c.b.b;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class a
implements a.a.c.a,
Externalizable {
    int a;
    int b;
    a c;
    a d;

    public a() {
        this.d = this.c = null;
    }

    public a(a.a.c.a a2) {
        this.d = this.c = null;
        this.a = a2.b();
        a.a.b.b b2 = a2.a();
        while (b2.hasNext()) {
            int n2 = b2.a();
            this.a(n2);
        }
    }

    public int b() {
        return this.a;
    }

    public boolean a(int n2) {
        a a2 = new a(n2);
        if (a.b(this.c)) {
            this.c = a2;
            this.d = a2;
        } else {
            a2.a(this.d);
            this.d.b(a2);
            this.d = a2;
        }
        ++this.b;
        return true;
    }

    public void c() {
        this.b = 0;
        this.c = null;
        this.d = null;
    }

    public boolean b(int n2) {
        boolean bl = false;
        a a2 = this.c;
        while (a.a(a2)) {
            if (a2.a() == n2) {
                bl = true;
                this.a(a2);
            }
            a2 = a2.c();
        }
        return bl;
    }

    private void a(a a2) {
        if (a.b(a2)) {
            return;
        }
        --this.b;
        a a3 = a2.b();
        a a4 = a2.c();
        if (a.a(a3)) {
            a3.b(a4);
        } else {
            this.c = a4;
        }
        if (a.a(a4)) {
            a4.a(a3);
        } else {
            this.d = a3;
        }
        a2.b(null);
        a2.a(null);
    }

    public boolean a(a.a.e.b b2) {
        a a2 = this.c;
        while (a.a(a2)) {
            if (!b2.a(a2.a())) {
                return false;
            }
            a2 = a2.c();
        }
        return true;
    }

    public a.a.b.b a() {
        return new b(this);
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(0);
        objectOutput.writeInt(this.a);
        objectOutput.writeInt(this.b);
        a.a.b.b b2 = this.a();
        while (b2.hasNext()) {
            int n2 = b2.a();
            objectOutput.writeInt(n2);
        }
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        objectInput.readByte();
        this.a = objectInput.readInt();
        int n2 = objectInput.readInt();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.a(objectInput.readInt());
        }
    }

    static boolean a(Object object) {
        return object != null;
    }

    static boolean b(Object object) {
        return object == null;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        a a2 = (a)object;
        if (this.a != a2.a) {
            return false;
        }
        if (this.b != a2.b) {
            return false;
        }
        a.a.b.b b2 = this.a();
        a.a.b.b b3 = a2.a();
        while (b2.hasNext()) {
            if (!b3.hasNext()) {
                return false;
            }
            if (b2.a() == b3.a()) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        int n2 = a.a.a.b.a(this.a);
        n2 = 31 * n2 + this.b;
        a.a.b.b b2 = this.a();
        while (b2.hasNext()) {
            n2 = 31 * n2 + a.a.a.b.a(b2.a());
        }
        return n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        a.a.b.b b2 = this.a();
        while (b2.hasNext()) {
            int n2 = b2.a();
            stringBuilder.append(n2);
            if (!b2.hasNext()) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    static /* synthetic */ void a(a a2, a a3) {
        a2.a(a3);
    }

    static class a {
        int a;
        a b;
        a c;

        a(int n2) {
            this.a = n2;
        }

        public int a() {
            return this.a;
        }

        public a b() {
            return this.b;
        }

        public void a(a a2) {
            this.b = a2;
        }

        public a c() {
            return this.c;
        }

        public void b(a a2) {
            this.c = a2;
        }
    }

}

