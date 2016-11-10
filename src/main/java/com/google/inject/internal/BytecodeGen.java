/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.inject.internal.cglib.reflect.
 */
package com.google.inject.internal;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.internal.InternalFlags;
import com.google.inject.internal.cglib.core.$DefaultNamingPolicy;
import com.google.inject.internal.cglib.core.$NamingPolicy;
import com.google.inject.internal.cglib.core.$Predicate;
import com.google.inject.internal.cglib.core.$VisibilityPredicate;
import com.google.inject.internal.cglib.proxy.$Enhancer;
import com.google.inject.internal.cglib.reflect.$FastClass;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BytecodeGen {
    static final Logger logger = Logger.getLogger(BytecodeGen.class.getName());
    static final ClassLoader GUICE_CLASS_LOADER = BytecodeGen.canonicalize(BytecodeGen.class.getClassLoader());
    static final String GUICE_INTERNAL_PACKAGE = BytecodeGen.class.getName().replaceFirst("\\.internal\\..*$", ".internal");
    static final String CGLIB_PACKAGE = $Enhancer.class.getName().replaceFirst("\\.cglib\\..*$", ".cglib");
    static final $NamingPolicy FASTCLASS_NAMING_POLICY = new $DefaultNamingPolicy(){

        @Override
        protected String getTag() {
            return "ByGuice";
        }

        @Override
        public String getClassName(String string, String string2, Object object, $Predicate predicate) {
            return super.getClassName(string, "FastClass", object, predicate);
        }
    };
    static final $NamingPolicy ENHANCER_NAMING_POLICY = new $DefaultNamingPolicy(){

        @Override
        protected String getTag() {
            return "ByGuice";
        }

        @Override
        public String getClassName(String string, String string2, Object object, $Predicate predicate) {
            return super.getClassName(string, "Enhancer", object, predicate);
        }
    };
    private static final LoadingCache<ClassLoader, ClassLoader> CLASS_LOADER_CACHE;

    private static ClassLoader canonicalize(ClassLoader classLoader) {
        return classLoader != null ? classLoader : SystemBridgeHolder.SYSTEM_BRIDGE.getParent();
    }

    public static ClassLoader getClassLoader(Class<?> class_) {
        return BytecodeGen.getClassLoader(class_, class_.getClassLoader());
    }

    private static ClassLoader getClassLoader(Class<?> class_, ClassLoader classLoader) {
        if (InternalFlags.getCustomClassLoadingOption() == InternalFlags.CustomClassLoadingOption.OFF) {
            return classLoader;
        }
        if (class_.getName().startsWith("java.")) {
            return GUICE_CLASS_LOADER;
        }
        if ((classLoader = BytecodeGen.canonicalize(classLoader)) == GUICE_CLASS_LOADER || classLoader instanceof BridgeClassLoader) {
            return classLoader;
        }
        if (Visibility.forType(class_) == Visibility.PUBLIC) {
            if (classLoader != SystemBridgeHolder.SYSTEM_BRIDGE.getParent()) {
                return CLASS_LOADER_CACHE.getUnchecked(classLoader);
            }
            return SystemBridgeHolder.SYSTEM_BRIDGE;
        }
        return classLoader;
    }

    public static .FastClass newFastClassForMember(Member member) {
        return BytecodeGen.newFastClassForMember(member.getDeclaringClass(), member);
    }

    public static .FastClass newFastClassForMember(Class<?> class_, Member member) {
        if (!new $VisibilityPredicate(class_, false).evaluate(member)) {
            return null;
        }
        boolean bl = BytecodeGen.isPubliclyCallable(member);
        if (!bl && !BytecodeGen.hasSameVersionOfCglib(class_.getClassLoader())) {
            return null;
        }
        .FastClass.Generator generator = new .FastClass.Generator();
        if (bl) {
            generator.setClassLoader(BytecodeGen.getClassLoader(class_));
        }
        generator.setType(class_);
        generator.setNamingPolicy(FASTCLASS_NAMING_POLICY);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Loading " + class_ + " FastClass with " + generator.getClassLoader());
        }
        return generator.create();
    }

    private static boolean hasSameVersionOfCglib(ClassLoader classLoader) {
        Class<.FastClass> class_ = .FastClass.class;
        try {
            return classLoader.loadClass(class_.getName()) == class_;
        }
        catch (ClassNotFoundException classNotFoundException) {
            return false;
        }
    }

    private static boolean isPubliclyCallable(Member member) {
        Class<?>[] arrclass;
        if (!Modifier.isPublic(member.getModifiers())) {
            return false;
        }
        if (member instanceof Constructor) {
            arrclass = ((Constructor)member).getParameterTypes();
        } else {
            Class<?>[] arrclass2 = (Class<?>[])member;
            if (!Modifier.isPublic(arrclass2.getReturnType().getModifiers())) {
                return false;
            }
            arrclass = arrclass2.getParameterTypes();
        }
        for (Class class_ : arrclass) {
            if (Modifier.isPublic(class_.getModifiers())) continue;
            return false;
        }
        return true;
    }

    public static $Enhancer newEnhancer(Class<?> class_, Visibility visibility) {
        $Enhancer $Enhancer = new $Enhancer();
        $Enhancer.setSuperclass(class_);
        $Enhancer.setUseFactory(false);
        if (visibility == Visibility.PUBLIC) {
            $Enhancer.setClassLoader(BytecodeGen.getClassLoader(class_));
        }
        $Enhancer.setNamingPolicy(ENHANCER_NAMING_POLICY);
        logger.fine("Loading " + class_ + " Enhancer with " + $Enhancer.getClassLoader());
        return $Enhancer;
    }

    static {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder().weakKeys().weakValues();
        if (InternalFlags.getCustomClassLoadingOption() == InternalFlags.CustomClassLoadingOption.OFF) {
            cacheBuilder.maximumSize(0);
        }
        CLASS_LOADER_CACHE = cacheBuilder.build(new CacheLoader<ClassLoader, ClassLoader>(){

            @Override
            public ClassLoader load(final ClassLoader classLoader) {
                BytecodeGen.logger.fine("Creating a bridge ClassLoader for " + classLoader);
                return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

                    @Override
                    public ClassLoader run() {
                        return new BridgeClassLoader(classLoader);
                    }
                });
            }

        });
    }

    private static class BridgeClassLoader
    extends ClassLoader {
        BridgeClassLoader() {
        }

        BridgeClassLoader(ClassLoader classLoader) {
            super(classLoader);
        }

        @Override
        protected Class<?> loadClass(String string, boolean bl) throws ClassNotFoundException {
            if (string.startsWith("sun.reflect")) {
                return SystemBridgeHolder.SYSTEM_BRIDGE.classicLoadClass(string, bl);
            }
            if (string.startsWith(BytecodeGen.GUICE_INTERNAL_PACKAGE) || string.startsWith(BytecodeGen.CGLIB_PACKAGE)) {
                if (null == BytecodeGen.GUICE_CLASS_LOADER) {
                    return SystemBridgeHolder.SYSTEM_BRIDGE.classicLoadClass(string, bl);
                }
                try {
                    Class class_ = BytecodeGen.GUICE_CLASS_LOADER.loadClass(string);
                    if (bl) {
                        this.resolveClass(class_);
                    }
                    return class_;
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            return this.classicLoadClass(string, bl);
        }

        Class<?> classicLoadClass(String string, boolean bl) throws ClassNotFoundException {
            return super.loadClass(string, bl);
        }
    }

    public static enum Visibility {
        PUBLIC{

            @Override
            public Visibility and(Visibility visibility) {
                return visibility;
            }
        }
        ,
        SAME_PACKAGE{

            @Override
            public Visibility and(Visibility visibility) {
                return this;
            }
        };
        

        private Visibility() {
        }

        public static Visibility forMember(Member member) {
            Class<?>[] arrclass;
            if ((member.getModifiers() & 5) == 0) {
                return SAME_PACKAGE;
            }
            if (member instanceof Constructor) {
                arrclass = ((Constructor)member).getParameterTypes();
            } else {
                Class<?>[] arrclass2 = (Class<?>[])member;
                if (Visibility.forType(arrclass2.getReturnType()) == SAME_PACKAGE) {
                    return SAME_PACKAGE;
                }
                arrclass = arrclass2.getParameterTypes();
            }
            for (Class class_ : arrclass) {
                if (Visibility.forType(class_) != SAME_PACKAGE) continue;
                return SAME_PACKAGE;
            }
            return PUBLIC;
        }

        public static Visibility forType(Class<?> class_) {
            return (class_.getModifiers() & 5) != 0 ? PUBLIC : SAME_PACKAGE;
        }

        public abstract Visibility and(Visibility var1);

    }

    private static class SystemBridgeHolder {
        static final BridgeClassLoader SYSTEM_BRIDGE = new BridgeClassLoader();

        private SystemBridgeHolder() {
        }
    }

}

