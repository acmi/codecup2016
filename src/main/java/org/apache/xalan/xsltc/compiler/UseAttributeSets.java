/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.AttributeSet;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class UseAttributeSets
extends Instruction {
    private static final String ATTR_SET_NOT_FOUND = "";
    private final Vector _sets = new Vector(2);

    public UseAttributeSets(String string, Parser parser) {
        this.setParser(parser);
        this.addAttributeSets(string);
    }

    public void addAttributeSets(String string) {
        if (string != null && !string.equals("")) {
            StringTokenizer stringTokenizer = new StringTokenizer(string);
            while (stringTokenizer.hasMoreTokens()) {
                QName qName = this.getParser().getQNameIgnoreDefaultNs(stringTokenizer.nextToken());
                this._sets.add(qName);
            }
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        SymbolTable symbolTable = this.getParser().getSymbolTable();
        for (int i2 = 0; i2 < this._sets.size(); ++i2) {
            Object object;
            QName qName = (QName)this._sets.elementAt(i2);
            AttributeSet attributeSet = symbolTable.lookupAttributeSet(qName);
            if (attributeSet != null) {
                object = attributeSet.getMethodName();
                instructionList.append(classGenerator.loadTranslet());
                instructionList.append(methodGenerator.loadDOM());
                instructionList.append(methodGenerator.loadIterator());
                instructionList.append(methodGenerator.loadHandler());
                int n2 = constantPoolGen.addMethodref(classGenerator.getClassName(), (String)object, ATTR_SET_SIG);
                instructionList.append(new INVOKESPECIAL(n2));
                continue;
            }
            object = this.getParser();
            String string = qName.toString();
            this.reportError(this, (Parser)object, "ATTRIBSET_UNDEF_ERR", string);
        }
    }
}

