/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$AbstractClassGenerator;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$Customizer;
import com.google.inject.internal.cglib.core.$EmitUtils;
import com.google.inject.internal.cglib.core.$ReflectUtils;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$TypeUtils;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public abstract class $KeyFactory {
    private static final $Signature GET_NAME = $TypeUtils.parseSignature("String getName()");
    private static final $Signature GET_CLASS = $TypeUtils.parseSignature("Class getClass()");
    private static final $Signature HASH_CODE = $TypeUtils.parseSignature("int hashCode()");
    private static final $Signature EQUALS = $TypeUtils.parseSignature("boolean equals(Object)");
    private static final $Signature TO_STRING = $TypeUtils.parseSignature("String toString()");
    private static final $Signature APPEND_STRING = $TypeUtils.parseSignature("StringBuffer append(String)");
    private static final $Type KEY_FACTORY = $TypeUtils.parseType("com.google.inject.internal.cglib.core.$KeyFactory");
    private static final int[] PRIMES = new int[]{11, 73, 179, 331, 521, 787, 1213, 1823, 2609, 3691, 5189, 7247, 10037, 13931, 19289, 26627, 36683, 50441, 69403, 95401, 131129, 180179, 247501, 340057, 467063, 641371, 880603, 1209107, 1660097, 2279161, 3129011, 4295723, 5897291, 8095873, 11114263, 15257791, 20946017, 28754629, 39474179, 54189869, 74391461, 102123817, 140194277, 192456917, 264202273, 362693231, 497900099, 683510293, 938313161, 1288102441, 1768288259};
    public static final $Customizer CLASS_BY_NAME = new $Customizer(){

        public void customize($CodeEmitter $CodeEmitter, $Type $Type) {
            if ($Type.equals($Constants.TYPE_CLASS)) {
                $CodeEmitter.invoke_virtual($Constants.TYPE_CLASS, GET_NAME);
            }
        }
    };
    public static final $Customizer OBJECT_BY_CLASS = new $Customizer(){

        public void customize($CodeEmitter $CodeEmitter, $Type $Type) {
            $CodeEmitter.invoke_virtual($Constants.TYPE_OBJECT, GET_CLASS);
        }
    };

    protected $KeyFactory() {
    }

    public static $KeyFactory create(Class class_) {
        return $KeyFactory.create(class_, null);
    }

    public static $KeyFactory create(Class class_, $Customizer $Customizer) {
        return $KeyFactory.create(class_.getClassLoader(), class_, $Customizer);
    }

    public static $KeyFactory create(ClassLoader classLoader, Class class_, $Customizer $Customizer) {
        Generator generator = new Generator();
        generator.setInterface(class_);
        generator.setCustomizer($Customizer);
        generator.setClassLoader(classLoader);
        return generator.create();
    }

    public static class Generator
    extends $AbstractClassGenerator {
        private static final $AbstractClassGenerator.Source SOURCE = new $AbstractClassGenerator.Source($KeyFactory.class.getName());
        private Class keyInterface;
        private $Customizer customizer;
        private int constant;
        private int multiplier;

        public Generator() {
            super(SOURCE);
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.keyInterface.getClassLoader();
        }

        protected ProtectionDomain getProtectionDomain() {
            return $ReflectUtils.getProtectionDomain(this.keyInterface);
        }

        public void setCustomizer($Customizer $Customizer) {
            this.customizer = $Customizer;
        }

        public void setInterface(Class class_) {
            this.keyInterface = class_;
        }

        public $KeyFactory create() {
            this.setNamePrefix(this.keyInterface.getName());
            return ($KeyFactory)super.create(this.keyInterface.getName());
        }

        public void setHashConstant(int n2) {
            this.constant = n2;
        }

        public void setHashMultiplier(int n2) {
            this.multiplier = n2;
        }

        protected Object firstInstance(Class class_) {
            return $ReflectUtils.newInstance(class_);
        }

        protected Object nextInstance(Object object) {
            return object;
        }

        public void generateClass($ClassVisitor $ClassVisitor) {
            int n2;
            int n3;
            $ClassEmitter $ClassEmitter = new $ClassEmitter($ClassVisitor);
            Method method = $ReflectUtils.findNewInstance(this.keyInterface);
            if (!method.getReturnType().equals(Object.class)) {
                throw new IllegalArgumentException("newInstance method must return Object");
            }
            $Type[] arr$Type = $TypeUtils.getTypes(method.getParameterTypes());
            $ClassEmitter.begin_class(46, 1, this.getClassName(), KEY_FACTORY, new $Type[]{$Type.getType(this.keyInterface)}, "<generated>");
            $EmitUtils.null_constructor($ClassEmitter);
            $EmitUtils.factory_method($ClassEmitter, $ReflectUtils.getSignature(method));
            int n4 = 0;
            $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, $TypeUtils.parseConstructor(arr$Type), null);
            $CodeEmitter.load_this();
            $CodeEmitter.super_invoke_constructor();
            $CodeEmitter.load_this();
            for (n3 = 0; n3 < arr$Type.length; ++n3) {
                n4 += arr$Type[n3].hashCode();
                $ClassEmitter.declare_field(18, this.getFieldName(n3), arr$Type[n3], null);
                $CodeEmitter.dup();
                $CodeEmitter.load_arg(n3);
                $CodeEmitter.putfield(this.getFieldName(n3));
            }
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
            $CodeEmitter = $ClassEmitter.begin_method(1, HASH_CODE, null);
            n3 = this.constant != 0 ? this.constant : PRIMES[Math.abs(n4) % PRIMES.length];
            int n5 = this.multiplier != 0 ? this.multiplier : PRIMES[Math.abs(n4 * 13) % PRIMES.length];
            $CodeEmitter.push(n3);
            for (int i2 = 0; i2 < arr$Type.length; ++i2) {
                $CodeEmitter.load_this();
                $CodeEmitter.getfield(this.getFieldName(i2));
                $EmitUtils.hash_code($CodeEmitter, arr$Type[i2], n5, this.customizer);
            }
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
            $CodeEmitter = $ClassEmitter.begin_method(1, EQUALS, null);
            $Label $Label = $CodeEmitter.make_label();
            $CodeEmitter.load_arg(0);
            $CodeEmitter.instance_of_this();
            $CodeEmitter.if_jump(153, $Label);
            for (n2 = 0; n2 < arr$Type.length; ++n2) {
                $CodeEmitter.load_this();
                $CodeEmitter.getfield(this.getFieldName(n2));
                $CodeEmitter.load_arg(0);
                $CodeEmitter.checkcast_this();
                $CodeEmitter.getfield(this.getFieldName(n2));
                $EmitUtils.not_equals($CodeEmitter, arr$Type[n2], $Label, this.customizer);
            }
            $CodeEmitter.push(1);
            $CodeEmitter.return_value();
            $CodeEmitter.mark($Label);
            $CodeEmitter.push(0);
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
            $CodeEmitter = $ClassEmitter.begin_method(1, TO_STRING, null);
            $CodeEmitter.new_instance($Constants.TYPE_STRING_BUFFER);
            $CodeEmitter.dup();
            $CodeEmitter.invoke_constructor($Constants.TYPE_STRING_BUFFER);
            for (n2 = 0; n2 < arr$Type.length; ++n2) {
                if (n2 > 0) {
                    $CodeEmitter.push(", ");
                    $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_STRING);
                }
                $CodeEmitter.load_this();
                $CodeEmitter.getfield(this.getFieldName(n2));
                $EmitUtils.append_string($CodeEmitter, arr$Type[n2], $EmitUtils.DEFAULT_DELIMITERS, this.customizer);
            }
            $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, TO_STRING);
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
            $ClassEmitter.end_class();
        }

        private String getFieldName(int n2) {
            return new StringBuilder(17).append("FIELD_").append(n2).toString();
        }
    }

}

