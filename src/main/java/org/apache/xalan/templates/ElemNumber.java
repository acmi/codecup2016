/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.CountersTable;
import org.apache.xalan.transformer.DecimalToRoman;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xml.utils.res.CharArrayWrapper;
import org.apache.xml.utils.res.IntArrayWrapper;
import org.apache.xml.utils.res.LongArrayWrapper;
import org.apache.xml.utils.res.StringArrayWrapper;
import org.apache.xml.utils.res.XResourceBundle;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ElemNumber
extends ElemTemplateElement {
    static final long serialVersionUID = 8118472298274407610L;
    private CharArrayWrapper m_alphaCountTable = null;
    private XPath m_countMatchPattern = null;
    private XPath m_fromMatchPattern = null;
    private int m_level = 1;
    private XPath m_valueExpr = null;
    private AVT m_format_avt = null;
    private AVT m_lang_avt = null;
    private AVT m_lettervalue_avt = null;
    private AVT m_groupingSeparator_avt = null;
    private AVT m_groupingSize_avt = null;
    private static final DecimalToRoman[] m_romanConvertTable = new DecimalToRoman[]{new DecimalToRoman(1000, "M", 900, "CM"), new DecimalToRoman(500, "D", 400, "CD"), new DecimalToRoman(100, "C", 90, "XC"), new DecimalToRoman(50, "L", 40, "XL"), new DecimalToRoman(10, "X", 9, "IX"), new DecimalToRoman(5, "V", 4, "IV"), new DecimalToRoman(1, "I", 1, "I")};

    public void setCount(XPath xPath) {
        this.m_countMatchPattern = xPath;
    }

    public XPath getCount() {
        return this.m_countMatchPattern;
    }

    public void setFrom(XPath xPath) {
        this.m_fromMatchPattern = xPath;
    }

    public XPath getFrom() {
        return this.m_fromMatchPattern;
    }

    public void setLevel(int n2) {
        this.m_level = n2;
    }

    public int getLevel() {
        return this.m_level;
    }

    public void setValue(XPath xPath) {
        this.m_valueExpr = xPath;
    }

    public XPath getValue() {
        return this.m_valueExpr;
    }

    public void setFormat(AVT aVT) {
        this.m_format_avt = aVT;
    }

    public AVT getFormat() {
        return this.m_format_avt;
    }

    public void setLang(AVT aVT) {
        this.m_lang_avt = aVT;
    }

    public AVT getLang() {
        return this.m_lang_avt;
    }

    public void setLetterValue(AVT aVT) {
        this.m_lettervalue_avt = aVT;
    }

    public AVT getLetterValue() {
        return this.m_lettervalue_avt;
    }

    public void setGroupingSeparator(AVT aVT) {
        this.m_groupingSeparator_avt = aVT;
    }

    public AVT getGroupingSeparator() {
        return this.m_groupingSeparator_avt;
    }

    public void setGroupingSize(AVT aVT) {
        this.m_groupingSize_avt = aVT;
    }

    public AVT getGroupingSize() {
        return this.m_groupingSize_avt;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        StylesheetRoot.ComposeState composeState = stylesheetRoot.getComposeState();
        Vector vector = composeState.getVariableNames();
        if (null != this.m_countMatchPattern) {
            this.m_countMatchPattern.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_format_avt) {
            this.m_format_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_fromMatchPattern) {
            this.m_fromMatchPattern.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_groupingSeparator_avt) {
            this.m_groupingSeparator_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_groupingSize_avt) {
            this.m_groupingSize_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_lang_avt) {
            this.m_lang_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_lettervalue_avt) {
            this.m_lettervalue_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_valueExpr) {
            this.m_valueExpr.fixupVariables(vector, composeState.getGlobalsSize());
        }
    }

    public int getXSLToken() {
        return 35;
    }

    public String getNodeName() {
        return "number";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        int n2 = transformerImpl.getXPathContext().getCurrentNode();
        String string = this.getCountString(transformerImpl, n2);
        try {
            transformerImpl.getResultTreeHandler().characters(string.toCharArray(), 0, string.length());
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
        finally {
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
        }
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        this.error("ER_CANNOT_ADD", new Object[]{elemTemplateElement.getNodeName(), this.getNodeName()});
        return null;
    }

    int findAncestor(XPathContext xPathContext, XPath xPath, XPath xPath2, int n2, ElemNumber elemNumber) throws TransformerException {
        DTM dTM = xPathContext.getDTM(n2);
        while (!(-1 == n2 || null != xPath && xPath.getMatchScore(xPathContext, n2) != Double.NEGATIVE_INFINITY || null != xPath2 && xPath2.getMatchScore(xPathContext, n2) != Double.NEGATIVE_INFINITY)) {
            n2 = dTM.getParent(n2);
        }
        return n2;
    }

    private int findPrecedingOrAncestorOrSelf(XPathContext xPathContext, XPath xPath, XPath xPath2, int n2, ElemNumber elemNumber) throws TransformerException {
        DTM dTM = xPathContext.getDTM(n2);
        while (-1 != n2) {
            if (null != xPath && xPath.getMatchScore(xPathContext, n2) != Double.NEGATIVE_INFINITY) {
                n2 = -1;
                break;
            }
            if (null != xPath2 && xPath2.getMatchScore(xPathContext, n2) != Double.NEGATIVE_INFINITY) break;
            int n3 = dTM.getPreviousSibling(n2);
            if (-1 == n3) {
                n2 = dTM.getParent(n2);
                continue;
            }
            n2 = dTM.getLastChild(n3);
            if (n2 != -1) continue;
            n2 = n3;
        }
        return n2;
    }

    XPath getCountMatchPattern(XPathContext xPathContext, int n2) throws TransformerException {
        XPath xPath = this.m_countMatchPattern;
        DTM dTM = xPathContext.getDTM(n2);
        if (null == xPath) {
            switch (dTM.getNodeType(n2)) {
                case 1: {
                    MyPrefixResolver myPrefixResolver = dTM.getNamespaceURI(n2) == null ? new MyPrefixResolver(this, dTM.getNode(n2), dTM, n2, false) : new MyPrefixResolver(this, dTM.getNode(n2), dTM, n2, true);
                    xPath = new XPath(dTM.getNodeName(n2), this, myPrefixResolver, 1, xPathContext.getErrorListener());
                    break;
                }
                case 2: {
                    xPath = new XPath("@" + dTM.getNodeName(n2), this, this, 1, xPathContext.getErrorListener());
                    break;
                }
                case 3: 
                case 4: {
                    xPath = new XPath("text()", this, this, 1, xPathContext.getErrorListener());
                    break;
                }
                case 8: {
                    xPath = new XPath("comment()", this, this, 1, xPathContext.getErrorListener());
                    break;
                }
                case 9: {
                    xPath = new XPath("/", this, this, 1, xPathContext.getErrorListener());
                    break;
                }
                case 7: {
                    xPath = new XPath("pi(" + dTM.getNodeName(n2) + ")", this, this, 1, xPathContext.getErrorListener());
                    break;
                }
                default: {
                    xPath = null;
                }
            }
        }
        return xPath;
    }

    String getCountString(TransformerImpl transformerImpl, int n2) throws TransformerException {
        long[] arrl = null;
        XPathContext xPathContext = transformerImpl.getXPathContext();
        CountersTable countersTable = transformerImpl.getCountersTable();
        if (null != this.m_valueExpr) {
            XObject xObject = this.m_valueExpr.execute(xPathContext, n2, (PrefixResolver)this);
            double d2 = Math.floor(xObject.num() + 0.5);
            if (Double.isNaN(d2)) {
                return "NaN";
            }
            if (d2 < 0.0 && Double.isInfinite(d2)) {
                return "-Infinity";
            }
            if (Double.isInfinite(d2)) {
                return "Infinity";
            }
            if (d2 == 0.0) {
                return "0";
            }
            long l2 = (long)d2;
            arrl = new long[]{l2};
        } else if (3 == this.m_level) {
            arrl = new long[]{countersTable.countNode(xPathContext, this, n2)};
        } else {
            NodeVector nodeVector = this.getMatchingAncestors(xPathContext, n2, 1 == this.m_level);
            int n3 = nodeVector.size() - 1;
            if (n3 >= 0) {
                arrl = new long[n3 + 1];
                for (int i2 = n3; i2 >= 0; --i2) {
                    int n4 = nodeVector.elementAt(i2);
                    arrl[n3 - i2] = countersTable.countNode(xPathContext, this, n4);
                }
            }
        }
        return null != arrl ? this.formatNumberList(transformerImpl, arrl, n2) : "";
    }

    public int getPreviousNode(XPathContext xPathContext, int n2) throws TransformerException {
        XPath xPath = this.getCountMatchPattern(xPathContext, n2);
        DTM dTM = xPathContext.getDTM(n2);
        if (3 == this.m_level) {
            XPath xPath2 = this.m_fromMatchPattern;
            while (-1 != n2) {
                int n3 = dTM.getPreviousSibling(n2);
                if (-1 == n3) {
                    n3 = dTM.getParent(n2);
                    if (-1 != n3 && (null != xPath2 && xPath2.getMatchScore(xPathContext, n3) != Double.NEGATIVE_INFINITY || dTM.getNodeType(n3) == 9)) {
                        n2 = -1;
                        break;
                    }
                } else {
                    int n4 = n3;
                    while (-1 != n4) {
                        n4 = dTM.getLastChild(n3);
                        if (-1 == n4) continue;
                        n3 = n4;
                    }
                }
                if (-1 == (n2 = n3) || null != xPath && xPath.getMatchScore(xPathContext, n2) == Double.NEGATIVE_INFINITY) continue;
                break;
            }
        } else {
            while (-1 != n2 && (-1 == (n2 = dTM.getPreviousSibling(n2)) || null != xPath && xPath.getMatchScore(xPathContext, n2) == Double.NEGATIVE_INFINITY)) {
            }
        }
        return n2;
    }

    public int getTargetNode(XPathContext xPathContext, int n2) throws TransformerException {
        int n3 = -1;
        XPath xPath = this.getCountMatchPattern(xPathContext, n2);
        n3 = 3 == this.m_level ? this.findPrecedingOrAncestorOrSelf(xPathContext, this.m_fromMatchPattern, xPath, n2, this) : this.findAncestor(xPathContext, this.m_fromMatchPattern, xPath, n2, this);
        return n3;
    }

    NodeVector getMatchingAncestors(XPathContext xPathContext, int n2, boolean bl) throws TransformerException {
        NodeSetDTM nodeSetDTM = new NodeSetDTM(xPathContext.getDTMManager());
        XPath xPath = this.getCountMatchPattern(xPathContext, n2);
        DTM dTM = xPathContext.getDTM(n2);
        while (-1 != n2 && (null == this.m_fromMatchPattern || this.m_fromMatchPattern.getMatchScore(xPathContext, n2) == Double.NEGATIVE_INFINITY || bl)) {
            if (null == xPath) {
                System.out.println("Programmers error! countMatchPattern should never be null!");
            }
            if (xPath.getMatchScore(xPathContext, n2) != Double.NEGATIVE_INFINITY) {
                nodeSetDTM.addElement(n2);
                if (bl) break;
            }
            n2 = dTM.getParent(n2);
        }
        return nodeSetDTM;
    }

    Locale getLocale(TransformerImpl transformerImpl, int n2) throws TransformerException {
        Locale locale = null;
        if (null != this.m_lang_avt) {
            XPathContext xPathContext = transformerImpl.getXPathContext();
            String string = this.m_lang_avt.evaluate(xPathContext, n2, this);
            if (null != string && null == (locale = new Locale(string.toUpperCase(), ""))) {
                transformerImpl.getMsgMgr().warn(this, null, xPathContext.getDTM(n2).getNode(n2), "WG_LOCALE_NOT_FOUND", new Object[]{string});
                locale = Locale.getDefault();
            }
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    private DecimalFormat getNumberFormatter(TransformerImpl transformerImpl, int n2) throws TransformerException {
        String string;
        String string2;
        Locale locale = (Locale)this.getLocale(transformerImpl, n2).clone();
        DecimalFormat decimalFormat = null;
        String string3 = string2 = null != this.m_groupingSeparator_avt ? this.m_groupingSeparator_avt.evaluate(transformerImpl.getXPathContext(), n2, this) : null;
        if (string2 != null && !this.m_groupingSeparator_avt.isSimple() && string2.length() != 1) {
            transformerImpl.getMsgMgr().warn(this, "WG_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"name", this.m_groupingSeparator_avt.getName()});
        }
        String string4 = string = null != this.m_groupingSize_avt ? this.m_groupingSize_avt.evaluate(transformerImpl.getXPathContext(), n2, this) : null;
        if (null != string2 && null != string && string2.length() > 0) {
            try {
                decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance(locale);
                decimalFormat.setGroupingSize(Integer.valueOf(string));
                DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
                decimalFormatSymbols.setGroupingSeparator(string2.charAt(0));
                decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
                decimalFormat.setGroupingUsed(true);
            }
            catch (NumberFormatException numberFormatException) {
                decimalFormat.setGroupingUsed(false);
            }
        }
        return decimalFormat;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    String formatNumberList(TransformerImpl transformerImpl, long[] arrl, int n2) throws TransformerException {
        String string;
        FastStringBuffer fastStringBuffer = StringBufferPool.get();
        try {
            String string2;
            String string3;
            int n3 = arrl.length;
            int n4 = 1;
            char c2 = '1';
            String string4 = null;
            String string5 = null;
            String string6 = ".";
            boolean bl = true;
            String string7 = string2 = null != this.m_format_avt ? this.m_format_avt.evaluate(transformerImpl.getXPathContext(), n2, this) : null;
            if (null == string2) {
                string2 = "1";
            }
            NumberFormatStringTokenizer numberFormatStringTokenizer = new NumberFormatStringTokenizer(this, string2);
            for (int i2 = 0; i2 < n3; ++i2) {
                if (numberFormatStringTokenizer.hasMoreTokens()) {
                    string3 = numberFormatStringTokenizer.nextToken();
                    if (Character.isLetterOrDigit(string3.charAt(string3.length() - 1))) {
                        n4 = string3.length();
                        c2 = string3.charAt(n4 - 1);
                    } else if (numberFormatStringTokenizer.isLetterOrDigitAhead()) {
                        StringBuffer stringBuffer = new StringBuffer(string3);
                        while (numberFormatStringTokenizer.nextIsSep()) {
                            string3 = numberFormatStringTokenizer.nextToken();
                            stringBuffer.append(string3);
                        }
                        string5 = stringBuffer.toString();
                        if (!bl) {
                            string6 = string5;
                        }
                        string3 = numberFormatStringTokenizer.nextToken();
                        n4 = string3.length();
                        c2 = string3.charAt(n4 - 1);
                    } else {
                        string4 = string3;
                        while (numberFormatStringTokenizer.hasMoreTokens()) {
                            string3 = numberFormatStringTokenizer.nextToken();
                            string4 = string4 + string3;
                        }
                    }
                }
                if (null != string5 && bl) {
                    fastStringBuffer.append(string5);
                } else if (null != string6 && !bl) {
                    fastStringBuffer.append(string6);
                }
                this.getFormattedNumber(transformerImpl, n2, c2, n4, arrl[i2], fastStringBuffer);
                bl = false;
            }
            while (numberFormatStringTokenizer.isLetterOrDigitAhead()) {
                numberFormatStringTokenizer.nextToken();
            }
            if (string4 != null) {
                fastStringBuffer.append(string4);
            }
            while (numberFormatStringTokenizer.hasMoreTokens()) {
                string3 = numberFormatStringTokenizer.nextToken();
                fastStringBuffer.append(string3);
            }
            string = fastStringBuffer.toString();
        }
        finally {
            StringBufferPool.free(fastStringBuffer);
        }
        return string;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void getFormattedNumber(TransformerImpl transformerImpl, int n2, char c2, int n3, long l2, FastStringBuffer fastStringBuffer) throws TransformerException {
        String string = this.m_lettervalue_avt != null ? this.m_lettervalue_avt.evaluate(transformerImpl.getXPathContext(), n2, this) : null;
        Object var9_8 = null;
        XResourceBundle xResourceBundle = null;
        switch (c2) {
            case 'A': {
                if (null == this.m_alphaCountTable) {
                    xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", this.getLocale(transformerImpl, n2));
                    this.m_alphaCountTable = (CharArrayWrapper)xResourceBundle.getObject("alphabet");
                }
                this.int2alphaCount(l2, this.m_alphaCountTable, fastStringBuffer);
                break;
            }
            case 'a': {
                if (null == this.m_alphaCountTable) {
                    xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", this.getLocale(transformerImpl, n2));
                    this.m_alphaCountTable = (CharArrayWrapper)xResourceBundle.getObject("alphabet");
                }
                FastStringBuffer fastStringBuffer2 = StringBufferPool.get();
                try {
                    this.int2alphaCount(l2, this.m_alphaCountTable, fastStringBuffer2);
                    fastStringBuffer.append(fastStringBuffer2.toString().toLowerCase(this.getLocale(transformerImpl, n2)));
                }
                finally {
                    StringBufferPool.free(fastStringBuffer2);
                }
            }
            case 'I': {
                fastStringBuffer.append(this.long2roman(l2, true));
                break;
            }
            case 'i': {
                fastStringBuffer.append(this.long2roman(l2, true).toLowerCase(this.getLocale(transformerImpl, n2)));
                break;
            }
            case '\u3042': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ja", "JP", "HA"));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                fastStringBuffer.append(this.int2singlealphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet")));
                break;
            }
            case '\u3044': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ja", "JP", "HI"));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                fastStringBuffer.append(this.int2singlealphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet")));
                break;
            }
            case '\u30a2': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ja", "JP", "A"));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                fastStringBuffer.append(this.int2singlealphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet")));
                break;
            }
            case '\u30a4': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ja", "JP", "I"));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                fastStringBuffer.append(this.int2singlealphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet")));
                break;
            }
            case '\u4e00': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("zh", "CN"));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                this.int2alphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet"), fastStringBuffer);
                break;
            }
            case '\u58f9': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("zh", "TW"));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                this.int2alphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet"), fastStringBuffer);
                break;
            }
            case '\u0e51': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("th", ""));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                this.int2alphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet"), fastStringBuffer);
                break;
            }
            case '\u05d0': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("he", ""));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                this.int2alphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet"), fastStringBuffer);
                break;
            }
            case '\u10d0': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ka", ""));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                this.int2alphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet"), fastStringBuffer);
                break;
            }
            case '\u03b1': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("el", ""));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                this.int2alphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet"), fastStringBuffer);
                break;
            }
            case '\u0430': {
                xResourceBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("cy", ""));
                if (string != null && string.equals("traditional")) {
                    fastStringBuffer.append(this.tradAlphaCount(l2, xResourceBundle));
                    break;
                }
                this.int2alphaCount(l2, (CharArrayWrapper)xResourceBundle.getObject("alphabet"), fastStringBuffer);
                break;
            }
            default: {
                DecimalFormat decimalFormat = this.getNumberFormatter(transformerImpl, n2);
                String string2 = decimalFormat == null ? String.valueOf(0) : decimalFormat.format(0);
                String string3 = decimalFormat == null ? String.valueOf(l2) : decimalFormat.format(l2);
                int n4 = n3 - string3.length();
                for (int i2 = 0; i2 < n4; ++i2) {
                    fastStringBuffer.append(string2);
                }
                fastStringBuffer.append(string3);
            }
        }
    }

    String getZeroString() {
        return "0";
    }

    protected String int2singlealphaCount(long l2, CharArrayWrapper charArrayWrapper) {
        int n2 = charArrayWrapper.getLength();
        if (l2 > (long)n2) {
            return this.getZeroString();
        }
        return new Character(charArrayWrapper.getChar((int)l2 - 1)).toString();
    }

    protected void int2alphaCount(long l2, CharArrayWrapper charArrayWrapper, FastStringBuffer fastStringBuffer) {
        int n2;
        int n3 = charArrayWrapper.getLength();
        char[] arrc = new char[n3];
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            arrc[n2 + 1] = charArrayWrapper.getChar(n2);
        }
        arrc[0] = charArrayWrapper.getChar(n2);
        char[] arrc2 = new char[100];
        int n4 = arrc2.length - 1;
        int n5 = 1;
        long l3 = 0;
        do {
            l3 = n5 == 0 || l3 != 0 && n5 == n3 - 1 ? (long)(n3 - 1) : 0;
            n5 = (int)(l2 + l3) % n3;
            if (n5 == 0 && (l2 /= (long)n3) == 0) break;
            arrc2[n4--] = arrc[n5];
        } while (l2 > 0);
        fastStringBuffer.append(arrc2, n4 + 1, arrc2.length - n4 - 1);
    }

    protected String tradAlphaCount(long l2, XResourceBundle xResourceBundle) {
        CharArrayWrapper charArrayWrapper;
        int n2;
        if (l2 > Long.MAX_VALUE) {
            this.error("ER_NUMBER_TOO_BIG");
            return "#error";
        }
        char[] arrc = null;
        int n3 = 1;
        char[] arrc2 = new char[100];
        int n4 = 0;
        IntArrayWrapper intArrayWrapper = (IntArrayWrapper)xResourceBundle.getObject("numberGroups");
        StringArrayWrapper stringArrayWrapper = (StringArrayWrapper)xResourceBundle.getObject("tables");
        String string = xResourceBundle.getString("numbering");
        if (string.equals("multiplicative-additive")) {
            String string2 = xResourceBundle.getString("multiplierOrder");
            LongArrayWrapper longArrayWrapper = (LongArrayWrapper)xResourceBundle.getObject("multiplier");
            charArrayWrapper = (CharArrayWrapper)xResourceBundle.getObject("zero");
            for (n2 = 0; n2 < longArrayWrapper.getLength() && l2 < longArrayWrapper.getLong(n2); ++n2) {
            }
            while (n2 < longArrayWrapper.getLength()) {
                if (l2 < longArrayWrapper.getLong(n2)) {
                    if (charArrayWrapper.getLength() == 0) {
                        ++n2;
                    } else {
                        if (arrc2[n4 - 1] != charArrayWrapper.getChar(0)) {
                            arrc2[n4++] = charArrayWrapper.getChar(0);
                        }
                        ++n2;
                    }
                } else if (l2 >= longArrayWrapper.getLong(n2)) {
                    long l3 = l2 / longArrayWrapper.getLong(n2);
                    l2 %= longArrayWrapper.getLong(n2);
                    for (int i2 = 0; i2 < intArrayWrapper.getLength(); ++i2) {
                        int n5;
                        n3 = 1;
                        if (l3 / (long)intArrayWrapper.getInt(i2) <= 0) {
                            continue;
                        }
                        CharArrayWrapper charArrayWrapper2 = (CharArrayWrapper)xResourceBundle.getObject(stringArrayWrapper.getString(i2));
                        arrc = new char[charArrayWrapper2.getLength() + 1];
                        for (n5 = 0; n5 < charArrayWrapper2.getLength(); ++n5) {
                            arrc[n5 + 1] = charArrayWrapper2.getChar(n5);
                        }
                        arrc[0] = charArrayWrapper2.getChar(n5 - 1);
                        n3 = (int)l3 / intArrayWrapper.getInt(i2);
                        if (n3 == 0 && l3 == 0) break;
                        char c2 = ((CharArrayWrapper)xResourceBundle.getObject("multiplierChar")).getChar(n2);
                        if (n3 < arrc.length) {
                            if (string2.equals("precedes")) {
                                arrc2[n4++] = c2;
                                arrc2[n4++] = arrc[n3];
                                break;
                            }
                            if (n3 != 1 || n2 != longArrayWrapper.getLength() - 1) {
                                arrc2[n4++] = arrc[n3];
                            }
                            arrc2[n4++] = c2;
                            break;
                        }
                        return "#error";
                    }
                    ++n2;
                }
                if (n2 < longArrayWrapper.getLength()) continue;
            }
        }
        int n6 = 0;
        while (n6 < intArrayWrapper.getLength()) {
            if (l2 / (long)intArrayWrapper.getInt(n6) <= 0) {
                ++n6;
                continue;
            }
            charArrayWrapper = (CharArrayWrapper)xResourceBundle.getObject(stringArrayWrapper.getString(n6));
            arrc = new char[charArrayWrapper.getLength() + 1];
            for (n2 = 0; n2 < charArrayWrapper.getLength(); ++n2) {
                arrc[n2 + 1] = charArrayWrapper.getChar(n2);
            }
            arrc[0] = charArrayWrapper.getChar(n2 - 1);
            n3 = (int)l2 / intArrayWrapper.getInt(n6);
            if (n3 == 0 && (l2 %= (long)intArrayWrapper.getInt(n6)) == 0) break;
            if (n3 >= arrc.length) {
                return "#error";
            }
            arrc2[n4++] = arrc[n3];
            ++n6;
        }
        return new String(arrc2, 0, n4);
    }

    protected String long2roman(long l2, boolean bl) {
        String string;
        if (l2 <= 0) {
            return this.getZeroString();
        }
        int n2 = 0;
        if (l2 <= 3999) {
            StringBuffer stringBuffer = new StringBuffer();
            do {
                if (l2 >= ElemNumber.m_romanConvertTable[n2].m_postValue) {
                    stringBuffer.append(ElemNumber.m_romanConvertTable[n2].m_postLetter);
                    l2 -= ElemNumber.m_romanConvertTable[n2].m_postValue;
                    continue;
                }
                if (bl && l2 >= ElemNumber.m_romanConvertTable[n2].m_preValue) {
                    stringBuffer.append(ElemNumber.m_romanConvertTable[n2].m_preLetter);
                    l2 -= ElemNumber.m_romanConvertTable[n2].m_preValue;
                }
                ++n2;
                if (l2 <= 0) break;
            } while (true);
            string = stringBuffer.toString();
        } else {
            string = "#error";
        }
        return string;
    }

    public void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        if (bl) {
            if (null != this.m_countMatchPattern) {
                this.m_countMatchPattern.getExpression().callVisitors(this.m_countMatchPattern, xSLTVisitor);
            }
            if (null != this.m_fromMatchPattern) {
                this.m_fromMatchPattern.getExpression().callVisitors(this.m_fromMatchPattern, xSLTVisitor);
            }
            if (null != this.m_valueExpr) {
                this.m_valueExpr.getExpression().callVisitors(this.m_valueExpr, xSLTVisitor);
            }
            if (null != this.m_format_avt) {
                this.m_format_avt.callVisitors(xSLTVisitor);
            }
            if (null != this.m_groupingSeparator_avt) {
                this.m_groupingSeparator_avt.callVisitors(xSLTVisitor);
            }
            if (null != this.m_groupingSize_avt) {
                this.m_groupingSize_avt.callVisitors(xSLTVisitor);
            }
            if (null != this.m_lang_avt) {
                this.m_lang_avt.callVisitors(xSLTVisitor);
            }
            if (null != this.m_lettervalue_avt) {
                this.m_lettervalue_avt.callVisitors(xSLTVisitor);
            }
        }
        super.callChildVisitors(xSLTVisitor, bl);
    }

    class NumberFormatStringTokenizer {
        private int currentPosition;
        private int maxPosition;
        private String str;
        private final ElemNumber this$0;

        public NumberFormatStringTokenizer(ElemNumber elemNumber, String string) {
            this.this$0 = elemNumber;
            this.str = string;
            this.maxPosition = string.length();
        }

        public void reset() {
            this.currentPosition = 0;
        }

        public String nextToken() {
            if (this.currentPosition >= this.maxPosition) {
                throw new NoSuchElementException();
            }
            int n2 = this.currentPosition;
            while (this.currentPosition < this.maxPosition && Character.isLetterOrDigit(this.str.charAt(this.currentPosition))) {
                ++this.currentPosition;
            }
            if (n2 == this.currentPosition && !Character.isLetterOrDigit(this.str.charAt(this.currentPosition))) {
                ++this.currentPosition;
            }
            return this.str.substring(n2, this.currentPosition);
        }

        public boolean isLetterOrDigitAhead() {
            for (int i2 = this.currentPosition; i2 < this.maxPosition; ++i2) {
                if (!Character.isLetterOrDigit(this.str.charAt(i2))) continue;
                return true;
            }
            return false;
        }

        public boolean nextIsSep() {
            if (Character.isLetterOrDigit(this.str.charAt(this.currentPosition))) {
                return false;
            }
            return true;
        }

        public boolean hasMoreTokens() {
            return this.currentPosition < this.maxPosition;
        }

        public int countTokens() {
            int n2 = 0;
            int n3 = this.currentPosition;
            while (n3 < this.maxPosition) {
                int n4 = n3;
                while (n3 < this.maxPosition && Character.isLetterOrDigit(this.str.charAt(n3))) {
                    ++n3;
                }
                if (n4 == n3 && !Character.isLetterOrDigit(this.str.charAt(n3))) {
                    ++n3;
                }
                ++n2;
            }
            return n2;
        }
    }

    private class MyPrefixResolver
    implements PrefixResolver {
        DTM dtm;
        int handle;
        boolean handleNullPrefix;
        private final ElemNumber this$0;

        public MyPrefixResolver(ElemNumber elemNumber, Node node, DTM dTM, int n2, boolean bl) {
            this.this$0 = elemNumber;
            this.dtm = dTM;
            this.handle = n2;
            this.handleNullPrefix = bl;
        }

        public String getNamespaceForPrefix(String string) {
            return this.dtm.getNamespaceURI(this.handle);
        }

        public String getNamespaceForPrefix(String string, Node node) {
            return this.getNamespaceForPrefix(string);
        }

        public String getBaseIdentifier() {
            return this.this$0.getBaseIdentifier();
        }

        public boolean handlesNullPrefixes() {
            return this.handleNullPrefix;
        }
    }

}

