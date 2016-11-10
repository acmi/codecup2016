/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.reflection;

import com.codeforces.commons.reflection.MethodSignature;
import com.codeforces.commons.reflection.Name;
import com.codeforces.commons.text.StringUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.StringUtils;

public class ReflectionUtil {
    private static final ConcurrentMap<Class<?>, Map<String, List<Field>>> fieldsByNameByClass = new ConcurrentHashMap();
    private static final ConcurrentMap<Class<?>, Map<MethodSignature, Method>> publicMethodBySignatureByClass = new ConcurrentHashMap();
    private static final Map<Class<?>, Object> fastClassCache = new ConcurrentHashMap();
    private static final Map<Class<?>, Map<String, Object>> gettersCache = new ConcurrentHashMap();
    private static final Map<Class<?>, Map<String, Object>> settersCache = new ConcurrentHashMap();

    public static <T> Object getDeepValue(T t2, String string) {
        return ReflectionUtil.getDeepValue(t2, string, false, false, false);
    }

    public static <T> Object getDeepValue(T t2, String string, boolean bl, boolean bl2, boolean bl3) {
        Object object = null;
        Object object2 = t2;
        for (String string2 : StringUtil.split(string, '.')) {
            Method method /* !! */ ;
            if (StringUtil.isBlank(string2)) {
                throw new IllegalArgumentException("Field name can not be neither 'null' nor blank.");
            }
            boolean bl4 = false;
            List<Field> list = ReflectionUtil.getFieldsByNameMap(object2.getClass()).get(string2);
            if (list != null && !list.isEmpty()) {
                object = ReflectionUtil.getFieldValue(list.get(0), object2);
                bl4 = true;
            }
            if (!bl4 && !bl) {
                method /* !! */  = ReflectionUtil.findPublicGetter(string2, object2.getClass());
                try {
                    if (method /* !! */  != null) {
                        object = method /* !! */ .invoke(object2, new Object[0]);
                        bl4 = true;
                    }
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw new IllegalStateException("This exception is unexpected because method should be public.", illegalAccessException);
                }
                catch (InvocationTargetException invocationTargetException) {
                    if (invocationTargetException.getTargetException() instanceof RuntimeException) {
                        throw (RuntimeException)invocationTargetException.getTargetException();
                    }
                    throw new IllegalStateException("This type of exception is unexpected.", invocationTargetException);
                }
            }
            if (!bl4 && !bl2 && object2 instanceof Map) {
                object = ((Map)object2).get(string2);
                bl4 = true;
            }
            if (!bl4 && !bl3) {
                try {
                    List list2;
                    method /* !! */  = (Method)Integer.parseInt(string2);
                    if (object2 instanceof List) {
                        list2 = (List)object2;
                        object = list2.get((int)(method /* !! */  < 0 ? (Method)(list2.size() + method /* !! */ ) : method /* !! */ ));
                        bl4 = true;
                    } else if (object2 instanceof Collection) {
                        list2 = (Collection)object2;
                        Iterator iterator = list2.iterator();
                        if (method /* !! */  < 0) {
                            method /* !! */  += list2.size();
                        }
                        for (int i2 = 0; i2 <= method /* !! */ ; ++i2) {
                            object = iterator.next();
                        }
                        bl4 = true;
                    }
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
            if (!bl4) {
                throw new IllegalArgumentException(String.format("Can't find '%s' in %s.", string2, object2.getClass()));
            }
            if (object == null) break;
            object2 = object;
        }
        return object;
    }

    public static Method findPublicGetter(String string, Class<?> class_) {
        Map<MethodSignature, Method> map = ReflectionUtil.getPublicMethodBySignatureMap(class_);
        String string2 = StringUtils.capitalize(string);
        Method method = map.get(new MethodSignature("is" + string2, new Class[0]));
        if (method != null && method.getReturnType() == Boolean.TYPE && ReflectionUtil.throwsOnlyRuntimeExceptions(method)) {
            return method;
        }
        method = map.get(new MethodSignature("get" + string2, new Class[0]));
        if (method != null && method.getReturnType() != Void.TYPE && method.getReturnType() != Void.class && ReflectionUtil.throwsOnlyRuntimeExceptions(method)) {
            return method;
        }
        method = map.get(new MethodSignature(string, new Class[0]));
        if (method != null && method.getReturnType() != Void.TYPE && method.getReturnType() != Void.class && ReflectionUtil.throwsOnlyRuntimeExceptions(method)) {
            return method;
        }
        return null;
    }

    public static Map<String, List<Field>> getFieldsByNameMap(Class class_) {
        Map<String, List<Field>> map = fieldsByNameByClass.get(class_);
        if (map == null) {
            map = new LinkedHashMap<String, List<Field>>();
            Class class_2 = class_.getSuperclass();
            if (class_2 != null) {
                map.putAll(ReflectionUtil.getFieldsByNameMap(class_2));
            }
            for (Field field : class_.getDeclaredFields()) {
                if (field.isEnumConstant() || Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) continue;
                field.setAccessible(true);
                Name name = field.getAnnotation(Name.class);
                String string = name == null ? field.getName() : name.value();
                List<Field> list = map.get(string);
                if (list == null) {
                    list = new ArrayList<Field>(1);
                    list.add(field);
                } else {
                    List<Field> list2 = list;
                    list = new ArrayList<Field>(list2.size() + 1);
                    list.add(field);
                    list.addAll(list2);
                }
                map.put(string, Collections.unmodifiableList(list));
            }
            fieldsByNameByClass.putIfAbsent(class_, Collections.unmodifiableMap(map));
            return fieldsByNameByClass.get(class_);
        }
        return map;
    }

    private static boolean throwsOnlyRuntimeExceptions(Method method) {
        for (Class class_ : method.getExceptionTypes()) {
            if (RuntimeException.class.isAssignableFrom(class_)) continue;
            return false;
        }
        return true;
    }

    public static Map<MethodSignature, Method> getPublicMethodBySignatureMap(Class class_) {
        Map<MethodSignature, Method> map = publicMethodBySignatureByClass.get(class_);
        if (map == null) {
            Method[] arrmethod = class_.getMethods();
            int n2 = arrmethod.length;
            map = new LinkedHashMap<MethodSignature, Method>(n2);
            for (int i2 = 0; i2 < n2; ++i2) {
                Method method = arrmethod[i2];
                Name name = method.getAnnotation(Name.class);
                String string = name == null ? method.getName() : name.value();
                method.setAccessible(true);
                map.put(new MethodSignature(string, method.getParameterTypes()), method);
            }
            publicMethodBySignatureByClass.putIfAbsent(class_, Collections.unmodifiableMap(map));
            return publicMethodBySignatureByClass.get(class_);
        }
        return map;
    }

    private static Object getFieldValue(Field field, Object object) {
        try {
            return field.get(object);
        }
        catch (IllegalAccessException illegalAccessException) {
            Name name = field.getAnnotation(Name.class);
            String string = name == null ? field.getName() : name.value();
            throw new IllegalArgumentException("Can't get value of inaccessible field '" + string + "'.", illegalAccessException);
        }
    }
}

