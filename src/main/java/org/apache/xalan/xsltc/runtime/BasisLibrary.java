/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.dom.AbsoluteIterator;
import org.apache.xalan.xsltc.dom.ArrayNodeListIterator;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.MultiDOM;
import org.apache.xalan.xsltc.dom.SingletonIterator;
import org.apache.xalan.xsltc.dom.StepIterator;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.InternalRuntimeError;
import org.apache.xalan.xsltc.runtime.Node;
import org.apache.xalan.xsltc.runtime.Operators;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XML11Char;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class BasisLibrary {
    private static final String EMPTYSTRING = "";
    private static final int DOUBLE_FRACTION_DIGITS = 340;
    private static final double lowerBounds = 0.001;
    private static final double upperBounds = 1.0E7;
    private static DecimalFormat defaultFormatter;
    private static String defaultPattern;
    private static FieldPosition _fieldPosition;
    private static char[] _characterArray;
    private static int prefixIndex;
    public static final String RUN_TIME_INTERNAL_ERR = "RUN_TIME_INTERNAL_ERR";
    public static final String RUN_TIME_COPY_ERR = "RUN_TIME_COPY_ERR";
    public static final String DATA_CONVERSION_ERR = "DATA_CONVERSION_ERR";
    public static final String EXTERNAL_FUNC_ERR = "EXTERNAL_FUNC_ERR";
    public static final String EQUALITY_EXPR_ERR = "EQUALITY_EXPR_ERR";
    public static final String INVALID_ARGUMENT_ERR = "INVALID_ARGUMENT_ERR";
    public static final String FORMAT_NUMBER_ERR = "FORMAT_NUMBER_ERR";
    public static final String ITERATOR_CLONE_ERR = "ITERATOR_CLONE_ERR";
    public static final String AXIS_SUPPORT_ERR = "AXIS_SUPPORT_ERR";
    public static final String TYPED_AXIS_SUPPORT_ERR = "TYPED_AXIS_SUPPORT_ERR";
    public static final String STRAY_ATTRIBUTE_ERR = "STRAY_ATTRIBUTE_ERR";
    public static final String STRAY_NAMESPACE_ERR = "STRAY_NAMESPACE_ERR";
    public static final String NAMESPACE_PREFIX_ERR = "NAMESPACE_PREFIX_ERR";
    public static final String DOM_ADAPTER_INIT_ERR = "DOM_ADAPTER_INIT_ERR";
    public static final String PARSER_DTD_SUPPORT_ERR = "PARSER_DTD_SUPPORT_ERR";
    public static final String NAMESPACES_SUPPORT_ERR = "NAMESPACES_SUPPORT_ERR";
    public static final String CANT_RESOLVE_RELATIVE_URI_ERR = "CANT_RESOLVE_RELATIVE_URI_ERR";
    public static final String UNSUPPORTED_XSL_ERR = "UNSUPPORTED_XSL_ERR";
    public static final String UNSUPPORTED_EXT_ERR = "UNSUPPORTED_EXT_ERR";
    public static final String UNKNOWN_TRANSLET_VERSION_ERR = "UNKNOWN_TRANSLET_VERSION_ERR";
    public static final String INVALID_QNAME_ERR = "INVALID_QNAME_ERR";
    public static final String INVALID_NCNAME_ERR = "INVALID_NCNAME_ERR";
    public static final String UNALLOWED_EXTENSION_FUNCTION_ERR = "UNALLOWED_EXTENSION_FUNCTION_ERR";
    public static final String UNALLOWED_EXTENSION_ELEMENT_ERR = "UNALLOWED_EXTENSION_ELEMENT_ERR";
    private static ResourceBundle m_bundle;
    public static final String ERROR_MESSAGES_KEY = "error-messages";
    static Class class$java$lang$String;

    public static int countF(DTMAxisIterator dTMAxisIterator) {
        return dTMAxisIterator.getLast();
    }

    public static int positionF(DTMAxisIterator dTMAxisIterator) {
        return dTMAxisIterator.isReverse() ? dTMAxisIterator.getLast() - dTMAxisIterator.getPosition() + 1 : dTMAxisIterator.getPosition();
    }

    public static double sumF(DTMAxisIterator dTMAxisIterator, DOM dOM) {
        try {
            int n2;
            double d2 = 0.0;
            while ((n2 = dTMAxisIterator.next()) != -1) {
                d2 += Double.parseDouble(dOM.getStringValueX(n2));
            }
            return d2;
        }
        catch (NumberFormatException numberFormatException) {
            return Double.NaN;
        }
    }

    public static String stringF(int n2, DOM dOM) {
        return dOM.getStringValueX(n2);
    }

    public static String stringF(Object object, DOM dOM) {
        if (object instanceof DTMAxisIterator) {
            return dOM.getStringValueX(((DTMAxisIterator)object).reset().next());
        }
        if (object instanceof Node) {
            return dOM.getStringValueX(((Node)object).node);
        }
        if (object instanceof DOM) {
            return ((DOM)object).getStringValue();
        }
        return object.toString();
    }

    public static String stringF(Object object, int n2, DOM dOM) {
        if (object instanceof DTMAxisIterator) {
            return dOM.getStringValueX(((DTMAxisIterator)object).reset().next());
        }
        if (object instanceof Node) {
            return dOM.getStringValueX(((Node)object).node);
        }
        if (object instanceof DOM) {
            return ((DOM)object).getStringValue();
        }
        if (object instanceof Double) {
            int n3;
            Double d2 = (Double)object;
            String string = d2.toString();
            if (string.charAt((n3 = string.length()) - 2) == '.' && string.charAt(n3 - 1) == '0') {
                return string.substring(0, n3 - 2);
            }
            return string;
        }
        if (object != null) {
            return object.toString();
        }
        return BasisLibrary.stringF(n2, dOM);
    }

    public static double numberF(int n2, DOM dOM) {
        return BasisLibrary.stringToReal(dOM.getStringValueX(n2));
    }

    public static double numberF(Object object, DOM dOM) {
        if (object instanceof Double) {
            return (Double)object;
        }
        if (object instanceof Integer) {
            return ((Integer)object).doubleValue();
        }
        if (object instanceof Boolean) {
            return (Boolean)object != false ? 1.0 : 0.0;
        }
        if (object instanceof String) {
            return BasisLibrary.stringToReal((String)object);
        }
        if (object instanceof DTMAxisIterator) {
            DTMAxisIterator dTMAxisIterator = (DTMAxisIterator)object;
            return BasisLibrary.stringToReal(dOM.getStringValueX(dTMAxisIterator.reset().next()));
        }
        if (object instanceof Node) {
            return BasisLibrary.stringToReal(dOM.getStringValueX(((Node)object).node));
        }
        if (object instanceof DOM) {
            return BasisLibrary.stringToReal(((DOM)object).getStringValue());
        }
        String string = object.getClass().getName();
        BasisLibrary.runTimeError("INVALID_ARGUMENT_ERR", string, "number()");
        return 0.0;
    }

    public static double roundF(double d2) {
        return d2 < -0.5 || d2 > 0.0 ? Math.floor(d2 + 0.5) : (d2 == 0.0 ? d2 : (Double.isNaN(d2) ? Double.NaN : -0.0));
    }

    public static boolean booleanF(Object object) {
        if (object instanceof Double) {
            double d2 = (Double)object;
            return d2 != 0.0 && !Double.isNaN(d2);
        }
        if (object instanceof Integer) {
            return ((Integer)object).doubleValue() != 0.0;
        }
        if (object instanceof Boolean) {
            return (Boolean)object;
        }
        if (object instanceof String) {
            return !((String)object).equals("");
        }
        if (object instanceof DTMAxisIterator) {
            DTMAxisIterator dTMAxisIterator = (DTMAxisIterator)object;
            return dTMAxisIterator.reset().next() != -1;
        }
        if (object instanceof Node) {
            return true;
        }
        if (object instanceof DOM) {
            String string = ((DOM)object).getStringValue();
            return !string.equals("");
        }
        String string = object.getClass().getName();
        BasisLibrary.runTimeError("INVALID_ARGUMENT_ERR", string, "boolean()");
        return false;
    }

    public static String substringF(String string, double d2) {
        try {
            int n2 = string.length();
            int n3 = (int)Math.round(d2) - 1;
            if (Double.isNaN(d2)) {
                return "";
            }
            if (n3 > n2) {
                return "";
            }
            if (n3 < 1) {
                n3 = 0;
            }
            return string.substring(n3);
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            BasisLibrary.runTimeError("RUN_TIME_INTERNAL_ERR", "substring()");
            return null;
        }
    }

    public static String substringF(String string, double d2, double d3) {
        try {
            int n2 = string.length();
            int n3 = (int)Math.round(d2) - 1;
            int n4 = n3 + (int)Math.round(d3);
            if (Double.isInfinite(d3)) {
                n4 = Integer.MAX_VALUE;
            }
            if (Double.isNaN(d2) || Double.isNaN(d3)) {
                return "";
            }
            if (Double.isInfinite(d2)) {
                return "";
            }
            if (n3 > n2) {
                return "";
            }
            if (n4 < 0) {
                return "";
            }
            if (n3 < 0) {
                n3 = 0;
            }
            if (n4 > n2) {
                return string.substring(n3);
            }
            return string.substring(n3, n4);
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            BasisLibrary.runTimeError("RUN_TIME_INTERNAL_ERR", "substring()");
            return null;
        }
    }

    public static String substring_afterF(String string, String string2) {
        int n2 = string.indexOf(string2);
        if (n2 >= 0) {
            return string.substring(n2 + string2.length());
        }
        return "";
    }

    public static String substring_beforeF(String string, String string2) {
        int n2 = string.indexOf(string2);
        if (n2 >= 0) {
            return string.substring(0, n2);
        }
        return "";
    }

    public static String translateF(String string, String string2, String string3) {
        int n2 = string3.length();
        int n3 = string2.length();
        int n4 = string.length();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < n4; ++i2) {
            int n5;
            char c2 = string.charAt(i2);
            for (n5 = 0; n5 < n3; ++n5) {
                if (c2 != string2.charAt(n5)) continue;
                if (n5 >= n2) break;
                stringBuffer.append(string3.charAt(n5));
                break;
            }
            if (n5 != n3) continue;
            stringBuffer.append(c2);
        }
        return stringBuffer.toString();
    }

    public static String normalize_spaceF(int n2, DOM dOM) {
        return BasisLibrary.normalize_spaceF(dOM.getStringValueX(n2));
    }

    public static String normalize_spaceF(String string) {
        int n2;
        int n3 = string.length();
        StringBuffer stringBuffer = new StringBuffer();
        for (n2 = 0; n2 < n3 && BasisLibrary.isWhiteSpace(string.charAt(n2)); ++n2) {
        }
        do {
            if (n2 < n3 && !BasisLibrary.isWhiteSpace(string.charAt(n2))) {
                stringBuffer.append(string.charAt(n2++));
                continue;
            }
            if (n2 == n3) break;
            while (n2 < n3 && BasisLibrary.isWhiteSpace(string.charAt(n2))) {
                ++n2;
            }
            if (n2 >= n3) continue;
            stringBuffer.append(' ');
        } while (true);
        return stringBuffer.toString();
    }

    public static String generate_idF(int n2) {
        if (n2 > 0) {
            return "N" + n2;
        }
        return "";
    }

    public static String getLocalName(String string) {
        int n2 = string.lastIndexOf(58);
        if (n2 >= 0) {
            string = string.substring(n2 + 1);
        }
        if ((n2 = string.lastIndexOf(64)) >= 0) {
            string = string.substring(n2 + 1);
        }
        return string;
    }

    public static void unresolved_externalF(String string) {
        BasisLibrary.runTimeError("EXTERNAL_FUNC_ERR", string);
    }

    public static void unallowed_extension_functionF(String string) {
        BasisLibrary.runTimeError("UNALLOWED_EXTENSION_FUNCTION_ERR", string);
    }

    public static void unallowed_extension_elementF(String string) {
        BasisLibrary.runTimeError("UNALLOWED_EXTENSION_ELEMENT_ERR", string);
    }

    public static void unsupported_ElementF(String string, boolean bl) {
        if (bl) {
            BasisLibrary.runTimeError("UNSUPPORTED_EXT_ERR", string);
        } else {
            BasisLibrary.runTimeError("UNSUPPORTED_XSL_ERR", string);
        }
    }

    public static String namespace_uriF(DTMAxisIterator dTMAxisIterator, DOM dOM) {
        return BasisLibrary.namespace_uriF(dTMAxisIterator.next(), dOM);
    }

    public static String system_propertyF(String string) {
        if (string.equals("xsl:version")) {
            return "1.0";
        }
        if (string.equals("xsl:vendor")) {
            return "Apache Software Foundation (Xalan XSLTC)";
        }
        if (string.equals("xsl:vendor-url")) {
            return "http://xml.apache.org/xalan-j";
        }
        BasisLibrary.runTimeError("INVALID_ARGUMENT_ERR", string, "system-property()");
        return "";
    }

    public static String namespace_uriF(int n2, DOM dOM) {
        String string = dOM.getNodeName(n2);
        int n3 = string.lastIndexOf(58);
        if (n3 >= 0) {
            return string.substring(0, n3);
        }
        return "";
    }

    public static String objectTypeF(Object object) {
        if (object instanceof String) {
            return "string";
        }
        if (object instanceof Boolean) {
            return "boolean";
        }
        if (object instanceof Number) {
            return "number";
        }
        if (object instanceof DOM) {
            return "RTF";
        }
        if (object instanceof DTMAxisIterator) {
            return "node-set";
        }
        return "unknown";
    }

    public static DTMAxisIterator nodesetF(Object object) {
        if (object instanceof DOM) {
            DOM dOM = (DOM)object;
            return new SingletonIterator(dOM.getDocument(), true);
        }
        if (object instanceof DTMAxisIterator) {
            return (DTMAxisIterator)object;
        }
        String string = object.getClass().getName();
        BasisLibrary.runTimeError("DATA_CONVERSION_ERR", "node-set", string);
        return null;
    }

    private static boolean isWhiteSpace(char c2) {
        return c2 == ' ' || c2 == '\t' || c2 == '\n' || c2 == '\r';
    }

    private static boolean compareStrings(String string, String string2, int n2, DOM dOM) {
        switch (n2) {
            case 0: {
                return string.equals(string2);
            }
            case 1: {
                return !string.equals(string2);
            }
            case 2: {
                return BasisLibrary.numberF(string, dOM) > BasisLibrary.numberF(string2, dOM);
            }
            case 3: {
                return BasisLibrary.numberF(string, dOM) < BasisLibrary.numberF(string2, dOM);
            }
            case 4: {
                return BasisLibrary.numberF(string, dOM) >= BasisLibrary.numberF(string2, dOM);
            }
            case 5: {
                return BasisLibrary.numberF(string, dOM) <= BasisLibrary.numberF(string2, dOM);
            }
        }
        BasisLibrary.runTimeError("RUN_TIME_INTERNAL_ERR", "compare()");
        return false;
    }

    public static boolean compare(DTMAxisIterator dTMAxisIterator, DTMAxisIterator dTMAxisIterator2, int n2, DOM dOM) {
        int n3;
        dTMAxisIterator.reset();
        while ((n3 = dTMAxisIterator.next()) != -1) {
            int n4;
            String string = dOM.getStringValueX(n3);
            dTMAxisIterator2.reset();
            while ((n4 = dTMAxisIterator2.next()) != -1) {
                if (n3 == n4) {
                    if (n2 == 0) {
                        return true;
                    }
                    if (n2 == 1) continue;
                }
                if (!BasisLibrary.compareStrings(string, dOM.getStringValueX(n4), n2, dOM)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean compare(int n2, DTMAxisIterator dTMAxisIterator, int n3, DOM dOM) {
        switch (n3) {
            case 0: {
                int n4 = dTMAxisIterator.next();
                if (n4 == -1) break;
                String string = dOM.getStringValueX(n2);
                do {
                    if (n2 != n4 && !string.equals(dOM.getStringValueX(n4))) continue;
                    return true;
                } while ((n4 = dTMAxisIterator.next()) != -1);
                break;
            }
            case 1: {
                int n5 = dTMAxisIterator.next();
                if (n5 == -1) break;
                String string = dOM.getStringValueX(n2);
                do {
                    if (n2 == n5 || string.equals(dOM.getStringValueX(n5))) continue;
                    return true;
                } while ((n5 = dTMAxisIterator.next()) != -1);
                break;
            }
            case 3: {
                int n6;
                while ((n6 = dTMAxisIterator.next()) != -1) {
                    if (n6 <= n2) continue;
                    return true;
                }
                break;
            }
            case 2: {
                int n7;
                while ((n7 = dTMAxisIterator.next()) != -1) {
                    if (n7 >= n2) continue;
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public static boolean compare(DTMAxisIterator dTMAxisIterator, double d2, int n2, DOM dOM) {
        switch (n2) {
            case 0: {
                int n3;
                while ((n3 = dTMAxisIterator.next()) != -1) {
                    if (BasisLibrary.numberF(dOM.getStringValueX(n3), dOM) != d2) continue;
                    return true;
                }
                break;
            }
            case 1: {
                int n4;
                while ((n4 = dTMAxisIterator.next()) != -1) {
                    if (BasisLibrary.numberF(dOM.getStringValueX(n4), dOM) == d2) continue;
                    return true;
                }
                break;
            }
            case 2: {
                int n5;
                while ((n5 = dTMAxisIterator.next()) != -1) {
                    if (BasisLibrary.numberF(dOM.getStringValueX(n5), dOM) <= d2) continue;
                    return true;
                }
                break;
            }
            case 3: {
                int n6;
                while ((n6 = dTMAxisIterator.next()) != -1) {
                    if (BasisLibrary.numberF(dOM.getStringValueX(n6), dOM) >= d2) continue;
                    return true;
                }
                break;
            }
            case 4: {
                int n7;
                while ((n7 = dTMAxisIterator.next()) != -1) {
                    if (BasisLibrary.numberF(dOM.getStringValueX(n7), dOM) < d2) continue;
                    return true;
                }
                break;
            }
            case 5: {
                int n8;
                while ((n8 = dTMAxisIterator.next()) != -1) {
                    if (BasisLibrary.numberF(dOM.getStringValueX(n8), dOM) > d2) continue;
                    return true;
                }
                break;
            }
            default: {
                BasisLibrary.runTimeError("RUN_TIME_INTERNAL_ERR", "compare()");
            }
        }
        return false;
    }

    public static boolean compare(DTMAxisIterator dTMAxisIterator, String string, int n2, DOM dOM) {
        int n3;
        while ((n3 = dTMAxisIterator.next()) != -1) {
            if (!BasisLibrary.compareStrings(dOM.getStringValueX(n3), string, n2, dOM)) continue;
            return true;
        }
        return false;
    }

    public static boolean compare(Object object, Object object2, int n2, DOM dOM) {
        boolean bl;
        boolean bl2 = false;
        boolean bl3 = bl = BasisLibrary.hasSimpleType(object) && BasisLibrary.hasSimpleType(object2);
        if (n2 != 0 && n2 != 1) {
            if (object instanceof Node || object2 instanceof Node) {
                if (object instanceof Boolean) {
                    object2 = new Boolean(BasisLibrary.booleanF(object2));
                    bl = true;
                }
                if (object2 instanceof Boolean) {
                    object = new Boolean(BasisLibrary.booleanF(object));
                    bl = true;
                }
            }
            if (bl) {
                switch (n2) {
                    case 2: {
                        return BasisLibrary.numberF(object, dOM) > BasisLibrary.numberF(object2, dOM);
                    }
                    case 3: {
                        return BasisLibrary.numberF(object, dOM) < BasisLibrary.numberF(object2, dOM);
                    }
                    case 4: {
                        return BasisLibrary.numberF(object, dOM) >= BasisLibrary.numberF(object2, dOM);
                    }
                    case 5: {
                        return BasisLibrary.numberF(object, dOM) <= BasisLibrary.numberF(object2, dOM);
                    }
                }
                BasisLibrary.runTimeError("RUN_TIME_INTERNAL_ERR", "compare()");
            }
        }
        if (bl) {
            bl2 = object instanceof Boolean || object2 instanceof Boolean ? BasisLibrary.booleanF(object) == BasisLibrary.booleanF(object2) : (object instanceof Double || object2 instanceof Double || object instanceof Integer || object2 instanceof Integer ? BasisLibrary.numberF(object, dOM) == BasisLibrary.numberF(object2, dOM) : BasisLibrary.stringF(object, dOM).equals(BasisLibrary.stringF(object2, dOM)));
            if (n2 == 1) {
                bl2 = !bl2;
            }
        } else {
            Object object3;
            if (object instanceof Node) {
                object = new SingletonIterator(((Node)object).node);
            }
            if (object2 instanceof Node) {
                object2 = new SingletonIterator(((Node)object2).node);
            }
            if (BasisLibrary.hasSimpleType(object) || object instanceof DOM && object2 instanceof DTMAxisIterator) {
                object3 = object2;
                object2 = object;
                object = object3;
                n2 = Operators.swapOp(n2);
            }
            if (object instanceof DOM) {
                if (object2 instanceof Boolean) {
                    bl2 = (Boolean)object2;
                    return bl2 == (n2 == 0);
                }
                object3 = ((DOM)object).getStringValue();
                if (object2 instanceof Number) {
                    bl2 = ((Number)object2).doubleValue() == BasisLibrary.stringToReal((String)object3);
                } else if (object2 instanceof String) {
                    bl2 = object3.equals((String)object2);
                } else if (object2 instanceof DOM) {
                    bl2 = object3.equals(((DOM)object2).getStringValue());
                }
                if (n2 == 1) {
                    bl2 = !bl2;
                }
                return bl2;
            }
            object3 = ((DTMAxisIterator)object).reset();
            if (object2 instanceof DTMAxisIterator) {
                bl2 = BasisLibrary.compare((DTMAxisIterator)object3, (DTMAxisIterator)object2, n2, dOM);
            } else if (object2 instanceof String) {
                bl2 = BasisLibrary.compare((DTMAxisIterator)object3, (String)object2, n2, dOM);
            } else if (object2 instanceof Number) {
                double d2 = ((Number)object2).doubleValue();
                bl2 = BasisLibrary.compare((DTMAxisIterator)object3, d2, n2, dOM);
            } else if (object2 instanceof Boolean) {
                boolean bl4 = (Boolean)object2;
                bl2 = object3.reset().next() != -1 == bl4;
            } else if (object2 instanceof DOM) {
                bl2 = BasisLibrary.compare((DTMAxisIterator)object3, ((DOM)object2).getStringValue(), n2, dOM);
            } else {
                if (object2 == null) {
                    return false;
                }
                String string = object2.getClass().getName();
                BasisLibrary.runTimeError("INVALID_ARGUMENT_ERR", string, "compare()");
            }
        }
        return bl2;
    }

    public static boolean testLanguage(String string, DOM dOM, int n2) {
        String string2 = dOM.getLanguage(n2);
        if (string2 == null) {
            return false;
        }
        string2 = string2.toLowerCase();
        if ((string = string.toLowerCase()).length() == 2) {
            return string2.startsWith(string);
        }
        return string2.equals(string);
    }

    private static boolean hasSimpleType(Object object) {
        return object instanceof Boolean || object instanceof Double || object instanceof Integer || object instanceof String || object instanceof Node || object instanceof DOM;
    }

    public static double stringToReal(String string) {
        try {
            return Double.valueOf(string);
        }
        catch (NumberFormatException numberFormatException) {
            return Double.NaN;
        }
    }

    public static int stringToInt(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    public static String realToString(double d2) {
        double d3 = Math.abs(d2);
        if (d3 >= 0.001 && d3 < 1.0E7) {
            int n2;
            String string = Double.toString(d2);
            if (string.charAt((n2 = string.length()) - 2) == '.' && string.charAt(n2 - 1) == '0') {
                return string.substring(0, n2 - 2);
            }
            return string;
        }
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            return Double.toString(d2);
        }
        return BasisLibrary.formatNumber(d2, defaultPattern, defaultFormatter);
    }

    public static int realToInt(double d2) {
        return (int)d2;
    }

    public static String formatNumber(double d2, String string, DecimalFormat decimalFormat) {
        if (decimalFormat == null) {
            decimalFormat = defaultFormatter;
        }
        try {
            StringBuffer stringBuffer = new StringBuffer();
            if (string != defaultPattern) {
                decimalFormat.applyLocalizedPattern(string);
            }
            decimalFormat.format(d2, stringBuffer, _fieldPosition);
            return stringBuffer.toString();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            BasisLibrary.runTimeError("FORMAT_NUMBER_ERR", Double.toString(d2), string);
            return "";
        }
    }

    public static DTMAxisIterator referenceToNodeSet(Object object) {
        if (object instanceof Node) {
            return new SingletonIterator(((Node)object).node);
        }
        if (object instanceof DTMAxisIterator) {
            return ((DTMAxisIterator)object).cloneIterator().reset();
        }
        String string = object.getClass().getName();
        BasisLibrary.runTimeError("DATA_CONVERSION_ERR", string, "node-set");
        return null;
    }

    public static NodeList referenceToNodeList(Object object, DOM dOM) {
        if (object instanceof Node || object instanceof DTMAxisIterator) {
            DTMAxisIterator dTMAxisIterator = BasisLibrary.referenceToNodeSet(object);
            return dOM.makeNodeList(dTMAxisIterator);
        }
        if (object instanceof DOM) {
            dOM = (DOM)object;
            return dOM.makeNodeList(0);
        }
        String string = object.getClass().getName();
        BasisLibrary.runTimeError("DATA_CONVERSION_ERR", string, "org.w3c.dom.NodeList");
        return null;
    }

    public static org.w3c.dom.Node referenceToNode(Object object, DOM dOM) {
        if (object instanceof Node || object instanceof DTMAxisIterator) {
            DTMAxisIterator dTMAxisIterator = BasisLibrary.referenceToNodeSet(object);
            return dOM.makeNode(dTMAxisIterator);
        }
        if (object instanceof DOM) {
            dOM = (DOM)object;
            DTMAxisIterator dTMAxisIterator = dOM.getChildren(0);
            return dOM.makeNode(dTMAxisIterator);
        }
        String string = object.getClass().getName();
        BasisLibrary.runTimeError("DATA_CONVERSION_ERR", string, "org.w3c.dom.Node");
        return null;
    }

    public static long referenceToLong(Object object) {
        if (object instanceof Number) {
            return ((Number)object).longValue();
        }
        String string = object.getClass().getName();
        BasisLibrary.runTimeError("DATA_CONVERSION_ERR", string, Long.TYPE);
        return 0;
    }

    public static double referenceToDouble(Object object) {
        if (object instanceof Number) {
            return ((Number)object).doubleValue();
        }
        String string = object.getClass().getName();
        BasisLibrary.runTimeError("DATA_CONVERSION_ERR", string, Double.TYPE);
        return 0.0;
    }

    public static boolean referenceToBoolean(Object object) {
        if (object instanceof Boolean) {
            return (Boolean)object;
        }
        String string = object.getClass().getName();
        BasisLibrary.runTimeError("DATA_CONVERSION_ERR", string, Boolean.TYPE);
        return false;
    }

    public static String referenceToString(Object object, DOM dOM) {
        if (object instanceof String) {
            return (String)object;
        }
        if (object instanceof DTMAxisIterator) {
            return dOM.getStringValueX(((DTMAxisIterator)object).reset().next());
        }
        if (object instanceof Node) {
            return dOM.getStringValueX(((Node)object).node);
        }
        if (object instanceof DOM) {
            return ((DOM)object).getStringValue();
        }
        String string = object.getClass().getName();
        Class class_ = class$java$lang$String == null ? (BasisLibrary.class$java$lang$String = BasisLibrary.class$("java.lang.String")) : class$java$lang$String;
        BasisLibrary.runTimeError("DATA_CONVERSION_ERR", string, class_);
        return null;
    }

    public static DTMAxisIterator node2Iterator(org.w3c.dom.Node node, Translet translet, DOM dOM) {
        org.w3c.dom.Node node2 = node;
        NodeList nodeList = new NodeList(node2){
            private final org.w3c.dom.Node val$inNode;

            public int getLength() {
                return 1;
            }

            public org.w3c.dom.Node item(int n2) {
                if (n2 == 0) {
                    return this.val$inNode;
                }
                return null;
            }
        };
        return BasisLibrary.nodeList2Iterator(nodeList, translet, dOM);
    }

    private static DTMAxisIterator nodeList2IteratorUsingHandleFromNode(NodeList nodeList, Translet translet, DOM dOM) {
        int n2 = nodeList.getLength();
        int[] arrn = new int[n2];
        DTMManager dTMManager = null;
        if (dOM instanceof MultiDOM) {
            dTMManager = ((MultiDOM)dOM).getDTMManager();
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3;
            org.w3c.dom.Node node = nodeList.item(i2);
            if (dTMManager != null) {
                n3 = dTMManager.getDTMHandleFromNode(node);
            } else if (node instanceof DTMNodeProxy && ((DTMNodeProxy)node).getDTM() == dOM) {
                n3 = ((DTMNodeProxy)node).getDTMNodeNumber();
            } else {
                BasisLibrary.runTimeError("RUN_TIME_INTERNAL_ERR", "need MultiDOM");
                return null;
            }
            arrn[i2] = n3;
            System.out.println("Node " + i2 + " has handle 0x" + Integer.toString(n3, 16));
        }
        return new ArrayNodeListIterator(arrn);
    }

    public static DTMAxisIterator nodeList2Iterator(NodeList nodeList, Translet translet, DOM dOM) {
        Object object;
        Object object2;
        Object object3;
        Object object4;
        int n2 = 0;
        Document document = null;
        DTMManager dTMManager = null;
        int[] arrn = new int[nodeList.getLength()];
        if (dOM instanceof MultiDOM) {
            dTMManager = ((MultiDOM)dOM).getDTMManager();
        }
        block12 : for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            object = nodeList.item(i2);
            if (object instanceof DTMNodeProxy) {
                boolean bl;
                object2 = (DTMNodeProxy)object;
                object4 = object2.getDTM();
                int n3 = object2.getDTMNodeNumber();
                boolean bl2 = bl = object4 == dOM;
                if (!bl && dTMManager != null) {
                    try {
                        bl = object4 == dTMManager.getDTM(n3);
                    }
                    catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                        // empty catch block
                    }
                }
                if (bl) {
                    arrn[i2] = n3;
                    ++n2;
                    continue;
                }
            }
            arrn[i2] = -1;
            short s2 = object.getNodeType();
            if (document == null) {
                if (!(dOM instanceof MultiDOM)) {
                    BasisLibrary.runTimeError("RUN_TIME_INTERNAL_ERR", "need MultiDOM");
                    return null;
                }
                try {
                    object4 = (AbstractTranslet)translet;
                    document = object4.newDocument("", "__top__");
                }
                catch (ParserConfigurationException parserConfigurationException) {
                    BasisLibrary.runTimeError("RUN_TIME_INTERNAL_ERR", parserConfigurationException.getMessage());
                    return null;
                }
            }
            switch (s2) {
                case 1: 
                case 3: 
                case 4: 
                case 5: 
                case 7: 
                case 8: {
                    object4 = document.createElementNS(null, "__dummy__");
                    object4.appendChild(document.importNode((org.w3c.dom.Node)object, true));
                    document.getDocumentElement().appendChild((org.w3c.dom.Node)object4);
                    ++n2;
                    continue block12;
                }
                case 2: {
                    object4 = document.createElementNS(null, "__dummy__");
                    object4.setAttributeNodeNS((Attr)document.importNode((org.w3c.dom.Node)object, true));
                    document.getDocumentElement().appendChild((org.w3c.dom.Node)object4);
                    ++n2;
                    continue block12;
                }
                default: {
                    BasisLibrary.runTimeError("RUN_TIME_INTERNAL_ERR", "Don't know how to convert node type " + s2);
                }
            }
        }
        DTMAxisIterator dTMAxisIterator = null;
        object = null;
        object2 = null;
        if (document != null) {
            object4 = (int[])dOM;
            DOM dOM2 = (DOM)((Object)dTMManager.getDTM(new DOMSource(document), false, null, true, false));
            DOMAdapter dOMAdapter = new DOMAdapter(dOM2, translet.getNamesArray(), translet.getUrisArray(), translet.getTypesArray(), translet.getNamespaceArray());
            object4.addDOMAdapter(dOMAdapter);
            object3 = dOM2.getAxisIterator(3);
            DTMAxisIterator dTMAxisIterator2 = dOM2.getAxisIterator(3);
            dTMAxisIterator = new AbsoluteIterator(new StepIterator((DTMAxisIterator)object3, dTMAxisIterator2));
            dTMAxisIterator.setStartNode(0);
            object = dOM2.getAxisIterator(3);
            object2 = dOM2.getAxisIterator(2);
        }
        object4 = new int[n2];
        n2 = 0;
        for (int i3 = 0; i3 < nodeList.getLength(); ++i3) {
            if (arrn[i3] != -1) {
                object4[n2++] = arrn[i3];
                continue;
            }
            org.w3c.dom.Node node = nodeList.item(i3);
            object3 = null;
            short s3 = node.getNodeType();
            switch (s3) {
                case 1: 
                case 3: 
                case 4: 
                case 5: 
                case 7: 
                case 8: {
                    object3 = object;
                    break;
                }
                case 2: {
                    object3 = object2;
                    break;
                }
                default: {
                    throw new InternalRuntimeError("Mismatched cases");
                }
            }
            if (object3 == null) continue;
            object3.setStartNode(dTMAxisIterator.next());
            object4[n2] = object3.next();
            if (object4[n2] == -1) {
                throw new InternalRuntimeError("Expected element missing at " + i3);
            }
            if (object3.next() != -1) {
                throw new InternalRuntimeError("Too many elements at " + i3);
            }
            ++n2;
        }
        if (n2 != object4.length) {
            throw new InternalRuntimeError("Nodes lost in second pass");
        }
        return new ArrayNodeListIterator((int[])object4);
    }

    public static DOM referenceToResultTree(Object object) {
        try {
            return (DOM)object;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            String string = object.getClass().getName();
            BasisLibrary.runTimeError("DATA_CONVERSION_ERR", "reference", string);
            return null;
        }
    }

    public static DTMAxisIterator getSingleNode(DTMAxisIterator dTMAxisIterator) {
        int n2 = dTMAxisIterator.next();
        return new SingletonIterator(n2);
    }

    public static void copy(Object object, SerializationHandler serializationHandler, int n2, DOM dOM) {
        try {
            if (object instanceof DTMAxisIterator) {
                DTMAxisIterator dTMAxisIterator = (DTMAxisIterator)object;
                dOM.copy(dTMAxisIterator.reset(), serializationHandler);
            } else if (object instanceof Node) {
                dOM.copy(((Node)object).node, serializationHandler);
            } else if (object instanceof DOM) {
                DOM dOM2 = (DOM)object;
                dOM2.copy(dOM2.getDocument(), serializationHandler);
            } else {
                String string = object.toString();
                int n3 = string.length();
                if (n3 > _characterArray.length) {
                    _characterArray = new char[n3];
                }
                string.getChars(0, n3, _characterArray, 0);
                serializationHandler.characters(_characterArray, 0, n3);
            }
        }
        catch (SAXException sAXException) {
            BasisLibrary.runTimeError("RUN_TIME_COPY_ERR");
        }
    }

    public static void checkAttribQName(String string) {
        int n2 = string.indexOf(58);
        int n3 = string.lastIndexOf(58);
        String string2 = string.substring(n3 + 1);
        if (n2 > 0) {
            String string3;
            String string4 = string.substring(0, n2);
            if (n2 != n3 && !XML11Char.isXML11ValidNCName(string3 = string.substring(n2 + 1, n3))) {
                BasisLibrary.runTimeError("INVALID_QNAME_ERR", string3 + ":" + string2);
            }
            if (!XML11Char.isXML11ValidNCName(string4)) {
                BasisLibrary.runTimeError("INVALID_QNAME_ERR", string4 + ":" + string2);
            }
        }
        if (!XML11Char.isXML11ValidNCName(string2) || string2.equals("xmlns")) {
            BasisLibrary.runTimeError("INVALID_QNAME_ERR", string2);
        }
    }

    public static void checkNCName(String string) {
        if (!XML11Char.isXML11ValidNCName(string)) {
            BasisLibrary.runTimeError("INVALID_NCNAME_ERR", string);
        }
    }

    public static void checkQName(String string) {
        if (!XML11Char.isXML11ValidQName(string)) {
            BasisLibrary.runTimeError("INVALID_QNAME_ERR", string);
        }
    }

    public static String startXslElement(String string, String string2, SerializationHandler serializationHandler, DOM dOM, int n2) {
        try {
            int n3 = string.indexOf(58);
            if (n3 > 0) {
                String string3 = string.substring(0, n3);
                if (string2 == null || string2.length() == 0) {
                    BasisLibrary.runTimeError("NAMESPACE_PREFIX_ERR", string3);
                }
                serializationHandler.startElement(string2, string.substring(n3 + 1), string);
                serializationHandler.namespaceAfterStartElement(string3, string2);
            } else if (string2 != null && string2.length() > 0) {
                String string4 = BasisLibrary.generatePrefix();
                string = string4 + ':' + string;
                serializationHandler.startElement(string2, string, string);
                serializationHandler.namespaceAfterStartElement(string4, string2);
            } else {
                serializationHandler.startElement(null, null, string);
            }
        }
        catch (SAXException sAXException) {
            throw new RuntimeException(sAXException.getMessage());
        }
        return string;
    }

    public static String lookupStylesheetQNameNamespace(String string, int n2, int[] arrn, int[] arrn2, String[] arrstring, boolean bl) {
        String string2 = BasisLibrary.getPrefix(string);
        String string3 = "";
        if (string2 == null && !bl) {
            string2 = "";
        }
        if (string2 != null) {
            int n3 = n2;
            block0 : while (n3 >= 0) {
                int n4 = arrn2[n3];
                int n5 = n3 + 1 < arrn2.length ? arrn2[n3 + 1] : arrstring.length;
                for (int i2 = n4; i2 < n5; i2 += 2) {
                    if (!string2.equals(arrstring[i2])) continue;
                    string3 = arrstring[i2 + 1];
                    break block0;
                }
                n3 = arrn[n3];
            }
        }
        return string3;
    }

    public static String expandStylesheetQNameRef(String string, int n2, int[] arrn, int[] arrn2, String[] arrstring, boolean bl) {
        String string2 = BasisLibrary.getPrefix(string);
        String string3 = string2 != null ? string.substring(string2.length() + 1) : string;
        String string4 = BasisLibrary.lookupStylesheetQNameNamespace(string, n2, arrn, arrn2, arrstring, bl);
        if (string2 != null && string2.length() != 0 && (string4 == null || string4.length() == 0)) {
            BasisLibrary.runTimeError("NAMESPACE_PREFIX_ERR", string2);
        }
        String string5 = string4.length() == 0 ? string3 : string4 + ':' + string3;
        return string5;
    }

    public static String getPrefix(String string) {
        int n2 = string.indexOf(58);
        return n2 > 0 ? string.substring(0, n2) : null;
    }

    public static String generatePrefix() {
        return "ns" + prefixIndex++;
    }

    public static void runTimeError(String string) {
        throw new RuntimeException(m_bundle.getString(string));
    }

    public static void runTimeError(String string, Object[] arrobject) {
        String string2 = MessageFormat.format(m_bundle.getString(string), arrobject);
        throw new RuntimeException(string2);
    }

    public static void runTimeError(String string, Object object) {
        BasisLibrary.runTimeError(string, new Object[]{object});
    }

    public static void runTimeError(String string, Object object, Object object2) {
        BasisLibrary.runTimeError(string, new Object[]{object, object2});
    }

    public static void consoleOutput(String string) {
        System.out.println(string);
    }

    public static String replace(String string, char c2, String string2) {
        return string.indexOf(c2) < 0 ? string : BasisLibrary.replace(string, String.valueOf(c2), new String[]{string2});
    }

    public static String replace(String string, String string2, String[] arrstring) {
        int n2 = string.length();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            int n3 = string2.indexOf(c2);
            if (n3 >= 0) {
                stringBuffer.append(arrstring[n3]);
                continue;
            }
            stringBuffer.append(c2);
        }
        return stringBuffer.toString();
    }

    public static String mapQNameToJavaName(String string) {
        return BasisLibrary.replace(string, ".-:/{}?#%*", new String[]{"$dot$", "$dash$", "$colon$", "$slash$", "", "$colon$", "$ques$", "$hash$", "$per$", "$aster$"});
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
        defaultPattern = "";
        Object object = NumberFormat.getInstance(Locale.getDefault());
        defaultFormatter = object instanceof DecimalFormat ? (DecimalFormat)object : new DecimalFormat();
        defaultFormatter.setMaximumFractionDigits(340);
        defaultFormatter.setMinimumFractionDigits(0);
        defaultFormatter.setMinimumIntegerDigits(1);
        defaultFormatter.setGroupingUsed(false);
        _fieldPosition = new FieldPosition(0);
        _characterArray = new char[32];
        prefixIndex = 0;
        object = "org.apache.xalan.xsltc.runtime.ErrorMessages";
        m_bundle = ResourceBundle.getBundle((String)object);
    }

}

