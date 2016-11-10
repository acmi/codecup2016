/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.compiler;

import java.util.HashMap;
import javax.xml.transform.TransformerException;
import org.apache.xpath.functions.Function;

public class FunctionTable {
    private static Class[] m_functions;
    private static HashMap m_functionID;
    private Class[] m_functions_customer = new Class[30];
    private HashMap m_functionID_customer = new HashMap();
    private int m_funcNextFreeIndex = 37;
    static Class class$org$apache$xpath$functions$FuncCurrent;
    static Class class$org$apache$xpath$functions$FuncLast;
    static Class class$org$apache$xpath$functions$FuncPosition;
    static Class class$org$apache$xpath$functions$FuncCount;
    static Class class$org$apache$xpath$functions$FuncId;
    static Class class$org$apache$xalan$templates$FuncKey;
    static Class class$org$apache$xpath$functions$FuncLocalPart;
    static Class class$org$apache$xpath$functions$FuncNamespace;
    static Class class$org$apache$xpath$functions$FuncQname;
    static Class class$org$apache$xpath$functions$FuncGenerateId;
    static Class class$org$apache$xpath$functions$FuncNot;
    static Class class$org$apache$xpath$functions$FuncTrue;
    static Class class$org$apache$xpath$functions$FuncFalse;
    static Class class$org$apache$xpath$functions$FuncBoolean;
    static Class class$org$apache$xpath$functions$FuncLang;
    static Class class$org$apache$xpath$functions$FuncNumber;
    static Class class$org$apache$xpath$functions$FuncFloor;
    static Class class$org$apache$xpath$functions$FuncCeiling;
    static Class class$org$apache$xpath$functions$FuncRound;
    static Class class$org$apache$xpath$functions$FuncSum;
    static Class class$org$apache$xpath$functions$FuncString;
    static Class class$org$apache$xpath$functions$FuncStartsWith;
    static Class class$org$apache$xpath$functions$FuncContains;
    static Class class$org$apache$xpath$functions$FuncSubstringBefore;
    static Class class$org$apache$xpath$functions$FuncSubstringAfter;
    static Class class$org$apache$xpath$functions$FuncNormalizeSpace;
    static Class class$org$apache$xpath$functions$FuncTranslate;
    static Class class$org$apache$xpath$functions$FuncConcat;
    static Class class$org$apache$xpath$functions$FuncSystemProperty;
    static Class class$org$apache$xpath$functions$FuncExtFunctionAvailable;
    static Class class$org$apache$xpath$functions$FuncExtElementAvailable;
    static Class class$org$apache$xpath$functions$FuncSubstring;
    static Class class$org$apache$xpath$functions$FuncStringLength;
    static Class class$org$apache$xpath$functions$FuncDoclocation;
    static Class class$org$apache$xpath$functions$FuncUnparsedEntityURI;

    String getFunctionName(int n2) {
        if (n2 < 37) {
            return m_functions[n2].getName();
        }
        return this.m_functions_customer[n2 - 37].getName();
    }

    Function getFunction(int n2) throws TransformerException {
        try {
            if (n2 < 37) {
                return (Function)m_functions[n2].newInstance();
            }
            return (Function)this.m_functions_customer[n2 - 37].newInstance();
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new TransformerException(illegalAccessException.getMessage());
        }
        catch (InstantiationException instantiationException) {
            throw new TransformerException(instantiationException.getMessage());
        }
    }

    Object getFunctionID(String string) {
        Object v2 = this.m_functionID_customer.get(string);
        if (null == v2) {
            v2 = m_functionID.get(string);
        }
        return v2;
    }

    public int installFunction(String string, Class class_) {
        int n2;
        Object object = this.getFunctionID(string);
        if (null != object) {
            n2 = (Integer)object;
            if (n2 < 37) {
                n2 = this.m_funcNextFreeIndex++;
                this.m_functionID_customer.put(string, new Integer(n2));
            }
            this.m_functions_customer[n2 - 37] = class_;
        } else {
            n2 = this.m_funcNextFreeIndex++;
            this.m_functions_customer[n2 - 37] = class_;
            this.m_functionID_customer.put(string, new Integer(n2));
        }
        return n2;
    }

