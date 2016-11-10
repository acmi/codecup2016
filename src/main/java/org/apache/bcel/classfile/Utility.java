/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

import java.io.IOException;
import java.io.PrintStream;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.util.ByteSequence;

public abstract class Utility {
    private static int consumed_chars;
    private static boolean wide;
    private static int[] CHAR_MAP;
    private static int[] MAP_CHAR;

    public static final String accessToString(int n2) {
        return Utility.accessToString(n2, false);
    }

    public static final String accessToString(int n2, boolean bl) {
        StringBuffer stringBuffer = new StringBuffer();
        int n3 = 0;
        int n4 = 0;
        while (n3 < 1024) {
            n3 = Utility.pow2(n4);
            if ((n2 & n3) != 0 && (!bl || n3 != 32 && n3 != 512)) {
                stringBuffer.append(Constants.ACCESS_NAMES[n4] + " ");
            }
            ++n4;
        }
        return stringBuffer.toString().trim();
    }

    public static final String classOrInterface(int n2) {
        return (n2 & 512) != 0 ? "interface" : "class";
    }

    public static final String codeToString(byte[] arrby, ConstantPool constantPool, int n2, int n3, boolean bl) {
        StringBuffer stringBuffer = new StringBuffer(arrby.length * 20);
        ByteSequence byteSequence = new ByteSequence(arrby);
        try {
            int n4 = 0;
            while (n4 < n2) {
                Utility.codeToString(byteSequence, constantPool, bl);
                ++n4;
            }
            int n5 = 0;
            while (byteSequence.available() > 0) {
                if (n3 < 0 || n5 < n3) {
                    String string = Utility.fillup("" + byteSequence.getIndex() + ":", 6, true, ' ');
                    stringBuffer.append(string + Utility.codeToString(byteSequence, constantPool, bl) + '\n');
                }
                ++n5;
            }
        }
        catch (IOException iOException) {
            System.out.println(stringBuffer.toString());
            iOException.printStackTrace();
            throw new ClassFormatError("Byte code error: " + iOException);
        }
        return stringBuffer.toString();
    }

