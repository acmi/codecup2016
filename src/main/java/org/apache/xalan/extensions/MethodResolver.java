/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XString;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

public class MethodResolver {
    public static final int STATIC_ONLY = 1;
    public static final int INSTANCE_ONLY = 2;
    public static final int STATIC_AND_INSTANCE = 3;
    public static final int DYNAMIC = 4;
    private static final int SCOREBASE = 1;
    private static final ConversionInfo[] m_javaObjConversions;
    private static final ConversionInfo[] m_booleanConversions;
    private static final ConversionInfo[] m_numberConversions;
    private static final ConversionInfo[] m_stringConversions;
    private static final ConversionInfo[] m_rtfConversions;
    private static final ConversionInfo[] m_nodesetConversions;
    private static final ConversionInfo[][] m_conversions;
    static Class class$org$apache$xalan$extensions$ExpressionContext;
    static Class class$org$apache$xalan$templates$ElemExtensionCall;
    static Class class$org$apache$xalan$extensions$XSLProcessorContext;
    static Class class$java$lang$String;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Object;
    static Class class$java$lang$Double;
    static Class class$org$w3c$dom$traversal$NodeIterator;
    static Class class$org$w3c$dom$NodeList;
    static Class class$org$w3c$dom$Node;
    static Class class$java$lang$Class;

    public static Constructor getConstructor(Class class_, Object[] arrobject, Object[][] arrobject2, ExpressionContext expressionContext) throws NoSuchMethodException, SecurityException, TransformerException {
        Constructor constructor = null;
        Class[] arrclass = null;
        Constructor<?>[] arrconstructor = class_.getConstructors();
        int n2 = arrconstructor.length;
        int n3 = Integer.MAX_VALUE;
        int n4 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5;
            int n6;
            Constructor constructor2 = arrconstructor[i2];
            Class[] arrclass2 = constructor2.getParameterTypes();
            int n7 = arrclass2.length;
            int n8 = 0;
            boolean bl = false;
            if (n7 == arrobject.length + 1) {
                Class class_2;
                if (!(class$org$apache$xalan$extensions$ExpressionContext == null ? MethodResolver.class$("org.apache.xalan.extensions.ExpressionContext") : class$org$apache$xalan$extensions$ExpressionContext).isAssignableFrom(class_2 = arrclass2[0])) continue;
                bl = true;
                n5 = 0;
                ++n8;
            } else {
                n5 = 1000;
            }
            if (arrobject.length != n7 - n8 || -1 == (n6 = MethodResolver.scoreMatch(arrclass2, n8, arrobject, n5))) continue;
            if (n6 < n3) {
                constructor = constructor2;
                arrclass = arrclass2;
                n3 = n6;
                n4 = 1;
                continue;
            }
            if (n6 != n3) continue;
            ++n4;
        }
        if (null == constructor) {
            throw new NoSuchMethodException(MethodResolver.errString("function", "constructor", class_, "", 0, arrobject));
        }
        MethodResolver.convertParams(arrobject, arrobject2, arrclass, expressionContext);
        return constructor;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Method getMethod(Class class_, String string, Object[] arrobject, Object[][] arrobject2, ExpressionContext expressionContext, int n2) throws NoSuchMethodException, SecurityException, TransformerException {
        if (string.indexOf("-") > 0) {
            string = MethodResolver.replaceDash(string);
        }
        Method method = null;
        Class[] arrclass = null;
        Method[] arrmethod = class_.getMethods();
        int n3 = arrmethod.length;
        int n4 = Integer.MAX_VALUE;
        int n5 = 0;
        block6 : for (int i2 = 0; i2 < n3; ++i2) {
            int n6;
            int n7;
            int n8;
            Method method2 = arrmethod[i2];
            int n9 = 0;
            if (!method2.getName().equals(string)) continue;
            boolean bl = Modifier.isStatic(method2.getModifiers());
            switch (n2) {
                case 1: {
                    if (bl) break;
                    continue block6;
                }
                case 2: {
                    if (!bl) break;
                    continue block6;
                }
                case 3: {
                    break;
                }
                case 4: {
                    if (bl) break;
                    n9 = 1;
                }
            }
            int n10 = 0;
            Class[] arrclass2 = method2.getParameterTypes();
            int n11 = arrclass2.length;
            boolean bl2 = false;
            int n12 = n7 = null != arrobject ? arrobject.length : 0;
            if (n11 == n7 - n9 + 1) {
                Class class_2;
                if (!(class$org$apache$xalan$extensions$ExpressionContext == null ? MethodResolver.class$("org.apache.xalan.extensions.ExpressionContext") : class$org$apache$xalan$extensions$ExpressionContext).isAssignableFrom(class_2 = arrclass2[0])) continue;
                bl2 = true;
                n8 = 0;
                ++n10;
            } else {
                n8 = 1000;
            }
            if (n7 - n9 != n11 - n10 || -1 == (n6 = MethodResolver.scoreMatch(arrclass2, n10, arrobject, n8))) continue;
            if (n6 < n4) {
                method = method2;
                arrclass = arrclass2;
                n4 = n6;
                n5 = 1;
                continue;
            }
            if (n6 != n4) continue;
            ++n5;
        }
        if (null == method) {
            throw new NoSuchMethodException(MethodResolver.errString("function", "method", class_, string, n2, arrobject));
        }
        MethodResolver.convertParams(arrobject, arrobject2, arrclass, expressionContext);
        return method;
    }

