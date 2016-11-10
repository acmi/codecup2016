/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.LiteralExpr;
import org.apache.xalan.xsltc.compiler.ObjectFactory;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class FunctionAvailableCall
extends FunctionCall {
    private Expression _arg;
    private String _nameOfFunct = null;
    private String _namespaceOfFunct = null;
    private boolean _isFunctionAvailable = false;

    public FunctionAvailableCall(QName qName, Vector vector) {
        super(qName, vector);
        this._arg = (Expression)vector.elementAt(0);
        this._type = null;
        if (this._arg instanceof LiteralExpr) {
            LiteralExpr literalExpr = (LiteralExpr)this._arg;
            this._namespaceOfFunct = literalExpr.getNamespace();
            this._nameOfFunct = literalExpr.getValue();
            if (!this.isInternalNamespace()) {
                this._isFunctionAvailable = this.hasMethods();
            }
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        if (this._type != null) {
            return this._type;
        }
        if (this._arg instanceof LiteralExpr) {
            this._type = Type.Boolean;
            return this._type;
        }
        ErrorMsg errorMsg = new ErrorMsg("NEED_LITERAL_ERR", (Object)"function-available", this);
        throw new TypeCheckError(errorMsg);
    }

    public Object evaluateAtCompileTime() {
        return this.getResult() ? Boolean.TRUE : Boolean.FALSE;
    }

    private boolean hasMethods() {
        Object object;
        String string = this.getClassNameFromUri(this._namespaceOfFunct);
        String string2 = null;
        int n2 = this._nameOfFunct.indexOf(":");
        if (n2 > 0) {
            object = this._nameOfFunct.substring(n2 + 1);
            int n3 = object.lastIndexOf(46);
            if (n3 > 0) {
                string2 = object.substring(n3 + 1);
                string = string != null && string.length() != 0 ? string + "." + object.substring(0, n3) : object.substring(0, n3);
            } else {
                string2 = object;
            }
        } else {
            string2 = this._nameOfFunct;
        }
        if (string == null || string2 == null) {
            return false;
        }
        if (string2.indexOf(45) > 0) {
            string2 = FunctionAvailableCall.replaceDash(string2);
        }
        try {
            object = ObjectFactory.findProviderClass(string, ObjectFactory.findClassLoader(), true);
            if (object == null) {
                return false;
            }
            Method[] arrmethod = object.getMethods();
            for (int i2 = 0; i2 < arrmethod.length; ++i2) {
                int n4 = arrmethod[i2].getModifiers();
                if (!Modifier.isPublic(n4) || !Modifier.isStatic(n4) || !arrmethod[i2].getName().equals(string2)) continue;
                return true;
            }
        }
        catch (ClassNotFoundException classNotFoundException) {
            return false;
        }
        return false;
    }

    public boolean getResult() {
        if (this._nameOfFunct == null) {
            return false;
        }
        if (this.isInternalNamespace()) {
            Parser parser = this.getParser();
            this._isFunctionAvailable = parser.functionSupported(Util.getLocalName(this._nameOfFunct));
        }
        return this._isFunctionAvailable;
    }

    private boolean isInternalNamespace() {
        return this._namespaceOfFunct == null || this._namespaceOfFunct.equals("") || this._namespaceOfFunct.equals("http://xml.apache.org/xalan/xsltc");
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        methodGenerator.getInstructionList().append(new PUSH(constantPoolGen, this.getResult()));
    }
}