    public static final String codeToString(ByteSequence byteSequence, ConstantPool constantPool, boolean bl) throws IOException {
        int n2;
        int n3;
        int n4;
        short s2 = (short)byteSequence.readUnsignedByte();
        int n5 = 0;
        int n6 = 0;
        StringBuffer stringBuffer = new StringBuffer(Constants.OPCODE_NAMES[s2]);
        if (s2 == 170 || s2 == 171) {
            n4 = byteSequence.getIndex() % 4;
            n6 = n4 == 0 ? 0 : 4 - n4;
            n3 = 0;
            while (n3 < n6) {
                byte by = byteSequence.readByte();
                n2 = by;
                if (by != 0) {
                    System.err.println("Warning: Padding byte != 0 in " + Constants.OPCODE_NAMES[s2] + ":" + n2);
                }
                ++n3;
            }
            n5 = byteSequence.readInt();
        }
        switch (s2) {
            case 170: {
                int n7 = byteSequence.readInt();
                int n8 = byteSequence.readInt();
                int n9 = byteSequence.getIndex() - 12 - n6 - 1;
                stringBuffer.append("\tdefault = " + (n5 += n9) + ", low = " + n7 + ", high = " + n8 + "(");
                int[] arrn = new int[n8 - n7 + 1];
                n4 = 0;
                while (n4 < arrn.length) {
                    arrn[n4] = n9 + byteSequence.readInt();
                    stringBuffer.append(arrn[n4]);
                    if (n4 < arrn.length - 1) {
                        stringBuffer.append(", ");
                    }
                    ++n4;
                }
                stringBuffer.append(")");
                break;
            }
            case 171: {
                int n10 = byteSequence.readInt();
                int n11 = byteSequence.getIndex() - 8 - n6 - 1;
                int[] arrn = new int[n10];
                int[] arrn2 = new int[n10];
                stringBuffer.append("\tdefault = " + (n5 += n11) + ", npairs = " + n10 + " (");
                n3 = 0;
                while (n3 < n10) {
                    arrn[n3] = byteSequence.readInt();
                    arrn2[n3] = n11 + byteSequence.readInt();
                    stringBuffer.append("(" + arrn[n3] + ", " + arrn2[n3] + ")");
                    if (n3 < n10 - 1) {
                        stringBuffer.append(", ");
                    }
                    ++n3;
                }
                stringBuffer.append(")");
                break;
            }
            case 153: 
            case 154: 
            case 155: 
            case 156: 
            case 157: 
            case 158: 
            case 159: 
            case 160: 
            case 161: 
            case 162: 
            case 163: 
            case 164: 
            case 165: 
            case 166: 
            case 167: 
            case 168: 
            case 198: 
            case 199: {
                stringBuffer.append("\t\t#" + (byteSequence.getIndex() - 1 + byteSequence.readShort()));
                break;
            }
            case 200: 
            case 201: {
                stringBuffer.append("\t\t#" + (byteSequence.getIndex() - 1 + byteSequence.readInt()));
                break;
            }
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 54: 
            case 55: 
            case 56: 
            case 57: 
            case 58: 
            case 169: {
                int n12;
                if (wide) {
                    n12 = byteSequence.readUnsignedShort();
                    wide = false;
                } else {
                    n12 = byteSequence.readUnsignedByte();
                }
                stringBuffer.append("\t\t%" + n12);
                break;
            }
            case 196: {
                wide = true;
                stringBuffer.append("\t(wide)");
                break;
            }
            case 188: {
                stringBuffer.append("\t\t<" + Constants.TYPE_NAMES[byteSequence.readByte()] + ">");
                break;
            }
            case 178: 
            case 179: 
            case 180: 
            case 181: {
                int n13 = byteSequence.readUnsignedShort();
                stringBuffer.append("\t\t" + constantPool.constantToString(n13, 9) + (bl ? new StringBuffer().append(" (").append(n13).append(")").toString() : ""));
                break;
            }
            case 187: 
            case 192: {
                stringBuffer.append("\t");
            }
            case 193: {
                int n14 = byteSequence.readUnsignedShort();
                stringBuffer.append("\t<" + constantPool.constantToString(n14, 7) + ">" + (bl ? new StringBuffer().append(" (").append(n14).append(")").toString() : ""));
                break;
            }
            case 182: 
            case 183: 
            case 184: {
                int n15 = byteSequence.readUnsignedShort();
                stringBuffer.append("\t" + constantPool.constantToString(n15, 10) + (bl ? new StringBuffer().append(" (").append(n15).append(")").toString() : ""));
                break;
            }
            case 185: {
                int n16 = byteSequence.readUnsignedShort();
                n3 = byteSequence.readUnsignedByte();
                stringBuffer.append("\t" + constantPool.constantToString(n16, 11) + (bl ? new StringBuffer().append(" (").append(n16).append(")\t").toString() : "") + n3 + "\t" + byteSequence.readUnsignedByte());
                break;
            }
            case 19: 
            case 20: {
                int n17 = byteSequence.readUnsignedShort();
                stringBuffer.append("\t\t" + constantPool.constantToString(n17, constantPool.getConstant(n17).getTag()) + (bl ? new StringBuffer().append(" (").append(n17).append(")").toString() : ""));
                break;
            }
            case 18: {
                int n18 = byteSequence.readUnsignedByte();
                stringBuffer.append("\t\t" + constantPool.constantToString(n18, constantPool.getConstant(n18).getTag()) + (bl ? new StringBuffer().append(" (").append(n18).append(")").toString() : ""));
                break;
            }
            case 189: {
                int n19 = byteSequence.readUnsignedShort();
                stringBuffer.append("\t\t<" + Utility.compactClassName(constantPool.getConstantString(n19, 7), false) + ">" + (bl ? new StringBuffer().append(" (").append(n19).append(")").toString() : ""));
                break;
            }
            case 197: {
                int n20 = byteSequence.readUnsignedShort();
                n2 = byteSequence.readUnsignedByte();
                stringBuffer.append("\t<" + Utility.compactClassName(constantPool.getConstantString(n20, 7), false) + ">\t" + n2 + (bl ? new StringBuffer().append(" (").append(n20).append(")").toString() : ""));
                break;
            }
            case 132: {
                int n21;
                short s3;
                if (wide) {
                    n21 = byteSequence.readUnsignedShort();
                    s3 = byteSequence.readShort();
                    wide = false;
                } else {
                    n21 = byteSequence.readUnsignedByte();
                    s3 = byteSequence.readByte();
                }
                stringBuffer.append("\t\t%" + n21 + "\t" + s3);
                break;
            }
            default: {
                if (Constants.NO_OF_OPERANDS[s2] <= 0) break;
                n2 = 0;
                while (n2 < Constants.TYPE_OF_OPERANDS[s2].length) {
                    stringBuffer.append("\t\t");
                    switch (Constants.TYPE_OF_OPERANDS[s2][n2]) {
                        case 8: {
                            stringBuffer.append(byteSequence.readByte());
                            break;
                        }
                        case 9: {
                            stringBuffer.append(byteSequence.readShort());
                            break;
                        }
                        case 10: {
                            stringBuffer.append(byteSequence.readInt());
                            break;
                        }
                        default: {
                            System.err.println("Unreachable default case reached!");
                            System.exit(-1);
                        }
                    }
                    ++n2;
                }
                break block0;
            }
        }
        return stringBuffer.toString();
    }

