/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.TypeValidator;
import org.apache.xerces.xs.datatypes.XSDouble;

public class DoubleDV
extends TypeValidator {
    public short getAllowedFacets() {
        return 2552;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        try {
            return new XDouble(string);
        }
        catch (NumberFormatException numberFormatException) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "double"});
        }
    }

    public int compare(Object object, Object object2) {
        return XDouble.access$000((XDouble)object, (XDouble)object2);
    }

    public boolean isIdentical(Object object, Object object2) {
        if (object2 instanceof XDouble) {
            return ((XDouble)object).isIdentical((XDouble)object2);
        }
        return false;
    }

    static boolean isPossibleFP(String string) {
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if ((c2 < '0' || c2 > '9') && c2 != '.' && c2 != '-' && c2 != '+' && c2 != 'E' && c2 != 'e') {
                return false;
            }
            ++n3;
        }
        return true;
    }

    private static final class XDouble
    implements XSDouble {
        private final double value;
        private String canonical;

        public XDouble(String string) throws NumberFormatException {
            if (DoubleDV.isPossibleFP(string)) {
                this.value = Double.parseDouble(string);
            } else if (string.equals("INF")) {
                this.value = Double.POSITIVE_INFINITY;
            } else if (string.equals("-INF")) {
                this.value = Double.NEGATIVE_INFINITY;
            } else if (string.equals("NaN")) {
                this.value = Double.NaN;
            } else {
                throw new NumberFormatException(string);
            }
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof XDouble)) {
                return false;
            }
            XDouble xDouble = (XDouble)object;
            if (this.value == xDouble.value) {
                return true;
            }
            if (this.value != this.value && xDouble.value != xDouble.value) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (this.value == 0.0) {
                return 0;
            }
            long l2 = Double.doubleToLongBits(this.value);
            return (int)(l2 ^ l2 >>> 32);
        }

        public boolean isIdentical(XDouble xDouble) {
            if (xDouble == this) {
                return true;
            }
            if (this.value == xDouble.value) {
                return this.value != 0.0 || Double.doubleToLongBits(this.value) == Double.doubleToLongBits(xDouble.value);
            }
            if (this.value != this.value && xDouble.value != xDouble.value) {
                return true;
            }
            return false;
        }

        private int compareTo(XDouble xDouble) {
            double d2 = xDouble.value;
            if (this.value < d2) {
                return -1;
            }
            if (this.value > d2) {
                return 1;
            }
            if (this.value == d2) {
                return 0;
            }
            if (this.value != this.value) {
                if (d2 != d2) {
                    return 0;
                }
                return 2;
            }
            return 2;
        }

        public synchronized String toString() {
            if (this.canonical == null) {
                if (this.value == Double.POSITIVE_INFINITY) {
                    this.canonical = "INF";
                } else if (this.value == Double.NEGATIVE_INFINITY) {
                    this.canonical = "-INF";
                } else if (this.value != this.value) {
                    this.canonical = "NaN";
                } else if (this.value == 0.0) {
                    this.canonical = "0.0E1";
                } else {
                    this.canonical = Double.toString(this.value);
                    if (this.canonical.indexOf(69) == -1) {
                        int n2;
                        int n3 = this.canonical.length();
                        char[] arrc = new char[n3 + 3];
                        this.canonical.getChars(0, n3, arrc, 0);
                        int n4 = n2 = arrc[0] == '-' ? 2 : 1;
                        if (this.value >= 1.0 || this.value <= -1.0) {
                            int n5;
                            int n6 = n5 = this.canonical.indexOf(46);
                            while (n6 > n2) {
                                arrc[n6] = arrc[n6 - 1];
                                --n6;
                            }
                            arrc[n2] = 46;
                            while (arrc[n3 - 1] == '0') {
                                --n3;
                            }
                            if (arrc[n3 - 1] == '.') {
                                ++n3;
                            }
                            arrc[n3++] = 69;
                            int n7 = n5 - n2;
                            arrc[n3++] = (char)(n7 + 48);
                        } else {
                            int n8 = n2 + 1;
                            while (arrc[n8] == '0') {
                                ++n8;
                            }
                            arrc[n2 - 1] = arrc[n8];
                            arrc[n2] = 46;
                            int n9 = n8 + 1;
                            int n10 = n2 + 1;
                            while (n9 < n3) {
                                arrc[n10] = arrc[n9];
                                ++n9;
                                ++n10;
                            }
                            if ((n3 -= n8 - n2) == n2 + 1) {
                                arrc[n3++] = 48;
                            }
                            arrc[n3++] = 69;
                            arrc[n3++] = 45;
                            int n11 = n8 - n2;
                            arrc[n3++] = (char)(n11 + 48);
                        }
                        this.canonical = new String(arrc, 0, n3);
                    }
                }
            }
            return this.canonical;
        }

        public double getValue() {
            return this.value;
        }

        static int access$000(XDouble xDouble, XDouble xDouble2) {
            return xDouble.compareTo(xDouble2);
        }
    }

}

