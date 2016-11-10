/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.cglib.core.$AbstractClassGenerator;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import com.google.inject.internal.cglib.core.$GeneratorStrategy;
import com.google.inject.internal.cglib.core.$NamingPolicy;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.proxy.$MethodInterceptorGenerator;
import com.google.inject.internal.cglib.reflect.$FastClass;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class $MethodProxy {
    private $Signature sig1;
    private $Signature sig2;
    private CreateInfo createInfo;
    private final Object initLock = new Object();
    private volatile FastClassInfo fastClassInfo;

    public static $MethodProxy create(Class class_, Class class_2, String string, String string2, String string3) {
        $MethodProxy $MethodProxy = new $MethodProxy();
        $MethodProxy.sig1 = new $Signature(string2, string);
        $MethodProxy.sig2 = new $Signature(string3, string);
        $MethodProxy.createInfo = new CreateInfo(class_, class_2);
        return $MethodProxy;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void init() {
        if (this.fastClassInfo == null) {
            Object object = this.initLock;
            synchronized (object) {
                if (this.fastClassInfo == null) {
                    CreateInfo createInfo = this.createInfo;
                    FastClassInfo fastClassInfo = new FastClassInfo();
                    fastClassInfo.f1 = $MethodProxy.helper(createInfo, createInfo.c1);
                    fastClassInfo.f2 = $MethodProxy.helper(createInfo, createInfo.c2);
                    fastClassInfo.i1 = fastClassInfo.f1.getIndex(this.sig1);
                    fastClassInfo.i2 = fastClassInfo.f2.getIndex(this.sig2);
                    this.fastClassInfo = fastClassInfo;
                    this.createInfo = null;
                }
            }
        }
    }

    private static $FastClass helper(CreateInfo createInfo, Class class_) {
        $FastClass.Generator generator = new $FastClass.Generator();
        generator.setType(class_);
        generator.setClassLoader(createInfo.c2.getClassLoader());
        generator.setNamingPolicy(createInfo.namingPolicy);
        generator.setStrategy(createInfo.strategy);
        generator.setAttemptLoad(createInfo.attemptLoad);
        return generator.create();
    }

    private $MethodProxy() {
    }

    public $Signature getSignature() {
        return this.sig1;
    }

    public String getSuperName() {
        return this.sig2.getName();
    }

    public int getSuperIndex() {
        this.init();
        return this.fastClassInfo.i2;
    }

    $FastClass getFastClass() {
        this.init();
        return this.fastClassInfo.f1;
    }

    $FastClass getSuperFastClass() {
        this.init();
        return this.fastClassInfo.f2;
    }

    public static $MethodProxy find(Class class_, $Signature $Signature) {
        try {
            Method method = class_.getDeclaredMethod("CGLIB$findMethodProxy", $MethodInterceptorGenerator.FIND_PROXY_TYPES);
            return ($MethodProxy)method.invoke(null, $Signature);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            String string = String.valueOf(class_);
            throw new IllegalArgumentException(new StringBuilder(39 + String.valueOf(string).length()).append("Class ").append(string).append(" does not use a MethodInterceptor").toString());
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new $CodeGenerationException(illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new $CodeGenerationException(invocationTargetException);
        }
    }

    public Object invoke(Object object, Object[] arrobject) throws Throwable {
        try {
            this.init();
            FastClassInfo fastClassInfo = this.fastClassInfo;
            return fastClassInfo.f1.invoke(fastClassInfo.i1, object, arrobject);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getTargetException();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            if (this.fastClassInfo.i1 < 0) {
                String string = String.valueOf(this.sig1);
                throw new IllegalArgumentException(new StringBuilder(18 + String.valueOf(string).length()).append("Protected method: ").append(string).toString());
            }
            throw illegalArgumentException;
        }
    }

    public Object invokeSuper(Object object, Object[] arrobject) throws Throwable {
        try {
            this.init();
            FastClassInfo fastClassInfo = this.fastClassInfo;
            return fastClassInfo.f2.invoke(fastClassInfo.i2, object, arrobject);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getTargetException();
        }
    }

    private static class CreateInfo {
        Class c1;
        Class c2;
        $NamingPolicy namingPolicy;
        $GeneratorStrategy strategy;
        boolean attemptLoad;

        public CreateInfo(Class class_, Class class_2) {
            this.c1 = class_;
            this.c2 = class_2;
            $AbstractClassGenerator $AbstractClassGenerator = $AbstractClassGenerator.getCurrent();
            if ($AbstractClassGenerator != null) {
                this.namingPolicy = $AbstractClassGenerator.getNamingPolicy();
                this.strategy = $AbstractClassGenerator.getStrategy();
                this.attemptLoad = $AbstractClassGenerator.getAttemptLoad();
            }
        }
    }

    private static class FastClassInfo {
        $FastClass f1;
        $FastClass f2;
        int i1;
        int i2;

        private FastClassInfo() {
        }
    }

}