    public static final String compactClassName(String string, String string2, boolean bl) {
        int n2 = string2.length();
        string = string.replace('/', '.');
        if (bl && string.startsWith(string2) && string.substring(n2).indexOf(46) == -1) {
            string = string.substring(n2);
        }
        return string;
    }

    public static final String compactClassName(String string, boolean bl) {
        return Utility.compactClassName(string, "java.lang.", bl);
    }

    public static final String methodSignatureToString(String string, String string2, String string3, boolean bl, LocalVariableTable localVariableTable) throws ClassFormatError {
        String string4;
        StringBuffer stringBuffer = new StringBuffer("(");
        int n2 = string3.indexOf("static") >= 0 ? 0 : 1;
        try {
            if (string.charAt(0) != '(') {
                throw new ClassFormatError("Invalid method signature: " + string);
            }
            int n3 = 1;
            while (string.charAt(n3) != ')') {
                stringBuffer.append(Utility.signatureToString(string.substring(n3), bl));
                if (localVariableTable != null) {
                    LocalVariable localVariable = localVariableTable.getLocalVariable(n2);
                    if (localVariable != null) {
                        stringBuffer.append(" " + localVariable.getName());
                    }
                } else {
                    stringBuffer.append(" arg" + n2);
                }
                ++n2;
                stringBuffer.append(", ");
                n3 += consumed_chars;
            }
            string4 = Utility.signatureToString(string.substring(++n3), bl);
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new ClassFormatError("Invalid method signature: " + string);
        }
        if (stringBuffer.length() > 1) {
            stringBuffer.setLength(stringBuffer.length() - 2);
        }
        stringBuffer.append(")");
        return string3 + (string3.length() > 0 ? " " : "") + string4 + " " + string2 + stringBuffer.toString();
    }

    private static final int pow2(int n2) {
        return 1 << n2;
    }

