/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.$AnnotationVisitor;
import com.google.inject.internal.asm.$Attribute;
import com.google.inject.internal.asm.$ByteVector;
import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$ClassWriter;
import com.google.inject.internal.asm.$Context;
import com.google.inject.internal.asm.$FieldVisitor;
import com.google.inject.internal.asm.$Handle;
import com.google.inject.internal.asm.$Item;
import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$MethodVisitor;
import com.google.inject.internal.asm.$MethodWriter;
import com.google.inject.internal.asm.$Opcodes;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.asm.$TypePath;
import java.io.IOException;
import java.io.InputStream;

public class $ClassReader {
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    public static final int EXPAND_FRAMES = 8;
    public final byte[] b;
    private final int[] a;
    private final String[] c;
    private final int d;
    public final int header;

    public $ClassReader(byte[] arrby) {
        this(arrby, 0, arrby.length);
    }

    public $ClassReader(byte[] arrby, int n2, int n3) {
        this.b = arrby;
        if (this.readShort(n2 + 6) > 52) {
            throw new IllegalArgumentException();
        }
        this.a = new int[this.readUnsignedShort(n2 + 8)];
        int n4 = this.a.length;
        this.c = new String[n4];
        int n5 = 0;
        int n6 = n2 + 10;
        for (int i2 = 1; i2 < n4; ++i2) {
            int n7;
            this.a[i2] = n6 + 1;
            switch (arrby[n6]) {
                case 3: 
                case 4: 
                case 9: 
                case 10: 
                case 11: 
                case 12: 
                case 18: {
                    n7 = 5;
                    break;
                }
                case 5: 
                case 6: {
                    n7 = 9;
                    ++i2;
                    break;
                }
                case 1: {
                    n7 = 3 + this.readUnsignedShort(n6 + 1);
                    if (n7 <= n5) break;
                    n5 = n7;
                    break;
                }
                case 15: {
                    n7 = 4;
                    break;
                }
                default: {
                    n7 = 3;
                }
            }
            n6 += n7;
        }
        this.d = n5;
        this.header = n6;
    }

    public int getAccess() {
        return this.readUnsignedShort(this.header);
    }

    public String getClassName() {
        return this.readClass(this.header + 2, new char[this.d]);
    }

    public String getSuperName() {
        return this.readClass(this.header + 4, new char[this.d]);
    }

    public String[] getInterfaces() {
        int n2 = this.header + 6;
        int n3 = this.readUnsignedShort(n2);
        String[] arrstring = new String[n3];
        if (n3 > 0) {
            char[] arrc = new char[this.d];
            for (int i2 = 0; i2 < n3; ++i2) {
                arrstring[i2] = this.readClass(n2 += 2, arrc);
            }
        }
        return arrstring;
    }

    void a($ClassWriter $ClassWriter) {
        int n2;
        char[] arrc = new char[this.d];
        int n3 = this.a.length;
        $Item[] arr$Item = new $Item[n3];
        for (n2 = 1; n2 < n3; ++n2) {
            int n4;
            int n5 = this.a[n2];
            byte by = this.b[n5 - 1];
            $Item $Item = new $Item(n2);
            switch (by) {
                int n6;
                case 9: 
                case 10: 
                case 11: {
                    n6 = this.a[this.readUnsignedShort(n5 + 2)];
                    $Item.a(by, this.readClass(n5, arrc), this.readUTF8(n6, arrc), this.readUTF8(n6 + 2, arrc));
                    break;
                }
                case 3: {
                    $Item.a(this.readInt(n5));
                    break;
                }
                case 4: {
                    $Item.a(Float.intBitsToFloat(this.readInt(n5)));
                    break;
                }
                case 12: {
                    $Item.a(by, this.readUTF8(n5, arrc), this.readUTF8(n5 + 2, arrc), null);
                    break;
                }
                case 5: {
                    $Item.a(this.readLong(n5));
                    ++n2;
                    break;
                }
                case 6: {
                    $Item.a(Double.longBitsToDouble(this.readLong(n5)));
                    ++n2;
                    break;
                }
                case 1: {
                    String string = this.c[n2];
                    if (string == null) {
                        n5 = this.a[n2];
                        string = this.c[n2] = this.a(n5 + 2, this.readUnsignedShort(n5), arrc);
                    }
                    $Item.a(by, string, null, null);
                    break;
                }
                case 15: {
                    n4 = this.a[this.readUnsignedShort(n5 + 1)];
                    n6 = this.a[this.readUnsignedShort(n4 + 2)];
                    $Item.a(20 + this.readByte(n5), this.readClass(n4, arrc), this.readUTF8(n6, arrc), this.readUTF8(n6 + 2, arrc));
                    break;
                }
                case 18: {
                    if ($ClassWriter.A == null) {
                        this.a($ClassWriter, arr$Item, arrc);
                    }
                    n6 = this.a[this.readUnsignedShort(n5 + 2)];
                    $Item.a(this.readUTF8(n6, arrc), this.readUTF8(n6 + 2, arrc), this.readUnsignedShort(n5));
                    break;
                }
                default: {
                    $Item.a(by, this.readUTF8(n5, arrc), null, null);
                }
            }
            n4 = $Item.j % arr$Item.length;
            $Item.k = arr$Item[n4];
            arr$Item[n4] = $Item;
        }
        n2 = this.a[1] - 1;
        $ClassWriter.d.putByteArray(this.b, n2, this.header - n2);
        $ClassWriter.e = arr$Item;
        $ClassWriter.f = (int)(0.75 * (double)n3);
        $ClassWriter.c = n3;
    }

    private void a($ClassWriter $ClassWriter, $Item[] arr$Item, char[] arrc) {
        int n2;
        int n3;
        int n4 = this.a();
        boolean bl = false;
        for (n2 = this.readUnsignedShort((int)n4); n2 > 0; --n2) {
            String string = this.readUTF8(n4 + 2, arrc);
            if ("BootstrapMethods".equals(string)) {
                bl = true;
                break;
            }
            n4 += 6 + this.readInt(n4 + 4);
        }
        if (!bl) {
            return;
        }
        n2 = this.readUnsignedShort(n4 + 8);
        int n5 = n4 + 10;
        for (n3 = 0; n3 < n2; ++n3) {
            int n6 = n5 - n4 - 10;
            int n7 = this.readConst(this.readUnsignedShort(n5), arrc).hashCode();
            for (int i2 = this.readUnsignedShort((int)(n5 + 2)); i2 > 0; --i2) {
                n7 ^= this.readConst(this.readUnsignedShort(n5 + 4), arrc).hashCode();
                n5 += 2;
            }
            n5 += 4;
            $Item $Item = new $Item(n3);
            $Item.a(n6, n7 & Integer.MAX_VALUE);
            int n8 = $Item.j % arr$Item.length;
            $Item.k = arr$Item[n8];
            arr$Item[n8] = $Item;
        }
        n3 = this.readInt(n4 + 4);
        $ByteVector $ByteVector = new $ByteVector(n3 + 62);
        $ByteVector.putByteArray(this.b, n4 + 10, n3 - 2);
        $ClassWriter.z = n2;
        $ClassWriter.A = $ByteVector;
    }

    public $ClassReader(InputStream inputStream) throws IOException {
        this($ClassReader.a(inputStream, false));
    }

    public $ClassReader(String string) throws IOException {
        this($ClassReader.a(ClassLoader.getSystemResourceAsStream(string.replace('.', '/') + ".class"), true));
    }

