/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xpath.XPath;
import org.apache.xerces.util.IntStack;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSTypeDefinition;

public class XPathMatcher {
    protected static final boolean DEBUG_ALL = false;
    protected static final boolean DEBUG_METHODS = false;
    protected static final boolean DEBUG_METHODS2 = false;
    protected static final boolean DEBUG_METHODS3 = false;
    protected static final boolean DEBUG_MATCH = false;
    protected static final boolean DEBUG_STACK = false;
    protected static final boolean DEBUG_ANY = false;
    protected static final int MATCHED = 1;
    protected static final int MATCHED_ATTRIBUTE = 3;
    protected static final int MATCHED_DESCENDANT = 5;
    protected static final int MATCHED_DESCENDANT_PREVIOUS = 13;
    private final XPath.LocationPath[] fLocationPaths;
    private final int[] fMatched;
    protected Object fMatchedString;
    private final IntStack[] fStepIndexes;
    private final int[] fCurrentStep;
    private final int[] fNoMatchDepth;
    final QName fQName = new QName();

    public XPathMatcher(XPath xPath) {
        this.fLocationPaths = xPath.getLocationPaths();
        this.fStepIndexes = new IntStack[this.fLocationPaths.length];
        int n2 = 0;
        while (n2 < this.fStepIndexes.length) {
            this.fStepIndexes[n2] = new IntStack();
            ++n2;
        }
        this.fCurrentStep = new int[this.fLocationPaths.length];
        this.fNoMatchDepth = new int[this.fLocationPaths.length];
        this.fMatched = new int[this.fLocationPaths.length];
    }

