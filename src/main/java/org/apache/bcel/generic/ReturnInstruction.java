/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ExceptionThrower;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;

public abstract class ReturnInstruction
extends Instruction
implements ExceptionThrower,
StackConsumer,
TypedInstruction {
    protected ReturnInstruction(short s2) {
        super(s2, 1);
    }

    public Type getType() {
        switch (this.opcode) {
            case 172: {
                return Type.INT;
            }
            case 173: {
                return Type.LONG;
            }
            case 174: {
                return Type.FLOAT;
            }
            case 175: {
                return Type.DOUBLE;
            }
            case 176: {
                return Type.OBJECT;
            }
            case 177: {
                return Type.VOID;
            }
        }
        throw new ClassGenException("Unknown type " + this.opcode);
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        return this.getType();
    }
}

