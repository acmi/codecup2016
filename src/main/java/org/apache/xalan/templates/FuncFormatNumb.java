/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function3Args;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncFormatNumb
extends Function3Args {
    static final long serialVersionUID = -8869935264870858636L;

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        ElemTemplateElement elemTemplateElement = (ElemTemplateElement)xPathContext.getNamespaceContext();
        StylesheetRoot stylesheetRoot = elemTemplateElement.getStylesheetRoot();
        NumberFormat numberFormat = null;
        DecimalFormatSymbols decimalFormatSymbols = null;
        double d2 = this.getArg0().execute(xPathContext).num();
        String string = this.getArg1().execute(xPathContext).str();
        if (string.indexOf(164) > 0) {
            stylesheetRoot.error("ER_CURRENCY_SIGN_ILLEGAL");
        }
        try {
            Expression expression = this.getArg2();
            if (null != expression) {
                String string2 = expression.execute(xPathContext).str();
                QName qName = new QName(string2, xPathContext.getNamespaceContext());
                decimalFormatSymbols = stylesheetRoot.getDecimalFormatComposed(qName);
                if (null == decimalFormatSymbols) {
                    this.warn(xPathContext, "WG_NO_DECIMALFORMAT_DECLARATION", new Object[]{string2});
                } else {
                    numberFormat = new DecimalFormat();
                    numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);
                    numberFormat.applyLocalizedPattern(string);
                }
            }
            if (null == numberFormat) {
                decimalFormatSymbols = stylesheetRoot.getDecimalFormatComposed(new QName(""));
                if (decimalFormatSymbols != null) {
                    numberFormat = new DecimalFormat();
                    numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);
                    numberFormat.applyLocalizedPattern(string);
                } else {
                    decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
                    decimalFormatSymbols.setInfinity("Infinity");
                    decimalFormatSymbols.setNaN("NaN");
                    numberFormat = new DecimalFormat();
                    numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);
                    if (null != string) {
                        numberFormat.applyLocalizedPattern(string);
                    }
                }
            }
            return new XString(numberFormat.format(d2));
        }
        catch (Exception exception) {
            elemTemplateElement.error("ER_MALFORMED_FORMAT_STRING", new Object[]{string});
            return XString.EMPTYSTRING;
        }
    }

    public void warn(XPathContext xPathContext, String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createWarning(string, arrobject);
        ErrorListener errorListener = xPathContext.getErrorListener();
        errorListener.warning(new TransformerException(string2, (SAXSourceLocator)xPathContext.getSAXLocator()));
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
        if (n2 > 3 || n2 < 2) {
            this.reportWrongNumberArgs();
        }
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createMessage("ER_TWO_OR_THREE", null));
    }
}

