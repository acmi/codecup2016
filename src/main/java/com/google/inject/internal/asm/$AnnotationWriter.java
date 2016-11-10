/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.$AnnotationVisitor;
import com.google.inject.internal.asm.$ByteVector;
import com.google.inject.internal.asm.$ClassWriter;
import com.google.inject.internal.asm.$Item;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.asm.$TypePath;

final class $AnnotationWriter
extends $AnnotationVisitor {
    private final $ClassWriter a;
    private int b;
    private final boolean c;
    private final $ByteVector d;
    private final $ByteVector e;
    private final int f;
    $AnnotationWriter g;
    $AnnotationWriter h;

    $AnnotationWriter($ClassWriter classWriter, boolean bl, $ByteVector byteVector, $ByteVector byteVector2, int n2) {
        super(327680);
        this.a = classWriter;
        this.c = bl;
        this.d = byteVector;
        this.e = byteVector2;
        this.f = n2;
    }

    public void visit(String string, Object object) {
        ++this.b;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(string));
        }
        if (object instanceof String) {
            this.d.b(115, this.a.newUTF8((String)object));
        } else if (object instanceof Byte) {
            this.d.b(66, this.a.a((int)((Byte)object).byteValue()).a);
        } else if (object instanceof Boolean) {
            int n2 = (Boolean)object != false ? 1 : 0;
            this.d.b(90, this.a.a((int)n2).a);
        } else if (object instanceof Character) {
            this.d.b(67, this.a.a((int)((Character)object).charValue()).a);
        } else if (object instanceof Short) {
            this.d.b(83, this.a.a((int)((Short)object).shortValue()).a);
        } else if (object instanceof $Type) {
            this.d.b(99, this.a.newUTF8((($Type)object).getDescriptor()));
        } else if (object instanceof byte[]) {
            byte[] arrby = (byte[])object;
            this.d.b(91, arrby.length);
            for (int i2 = 0; i2 < arrby.length; ++i2) {
                this.d.b(66, this.a.a((int)arrby[i2]).a);
            }
        } else if (object instanceof boolean[]) {
            boolean[] arrbl = (boolean[])object;
            this.d.b(91, arrbl.length);
            for (int i3 = 0; i3 < arrbl.length; ++i3) {
                this.d.b(90, this.a.a((int)(arrbl[i3] != false ? 1 : 0)).a);
            }
        } else if (object instanceof short[]) {
            short[] arrs = (short[])object;
            this.d.b(91, arrs.length);
            for (int i4 = 0; i4 < arrs.length; ++i4) {
                this.d.b(83, this.a.a((int)arrs[i4]).a);
            }
        } else if (object instanceof char[]) {
            char[] arrc = (char[])object;
            this.d.b(91, arrc.length);
            for (int i5 = 0; i5 < arrc.length; ++i5) {
                this.d.b(67, this.a.a((int)arrc[i5]).a);
            }
        } else if (object instanceof int[]) {
            int[] arrn = (int[])object;
            this.d.b(91, arrn.length);
            for (int i6 = 0; i6 < arrn.length; ++i6) {
                this.d.b(73, this.a.a((int)arrn[i6]).a);
            }
        } else if (object instanceof long[]) {
            long[] arrl = (long[])object;
            this.d.b(91, arrl.length);
            for (int i7 = 0; i7 < arrl.length; ++i7) {
                this.d.b(74, this.a.a((long)arrl[i7]).a);
            }
        } else if (object instanceof float[]) {
            float[] arrf = (float[])object;
            this.d.b(91, arrf.length);
            for (int i8 = 0; i8 < arrf.length; ++i8) {
                this.d.b(70, this.a.a((float)arrf[i8]).a);
            }
        } else if (object instanceof double[]) {
            double[] arrd = (double[])object;
            this.d.b(91, arrd.length);
            for (int i9 = 0; i9 < arrd.length; ++i9) {
                this.d.b(68, this.a.a((double)arrd[i9]).a);
            }
        } else {
            $Item $Item = this.a.a(object);
            this.d.b(".s.IFJDCS".charAt($Item.b), $Item.a);
        }
    }

    public void visitEnum(String string, String string2, String string3) {
        ++this.b;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(string));
        }
        this.d.b(101, this.a.newUTF8(string2)).putShort(this.a.newUTF8(string3));
    }

    public $AnnotationVisitor visitAnnotation(String string, String string2) {
        ++this.b;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(string));
        }
        this.d.b(64, this.a.newUTF8(string2)).putShort(0);
        return new $AnnotationWriter(this.a, true, this.d, this.d, this.d.b - 2);
    }

    public $AnnotationVisitor visitArray(String string) {
        ++this.b;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(string));
        }
        this.d.b(91, 0);
        return new $AnnotationWriter(this.a, false, this.d, this.d, this.d.b - 2);
    }

    public void visitEnd() {
        if (this.e != null) {
            byte[] arrby = this.e.a;
            arrby[this.f] = (byte)(this.b >>> 8);
            arrby[this.f + 1] = (byte)this.b;
        }
    }

    int a() {
        int n2 = 0;
        $AnnotationWriter $AnnotationWriter = this;
        while ($AnnotationWriter != null) {
            n2 += $AnnotationWriter.d.b;
            $AnnotationWriter = $AnnotationWriter.g;
        }
        return n2;
    }

    void a($ByteVector $ByteVector) {
        int n2 = 0;
        int n3 = 2;
        $AnnotationWriter $AnnotationWriter = this;
        $AnnotationWriter $AnnotationWriter2 = null;
        while ($AnnotationWriter != null) {
            ++n2;
            n3 += $AnnotationWriter.d.b;
            $AnnotationWriter.visitEnd();
            $AnnotationWriter.h = $AnnotationWriter2;
            $AnnotationWriter2 = $AnnotationWriter;
            $AnnotationWriter = $AnnotationWriter.g;
        }
        $ByteVector.putInt(n3);
        $ByteVector.putShort(n2);
        $AnnotationWriter = $AnnotationWriter2;
        while ($AnnotationWriter != null) {
            $ByteVector.putByteArray($AnnotationWriter.d.a, 0, $AnnotationWriter.d.b);
            $AnnotationWriter = $AnnotationWriter.h;
        }
    }

    static void a($AnnotationWriter[] arr$AnnotationWriter, int n2, $ByteVector $ByteVector) {
        int n3;
        int n4 = 1 + 2 * (arr$AnnotationWriter.length - n2);
        for (n3 = n2; n3 < arr$AnnotationWriter.length; ++n3) {
            n4 += arr$AnnotationWriter[n3] == null ? 0 : arr$AnnotationWriter[n3].a();
        }
        $ByteVector.putInt(n4).putByte(arr$AnnotationWriter.length - n2);
        for (n3 = n2; n3 < arr$AnnotationWriter.length; ++n3) {
            $AnnotationWriter $AnnotationWriter = arr$AnnotationWriter[n3];
            $AnnotationWriter $AnnotationWriter2 = null;
            int n5 = 0;
            while ($AnnotationWriter != null) {
                ++n5;
                $AnnotationWriter.visitEnd();
                $AnnotationWriter.h = $AnnotationWriter2;
                $AnnotationWriter2 = $AnnotationWriter;
                $AnnotationWriter = $AnnotationWriter.g;
            }
            $ByteVector.putShort(n5);
            $AnnotationWriter = $AnnotationWriter2;
            while ($AnnotationWriter != null) {
                $ByteVector.putByteArray($AnnotationWriter.d.a, 0, $AnnotationWriter.d.b);
                $AnnotationWriter = $AnnotationWriter.h;
            }
        }
    }

    static void a(int n2, $TypePath $TypePath, $ByteVector $ByteVector) {
        switch (n2 >>> 24) {
            case 0: 
            case 1: 
            case 22: {
                $ByteVector.putShort(n2 >>> 16);
                break;
            }
            case 19: 
            case 20: 
            case 21: {
                $ByteVector.putByte(n2 >>> 24);
                break;
            }
            case 71: 
            case 72: 
            case 73: 
            case 74: 
            case 75: {
                $ByteVector.putInt(n2);
                break;
            }
            default: {
                $ByteVector.b(n2 >>> 24, (n2 & 16776960) >> 8);
            }
        }
        if ($TypePath == null) {
            $ByteVector.putByte(0);
        } else {
            int n3 = $TypePath.a[$TypePath.b] * 2 + 1;
            $ByteVector.putByteArray($TypePath.a, $TypePath.b, n3);
        }
    }
}

