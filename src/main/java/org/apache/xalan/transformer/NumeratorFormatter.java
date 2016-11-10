/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.apache.xalan.transformer.DecimalToRoman;
import org.apache.xalan.transformer.TransformerImpl;
import org.w3c.dom.Element;

class NumeratorFormatter {
    protected Element m_xslNumberElement;
    NumberFormatStringTokenizer m_formatTokenizer;
    Locale m_locale;
    NumberFormat m_formatter;
    TransformerImpl m_processor;
    private static final DecimalToRoman[] m_romanConvertTable = new DecimalToRoman[]{new DecimalToRoman(1000, "M", 900, "CM"), new DecimalToRoman(500, "D", 400, "CD"), new DecimalToRoman(100, "C", 90, "XC"), new DecimalToRoman(50, "L", 40, "XL"), new DecimalToRoman(10, "X", 9, "IX"), new DecimalToRoman(5, "V", 4, "IV"), new DecimalToRoman(1, "I", 1, "I")};
    private static final char[] m_alphaCountTable = new char[]{'Z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y'};

    NumeratorFormatter(Element element, TransformerImpl transformerImpl) {
        this.m_xslNumberElement = element;
        this.m_processor = transformerImpl;
    }

    protected String int2alphaCount(int n2, char[] arrc) {
        int n3 = arrc.length;
        char[] arrc2 = new char[100];
        int n4 = arrc2.length - 1;
        int n5 = 1;
        int n6 = 0;
        do {
            n6 = n5 == 0 || n6 != 0 && n5 == n3 - 1 ? n3 - 1 : 0;
            n5 = (n2 + n6) % n3;
            if (n5 == 0 && (n2 /= n3) == 0) break;
            arrc2[n4--] = arrc[n5];
        } while (n2 > 0);
        return new String(arrc2, n4 + 1, arrc2.length - n4 - 1);
    }

    String long2roman(long l2, boolean bl) {
        String string;
        if (l2 <= 0) {
            return "#E(" + l2 + ")";
        }
        int n2 = 0;
        if (l2 <= 3999) {
            StringBuffer stringBuffer = new StringBuffer();
            do {
                if (l2 >= NumeratorFormatter.m_romanConvertTable[n2].m_postValue) {
                    stringBuffer.append(NumeratorFormatter.m_romanConvertTable[n2].m_postLetter);
                    l2 -= NumeratorFormatter.m_romanConvertTable[n2].m_postValue;
                    continue;
                }
                if (bl && l2 >= NumeratorFormatter.m_romanConvertTable[n2].m_preValue) {
                    stringBuffer.append(NumeratorFormatter.m_romanConvertTable[n2].m_preLetter);
                    l2 -= NumeratorFormatter.m_romanConvertTable[n2].m_preValue;
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

    static class NumberFormatStringTokenizer {
        private int currentPosition;
        private int maxPosition;
        private String str;

        NumberFormatStringTokenizer(String string) {
            this.str = string;
            this.maxPosition = string.length();
        }

        void reset() {
            this.currentPosition = 0;
        }

        String nextToken() {
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

        boolean hasMoreTokens() {
            return this.currentPosition < this.maxPosition;
        }

        int countTokens() {
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

}