    private static String replaceDash(String string) {
        char c2 = '-';
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i2 = 0; i2 < string.length(); ++i2) {
            if (string.charAt(i2) == c2) continue;
            if (i2 > 0 && string.charAt(i2 - 1) == c2) {
                stringBuffer.append(Character.toUpperCase(string.charAt(i2)));
                continue;
            }
            stringBuffer.append(string.charAt(i2));
        }
        return stringBuffer.toString();
    }

    public static Method getElementMethod(Class class_, String string) throws NoSuchMethodException, SecurityException, TransformerException {
        Method method = null;
        Method[] arrmethod = class_.getMethods();
        int n2 = arrmethod.length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            Class<?>[] arrclass;
            Method method2 = arrmethod[i2];
            if (!method2.getName().equals(string) || (arrclass = method2.getParameterTypes()).length != 2 || !arrclass[1].isAssignableFrom(class$org$apache$xalan$templates$ElemExtensionCall == null ? MethodResolver.class$("org.apache.xalan.templates.ElemExtensionCall") : class$org$apache$xalan$templates$ElemExtensionCall) || !arrclass[0].isAssignableFrom(class$org$apache$xalan$extensions$XSLProcessorContext == null ? MethodResolver.class$("org.apache.xalan.extensions.XSLProcessorContext") : class$org$apache$xalan$extensions$XSLProcessorContext)) continue;
            if (++n3 != 1) break;
            method = method2;
        }
        if (null == method) {
            throw new NoSuchMethodException(MethodResolver.errString("element", "method", class_, string, 0, null));
        }
        if (n3 > 1) {
            throw new TransformerException(XSLMessages.createMessage("ER_MORE_MATCH_ELEMENT", new Object[]{string}));
        }
        return method;
    }

    public static void convertParams(Object[] arrobject, Object[][] arrobject2, Class[] arrclass, ExpressionContext expressionContext) throws TransformerException {
        if (arrclass == null) {
            arrobject2[0] = null;
        } else {
            int n2 = arrclass.length;
            arrobject2[0] = new Object[n2];
            int n3 = 0;
            if (n2 > 0) {
                Class class_ = class$org$apache$xalan$extensions$ExpressionContext == null ? (MethodResolver.class$org$apache$xalan$extensions$ExpressionContext = MethodResolver.class$("org.apache.xalan.extensions.ExpressionContext")) : class$org$apache$xalan$extensions$ExpressionContext;
                if (class_.isAssignableFrom(arrclass[0])) {
                    arrobject2[0][0] = expressionContext;
                    ++n3;
                }
            }
            if (arrobject != null) {
                int n4 = arrobject.length - n2 + n3;
                while (n3 < n2) {
                    arrobject2[0][n3] = MethodResolver.convert(arrobject[n4], arrclass[n3]);
                    ++n4;
                    ++n3;
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static int scoreMatch(Class[] var0, int var1_1, Object[] var2_2, int var3_3) {
        if (var2_2 == null) return var3_3;
        if (var0 == null) {
            return var3_3;
        }
        var4_4 = var2_2.length;
        var5_5 = var4_4 - var0.length + var1_1;
        var6_6 = var1_1;
        while (var5_5 < var4_4) {
            var7_7 = var2_2[var5_5];
            var8_8 = var7_7 instanceof XObject != false ? ((XObject)var7_7).getType() : 0;
            var9_9 = var0[var6_6];
            if (var8_8 != -1) ** GOTO lbl15
            if (var9_9.isPrimitive() != false) return -1;
            var3_3 += 10;
            ** GOTO lbl35
lbl15: // 1 sources:
            var10_10 = MethodResolver.m_conversions[var8_8];
            var11_11 = var10_10.length;
            for (var12_12 = 0; var12_12 < var11_11; ++var12_12) {
                var13_13 = var10_10[var12_12];
                if (!var9_9.isAssignableFrom(var13_13.m_class)) continue;
                var3_3 += var13_13.m_score;
                break;
            }
            if (var12_12 != var11_11) ** GOTO lbl35
            if (0 != var8_8) return -1;
            var13_13 = null;
            if (!(var7_7 instanceof XObject)) ** GOTO lbl32
            var14_14 = ((XObject)var7_7).object();
            if (null == var14_14) ** GOTO lbl30
            var13_13 = var14_14.getClass();
            ** GOTO lbl33
lbl30: // 1 sources:
            var3_3 += 10;
            ** GOTO lbl35
lbl32: // 1 sources:
            var13_13 = var7_7.getClass();
lbl33: // 2 sources:
            if (var9_9.isAssignableFrom(var13_13) == false) return -1;
            var3_3 += 0;
lbl35: // 4 sources:
            ++var5_5;
            ++var6_6;
        }
        return var3_3;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    static Object convert(Object var0, Class var1_1) throws TransformerException {
        if (!(var0 instanceof XObject)) ** GOTO lbl106
        var2_2 = (XObject)var0;
        var3_3 = var2_2.getType();
        switch (var3_3) {
            case -1: {
                return null;
            }
            case 1: {
                v0 = MethodResolver.class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : MethodResolver.class$java$lang$String;
                if (var1_1 == v0) {
                    return var2_2.str();
                }
                if (var2_2.bool()) {
                    v1 = Boolean.TRUE;
                    return v1;
                }
                v1 = Boolean.FALSE;
                return v1;
            }
            case 2: {
                v2 = MethodResolver.class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : MethodResolver.class$java$lang$String;
                if (var1_1 == v2) {
                    return var2_2.str();
                }
                if (var1_1 != Boolean.TYPE) return MethodResolver.convertDoubleToNumber(var2_2.num(), var1_1);
                if (var2_2.bool()) {
                    v3 = Boolean.TRUE;
                    return v3;
                }
                v3 = Boolean.FALSE;
                return v3;
            }
            case 3: {
                v4 = MethodResolver.class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : MethodResolver.class$java$lang$String;
                if (var1_1 == v4) return var2_2.str();
                v5 = MethodResolver.class$java$lang$Object == null ? (MethodResolver.class$java$lang$Object = MethodResolver.class$("java.lang.Object")) : MethodResolver.class$java$lang$Object;
                if (var1_1 == v5) {
                    return var2_2.str();
                }
                if (var1_1 == Character.TYPE) {
                    var4_5 = var2_2.str();
                    if (var4_5.length() <= 0) return null;
                    return new Character(var4_5.charAt(0));
                }
                if (var1_1 != Boolean.TYPE) return MethodResolver.convertDoubleToNumber(var2_2.num(), var1_1);
                if (var2_2.bool()) {
                    v6 = Boolean.TRUE;
                    return v6;
                }
                v6 = Boolean.FALSE;
                return v6;
            }
            case 5: {
                v7 = MethodResolver.class$org$w3c$dom$traversal$NodeIterator == null ? (MethodResolver.class$org$w3c$dom$traversal$NodeIterator = MethodResolver.class$("org.w3c.dom.traversal.NodeIterator")) : MethodResolver.class$org$w3c$dom$traversal$NodeIterator;
                if (var1_1 == v7) ** GOTO lbl-1000
                v8 = MethodResolver.class$java$lang$Object == null ? (MethodResolver.class$java$lang$Object = MethodResolver.class$("java.lang.Object")) : MethodResolver.class$java$lang$Object;
                if (var1_1 == v8) lbl-1000: // 2 sources:
                {
                    var4_6 = ((XRTreeFrag)var2_2).asNodeIterator();
                    return new DTMNodeIterator(var4_6);
                }
                v9 = MethodResolver.class$org$w3c$dom$NodeList == null ? (MethodResolver.class$org$w3c$dom$NodeList = MethodResolver.class$("org.w3c.dom.NodeList")) : MethodResolver.class$org$w3c$dom$NodeList;
                if (var1_1 == v9) {
                    return ((XRTreeFrag)var2_2).convertToNodeset();
                }
                v10 = MethodResolver.class$org$w3c$dom$Node == null ? (MethodResolver.class$org$w3c$dom$Node = MethodResolver.class$("org.w3c.dom.Node")) : MethodResolver.class$org$w3c$dom$Node;
                if (var1_1 == v10) {
                    var4_7 = ((XRTreeFrag)var2_2).asNodeIterator();
                    var5_11 = var4_7.nextNode();
                    var6_15 = var4_7.getDTM(var5_11);
                    return var6_15.getNode(var6_15.getFirstChild(var5_11));
                }
                v11 = MethodResolver.class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : MethodResolver.class$java$lang$String;
                if (var1_1 == v11) {
                    return var2_2.str();
                }
                if (var1_1 == Boolean.TYPE) {
                    if (var2_2.bool()) {
                        v12 = Boolean.TRUE;
                        return v12;
                    }
                    v12 = Boolean.FALSE;
                    return v12;
                }
                if (var1_1.isPrimitive()) {
                    return MethodResolver.convertDoubleToNumber(var2_2.num(), var1_1);
                }
                var4_8 = ((XRTreeFrag)var2_2).asNodeIterator();
                var6_16 = var4_8.getDTM(var5_12 = var4_8.nextNode());
                var7_18 = var6_16.getNode(var6_16.getFirstChild(var5_12));
                if (var1_1.isAssignableFrom(var7_18.getClass()) == false) return null;
                return var7_18;
            }
            case 4: {
                v13 = MethodResolver.class$org$w3c$dom$traversal$NodeIterator == null ? (MethodResolver.class$org$w3c$dom$traversal$NodeIterator = MethodResolver.class$("org.w3c.dom.traversal.NodeIterator")) : MethodResolver.class$org$w3c$dom$traversal$NodeIterator;
                if (var1_1 == v13) return var2_2.nodeset();
                v14 = MethodResolver.class$java$lang$Object == null ? (MethodResolver.class$java$lang$Object = MethodResolver.class$("java.lang.Object")) : MethodResolver.class$java$lang$Object;
                if (var1_1 == v14) {
                    return var2_2.nodeset();
                }
                v15 = MethodResolver.class$org$w3c$dom$NodeList == null ? (MethodResolver.class$org$w3c$dom$NodeList = MethodResolver.class$("org.w3c.dom.NodeList")) : MethodResolver.class$org$w3c$dom$NodeList;
                if (var1_1 == v15) {
                    return var2_2.nodelist();
                }
                v16 = MethodResolver.class$org$w3c$dom$Node == null ? (MethodResolver.class$org$w3c$dom$Node = MethodResolver.class$("org.w3c.dom.Node")) : MethodResolver.class$org$w3c$dom$Node;
                if (var1_1 == v16) {
                    var4_9 = var2_2.iter();
                    var5_13 = var4_9.nextNode();
                    if (var5_13 == -1) return null;
                    return var4_9.getDTM(var5_13).getNode(var5_13);
                }
                v17 = MethodResolver.class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : MethodResolver.class$java$lang$String;
                if (var1_1 == v17) {
                    return var2_2.str();
                }
                if (var1_1 == Boolean.TYPE) {
                    if (var2_2.bool()) {
                        v18 = Boolean.TRUE;
                        return v18;
                    }
                    v18 = Boolean.FALSE;
                    return v18;
                }
                if (var1_1.isPrimitive()) {
                    return MethodResolver.convertDoubleToNumber(var2_2.num(), var1_1);
                }
                var4_10 = var2_2.iter();
                var6_17 = var4_10.getDTM(var5_14 = var4_10.nextNode());
                var7_19 = var6_17.getNode(var5_14);
                if (var1_1.isAssignableFrom(var7_19.getClass()) == false) return null;
                return var7_19;
            }
        }
        var0 = var2_2.object();
lbl106: // 2 sources:
        if (null == var0) return var0;
        v19 = MethodResolver.class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : MethodResolver.class$java$lang$String;
        if (var1_1 == v19) {
            return var0.toString();
        }
        if (var1_1.isPrimitive()) {
            var2_2 = new XString(var0.toString());
            var3_4 = var2_2.num();
            return MethodResolver.convertDoubleToNumber(var3_4, var1_1);
        }
        v20 = MethodResolver.class$java$lang$Class == null ? (MethodResolver.class$java$lang$Class = MethodResolver.class$("java.lang.Class")) : MethodResolver.class$java$lang$Class;
        if (var1_1 != v20) return var0;
        return var0.getClass();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static Object convertDoubleToNumber(double d2, Class class_) {
        if (class_ == Double.TYPE) return new Double(d2);
        Class class_2 = class$java$lang$Double == null ? (MethodResolver.class$java$lang$Double = MethodResolver.class$("java.lang.Double")) : class$java$lang$Double;
        if (class_ == class_2) {
            return new Double(d2);
        }
        if (class_ == Float.TYPE) {
            return new Float(d2);
        }
        if (class_ == Long.TYPE) {
            return new Long((long)d2);
        }
        if (class_ == Integer.TYPE) {
            return new Integer((int)d2);
        }
        if (class_ == Short.TYPE) {
            return new Short((short)d2);
        }
        if (class_ == Character.TYPE) {
            return new Character((char)d2);
        }
        if (class_ != Byte.TYPE) return new Double(d2);
        return new Byte((byte)d2);
    }

    private static String errString(String string, String string2, Class class_, String string3, int n2, Object[] arrobject) {
        String string4 = "For extension " + string + ", could not find " + string2 + " ";
        switch (n2) {
            case 1: {
                return string4 + "static " + class_.getName() + "." + string3 + "([ExpressionContext,] " + MethodResolver.errArgs(arrobject, 0) + ").";
            }
            case 2: {
                return string4 + class_.getName() + "." + string3 + "([ExpressionContext,] " + MethodResolver.errArgs(arrobject, 0) + ").";
            }
            case 3: {
                return string4 + class_.getName() + "." + string3 + "([ExpressionContext,] " + MethodResolver.errArgs(arrobject, 0) + ").\n" + "Checked both static and instance methods.";
            }
            case 4: {
                return string4 + "static " + class_.getName() + "." + string3 + "([ExpressionContext, ]" + MethodResolver.errArgs(arrobject, 0) + ") nor\n" + class_ + "." + string3 + "([ExpressionContext,] " + MethodResolver.errArgs(arrobject, 1) + ").";
            }
        }
        if (string.equals("function")) {
            return string4 + class_.getName() + "([ExpressionContext,] " + MethodResolver.errArgs(arrobject, 0) + ").";
        }
        return string4 + class_.getName() + "." + string3 + "(org.apache.xalan.extensions.XSLProcessorContext, " + "org.apache.xalan.templates.ElemExtensionCall).";
    }

    private static String errArgs(Object[] arrobject, int n2) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = n2; i2 < arrobject.length; ++i2) {
            if (i2 != n2) {
                stringBuffer.append(", ");
            }
            if (arrobject[i2] instanceof XObject) {
                stringBuffer.append(((XObject)arrobject[i2]).getTypeString());
                continue;
            }
            stringBuffer.append(arrobject[i2].getClass().getName());
        }
        return stringBuffer.toString();
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static {
        ConversionInfo[] arrconversionInfo = new ConversionInfo[8];
        arrconversionInfo[0] = new ConversionInfo(Double.TYPE, 11);
        arrconversionInfo[1] = new ConversionInfo(Float.TYPE, 12);
        arrconversionInfo[2] = new ConversionInfo(Long.TYPE, 13);
        arrconversionInfo[3] = new ConversionInfo(Integer.TYPE, 14);
        arrconversionInfo[4] = new ConversionInfo(Short.TYPE, 15);
        arrconversionInfo[5] = new ConversionInfo(Character.TYPE, 16);
        arrconversionInfo[6] = new ConversionInfo(Byte.TYPE, 17);
        Class class_ = class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : class$java$lang$String;
        arrconversionInfo[7] = new ConversionInfo(class_, 18);
        m_javaObjConversions = arrconversionInfo;
        ConversionInfo[] arrconversionInfo2 = new ConversionInfo[4];
        arrconversionInfo2[0] = new ConversionInfo(Boolean.TYPE, 0);
        Class class_2 = class$java$lang$Boolean == null ? (MethodResolver.class$java$lang$Boolean = MethodResolver.class$("java.lang.Boolean")) : class$java$lang$Boolean;
        arrconversionInfo2[1] = new ConversionInfo(class_2, 1);
        Class class_3 = class$java$lang$Object == null ? (MethodResolver.class$java$lang$Object = MethodResolver.class$("java.lang.Object")) : class$java$lang$Object;
        arrconversionInfo2[2] = new ConversionInfo(class_3, 2);
        arrconversionInfo2[3] = new ConversionInfo(class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : class$java$lang$String, 3);
        m_booleanConversions = arrconversionInfo2;
        ConversionInfo[] arrconversionInfo3 = new ConversionInfo[11];
        arrconversionInfo3[0] = new ConversionInfo(Double.TYPE, 0);
        Class class_4 = class$java$lang$Double == null ? (MethodResolver.class$java$lang$Double = MethodResolver.class$("java.lang.Double")) : class$java$lang$Double;
        arrconversionInfo3[1] = new ConversionInfo(class_4, 1);
        arrconversionInfo3[2] = new ConversionInfo(Float.TYPE, 3);
        arrconversionInfo3[3] = new ConversionInfo(Long.TYPE, 4);
        arrconversionInfo3[4] = new ConversionInfo(Integer.TYPE, 5);
        arrconversionInfo3[5] = new ConversionInfo(Short.TYPE, 6);
        arrconversionInfo3[6] = new ConversionInfo(Character.TYPE, 7);
        arrconversionInfo3[7] = new ConversionInfo(Byte.TYPE, 8);
        arrconversionInfo3[8] = new ConversionInfo(Boolean.TYPE, 9);
        arrconversionInfo3[9] = new ConversionInfo(class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : class$java$lang$String, 10);
        arrconversionInfo3[10] = new ConversionInfo(class$java$lang$Object == null ? (MethodResolver.class$java$lang$Object = MethodResolver.class$("java.lang.Object")) : class$java$lang$Object, 11);
        m_numberConversions = arrconversionInfo3;
        ConversionInfo[] arrconversionInfo4 = new ConversionInfo[10];
        arrconversionInfo4[0] = new ConversionInfo(class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : class$java$lang$String, 0);
        arrconversionInfo4[1] = new ConversionInfo(class$java$lang$Object == null ? (MethodResolver.class$java$lang$Object = MethodResolver.class$("java.lang.Object")) : class$java$lang$Object, 1);
        arrconversionInfo4[2] = new ConversionInfo(Character.TYPE, 2);
        arrconversionInfo4[3] = new ConversionInfo(Double.TYPE, 3);
        arrconversionInfo4[4] = new ConversionInfo(Float.TYPE, 3);
        arrconversionInfo4[5] = new ConversionInfo(Long.TYPE, 3);
        arrconversionInfo4[6] = new ConversionInfo(Integer.TYPE, 3);
        arrconversionInfo4[7] = new ConversionInfo(Short.TYPE, 3);
        arrconversionInfo4[8] = new ConversionInfo(Byte.TYPE, 3);
        arrconversionInfo4[9] = new ConversionInfo(Boolean.TYPE, 4);
        m_stringConversions = arrconversionInfo4;
        ConversionInfo[] arrconversionInfo5 = new ConversionInfo[13];
        Class class_5 = class$org$w3c$dom$traversal$NodeIterator == null ? (MethodResolver.class$org$w3c$dom$traversal$NodeIterator = MethodResolver.class$("org.w3c.dom.traversal.NodeIterator")) : class$org$w3c$dom$traversal$NodeIterator;
        arrconversionInfo5[0] = new ConversionInfo(class_5, 0);
        Class class_6 = class$org$w3c$dom$NodeList == null ? (MethodResolver.class$org$w3c$dom$NodeList = MethodResolver.class$("org.w3c.dom.NodeList")) : class$org$w3c$dom$NodeList;
        arrconversionInfo5[1] = new ConversionInfo(class_6, 1);
        Class class_7 = class$org$w3c$dom$Node == null ? (MethodResolver.class$org$w3c$dom$Node = MethodResolver.class$("org.w3c.dom.Node")) : class$org$w3c$dom$Node;
        arrconversionInfo5[2] = new ConversionInfo(class_7, 2);
        arrconversionInfo5[3] = new ConversionInfo(class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : class$java$lang$String, 3);
        arrconversionInfo5[4] = new ConversionInfo(class$java$lang$Object == null ? (MethodResolver.class$java$lang$Object = MethodResolver.class$("java.lang.Object")) : class$java$lang$Object, 5);
        arrconversionInfo5[5] = new ConversionInfo(Character.TYPE, 6);
        arrconversionInfo5[6] = new ConversionInfo(Double.TYPE, 7);
        arrconversionInfo5[7] = new ConversionInfo(Float.TYPE, 7);
        arrconversionInfo5[8] = new ConversionInfo(Long.TYPE, 7);
        arrconversionInfo5[9] = new ConversionInfo(Integer.TYPE, 7);
        arrconversionInfo5[10] = new ConversionInfo(Short.TYPE, 7);
        arrconversionInfo5[11] = new ConversionInfo(Byte.TYPE, 7);
        arrconversionInfo5[12] = new ConversionInfo(Boolean.TYPE, 8);
        m_rtfConversions = arrconversionInfo5;
        ConversionInfo[] arrconversionInfo6 = new ConversionInfo[13];
        arrconversionInfo6[0] = new ConversionInfo(class$org$w3c$dom$traversal$NodeIterator == null ? (MethodResolver.class$org$w3c$dom$traversal$NodeIterator = MethodResolver.class$("org.w3c.dom.traversal.NodeIterator")) : class$org$w3c$dom$traversal$NodeIterator, 0);
        arrconversionInfo6[1] = new ConversionInfo(class$org$w3c$dom$NodeList == null ? (MethodResolver.class$org$w3c$dom$NodeList = MethodResolver.class$("org.w3c.dom.NodeList")) : class$org$w3c$dom$NodeList, 1);
        arrconversionInfo6[2] = new ConversionInfo(class$org$w3c$dom$Node == null ? (MethodResolver.class$org$w3c$dom$Node = MethodResolver.class$("org.w3c.dom.Node")) : class$org$w3c$dom$Node, 2);
        arrconversionInfo6[3] = new ConversionInfo(class$java$lang$String == null ? (MethodResolver.class$java$lang$String = MethodResolver.class$("java.lang.String")) : class$java$lang$String, 3);
        arrconversionInfo6[4] = new ConversionInfo(class$java$lang$Object == null ? (MethodResolver.class$java$lang$Object = MethodResolver.class$("java.lang.Object")) : class$java$lang$Object, 5);
        arrconversionInfo6[5] = new ConversionInfo(Character.TYPE, 6);
        arrconversionInfo6[6] = new ConversionInfo(Double.TYPE, 7);
        arrconversionInfo6[7] = new ConversionInfo(Float.TYPE, 7);
        arrconversionInfo6[8] = new ConversionInfo(Long.TYPE, 7);
        arrconversionInfo6[9] = new ConversionInfo(Integer.TYPE, 7);
        arrconversionInfo6[10] = new ConversionInfo(Short.TYPE, 7);
        arrconversionInfo6[11] = new ConversionInfo(Byte.TYPE, 7);
        arrconversionInfo6[12] = new ConversionInfo(Boolean.TYPE, 8);
        m_nodesetConversions = arrconversionInfo6;
        m_conversions = new ConversionInfo[][]{m_javaObjConversions, m_booleanConversions, m_numberConversions, m_stringConversions, m_nodesetConversions, m_rtfConversions};
    }

    static class ConversionInfo {
        Class m_class;
        int m_score;

        ConversionInfo(Class class_, int n2) {
            this.m_class = class_;
            this.m_score = n2;
        }
    }

}

