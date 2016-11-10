/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.lib.Extensions;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xpath.NodeSet;
import org.apache.xpath.axes.RTFIterator;

public class ExsltCommon {
    public static String objectType(Object object) {
        if (object instanceof String) {
            return "string";
        }
        if (object instanceof Boolean) {
            return "boolean";
        }
        if (object instanceof Number) {
            return "number";
        }
        if (object instanceof DTMNodeIterator) {
            DTMIterator dTMIterator = ((DTMNodeIterator)object).getDTMIterator();
            if (dTMIterator instanceof RTFIterator) {
                return "RTF";
            }
            return "node-set";
        }
        return "unknown";
    }

    public static NodeSet nodeSet(ExpressionContext expressionContext, Object object) {
        return Extensions.nodeset(expressionContext, object);
    }
}

