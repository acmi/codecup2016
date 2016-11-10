/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.$AnnotationVisitor;
import com.google.inject.internal.asm.$Attribute;
import com.google.inject.internal.asm.$FieldVisitor;
import com.google.inject.internal.asm.$MethodVisitor;
import com.google.inject.internal.asm.$TypePath;

public abstract class $ClassVisitor {
    protected final int api;
    protected $ClassVisitor cv;

    public $ClassVisitor(int n2) {
        this(n2, null);
    }

    public $ClassVisitor(int n2, $ClassVisitor $ClassVisitor) {
        if (n2 != 262144 && n2 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = n2;
        this.cv = $ClassVisitor;
    }

    public void visit(int n2, int n3, String string, String string2, String string3, String[] arrstring) {
        if (this.cv != null) {
            this.cv.visit(n2, n3, string, string2, string3, arrstring);
        }
    }

    public void visitSource(String string, String string2) {
        if (this.cv != null) {
            this.cv.visitSource(string, string2);
        }
    }

    public void visitOuterClass(String string, String string2, String string3) {
        if (this.cv != null) {
            this.cv.visitOuterClass(string, string2, string3);
        }
    }

    public $AnnotationVisitor visitAnnotation(String string, boolean bl) {
        if (this.cv != null) {
            return this.cv.visitAnnotation(string, bl);
        }
        return null;
    }

    public $AnnotationVisitor visitTypeAnnotation(int n2, $TypePath $TypePath, String string, boolean bl) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.cv != null) {
            return this.cv.visitTypeAnnotation(n2, $TypePath, string, bl);
        }
        return null;
    }

    public void visitAttribute($Attribute $Attribute) {
        if (this.cv != null) {
            this.cv.visitAttribute($Attribute);
        }
    }

    public void visitInnerClass(String string, String string2, String string3, int n2) {
        if (this.cv != null) {
            this.cv.visitInnerClass(string, string2, string3, n2);
        }
    }

    public $FieldVisitor visitField(int n2, String string, String string2, String string3, Object object) {
        if (this.cv != null) {
            return this.cv.visitField(n2, string, string2, string3, object);
        }
        return null;
    }

    public $MethodVisitor visitMethod(int n2, String string, String string2, String string3, String[] arrstring) {
        if (this.cv != null) {
            return this.cv.visitMethod(n2, string, string2, string3, arrstring);
        }
        return null;
    }

    public void visitEnd() {
        if (this.cv != null) {
            this.cv.visitEnd();
        }
    }
}

