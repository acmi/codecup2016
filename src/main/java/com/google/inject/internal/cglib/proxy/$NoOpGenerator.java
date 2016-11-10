/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$EmitUtils;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$TypeUtils;
import com.google.inject.internal.cglib.proxy.$CallbackGenerator;
import java.util.List;

class $NoOpGenerator
implements $CallbackGenerator {
    public static final $NoOpGenerator INSTANCE = new $NoOpGenerator();

    $NoOpGenerator() {
    }

    public void generate($ClassEmitter $ClassEmitter, $CallbackGenerator.Context context, List list) {
        for ($MethodInfo $MethodInfo : list) {
            if (!$TypeUtils.isBridge($MethodInfo.getModifiers()) && (!$TypeUtils.isProtected(context.getOriginalModifiers($MethodInfo)) || !$TypeUtils.isPublic($MethodInfo.getModifiers()))) continue;
            $CodeEmitter $CodeEmitter = $EmitUtils.begin_method($ClassEmitter, $MethodInfo);
            $CodeEmitter.load_this();
            $CodeEmitter.load_args();
            context.emitInvoke($CodeEmitter, $MethodInfo);
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
        }
    }

    public void generateStatic($CodeEmitter $CodeEmitter, $CallbackGenerator.Context context, List list) {
    }
}