    public boolean isMatched() {
        int n2 = 0;
        while (n2 < this.fLocationPaths.length) {
            if ((this.fMatched[n2] & 1) == 1 && (this.fMatched[n2] & 13) != 13 && (this.fNoMatchDepth[n2] == 0 || (this.fMatched[n2] & 5) == 5)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    protected void handleContent(XSTypeDefinition xSTypeDefinition, boolean bl, Object object, short s2, ShortList shortList) {
    }

    protected void matched(Object object, short s2, ShortList shortList, boolean bl) {
    }

    public void startDocumentFragment() {
        this.fMatchedString = null;
        int n2 = 0;
        while (n2 < this.fLocationPaths.length) {
            this.fStepIndexes[n2].clear();
            this.fCurrentStep[n2] = 0;
            this.fNoMatchDepth[n2] = 0;
            this.fMatched[n2] = 0;
            ++n2;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void startElement(QName var1_1, XMLAttributes var2_2) {
        var3_3 = 0;
        while (var3_3 < this.fLocationPaths.length) {
            var4_4 = this.fCurrentStep[var3_3];
            this.fStepIndexes[var3_3].push(var4_4);
            if ((this.fMatched[var3_3] & 5) != 1 && this.fNoMatchDepth[var3_3] <= 0) ** GOTO lbl10
            v0 = this.fNoMatchDepth;
            v1 = var3_3;
            v0[v1] = v0[v1] + 1;
            ** GOTO lbl85
lbl10: // 1 sources:
            if ((this.fMatched[var3_3] & 5) == 5) {
                this.fMatched[var3_3] = 13;
            }
            var5_5 = this.fLocationPaths[var3_3].steps;
            while (this.fCurrentStep[var3_3] < var5_5.length && var5_5[this.fCurrentStep[var3_3]].axis.type == 3) {
                v2 = this.fCurrentStep;
                v3 = var3_3;
                v2[v3] = v2[v3] + 1;
            }
            if (this.fCurrentStep[var3_3] != var5_5.length) ** GOTO lbl21
            this.fMatched[var3_3] = 1;
            ** GOTO lbl85
lbl21: // 1 sources:
            var6_6 = this.fCurrentStep[var3_3];
            while (this.fCurrentStep[var3_3] < var5_5.length && var5_5[this.fCurrentStep[var3_3]].axis.type == 4) {
                v4 = this.fCurrentStep;
                v5 = var3_3;
                v4[v5] = v4[v5] + 1;
            }
            v6 = var7_7 = this.fCurrentStep[var3_3] > var6_6;
            if (this.fCurrentStep[var3_3] != var5_5.length) ** GOTO lbl33
            v7 = this.fNoMatchDepth;
            v8 = var3_3;
            v7[v8] = v7[v8] + 1;
            ** GOTO lbl85
lbl33: // 1 sources:
            if (this.fCurrentStep[var3_3] != var4_4 && this.fCurrentStep[var3_3] <= var6_6 || var5_5[this.fCurrentStep[var3_3]].axis.type != 1) ** GOTO lbl47
            var8_8 = var5_5[this.fCurrentStep[var3_3]];
            var9_10 = var8_8.nodeTest;
            if (XPathMatcher.matches(var9_10, var1_1)) ** GOTO lbl44
            if (this.fCurrentStep[var3_3] > var6_6) {
                this.fCurrentStep[var3_3] = var6_6;
            } else {
                v9 = this.fNoMatchDepth;
                v10 = var3_3;
                v9[v10] = v9[v10] + 1;
            }
            ** GOTO lbl85
lbl44: // 1 sources:
            v11 = this.fCurrentStep;
            v12 = var3_3;
            v11[v12] = v11[v12] + 1;
lbl47: // 2 sources:
            if (this.fCurrentStep[var3_3] != var5_5.length) ** GOTO lbl54
            if (var7_7) {
                this.fCurrentStep[var3_3] = var6_6;
                this.fMatched[var3_3] = 5;
            } else {
                this.fMatched[var3_3] = 1;
            }
            ** GOTO lbl85
lbl54: // 1 sources:
            if (this.fCurrentStep[var3_3] >= var5_5.length || var5_5[this.fCurrentStep[var3_3]].axis.type != 2) ** GOTO lbl85
            var8_9 = var2_2.getLength();
            if (var8_9 <= 0) ** GOTO lbl78
            var9_10 = var5_5[this.fCurrentStep[var3_3]].nodeTest;
            var10_11 = 0;
            while (var10_11 < var8_9) {
                var2_2.getName(var10_11, this.fQName);
                if (!XPathMatcher.matches(var9_10, this.fQName)) ** GOTO lbl69
                v13 = this.fCurrentStep;
                v14 = var3_3;
                v13[v14] = v13[v14] + 1;
                if (this.fCurrentStep[var3_3] != var5_5.length) break;
                this.fMatched[var3_3] = 3;
                var11_12 = 0;
                ** GOTO lbl72
lbl69: // 1 sources:
                ++var10_11;
                continue;
lbl-1000: // 1 sources:
                {
                    ++var11_12;
lbl72: // 2 sources:
                    ** while (var11_12 < var3_3 && (this.fMatched[var11_12] & 1) != 1)
                }
lbl73: // 1 sources:
                if (var11_12 != var3_3) break;
                var12_13 = (AttributePSVI)var2_2.getAugmentations(var10_11).getItem("ATTRIBUTE_PSVI");
                this.fMatchedString = var12_13.getActualNormalizedValue();
                this.matched(this.fMatchedString, var12_13.getActualNormalizedValueType(), var12_13.getItemValueTypes(), false);
                break;
            }
lbl78: // 5 sources:
            if ((this.fMatched[var3_3] & 1) != 1) {
                if (this.fCurrentStep[var3_3] > var6_6) {
                    this.fCurrentStep[var3_3] = var6_6;
                } else {
                    v15 = this.fNoMatchDepth;
                    v16 = var3_3;
                    v15[v16] = v15[v16] + 1;
                }
            }
lbl85: // 12 sources:
            ++var3_3;
        }
    }

    public void endElement(QName qName, XSTypeDefinition xSTypeDefinition, boolean bl, Object object, short s2, ShortList shortList) {
        int n2 = 0;
        while (n2 < this.fLocationPaths.length) {
            this.fCurrentStep[n2] = this.fStepIndexes[n2].pop();
            if (this.fNoMatchDepth[n2] > 0) {
                int[] arrn = this.fNoMatchDepth;
                int n3 = n2;
                arrn[n3] = arrn[n3] - 1;
            } else {
                int n4 = 0;
                while (n4 < n2 && (this.fMatched[n4] & 1) != 1) {
                    ++n4;
                }
                if (n4 >= n2 && this.fMatched[n4] != 0) {
                    if ((this.fMatched[n4] & 3) == 3) {
                        this.fMatched[n2] = 0;
                    } else {
                        this.handleContent(xSTypeDefinition, bl, object, s2, shortList);
                        this.fMatched[n2] = 0;
                    }
                }
            }
            ++n2;
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = super.toString();
        int n2 = string.lastIndexOf(46);
        if (n2 != -1) {
            string = string.substring(n2 + 1);
        }
        stringBuffer.append(string);
        int n3 = 0;
        while (n3 < this.fLocationPaths.length) {
            stringBuffer.append('[');
            XPath.Step[] arrstep = this.fLocationPaths[n3].steps;
            int n4 = 0;
            while (n4 < arrstep.length) {
                if (n4 == this.fCurrentStep[n3]) {
                    stringBuffer.append('^');
                }
                stringBuffer.append(arrstep[n4].toString());
                if (n4 < arrstep.length - 1) {
                    stringBuffer.append('/');
                }
                ++n4;
            }
            if (this.fCurrentStep[n3] == arrstep.length) {
                stringBuffer.append('^');
            }
            stringBuffer.append(']');
            stringBuffer.append(',');
            ++n3;
        }
        return stringBuffer.toString();
    }

    private String normalize(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            switch (c2) {
                case '\n': {
                    stringBuffer.append("\\n");
                    break;
                }
                default: {
                    stringBuffer.append(c2);
                }
            }
            ++n3;
        }
        return stringBuffer.toString();
    }

    private static boolean matches(XPath.NodeTest nodeTest, QName qName) {
        if (nodeTest.type == 1) {
            return nodeTest.name.equals(qName);
        }
        if (nodeTest.type == 4) {
            return nodeTest.name.uri == qName.uri;
        }
        return true;
    }
}

