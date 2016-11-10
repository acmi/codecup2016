/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import org.apache.xml.utils.XMLString;
import org.apache.xpath.objects.Comparator;

class NotEqualComparator
extends Comparator {
    NotEqualComparator() {
    }

    boolean compareStrings(XMLString xMLString, XMLString xMLString2) {
        return !xMLString.equals(xMLString2);
    }

    boolean compareNumbers(double d2, double d3) {
        return d2 != d3;
    }
}

