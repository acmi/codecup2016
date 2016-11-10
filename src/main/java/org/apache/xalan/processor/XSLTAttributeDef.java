/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.StringToIntTable;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.XML11Char;
import org.apache.xpath.XPath;
import org.xml.sax.SAXException;

public class XSLTAttributeDef {
    static final int FATAL = 0;
    static final int ERROR = 1;
    static final int WARNING = 2;
    static final int T_CDATA = 1;
    static final int T_URL = 2;
    static final int T_AVT = 3;
    static final int T_PATTERN = 4;
    static final int T_EXPR = 5;
    static final int T_CHAR = 6;
    static final int T_NUMBER = 7;
    static final int T_YESNO = 8;
    static final int T_QNAME = 9;
    static final int T_QNAMES = 10;
    static final int T_ENUM = 11;
    static final int T_SIMPLEPATTERNLIST = 12;
    static final int T_NMTOKEN = 13;
    static final int T_STRINGLIST = 14;
    static final int T_PREFIX_URLLIST = 15;
    static final int T_ENUM_OR_PQNAME = 16;
    static final int T_NCNAME = 17;
    static final int T_AVT_QNAME = 18;
    static final int T_QNAMES_RESOLVE_NULL = 19;
    static final int T_PREFIXLIST = 20;
    static final XSLTAttributeDef m_foreignAttr = new XSLTAttributeDef("*", "*", 1, false, false, 2);
    static final String S_FOREIGNATTR_SETTER = "setForeignAttr";
    private String m_namespace;
    private String m_name;
    private int m_type;
    private StringToIntTable m_enums;
    private String m_default;
    private boolean m_required;
    private boolean m_supportsAVT;
    int m_errorType = 2;
    String m_setterString = null;
    static Class class$org$apache$xpath$XPath;
    static Class class$java$lang$Double;
    static Class class$java$lang$Float;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Character;
    static Class class$java$lang$Short;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;

    XSLTAttributeDef(String string, String string2, int n2, boolean bl, boolean bl2, int n3) {
        this.m_namespace = string;
        this.m_name = string2;
        this.m_type = n2;
        this.m_required = bl;
        this.m_supportsAVT = bl2;
        this.m_errorType = n3;
    }

    XSLTAttributeDef(String string, String string2, int n2, boolean bl, int n3, String string3) {
        this.m_namespace = string;
        this.m_name = string2;
        this.m_type = n2;
        this.m_required = false;
        this.m_supportsAVT = bl;
        this.m_errorType = n3;
        this.m_default = string3;
    }

    XSLTAttributeDef(String string, String string2, boolean bl, boolean bl2, boolean bl3, int n2, String string3, int n3, String string4, int n4) {
        this.m_namespace = string;
        this.m_name = string2;
        this.m_type = bl3 ? 16 : 11;
        this.m_required = bl;
        this.m_supportsAVT = bl2;
        this.m_errorType = n2;
        this.m_enums = new StringToIntTable(2);
        this.m_enums.put(string3, n3);
        this.m_enums.put(string4, n4);
    }

    XSLTAttributeDef(String string, String string2, boolean bl, boolean bl2, boolean bl3, int n2, String string3, int n3, String string4, int n4, String string5, int n5) {
        this.m_namespace = string;
        this.m_name = string2;
        this.m_type = bl3 ? 16 : 11;
        this.m_required = bl;
        this.m_supportsAVT = bl2;
        this.m_errorType = n2;
        this.m_enums = new StringToIntTable(3);
        this.m_enums.put(string3, n3);
        this.m_enums.put(string4, n4);
        this.m_enums.put(string5, n5);
    }

    XSLTAttributeDef(String string, String string2, boolean bl, boolean bl2, boolean bl3, int n2, String string3, int n3, String string4, int n4, String string5, int n5, String string6, int n6) {
        this.m_namespace = string;
        this.m_name = string2;
        this.m_type = bl3 ? 16 : 11;
        this.m_required = bl;
        this.m_supportsAVT = bl2;
        this.m_errorType = n2;
        this.m_enums = new StringToIntTable(4);
        this.m_enums.put(string3, n3);
        this.m_enums.put(string4, n4);
        this.m_enums.put(string5, n5);
        this.m_enums.put(string6, n6);
    }

    String getNamespace() {
        return this.m_namespace;
    }

    String getName() {
        return this.m_name;
    }

    int getType() {
        return this.m_type;
    }

    private int getEnum(String string) {
        return this.m_enums.get(string);
    }

    private String[] getEnumNames() {
        return this.m_enums.keys();
    }

    String getDefault() {
        return this.m_default;
    }

