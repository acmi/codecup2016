/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$FieldVisitor;
import com.google.inject.internal.asm.$MethodVisitor;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$ClassInfo;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$TypeUtils;
import com.google.inject.internal.cglib.transform.$ClassTransformer;
import java.util.HashMap;
import java.util.Map;

public class $ClassEmitter
extends $ClassTransformer {
    private $ClassInfo classInfo;
    private Map fieldInfo;
    private static int hookCounter;
    private $MethodVisitor rawStaticInit;
    private $CodeEmitter staticInit;
    private $CodeEmitter staticHook;
    private $Signature staticHookSig;

    public $ClassEmitter($ClassVisitor $ClassVisitor) {
        this.setTarget($ClassVisitor);
    }

    public $ClassEmitter() {
        super(327680);
    }

    public void setTarget($ClassVisitor $ClassVisitor) {
        this.cv = $ClassVisitor;
        this.fieldInfo = new HashMap();
        this.staticHook = null;
        this.staticInit = null;
        this.staticHookSig = null;
    }

    private static synchronized int getNextHook() {
        return ++hookCounter;
    }

    public $ClassInfo getClassInfo() {
        return this.classInfo;
    }

    public void begin_class(int n2, final int n3, String string, final $Type $Type, final $Type[] arr$Type, String string2) {
        String string3 = String.valueOf(string.replace('.', '/'));
        final $Type $Type2 = $Type.getType(new StringBuilder(2 + String.valueOf(string3).length()).append("L").append(string3).append(";").toString());
        this.classInfo = new $ClassInfo(){

            public $Type getType() {
                return $Type2;
            }

            public $Type getSuperType() {
                return $Type != null ? $Type : $Constants.TYPE_OBJECT;
            }

            public $Type[] getInterfaces() {
                return arr$Type;
            }

            public int getModifiers() {
                return n3;
            }
        };
        this.cv.visit(n2, n3, this.classInfo.getType().getInternalName(), null, this.classInfo.getSuperType().getInternalName(), $TypeUtils.toInternalNames(arr$Type));
        if (string2 != null) {
            this.cv.visitSource(string2, null);
        }
        this.init();
    }

    public $CodeEmitter getStaticHook() {
        if ($TypeUtils.isInterface(this.getAccess())) {
            throw new IllegalStateException("static hook is invalid for this class");
        }
        if (this.staticHook == null) {
            int n2 = $ClassEmitter.getNextHook();
            this.staticHookSig = new $Signature(new StringBuilder(27).append("CGLIB$STATICHOOK").append(n2).toString(), "()V");
            this.staticHook = this.begin_method(8, this.staticHookSig, null);
            if (this.staticInit != null) {
                this.staticInit.invoke_static_this(this.staticHookSig);
            }
        }
        return this.staticHook;
    }

    protected void init() {
    }

    public int getAccess() {
        return this.classInfo.getModifiers();
    }

    public $Type getClassType() {
        return this.classInfo.getType();
    }

    public $Type getSuperType() {
        return this.classInfo.getSuperType();
    }

    public void end_class() {
        if (this.staticHook != null && this.staticInit == null) {
            this.begin_static();
        }
        if (this.staticInit != null) {
            this.staticHook.return_value();
            this.staticHook.end_method();
            this.rawStaticInit.visitInsn(177);
            this.rawStaticInit.visitMaxs(0, 0);
            this.staticHook = null;
            this.staticInit = null;
            this.staticHookSig = null;
        }
        this.cv.visitEnd();
    }

    public $CodeEmitter begin_method(int n2, $Signature $Signature, $Type[] arr$Type) {
        if (this.classInfo == null) {
            String string = String.valueOf(this);
            throw new IllegalStateException(new StringBuilder(19 + String.valueOf(string).length()).append("classInfo is null! ").append(string).toString());
        }
        $MethodVisitor $MethodVisitor = this.cv.visitMethod(n2, $Signature.getName(), $Signature.getDescriptor(), null, $TypeUtils.toInternalNames(arr$Type));
        if ($Signature.equals($Constants.SIG_STATIC) && !$TypeUtils.isInterface(this.getAccess())) {
            this.rawStaticInit = $MethodVisitor;
            $MethodVisitor $MethodVisitor2 = new $MethodVisitor(327680, $MethodVisitor){

                public void visitMaxs(int n2, int n3) {
                }

                public void visitInsn(int n2) {
                    if (n2 != 177) {
                        super.visitInsn(n2);
                    }
                }
            };
            this.staticInit = new $CodeEmitter(this, $MethodVisitor2, n2, $Signature, arr$Type);
            if (this.staticHook == null) {
                this.getStaticHook();
            } else {
                this.staticInit.invoke_static_this(this.staticHookSig);
            }
            return this.staticInit;
        }
        if ($Signature.equals(this.staticHookSig)) {
            return new $CodeEmitter(this, $MethodVisitor, n2, $Signature, arr$Type){

                public boolean isStaticHook() {
                    return true;
                }
            };
        }
        return new $CodeEmitter(this, $MethodVisitor, n2, $Signature, arr$Type);
    }

    public $CodeEmitter begin_static() {
        return this.begin_method(8, $Constants.SIG_STATIC, null);
    }

    public void declare_field(int n2, String string, $Type $Type, Object object) {
        FieldInfo fieldInfo = (FieldInfo)this.fieldInfo.get(string);
        FieldInfo fieldInfo2 = new FieldInfo(n2, string, $Type, object);
        if (fieldInfo != null) {
            if (!fieldInfo2.equals(fieldInfo)) {
                throw new IllegalArgumentException(new StringBuilder(38 + String.valueOf(string).length()).append("Field \"").append(string).append("\" has been declared differently").toString());
            }
        } else {
            this.fieldInfo.put(string, fieldInfo2);
            this.cv.visitField(n2, string, $Type.getDescriptor(), null, object);
        }
    }

    boolean isFieldDeclared(String string) {
        return this.fieldInfo.get(string) != null;
    }

    FieldInfo getFieldInfo(String string) {
        FieldInfo fieldInfo = (FieldInfo)this.fieldInfo.get(string);
        if (fieldInfo == null) {
            String string2 = String.valueOf(this.getClassType().getClassName());
            throw new IllegalArgumentException(new StringBuilder(26 + String.valueOf(string).length() + String.valueOf(string2).length()).append("Field ").append(string).append(" is not declared in ").append(string2).toString());
        }
        return fieldInfo;
    }

    public void visit(int n2, int n3, String string, String string2, String string3, String[] arrstring) {
        this.begin_class(n2, n3, string.replace('/', '.'), $TypeUtils.fromInternalName(string3), $TypeUtils.fromInternalNames(arrstring), null);
    }

    public void visitEnd() {
        this.end_class();
    }

    public $FieldVisitor visitField(int n2, String string, String string2, String string3, Object object) {
        this.declare_field(n2, string, $Type.getType(string2), object);
        return null;
    }

    public $MethodVisitor visitMethod(int n2, String string, String string2, String string3, String[] arrstring) {
        return this.begin_method(n2, new $Signature(string, string2), $TypeUtils.fromInternalNames(arrstring));
    }

    static class FieldInfo {
        int access;
        String name;
        $Type type;
        Object value;

        public FieldInfo(int n2, String string, $Type $Type, Object object) {
            this.access = n2;
            this.name = string;
            this.type = $Type;
            this.value = object;
        }

        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (!(object instanceof FieldInfo)) {
                return false;
            }
            FieldInfo fieldInfo = (FieldInfo)object;
            if (this.access != fieldInfo.access || !this.name.equals(fieldInfo.name) || !this.type.equals(fieldInfo.type)) {
                return false;
            }
            if (this.value == null ^ fieldInfo.value == null) {
                return false;
            }
            if (this.value != null && !this.value.equals(fieldInfo.value)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.access ^ this.name.hashCode() ^ this.type.hashCode() ^ (this.value == null ? 0 : this.value.hashCode());
        }
    }

}

