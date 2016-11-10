/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGenOrMethodGen;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.IndexedInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionTargeter;
import org.apache.bcel.generic.LineNumberGen;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MethodObserver;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.Select;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;

public class MethodGen
extends FieldGenOrMethodGen {
    private String class_name;
    private Type[] arg_types;
    private String[] arg_names;
    private int max_locals;
    private int max_stack;
    private InstructionList il;
    private boolean strip_attributes;
    private ArrayList variable_vec = new ArrayList();
    private ArrayList line_number_vec = new ArrayList();
    private ArrayList exception_vec = new ArrayList();
    private ArrayList throws_vec = new ArrayList();
    private ArrayList code_attrs_vec = new ArrayList();
    private ArrayList observers;

    public MethodGen(int n2, Type type, Type[] arrtype, String[] arrstring, String string, String string2, InstructionList instructionList, ConstantPoolGen constantPoolGen) {
        this.setAccessFlags(n2);
        this.setType(type);
        this.setArgumentTypes(arrtype);
        this.setArgumentNames(arrstring);
        this.setName(string);
        this.setClassName(string2);
        this.setInstructionList(instructionList);
        this.setConstantPool(constantPoolGen);
        if ((n2 & 1280) == 0) {
            InstructionHandle instructionHandle = instructionList.getStart();
            InstructionHandle instructionHandle2 = instructionList.getEnd();
            if (!this.isStatic() && string2 != null) {
                this.addLocalVariable("this", new ObjectType(string2), instructionHandle, instructionHandle2);
            }
            if (arrtype != null) {
                int n3;
                int n4 = arrtype.length;
                if (arrstring != null) {
                    if (n4 != arrstring.length) {
                        throw new ClassGenException("Mismatch in argument array lengths: " + n4 + " vs. " + arrstring.length);
                    }
                } else {
                    arrstring = new String[n4];
                    n3 = 0;
                    while (n3 < n4) {
                        arrstring[n3] = "arg" + n3;
                        ++n3;
                    }
                    this.setArgumentNames(arrstring);
                }
                n3 = 0;
                while (n3 < n4) {
                    this.addLocalVariable(arrstring[n3], arrtype[n3], instructionHandle, instructionHandle2);
                    ++n3;
                }
            }
        }
    }

    public MethodGen(Method method, String string, ConstantPoolGen constantPoolGen) {
        this(method.getAccessFlags(), Type.getReturnType(method.getSignature()), Type.getArgumentTypes(method.getSignature()), null, method.getName(), string, (method.getAccessFlags() & 1280) == 0 ? new InstructionList(method.getCode().getCode()) : null, constantPoolGen);
        Attribute[] arrattribute = method.getAttributes();
        int n2 = 0;
        while (n2 < arrattribute.length) {
            String[] arrstring;
            Attribute attribute = arrattribute[n2];
            if (attribute instanceof Code) {
                Object object;
                Object object2;
                InstructionHandle instructionHandle;
                arrstring = (String[])attribute;
                this.setMaxStack(arrstring.getMaxStack());
                this.setMaxLocals(arrstring.getMaxLocals());
                CodeException[] arrcodeException = arrstring.getExceptionTable();
                if (arrcodeException != null) {
                    int n3 = 0;
                    while (n3 < arrcodeException.length) {
                        CodeException codeException = arrcodeException[n3];
                        int n4 = codeException.getCatchType();
                        ObjectType objectType = null;
                        if (n4 > 0) {
                            object2 = method.getConstantPool().getConstantString(n4, 7);
                            objectType = new ObjectType((String)object2);
                        }
                        int n5 = codeException.getEndPC();
                        object = method.getCode().getCode().length;
                        if (object == n5) {
                            instructionHandle = this.il.getEnd();
                        } else {
                            instructionHandle = this.il.findHandle(n5);
                            instructionHandle = instructionHandle.getPrev();
                        }
                        this.addExceptionHandler(this.il.findHandle(codeException.getStartPC()), instructionHandle, this.il.findHandle(codeException.getHandlerPC()), objectType);
                        ++n3;
                    }
                }
                Attribute[] arrattribute2 = arrstring.getAttributes();
                int n6 = 0;
                while (n6 < arrattribute2.length) {
                    attribute = arrattribute2[n6];
                    if (attribute instanceof LineNumberTable) {
                        LineNumber[] arrlineNumber = ((LineNumberTable)attribute).getLineNumberTable();
                        int n7 = 0;
                        while (n7 < arrlineNumber.length) {
                            object2 = arrlineNumber[n7];
                            this.addLineNumber(this.il.findHandle(object2.getStartPC()), object2.getLineNumber());
                            ++n7;
                        }
                    } else if (attribute instanceof LocalVariableTable) {
                        LocalVariable[] arrlocalVariable = ((LocalVariableTable)attribute).getLocalVariableTable();
                        int n8 = 0;
                        while (n8 < arrlocalVariable.length) {
                            object2 = arrlocalVariable[n8];
                            object = this.il.findHandle(object2.getStartPC());
                            instructionHandle = this.il.findHandle(object2.getStartPC() + object2.getLength());
                            if (object == null) {
                                object = this.il.getStart();
                            }
                            if (instructionHandle == null) {
                                instructionHandle = this.il.getEnd();
                            }
                            this.addLocalVariable(object2.getName(), Type.getType(object2.getSignature()), object2.getIndex(), (InstructionHandle)object, instructionHandle);
                            ++n8;
                        }
                    } else {
                        this.addCodeAttribute(attribute);
                    }
                    ++n6;
                }
            } else if (attribute instanceof ExceptionTable) {
                arrstring = ((ExceptionTable)attribute).getExceptionNames();
                int n9 = 0;
                while (n9 < arrstring.length) {
                    this.addException(arrstring[n9]);
                    ++n9;
                }
            } else {
                this.addAttribute(attribute);
            }
            ++n2;
        }
    }

    public LocalVariableGen addLocalVariable(String string, Type type, int n2, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        LocalVariableGen localVariableGen;
        int n3;
        byte by = type.getType();
        int n4 = type.getSize();
        if (n2 + n4 > this.max_locals) {
            this.max_locals = n2 + n4;
        }
        if ((n3 = this.variable_vec.indexOf(localVariableGen = new LocalVariableGen(n2, string, type, instructionHandle, instructionHandle2))) >= 0) {
            this.variable_vec.set(n3, localVariableGen);
        } else {
            this.variable_vec.add(localVariableGen);
        }
        return localVariableGen;
    }

    public LocalVariableGen addLocalVariable(String string, Type type, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        return this.addLocalVariable(string, type, this.max_locals, instructionHandle, instructionHandle2);
    }

    public void removeLocalVariable(LocalVariableGen localVariableGen) {
        this.variable_vec.remove(localVariableGen);
    }

    public void removeLocalVariables() {
        this.variable_vec.clear();
    }

    private static final void sort(LocalVariableGen[] arrlocalVariableGen, int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        int n6 = arrlocalVariableGen[(n2 + n3) / 2].getIndex();
        do {
            if (arrlocalVariableGen[n4].getIndex() < n6) {
                ++n4;
                continue;
            }
            while (n6 < arrlocalVariableGen[n5].getIndex()) {
                --n5;
            }
            if (n4 <= n5) {
                LocalVariableGen localVariableGen = arrlocalVariableGen[n4];
                arrlocalVariableGen[n4] = arrlocalVariableGen[n5];
                arrlocalVariableGen[n5] = localVariableGen;
                ++n4;
                --n5;
            }
            if (n4 > n5) break;
        } while (true);
        if (n2 < n5) {
            MethodGen.sort(arrlocalVariableGen, n2, n5);
        }
        if (n4 < n3) {
            MethodGen.sort(arrlocalVariableGen, n4, n3);
        }
    }

    public LocalVariableGen[] getLocalVariables() {
        int n2 = this.variable_vec.size();
        LocalVariableGen[] arrlocalVariableGen = new LocalVariableGen[n2];
        this.variable_vec.toArray(arrlocalVariableGen);
        int n3 = 0;
        while (n3 < n2) {
            if (arrlocalVariableGen[n3].getStart() == null) {
                arrlocalVariableGen[n3].setStart(this.il.getStart());
            }
            if (arrlocalVariableGen[n3].getEnd() == null) {
                arrlocalVariableGen[n3].setEnd(this.il.getEnd());
            }
            ++n3;
        }
        if (n2 > 1) {
            MethodGen.sort(arrlocalVariableGen, 0, n2 - 1);
        }
        return arrlocalVariableGen;
    }

    public LocalVariableTable getLocalVariableTable(ConstantPoolGen constantPoolGen) {
        LocalVariableGen[] arrlocalVariableGen = this.getLocalVariables();
        int n2 = arrlocalVariableGen.length;
        LocalVariable[] arrlocalVariable = new LocalVariable[n2];
        int n3 = 0;
        while (n3 < n2) {
            arrlocalVariable[n3] = arrlocalVariableGen[n3].getLocalVariable(constantPoolGen);
            ++n3;
        }
        return new LocalVariableTable(constantPoolGen.addUtf8("LocalVariableTable"), 2 + arrlocalVariable.length * 10, arrlocalVariable, constantPoolGen.getConstantPool());
    }

    public LineNumberGen addLineNumber(InstructionHandle instructionHandle, int n2) {
        LineNumberGen lineNumberGen = new LineNumberGen(instructionHandle, n2);
        this.line_number_vec.add(lineNumberGen);
        return lineNumberGen;
    }

    public void removeLineNumber(LineNumberGen lineNumberGen) {
        this.line_number_vec.remove(lineNumberGen);
    }

    public void removeLineNumbers() {
        this.line_number_vec.clear();
    }

    public LineNumberGen[] getLineNumbers() {
        LineNumberGen[] arrlineNumberGen = new LineNumberGen[this.line_number_vec.size()];
        this.line_number_vec.toArray(arrlineNumberGen);
        return arrlineNumberGen;
    }

    public LineNumberTable getLineNumberTable(ConstantPoolGen constantPoolGen) {
        int n2 = this.line_number_vec.size();
        LineNumber[] arrlineNumber = new LineNumber[n2];
        try {
            int n3 = 0;
            while (n3 < n2) {
                arrlineNumber[n3] = ((LineNumberGen)this.line_number_vec.get(n3)).getLineNumber();
                ++n3;
            }
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            // empty catch block
        }
        return new LineNumberTable(constantPoolGen.addUtf8("LineNumberTable"), 2 + arrlineNumber.length * 4, arrlineNumber, constantPoolGen.getConstantPool());
    }

    public CodeExceptionGen addExceptionHandler(InstructionHandle instructionHandle, InstructionHandle instructionHandle2, InstructionHandle instructionHandle3, ObjectType objectType) {
        if (instructionHandle == null || instructionHandle2 == null || instructionHandle3 == null) {
            throw new ClassGenException("Exception handler target is null instruction");
        }
        CodeExceptionGen codeExceptionGen = new CodeExceptionGen(instructionHandle, instructionHandle2, instructionHandle3, objectType);
        this.exception_vec.add(codeExceptionGen);
        return codeExceptionGen;
    }

    public void removeExceptionHandler(CodeExceptionGen codeExceptionGen) {
        this.exception_vec.remove(codeExceptionGen);
    }

    public void removeExceptionHandlers() {
        this.exception_vec.clear();
    }

    public CodeExceptionGen[] getExceptionHandlers() {
        CodeExceptionGen[] arrcodeExceptionGen = new CodeExceptionGen[this.exception_vec.size()];
        this.exception_vec.toArray(arrcodeExceptionGen);
        return arrcodeExceptionGen;
    }

    private CodeException[] getCodeExceptions() {
        int n2 = this.exception_vec.size();
        CodeException[] arrcodeException = new CodeException[n2];
        try {
            int n3 = 0;
            while (n3 < n2) {
                CodeExceptionGen codeExceptionGen = (CodeExceptionGen)this.exception_vec.get(n3);
                arrcodeException[n3] = codeExceptionGen.getCodeException(this.cp);
                ++n3;
            }
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            // empty catch block
        }
        return arrcodeException;
    }

    public void addException(String string) {
        this.throws_vec.add(string);
    }

    public void removeException(String string) {
        this.throws_vec.remove(string);
    }

    public void removeExceptions() {
        this.throws_vec.clear();
    }

    public String[] getExceptions() {
        String[] arrstring = new String[this.throws_vec.size()];
        this.throws_vec.toArray(arrstring);
        return arrstring;
    }

    private ExceptionTable getExceptionTable(ConstantPoolGen constantPoolGen) {
        int n2 = this.throws_vec.size();
        int[] arrn = new int[n2];
        try {
            int n3 = 0;
            while (n3 < n2) {
                arrn[n3] = constantPoolGen.addClass((String)this.throws_vec.get(n3));
                ++n3;
            }
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            // empty catch block
        }
        return new ExceptionTable(constantPoolGen.addUtf8("Exceptions"), 2 + 2 * n2, arrn, constantPoolGen.getConstantPool());
    }

    public void addCodeAttribute(Attribute attribute) {
        this.code_attrs_vec.add(attribute);
    }

    public void removeCodeAttribute(Attribute attribute) {
        this.code_attrs_vec.remove(attribute);
    }

    public void removeCodeAttributes() {
        this.code_attrs_vec.clear();
    }

    public Attribute[] getCodeAttributes() {
        Attribute[] arrattribute = new Attribute[this.code_attrs_vec.size()];
        this.code_attrs_vec.toArray(arrattribute);
        return arrattribute;
    }

    public Method getMethod() {
        String string = this.getSignature();
        int n2 = this.cp.addUtf8(this.name);
        int n3 = this.cp.addUtf8(string);
        byte[] arrby = null;
        if (this.il != null) {
            arrby = this.il.getByteCode();
        }
        LineNumberTable lineNumberTable = null;
        LocalVariableTable localVariableTable = null;
        if (this.variable_vec.size() > 0 && !this.strip_attributes) {
            localVariableTable = this.getLocalVariableTable(this.cp);
            this.addCodeAttribute(localVariableTable);
        }
        if (this.line_number_vec.size() > 0 && !this.strip_attributes) {
            lineNumberTable = this.getLineNumberTable(this.cp);
            this.addCodeAttribute(lineNumberTable);
        }
        Attribute[] arrattribute = this.getCodeAttributes();
        int n4 = 0;
        int n5 = 0;
        while (n5 < arrattribute.length) {
            n4 += arrattribute[n5].getLength() + 6;
            ++n5;
        }
        CodeException[] arrcodeException = this.getCodeExceptions();
        int n6 = arrcodeException.length * 8;
        Code code = null;
        if (this.il != null && !this.isAbstract()) {
            code = new Code(this.cp.addUtf8("Code"), 8 + arrby.length + 2 + n6 + 2 + n4, this.max_stack, this.max_locals, arrby, arrcodeException, arrattribute, this.cp.getConstantPool());
            this.addAttribute(code);
        }
        ExceptionTable exceptionTable = null;
        if (this.throws_vec.size() > 0) {
            exceptionTable = this.getExceptionTable(this.cp);
            this.addAttribute(exceptionTable);
        }
        Method method = new Method(this.access_flags, n2, n3, this.getAttributes(), this.cp.getConstantPool());
        if (localVariableTable != null) {
            this.removeCodeAttribute(localVariableTable);
        }
        if (lineNumberTable != null) {
            this.removeCodeAttribute(lineNumberTable);
        }
        if (code != null) {
            this.removeAttribute(code);
        }
        if (exceptionTable != null) {
            this.removeAttribute(exceptionTable);
        }
        return method;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void removeNOPs() {
        if (this.il == null) return;
        var2_1 = this.il.getStart();
        while (var2_1 != null) {
            block6 : {
                var1_2 = var2_1.next;
                if (var1_2 != null && var2_1.getInstruction() instanceof NOP) {
                    try {
                        this.il.delete(var2_1);
                        break block6;
                    }
                    catch (TargetLostException var3_3) {
                        var4_4 = var3_3.getTargets();
                        var5_5 = 0;
                        ** while (var5_5 < var4_4.length)
                    }
lbl-1000: // 1 sources:
                    {
                        var6_6 = var4_4[var5_5].getTargeters();
                        var7_7 = 0;
                        while (var7_7 < var6_6.length) {
                            var6_6[var7_7].updateTarget(var4_4[var5_5], var1_2);
                            ++var7_7;
                        }
                        ++var5_5;
                        continue;
                    }
                }
            }
            var2_1 = var1_2;
        }
    }

    public void setMaxLocals(int n2) {
        this.max_locals = n2;
    }

    public int getMaxLocals() {
        return this.max_locals;
    }

    public void setMaxStack(int n2) {
        this.max_stack = n2;
    }

    public int getMaxStack() {
        return this.max_stack;
    }

    public String getClassName() {
        return this.class_name;
    }

    public void setClassName(String string) {
        this.class_name = string;
    }

    public void setReturnType(Type type) {
        this.setType(type);
    }

    public Type getReturnType() {
        return this.getType();
    }

    public void setArgumentTypes(Type[] arrtype) {
        this.arg_types = arrtype;
    }

    public Type[] getArgumentTypes() {
        return (Type[])this.arg_types.clone();
    }

    public void setArgumentType(int n2, Type type) {
        this.arg_types[n2] = type;
    }

    public Type getArgumentType(int n2) {
        return this.arg_types[n2];
    }

    public void setArgumentNames(String[] arrstring) {
        this.arg_names = arrstring;
    }

    public String[] getArgumentNames() {
        return (String[])this.arg_names.clone();
    }

    public void setArgumentName(int n2, String string) {
        this.arg_names[n2] = string;
    }

    public String getArgumentName(int n2) {
        return this.arg_names[n2];
    }

    public InstructionList getInstructionList() {
        return this.il;
    }

    public void setInstructionList(InstructionList instructionList) {
        this.il = instructionList;
    }

    public String getSignature() {
        return Type.getMethodSignature(this.type, this.arg_types);
    }

    public void setMaxStack() {
        this.max_stack = this.il != null ? MethodGen.getMaxStack(this.cp, this.il, this.getExceptionHandlers()) : 0;
    }

    public void setMaxLocals() {
        if (this.il != null) {
            int n2;
            int n3 = n2 = this.isStatic() ? 0 : 1;
            if (this.arg_types != null) {
                int n4 = 0;
                while (n4 < this.arg_types.length) {
                    n2 += this.arg_types[n4].getSize();
                    ++n4;
                }
            }
            InstructionHandle instructionHandle = this.il.getStart();
            while (instructionHandle != null) {
                int n5;
                Instruction instruction = instructionHandle.getInstruction();
                if ((instruction instanceof LocalVariableInstruction || instruction instanceof RET || instruction instanceof IINC) && (n5 = ((IndexedInstruction)((Object)instruction)).getIndex() + ((TypedInstruction)((Object)instruction)).getType(this.cp).getSize()) > n2) {
                    n2 = n5;
                }
                instructionHandle = instructionHandle.getNext();
            }
            this.max_locals = n2;
        } else {
            this.max_locals = 0;
        }
    }

    public void stripAttributes(boolean bl) {
        this.strip_attributes = bl;
    }

    public static int getMaxStack(ConstantPoolGen constantPoolGen, InstructionList instructionList, CodeExceptionGen[] arrcodeExceptionGen) {
        BranchStack branchStack = new BranchStack();
        int n2 = 0;
        while (n2 < arrcodeExceptionGen.length) {
            InstructionHandle instructionHandle = arrcodeExceptionGen[n2].getHandlerPC();
            if (instructionHandle != null) {
                branchStack.push(instructionHandle, 1);
            }
            ++n2;
        }
        int n3 = 0;
        int n4 = 0;
        InstructionHandle instructionHandle = instructionList.getStart();
        while (instructionHandle != null) {
            Object object;
            Instruction instruction = instructionHandle.getInstruction();
            short s2 = instruction.getOpcode();
            int n5 = instruction.produceStack(constantPoolGen) - instruction.consumeStack(constantPoolGen);
            if ((n3 += n5) > n4) {
                n4 = n3;
            }
            if (instruction instanceof BranchInstruction) {
                object = (BranchInstruction)instruction;
                if (instruction instanceof Select) {
                    Select select = (Select)object;
                    InstructionHandle[] arrinstructionHandle = select.getTargets();
                    int n6 = 0;
                    while (n6 < arrinstructionHandle.length) {
                        branchStack.push(arrinstructionHandle[n6], n3);
                        ++n6;
                    }
                    instructionHandle = null;
                } else if (!(object instanceof IfInstruction)) {
                    if (s2 == 168 || s2 == 201) {
                        branchStack.push(instructionHandle.getNext(), n3 - 1);
                    }
                    instructionHandle = null;
                }
                branchStack.push(object.getTarget(), n3);
            } else if (s2 == 191 || s2 == 169 || s2 >= 172 && s2 <= 177) {
                instructionHandle = null;
            }
            if (instructionHandle != null) {
                instructionHandle = instructionHandle.getNext();
            }
            if (instructionHandle != null || (object = branchStack.pop()) == null) continue;
            instructionHandle = object.target;
            n3 = object.stackDepth;
        }
        return n4;
    }

    public void addObserver(MethodObserver methodObserver) {
        if (this.observers == null) {
            this.observers = new ArrayList();
        }
        this.observers.add(methodObserver);
    }

    public void removeObserver(MethodObserver methodObserver) {
        if (this.observers != null) {
            this.observers.remove(methodObserver);
        }
    }

    public void update() {
        if (this.observers != null) {
            Iterator iterator = this.observers.iterator();
            while (iterator.hasNext()) {
                ((MethodObserver)iterator.next()).notify(this);
            }
        }
    }

    public final String toString() {
        String string = Utility.accessToString(this.access_flags);
        String string2 = Type.getMethodSignature(this.type, this.arg_types);
        string2 = Utility.methodSignatureToString(string2, this.name, string, true, this.getLocalVariableTable(this.cp));
        StringBuffer stringBuffer = new StringBuffer(string2);
        if (this.throws_vec.size() > 0) {
            Iterator iterator = this.throws_vec.iterator();
            while (iterator.hasNext()) {
                stringBuffer.append("\n\t\tthrows " + iterator.next());
            }
        }
        return stringBuffer.toString();
    }

    public MethodGen copy(String string, ConstantPoolGen constantPoolGen) {
        Method method = ((MethodGen)this.clone()).getMethod();
        MethodGen methodGen = new MethodGen(method, string, this.cp);
        if (this.cp != constantPoolGen) {
            methodGen.setConstantPool(constantPoolGen);
            methodGen.getInstructionList().replaceConstantPool(this.cp, constantPoolGen);
        }
        return methodGen;
    }

    static final class BranchStack {
        Stack branchTargets = new Stack();
        Hashtable visitedTargets = new Hashtable();

        BranchStack() {
        }

        public void push(InstructionHandle instructionHandle, int n2) {
            if (this.visited(instructionHandle)) {
                return;
            }
            this.branchTargets.push(this.visit(instructionHandle, n2));
        }

        public BranchTarget pop() {
            if (!this.branchTargets.empty()) {
                BranchTarget branchTarget = (BranchTarget)this.branchTargets.pop();
                return branchTarget;
            }
            return null;
        }

        private final BranchTarget visit(InstructionHandle instructionHandle, int n2) {
            BranchTarget branchTarget = new BranchTarget(instructionHandle, n2);
            this.visitedTargets.put(instructionHandle, branchTarget);
            return branchTarget;
        }

        private final boolean visited(InstructionHandle instructionHandle) {
            return this.visitedTargets.get(instructionHandle) != null;
        }
    }

    static final class BranchTarget {
        InstructionHandle target;
        int stackDepth;

        BranchTarget(InstructionHandle instructionHandle, int n2) {
            this.target = instructionHandle;
            this.stackDepth = n2;
        }
    }

}

