/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$Attribute;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$ClassInfo;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$TypeUtils;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class $ReflectUtils {
    private static final Map primitives = new HashMap(8);
    private static final Map transforms = new HashMap(8);
    private static final ClassLoader defaultLoader = $ReflectUtils.class.getClassLoader();
    private static Method DEFINE_CLASS;
    private static final ProtectionDomain PROTECTION_DOMAIN;
    private static final String[] CGLIB_PACKAGES;

    private $ReflectUtils() {
    }

    public static ProtectionDomain getProtectionDomain(final Class class_) {
        if (class_ == null) {
            return null;
        }
        return (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                return class_.getProtectionDomain();
            }
        });
    }

    public static $Type[] getExceptionTypes(Member member) {
        if (member instanceof Method) {
            return $TypeUtils.getTypes(((Method)member).getExceptionTypes());
        }
        if (member instanceof Constructor) {
            return $TypeUtils.getTypes(((Constructor)member).getExceptionTypes());
        }
        throw new IllegalArgumentException("Cannot get exception types of a field");
    }

    public static $Signature getSignature(Member member) {
        if (member instanceof Method) {
            return new $Signature(member.getName(), $Type.getMethodDescriptor((Method)member));
        }
        if (member instanceof Constructor) {
            $Type[] arr$Type = $TypeUtils.getTypes(((Constructor)member).getParameterTypes());
            return new $Signature("<init>", $Type.getMethodDescriptor($Type.VOID_TYPE, arr$Type));
        }
        throw new IllegalArgumentException("Cannot get signature of a field");
    }

    public static Constructor findConstructor(String string) {
        return $ReflectUtils.findConstructor(string, defaultLoader);
    }

    public static Constructor findConstructor(String string, ClassLoader classLoader) {
        try {
            int n2 = string.indexOf(40);
            String string2 = string.substring(0, n2).trim();
            return $ReflectUtils.getClass(string2, classLoader).getConstructor($ReflectUtils.parseTypes(string, classLoader));
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new $CodeGenerationException(classNotFoundException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new $CodeGenerationException(noSuchMethodException);
        }
    }

    public static Method findMethod(String string) {
        return $ReflectUtils.findMethod(string, defaultLoader);
    }

    public static Method findMethod(String string, ClassLoader classLoader) {
        try {
            int n2 = string.indexOf(40);
            int n3 = string.lastIndexOf(46, n2);
            String string2 = string.substring(0, n3).trim();
            String string3 = string.substring(n3 + 1, n2).trim();
            return $ReflectUtils.getClass(string2, classLoader).getDeclaredMethod(string3, $ReflectUtils.parseTypes(string, classLoader));
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new $CodeGenerationException(classNotFoundException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new $CodeGenerationException(noSuchMethodException);
        }
    }

    private static Class[] parseTypes(String string, ClassLoader classLoader) throws ClassNotFoundException {
        int n2;
        int n3 = string.indexOf(40);
        int n4 = string.indexOf(41, n3);
        ArrayList<String> arrayList = new ArrayList<String>();
        int n5 = n3 + 1;
        while ((n2 = string.indexOf(44, n5)) >= 0) {
            arrayList.add(string.substring(n5, n2).trim());
            n5 = n2 + 1;
        }
        if (n5 < n4) {
            arrayList.add(string.substring(n5, n4).trim());
        }
        Class[] arrclass = new Class[arrayList.size()];
        for (int i2 = 0; i2 < arrclass.length; ++i2) {
            arrclass[i2] = $ReflectUtils.getClass((String)arrayList.get(i2), classLoader);
        }
        return arrclass;
    }

    private static Class getClass(String string, ClassLoader classLoader) throws ClassNotFoundException {
        return $ReflectUtils.getClass(string, classLoader, CGLIB_PACKAGES);
    }

    private static Class getClass(String string, ClassLoader classLoader, String[] arrstring) throws ClassNotFoundException {
        String string2;
        String string3;
        String string4 = string;
        int n2 = 0;
        int n3 = 0;
        while ((n3 = string.indexOf("[]", n3) + 1) > 0) {
            ++n2;
        }
        StringBuffer stringBuffer = new StringBuffer(string.length() - n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuffer.append('[');
        }
        string = string.substring(0, string.length() - 2 * n2);
        if (n2 > 0) {
            string3 = String.valueOf(stringBuffer);
            string2 = new StringBuilder(1 + String.valueOf(string3).length()).append(string3).append("L").toString();
        } else {
            string2 = "";
        }
        String string5 = string2;
        string3 = n2 > 0 ? ";" : "";
        try {
            String string6 = string;
            return Class.forName(new StringBuilder(0 + String.valueOf(string5).length() + String.valueOf(string6).length() + String.valueOf(string3).length()).append(string5).append(string6).append(string3).toString(), false, classLoader);
        }
        catch (ClassNotFoundException classNotFoundException) {
            for (int i3 = 0; i3 < arrstring.length; ++i3) {
                try {
                    String string7 = arrstring[i3];
                    String string8 = string;
                    return Class.forName(new StringBuilder(1 + String.valueOf(string5).length() + String.valueOf(string7).length() + String.valueOf(string8).length() + String.valueOf(string3).length()).append(string5).append(string7).append(".").append(string8).append(string3).toString(), false, classLoader);
                }
                catch (ClassNotFoundException classNotFoundException2) {
                    continue;
                }
            }
            if (n2 == 0) {
                Class class_ = (Class)primitives.get(string);
                if (class_ != null) {
                    return class_;
                }
            } else {
                String string9 = (String)transforms.get(string);
                if (string9 != null) {
                    try {
                        String string10 = String.valueOf(stringBuffer);
                        return Class.forName(new StringBuilder(0 + String.valueOf(string10).length() + String.valueOf(string9).length()).append(string10).append(string9).toString(), false, classLoader);
                    }
                    catch (ClassNotFoundException classNotFoundException3) {
                        // empty catch block
                    }
                }
            }
            throw new ClassNotFoundException(string4);
        }
    }

    public static Object newInstance(Class class_) {
        return $ReflectUtils.newInstance(class_, $Constants.EMPTY_CLASS_ARRAY, null);
    }

    public static Object newInstance(Class class_, Class[] arrclass, Object[] arrobject) {
        return $ReflectUtils.newInstance($ReflectUtils.getConstructor(class_, arrclass), arrobject);
    }

    public static Object newInstance(Constructor constructor, Object[] arrobject) {
        boolean bl = constructor.isAccessible();
        try {
            Object t2;
            constructor.setAccessible(true);
            Object t3 = t2 = constructor.newInstance(arrobject);
            return t3;
        }
        catch (InstantiationException instantiationException) {
            throw new $CodeGenerationException(instantiationException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new $CodeGenerationException(illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new $CodeGenerationException(invocationTargetException.getTargetException());
        }
        finally {
            constructor.setAccessible(bl);
        }
    }

    public static Constructor getConstructor(Class class_, Class[] arrclass) {
        try {
            Constructor constructor = class_.getDeclaredConstructor(arrclass);
            constructor.setAccessible(true);
            return constructor;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new $CodeGenerationException(noSuchMethodException);
        }
    }

    public static String[] getNames(Class[] arrclass) {
        if (arrclass == null) {
            return null;
        }
        String[] arrstring = new String[arrclass.length];
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            arrstring[i2] = arrclass[i2].getName();
        }
        return arrstring;
    }

    public static Class[] getClasses(Object[] arrobject) {
        Class[] arrclass = new Class[arrobject.length];
        for (int i2 = 0; i2 < arrobject.length; ++i2) {
            arrclass[i2] = arrobject[i2].getClass();
        }
        return arrclass;
    }

    public static Method findNewInstance(Class class_) {
        Method method = $ReflectUtils.findInterfaceMethod(class_);
        if (!method.getName().equals("newInstance")) {
            String string = String.valueOf(class_);
            throw new IllegalArgumentException(new StringBuilder(27 + String.valueOf(string).length()).append(string).append(" missing newInstance method").toString());
        }
        return method;
    }

    public static Method[] getPropertyMethods(PropertyDescriptor[] arrpropertyDescriptor, boolean bl, boolean bl2) {
        HashSet<Method> hashSet = new HashSet<Method>();
        for (int i2 = 0; i2 < arrpropertyDescriptor.length; ++i2) {
            PropertyDescriptor propertyDescriptor = arrpropertyDescriptor[i2];
            if (bl) {
                hashSet.add(propertyDescriptor.getReadMethod());
            }
            if (!bl2) continue;
            hashSet.add(propertyDescriptor.getWriteMethod());
        }
        hashSet.remove(null);
        return hashSet.toArray(new Method[hashSet.size()]);
    }

    public static PropertyDescriptor[] getBeanProperties(Class class_) {
        return $ReflectUtils.getPropertiesHelper(class_, true, true);
    }

    public static PropertyDescriptor[] getBeanGetters(Class class_) {
        return $ReflectUtils.getPropertiesHelper(class_, true, false);
    }

    public static PropertyDescriptor[] getBeanSetters(Class class_) {
        return $ReflectUtils.getPropertiesHelper(class_, false, true);
    }

    private static PropertyDescriptor[] getPropertiesHelper(Class class_, boolean bl, boolean bl2) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(class_, Object.class);
            PropertyDescriptor[] arrpropertyDescriptor = beanInfo.getPropertyDescriptors();
            if (bl && bl2) {
                return arrpropertyDescriptor;
            }
            ArrayList<PropertyDescriptor> arrayList = new ArrayList<PropertyDescriptor>(arrpropertyDescriptor.length);
            for (int i2 = 0; i2 < arrpropertyDescriptor.length; ++i2) {
                PropertyDescriptor propertyDescriptor = arrpropertyDescriptor[i2];
                if ((!bl || propertyDescriptor.getReadMethod() == null) && (!bl2 || propertyDescriptor.getWriteMethod() == null)) continue;
                arrayList.add(propertyDescriptor);
            }
            return arrayList.toArray(new PropertyDescriptor[arrayList.size()]);
        }
        catch (IntrospectionException introspectionException) {
            throw new $CodeGenerationException(introspectionException);
        }
    }

    public static Method findDeclaredMethod(Class class_, String string, Class[] arrclass) throws NoSuchMethodException {
        for (Class class_2 = class_; class_2 != null; class_2 = class_2.getSuperclass()) {
            try {
                return class_2.getDeclaredMethod(string, arrclass);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                continue;
            }
        }
        throw new NoSuchMethodException(string);
    }

    public static List addAllMethods(Class class_, List list) {
        list.addAll(Arrays.asList(class_.getDeclaredMethods()));
        Class class_2 = class_.getSuperclass();
        if (class_2 != null) {
            $ReflectUtils.addAllMethods(class_2, list);
        }
        Class<?>[] arrclass = class_.getInterfaces();
        for (int i2 = 0; i2 < arrclass.length; ++i2) {
            $ReflectUtils.addAllMethods(arrclass[i2], list);
        }
        return list;
    }

    public static List addAllInterfaces(Class class_, List list) {
        Class class_2 = class_.getSuperclass();
        if (class_2 != null) {
            list.addAll(Arrays.asList(class_.getInterfaces()));
            $ReflectUtils.addAllInterfaces(class_2, list);
        }
        return list;
    }

    public static Method findInterfaceMethod(Class class_) {
        if (!class_.isInterface()) {
            String string = String.valueOf(class_);
            throw new IllegalArgumentException(new StringBuilder(20 + String.valueOf(string).length()).append(string).append(" is not an interface").toString());
        }
        Method[] arrmethod = class_.getDeclaredMethods();
        if (arrmethod.length != 1) {
            String string = String.valueOf(class_);
            throw new IllegalArgumentException(new StringBuilder(30 + String.valueOf(string).length()).append("expecting exactly 1 method in ").append(string).toString());
        }
        return arrmethod[0];
    }

    public static Class defineClass(String string, byte[] arrby, ClassLoader classLoader) throws Exception {
        return $ReflectUtils.defineClass(string, arrby, classLoader, PROTECTION_DOMAIN);
    }

    public static Class defineClass(String string, byte[] arrby, ClassLoader classLoader, ProtectionDomain protectionDomain) throws Exception {
        Object[] arrobject = new Object[]{string, arrby, new Integer(0), new Integer(arrby.length), protectionDomain};
        Class class_ = (Class)DEFINE_CLASS.invoke(classLoader, arrobject);
        Class.forName(string, true, classLoader);
        return class_;
    }

    public static int findPackageProtected(Class[] arrclass) {
        for (int i2 = 0; i2 < arrclass.length; ++i2) {
            if (Modifier.isPublic(arrclass[i2].getModifiers())) continue;
            return i2;
        }
        return 0;
    }

    public static $MethodInfo getMethodInfo(final Member member, final int n2) {
        final $Signature $Signature = $ReflectUtils.getSignature(member);
        return new $MethodInfo(){
            private $ClassInfo ci;

            public $ClassInfo getClassInfo() {
                if (this.ci == null) {
                    this.ci = $ReflectUtils.getClassInfo(member.getDeclaringClass());
                }
                return this.ci;
            }

            public int getModifiers() {
                return n2;
            }

            public $Signature getSignature() {
                return $Signature;
            }

            public $Type[] getExceptionTypes() {
                return $ReflectUtils.getExceptionTypes(member);
            }

            public $Attribute getAttribute() {
                return null;
            }
        };
    }

    public static $MethodInfo getMethodInfo(Member member) {
        return $ReflectUtils.getMethodInfo(member, member.getModifiers());
    }

    public static $ClassInfo getClassInfo(final Class class_) {
        final $Type $Type = $Type.getType(class_);
        final $Type $Type2 = class_.getSuperclass() == null ? null : $Type.getType(class_.getSuperclass());
        return new $ClassInfo(){

            public $Type getType() {
                return $Type;
            }

            public $Type getSuperType() {
                return $Type2;
            }

            public $Type[] getInterfaces() {
                return $TypeUtils.getTypes(class_.getInterfaces());
            }

            public int getModifiers() {
                return class_.getModifiers();
            }
        };
    }

    public static Method[] findMethods(String[] arrstring, Method[] arrmethod) {
        HashMap<String, Method> hashMap = new HashMap<String, Method>();
        for (int i2 = 0; i2 < arrmethod.length; ++i2) {
            Method method = arrmethod[i2];
            String string = String.valueOf(method.getName());
            String string2 = String.valueOf($Type.getMethodDescriptor(method));
            hashMap.put(string2.length() != 0 ? string.concat(string2) : new String(string), method);
        }
        Method[] arrmethod2 = new Method[arrstring.length / 2];
        for (int i3 = 0; i3 < arrmethod2.length; ++i3) {
            String string = String.valueOf(arrstring[i3 * 2]);
            String string3 = String.valueOf(arrstring[i3 * 2 + 1]);
            arrmethod2[i3] = (Method)hashMap.get(string3.length() != 0 ? string.concat(string3) : new String(string));
            if (arrmethod2[i3] != null) continue;
        }
        return arrmethod2;
    }

    static {
        PROTECTION_DOMAIN = $ReflectUtils.getProtectionDomain($ReflectUtils.class);
        AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                try {
                    Class class_ = Class.forName("java.lang.ClassLoader");
                    DEFINE_CLASS = class_.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
                    DEFINE_CLASS.setAccessible(true);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw new $CodeGenerationException(classNotFoundException);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    throw new $CodeGenerationException(noSuchMethodException);
                }
                return null;
            }
        });
        CGLIB_PACKAGES = new String[]{"java.lang"};
        primitives.put("byte", Byte.TYPE);
        primitives.put("char", Character.TYPE);
        primitives.put("double", Double.TYPE);
        primitives.put("float", Float.TYPE);
        primitives.put("int", Integer.TYPE);
        primitives.put("long", Long.TYPE);
        primitives.put("short", Short.TYPE);
        primitives.put("boolean", Boolean.TYPE);
        transforms.put("byte", "B");
        transforms.put("char", "C");
        transforms.put("double", "D");
        transforms.put("float", "F");
        transforms.put("int", "I");
        transforms.put("long", "J");
        transforms.put("short", "S");
        transforms.put("boolean", "Z");
    }

}

