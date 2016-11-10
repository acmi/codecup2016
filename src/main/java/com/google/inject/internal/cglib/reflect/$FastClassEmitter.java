/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.reflect;

import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$Block;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$CollectionUtils;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$DuplicatesPredicate;
import com.google.inject.internal.cglib.core.$EmitUtils;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$MethodInfoTransformer;
import com.google.inject.internal.cglib.core.$ObjectSwitchCallback;
import com.google.inject.internal.cglib.core.$ProcessSwitchCallback;
import com.google.inject.internal.cglib.core.$ReflectUtils;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$Transformer;
import com.google.inject.internal.cglib.core.$TypeUtils;
import com.google.inject.internal.cglib.core.$VisibilityPredicate;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class $FastClassEmitter
extends $ClassEmitter {
    private static final $Signature CSTRUCT_CLASS = $TypeUtils.parseConstructor("Class");
    private static final $Signature METHOD_GET_INDEX = $TypeUtils.parseSignature("int getIndex(String, Class[])");
    private static final $Signature SIGNATURE_GET_INDEX = new $Signature("getIndex", $Type.INT_TYPE, new $Type[]{$Constants.TYPE_SIGNATURE});
    private static final $Signature TO_STRING = $TypeUtils.parseSignature("String toString()");
    private static final $Signature CONSTRUCTOR_GET_INDEX = $TypeUtils.parseSignature("int getIndex(Class[])");
    private static final $Signature INVOKE = $TypeUtils.parseSignature("Object invoke(int, Object, Object[])");
    private static final $Signature NEW_INSTANCE = $TypeUtils.parseSignature("Object newInstance(int, Object[])");
    private static final $Signature GET_MAX_INDEX = $TypeUtils.parseSignature("int getMaxIndex()");
    private static final $Signature GET_SIGNATURE_WITHOUT_RETURN_TYPE = $TypeUtils.parseSignature("String getSignatureWithoutReturnType(String, Class[])");
    private static final $Type FAST_CLASS = $TypeUtils.parseType("com.google.inject.internal.cglib.reflect.$FastClass");
    private static final $Type ILLEGAL_ARGUMENT_EXCEPTION = $TypeUtils.parseType("IllegalArgumentException");
    private static final $Type INVOCATION_TARGET_EXCEPTION = $TypeUtils.parseType("java.lang.reflect.InvocationTargetException");
    private static final $Type[] INVOCATION_TARGET_EXCEPTION_ARRAY = new $Type[]{INVOCATION_TARGET_EXCEPTION};
    private static final int TOO_MANY_METHODS = 100;

    public $FastClassEmitter($ClassVisitor $ClassVisitor, String string, Class class_) {
        super($ClassVisitor);
        $Type $Type = $Type.getType(class_);
        this.begin_class(46, 1, string, FAST_CLASS, null, "<generated>");
        $CodeEmitter $CodeEmitter = this.begin_method(1, CSTRUCT_CLASS, null);
        $CodeEmitter.load_this();
        $CodeEmitter.load_args();
        $CodeEmitter.super_invoke_constructor(CSTRUCT_CLASS);
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
        $VisibilityPredicate $VisibilityPredicate = new $VisibilityPredicate(class_, false);
        List list = $ReflectUtils.addAllMethods(class_, new ArrayList());
        $CollectionUtils.filter(list, $VisibilityPredicate);
        $CollectionUtils.filter(list, new $DuplicatesPredicate());
        ArrayList arrayList = new ArrayList(Arrays.asList(class_.getDeclaredConstructors()));
        $CollectionUtils.filter(arrayList, $VisibilityPredicate);
        this.emitIndexBySignature(list);
        this.emitIndexByClassArray(list);
        $CodeEmitter = this.begin_method(1, CONSTRUCTOR_GET_INDEX, null);
        $CodeEmitter.load_args();
        List list2 = $CollectionUtils.transform(arrayList, $MethodInfoTransformer.getInstance());
        $EmitUtils.constructor_switch($CodeEmitter, list2, new GetIndexCallback($CodeEmitter, list2));
        $CodeEmitter.end_method();
        $CodeEmitter = this.begin_method(1, INVOKE, INVOCATION_TARGET_EXCEPTION_ARRAY);
        $CodeEmitter.load_arg(1);
        $CodeEmitter.checkcast($Type);
        $CodeEmitter.load_arg(0);
        $FastClassEmitter.invokeSwitchHelper($CodeEmitter, list, 2, $Type);
        $CodeEmitter.end_method();
        $CodeEmitter = this.begin_method(1, NEW_INSTANCE, INVOCATION_TARGET_EXCEPTION_ARRAY);
        $CodeEmitter.new_instance($Type);
        $CodeEmitter.dup();
        $CodeEmitter.load_arg(0);
        $FastClassEmitter.invokeSwitchHelper($CodeEmitter, arrayList, 1, $Type);
        $CodeEmitter.end_method();
        $CodeEmitter = this.begin_method(1, GET_MAX_INDEX, null);
        $CodeEmitter.push(list.size() - 1);
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
        this.end_class();
    }

    private void emitIndexBySignature(List list) {
        $CodeEmitter $CodeEmitter = this.begin_method(1, SIGNATURE_GET_INDEX, null);
        List list2 = $CollectionUtils.transform(list, new $Transformer(){

            public Object transform(Object object) {
                return $ReflectUtils.getSignature((Method)object).toString();
            }
        });
        $CodeEmitter.load_arg(0);
        $CodeEmitter.invoke_virtual($Constants.TYPE_OBJECT, TO_STRING);
        this.signatureSwitchHelper($CodeEmitter, list2);
        $CodeEmitter.end_method();
    }

    private void emitIndexByClassArray(List list) {
        $CodeEmitter $CodeEmitter = this.begin_method(1, METHOD_GET_INDEX, null);
        if (list.size() > 100) {
            List list2 = $CollectionUtils.transform(list, new $Transformer(){

                public Object transform(Object object) {
                    String string = $ReflectUtils.getSignature((Method)object).toString();
                    return string.substring(0, string.lastIndexOf(41) + 1);
                }
            });
            $CodeEmitter.load_args();
            $CodeEmitter.invoke_static(FAST_CLASS, GET_SIGNATURE_WITHOUT_RETURN_TYPE);
            this.signatureSwitchHelper($CodeEmitter, list2);
        } else {
            $CodeEmitter.load_args();
            List list3 = $CollectionUtils.transform(list, $MethodInfoTransformer.getInstance());
            $EmitUtils.method_switch($CodeEmitter, list3, new GetIndexCallback($CodeEmitter, list3));
        }
        $CodeEmitter.end_method();
    }

    private void signatureSwitchHelper(final $CodeEmitter $CodeEmitter, final List list) {
        $ObjectSwitchCallback $ObjectSwitchCallback = new $ObjectSwitchCallback(){

            public void processCase(Object object, $Label $Label) {
                $CodeEmitter.push(list.indexOf(object));
                $CodeEmitter.return_value();
            }

            public void processDefault() {
                $CodeEmitter.push(-1);
                $CodeEmitter.return_value();
            }
        };
        $EmitUtils.string_switch($CodeEmitter, list.toArray(new String[list.size()]), 1, $ObjectSwitchCallback);
    }

    private static void invokeSwitchHelper(final $CodeEmitter $CodeEmitter, List list, final int n2, final $Type $Type) {
        final List list2 = $CollectionUtils.transform(list, $MethodInfoTransformer.getInstance());
        final $Label $Label = $CodeEmitter.make_label();
        $Block $Block = $CodeEmitter.begin_block();
        $CodeEmitter.process_switch($FastClassEmitter.getIntRange(list2.size()), new $ProcessSwitchCallback(){

            public void processCase(int n22, $Label $Label2) {
                $MethodInfo $MethodInfo = ($MethodInfo)list2.get(n22);
                $Type[] arr$Type = $MethodInfo.getSignature().getArgumentTypes();
                for (int i2 = 0; i2 < arr$Type.length; ++i2) {
                    $CodeEmitter.load_arg(n2);
                    $CodeEmitter.aaload(i2);
                    $CodeEmitter.unbox(arr$Type[i2]);
                }
                $CodeEmitter.invoke($MethodInfo, $Type);
                if (!$TypeUtils.isConstructor($MethodInfo)) {
                    $CodeEmitter.box($MethodInfo.getSignature().getReturnType());
                }
                $CodeEmitter.return_value();
            }

            public void processDefault() {
                $CodeEmitter.goTo($Label);
            }
        });
        $Block.end();
        $EmitUtils.wrap_throwable($Block, INVOCATION_TARGET_EXCEPTION);
        $CodeEmitter.mark($Label);
        $CodeEmitter.throw_exception(ILLEGAL_ARGUMENT_EXCEPTION, "Cannot find matching method/constructor");
    }

    private static int[] getIntRange(int n2) {
        int[] arrn = new int[n2];
        int n3 = 0;
        while (n3 < n2) {
            arrn[n3] = n3++;
        }
        return arrn;
    }

    private static class GetIndexCallback
    implements $ObjectSwitchCallback {
        private $CodeEmitter e;
        private Map indexes = new HashMap();

        public GetIndexCallback($CodeEmitter $CodeEmitter, List list) {
            this.e = $CodeEmitter;
            int n2 = 0;
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                this.indexes.put(iterator.next(), new Integer(n2++));
            }
        }

        public void processCase(Object object, $Label $Label) {
            this.e.push((Integer)this.indexes.get(object));
            this.e.return_value();
        }

        public void processDefault() {
            this.e.push(-1);
            this.e.return_value();
        }
    }

}

