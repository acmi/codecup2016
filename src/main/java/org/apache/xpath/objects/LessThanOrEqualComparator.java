/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import org.apache.xml.utils.XMLString;
import org.apache.xpath.objects.Comparator;

class LessThanOrEqualComparator
extends Comparator {
    LessThanOrEqualComparator() {
    }

    boolean compareStrings(XMLString xMLString, XMLString xMLString2) {
        return xMLString.toDouble() <= xMLString2.toDouble();
    }

    boolean compareNumbers(double d2, double d3) {
        return d2 <= d3;
    }
}

