/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Iterator;
import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

public final class FlowList {
    private Vector _elements;

    public FlowList() {
        this._elements = null;
    }

    public FlowList(InstructionHandle instructionHandle) {
        this._elements = new Vector();
        this._elements.addElement(instructionHandle);
    }

    public FlowList(FlowList flowList) {
        this._elements = flowList._elements;
    }

    public FlowList add(InstructionHandle instructionHandle) {
        if (this._elements == null) {
            this._elements = new Vector();
        }
        this._elements.addElement(instructionHandle);
        return this;
    }

    public FlowList append(FlowList flowList) {
        if (this._elements == null) {
            this._elements = flowList._elements;
        } else {
            Vector vector = flowList._elements;
            if (vector != null) {
                int n2 = vector.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    this._elements.addElement(vector.elementAt(i2));
                }
            }
        }
        return this;
    }

    public void backPatch(InstructionHandle instructionHandle) {
        if (this._elements != null) {
            int n2 = this._elements.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                BranchHandle branchHandle = (BranchHandle)this._elements.elementAt(i2);
                branchHandle.setTarget(instructionHandle);
            }
            this._elements.clear();
        }
    }

    public FlowList copyAndRedirect(InstructionList instructionList, InstructionList instructionList2) {
        FlowList flowList = new FlowList();
        if (this._elements == null) {
            return flowList;
        }
        int n2 = this._elements.size();
        Iterator iterator = instructionList.iterator();
        Iterator iterator2 = instructionList2.iterator();
        while (iterator.hasNext()) {
            InstructionHandle instructionHandle = (InstructionHandle)iterator.next();
            InstructionHandle instructionHandle2 = (InstructionHandle)iterator2.next();
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this._elements.elementAt(i2) != instructionHandle) continue;
                flowList.add(instructionHandle2);
            }
        }
        return flowList;
    }
}

