/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Otherwise;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.Text;
import org.apache.xalan.xsltc.compiler.When;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Choose
extends Instruction {
    Choose() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Choose");
        this.indent(n2 + 4);
        this.displayContents(n2 + 4);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        Object object;
        Object object2;
        Object object3;
        ArrayList<Object> arrayList = new ArrayList<Object>();
        Otherwise otherwise = null;
        Enumeration enumeration = this.elements();
        ErrorMsg errorMsg = null;
        int n2 = this.getLineNumber();
        while (enumeration.hasMoreElements()) {
            object3 = enumeration.nextElement();
            if (object3 instanceof When) {
                arrayList.add(object3);
                continue;
            }
            if (object3 instanceof Otherwise) {
                if (otherwise == null) {
                    otherwise = (Otherwise)object3;
                    continue;
                }
                errorMsg = new ErrorMsg("MULTIPLE_OTHERWISE_ERR", this);
                this.getParser().reportError(3, errorMsg);
                continue;
            }
            if (object3 instanceof Text) {
                ((Text)object3).ignore();
                continue;
            }
            errorMsg = new ErrorMsg("WHEN_ELEMENT_ERR", this);
            this.getParser().reportError(3, errorMsg);
        }
        if (arrayList.size() == 0) {
            errorMsg = new ErrorMsg("MISSING_WHEN_ERR", this);
            this.getParser().reportError(3, errorMsg);
            return;
        }
        object3 = methodGenerator.getInstructionList();
        BranchHandle branchHandle = null;
        ArrayList<BranchHandle> arrayList2 = new ArrayList<BranchHandle>();
        InstructionHandle instructionHandle = null;
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            object = (When)iterator.next();
            object2 = object.getTest();
            InstructionHandle instructionHandle2 = object3.getEnd();
            if (branchHandle != null) {
                branchHandle.setTarget(object3.append(NOP));
            }
            object2.translateDesynthesized(classGenerator, methodGenerator);
            if (object2 instanceof FunctionCall) {
                FunctionCall functionCall = (FunctionCall)object2;
                try {
                    Type type = functionCall.typeCheck(this.getParser().getSymbolTable());
                    if (type != Type.Boolean) {
                        object2._falseList.add(object3.append(new IFEQ(null)));
                    }
                }
                catch (TypeCheckError typeCheckError) {
                    // empty catch block
                }
            }
            instructionHandle2 = object3.getEnd();
            if (!object.ignore()) {
                object.translateContents(classGenerator, methodGenerator);
            }
            arrayList2.add(object3.append(new GOTO(null)));
            if (iterator.hasNext() || otherwise != null) {
                branchHandle = object3.append(new GOTO(null));
                object2.backPatchFalseList(branchHandle);
            } else {
                instructionHandle = object3.append(NOP);
                object2.backPatchFalseList(instructionHandle);
            }
            object2.backPatchTrueList(instructionHandle2.getNext());
        }
        if (otherwise != null) {
            branchHandle.setTarget(object3.append(NOP));
            otherwise.translateContents(classGenerator, methodGenerator);
            instructionHandle = object3.append(NOP);
        }
        object = arrayList2.iterator();
        while (object.hasNext()) {
            object2 = (BranchHandle)object.next();
            object2.setTarget(instructionHandle);
        }
    }
}

