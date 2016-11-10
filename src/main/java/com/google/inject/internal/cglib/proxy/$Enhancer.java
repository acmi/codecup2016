/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$AbstractClassGenerator;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import com.google.inject.internal.cglib.core.$CollectionUtils;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$DuplicatesPredicate;
import com.google.inject.internal.cglib.core.$EmitUtils;
import com.google.inject.internal.cglib.core.$KeyFactory;
import com.google.inject.internal.cglib.core.$Local;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$MethodInfoTransformer;
import com.google.inject.internal.cglib.core.$MethodWrapper;
import com.google.inject.internal.cglib.core.$ObjectSwitchCallback;
import com.google.inject.internal.cglib.core.$ProcessSwitchCallback;
import com.google.inject.internal.cglib.core.$ReflectUtils;
import com.google.inject.internal.cglib.core.$RejectModifierPredicate;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$Transformer;
import com.google.inject.internal.cglib.core.$TypeUtils;
import com.google.inject.internal.cglib.core.$VisibilityPredicate;
import com.google.inject.internal.cglib.proxy.$BridgeMethodResolver;
import com.google.inject.internal.cglib.proxy.$Callback;
import com.google.inject.internal.cglib.proxy.$CallbackFilter;
import com.google.inject.internal.cglib.proxy.$CallbackGenerator;
import com.google.inject.internal.cglib.proxy.$CallbackInfo;
import com.google.inject.internal.cglib.proxy.$Factory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class $Enhancer
extends $AbstractClassGenerator {
    private static final $CallbackFilter ALL_ZERO = new $CallbackFilter(){

        public int accept(Method method) {
            return 0;
        }
    };
    private static final $AbstractClassGenerator.Source SOURCE = new $AbstractClassGenerator.Source($Enhancer.class.getName());
    private static final EnhancerKey KEY_FACTORY = (EnhancerKey)((Object)$KeyFactory.create(EnhancerKey.class));
    private static final String BOUND_FIELD = "CGLIB$BOUND";
    private static final String THREAD_CALLBACKS_FIELD = "CGLIB$THREAD_CALLBACKS";
    private static final String STATIC_CALLBACKS_FIELD = "CGLIB$STATIC_CALLBACKS";
    private static final String SET_THREAD_CALLBACKS_NAME = "CGLIB$SET_THREAD_CALLBACKS";
    private static final String SET_STATIC_CALLBACKS_NAME = "CGLIB$SET_STATIC_CALLBACKS";
    private static final String CONSTRUCTED_FIELD = "CGLIB$CONSTRUCTED";
    private static final $Type FACTORY = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$Factory");
    private static final $Type ILLEGAL_STATE_EXCEPTION = $TypeUtils.parseType("IllegalStateException");
    private static final $Type ILLEGAL_ARGUMENT_EXCEPTION = $TypeUtils.parseType("IllegalArgumentException");
    private static final $Type THREAD_LOCAL = $TypeUtils.parseType("ThreadLocal");
    private static final $Type CALLBACK = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$Callback");
    private static final $Type CALLBACK_ARRAY = $Type.getType($Callback[].class);
    private static final $Signature CSTRUCT_NULL = $TypeUtils.parseConstructor("");
    private static final $Signature SET_THREAD_CALLBACKS = new $Signature("CGLIB$SET_THREAD_CALLBACKS", $Type.VOID_TYPE, new $Type[]{CALLBACK_ARRAY});
    private static final $Signature SET_STATIC_CALLBACKS = new $Signature("CGLIB$SET_STATIC_CALLBACKS", $Type.VOID_TYPE, new $Type[]{CALLBACK_ARRAY});
    private static final $Signature NEW_INSTANCE = new $Signature("newInstance", $Constants.TYPE_OBJECT, new $Type[]{CALLBACK_ARRAY});
    private static final $Signature MULTIARG_NEW_INSTANCE = new $Signature("newInstance", $Constants.TYPE_OBJECT, new $Type[]{$Constants.TYPE_CLASS_ARRAY, $Constants.TYPE_OBJECT_ARRAY, CALLBACK_ARRAY});
    private static final $Signature SINGLE_NEW_INSTANCE = new $Signature("newInstance", $Constants.TYPE_OBJECT, new $Type[]{CALLBACK});
    private static final $Signature SET_CALLBACK = new $Signature("setCallback", $Type.VOID_TYPE, new $Type[]{$Type.INT_TYPE, CALLBACK});
    private static final $Signature GET_CALLBACK = new $Signature("getCallback", CALLBACK, new $Type[]{$Type.INT_TYPE});
    private static final $Signature SET_CALLBACKS = new $Signature("setCallbacks", $Type.VOID_TYPE, new $Type[]{CALLBACK_ARRAY});
    private static final $Signature GET_CALLBACKS = new $Signature("getCallbacks", CALLBACK_ARRAY, new $Type[0]);
    private static final $Signature THREAD_LOCAL_GET = $TypeUtils.parseSignature("Object get()");
    private static final $Signature THREAD_LOCAL_SET = $TypeUtils.parseSignature("void set(Object)");
    private static final $Signature BIND_CALLBACKS = $TypeUtils.parseSignature("void CGLIB$BIND_CALLBACKS(Object)");
    private Class[] interfaces;
    private $CallbackFilter filter;
    private $Callback[] callbacks;
    private $Type[] callbackTypes;
    private boolean classOnly;
    private Class superclass;
    private Class[] argumentTypes;
    private Object[] arguments;
    private boolean useFactory = true;
    private Long serialVersionUID;
    private boolean interceptDuringConstruction = true;

    public $Enhancer() {
        super(SOURCE);
    }

    public void setSuperclass(Class class_) {
        if (class_ != null && class_.isInterface()) {
            this.setInterfaces(new Class[]{class_});
        } else {
            this.superclass = class_ != null && class_.equals(Object.class) ? null : class_;
        }
    }

    public void setInterfaces(Class[] arrclass) {
        this.interfaces = arrclass;
    }

    public void setCallbackFilter($CallbackFilter $CallbackFilter) {
        this.filter = $CallbackFilter;
    }

    public void setCallback($Callback $Callback) {
        this.setCallbacks(new $Callback[]{$Callback});
    }

    public void setCallbacks($Callback[] arr$Callback) {
        if (arr$Callback != null && arr$Callback.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }
        this.callbacks = arr$Callback;
    }

    public void setUseFactory(boolean bl) {
        this.useFactory = bl;
    }

    public void setInterceptDuringConstruction(boolean bl) {
        this.interceptDuringConstruction = bl;
    }

    public void setCallbackType(Class class_) {
        this.setCallbackTypes(new Class[]{class_});
    }

    public void setCallbackTypes(Class[] arrclass) {
        if (arrclass != null && arrclass.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }
        this.callbackTypes = $CallbackInfo.determineTypes(arrclass);
    }

    public Object create() {
        this.classOnly = false;
        this.argumentTypes = null;
        return this.createHelper();
    }

    public Object create(Class[] arrclass, Object[] arrobject) {
        this.classOnly = false;
        if (arrclass == null || arrobject == null || arrclass.length != arrobject.length) {
            throw new IllegalArgumentException("Arguments must be non-null and of equal length");
        }
        this.argumentTypes = arrclass;
        this.arguments = arrobject;
        return this.createHelper();
    }

    public Class createClass() {
        this.classOnly = true;
        return (Class)this.createHelper();
    }

    public void setSerialVersionUID(Long l2) {
        this.serialVersionUID = l2;
    }

    private void validate() {
        if (this.classOnly ^ this.callbacks == null) {
            if (this.classOnly) {
                throw new IllegalStateException("createClass does not accept callbacks");
            }
            throw new IllegalStateException("Callbacks are required");
        }
        if (this.classOnly && this.callbackTypes == null) {
            throw new IllegalStateException("Callback types are required");
        }
        if (this.callbacks != null && this.callbackTypes != null) {
            if (this.callbacks.length != this.callbackTypes.length) {
                throw new IllegalStateException("Lengths of callback and callback types array must be the same");
            }
            $Type[] arr$Type = $CallbackInfo.determineTypes(this.callbacks);
            for (int i2 = 0; i2 < arr$Type.length; ++i2) {
                if (arr$Type[i2].equals(this.callbackTypes[i2])) continue;
                String string = String.valueOf(arr$Type[i2]);
                String string2 = String.valueOf(this.callbackTypes[i2]);
                throw new IllegalStateException(new StringBuilder(31 + String.valueOf(string).length() + String.valueOf(string2).length()).append("Callback ").append(string).append(" is not assignable to ").append(string2).toString());
            }
        } else if (this.callbacks != null) {
            this.callbackTypes = $CallbackInfo.determineTypes(this.callbacks);
        }
        if (this.filter == null) {
            if (this.callbackTypes.length > 1) {
                throw new IllegalStateException("Multiple callback types possible but no filter specified");
            }
            this.filter = ALL_ZERO;
        }
        if (this.interfaces != null) {
            for (int i3 = 0; i3 < this.interfaces.length; ++i3) {
                if (this.interfaces[i3] == null) {
                    throw new IllegalStateException("Interfaces cannot be null");
                }
                if (this.interfaces[i3].isInterface()) continue;
                String string = String.valueOf(this.interfaces[i3]);
                throw new IllegalStateException(new StringBuilder(20 + String.valueOf(string).length()).append(string).append(" is not an interface").toString());
            }
        }
    }

    private Object createHelper() {
        this.validate();
        if (this.superclass != null) {
            this.setNamePrefix(this.superclass.getName());
        } else if (this.interfaces != null) {
            this.setNamePrefix(this.interfaces[$ReflectUtils.findPackageProtected(this.interfaces)].getName());
        }
        return super.create(KEY_FACTORY.newInstance(this.superclass != null ? this.superclass.getName() : null, $ReflectUtils.getNames(this.interfaces), this.filter, this.callbackTypes, this.useFactory, this.interceptDuringConstruction, this.serialVersionUID));
    }

    protected ClassLoader getDefaultClassLoader() {
        if (this.superclass != null) {
            return this.superclass.getClassLoader();
        }
        if (this.interfaces != null) {
            return this.interfaces[0].getClassLoader();
        }
        return null;
    }

    protected ProtectionDomain getProtectionDomain() {
        if (this.superclass != null) {
            return $ReflectUtils.getProtectionDomain(this.superclass);
        }
        if (this.interfaces != null) {
            return $ReflectUtils.getProtectionDomain(this.interfaces[0]);
        }
        return null;
    }

    private $Signature rename($Signature $Signature, int n2) {
        String string = $Signature.getName();
        return new $Signature(new StringBuilder(18 + String.valueOf(string).length()).append("CGLIB$").append(string).append("$").append(n2).toString(), $Signature.getDescriptor());
    }

    public static void getMethods(Class class_, Class[] arrclass, List list) {
        $Enhancer.getMethods(class_, arrclass, list, null, null);
    }

    private static void getMethods(Class class_, Class[] arrclass, List list, List list2, Set set) {
        List list3;
        $ReflectUtils.addAllMethods(class_, list);
        List list4 = list3 = list2 != null ? list2 : list;
        if (arrclass != null) {
            for (int i2 = 0; i2 < arrclass.length; ++i2) {
                if (arrclass[i2] == $Factory.class) continue;
                $ReflectUtils.addAllMethods(arrclass[i2], list3);
            }
        }
        if (list2 != null) {
            if (set != null) {
                set.addAll($MethodWrapper.createSet(list2));
            }
            list.addAll(list2);
        }
        $CollectionUtils.filter(list, new $RejectModifierPredicate(8));
        $CollectionUtils.filter(list, new $VisibilityPredicate(class_, true));
        $CollectionUtils.filter(list, new $DuplicatesPredicate());
        $CollectionUtils.filter(list, new $RejectModifierPredicate(16));
    }

    public void generateClass($ClassVisitor $ClassVisitor) throws Exception {
        Class class_;
        Class class_2 = class_ = this.superclass == null ? Object.class : this.superclass;
        if ($TypeUtils.isFinal(class_.getModifiers())) {
            String string = String.valueOf(class_.getName());
            throw new IllegalArgumentException(string.length() != 0 ? "Cannot subclass final class ".concat(string) : new String("Cannot subclass final class "));
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(class_.getDeclaredConstructors()));
        this.filterConstructors(class_, arrayList);
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        final HashSet hashSet = new HashSet();
        $Enhancer.getMethods(class_, this.interfaces, arrayList2, arrayList3, hashSet);
        List list = $CollectionUtils.transform(arrayList2, new $Transformer(){

            public Object transform(Object object) {
                Method method = (Method)object;
                int n2 = 16 | method.getModifiers() & -1025 & -257 & -33;
                if (hashSet.contains($MethodWrapper.create(method))) {
                    n2 = n2 & -5 | 1;
                }
                return $ReflectUtils.getMethodInfo(method, n2);
            }
        });
        $ClassEmitter $ClassEmitter = new $ClassEmitter($ClassVisitor);
        $ClassEmitter.begin_class(46, 1, this.getClassName(), $Type.getType(class_), this.useFactory ? $TypeUtils.add($TypeUtils.getTypes(this.interfaces), FACTORY) : $TypeUtils.getTypes(this.interfaces), "<generated>");
        List list2 = $CollectionUtils.transform(arrayList, $MethodInfoTransformer.getInstance());
        $ClassEmitter.declare_field(2, "CGLIB$BOUND", $Type.BOOLEAN_TYPE, null);
        if (!this.interceptDuringConstruction) {
            $ClassEmitter.declare_field(2, "CGLIB$CONSTRUCTED", $Type.BOOLEAN_TYPE, null);
        }
        $ClassEmitter.declare_field(26, "CGLIB$THREAD_CALLBACKS", THREAD_LOCAL, null);
        $ClassEmitter.declare_field(26, "CGLIB$STATIC_CALLBACKS", CALLBACK_ARRAY, null);
        if (this.serialVersionUID != null) {
            $ClassEmitter.declare_field(26, "serialVersionUID", $Type.LONG_TYPE, this.serialVersionUID);
        }
        for (int i2 = 0; i2 < this.callbackTypes.length; ++i2) {
            $ClassEmitter.declare_field(2, $Enhancer.getCallbackField(i2), this.callbackTypes[i2], null);
        }
        this.emitMethods($ClassEmitter, list, arrayList2);
        this.emitConstructors($ClassEmitter, list2);
        this.emitSetThreadCallbacks($ClassEmitter);
        this.emitSetStaticCallbacks($ClassEmitter);
        this.emitBindCallbacks($ClassEmitter);
        if (this.useFactory) {
            int[] arrn = this.getCallbackKeys();
            this.emitNewInstanceCallbacks($ClassEmitter);
            this.emitNewInstanceCallback($ClassEmitter);
            this.emitNewInstanceMultiarg($ClassEmitter, list2);
            this.emitGetCallback($ClassEmitter, arrn);
            this.emitSetCallback($ClassEmitter, arrn);
            this.emitGetCallbacks($ClassEmitter);
            this.emitSetCallbacks($ClassEmitter);
        }
        $ClassEmitter.end_class();
    }

    protected void filterConstructors(Class class_, List list) {
        $CollectionUtils.filter(list, new $VisibilityPredicate(class_, true));
        if (list.size() == 0) {
            String string = String.valueOf(class_);
            throw new IllegalArgumentException(new StringBuilder(27 + String.valueOf(string).length()).append("No visible constructors in ").append(string).toString());
        }
    }

    protected Object firstInstance(Class class_) throws Exception {
        if (this.classOnly) {
            return class_;
        }
        return this.createUsingReflection(class_);
    }

    protected Object nextInstance(Object object) {
        Class class_;
        Class class_2 = class_ = object instanceof Class ? (Class)object : object.getClass();
        if (this.classOnly) {
            return class_;
        }
        if (object instanceof $Factory) {
            if (this.argumentTypes != null) {
                return (($Factory)object).newInstance(this.argumentTypes, this.arguments, this.callbacks);
            }
            return (($Factory)object).newInstance(this.callbacks);
        }
        return this.createUsingReflection(class_);
    }

    public static void registerCallbacks(Class class_, $Callback[] arr$Callback) {
        $Enhancer.setThreadCallbacks(class_, arr$Callback);
    }

    public static void registerStaticCallbacks(Class class_, $Callback[] arr$Callback) {
        $Enhancer.setCallbacksHelper(class_, arr$Callback, "CGLIB$SET_STATIC_CALLBACKS");
    }

    public static boolean isEnhanced(Class class_) {
        try {
            $Enhancer.getCallbacksSetter(class_, "CGLIB$SET_THREAD_CALLBACKS");
            return true;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return false;
        }
    }

    private static void setThreadCallbacks(Class class_, $Callback[] arr$Callback) {
        $Enhancer.setCallbacksHelper(class_, arr$Callback, "CGLIB$SET_THREAD_CALLBACKS");
    }

    private static void setCallbacksHelper(Class class_, $Callback[] arr$Callback, String string) {
        try {
            Method method = $Enhancer.getCallbacksSetter(class_, string);
            method.invoke(null, arr$Callback);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            String string2 = String.valueOf(class_);
            throw new IllegalArgumentException(new StringBuilder(25 + String.valueOf(string2).length()).append(string2).append(" is not an enhanced class").toString());
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new $CodeGenerationException(illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new $CodeGenerationException(invocationTargetException);
        }
    }

    private static Method getCallbacksSetter(Class class_, String string) throws NoSuchMethodException {
        return class_.getDeclaredMethod(string, $Callback[].class);
    }

    private Object createUsingReflection(Class class_) {
        $Enhancer.setThreadCallbacks(class_, this.callbacks);
        try {
            if (this.argumentTypes != null) {
                Object object = $ReflectUtils.newInstance(class_, this.argumentTypes, this.arguments);
                return object;
            }
            Object object = $ReflectUtils.newInstance(class_);
            return object;
        }
        finally {
            $Enhancer.setThreadCallbacks(class_, null);
        }
    }

    public static Object create(Class class_, $Callback $Callback) {
        $Enhancer $Enhancer = new $Enhancer();
        $Enhancer.setSuperclass(class_);
        $Enhancer.setCallback($Callback);
        return $Enhancer.create();
    }

    public static Object create(Class class_, Class[] arrclass, $Callback $Callback) {
        $Enhancer $Enhancer = new $Enhancer();
        $Enhancer.setSuperclass(class_);
        $Enhancer.setInterfaces(arrclass);
        $Enhancer.setCallback($Callback);
        return $Enhancer.create();
    }

    public static Object create(Class class_, Class[] arrclass, $CallbackFilter $CallbackFilter, $Callback[] arr$Callback) {
        $Enhancer $Enhancer = new $Enhancer();
        $Enhancer.setSuperclass(class_);
        $Enhancer.setInterfaces(arrclass);
        $Enhancer.setCallbackFilter($CallbackFilter);
        $Enhancer.setCallbacks(arr$Callback);
        return $Enhancer.create();
    }

    private void emitConstructors($ClassEmitter $ClassEmitter, List list) {
        boolean bl = false;
        for ($MethodInfo $MethodInfo : list) {
            $CodeEmitter $CodeEmitter = $EmitUtils.begin_method($ClassEmitter, $MethodInfo, 1);
            $CodeEmitter.load_this();
            $CodeEmitter.dup();
            $CodeEmitter.load_args();
            $Signature $Signature = $MethodInfo.getSignature();
            bl = bl || $Signature.getDescriptor().equals("()V");
            $CodeEmitter.super_invoke_constructor($Signature);
            $CodeEmitter.invoke_static_this(BIND_CALLBACKS);
            if (!this.interceptDuringConstruction) {
                $CodeEmitter.load_this();
                $CodeEmitter.push(1);
                $CodeEmitter.putfield("CGLIB$CONSTRUCTED");
            }
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
        }
        if (!this.classOnly && !bl && this.arguments == null) {
            throw new IllegalArgumentException("Superclass has no null constructors but no arguments were given");
        }
    }

    private int[] getCallbackKeys() {
        int[] arrn = new int[this.callbackTypes.length];
        int n2 = 0;
        while (n2 < this.callbackTypes.length) {
            arrn[n2] = n2++;
        }
        return arrn;
    }

    private void emitGetCallback($ClassEmitter $ClassEmitter, int[] arrn) {
        final $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, GET_CALLBACK, null);
        $CodeEmitter.load_this();
        $CodeEmitter.invoke_static_this(BIND_CALLBACKS);
        $CodeEmitter.load_this();
        $CodeEmitter.load_arg(0);
        $CodeEmitter.process_switch(arrn, new $ProcessSwitchCallback(){

            public void processCase(int n2, $Label $Label) {
                $CodeEmitter.getfield($Enhancer.getCallbackField(n2));
                $CodeEmitter.goTo($Label);
            }

            public void processDefault() {
                $CodeEmitter.pop();
                $CodeEmitter.aconst_null();
            }
        });
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private void emitSetCallback($ClassEmitter $ClassEmitter, int[] arrn) {
        final $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, SET_CALLBACK, null);
        $CodeEmitter.load_arg(0);
        $CodeEmitter.process_switch(arrn, new $ProcessSwitchCallback(){

            public void processCase(int n2, $Label $Label) {
                $CodeEmitter.load_this();
                $CodeEmitter.load_arg(1);
                $CodeEmitter.checkcast($Enhancer.this.callbackTypes[n2]);
                $CodeEmitter.putfield($Enhancer.getCallbackField(n2));
                $CodeEmitter.goTo($Label);
            }

            public void processDefault() {
            }
        });
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private void emitSetCallbacks($ClassEmitter $ClassEmitter) {
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, SET_CALLBACKS, null);
        $CodeEmitter.load_this();
        $CodeEmitter.load_arg(0);
        for (int i2 = 0; i2 < this.callbackTypes.length; ++i2) {
            $CodeEmitter.dup2();
            $CodeEmitter.aaload(i2);
            $CodeEmitter.checkcast(this.callbackTypes[i2]);
            $CodeEmitter.putfield($Enhancer.getCallbackField(i2));
        }
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private void emitGetCallbacks($ClassEmitter $ClassEmitter) {
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, GET_CALLBACKS, null);
        $CodeEmitter.load_this();
        $CodeEmitter.invoke_static_this(BIND_CALLBACKS);
        $CodeEmitter.load_this();
        $CodeEmitter.push(this.callbackTypes.length);
        $CodeEmitter.newarray(CALLBACK);
        for (int i2 = 0; i2 < this.callbackTypes.length; ++i2) {
            $CodeEmitter.dup();
            $CodeEmitter.push(i2);
            $CodeEmitter.load_this();
            $CodeEmitter.getfield($Enhancer.getCallbackField(i2));
            $CodeEmitter.aastore();
        }
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private void emitNewInstanceCallbacks($ClassEmitter $ClassEmitter) {
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, NEW_INSTANCE, null);
        $CodeEmitter.load_arg(0);
        $CodeEmitter.invoke_static_this(SET_THREAD_CALLBACKS);
        this.emitCommonNewInstance($CodeEmitter);
    }

    private void emitCommonNewInstance($CodeEmitter $CodeEmitter) {
        $CodeEmitter.new_instance_this();
        $CodeEmitter.dup();
        $CodeEmitter.invoke_constructor_this();
        $CodeEmitter.aconst_null();
        $CodeEmitter.invoke_static_this(SET_THREAD_CALLBACKS);
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private void emitNewInstanceCallback($ClassEmitter $ClassEmitter) {
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, SINGLE_NEW_INSTANCE, null);
        switch (this.callbackTypes.length) {
            case 0: {
                break;
            }
            case 1: {
                $CodeEmitter.push(1);
                $CodeEmitter.newarray(CALLBACK);
                $CodeEmitter.dup();
                $CodeEmitter.push(0);
                $CodeEmitter.load_arg(0);
                $CodeEmitter.aastore();
                $CodeEmitter.invoke_static_this(SET_THREAD_CALLBACKS);
                break;
            }
            default: {
                $CodeEmitter.throw_exception(ILLEGAL_STATE_EXCEPTION, "More than one callback object required");
            }
        }
        this.emitCommonNewInstance($CodeEmitter);
    }

    private void emitNewInstanceMultiarg($ClassEmitter $ClassEmitter, List list) {
        final $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, MULTIARG_NEW_INSTANCE, null);
        $CodeEmitter.load_arg(2);
        $CodeEmitter.invoke_static_this(SET_THREAD_CALLBACKS);
        $CodeEmitter.new_instance_this();
        $CodeEmitter.dup();
        $CodeEmitter.load_arg(0);
        $EmitUtils.constructor_switch($CodeEmitter, list, new $ObjectSwitchCallback(){

            public void processCase(Object object, $Label $Label) {
                $MethodInfo $MethodInfo = ($MethodInfo)object;
                $Type[] arr$Type = $MethodInfo.getSignature().getArgumentTypes();
                for (int i2 = 0; i2 < arr$Type.length; ++i2) {
                    $CodeEmitter.load_arg(1);
                    $CodeEmitter.push(i2);
                    $CodeEmitter.aaload();
                    $CodeEmitter.unbox(arr$Type[i2]);
                }
                $CodeEmitter.invoke_constructor_this($MethodInfo.getSignature());
                $CodeEmitter.goTo($Label);
            }

            public void processDefault() {
                $CodeEmitter.throw_exception(ILLEGAL_ARGUMENT_EXCEPTION, "Constructor not found");
            }
        });
        $CodeEmitter.aconst_null();
        $CodeEmitter.invoke_static_this(SET_THREAD_CALLBACKS);
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private void emitMethods($ClassEmitter $ClassEmitter, List list, List list2) {
        Iterator iterator;
        ArrayList<Object> arrayList;
        Object object2;
        Object object;
        $CallbackGenerator[] arr$CallbackGenerator = $CallbackInfo.getGenerators(this.callbackTypes);
        HashMap<$CallbackGenerator, ArrayList<Object>> hashMap = new HashMap<$CallbackGenerator, ArrayList<Object>>();
        final HashMap<Object, Integer> hashMap2 = new HashMap<Object, Integer>();
        final HashMap<Object, Integer> hashMap3 = new HashMap<Object, Integer>();
        final Map map = $CollectionUtils.getIndexMap(list);
        HashMap hashMap4 = new HashMap();
        Iterator iterator2 = list.iterator();
        Iterator iterator3 = iterator = list2 != null ? list2.iterator() : null;
        while (iterator2.hasNext()) {
            void hashSet;
            object = ($MethodInfo)iterator2.next();
            object2 = iterator != null ? (Method)iterator.next() : null;
            int n2 = this.filter.accept((Method)object2);
            if (n2 >= this.callbackTypes.length) {
                throw new IllegalArgumentException(new StringBuilder(64).append("Callback filter returned an index that is too large: ").append(n2).toString());
            }
            hashMap3.put(object, new Integer(object2 != null ? object2.getModifiers() : object.getModifiers()));
            hashMap2.put(object, new Integer(n2));
            arrayList = (List)hashMap.get(arr$CallbackGenerator[n2]);
            if (arrayList == null) {
                arrayList = new ArrayList<Object>(list.size());
                hashMap.put(arr$CallbackGenerator[n2], arrayList);
            }
            arrayList.add(object);
            if (!$TypeUtils.isBridge(object2.getModifiers())) continue;
            Set set = (Set)hashMap4.get(object2.getDeclaringClass());
            if (set == null) {
                HashSet hashSet2 = new HashSet();
                hashMap4.put(object2.getDeclaringClass(), hashSet2);
            }
            hashSet.add(object.getSignature());
        }
        object = new $BridgeMethodResolver(hashMap4).resolveAll();
        object2 = new HashSet();
        $CodeEmitter $CodeEmitter = $ClassEmitter.getStaticHook();
        $CodeEmitter.new_instance(THREAD_LOCAL);
        $CodeEmitter.dup();
        $CodeEmitter.invoke_constructor(THREAD_LOCAL, CSTRUCT_NULL);
        $CodeEmitter.putfield("CGLIB$THREAD_CALLBACKS");
        arrayList = new Object[1];
        $CallbackGenerator.Context context = new $CallbackGenerator.Context((Map)object){
            final /* synthetic */ Map val$bridgeToTarget;

            public ClassLoader getClassLoader() {
                return $Enhancer.this.getClassLoader();
            }

            public int getOriginalModifiers($MethodInfo $MethodInfo) {
                return (Integer)hashMap3.get($MethodInfo);
            }

            public int getIndex($MethodInfo $MethodInfo) {
                return (Integer)hashMap2.get($MethodInfo);
            }

            public void emitCallback($CodeEmitter $CodeEmitter, int n2) {
                $Enhancer.this.emitCurrentCallback($CodeEmitter, n2);
            }

            public $Signature getImplSignature($MethodInfo $MethodInfo) {
                return $Enhancer.this.rename($MethodInfo.getSignature(), (Integer)map.get($MethodInfo));
            }

            public void emitInvoke($CodeEmitter $CodeEmitter, $MethodInfo $MethodInfo) {
                $Signature $Signature = ($Signature)this.val$bridgeToTarget.get($MethodInfo.getSignature());
                if ($Signature != null) {
                    $CodeEmitter.invoke_virtual_this($Signature);
                    $Type $Type = $MethodInfo.getSignature().getReturnType();
                    if (!$Type.equals($Signature.getReturnType())) {
                        $CodeEmitter.checkcast($Type);
                    }
                } else {
                    $CodeEmitter.super_invoke($MethodInfo.getSignature());
                }
            }

            public $CodeEmitter beginMethod($ClassEmitter $ClassEmitter, $MethodInfo $MethodInfo) {
                $CodeEmitter $CodeEmitter = $EmitUtils.begin_method($ClassEmitter, $MethodInfo);
                if (!$Enhancer.this.interceptDuringConstruction && !$TypeUtils.isAbstract($MethodInfo.getModifiers())) {
                    $Label $Label = $CodeEmitter.make_label();
                    $CodeEmitter.load_this();
                    $CodeEmitter.getfield("CGLIB$CONSTRUCTED");
                    $CodeEmitter.if_jump(154, $Label);
                    $CodeEmitter.load_this();
                    $CodeEmitter.load_args();
                    $CodeEmitter.super_invoke();
                    $CodeEmitter.return_value();
                    $CodeEmitter.mark($Label);
                }
                return $CodeEmitter;
            }
        };
        for (int i2 = 0; i2 < this.callbackTypes.length; ++i2) {
            $CallbackGenerator $CallbackGenerator = arr$CallbackGenerator[i2];
            if (object2.contains($CallbackGenerator)) continue;
            object2.add($CallbackGenerator);
            List list3 = (List)hashMap.get($CallbackGenerator);
            if (list3 == null) continue;
            try {
                $CallbackGenerator.generate($ClassEmitter, context, list3);
                $CallbackGenerator.generateStatic($CodeEmitter, context, list3);
                continue;
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
            catch (Exception exception) {
                throw new $CodeGenerationException(exception);
            }
        }
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private void emitSetThreadCallbacks($ClassEmitter $ClassEmitter) {
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(9, SET_THREAD_CALLBACKS, null);
        $CodeEmitter.getfield("CGLIB$THREAD_CALLBACKS");
        $CodeEmitter.load_arg(0);
        $CodeEmitter.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_SET);
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private void emitSetStaticCallbacks($ClassEmitter $ClassEmitter) {
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(9, SET_STATIC_CALLBACKS, null);
        $CodeEmitter.load_arg(0);
        $CodeEmitter.putfield("CGLIB$STATIC_CALLBACKS");
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private void emitCurrentCallback($CodeEmitter $CodeEmitter, int n2) {
        $CodeEmitter.load_this();
        $CodeEmitter.getfield($Enhancer.getCallbackField(n2));
        $CodeEmitter.dup();
        $Label $Label = $CodeEmitter.make_label();
        $CodeEmitter.ifnonnull($Label);
        $CodeEmitter.pop();
        $CodeEmitter.load_this();
        $CodeEmitter.invoke_static_this(BIND_CALLBACKS);
        $CodeEmitter.load_this();
        $CodeEmitter.getfield($Enhancer.getCallbackField(n2));
        $CodeEmitter.mark($Label);
    }

    private void emitBindCallbacks($ClassEmitter $ClassEmitter) {
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(26, BIND_CALLBACKS, null);
        $Local $Local = $CodeEmitter.make_local();
        $CodeEmitter.load_arg(0);
        $CodeEmitter.checkcast_this();
        $CodeEmitter.store_local($Local);
        $Label $Label = $CodeEmitter.make_label();
        $CodeEmitter.load_local($Local);
        $CodeEmitter.getfield("CGLIB$BOUND");
        $CodeEmitter.if_jump(154, $Label);
        $CodeEmitter.load_local($Local);
        $CodeEmitter.push(1);
        $CodeEmitter.putfield("CGLIB$BOUND");
        $CodeEmitter.getfield("CGLIB$THREAD_CALLBACKS");
        $CodeEmitter.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_GET);
        $CodeEmitter.dup();
        $Label $Label2 = $CodeEmitter.make_label();
        $CodeEmitter.ifnonnull($Label2);
        $CodeEmitter.pop();
        $CodeEmitter.getfield("CGLIB$STATIC_CALLBACKS");
        $CodeEmitter.dup();
        $CodeEmitter.ifnonnull($Label2);
        $CodeEmitter.pop();
        $CodeEmitter.goTo($Label);
        $CodeEmitter.mark($Label2);
        $CodeEmitter.checkcast(CALLBACK_ARRAY);
        $CodeEmitter.load_local($Local);
        $CodeEmitter.swap();
        for (int i2 = this.callbackTypes.length - 1; i2 >= 0; --i2) {
            if (i2 != 0) {
                $CodeEmitter.dup2();
            }
            $CodeEmitter.aaload(i2);
            $CodeEmitter.checkcast(this.callbackTypes[i2]);
            $CodeEmitter.putfield($Enhancer.getCallbackField(i2));
        }
        $CodeEmitter.mark($Label);
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    private static String getCallbackField(int n2) {
        return new StringBuilder(26).append("CGLIB$CALLBACK_").append(n2).toString();
    }

    public static interface EnhancerKey {
        public Object newInstance(String var1, String[] var2, $CallbackFilter var3, $Type[] var4, boolean var5, boolean var6, Long var7);
    }

}

