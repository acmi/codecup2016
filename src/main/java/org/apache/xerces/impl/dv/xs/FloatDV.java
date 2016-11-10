/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.DoubleDV;
import org.apache.xerces.impl.dv.xs.TypeValidator;
import org.apache.xerces.xs.datatypes.XSFloat;

public class FloatDV
extends TypeValidator {
    public short getAllowedFacets() {
        return 2552;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        try {
            return new XFloat(string);
        }
        catch (NumberFormatException numberFormatException) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "float"});
        }
    }

    public int compare(Object object, Object object2) {
        return XFloat.access$000((XFloat)object, (XFloat)object2);
    }

    public boolean isIdentical(Object object, Object object2) {
        if (object2 instanceof XFloat) {
            return ((XFloat)object).isIdentical((XFloat)object2);
        }
        return false;
    }

    private static final class XFloat
    implements XSFloat {
        private final float value;
        private String canonical;

        public XFloat(String string) throws NumberFormatException {
            if (DoubleDV.isPossibleFP(string)) {
                this.value = Float.parseFloat(string);
            } else if (string.equals("INF")) {
                this.value = Float.POSITIVE_INFINITY;
            } else if (string.equals("-INF")) {
                this.value = Float.NEGATIVE_INFINITY;
            } else if (string.equals("NaN")) {
                this.value = Float.NaN;
            } else {
                throw new NumberFormatException(string);
            }
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof XFloat)) {
                return false;
            }
            XFloat xFloat = (XFloat)object;
            if (this.value == xFloat.value) {
                return true;
            }
            if (this.value != this.value && xFloat.value != xFloat.value) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.value == 0.0f ? 0 : Float.floatToIntBits(this.value);
        }

        public boolean isIdentical(XFloat xFloat) {
            if (xFloat == this) {
                return true;
            }
            if (this.value == xFloat.value) {
                return this.value != 0.0f || Float.floatToIntBits(this.value) == Float.floatToIntBits(xFloat.value);
            }
            if (this.value != this.value && xFloat.value != xFloat.value) {
                return true;
            }
            return false;
        }

        private int compareTo(XFloat xFloat) {
            float f2 = xFloat.value;
            if (this.value < f2) {
                return -1;
            }
            if (this.value > f2) {
                return 1;
            }
            if (this.value == f2) {
                return 0;
            }
            if (this.value != this.value) {
                if (f2 != f2) {
                    return 0;
                }
                return 2;
            }
            return 2;
        }

        public synchronized String toString() {
            if (this.canonical == null) {
                if (this.value == Float.POSITIVE_INFINITY) {
                    this.canonical = "INF";
                } else if (this.value == Float.NEGATIVE_INFINITY) {
                    this.canonical = "-INF";
                } else if (this.value != this.value) {
                    this.canonical = "NaN";
                } else if (this.value == 0.0f) {
                    this.canonical = "0.0E1";
                } else {
                    this.canonical = Float.toString(this.value);
                    if (this.canonical.indexOf(69) == -1) {
                        int n2;
                        int n3 = this.canonical.length();
                        char[] arrc = new char[n3 + 3];
                        this.canonical.getChars(0, n3, arrc, 0);
                        int n4 = n2 = arrc[0] == '-' ? 2 : 1;
                        if (this.value >= 1.0f || this.value <= -1.0f) {
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

        public float getValue() {
            return this.value;
        }

        static int access$000(XFloat xFloat, XFloat xFloat2) {
            return xFloat.compareTo(xFloat2);
        }
    }

}

