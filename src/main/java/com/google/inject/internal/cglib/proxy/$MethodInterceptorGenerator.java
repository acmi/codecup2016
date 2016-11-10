/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$ClassInfo;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$CollectionUtils;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$EmitUtils;
import com.google.inject.internal.cglib.core.$Local;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$ObjectSwitchCallback;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$Transformer;
import com.google.inject.internal.cglib.core.$TypeUtils;
import com.google.inject.internal.cglib.proxy.$CallbackGenerator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class $MethodInterceptorGenerator
implements $CallbackGenerator {
    public static final $MethodInterceptorGenerator INSTANCE = new $MethodInterceptorGenerator();
    static final String EMPTY_ARGS_NAME = "CGLIB$emptyArgs";
    static final String FIND_PROXY_NAME = "CGLIB$findMethodProxy";
    static final Class[] FIND_PROXY_TYPES = new Class[]{$Signature.class};
    private static final $Type ABSTRACT_METHOD_ERROR = $TypeUtils.parseType("AbstractMethodError");
    private static final $Type METHOD = $TypeUtils.parseType("java.lang.reflect.Method");
    private static final $Type REFLECT_UTILS = $TypeUtils.parseType("com.google.inject.internal.cglib.core.$ReflectUtils");
    private static final $Type METHOD_PROXY = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$MethodProxy");
    private static final $Type METHOD_INTERCEPTOR = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$MethodInterceptor");
    private static final $Signature GET_DECLARED_METHODS = $TypeUtils.parseSignature("java.lang.reflect.Method[] getDeclaredMethods()");
    private static final $Signature GET_DECLARING_CLASS = $TypeUtils.parseSignature("Class getDeclaringClass()");
    private static final $Signature FIND_METHODS = $TypeUtils.parseSignature("java.lang.reflect.Method[] findMethods(String[], java.lang.reflect.Method[])");
    private static final $Signature MAKE_PROXY = new $Signature("create", METHOD_PROXY, new $Type[]{$Constants.TYPE_CLASS, $Constants.TYPE_CLASS, $Constants.TYPE_STRING, $Constants.TYPE_STRING, $Constants.TYPE_STRING});
    private static final $Signature INTERCEPT = new $Signature("intercept", $Constants.TYPE_OBJECT, new $Type[]{$Constants.TYPE_OBJECT, METHOD, $Constants.TYPE_OBJECT_ARRAY, METHOD_PROXY});
    private static final $Signature FIND_PROXY = new $Signature("CGLIB$findMethodProxy", METHOD_PROXY, new $Type[]{$Constants.TYPE_SIGNATURE});
    private static final $Signature TO_STRING = $TypeUtils.parseSignature("String toString()");
    private static final $Transformer METHOD_TO_CLASS = new $Transformer(){

        public Object transform(Object object) {
            return (($MethodInfo)object).getClassInfo();
        }
    };
    private static final $Signature CSTRUCT_SIGNATURE = $TypeUtils.parseConstructor("String, String");

    $MethodInterceptorGenerator() {
    }

    private String getMethodField($Signature $Signature) {
        return String.valueOf($Signature.getName()).concat("$Method");
    }

    private String getMethodProxyField($Signature $Signature) {
        return String.valueOf($Signature.getName()).concat("$Proxy");
    }

    public void generate($ClassEmitter $ClassEmitter, $CallbackGenerator.Context context, List list) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for ($MethodInfo $MethodInfo : list) {
            $Signature $Signature = $MethodInfo.getSignature();
            $Signature $Signature2 = context.getImplSignature($MethodInfo);
            String string = this.getMethodField($Signature2);
            String string2 = this.getMethodProxyField($Signature2);
            hashMap.put($Signature.toString(), string2);
            $ClassEmitter.declare_field(26, string, METHOD, null);
            $ClassEmitter.declare_field(26, string2, METHOD_PROXY, null);
            $ClassEmitter.declare_field(26, "CGLIB$emptyArgs", $Constants.TYPE_OBJECT_ARRAY, null);
            $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(16, $Signature2, $MethodInfo.getExceptionTypes());
            $MethodInterceptorGenerator.superHelper($CodeEmitter, $MethodInfo, context);
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
            $CodeEmitter = context.beginMethod($ClassEmitter, $MethodInfo);
            $Label $Label = $CodeEmitter.make_label();
            context.emitCallback($CodeEmitter, context.getIndex($MethodInfo));
            $CodeEmitter.dup();
            $CodeEmitter.ifnull($Label);
            $CodeEmitter.load_this();
            $CodeEmitter.getfield(string);
            if ($Signature.getArgumentTypes().length == 0) {
                $CodeEmitter.getfield("CGLIB$emptyArgs");
            } else {
                $CodeEmitter.create_arg_array();
            }
            $CodeEmitter.getfield(string2);
            $CodeEmitter.invoke_interface(METHOD_INTERCEPTOR, INTERCEPT);
            $CodeEmitter.unbox_or_zero($Signature.getReturnType());
            $CodeEmitter.return_value();
            $CodeEmitter.mark($Label);
            $MethodInterceptorGenerator.superHelper($CodeEmitter, $MethodInfo, context);
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
        }
        this.generateFindProxy($ClassEmitter, hashMap);
    }

    private static void superHelper($CodeEmitter $CodeEmitter, $MethodInfo $MethodInfo, $CallbackGenerator.Context context) {
        if ($TypeUtils.isAbstract($MethodInfo.getModifiers())) {
            $CodeEmitter.throw_exception(ABSTRACT_METHOD_ERROR, String.valueOf($MethodInfo.toString()).concat(" is abstract"));
        } else {
            $CodeEmitter.load_this();
            $CodeEmitter.load_args();
            context.emitInvoke($CodeEmitter, $MethodInfo);
        }
    }

    public void generateStatic($CodeEmitter $CodeEmitter, $CallbackGenerator.Context context, List list) throws Exception {
        $CodeEmitter.push(0);
        $CodeEmitter.newarray();
        $CodeEmitter.putfield("CGLIB$emptyArgs");
        $Local $Local = $CodeEmitter.make_local();
        $Local $Local2 = $CodeEmitter.make_local();
        $EmitUtils.load_class_this($CodeEmitter);
        $CodeEmitter.store_local($Local);
        Map map = $CollectionUtils.bucket(list, METHOD_TO_CLASS);
        for ($ClassInfo $ClassInfo : map.keySet()) {
            $Signature $Signature;
            int n2;
            $MethodInfo $MethodInfo;
            List list2 = (List)map.get($ClassInfo);
            $CodeEmitter.push(2 * list2.size());
            $CodeEmitter.newarray($Constants.TYPE_STRING);
            for (n2 = 0; n2 < list2.size(); ++n2) {
                $MethodInfo = ($MethodInfo)list2.get(n2);
                $Signature = $MethodInfo.getSignature();
                $CodeEmitter.dup();
                $CodeEmitter.push(2 * n2);
                $CodeEmitter.push($Signature.getName());
                $CodeEmitter.aastore();
                $CodeEmitter.dup();
                $CodeEmitter.push(2 * n2 + 1);
                $CodeEmitter.push($Signature.getDescriptor());
                $CodeEmitter.aastore();
            }
            $EmitUtils.load_class($CodeEmitter, $ClassInfo.getType());
            $CodeEmitter.dup();
            $CodeEmitter.store_local($Local2);
            $CodeEmitter.invoke_virtual($Constants.TYPE_CLASS, GET_DECLARED_METHODS);
            $CodeEmitter.invoke_static(REFLECT_UTILS, FIND_METHODS);
            for (n2 = 0; n2 < list2.size(); ++n2) {
                $MethodInfo = ($MethodInfo)list2.get(n2);
                $Signature = $MethodInfo.getSignature();
                $Signature $Signature2 = context.getImplSignature($MethodInfo);
                $CodeEmitter.dup();
                $CodeEmitter.push(n2);
                $CodeEmitter.array_load(METHOD);
                $CodeEmitter.putfield(this.getMethodField($Signature2));
                $CodeEmitter.load_local($Local2);
                $CodeEmitter.load_local($Local);
                $CodeEmitter.push($Signature.getDescriptor());
                $CodeEmitter.push($Signature.getName());
                $CodeEmitter.push($Signature2.getName());
                $CodeEmitter.invoke_static(METHOD_PROXY, MAKE_PROXY);
                $CodeEmitter.putfield(this.getMethodProxyField($Signature2));
            }
            $CodeEmitter.pop();
        }
    }

    public void generateFindProxy($ClassEmitter $ClassEmitter, final Map map) {
        final $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(9, FIND_PROXY, null);
        $CodeEmitter.load_arg(0);
        $CodeEmitter.invoke_virtual($Constants.TYPE_OBJECT, TO_STRING);
        $ObjectSwitchCallback $ObjectSwitchCallback = new $ObjectSwitchCallback(){

            public void processCase(Object object, $Label $Label) {
                $CodeEmitter.getfield((String)map.get(object));
                $CodeEmitter.return_value();
            }

            public void processDefault() {
                $CodeEmitter.aconst_null();
                $CodeEmitter.return_value();
            }
        };
        $EmitUtils.string_switch($CodeEmitter, map.keySet().toArray(new String[0]), 1, $ObjectSwitchCallback);
        $CodeEmitter.end_method();
    }

}

