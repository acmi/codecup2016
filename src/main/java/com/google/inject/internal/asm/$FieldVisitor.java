/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.$AnnotationVisitor;
import com.google.inject.internal.asm.$Attribute;
import com.google.inject.internal.asm.$TypePath;

public abstract class $FieldVisitor {
    protected final int api;
    protected $FieldVisitor fv;

    public $FieldVisitor(int n2) {
        this(n2, null);
    }

    public $FieldVisitor(int n2, $FieldVisitor $FieldVisitor) {
        if (n2 != 262144 && n2 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = n2;
        this.fv = $FieldVisitor;
    }

    public $AnnotationVisitor visitAnnotation(String string, boolean bl) {
        if (this.fv != null) {
            return this.fv.visitAnnotation(string, bl);
        }
        return null;
    }

    public $AnnotationVisitor visitTypeAnnotation(int n2, $TypePath $TypePath, String string, boolean bl) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.fv != null) {
            return this.fv.visitTypeAnnotation(n2, $TypePath, string, bl);
        }
        return null;
    }

    public void visitAttribute($Attribute $Attribute) {
        if (this.fv != null) {
            this.fv.visitAttribute($Attribute);
        }
    }

    public void visitEnd() {
        if (this.fv != null) {
            this.fv.visitEnd();
        }
    }
}

