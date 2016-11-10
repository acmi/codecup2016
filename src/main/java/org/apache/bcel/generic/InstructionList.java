/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Select;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.util.ByteSequence;

public class InstructionList
implements Serializable {
    private InstructionHandle start = null;
    private InstructionHandle end = null;
    private int length = 0;
    private int[] byte_positions;

    public InstructionList() {
    }

    public InstructionList(Instruction instruction) {
        this.append(instruction);
    }

    public InstructionList(BranchInstruction branchInstruction) {
        this.append(branchInstruction);
    }

    public boolean isEmpty() {
        return this.start == null;
    }

    public static InstructionHandle findHandle(InstructionHandle[] arrinstructionHandle, int[] arrn, int n2, int n3) {
        int n4 = 0;
        int n5 = n2 - 1;
        do {
            int n6;
            int n7;
            if ((n7 = arrn[n6 = (n4 + n5) / 2]) == n3) {
                return arrinstructionHandle[n6];
            }
            if (n3 < n7) {
                n5 = n6 - 1;
                continue;
            }
            n4 = n6 + 1;
        } while (n4 <= n5);
        return null;
    }

    public InstructionHandle findHandle(int n2) {
        InstructionHandle[] arrinstructionHandle = this.getInstructionHandles();
        return InstructionList.findHandle(arrinstructionHandle, this.byte_positions, this.length, n2);
    }

    public InstructionList(byte[] arrby) {
        int n2;
        Instruction instruction;
        ByteSequence byteSequence = new ByteSequence(arrby);
        InstructionHandle[] arrinstructionHandle = new InstructionHandle[arrby.length];
        int[] arrn = new int[arrby.length];
        int n3 = 0;
        try {
            while (byteSequence.available() > 0) {
                arrn[n3] = n2 = byteSequence.getIndex();
                instruction = Instruction.readInstruction(byteSequence);
                InstructionHandle instructionHandle = instruction instanceof BranchInstruction ? this.append((BranchInstruction)instruction) : this.append(instruction);
                instructionHandle.setPosition(n2);
                arrinstructionHandle[n3] = instructionHandle;
                ++n3;
            }
        }
        catch (IOException iOException) {
            throw new ClassGenException(iOException.toString());
        }
        this.byte_positions = new int[n3];
        System.arraycopy(arrn, 0, this.byte_positions, 0, n3);
        n2 = 0;
        while (n2 < n3) {
            if (arrinstructionHandle[n2] instanceof BranchHandle) {
                instruction = (BranchInstruction)arrinstructionHandle[n2].instruction;
                int n4 = instruction.position + instruction.getIndex();
                InstructionHandle instructionHandle = InstructionList.findHandle(arrinstructionHandle, arrn, n3, n4);
                if (instructionHandle == null) {
                    throw new ClassGenException("Couldn't find target for branch: " + instruction);
                }
                instruction.setTarget(instructionHandle);
                if (instruction instanceof Select) {
                    Select select = (Select)instruction;
                    int[] arrn2 = select.getIndices();
                    int n5 = 0;
                    while (n5 < arrn2.length) {
                        n4 = instruction.position + arrn2[n5];
                        instructionHandle = InstructionList.findHandle(arrinstructionHandle, arrn, n3, n4);
                        if (instructionHandle == null) {
                            throw new ClassGenException("Couldn't find target for switch: " + instruction);
                        }
                        select.setTarget(n5, instructionHandle);
                        ++n5;
                    }
                }
            }
            ++n2;
        }
    }

    public InstructionHandle append(InstructionHandle instructionHandle, InstructionList instructionList) {
        if (instructionList == null) {
            throw new ClassGenException("Appending null InstructionList");
        }
        if (instructionList.isEmpty()) {
            return instructionHandle;
        }
        InstructionHandle instructionHandle2 = instructionHandle.next;
        InstructionHandle instructionHandle3 = instructionList.start;
        instructionHandle.next = instructionList.start;
        instructionList.start.prev = instructionHandle;
        instructionList.end.next = instructionHandle2;
        if (instructionHandle2 != null) {
            instructionHandle2.prev = instructionList.end;
        } else {
            this.end = instructionList.end;
        }
        this.length += instructionList.length;
        instructionList.clear();
        return instructionHandle3;
    }

    public InstructionHandle append(InstructionList instructionList) {
        if (instructionList == null) {
            throw new ClassGenException("Appending null InstructionList");
        }
        if (instructionList.isEmpty()) {
            return null;
        }
        if (this.isEmpty()) {
            this.start = instructionList.start;
            this.end = instructionList.end;
            this.length = instructionList.length;
            instructionList.clear();
            return this.start;
        }
        return this.append(this.end, instructionList);
    }

    private void append(InstructionHandle instructionHandle) {
        if (this.isEmpty()) {
            this.start = this.end = instructionHandle;
            instructionHandle.prev = null;
            instructionHandle.next = null;
        } else {
            this.end.next = instructionHandle;
            instructionHandle.prev = this.end;
            instructionHandle.next = null;
            this.end = instructionHandle;
        }
        ++this.length;
    }

    public InstructionHandle append(Instruction instruction) {
        InstructionHandle instructionHandle = InstructionHandle.getInstructionHandle(instruction);
        this.append(instructionHandle);
        return instructionHandle;
    }

    public BranchHandle append(BranchInstruction branchInstruction) {
        BranchHandle branchHandle = BranchHandle.getBranchHandle(branchInstruction);
        this.append(branchHandle);
        return branchHandle;
    }

    public InstructionHandle append(CompoundInstruction compoundInstruction) {
        return this.append(compoundInstruction.getInstructionList());
    }

    public InstructionHandle append(InstructionHandle instructionHandle, Instruction instruction) {
        return this.append(instructionHandle, new InstructionList(instruction));
    }

    public BranchHandle append(InstructionHandle instructionHandle, BranchInstruction branchInstruction) {
        BranchHandle branchHandle = BranchHandle.getBranchHandle(branchInstruction);
        InstructionList instructionList = new InstructionList();
        instructionList.append(branchHandle);
        this.append(instructionHandle, instructionList);
        return branchHandle;
    }

    public InstructionHandle insert(InstructionHandle instructionHandle, InstructionList instructionList) {
        if (instructionList == null) {
            throw new ClassGenException("Inserting null InstructionList");
        }
        if (instructionList.isEmpty()) {
            return instructionHandle;
        }
        InstructionHandle instructionHandle2 = instructionHandle.prev;
        InstructionHandle instructionHandle3 = instructionList.start;
        instructionHandle.prev = instructionList.end;
        instructionList.end.next = instructionHandle;
        instructionList.start.prev = instructionHandle2;
        if (instructionHandle2 != null) {
            instructionHandle2.next = instructionList.start;
        } else {
            this.start = instructionList.start;
        }
        this.length += instructionList.length;
        instructionList.clear();
        return instructionHandle3;
    }

    public InstructionHandle insert(InstructionList instructionList) {
        if (this.isEmpty()) {
            this.append(instructionList);
            return this.start;
        }
        return this.insert(this.start, instructionList);
    }

    public InstructionHandle insert(InstructionHandle instructionHandle, Instruction instruction) {
        return this.insert(instructionHandle, new InstructionList(instruction));
    }

    public BranchHandle insert(InstructionHandle instructionHandle, BranchInstruction branchInstruction) {
        BranchHandle branchHandle = BranchHandle.getBranchHandle(branchInstruction);
        InstructionList instructionList = new InstructionList();
        instructionList.append(branchHandle);
        this.insert(instructionHandle, instructionList);
        return branchHandle;
    }

    private void remove(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) throws TargetLostException {
        InstructionHandle instructionHandle3;
        InstructionHandle instructionHandle4;
        if (instructionHandle == null && instructionHandle2 == null) {
            instructionHandle3 = instructionHandle4 = this.start;
            this.end = null;
            this.start = null;
        } else {
            if (instructionHandle == null) {
                instructionHandle3 = this.start;
                this.start = instructionHandle2;
            } else {
                instructionHandle3 = instructionHandle.next;
                instructionHandle.next = instructionHandle2;
            }
            if (instructionHandle2 == null) {
                instructionHandle4 = this.end;
                this.end = instructionHandle;
            } else {
                instructionHandle4 = instructionHandle2.prev;
                instructionHandle2.prev = instructionHandle;
            }
        }
        instructionHandle3.prev = null;
        instructionHandle4.next = null;
        ArrayList<InstructionHandle> arrayList = new ArrayList<InstructionHandle>();
        InstructionHandle instructionHandle5 = instructionHandle3;
        while (instructionHandle5 != null) {
            instructionHandle5.getInstruction().dispose();
            instructionHandle5 = instructionHandle5.next;
        }
        StringBuffer stringBuffer = new StringBuffer("{ ");
        InstructionHandle instructionHandle6 = instructionHandle3;
        while (instructionHandle6 != null) {
            instructionHandle2 = instructionHandle6.next;
            --this.length;
            if (instructionHandle6.hasTargeters()) {
                arrayList.add(instructionHandle6);
                stringBuffer.append(instructionHandle6.toString(true) + " ");
                instructionHandle6.prev = null;
                instructionHandle6.next = null;
            } else {
                instructionHandle6.dispose();
            }
            instructionHandle6 = instructionHandle2;
        }
        stringBuffer.append("}");
        if (!arrayList.isEmpty()) {
            InstructionHandle[] arrinstructionHandle = new InstructionHandle[arrayList.size()];
            arrayList.toArray(arrinstructionHandle);
            throw new TargetLostException(arrinstructionHandle, stringBuffer.toString());
        }
    }

    public void delete(InstructionHandle instructionHandle) throws TargetLostException {
        this.remove(instructionHandle.prev, instructionHandle.next);
    }

    public void delete(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) throws TargetLostException {
        this.remove(instructionHandle.prev, instructionHandle2.next);
    }

    private InstructionHandle findInstruction1(Instruction instruction) {
        InstructionHandle instructionHandle = this.start;
        while (instructionHandle != null) {
            if (instructionHandle.instruction == instruction) {
                return instructionHandle;
            }
            instructionHandle = instructionHandle.next;
        }
        return null;
    }

    public boolean contains(Instruction instruction) {
        return this.findInstruction1(instruction) != null;
    }

    public void setPositions() {
        this.setPositions(false);
    }

    public void setPositions(boolean bl) {
        void var9_13;
        InstructionHandle instructionHandle;
        InstructionHandle[] arrinstructionHandle;
        Instruction instruction2;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int[] arrn = new int[this.length];
        if (bl) {
            instructionHandle = this.start;
            while (instructionHandle != null) {
                instruction2 = instructionHandle.instruction;
                if (instruction2 instanceof BranchInstruction) {
                    Instruction instruction = ((BranchInstruction)instruction2).getTarget().instruction;
                    if (!this.contains(instruction)) {
                        throw new ClassGenException("Branch target of " + Constants.OPCODE_NAMES[instruction2.opcode] + ":" + instruction + " not in instruction list");
                    }
                    if (instruction2 instanceof Select) {
                        arrinstructionHandle = ((Select)instruction2).getTargets();
                        int n6 = 0;
                        while (n6 < arrinstructionHandle.length) {
                            Instruction instruction3 = arrinstructionHandle[n6].instruction;
                            if (!this.contains(instruction3)) {
                                throw new ClassGenException("Branch target of " + Constants.OPCODE_NAMES[instruction2.opcode] + ":" + instruction3 + " not in instruction list");
                            }
                            ++n6;
                        }
                    }
                    if (!(instructionHandle instanceof BranchHandle)) {
                        void instruction4;
                        throw new ClassGenException("Branch instruction " + Constants.OPCODE_NAMES[instruction2.opcode] + ":" + instruction4 + " not contained in BranchHandle.");
                    }
                }
                instructionHandle = instructionHandle.next;
            }
        }
        instructionHandle = this.start;
        while (instructionHandle != null) {
            instruction2 = instructionHandle.instruction;
            instructionHandle.setPosition(n4);
            arrn[n5++] = n4;
            switch (instruction2.getOpcode()) {
                case 167: 
                case 168: {
                    n2 += 2;
                    break;
                }
                case 170: 
                case 171: {
                    n2 += 3;
                }
            }
            n4 += instruction2.getLength();
            instructionHandle = instructionHandle.next;
        }
        instruction2 = this.start;
        while (instruction2 != null) {
            n3 += instruction2.updatePosition(n3, n2);
            instruction2 = instruction2.next;
        }
        n5 = 0;
        n4 = 0;
        InstructionHandle instructionHandle2 = this.start;
        while (var9_13 != null) {
            arrinstructionHandle = var9_13.instruction;
            var9_13.setPosition(n4);
            arrn[n5++] = n4;
            n4 += arrinstructionHandle.getLength();
            InstructionHandle instructionHandle3 = var9_13.next;
        }
        this.byte_positions = new int[n5];
        System.arraycopy(arrn, 0, this.byte_positions, 0, n5);
    }

    public byte[] getByteCode() {
        this.setPositions();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            InstructionHandle instructionHandle = this.start;
            while (instructionHandle != null) {
                Instruction instruction = instructionHandle.instruction;
                instruction.dump(dataOutputStream);
                instructionHandle = instructionHandle.next;
            }
        }
        catch (IOException iOException) {
            System.err.println(iOException);
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public String toString() {
        return this.toString(true);
    }

    public String toString(boolean bl) {
        StringBuffer stringBuffer = new StringBuffer();
        InstructionHandle instructionHandle = this.start;
        while (instructionHandle != null) {
            stringBuffer.append(instructionHandle.toString(bl) + "\n");
            instructionHandle = instructionHandle.next;
        }
        return stringBuffer.toString();
    }

    public Iterator iterator() {
        return new Iterator(this){
            private InstructionHandle ih;
            private final InstructionList this$0;

            public Object next() {
                InstructionHandle instructionHandle = this.ih;
                this.ih = this.ih.next;
                return instructionHandle;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext() {
                return this.ih != null;
            }
        };
    }

    public InstructionHandle[] getInstructionHandles() {
        InstructionHandle[] arrinstructionHandle = new InstructionHandle[this.length];
        InstructionHandle instructionHandle = this.start;
        int n2 = 0;
        while (n2 < this.length) {
            arrinstructionHandle[n2] = instructionHandle;
            instructionHandle = instructionHandle.next;
            ++n2;
        }
        return arrinstructionHandle;
    }

    public InstructionList copy() {
        void var4_6;
        HashMap<InstructionHandle, InstructionHandle> hashMap = new HashMap<InstructionHandle, InstructionHandle>();
        InstructionList instructionList = new InstructionList();
        InstructionHandle instructionHandle3 = this.start;
        while (instructionHandle3 != null) {
            Instruction instructionHandle = instructionHandle3.instruction;
            Instruction instruction = instructionHandle.copy();
            if (instruction instanceof BranchInstruction) {
                hashMap.put(instructionHandle3, instructionList.append((BranchInstruction)instruction));
            } else {
                hashMap.put(instructionHandle3, instructionList.append(instruction));
            }
            instructionHandle3 = instructionHandle3.next;
        }
        InstructionHandle instructionHandle = this.start;
        InstructionHandle instructionHandle2 = instructionList.start;
        while (var4_6 != null) {
            Instruction instruction = var4_6.instruction;
            Instruction instruction2 = var5_10.instruction;
            if (instruction instanceof BranchInstruction) {
                BranchInstruction branchInstruction = (BranchInstruction)instruction;
                BranchInstruction branchInstruction2 = (BranchInstruction)instruction2;
                InstructionHandle instructionHandle4 = branchInstruction.getTarget();
                branchInstruction2.setTarget((InstructionHandle)hashMap.get(instructionHandle4));
                if (branchInstruction instanceof Select) {
                    InstructionHandle[] arrinstructionHandle = ((Select)branchInstruction).getTargets();
                    InstructionHandle[] arrinstructionHandle2 = ((Select)branchInstruction2).getTargets();
                    int n2 = 0;
                    while (n2 < arrinstructionHandle.length) {
                        arrinstructionHandle2[n2] = (InstructionHandle)hashMap.get(arrinstructionHandle[n2]);
                        ++n2;
                    }
                }
            }
            InstructionHandle instructionHandle5 = var4_6.next;
            InstructionHandle instructionHandle6 = var5_10.next;
        }
        return instructionList;
    }

    public void replaceConstantPool(ConstantPoolGen constantPoolGen, ConstantPoolGen constantPoolGen2) {
        InstructionHandle instructionHandle = this.start;
        while (instructionHandle != null) {
            Instruction instruction = instructionHandle.instruction;
            if (instruction instanceof CPInstruction) {
                CPInstruction cPInstruction = (CPInstruction)instruction;
                Constant constant = constantPoolGen.getConstant(cPInstruction.getIndex());
                cPInstruction.setIndex(constantPoolGen2.addConstant(constant, constantPoolGen));
            }
            instructionHandle = instructionHandle.next;
        }
    }

    private void clear() {
        this.end = null;
        this.start = null;
        this.length = 0;
    }

    public InstructionHandle getStart() {
        return this.start;
    }

    public InstructionHandle getEnd() {
        return this.end;
    }

    public int getLength() {
        return this.length;
    }

    static InstructionHandle access$000(InstructionList instructionList) {
        return instructionList.start;
    }

}

