/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Constants;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.VoidType;

public abstract class Type
implements Constants {
    public static final Type Int = new IntType();
    public static final Type Real = new RealType();
    public static final Type Boolean = new BooleanType();
    public static final Type NodeSet = new NodeSetType();
    public static final Type String = new StringType();
    public static final Type ResultTree = new ResultTreeType();
    public static final Type Reference = new ReferenceType();
    public static final Type Void = new VoidType();
    public static final Type Object;
    public static final Type Node;
    public static final Type Root;
    public static final Type Element;
    public static final Type Attribute;
    public static final Type Text;
    public static final Type Comment;
    public static final Type Processing_Instruction;
    static Class class$java$lang$Object;
    static Class class$java$lang$String;

    public static Type newObjectType(String string) {
        if (string == "java.lang.Object") {
            return Object;
        }
        if (string == "java.lang.String") {
            return String;
        }
        return new ObjectType(string);
    }

    public static Type newObjectType(Class class_) {
        Class class_2 = class$java$lang$Object == null ? (Type.class$java$lang$Object = Type.class$("java.lang.Object")) : class$java$lang$Object;
        if (class_ == class_2) {
            return Object;
        }
        Class class_3 = class$java$lang$String == null ? (Type.class$java$lang$String = Type.class$("java.lang.String")) : class$java$lang$String;
        if (class_ == class_3) {
            return String;
        }
        return new ObjectType(class_);
    }

    public abstract String toString();

    public abstract boolean identicalTo(Type var1);

    public boolean isNumber() {
        return false;
    }

    public boolean implementedAsMethod() {
        return false;
    }

    public boolean isSimple() {
        return false;
    }

    public abstract org.apache.bcel.generic.Type toJCType();

    public int distanceTo(Type type) {
        return type == this ? 0 : Integer.MAX_VALUE;
    }

    public abstract String toSignature();

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)type.toString());
        classGenerator.getParser().reportError(2, errorMsg);
    }

    public FlowList translateToDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator, Type type) {
        FlowList flowList = null;
        if (type == Boolean) {
            flowList = this.translateToDesynthesized(classGenerator, methodGenerator, (BooleanType)type);
        } else {
            this.translateTo(classGenerator, methodGenerator, type);
        }
        return flowList;
    }

    public FlowList translateToDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator, BooleanType booleanType) {
        ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)booleanType.toString());
        classGenerator.getParser().reportError(2, errorMsg);
        return null;
    }

    public void translateTo(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)class_.getClass().toString());
        classGenerator.getParser().reportError(2, errorMsg);
    }

    public void translateFrom(ClassGenerator classGenerator, MethodGenerator methodGenerator, Class class_) {
        ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)class_.getClass().toString(), (Object)this.toString());
        classGenerator.getParser().reportError(2, errorMsg);
    }

    public void translateBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)this.toString(), (Object)("[" + this.toString() + "]"));
        classGenerator.getParser().reportError(2, errorMsg);
    }

    public void translateUnBox(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ErrorMsg errorMsg = new ErrorMsg("DATA_CONVERSION_ERR", (Object)("[" + this.toString() + "]"), (Object)this.toString());
        classGenerator.getParser().reportError(2, errorMsg);
    }

    public String getClassName() {
        return "";
    }

    public Instruction ADD() {
        return null;
    }

    public Instruction SUB() {
        return null;
    }

    public Instruction MUL() {
        return null;
    }

    public Instruction DIV() {
        return null;
    }

    public Instruction REM() {
        return null;
    }

    public Instruction NEG() {
        return null;
    }

    public Instruction LOAD(int n2) {
        return null;
    }

    public Instruction STORE(int n2) {
        return null;
    }

    public Instruction POP() {
        return POP;
    }

    public BranchInstruction GT(boolean bl) {
        return null;
    }

    public BranchInstruction GE(boolean bl) {
        return null;
    }

    public BranchInstruction LT(boolean bl) {
        return null;
    }

    public BranchInstruction LE(boolean bl) {
        return null;
    }

    public Instruction CMP(boolean bl) {
        return null;
    }

    public Instruction DUP() {
        return DUP;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static {
        Class class_ = class$java$lang$Object == null ? (Type.class$java$lang$Object = Type.class$("java.lang.Object")) : class$java$lang$Object;
        Object = new ObjectType(class_);
        Node = new NodeType(-1);
        Root = new NodeType(9);
        Element = new NodeType(1);
        Attribute = new NodeType(2);
        Text = new NodeType(3);
        Comment = new NodeType(8);
        Processing_Instruction = new NodeType(7);
    }
}

