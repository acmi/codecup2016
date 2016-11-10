/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.CastExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.ObjectFactory;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.MultiHashtable;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

class FunctionCall
extends Expression {
    private QName _fname;
    private final Vector _arguments;
    private static final Vector EMPTY_ARG_LIST = new Vector(0);
    protected static final String EXT_XSLTC = "http://xml.apache.org/xalan/xsltc";
    protected static final String JAVA_EXT_XSLTC = "http://xml.apache.org/xalan/xsltc/java";
    protected static final String EXT_XALAN = "http://xml.apache.org/xalan";
    protected static final String JAVA_EXT_XALAN = "http://xml.apache.org/xalan/java";
    protected static final String JAVA_EXT_XALAN_OLD = "http://xml.apache.org/xslt/java";
    protected static final String EXSLT_COMMON = "http://exslt.org/common";
    protected static final String EXSLT_MATH = "http://exslt.org/math";
    protected static final String EXSLT_SETS = "http://exslt.org/sets";
    protected static final String EXSLT_DATETIME = "http://exslt.org/dates-and-times";
    protected static final String EXSLT_STRINGS = "http://exslt.org/strings";
    protected static final int NAMESPACE_FORMAT_JAVA = 0;
    protected static final int NAMESPACE_FORMAT_CLASS = 1;
    protected static final int NAMESPACE_FORMAT_PACKAGE = 2;
    protected static final int NAMESPACE_FORMAT_CLASS_OR_PACKAGE = 3;
    private int _namespace_format = 0;
    Expression _thisArgument = null;
    private String _className;
    private Class _clazz;
    private Method _chosenMethod;
    private Constructor _chosenConstructor;
    private MethodType _chosenMethodType;
    private boolean unresolvedExternal;
    private boolean _isExtConstructor = false;
    private boolean _isStatic = false;
    private static final MultiHashtable _internal2Java = new MultiHashtable();
    private static final Hashtable _java2Internal = new Hashtable();
    private static final Hashtable _extensionNamespaceTable = new Hashtable();
    private static final Hashtable _extensionFunctionTable = new Hashtable();
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Object;
    static Class class$java$lang$Double;
    static Class class$java$lang$String;

    public FunctionCall(QName qName, Vector vector) {
        this._fname = qName;
        this._arguments = vector;
        this._type = null;
    }

    public FunctionCall(QName qName) {
        this(qName, EMPTY_ARG_LIST);
    }

    public String getName() {
        return this._fname.toString();
    }

    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._arguments != null) {
            int n2 = this._arguments.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Expression expression = (Expression)this._arguments.elementAt(i2);
                expression.setParser(parser);
                expression.setParent(this);
            }
        }
    }

    public String getClassNameFromUri(String string) {
        String string2 = (String)_extensionNamespaceTable.get(string);
        if (string2 != null) {
            return string2;
        }
        if (string.startsWith("http://xml.apache.org/xalan/xsltc/java")) {
            int n2 = "http://xml.apache.org/xalan/xsltc/java".length() + 1;
            return string.length() > n2 ? string.substring(n2) : "";
        }
        if (string.startsWith("http://xml.apache.org/xalan/java")) {
            int n3 = "http://xml.apache.org/xalan/java".length() + 1;
            return string.length() > n3 ? string.substring(n3) : "";
        }
        if (string.startsWith("http://xml.apache.org/xslt/java")) {
            int n4 = "http://xml.apache.org/xslt/java".length() + 1;
            return string.length() > n4 ? string.substring(n4) : "";
        }
        int n5 = string.lastIndexOf(47);
        return n5 > 0 ? string.substring(n5 + 1) : string;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._type != null) {
            return this._type;
        }
        String string = this._fname.getNamespace();
        String string2 = this._fname.getLocalPart();
        if (this.isExtension()) {
            this._fname = new QName(null, null, string2);
            return this.typeCheckStandard(symbolTable);
        }
        if (this.isStandard()) {
            return this.typeCheckStandard(symbolTable);
        }
        try {
            this._className = this.getClassNameFromUri(string);
            int n2 = string2.lastIndexOf(46);
            if (n2 > 0) {
                this._isStatic = true;
                if (this._className != null && this._className.length() > 0) {
                    this._namespace_format = 2;
                    this._className = this._className + "." + string2.substring(0, n2);
                } else {
                    this._namespace_format = 0;
                    this._className = string2.substring(0, n2);
                }
                this._fname = new QName(string, null, string2.substring(n2 + 1));
            } else {
                String string3;
                if (this._className != null && this._className.length() > 0) {
                    try {
                        this._clazz = ObjectFactory.findProviderClass(this._className, ObjectFactory.findClassLoader(), true);
                        this._namespace_format = 1;
                    }
                    catch (ClassNotFoundException classNotFoundException) {
                        this._namespace_format = 2;
                    }
                } else {
                    this._namespace_format = 0;
                }
                if (string2.indexOf(45) > 0) {
                    string2 = FunctionCall.replaceDash(string2);
                }
                if ((string3 = (String)_extensionFunctionTable.get(string + ":" + string2)) != null) {
                    this._fname = new QName(null, null, string3);
                    return this.typeCheckStandard(symbolTable);
                }
                this._fname = new QName(string, null, string2);
            }
            return this.typeCheckExternal(symbolTable);
        }
        catch (TypeCheckError typeCheckError) {
            ErrorMsg errorMsg = typeCheckError.getErrorMsg();
            if (errorMsg == null) {
                String string4 = this._fname.getLocalPart();
                errorMsg = new ErrorMsg("METHOD_NOT_FOUND_ERR", string4);
            }
            this.getParser().reportError(3, errorMsg);
            this._type = Type.Void;
            return this._type;
        }
    }

    public Type typeCheckStandard(SymbolTable symbolTable) throws TypeCheckError {
        this._fname.clearNamespace();
        int n2 = this._arguments.size();
        Vector vector = this.typeCheckArgs(symbolTable);
        MethodType methodType = new MethodType(Type.Void, vector);
        MethodType methodType2 = this.lookupPrimop(symbolTable, this._fname.getLocalPart(), methodType);
        if (methodType2 != null) {
            for (int i2 = 0; i2 < n2; ++i2) {
                Expression expression;
                Type type = (Type)methodType2.argsType().elementAt(i2);
                if (type.identicalTo((expression = (Expression)this._arguments.elementAt(i2)).getType())) continue;
                try {
                    this._arguments.setElementAt(new CastExpr(expression, type), i2);
                    continue;
                }
                catch (TypeCheckError typeCheckError) {
                    throw new TypeCheckError(this);
                }
            }
            this._chosenMethodType = methodType2;
            this._type = methodType2.resultType();
            return this._type;
        }
        throw new TypeCheckError(this);
    }

    public Type typeCheckConstructor(SymbolTable symbolTable) throws TypeCheckError {
        Vector vector = this.findConstructors();
        if (vector == null) {
            throw new TypeCheckError("CONSTRUCTOR_NOT_FOUND", this._className);
        }
        int n2 = vector.size();
        int n3 = this._arguments.size();
        Vector vector2 = this.typeCheckArgs(symbolTable);
        int n4 = Integer.MAX_VALUE;
        this._type = null;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5;
            Constructor constructor = (Constructor)vector.elementAt(i2);
            Class<?>[] arrclass = constructor.getParameterTypes();
            Class class_ = null;
            int n6 = 0;
            for (n5 = 0; n5 < n3; ++n5) {
                class_ = arrclass[n5];
                Type type = (Type)vector2.elementAt(n5);
                Object object = _internal2Java.maps(type, class_);
                if (object != null) {
                    n6 += ((JavaType)object).distance;
                    continue;
                }
                if (type instanceof ObjectType) {
                    ObjectType objectType = (ObjectType)type;
                    if (objectType.getJavaClass() == class_) continue;
                    if (class_.isAssignableFrom(objectType.getJavaClass())) {
                        ++n6;
                        continue;
                    }
                    n6 = Integer.MAX_VALUE;
                    break;
                }
                n6 = Integer.MAX_VALUE;
                break;
            }
            if (n5 != n3 || n6 >= n4) continue;
            this._chosenConstructor = constructor;
            this._isExtConstructor = true;
            n4 = n6;
            this._type = this._clazz != null ? Type.newObjectType(this._clazz) : Type.newObjectType(this._className);
        }
        if (this._type != null) {
            return this._type;
        }
        throw new TypeCheckError("ARGUMENT_CONVERSION_ERR", this.getMethodSignature(vector2));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Type typeCheckExternal(SymbolTable symbolTable) throws TypeCheckError {
        Class class_;
        Vector vector;
        int n2 = this._arguments.size();
        String string = this._fname.getLocalPart();
        if (this._fname.getLocalPart().equals("new")) {
            return this.typeCheckConstructor(symbolTable);
        }
        boolean bl = false;
        if (n2 == 0) {
            this._isStatic = true;
        }
        if (!this._isStatic) {
            if (this._namespace_format == 0 || this._namespace_format == 2) {
                bl = true;
            }
            class_ = (Expression)this._arguments.elementAt(0);
            Type type = class_.typeCheck(symbolTable);
            if (this._namespace_format == 1 && type instanceof ObjectType && this._clazz != null && this._clazz.isAssignableFrom(((ObjectType)type).getJavaClass())) {
                bl = true;
            }
            if (bl) {
                this._thisArgument = (Expression)this._arguments.elementAt(0);
                this._arguments.remove(0);
                --n2;
                if (!(type instanceof ObjectType)) throw new TypeCheckError("NO_JAVA_FUNCT_THIS_REF", string);
                this._className = ((ObjectType)type).getJavaClassName();
            }
        } else if (this._className.length() == 0) {
            Parser parser = this.getParser();
            if (parser != null) {
                this.reportWarning(this, parser, "FUNCTION_RESOLVE_ERR", this._fname.toString());
            }
            this.unresolvedExternal = true;
            this._type = Type.Int;
            return this._type;
        }
        if ((vector = this.findMethods()) == null) {
            throw new TypeCheckError("METHOD_NOT_FOUND_ERR", this._className + "." + string);
        }
        class_ = null;
        int n3 = vector.size();
        Vector vector2 = this.typeCheckArgs(symbolTable);
        int n4 = Integer.MAX_VALUE;
        this._type = null;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n5;
            Method method = (Method)vector.elementAt(i2);
            Class<?>[] arrclass = method.getParameterTypes();
            int n6 = 0;
            for (n5 = 0; n5 < n2; ++n5) {
                class_ = arrclass[n5];
                Type type = (Type)vector2.elementAt(n5);
                Object object = _internal2Java.maps(type, class_);
                if (object != null) {
                    n6 += ((JavaType)object).distance;
                    continue;
                }
                if (type instanceof ReferenceType) {
                    ++n6;
                    continue;
                }
                if (type instanceof ObjectType) {
                    ObjectType objectType = (ObjectType)type;
                    if (class_.getName().equals(objectType.getJavaClassName())) {
                        n6 += 0;
                        continue;
                    }
                    if (class_.isAssignableFrom(objectType.getJavaClass())) {
                        ++n6;
                        continue;
                    }
                    n6 = Integer.MAX_VALUE;
                    break;
                }
                n6 = Integer.MAX_VALUE;
                break;
            }
            if (n5 != n2) continue;
            class_ = method.getReturnType();
            this._type = (Type)_java2Internal.get(class_);
            if (this._type == null) {
                this._type = Type.newObjectType(class_);
            }
            if (this._type == null || n6 >= n4) continue;
            this._chosenMethod = method;
            n4 = n6;
        }
        if (this._chosenMethod != null && this._thisArgument == null && !Modifier.isStatic(this._chosenMethod.getModifiers())) {
            throw new TypeCheckError("NO_JAVA_FUNCT_THIS_REF", this.getMethodSignature(vector2));
        }
        if (this._type == null) throw new TypeCheckError("ARGUMENT_CONVERSION_ERR", this.getMethodSignature(vector2));
        if (this._type != Type.NodeSet) return this._type;
        this.getXSLTC().setMultiDocument(true);
        return this._type;
    }

    public Vector typeCheckArgs(SymbolTable symbolTable) throws TypeCheckError {
        Vector<Type> vector = new Vector<Type>();
        Enumeration enumeration = this._arguments.elements();
        while (enumeration.hasMoreElements()) {
            Expression expression = (Expression)enumeration.nextElement();
            vector.addElement(expression.typeCheck(symbolTable));
        }
        return vector;
    }

    protected final Expression argument(int n2) {
        return (Expression)this._arguments.elementAt(n2);
    }

    protected final Expression argument() {
        return this.argument(0);
    }

    protected final int argumentCount() {
        return this._arguments.size();
    }

    protected final void setArgument(int n2, Expression expression) {
        this._arguments.setElementAt(expression, n2);
    }

    public void translateDesynthesized(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Type type = Type.Boolean;
        if (this._chosenMethodType != null) {
            type = this._chosenMethodType.resultType();
        }
        InstructionList instructionList = methodGenerator.getInstructionList();
        this.translate(classGenerator, methodGenerator);
        if (type instanceof BooleanType || type instanceof IntType) {
            this._falseList.add(instructionList.append(new IFEQ(null)));
        }
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        int n2 = this.argumentCount();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        boolean bl = classGenerator.getParser().getXSLTC().isSecureProcessing();
        if (this.isStandard() || this.isExtension()) {
            Object object;
            for (int i2 = 0; i2 < n2; ++i2) {
                object = this.argument(i2);
                object.translate(classGenerator, methodGenerator);
                object.startIterator(classGenerator, methodGenerator);
            }
            String string = this._fname.toString().replace('-', '_') + "F";
            object = "";
            if (string.equals("sumF")) {
                object = "Lorg/apache/xalan/xsltc/DOM;";
                instructionList.append(methodGenerator.loadDOM());
            } else if (string.equals("normalize_spaceF") && this._chosenMethodType.toSignature((String)object).equals("()Ljava/lang/String;")) {
                object = "ILorg/apache/xalan/xsltc/DOM;";
                instructionList.append(methodGenerator.loadContextNode());
                instructionList.append(methodGenerator.loadDOM());
            }
            int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", string, this._chosenMethodType.toSignature((String)object));
            instructionList.append(new INVOKESTATIC(n3));
        } else if (this.unresolvedExternal) {
            int n4 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unresolved_externalF", "(Ljava/lang/String;)V");
            instructionList.append(new PUSH(constantPoolGen, this._fname.toString()));
            instructionList.append(new INVOKESTATIC(n4));
        } else if (this._isExtConstructor) {
            Expression expression;
            int n5;
            if (bl) {
                this.translateUnallowedExtension(constantPoolGen, instructionList);
            }
            String string = this._chosenConstructor.getDeclaringClass().getName();
            Class<?>[] arrclass = this._chosenConstructor.getParameterTypes();
            LocalVariableGen[] arrlocalVariableGen = new LocalVariableGen[n2];
            for (n5 = 0; n5 < n2; ++n5) {
                expression = this.argument(n5);
                Type type = expression.getType();
                expression.translate(classGenerator, methodGenerator);
                expression.startIterator(classGenerator, methodGenerator);
                type.translateTo(classGenerator, methodGenerator, arrclass[n5]);
                arrlocalVariableGen[n5] = methodGenerator.addLocalVariable("function_call_tmp" + n5, type.toJCType(), null, null);
                arrlocalVariableGen[n5].setStart(instructionList.append(type.STORE(arrlocalVariableGen[n5].getIndex())));
            }
            instructionList.append(new NEW(constantPoolGen.addClass(this._className)));
            instructionList.append(InstructionConstants.DUP);
            for (n5 = 0; n5 < n2; ++n5) {
                expression = this.argument(n5);
                arrlocalVariableGen[n5].setEnd(instructionList.append(expression.getType().LOAD(arrlocalVariableGen[n5].getIndex())));
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append('(');
            for (int i3 = 0; i3 < arrclass.length; ++i3) {
                stringBuffer.append(FunctionCall.getSignature(arrclass[i3]));
            }
            stringBuffer.append(')');
            stringBuffer.append("V");
            int n6 = constantPoolGen.addMethodref(string, "<init>", stringBuffer.toString());
            instructionList.append(new INVOKESPECIAL(n6));
            Type.Object.translateFrom(classGenerator, methodGenerator, this._chosenConstructor.getDeclaringClass());
        } else {
            if (bl) {
                this.translateUnallowedExtension(constantPoolGen, instructionList);
            }
            String string = this._chosenMethod.getDeclaringClass().getName();
            Class<?>[] arrclass = this._chosenMethod.getParameterTypes();
            if (this._thisArgument != null) {
                this._thisArgument.translate(classGenerator, methodGenerator);
            }
            for (int i4 = 0; i4 < n2; ++i4) {
                Expression expression = this.argument(i4);
                expression.translate(classGenerator, methodGenerator);
                expression.startIterator(classGenerator, methodGenerator);
                expression.getType().translateTo(classGenerator, methodGenerator, arrclass[i4]);
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append('(');
            for (int i5 = 0; i5 < arrclass.length; ++i5) {
                stringBuffer.append(FunctionCall.getSignature(arrclass[i5]));
            }
            stringBuffer.append(')');
            stringBuffer.append(FunctionCall.getSignature(this._chosenMethod.getReturnType()));
            if (this._thisArgument != null && this._clazz.isInterface()) {
                int n7 = constantPoolGen.addInterfaceMethodref(string, this._fname.getLocalPart(), stringBuffer.toString());
                instructionList.append(new INVOKEINTERFACE(n7, n2 + 1));
            } else {
                int n8 = constantPoolGen.addMethodref(string, this._fname.getLocalPart(), stringBuffer.toString());
                instructionList.append(this._thisArgument != null ? new INVOKEVIRTUAL(n8) : new INVOKESTATIC(n8));
            }
            this._type.translateFrom(classGenerator, methodGenerator, this._chosenMethod.getReturnType());
        }
    }

    public String toString() {
        return "funcall(" + this._fname + ", " + this._arguments + ')';
    }

    public boolean isStandard() {
        String string = this._fname.getNamespace();
        return string == null || string.equals("");
    }

    public boolean isExtension() {
        String string = this._fname.getNamespace();
        return string != null && string.equals("http://xml.apache.org/xalan/xsltc");
    }

    private Vector findMethods() {
        Vector<Method> vector = null;
        String string = this._fname.getNamespace();
        if (this._className != null && this._className.length() > 0) {
            int n2 = this._arguments.size();
            try {
                Object object;
                if (this._clazz == null) {
                    this._clazz = ObjectFactory.findProviderClass(this._className, ObjectFactory.findClassLoader(), true);
                    if (this._clazz == null) {
                        object = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
                        this.getParser().reportError(3, (ErrorMsg)object);
                    }
                }
                object = this._fname.getLocalPart();
                Method[] arrmethod = this._clazz.getMethods();
                for (int i2 = 0; i2 < arrmethod.length; ++i2) {
                    int n3 = arrmethod[i2].getModifiers();
                    if (!Modifier.isPublic(n3) || !arrmethod[i2].getName().equals(object) || arrmethod[i2].getParameterTypes().length != n2) continue;
                    if (vector == null) {
                        vector = new Vector<Method>();
                    }
                    vector.addElement(arrmethod[i2]);
                }
            }
            catch (ClassNotFoundException classNotFoundException) {
                ErrorMsg errorMsg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
                this.getParser().reportError(3, errorMsg);
            }
        }
        return vector;
    }

    private Vector findConstructors() {
        Vector<Object> vector = null;
        String string = this._fname.getNamespace();
        int n2 = this._arguments.size();
        try {
            Object object;
            if (this._clazz == null) {
                this._clazz = ObjectFactory.findProviderClass(this._className, ObjectFactory.findClassLoader(), true);
                if (this._clazz == null) {
                    object = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
                    this.getParser().reportError(3, (ErrorMsg)object);
                }
            }
            object = this._clazz.getConstructors();
            for (int i2 = 0; i2 < object.length; ++i2) {
                int n3 = object[i2].getModifiers();
                if (!Modifier.isPublic(n3) || object[i2].getParameterTypes().length != n2) continue;
                if (vector == null) {
                    vector = new Vector<Object>();
                }
                vector.addElement(object[i2]);
            }
        }
        catch (ClassNotFoundException classNotFoundException) {
            ErrorMsg errorMsg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
            this.getParser().reportError(3, errorMsg);
        }
        return vector;
    }

    static final String getSignature(Class class_) {
        if (class_.isArray()) {
            StringBuffer stringBuffer = new StringBuffer();
            Class class_2 = class_;
            while (class_2.isArray()) {
                stringBuffer.append("[");
                class_2 = class_2.getComponentType();
            }
            stringBuffer.append(FunctionCall.getSignature(class_2));
            return stringBuffer.toString();
        }
        if (class_.isPrimitive()) {
            if (class_ == Integer.TYPE) {
                return "I";
            }
            if (class_ == Byte.TYPE) {
                return "B";
            }
            if (class_ == Long.TYPE) {
                return "J";
            }
            if (class_ == Float.TYPE) {
                return "F";
            }
            if (class_ == Double.TYPE) {
                return "D";
            }
            if (class_ == Short.TYPE) {
                return "S";
            }
            if (class_ == Character.TYPE) {
                return "C";
            }
            if (class_ == Boolean.TYPE) {
                return "Z";
            }
            if (class_ == Void.TYPE) {
                return "V";
            }
            String string = class_.toString();
            ErrorMsg errorMsg = new ErrorMsg("UNKNOWN_SIG_TYPE_ERR", string);
            throw new Error(errorMsg.toString());
        }
        return "L" + class_.getName().replace('.', '/') + ';';
    }

    static final String getSignature(Method method) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        Class<?>[] arrclass = method.getParameterTypes();
        for (int i2 = 0; i2 < arrclass.length; ++i2) {
            stringBuffer.append(FunctionCall.getSignature(arrclass[i2]));
        }
        return stringBuffer.append(')').append(FunctionCall.getSignature(method.getReturnType())).toString();
    }

    static final String getSignature(Constructor constructor) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        Class<?>[] arrclass = constructor.getParameterTypes();
        for (int i2 = 0; i2 < arrclass.length; ++i2) {
            stringBuffer.append(FunctionCall.getSignature(arrclass[i2]));
        }
        return stringBuffer.append(")V").toString();
    }

    private String getMethodSignature(Vector vector) {
        StringBuffer stringBuffer = new StringBuffer(this._className);
        stringBuffer.append('.').append(this._fname.getLocalPart()).append('(');
        int n2 = vector.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Type type = (Type)vector.elementAt(i2);
            stringBuffer.append(type.toString());
            if (i2 >= n2 - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(')');
        return stringBuffer.toString();
    }

    protected static String replaceDash(String string) {
        char c2 = '-';
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i2 = 0; i2 < string.length(); ++i2) {
            if (i2 > 0 && string.charAt(i2 - 1) == c2) {
                stringBuffer.append(Character.toUpperCase(string.charAt(i2)));
                continue;
            }
            if (string.charAt(i2) == c2) continue;
            stringBuffer.append(string.charAt(i2));
        }
        return stringBuffer.toString();
    }

    private void translateUnallowedExtension(ConstantPoolGen constantPoolGen, InstructionList instructionList) {
        int n2 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unallowed_extension_functionF", "(Ljava/lang/String;)V");
        instructionList.append(new PUSH(constantPoolGen, this._fname.toString()));
        instructionList.append(new INVOKESTATIC(n2));
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
        try {
            Class class_ = Class.forName("org.w3c.dom.Node");
            Class class_2 = Class.forName("org.w3c.dom.NodeList");
            _internal2Java.put(Type.Boolean, new JavaType(Boolean.TYPE, 0));
            Class class_3 = class$java$lang$Boolean == null ? (FunctionCall.class$java$lang$Boolean = FunctionCall.class$("java.lang.Boolean")) : class$java$lang$Boolean;
            _internal2Java.put(Type.Boolean, new JavaType(class_3, 1));
            Class class_4 = class$java$lang$Object == null ? (FunctionCall.class$java$lang$Object = FunctionCall.class$("java.lang.Object")) : class$java$lang$Object;
            _internal2Java.put(Type.Boolean, new JavaType(class_4, 2));
            _internal2Java.put(Type.Real, new JavaType(Double.TYPE, 0));
            Class class_5 = class$java$lang$Double == null ? (FunctionCall.class$java$lang$Double = FunctionCall.class$("java.lang.Double")) : class$java$lang$Double;
            _internal2Java.put(Type.Real, new JavaType(class_5, 1));
            _internal2Java.put(Type.Real, new JavaType(Float.TYPE, 2));
            _internal2Java.put(Type.Real, new JavaType(Long.TYPE, 3));
            _internal2Java.put(Type.Real, new JavaType(Integer.TYPE, 4));
            _internal2Java.put(Type.Real, new JavaType(Short.TYPE, 5));
            _internal2Java.put(Type.Real, new JavaType(Byte.TYPE, 6));
            _internal2Java.put(Type.Real, new JavaType(Character.TYPE, 7));
            _internal2Java.put(Type.Real, new JavaType(class$java$lang$Object == null ? (FunctionCall.class$java$lang$Object = FunctionCall.class$("java.lang.Object")) : class$java$lang$Object, 8));
            _internal2Java.put(Type.Int, new JavaType(Double.TYPE, 0));
            _internal2Java.put(Type.Int, new JavaType(class$java$lang$Double == null ? (FunctionCall.class$java$lang$Double = FunctionCall.class$("java.lang.Double")) : class$java$lang$Double, 1));
            _internal2Java.put(Type.Int, new JavaType(Float.TYPE, 2));
            _internal2Java.put(Type.Int, new JavaType(Long.TYPE, 3));
            _internal2Java.put(Type.Int, new JavaType(Integer.TYPE, 4));
            _internal2Java.put(Type.Int, new JavaType(Short.TYPE, 5));
            _internal2Java.put(Type.Int, new JavaType(Byte.TYPE, 6));
            _internal2Java.put(Type.Int, new JavaType(Character.TYPE, 7));
            _internal2Java.put(Type.Int, new JavaType(class$java$lang$Object == null ? (FunctionCall.class$java$lang$Object = FunctionCall.class$("java.lang.Object")) : class$java$lang$Object, 8));
            Class class_6 = class$java$lang$String == null ? (FunctionCall.class$java$lang$String = FunctionCall.class$("java.lang.String")) : class$java$lang$String;
            _internal2Java.put(Type.String, new JavaType(class_6, 0));
            _internal2Java.put(Type.String, new JavaType(class$java$lang$Object == null ? (FunctionCall.class$java$lang$Object = FunctionCall.class$("java.lang.Object")) : class$java$lang$Object, 1));
            _internal2Java.put(Type.NodeSet, new JavaType(class_2, 0));
            _internal2Java.put(Type.NodeSet, new JavaType(class_, 1));
            _internal2Java.put(Type.NodeSet, new JavaType(class$java$lang$Object == null ? (FunctionCall.class$java$lang$Object = FunctionCall.class$("java.lang.Object")) : class$java$lang$Object, 2));
            _internal2Java.put(Type.NodeSet, new JavaType(class$java$lang$String == null ? (FunctionCall.class$java$lang$String = FunctionCall.class$("java.lang.String")) : class$java$lang$String, 3));
            _internal2Java.put(Type.Node, new JavaType(class_2, 0));
            _internal2Java.put(Type.Node, new JavaType(class_, 1));
            _internal2Java.put(Type.Node, new JavaType(class$java$lang$Object == null ? (FunctionCall.class$java$lang$Object = FunctionCall.class$("java.lang.Object")) : class$java$lang$Object, 2));
            _internal2Java.put(Type.Node, new JavaType(class$java$lang$String == null ? (FunctionCall.class$java$lang$String = FunctionCall.class$("java.lang.String")) : class$java$lang$String, 3));
            _internal2Java.put(Type.ResultTree, new JavaType(class_2, 0));
            _internal2Java.put(Type.ResultTree, new JavaType(class_, 1));
            _internal2Java.put(Type.ResultTree, new JavaType(class$java$lang$Object == null ? (FunctionCall.class$java$lang$Object = FunctionCall.class$("java.lang.Object")) : class$java$lang$Object, 2));
            _internal2Java.put(Type.ResultTree, new JavaType(class$java$lang$String == null ? (FunctionCall.class$java$lang$String = FunctionCall.class$("java.lang.String")) : class$java$lang$String, 3));
            _internal2Java.put(Type.Reference, new JavaType(class$java$lang$Object == null ? (FunctionCall.class$java$lang$Object = FunctionCall.class$("java.lang.Object")) : class$java$lang$Object, 0));
            _java2Internal.put(Boolean.TYPE, Type.Boolean);
            _java2Internal.put(Void.TYPE, Type.Void);
            _java2Internal.put(Character.TYPE, Type.Real);
            _java2Internal.put(Byte.TYPE, Type.Real);
            _java2Internal.put(Short.TYPE, Type.Real);
            _java2Internal.put(Integer.TYPE, Type.Real);
            _java2Internal.put(Long.TYPE, Type.Real);
            _java2Internal.put(Float.TYPE, Type.Real);
            _java2Internal.put(Double.TYPE, Type.Real);
            _java2Internal.put(class$java$lang$String == null ? (FunctionCall.class$java$lang$String = FunctionCall.class$("java.lang.String")) : class$java$lang$String, Type.String);
            _java2Internal.put(class$java$lang$Object == null ? (FunctionCall.class$java$lang$Object = FunctionCall.class$("java.lang.Object")) : class$java$lang$Object, Type.Reference);
            _java2Internal.put(class_2, Type.NodeSet);
            _java2Internal.put(class_, Type.NodeSet);
            _extensionNamespaceTable.put("http://xml.apache.org/xalan", "org.apache.xalan.lib.Extensions");
            _extensionNamespaceTable.put("http://exslt.org/common", "org.apache.xalan.lib.ExsltCommon");
            _extensionNamespaceTable.put("http://exslt.org/math", "org.apache.xalan.lib.ExsltMath");
            _extensionNamespaceTable.put("http://exslt.org/sets", "org.apache.xalan.lib.ExsltSets");
            _extensionNamespaceTable.put("http://exslt.org/dates-and-times", "org.apache.xalan.lib.ExsltDatetime");
            _extensionNamespaceTable.put("http://exslt.org/strings", "org.apache.xalan.lib.ExsltStrings");
            _extensionFunctionTable.put("http://exslt.org/common:nodeSet", "nodeset");
            _extensionFunctionTable.put("http://exslt.org/common:objectType", "objectType");
            _extensionFunctionTable.put("http://xml.apache.org/xalan:nodeset", "nodeset");
        }
        catch (ClassNotFoundException classNotFoundException) {
            System.err.println(classNotFoundException);
        }
    }

    static class JavaType {
        public Class type;
        public int distance;

        public JavaType(Class class_, int n2) {
            this.type = class_;
            this.distance = n2;
        }

        public boolean equals(Object object) {
            return object.equals(this.type);
        }
    }

}

