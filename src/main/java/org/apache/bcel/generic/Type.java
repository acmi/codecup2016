/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.util.ArrayList;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.ReferenceType;

public abstract class Type {
    protected byte type;
    protected String signature;
    public static final BasicType VOID = new BasicType(12);
    public static final BasicType BOOLEAN = new BasicType(4);
    public static final BasicType INT = new BasicType(10);
    public static final BasicType SHORT = new BasicType(9);
    public static final BasicType BYTE = new BasicType(8);
    public static final BasicType LONG = new BasicType(11);
    public static final BasicType DOUBLE = new BasicType(7);
    public static final BasicType FLOAT = new BasicType(6);
    public static final BasicType CHAR = new BasicType(5);
    public static final ObjectType OBJECT = new ObjectType("java.lang.Object");
    public static final ObjectType STRING = new ObjectType("java.lang.String");
    public static final ObjectType STRINGBUFFER = new ObjectType("java.lang.StringBuffer");
    public static final ObjectType THROWABLE = new ObjectType("java.lang.Throwable");
    public static final Type[] NO_ARGS = new Type[0];
    public static final ReferenceType NULL = new ReferenceType();
    public static final Type UNKNOWN = new Type(15, "<unknown object>"){};
    private static int consumed_chars = 0;

    protected Type(byte by, String string) {
        this.type = by;
        this.signature = string;
    }

    public String getSignature() {
        return this.signature;
    }

    public byte getType() {
        return this.type;
    }

    public int getSize() {
        switch (this.type) {
            case 7: 
            case 11: {
                return 2;
            }
            case 12: {
                return 0;
            }
        }
        return 1;
    }

    public String toString() {
        return this.equals(NULL) || this.type >= 15 ? this.signature : Utility.signatureToString(this.signature, false);
    }

    public static String getMethodSignature(Type type, Type[] arrtype) {
        StringBuffer stringBuffer = new StringBuffer("(");
        int n2 = arrtype == null ? 0 : arrtype.length;
        int n3 = 0;
        while (n3 < n2) {
            stringBuffer.append(arrtype[n3].getSignature());
            ++n3;
        }
        stringBuffer.append(')');
        stringBuffer.append(type.getSignature());
        return stringBuffer.toString();
    }

    public static final Type getType(String string) throws StringIndexOutOfBoundsException {
        byte by = Utility.typeOfSignature(string);
        if (by <= 12) {
            consumed_chars = 1;
            return BasicType.getType(by);
        }
        if (by == 13) {
            int n2 = 0;
            while (string.charAt(++n2) == '[') {
            }
            Type type = Type.getType(string.substring(n2));
            consumed_chars += n2;
            return new ArrayType(type, n2);
        }
        int n3 = string.indexOf(59);
        if (n3 < 0) {
            throw new ClassFormatError("Invalid signature: " + string);
        }
        consumed_chars = n3 + 1;
        return new ObjectType(string.substring(1, n3).replace('/', '.'));
    }

    public static Type getReturnType(String string) {
        try {
            int n2 = string.lastIndexOf(41) + 1;
            return Type.getType(string.substring(n2));
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new ClassFormatError("Invalid method signature: " + string);
        }
    }

    public static Type[] getArgumentTypes(String string) {
        ArrayList<Type> arrayList = new ArrayList<Type>();
        try {
            if (string.charAt(0) != '(') {
                throw new ClassFormatError("Invalid method signature: " + string);
            }
            int n2 = 1;
            while (string.charAt(n2) != ')') {
                arrayList.add(Type.getType(string.substring(n2)));
                n2 += consumed_chars;
            }
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new ClassFormatError("Invalid method signature: " + string);
        }
        Type[] arrtype = new Type[arrayList.size()];
        arrayList.toArray(arrtype);
        return arrtype;
    }

}

