/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.$AnnotationVisitor;
import com.google.inject.internal.asm.$AnnotationWriter;
import com.google.inject.internal.asm.$Attribute;
import com.google.inject.internal.asm.$ByteVector;
import com.google.inject.internal.asm.$ClassReader;
import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$FieldVisitor;
import com.google.inject.internal.asm.$FieldWriter;
import com.google.inject.internal.asm.$Handle;
import com.google.inject.internal.asm.$Item;
import com.google.inject.internal.asm.$MethodVisitor;
import com.google.inject.internal.asm.$MethodWriter;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.asm.$TypePath;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class $ClassWriter
extends $ClassVisitor {
    public static final int COMPUTE_MAXS = 1;
    public static final int COMPUTE_FRAMES = 2;
    static final byte[] a;
    $ClassReader M;
    int b;
    int c = 1;
    final $ByteVector d = new $ByteVector();
    $Item[] e = new $Item[256];
    int f = (int)(0.75 * (double)this.e.length);
    final $Item g = new $Item();
    final $Item h = new $Item();
    final $Item i = new $Item();
    final $Item j = new $Item();
    $Item[] H;
    private short G;
    private int k;
    private int l;
    String I;
    private int m;
    private int n;
    private int o;
    private int[] p;
    private int q;
    private $ByteVector r;
    private int s;
    private int t;
    private $AnnotationWriter u;
    private $AnnotationWriter v;
    private $AnnotationWriter N;
    private $AnnotationWriter O;
    private $Attribute w;
    private int x;
    private $ByteVector y;
    int z;
    $ByteVector A;
    $FieldWriter B;
    $FieldWriter C;
    $MethodWriter D;
    $MethodWriter E;
    private boolean K;
    private boolean J;
    boolean L;

    public $ClassWriter(int n2) {
        super(327680);
        this.K = (n2 & 1) != 0;
        this.J = (n2 & 2) != 0;
    }

    public $ClassWriter($ClassReader $ClassReader, int n2) {
        this(n2);
        $ClassReader.a(this);
        this.M = $ClassReader;
    }

    public final void visit(int n2, int n3, String string, String string2, String string3, String[] arrstring) {
        this.b = n2;
        this.k = n3;
        this.l = this.newClass(string);
        this.I = string;
        if (string2 != null) {
            this.m = this.newUTF8(string2);
        }
        int n4 = this.n = string3 == null ? 0 : this.newClass(string3);
        if (arrstring != null && arrstring.length > 0) {
            this.o = arrstring.length;
            this.p = new int[this.o];
            for (int i2 = 0; i2 < this.o; ++i2) {
                this.p[i2] = this.newClass(arrstring[i2]);
            }
        }
    }

    public final void visitSource(String string, String string2) {
        if (string != null) {
            this.q = this.newUTF8(string);
        }
        if (string2 != null) {
            this.r = new $ByteVector().encodeUTF8(string2, 0, Integer.MAX_VALUE);
        }
    }

    public final void visitOuterClass(String string, String string2, String string3) {
        this.s = this.newClass(string);
        if (string2 != null && string3 != null) {
            this.t = this.newNameType(string2, string3);
        }
    }

    public final $AnnotationVisitor visitAnnotation(String string, boolean bl) {
        $ByteVector $ByteVector = new $ByteVector();
        $ByteVector.putShort(this.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this, true, $ByteVector, $ByteVector, 2);
        if (bl) {
            $AnnotationWriter.g = this.u;
            this.u = $AnnotationWriter;
        } else {
            $AnnotationWriter.g = this.v;
            this.v = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public final $AnnotationVisitor visitTypeAnnotation(int n2, $TypePath $TypePath, String string, boolean bl) {
        $ByteVector $ByteVector = new $ByteVector();
        $AnnotationWriter.a(n2, $TypePath, $ByteVector);
        $ByteVector.putShort(this.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this, true, $ByteVector, $ByteVector, $ByteVector.b - 2);
        if (bl) {
            $AnnotationWriter.g = this.N;
            this.N = $AnnotationWriter;
        } else {
            $AnnotationWriter.g = this.O;
            this.O = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public final void visitAttribute($Attribute $Attribute) {
        $Attribute.a = this.w;
        this.w = $Attribute;
    }

    public final void visitInnerClass(String string, String string2, String string3, int n2) {
        if (this.y == null) {
            this.y = new $ByteVector();
        }
        $Item $Item = this.a(string);
        if ($Item.c == 0) {
            ++this.x;
            this.y.putShort($Item.a);
            this.y.putShort(string2 == null ? 0 : this.newClass(string2));
            this.y.putShort(string3 == null ? 0 : this.newUTF8(string3));
            this.y.putShort(n2);
            $Item.c = this.x;
        }
    }

    public final $FieldVisitor visitField(int n2, String string, String string2, String string3, Object object) {
        return new $FieldWriter(this, n2, string, string2, string3, object);
    }

    public final $MethodVisitor visitMethod(int n2, String string, String string2, String string3, String[] arrstring) {
        return new $MethodWriter(this, n2, string, string2, string3, arrstring, this.K, this.J);
    }

    public final void visitEnd() {
    }

    public byte[] toByteArray() {
        int n2;
        if (this.c > 65535) {
            throw new RuntimeException("Class file too large!");
        }
        int n3 = 24 + 2 * this.o;
        int n4 = 0;
        $FieldWriter $FieldWriter = this.B;
        while ($FieldWriter != null) {
            ++n4;
            n3 += $FieldWriter.a();
            $FieldWriter = ($FieldWriter)$FieldWriter.fv;
        }
        int n5 = 0;
        $MethodWriter $MethodWriter = this.D;
        while ($MethodWriter != null) {
            ++n5;
            n3 += $MethodWriter.a();
            $MethodWriter = ($MethodWriter)$MethodWriter.mv;
        }
        int n6 = 0;
        if (this.A != null) {
            ++n6;
            n3 += 8 + this.A.b;
            this.newUTF8("BootstrapMethods");
        }
        if (this.m != 0) {
            ++n6;
            n3 += 8;
            this.newUTF8("Signature");
        }
        if (this.q != 0) {
            ++n6;
            n3 += 8;
            this.newUTF8("SourceFile");
        }
        if (this.r != null) {
            ++n6;
            n3 += this.r.b + 6;
            this.newUTF8("SourceDebugExtension");
        }
        if (this.s != 0) {
            ++n6;
            n3 += 10;
            this.newUTF8("EnclosingMethod");
        }
        if ((this.k & 131072) != 0) {
            ++n6;
            n3 += 6;
            this.newUTF8("Deprecated");
        }
        if ((this.k & 4096) != 0 && ((this.b & 65535) < 49 || (this.k & 262144) != 0)) {
            ++n6;
            n3 += 6;
            this.newUTF8("Synthetic");
        }
        if (this.y != null) {
            ++n6;
            n3 += 8 + this.y.b;
            this.newUTF8("InnerClasses");
        }
        if (this.u != null) {
            ++n6;
            n3 += 8 + this.u.a();
            this.newUTF8("RuntimeVisibleAnnotations");
        }
        if (this.v != null) {
            ++n6;
            n3 += 8 + this.v.a();
            this.newUTF8("RuntimeInvisibleAnnotations");
        }
        if (this.N != null) {
            ++n6;
            n3 += 8 + this.N.a();
            this.newUTF8("RuntimeVisibleTypeAnnotations");
        }
        if (this.O != null) {
            ++n6;
            n3 += 8 + this.O.a();
            this.newUTF8("RuntimeInvisibleTypeAnnotations");
        }
        if (this.w != null) {
            n6 += this.w.a();
            n3 += this.w.a(this, null, 0, -1, -1);
        }
        $ByteVector $ByteVector = new $ByteVector(n3 += this.d.b);
        $ByteVector.putInt(-889275714).putInt(this.b);
        $ByteVector.putShort(this.c).putByteArray(this.d.a, 0, this.d.b);
        int n7 = 393216 | (this.k & 262144) / 64;
        $ByteVector.putShort(this.k & ~ n7).putShort(this.l).putShort(this.n);
        $ByteVector.putShort(this.o);
        for (n2 = 0; n2 < this.o; ++n2) {
            $ByteVector.putShort(this.p[n2]);
        }
        $ByteVector.putShort(n4);
        $FieldWriter = this.B;
        while ($FieldWriter != null) {
            $FieldWriter.a($ByteVector);
            $FieldWriter = ($FieldWriter)$FieldWriter.fv;
        }
        $ByteVector.putShort(n5);
        $MethodWriter = this.D;
        while ($MethodWriter != null) {
            $MethodWriter.a($ByteVector);
            $MethodWriter = ($MethodWriter)$MethodWriter.mv;
        }
        $ByteVector.putShort(n6);
        if (this.A != null) {
            $ByteVector.putShort(this.newUTF8("BootstrapMethods"));
            $ByteVector.putInt(this.A.b + 2).putShort(this.z);
            $ByteVector.putByteArray(this.A.a, 0, this.A.b);
        }
        if (this.m != 0) {
            $ByteVector.putShort(this.newUTF8("Signature")).putInt(2).putShort(this.m);
        }
        if (this.q != 0) {
            $ByteVector.putShort(this.newUTF8("SourceFile")).putInt(2).putShort(this.q);
        }
        if (this.r != null) {
            n2 = this.r.b;
            $ByteVector.putShort(this.newUTF8("SourceDebugExtension")).putInt(n2);
            $ByteVector.putByteArray(this.r.a, 0, n2);
        }
        if (this.s != 0) {
            $ByteVector.putShort(this.newUTF8("EnclosingMethod")).putInt(4);
            $ByteVector.putShort(this.s).putShort(this.t);
        }
        if ((this.k & 131072) != 0) {
            $ByteVector.putShort(this.newUTF8("Deprecated")).putInt(0);
        }
        if ((this.k & 4096) != 0 && ((this.b & 65535) < 49 || (this.k & 262144) != 0)) {
            $ByteVector.putShort(this.newUTF8("Synthetic")).putInt(0);
        }
        if (this.y != null) {
            $ByteVector.putShort(this.newUTF8("InnerClasses"));
            $ByteVector.putInt(this.y.b + 2).putShort(this.x);
            $ByteVector.putByteArray(this.y.a, 0, this.y.b);
        }
        if (this.u != null) {
            $ByteVector.putShort(this.newUTF8("RuntimeVisibleAnnotations"));
            this.u.a($ByteVector);
        }
        if (this.v != null) {
            $ByteVector.putShort(this.newUTF8("RuntimeInvisibleAnnotations"));
            this.v.a($ByteVector);
        }
        if (this.N != null) {
            $ByteVector.putShort(this.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.N.a($ByteVector);
        }
        if (this.O != null) {
            $ByteVector.putShort(this.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.O.a($ByteVector);
        }
        if (this.w != null) {
            this.w.a(this, null, 0, -1, -1, $ByteVector);
        }
        if (this.L) {
            this.u = null;
            this.v = null;
            this.w = null;
            this.x = 0;
            this.y = null;
            this.z = 0;
            this.A = null;
            this.B = null;
            this.C = null;
            this.D = null;
            this.E = null;
            this.K = false;
            this.J = true;
            this.L = false;
            new $ClassReader($ByteVector.a).accept(this, 4);
            return this.toByteArray();
        }
        return $ByteVector.a;
    }

    $Item a(Object object) {
        if (object instanceof Integer) {
            int n2 = (Integer)object;
            return this.a(n2);
        }
        if (object instanceof Byte) {
            int n3 = ((Byte)object).intValue();
            return this.a(n3);
        }
        if (object instanceof Character) {
            char c2 = ((Character)object).charValue();
            return this.a(c2);
        }
        if (object instanceof Short) {
            int n4 = ((Short)object).intValue();
            return this.a(n4);
        }
        if (object instanceof Boolean) {
            int n5 = (Boolean)object != false ? 1 : 0;
            return this.a(n5);
        }
        if (object instanceof Float) {
            float f2 = ((Float)object).floatValue();
            return this.a(f2);
        }
        if (object instanceof Long) {
            long l2 = (Long)object;
            return this.a(l2);
        }
        if (object instanceof Double) {
            double d2 = (Double)object;
            return this.a(d2);
        }
        if (object instanceof String) {
            return this.b((String)object);
        }
        if (object instanceof $Type) {
            $Type $Type = ($Type)object;
            int n6 = $Type.getSort();
            if (n6 == 10) {
                return this.a($Type.getInternalName());
            }
            if (n6 == 11) {
                return this.c($Type.getDescriptor());
            }
            return this.a($Type.getDescriptor());
        }
        if (object instanceof $Handle) {
            $Handle $Handle = ($Handle)object;
            return this.a($Handle.a, $Handle.b, $Handle.c, $Handle.d);
        }
        throw new IllegalArgumentException("value " + object);
    }

    public int newConst(Object object) {
        return this.a((Object)object).a;
    }

    public int newUTF8(String string) {
        this.g.a(1, string, null, null);
        $Item $Item = this.a(this.g);
        if ($Item == null) {
            this.d.putByte(1).putUTF8(string);
            $Item = new $Item(this.c++, this.g);
            this.b($Item);
        }
        return $Item.a;
    }

    $Item a(String string) {
        this.h.a(7, string, null, null);
        $Item $Item = this.a(this.h);
        if ($Item == null) {
            this.d.b(7, this.newUTF8(string));
            $Item = new $Item(this.c++, this.h);
            this.b($Item);
        }
        return $Item;
    }

    public int newClass(String string) {
        return this.a((String)string).a;
    }

    $Item c(String string) {
        this.h.a(16, string, null, null);
        $Item $Item = this.a(this.h);
        if ($Item == null) {
            this.d.b(16, this.newUTF8(string));
            $Item = new $Item(this.c++, this.h);
            this.b($Item);
        }
        return $Item;
    }

    public int newMethodType(String string) {
        return this.c((String)string).a;
    }

    $Item a(int n2, String string, String string2, String string3) {
        this.j.a(20 + n2, string, string2, string3);
        $Item $Item = this.a(this.j);
        if ($Item == null) {
            if (n2 <= 4) {
                this.b(15, n2, this.newField(string, string2, string3));
            } else {
                this.b(15, n2, this.newMethod(string, string2, string3, n2 == 9));
            }
            $Item = new $Item(this.c++, this.j);
            this.b($Item);
        }
        return $Item;
    }

    public int newHandle(int n2, String string, String string2, String string3) {
        return this.a((int)n2, (String)string, (String)string2, (String)string3).a;
    }

    /* varargs */ $Item a(String string, String string2, $Handle $Handle, Object ... arrobject) {
        int n2;
        $ByteVector $ByteVector = this.A;
        if ($ByteVector == null) {
            $ByteVector = this.A = new $ByteVector();
        }
        int n3 = $ByteVector.b;
        int n4 = $Handle.hashCode();
        $ByteVector.putShort(this.newHandle($Handle.a, $Handle.b, $Handle.c, $Handle.d));
        int n5 = arrobject.length;
        $ByteVector.putShort(n5);
        for (int i2 = 0; i2 < n5; ++i2) {
            Object object = arrobject[i2];
            n4 ^= object.hashCode();
            $ByteVector.putShort(this.newConst(object));
        }
        byte[] arrby = $ByteVector.a;
        int n6 = 2 + n5 << 1;
        $Item $Item = this.e[(n4 &= Integer.MAX_VALUE) % this.e.length];
        block1 : while ($Item != null) {
            if ($Item.b != 33 || $Item.j != n4) {
                $Item = $Item.k;
                continue;
            }
            n2 = $Item.c;
            for (int i3 = 0; i3 < n6; ++i3) {
                if (arrby[n3 + i3] == arrby[n2 + i3]) continue;
                $Item = $Item.k;
                continue block1;
            }
        }
        if ($Item != null) {
            n2 = $Item.a;
            $ByteVector.b = n3;
        } else {
            n2 = this.z++;
            $Item = new $Item(n2);
            $Item.a(n3, n4);
            this.b($Item);
        }
        this.i.a(string, string2, n2);
        $Item = this.a(this.i);
        if ($Item == null) {
            this.a(18, n2, this.newNameType(string, string2));
            $Item = new $Item(this.c++, this.i);
            this.b($Item);
        }
        return $Item;
    }

    public /* varargs */ int newInvokeDynamic(String string, String string2, $Handle $Handle, Object ... arrobject) {
        return this.a((String)string, (String)string2, ($Handle)$Handle, (Object[])arrobject).a;
    }

    $Item a(String string, String string2, String string3) {
        this.i.a(9, string, string2, string3);
        $Item $Item = this.a(this.i);
        if ($Item == null) {
            this.a(9, this.newClass(string), this.newNameType(string2, string3));
            $Item = new $Item(this.c++, this.i);
            this.b($Item);
        }
        return $Item;
    }

    public int newField(String string, String string2, String string3) {
        return this.a((String)string, (String)string2, (String)string3).a;
    }

    $Item a(String string, String string2, String string3, boolean bl) {
        int n2 = bl ? 11 : 10;
        this.i.a(n2, string, string2, string3);
        $Item $Item = this.a(this.i);
        if ($Item == null) {
            this.a(n2, this.newClass(string), this.newNameType(string2, string3));
            $Item = new $Item(this.c++, this.i);
            this.b($Item);
        }
        return $Item;
    }

    public int newMethod(String string, String string2, String string3, boolean bl) {
        return this.a((String)string, (String)string2, (String)string3, (boolean)bl).a;
    }

    $Item a(int n2) {
        this.g.a(n2);
        $Item $Item = this.a(this.g);
        if ($Item == null) {
            this.d.putByte(3).putInt(n2);
            $Item = new $Item(this.c++, this.g);
            this.b($Item);
        }
        return $Item;
    }

    $Item a(float f2) {
        this.g.a(f2);
        $Item $Item = this.a(this.g);
        if ($Item == null) {
            this.d.putByte(4).putInt(this.g.c);
            $Item = new $Item(this.c++, this.g);
            this.b($Item);
        }
        return $Item;
    }

    $Item a(long l2) {
        this.g.a(l2);
        $Item $Item = this.a(this.g);
        if ($Item == null) {
            this.d.putByte(5).putLong(l2);
            $Item = new $Item(this.c, this.g);
            this.c += 2;
            this.b($Item);
        }
        return $Item;
    }

    $Item a(double d2) {
        this.g.a(d2);
        $Item $Item = this.a(this.g);
        if ($Item == null) {
            this.d.putByte(6).putLong(this.g.d);
            $Item = new $Item(this.c, this.g);
            this.c += 2;
            this.b($Item);
        }
        return $Item;
    }

    private $Item b(String string) {
        this.h.a(8, string, null, null);
        $Item $Item = this.a(this.h);
        if ($Item == null) {
            this.d.b(8, this.newUTF8(string));
            $Item = new $Item(this.c++, this.h);
            this.b($Item);
        }
        return $Item;
    }

    public int newNameType(String string, String string2) {
        return this.a((String)string, (String)string2).a;
    }

    $Item a(String string, String string2) {
        this.h.a(12, string, string2, null);
        $Item $Item = this.a(this.h);
        if ($Item == null) {
            this.a(12, this.newUTF8(string), this.newUTF8(string2));
            $Item = new $Item(this.c++, this.h);
            this.b($Item);
        }
        return $Item;
    }

    int c(String string) {
        this.g.a(30, string, null, null);
        $Item $Item = this.a(this.g);
        if ($Item == null) {
            $Item = this.c(this.g);
        }
        return $Item.a;
    }

    int a(String string, int n2) {
        this.g.b = 31;
        this.g.c = n2;
        this.g.g = string;
        this.g.j = Integer.MAX_VALUE & 31 + string.hashCode() + n2;
        $Item $Item = this.a(this.g);
        if ($Item == null) {
            $Item = this.c(this.g);
        }
        return $Item.a;
    }

    private $Item c($Item $Item) {
        this.G = (short)(this.G + 1);
        $Item $Item2 = new $Item(this.G, this.g);
        this.b($Item2);
        if (this.H == null) {
            this.H = new $Item[16];
        }
        if (this.G == this.H.length) {
            $Item[] arr$Item = new $Item[2 * this.H.length];
            System.arraycopy(this.H, 0, arr$Item, 0, this.H.length);
            this.H = arr$Item;
        }
        this.H[this.G] = $Item2;
        return $Item2;
    }

    int a(int n2, int n3) {
        this.h.b = 32;
        this.h.d = (long)n2 | (long)n3 << 32;
        this.h.j = Integer.MAX_VALUE & 32 + n2 + n3;
        $Item $Item = this.a(this.h);
        if ($Item == null) {
            String string = this.H[n2].g;
            String string2 = this.H[n3].g;
            this.h.c = this.c(this.getCommonSuperClass(string, string2));
            $Item = new $Item(0, this.h);
            this.b($Item);
        }
        return $Item.c;
    }

    protected String getCommonSuperClass(String string, String string2) {
        Class class_;
        Class class_2;
        ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            class_2 = Class.forName(string.replace('/', '.'), false, classLoader);
            class_ = Class.forName(string2.replace('/', '.'), false, classLoader);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
        if (class_2.isAssignableFrom(class_)) {
            return string;
        }
        if (class_.isAssignableFrom(class_2)) {
            return string2;
        }
        if (class_2.isInterface() || class_.isInterface()) {
            return "java/lang/Object";
        }
        while (!(class_2 = class_2.getSuperclass()).isAssignableFrom(class_)) {
        }
        return class_2.getName().replace('.', '/');
    }

    private $Item a($Item $Item) {
        $Item $Item2 = this.e[$Item.j % this.e.length];
        while (!($Item2 == null || $Item2.b == $Item.b && $Item.a($Item2))) {
            $Item2 = $Item2.k;
        }
        return $Item2;
    }

    private void b($Item $Item) {
        int n2;
        if (this.c + this.G > this.f) {
            n2 = this.e.length;
            int n3 = n2 * 2 + 1;
            $Item[] arr$Item = new $Item[n3];
            for (int i2 = n2 - 1; i2 >= 0; --i2) {
                $Item $Item2 = this.e[i2];
                while ($Item2 != null) {
                    int n4 = $Item2.j % arr$Item.length;
                    $Item $Item3 = $Item2.k;
                    $Item2.k = arr$Item[n4];
                    arr$Item[n4] = $Item2;
                    $Item2 = $Item3;
                }
            }
            this.e = arr$Item;
            this.f = (int)((double)n3 * 0.75);
        }
        n2 = $Item.j % this.e.length;
        $Item.k = this.e[n2];
        this.e[n2] = $Item;
    }

    private void a(int n2, int n3, int n4) {
        this.d.b(n2, n3).putShort(n4);
    }

    private void b(int n2, int n3, int n4) {
        this.d.a(n2, n3).putShort(n4);
    }

    static {
        $ClassWriter._clinit_();
        byte[] arrby = new byte[220];
        String string = "AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKJJJJJJJJJJJJJJJJJJ";
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby[i2] = (byte)(string.charAt(i2) - 65);
        }
        a = arrby;
    }

    static void _clinit_() {
    }
}