    private static byte[] a(InputStream inputStream, boolean bl) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        }
        try {
            byte[] arrby = new byte[inputStream.available()];
            int n2 = 0;
            do {
                byte[] arrby2;
                int n3;
                if ((n3 = inputStream.read(arrby, n2, arrby.length - n2)) == -1) {
                    byte[] arrby3;
                    if (n2 < arrby.length) {
                        arrby3 = new byte[n2];
                        System.arraycopy(arrby, 0, arrby3, 0, n2);
                        arrby = arrby3;
                    }
                    arrby3 = arrby;
                    return arrby3;
                }
                if ((n2 += n3) != arrby.length) continue;
                int n4 = inputStream.read();
                if (n4 < 0) {
                    arrby2 = arrby;
                    return arrby2;
                }
                arrby2 = new byte[arrby.length + 1000];
                System.arraycopy(arrby, 0, arrby2, 0, n2);
                arrby2[n2++] = (byte)n4;
                arrby = arrby2;
            } while (true);
        }
        finally {
            if (bl) {
                inputStream.close();
            }
        }
    }

    public void accept($ClassVisitor $ClassVisitor, int n2) {
        this.accept($ClassVisitor, new $Attribute[0], n2);
    }

    public void accept($ClassVisitor $ClassVisitor, $Attribute[] arr$Attribute, int n2) {
        String string;
        int n3;
        int n4 = this.header;
        char[] arrc = new char[this.d];
        $Context $Context = new $Context();
        $Context.a = arr$Attribute;
        $Context.b = n2;
        $Context.c = arrc;
        int n5 = this.readUnsignedShort(n4);
        String string2 = this.readClass(n4 + 2, arrc);
        String string3 = this.readClass(n4 + 4, arrc);
        String[] arrstring = new String[this.readUnsignedShort(n4 + 6)];
        n4 += 8;
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            arrstring[i2] = this.readClass(n4, arrc);
            n4 += 2;
        }
        String string4 = null;
        String string5 = null;
        String string6 = null;
        String string7 = null;
        String string8 = null;
        String string9 = null;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        $Attribute $Attribute = null;
        n4 = this.a();
        for (n3 = this.readUnsignedShort((int)n4); n3 > 0; --n3) {
            int n11;
            string = this.readUTF8(n4 + 2, arrc);
            if ("SourceFile".equals(string)) {
                string5 = this.readUTF8(n4 + 8, arrc);
            } else if ("InnerClasses".equals(string)) {
                n10 = n4 + 8;
            } else if ("EnclosingMethod".equals(string)) {
                string7 = this.readClass(n4 + 8, arrc);
                n11 = this.readUnsignedShort(n4 + 10);
                if (n11 != 0) {
                    string8 = this.readUTF8(this.a[n11], arrc);
                    string9 = this.readUTF8(this.a[n11] + 2, arrc);
                }
            } else if ("Signature".equals(string)) {
                string4 = this.readUTF8(n4 + 8, arrc);
            } else if ("RuntimeVisibleAnnotations".equals(string)) {
                n6 = n4 + 8;
            } else if ("RuntimeVisibleTypeAnnotations".equals(string)) {
                n8 = n4 + 8;
            } else if ("Deprecated".equals(string)) {
                n5 |= 131072;
            } else if ("Synthetic".equals(string)) {
                n5 |= 266240;
            } else if ("SourceDebugExtension".equals(string)) {
                n11 = this.readInt(n4 + 4);
                string6 = this.a(n4 + 8, n11, new char[n11]);
            } else if ("RuntimeInvisibleAnnotations".equals(string)) {
                n7 = n4 + 8;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(string)) {
                n9 = n4 + 8;
            } else if ("BootstrapMethods".equals(string)) {
                int[] arrn = new int[this.readUnsignedShort(n4 + 8)];
                int n12 = n4 + 10;
                for (int i3 = 0; i3 < arrn.length; ++i3) {
                    arrn[i3] = n12;
                    n12 += 2 + this.readUnsignedShort(n12 + 2) << 1;
                }
                $Context.d = arrn;
            } else {
                $Attribute $Attribute2 = this.a(arr$Attribute, string, n4 + 8, this.readInt(n4 + 4), arrc, -1, null);
                if ($Attribute2 != null) {
                    $Attribute2.a = $Attribute;
                    $Attribute = $Attribute2;
                }
            }
            n4 += 6 + this.readInt(n4 + 4);
        }
        $ClassVisitor.visit(this.readInt(this.a[1] - 7), n5, string2, string4, string3, arrstring);
        if ((n2 & 2) == 0 && (string5 != null || string6 != null)) {
            $ClassVisitor.visitSource(string5, string6);
        }
        if (string7 != null) {
            $ClassVisitor.visitOuterClass(string7, string8, string9);
        }
        if (n6 != 0) {
            int n13 = n6 + 2;
            for (n3 = this.readUnsignedShort((int)n6); n3 > 0; --n3) {
                n13 = this.a(n13 + 2, arrc, true, $ClassVisitor.visitAnnotation(this.readUTF8(n13, arrc), true));
            }
        }
        if (n7 != 0) {
            string = (String)(n7 + 2);
            for (n3 = this.readUnsignedShort((int)n7); n3 > 0; --n3) {
                string = (String)this.a((int)(string + 2), arrc, true, $ClassVisitor.visitAnnotation(this.readUTF8((int)string, arrc), false));
            }
        }
        if (n8 != 0) {
            string = (String)(n8 + 2);
            for (n3 = this.readUnsignedShort((int)n8); n3 > 0; --n3) {
                string = (String)this.a($Context, (int)string);
                string = (String)this.a((int)(string + 2), arrc, true, $ClassVisitor.visitTypeAnnotation($Context.i, $Context.j, this.readUTF8((int)string, arrc), true));
            }
        }
        if (n9 != 0) {
            string = (String)(n9 + 2);
            for (n3 = this.readUnsignedShort((int)n9); n3 > 0; --n3) {
                string = (String)this.a($Context, (int)string);
                string = (String)this.a((int)(string + 2), arrc, true, $ClassVisitor.visitTypeAnnotation($Context.i, $Context.j, this.readUTF8((int)string, arrc), false));
            }
        }
        while ($Attribute != null) {
            $Attribute $Attribute3 = $Attribute.a;
            $Attribute.a = null;
            $ClassVisitor.visitAttribute($Attribute);
            $Attribute = $Attribute3;
        }
        if (n10 != 0) {
            n3 = n10 + 2;
            for (string = (String)this.readUnsignedShort((int)n10); string > 0; --string) {
                $ClassVisitor.visitInnerClass(this.readClass(n3, arrc), this.readClass(n3 + 2, arrc), this.readUTF8(n3 + 4, arrc), this.readUnsignedShort(n3 + 6));
                n3 += 8;
            }
        }
        n4 = this.header + 10 + 2 * arrstring.length;
        for (n3 = this.readUnsignedShort((int)(n4 - 2)); n3 > 0; --n3) {
            n4 = this.a($ClassVisitor, $Context, n4);
        }
        for (n3 = this.readUnsignedShort((int)((n4 += 2) - 2)); n3 > 0; --n3) {
            n4 = this.b($ClassVisitor, $Context, n4);
        }
        $ClassVisitor.visitEnd();
    }

    private int a($ClassVisitor $ClassVisitor, $Context $Context, int n2) {
        int n3;
        String string;
        char[] arrc = $Context.c;
        int n4 = this.readUnsignedShort(n2);
        String string2 = this.readUTF8(n2 + 2, arrc);
        String string3 = this.readUTF8(n2 + 4, arrc);
        String string4 = null;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        Object object = null;
        $Attribute $Attribute = null;
        for (int i2 = this.readUnsignedShort((int)(n2 += 6)); i2 > 0; --i2) {
            string = this.readUTF8(n2 + 2, arrc);
            if ("ConstantValue".equals(string)) {
                n3 = this.readUnsignedShort(n2 + 8);
                object = n3 == 0 ? null : this.readConst(n3, arrc);
            } else if ("Signature".equals(string)) {
                string4 = this.readUTF8(n2 + 8, arrc);
            } else if ("Deprecated".equals(string)) {
                n4 |= 131072;
            } else if ("Synthetic".equals(string)) {
                n4 |= 266240;
            } else if ("RuntimeVisibleAnnotations".equals(string)) {
                n5 = n2 + 8;
            } else if ("RuntimeVisibleTypeAnnotations".equals(string)) {
                n7 = n2 + 8;
            } else if ("RuntimeInvisibleAnnotations".equals(string)) {
                n6 = n2 + 8;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(string)) {
                n8 = n2 + 8;
            } else {
                $Attribute $Attribute2 = this.a($Context.a, string, n2 + 8, this.readInt(n2 + 4), arrc, -1, null);
                if ($Attribute2 != null) {
                    $Attribute2.a = $Attribute;
                    $Attribute = $Attribute2;
                }
            }
            n2 += 6 + this.readInt(n2 + 4);
        }
        n2 += 2;
        $FieldVisitor $FieldVisitor = $ClassVisitor.visitField(n4, string2, string3, string4, object);
        if ($FieldVisitor == null) {
            return n2;
        }
        if (n5 != 0) {
            n3 = n5 + 2;
            for (int i3 = this.readUnsignedShort((int)n5); i3 > 0; --i3) {
                n3 = this.a(n3 + 2, arrc, true, $FieldVisitor.visitAnnotation(this.readUTF8(n3, arrc), true));
            }
        }
        if (n6 != 0) {
            n3 = n6 + 2;
            for (string = (String)this.readUnsignedShort((int)n6); string > 0; --string) {
                n3 = this.a(n3 + 2, arrc, true, $FieldVisitor.visitAnnotation(this.readUTF8(n3, arrc), false));
            }
        }
        if (n7 != 0) {
            n3 = n7 + 2;
            for (string = (String)this.readUnsignedShort((int)n7); string > 0; --string) {
                n3 = this.a($Context, n3);
                n3 = this.a(n3 + 2, arrc, true, $FieldVisitor.visitTypeAnnotation($Context.i, $Context.j, this.readUTF8(n3, arrc), true));
            }
        }
        if (n8 != 0) {
            n3 = n8 + 2;
            for (string = (String)this.readUnsignedShort((int)n8); string > 0; --string) {
                n3 = this.a($Context, n3);
                n3 = this.a(n3 + 2, arrc, true, $FieldVisitor.visitTypeAnnotation($Context.i, $Context.j, this.readUTF8(n3, arrc), false));
            }
        }
        while ($Attribute != null) {
            $Attribute $Attribute3 = $Attribute.a;
            $Attribute.a = null;
            $FieldVisitor.visitAttribute($Attribute);
            $Attribute = $Attribute3;
        }
        $FieldVisitor.visitEnd();
        return n2;
    }

    private int b($ClassVisitor $ClassVisitor, $Context $Context, int n2) {
        int n3;
        Object object;
        char[] arrc = $Context.c;
        $Context.e = this.readUnsignedShort(n2);
        $Context.f = this.readUTF8(n2 + 2, arrc);
        $Context.g = this.readUTF8(n2 + 4, arrc);
        int n4 = 0;
        int n5 = 0;
        String[] arrstring = null;
        String string = null;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        int n14 = n2 += 6;
        Object object2 = null;
        for (int i2 = this.readUnsignedShort((int)n2); i2 > 0; --i2) {
            object = this.readUTF8(n2 + 2, arrc);
            if ("Code".equals(object)) {
                if (($Context.b & 1) == 0) {
                    n4 = n2 + 8;
                }
            } else if ("Exceptions".equals(object)) {
                arrstring = new String[this.readUnsignedShort(n2 + 8)];
                n5 = n2 + 10;
                for (n3 = 0; n3 < arrstring.length; ++n3) {
                    arrstring[n3] = this.readClass(n5, arrc);
                    n5 += 2;
                }
            } else if ("Signature".equals(object)) {
                string = this.readUTF8(n2 + 8, arrc);
            } else if ("Deprecated".equals(object)) {
                $Context.e |= 131072;
            } else if ("RuntimeVisibleAnnotations".equals(object)) {
                n7 = n2 + 8;
            } else if ("RuntimeVisibleTypeAnnotations".equals(object)) {
                n9 = n2 + 8;
            } else if ("AnnotationDefault".equals(object)) {
                n11 = n2 + 8;
            } else if ("Synthetic".equals(object)) {
                $Context.e |= 266240;
            } else if ("RuntimeInvisibleAnnotations".equals(object)) {
                n8 = n2 + 8;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(object)) {
                n10 = n2 + 8;
            } else if ("RuntimeVisibleParameterAnnotations".equals(object)) {
                n12 = n2 + 8;
            } else if ("RuntimeInvisibleParameterAnnotations".equals(object)) {
                n13 = n2 + 8;
            } else if ("MethodParameters".equals(object)) {
                n6 = n2 + 8;
            } else {
                $Attribute $Attribute = this.a($Context.a, (String)object, n2 + 8, this.readInt(n2 + 4), arrc, -1, null);
                if ($Attribute != null) {
                    $Attribute.a = object2;
                    object2 = $Attribute;
                }
            }
            n2 += 6 + this.readInt(n2 + 4);
        }
        n2 += 2;
        $MethodVisitor $MethodVisitor = $ClassVisitor.visitMethod($Context.e, $Context.f, $Context.g, string, arrstring);
        if ($MethodVisitor == null) {
            return n2;
        }
        if ($MethodVisitor instanceof $MethodWriter) {
            object = ($MethodWriter)$MethodVisitor;
            if (object.b.M == this && string == object.g) {
                n3 = 0;
                if (arrstring == null) {
                    n3 = object.j == 0 ? 1 : 0;
                } else if (arrstring.length == object.j) {
                    n3 = 1;
                    for (int i3 = arrstring.length - 1; i3 >= 0; --i3) {
                        if (object.k[i3] == this.readUnsignedShort(n5 -= 2)) continue;
                        n3 = 0;
                        break;
                    }
                }
                if (n3 != 0) {
                    object.h = n14;
                    object.i = n2 - n14;
                    return n2;
                }
            }
        }
        if (n6 != 0) {
            int n15 = this.b[n6] & 255;
            n3 = n6 + 1;
            while (n15 > 0) {
                $MethodVisitor.visitParameter(this.readUTF8(n3, arrc), this.readUnsignedShort(n3 + 2));
                --n15;
                n3 += 4;
            }
        }
        if (n11 != 0) {
            object = $MethodVisitor.visitAnnotationDefault();
            this.a(n11, arrc, null, ($AnnotationVisitor)object);
            if (object != null) {
                object.visitEnd();
            }
        }
        if (n7 != 0) {
            n3 = n7 + 2;
            for (int i4 = this.readUnsignedShort((int)n7); i4 > 0; --i4) {
                n3 = this.a(n3 + 2, arrc, true, $MethodVisitor.visitAnnotation(this.readUTF8(n3, arrc), true));
            }
        }
        if (n8 != 0) {
            n3 = n8 + 2;
            for (int i5 = this.readUnsignedShort((int)n8); i5 > 0; --i5) {
                n3 = this.a(n3 + 2, arrc, true, $MethodVisitor.visitAnnotation(this.readUTF8(n3, arrc), false));
            }
        }
        if (n9 != 0) {
            n3 = n9 + 2;
            for (int i6 = this.readUnsignedShort((int)n9); i6 > 0; --i6) {
                n3 = this.a($Context, n3);
                n3 = this.a(n3 + 2, arrc, true, $MethodVisitor.visitTypeAnnotation($Context.i, $Context.j, this.readUTF8(n3, arrc), true));
            }
        }
        if (n10 != 0) {
            n3 = n10 + 2;
            for (int i7 = this.readUnsignedShort((int)n10); i7 > 0; --i7) {
                n3 = this.a($Context, n3);
                n3 = this.a(n3 + 2, arrc, true, $MethodVisitor.visitTypeAnnotation($Context.i, $Context.j, this.readUTF8(n3, arrc), false));
            }
        }
        if (n12 != 0) {
            this.b($MethodVisitor, $Context, n12, true);
        }
        if (n13 != 0) {
            this.b($MethodVisitor, $Context, n13, false);
        }
        while (object2 != null) {
            object = object2.a;
            object2.a = null;
            $MethodVisitor.visitAttribute(($Attribute)object2);
            object2 = object;
        }
        if (n4 != 0) {
            $MethodVisitor.visitCode();
            this.a($MethodVisitor, $Context, n4);
        }
        $MethodVisitor.visitEnd();
        return n2;
    }

    private void a($MethodVisitor $MethodVisitor, $Context $Context, int n2) {
        int n3;
        int n4;
        int n5;
        Object object;
        int n6;
        int n7;
        byte[] arrby = this.b;
        char[] arrc = $Context.c;
        int n8 = this.readUnsignedShort(n2);
        int n9 = this.readUnsignedShort(n2 + 2);
        int n10 = this.readInt(n2 + 4);
        int n11 = n2 += 8;
        int n12 = n2 + n10;
        $Context.h = new $Label[n10 + 2];
        $Label[] arr$Label = $Context.h;
        this.readLabel(n10 + 1, arr$Label);
        block29 : while (n2 < n12) {
            n5 = n2 - n11;
            int n13 = arrby[n2] & 255;
            switch ($ClassWriter.a[n13]) {
                int n14;
                case 0: 
                case 4: {
                    ++n2;
                    continue block29;
                }
                case 9: {
                    this.readLabel(n5 + this.readShort(n2 + 1), arr$Label);
                    n2 += 3;
                    continue block29;
                }
                case 10: {
                    this.readLabel(n5 + this.readInt(n2 + 1), arr$Label);
                    n2 += 5;
                    continue block29;
                }
                case 17: {
                    n13 = arrby[n2 + 1] & 255;
                    if (n13 == 132) {
                        n2 += 6;
                        continue block29;
                    }
                    n2 += 4;
                    continue block29;
                }
                case 14: {
                    n2 = n2 + 4 - (n5 & 3);
                    this.readLabel(n5 + this.readInt(n2), arr$Label);
                    for (n14 = this.readInt((int)(n2 + 8)) - this.readInt((int)(n2 + 4)) + 1; n14 > 0; --n14) {
                        this.readLabel(n5 + this.readInt(n2 + 12), arr$Label);
                        n2 += 4;
                    }
                    n2 += 12;
                    continue block29;
                }
                case 15: {
                    n2 = n2 + 4 - (n5 & 3);
                    this.readLabel(n5 + this.readInt(n2), arr$Label);
                    for (n14 = this.readInt((int)(n2 + 4)); n14 > 0; --n14) {
                        this.readLabel(n5 + this.readInt(n2 + 12), arr$Label);
                        n2 += 8;
                    }
                    n2 += 8;
                    continue block29;
                }
                case 1: 
                case 3: 
                case 11: {
                    n2 += 2;
                    continue block29;
                }
                case 2: 
                case 5: 
                case 6: 
                case 12: 
                case 13: {
                    n2 += 3;
                    continue block29;
                }
                case 7: 
                case 8: {
                    n2 += 5;
                    continue block29;
                }
            }
            n2 += 4;
        }
        for (n5 = this.readUnsignedShort((int)n2); n5 > 0; --n5) {
            $Label $Label = this.readLabel(this.readUnsignedShort(n2 + 2), arr$Label);
            $Label $Label2 = this.readLabel(this.readUnsignedShort(n2 + 4), arr$Label);
            $Label $Label3 = this.readLabel(this.readUnsignedShort(n2 + 6), arr$Label);
            String string = this.readUTF8(this.a[this.readUnsignedShort(n2 + 8)], arrc);
            $MethodVisitor.visitTryCatchBlock($Label, $Label2, $Label3, string);
            n2 += 8;
        }
        int[] arrn = null;
        int[] arrn2 = null;
        int n15 = 0;
        int n16 = 0;
        int n17 = -1;
        int n18 = -1;
        int n19 = 0;
        int n20 = 0;
        boolean bl = true;
        boolean bl2 = ($Context.b & 8) != 0;
        int n21 = 0;
        int n22 = 0;
        int n23 = 0;
        $Context $Context2 = null;
        $Attribute $Attribute = null;
        for (n6 = this.readUnsignedShort((int)(n2 += 2)); n6 > 0; --n6) {
            object = this.readUTF8(n2 + 2, arrc);
            if ("LocalVariableTable".equals(object)) {
                if (($Context.b & 2) == 0) {
                    n19 = n2 + 8;
                    n4 = n2;
                    for (n3 = this.readUnsignedShort((int)(n2 + 8)); n3 > 0; --n3) {
                        n7 = this.readUnsignedShort(n4 + 10);
                        if (arr$Label[n7] == null) {
                            this.readLabel((int)n7, ($Label[])arr$Label).a |= 1;
                        }
                        if (arr$Label[n7 += this.readUnsignedShort(n4 + 12)] == null) {
                            this.readLabel((int)n7, ($Label[])arr$Label).a |= 1;
                        }
                        n4 += 10;
                    }
                }
            } else if ("LocalVariableTypeTable".equals(object)) {
                n20 = n2 + 8;
            } else if ("LineNumberTable".equals(object)) {
                if (($Context.b & 2) == 0) {
                    n4 = n2;
                    for (n3 = this.readUnsignedShort((int)(n2 + 8)); n3 > 0; --n3) {
                        n7 = this.readUnsignedShort(n4 + 10);
                        if (arr$Label[n7] == null) {
                            this.readLabel((int)n7, ($Label[])arr$Label).a |= 1;
                        }
                        arr$Label[n7].b = this.readUnsignedShort(n4 + 12);
                        n4 += 4;
                    }
                }
            } else if ("RuntimeVisibleTypeAnnotations".equals(object)) {
                arrn = this.a($MethodVisitor, $Context, n2 + 8, true);
                n17 = arrn.length == 0 || this.readByte(arrn[0]) < 67 ? -1 : this.readUnsignedShort(arrn[0] + 1);
            } else if ("RuntimeInvisibleTypeAnnotations".equals(object)) {
                arrn2 = this.a($MethodVisitor, $Context, n2 + 8, false);
                n18 = arrn2.length == 0 || this.readByte(arrn2[0]) < 67 ? -1 : this.readUnsignedShort(arrn2[0] + 1);
            } else if ("StackMapTable".equals(object)) {
                if (($Context.b & 4) == 0) {
                    n21 = n2 + 10;
                    n22 = this.readInt(n2 + 4);
                    n23 = this.readUnsignedShort(n2 + 8);
                }
            } else if ("StackMap".equals(object)) {
                if (($Context.b & 4) == 0) {
                    bl = false;
                    n21 = n2 + 10;
                    n22 = this.readInt(n2 + 4);
                    n23 = this.readUnsignedShort(n2 + 8);
                }
            } else {
                for (n3 = 0; n3 < $Context.a.length; ++n3) {
                    $Attribute $Attribute2;
                    if (!$Context.a[n3].type.equals(object) || ($Attribute2 = $Context.a[n3].read(this, n2 + 8, this.readInt(n2 + 4), arrc, n11 - 8, arr$Label)) == null) continue;
                    $Attribute2.a = $Attribute;
                    $Attribute = $Attribute2;
                }
            }
            n2 += 6 + this.readInt(n2 + 4);
        }
        n2 += 2;
        if (n21 != 0) {
            $Context2 = $Context;
            $Context2.o = -1;
            $Context2.p = 0;
            $Context2.q = 0;
            $Context2.r = 0;
            $Context2.t = 0;
            $Context2.s = new Object[n9];
            $Context2.u = new Object[n8];
            if (bl2) {
                this.a($Context);
            }
            for (n6 = n21; n6 < n21 + n22 - 2; ++n6) {
                int n24;
                if (arrby[n6] != 8 || (n24 = this.readUnsignedShort(n6 + 1)) < 0 || n24 >= n10 || (arrby[n11 + n24] & 255) != 187) continue;
                this.readLabel(n24, arr$Label);
            }
        }
        n2 = n11;
        while (n2 < n12) {
            n6 = n2 - n11;
            object = arr$Label[n6];
            if (object != null) {
                $MethodVisitor.visitLabel(($Label)object);
                if (($Context.b & 2) == 0 && object.b > 0) {
                    $MethodVisitor.visitLineNumber(object.b, ($Label)object);
                }
            }
            while ($Context2 != null && ($Context2.o == n6 || $Context2.o == -1)) {
                if ($Context2.o != -1) {
                    if (!bl || bl2) {
                        $MethodVisitor.visitFrame(-1, $Context2.q, $Context2.s, $Context2.t, $Context2.u);
                    } else {
                        $MethodVisitor.visitFrame($Context2.p, $Context2.r, $Context2.s, $Context2.t, $Context2.u);
                    }
                }
                if (n23 > 0) {
                    n21 = this.a(n21, bl, bl2, $Context2);
                    --n23;
                    continue;
                }
                $Context2 = null;
            }
            n3 = arrby[n2] & 255;
            switch ($ClassWriter.a[n3]) {
                Object object2;
                int n25;
                case 0: {
                    $MethodVisitor.visitInsn(n3);
                    ++n2;
                    break;
                }
                case 4: {
                    if (n3 > 54) {
                        $MethodVisitor.visitVarInsn(54 + (n3 >> 2), (n3 -= 59) & 3);
                    } else {
                        $MethodVisitor.visitVarInsn(21 + (n3 >> 2), (n3 -= 26) & 3);
                    }
                    ++n2;
                    break;
                }
                case 9: {
                    $MethodVisitor.visitJumpInsn(n3, arr$Label[n6 + this.readShort(n2 + 1)]);
                    n2 += 3;
                    break;
                }
                case 10: {
                    $MethodVisitor.visitJumpInsn(n3 - 33, arr$Label[n6 + this.readInt(n2 + 1)]);
                    n2 += 5;
                    break;
                }
                case 17: {
                    n3 = arrby[n2 + 1] & 255;
                    if (n3 == 132) {
                        $MethodVisitor.visitIincInsn(this.readUnsignedShort(n2 + 2), this.readShort(n2 + 4));
                        n2 += 6;
                        break;
                    }
                    $MethodVisitor.visitVarInsn(n3, this.readUnsignedShort(n2 + 2));
                    n2 += 4;
                    break;
                }
                case 14: {
                    n2 = n2 + 4 - (n6 & 3);
                    n4 = n6 + this.readInt(n2);
                    n7 = this.readInt(n2 + 4);
                    int n26 = this.readInt(n2 + 8);
                    object2 = new $Label[n26 - n7 + 1];
                    n2 += 12;
                    for (n25 = 0; n25 < object2.length; n25 += 1) {
                        object2[n25] = arr$Label[n6 + this.readInt(n2)];
                        n2 += 4;
                    }
                    $MethodVisitor.visitTableSwitchInsn(n7, n26, arr$Label[n4], ($Label[])object2);
                    break;
                }
                case 15: {
                    n2 = n2 + 4 - (n6 & 3);
                    n4 = n6 + this.readInt(n2);
                    n7 = this.readInt(n2 + 4);
                    int[] arrn3 = new int[n7];
                    object2 = new $Label[n7];
                    n2 += 8;
                    for (n25 = 0; n25 < n7; n25 += 1) {
                        arrn3[n25] = this.readInt(n2);
                        object2[n25] = arr$Label[n6 + this.readInt(n2 + 4)];
                        n2 += 8;
                    }
                    $MethodVisitor.visitLookupSwitchInsn(arr$Label[n4], arrn3, ($Label[])object2);
                    break;
                }
                case 3: {
                    $MethodVisitor.visitVarInsn(n3, arrby[n2 + 1] & 255);
                    n2 += 2;
                    break;
                }
                case 1: {
                    $MethodVisitor.visitIntInsn(n3, arrby[n2 + 1]);
                    n2 += 2;
                    break;
                }
                case 2: {
                    $MethodVisitor.visitIntInsn(n3, this.readShort(n2 + 1));
                    n2 += 3;
                    break;
                }
                case 11: {
                    $MethodVisitor.visitLdcInsn(this.readConst(arrby[n2 + 1] & 255, arrc));
                    n2 += 2;
                    break;
                }
                case 12: {
                    $MethodVisitor.visitLdcInsn(this.readConst(this.readUnsignedShort(n2 + 1), arrc));
                    n2 += 3;
                    break;
                }
                case 6: 
                case 7: {
                    n4 = this.a[this.readUnsignedShort(n2 + 1)];
                    n7 = arrby[n4 - 1] == 11 ? 1 : 0;
                    String string = this.readClass(n4, arrc);
                    n4 = this.a[this.readUnsignedShort(n4 + 2)];
                    object2 = this.readUTF8(n4, arrc);
                    String string2 = this.readUTF8(n4 + 2, arrc);
                    if (n3 < 182) {
                        $MethodVisitor.visitFieldInsn(n3, string, (String)object2, string2);
                    } else {
                        $MethodVisitor.visitMethodInsn(n3, string, (String)object2, string2, (boolean)n7);
                    }
                    if (n3 == 185) {
                        n2 += 5;
                        break;
                    }
                    n2 += 3;
                    break;
                }
                case 8: {
                    n4 = this.a[this.readUnsignedShort(n2 + 1)];
                    n7 = $Context.d[this.readUnsignedShort(n4)];
                    $Handle $Handle = ($Handle)this.readConst(this.readUnsignedShort(n7), arrc);
                    int n27 = this.readUnsignedShort(n7 + 2);
                    Object[] arrobject = new Object[n27];
                    n7 += 4;
                    for (int i2 = 0; i2 < n27; ++i2) {
                        arrobject[i2] = this.readConst(this.readUnsignedShort(n7), arrc);
                        n7 += 2;
                    }
                    n4 = this.a[this.readUnsignedShort(n4 + 2)];
                    String string = this.readUTF8(n4, arrc);
                    String string3 = this.readUTF8(n4 + 2, arrc);
                    $MethodVisitor.visitInvokeDynamicInsn(string, string3, $Handle, arrobject);
                    n2 += 5;
                    break;
                }
                case 5: {
                    $MethodVisitor.visitTypeInsn(n3, this.readClass(n2 + 1, arrc));
                    n2 += 3;
                    break;
                }
                case 13: {
                    $MethodVisitor.visitIincInsn(arrby[n2 + 1] & 255, arrby[n2 + 2]);
                    n2 += 3;
                    break;
                }
                default: {
                    $MethodVisitor.visitMultiANewArrayInsn(this.readClass(n2 + 1, arrc), arrby[n2 + 3] & 255);
                    n2 += 4;
                }
            }
            while (arrn != null && n15 < arrn.length && n17 <= n6) {
                if (n17 == n6) {
                    n4 = this.a($Context, arrn[n15]);
                    this.a(n4 + 2, arrc, true, $MethodVisitor.visitInsnAnnotation($Context.i, $Context.j, this.readUTF8(n4, arrc), true));
                }
                n17 = ++n15 >= arrn.length || this.readByte(arrn[n15]) < 67 ? -1 : this.readUnsignedShort(arrn[n15] + 1);
            }
            while (arrn2 != null && n16 < arrn2.length && n18 <= n6) {
                if (n18 == n6) {
                    n4 = this.a($Context, arrn2[n16]);
                    this.a(n4 + 2, arrc, true, $MethodVisitor.visitInsnAnnotation($Context.i, $Context.j, this.readUTF8(n4, arrc), false));
                }
                n18 = ++n16 >= arrn2.length || this.readByte(arrn2[n16]) < 67 ? -1 : this.readUnsignedShort(arrn2[n16] + 1);
            }
        }
        if (arr$Label[n10] != null) {
            $MethodVisitor.visitLabel(arr$Label[n10]);
        }
        if (($Context.b & 2) == 0 && n19 != 0) {
            int[] arrn4 = null;
            if (n20 != 0) {
                n2 = n20 + 2;
                arrn4 = new int[this.readUnsignedShort(n20) * 3];
                int n28 = arrn4.length;
                while (n28 > 0) {
                    arrn4[--n28] = n2 + 6;
                    arrn4[--n28] = this.readUnsignedShort(n2 + 8);
                    arrn4[--n28] = this.readUnsignedShort(n2);
                    n2 += 10;
                }
            }
            n2 = n19 + 2;
            for (object = (Object)this.readUnsignedShort((int)n19); object > 0; --object) {
                n3 = this.readUnsignedShort(n2);
                n4 = this.readUnsignedShort(n2 + 2);
                n7 = this.readUnsignedShort(n2 + 8);
                String string = null;
                if (arrn4 != null) {
                    for (int i3 = 0; i3 < arrn4.length; i3 += 3) {
                        if (arrn4[i3] != n3 || arrn4[i3 + 1] != n7) continue;
                        string = this.readUTF8(arrn4[i3 + 2], arrc);
                        break;
                    }
                }
                $MethodVisitor.visitLocalVariable(this.readUTF8(n2 + 4, arrc), this.readUTF8(n2 + 6, arrc), string, arr$Label[n3], arr$Label[n3 + n4], n7);
                n2 += 10;
            }
        }
        if (arrn != null) {
            for (n6 = 0; n6 < arrn.length; ++n6) {
                if (this.readByte((int)arrn[n6]) >> 1 != 32) continue;
                object = this.a($Context, arrn[n6]);
                object = this.a((int)(object + 2), arrc, true, $MethodVisitor.visitLocalVariableAnnotation($Context.i, $Context.j, $Context.l, $Context.m, $Context.n, this.readUTF8((int)object, arrc), true));
            }
        }
        if (arrn2 != null) {
            for (n6 = 0; n6 < arrn2.length; ++n6) {
                if (this.readByte((int)arrn2[n6]) >> 1 != 32) continue;
                object = this.a($Context, arrn2[n6]);
                object = this.a((int)(object + 2), arrc, true, $MethodVisitor.visitLocalVariableAnnotation($Context.i, $Context.j, $Context.l, $Context.m, $Context.n, this.readUTF8((int)object, arrc), false));
            }
        }
        while ($Attribute != null) {
            $Attribute $Attribute3 = $Attribute.a;
            $Attribute.a = null;
            $MethodVisitor.visitAttribute($Attribute);
            $Attribute = $Attribute3;
        }
        $MethodVisitor.visitMaxs(n8, n9);
    }

    private int[] a($MethodVisitor $MethodVisitor, $Context $Context, int n2, boolean bl) {
        char[] arrc = $Context.c;
        int[] arrn = new int[this.readUnsignedShort(n2)];
        n2 += 2;
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            Object object;
            int n3;
            arrn[i2] = n2;
            int n4 = this.readInt(n2);
            switch (n4 >>> 24) {
                case 0: 
                case 1: 
                case 22: {
                    n2 += 2;
                    break;
                }
                case 19: 
                case 20: 
                case 21: {
                    ++n2;
                    break;
                }
                case 64: 
                case 65: {
                    for (n3 = this.readUnsignedShort((int)(n2 + 1)); n3 > 0; --n3) {
                        object = this.readUnsignedShort(n2 + 3);
                        int n5 = this.readUnsignedShort(n2 + 5);
                        this.readLabel((int)object, $Context.h);
                        this.readLabel((int)(object + n5), $Context.h);
                        n2 += 6;
                    }
                    n2 += 3;
                    break;
                }
                case 71: 
                case 72: 
                case 73: 
                case 74: 
                case 75: {
                    n2 += 4;
                    break;
                }
                default: {
                    n2 += 3;
                }
            }
            n3 = this.readByte(n2);
            if (n4 >>> 24 == 66) {
                object = n3 == 0 ? null : new $TypePath(this.b, n2);
                n2 += 1 + 2 * n3;
                n2 = this.a(n2 + 2, arrc, true, $MethodVisitor.visitTryCatchAnnotation(n4, ($TypePath)object, this.readUTF8(n2, arrc), bl));
                continue;
            }
            n2 = this.a(n2 + 3 + 2 * n3, arrc, true, null);
        }
        return arrn;
    }

    private int a($Context $Context, int n2) {
        int n3;
        int n4 = this.readInt(n2);
        switch (n4 >>> 24) {
            case 0: 
            case 1: 
            case 22: {
                n4 &= -65536;
                n2 += 2;
                break;
            }
            case 19: 
            case 20: 
            case 21: {
                n4 &= -16777216;
                ++n2;
                break;
            }
            case 64: 
            case 65: {
                n4 &= -16777216;
                n3 = this.readUnsignedShort(n2 + 1);
                $Context.l = new $Label[n3];
                $Context.m = new $Label[n3];
                $Context.n = new int[n3];
                n2 += 3;
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n5 = this.readUnsignedShort(n2);
                    int n6 = this.readUnsignedShort(n2 + 2);
                    $Context.l[i2] = this.readLabel(n5, $Context.h);
                    $Context.m[i2] = this.readLabel(n5 + n6, $Context.h);
                    $Context.n[i2] = this.readUnsignedShort(n2 + 4);
                    n2 += 6;
                }
                break;
            }
            case 71: 
            case 72: 
            case 73: 
            case 74: 
            case 75: {
                n4 &= -16776961;
                n2 += 4;
                break;
            }
            default: {
                n4 &= n4 >>> 24 < 67 ? -256 : -16777216;
                n2 += 3;
            }
        }
        n3 = this.readByte(n2);
        $Context.i = n4;
        $Context.j = n3 == 0 ? null : new $TypePath(this.b, n2);
        return n2 + 1 + 2 * n3;
    }

    private void b($MethodVisitor $MethodVisitor, $Context $Context, int n2, boolean bl) {
        $AnnotationVisitor $AnnotationVisitor;
        int n3;
        int n4 = this.b[n2++] & 255;
        int n5 = $Type.getArgumentTypes($Context.g).length - n4;
        for (n3 = 0; n3 < n5; ++n3) {
            $AnnotationVisitor = $MethodVisitor.visitParameterAnnotation(n3, "Ljava/lang/Synthetic;", false);
            if ($AnnotationVisitor == null) continue;
            $AnnotationVisitor.visitEnd();
        }
        char[] arrc = $Context.c;
        while (n3 < n4 + n5) {
            int n6 = this.readUnsignedShort(n2);
            n2 += 2;
            while (n6 > 0) {
                $AnnotationVisitor = $MethodVisitor.visitParameterAnnotation(n3, this.readUTF8(n2, arrc), bl);
                n2 = this.a(n2 + 2, arrc, true, $AnnotationVisitor);
                --n6;
            }
            ++n3;
        }
    }

    private int a(int n2, char[] arrc, boolean bl, $AnnotationVisitor $AnnotationVisitor) {
        int n3 = this.readUnsignedShort(n2);
        n2 += 2;
        if (bl) {
            while (n3 > 0) {
                n2 = this.a(n2 + 2, arrc, this.readUTF8(n2, arrc), $AnnotationVisitor);
                --n3;
            }
        } else {
            while (n3 > 0) {
                n2 = this.a(n2, arrc, null, $AnnotationVisitor);
                --n3;
            }
        }
        if ($AnnotationVisitor != null) {
            $AnnotationVisitor.visitEnd();
        }
        return n2;
    }

    private int a(int n2, char[] arrc, String string, $AnnotationVisitor $AnnotationVisitor) {
        if ($AnnotationVisitor == null) {
            switch (this.b[n2] & 255) {
                case 101: {
                    return n2 + 5;
                }
                case 64: {
                    return this.a(n2 + 3, arrc, true, null);
                }
                case 91: {
                    return this.a(n2 + 1, arrc, false, null);
                }
            }
            return n2 + 3;
        }
        block5 : switch (this.b[n2++] & 255) {
            case 68: 
            case 70: 
            case 73: 
            case 74: {
                $AnnotationVisitor.visit(string, this.readConst(this.readUnsignedShort(n2), arrc));
                n2 += 2;
                break;
            }
            case 66: {
                $AnnotationVisitor.visit(string, new Byte((byte)this.readInt(this.a[this.readUnsignedShort(n2)])));
                n2 += 2;
                break;
            }
            case 90: {
                $AnnotationVisitor.visit(string, this.readInt(this.a[this.readUnsignedShort(n2)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                n2 += 2;
                break;
            }
            case 83: {
                $AnnotationVisitor.visit(string, new Short((short)this.readInt(this.a[this.readUnsignedShort(n2)])));
                n2 += 2;
                break;
            }
            case 67: {
                $AnnotationVisitor.visit(string, new Character((char)this.readInt(this.a[this.readUnsignedShort(n2)])));
                n2 += 2;
                break;
            }
            case 115: {
                $AnnotationVisitor.visit(string, this.readUTF8(n2, arrc));
                n2 += 2;
                break;
            }
            case 101: {
                $AnnotationVisitor.visitEnum(string, this.readUTF8(n2, arrc), this.readUTF8(n2 + 2, arrc));
                n2 += 4;
                break;
            }
            case 99: {
                $AnnotationVisitor.visit(string, $Type.getType(this.readUTF8(n2, arrc)));
                n2 += 2;
                break;
            }
            case 64: {
                n2 = this.a(n2 + 2, arrc, true, $AnnotationVisitor.visitAnnotation(string, this.readUTF8(n2, arrc)));
                break;
            }
            case 91: {
                int n3 = this.readUnsignedShort(n2);
                n2 += 2;
                if (n3 == 0) {
                    return this.a(n2 - 2, arrc, false, $AnnotationVisitor.visitArray(string));
                }
                switch (this.b[n2++] & 255) {
                    case 66: {
                        byte[] arrby = new byte[n3];
                        for (int i2 = 0; i2 < n3; ++i2) {
                            arrby[i2] = (byte)this.readInt(this.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        $AnnotationVisitor.visit(string, arrby);
                        --n2;
                        break block5;
                    }
                    case 90: {
                        boolean[] arrbl = new boolean[n3];
                        for (int i3 = 0; i3 < n3; ++i3) {
                            arrbl[i3] = this.readInt(this.a[this.readUnsignedShort(n2)]) != 0;
                            n2 += 3;
                        }
                        $AnnotationVisitor.visit(string, arrbl);
                        --n2;
                        break block5;
                    }
                    case 83: {
                        short[] arrs = new short[n3];
                        for (int i4 = 0; i4 < n3; ++i4) {
                            arrs[i4] = (short)this.readInt(this.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        $AnnotationVisitor.visit(string, arrs);
                        --n2;
                        break block5;
                    }
                    case 67: {
                        char[] arrc2 = new char[n3];
                        for (int i5 = 0; i5 < n3; ++i5) {
                            arrc2[i5] = (char)this.readInt(this.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        $AnnotationVisitor.visit(string, arrc2);
                        --n2;
                        break block5;
                    }
                    case 73: {
                        int[] arrn = new int[n3];
                        for (int i6 = 0; i6 < n3; ++i6) {
                            arrn[i6] = this.readInt(this.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        $AnnotationVisitor.visit(string, arrn);
                        --n2;
                        break block5;
                    }
                    case 74: {
                        long[] arrl = new long[n3];
                        for (int i7 = 0; i7 < n3; ++i7) {
                            arrl[i7] = this.readLong(this.a[this.readUnsignedShort(n2)]);
                            n2 += 3;
                        }
                        $AnnotationVisitor.visit(string, arrl);
                        --n2;
                        break block5;
                    }
                    case 70: {
                        float[] arrf = new float[n3];
                        for (int i8 = 0; i8 < n3; ++i8) {
                            arrf[i8] = Float.intBitsToFloat(this.readInt(this.a[this.readUnsignedShort(n2)]));
                            n2 += 3;
                        }
                        $AnnotationVisitor.visit(string, arrf);
                        --n2;
                        break block5;
                    }
                    case 68: {
                        double[] arrd = new double[n3];
                        for (int i9 = 0; i9 < n3; ++i9) {
                            arrd[i9] = Double.longBitsToDouble(this.readLong(this.a[this.readUnsignedShort(n2)]));
                            n2 += 3;
                        }
                        $AnnotationVisitor.visit(string, arrd);
                        --n2;
                        break block5;
                    }
                }
                n2 = this.a(n2 - 3, arrc, false, $AnnotationVisitor.visitArray(string));
            }
        }
        return n2;
    }

    private void a($Context $Context) {
        String string = $Context.g;
        Object[] arrobject = $Context.s;
        int n2 = 0;
        if (($Context.e & 8) == 0) {
            arrobject[n2++] = "<init>".equals($Context.f) ? $Opcodes.UNINITIALIZED_THIS : this.readClass(this.header + 2, $Context.c);
        }
        int n3 = 1;
        block8 : do {
            int n4 = n3;
            switch (string.charAt(n3++)) {
                case 'B': 
                case 'C': 
                case 'I': 
                case 'S': 
                case 'Z': {
                    arrobject[n2++] = $Opcodes.INTEGER;
                    continue block8;
                }
                case 'F': {
                    arrobject[n2++] = $Opcodes.FLOAT;
                    continue block8;
                }
                case 'J': {
                    arrobject[n2++] = $Opcodes.LONG;
                    continue block8;
                }
                case 'D': {
                    arrobject[n2++] = $Opcodes.DOUBLE;
                    continue block8;
                }
                case '[': {
                    while (string.charAt(n3) == '[') {
                        ++n3;
                    }
                    if (string.charAt(n3) == 'L') {
                        ++n3;
                        while (string.charAt(n3) != ';') {
                            ++n3;
                        }
                    }
                    arrobject[n2++] = string.substring(n4, ++n3);
                    continue block8;
                }
                case 'L': {
                    while (string.charAt(n3) != ';') {
                        ++n3;
                    }
                    arrobject[n2++] = string.substring(n4 + 1, n3++);
                    continue block8;
                }
            }
            break;
        } while (true);
        $Context.q = n2;
    }

    private int a(int n2, boolean bl, boolean bl2, $Context $Context) {
        int n3;
        int n4;
        char[] arrc = $Context.c;
        $Label[] arr$Label = $Context.h;
        if (bl) {
            n3 = this.b[n2++] & 255;
        } else {
            n3 = 255;
            $Context.o = -1;
        }
        $Context.r = 0;
        if (n3 < 64) {
            n4 = n3;
            $Context.p = 3;
            $Context.t = 0;
        } else if (n3 < 128) {
            n4 = n3 - 64;
            n2 = this.a($Context.u, 0, n2, arrc, arr$Label);
            $Context.p = 4;
            $Context.t = 1;
        } else {
            n4 = this.readUnsignedShort(n2);
            n2 += 2;
            if (n3 == 247) {
                n2 = this.a($Context.u, 0, n2, arrc, arr$Label);
                $Context.p = 4;
                $Context.t = 1;
            } else if (n3 >= 248 && n3 < 251) {
                $Context.p = 2;
                $Context.r = 251 - n3;
                $Context.q -= $Context.r;
                $Context.t = 0;
            } else if (n3 == 251) {
                $Context.p = 3;
                $Context.t = 0;
            } else if (n3 < 255) {
                int n5 = bl2 ? $Context.q : 0;
                for (int i2 = n3 - 251; i2 > 0; --i2) {
                    n2 = this.a($Context.s, n5++, n2, arrc, arr$Label);
                }
                $Context.p = 1;
                $Context.r = n3 - 251;
                $Context.q += $Context.r;
                $Context.t = 0;
            } else {
                $Context.p = 0;
                int n6 = this.readUnsignedShort(n2);
                n2 += 2;
                $Context.r = n6;
                $Context.q = n6;
                int n7 = 0;
                while (n6 > 0) {
                    n2 = this.a($Context.s, n7++, n2, arrc, arr$Label);
                    --n6;
                }
                n6 = this.readUnsignedShort(n2);
                n2 += 2;
                $Context.t = n6;
                n7 = 0;
                while (n6 > 0) {
                    n2 = this.a($Context.u, n7++, n2, arrc, arr$Label);
                    --n6;
                }
            }
        }
        $Context.o += n4 + 1;
        this.readLabel($Context.o, arr$Label);
        return n2;
    }

    private int a(Object[] arrobject, int n2, int n3, char[] arrc, $Label[] arr$Label) {
        int n4 = this.b[n3++] & 255;
        switch (n4) {
            case 0: {
                arrobject[n2] = $Opcodes.TOP;
                break;
            }
            case 1: {
                arrobject[n2] = $Opcodes.INTEGER;
                break;
            }
            case 2: {
                arrobject[n2] = $Opcodes.FLOAT;
                break;
            }
            case 3: {
                arrobject[n2] = $Opcodes.DOUBLE;
                break;
            }
            case 4: {
                arrobject[n2] = $Opcodes.LONG;
                break;
            }
            case 5: {
                arrobject[n2] = $Opcodes.NULL;
                break;
            }
            case 6: {
                arrobject[n2] = $Opcodes.UNINITIALIZED_THIS;
                break;
            }
            case 7: {
                arrobject[n2] = this.readClass(n3, arrc);
                n3 += 2;
                break;
            }
            default: {
                arrobject[n2] = this.readLabel(this.readUnsignedShort(n3), arr$Label);
                n3 += 2;
            }
        }
        return n3;
    }

    protected $Label readLabel(int n2, $Label[] arr$Label) {
        if (arr$Label[n2] == null) {
            arr$Label[n2] = new $Label();
        }
        return arr$Label[n2];
    }

    private int a() {
        int n2;
        int n3;
        int n4 = this.header + 8 + this.readUnsignedShort(this.header + 6) * 2;
        for (n2 = this.readUnsignedShort((int)n4); n2 > 0; --n2) {
            for (n3 = this.readUnsignedShort((int)(n4 + 8)); n3 > 0; --n3) {
                n4 += 6 + this.readInt(n4 + 12);
            }
            n4 += 8;
        }
        for (n2 = this.readUnsignedShort((int)(n4 += 2)); n2 > 0; --n2) {
            for (n3 = this.readUnsignedShort((int)(n4 + 8)); n3 > 0; --n3) {
                n4 += 6 + this.readInt(n4 + 12);
            }
            n4 += 8;
        }
        return n4 + 2;
    }

    private $Attribute a($Attribute[] arr$Attribute, String string, int n2, int n3, char[] arrc, int n4, $Label[] arr$Label) {
        for (int i2 = 0; i2 < arr$Attribute.length; ++i2) {
            if (!arr$Attribute[i2].type.equals(string)) continue;
            return arr$Attribute[i2].read(this, n2, n3, arrc, n4, arr$Label);
        }
        return new $Attribute(string).read(this, n2, n3, null, -1, null);
    }

    public int getItemCount() {
        return this.a.length;
    }

    public int getItem(int n2) {
        return this.a[n2];
    }

    public int getMaxStringLength() {
        return this.d;
    }

    public int readByte(int n2) {
        return this.b[n2] & 255;
    }

    public int readUnsignedShort(int n2) {
        byte[] arrby = this.b;
        return (arrby[n2] & 255) << 8 | arrby[n2 + 1] & 255;
    }

    public short readShort(int n2) {
        byte[] arrby = this.b;
        return (short)((arrby[n2] & 255) << 8 | arrby[n2 + 1] & 255);
    }

    public int readInt(int n2) {
        byte[] arrby = this.b;
        return (arrby[n2] & 255) << 24 | (arrby[n2 + 1] & 255) << 16 | (arrby[n2 + 2] & 255) << 8 | arrby[n2 + 3] & 255;
    }

    public long readLong(int n2) {
        long l2 = this.readInt(n2);
        long l3 = (long)this.readInt(n2 + 4) & 0xFFFFFFFFL;
        return l2 << 32 | l3;
    }

    public String readUTF8(int n2, char[] arrc) {
        int n3 = this.readUnsignedShort(n2);
        if (n2 == 0 || n3 == 0) {
            return null;
        }
        String string = this.c[n3];
        if (string != null) {
            return string;
        }
        n2 = this.a[n3];
        this.c[n3] = this.a(n2 + 2, this.readUnsignedShort(n2), arrc);
        return this.c[n3];
    }

    private String a(int n2, int n3, char[] arrc) {
        int n4 = n2 + n3;
        byte[] arrby = this.b;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        while (n2 < n4) {
            int n8 = arrby[n2++];
            switch (n6) {
                case 0: {
                    if ((n8 &= 255) < 128) {
                        arrc[n5++] = (char)n8;
                        break;
                    }
                    if (n8 < 224 && n8 > 191) {
                        n7 = (char)(n8 & 31);
                        n6 = 1;
                        break;
                    }
                    n7 = (char)(n8 & 15);
                    n6 = 2;
                    break;
                }
                case 1: {
                    arrc[n5++] = (char)(n7 << 6 | n8 & 63);
                    n6 = 0;
                    break;
                }
                case 2: {
                    n7 = (char)(n7 << 6 | n8 & 63);
                    n6 = 1;
                }
            }
        }
        return new String(arrc, 0, n5);
    }

    public String readClass(int n2, char[] arrc) {
        return this.readUTF8(this.a[this.readUnsignedShort(n2)], arrc);
    }

    public Object readConst(int n2, char[] arrc) {
        int n3 = this.a[n2];
        switch (this.b[n3 - 1]) {
            case 3: {
                return new Integer(this.readInt(n3));
            }
            case 4: {
                return new Float(Float.intBitsToFloat(this.readInt(n3)));
            }
            case 5: {
                return new Long(this.readLong(n3));
            }
            case 6: {
                return new Double(Double.longBitsToDouble(this.readLong(n3)));
            }
            case 7: {
                return $Type.getObjectType(this.readUTF8(n3, arrc));
            }
            case 8: {
                return this.readUTF8(n3, arrc);
            }
            case 16: {
                return $Type.getMethodType(this.readUTF8(n3, arrc));
            }
        }
        int n4 = this.readByte(n3);
        int[] arrn = this.a;
        int n5 = arrn[this.readUnsignedShort(n3 + 1)];
        String string = this.readClass(n5, arrc);
        n5 = arrn[this.readUnsignedShort(n5 + 2)];
        String string2 = this.readUTF8(n5, arrc);
        String string3 = this.readUTF8(n5 + 2, arrc);
        return new $Handle(n4, string, string2, string3);
    }
}

