/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$CollectionUtils;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class $TypeUtils {
    private static final Map transforms = new HashMap();
    private static final Map rtransforms = new HashMap();

    private $TypeUtils() {
    }

    public static $Type getType(String string) {
        String string2 = String.valueOf(string.replace('.', '/'));
        return $Type.getType(new StringBuilder(2 + String.valueOf(string2).length()).append("L").append(string2).append(";").toString());
    }

    public static boolean isFinal(int n2) {
        return (16 & n2) != 0;
    }

    public static boolean isStatic(int n2) {
        return (8 & n2) != 0;
    }

    public static boolean isProtected(int n2) {
        return (4 & n2) != 0;
    }

    public static boolean isPublic(int n2) {
        return (1 & n2) != 0;
    }

    public static boolean isAbstract(int n2) {
        return (1024 & n2) != 0;
    }

    public static boolean isInterface(int n2) {
        return (512 & n2) != 0;
    }

    public static boolean isPrivate(int n2) {
        return (2 & n2) != 0;
    }

    public static boolean isSynthetic(int n2) {
        return (4096 & n2) != 0;
    }

    public static boolean isBridge(int n2) {
        return (64 & n2) != 0;
    }

    public static String getPackageName($Type $Type) {
        return $TypeUtils.getPackageName($TypeUtils.getClassName($Type));
    }

    public static String getPackageName(String string) {
        int n2 = string.lastIndexOf(46);
        return n2 < 0 ? "" : string.substring(0, n2);
    }

    public static String upperFirst(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        char c2 = Character.toUpperCase(string.charAt(0));
        String string2 = String.valueOf(string.substring(1));
        return new StringBuilder(1 + String.valueOf(string2).length()).append(c2).append(string2).toString();
    }

    public static String getClassName($Type $Type) {
        if ($TypeUtils.isPrimitive($Type)) {
            return (String)rtransforms.get($Type.getDescriptor());
        }
        if ($TypeUtils.isArray($Type)) {
            return String.valueOf($TypeUtils.getClassName($TypeUtils.getComponentType($Type))).concat("[]");
        }
        return $Type.getClassName();
    }

    public static $Type[] add($Type[] arr$Type, $Type $Type) {
        if (arr$Type == null) {
            return new $Type[]{$Type};
        }
        List<$Type> list = Arrays.asList(arr$Type);
        if (list.contains($Type)) {
            return arr$Type;
        }
        $Type[] arr$Type2 = new $Type[arr$Type.length + 1];
        System.arraycopy(arr$Type, 0, arr$Type2, 0, arr$Type.length);
        arr$Type2[arr$Type.length] = $Type;
        return arr$Type2;
    }

    public static $Type[] add($Type[] arr$Type, $Type[] arr$Type2) {
        $Type[] arr$Type3 = new $Type[arr$Type.length + arr$Type2.length];
        System.arraycopy(arr$Type, 0, arr$Type3, 0, arr$Type.length);
        System.arraycopy(arr$Type2, 0, arr$Type3, arr$Type.length, arr$Type2.length);
        return arr$Type3;
    }

    public static $Type fromInternalName(String string) {
        return $Type.getType(new StringBuilder(2 + String.valueOf(string).length()).append("L").append(string).append(";").toString());
    }

    public static $Type[] fromInternalNames(String[] arrstring) {
        if (arrstring == null) {
            return null;
        }
        $Type[] arr$Type = new $Type[arrstring.length];
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            arr$Type[i2] = $TypeUtils.fromInternalName(arrstring[i2]);
        }
        return arr$Type;
    }

    public static int getStackSize($Type[] arr$Type) {
        int n2 = 0;
        for (int i2 = 0; i2 < arr$Type.length; ++i2) {
            n2 += arr$Type[i2].getSize();
        }
        return n2;
    }

    public static String[] toInternalNames($Type[] arr$Type) {
        if (arr$Type == null) {
            return null;
        }
        String[] arrstring = new String[arr$Type.length];
        for (int i2 = 0; i2 < arr$Type.length; ++i2) {
            arrstring[i2] = arr$Type[i2].getInternalName();
        }
        return arrstring;
    }

    public static $Signature parseSignature(String string) {
        int n2 = string.indexOf(32);
        int n3 = string.indexOf(40, n2);
        int n4 = string.indexOf(41, n3);
        String string2 = string.substring(0, n2);
        String string3 = string.substring(n2 + 1, n3);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        Iterator iterator = $TypeUtils.parseTypes(string, n3 + 1, n4).iterator();
        while (iterator.hasNext()) {
            stringBuffer.append(iterator.next());
        }
        stringBuffer.append(')');
        stringBuffer.append($TypeUtils.map(string2));
        return new $Signature(string3, stringBuffer.toString());
    }

    public static $Type parseType(String string) {
        return $Type.getType($TypeUtils.map(string));
    }

    public static $Type[] parseTypes(String string) {
        List list = $TypeUtils.parseTypes(string, 0, string.length());
        $Type[] arr$Type = new $Type[list.size()];
        for (int i2 = 0; i2 < arr$Type.length; ++i2) {
            arr$Type[i2] = $Type.getType((String)list.get(i2));
        }
        return arr$Type;
    }

    public static $Signature parseConstructor($Type[] arr$Type) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for (int i2 = 0; i2 < arr$Type.length; ++i2) {
            stringBuffer.append(arr$Type[i2].getDescriptor());
        }
        stringBuffer.append(")");
        stringBuffer.append("V");
        return new $Signature("<init>", stringBuffer.toString());
    }

    public static $Signature parseConstructor(String string) {
        return $TypeUtils.parseSignature(new StringBuilder(13 + String.valueOf(string).length()).append("void <init>(").append(string).append(")").toString());
    }

    private static List parseTypes(String string, int n2, int n3) {
        int n4;
        ArrayList<String> arrayList = new ArrayList<String>(5);
        while ((n4 = string.indexOf(44, n2)) >= 0) {
            arrayList.add($TypeUtils.map(string.substring(n2, n4).trim()));
            n2 = n4 + 1;
        }
        arrayList.add($TypeUtils.map(string.substring(n2, n3).trim()));
        return arrayList;
    }

    private static String map(String string) {
        if (string.equals("")) {
            return string;
        }
        String string2 = (String)transforms.get(string);
        if (string2 != null) {
            return string2;
        }
        if (string.indexOf(46) < 0) {
            String string3 = String.valueOf(string);
            return $TypeUtils.map(string3.length() != 0 ? "java.lang.".concat(string3) : new String("java.lang."));
        }
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        while ((n2 = string.indexOf("[]", n2) + 1) > 0) {
            stringBuffer.append('[');
        }
        string = string.substring(0, string.length() - stringBuffer.length() * 2);
        stringBuffer.append('L').append(string.replace('.', '/')).append(';');
        return stringBuffer.toString();
    }

    public static $Type getBoxedType($Type $Type) {
        switch ($Type.getSort()) {
            case 2: {
                return $Constants.TYPE_CHARACTER;
            }
            case 1: {
                return $Constants.TYPE_BOOLEAN;
            }
            case 8: {
                return $Constants.TYPE_DOUBLE;
            }
            case 6: {
                return $Constants.TYPE_FLOAT;
            }
            case 7: {
                return $Constants.TYPE_LONG;
            }
            case 5: {
                return $Constants.TYPE_INTEGER;
            }
            case 4: {
                return $Constants.TYPE_SHORT;
            }
            case 3: {
                return $Constants.TYPE_BYTE;
            }
        }
        return $Type;
    }

    public static $Type getUnboxedType($Type $Type) {
        if ($Constants.TYPE_INTEGER.equals($Type)) {
            return $Type.INT_TYPE;
        }
        if ($Constants.TYPE_BOOLEAN.equals($Type)) {
            return $Type.BOOLEAN_TYPE;
        }
        if ($Constants.TYPE_DOUBLE.equals($Type)) {
            return $Type.DOUBLE_TYPE;
        }
        if ($Constants.TYPE_LONG.equals($Type)) {
            return $Type.LONG_TYPE;
        }
        if ($Constants.TYPE_CHARACTER.equals($Type)) {
            return $Type.CHAR_TYPE;
        }
        if ($Constants.TYPE_BYTE.equals($Type)) {
            return $Type.BYTE_TYPE;
        }
        if ($Constants.TYPE_FLOAT.equals($Type)) {
            return $Type.FLOAT_TYPE;
        }
        if ($Constants.TYPE_SHORT.equals($Type)) {
            return $Type.SHORT_TYPE;
        }
        return $Type;
    }

    public static boolean isArray($Type $Type) {
        return $Type.getSort() == 9;
    }

    public static $Type getComponentType($Type $Type) {
        if (!$TypeUtils.isArray($Type)) {
            String string = String.valueOf($Type);
            throw new IllegalArgumentException(new StringBuilder(21 + String.valueOf(string).length()).append("Type ").append(string).append(" is not an array").toString());
        }
        return $Type.getType($Type.getDescriptor().substring(1));
    }

    public static boolean isPrimitive($Type $Type) {
        switch ($Type.getSort()) {
            case 9: 
            case 10: {
                return false;
            }
        }
        return true;
    }

    public static String emulateClassGetName($Type $Type) {
        if ($TypeUtils.isArray($Type)) {
            return $Type.getDescriptor().replace('/', '.');
        }
        return $TypeUtils.getClassName($Type);
    }

    public static boolean isConstructor($MethodInfo $MethodInfo) {
        return $MethodInfo.getSignature().getName().equals("<init>");
    }

    public static $Type[] getTypes(Class[] arrclass) {
        if (arrclass == null) {
            return null;
        }
        $Type[] arr$Type = new $Type[arrclass.length];
        for (int i2 = 0; i2 < arrclass.length; ++i2) {
            arr$Type[i2] = $Type.getType(arrclass[i2]);
        }
        return arr$Type;
    }

    public static int ICONST(int n2) {
        switch (n2) {
            case -1: {
                return 2;
            }
            case 0: {
                return 3;
            }
            case 1: {
                return 4;
            }
            case 2: {
                return 5;
            }
            case 3: {
                return 6;
            }
            case 4: {
                return 7;
            }
            case 5: {
                return 8;
            }
        }
        return -1;
    }

    public static int LCONST(long l2) {
        if (l2 == 0) {
            return 9;
        }
        if (l2 == 1) {
            return 10;
        }
        return -1;
    }

    public static int FCONST(float f2) {
        if (f2 == 0.0f) {
            return 11;
        }
        if (f2 == 1.0f) {
            return 12;
        }
        if (f2 == 2.0f) {
            return 13;
        }
        return -1;
    }

    public static int DCONST(double d2) {
        if (d2 == 0.0) {
            return 14;
        }
        if (d2 == 1.0) {
            return 15;
        }
        return -1;
    }

    public static int NEWARRAY($Type $Type) {
        switch ($Type.getSort()) {
            case 3: {
                return 8;
            }
            case 2: {
                return 5;
            }
            case 8: {
                return 7;
            }
            case 6: {
                return 6;
            }
            case 5: {
                return 10;
            }
            case 7: {
                return 11;
            }
            case 4: {
                return 9;
            }
            case 1: {
                return 4;
            }
        }
        return -1;
    }

    public static String escapeType(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = string.length();
        block9 : for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            switch (c2) {
                case '$': {
                    stringBuffer.append("$24");
                    continue block9;
                }
                case '.': {
                    stringBuffer.append("$2E");
                    continue block9;
                }
                case '[': {
                    stringBuffer.append("$5B");
                    continue block9;
                }
                case ';': {
                    stringBuffer.append("$3B");
                    continue block9;
                }
                case '(': {
                    stringBuffer.append("$28");
                    continue block9;
                }
                case ')': {
                    stringBuffer.append("$29");
                    continue block9;
                }
                case '/': {
                    stringBuffer.append("$2F");
                    continue block9;
                }
                default: {
                    stringBuffer.append(c2);
                }
            }
        }
        return stringBuffer.toString();
    }

    static {
        transforms.put("void", "V");
        transforms.put("byte", "B");
        transforms.put("char", "C");
        transforms.put("double", "D");
        transforms.put("float", "F");
        transforms.put("int", "I");
        transforms.put("long", "J");
        transforms.put("short", "S");
        transforms.put("boolean", "Z");
        $CollectionUtils.reverse(transforms, rtransforms);
    }
}

