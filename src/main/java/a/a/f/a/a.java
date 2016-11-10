/*
 * Decompiled with CFR 0_119.
 */
package a.a.f.a;

import a.a.a.a.g;
import a.a.e.e;
import a.a.f.a.b;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class a<E>
extends g<E>
implements Externalizable,
Iterable<E>,
Set<E> {
    public a() {
    }

    public a(int n2) {
        super(n2);
    }

    @Override
    public boolean add(E e2) {
        int n2 = this.b(e2);
        if (n2 < 0) {
            return false;
        }
        this.b(this.k);
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Set)) {
            return false;
        }
        Set set = (Set)object;
        if (set.size() != this.size()) {
            return false;
        }
        return this.containsAll(set);
    }

    @Override
    public int hashCode() {
        a a2 = new a(null);
        this.a(a2);
        return a2.a();
    }

    @Override
    protected void d(int n2) {
        int n3 = this.h.length;
        int n4 = this.size();
        Object[] arrobject = this.h;
        this.h = new Object[n2];
        Arrays.fill(this.h, j);
        int n5 = 0;
        int n6 = n3;
        while (n6-- > 0) {
            Object object = arrobject[n6];
            if (object == j || object == i) continue;
            int n7 = this.b(object);
            if (n7 < 0) {
                this.a(this.h[- n7 - 1], object, this.size(), n4, arrobject);
            }
            ++n5;
        }
        a.a(this.size(), n4);
    }

    @Override
    public Object[] toArray() {
        Object[] arrobject = new Object[this.size()];
        this.a(new a.a.e.a.a<Object>(arrobject));
        return arrobject;
    }

    @Override
    public <T> T[] toArray(T[] arrT) {
        int n2 = this.size();
        if (arrT.length < n2) {
            arrT = (Object[])Array.newInstance(arrT.getClass().getComponentType(), n2);
        }
        this.a(new a.a.e.a.a<T>(arrT));
        if (arrT.length > n2) {
            arrT[n2] = null;
        }
        return arrT;
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(this.h, 0, this.h.length, j);
    }

    @Override
    public boolean remove(Object object) {
        int n2 = this.a(object);
        if (n2 >= 0) {
            this.b(n2);
            return true;
        }
        return false;
    }

    public a.a.b.a.a<E> d() {
        return new a.a.b.a.a(this);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (this.contains(iterator.next())) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean bl = false;
        int n2 = collection.size();
        this.b_(n2);
        Iterator<E> iterator = collection.iterator();
        while (n2-- > 0) {
            if (!this.add(iterator.next())) continue;
            bl = true;
        }
        return bl;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean bl = false;
        int n2 = collection.size();
        Iterator iterator = collection.iterator();
        while (n2-- > 0) {
            if (!this.remove(iterator.next())) continue;
            bl = true;
        }
        return bl;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean bl = false;
        int n2 = this.size();
        a.a.b.a.a<E> a2 = this.d();
        while (n2-- > 0) {
            if (collection.contains(a2.next())) continue;
            a2.remove();
            bl = true;
        }
        return bl;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        this.a(new b(this, stringBuilder));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(1);
        super.writeExternal(objectOutput);
        objectOutput.writeInt(this.a);
        this.a(objectOutput);
    }

    protected void a(ObjectOutput objectOutput) throws IOException {
        int n2 = this.h.length;
        while (n2-- > 0) {
            if (this.h[n2] == i || this.h[n2] == j) continue;
            objectOutput.writeObject(this.h[n2]);
        }
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        byte by = objectInput.readByte();
        if (by != 0) {
            super.readExternal(objectInput);
        }
        int n2 = objectInput.readInt();
        this.c(n2);
        while (n2-- > 0) {
            Object object = objectInput.readObject();
            this.add(object);
        }
    }

    @Override
    public /* synthetic */ Iterator iterator() {
        return this.d();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private final class a
    implements e<E> {
        private int b;

        private a() {
            this.b = 0;
        }

        public int a() {
            return this.b;
        }

        @Override
        public final boolean a(E e2) {
            this.b += a.a.a.b.a(e2);
            return true;
        }

        /* synthetic */ a(b b2) {
            this();
        }
    }

}

