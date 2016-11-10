/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.util.Vector;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;

public abstract class NodeCounter {
    public static final int END = -1;
    protected int _node = -1;
    protected int _nodeType = -1;
    protected double _value = -2.147483648E9;
    public final DOM _document;
    public final DTMAxisIterator _iterator;
    public final Translet _translet;
    protected String _format;
    protected String _lang;
    protected String _letterValue;
    protected String _groupSep;
    protected int _groupSize;
    private boolean _separFirst = true;
    private boolean _separLast = false;
    private Vector _separToks = new Vector();
    private Vector _formatToks = new Vector();
    private int _nSepars = 0;
    private int _nFormats = 0;
    private static final String[] Thousands = new String[]{"", "m", "mm", "mmm"};
    private static final String[] Hundreds = new String[]{"", "c", "cc", "ccc", "cd", "d", "dc", "dcc", "dccc", "cm"};
    private static final String[] Tens = new String[]{"", "x", "xx", "xxx", "xl", "l", "lx", "lxx", "lxxx", "xc"};
    private static final String[] Ones = new String[]{"", "i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix"};
    private StringBuffer _tempBuffer = new StringBuffer();

    protected NodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
        this._translet = translet;
        this._document = dOM;
        this._iterator = dTMAxisIterator;
    }

    public abstract NodeCounter setStartNode(int var1);

    public NodeCounter setValue(double d2) {
        this._value = d2;
        return this;
    }

    protected void setFormatting(String string, String string2, String string3, String string4, String string5) {
        this._lang = string2;
        this._groupSep = string4;
        this._letterValue = string3;
        try {
            this._groupSize = Integer.parseInt(string5);
        }
        catch (NumberFormatException numberFormatException) {
            this._groupSize = 0;
        }
        this.setTokens(string);
    }

    private final void setTokens(String string) {
        if (this._format != null && string.equals(this._format)) {
            return;
        }
        this._format = string;
        int n2 = this._format.length();
        boolean bl = true;
        this._separFirst = true;
        this._separLast = false;
        this._nSepars = 0;
        this._nFormats = 0;
        this._separToks.clear();
        this._formatToks.clear();
        int n3 = 0;
        int n4 = 0;
        while (n4 < n2) {
            char c2 = string.charAt(n4);
            n3 = n4;
            while (Character.isLetterOrDigit(c2) && ++n4 != n2) {
                c2 = string.charAt(n4);
            }
            if (n4 > n3) {
                if (bl) {
                    this._separToks.addElement(".");
                    this._separFirst = false;
                    bl = false;
                }
                this._formatToks.addElement(string.substring(n3, n4));
            }
            if (n4 == n2) break;
            c2 = string.charAt(n4);
            n3 = n4;
            while (!Character.isLetterOrDigit(c2) && ++n4 != n2) {
                c2 = string.charAt(n4);
                bl = false;
            }
            if (n4 <= n3) continue;
            this._separToks.addElement(string.substring(n3, n4));
        }
        this._nSepars = this._separToks.size();
        this._nFormats = this._formatToks.size();
        if (this._nSepars > this._nFormats) {
            this._separLast = true;
        }
        if (this._separFirst) {
            --this._nSepars;
        }
        if (this._separLast) {
            --this._nSepars;
        }
        if (this._nSepars == 0) {
            this._separToks.insertElementAt(".", 1);
            ++this._nSepars;
        }
        if (this._separFirst) {
            ++this._nSepars;
        }
    }

    public NodeCounter setDefaultFormatting() {
        this.setFormatting("1", "en", "alphabetic", null, null);
        return this;
    }

    public abstract String getCounter();

    public String getCounter(String string, String string2, String string3, String string4, String string5) {
        this.setFormatting(string, string2, string3, string4, string5);
        return this.getCounter();
    }

    public boolean matchesCount(int n2) {
        return this._nodeType == this._document.getExpandedTypeID(n2);
    }

    public boolean matchesFrom(int n2) {
        return false;
    }

    protected String formatNumbers(int n2) {
        return this.formatNumbers(new int[]{n2});
    }

    protected String formatNumbers(int[] arrn) {
        int n2;
        int n3 = arrn.length;
        int n4 = this._format.length();
        boolean bl = true;
        for (n2 = 0; n2 < n3; ++n2) {
            if (arrn[n2] == Integer.MIN_VALUE) continue;
            bl = false;
        }
        if (bl) {
            return "";
        }
        n2 = 1;
        int n5 = 0;
        int n6 = 0;
        int n7 = 1;
        this._tempBuffer.setLength(0);
        StringBuffer stringBuffer = this._tempBuffer;
        if (this._separFirst) {
            stringBuffer.append((String)this._separToks.elementAt(0));
        }
        while (n6 < n3) {
            int n8 = arrn[n6];
            if (n8 != Integer.MIN_VALUE) {
                if (n2 == 0) {
                    stringBuffer.append((String)this._separToks.elementAt(n7++));
                }
                this.formatValue(n8, (String)this._formatToks.elementAt(n5++), stringBuffer);
                if (n5 == this._nFormats) {
                    --n5;
                }
                if (n7 >= this._nSepars) {
                    --n7;
                }
                n2 = 0;
            }
            ++n6;
        }
        if (this._separLast) {
            stringBuffer.append((String)this._separToks.lastElement());
        }
        return stringBuffer.toString();
    }

    private void formatValue(int n2, String string, StringBuffer stringBuffer) {
        int n3 = string.charAt(0);
        if (Character.isDigit((char)n3)) {
            int n4;
            char c2 = (char)(n3 - Character.getNumericValue((char)n3));
            StringBuffer stringBuffer2 = stringBuffer;
            if (this._groupSize > 0) {
                stringBuffer2 = new StringBuffer();
            }
            String string2 = "";
            for (int i2 = n2; i2 > 0; i2 /= 10) {
                string2 = "" + (char)(c2 + i2 % 10) + string2;
            }
            for (n4 = 0; n4 < string.length() - string2.length(); ++n4) {
                stringBuffer2.append(c2);
            }
            stringBuffer2.append(string2);
            if (this._groupSize > 0) {
                for (n4 = 0; n4 < stringBuffer2.length(); ++n4) {
                    if (n4 != 0 && (stringBuffer2.length() - n4) % this._groupSize == 0) {
                        stringBuffer.append(this._groupSep);
                    }
                    stringBuffer.append(stringBuffer2.charAt(n4));
                }
            }
        } else if (n3 == 105 && !this._letterValue.equals("alphabetic")) {
            stringBuffer.append(this.romanValue(n2));
        } else if (n3 == 73 && !this._letterValue.equals("alphabetic")) {
            stringBuffer.append(this.romanValue(n2).toUpperCase());
        } else {
            int n5 = n3;
            int n6 = n3;
            if (n3 >= 945 && n3 <= 969) {
                n6 = 969;
            } else {
                while (Character.isLetterOrDigit((char)(n6 + '\u0001'))) {
                    ++n6;
                }
            }
            stringBuffer.append(this.alphaValue(n2, n5, n6));
        }
    }

    private String alphaValue(int n2, int n3, int n4) {
        if (n2 <= 0) {
            return "" + n2;
        }
        int n5 = n4 - n3 + 1;
        char c2 = (char)((n2 - 1) % n5 + n3);
        if (n2 > n5) {
            return this.alphaValue((n2 - 1) / n5, n3, n4) + c2;
        }
        return "" + c2;
    }

    private String romanValue(int n2) {
        if (n2 <= 0 || n2 > 4000) {
            return "" + n2;
        }
        return Thousands[n2 / 1000] + Hundreds[n2 / 100 % 10] + Tens[n2 / 10 % 10] + Ones[n2 % 10];
    }
}

