/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$ClassInfo;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$TypeUtils;
import com.google.inject.internal.cglib.proxy.$CallbackGenerator;
import java.util.List;

class $DispatcherGenerator
implements $CallbackGenerator {
    public static final $DispatcherGenerator INSTANCE = new $DispatcherGenerator(false);
    public static final $DispatcherGenerator PROXY_REF_INSTANCE = new $DispatcherGenerator(true);
    private static final $Type DISPATCHER = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$Dispatcher");
    private static final $Type PROXY_REF_DISPATCHER = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$ProxyRefDispatcher");
    private static final $Signature LOAD_OBJECT = $TypeUtils.parseSignature("Object loadObject()");
    private static final $Signature PROXY_REF_LOAD_OBJECT = $TypeUtils.parseSignature("Object loadObject(Object)");
    private boolean proxyRef;

    private $DispatcherGenerator(boolean bl) {
        this.proxyRef = bl;
    }

    public void generate($ClassEmitter $ClassEmitter, $CallbackGenerator.Context context, List list) {
        for ($MethodInfo $MethodInfo : list) {
            if ($TypeUtils.isProtected($MethodInfo.getModifiers())) continue;
            $CodeEmitter $CodeEmitter = context.beginMethod($ClassEmitter, $MethodInfo);
            context.emitCallback($CodeEmitter, context.getIndex($MethodInfo));
            if (this.proxyRef) {
                $CodeEmitter.load_this();
                $CodeEmitter.invoke_interface(PROXY_REF_DISPATCHER, PROXY_REF_LOAD_OBJECT);
            } else {
                $CodeEmitter.invoke_interface(DISPATCHER, LOAD_OBJECT);
            }
            $CodeEmitter.checkcast($MethodInfo.getClassInfo().getType());
            $CodeEmitter.load_args();
            $CodeEmitter.invoke($MethodInfo);
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
        }
    }

    public void generateStatic($CodeEmitter $CodeEmitter, $CallbackGenerator.Context context, List list) {
    }
}

