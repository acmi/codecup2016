/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.bcel.Constants;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

public class InstructionFinder {
    private static final HashMap map = new HashMap();
    private InstructionList il;
    private String il_string;
    private InstructionHandle[] handles;

    public InstructionFinder(InstructionList instructionList) {
        this.il = instructionList;
        this.reread();
    }

    public final void reread() {
        int n2 = this.il.getLength();
        char[] arrc = new char[n2];
        this.handles = this.il.getInstructionHandles();
        int n3 = 0;
        while (n3 < n2) {
            arrc[n3] = InstructionFinder.makeChar(this.handles[n3].getInstruction().getOpcode());
            ++n3;
        }
        this.il_string = new String(arrc);
    }

    private static final String mapName(String string) {
        String string2 = (String)map.get(string);
        if (string2 != null) {
            return string2;
        }
        short s2 = 0;
        while (s2 < 256) {
            if (string.equals(Constants.OPCODE_NAMES[s2])) {
                return "" + InstructionFinder.makeChar(s2);
            }
            s2 = (short)(s2 + 1);
        }
        throw new RuntimeException("Instruction unknown: " + string);
    }

    private static final String compilePattern(String string) {
        String string2 = string.toLowerCase();
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string2.charAt(n3);
            if (Character.isLetterOrDigit(c2)) {
                StringBuffer stringBuffer2 = new StringBuffer();
                while ((Character.isLetterOrDigit(c2) || c2 == '_') && n3 < n2) {
                    stringBuffer2.append(c2);
                    if (++n3 >= n2) break;
                    c2 = string2.charAt(n3);
                }
                --n3;
                stringBuffer.append(InstructionFinder.mapName(stringBuffer2.toString()));
            } else if (!Character.isWhitespace(c2)) {
                stringBuffer.append(c2);
            }
            ++n3;
        }
        return stringBuffer.toString();
    }

    private InstructionHandle[] getMatch(int n2, int n3) {
        InstructionHandle[] arrinstructionHandle = new InstructionHandle[n3];
        System.arraycopy(this.handles, n2, arrinstructionHandle, 0, n3);
        return arrinstructionHandle;
    }

    public final Iterator search(String string, InstructionHandle instructionHandle, CodeConstraint codeConstraint) {
        String string2 = InstructionFinder.compilePattern(string);
        int n2 = -1;
        int n3 = 0;
        while (n3 < this.handles.length) {
            if (this.handles[n3] == instructionHandle) {
                n2 = n3;
                break;
            }
            ++n3;
        }
        if (n2 == -1) {
            throw new ClassGenException("Instruction handle " + instructionHandle + " not found in instruction list.");
        }
        try {
            RE rE = new RE(string2);
            ArrayList<InstructionHandle[]> arrayList = new ArrayList<InstructionHandle[]>();
            while (n2 < this.il_string.length() && rE.match(this.il_string, n2)) {
                int n4 = rE.getParenStart(0);
                int n5 = rE.getParenEnd(0);
                int n6 = rE.getParenLength(0);
                InstructionHandle[] arrinstructionHandle = this.getMatch(n4, n6);
                if (codeConstraint == null || codeConstraint.checkCode(arrinstructionHandle)) {
                    arrayList.add(arrinstructionHandle);
                }
                n2 = n5;
            }
            return arrayList.iterator();
        }
        catch (RESyntaxException rESyntaxException) {
            System.err.println(rESyntaxException);
            return null;
        }
    }

    public final Iterator search(String string) {
        return this.search(string, this.il.getStart(), null);
    }

    private static final char makeChar(short s2) {
        return (char)(s2 + 32767);
    }

    private static String precompile(short s2, short s3, short s4) {
        StringBuffer stringBuffer = new StringBuffer("(");
        short s5 = s2;
        while (s5 <= s3) {
            stringBuffer.append(InstructionFinder.makeChar(s5));
            stringBuffer.append('|');
            s5 = (short)(s5 + 1);
        }
        stringBuffer.append(InstructionFinder.makeChar(s4));
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    static {
        map.put("arithmeticinstruction", "(irem|lrem|iand|ior|ineg|isub|lneg|fneg|fmul|ldiv|fadd|lxor|frem|idiv|land|ixor|ishr|fsub|lshl|fdiv|iadd|lor|dmul|lsub|ishl|imul|lmul|lushr|dneg|iushr|lshr|ddiv|drem|dadd|ladd|dsub)");
        map.put("invokeinstruction", "(invokevirtual|invokeinterface|invokestatic|invokespecial)");
        map.put("arrayinstruction", "(baload|aastore|saload|caload|fastore|lastore|iaload|castore|iastore|aaload|bastore|sastore|faload|laload|daload|dastore)");
        map.put("gotoinstruction", "(goto|goto_w)");
        map.put("conversioninstruction", "(d2l|l2d|i2s|d2i|l2i|i2b|l2f|d2f|f2i|i2d|i2l|f2d|i2c|f2l|i2f)");
        map.put("localvariableinstruction", "(fstore|iinc|lload|dstore|dload|iload|aload|astore|istore|fload|lstore)");
        map.put("loadinstruction", "(fload|dload|lload|iload|aload)");
        map.put("fieldinstruction", "(getfield|putstatic|getstatic|putfield)");
        map.put("cpinstruction", "(ldc2_w|invokeinterface|multianewarray|putstatic|instanceof|getstatic|checkcast|getfield|invokespecial|ldc_w|invokestatic|invokevirtual|putfield|ldc|new|anewarray)");
        map.put("stackinstruction", "(dup2|swap|dup2_x2|pop|pop2|dup|dup2_x1|dup_x2|dup_x1)");
        map.put("branchinstruction", "(ifle|if_acmpne|if_icmpeq|if_acmpeq|ifnonnull|goto_w|iflt|ifnull|if_icmpne|tableswitch|if_icmple|ifeq|if_icmplt|jsr_w|if_icmpgt|ifgt|jsr|goto|ifne|ifge|lookupswitch|if_icmpge)");
        map.put("returninstruction", "(lreturn|ireturn|freturn|dreturn|areturn|return)");
        map.put("storeinstruction", "(istore|fstore|dstore|astore|lstore)");
        map.put("select", "(tableswitch|lookupswitch)");
        map.put("ifinstruction", "(ifeq|ifgt|if_icmpne|if_icmpeq|ifge|ifnull|ifne|if_icmple|if_icmpge|if_acmpeq|if_icmplt|if_acmpne|ifnonnull|iflt|if_icmpgt|ifle)");
        map.put("jsrinstruction", "(jsr|jsr_w)");
        map.put("variablelengthinstruction", "(tableswitch|jsr|goto|lookupswitch)");
        map.put("unconditionalbranch", "(goto|jsr|jsr_w|athrow|goto_w)");
        map.put("constantpushinstruction", "(dconst|bipush|sipush|fconst|iconst|lconst)");
        map.put("typedinstruction", "(imul|lsub|aload|fload|lor|new|aaload|fcmpg|iand|iaload|lrem|idiv|d2l|isub|dcmpg|dastore|ret|f2d|f2i|drem|iinc|i2c|checkcast|frem|lreturn|astore|lushr|daload|dneg|fastore|istore|lshl|ldiv|lstore|areturn|ishr|ldc_w|invokeinterface|aastore|lxor|ishl|l2d|i2f|return|faload|sipush|iushr|caload|instanceof|invokespecial|putfield|fmul|ireturn|laload|d2f|lneg|ixor|i2l|fdiv|lastore|multianewarray|i2b|getstatic|i2d|putstatic|fcmpl|saload|ladd|irem|dload|jsr_w|dconst|dcmpl|fsub|freturn|ldc|aconst_null|castore|lmul|ldc2_w|dadd|iconst|f2l|ddiv|dstore|land|jsr|anewarray|dmul|bipush|dsub|sastore|d2i|i2s|lshr|iadd|l2i|lload|bastore|fstore|fneg|iload|fadd|baload|fconst|ior|ineg|dreturn|l2f|lconst|getfield|invokevirtual|invokestatic|iastore)");
        map.put("popinstruction", "(fstore|dstore|pop|pop2|astore|putstatic|istore|lstore)");
        map.put("allocationinstruction", "(multianewarray|new|anewarray|newarray)");
        map.put("indexedinstruction", "(lload|lstore|fload|ldc2_w|invokeinterface|multianewarray|astore|dload|putstatic|instanceof|getstatic|checkcast|getfield|invokespecial|dstore|istore|iinc|ldc_w|ret|fstore|invokestatic|iload|putfield|invokevirtual|ldc|new|aload|anewarray)");
        map.put("pushinstruction", "(dup|lload|dup2|bipush|fload|ldc2_w|sipush|lconst|fconst|dload|getstatic|ldc_w|aconst_null|dconst|iload|ldc|iconst|aload)");
        map.put("stackproducer", "(imul|lsub|aload|fload|lor|new|aaload|fcmpg|iand|iaload|lrem|idiv|d2l|isub|dcmpg|dup|f2d|f2i|drem|i2c|checkcast|frem|lushr|daload|dneg|lshl|ldiv|ishr|ldc_w|invokeinterface|lxor|ishl|l2d|i2f|faload|sipush|iushr|caload|instanceof|invokespecial|fmul|laload|d2f|lneg|ixor|i2l|fdiv|getstatic|i2b|swap|i2d|dup2|fcmpl|saload|ladd|irem|dload|jsr_w|dconst|dcmpl|fsub|ldc|arraylength|aconst_null|tableswitch|lmul|ldc2_w|iconst|dadd|f2l|ddiv|land|jsr|anewarray|dmul|bipush|dsub|d2i|newarray|i2s|lshr|iadd|lload|l2i|fneg|iload|fadd|baload|fconst|lookupswitch|ior|ineg|lconst|l2f|getfield|invokevirtual|invokestatic)");
        map.put("stackconsumer", "(imul|lsub|lor|iflt|fcmpg|if_icmpgt|iand|ifeq|if_icmplt|lrem|ifnonnull|idiv|d2l|isub|dcmpg|dastore|if_icmpeq|f2d|f2i|drem|i2c|checkcast|frem|lreturn|astore|lushr|pop2|monitorexit|dneg|fastore|istore|lshl|ldiv|lstore|areturn|if_icmpge|ishr|monitorenter|invokeinterface|aastore|lxor|ishl|l2d|i2f|return|iushr|instanceof|invokespecial|fmul|ireturn|d2f|lneg|ixor|pop|i2l|ifnull|fdiv|lastore|i2b|if_acmpeq|ifge|swap|i2d|putstatic|fcmpl|ladd|irem|dcmpl|fsub|freturn|ifgt|castore|lmul|dadd|f2l|ddiv|dstore|land|if_icmpne|if_acmpne|dmul|dsub|sastore|ifle|d2i|i2s|lshr|iadd|l2i|bastore|fstore|fneg|fadd|ior|ineg|ifne|dreturn|l2f|if_icmple|getfield|invokevirtual|invokestatic|iastore)");
        map.put("exceptionthrower", "(irem|lrem|laload|putstatic|baload|dastore|areturn|getstatic|ldiv|anewarray|iastore|castore|idiv|saload|lastore|fastore|putfield|lreturn|caload|getfield|return|aastore|freturn|newarray|instanceof|multianewarray|athrow|faload|iaload|aaload|dreturn|monitorenter|checkcast|bastore|arraylength|new|invokevirtual|sastore|ldc_w|ireturn|invokespecial|monitorexit|invokeinterface|ldc|invokestatic|daload)");
        map.put("loadclass", "(multianewarray|invokeinterface|instanceof|invokespecial|putfield|checkcast|putstatic|invokevirtual|new|getstatic|invokestatic|getfield|anewarray)");
        map.put("instructiontargeter", "(ifle|if_acmpne|if_icmpeq|if_acmpeq|ifnonnull|goto_w|iflt|ifnull|if_icmpne|tableswitch|if_icmple|ifeq|if_icmplt|jsr_w|if_icmpgt|ifgt|jsr|goto|ifne|ifge|lookupswitch|if_icmpge)");
        map.put("if_icmp", "(if_icmpne|if_icmpeq|if_icmple|if_icmpge|if_icmplt|if_icmpgt)");
        map.put("if_acmp", "(if_acmpeq|if_acmpne)");
        map.put("if", "(ifeq|ifne|iflt|ifge|ifgt|ifle)");
        map.put("iconst", InstructionFinder.precompile(3, 8, 2));
        map.put("lconst", new String(new char[]{'(', InstructionFinder.makeChar(9), '|', InstructionFinder.makeChar(10), ')'}));
        map.put("dconst", new String(new char[]{'(', InstructionFinder.makeChar(14), '|', InstructionFinder.makeChar(15), ')'}));
        map.put("fconst", new String(new char[]{'(', InstructionFinder.makeChar(11), '|', InstructionFinder.makeChar(12), ')'}));
        map.put("iload", InstructionFinder.precompile(26, 29, 21));
        map.put("dload", InstructionFinder.precompile(38, 41, 24));
        map.put("fload", InstructionFinder.precompile(34, 37, 23));
        map.put("aload", InstructionFinder.precompile(42, 45, 25));
        map.put("istore", InstructionFinder.precompile(59, 62, 54));
        map.put("dstore", InstructionFinder.precompile(71, 74, 57));
        map.put("fstore", InstructionFinder.precompile(67, 70, 56));
        map.put("astore", InstructionFinder.precompile(75, 78, 58));
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String string = (String)iterator.next();
            String s2 = (String)map.get(string);
            char c2 = s2.charAt(1);
            if (c2 >= '\u7fff') continue;
            map.put(string, InstructionFinder.compilePattern(s2));
        }
        StringBuffer stringBuffer = new StringBuffer("(");
        short s2 = 0;
        while (s2 < 256) {
            if (Constants.NO_OF_OPERANDS[s2] != -1) {
                stringBuffer.append(InstructionFinder.makeChar(s2));
                if (s2 < 255) {
                    stringBuffer.append('|');
                }
            }
            s2 = (short)(s2 + 1);
        }
        stringBuffer.append(')');
        map.put("instruction", stringBuffer.toString());
    }

    public static interface CodeConstraint {
        public boolean checkCode(InstructionHandle[] var1);
    }

}

