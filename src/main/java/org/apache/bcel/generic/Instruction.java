/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.util.ByteSequence;

public abstract class Instruction
implements Serializable,
Cloneable {
    protected short length = 1;
    protected short opcode = -1;

    Instruction() {
    }

    public Instruction(short s2, short s3) {
        this.length = s3;
        this.opcode = s2;
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
    }

    public String toString(boolean bl) {
        if (bl) {
            return Constants.OPCODE_NAMES[this.opcode] + "[" + this.opcode + "](" + this.length + ")";
        }
        return Constants.OPCODE_NAMES[this.opcode];
    }

    public String toString() {
        return this.toString(true);
    }

    public String toString(ConstantPool constantPool) {
        return this.toString(false);
    }

    public Instruction copy() {
        Instruction instruction = null;
        if (InstructionConstants.INSTRUCTIONS[this.getOpcode()] != null) {
            instruction = this;
        } else {
            try {
                instruction = (Instruction)this.clone();
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                System.err.println(cloneNotSupportedException);
            }
        }
        return instruction;
    }

    protected void initFromFile(ByteSequence byteSequence, boolean bl) throws IOException {
    }

    public static final Instruction readInstruction(ByteSequence byteSequence) throws IOException {
        Class class_;
        boolean bl = false;
        short s2 = (short)byteSequence.readUnsignedByte();
        Instruction instruction = null;
        if (s2 == 196) {
            bl = true;
            s2 = (short)byteSequence.readUnsignedByte();
        }
        if (InstructionConstants.INSTRUCTIONS[s2] != null) {
            return InstructionConstants.INSTRUCTIONS[s2];
        }
        try {
            class_ = Class.forName(Instruction.className(s2));
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new ClassGenException("Illegal opcode detected.");
        }
        try {
            instruction = (Instruction)class_.newInstance();
            if (bl && !(instruction instanceof LocalVariableInstruction) && !(instruction instanceof IINC) && !(instruction instanceof RET)) {
                throw new Exception("Illegal opcode after wide: " + s2);
            }
            instruction.setOpcode(s2);
            instruction.initFromFile(byteSequence, bl);
        }
        catch (Exception exception) {
            throw new ClassGenException(exception.toString());
        }
        return instruction;
    }

    private static final String className(short s2) {
        String string = Constants.OPCODE_NAMES[s2].toUpperCase();
        try {
            int n2 = string.length();
            char c2 = string.charAt(n2 - 2);
            char c3 = string.charAt(n2 - 1);
            if (c2 == '_' && c3 >= '0' && c3 <= '5') {
                string = string.substring(0, n2 - 2);
            }
            if (string.equals("ICONST_M1")) {
                string = "ICONST";
            }
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            System.err.println(stringIndexOutOfBoundsException);
        }
        return "org.apache.bcel.generic." + string;
    }

    public int consumeStack(ConstantPoolGen constantPoolGen) {
        return Constants.CONSUME_STACK[this.opcode];
    }

    public int produceStack(ConstantPoolGen constantPoolGen) {
        return Constants.PRODUCE_STACK[this.opcode];
    }

    public short getOpcode() {
        return this.opcode;
    }

    public int getLength() {
        return this.length;
    }

    private void setOpcode(short s2) {
        this.opcode = s2;
    }

    void dispose() {
    }

    public abstract void accept(Visitor var1);
}

