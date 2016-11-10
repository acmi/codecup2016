/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import java.util.Vector;
import org.apache.xalan.xsltc.compiler.util.Type;

public final class MethodType
extends Type {
    private final Type _resultType;
    private final Vector _argsType;

    public MethodType(Type type) {
        this._argsType = null;
        this._resultType = type;
    }

    public MethodType(Type type, Type type2) {
        if (type2 != Type.Void) {
            this._argsType = new Vector();
            this._argsType.addElement(type2);
        } else {
            this._argsType = null;
        }
        this._resultType = type;
    }

    public MethodType(Type type, Type type2, Type type3) {
        this._argsType = new Vector(2);
        this._argsType.addElement(type2);
        this._argsType.addElement(type3);
        this._resultType = type;
    }

    public MethodType(Type type, Type type2, Type type3, Type type4) {
        this._argsType = new Vector(3);
        this._argsType.addElement(type2);
        this._argsType.addElement(type3);
        this._argsType.addElement(type4);
        this._resultType = type;
    }

    public MethodType(Type type, Vector vector) {
        this._resultType = type;
        this._argsType = vector.size() > 0 ? vector : null;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("method{");
        if (this._argsType != null) {
            int n2 = this._argsType.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                stringBuffer.append(this._argsType.elementAt(i2));
                if (i2 == n2 - 1) continue;
                stringBuffer.append(',');
            }
        } else {
            stringBuffer.append("void");
        }
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    public String toSignature() {
        return this.toSignature("");
    }

    public String toSignature(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        if (this._argsType != null) {
            int n2 = this._argsType.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                stringBuffer.append(((Type)this._argsType.elementAt(i2)).toSignature());
            }
        }
        return stringBuffer.append(string).append(')').append(this._resultType.toSignature()).toString();
    }

    public org.apache.bcel.generic.Type toJCType() {
        return null;
    }

    public boolean identicalTo(Type type) {
        boolean bl = false;
        if (type instanceof MethodType) {
            MethodType methodType = (MethodType)type;
            if (this._resultType.identicalTo(methodType._resultType)) {
                int n2 = this.argsCount();
                bl = n2 == methodType.argsCount();
                for (int i2 = 0; i2 < n2 && bl; ++i2) {
                    Type type2 = (Type)this._argsType.elementAt(i2);
                    Type type3 = (Type)methodType._argsType.elementAt(i2);
                    bl = type2.identicalTo(type3);
                }
            }
        }
        return bl;
    }

    public int distanceTo(Type type) {
        int n2;
        n2 = Integer.MAX_VALUE;
        if (type instanceof MethodType) {
            MethodType methodType = (MethodType)type;
            if (this._argsType != null) {
                int n3 = this._argsType.size();
                if (n3 == methodType._argsType.size()) {
                    n2 = 0;
                    for (int i2 = 0; i2 < n3; ++i2) {
                        Type type2;
                        Type type3 = (Type)this._argsType.elementAt(i2);
                        int n4 = type3.distanceTo(type2 = (Type)methodType._argsType.elementAt(i2));
                        if (n4 == Integer.MAX_VALUE) {
                            n2 = n4;
                            break;
                        }
                        n2 += type3.distanceTo(type2);
                    }
                }
            } else if (methodType._argsType == null) {
                n2 = 0;
            }
        }
        return n2;
    }

    public Type resultType() {
        return this._resultType;
    }

    public Vector argsType() {
        return this._argsType;
    }

    public int argsCount() {
        return this._argsType == null ? 0 : this._argsType.size();
    }
}

