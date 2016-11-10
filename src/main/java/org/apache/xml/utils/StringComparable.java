/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.text.CollationElementIterator;
import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Locale;

public class StringComparable
implements Comparable {
    private String m_text;
    private Locale m_locale;
    private RuleBasedCollator m_collator;
    private String m_caseOrder;
    private int m_mask = -1;

    public StringComparable(String string, Locale locale, Collator collator, String string2) {
        this.m_text = string;
        this.m_locale = locale;
        this.m_collator = (RuleBasedCollator)collator;
        this.m_caseOrder = string2;
        this.m_mask = StringComparable.getMask(this.m_collator.getStrength());
    }

    public static final Comparable getComparator(String string, Locale locale, Collator collator, String string2) {
        if (string2 == null || string2.length() == 0) {
            return ((RuleBasedCollator)collator).getCollationKey(string);
        }
        return new StringComparable(string, locale, collator, string2);
    }

    public final String toString() {
        return this.m_text;
    }

    public int compareTo(Object object) {
        String string = ((StringComparable)object).toString();
        if (this.m_text.equals(string)) {
            return 0;
        }
        int n2 = this.m_collator.getStrength();
        int n3 = 0;
        if (n2 == 0 || n2 == 1) {
            n3 = this.m_collator.compare(this.m_text, string);
        } else {
            this.m_collator.setStrength(1);
            n3 = this.m_collator.compare(this.m_text, string);
            this.m_collator.setStrength(n2);
        }
        if (n3 != 0) {
            return n3;
        }
        n3 = this.getCaseDiff(this.m_text, string);
        if (n3 != 0) {
            return n3;
        }
        return this.m_collator.compare(this.m_text, string);
    }

    private final int getCaseDiff(String string, String string2) {
        int n2 = this.m_collator.getStrength();
        int n3 = this.m_collator.getDecomposition();
        this.m_collator.setStrength(2);
        this.m_collator.setDecomposition(1);
        int[] arrn = this.getFirstCaseDiff(string, string2, this.m_locale);
        this.m_collator.setStrength(n2);
        this.m_collator.setDecomposition(n3);
        if (arrn != null) {
            if (this.m_caseOrder.equals("upper-first")) {
                if (arrn[0] == 1) {
                    return -1;
                }
                return 1;
            }
            if (arrn[0] == 2) {
                return -1;
            }
            return 1;
        }
        return 0;
    }

    private final int[] getFirstCaseDiff(String string, String string2, Locale locale) {
        int[] arrn;
        CollationElementIterator collationElementIterator = this.m_collator.getCollationElementIterator(string);
        CollationElementIterator collationElementIterator2 = this.m_collator.getCollationElementIterator(string2);
        int n2 = -1;
        int n3 = -1;
        int n4 = -1;
        int n5 = -1;
        int n6 = this.getElement(-1);
        int n7 = 0;
        int n8 = 0;
        boolean bl = true;
        boolean bl2 = true;
        do {
            String string3;
            if (bl) {
                n4 = collationElementIterator2.getOffset();
                n7 = this.getElement(collationElementIterator2.next());
                n5 = collationElementIterator2.getOffset();
            }
            if (bl2) {
                n2 = collationElementIterator.getOffset();
                n8 = this.getElement(collationElementIterator.next());
                n3 = collationElementIterator.getOffset();
            }
            bl = true;
            bl2 = true;
            if (n7 == n6 || n8 == n6) {
                return null;
            }
            if (n8 == 0) {
                bl = false;
                continue;
            }
            if (n7 == 0) {
                bl2 = false;
                continue;
            }
            if (n8 == n7 || n4 >= n5 || n2 >= n3) continue;
            String string4 = string.substring(n2, n3);
            String string5 = string2.substring(n4, n5);
            String string6 = string4.toUpperCase(locale);
            if (this.m_collator.compare(string6, string3 = string5.toUpperCase(locale)) != 0) continue;
            arrn = new int[]{-1, -1};
            if (this.m_collator.compare(string4, string6) == 0) {
                arrn[0] = 1;
            } else if (this.m_collator.compare(string4, string4.toLowerCase(locale)) == 0) {
                arrn[0] = 2;
            }
            if (this.m_collator.compare(string5, string3) == 0) {
                arrn[1] = 1;
            } else if (this.m_collator.compare(string5, string5.toLowerCase(locale)) == 0) {
                arrn[1] = 2;
            }
            if (arrn[0] == 1 && arrn[1] == 2 || arrn[0] == 2 && arrn[1] == 1) break;
        } while (true);
        return arrn;
    }

    private static final int getMask(int n2) {
        switch (n2) {
            case 0: {
                return -65536;
            }
            case 1: {
                return -256;
            }
        }
        return -1;
    }

    private final int getElement(int n2) {
        return n2 & this.m_mask;
    }
}

