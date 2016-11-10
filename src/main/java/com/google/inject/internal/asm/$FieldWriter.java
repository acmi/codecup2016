/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.$AnnotationVisitor;
import com.google.inject.internal.asm.$AnnotationWriter;
import com.google.inject.internal.asm.$Attribute;
import com.google.inject.internal.asm.$ByteVector;
import com.google.inject.internal.asm.$ClassWriter;
import com.google.inject.internal.asm.$FieldVisitor;
import com.google.inject.internal.asm.$Item;
import com.google.inject.internal.asm.$TypePath;

final class $FieldWriter
extends $FieldVisitor {
    private final $ClassWriter b;
    private final int c;
    private final int d;
    private final int e;
    private int f;
    private int g;
    private $AnnotationWriter h;
    private $AnnotationWriter i;
    private $AnnotationWriter k;
    private $AnnotationWriter l;
    private $Attribute j;

    $FieldWriter($ClassWriter $ClassWriter, int n2, String string, String string2, String string3, Object object) {
        super(327680);
        if ($ClassWriter.B == null) {
            $ClassWriter.B = this;
        } else {
            $ClassWriter.C.fv = this;
        }
        $ClassWriter.C = this;
        this.b = $ClassWriter;
        this.c = n2;
        this.d = $ClassWriter.newUTF8(string);
        this.e = $ClassWriter.newUTF8(string2);
        if (string3 != null) {
            this.f = $ClassWriter.newUTF8(string3);
        }
        if (object != null) {
            this.g = $ClassWriter.a((Object)object).a;
        }
    }

    public $AnnotationVisitor visitAnnotation(String string, boolean bl) {
        $ByteVector $ByteVector = new $ByteVector();
        $ByteVector.putShort(this.b.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this.b, true, $ByteVector, $ByteVector, 2);
        if (bl) {
            $AnnotationWriter.g = this.h;
            this.h = $AnnotationWriter;
        } else {
            $AnnotationWriter.g = this.i;
            this.i = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public $AnnotationVisitor visitTypeAnnotation(int n2, $TypePath $TypePath, String string, boolean bl) {
        $ByteVector $ByteVector = new $ByteVector();
        $AnnotationWriter.a(n2, $TypePath, $ByteVector);
        $ByteVector.putShort(this.b.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this.b, true, $ByteVector, $ByteVector, $ByteVector.b - 2);
        if (bl) {
            $AnnotationWriter.g = this.k;
            this.k = $AnnotationWriter;
        } else {
            $AnnotationWriter.g = this.l;
            this.l = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public void visitAttribute($Attribute $Attribute) {
        $Attribute.a = this.j;
        this.j = $Attribute;
    }

    public void visitEnd() {
    }

    int a() {
        int n2 = 8;
        if (this.g != 0) {
            this.b.newUTF8("ConstantValue");
            n2 += 8;
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            this.b.newUTF8("Synthetic");
            n2 += 6;
        }
        if ((this.c & 131072) != 0) {
            this.b.newUTF8("Deprecated");
            n2 += 6;
        }
        if (this.f != 0) {
            this.b.newUTF8("Signature");
            n2 += 8;
        }
        if (this.h != null) {
            this.b.newUTF8("RuntimeVisibleAnnotations");
            n2 += 8 + this.h.a();
        }
        if (this.i != null) {
            this.b.newUTF8("RuntimeInvisibleAnnotations");
            n2 += 8 + this.i.a();
        }
        if (this.k != null) {
            this.b.newUTF8("RuntimeVisibleTypeAnnotations");
            n2 += 8 + this.k.a();
        }
        if (this.l != null) {
            this.b.newUTF8("RuntimeInvisibleTypeAnnotations");
            n2 += 8 + this.l.a();
        }
        if (this.j != null) {
            n2 += this.j.a(this.b, null, 0, -1, -1);
        }
        return n2;
    }

    void a($ByteVector $ByteVector) {
        int n2 = 64;
        int n3 = 393216 | (this.c & 262144) / 64;
        $ByteVector.putShort(this.c & ~ n3).putShort(this.d).putShort(this.e);
        int n4 = 0;
        if (this.g != 0) {
            ++n4;
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            ++n4;
        }
        if ((this.c & 131072) != 0) {
            ++n4;
        }
        if (this.f != 0) {
            ++n4;
        }
        if (this.h != null) {
            ++n4;
        }
        if (this.i != null) {
            ++n4;
        }
        if (this.k != null) {
            ++n4;
        }
        if (this.l != null) {
            ++n4;
        }
        if (this.j != null) {
            n4 += this.j.a();
        }
        $ByteVector.putShort(n4);
        if (this.g != 0) {
            $ByteVector.putShort(this.b.newUTF8("ConstantValue"));
            $ByteVector.putInt(2).putShort(this.g);
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            $ByteVector.putShort(this.b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.c & 131072) != 0) {
            $ByteVector.putShort(this.b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.f != 0) {
            $ByteVector.putShort(this.b.newUTF8("Signature"));
            $ByteVector.putInt(2).putShort(this.f);
        }
        if (this.h != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeVisibleAnnotations"));
            this.h.a($ByteVector);
        }
        if (this.i != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeInvisibleAnnotations"));
            this.i.a($ByteVector);
        }
        if (this.k != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.k.a($ByteVector);
        }
        if (this.l != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.l.a($ByteVector);
        }
        if (this.j != null) {
            this.j.a(this.b, null, 0, -1, -1, $ByteVector);
        }
    }
}

