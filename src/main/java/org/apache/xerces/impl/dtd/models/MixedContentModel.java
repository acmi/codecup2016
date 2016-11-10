/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.xni.QName;

public class MixedContentModel
implements ContentModelValidator {
    private final int fCount;
    private final QName[] fChildren;
    private final int[] fChildrenType;
    private final boolean fOrdered;

    public MixedContentModel(QName[] arrqName, int[] arrn, int n2, int n3, boolean bl) {
        this.fCount = n3;
        this.fChildren = new QName[this.fCount];
        this.fChildrenType = new int[this.fCount];
        int n4 = 0;
        while (n4 < this.fCount) {
            this.fChildren[n4] = new QName(arrqName[n2 + n4]);
            this.fChildrenType[n4] = arrn[n2 + n4];
            ++n4;
        }
        this.fOrdered = bl;
    }

    public int validate(QName[] arrqName, int n2, int n3) {
        if (this.fOrdered) {
            int n4 = 0;
            int n5 = 0;
            while (n5 < n3) {
                QName qName = arrqName[n2 + n5];
                if (qName.localpart != null) {
                    String string;
                    int n6 = this.fChildrenType[n4];
                    if (n6 == 0 ? this.fChildren[n4].rawname != arrqName[n2 + n5].rawname : (n6 == 6 ? (string = this.fChildren[n4].uri) != null && string != arrqName[n5].uri : (n6 == 8 ? arrqName[n5].uri != null : n6 == 7 && this.fChildren[n4].uri == arrqName[n5].uri))) {
                        return n5;
                    }
                    ++n4;
                }
                ++n5;
            }
        } else {
            int n7 = 0;
            while (n7 < n3) {
                QName qName = arrqName[n2 + n7];
                if (qName.localpart != null) {
                    int n8 = 0;
                    while (n8 < this.fCount) {
                        String string;
                        int n9 = this.fChildrenType[n8];
                        if (n9 == 0 ? qName.rawname == this.fChildren[n8].rawname : (n9 == 6 ? (string = this.fChildren[n8].uri) == null || string == arrqName[n7].uri : (n9 == 8 ? arrqName[n7].uri == null : n9 == 7 && this.fChildren[n8].uri != arrqName[n7].uri))) break;
                        ++n8;
                    }
                    if (n8 == this.fCount) {
                        return n7;
                    }
                }
                ++n7;
            }
        }
        return -1;
    }
}

