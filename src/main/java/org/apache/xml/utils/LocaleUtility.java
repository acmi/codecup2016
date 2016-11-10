/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.Locale;

public class LocaleUtility {
    public static Locale langToLocale(String string) {
        if (string == null || string.equals("")) {
            return Locale.getDefault();
        }
        String string2 = "";
        String string3 = "";
        String string4 = "";
        int n2 = string.indexOf(45);
        if (n2 < 0) {
            string2 = string;
        } else {
            int n3;
            string2 = string.substring(0, n2);
            if ((n3 = string.indexOf(45, ++n2)) < 0) {
                string3 = string.substring(n2);
            } else {
                string3 = string.substring(n2, n3);
                string4 = string.substring(n3 + 1);
            }
        }
        string2 = string2.length() == 2 ? string2.toLowerCase() : "";
        string3 = string3.length() == 2 ? string3.toUpperCase() : "";
        string4 = string4.length() > 0 && (string2.length() == 2 || string3.length() == 2) ? string4.toUpperCase() : "";
        return new Locale(string2, string3, string4);
    }
}

