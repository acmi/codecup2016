/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.$AnnotationVisitor;
import com.google.inject.internal.asm.$AnnotationWriter;
import com.google.inject.internal.asm.$Attribute;
import com.google.inject.internal.asm.$ByteVector;
import com.google.inject.internal.asm.$ClassReader;
import com.google.inject.internal.asm.$ClassWriter;
import com.google.inject.internal.asm.$Edge;
import com.google.inject.internal.asm.$Frame;
import com.google.inject.internal.asm.$Handle;
import com.google.inject.internal.asm.$Handler;
import com.google.inject.internal.asm.$Item;
import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$MethodVisitor;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.asm.$TypePath;

class $MethodWriter
extends $MethodVisitor {
    final $ClassWriter b;
    private int c;
    private final int d;
    private final int e;
    private final String f;
    String g;
    int h;
    int i;
    int j;
    int[] k;
    private $ByteVector l;
    private $AnnotationWriter m;
    private $AnnotationWriter n;
    private $AnnotationWriter U;
    private $AnnotationWriter V;
    private $AnnotationWriter[] o;
    private $AnnotationWriter[] p;
    private int S;
    private $Attribute q;
    private $ByteVector r = new $ByteVector();
    private int s;
    private int t;
    private int T;
    private int u;
    private $ByteVector v;
    private int w;
    private int[] x;
    private int[] z;
    private int A;
    private $Handler B;
    private $Handler C;
    private int Z;
    private $ByteVector $;
    private int D;
    private $ByteVector E;
    private int F;
    private $ByteVector G;
    private int H;
    private $ByteVector I;
    private int Y;
    private $AnnotationWriter W;
    private $AnnotationWriter X;
    private $Attribute J;
    private boolean K;
    private int L;
    private final int M;
    private $Label N;
    private $Label O;
    private $Label P;
    private int Q;
    private int R;

    $MethodWriter($ClassWriter $ClassWriter, int n2, String string, String string2, String string3, String[] arrstring, boolean bl, boolean bl2) {
        int n3;
        super(327680);
        if ($ClassWriter.D == null) {
            $ClassWriter.D = this;
        } else {
            $ClassWriter.E.mv = this;
        }
        $ClassWriter.E = this;
        this.b = $ClassWriter;
        this.c = n2;
        if ("<init>".equals(string)) {
            this.c |= 524288;
        }
        this.d = $ClassWriter.newUTF8(string);
        this.e = $ClassWriter.newUTF8(string2);
        this.f = string2;
        this.g = string3;
        if (arrstring != null && arrstring.length > 0) {
            this.j = arrstring.length;
            this.k = new int[this.j];
            for (n3 = 0; n3 < this.j; ++n3) {
                this.k[n3] = $ClassWriter.newClass(arrstring[n3]);
            }
        }
        int n4 = bl2 ? 0 : (this.M = bl ? 1 : 2);
        if (bl || bl2) {
            n3 = $Type.getArgumentsAndReturnSizes(this.f) >> 2;
            if ((n2 & 8) != 0) {
                --n3;
            }
            this.t = n3;
            this.T = n3;
            this.N = new $Label();
            this.N.a |= 8;
            this.visitLabel(this.N);
        }
    }

    public void visitParameter(String string, int n2) {
        if (this.$ == null) {
            this.$ = new $ByteVector();
        }
        ++this.Z;
        this.$.putShort(string == null ? 0 : this.b.newUTF8(string)).putShort(n2);
    }

    public $AnnotationVisitor visitAnnotationDefault() {
        this.l = new $ByteVector();
        return new $AnnotationWriter(this.b, false, this.l, null, 0);
    }

    public $AnnotationVisitor visitAnnotation(String string, boolean bl) {
        $ByteVector $ByteVector = new $ByteVector();
        $ByteVector.putShort(this.b.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this.b, true, $ByteVector, $ByteVector, 2);
        if (bl) {
            $AnnotationWriter.g = this.m;
            this.m = $AnnotationWriter;
        } else {
            $AnnotationWriter.g = this.n;
            this.n = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public $AnnotationVisitor visitTypeAnnotation(int n2, $TypePath $TypePath, String string, boolean bl) {
        $ByteVector $ByteVector = new $ByteVector();
        $AnnotationWriter.a(n2, $TypePath, $ByteVector);
        $ByteVector.putShort(this.b.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this.b, true, $ByteVector, $ByteVector, $ByteVector.b - 2);
        if (bl) {
            $AnnotationWriter.g = this.U;
            this.U = $AnnotationWriter;
        } else {
            $AnnotationWriter.g = this.V;
            this.V = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public $AnnotationVisitor visitParameterAnnotation(int n2, String string, boolean bl) {
        $ByteVector $ByteVector = new $ByteVector();
        if ("Ljava/lang/Synthetic;".equals(string)) {
            this.S = Math.max(this.S, n2 + 1);
            return new $AnnotationWriter(this.b, false, $ByteVector, null, 0);
        }
        $ByteVector.putShort(this.b.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this.b, true, $ByteVector, $ByteVector, 2);
        if (bl) {
            if (this.o == null) {
                this.o = new $AnnotationWriter[$Type.getArgumentTypes(this.f).length];
            }
            $AnnotationWriter.g = this.o[n2];
            this.o[n2] = $AnnotationWriter;
        } else {
            if (this.p == null) {
                this.p = new $AnnotationWriter[$Type.getArgumentTypes(this.f).length];
            }
            $AnnotationWriter.g = this.p[n2];
            this.p[n2] = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public void visitAttribute($Attribute $Attribute) {
        if ($Attribute.isCodeAttribute()) {
            $Attribute.a = this.J;
            this.J = $Attribute;
        } else {
            $Attribute.a = this.q;
            this.q = $Attribute;
        }
    }

    public void visitCode() {
    }

    public void visitFrame(int n2, int n3, Object[] arrobject, int n4, Object[] arrobject2) {
        if (this.M == 0) {
            return;
        }
        if (n2 == -1) {
            int n5;
            if (this.x == null) {
                this.f();
            }
            this.T = n3;
            int n6 = this.a(this.r.b, n3, n4);
            for (n5 = 0; n5 < n3; ++n5) {
                this.z[n6++] = arrobject[n5] instanceof String ? 24117248 | this.b.c((String)arrobject[n5]) : (arrobject[n5] instanceof Integer ? (Integer)arrobject[n5] : 25165824 | this.b.a("", (($Label)arrobject[n5]).c));
            }
            for (n5 = 0; n5 < n4; ++n5) {
                this.z[n6++] = arrobject2[n5] instanceof String ? 24117248 | this.b.c((String)arrobject2[n5]) : (arrobject2[n5] instanceof Integer ? (Integer)arrobject2[n5] : 25165824 | this.b.a("", (($Label)arrobject2[n5]).c));
            }
            this.b();
        } else {
            int n7;
            if (this.v == null) {
                this.v = new $ByteVector();
                n7 = this.r.b;
            } else {
                n7 = this.r.b - this.w - 1;
                if (n7 < 0) {
                    if (n2 == 3) {
                        return;
                    }
                    throw new IllegalStateException();
                }
            }
            switch (n2) {
                case 0: {
                    int n8;
                    this.T = n3;
                    this.v.putByte(255).putShort(n7).putShort(n3);
                    for (n8 = 0; n8 < n3; ++n8) {
                        this.a(arrobject[n8]);
                    }
                    this.v.putShort(n4);
                    for (n8 = 0; n8 < n4; ++n8) {
                        this.a(arrobject2[n8]);
                    }
                    break;
                }
                case 1: {
                    this.T += n3;
                    this.v.putByte(251 + n3).putShort(n7);
                    for (int i2 = 0; i2 < n3; ++i2) {
                        this.a(arrobject[i2]);
                    }
                    break;
                }
                case 2: {
                    this.T -= n3;
                    this.v.putByte(251 - n3).putShort(n7);
                    break;
                }
                case 3: {
                    if (n7 < 64) {
                        this.v.putByte(n7);
                        break;
                    }
                    this.v.putByte(251).putShort(n7);
                    break;
                }
                case 4: {
                    if (n7 < 64) {
                        this.v.putByte(64 + n7);
                    } else {
                        this.v.putByte(247).putShort(n7);
                    }
                    this.a(arrobject2[0]);
                }
            }
            this.w = this.r.b;
            ++this.u;
        }
        this.s = Math.max(this.s, n4);
        this.t = Math.max(this.t, this.T);
    }

    public void visitInsn(int n2) {
        this.Y = this.r.b;
        this.r.putByte(n2);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, 0, null, null);
            } else {
                int n3 = this.Q + $Frame.a[n2];
                if (n3 > this.R) {
                    this.R = n3;
                }
                this.Q = n3;
            }
            if (n2 >= 172 && n2 <= 177 || n2 == 191) {
                this.e();
            }
        }
    }

    public void visitIntInsn(int n2, int n3) {
        this.Y = this.r.b;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, n3, null, null);
            } else if (n2 != 188) {
                int n4 = this.Q + 1;
                if (n4 > this.R) {
                    this.R = n4;
                }
                this.Q = n4;
            }
        }
        if (n2 == 17) {
            this.r.b(n2, n3);
        } else {
            this.r.a(n2, n3);
        }
    }

    public void visitVarInsn(int n2, int n3) {
        int n4;
        this.Y = this.r.b;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, n3, null, null);
            } else if (n2 == 169) {
                this.P.a |= 256;
                this.P.f = this.Q;
                this.e();
            } else {
                n4 = this.Q + $Frame.a[n2];
                if (n4 > this.R) {
                    this.R = n4;
                }
                this.Q = n4;
            }
        }
        if (this.M != 2) {
            n4 = n2 == 22 || n2 == 24 || n2 == 55 || n2 == 57 ? n3 + 2 : n3 + 1;
            if (n4 > this.t) {
                this.t = n4;
            }
        }
        if (n3 < 4 && n2 != 169) {
            n4 = n2 < 54 ? 26 + (n2 - 21 << 2) + n3 : 59 + (n2 - 54 << 2) + n3;
            this.r.putByte(n4);
        } else if (n3 >= 256) {
            this.r.putByte(196).b(n2, n3);
        } else {
            this.r.a(n2, n3);
        }
        if (n2 >= 54 && this.M == 0 && this.A > 0) {
            this.visitLabel(new $Label());
        }
    }

    public void visitTypeInsn(int n2, String string) {
        this.Y = this.r.b;
        $Item $Item = this.b.a(string);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, this.r.b, this.b, $Item);
            } else if (n2 == 187) {
                int n3 = this.Q + 1;
                if (n3 > this.R) {
                    this.R = n3;
                }
                this.Q = n3;
            }
        }
        this.r.b(n2, $Item.a);
    }

    public void visitFieldInsn(int n2, String string, String string2, String string3) {
        this.Y = this.r.b;
        $Item $Item = this.b.a(string, string2, string3);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, 0, this.b, $Item);
            } else {
                int n3;
                char c2 = string3.charAt(0);
                switch (n2) {
                    case 178: {
                        n3 = this.Q + (c2 == 'D' || c2 == 'J' ? 2 : 1);
                        break;
                    }
                    case 179: {
                        n3 = this.Q + (c2 == 'D' || c2 == 'J' ? -2 : -1);
                        break;
                    }
                    case 180: {
                        n3 = this.Q + (c2 == 'D' || c2 == 'J' ? 1 : 0);
                        break;
                    }
                    default: {
                        n3 = this.Q + (c2 == 'D' || c2 == 'J' ? -3 : -2);
                    }
                }
                if (n3 > this.R) {
                    this.R = n3;
                }
                this.Q = n3;
            }
        }
        this.r.b(n2, $Item.a);
    }

    public void visitMethodInsn(int n2, String string, String string2, String string3, boolean bl) {
        this.Y = this.r.b;
        $Item $Item = this.b.a(string, string2, string3, bl);
        int n3 = $Item.c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, 0, this.b, $Item);
            } else {
                if (n3 == 0) {
                    $Item.c = n3 = $Type.getArgumentsAndReturnSizes(string3);
                }
                int n4 = n2 == 184 ? this.Q - (n3 >> 2) + (n3 & 3) + 1 : this.Q - (n3 >> 2) + (n3 & 3);
                if (n4 > this.R) {
                    this.R = n4;
                }
                this.Q = n4;
            }
        }
        if (n2 == 185) {
            if (n3 == 0) {
                $Item.c = n3 = $Type.getArgumentsAndReturnSizes(string3);
            }
            this.r.b(185, $Item.a).a(n3 >> 2, 0);
        } else {
            this.r.b(n2, $Item.a);
        }
    }

    public /* varargs */ void visitInvokeDynamicInsn(String string, String string2, $Handle $Handle, Object ... arrobject) {
        this.Y = this.r.b;
        $Item $Item = this.b.a(string, string2, $Handle, arrobject);
        int n2 = $Item.c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(186, 0, this.b, $Item);
            } else {
                int n3;
                if (n2 == 0) {
                    $Item.c = n2 = $Type.getArgumentsAndReturnSizes(string2);
                }
                if ((n3 = this.Q - (n2 >> 2) + (n2 & 3) + 1) > this.R) {
                    this.R = n3;
                }
                this.Q = n3;
            }
        }
        this.r.b(186, $Item.a);
        this.r.putShort(0);
    }

    public void visitJumpInsn(int n2, $Label $Label) {
        this.Y = this.r.b;
        $Label $Label2 = null;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(n2, 0, null, null);
                $Label.a().a |= 16;
                this.a(0, $Label);
                if (n2 != 167) {
                    $Label2 = new $Label();
                }
            } else if (n2 == 168) {
                if (($Label.a & 512) == 0) {
                    $Label.a |= 512;
                    ++this.L;
                }
                this.P.a |= 128;
                this.a(this.Q + 1, $Label);
                $Label2 = new $Label();
            } else {
                this.Q += $Frame.a[n2];
                this.a(this.Q, $Label);
            }
        }
        if (($Label.a & 2) != 0 && $Label.c - this.r.b < -32768) {
            if (n2 == 167) {
                this.r.putByte(200);
            } else if (n2 == 168) {
                this.r.putByte(201);
            } else {
                if ($Label2 != null) {
                    $Label2.a |= 16;
                }
                this.r.putByte(n2 <= 166 ? (n2 + 1 ^ 1) - 1 : n2 ^ 1);
                this.r.putShort(8);
                this.r.putByte(200);
            }
            $Label.a(this, this.r, this.r.b - 1, true);
        } else {
            this.r.putByte(n2);
            $Label.a(this, this.r, this.r.b - 1, false);
        }
        if (this.P != null) {
            if ($Label2 != null) {
                this.visitLabel($Label2);
            }
            if (n2 == 167) {
                this.e();
            }
        }
    }

    public void visitLabel($Label $Label) {
        this.K |= $Label.a(this, this.r.b, this.r.a);
        if (($Label.a & 1) != 0) {
            return;
        }
        if (this.M == 0) {
            if (this.P != null) {
                if ($Label.c == this.P.c) {
                    this.P.a |= $Label.a & 16;
                    $Label.h = this.P.h;
                    return;
                }
                this.a(0, $Label);
            }
            this.P = $Label;
            if ($Label.h == null) {
                $Label.h = new $Frame();
                $Label.h.b = $Label;
            }
            if (this.O != null) {
                if ($Label.c == this.O.c) {
                    this.O.a |= $Label.a & 16;
                    $Label.h = this.O.h;
                    this.P = this.O;
                    return;
                }
                this.O.i = $Label;
            }
            this.O = $Label;
        } else if (this.M == 1) {
            if (this.P != null) {
                this.P.g = this.R;
                this.a(this.Q, $Label);
            }
            this.P = $Label;
            this.Q = 0;
            this.R = 0;
            if (this.O != null) {
                this.O.i = $Label;
            }
            this.O = $Label;
        }
    }

    public void visitLdcInsn(Object object) {
        int n2;
        this.Y = this.r.b;
        $Item $Item = this.b.a(object);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(18, 0, this.b, $Item);
            } else {
                n2 = $Item.b == 5 || $Item.b == 6 ? this.Q + 2 : this.Q + 1;
                if (n2 > this.R) {
                    this.R = n2;
                }
                this.Q = n2;
            }
        }
        n2 = $Item.a;
        if ($Item.b == 5 || $Item.b == 6) {
            this.r.b(20, n2);
        } else if (n2 >= 256) {
            this.r.b(19, n2);
        } else {
            this.r.a(18, n2);
        }
    }

    public void visitIincInsn(int n2, int n3) {
        int n4;
        this.Y = this.r.b;
        if (this.P != null && this.M == 0) {
            this.P.h.a(132, n2, null, null);
        }
        if (this.M != 2 && (n4 = n2 + 1) > this.t) {
            this.t = n4;
        }
        if (n2 > 255 || n3 > 127 || n3 < -128) {
            this.r.putByte(196).b(132, n2).putShort(n3);
        } else {
            this.r.putByte(132).a(n2, n3);
        }
    }

    public /* varargs */ void visitTableSwitchInsn(int n2, int n3, $Label $Label, $Label ... arr$Label) {
        this.Y = this.r.b;
        int n4 = this.r.b;
        this.r.putByte(170);
        this.r.putByteArray(null, 0, (4 - this.r.b % 4) % 4);
        $Label.a(this, this.r, n4, true);
        this.r.putInt(n2).putInt(n3);
        for (int i2 = 0; i2 < arr$Label.length; ++i2) {
            arr$Label[i2].a(this, this.r, n4, true);
        }
        this.a($Label, arr$Label);
    }

    public void visitLookupSwitchInsn($Label $Label, int[] arrn, $Label[] arr$Label) {
        this.Y = this.r.b;
        int n2 = this.r.b;
        this.r.putByte(171);
        this.r.putByteArray(null, 0, (4 - this.r.b % 4) % 4);
        $Label.a(this, this.r, n2, true);
        this.r.putInt(arr$Label.length);
        for (int i2 = 0; i2 < arr$Label.length; ++i2) {
            this.r.putInt(arrn[i2]);
            arr$Label[i2].a(this, this.r, n2, true);
        }
        this.a($Label, arr$Label);
    }

    private void a($Label $Label, $Label[] arr$Label) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(171, 0, null, null);
                this.a(0, $Label);
                $Label.a().a |= 16;
                for (int i2 = 0; i2 < arr$Label.length; ++i2) {
                    this.a(0, arr$Label[i2]);
                    arr$Label[i2].a().a |= 16;
                }
            } else {
                --this.Q;
                this.a(this.Q, $Label);
                for (int i3 = 0; i3 < arr$Label.length; ++i3) {
                    this.a(this.Q, arr$Label[i3]);
                }
            }
            this.e();
        }
    }

    public void visitMultiANewArrayInsn(String string, int n2) {
        this.Y = this.r.b;
        $Item $Item = this.b.a(string);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(197, n2, this.b, $Item);
            } else {
                this.Q += 1 - n2;
            }
        }
        this.r.b(197, $Item.a).putByte(n2);
    }

    public $AnnotationVisitor visitInsnAnnotation(int n2, $TypePath $TypePath, String string, boolean bl) {
        $ByteVector $ByteVector = new $ByteVector();
        n2 = n2 & -16776961 | this.Y << 8;
        $AnnotationWriter.a(n2, $TypePath, $ByteVector);
        $ByteVector.putShort(this.b.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this.b, true, $ByteVector, $ByteVector, $ByteVector.b - 2);
        if (bl) {
            $AnnotationWriter.g = this.W;
            this.W = $AnnotationWriter;
        } else {
            $AnnotationWriter.g = this.X;
            this.X = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public void visitTryCatchBlock($Label $Label, $Label $Label2, $Label $Label3, String string) {
        ++this.A;
        $Handler $Handler = new $Handler();
        $Handler.a = $Label;
        $Handler.b = $Label2;
        $Handler.c = $Label3;
        $Handler.d = string;
        int n2 = $Handler.e = string != null ? this.b.newClass(string) : 0;
        if (this.C == null) {
            this.B = $Handler;
        } else {
            this.C.f = $Handler;
        }
        this.C = $Handler;
    }

    public $AnnotationVisitor visitTryCatchAnnotation(int n2, $TypePath $TypePath, String string, boolean bl) {
        $ByteVector $ByteVector = new $ByteVector();
        $AnnotationWriter.a(n2, $TypePath, $ByteVector);
        $ByteVector.putShort(this.b.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this.b, true, $ByteVector, $ByteVector, $ByteVector.b - 2);
        if (bl) {
            $AnnotationWriter.g = this.W;
            this.W = $AnnotationWriter;
        } else {
            $AnnotationWriter.g = this.X;
            this.X = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public void visitLocalVariable(String string, String string2, String string3, $Label $Label, $Label $Label2, int n2) {
        char c2;
        int n3;
        if (string3 != null) {
            if (this.G == null) {
                this.G = new $ByteVector();
            }
            ++this.F;
            this.G.putShort($Label.c).putShort($Label2.c - $Label.c).putShort(this.b.newUTF8(string)).putShort(this.b.newUTF8(string3)).putShort(n2);
        }
        if (this.E == null) {
            this.E = new $ByteVector();
        }
        ++this.D;
        this.E.putShort($Label.c).putShort($Label2.c - $Label.c).putShort(this.b.newUTF8(string)).putShort(this.b.newUTF8(string2)).putShort(n2);
        if (this.M != 2 && (n3 = n2 + ((c2 = string2.charAt(0)) == 'J' || c2 == 'D' ? 2 : 1)) > this.t) {
            this.t = n3;
        }
    }

    public $AnnotationVisitor visitLocalVariableAnnotation(int n2, $TypePath $TypePath, $Label[] arr$Label, $Label[] arr$Label2, int[] arrn, String string, boolean bl) {
        int n3;
        $ByteVector $ByteVector = new $ByteVector();
        $ByteVector.putByte(n2 >>> 24).putShort(arr$Label.length);
        for (n3 = 0; n3 < arr$Label.length; ++n3) {
            $ByteVector.putShort(arr$Label[n3].c).putShort(arr$Label2[n3].c - arr$Label[n3].c).putShort(arrn[n3]);
        }
        if ($TypePath == null) {
            $ByteVector.putByte(0);
        } else {
            n3 = $TypePath.a[$TypePath.b] * 2 + 1;
            $ByteVector.putByteArray($TypePath.a, $TypePath.b, n3);
        }
        $ByteVector.putShort(this.b.newUTF8(string)).putShort(0);
        $AnnotationWriter $AnnotationWriter = new $AnnotationWriter(this.b, true, $ByteVector, $ByteVector, $ByteVector.b - 2);
        if (bl) {
            $AnnotationWriter.g = this.W;
            this.W = $AnnotationWriter;
        } else {
            $AnnotationWriter.g = this.X;
            this.X = $AnnotationWriter;
        }
        return $AnnotationWriter;
    }

    public void visitLineNumber(int n2, $Label $Label) {
        if (this.I == null) {
            this.I = new $ByteVector();
        }
        ++this.H;
        this.I.putShort($Label.c);
        this.I.putShort(n2);
    }

    public void visitMaxs(int n2, int n3) {
        if (this.K) {
            this.d();
        }
        if (this.M == 0) {
            Object object;
            Object object3;
            $Edge $Edge /* !! */ ;
            int n4;
            $Type[] arr$Type;
            Object object2;
            $Handler $Handler = this.B;
            while ($Handler != null) {
                object3 = $Handler.a.a();
                arr$Type = $Handler.c.a();
                $Label $Label = $Handler.b.a();
                object2 = $Handler.d == null ? "java/lang/Throwable" : $Handler.d;
                int n5 = 24117248 | this.b.c((String)object2);
                arr$Type.a |= 16;
                while (object3 != $Label) {
                    $Edge /* !! */  = new $Edge();
                    $Edge /* !! */ .a = n5;
                    $Edge /* !! */ .b = arr$Type;
                    $Edge /* !! */ .c = object3.j;
                    object3.j = $Edge /* !! */ ;
                    object3 = object3.i;
                }
                $Handler = $Handler.f;
            }
            object3 = this.N.h;
            arr$Type = $Type.getArgumentTypes(this.f);
            object3.a(this.b, this.c, arr$Type, this.t);
            this.b(($Frame)object3);
            Object object4 = 0;
            object2 = this.N;
            while (object2 != null) {
                Object object5 = object2;
                object2 = object2.k;
                object5.k = null;
                object3 = object5.h;
                if ((object5.a & 16) != 0) {
                    object5.a |= 32;
                }
                object5.a |= 64;
                $Edge /* !! */  = ($Edge)(object3.d.length + object5.g);
                if ($Edge /* !! */  > object4) {
                    object4 = $Edge /* !! */ ;
                }
                $Edge $Edge2 = object5.j;
                while ($Edge2 != null) {
                    object = $Edge2.b.a();
                    n4 = (int)object3.a(this.b, object.h, $Edge2.a) ? 1 : 0;
                    if (n4 != 0 && object.k == null) {
                        object.k = object2;
                        object2 = object;
                    }
                    $Edge2 = $Edge2.c;
                }
            }
            $Label $Label = this.N;
            while ($Label != null) {
                $Label $Label2;
                int n5;
                object3 = $Label.h;
                if (($Label.a & 32) != 0) {
                    this.b(($Frame)object3);
                }
                if (($Label.a & 64) == 0 && (object = (($Label2 = $Label.i) == null ? this.r.b : $Label2.c) - 1) >= (n5 = $Label.c)) {
                    object4 = Math.max((int)object4, 1);
                    for (n4 = n5; n4 < object; ++n4) {
                        this.r.a[n4] = 0;
                    }
                    this.r.a[object] = -65;
                    n4 = this.a(n5, 0, 1);
                    this.z[n4] = 24117248 | this.b.c("java/lang/Throwable");
                    this.b();
                    this.B = $Handler.a(this.B, $Label, $Label2);
                }
                $Label = $Label.i;
            }
            $Handler = this.B;
            this.A = 0;
            while ($Handler != null) {
                ++this.A;
                $Handler = $Handler.f;
            }
            this.s = object4;
        } else if (this.M == 1) {
            Object object;
            $Label $Label;
            $Label $Label3;
            $Handler $Handler = this.B;
            while ($Handler != null) {
                $Label $Label4 = $Handler.a;
                $Label3 = $Handler.c;
                $Label = $Handler.b;
                while ($Label4 != $Label) {
                    object = new $Edge();
                    object.a = Integer.MAX_VALUE;
                    object.b = $Label3;
                    if (($Label4.a & 128) == 0) {
                        object.c = $Label4.j;
                        $Label4.j = object;
                    } else {
                        object.c = $Label4.j.c.c;
                        $Label4.j.c.c = object;
                    }
                    $Label4 = $Label4.i;
                }
                $Handler = $Handler.f;
            }
            if (this.L > 0) {
                int n7 = 0;
                this.N.b(null, 1, this.L);
                $Label3 = this.N;
                while ($Label3 != null) {
                    if (($Label3.a & 128) != 0) {
                        $Label = $Label3.j.c.b;
                        if (($Label.a & 1024) == 0) {
                            $Label.b(null, (long)n7 / 32 << 32 | 1 << ++n7 % 32, this.L);
                        }
                    }
                    $Label3 = $Label3.i;
                }
                $Label3 = this.N;
                while ($Label3 != null) {
                    if (($Label3.a & 128) != 0) {
                        $Label = this.N;
                        while ($Label != null) {
                            $Label.a &= -2049;
                            $Label = $Label.i;
                        }
                        object = $Label3.j.c.b;
                        object.b($Label3, 0, this.L);
                    }
                    $Label3 = $Label3.i;
                }
            }
            int n8 = 0;
            $Label3 = this.N;
            while ($Label3 != null) {
                void $Edge;
                $Label = $Label3;
                $Label3 = $Label3.k;
                int n9 = $Label.f;
                int n10 = n9 + $Label.g;
                if (n10 > n8) {
                    n8 = n10;
                }
                $Edge $Edge3 = $Label.j;
                if (($Label.a & 128) != 0) {
                    $Edge $Edge4 = $Edge3.c;
                }
                while ($Edge != null) {
                    $Label = $Edge.b;
                    if (($Label.a & 8) == 0) {
                        $Label.f = $Edge.a == Integer.MAX_VALUE ? 1 : n9 + $Edge.a;
                        $Label.a |= 8;
                        $Label.k = $Label3;
                        $Label3 = $Label;
                    }
                    $Edge $Edge5 = $Edge.c;
                }
            }
            this.s = Math.max(n2, n8);
        } else {
            this.s = n2;
            this.t = n3;
        }
    }

    public void visitEnd() {
    }

    private void a(int n2, $Label $Label) {
        $Edge $Edge = new $Edge();
        $Edge.a = n2;
        $Edge.b = $Label;
        $Edge.c = this.P.j;
        this.P.j = $Edge;
    }

    private void e() {
        if (this.M == 0) {
            $Label $Label = new $Label();
            $Label.h = new $Frame();
            $Label.h.b = $Label;
            $Label.a(this, this.r.b, this.r.a);
            this.O.i = $Label;
            this.O = $Label;
        } else {
            this.P.g = this.R;
        }
        this.P = null;
    }

    private void b($Frame $Frame) {
        int n2;
        int n3;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int[] arrn = $Frame.c;
        int[] arrn2 = $Frame.d;
        for (n2 = 0; n2 < arrn.length; ++n2) {
            n3 = arrn[n2];
            if (n3 == 16777216) {
                ++n4;
            } else {
                n5 += n4 + 1;
                n4 = 0;
            }
            if (n3 != 16777220 && n3 != 16777219) continue;
            ++n2;
        }
        for (n2 = 0; n2 < arrn2.length; ++n2) {
            n3 = arrn2[n2];
            ++n6;
            if (n3 != 16777220 && n3 != 16777219) continue;
            ++n2;
        }
        int n7 = this.a($Frame.b.c, n5, n6);
        n2 = 0;
        while (n5 > 0) {
            n3 = arrn[n2];
            this.z[n7++] = n3;
            if (n3 == 16777220 || n3 == 16777219) {
                ++n2;
            }
            ++n2;
            --n5;
        }
        for (n2 = 0; n2 < arrn2.length; ++n2) {
            n3 = arrn2[n2];
            this.z[n7++] = n3;
            if (n3 != 16777220 && n3 != 16777219) continue;
            ++n2;
        }
        this.b();
    }

    private void f() {
        int n2 = this.a(0, this.f.length() + 1, 0);
        if ((this.c & 8) == 0) {
            this.z[n2++] = (this.c & 524288) == 0 ? 24117248 | this.b.c(this.b.I) : 6;
        }
        int n3 = 1;
        block8 : do {
            int n4 = n3;
            switch (this.f.charAt(n3++)) {
                case 'B': 
                case 'C': 
                case 'I': 
                case 'S': 
                case 'Z': {
                    this.z[n2++] = 1;
                    continue block8;
                }
                case 'F': {
                    this.z[n2++] = 2;
                    continue block8;
                }
                case 'J': {
                    this.z[n2++] = 4;
                    continue block8;
                }
                case 'D': {
                    this.z[n2++] = 3;
                    continue block8;
                }
                case '[': {
                    while (this.f.charAt(n3) == '[') {
                        ++n3;
                    }
                    if (this.f.charAt(n3) == 'L') {
                        ++n3;
                        while (this.f.charAt(n3) != ';') {
                            ++n3;
                        }
                    }
                    this.z[n2++] = 24117248 | this.b.c(this.f.substring(n4, ++n3));
                    continue block8;
                }
                case 'L': {
                    while (this.f.charAt(n3) != ';') {
                        ++n3;
                    }
                    this.z[n2++] = 24117248 | this.b.c(this.f.substring(n4 + 1, n3++));
                    continue block8;
                }
            }
            break;
        } while (true);
        this.z[1] = n2 - 3;
        this.b();
    }

    private int a(int n2, int n3, int n4) {
        int n5 = 3 + n3 + n4;
        if (this.z == null || this.z.length < n5) {
            this.z = new int[n5];
        }
        this.z[0] = n2;
        this.z[1] = n3;
        this.z[2] = n4;
        return 3;
    }

    private void b() {
        if (this.x != null) {
            if (this.v == null) {
                this.v = new $ByteVector();
            }
            this.c();
            ++this.u;
        }
        this.x = this.z;
        this.z = null;
    }

    private void c() {
        int n2 = this.z[1];
        int n3 = this.z[2];
        if ((this.b.b & 65535) < 50) {
            this.v.putShort(this.z[0]).putShort(n2);
            this.a(3, 3 + n2);
            this.v.putShort(n3);
            this.a(3 + n2, 3 + n2 + n3);
            return;
        }
        int n4 = this.x[1];
        int n5 = 255;
        int n6 = 0;
        int n7 = this.u == 0 ? this.z[0] : this.z[0] - this.x[0] - 1;
        if (n3 == 0) {
            n6 = n2 - n4;
            switch (n6) {
                case -3: 
                case -2: 
                case -1: {
                    n5 = 248;
                    n4 = n2;
                    break;
                }
                case 0: {
                    n5 = n7 < 64 ? 0 : 251;
                    break;
                }
                case 1: 
                case 2: 
                case 3: {
                    n5 = 252;
                }
            }
        } else if (n2 == n4 && n3 == 1) {
            int n8 = n5 = n7 < 63 ? 64 : 247;
        }
        if (n5 != 255) {
            int n9 = 3;
            for (int i2 = 0; i2 < n4; ++i2) {
                if (this.z[n9] != this.x[n9]) {
                    n5 = 255;
                    break;
                }
                ++n9;
            }
        }
        switch (n5) {
            case 0: {
                this.v.putByte(n7);
                break;
            }
            case 64: {
                this.v.putByte(64 + n7);
                this.a(3 + n2, 4 + n2);
                break;
            }
            case 247: {
                this.v.putByte(247).putShort(n7);
                this.a(3 + n2, 4 + n2);
                break;
            }
            case 251: {
                this.v.putByte(251).putShort(n7);
                break;
            }
            case 248: {
                this.v.putByte(251 + n6).putShort(n7);
                break;
            }
            case 252: {
                this.v.putByte(251 + n6).putShort(n7);
                this.a(3 + n4, 3 + n2);
                break;
            }
            default: {
                this.v.putByte(255).putShort(n7).putShort(n2);
                this.a(3, 3 + n2);
                this.v.putShort(n3);
                this.a(3 + n2, 3 + n2 + n3);
            }
        }
    }

    private void a(int n2, int n3) {
        block13 : for (int i2 = n2; i2 < n3; ++i2) {
            int n4 = this.z[i2];
            int n5 = n4 & -268435456;
            if (n5 == 0) {
                int n6 = n4 & 1048575;
                switch (n4 & 267386880) {
                    case 24117248: {
                        this.v.putByte(7).putShort(this.b.newClass(this.b.H[n6].g));
                        continue block13;
                    }
                    case 25165824: {
                        this.v.putByte(8).putShort(this.b.H[n6].c);
                        continue block13;
                    }
                }
                this.v.putByte(n6);
                continue;
            }
            StringBuffer stringBuffer = new StringBuffer();
            n5 >>= 28;
            while (n5-- > 0) {
                stringBuffer.append('[');
            }
            if ((n4 & 267386880) == 24117248) {
                stringBuffer.append('L');
                stringBuffer.append(this.b.H[n4 & 1048575].g);
                stringBuffer.append(';');
            } else {
                switch (n4 & 15) {
                    case 1: {
                        stringBuffer.append('I');
                        break;
                    }
                    case 2: {
                        stringBuffer.append('F');
                        break;
                    }
                    case 3: {
                        stringBuffer.append('D');
                        break;
                    }
                    case 9: {
                        stringBuffer.append('Z');
                        break;
                    }
                    case 10: {
                        stringBuffer.append('B');
                        break;
                    }
                    case 11: {
                        stringBuffer.append('C');
                        break;
                    }
                    case 12: {
                        stringBuffer.append('S');
                        break;
                    }
                    default: {
                        stringBuffer.append('J');
                    }
                }
            }
            this.v.putByte(7).putShort(this.b.newClass(stringBuffer.toString()));
        }
    }

    private void a(Object object) {
        if (object instanceof String) {
            this.v.putByte(7).putShort(this.b.newClass((String)object));
        } else if (object instanceof Integer) {
            this.v.putByte((Integer)object);
        } else {
            this.v.putByte(8).putShort((($Label)object).c);
        }
    }

    final int a() {
        int n2;
        if (this.h != 0) {
            return 6 + this.i;
        }
        int n3 = 8;
        if (this.r.b > 0) {
            if (this.r.b > 65536) {
                throw new RuntimeException("Method code too large!");
            }
            this.b.newUTF8("Code");
            n3 += 18 + this.r.b + 8 * this.A;
            if (this.E != null) {
                this.b.newUTF8("LocalVariableTable");
                n3 += 8 + this.E.b;
            }
            if (this.G != null) {
                this.b.newUTF8("LocalVariableTypeTable");
                n3 += 8 + this.G.b;
            }
            if (this.I != null) {
                this.b.newUTF8("LineNumberTable");
                n3 += 8 + this.I.b;
            }
            if (this.v != null) {
                n2 = (this.b.b & 65535) >= 50 ? 1 : 0;
                this.b.newUTF8(n2 != 0 ? "StackMapTable" : "StackMap");
                n3 += 8 + this.v.b;
            }
            if (this.W != null) {
                this.b.newUTF8("RuntimeVisibleTypeAnnotations");
                n3 += 8 + this.W.a();
            }
            if (this.X != null) {
                this.b.newUTF8("RuntimeInvisibleTypeAnnotations");
                n3 += 8 + this.X.a();
            }
            if (this.J != null) {
                n3 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
        }
        if (this.j > 0) {
            this.b.newUTF8("Exceptions");
            n3 += 8 + 2 * this.j;
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            this.b.newUTF8("Synthetic");
            n3 += 6;
        }
        if ((this.c & 131072) != 0) {
            this.b.newUTF8("Deprecated");
            n3 += 6;
        }
        if (this.g != null) {
            this.b.newUTF8("Signature");
            this.b.newUTF8(this.g);
            n3 += 8;
        }
        if (this.$ != null) {
            this.b.newUTF8("MethodParameters");
            n3 += 7 + this.$.b;
        }
        if (this.l != null) {
            this.b.newUTF8("AnnotationDefault");
            n3 += 6 + this.l.b;
        }
        if (this.m != null) {
            this.b.newUTF8("RuntimeVisibleAnnotations");
            n3 += 8 + this.m.a();
        }
        if (this.n != null) {
            this.b.newUTF8("RuntimeInvisibleAnnotations");
            n3 += 8 + this.n.a();
        }
        if (this.U != null) {
            this.b.newUTF8("RuntimeVisibleTypeAnnotations");
            n3 += 8 + this.U.a();
        }
        if (this.V != null) {
            this.b.newUTF8("RuntimeInvisibleTypeAnnotations");
            n3 += 8 + this.V.a();
        }
        if (this.o != null) {
            this.b.newUTF8("RuntimeVisibleParameterAnnotations");
            n3 += 7 + 2 * (this.o.length - this.S);
            for (n2 = this.o.length - 1; n2 >= this.S; --n2) {
                n3 += this.o[n2] == null ? 0 : this.o[n2].a();
            }
        }
        if (this.p != null) {
            this.b.newUTF8("RuntimeInvisibleParameterAnnotations");
            n3 += 7 + 2 * (this.p.length - this.S);
            for (n2 = this.p.length - 1; n2 >= this.S; --n2) {
                n3 += this.p[n2] == null ? 0 : this.p[n2].a();
            }
        }
        if (this.q != null) {
            n3 += this.q.a(this.b, null, 0, -1, -1);
        }
        return n3;
    }

    final void a($ByteVector $ByteVector) {
        int n2;
        int n3 = 64;
        int n4 = 917504 | (this.c & 262144) / 64;
        $ByteVector.putShort(this.c & ~ n4).putShort(this.d).putShort(this.e);
        if (this.h != 0) {
            $ByteVector.putByteArray(this.b.M.b, this.h, this.i);
            return;
        }
        int n5 = 0;
        if (this.r.b > 0) {
            ++n5;
        }
        if (this.j > 0) {
            ++n5;
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            ++n5;
        }
        if ((this.c & 131072) != 0) {
            ++n5;
        }
        if (this.g != null) {
            ++n5;
        }
        if (this.$ != null) {
            ++n5;
        }
        if (this.l != null) {
            ++n5;
        }
        if (this.m != null) {
            ++n5;
        }
        if (this.n != null) {
            ++n5;
        }
        if (this.U != null) {
            ++n5;
        }
        if (this.V != null) {
            ++n5;
        }
        if (this.o != null) {
            ++n5;
        }
        if (this.p != null) {
            ++n5;
        }
        if (this.q != null) {
            n5 += this.q.a();
        }
        $ByteVector.putShort(n5);
        if (this.r.b > 0) {
            n2 = 12 + this.r.b + 8 * this.A;
            if (this.E != null) {
                n2 += 8 + this.E.b;
            }
            if (this.G != null) {
                n2 += 8 + this.G.b;
            }
            if (this.I != null) {
                n2 += 8 + this.I.b;
            }
            if (this.v != null) {
                n2 += 8 + this.v.b;
            }
            if (this.W != null) {
                n2 += 8 + this.W.a();
            }
            if (this.X != null) {
                n2 += 8 + this.X.a();
            }
            if (this.J != null) {
                n2 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
            $ByteVector.putShort(this.b.newUTF8("Code")).putInt(n2);
            $ByteVector.putShort(this.s).putShort(this.t);
            $ByteVector.putInt(this.r.b).putByteArray(this.r.a, 0, this.r.b);
            $ByteVector.putShort(this.A);
            if (this.A > 0) {
                $Handler $Handler = this.B;
                while ($Handler != null) {
                    $ByteVector.putShort($Handler.a.c).putShort($Handler.b.c).putShort($Handler.c.c).putShort($Handler.e);
                    $Handler = $Handler.f;
                }
            }
            n5 = 0;
            if (this.E != null) {
                ++n5;
            }
            if (this.G != null) {
                ++n5;
            }
            if (this.I != null) {
                ++n5;
            }
            if (this.v != null) {
                ++n5;
            }
            if (this.W != null) {
                ++n5;
            }
            if (this.X != null) {
                ++n5;
            }
            if (this.J != null) {
                n5 += this.J.a();
            }
            $ByteVector.putShort(n5);
            if (this.E != null) {
                $ByteVector.putShort(this.b.newUTF8("LocalVariableTable"));
                $ByteVector.putInt(this.E.b + 2).putShort(this.D);
                $ByteVector.putByteArray(this.E.a, 0, this.E.b);
            }
            if (this.G != null) {
                $ByteVector.putShort(this.b.newUTF8("LocalVariableTypeTable"));
                $ByteVector.putInt(this.G.b + 2).putShort(this.F);
                $ByteVector.putByteArray(this.G.a, 0, this.G.b);
            }
            if (this.I != null) {
                $ByteVector.putShort(this.b.newUTF8("LineNumberTable"));
                $ByteVector.putInt(this.I.b + 2).putShort(this.H);
                $ByteVector.putByteArray(this.I.a, 0, this.I.b);
            }
            if (this.v != null) {
                boolean bl = (this.b.b & 65535) >= 50;
                $ByteVector.putShort(this.b.newUTF8(bl ? "StackMapTable" : "StackMap"));
                $ByteVector.putInt(this.v.b + 2).putShort(this.u);
                $ByteVector.putByteArray(this.v.a, 0, this.v.b);
            }
            if (this.W != null) {
                $ByteVector.putShort(this.b.newUTF8("RuntimeVisibleTypeAnnotations"));
                this.W.a($ByteVector);
            }
            if (this.X != null) {
                $ByteVector.putShort(this.b.newUTF8("RuntimeInvisibleTypeAnnotations"));
                this.X.a($ByteVector);
            }
            if (this.J != null) {
                this.J.a(this.b, this.r.a, this.r.b, this.t, this.s, $ByteVector);
            }
        }
        if (this.j > 0) {
            $ByteVector.putShort(this.b.newUTF8("Exceptions")).putInt(2 * this.j + 2);
            $ByteVector.putShort(this.j);
            for (n2 = 0; n2 < this.j; ++n2) {
                $ByteVector.putShort(this.k[n2]);
            }
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            $ByteVector.putShort(this.b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.c & 131072) != 0) {
            $ByteVector.putShort(this.b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.g != null) {
            $ByteVector.putShort(this.b.newUTF8("Signature")).putInt(2).putShort(this.b.newUTF8(this.g));
        }
        if (this.$ != null) {
            $ByteVector.putShort(this.b.newUTF8("MethodParameters"));
            $ByteVector.putInt(this.$.b + 1).putByte(this.Z);
            $ByteVector.putByteArray(this.$.a, 0, this.$.b);
        }
        if (this.l != null) {
            $ByteVector.putShort(this.b.newUTF8("AnnotationDefault"));
            $ByteVector.putInt(this.l.b);
            $ByteVector.putByteArray(this.l.a, 0, this.l.b);
        }
        if (this.m != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeVisibleAnnotations"));
            this.m.a($ByteVector);
        }
        if (this.n != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeInvisibleAnnotations"));
            this.n.a($ByteVector);
        }
        if (this.U != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.U.a($ByteVector);
        }
        if (this.V != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.V.a($ByteVector);
        }
        if (this.o != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeVisibleParameterAnnotations"));
            $AnnotationWriter.a(this.o, this.S, $ByteVector);
        }
        if (this.p != null) {
            $ByteVector.putShort(this.b.newUTF8("RuntimeInvisibleParameterAnnotations"));
            $AnnotationWriter.a(this.p, this.S, $ByteVector);
        }
        if (this.q != null) {
            this.q.a(this.b, null, 0, -1, -1, $ByteVector);
        }
    }

    private void d() {
        int[] arrn;
        int n2;
        int n3;
        int n4;
        Object object;
        int n5;
        int n6;
        byte[] arrby = this.r.a;
        Object object2 = new int[]{};
        int[] arrn2 = new int[]{};
        boolean[] arrbl = new boolean[this.r.b];
        int n7 = 3;
        do {
            if (n7 == 3) {
                n7 = 2;
            }
            n2 = 0;
            while (n2 < arrby.length) {
                int n8 = arrby[n2] & 255;
                n6 = 0;
                switch ($ClassWriter.a[n8]) {
                    case 0: 
                    case 4: {
                        ++n2;
                        break;
                    }
                    case 9: {
                        if (n8 > 201) {
                            n8 = n8 < 218 ? n8 - 49 : n8 - 20;
                            n5 = n2 + $MethodWriter.c(arrby, n2 + 1);
                        } else {
                            n5 = n2 + $MethodWriter.b(arrby, n2 + 1);
                        }
                        n4 = $MethodWriter.a((int[])object2, arrn2, n2, n5);
                        if (!(n4 >= -32768 && n4 <= 32767 || arrbl[n2])) {
                            n6 = n8 == 167 || n8 == 168 ? 2 : 5;
                            arrbl[n2] = true;
                        }
                        n2 += 3;
                        break;
                    }
                    case 10: {
                        n2 += 5;
                        break;
                    }
                    case 14: {
                        if (n7 == 1) {
                            n4 = $MethodWriter.a((int[])object2, arrn2, 0, n2);
                            n6 = - (n4 & 3);
                        } else if (!arrbl[n2]) {
                            n6 = n2 & 3;
                            arrbl[n2] = true;
                        }
                        n2 = n2 + 4 - (n2 & 3);
                        n2 += 4 * ($MethodWriter.a(arrby, n2 + 8) - $MethodWriter.a(arrby, n2 + 4) + 1) + 12;
                        break;
                    }
                    case 15: {
                        if (n7 == 1) {
                            n4 = $MethodWriter.a((int[])object2, arrn2, 0, n2);
                            n6 = - (n4 & 3);
                        } else if (!arrbl[n2]) {
                            n6 = n2 & 3;
                            arrbl[n2] = true;
                        }
                        n2 = n2 + 4 - (n2 & 3);
                        n2 += 8 * $MethodWriter.a(arrby, n2 + 4) + 8;
                        break;
                    }
                    case 17: {
                        n8 = arrby[n2 + 1] & 255;
                        if (n8 == 132) {
                            n2 += 6;
                            break;
                        }
                        n2 += 4;
                        break;
                    }
                    case 1: 
                    case 3: 
                    case 11: {
                        n2 += 2;
                        break;
                    }
                    case 2: 
                    case 5: 
                    case 6: 
                    case 12: 
                    case 13: {
                        n2 += 3;
                        break;
                    }
                    case 7: 
                    case 8: {
                        n2 += 5;
                        break;
                    }
                    default: {
                        n2 += 4;
                    }
                }
                if (n6 == 0) continue;
                object = new int[object2.length + 1];
                arrn = new int[arrn2.length + 1];
                System.arraycopy(object2, 0, object, 0, object2.length);
                System.arraycopy(arrn2, 0, arrn, 0, arrn2.length);
                object[object2.length] = n2;
                arrn[arrn2.length] = n6;
                object2 = object;
                arrn2 = arrn;
                if (n6 <= 0) continue;
                n7 = 3;
            }
            if (n7 >= 3) continue;
            --n7;
        } while (n7 != 0);
        $ByteVector $ByteVector = new $ByteVector(this.r.b);
        n2 = 0;
        block24 : while (n2 < this.r.b) {
            n6 = arrby[n2] & 255;
            switch ($ClassWriter.a[n6]) {
                int n9;
                int n10;
                case 0: 
                case 4: {
                    $ByteVector.putByte(n6);
                    ++n2;
                    continue block24;
                }
                case 9: {
                    if (n6 > 201) {
                        n6 = n6 < 218 ? n6 - 49 : n6 - 20;
                        n5 = n2 + $MethodWriter.c(arrby, n2 + 1);
                    } else {
                        n5 = n2 + $MethodWriter.b(arrby, n2 + 1);
                    }
                    n4 = $MethodWriter.a((int[])object2, arrn2, n2, n5);
                    if (arrbl[n2]) {
                        if (n6 == 167) {
                            $ByteVector.putByte(200);
                        } else if (n6 == 168) {
                            $ByteVector.putByte(201);
                        } else {
                            $ByteVector.putByte(n6 <= 166 ? (n6 + 1 ^ 1) - 1 : n6 ^ 1);
                            $ByteVector.putShort(8);
                            $ByteVector.putByte(200);
                            n4 -= 3;
                        }
                        $ByteVector.putInt(n4);
                    } else {
                        $ByteVector.putByte(n6);
                        $ByteVector.putShort(n4);
                    }
                    n2 += 3;
                    continue block24;
                }
                case 10: {
                    n5 = n2 + $MethodWriter.a(arrby, n2 + 1);
                    n4 = $MethodWriter.a((int[])object2, arrn2, n2, n5);
                    $ByteVector.putByte(n6);
                    $ByteVector.putInt(n4);
                    n2 += 5;
                    continue block24;
                }
                case 14: {
                    n10 = n2;
                    n2 = n2 + 4 - (n10 & 3);
                    $ByteVector.putByte(170);
                    $ByteVector.putByteArray(null, 0, (4 - $ByteVector.b % 4) % 4);
                    n5 = n10 + $MethodWriter.a(arrby, n2);
                    n4 = $MethodWriter.a((int[])object2, arrn2, n10, n5);
                    $ByteVector.putInt(n4);
                    n9 = $MethodWriter.a(arrby, n2 += 4);
                    $ByteVector.putInt(n9);
                    $ByteVector.putInt($MethodWriter.a(arrby, (n2 += 4) - 4));
                    for (n9 = $MethodWriter.a((byte[])arrby, (int)(n2 += 4)) - n9 + 1; n9 > 0; --n9) {
                        n5 = n10 + $MethodWriter.a(arrby, n2);
                        n2 += 4;
                        n4 = $MethodWriter.a((int[])object2, arrn2, n10, n5);
                        $ByteVector.putInt(n4);
                    }
                    continue block24;
                }
                case 15: {
                    n10 = n2;
                    n2 = n2 + 4 - (n10 & 3);
                    $ByteVector.putByte(171);
                    $ByteVector.putByteArray(null, 0, (4 - $ByteVector.b % 4) % 4);
                    n5 = n10 + $MethodWriter.a(arrby, n2);
                    n4 = $MethodWriter.a((int[])object2, arrn2, n10, n5);
                    $ByteVector.putInt(n4);
                    n2 += 4;
                    $ByteVector.putInt(n9);
                    for (n9 = $MethodWriter.a((byte[])arrby, (int)(n2 += 4)); n9 > 0; --n9) {
                        $ByteVector.putInt($MethodWriter.a(arrby, n2));
                        n5 = n10 + $MethodWriter.a(arrby, n2 += 4);
                        n2 += 4;
                        n4 = $MethodWriter.a((int[])object2, arrn2, n10, n5);
                        $ByteVector.putInt(n4);
                    }
                    continue block24;
                }
                case 17: {
                    n6 = arrby[n2 + 1] & 255;
                    if (n6 == 132) {
                        $ByteVector.putByteArray(arrby, n2, 6);
                        n2 += 6;
                        continue block24;
                    }
                    $ByteVector.putByteArray(arrby, n2, 4);
                    n2 += 4;
                    continue block24;
                }
                case 1: 
                case 3: 
                case 11: {
                    $ByteVector.putByteArray(arrby, n2, 2);
                    n2 += 2;
                    continue block24;
                }
                case 2: 
                case 5: 
                case 6: 
                case 12: 
                case 13: {
                    $ByteVector.putByteArray(arrby, n2, 3);
                    n2 += 3;
                    continue block24;
                }
                case 7: 
                case 8: {
                    $ByteVector.putByteArray(arrby, n2, 5);
                    n2 += 5;
                    continue block24;
                }
            }
            $ByteVector.putByteArray(arrby, n2, 4);
            n2 += 4;
        }
        if (this.M == 0) {
            $Label $Label = this.N;
            while ($Label != null) {
                n2 = $Label.c - 3;
                if (n2 >= 0 && arrbl[n2]) {
                    $Label.a |= 16;
                }
                $MethodWriter.a((int[])object2, arrn2, $Label);
                $Label = $Label.i;
            }
            for (n3 = 0; n3 < this.b.H.length; ++n3) {
                object = this.b.H[n3];
                if (object == null || object.b != 31) continue;
                object.c = $MethodWriter.a((int[])object2, arrn2, 0, object.c);
            }
        } else if (this.u > 0) {
            this.b.L = true;
        }
        $Handler $Handler = this.B;
        while ($Handler != null) {
            $MethodWriter.a((int[])object2, arrn2, $Handler.a);
            $MethodWriter.a((int[])object2, arrn2, $Handler.b);
            $MethodWriter.a((int[])object2, arrn2, $Handler.c);
            $Handler = $Handler.f;
        }
        for (n3 = 0; n3 < 2; ++n3) {
            Object object3 = object = n3 == 0 ? this.E : this.G;
            if (object == null) continue;
            arrby = object.a;
            for (n2 = 0; n2 < object.b; n2 += 10) {
                n5 = $MethodWriter.c(arrby, n2);
                n4 = $MethodWriter.a((int[])object2, arrn2, 0, n5);
                $MethodWriter.a(arrby, n2, n4);
                n4 = $MethodWriter.a((int[])object2, arrn2, 0, n5 += $MethodWriter.c(arrby, n2 + 2)) - n4;
                $MethodWriter.a(arrby, n2 + 2, n4);
            }
        }
        if (this.I != null) {
            arrby = this.I.a;
            for (n2 = 0; n2 < this.I.b; n2 += 4) {
                $MethodWriter.a(arrby, n2, $MethodWriter.a((int[])object2, arrn2, 0, $MethodWriter.c(arrby, n2)));
            }
        }
        object = this.J;
        while (object != null) {
            arrn = object.getLabels();
            if (arrn != null) {
                for (n3 = arrn.length - 1; n3 >= 0; --n3) {
                    $MethodWriter.a((int[])object2, arrn2, ($Label)arrn[n3]);
                }
            }
            object = object.a;
        }
        this.r = $ByteVector;
    }

    static int c(byte[] arrby, int n2) {
        return (arrby[n2] & 255) << 8 | arrby[n2 + 1] & 255;
    }

    static short b(byte[] arrby, int n2) {
        return (short)((arrby[n2] & 255) << 8 | arrby[n2 + 1] & 255);
    }

    static int a(byte[] arrby, int n2) {
        return (arrby[n2] & 255) << 24 | (arrby[n2 + 1] & 255) << 16 | (arrby[n2 + 2] & 255) << 8 | arrby[n2 + 3] & 255;
    }

    static void a(byte[] arrby, int n2, int n3) {
        arrby[n2] = (byte)(n3 >>> 8);
        arrby[n2 + 1] = (byte)n3;
    }

    static int a(int[] arrn, int[] arrn2, int n2, int n3) {
        int n4 = n3 - n2;
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            if (n2 < arrn[i2] && arrn[i2] <= n3) {
                n4 += arrn2[i2];
                continue;
            }
            if (n3 >= arrn[i2] || arrn[i2] > n2) continue;
            n4 -= arrn2[i2];
        }
        return n4;
    }

    static void a(int[] arrn, int[] arrn2, $Label $Label) {
        if (($Label.a & 4) == 0) {
            $Label.c = $MethodWriter.a(arrn, arrn2, 0, $Label.c);
            $Label.a |= 4;
        }
    }
}

