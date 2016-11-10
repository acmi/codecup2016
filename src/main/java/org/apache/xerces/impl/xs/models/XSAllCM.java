/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.models;

import java.util.Vector;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.xni.QName;

public class XSAllCM
implements XSCMValidator {
    private static final short STATE_START = 0;
    private static final short STATE_VALID = 1;
    private static final short STATE_CHILD = 1;
    private final XSElementDecl[] fAllElements;
    private final boolean[] fIsOptionalElement;
    private final boolean fHasOptionalContent;
    private int fNumElements = 0;

    public XSAllCM(boolean bl, int n2) {
        this.fHasOptionalContent = bl;
        this.fAllElements = new XSElementDecl[n2];
        this.fIsOptionalElement = new boolean[n2];
    }

    public void addElement(XSElementDecl xSElementDecl, boolean bl) {
        this.fAllElements[this.fNumElements] = xSElementDecl;
        this.fIsOptionalElement[this.fNumElements] = bl;
        ++this.fNumElements;
    }

    public int[] startContentModel() {
        int[] arrn = new int[this.fNumElements + 1];
        int n2 = 0;
        while (n2 <= this.fNumElements) {
            arrn[n2] = 0;
            ++n2;
        }
        return arrn;
    }

    Object findMatchingDecl(QName qName, SubstitutionGroupHandler substitutionGroupHandler) {
        XSElementDecl xSElementDecl = null;
        int n2 = 0;
        while (n2 < this.fNumElements) {
            xSElementDecl = substitutionGroupHandler.getMatchingElemDecl(qName, this.fAllElements[n2]);
            if (xSElementDecl != null) break;
            ++n2;
        }
        return xSElementDecl;
    }

    public Object oneTransition(QName qName, int[] arrn, SubstitutionGroupHandler substitutionGroupHandler) {
        if (arrn[0] < 0) {
            arrn[0] = -2;
            return this.findMatchingDecl(qName, substitutionGroupHandler);
        }
        arrn[0] = 1;
        XSElementDecl xSElementDecl = null;
        int n2 = 0;
        while (n2 < this.fNumElements) {
            if (arrn[n2 + 1] == 0 && (xSElementDecl = substitutionGroupHandler.getMatchingElemDecl(qName, this.fAllElements[n2])) != null) {
                arrn[n2 + 1] = 1;
                return xSElementDecl;
            }
            ++n2;
        }
        arrn[0] = -1;
        return this.findMatchingDecl(qName, substitutionGroupHandler);
    }

    public boolean endContentModel(int[] arrn) {
        int n2 = arrn[0];
        if (n2 == -1 || n2 == -2) {
            return false;
        }
        if (this.fHasOptionalContent && n2 == 0) {
            return true;
        }
        int n3 = 0;
        while (n3 < this.fNumElements) {
            if (!this.fIsOptionalElement[n3] && arrn[n3 + 1] == 0) {
                return false;
            }
            ++n3;
        }
        return true;
    }

    public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler substitutionGroupHandler) throws XMLSchemaException {
        int n2 = 0;
        while (n2 < this.fNumElements) {
            int n3 = n2 + 1;
            while (n3 < this.fNumElements) {
                if (XSConstraints.overlapUPA(this.fAllElements[n2], this.fAllElements[n3], substitutionGroupHandler)) {
                    throw new XMLSchemaException("cos-nonambig", new Object[]{this.fAllElements[n2].toString(), this.fAllElements[n3].toString()});
                }
                ++n3;
            }
            ++n2;
        }
        return false;
    }

    public Vector whatCanGoHere(int[] arrn) {
        Vector<XSElementDecl> vector = new Vector<XSElementDecl>();
        int n2 = 0;
        while (n2 < this.fNumElements) {
            if (arrn[n2 + 1] == 0) {
                vector.addElement(this.fAllElements[n2]);
            }
            ++n2;
        }
        return vector;
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

