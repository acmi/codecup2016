/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import org.apache.xalan.lib.ExsltBase;
import org.apache.xpath.NodeSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExsltMath
extends ExsltBase {
    private static String PI = "3.1415926535897932384626433832795028841971693993751";
    private static String E = "2.71828182845904523536028747135266249775724709369996";
    private static String SQRRT2 = "1.41421356237309504880168872420969807856967187537694";
    private static String LN2 = "0.69314718055994530941723212145817656807550013436025";
    private static String LN10 = "2.302585092994046";
    private static String LOG2E = "1.4426950408889633";
    private static String SQRT1_2 = "0.7071067811865476";

    public static double max(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return Double.NaN;
        }
        double d2 = -1.7976931348623157E308;
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            Node node = nodeList.item(i2);
            double d3 = ExsltMath.toNumber(node);
            if (Double.isNaN(d3)) {
                return Double.NaN;
            }
            if (d3 <= d2) continue;
            d2 = d3;
        }
        return d2;
    }

    public static double min(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return Double.NaN;
        }
        double d2 = Double.MAX_VALUE;
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            Node node = nodeList.item(i2);
            double d3 = ExsltMath.toNumber(node);
            if (Double.isNaN(d3)) {
                return Double.NaN;
            }
            if (d3 >= d2) continue;
            d2 = d3;
        }
        return d2;
    }

    public static NodeList highest(NodeList nodeList) {
        double d2 = ExsltMath.max(nodeList);
        NodeSet nodeSet = new NodeSet();
        nodeSet.setShouldCacheNodes(true);
        if (Double.isNaN(d2)) {
            return nodeSet;
        }
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            Node node = nodeList.item(i2);
            double d3 = ExsltMath.toNumber(node);
            if (d3 != d2) continue;
            nodeSet.addElement(node);
        }
        return nodeSet;
    }

    public static NodeList lowest(NodeList nodeList) {
        double d2 = ExsltMath.min(nodeList);
        NodeSet nodeSet = new NodeSet();
        nodeSet.setShouldCacheNodes(true);
        if (Double.isNaN(d2)) {
            return nodeSet;
        }
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            Node node = nodeList.item(i2);
            double d3 = ExsltMath.toNumber(node);
            if (d3 != d2) continue;
            nodeSet.addElement(node);
        }
        return nodeSet;
    }

    public static double abs(double d2) {
        return Math.abs(d2);
    }

    public static double acos(double d2) {
        return Math.acos(d2);
    }

    public static double asin(double d2) {
        return Math.asin(d2);
    }

    public static double atan(double d2) {
        return Math.atan(d2);
    }

    public static double atan2(double d2, double d3) {
        return Math.atan2(d2, d3);
    }

    public static double cos(double d2) {
        return Math.cos(d2);
    }

    public static double exp(double d2) {
        return Math.exp(d2);
    }

    public static double log(double d2) {
        return Math.log(d2);
    }

    public static double power(double d2, double d3) {
        return Math.pow(d2, d3);
    }

    public static double random() {
        return Math.random();
    }

    public static double sin(double d2) {
        return Math.sin(d2);
    }

    public static double sqrt(double d2) {
        return Math.sqrt(d2);
    }

    public static double tan(double d2) {
        return Math.tan(d2);
    }

    public static double constant(String string, double d2) {
        String string2 = null;
        if (string.equals("PI")) {
            string2 = PI;
        } else if (string.equals("E")) {
            string2 = E;
        } else if (string.equals("SQRRT2")) {
            string2 = SQRRT2;
        } else if (string.equals("LN2")) {
            string2 = LN2;
        } else if (string.equals("LN10")) {
            string2 = LN10;
        } else if (string.equals("LOG2E")) {
            string2 = LOG2E;
        } else if (string.equals("SQRT1_2")) {
            string2 = SQRT1_2;
        }
        if (string2 != null) {
            int n2 = new Double(d2).intValue();
            if (n2 <= string2.length()) {
                string2 = string2.substring(0, n2);
            }
            return new Double(string2);
        }
        return Double.NaN;
    }
}

