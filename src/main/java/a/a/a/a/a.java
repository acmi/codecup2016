/*
 * Decompiled with CFR 0_119.
 */
package a.a.a.a;

import a.a.a.b;
import a.a.a.c;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class a
implements Externalizable {
    protected transient int a;
    protected transient int b;
    protected float c;
    protected int d;
    protected int e;
    protected float f;
    protected transient boolean g = false;

    public a() {
        this(10, 0.5f);
    }

    public a(int n2) {
        this(n2, 0.5f);
    }

    public a(int n2, float f2) {
        this.c = f2;
        this.f = f2;
        this.c(b.a((float)n2 / f2));
    }

    public boolean isEmpty() {
        return 0 == this.a;
    }

    public int size() {
        return this.a;
    }

    public abstract int a();

    public void b_(int n2) {
        if (n2 > this.d - this.size()) {
            this.d(c.a(Math.max(this.size() + 1, b.a((float)(n2 + this.size()) / this.c) + 1)));
            this.e(this.a());
        }
    }

    public void b() {
        this.d(c.a(Math.max(this.a + 1, b.a((float)this.size() / this.c) + 1)));
        this.e(this.a());
        if (this.f != 0.0f) {
            this.f(this.size());
        }
    }

    protected void b(int n2) {
        --this.a;
        if (this.f != 0.0f) {
            --this.e;
            if (!this.g && this.e <= 0) {
                this.b();
            }
        }
    }

    public void clear() {
        this.a = 0;
        this.b = this.a();
    }

    protected int c(int n2) {
        int n3 = c.a(n2);
        this.e(n3);
        this.f(n2);
        return n3;
    }

    protected abstract void d(int var1);

    public void c() {
        this.g = true;
    }

    public void a(boolean bl) {
        this.g = false;
        if (bl && this.e <= 0 && this.f != 0.0f) {
            this.b();
        }
    }

    protected void e(int n2) {
        this.d = Math.min(n2 - 1, (int)((float)n2 * this.c));
        this.b = n2 - this.a;
    }

    protected void f(int n2) {
        if (this.f != 0.0f) {
            this.e = (int)((float)n2 * this.f + 0.5f);
        }
    }

    protected final void b(boolean bl) {
        if (bl) {
            --this.b;
        }
        if (++this.a > this.d || this.b == 0) {
            int n2 = this.a > this.d ? c.a(this.a() << 1) : this.a();
            this.d(n2);
            this.e(this.a());
        }
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(0);
        objectOutput.writeFloat(this.c);
        objectOutput.writeFloat(this.f);
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        objectInput.readByte();
        float f2 = this.c;
        this.c = objectInput.readFloat();
        this.f = objectInput.readFloat();
        if (f2 != this.c) {
            this.c((int)Math.ceil(10.0f / this.c));
        }
    }
}

