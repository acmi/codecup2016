/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XObject;

public class XNumber
extends XObject {
    double m_val;

    public XNumber(double d2) {
        this.m_val = d2;
    }

    public XNumber(Number number) {
        this.m_val = number.doubleValue();
        this.setObject(number);
    }

    public int getType() {
        return 2;
    }

    public String getTypeString() {
        return "#NUMBER";
    }

    public double num() {
        return this.m_val;
    }

    public double num(XPathContext xPathContext) throws TransformerException {
        return this.m_val;
    }

    public boolean bool() {
        return !Double.isNaN(this.m_val) && this.m_val != 0.0;
    }

    public String str() {
        String string;
        int n2;
        if (Double.isNaN(this.m_val)) {
            return "NaN";
        }
        if (Double.isInfinite(this.m_val)) {
            if (this.m_val > 0.0) {
                return "Infinity";
            }
            return "-Infinity";
        }
        double d2 = this.m_val;
        String string2 = Double.toString(d2);
        if (string2.charAt((n2 = string2.length()) - 2) == '.' && string2.charAt(n2 - 1) == '0') {
            if ((string2 = string2.substring(0, n2 - 2)).equals("-0")) {
                return "0";
            }
            return string2;
        }
        int n3 = string2.indexOf(69);
        if (n3 < 0) {
            if (string2.charAt(n2 - 1) == '0') {
                return string2.substring(0, n2 - 1);
            }
            return string2;
        }
        int n4 = Integer.parseInt(string2.substring(n3 + 1));
        if (string2.charAt(0) == '-') {
            string = "-";
            string2 = string2.substring(1);
            --n3;
        } else {
            string = "";
        }
        int n5 = n3 - 2;
        if (n4 >= n5) {
            return string + string2.substring(0, 1) + string2.substring(2, n3) + XNumber.zeros(n4 - n5);
        }
        while (string2.charAt(n3 - 1) == '0') {
            --n3;
        }
        if (n4 > 0) {
            return string + string2.substring(0, 1) + string2.substring(2, 2 + n4) + "." + string2.substring(2 + n4, n3);
        }
        return string + "0." + XNumber.zeros(-1 - n4) + string2.substring(0, 1) + string2.substring(2, n3);
    }

    private static String zeros(int n2) {
        if (n2 < 1) {
            return "";
        }
        char[] arrc = new char[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrc[i2] = 48;
        }
        return new String(arrc);
    }

    public Object object() {
        if (null == this.m_obj) {
            this.setObject(new Double(this.m_val));
        }
        return this.m_obj;
    }

    public boolean equals(XObject xObject) {
        int n2 = xObject.getType();
        try {
            if (n2 == 4) {
                return xObject.equals(this);
            }
            if (n2 == 1) {
                return xObject.bool() == this.bool();
            }
            return this.m_val == xObject.num();
        }
        catch (TransformerException transformerException) {
            throw new WrappedRuntimeException(transformerException);
        }
    }

    public boolean isStableNumber() {
        return true;
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        xPathVisitor.visitNumberLiteral(expressionOwner, this);
    }
}

