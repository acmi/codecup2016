/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.TypeValidator;

class PrecisionDecimalDV
extends TypeValidator {
    PrecisionDecimalDV() {
    }

    public short getAllowedFacets() {
        return 4088;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        try {
            return new XPrecisionDecimal(string);
        }
        catch (NumberFormatException numberFormatException) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "precisionDecimal"});
        }
    }

    public int compare(Object object, Object object2) {
        return ((XPrecisionDecimal)object).compareTo((XPrecisionDecimal)object2);
    }

    public int getFractionDigits(Object object) {
        return ((XPrecisionDecimal)object).fracDigits;
    }

    public int getTotalDigits(Object object) {
        return ((XPrecisionDecimal)object).totalDigits;
    }

    public boolean isIdentical(Object object, Object object2) {
        if (!(object2 instanceof XPrecisionDecimal) || !(object instanceof XPrecisionDecimal)) {
            return false;
        }
        return ((XPrecisionDecimal)object).isIdentical((XPrecisionDecimal)object2);
    }

    static class XPrecisionDecimal {
        int sign = 1;
        int totalDigits = 0;
        int intDigits = 0;
        int fracDigits = 0;
        String ivalue = "";
        String fvalue = "";
        int pvalue = 0;
        private String canonical;

        XPrecisionDecimal(String string) throws NumberFormatException {
            if (string.equals("NaN")) {
                this.ivalue = string;
                this.sign = 0;
            }
            if (string.equals("+INF") || string.equals("INF") || string.equals("-INF")) {
                this.ivalue = string.charAt(0) == '+' ? string.substring(1) : string;
                return;
            }
            this.initD(string);
        }

        void initD(String string) throws NumberFormatException {
            int n2 = string.length();
            if (n2 == 0) {
                throw new NumberFormatException();
            }
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            if (string.charAt(0) == '+') {
                n3 = 1;
            } else if (string.charAt(0) == '-') {
                n3 = 1;
                this.sign = -1;
            }
            int n7 = n3;
            while (n7 < n2 && string.charAt(n7) == '0') {
                ++n7;
            }
            n4 = n7;
            while (n4 < n2 && TypeValidator.isDigit(string.charAt(n4))) {
                ++n4;
            }
            if (n4 < n2) {
                if (string.charAt(n4) != '.' && string.charAt(n4) != 'E' && string.charAt(n4) != 'e') {
                    throw new NumberFormatException();
                }
                if (string.charAt(n4) == '.') {
                    n6 = n5 = n4 + 1;
                    while (n6 < n2 && TypeValidator.isDigit(string.charAt(n6))) {
                        ++n6;
                    }
                } else {
                    this.pvalue = Integer.parseInt(string.substring(n4 + 1, n2));
                }
            }
            if (n3 == n4 && n5 == n6) {
                throw new NumberFormatException();
            }
            int n8 = n5;
            while (n8 < n6) {
                if (!TypeValidator.isDigit(string.charAt(n8))) {
                    throw new NumberFormatException();
                }
                ++n8;
            }
            this.intDigits = n4 - n7;
            this.fracDigits = n6 - n5;
            if (this.intDigits > 0) {
                this.ivalue = string.substring(n7, n4);
            }
            if (this.fracDigits > 0) {
                this.fvalue = string.substring(n5, n6);
                if (n6 < n2) {
                    this.pvalue = Integer.parseInt(string.substring(n6 + 1, n2));
                }
            }
            this.totalDigits = this.intDigits + this.fracDigits;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof XPrecisionDecimal)) {
                return false;
            }
            XPrecisionDecimal xPrecisionDecimal = (XPrecisionDecimal)object;
            return this.compareTo(xPrecisionDecimal) == 0;
        }

        private int compareFractionalPart(XPrecisionDecimal xPrecisionDecimal) {
            if (this.fvalue.equals(xPrecisionDecimal.fvalue)) {
                return 0;
            }
            StringBuffer stringBuffer = new StringBuffer(this.fvalue);
            StringBuffer stringBuffer2 = new StringBuffer(xPrecisionDecimal.fvalue);
            this.truncateTrailingZeros(stringBuffer, stringBuffer2);
            return stringBuffer.toString().compareTo(stringBuffer2.toString());
        }

        private void truncateTrailingZeros(StringBuffer stringBuffer, StringBuffer stringBuffer2) {
            int n2 = stringBuffer.length() - 1;
            while (n2 >= 0) {
                if (stringBuffer.charAt(n2) != '0') break;
                stringBuffer.deleteCharAt(n2);
                --n2;
            }
            int n3 = stringBuffer2.length() - 1;
            while (n3 >= 0) {
                if (stringBuffer2.charAt(n3) != '0') break;
                stringBuffer2.deleteCharAt(n3);
                --n3;
            }
        }

        public int compareTo(XPrecisionDecimal xPrecisionDecimal) {
            if (this.sign == 0) {
                return 2;
            }
            if (this.ivalue.equals("INF") || xPrecisionDecimal.ivalue.equals("INF")) {
                if (this.ivalue.equals(xPrecisionDecimal.ivalue)) {
                    return 0;
                }
                if (this.ivalue.equals("INF")) {
                    return 1;
                }
                return -1;
            }
            if (this.ivalue.equals("-INF") || xPrecisionDecimal.ivalue.equals("-INF")) {
                if (this.ivalue.equals(xPrecisionDecimal.ivalue)) {
                    return 0;
                }
                if (this.ivalue.equals("-INF")) {
                    return -1;
                }
                return 1;
            }
            if (this.sign != xPrecisionDecimal.sign) {
                return this.sign > xPrecisionDecimal.sign ? 1 : -1;
            }
            return this.sign * this.compare(xPrecisionDecimal);
        }

        private int compare(XPrecisionDecimal xPrecisionDecimal) {
            if (this.pvalue != 0 || xPrecisionDecimal.pvalue != 0) {
                if (this.pvalue == xPrecisionDecimal.pvalue) {
                    return this.intComp(xPrecisionDecimal);
                }
                if (this.intDigits + this.pvalue != xPrecisionDecimal.intDigits + xPrecisionDecimal.pvalue) {
                    return this.intDigits + this.pvalue > xPrecisionDecimal.intDigits + xPrecisionDecimal.pvalue ? 1 : -1;
                }
                if (this.pvalue > xPrecisionDecimal.pvalue) {
                    int n2 = this.pvalue - xPrecisionDecimal.pvalue;
                    StringBuffer stringBuffer = new StringBuffer(this.ivalue);
                    StringBuffer stringBuffer2 = new StringBuffer(this.fvalue);
                    int n3 = 0;
                    while (n3 < n2) {
                        if (n3 < this.fracDigits) {
                            stringBuffer.append(this.fvalue.charAt(n3));
                            stringBuffer2.deleteCharAt(n3);
                        } else {
                            stringBuffer.append('0');
                        }
                        ++n3;
                    }
                    return this.compareDecimal(stringBuffer.toString(), xPrecisionDecimal.ivalue, stringBuffer2.toString(), xPrecisionDecimal.fvalue);
                }
                int n4 = xPrecisionDecimal.pvalue - this.pvalue;
                StringBuffer stringBuffer = new StringBuffer(xPrecisionDecimal.ivalue);
                StringBuffer stringBuffer3 = new StringBuffer(xPrecisionDecimal.fvalue);
                int n5 = 0;
                while (n5 < n4) {
                    if (n5 < xPrecisionDecimal.fracDigits) {
                        stringBuffer.append(xPrecisionDecimal.fvalue.charAt(n5));
                        stringBuffer3.deleteCharAt(n5);
                    } else {
                        stringBuffer.append('0');
                    }
                    ++n5;
                }
                return this.compareDecimal(this.ivalue, stringBuffer.toString(), this.fvalue, stringBuffer3.toString());
            }
            return this.intComp(xPrecisionDecimal);
        }

        private int intComp(XPrecisionDecimal xPrecisionDecimal) {
            if (this.intDigits != xPrecisionDecimal.intDigits) {
                return this.intDigits > xPrecisionDecimal.intDigits ? 1 : -1;
            }
            return this.compareDecimal(this.ivalue, xPrecisionDecimal.ivalue, this.fvalue, xPrecisionDecimal.fvalue);
        }

        private int compareDecimal(String string, String string2, String string3, String string4) {
            int n2 = string.compareTo(string3);
            if (n2 != 0) {
                return n2 > 0 ? 1 : -1;
            }
            if (string2.equals(string4)) {
                return 0;
            }
            StringBuffer stringBuffer = new StringBuffer(string2);
            StringBuffer stringBuffer2 = new StringBuffer(string4);
            this.truncateTrailingZeros(stringBuffer, stringBuffer2);
            n2 = stringBuffer.toString().compareTo(stringBuffer2.toString());
            return n2 == 0 ? 0 : (n2 > 0 ? 1 : -1);
        }

        public synchronized String toString() {
            if (this.canonical == null) {
                this.makeCanonical();
            }
            return this.canonical;
        }

        private void makeCanonical() {
            this.canonical = "TBD by Working Group";
        }

        public boolean isIdentical(XPrecisionDecimal xPrecisionDecimal) {
            if (this.ivalue.equals(xPrecisionDecimal.ivalue) && (this.ivalue.equals("INF") || this.ivalue.equals("-INF") || this.ivalue.equals("NaN"))) {
                return true;
            }
            if (this.sign == xPrecisionDecimal.sign && this.intDigits == xPrecisionDecimal.intDigits && this.fracDigits == xPrecisionDecimal.fracDigits && this.pvalue == xPrecisionDecimal.pvalue && this.ivalue.equals(xPrecisionDecimal.ivalue) && this.fvalue.equals(xPrecisionDecimal.fvalue)) {
                return true;
            }
            return false;
        }
    }

}

