/*
 * Decompiled with CFR 0_119.
 */
package a.a.c.a;

import a.a.a.b;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class a
implements a.a.c.a,
Externalizable {
    protected int[] a;
    protected int b;
    protected int c;

    public a() {
        this(10, 0);
    }

    public a(int n2) {
        this(n2, 0);
    }

    public a(int n2, int n3) {
        this.a = new int[n2];
        this.b = 0;
        this.c = n3;
    }

    public int b() {
        return this.c;
    }

    public void c(int n2) {
        if (n2 > this.a.length) {
            int n3 = Math.max(this.a.length << 1, n2);
            int[] arrn = new int[n3];
            System.arraycopy(this.a, 0, arrn, 0, this.a.length);
            this.a = arrn;
        }
    }

    public int d() {
        return this.b;
    }

    public boolean a(int n2) {
        this.c(this.b + 1);
        this.a[this.b++] = n2;
        return true;
    }

    public int d(int n2) {
        if (n2 >= this.b) {
            throw new ArrayIndexOutOfBoundsException(n2);
        }
        return this.a[n2];
    }

    public void c() {
        this.e(10);
    }

    public void e(int n2) {
        this.a = new int[n2];
        this.b = 0;
    }

    public boolean b(int n2) {
        for (int i2 = 0; i2 < this.b; ++i2) {
            if (n2 != this.a[i2]) continue;
            this.a(i2, 1);
            return true;
        }
        return false;
    }

    public void a(int n2, int n3) {
        if (n3 == 0) {
            return;
        }
        if (n2 < 0 || n2 >= this.b) {
            throw new ArrayIndexOutOfBoundsException(n2);
        }
        if (n2 == 0) {
            System.arraycopy(this.a, n3, this.a, 0, this.b - n3);
        } else if (this.b - n3 != n2) {
            System.arraycopy(this.a, n2 + n3, this.a, n2, this.b - (n2 + n3));
        }
        this.b -= n3;
    }

    public a.a.b.b a() {
        return new a(0);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof a) {
            a a2 = (a)object;
            if (a2.d() != this.d()) {
                return false;
            }
            int n2 = this.b;
            while (n2-- > 0) {
                if (this.a[n2] == a2.a[n2]) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int n2 = 0;
        int n3 = this.b;
        while (n3-- > 0) {
            n2 += b.a(this.a[n3]);
        }
        return n2;
    }

    public boolean a(a.a.e.b b2) {
        for (int i2 = 0; i2 < this.b; ++i2) {
            if (b2.a(this.a[i2])) continue;
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        int n2 = this.b - 1;
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder.append(this.a[i2]);
            stringBuilder.append(", ");
        }
        if (this.d() > 0) {
            stringBuilder.append(this.a[this.b - 1]);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(0);
        objectOutput.writeInt(this.b);
        objectOutput.writeInt(this.c);
        int n2 = this.a.length;
        objectOutput.writeInt(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            objectOutput.writeInt(this.a[i2]);
        }
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        objectInput.readByte();
        this.b = objectInput.readInt();
        this.c = objectInput.readInt();
        int n2 = objectInput.readInt();
        this.a = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.a[i2] = objectInput.readInt();
        }
    }

    class a
    implements a.a.b.b {
        private int c;
        int a;

        a(int n2) {
            this.c = 0;
            this.a = -1;
            this.c = n2;
        }

        public boolean hasNext() {
            return this.c < a.this.d();
        }

        public int a() {
            try {
                int n2 = a.this.d(this.c);
                this.a = this.c++;
                return n2;
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (this.a == -1) {
                throw new IllegalStateException();
            }
            try {
                a.this.a(this.a, 1);
                if (this.a < this.c) {
                    --this.c;
                }
                this.a = -1;
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                throw new ConcurrentModificationException();
            }
        }
    }

}

