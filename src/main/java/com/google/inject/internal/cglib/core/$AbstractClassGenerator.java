/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$ClassReader;
import com.google.inject.internal.cglib.core.$ClassGenerator;
import com.google.inject.internal.cglib.core.$ClassNameReader;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import com.google.inject.internal.cglib.core.$DefaultGeneratorStrategy;
import com.google.inject.internal.cglib.core.$DefaultNamingPolicy;
import com.google.inject.internal.cglib.core.$GeneratorStrategy;
import com.google.inject.internal.cglib.core.$NamingPolicy;
import com.google.inject.internal.cglib.core.$Predicate;
import com.google.inject.internal.cglib.core.$ReflectUtils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class $AbstractClassGenerator
implements $ClassGenerator {
    private static final Object NAME_KEY = new Object();
    private static final ThreadLocal CURRENT = new ThreadLocal<T>();
    private $GeneratorStrategy strategy = $DefaultGeneratorStrategy.INSTANCE;
    private $NamingPolicy namingPolicy = $DefaultNamingPolicy.INSTANCE;
    private Source source;
    private ClassLoader classLoader;
    private String namePrefix;
    private Object key;
    private boolean useCache = true;
    private String className;
    private boolean attemptLoad;

    protected $AbstractClassGenerator(Source source) {
        this.source = source;
    }

    protected void setNamePrefix(String string) {
        this.namePrefix = string;
    }

    protected final String getClassName() {
        if (this.className == null) {
            this.className = this.getClassName(this.getClassLoader());
        }
        return this.className;
    }

    private String getClassName(ClassLoader classLoader) {
        final Set set = this.getClassNameCache(classLoader);
        return this.namingPolicy.getClassName(this.namePrefix, this.source.name, this.key, new $Predicate(){

            public boolean evaluate(Object object) {
                return set.contains(object);
            }
        });
    }

    private Set getClassNameCache(ClassLoader classLoader) {
        return (Set)((Map)this.source.cache.get(classLoader)).get(NAME_KEY);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setNamingPolicy($NamingPolicy $NamingPolicy) {
        if ($NamingPolicy == null) {
            $NamingPolicy = $DefaultNamingPolicy.INSTANCE;
        }
        this.namingPolicy = $NamingPolicy;
    }

    public $NamingPolicy getNamingPolicy() {
        return this.namingPolicy;
    }

    public void setUseCache(boolean bl) {
        this.useCache = bl;
    }

    public boolean getUseCache() {
        return this.useCache;
    }

    public void setAttemptLoad(boolean bl) {
        this.attemptLoad = bl;
    }

    public boolean getAttemptLoad() {
        return this.attemptLoad;
    }

    public void setStrategy($GeneratorStrategy $GeneratorStrategy) {
        if ($GeneratorStrategy == null) {
            $GeneratorStrategy = $DefaultGeneratorStrategy.INSTANCE;
        }
        this.strategy = $GeneratorStrategy;
    }

    public $GeneratorStrategy getStrategy() {
        return this.strategy;
    }

    public static $AbstractClassGenerator getCurrent() {
        return ($AbstractClassGenerator)CURRENT.get();
    }

    public ClassLoader getClassLoader() {
        ClassLoader classLoader = this.classLoader;
        if (classLoader == null) {
            classLoader = this.getDefaultClassLoader();
        }
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        if (classLoader == null) {
            throw new IllegalStateException("Cannot determine classloader");
        }
        return classLoader;
    }

    protected abstract ClassLoader getDefaultClassLoader();

    protected ProtectionDomain getProtectionDomain() {
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected Object create(Object object) {
        try {
            Class class_ = null;
            Source source = this.source;
            synchronized (source) {
                Reference reference;
                ClassLoader classLoader = this.getClassLoader();
                ProtectionDomain protectionDomain = this.getProtectionDomain();
                HashMap<Object, Object> hashMap = null;
                hashMap = (HashMap<Object, Object>)this.source.cache.get(classLoader);
                if (hashMap == null) {
                    hashMap = new HashMap<Object, Object>();
                    hashMap.put(NAME_KEY, new HashSet<E>());
                    this.source.cache.put(classLoader, hashMap);
                } else if (this.useCache) {
                    reference = (Reference)hashMap.get(object);
                    class_ = reference == null ? null : reference.get();
                }
                if (class_ != null) {
                    return this.firstInstance(class_);
                }
                reference = CURRENT.get();
                CURRENT.set(this);
                try {
                    Object object2;
                    this.key = object;
                    if (this.attemptLoad) {
                        try {
                            class_ = classLoader.loadClass(this.getClassName());
                        }
                        catch (ClassNotFoundException classNotFoundException) {
                            // empty catch block
                        }
                    }
                    if (class_ == null) {
                        object2 = this.strategy.generate(this);
                        String string = $ClassNameReader.getClassName(new $ClassReader((byte[])object2));
                        this.getClassNameCache(classLoader).add(string);
                        class_ = protectionDomain == null ? $ReflectUtils.defineClass(string, (byte[])object2, classLoader) : $ReflectUtils.defineClass(string, (byte[])object2, classLoader, protectionDomain);
                    }
                    if (this.useCache) {
                        hashMap.put(object, new WeakReference<Class<?>>(class_));
                    }
                    object2 = this.firstInstance(class_);
                    return object2;
                }
                finally {
                    CURRENT.set(reference);
                }
            }
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Error error) {
            throw error;
        }
        catch (Exception exception) {
            throw new $CodeGenerationException(exception);
        }
    }

    protected abstract Object firstInstance(Class var1) throws Exception;

    protected abstract Object nextInstance(Object var1) throws Exception;

    protected static class Source {
        String name;
        Map cache = new WeakHashMap();

        public Source(String string) {
            this.name = string;
        }
    }

}

