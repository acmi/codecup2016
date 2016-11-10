/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$ClassInfo;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$TypeUtils;
import com.google.inject.internal.cglib.proxy.$CallbackGenerator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

class $LazyLoaderGenerator
implements $CallbackGenerator {
    public static final $LazyLoaderGenerator INSTANCE = new $LazyLoaderGenerator();
    private static final $Signature LOAD_OBJECT = $TypeUtils.parseSignature("Object loadObject()");
    private static final $Type LAZY_LOADER = $TypeUtils.parseType("com.google.inject.internal.cglib.proxy.$LazyLoader");

    $LazyLoaderGenerator() {
    }

    public void generate($ClassEmitter $ClassEmitter, $CallbackGenerator.Context context, List list) {
        $CodeEmitter $CodeEmitter;
        HashSet<Integer> hashSet = new HashSet<Integer>();
        for ($MethodInfo $MethodInfo : list) {
            if ($TypeUtils.isProtected($MethodInfo.getModifiers())) continue;
            int n2 = context.getIndex($MethodInfo);
            hashSet.add(new Integer(n2));
            $CodeEmitter = context.beginMethod($ClassEmitter, $MethodInfo);
            $CodeEmitter.load_this();
            $CodeEmitter.dup();
            $CodeEmitter.invoke_virtual_this(this.loadMethod(n2));
            $CodeEmitter.checkcast($MethodInfo.getClassInfo().getType());
            $CodeEmitter.load_args();
            $CodeEmitter.invoke($MethodInfo);
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
        }
        Iterator iterator = hashSet.iterator();
        while (iterator.hasNext()) {
            int n3 = (Integer)iterator.next();
            String string = new StringBuilder(29).append("CGLIB$LAZY_LOADER_").append(n3).toString();
            $ClassEmitter.declare_field(2, string, $Constants.TYPE_OBJECT, null);
            $CodeEmitter = $ClassEmitter.begin_method(50, this.loadMethod(n3), null);
            $CodeEmitter.load_this();
            $CodeEmitter.getfield(string);
            $CodeEmitter.dup();
            $Label $Label = $CodeEmitter.make_label();
            $CodeEmitter.ifnonnull($Label);
            $CodeEmitter.pop();
            $CodeEmitter.load_this();
            context.emitCallback($CodeEmitter, n3);
            $CodeEmitter.invoke_interface(LAZY_LOADER, LOAD_OBJECT);
            $CodeEmitter.dup_x1();
            $CodeEmitter.putfield(string);
            $CodeEmitter.mark($Label);
            $CodeEmitter.return_value();
            $CodeEmitter.end_method();
        }
    }

    private $Signature loadMethod(int n2) {
        return new $Signature(new StringBuilder(30).append("CGLIB$LOAD_PRIVATE_").append(n2).toString(), $Constants.TYPE_OBJECT, $Constants.TYPES_EMPTY);
    }

    public void generateStatic($CodeEmitter $CodeEmitter, $CallbackGenerator.Context context, List list) {
    }
}

