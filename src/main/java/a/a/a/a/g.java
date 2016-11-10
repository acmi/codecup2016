/*
 * Decompiled with CFR 0_119.
 */
package a.a.a.a;

import a.a.a.a.a;
import a.a.e.e;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class g<T>
extends a {
    public transient Object[] h;
    public static final Object i = new Object();
    public static final Object j = new Object();
    protected boolean k;

    public g() {
    }

    public g(int n2) {
        super(n2);
    }

    @Override
    public int a() {
        return this.h.length;
    }

    @Override
    protected void b(int n2) {
        this.h[n2] = i;
        super.b(n2);
    }

    @Override
    public int c(int n2) {
        int n3 = super.c(n2);
        this.h = new Object[n3];
        Arrays.fill(this.h, j);
        return n3;
    }

    public boolean a(e<? super T> e2) {
        Object[] arrobject = this.h;
        int n2 = arrobject.length;
        while (n2-- > 0) {
            if (arrobject[n2] == j || arrobject[n2] == i || e2.a(arrobject[n2])) continue;
            return false;
        }
        return true;
    }

    public boolean contains(Object object) {
        return this.a(object) >= 0;
    }

    protected int a(Object object) {
        if (object == null) {
            return this.d();
        }
        int n2 = this.c(object) & Integer.MAX_VALUE;
        int n3 = n2 % this.h.length;
        Object object2 = this.h[n3];
        if (object2 == j) {
            return -1;
        }
        if (object2 == object || this.a(object, object2)) {
            return n3;
        }
        return this.a(object, n3, n2, object2);
    }

    private int a(Object object, int n2, int n3, Object object2) {
        Object[] arrobject = this.h;
        int n4 = arrobject.length;
        int n5 = 1 + n3 % (n4 - 2);
        int n6 = n2;
        do {
            if ((n2 -= n5) < 0) {
                n2 += n4;
            }
            if ((object2 = arrobject[n2]) == j) {
                return -1;
            }
            if (object2 != object && !this.a(object, object2)) continue;
            return n2;
        } while (n2 != n6);
        return -1;
    }

    private int d() {
        int n2 = 0;
        for (Object object : this.h) {
            if (object == null) {
                return n2;
            }
            if (object == j) {
                return -1;
            }
            ++n2;
        }
        return -1;
    }

    protected int b(T t2) {
        this.k = false;
        if (t2 == null) {
            return this.e();
        }
        int n2 = this.c(t2) & Integer.MAX_VALUE;
        int n3 = n2 % this.h.length;
        Object object = this.h[n3];
        if (object == j) {
            this.k = true;
            this.h[n3] = t2;
            return n3;
        }
        if (object == t2 || this.a(t2, object)) {
            return - n3 - 1;
        }
        return this.b(t2, n3, n2, object);
    }

    private int b(T t2, int n2, int n3, Object object) {
        Object[] arrobject = this.h;
        int n4 = arrobject.length;
        int n5 = 1 + n3 % (n4 - 2);
        int n6 = n2;
        int n7 = -1;
        do {
            if (object == i && n7 == -1) {
                n7 = n2;
            }
            if ((n2 -= n5) < 0) {
                n2 += n4;
            }
            if ((object = arrobject[n2]) == j) {
                if (n7 != -1) {
                    this.h[n7] = t2;
                    return n7;
                }
                this.k = true;
                this.h[n2] = t2;
                return n2;
            }
            if (object != t2 && !this.a(t2, object)) continue;
            return - n2 - 1;
        } while (n2 != n6);
        if (n7 != -1) {
            this.h[n7] = t2;
            return n7;
        }
        throw new IllegalStateException("No free or removed slots available. Key set full?!!");
    }

    private int e() {
        int n2 = 0;
        int n3 = -1;
        for (Object object : this.h) {
            if (object == i && n3 == -1) {
                n3 = n2;
            }
            if (object == j) {
                if (n3 != -1) {
                    this.h[n3] = null;
                    return n3;
                }
                this.k = true;
                this.h[n2] = null;
                return n2;
            }
            if (object == null) {
                return - n2 - 1;
            }
            ++n2;
        }
        if (n3 != -1) {
            this.h[n3] = null;
            return n3;
        }
        throw new IllegalStateException("Could not find insertion index for null key. Key set full!?!!");
    }

    protected final void a(Object object, Object object2, int n2, int n3, Object[] arrobject) throws IllegalArgumentException {
        String string = this.b(object, object2, this.size(), n3, arrobject);
        throw this.a(object, object2, string);
    }

    protected final IllegalArgumentException a(Object object, Object object2, String string) {
        return new IllegalArgumentException("Equal objects must have equal hashcodes. During rehashing, Trove discovered that the following two objects claim to be equal (as in java.lang.Object.equals()) but their hashCodes (or those calculated by your TObjectHashingStrategy) are not equal.This violates the general contract of java.lang.Object.hashCode().  See bullet point two in that method's documentation. object #1 =" + g.d(object) + "; object #2 =" + g.d(object2) + "\n" + string);
    }

    protected boolean a(Object object, Object object2) {
        if (object2 == null || object2 == i) {
            return false;
        }
        return object.equals(object2);
    }

    protected int c(Object object) {
        return object.hashCode();
    }

    protected static String a(int n2, int n3) {
        if (n2 != n3) {
            return "[Warning] apparent concurrent modification of the key set. Size before and after rehash() do not match " + n3 + " vs " + n2;
        }
        return "";
    }

    protected String b(Object object, Object object2, int n2, int n3, Object[] arrobject) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.c(object, object2));
        stringBuilder.append(g.a(n2, n3));
        stringBuilder.append(g.a(arrobject, n3));
        if (object == object2) {
            stringBuilder.append("Inserting same object twice, rehashing bug. Object= ").append(object2);
        }
        return stringBuilder.toString();
    }

    private static String a(Object[] arrobject, int n2) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<Object> set = g.a(arrobject);
        if (set.size() != n2) {
            stringBuilder.append("\nhashCode() and/or equals() have inconsistent implementation");
            stringBuilder.append("\nKey set lost entries, now got ").append(set.size()).append(" instead of ").append(n2);
            stringBuilder.append(". This can manifest itself as an apparent duplicate key.");
        }
        return stringBuilder.toString();
    }

    private static Set<Object> a(Object[] arrobject) {
        HashSet<Object> hashSet = new HashSet<Object>();
        for (Object object : arrobject) {
            if (object == j || object == i) continue;
            hashSet.add(object);
        }
        return hashSet;
    }

    private static String b(Object object, Object object2) {
        StringBuilder stringBuilder = new StringBuilder();
        if (object == object2) {
            return "a == b";
        }
        if (object.getClass() != object2.getClass()) {
            stringBuilder.append("Class of objects differ a=").append(object.getClass()).append(" vs b=").append(object2.getClass());
            boolean bl = object.equals(object2);
            boolean bl2 = object2.equals(object);
            if (bl != bl2) {
                stringBuilder.append("\nequals() of a or b object are asymmetric");
                stringBuilder.append("\na.equals(b) =").append(bl);
                stringBuilder.append("\nb.equals(a) =").append(bl2);
            }
        }
        return stringBuilder.toString();
    }

    protected static String d(Object object) {
        return (object == null ? "class null" : object.getClass()) + " id= " + System.identityHashCode(object) + " hashCode= " + (object == null ? 0 : object.hashCode()) + " toString= " + String.valueOf(object);
    }

    private String c(Object object, Object object2) {
        StringBuilder stringBuilder = new StringBuilder();
        HashSet hashSet = new HashSet();
        for (Object object3 : this.h) {
            if (object3 == j || object3 == i) continue;
            if (object3 != null) {
                hashSet.add(object3.getClass());
                continue;
            }
            hashSet.add(null);
        }
        if (hashSet.size() > 1) {
            stringBuilder.append("\nMore than one type used for keys. Watch out for asymmetric equals(). Read about the 'Liskov substitution principle' and the implications for equals() in java.");
            stringBuilder.append("\nKey types: ").append(hashSet);
            stringBuilder.append(g.b(object, object2));
        }
        return stringBuilder.toString();
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(0);
        super.writeExternal(objectOutput);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        objectInput.readByte();
        super.readExternal(objectInput);
    }
}

