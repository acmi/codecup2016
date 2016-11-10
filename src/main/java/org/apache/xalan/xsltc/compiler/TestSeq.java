/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Dictionary;
import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.LocationPathPattern;
import org.apache.xalan.xsltc.compiler.Mode;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

final class TestSeq {
    private int _kernelType;
    private Vector _patterns = null;
    private Mode _mode = null;
    private Template _default = null;
    private InstructionList _instructionList;
    private InstructionHandle _start = null;

    public TestSeq(Vector vector, Mode mode) {
        this(vector, -2, mode);
    }

    public TestSeq(Vector vector, int n2, Mode mode) {
        this._patterns = vector;
        this._kernelType = n2;
        this._mode = mode;
    }

    public String toString() {
        int n2 = this._patterns.size();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < n2; ++i2) {
            LocationPathPattern locationPathPattern = (LocationPathPattern)this._patterns.elementAt(i2);
            if (i2 == 0) {
                stringBuffer.append("Testseq for kernel " + this._kernelType).append('\n');
            }
            stringBuffer.append("   pattern " + i2 + ": ").append(locationPathPattern.toString()).append('\n');
        }
        return stringBuffer.toString();
    }

    public InstructionList getInstructionList() {
        return this._instructionList;
    }

    public double getPriority() {
        Template template = this._patterns.size() == 0 ? this._default : ((Pattern)this._patterns.elementAt(0)).getTemplate();
        return template.getPriority();
    }

    public int getPosition() {
        Template template = this._patterns.size() == 0 ? this._default : ((Pattern)this._patterns.elementAt(0)).getTemplate();
        return template.getPosition();
    }

    public void reduce() {
        Vector<LocationPathPattern> vector = new Vector<LocationPathPattern>();
        int n2 = this._patterns.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            LocationPathPattern locationPathPattern = (LocationPathPattern)this._patterns.elementAt(i2);
            locationPathPattern.reduceKernelPattern();
            if (locationPathPattern.isWildcard()) {
                this._default = locationPathPattern.getTemplate();
                break;
            }
            vector.addElement(locationPathPattern);
        }
        this._patterns = vector;
    }

    public void findTemplates(Dictionary dictionary) {
        if (this._default != null) {
            dictionary.put(this._default, this);
        }
        for (int i2 = 0; i2 < this._patterns.size(); ++i2) {
            LocationPathPattern locationPathPattern = (LocationPathPattern)this._patterns.elementAt(i2);
            dictionary.put(locationPathPattern.getTemplate(), this);
        }
    }

    private InstructionHandle getTemplateHandle(Template template) {
        return this._mode.getTemplateInstructionHandle(template);
    }

    private LocationPathPattern getPattern(int n2) {
        return (LocationPathPattern)this._patterns.elementAt(n2);
    }

    public InstructionHandle compile(ClassGenerator classGenerator, MethodGenerator methodGenerator, InstructionHandle instructionHandle) {
        if (this._start != null) {
            return this._start;
        }
        int n2 = this._patterns.size();
        if (n2 == 0) {
            this._start = this.getTemplateHandle(this._default);
            return this._start;
        }
        InstructionHandle instructionHandle2 = this._default == null ? instructionHandle : this.getTemplateHandle(this._default);
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            FlowList flowList;
            LocationPathPattern locationPathPattern = this.getPattern(i2);
            Template template = locationPathPattern.getTemplate();
            InstructionList instructionList = new InstructionList();
            instructionList.append(methodGenerator.loadCurrentNode());
            InstructionList instructionList2 = methodGenerator.getInstructionList(locationPathPattern);
            if (instructionList2 == null) {
                instructionList2 = locationPathPattern.compile(classGenerator, methodGenerator);
                methodGenerator.addInstructionList(locationPathPattern, instructionList2);
            }
            InstructionList instructionList3 = instructionList2.copy();
            FlowList flowList2 = locationPathPattern.getTrueList();
            if (flowList2 != null) {
                flowList2 = flowList2.copyAndRedirect(instructionList2, instructionList3);
            }
            if ((flowList = locationPathPattern.getFalseList()) != null) {
                flowList = flowList.copyAndRedirect(instructionList2, instructionList3);
            }
            instructionList.append(instructionList3);
            InstructionHandle instructionHandle3 = this.getTemplateHandle(template);
            BranchHandle branchHandle = instructionList.append(new GOTO_W(instructionHandle3));
            if (flowList2 != null) {
                flowList2.backPatch(branchHandle);
            }
            if (flowList != null) {
                flowList.backPatch(instructionHandle2);
            }
            instructionHandle2 = instructionList.getStart();
            if (this._instructionList != null) {
                instructionList.append(this._instructionList);
            }
            this._instructionList = instructionList;
        }
        this._start = instructionHandle2;
        return this._start;
    }
}

