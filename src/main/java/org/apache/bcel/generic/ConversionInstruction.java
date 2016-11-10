/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.generic;

import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;

public abstract class ConversionInstruction
extends Instruction
implements StackConsumer,
StackProducer,
TypedInstruction {
    protected ConversionInstruction(short s2) {
        super(s2, 1);
    }

    public Type getType(ConstantPoolGen constantPoolGen) {
        switch (this.opcode) {
            case 136: 
            case 139: 
            case 142: {
                return Type.INT;
            }
            case 134: 
            case 137: 
            case 144: {
                return Type.FLOAT;
            }
            case 133: 
            case 140: 
            case 143: {
                return Type.LONG;
            }
            case 135: 
            case 138: 
            case 141: {
                return Type.DOUBLE;
            }
            case 145: {
                return Type.BYTE;
            }
            case 146: {
                return Type.CHAR;
            }
            case 147: {
                return Type.SHORT;
            }
        }
        throw new ClassGenException("Unknown type " + this.opcode);
    }
}

