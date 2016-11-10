/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

public class DecimalToRoman {
    public long m_postValue;
    public String m_postLetter;
    public long m_preValue;
    public String m_preLetter;

    public DecimalToRoman(long l2, String string, long l3, String string2) {
        this.m_postValue = l2;
        this.m_postLetter = string;
        this.m_preValue = l3;
        this.m_preLetter = string2;
    }
}

