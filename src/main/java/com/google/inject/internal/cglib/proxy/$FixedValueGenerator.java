/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$TypeUtils;
import com.google.inject.internal.cglib.proxy.$CallbackGenerator;
import java.util.List;

class $FixedValueGenerator
implements $CallbackGenerator {
    public static final $FixedValueGenerator INSTANCE = new $FixedValueGenerator();
    private static final $Type FIXED_VALUE = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$FixedValue");
    private static final $Signature LOAD_OBJECT = $TypeUtils.parseSignature("Object loadObject()");

    $FixedValueGenerator() {
    }

    public void generate($ClassEmitter $ClassEmitter, $CallbackGenerator.Context context, List list) {
        for ($MethodInfo $MethodInfo : list) {
            $CodeEmitter $CodeEmitter = context.beginMethod($ClassEmitter, $MethodInfo);
            context.emitCallback($CodeEmitter, context.getIndex($MethodInfo));
            $CodeEmitter.invoke_interface(FIXED_VALUE, LOAD_OBJECT);
            $CodeEmitter.unbox_or_zero($CodeEmitter.getReturnType());
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
        }
    }

    public void generateStatic($CodeEmitter $CodeEmitter, $CallbackGenerator.Context context, List list) {
    }
}

