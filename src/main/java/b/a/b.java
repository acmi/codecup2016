/*
 * Decompiled with CFR 0_119.
 */
package b.a;

public class b {
    public int a;
    public int b;
    boolean c = false;
    public int d;
    public int e;
    public Object f;

    public b(int n2) {
        this(n2, -1);
        this.d = -1;
        this.e = -1;
        this.f = null;
    }

    b(int n2, int n3) {
        this.a = n2;
        this.b = n3;
    }

    public b(int n2, int n3, int n4) {
        this(n2, n3, n4, null);
    }

    public b(int n2, int n3, int n4, Object object) {
        this(n2);
        this.d = n3;
        this.e = n4;
        this.f = object;
    }

    public b(int n2, Object object) {
        this(n2, -1, -1, object);
    }

    public String toString() {
        return "#" + this.a;
    }
}

