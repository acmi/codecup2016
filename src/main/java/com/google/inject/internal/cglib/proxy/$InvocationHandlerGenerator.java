/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$Block;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$EmitUtils;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$TypeUtils;
import com.google.inject.internal.cglib.proxy.$CallbackGenerator;
import java.util.List;

class $InvocationHandlerGenerator
implements $CallbackGenerator {
    public static final $InvocationHandlerGenerator INSTANCE = new $InvocationHandlerGenerator();
    private static final $Type INVOCATION_HANDLER = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$InvocationHandler");
    private static final $Type UNDECLARED_THROWABLE_EXCEPTION = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$UndeclaredThrowableException");
    private static final $Type METHOD = $TypeUtils.parseType("java.lang.reflect.Method");
    private static final $Signature INVOKE = $TypeUtils.parseSignature("Object invoke(Object, java.lang.reflect.Method, Object[])");

    $InvocationHandlerGenerator() {
    }

    public void generate($ClassEmitter $ClassEmitter, $CallbackGenerator.Context context, List list) {
        for ($MethodInfo $MethodInfo : list) {
            $Signature $Signature = context.getImplSignature($MethodInfo);
            $ClassEmitter.declare_field(26, $Signature.getName(), METHOD, null);
            $CodeEmitter $CodeEmitter = context.beginMethod($ClassEmitter, $MethodInfo);
            $Block $Block = $CodeEmitter.begin_block();
            context.emitCallback($CodeEmitter, context.getIndex($MethodInfo));
            $CodeEmitter.load_this();
            $CodeEmitter.getfield($Signature.getName());
            $CodeEmitter.create_arg_array();
            $CodeEmitter.invoke_interface(INVOCATION_HANDLER, INVOKE);
            $CodeEmitter.unbox($MethodInfo.getSignature().getReturnType());
            $CodeEmitter.return_value();
            $Block.end();
            $EmitUtils.wrap_undeclared_throwable($CodeEmitter, $Block, $MethodInfo.getExceptionTypes(), UNDECLARED_THROWABLE_EXCEPTION);
            $CodeEmitter.end_method();
        }
    }

    public void generateStatic($CodeEmitter $CodeEmitter, $CallbackGenerator.Context context, List list) {
        for ($MethodInfo $MethodInfo : list) {
            $EmitUtils.load_method($CodeEmitter, $MethodInfo);
            $CodeEmitter.putfield(context.getImplSignature($MethodInfo).getName());
        }
    }
}

