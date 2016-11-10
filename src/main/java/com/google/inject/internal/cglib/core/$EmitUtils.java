/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.core.$Block;
import com.google.inject.internal.cglib.core.$ClassEmitter;
import com.google.inject.internal.cglib.core.$ClassInfo;
import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import com.google.inject.internal.cglib.core.$CollectionUtils;
import com.google.inject.internal.cglib.core.$Constants;
import com.google.inject.internal.cglib.core.$Customizer;
import com.google.inject.internal.cglib.core.$Local;
import com.google.inject.internal.cglib.core.$MethodInfo;
import com.google.inject.internal.cglib.core.$ObjectSwitchCallback;
import com.google.inject.internal.cglib.core.$ProcessArrayCallback;
import com.google.inject.internal.cglib.core.$ProcessSwitchCallback;
import com.google.inject.internal.cglib.core.$Signature;
import com.google.inject.internal.cglib.core.$Transformer;
import com.google.inject.internal.cglib.core.$TypeUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class $EmitUtils {
    private static final $Signature CSTRUCT_NULL = $TypeUtils.parseConstructor("");
    private static final $Signature CSTRUCT_THROWABLE = $TypeUtils.parseConstructor("Throwable");
    private static final $Signature GET_NAME = $TypeUtils.parseSignature("String getName()");
    private static final $Signature HASH_CODE = $TypeUtils.parseSignature("int hashCode()");
    private static final $Signature EQUALS = $TypeUtils.parseSignature("boolean equals(Object)");
    private static final $Signature STRING_LENGTH = $TypeUtils.parseSignature("int length()");
    private static final $Signature STRING_CHAR_AT = $TypeUtils.parseSignature("char charAt(int)");
    private static final $Signature FOR_NAME = $TypeUtils.parseSignature("Class forName(String)");
    private static final $Signature DOUBLE_TO_LONG_BITS = $TypeUtils.parseSignature("long doubleToLongBits(double)");
    private static final $Signature FLOAT_TO_INT_BITS = $TypeUtils.parseSignature("int floatToIntBits(float)");
    private static final $Signature TO_STRING = $TypeUtils.parseSignature("String toString()");
    private static final $Signature APPEND_STRING = $TypeUtils.parseSignature("StringBuffer append(String)");
    private static final $Signature APPEND_INT = $TypeUtils.parseSignature("StringBuffer append(int)");
    private static final $Signature APPEND_DOUBLE = $TypeUtils.parseSignature("StringBuffer append(double)");
    private static final $Signature APPEND_FLOAT = $TypeUtils.parseSignature("StringBuffer append(float)");
    private static final $Signature APPEND_CHAR = $TypeUtils.parseSignature("StringBuffer append(char)");
    private static final $Signature APPEND_LONG = $TypeUtils.parseSignature("StringBuffer append(long)");
    private static final $Signature APPEND_BOOLEAN = $TypeUtils.parseSignature("StringBuffer append(boolean)");
    private static final $Signature LENGTH = $TypeUtils.parseSignature("int length()");
    private static final $Signature SET_LENGTH = $TypeUtils.parseSignature("void setLength(int)");
    private static final $Signature GET_DECLARED_METHOD = $TypeUtils.parseSignature("java.lang.reflect.Method getDeclaredMethod(String, Class[])");
    public static final ArrayDelimiters DEFAULT_DELIMITERS = new ArrayDelimiters("{", ", ", "}");

    private $EmitUtils() {
    }

    public static void factory_method($ClassEmitter $ClassEmitter, $Signature $Signature) {
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, $Signature, null);
        $CodeEmitter.new_instance_this();
        $CodeEmitter.dup();
        $CodeEmitter.load_args();
        $CodeEmitter.invoke_constructor_this($TypeUtils.parseConstructor($Signature.getArgumentTypes()));
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    public static void null_constructor($ClassEmitter $ClassEmitter) {
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, CSTRUCT_NULL, null);
        $CodeEmitter.load_this();
        $CodeEmitter.super_invoke_constructor();
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    public static void process_array($CodeEmitter $CodeEmitter, $Type $Type, $ProcessArrayCallback $ProcessArrayCallback) {
        $Type $Type2 = $TypeUtils.getComponentType($Type);
        $Local $Local = $CodeEmitter.make_local();
        $Local $Local2 = $CodeEmitter.make_local($Type.INT_TYPE);
        $Label $Label = $CodeEmitter.make_label();
        $Label $Label2 = $CodeEmitter.make_label();
        $CodeEmitter.store_local($Local);
        $CodeEmitter.push(0);
        $CodeEmitter.store_local($Local2);
        $CodeEmitter.goTo($Label2);
        $CodeEmitter.mark($Label);
        $CodeEmitter.load_local($Local);
        $CodeEmitter.load_local($Local2);
        $CodeEmitter.array_load($Type2);
        $ProcessArrayCallback.processElement($Type2);
        $CodeEmitter.iinc($Local2, 1);
        $CodeEmitter.mark($Label2);
        $CodeEmitter.load_local($Local2);
        $CodeEmitter.load_local($Local);
        $CodeEmitter.arraylength();
        $CodeEmitter.if_icmp(155, $Label);
    }

    public static void process_arrays($CodeEmitter $CodeEmitter, $Type $Type, $ProcessArrayCallback $ProcessArrayCallback) {
        $Type $Type2 = $TypeUtils.getComponentType($Type);
        $Local $Local = $CodeEmitter.make_local();
        $Local $Local2 = $CodeEmitter.make_local();
        $Local $Local3 = $CodeEmitter.make_local($Type.INT_TYPE);
        $Label $Label = $CodeEmitter.make_label();
        $Label $Label2 = $CodeEmitter.make_label();
        $CodeEmitter.store_local($Local);
        $CodeEmitter.store_local($Local2);
        $CodeEmitter.push(0);
        $CodeEmitter.store_local($Local3);
        $CodeEmitter.goTo($Label2);
        $CodeEmitter.mark($Label);
        $CodeEmitter.load_local($Local);
        $CodeEmitter.load_local($Local3);
        $CodeEmitter.array_load($Type2);
        $CodeEmitter.load_local($Local2);
        $CodeEmitter.load_local($Local3);
        $CodeEmitter.array_load($Type2);
        $ProcessArrayCallback.processElement($Type2);
        $CodeEmitter.iinc($Local3, 1);
        $CodeEmitter.mark($Label2);
        $CodeEmitter.load_local($Local3);
        $CodeEmitter.load_local($Local);
        $CodeEmitter.arraylength();
        $CodeEmitter.if_icmp(155, $Label);
    }

    public static void string_switch($CodeEmitter $CodeEmitter, String[] arrstring, int n2, $ObjectSwitchCallback $ObjectSwitchCallback) {
        block9 : {
            try {
                switch (n2) {
                    case 0: {
                        $EmitUtils.string_switch_trie($CodeEmitter, arrstring, $ObjectSwitchCallback);
                        break block9;
                    }
                    case 1: {
                        $EmitUtils.string_switch_hash($CodeEmitter, arrstring, $ObjectSwitchCallback, false);
                        break block9;
                    }
                    case 2: {
                        $EmitUtils.string_switch_hash($CodeEmitter, arrstring, $ObjectSwitchCallback, true);
                        break block9;
                    }
                }
                throw new IllegalArgumentException(new StringBuilder(32).append("unknown switch style ").append(n2).toString());
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
            catch (Error error) {
                throw error;
            }
            catch (Exception exception) {
                throw new $CodeGenerationException(exception);
            }
        }
    }

    private static void string_switch_trie(final $CodeEmitter $CodeEmitter, String[] arrstring, final $ObjectSwitchCallback $ObjectSwitchCallback) throws Exception {
        final $Label $Label = $CodeEmitter.make_label();
        final $Label $Label2 = $CodeEmitter.make_label();
        final Map map = $CollectionUtils.bucket(Arrays.asList(arrstring), new $Transformer(){

            public Object transform(Object object) {
                return new Integer(((String)object).length());
            }
        });
        $CodeEmitter.dup();
        $CodeEmitter.invoke_virtual($Constants.TYPE_STRING, STRING_LENGTH);
        $CodeEmitter.process_switch($EmitUtils.getSwitchKeys(map), new $ProcessSwitchCallback(){

            public void processCase(int n2, $Label $Label3) throws Exception {
                List list = (List)map.get(new Integer(n2));
                $EmitUtils.stringSwitchHelper($CodeEmitter, list, $ObjectSwitchCallback, $Label, $Label2, 0);
            }

            public void processDefault() {
                $CodeEmitter.goTo($Label);
            }
        });
        $CodeEmitter.mark($Label);
        $CodeEmitter.pop();
        $ObjectSwitchCallback.processDefault();
        $CodeEmitter.mark($Label2);
    }

    private static void stringSwitchHelper(final $CodeEmitter $CodeEmitter, List list, final $ObjectSwitchCallback $ObjectSwitchCallback, final $Label $Label, final $Label $Label2, final int n2) throws Exception {
        final int n3 = ((String)list.get(0)).length();
        final Map map = $CollectionUtils.bucket(list, new $Transformer(){

            public Object transform(Object object) {
                return new Integer(((String)object).charAt(n2));
            }
        });
        $CodeEmitter.dup();
        $CodeEmitter.push(n2);
        $CodeEmitter.invoke_virtual($Constants.TYPE_STRING, STRING_CHAR_AT);
        $CodeEmitter.process_switch($EmitUtils.getSwitchKeys(map), new $ProcessSwitchCallback(){

            public void processCase(int n22, $Label $Label3) throws Exception {
                List list = (List)map.get(new Integer(n22));
                if (n2 + 1 == n3) {
                    $CodeEmitter.pop();
                    $ObjectSwitchCallback.processCase(list.get(0), $Label2);
                } else {
                    $EmitUtils.stringSwitchHelper($CodeEmitter, list, $ObjectSwitchCallback, $Label, $Label2, n2 + 1);
                }
            }

            public void processDefault() {
                $CodeEmitter.goTo($Label);
            }
        });
    }

    static int[] getSwitchKeys(Map map) {
        int[] arrn = new int[map.size()];
        int n2 = 0;
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            arrn[n2++] = (Integer)iterator.next();
        }
        Arrays.sort(arrn);
        return arrn;
    }

    private static void string_switch_hash(final $CodeEmitter $CodeEmitter, String[] arrstring, final $ObjectSwitchCallback $ObjectSwitchCallback, final boolean bl) throws Exception {
        final Map map = $CollectionUtils.bucket(Arrays.asList(arrstring), new $Transformer(){

            public Object transform(Object object) {
                return new Integer(object.hashCode());
            }
        });
        final $Label $Label = $CodeEmitter.make_label();
        final $Label $Label2 = $CodeEmitter.make_label();
        $CodeEmitter.dup();
        $CodeEmitter.invoke_virtual($Constants.TYPE_OBJECT, HASH_CODE);
        $CodeEmitter.process_switch($EmitUtils.getSwitchKeys(map), new $ProcessSwitchCallback(){

            public void processCase(int n2, $Label $Label3) throws Exception {
                List list = (List)map.get(new Integer(n2));
                $Label $Label22 = null;
                if (bl && list.size() == 1) {
                    if (bl) {
                        $CodeEmitter.pop();
                    }
                    $ObjectSwitchCallback.processCase((String)list.get(0), $Label2);
                } else {
                    Iterator iterator = list.iterator();
                    while (iterator.hasNext()) {
                        String string = (String)iterator.next();
                        if ($Label22 != null) {
                            $CodeEmitter.mark($Label22);
                        }
                        if (iterator.hasNext()) {
                            $CodeEmitter.dup();
                        }
                        $CodeEmitter.push(string);
                        $CodeEmitter.invoke_virtual($Constants.TYPE_OBJECT, EQUALS);
                        if (iterator.hasNext()) {
                            $Label22 = $CodeEmitter.make_label();
                            $CodeEmitter.if_jump(153, $Label22);
                            $CodeEmitter.pop();
                        } else {
                            $CodeEmitter.if_jump(153, $Label);
                        }
                        $ObjectSwitchCallback.processCase(string, $Label2);
                    }
                }
            }

            public void processDefault() {
                $CodeEmitter.pop();
            }
        });
        $CodeEmitter.mark($Label);
        $ObjectSwitchCallback.processDefault();
        $CodeEmitter.mark($Label2);
    }

    public static void load_class_this($CodeEmitter $CodeEmitter) {
        $EmitUtils.load_class_helper($CodeEmitter, $CodeEmitter.getClassEmitter().getClassType());
    }

    public static void load_class($CodeEmitter $CodeEmitter, $Type $Type) {
        if ($TypeUtils.isPrimitive($Type)) {
            if ($Type == $Type.VOID_TYPE) {
                throw new IllegalArgumentException("cannot load void type");
            }
            $CodeEmitter.getstatic($TypeUtils.getBoxedType($Type), "TYPE", $Constants.TYPE_CLASS);
        } else {
            $EmitUtils.load_class_helper($CodeEmitter, $Type);
        }
    }

    private static void load_class_helper($CodeEmitter $CodeEmitter, $Type $Type) {
        if ($CodeEmitter.isStaticHook()) {
            $CodeEmitter.push($TypeUtils.emulateClassGetName($Type));
            $CodeEmitter.invoke_static($Constants.TYPE_CLASS, FOR_NAME);
        } else {
            String string;
            $ClassEmitter $ClassEmitter = $CodeEmitter.getClassEmitter();
            String string2 = $TypeUtils.emulateClassGetName($Type);
            String string3 = String.valueOf($TypeUtils.escapeType(string2));
            String string4 = string = string3.length() != 0 ? "CGLIB$load_class$".concat(string3) : new String("CGLIB$load_class$");
            if (!$ClassEmitter.isFieldDeclared(string)) {
                $ClassEmitter.declare_field(26, string, $Constants.TYPE_CLASS, null);
                $CodeEmitter $CodeEmitter2 = $ClassEmitter.getStaticHook();
                $CodeEmitter2.push(string2);
                $CodeEmitter2.invoke_static($Constants.TYPE_CLASS, FOR_NAME);
                $CodeEmitter2.putstatic($ClassEmitter.getClassType(), string, $Constants.TYPE_CLASS);
            }
            $CodeEmitter.getfield(string);
        }
    }

    public static void push_array($CodeEmitter $CodeEmitter, Object[] arrobject) {
        $CodeEmitter.push(arrobject.length);
        $CodeEmitter.newarray($Type.getType($EmitUtils.remapComponentType(arrobject.getClass().getComponentType())));
        for (int i2 = 0; i2 < arrobject.length; ++i2) {
            $CodeEmitter.dup();
            $CodeEmitter.push(i2);
            $EmitUtils.push_object($CodeEmitter, arrobject[i2]);
            $CodeEmitter.aastore();
        }
    }

    private static Class remapComponentType(Class class_) {
        if (class_.equals($Type.class)) {
            return Class.class;
        }
        return class_;
    }

    public static void push_object($CodeEmitter $CodeEmitter, Object object) {
        if (object == null) {
            $CodeEmitter.aconst_null();
        } else {
            Class class_ = object.getClass();
            if (class_.isArray()) {
                $EmitUtils.push_array($CodeEmitter, (Object[])object);
            } else if (object instanceof String) {
                $CodeEmitter.push((String)object);
            } else if (object instanceof $Type) {
                $EmitUtils.load_class($CodeEmitter, ($Type)object);
            } else if (object instanceof Class) {
                $EmitUtils.load_class($CodeEmitter, $Type.getType((Class)object));
            } else if (object instanceof BigInteger) {
                $CodeEmitter.new_instance($Constants.TYPE_BIG_INTEGER);
                $CodeEmitter.dup();
                $CodeEmitter.push(object.toString());
                $CodeEmitter.invoke_constructor($Constants.TYPE_BIG_INTEGER);
            } else if (object instanceof BigDecimal) {
                $CodeEmitter.new_instance($Constants.TYPE_BIG_DECIMAL);
                $CodeEmitter.dup();
                $CodeEmitter.push(object.toString());
                $CodeEmitter.invoke_constructor($Constants.TYPE_BIG_DECIMAL);
            } else {
                String string = String.valueOf(object.getClass());
                throw new IllegalArgumentException(new StringBuilder(14 + String.valueOf(string).length()).append("unknown type: ").append(string).toString());
            }
        }
    }

    public static void hash_code($CodeEmitter $CodeEmitter, $Type $Type, int n2, $Customizer $Customizer) {
        if ($TypeUtils.isArray($Type)) {
            $EmitUtils.hash_array($CodeEmitter, $Type, n2, $Customizer);
        } else {
            $CodeEmitter.swap($Type.INT_TYPE, $Type);
            $CodeEmitter.push(n2);
            $CodeEmitter.math(104, $Type.INT_TYPE);
            $CodeEmitter.swap($Type, $Type.INT_TYPE);
            if ($TypeUtils.isPrimitive($Type)) {
                $EmitUtils.hash_primitive($CodeEmitter, $Type);
            } else {
                $EmitUtils.hash_object($CodeEmitter, $Type, $Customizer);
            }
            $CodeEmitter.math(96, $Type.INT_TYPE);
        }
    }

    private static void hash_array(final $CodeEmitter $CodeEmitter, $Type $Type, final int n2, final $Customizer $Customizer) {
        $Label $Label = $CodeEmitter.make_label();
        $Label $Label2 = $CodeEmitter.make_label();
        $CodeEmitter.dup();
        $CodeEmitter.ifnull($Label);
        $EmitUtils.process_array($CodeEmitter, $Type, new $ProcessArrayCallback(){

            public void processElement($Type $Type) {
                $EmitUtils.hash_code($CodeEmitter, $Type, n2, $Customizer);
            }
        });
        $CodeEmitter.goTo($Label2);
        $CodeEmitter.mark($Label);
        $CodeEmitter.pop();
        $CodeEmitter.mark($Label2);
    }

    private static void hash_object($CodeEmitter $CodeEmitter, $Type $Type, $Customizer $Customizer) {
        $Label $Label = $CodeEmitter.make_label();
        $Label $Label2 = $CodeEmitter.make_label();
        $CodeEmitter.dup();
        $CodeEmitter.ifnull($Label);
        if ($Customizer != null) {
            $Customizer.customize($CodeEmitter, $Type);
        }
        $CodeEmitter.invoke_virtual($Constants.TYPE_OBJECT, HASH_CODE);
        $CodeEmitter.goTo($Label2);
        $CodeEmitter.mark($Label);
        $CodeEmitter.pop();
        $CodeEmitter.push(0);
        $CodeEmitter.mark($Label2);
    }

    private static void hash_primitive($CodeEmitter $CodeEmitter, $Type $Type) {
        switch ($Type.getSort()) {
            case 1: {
                $CodeEmitter.push(1);
                $CodeEmitter.math(130, $Type.INT_TYPE);
                break;
            }
            case 6: {
                $CodeEmitter.invoke_static($Constants.TYPE_FLOAT, FLOAT_TO_INT_BITS);
                break;
            }
            case 8: {
                $CodeEmitter.invoke_static($Constants.TYPE_DOUBLE, DOUBLE_TO_LONG_BITS);
            }
            case 7: {
                $EmitUtils.hash_long($CodeEmitter);
            }
        }
    }

    private static void hash_long($CodeEmitter $CodeEmitter) {
        $CodeEmitter.dup2();
        $CodeEmitter.push(32);
        $CodeEmitter.math(124, $Type.LONG_TYPE);
        $CodeEmitter.math(130, $Type.LONG_TYPE);
        $CodeEmitter.cast_numeric($Type.LONG_TYPE, $Type.INT_TYPE);
    }

    public static void not_equals(final $CodeEmitter $CodeEmitter, $Type $Type, final $Label $Label, final $Customizer $Customizer) {
        new $ProcessArrayCallback(){

            public void processElement($Type $Type) {
                $EmitUtils.not_equals_helper($CodeEmitter, $Type, $Label, $Customizer, this);
            }
        }.processElement($Type);
    }

    private static void not_equals_helper($CodeEmitter $CodeEmitter, $Type $Type, $Label $Label, $Customizer $Customizer, $ProcessArrayCallback $ProcessArrayCallback) {
        if ($TypeUtils.isPrimitive($Type)) {
            $CodeEmitter.if_cmp($Type, 154, $Label);
        } else {
            $Label $Label2 = $CodeEmitter.make_label();
            $EmitUtils.nullcmp($CodeEmitter, $Label, $Label2);
            if ($TypeUtils.isArray($Type)) {
                $Label $Label3 = $CodeEmitter.make_label();
                $CodeEmitter.dup2();
                $CodeEmitter.arraylength();
                $CodeEmitter.swap();
                $CodeEmitter.arraylength();
                $CodeEmitter.if_icmp(153, $Label3);
                $CodeEmitter.pop2();
                $CodeEmitter.goTo($Label);
                $CodeEmitter.mark($Label3);
                $EmitUtils.process_arrays($CodeEmitter, $Type, $ProcessArrayCallback);
            } else {
                if ($Customizer != null) {
                    $Customizer.customize($CodeEmitter, $Type);
                    $CodeEmitter.swap();
                    $Customizer.customize($CodeEmitter, $Type);
                }
                $CodeEmitter.invoke_virtual($Constants.TYPE_OBJECT, EQUALS);
                $CodeEmitter.if_jump(153, $Label);
            }
            $CodeEmitter.mark($Label2);
        }
    }

    private static void nullcmp($CodeEmitter $CodeEmitter, $Label $Label, $Label $Label2) {
        $CodeEmitter.dup2();
        $Label $Label3 = $CodeEmitter.make_label();
        $Label $Label4 = $CodeEmitter.make_label();
        $Label $Label5 = $CodeEmitter.make_label();
        $CodeEmitter.ifnonnull($Label3);
        $CodeEmitter.ifnonnull($Label4);
        $CodeEmitter.pop2();
        $CodeEmitter.goTo($Label2);
        $CodeEmitter.mark($Label3);
        $CodeEmitter.ifnull($Label4);
        $CodeEmitter.goTo($Label5);
        $CodeEmitter.mark($Label4);
        $CodeEmitter.pop2();
        $CodeEmitter.goTo($Label);
        $CodeEmitter.mark($Label5);
    }

    public static void append_string(final $CodeEmitter $CodeEmitter, $Type $Type, ArrayDelimiters arrayDelimiters, final $Customizer $Customizer) {
        final ArrayDelimiters arrayDelimiters2 = arrayDelimiters != null ? arrayDelimiters : DEFAULT_DELIMITERS;
        $ProcessArrayCallback $ProcessArrayCallback = new $ProcessArrayCallback(){

            public void processElement($Type $Type) {
                $EmitUtils.append_string_helper($CodeEmitter, $Type, arrayDelimiters2, $Customizer, this);
                $CodeEmitter.push(arrayDelimiters2.inside);
                $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_STRING);
            }
        };
        $EmitUtils.append_string_helper($CodeEmitter, $Type, arrayDelimiters2, $Customizer, $ProcessArrayCallback);
    }

    private static void append_string_helper($CodeEmitter $CodeEmitter, $Type $Type, ArrayDelimiters arrayDelimiters, $Customizer $Customizer, $ProcessArrayCallback $ProcessArrayCallback) {
        $Label $Label = $CodeEmitter.make_label();
        $Label $Label2 = $CodeEmitter.make_label();
        if ($TypeUtils.isPrimitive($Type)) {
            switch ($Type.getSort()) {
                case 3: 
                case 4: 
                case 5: {
                    $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_INT);
                    break;
                }
                case 8: {
                    $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_DOUBLE);
                    break;
                }
                case 6: {
                    $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_FLOAT);
                    break;
                }
                case 7: {
                    $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_LONG);
                    break;
                }
                case 1: {
                    $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_BOOLEAN);
                    break;
                }
                case 2: {
                    $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_CHAR);
                }
            }
        } else if ($TypeUtils.isArray($Type)) {
            $CodeEmitter.dup();
            $CodeEmitter.ifnull($Label);
            $CodeEmitter.swap();
            if (arrayDelimiters != null && arrayDelimiters.before != null && !"".equals(arrayDelimiters.before)) {
                $CodeEmitter.push(arrayDelimiters.before);
                $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_STRING);
                $CodeEmitter.swap();
            }
            $EmitUtils.process_array($CodeEmitter, $Type, $ProcessArrayCallback);
            $EmitUtils.shrinkStringBuffer($CodeEmitter, 2);
            if (arrayDelimiters != null && arrayDelimiters.after != null && !"".equals(arrayDelimiters.after)) {
                $CodeEmitter.push(arrayDelimiters.after);
                $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_STRING);
            }
        } else {
            $CodeEmitter.dup();
            $CodeEmitter.ifnull($Label);
            if ($Customizer != null) {
                $Customizer.customize($CodeEmitter, $Type);
            }
            $CodeEmitter.invoke_virtual($Constants.TYPE_OBJECT, TO_STRING);
            $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_STRING);
        }
        $CodeEmitter.goTo($Label2);
        $CodeEmitter.mark($Label);
        $CodeEmitter.pop();
        $CodeEmitter.push("null");
        $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, APPEND_STRING);
        $CodeEmitter.mark($Label2);
    }

    private static void shrinkStringBuffer($CodeEmitter $CodeEmitter, int n2) {
        $CodeEmitter.dup();
        $CodeEmitter.dup();
        $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, LENGTH);
        $CodeEmitter.push(n2);
        $CodeEmitter.math(100, $Type.INT_TYPE);
        $CodeEmitter.invoke_virtual($Constants.TYPE_STRING_BUFFER, SET_LENGTH);
    }

    public static void load_method($CodeEmitter $CodeEmitter, $MethodInfo $MethodInfo) {
        $EmitUtils.load_class($CodeEmitter, $MethodInfo.getClassInfo().getType());
        $CodeEmitter.push($MethodInfo.getSignature().getName());
        $EmitUtils.push_object($CodeEmitter, $MethodInfo.getSignature().getArgumentTypes());
        $CodeEmitter.invoke_virtual($Constants.TYPE_CLASS, GET_DECLARED_METHOD);
    }

    public static void method_switch($CodeEmitter $CodeEmitter, List list, $ObjectSwitchCallback $ObjectSwitchCallback) {
        $EmitUtils.member_switch_helper($CodeEmitter, list, $ObjectSwitchCallback, true);
    }

    public static void constructor_switch($CodeEmitter $CodeEmitter, List list, $ObjectSwitchCallback $ObjectSwitchCallback) {
        $EmitUtils.member_switch_helper($CodeEmitter, list, $ObjectSwitchCallback, false);
    }

    private static void member_switch_helper(final $CodeEmitter $CodeEmitter, List list, final $ObjectSwitchCallback $ObjectSwitchCallback, boolean bl) {
        try {
            final HashMap hashMap = new HashMap();
            final ParameterTyper parameterTyper = new ParameterTyper(){

                public $Type[] getParameterTypes($MethodInfo $MethodInfo) {
                    $Type[] arr$Type = ($Type[])hashMap.get($MethodInfo);
                    if (arr$Type == null) {
                        arr$Type = $MethodInfo.getSignature().getArgumentTypes();
                        hashMap.put($MethodInfo, arr$Type);
                    }
                    return arr$Type;
                }
            };
            final $Label $Label = $CodeEmitter.make_label();
            final $Label $Label2 = $CodeEmitter.make_label();
            if (bl) {
                $CodeEmitter.swap();
                final Map map = $CollectionUtils.bucket(list, new $Transformer(){

                    public Object transform(Object object) {
                        return (($MethodInfo)object).getSignature().getName();
                    }
                });
                String[] arrstring = map.keySet().toArray(new String[map.size()]);
                $EmitUtils.string_switch($CodeEmitter, arrstring, 1, new $ObjectSwitchCallback(){

                    public void processCase(Object object, $Label $Label3) throws Exception {
                        $EmitUtils.member_helper_size($CodeEmitter, (List)map.get(object), $ObjectSwitchCallback, parameterTyper, $Label, $Label2);
                    }

                    public void processDefault() throws Exception {
                        $CodeEmitter.goTo($Label);
                    }
                });
            } else {
                $EmitUtils.member_helper_size($CodeEmitter, list, $ObjectSwitchCallback, parameterTyper, $Label, $Label2);
            }
            $CodeEmitter.mark($Label);
            $CodeEmitter.pop();
            $ObjectSwitchCallback.processDefault();
            $CodeEmitter.mark($Label2);
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Error error) {
            throw error;
        }
        catch (Exception exception) {
            throw new $CodeGenerationException(exception);
        }
    }

    private static void member_helper_size(final $CodeEmitter $CodeEmitter, List list, final $ObjectSwitchCallback $ObjectSwitchCallback, final ParameterTyper parameterTyper, final $Label $Label, final $Label $Label2) throws Exception {
        final Map map = $CollectionUtils.bucket(list, new $Transformer(){

            public Object transform(Object object) {
                return new Integer(parameterTyper.getParameterTypes(($MethodInfo)object).length);
            }
        });
        $CodeEmitter.dup();
        $CodeEmitter.arraylength();
        $CodeEmitter.process_switch($EmitUtils.getSwitchKeys(map), new $ProcessSwitchCallback(){

            public void processCase(int n2, $Label $Label3) throws Exception {
                List list = (List)map.get(new Integer(n2));
                $EmitUtils.member_helper_type($CodeEmitter, list, $ObjectSwitchCallback, parameterTyper, $Label, $Label2, new BitSet());
            }

            public void processDefault() throws Exception {
                $CodeEmitter.goTo($Label);
            }
        });
    }

    private static void member_helper_type(final $CodeEmitter $CodeEmitter, List list, final $ObjectSwitchCallback $ObjectSwitchCallback, final ParameterTyper parameterTyper, final $Label $Label, final $Label $Label2, final BitSet bitSet) throws Exception {
        if (list.size() == 1) {
            $MethodInfo $MethodInfo = ($MethodInfo)list.get(0);
            $Type[] arr$Type = parameterTyper.getParameterTypes($MethodInfo);
            for (int i2 = 0; i2 < arr$Type.length; ++i2) {
                if (bitSet != null && bitSet.get(i2)) continue;
                $CodeEmitter.dup();
                $CodeEmitter.aaload(i2);
                $CodeEmitter.invoke_virtual($Constants.TYPE_CLASS, GET_NAME);
                $CodeEmitter.push($TypeUtils.emulateClassGetName(arr$Type[i2]));
                $CodeEmitter.invoke_virtual($Constants.TYPE_OBJECT, EQUALS);
                $CodeEmitter.if_jump(153, $Label);
            }
            $CodeEmitter.pop();
            $ObjectSwitchCallback.processCase($MethodInfo, $Label2);
        } else {
            $Type[] arr$Type = parameterTyper.getParameterTypes(($MethodInfo)list.get(0));
            Map map = null;
            int n2 = -1;
            for (int i3 = 0; i3 < arr$Type.length; ++i3) {
                final int n3 = i3;
                Map map2 = $CollectionUtils.bucket(list, new $Transformer(){

                    public Object transform(Object object) {
                        return $TypeUtils.emulateClassGetName(parameterTyper.getParameterTypes(($MethodInfo)object)[n3]);
                    }
                });
                if (map != null && map2.size() <= map.size()) continue;
                map = map2;
                n2 = i3;
            }
            if (map == null || map.size() == 1) {
                $CodeEmitter.goTo($Label);
            } else {
                bitSet.set(n2);
                $CodeEmitter.dup();
                $CodeEmitter.aaload(n2);
                $CodeEmitter.invoke_virtual($Constants.TYPE_CLASS, GET_NAME);
                final Map map3 = map;
                String[] arrstring = map.keySet().toArray(new String[map.size()]);
                $EmitUtils.string_switch($CodeEmitter, arrstring, 1, new $ObjectSwitchCallback(){

                    public void processCase(Object object, $Label $Label3) throws Exception {
                        $EmitUtils.member_helper_type($CodeEmitter, (List)map3.get(object), $ObjectSwitchCallback, parameterTyper, $Label, $Label2, bitSet);
                    }

                    public void processDefault() throws Exception {
                        $CodeEmitter.goTo($Label);
                    }
                });
            }
        }
    }

    public static void wrap_throwable($Block $Block, $Type $Type) {
        $CodeEmitter $CodeEmitter = $Block.getCodeEmitter();
        $CodeEmitter.catch_exception($Block, $Constants.TYPE_THROWABLE);
        $CodeEmitter.new_instance($Type);
        $CodeEmitter.dup_x1();
        $CodeEmitter.swap();
        $CodeEmitter.invoke_constructor($Type, CSTRUCT_THROWABLE);
        $CodeEmitter.athrow();
    }

    public static void add_properties($ClassEmitter $ClassEmitter, String[] arrstring, $Type[] arr$Type) {
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            String string = String.valueOf(arrstring[i2]);
            String string2 = string.length() != 0 ? "$cglib_prop_".concat(string) : new String("$cglib_prop_");
            $ClassEmitter.declare_field(2, string2, arr$Type[i2], null);
            $EmitUtils.add_property($ClassEmitter, arrstring[i2], arr$Type[i2], string2);
        }
    }

    public static void add_property($ClassEmitter $ClassEmitter, String string, $Type $Type, String string2) {
        String string3 = $TypeUtils.upperFirst(string);
        String string4 = String.valueOf(string3);
        $CodeEmitter $CodeEmitter = $ClassEmitter.begin_method(1, new $Signature(string4.length() != 0 ? "get".concat(string4) : new String("get"), $Type, $Constants.TYPES_EMPTY), null);
        $CodeEmitter.load_this();
        $CodeEmitter.getfield(string2);
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
        String string5 = String.valueOf(string3);
        $CodeEmitter = $ClassEmitter.begin_method(1, new $Signature(string5.length() != 0 ? "set".concat(string5) : new String("set"), $Type.VOID_TYPE, new $Type[]{$Type}), null);
        $CodeEmitter.load_this();
        $CodeEmitter.load_arg(0);
        $CodeEmitter.putfield(string2);
        $CodeEmitter.return_value();
        $CodeEmitter.end_method();
    }

    public static void wrap_undeclared_throwable($CodeEmitter $CodeEmitter, $Block $Block, $Type[] arr$Type, $Type $Type) {
        Set set;
        boolean bl;
        Set set2 = set = arr$Type == null ? Collections.EMPTY_SET : new HashSet<$Type>(Arrays.asList(arr$Type));
        if (set.contains($Constants.TYPE_THROWABLE)) {
            return;
        }
        boolean bl2 = bl = arr$Type != null;
        if (!set.contains($Constants.TYPE_RUNTIME_EXCEPTION)) {
            $CodeEmitter.catch_exception($Block, $Constants.TYPE_RUNTIME_EXCEPTION);
            bl = true;
        }
        if (!set.contains($Constants.TYPE_ERROR)) {
            $CodeEmitter.catch_exception($Block, $Constants.TYPE_ERROR);
            bl = true;
        }
        if (arr$Type != null) {
            for (int i2 = 0; i2 < arr$Type.length; ++i2) {
                $CodeEmitter.catch_exception($Block, arr$Type[i2]);
            }
        }
        if (bl) {
            $CodeEmitter.athrow();
        }
        $CodeEmitter.catch_exception($Block, $Constants.TYPE_THROWABLE);
        $CodeEmitter.new_instance($Type);
        $CodeEmitter.dup_x1();
        $CodeEmitter.swap();
        $CodeEmitter.invoke_constructor($Type, CSTRUCT_THROWABLE);
        $CodeEmitter.athrow();
    }

    public static $CodeEmitter begin_method($ClassEmitter $ClassEmitter, $MethodInfo $MethodInfo) {
        return $EmitUtils.begin_method($ClassEmitter, $MethodInfo, $MethodInfo.getModifiers());
    }

    public static $CodeEmitter begin_method($ClassEmitter $ClassEmitter, $MethodInfo $MethodInfo, int n2) {
        return $ClassEmitter.begin_method(n2, $MethodInfo.getSignature(), $MethodInfo.getExceptionTypes());
    }

    private static interface ParameterTyper {
        public $Type[] getParameterTypes($MethodInfo var1);
    }

    public static class ArrayDelimiters {
        private String before;
        private String inside;
        private String after;

        public ArrayDelimiters(String string, String string2, String string3) {
            this.before = string;
            this.inside = string2;
            this.after = string3;
        }
    }

}

