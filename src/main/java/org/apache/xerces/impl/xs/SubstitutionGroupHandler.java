/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class SubstitutionGroupHandler {
    private static final XSElementDecl[] EMPTY_GROUP = new XSElementDecl[0];
    XSGrammarBucket fGrammarBucket;
    Hashtable fSubGroupsB = new Hashtable();
    private static final OneSubGroup[] EMPTY_VECTOR = new OneSubGroup[0];
    Hashtable fSubGroups = new Hashtable();

    public SubstitutionGroupHandler(XSGrammarBucket xSGrammarBucket) {
        this.fGrammarBucket = xSGrammarBucket;
    }

    public XSElementDecl getMatchingElemDecl(QName qName, XSElementDecl xSElementDecl) {
        if (qName.localpart == xSElementDecl.fName && qName.uri == xSElementDecl.fTargetNamespace) {
            return xSElementDecl;
        }
        if (xSElementDecl.fScope != 1) {
            return null;
        }
        if ((xSElementDecl.fBlock & 4) != 0) {
            return null;
        }
        SchemaGrammar schemaGrammar = this.fGrammarBucket.getGrammar(qName.uri);
        if (schemaGrammar == null) {
            return null;
        }
        XSElementDecl xSElementDecl2 = schemaGrammar.getGlobalElementDecl(qName.localpart);
        if (xSElementDecl2 == null) {
            return null;
        }
        if (this.substitutionGroupOK(xSElementDecl2, xSElementDecl, xSElementDecl.fBlock)) {
            return xSElementDecl2;
        }
        return null;
    }

    protected boolean substitutionGroupOK(XSElementDecl xSElementDecl, XSElementDecl xSElementDecl2, short s2) {
        if (xSElementDecl == xSElementDecl2) {
            return true;
        }
        if ((s2 & 4) != 0) {
            return false;
        }
        XSElementDecl xSElementDecl3 = xSElementDecl.fSubGroup;
        while (xSElementDecl3 != null && xSElementDecl3 != xSElementDecl2) {
            xSElementDecl3 = xSElementDecl3.fSubGroup;
        }
        if (xSElementDecl3 == null) {
            return false;
        }
        return this.typeDerivationOK(xSElementDecl.fType, xSElementDecl2.fType, s2);
    }

    private boolean typeDerivationOK(XSTypeDefinition xSTypeDefinition, XSTypeDefinition xSTypeDefinition2, short s2) {
        short s3 = 0;
        short s4 = s2;
        XSTypeDefinition xSTypeDefinition3 = xSTypeDefinition;
        while (xSTypeDefinition3 != xSTypeDefinition2 && xSTypeDefinition3 != SchemaGrammar.fAnyType) {
            s3 = xSTypeDefinition3.getTypeCategory() == 15 ? (short)(s3 | ((XSComplexTypeDecl)xSTypeDefinition3).fDerivedBy) : (short)(s3 | 2);
            if ((xSTypeDefinition3 = xSTypeDefinition3.getBaseType()) == null) {
                xSTypeDefinition3 = SchemaGrammar.fAnyType;
            }
            if (xSTypeDefinition3.getTypeCategory() != 15) continue;
            s4 = (short)(s4 | ((XSComplexTypeDecl)xSTypeDefinition3).fBlock);
        }
        if (xSTypeDefinition3 != xSTypeDefinition2) {
            XSSimpleTypeDefinition xSSimpleTypeDefinition;
            if (xSTypeDefinition2.getTypeCategory() == 16 && (xSSimpleTypeDefinition = (XSSimpleTypeDefinition)xSTypeDefinition2).getVariety() == 3) {
                XSObjectList xSObjectList = xSSimpleTypeDefinition.getMemberTypes();
                int n2 = xSObjectList.getLength();
                int n3 = 0;
                while (n3 < n2) {
                    if (this.typeDerivationOK(xSTypeDefinition, (XSTypeDefinition)xSObjectList.item(n3), s2)) {
                        return true;
                    }
                    ++n3;
                }
            }
            return false;
        }
        if ((s3 & s4) != 0) {
            return false;
        }
        return true;
    }

    public boolean inSubstitutionGroup(XSElementDecl xSElementDecl, XSElementDecl xSElementDecl2) {
        return this.substitutionGroupOK(xSElementDecl, xSElementDecl2, xSElementDecl2.fBlock);
    }

    public void reset() {
        this.fSubGroupsB.clear();
        this.fSubGroups.clear();
    }

    public void addSubstitutionGroup(XSElementDecl[] arrxSElementDecl) {
        int n2 = arrxSElementDecl.length - 1;
        while (n2 >= 0) {
            XSElementDecl xSElementDecl = arrxSElementDecl[n2];
            XSElementDecl xSElementDecl2 = xSElementDecl.fSubGroup;
            Vector<XSElementDecl> vector = (Vector<XSElementDecl>)this.fSubGroupsB.get(xSElementDecl2);
            if (vector == null) {
                vector = new Vector<XSElementDecl>();
                this.fSubGroupsB.put(xSElementDecl2, vector);
            }
            vector.addElement(xSElementDecl);
            --n2;
        }
    }

    public XSElementDecl[] getSubstitutionGroup(XSElementDecl xSElementDecl) {
        Object v2 = this.fSubGroups.get(xSElementDecl);
        if (v2 != null) {
            return (XSElementDecl[])v2;
        }
        if ((xSElementDecl.fBlock & 4) != 0) {
            this.fSubGroups.put(xSElementDecl, EMPTY_GROUP);
            return EMPTY_GROUP;
        }
        OneSubGroup[] arroneSubGroup = this.getSubGroupB(xSElementDecl, new OneSubGroup());
        int n2 = arroneSubGroup.length;
        int n3 = 0;
        XSElementDecl[] arrxSElementDecl = new XSElementDecl[n2];
        int n4 = 0;
        while (n4 < n2) {
            if ((xSElementDecl.fBlock & arroneSubGroup[n4].dMethod) == 0) {
                arrxSElementDecl[n3++] = arroneSubGroup[n4].sub;
            }
            ++n4;
        }
        if (n3 < n2) {
            XSElementDecl[] arrxSElementDecl2 = new XSElementDecl[n3];
            System.arraycopy(arrxSElementDecl, 0, arrxSElementDecl2, 0, n3);
            arrxSElementDecl = arrxSElementDecl2;
        }
        this.fSubGroups.put(xSElementDecl, arrxSElementDecl);
        return arrxSElementDecl;
    }

    private OneSubGroup[] getSubGroupB(XSElementDecl xSElementDecl, OneSubGroup oneSubGroup) {
        Object object;
        Object v2 = this.fSubGroupsB.get(xSElementDecl);
        if (v2 == null) {
            this.fSubGroupsB.put(xSElementDecl, EMPTY_VECTOR);
            return EMPTY_VECTOR;
        }
        if (v2 instanceof OneSubGroup[]) {
            return (OneSubGroup[])v2;
        }
        Vector vector = (Vector)v2;
        Vector<OneSubGroup> vector2 = new Vector<OneSubGroup>();
        int n2 = vector.size() - 1;
        while (n2 >= 0) {
            object = (XSElementDecl)vector.elementAt(n2);
            if (this.getDBMethods(object.fType, xSElementDecl.fType, oneSubGroup)) {
                short s2 = oneSubGroup.dMethod;
                short s3 = oneSubGroup.bMethod;
                vector2.addElement(new OneSubGroup((XSElementDecl)object, oneSubGroup.dMethod, oneSubGroup.bMethod));
                OneSubGroup[] arroneSubGroup = this.getSubGroupB((XSElementDecl)object, oneSubGroup);
                int n3 = arroneSubGroup.length - 1;
                while (n3 >= 0) {
                    short s4 = (short)(s2 | arroneSubGroup[n3].dMethod);
                    short s5 = (short)(s3 | arroneSubGroup[n3].bMethod);
                    if ((s4 & s5) == 0) {
                        vector2.addElement(new OneSubGroup(arroneSubGroup[n3].sub, s4, s5));
                    }
                    --n3;
                }
            }
            --n2;
        }
        object = new OneSubGroup[vector2.size()];
        int n4 = vector2.size() - 1;
        while (n4 >= 0) {
            object[n4] = (OneSubGroup)vector2.elementAt(n4);
            --n4;
        }
        this.fSubGroupsB.put(xSElementDecl, object);
        return object;
    }

    private boolean getDBMethods(XSTypeDefinition xSTypeDefinition, XSTypeDefinition xSTypeDefinition2, OneSubGroup oneSubGroup) {
        int n2 = 0;
        short s2 = 0;
        while (xSTypeDefinition != xSTypeDefinition2 && xSTypeDefinition != SchemaGrammar.fAnyType) {
            n2 = xSTypeDefinition.getTypeCategory() == 15 ? (int)((short)(n2 | ((XSComplexTypeDecl)xSTypeDefinition).fDerivedBy)) : (int)((short)(n2 | 2));
            if ((xSTypeDefinition = xSTypeDefinition.getBaseType()) == null) {
                xSTypeDefinition = SchemaGrammar.fAnyType;
            }
            if (xSTypeDefinition.getTypeCategory() != 15) continue;
            s2 = (short)(s2 | ((XSComplexTypeDecl)xSTypeDefinition).fBlock);
        }
        if (xSTypeDefinition != xSTypeDefinition2 || n2 & s2) {
            return false;
        }
        oneSubGroup.dMethod = n2;
        oneSubGroup.bMethod = s2;
        return true;
    }

    private static final class OneSubGroup {
        XSElementDecl sub;
        short dMethod;
        short bMethod;

        OneSubGroup() {
        }

        OneSubGroup(XSElementDecl xSElementDecl, short s2, short s3) {
            this.sub = xSElementDecl;
            this.dMethod = s2;
            this.bMethod = s3;
        }
    }

}