    void setDefault(String string) {
        this.m_default = string;
    }

    boolean getRequired() {
        return this.m_required;
    }

    boolean getSupportsAVT() {
        return this.m_supportsAVT;
    }

    int getErrorType() {
        return this.m_errorType;
    }

    public String getSetterMethodName() {
        if (null == this.m_setterString) {
            if (m_foreignAttr == this) {
                return "setForeignAttr";
            }
            if (this.m_name.equals("*")) {
                this.m_setterString = "addLiteralResultAttribute";
                return this.m_setterString;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("set");
            if (this.m_namespace != null && this.m_namespace.equals("http://www.w3.org/XML/1998/namespace")) {
                stringBuffer.append("Xml");
            }
            int n2 = this.m_name.length();
            for (int i2 = 0; i2 < n2; ++i2) {
                char c2 = this.m_name.charAt(i2);
                if ('-' == c2) {
                    c2 = this.m_name.charAt(++i2);
                    c2 = Character.toUpperCase(c2);
                } else if (0 == i2) {
                    c2 = Character.toUpperCase(c2);
                }
                stringBuffer.append(c2);
            }
            this.m_setterString = stringBuffer.toString();
        }
        return this.m_setterString;
    }

    AVT processAVT(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        try {
            AVT aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
            return aVT;
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    Object processCDATA(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        if (this.getSupportsAVT()) {
            try {
                AVT aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                return aVT;
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
        }
        return string4;
    }

    Object processCHAR(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        if (this.getSupportsAVT()) {
            try {
                AVT aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                if (aVT.isSimple() && string4.length() != 1) {
                    this.handleError(stylesheetHandler, "INVALID_TCHAR", new Object[]{string2, string4}, null);
                    return null;
                }
                return aVT;
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
        }
        if (string4.length() != 1) {
            this.handleError(stylesheetHandler, "INVALID_TCHAR", new Object[]{string2, string4}, null);
            return null;
        }
        return new Character(string4.charAt(0));
    }

    Object processENUM(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        int n2;
        AVT aVT = null;
        if (this.getSupportsAVT()) {
            try {
                aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                if (!aVT.isSimple()) {
                    return aVT;
                }
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
        }
        if ((n2 = this.getEnum(string4)) == -10000) {
            StringBuffer stringBuffer = this.getListOfEnums();
            this.handleError(stylesheetHandler, "INVALID_ENUM", new Object[]{string2, string4, stringBuffer.toString()}, null);
            return null;
        }
        if (this.getSupportsAVT()) {
            return aVT;
        }
        return new Integer(n2);
    }

    Object processENUM_OR_PQNAME(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        int n2;
        Serializable serializable = null;
        if (this.getSupportsAVT()) {
            try {
                AVT aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                if (!aVT.isSimple()) {
                    return aVT;
                }
                serializable = aVT;
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
        }
        if ((n2 = this.getEnum(string4)) != -10000) {
            if (serializable == null) {
                serializable = new Integer(n2);
            }
        } else {
            try {
                QName qName = new QName(string4, stylesheetHandler, true);
                if (serializable == null) {
                    serializable = qName;
                }
                if (qName.getPrefix() == null) {
                    StringBuffer stringBuffer = this.getListOfEnums();
                    stringBuffer.append(" <qname-but-not-ncname>");
                    this.handleError(stylesheetHandler, "INVALID_ENUM", new Object[]{string2, string4, stringBuffer.toString()}, null);
                    return null;
                }
            }
            catch (IllegalArgumentException illegalArgumentException) {
                StringBuffer stringBuffer = this.getListOfEnums();
                stringBuffer.append(" <qname-but-not-ncname>");
                this.handleError(stylesheetHandler, "INVALID_ENUM", new Object[]{string2, string4, stringBuffer.toString()}, illegalArgumentException);
                return null;
            }
            catch (RuntimeException runtimeException) {
                StringBuffer stringBuffer = this.getListOfEnums();
                stringBuffer.append(" <qname-but-not-ncname>");
                this.handleError(stylesheetHandler, "INVALID_ENUM", new Object[]{string2, string4, stringBuffer.toString()}, runtimeException);
                return null;
            }
        }
        return serializable;
    }

    Object processEXPR(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        try {
            XPath xPath = stylesheetHandler.createXPath(string4, elemTemplateElement);
            return xPath;
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    Object processNMTOKEN(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        if (this.getSupportsAVT()) {
            try {
                AVT aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                if (aVT.isSimple() && !XML11Char.isXML11ValidNmtoken(string4)) {
                    this.handleError(stylesheetHandler, "INVALID_NMTOKEN", new Object[]{string2, string4}, null);
                    return null;
                }
                return aVT;
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
        }
        if (!XML11Char.isXML11ValidNmtoken(string4)) {
            this.handleError(stylesheetHandler, "INVALID_NMTOKEN", new Object[]{string2, string4}, null);
            return null;
        }
        return string4;
    }

    Object processPATTERN(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        try {
            XPath xPath = stylesheetHandler.createMatchPatternXPath(string4, elemTemplateElement);
            return xPath;
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    Object processNUMBER(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        if (this.getSupportsAVT()) {
            AVT aVT = null;
            try {
                aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                if (aVT.isSimple()) {
                    Double d2 = Double.valueOf(string4);
                }
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
            catch (NumberFormatException numberFormatException) {
                this.handleError(stylesheetHandler, "INVALID_NUMBER", new Object[]{string2, string4}, numberFormatException);
                return null;
            }
            return aVT;
        }
        try {
            return Double.valueOf(string4);
        }
        catch (NumberFormatException numberFormatException) {
            this.handleError(stylesheetHandler, "INVALID_NUMBER", new Object[]{string2, string4}, numberFormatException);
            return null;
        }
    }

    Object processQNAME(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        try {
            QName qName = new QName(string4, stylesheetHandler, true);
            return qName;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            this.handleError(stylesheetHandler, "INVALID_QNAME", new Object[]{string2, string4}, illegalArgumentException);
            return null;
        }
        catch (RuntimeException runtimeException) {
            this.handleError(stylesheetHandler, "INVALID_QNAME", new Object[]{string2, string4}, runtimeException);
            return null;
        }
    }

    Object processAVT_QNAME(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        AVT aVT = null;
        try {
            aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
            if (aVT.isSimple()) {
                String string5;
                int n2 = string4.indexOf(58);
                if (n2 >= 0 && !XML11Char.isXML11ValidNCName(string5 = string4.substring(0, n2))) {
                    this.handleError(stylesheetHandler, "INVALID_QNAME", new Object[]{string2, string4}, null);
                    return null;
                }
                String string6 = string5 = n2 < 0 ? string4 : string4.substring(n2 + 1);
                if (string5 == null || string5.length() == 0 || !XML11Char.isXML11ValidNCName(string5)) {
                    this.handleError(stylesheetHandler, "INVALID_QNAME", new Object[]{string2, string4}, null);
                    return null;
                }
            }
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
        return aVT;
    }

    Object processNCNAME(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        if (this.getSupportsAVT()) {
            AVT aVT = null;
            try {
                aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                if (aVT.isSimple() && !XML11Char.isXML11ValidNCName(string4)) {
                    this.handleError(stylesheetHandler, "INVALID_NCNAME", new Object[]{string2, string4}, null);
                    return null;
                }
                return aVT;
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
        }
        if (!XML11Char.isXML11ValidNCName(string4)) {
            this.handleError(stylesheetHandler, "INVALID_NCNAME", new Object[]{string2, string4}, null);
            return null;
        }
        return string4;
    }

    Vector processQNAMES(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4) throws SAXException {
        StringTokenizer stringTokenizer = new StringTokenizer(string4, " \t\n\r\f");
        int n2 = stringTokenizer.countTokens();
        Vector<QName> vector = new Vector<QName>(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            vector.addElement(new QName(stringTokenizer.nextToken(), stylesheetHandler));
        }
        return vector;
    }

    final Vector processQNAMESRNU(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4) throws SAXException {
        StringTokenizer stringTokenizer = new StringTokenizer(string4, " \t\n\r\f");
        int n2 = stringTokenizer.countTokens();
        Vector<QName> vector = new Vector<QName>(n2);
        String string5 = stylesheetHandler.getNamespaceForPrefix("");
        for (int i2 = 0; i2 < n2; ++i2) {
            String string6 = stringTokenizer.nextToken();
            if (string6.indexOf(58) == -1) {
                vector.addElement(new QName(string5, string6));
                continue;
            }
            vector.addElement(new QName(string6, stylesheetHandler));
        }
        return vector;
    }

    Vector processSIMPLEPATTERNLIST(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(string4, " \t\n\r\f");
            int n2 = stringTokenizer.countTokens();
            Vector<XPath> vector = new Vector<XPath>(n2);
            for (int i2 = 0; i2 < n2; ++i2) {
                XPath xPath = stylesheetHandler.createMatchPatternXPath(stringTokenizer.nextToken(), elemTemplateElement);
                vector.addElement(xPath);
            }
            return vector;
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    StringVector processSTRINGLIST(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4) {
        StringTokenizer stringTokenizer = new StringTokenizer(string4, " \t\n\r\f");
        int n2 = stringTokenizer.countTokens();
        StringVector stringVector = new StringVector(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            stringVector.addElement(stringTokenizer.nextToken());
        }
        return stringVector;
    }

    StringVector processPREFIX_URLLIST(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4) throws SAXException {
        StringTokenizer stringTokenizer = new StringTokenizer(string4, " \t\n\r\f");
        int n2 = stringTokenizer.countTokens();
        StringVector stringVector = new StringVector(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            String string5 = stringTokenizer.nextToken();
            String string6 = stylesheetHandler.getNamespaceForPrefix(string5);
            if (string6 == null) {
                throw new SAXException(XSLMessages.createMessage("ER_CANT_RESOLVE_NSPREFIX", new Object[]{string5}));
            }
            stringVector.addElement(string6);
        }
        return stringVector;
    }

    StringVector processPREFIX_LIST(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4) throws SAXException {
        StringTokenizer stringTokenizer = new StringTokenizer(string4, " \t\n\r\f");
        int n2 = stringTokenizer.countTokens();
        StringVector stringVector = new StringVector(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            String string5 = stringTokenizer.nextToken();
            String string6 = stylesheetHandler.getNamespaceForPrefix(string5);
            if (!string5.equals("#default") && string6 == null) {
                throw new SAXException(XSLMessages.createMessage("ER_CANT_RESOLVE_NSPREFIX", new Object[]{string5}));
            }
            stringVector.addElement(string5);
        }
        return stringVector;
    }

    Object processURL(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        if (this.getSupportsAVT()) {
            try {
                AVT aVT = new AVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                return aVT;
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
        }
        return string4;
    }

    private Boolean processYESNO(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4) throws SAXException {
        if (!string4.equals("yes") && !string4.equals("no")) {
            this.handleError(stylesheetHandler, "INVALID_BOOLEAN", new Object[]{string2, string4}, null);
            return null;
        }
        return new Boolean(string4.equals("yes"));
    }

    Object processValue(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        int n2 = this.getType();
        Object object = null;
        switch (n2) {
            case 3: {
                object = this.processAVT(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 1: {
                object = this.processCDATA(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 6: {
                object = this.processCHAR(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 11: {
                object = this.processENUM(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 5: {
                object = this.processEXPR(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 13: {
                object = this.processNMTOKEN(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 4: {
                object = this.processPATTERN(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 7: {
                object = this.processNUMBER(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 9: {
                object = this.processQNAME(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 10: {
                object = this.processQNAMES(stylesheetHandler, string, string2, string3, string4);
                break;
            }
            case 19: {
                object = this.processQNAMESRNU(stylesheetHandler, string, string2, string3, string4);
                break;
            }
            case 12: {
                object = this.processSIMPLEPATTERNLIST(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 2: {
                object = this.processURL(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 8: {
                object = this.processYESNO(stylesheetHandler, string, string2, string3, string4);
                break;
            }
            case 14: {
                object = this.processSTRINGLIST(stylesheetHandler, string, string2, string3, string4);
                break;
            }
            case 15: {
                object = this.processPREFIX_URLLIST(stylesheetHandler, string, string2, string3, string4);
                break;
            }
            case 16: {
                object = this.processENUM_OR_PQNAME(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 17: {
                object = this.processNCNAME(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 18: {
                object = this.processAVT_QNAME(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                break;
            }
            case 20: {
                object = this.processPREFIX_LIST(stylesheetHandler, string, string2, string3, string4);
                break;
            }
        }
        return object;
    }

    void setDefAttrValue(StylesheetHandler stylesheetHandler, ElemTemplateElement elemTemplateElement) throws SAXException {
        this.setAttrValue(stylesheetHandler, this.getNamespace(), this.getName(), this.getName(), this.getDefault(), elemTemplateElement);
    }

    private Class getPrimativeClass(Object object) {
        if (object instanceof XPath) {
            Class class_ = class$org$apache$xpath$XPath == null ? (XSLTAttributeDef.class$org$apache$xpath$XPath = XSLTAttributeDef.class$("org.apache.xpath.XPath")) : class$org$apache$xpath$XPath;
            return class_;
        }
        Class class_ = object.getClass();
        Class class_2 = class$java$lang$Double == null ? (XSLTAttributeDef.class$java$lang$Double = XSLTAttributeDef.class$("java.lang.Double")) : class$java$lang$Double;
        if (class_ == class_2) {
            class_ = Double.TYPE;
        }
        Class class_3 = class$java$lang$Float == null ? (XSLTAttributeDef.class$java$lang$Float = XSLTAttributeDef.class$("java.lang.Float")) : class$java$lang$Float;
        if (class_ == class_3) {
            class_ = Float.TYPE;
        } else {
            Class class_4 = class$java$lang$Boolean == null ? (XSLTAttributeDef.class$java$lang$Boolean = XSLTAttributeDef.class$("java.lang.Boolean")) : class$java$lang$Boolean;
            if (class_ == class_4) {
                class_ = Boolean.TYPE;
            } else {
                Class class_5 = class$java$lang$Byte == null ? (XSLTAttributeDef.class$java$lang$Byte = XSLTAttributeDef.class$("java.lang.Byte")) : class$java$lang$Byte;
                if (class_ == class_5) {
                    class_ = Byte.TYPE;
                } else {
                    Class class_6 = class$java$lang$Character == null ? (XSLTAttributeDef.class$java$lang$Character = XSLTAttributeDef.class$("java.lang.Character")) : class$java$lang$Character;
                    if (class_ == class_6) {
                        class_ = Character.TYPE;
                    } else {
                        Class class_7 = class$java$lang$Short == null ? (XSLTAttributeDef.class$java$lang$Short = XSLTAttributeDef.class$("java.lang.Short")) : class$java$lang$Short;
                        if (class_ == class_7) {
                            class_ = Short.TYPE;
                        } else {
                            Class class_8 = class$java$lang$Integer == null ? (XSLTAttributeDef.class$java$lang$Integer = XSLTAttributeDef.class$("java.lang.Integer")) : class$java$lang$Integer;
                            if (class_ == class_8) {
                                class_ = Integer.TYPE;
                            } else {
                                Class class_9 = class$java$lang$Long == null ? (XSLTAttributeDef.class$java$lang$Long = XSLTAttributeDef.class$("java.lang.Long")) : class$java$lang$Long;
                                if (class_ == class_9) {
                                    class_ = Long.TYPE;
                                }
                            }
                        }
                    }
                }
            }
        }
        return class_;
    }

    private StringBuffer getListOfEnums() {
        StringBuffer stringBuffer = new StringBuffer();
        String[] arrstring = this.getEnumNames();
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            if (i2 > 0) {
                stringBuffer.append(' ');
            }
            stringBuffer.append(arrstring[i2]);
        }
        return stringBuffer;
    }

    boolean setAttrValue(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4, ElemTemplateElement elemTemplateElement) throws SAXException {
        if (string3.equals("xmlns") || string3.startsWith("xmlns:")) {
            return true;
        }
        String string5 = this.getSetterMethodName();
        if (null != string5) {
            try {
                Method method;
                Object[] arrobject;
                if (string5.equals("setForeignAttr")) {
                    if (string == null) {
                        string = "";
                    }
                    Class class_ = string.getClass();
                    Class[] arrclass = new Class[]{class_, class_, class_, class_};
                    method = elemTemplateElement.getClass().getMethod(string5, arrclass);
                    arrobject = new Object[]{string, string2, string3, string4};
                } else {
                    Object object = this.processValue(stylesheetHandler, string, string2, string3, string4, elemTemplateElement);
                    if (null == object) {
                        return false;
                    }
                    Class[] arrclass = new Class[]{this.getPrimativeClass(object)};
                    try {
                        method = elemTemplateElement.getClass().getMethod(string5, arrclass);
                    }
                    catch (NoSuchMethodException noSuchMethodException) {
                        Class class_;
                        arrclass[0] = class_ = object.getClass();
                        method = elemTemplateElement.getClass().getMethod(string5, arrclass);
                    }
                    arrobject = new Object[]{object};
                }
                method.invoke(elemTemplateElement, arrobject);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                if (!string5.equals("setForeignAttr")) {
                    stylesheetHandler.error("ER_FAILED_CALLING_METHOD", new Object[]{string5}, noSuchMethodException);
                    return false;
                }
            }
            catch (IllegalAccessException illegalAccessException) {
                stylesheetHandler.error("ER_FAILED_CALLING_METHOD", new Object[]{string5}, illegalAccessException);
                return false;
            }
            catch (InvocationTargetException invocationTargetException) {
                this.handleError(stylesheetHandler, "WG_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"name", this.getName()}, invocationTargetException);
                return false;
            }
        }
        return true;
    }

    private void handleError(StylesheetHandler stylesheetHandler, String string, Object[] arrobject, Exception exception) throws SAXException {
        switch (this.getErrorType()) {
            case 0: 
            case 1: {
                stylesheetHandler.error(string, arrobject, exception);
                break;
            }
            case 2: {
                stylesheetHandler.warn(string, arrobject);
            }
        }
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

