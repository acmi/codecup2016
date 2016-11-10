/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.inject.internal.asm.
 */
package com.google.inject.internal.asm;

import com.google.inject.internal.asm.;
import com.google.inject.internal.asm.$ByteVector;
import com.google.inject.internal.asm.$ClassReader;
import com.google.inject.internal.asm.$ClassWriter;
import com.google.inject.internal.asm.$Label;

public class $Attribute {
    public final String type;
    byte[] b;
    $Attribute a;

    protected $Attribute(String string) {
        this.type = string;
    }

    public boolean isUnknown() {
        return true;
    }

    public boolean isCodeAttribute() {
        return false;
    }

    protected .Label[] getLabels() {
        return null;
    }

    protected $Attribute read($ClassReader $ClassReader, int n2, int n3, char[] arrc, int n4, .Label[] arrlabel) {
        $Attribute $Attribute = new $Attribute(this.type);
        $Attribute.b = new byte[n3];
        System.arraycopy($ClassReader.b, n2, $Attribute.b, 0, n3);
        return $Attribute;
    }

    protected $ByteVector write($ClassWriter $ClassWriter, byte[] arrby, int n2, int n3, int n4) {
        $ByteVector $ByteVector = new $ByteVector();
        $ByteVector.a = this.b;
        $ByteVector.b = this.b.length;
        return $ByteVector;
    }

    final int a() {
        int n2 = 0;
        $Attribute $Attribute = this;
        while ($Attribute != null) {
            ++n2;
            $Attribute = $Attribute.a;
        }
        return n2;
    }

    final int a($ClassWriter $ClassWriter, byte[] arrby, int n2, int n3, int n4) {
        $Attribute $Attribute = this;
        int n5 = 0;
        while ($Attribute != null) {
            $ClassWriter.newUTF8($Attribute.type);
            n5 += $Attribute.write(($ClassWriter)$ClassWriter, (byte[])arrby, (int)n2, (int)n3, (int)n4).b + 6;
            $Attribute = $Attribute.a;
        }
        return n5;
    }

    final void a($ClassWriter $ClassWriter, byte[] arrby, int n2, int n3, int n4, $ByteVector $ByteVector) {
        $Attribute $Attribute = this;
        while ($Attribute != null) {
            $ByteVector $ByteVector2 = $Attribute.write($ClassWriter, arrby, n2, n3, n4);
            $ByteVector.putShort($ClassWriter.newUTF8($Attribute.type)).putInt($ByteVector2.b);
            $ByteVector.putByteArray($ByteVector2.a, 0, $ByteVector2.b);
            $Attribute = $Attribute.a;
        }
    }
}

