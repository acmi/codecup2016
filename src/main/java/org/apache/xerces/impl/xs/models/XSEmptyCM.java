/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.models;

import java.util.Vector;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.xni.QName;

public class XSEmptyCM
implements XSCMValidator {
    private static final short STATE_START = 0;
    private static final Vector EMPTY = new Vector(0);

    public int[] startContentModel() {
        return new int[]{0};
    }

    public Object oneTransition(QName qName, int[] arrn, SubstitutionGroupHandler substitutionGroupHandler) {
        if (arrn[0] < 0) {
            arrn[0] = -2;
            return null;
        }
        arrn[0] = -1;
        return null;
    }

    public boolean endContentModel(int[] arrn) {
        boolean bl = false;
        int n2 = arrn[0];
        if (n2 < 0) {
            return false;
        }
        return true;
    }

    public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler substitutionGroupHandler) throws XMLSchemaException {
        return false;
    }

    public Vector whatCanGoHere(int[] arrn) {
        return EMPTY;
    }

    public int[] occurenceInfo(int[] arrn) {
        return null;
    }

    public String getTermName(int n2) {
        return null;
    }

    public boolean isCompactedForUPA() {
        return false;
    }
}

