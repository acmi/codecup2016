/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$Attribute;
import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$MethodVisitor;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$Block;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$ClassInfo;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$Local;
import com.google.inject.internal.cglib.core.$LocalVariablesSorter;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$ProcessSwitchCallback;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$TypeUtils;
import java.util.Arrays;

public class $CodeEmitter
extends $LocalVariablesSorter {
    private static final $Signature BOOLEAN_VALUE = $TypeUtils.parseSignature("boolean booleanValue()");
    private static final $Signature CHAR_VALUE = $TypeUtils.parseSignature("char charValue()");
    private static final $Signature LONG_VALUE = $TypeUtils.parseSignature("long longValue()");
    private static final $Signature DOUBLE_VALUE = $TypeUtils.parseSignature("double doubleValue()");
    private static final $Signature FLOAT_VALUE = $TypeUtils.parseSignature("float floatValue()");
    private static final $Signature INT_VALUE = $TypeUtils.parseSignature("int intValue()");
    private static final $Signature CSTRUCT_NULL = $TypeUtils.parseConstructor("");
    private static final $Signature CSTRUCT_STRING = $TypeUtils.parseConstructor("String");
    public static final int ADD = 96;
    public static final int MUL = 104;
    public static final int XOR = 130;
    public static final int USHR = 124;
    public static final int SUB = 100;
    public static final int DIV = 108;
    public static final int NEG = 116;
    public static final int REM = 112;
    public static final int AND = 126;
    public static final int OR = 128;
    public static final int GT = 157;
    public static final int LT = 155;
    public static final int GE = 156;
    public static final int LE = 158;
    public static final int NE = 154;
    public static final int EQ = 153;
    private $ClassEmitter ce;
    private State state;

    $CodeEmitter($ClassEmitter $ClassEmitter, $MethodVisitor $MethodVisitor, int n2, $Signature $Signature, $Type[] arr$Type) {
        super(n2, $Signature.getDescriptor(), $MethodVisitor);
        this.ce = $ClassEmitter;
        this.state = new State($ClassEmitter.getClassInfo(), n2, $Signature, arr$Type);
    }

    public $CodeEmitter($CodeEmitter $CodeEmitter) {
        super($CodeEmitter);
        this.ce = $CodeEmitter.ce;
        this.state = $CodeEmitter.state;
    }

    public boolean isStaticHook() {
        return false;
    }

    public $Signature getSignature() {
        return this.state.sig;
    }

    public $Type getReturnType() {
        return this.state.sig.getReturnType();
    }

    public $MethodInfo getMethodInfo() {
        return this.state;
    }

    public $ClassEmitter getClassEmitter() {
        return this.ce;
    }

    public void end_method() {
        this.visitMaxs(0, 0);
    }

    public $Block begin_block() {
        return new $Block(this);
    }

    public void catch_exception($Block $Block, $Type $Type) {
        if ($Block.getEnd() == null) {
            throw new IllegalStateException("end of block is unset");
        }
        this.mv.visitTryCatchBlock($Block.getStart(), $Block.getEnd(), this.mark(), $Type.getInternalName());
    }

    public void goTo($Label $Label) {
        this.mv.visitJumpInsn(167, $Label);
    }

    public void ifnull($Label $Label) {
        this.mv.visitJumpInsn(198, $Label);
    }

    public void ifnonnull($Label $Label) {
        this.mv.visitJumpInsn(199, $Label);
    }

    public void if_jump(int n2, $Label $Label) {
        this.mv.visitJumpInsn(n2, $Label);
    }

    public void if_icmp(int n2, $Label $Label) {
        this.if_cmp($Type.INT_TYPE, n2, $Label);
    }

    public void if_cmp($Type $Type, int n2, $Label $Label) {
        int n3 = -1;
        int n4 = n2;
        switch (n2) {
            case 156: {
                n4 = 155;
                break;
            }
            case 158: {
                n4 = 157;
            }
        }
        switch ($Type.getSort()) {
            case 7: {
                this.mv.visitInsn(148);
                break;
            }
            case 8: {
                this.mv.visitInsn(152);
                break;
            }
            case 6: {
                this.mv.visitInsn(150);
                break;
            }
            case 9: 
            case 10: {
                switch (n2) {
                    case 153: {
                        this.mv.visitJumpInsn(165, $Label);
                        return;
                    }
                    case 154: {
                        this.mv.visitJumpInsn(166, $Label);
                        return;
                    }
                }
                String string = String.valueOf($Type);
                throw new IllegalArgumentException(new StringBuilder(24 + String.valueOf(string).length()).append("Bad comparison for type ").append(string).toString());
            }
            default: {
                switch (n2) {
                    case 153: {
                        n3 = 159;
                        break;
                    }
                    case 154: {
                        n3 = 160;
                        break;
                    }
                    case 156: {
                        this.swap();
                    }
                    case 155: {
                        n3 = 161;
                        break;
                    }
                    case 158: {
                        this.swap();
                    }
                    case 157: {
                        n3 = 163;
                    }
                }
                this.mv.visitJumpInsn(n3, $Label);
                return;
            }
        }
        this.if_jump(n4, $Label);
    }

    public void pop() {
        this.mv.visitInsn(87);
    }

    public void pop2() {
        this.mv.visitInsn(88);
    }

    public void dup() {
        this.mv.visitInsn(89);
    }

    public void dup2() {
        this.mv.visitInsn(92);
    }

    public void dup_x1() {
        this.mv.visitInsn(90);
    }

    public void dup_x2() {
        this.mv.visitInsn(91);
    }

    public void dup2_x1() {
        this.mv.visitInsn(93);
    }

    public void dup2_x2() {
        this.mv.visitInsn(94);
    }

    public void swap() {
        this.mv.visitInsn(95);
    }

    public void aconst_null() {
        this.mv.visitInsn(1);
    }

    public void swap($Type $Type, $Type $Type2) {
        if ($Type2.getSize() == 1) {
            if ($Type.getSize() == 1) {
                this.swap();
            } else {
                this.dup_x2();
                this.pop();
            }
        } else if ($Type.getSize() == 1) {
            this.dup2_x1();
            this.pop2();
        } else {
            this.dup2_x2();
            this.pop2();
        }
    }

    public void monitorenter() {
        this.mv.visitInsn(194);
    }

    public void monitorexit() {
        this.mv.visitInsn(195);
    }

    public void math(int n2, $Type $Type) {
        this.mv.visitInsn($Type.getOpcode(n2));
    }

    public void array_load($Type $Type) {
        this.mv.visitInsn($Type.getOpcode(46));
    }

    public void array_store($Type $Type) {
        this.mv.visitInsn($Type.getOpcode(79));
    }

    public void cast_numeric($Type $Type, $Type $Type2) {
        if ($Type != $Type2) {
            if ($Type == $Type.DOUBLE_TYPE) {
                if ($Type2 == $Type.FLOAT_TYPE) {
                    this.mv.visitInsn(144);
                } else if ($Type2 == $Type.LONG_TYPE) {
                    this.mv.visitInsn(143);
                } else {
                    this.mv.visitInsn(142);
                    this.cast_numeric($Type.INT_TYPE, $Type2);
                }
            } else if ($Type == $Type.FLOAT_TYPE) {
                if ($Type2 == $Type.DOUBLE_TYPE) {
                    this.mv.visitInsn(141);
                } else if ($Type2 == $Type.LONG_TYPE) {
                    this.mv.visitInsn(140);
                } else {
                    this.mv.visitInsn(139);
                    this.cast_numeric($Type.INT_TYPE, $Type2);
                }
            } else if ($Type == $Type.LONG_TYPE) {
                if ($Type2 == $Type.DOUBLE_TYPE) {
                    this.mv.visitInsn(138);
                } else if ($Type2 == $Type.FLOAT_TYPE) {
                    this.mv.visitInsn(137);
                } else {
                    this.mv.visitInsn(136);
                    this.cast_numeric($Type.INT_TYPE, $Type2);
                }
            } else if ($Type2 == $Type.BYTE_TYPE) {
                this.mv.visitInsn(145);
            } else if ($Type2 == $Type.CHAR_TYPE) {
                this.mv.visitInsn(146);
            } else if ($Type2 == $Type.DOUBLE_TYPE) {
                this.mv.visitInsn(135);
            } else if ($Type2 == $Type.FLOAT_TYPE) {
                this.mv.visitInsn(134);
            } else if ($Type2 == $Type.LONG_TYPE) {
                this.mv.visitInsn(133);
            } else if ($Type2 == $Type.SHORT_TYPE) {
                this.mv.visitInsn(147);
            }
        }
    }

    public void push(int n2) {
        if (n2 < -1) {
            this.mv.visitLdcInsn(new Integer(n2));
        } else if (n2 <= 5) {
            this.mv.visitInsn($TypeUtils.ICONST(n2));
        } else if (n2 <= 127) {
            this.mv.visitIntInsn(16, n2);
        } else if (n2 <= 32767) {
            this.mv.visitIntInsn(17, n2);
        } else {
            this.mv.visitLdcInsn(new Integer(n2));
        }
    }

    public void push(long l2) {
        if (l2 == 0 || l2 == 1) {
            this.mv.visitInsn($TypeUtils.LCONST(l2));
        } else {
            this.mv.visitLdcInsn(new Long(l2));
        }
    }

    public void push(float f2) {
        if (f2 == 0.0f || f2 == 1.0f || f2 == 2.0f) {
            this.mv.visitInsn($TypeUtils.FCONST(f2));
        } else {
            this.mv.visitLdcInsn(new Float(f2));
        }
    }

    public void push(double d2) {
        if (d2 == 0.0 || d2 == 1.0) {
            this.mv.visitInsn($TypeUtils.DCONST(d2));
        } else {
            this.mv.visitLdcInsn(new Double(d2));
        }
    }

    public void push(String string) {
        this.mv.visitLdcInsn(string);
    }

    public void newarray() {
        this.newarray($Constants.TYPE_OBJECT);
    }

    public void newarray($Type $Type) {
        if ($TypeUtils.isPrimitive($Type)) {
            this.mv.visitIntInsn(188, $TypeUtils.NEWARRAY($Type));
        } else {
            this.emit_type(189, $Type);
        }
    }

    public void arraylength() {
        this.mv.visitInsn(190);
    }

    public void load_this() {
        if ($TypeUtils.isStatic(this.state.access)) {
            throw new IllegalStateException("no 'this' pointer within static method");
        }
        this.mv.visitVarInsn(25, 0);
    }

    public void load_args() {
        this.load_args(0, this.state.argumentTypes.length);
    }

    public void load_arg(int n2) {
        this.load_local(this.state.argumentTypes[n2], this.state.localOffset + this.skipArgs(n2));
    }

    public void load_args(int n2, int n3) {
        int n4 = this.state.localOffset + this.skipArgs(n2);
        for (int i2 = 0; i2 < n3; ++i2) {
            $Type $Type = this.state.argumentTypes[n2 + i2];
            this.load_local($Type, n4);
            n4 += $Type.getSize();
        }
    }

    private int skipArgs(int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 += this.state.argumentTypes[i2].getSize();
        }
        return n3;
    }

    private void load_local($Type $Type, int n2) {
        this.mv.visitVarInsn($Type.getOpcode(21), n2);
    }

    private void store_local($Type $Type, int n2) {
        this.mv.visitVarInsn($Type.getOpcode(54), n2);
    }

    public void iinc($Local $Local, int n2) {
        this.mv.visitIincInsn($Local.getIndex(), n2);
    }

    public void store_local($Local $Local) {
        this.store_local($Local.getType(), $Local.getIndex());
    }

    public void load_local($Local $Local) {
        this.load_local($Local.getType(), $Local.getIndex());
    }

    public void return_value() {
        this.mv.visitInsn(this.state.sig.getReturnType().getOpcode(172));
    }

    public void getfield(String string) {
        $ClassEmitter.FieldInfo fieldInfo = this.ce.getFieldInfo(string);
        int n2 = $TypeUtils.isStatic(fieldInfo.access) ? 178 : 180;
        this.emit_field(n2, this.ce.getClassType(), string, fieldInfo.type);
    }

    public void putfield(String string) {
        $ClassEmitter.FieldInfo fieldInfo = this.ce.getFieldInfo(string);
        int n2 = $TypeUtils.isStatic(fieldInfo.access) ? 179 : 181;
        this.emit_field(n2, this.ce.getClassType(), string, fieldInfo.type);
    }

    public void super_getfield(String string, $Type $Type) {
        this.emit_field(180, this.ce.getSuperType(), string, $Type);
    }

    public void super_putfield(String string, $Type $Type) {
        this.emit_field(181, this.ce.getSuperType(), string, $Type);
    }

    public void super_getstatic(String string, $Type $Type) {
        this.emit_field(178, this.ce.getSuperType(), string, $Type);
    }

    public void super_putstatic(String string, $Type $Type) {
        this.emit_field(179, this.ce.getSuperType(), string, $Type);
    }

    public void getfield($Type $Type, String string, $Type $Type2) {
        this.emit_field(180, $Type, string, $Type2);
    }

    public void putfield($Type $Type, String string, $Type $Type2) {
        this.emit_field(181, $Type, string, $Type2);
    }

    public void getstatic($Type $Type, String string, $Type $Type2) {
        this.emit_field(178, $Type, string, $Type2);
    }

    public void putstatic($Type $Type, String string, $Type $Type2) {
        this.emit_field(179, $Type, string, $Type2);
    }

    void emit_field(int n2, $Type $Type, String string, $Type $Type2) {
        this.mv.visitFieldInsn(n2, $Type.getInternalName(), string, $Type2.getDescriptor());
    }

    public void super_invoke() {
        this.super_invoke(this.state.sig);
    }

    public void super_invoke($Signature $Signature) {
        this.emit_invoke(183, this.ce.getSuperType(), $Signature);
    }

    public void invoke_constructor($Type $Type) {
        this.invoke_constructor($Type, CSTRUCT_NULL);
    }

    public void super_invoke_constructor() {
        this.invoke_constructor(this.ce.getSuperType());
    }

    public void invoke_constructor_this() {
        this.invoke_constructor(this.ce.getClassType());
    }

    private void emit_invoke(int n2, $Type $Type, $Signature $Signature) {
        if (!$Signature.getName().equals("<init>") || n2 == 182 || n2 == 184) {
            // empty if block
        }
        this.mv.visitMethodInsn(n2, $Type.getInternalName(), $Signature.getName(), $Signature.getDescriptor(), n2 == 185);
    }

    public void invoke_interface($Type $Type, $Signature $Signature) {
        this.emit_invoke(185, $Type, $Signature);
    }

    public void invoke_virtual($Type $Type, $Signature $Signature) {
        this.emit_invoke(182, $Type, $Signature);
    }

    public void invoke_static($Type $Type, $Signature $Signature) {
        this.emit_invoke(184, $Type, $Signature);
    }

    public void invoke_virtual_this($Signature $Signature) {
        this.invoke_virtual(this.ce.getClassType(), $Signature);
    }

    public void invoke_static_this($Signature $Signature) {
        this.invoke_static(this.ce.getClassType(), $Signature);
    }

    public void invoke_constructor($Type $Type, $Signature $Signature) {
        this.emit_invoke(183, $Type, $Signature);
    }

    public void invoke_constructor_this($Signature $Signature) {
        this.invoke_constructor(this.ce.getClassType(), $Signature);
    }

    public void super_invoke_constructor($Signature $Signature) {
        this.invoke_constructor(this.ce.getSuperType(), $Signature);
    }

    public void new_instance_this() {
        this.new_instance(this.ce.getClassType());
    }

    public void new_instance($Type $Type) {
        this.emit_type(187, $Type);
    }

    private void emit_type(int n2, $Type $Type) {
        String string = $TypeUtils.isArray($Type) ? $Type.getDescriptor() : $Type.getInternalName();
        this.mv.visitTypeInsn(n2, string);
    }

    public void aaload(int n2) {
        this.push(n2);
        this.aaload();
    }

    public void aaload() {
        this.mv.visitInsn(50);
    }

    public void aastore() {
        this.mv.visitInsn(83);
    }

    public void athrow() {
        this.mv.visitInsn(191);
    }

    public $Label make_label() {
        return new $Label();
    }

    public $Local make_local() {
        return this.make_local($Constants.TYPE_OBJECT);
    }

    public $Local make_local($Type $Type) {
        return new $Local(this.newLocal($Type.getSize()), $Type);
    }

    public void checkcast_this() {
        this.checkcast(this.ce.getClassType());
    }

    public void checkcast($Type $Type) {
        if (!$Type.equals($Constants.TYPE_OBJECT)) {
            this.emit_type(192, $Type);
        }
    }

    public void instance_of($Type $Type) {
        this.emit_type(193, $Type);
    }

    public void instance_of_this() {
        this.instance_of(this.ce.getClassType());
    }

    public void process_switch(int[] arrn, $ProcessSwitchCallback processSwitchCallback) {
        float f2 = arrn.length == 0 ? 0.0f : (float)arrn.length / (float)(arrn[arrn.length - 1] - arrn[0] + 1);
        this.process_switch(arrn, processSwitchCallback, f2 >= 0.5f);
    }

    public void process_switch(int[] arrn, $ProcessSwitchCallback $ProcessSwitchCallback, boolean bl) {
        if (!$CodeEmitter.isSorted(arrn)) {
            throw new IllegalArgumentException("keys to switch must be sorted ascending");
        }
        $Label $Label = this.make_label();
        $Label $Label2 = this.make_label();
        try {
            if (arrn.length > 0) {
                int n2 = arrn.length;
                int n3 = arrn[0];
                int n4 = arrn[n2 - 1];
                int n5 = n4 - n3 + 1;
                if (bl) {
                    int n6;
                    Object[] arrobject = new $Label[n5];
                    Arrays.fill(arrobject, $Label);
                    for (n6 = 0; n6 < n2; ++n6) {
                        arrobject[arrn[n6] - n3] = this.make_label();
                    }
                    this.mv.visitTableSwitchInsn(n3, n4, $Label, ($Label[])arrobject);
                    for (n6 = 0; n6 < n5; ++n6) {
                        Object object = arrobject[n6];
                        if (object == $Label) continue;
                        this.mark(($Label)object);
                        $ProcessSwitchCallback.processCase(n6 + n3, $Label2);
                    }
                } else {
                    int n7;
                    $Label[] arr$Label = new $Label[n2];
                    for (n7 = 0; n7 < n2; ++n7) {
                        arr$Label[n7] = this.make_label();
                    }
                    this.mv.visitLookupSwitchInsn($Label, arrn, arr$Label);
                    for (n7 = 0; n7 < n2; ++n7) {
                        this.mark(arr$Label[n7]);
                        $ProcessSwitchCallback.processCase(arrn[n7], $Label2);
                    }
                }
            }
            this.mark($Label);
            $ProcessSwitchCallback.processDefault();
            this.mark($Label2);
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Error error) {
            throw error;
        }
        catch (Exception exception) {
            throw new $CodeGenerationException(exception);
        }
    }

    private static boolean isSorted(int[] arrn) {
        for (int i2 = 1; i2 < arrn.length; ++i2) {
            if (arrn[i2] >= arrn[i2 - 1]) continue;
            return false;
        }
        return true;
    }

    public void mark($Label $Label) {
        this.mv.visitLabel($Label);
    }

    $Label mark() {
        $Label $Label = this.make_label();
        this.mv.visitLabel($Label);
        return $Label;
    }

    public void push(boolean bl) {
        this.push(bl ? 1 : 0);
    }

    public void not() {
        this.push(1);
        this.math(130, $Type.INT_TYPE);
    }

    public void throw_exception($Type $Type, String string) {
        this.new_instance($Type);
        this.dup();
        this.push(string);
        this.invoke_constructor($Type, CSTRUCT_STRING);
        this.athrow();
    }

    public void box($Type $Type) {
        if ($TypeUtils.isPrimitive($Type)) {
            if ($Type == $Type.VOID_TYPE) {
                this.aconst_null();
            } else {
                $Type $Type2 = $TypeUtils.getBoxedType($Type);
                this.new_instance($Type2);
                if ($Type.getSize() == 2) {
                    this.dup_x2();
                    this.dup_x2();
                    this.pop();
                } else {
                    this.dup_x1();
                    this.swap();
                }
                this.invoke_constructor($Type2, new $Signature("<init>", $Type.VOID_TYPE, new $Type[]{$Type}));
            }
        }
    }

    public void unbox($Type $Type) {
        $Type $Type2 = $Constants.TYPE_NUMBER;
        $Signature $Signature = null;
        switch ($Type.getSort()) {
            case 0: {
                return;
            }
            case 2: {
                $Type2 = $Constants.TYPE_CHARACTER;
                $Signature = CHAR_VALUE;
                break;
            }
            case 1: {
                $Type2 = $Constants.TYPE_BOOLEAN;
                $Signature = BOOLEAN_VALUE;
                break;
            }
            case 8: {
                $Signature = DOUBLE_VALUE;
                break;
            }
            case 6: {
                $Signature = FLOAT_VALUE;
                break;
            }
            case 7: {
                $Signature = LONG_VALUE;
                break;
            }
            case 3: 
            case 4: 
            case 5: {
                $Signature = INT_VALUE;
            }
        }
        if ($Signature == null) {
            this.checkcast($Type);
        } else {
            this.checkcast($Type2);
            this.invoke_virtual($Type2, $Signature);
        }
    }

    public void create_arg_array() {
        this.push(this.state.argumentTypes.length);
        this.newarray();
        for (int i2 = 0; i2 < this.state.argumentTypes.length; ++i2) {
            this.dup();
            this.push(i2);
            this.load_arg(i2);
            this.box(this.state.argumentTypes[i2]);
            this.aastore();
        }
    }

    public void zero_or_null($Type $Type) {
        if ($TypeUtils.isPrimitive($Type)) {
            switch ($Type.getSort()) {
                case 8: {
                    this.push(0.0);
                    break;
                }
                case 7: {
                    this.push(0);
                    break;
                }
                case 6: {
                    this.push(0.0f);
                    break;
                }
                case 0: {
                    this.aconst_null();
                }
                default: {
                    this.push(0);
                    break;
                }
            }
        } else {
            this.aconst_null();
        }
    }

    public void unbox_or_zero($Type $Type) {
        if ($TypeUtils.isPrimitive($Type)) {
            if ($Type != $Type.VOID_TYPE) {
                $Label $Label = this.make_label();
                $Label $Label2 = this.make_label();
                this.dup();
                this.ifnonnull($Label);
                this.pop();
                this.zero_or_null($Type);
                this.goTo($Label2);
                this.mark($Label);
                this.unbox($Type);
                this.mark($Label2);
            }
        } else {
            this.checkcast($Type);
        }
    }

    public void visitMaxs(int n2, int n3) {
        if (!$TypeUtils.isAbstract(this.state.access)) {
            this.mv.visitMaxs(0, 0);
        }
    }

    public void invoke($MethodInfo $MethodInfo, $Type $Type) {
        $ClassInfo $ClassInfo = $MethodInfo.getClassInfo();
        $Type $Type2 = $ClassInfo.getType();
        $Signature $Signature = $MethodInfo.getSignature();
        if ($Signature.getName().equals("<init>")) {
            this.invoke_constructor($Type2, $Signature);
        } else if ($TypeUtils.isInterface($ClassInfo.getModifiers())) {
            this.invoke_interface($Type2, $Signature);
        } else if ($TypeUtils.isStatic($MethodInfo.getModifiers())) {
            this.invoke_static($Type2, $Signature);
        } else {
            this.invoke_virtual($Type, $Signature);
        }
    }

    public void invoke($MethodInfo $MethodInfo) {
        this.invoke($MethodInfo, $MethodInfo.getClassInfo().getType());
    }

    private static class State
    extends $MethodInfo {
        $ClassInfo classInfo;
        int access;
        $Signature sig;
        $Type[] argumentTypes;
        int localOffset;
        $Type[] exceptionTypes;

        State($ClassInfo $ClassInfo, int n2, $Signature $Signature, $Type[] arr$Type) {
            this.classInfo = $ClassInfo;
            this.access = n2;
            this.sig = $Signature;
            this.exceptionTypes = arr$Type;
            this.localOffset = $TypeUtils.isStatic(n2) ? 0 : 1;
            this.argumentTypes = $Signature.getArgumentTypes();
        }

        public $ClassInfo getClassInfo() {
            return this.classInfo;
        }

        public int getModifiers() {
            return this.access;
        }

        public $Signature getSignature() {
            return this.sig;
        }

        public $Type[] getExceptionTypes() {
            return this.exceptionTypes;
        }

        public $Attribute getAttribute() {
            return null;
        }
    }

}