    public boolean functionAvailable(String string) {
        Object v2 = m_functionID.get(string);
        if (null != v2) {
            return true;
        }
        v2 = this.m_functionID_customer.get(string);
        return null != v2;
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
        m_functionID = new HashMap();
        m_functions = new Class[37];
        Class class_ = class$org$apache$xpath$functions$FuncCurrent == null ? (FunctionTable.class$org$apache$xpath$functions$FuncCurrent = FunctionTable.class$("org.apache.xpath.functions.FuncCurrent")) : class$org$apache$xpath$functions$FuncCurrent;
        FunctionTable.m_functions[0] = class_;
        Class class_2 = class$org$apache$xpath$functions$FuncLast == null ? (FunctionTable.class$org$apache$xpath$functions$FuncLast = FunctionTable.class$("org.apache.xpath.functions.FuncLast")) : class$org$apache$xpath$functions$FuncLast;
        FunctionTable.m_functions[1] = class_2;
        Class class_3 = class$org$apache$xpath$functions$FuncPosition == null ? (FunctionTable.class$org$apache$xpath$functions$FuncPosition = FunctionTable.class$("org.apache.xpath.functions.FuncPosition")) : class$org$apache$xpath$functions$FuncPosition;
        FunctionTable.m_functions[2] = class_3;
        Class class_4 = class$org$apache$xpath$functions$FuncCount == null ? (FunctionTable.class$org$apache$xpath$functions$FuncCount = FunctionTable.class$("org.apache.xpath.functions.FuncCount")) : class$org$apache$xpath$functions$FuncCount;
        FunctionTable.m_functions[3] = class_4;
        Class class_5 = class$org$apache$xpath$functions$FuncId == null ? (FunctionTable.class$org$apache$xpath$functions$FuncId = FunctionTable.class$("org.apache.xpath.functions.FuncId")) : class$org$apache$xpath$functions$FuncId;
        FunctionTable.m_functions[4] = class_5;
        Class class_6 = class$org$apache$xalan$templates$FuncKey == null ? (FunctionTable.class$org$apache$xalan$templates$FuncKey = FunctionTable.class$("org.apache.xalan.templates.FuncKey")) : class$org$apache$xalan$templates$FuncKey;
        FunctionTable.m_functions[5] = class_6;
        Class class_7 = class$org$apache$xpath$functions$FuncLocalPart == null ? (FunctionTable.class$org$apache$xpath$functions$FuncLocalPart = FunctionTable.class$("org.apache.xpath.functions.FuncLocalPart")) : class$org$apache$xpath$functions$FuncLocalPart;
        FunctionTable.m_functions[7] = class_7;
        Class class_8 = class$org$apache$xpath$functions$FuncNamespace == null ? (FunctionTable.class$org$apache$xpath$functions$FuncNamespace = FunctionTable.class$("org.apache.xpath.functions.FuncNamespace")) : class$org$apache$xpath$functions$FuncNamespace;
        FunctionTable.m_functions[8] = class_8;
        Class class_9 = class$org$apache$xpath$functions$FuncQname == null ? (FunctionTable.class$org$apache$xpath$functions$FuncQname = FunctionTable.class$("org.apache.xpath.functions.FuncQname")) : class$org$apache$xpath$functions$FuncQname;
        FunctionTable.m_functions[9] = class_9;
        Class class_10 = class$org$apache$xpath$functions$FuncGenerateId == null ? (FunctionTable.class$org$apache$xpath$functions$FuncGenerateId = FunctionTable.class$("org.apache.xpath.functions.FuncGenerateId")) : class$org$apache$xpath$functions$FuncGenerateId;
        FunctionTable.m_functions[10] = class_10;
        Class class_11 = class$org$apache$xpath$functions$FuncNot == null ? (FunctionTable.class$org$apache$xpath$functions$FuncNot = FunctionTable.class$("org.apache.xpath.functions.FuncNot")) : class$org$apache$xpath$functions$FuncNot;
        FunctionTable.m_functions[11] = class_11;
        Class class_12 = class$org$apache$xpath$functions$FuncTrue == null ? (FunctionTable.class$org$apache$xpath$functions$FuncTrue = FunctionTable.class$("org.apache.xpath.functions.FuncTrue")) : class$org$apache$xpath$functions$FuncTrue;
        FunctionTable.m_functions[12] = class_12;
        Class class_13 = class$org$apache$xpath$functions$FuncFalse == null ? (FunctionTable.class$org$apache$xpath$functions$FuncFalse = FunctionTable.class$("org.apache.xpath.functions.FuncFalse")) : class$org$apache$xpath$functions$FuncFalse;
        FunctionTable.m_functions[13] = class_13;
        Class class_14 = class$org$apache$xpath$functions$FuncBoolean == null ? (FunctionTable.class$org$apache$xpath$functions$FuncBoolean = FunctionTable.class$("org.apache.xpath.functions.FuncBoolean")) : class$org$apache$xpath$functions$FuncBoolean;
        FunctionTable.m_functions[14] = class_14;
        Class class_15 = class$org$apache$xpath$functions$FuncLang == null ? (FunctionTable.class$org$apache$xpath$functions$FuncLang = FunctionTable.class$("org.apache.xpath.functions.FuncLang")) : class$org$apache$xpath$functions$FuncLang;
        FunctionTable.m_functions[32] = class_15;
        Class class_16 = class$org$apache$xpath$functions$FuncNumber == null ? (FunctionTable.class$org$apache$xpath$functions$FuncNumber = FunctionTable.class$("org.apache.xpath.functions.FuncNumber")) : class$org$apache$xpath$functions$FuncNumber;
        FunctionTable.m_functions[15] = class_16;
        Class class_17 = class$org$apache$xpath$functions$FuncFloor == null ? (FunctionTable.class$org$apache$xpath$functions$FuncFloor = FunctionTable.class$("org.apache.xpath.functions.FuncFloor")) : class$org$apache$xpath$functions$FuncFloor;
        FunctionTable.m_functions[16] = class_17;
        Class class_18 = class$org$apache$xpath$functions$FuncCeiling == null ? (FunctionTable.class$org$apache$xpath$functions$FuncCeiling = FunctionTable.class$("org.apache.xpath.functions.FuncCeiling")) : class$org$apache$xpath$functions$FuncCeiling;
        FunctionTable.m_functions[17] = class_18;
        Class class_19 = class$org$apache$xpath$functions$FuncRound == null ? (FunctionTable.class$org$apache$xpath$functions$FuncRound = FunctionTable.class$("org.apache.xpath.functions.FuncRound")) : class$org$apache$xpath$functions$FuncRound;
        FunctionTable.m_functions[18] = class_19;
        Class class_20 = class$org$apache$xpath$functions$FuncSum == null ? (FunctionTable.class$org$apache$xpath$functions$FuncSum = FunctionTable.class$("org.apache.xpath.functions.FuncSum")) : class$org$apache$xpath$functions$FuncSum;
        FunctionTable.m_functions[19] = class_20;
        Class class_21 = class$org$apache$xpath$functions$FuncString == null ? (FunctionTable.class$org$apache$xpath$functions$FuncString = FunctionTable.class$("org.apache.xpath.functions.FuncString")) : class$org$apache$xpath$functions$FuncString;
        FunctionTable.m_functions[20] = class_21;
        Class class_22 = class$org$apache$xpath$functions$FuncStartsWith == null ? (FunctionTable.class$org$apache$xpath$functions$FuncStartsWith = FunctionTable.class$("org.apache.xpath.functions.FuncStartsWith")) : class$org$apache$xpath$functions$FuncStartsWith;
        FunctionTable.m_functions[21] = class_22;
        Class class_23 = class$org$apache$xpath$functions$FuncContains == null ? (FunctionTable.class$org$apache$xpath$functions$FuncContains = FunctionTable.class$("org.apache.xpath.functions.FuncContains")) : class$org$apache$xpath$functions$FuncContains;
        FunctionTable.m_functions[22] = class_23;
        Class class_24 = class$org$apache$xpath$functions$FuncSubstringBefore == null ? (FunctionTable.class$org$apache$xpath$functions$FuncSubstringBefore = FunctionTable.class$("org.apache.xpath.functions.FuncSubstringBefore")) : class$org$apache$xpath$functions$FuncSubstringBefore;
        FunctionTable.m_functions[23] = class_24;
        Class class_25 = class$org$apache$xpath$functions$FuncSubstringAfter == null ? (FunctionTable.class$org$apache$xpath$functions$FuncSubstringAfter = FunctionTable.class$("org.apache.xpath.functions.FuncSubstringAfter")) : class$org$apache$xpath$functions$FuncSubstringAfter;
        FunctionTable.m_functions[24] = class_25;
        Class class_26 = class$org$apache$xpath$functions$FuncNormalizeSpace == null ? (FunctionTable.class$org$apache$xpath$functions$FuncNormalizeSpace = FunctionTable.class$("org.apache.xpath.functions.FuncNormalizeSpace")) : class$org$apache$xpath$functions$FuncNormalizeSpace;
        FunctionTable.m_functions[25] = class_26;
        Class class_27 = class$org$apache$xpath$functions$FuncTranslate == null ? (FunctionTable.class$org$apache$xpath$functions$FuncTranslate = FunctionTable.class$("org.apache.xpath.functions.FuncTranslate")) : class$org$apache$xpath$functions$FuncTranslate;
        FunctionTable.m_functions[26] = class_27;
        Class class_28 = class$org$apache$xpath$functions$FuncConcat == null ? (FunctionTable.class$org$apache$xpath$functions$FuncConcat = FunctionTable.class$("org.apache.xpath.functions.FuncConcat")) : class$org$apache$xpath$functions$FuncConcat;
        FunctionTable.m_functions[27] = class_28;
        Class class_29 = class$org$apache$xpath$functions$FuncSystemProperty == null ? (FunctionTable.class$org$apache$xpath$functions$FuncSystemProperty = FunctionTable.class$("org.apache.xpath.functions.FuncSystemProperty")) : class$org$apache$xpath$functions$FuncSystemProperty;
        FunctionTable.m_functions[31] = class_29;
        Class class_30 = class$org$apache$xpath$functions$FuncExtFunctionAvailable == null ? (FunctionTable.class$org$apache$xpath$functions$FuncExtFunctionAvailable = FunctionTable.class$("org.apache.xpath.functions.FuncExtFunctionAvailable")) : class$org$apache$xpath$functions$FuncExtFunctionAvailable;
        FunctionTable.m_functions[33] = class_30;
        Class class_31 = class$org$apache$xpath$functions$FuncExtElementAvailable == null ? (FunctionTable.class$org$apache$xpath$functions$FuncExtElementAvailable = FunctionTable.class$("org.apache.xpath.functions.FuncExtElementAvailable")) : class$org$apache$xpath$functions$FuncExtElementAvailable;
        FunctionTable.m_functions[34] = class_31;
        Class class_32 = class$org$apache$xpath$functions$FuncSubstring == null ? (FunctionTable.class$org$apache$xpath$functions$FuncSubstring = FunctionTable.class$("org.apache.xpath.functions.FuncSubstring")) : class$org$apache$xpath$functions$FuncSubstring;
        FunctionTable.m_functions[29] = class_32;
        Class class_33 = class$org$apache$xpath$functions$FuncStringLength == null ? (FunctionTable.class$org$apache$xpath$functions$FuncStringLength = FunctionTable.class$("org.apache.xpath.functions.FuncStringLength")) : class$org$apache$xpath$functions$FuncStringLength;
        FunctionTable.m_functions[30] = class_33;
        Class class_34 = class$org$apache$xpath$functions$FuncDoclocation == null ? (FunctionTable.class$org$apache$xpath$functions$FuncDoclocation = FunctionTable.class$("org.apache.xpath.functions.FuncDoclocation")) : class$org$apache$xpath$functions$FuncDoclocation;
        FunctionTable.m_functions[35] = class_34;
        Class class_35 = class$org$apache$xpath$functions$FuncUnparsedEntityURI == null ? (FunctionTable.class$org$apache$xpath$functions$FuncUnparsedEntityURI = FunctionTable.class$("org.apache.xpath.functions.FuncUnparsedEntityURI")) : class$org$apache$xpath$functions$FuncUnparsedEntityURI;
        FunctionTable.m_functions[36] = class_35;
        m_functionID.put("current", new Integer(0));
        m_functionID.put("last", new Integer(1));
        m_functionID.put("position", new Integer(2));
        m_functionID.put("count", new Integer(3));
        m_functionID.put("id", new Integer(4));
        m_functionID.put("key", new Integer(5));
        m_functionID.put("local-name", new Integer(7));
        m_functionID.put("namespace-uri", new Integer(8));
        m_functionID.put("name", new Integer(9));
        m_functionID.put("generate-id", new Integer(10));
        m_functionID.put("not", new Integer(11));
        m_functionID.put("true", new Integer(12));
        m_functionID.put("false", new Integer(13));
        m_functionID.put("boolean", new Integer(14));
        m_functionID.put("lang", new Integer(32));
        m_functionID.put("number", new Integer(15));
        m_functionID.put("floor", new Integer(16));
        m_functionID.put("ceiling", new Integer(17));
        m_functionID.put("round", new Integer(18));
        m_functionID.put("sum", new Integer(19));
        m_functionID.put("string", new Integer(20));
        m_functionID.put("starts-with", new Integer(21));
        m_functionID.put("contains", new Integer(22));
        m_functionID.put("substring-before", new Integer(23));
        m_functionID.put("substring-after", new Integer(24));
        m_functionID.put("normalize-space", new Integer(25));
        m_functionID.put("translate", new Integer(26));
        m_functionID.put("concat", new Integer(27));
        m_functionID.put("system-property", new Integer(31));
        m_functionID.put("function-available", new Integer(33));
        m_functionID.put("element-available", new Integer(34));
        m_functionID.put("substring", new Integer(29));
        m_functionID.put("string-length", new Integer(30));
        m_functionID.put("unparsed-entity-uri", new Integer(36));
        m_functionID.put("document-location", new Integer(35));
    }
}

