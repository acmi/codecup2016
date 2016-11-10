/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.xalan.xsltc.compiler.Closure;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.VariableBase;
import org.apache.xalan.xsltc.compiler.VariableRefBase;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

final class ParameterRef
extends VariableRefBase {
    QName _name = null;

    public ParameterRef(Param param) {
        super(param);
        this._name = param._name;
    }

    public String toString() {
        return "parameter-ref(" + this._variable.getName() + '/' + this._variable.getType() + ')';
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        String string = BasisLibrary.mapQNameToJavaName(this._name.toString());
        String string2 = this._type.toSignature();
        if (this._variable.isLocal()) {
            if (classGenerator.isExternal()) {
                for (object = this._closure; object != null && !object.inInnerClass(); object = object.getParentClosure()) {
                }
                if (object != null) {
                    instructionList.append(ALOAD_0);
                    instructionList.append(new GETFIELD(constantPoolGen.addFieldref(object.getInnerClassName(), string, string2)));
                } else {
                    instructionList.append(this._variable.loadInstruction());
                }
            } else {
                instructionList.append(this._variable.loadInstruction());
            }
        } else {
            object = classGenerator.getClassName();
            instructionList.append(classGenerator.loadTranslet());
            if (classGenerator.isExternal()) {
                instructionList.append(new CHECKCAST(constantPoolGen.addClass((String)object)));
            }
            instructionList.append(new GETFIELD(constantPoolGen.addFieldref((String)object, string, string2)));
        }
        if (this._variable.getType() instanceof NodeSetType) {
            int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "cloneIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
            instructionList.append(new INVOKEINTERFACE(n2, 1));
        }
    }
}

