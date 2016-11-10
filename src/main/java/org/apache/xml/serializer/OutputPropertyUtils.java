/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.util.Properties;

public final class OutputPropertyUtils {
    public static boolean getBooleanProperty(String string, Properties properties) {
        String string2 = properties.getProperty(string);
        if (null == string2 || !string2.equals("yes")) {
            return false;
        }
        return true;
    }

    public static int getIntProperty(String string, Properties properties) {
        String string2 = properties.getProperty(string);
        if (null == string2) {
            return 0;
        }
        return Integer.parseInt(string2);
    }
}

