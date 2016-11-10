/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan;

import java.io.PrintStream;

public class Version {
    public static String getVersion() {
        return Version.getProduct() + " " + Version.getImplementationLanguage() + " " + Version.getMajorVersionNum() + "." + Version.getReleaseVersionNum() + "." + (Version.getDevelopmentVersionNum() > 0 ? new StringBuffer().append("D").append(Version.getDevelopmentVersionNum()).toString() : new StringBuffer().append("").append(Version.getMaintenanceVersionNum()).toString());
    }

    public static void main(String[] arrstring) {
        System.out.println(Version.getVersion());
    }

    public static String getProduct() {
        return "Xalan";
    }

    public static String getImplementationLanguage() {
        return "Java";
    }

    public static int getMajorVersionNum() {
        return 2;
    }

    public static int getReleaseVersionNum() {
        return 7;
    }

    public static int getMaintenanceVersionNum() {
        return 2;
    }

    public static int getDevelopmentVersionNum() {
        try {
            if (new String("").length() == 0) {
                return 0;
            }
            return Integer.parseInt("");
        }
        catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }
}

