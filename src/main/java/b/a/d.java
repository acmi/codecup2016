/*
 * Decompiled with CFR 0_119.
 */
package b.a;

import b.a.b;
import java.util.Stack;

public class d {
    protected Stack a;
    protected int b;
    protected Stack c;

    public d(Stack stack) throws Exception {
        if (stack == null) {
            throw new Exception("Internal parser error: attempt to create null virtual stack");
        }
        this.a = stack;
        this.c = new Stack();
        this.b = 0;
        this.a();
    }

    protected void a() {
        if (this.b >= this.a.size()) {
            return;
        }
        b b2 = (b)this.a.elementAt(this.a.size() - 1 - this.b);
        ++this.b;
        this.c.push(new Integer(b2.b));
    }

    public void b() throws Exception {
        if (this.c.empty()) {
            throw new Exception("Internal parser error: pop from empty virtual stack");
        }
        this.c.pop();
        if (this.c.empty()) {
            this.a();
        }
    }

    public void a(int n2) {
        this.c.push(new Integer(n2));
    }

    public int c() throws Exception {
        if (this.c.empty()) {
            throw new Exception("Internal parser error: top() called on empty virtual stack");
        }
        return (Integer)this.c.peek();
    }
}

