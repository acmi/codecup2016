/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$MethodVisitor;
import com.google.inject.internal.asm.$Type;

public class $LocalVariablesSorter
extends $MethodVisitor {
    protected final int firstLocal;
    private final State state;

    public $LocalVariablesSorter(int n2, String string, $MethodVisitor $MethodVisitor) {
        super(327680, $MethodVisitor);
        this.state = new State();
        $Type[] arr$Type = $Type.getArgumentTypes(string);
        this.state.nextLocal = (8 & n2) != 0 ? 0 : 1;
        for (int i2 = 0; i2 < arr$Type.length; ++i2) {
            this.state.nextLocal += arr$Type[i2].getSize();
        }
        this.firstLocal = this.state.nextLocal;
    }

    public $LocalVariablesSorter($LocalVariablesSorter $LocalVariablesSorter) {
        super(327680, $LocalVariablesSorter.mv);
        this.state = $LocalVariablesSorter.state;
        this.firstLocal = $LocalVariablesSorter.firstLocal;
    }

    public void visitVarInsn(int n2, int n3) {
        int n4;
        switch (n2) {
            case 22: 
            case 24: 
            case 55: 
            case 57: {
                n4 = 2;
                break;
            }
            default: {
                n4 = 1;
            }
        }
        this.mv.visitVarInsn(n2, this.remap(n3, n4));
    }

    public void visitIincInsn(int n2, int n3) {
        this.mv.visitIincInsn(this.remap(n2, 1), n3);
    }

    public void visitMaxs(int n2, int n3) {
        this.mv.visitMaxs(n2, this.state.nextLocal);
    }

    public void visitLocalVariable(String string, String string2, String string3, $Label $Label, $Label $Label2, int n2) {
        this.mv.visitLocalVariable(string, string2, string3, $Label, $Label2, this.remap(n2));
    }

    protected int newLocal(int n2) {
        int n3 = this.state.nextLocal;
        this.state.nextLocal += n2;
        return n3;
    }

    private int remap(int n2, int n3) {
        int n4;
        if (n2 < this.firstLocal) {
            return n2;
        }
        int n5 = 2 * n2 + n3 - 1;
        int n6 = this.state.mapping.length;
        if (n5 >= n6) {
            int[] arrn = new int[Math.max(2 * n6, n5 + 1)];
            System.arraycopy(this.state.mapping, 0, arrn, 0, n6);
            this.state.mapping = arrn;
        }
        if ((n4 = this.state.mapping[n5]) == 0) {
            this.state.mapping[n5] = n4 = this.state.nextLocal + 1;
            this.state.nextLocal += n3;
        }
        return n4 - 1;
    }

    private int remap(int n2) {
        int n3;
        if (n2 < this.firstLocal) {
            return n2;
        }
        int n4 = 2 * n2;
        int n5 = n3 = n4 < this.state.mapping.length ? this.state.mapping[n4] : 0;
        if (n3 == 0) {
            int n6 = n3 = n4 + 1 < this.state.mapping.length ? this.state.mapping[n4 + 1] : 0;
        }
        if (n3 == 0) {
            throw new IllegalStateException(new StringBuilder(34).append("Unknown local variable ").append(n2).toString());
        }
        return n3 - 1;
    }

    private static class State {
        int[] mapping = new int[40];
        int nextLocal;

        private State() {
        }
    }

}

