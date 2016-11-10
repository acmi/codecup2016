/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.reflect;

import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$AbstractClassGenerator;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$ReflectUtils;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.reflect.$FastClassEmitter;
import com.google.inject.internal.cglib.reflect.$FastConstructor;
import com.google.inject.internal.cglib.reflect.$FastMethod;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public abstract class $FastClass {
    private Class type;

    protected $FastClass() {
        throw new Error("Using the FastClass empty constructor--please report to the cglib-devel mailing list");
    }

    protected $FastClass(Class class_) {
        this.type = class_;
    }

    public static $FastClass create(Class class_) {
        return $FastClass.create(class_.getClassLoader(), class_);
    }

    public static $FastClass create(ClassLoader classLoader, Class class_) {
        Generator generator = new Generator();
        generator.setType(class_);
        generator.setClassLoader(classLoader);
        return generator.create();
    }

    public Object invoke(String string, Class[] arrclass, Object object, Object[] arrobject) throws InvocationTargetException {
        return this.invoke(this.getIndex(string, arrclass), object, arrobject);
    }

    public Object newInstance() throws InvocationTargetException {
        return this.newInstance(this.getIndex($Constants.EMPTY_CLASS_ARRAY), null);
    }

    public Object newInstance(Class[] arrclass, Object[] arrobject) throws InvocationTargetException {
        return this.newInstance(this.getIndex(arrclass), arrobject);
    }

    public $FastMethod getMethod(Method method) {
        return new $FastMethod(this, method);
    }

    public $FastConstructor getConstructor(Constructor constructor) {
        return new $FastConstructor(this, constructor);
    }

    public $FastMethod getMethod(String string, Class[] arrclass) {
        try {
            return this.getMethod(this.type.getMethod(string, arrclass));
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new NoSuchMethodError(noSuchMethodException.getMessage());
        }
    }

    public $FastConstructor getConstructor(Class[] arrclass) {
        try {
            return this.getConstructor(this.type.getConstructor(arrclass));
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new NoSuchMethodError(noSuchMethodException.getMessage());
        }
    }

    public String getName() {
        return this.type.getName();
    }

    public Class getJavaClass() {
        return this.type;
    }

    public String toString() {
        return this.type.toString();
    }

    public int hashCode() {
        return this.type.hashCode();
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof $FastClass)) {
            return false;
        }
        return this.type.equals((($FastClass)object).type);
    }

    public abstract int getIndex(String var1, Class[] var2);

    public abstract int getIndex(Class[] var1);

    public abstract Object invoke(int var1, Object var2, Object[] var3) throws InvocationTargetException;

    public abstract Object newInstance(int var1, Object[] var2) throws InvocationTargetException;

    public abstract int getIndex($Signature var1);

    public abstract int getMaxIndex();

    protected static String getSignatureWithoutReturnType(String string, Class[] arrclass) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(string);
        stringBuffer.append('(');
        for (int i2 = 0; i2 < arrclass.length; ++i2) {
            stringBuffer.append($Type.getDescriptor(arrclass[i2]));
        }
        stringBuffer.append(')');
        return stringBuffer.toString();
    }

    public static class Generator
    extends $AbstractClassGenerator {
        private static final $AbstractClassGenerator.Source SOURCE = new $AbstractClassGenerator.Source($FastClass.class.getName());
        private Class type;

        public Generator() {
            super(SOURCE);
        }

        public void setType(Class class_) {
            this.type = class_;
        }

        public $FastClass create() {
            this.setNamePrefix(this.type.getName());
            return ($FastClass)super.create(this.type.getName());
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.type.getClassLoader();
        }

        protected ProtectionDomain getProtectionDomain() {
            return $ReflectUtils.getProtectionDomain(this.type);
        }

        public void generateClass($ClassVisitor $ClassVisitor) throws Exception {
            new $FastClassEmitter($ClassVisitor, this.getClassName(), this.type);
        }

        protected Object firstInstance(Class class_) {
            return $ReflectUtils.newInstance(class_, new Class[]{Class.class}, new Object[]{this.type});
        }

        protected Object nextInstance(Object object) {
            return object;
        }
    }

}