    public static final String replace(String string, String string2, String string3) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            int n2 = string.indexOf(string2);
            if (n2 != -1) {
                int n3 = 0;
                while ((n2 = string.indexOf(string2, n3)) != -1) {
                    stringBuffer.append(string.substring(n3, n2));
                    stringBuffer.append(string3);
                    n3 = n2 + string2.length();
                }
                stringBuffer.append(string.substring(n3));
                string = stringBuffer.toString();
            }
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            System.err.println(stringIndexOutOfBoundsException);
        }
        return string;
    }

    public static final String signatureToString(String string) {
        return Utility.signatureToString(string, true);
    }

    public static final String signatureToString(String string, boolean bl) {
        consumed_chars = 1;
        try {
            switch (string.charAt(0)) {
                case 'B': {
                    return "byte";
                }
                case 'C': {
                    return "char";
                }
                case 'D': {
                    return "double";
                }
                case 'F': {
                    return "float";
                }
                case 'I': {
                    return "int";
                }
                case 'J': {
                    return "long";
                }
                case 'L': {
                    int n2 = string.indexOf(59);
                    if (n2 < 0) {
                        throw new ClassFormatError("Invalid signature: " + string);
                    }
                    consumed_chars = n2 + 1;
                    return Utility.compactClassName(string.substring(1, n2), bl);
                }
                case 'S': {
                    return "short";
                }
                case 'Z': {
                    return "boolean";
                }
                case '[': {
                    StringBuffer stringBuffer = new StringBuffer();
                    int n3 = 0;
                    while (string.charAt(n3) == '[') {
                        stringBuffer.append("[]");
                        ++n3;
                    }
                    int n4 = n3;
                    String string2 = Utility.signatureToString(string.substring(n3), bl);
                    consumed_chars += n4;
                    return string2 + stringBuffer.toString();
                }
                case 'V': {
                    return "void";
                }
            }
            throw new ClassFormatError("Invalid signature: `" + string + "'");
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new ClassFormatError("Invalid signature: " + stringIndexOutOfBoundsException + ":" + string);
        }
    }

    public static final byte typeOfSignature(String string) throws ClassFormatError {
        try {
            switch (string.charAt(0)) {
                case 'B': {
                    return 8;
                }
                case 'C': {
                    return 5;
                }
                case 'D': {
                    return 7;
                }
                case 'F': {
                    return 6;
                }
                case 'I': {
                    return 10;
                }
                case 'J': {
                    return 11;
                }
                case 'L': {
                    return 14;
                }
                case '[': {
                    return 13;
                }
                case 'V': {
                    return 12;
                }
                case 'Z': {
                    return 4;
                }
                case 'S': {
                    return 9;
                }
            }
            throw new ClassFormatError("Invalid method signature: " + string);
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new ClassFormatError("Invalid method signature: " + string);
        }
    }

    public static final String format(int n2, int n3, boolean bl, char c2) {
        return Utility.fillup(Integer.toString(n2), n3, bl, c2);
    }

    public static final String fillup(String string, int n2, boolean bl, char c2) {
        int n3 = n2 - string.length();
        char[] arrc = new char[n3 < 0 ? 0 : n3];
        int n4 = 0;
        while (n4 < arrc.length) {
            arrc[n4] = c2;
            ++n4;
        }
        if (bl) {
            return string + new String(arrc);
        }
        return new String(arrc) + string;
    }

    static {
        wide = false;
        CHAR_MAP = new int[48];
        MAP_CHAR = new int[256];
        int n2 = 0;
        boolean bl = false;
        int n3 = 65;
        while (n3 <= 90) {
            Utility.CHAR_MAP[n2] = n3++;
            Utility.MAP_CHAR[n3] = n2++;
        }
        int n4 = 103;
        while (n4 <= 122) {
            Utility.CHAR_MAP[n2] = n4++;
            Utility.MAP_CHAR[n4] = n2++;
        }
        Utility.CHAR_MAP[n2] = 36;
        Utility.MAP_CHAR[36] = n2++;
        Utility.CHAR_MAP[n2] = 95;
        Utility.MAP_CHAR[95] = n2;
    }
}

