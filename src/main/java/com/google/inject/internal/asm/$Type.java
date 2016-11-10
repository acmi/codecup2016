/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class $Type {
    public static final int VOID = 0;
    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int BYTE = 3;
    public static final int SHORT = 4;
    public static final int INT = 5;
    public static final int FLOAT = 6;
    public static final int LONG = 7;
    public static final int DOUBLE = 8;
    public static final int ARRAY = 9;
    public static final int OBJECT = 10;
    public static final int METHOD = 11;
    public static final $Type VOID_TYPE;
    public static final $Type BOOLEAN_TYPE;
    public static final $Type CHAR_TYPE;
    public static final $Type BYTE_TYPE;
    public static final $Type SHORT_TYPE;
    public static final $Type INT_TYPE;
    public static final $Type FLOAT_TYPE;
    public static final $Type LONG_TYPE;
    public static final $Type DOUBLE_TYPE;
    private final int a;
    private final char[] b;
    private final int c;
    private final int d;

    private $Type(int n2, char[] arrc, int n3, int n4) {
        this.a = n2;
        this.b = arrc;
        this.c = n3;
        this.d = n4;
    }

    public static $Type getType(String string) {
        return $Type.a(string.toCharArray(), 0);
    }

    public static $Type getObjectType(String string) {
        char[] arrc = string.toCharArray();
        return new $Type(arrc[0] == '[' ? 9 : 10, arrc, 0, arrc.length);
    }

    public static $Type getMethodType(String string) {
        return $Type.a(string.toCharArray(), 0);
    }

    public static /* varargs */ $Type getMethodType($Type $Type, $Type ... arr$Type) {
        return $Type.getType($Type.getMethodDescriptor($Type, arr$Type));
    }

    public static $Type getType(Class class_) {
        if (class_.isPrimitive()) {
            if (class_ == Integer.TYPE) {
                return INT_TYPE;
            }
            if (class_ == Void.TYPE) {
                return VOID_TYPE;
            }
            if (class_ == Boolean.TYPE) {
                return BOOLEAN_TYPE;
            }
            if (class_ == Byte.TYPE) {
                return BYTE_TYPE;
            }
            if (class_ == Character.TYPE) {
                return CHAR_TYPE;
            }
            if (class_ == Short.TYPE) {
                return SHORT_TYPE;
            }
            if (class_ == Double.TYPE) {
                return DOUBLE_TYPE;
            }
            if (class_ == Float.TYPE) {
                return FLOAT_TYPE;
            }
            return LONG_TYPE;
        }
        return $Type.getType($Type.getDescriptor(class_));
    }

    public static $Type getType(Constructor constructor) {
        return $Type.getType($Type.getConstructorDescriptor(constructor));
    }

    public static $Type getType(Method method) {
        return $Type.getType($Type.getMethodDescriptor(method));
    }

    public static $Type[] getArgumentTypes(String string) {
        char c2;
        char[] arrc = string.toCharArray();
        int n2 = 1;
        int n3 = 0;
        while ((c2 = arrc[n2++]) != ')') {
            if (c2 == 'L') {
                while (arrc[n2++] != ';') {
                }
                ++n3;
                continue;
            }
            if (c2 == '[') continue;
            ++n3;
        }
        $Type[] arr$Type = new $Type[n3];
        n2 = 1;
        n3 = 0;
        while (arrc[n2] != ')') {
            arr$Type[n3] = $Type.a(arrc, n2);
            n2 += arr$Type[n3].d + (arr$Type[n3].a == 10 ? 2 : 0);
            ++n3;
        }
        return arr$Type;
    }

    public static $Type[] getArgumentTypes(Method method) {
        Class<?>[] arrclass = method.getParameterTypes();
        $Type[] arr$Type = new $Type[arrclass.length];
        for (int i2 = arrclass.length - 1; i2 >= 0; --i2) {
            arr$Type[i2] = $Type.getType(arrclass[i2]);
        }
        return arr$Type;
    }

    public static $Type getReturnType(String string) {
        char[] arrc = string.toCharArray();
        return $Type.a(arrc, string.indexOf(41) + 1);
    }

    public static $Type getReturnType(Method method) {
        return $Type.getType(method.getReturnType());
    }

    public static int getArgumentsAndReturnSizes(String string) {
        int n2 = 1;
        int n3 = 1;
        do {
            char c2;
            if ((c2 = string.charAt(n3++)) == ')') {
                c2 = string.charAt(n3);
                return n2 << 2 | (c2 == 'V' ? 0 : (c2 == 'D' || c2 == 'J' ? 2 : 1));
            }
            if (c2 == 'L') {
                while (string.charAt(n3++) != ';') {
                }
                ++n2;
                continue;
            }
            if (c2 == '[') {
                while ((c2 = string.charAt(n3)) == '[') {
                    ++n3;
                }
                if (c2 != 'D' && c2 != 'J') continue;
                --n2;
                continue;
            }
            if (c2 == 'D' || c2 == 'J') {
                n2 += 2;
                continue;
            }
            ++n2;
        } while (true);
    }

    private static $Type a(char[] arrc, int n2) {
        switch (arrc[n2]) {
            case 'V': {
                return VOID_TYPE;
            }
            case 'Z': {
                return BOOLEAN_TYPE;
            }
            case 'C': {
                return CHAR_TYPE;
            }
            case 'B': {
                return BYTE_TYPE;
            }
            case 'S': {
                return SHORT_TYPE;
            }
            case 'I': {
                return INT_TYPE;
            }
            case 'F': {
                return FLOAT_TYPE;
            }
            case 'J': {
                return LONG_TYPE;
            }
            case 'D': {
                return DOUBLE_TYPE;
            }
            case '[': {
                int n3 = 1;
                while (arrc[n2 + n3] == '[') {
                    ++n3;
                }
                if (arrc[n2 + n3] == 'L') {
                    ++n3;
                    while (arrc[n2 + n3] != ';') {
                        ++n3;
                    }
                }
                return new $Type(9, arrc, n2, n3 + 1);
            }
            case 'L': {
                int n4 = 1;
                while (arrc[n2 + n4] != ';') {
                    ++n4;
                }
                return new $Type(10, arrc, n2 + 1, n4 - 1);
            }
        }
        return new $Type(11, arrc, n2, arrc.length - n2);
    }

    public int getSort() {
        return this.a;
    }

    public int getDimensions() {
        int n2 = 1;
        while (this.b[this.c + n2] == '[') {
            ++n2;
        }
        return n2;
    }

    public $Type getElementType() {
        return $Type.a(this.b, this.c + this.getDimensions());
    }

    public String getClassName() {
        switch (this.a) {
            case 0: {
                return "void";
            }
            case 1: {
                return "boolean";
            }
            case 2: {
                return "char";
            }
            case 3: {
                return "byte";
            }
            case 4: {
                return "short";
            }
            case 5: {
                return "int";
            }
            case 6: {
                return "float";
            }
            case 7: {
                return "long";
            }
            case 8: {
                return "double";
            }
            case 9: {
                StringBuffer stringBuffer = new StringBuffer(this.getElementType().getClassName());
                for (int i2 = this.getDimensions(); i2 > 0; --i2) {
                    stringBuffer.append("[]");
                }
                return stringBuffer.toString();
            }
            case 10: {
                return new String(this.b, this.c, this.d).replace('/', '.');
            }
        }
        return null;
    }

    public String getInternalName() {
        return new String(this.b, this.c, this.d);
    }

    public $Type[] getArgumentTypes() {
        return $Type.getArgumentTypes(this.getDescriptor());
    }

    public $Type getReturnType() {
        return $Type.getReturnType(this.getDescriptor());
    }

    public int getArgumentsAndReturnSizes() {
        return $Type.getArgumentsAndReturnSizes(this.getDescriptor());
    }

    public String getDescriptor() {
        StringBuffer stringBuffer = new StringBuffer();
        this.a(stringBuffer);
        return stringBuffer.toString();
    }

    public static /* varargs */ String getMethodDescriptor($Type $Type, $Type ... arr$Type) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (int i2 = 0; i2 < arr$Type.length; ++i2) {
            arr$Type[i2].a(stringBuffer);
        }
        stringBuffer.append(')');
        $Type.a(stringBuffer);
        return stringBuffer.toString();
    }

    private void a(StringBuffer stringBuffer) {
        if (this.b == null) {
            stringBuffer.append((char)((this.c & -16777216) >>> 24));
        } else if (this.a == 10) {
            stringBuffer.append('L');
            stringBuffer.append(this.b, this.c, this.d);
            stringBuffer.append(';');
        } else {
            stringBuffer.append(this.b, this.c, this.d);
        }
    }

    public static String getInternalName(Class class_) {
        return class_.getName().replace('.', '/');
    }

    public static String getDescriptor(Class class_) {
        StringBuffer stringBuffer = new StringBuffer();
        $Type.a(stringBuffer, class_);
        return stringBuffer.toString();
    }

    public static String getConstructorDescriptor(Constructor constructor) {
        Class<?>[] arrclass = constructor.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (int i2 = 0; i2 < arrclass.length; ++i2) {
            $Type.a(stringBuffer, arrclass[i2]);
        }
        return stringBuffer.append(")V").toString();
    }

    public static String getMethodDescriptor(Method method) {
        Class<?>[] arrclass = method.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (int i2 = 0; i2 < arrclass.length; ++i2) {
            $Type.a(stringBuffer, arrclass[i2]);
        }
        stringBuffer.append(')');
        $Type.a(stringBuffer, method.getReturnType());
        return stringBuffer.toString();
    }

    private static void a(StringBuffer stringBuffer, Class class_) {
        Class class_2 = class_;
        do {
            if (class_2.isPrimitive()) {
                int n2 = class_2 == Integer.TYPE ? 73 : (class_2 == Void.TYPE ? 86 : (class_2 == Boolean.TYPE ? 90 : (class_2 == Byte.TYPE ? 66 : (class_2 == Character.TYPE ? 67 : (class_2 == Short.TYPE ? 83 : (class_2 == Double.TYPE ? 68 : (class_2 == Float.TYPE ? 70 : 74)))))));
                stringBuffer.append((char)n2);
                return;
            }
            if (!class_2.isArray()) break;
            stringBuffer.append('[');
            class_2 = class_2.getComponentType();
        } while (true);
        stringBuffer.append('L');
        String string = class_2.getName();
        int n3 = string.length();
        for (int i2 = 0; i2 < n3; ++i2) {
            char c2 = string.charAt(i2);
            stringBuffer.append(c2 == '.' ? '/' : (char)c2);
        }
        stringBuffer.append(';');
    }

    public int getSize() {
        return this.b == null ? this.c & 255 : 1;
    }

    public int getOpcode(int n2) {
        if (n2 == 46 || n2 == 79) {
            return n2 + (this.b == null ? (this.c & 65280) >> 8 : 4);
        }
        return n2 + (this.b == null ? (this.c & 16711680) >> 16 : 4);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof $Type)) {
            return false;
        }
        $Type $Type = ($Type)object;
        if (this.a != $Type.a) {
            return false;
        }
        if (this.a >= 9) {
            if (this.d != $Type.d) {
                return false;
            }
            int n2 = this.c;
            int n3 = $Type.c;
            int n4 = n2 + this.d;
            while (n2 < n4) {
                if (this.b[n2] != $Type.b[n3]) {
                    return false;
                }
                ++n2;
                ++n3;
            }
        }
        return true;
    }

    public int hashCode() {
        int n2 = 13 * this.a;
        if (this.a >= 9) {
            int n3;
            int n4 = n3 + this.d;
            for (n3 = this.c; n3 < n4; ++n3) {
                n2 = 17 * (n2 + this.b[n3]);
            }
        }
        return n2;
    }

    public String toString() {
        return this.getDescriptor();
    }

    static {
        $Type._clinit_();
        VOID_TYPE = new $Type(0, null, 1443168256, 1);
        BOOLEAN_TYPE = new $Type(1, null, 1509950721, 1);
        CHAR_TYPE = new $Type(2, null, 1124075009, 1);
        BYTE_TYPE = new $Type(3, null, 1107297537, 1);
        SHORT_TYPE = new $Type(4, null, 1392510721, 1);
        INT_TYPE = new $Type(5, null, 1224736769, 1);
        FLOAT_TYPE = new $Type(6, null, 1174536705, 1);
        LONG_TYPE = new $Type(7, null, 1241579778, 1);
        DOUBLE_TYPE = new $Type(8, null, 1141048066, 1);
    }

    static void _clinit_() {
    }
}

