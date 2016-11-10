/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.xni.QName;

public class SimpleContentModel
implements ContentModelValidator {
    public static final short CHOICE = -1;
    public static final short SEQUENCE = -1;
    private final QName fFirstChild = new QName();
    private final QName fSecondChild = new QName();
    private final int fOperator;

    public SimpleContentModel(short s2, QName qName, QName qName2) {
        this.fFirstChild.setValues(qName);
        if (qName2 != null) {
            this.fSecondChild.setValues(qName2);
        } else {
            this.fSecondChild.clear();
        }
        this.fOperator = s2;
    }

    public int validate(QName[] arrqName, int n2, int n3) {
        switch (this.fOperator) {
            case 0: {
                if (n3 == 0) {
                    return 0;
                }
                if (arrqName[n2].rawname != this.fFirstChild.rawname) {
                    return 0;
                }
                if (n3 <= 1) break;
                return 1;
            }
            case 1: {
                if (n3 == 1 && arrqName[n2].rawname != this.fFirstChild.rawname) {
                    return 0;
                }
                if (n3 <= 1) break;
                return 1;
            }
            case 2: {
                if (n3 <= 0) break;
                int n4 = 0;
                while (n4 < n3) {
                    if (arrqName[n2 + n4].rawname != this.fFirstChild.rawname) {
                        return n4;
                    }
                    ++n4;
                }
                break;
            }
            case 3: {
                if (n3 == 0) {
                    return 0;
                }
                int n5 = 0;
                while (n5 < n3) {
                    if (arrqName[n2 + n5].rawname != this.fFirstChild.rawname) {
                        return n5;
                    }
                    ++n5;
                }
                break;
            }
            case 4: {
                if (n3 == 0) {
                    return 0;
                }
                if (arrqName[n2].rawname != this.fFirstChild.rawname && arrqName[n2].rawname != this.fSecondChild.rawname) {
                    return 0;
                }
                if (n3 <= 1) break;
                return 1;
            }
            case 5: {
                if (n3 == 2) {
                    if (arrqName[n2].rawname != this.fFirstChild.rawname) {
                        return 0;
                    }
                    if (arrqName[n2 + 1].rawname == this.fSecondChild.rawname) break;
                    return 1;
                }
                if (n3 > 2) {
                    return 2;
                }
                return n3;
            }
            default: {
                throw new RuntimeException("ImplementationMessages.VAL_CST");
            }
        }
        return -1;
    }
}

