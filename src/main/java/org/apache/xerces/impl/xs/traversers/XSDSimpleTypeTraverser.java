/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import java.util.ArrayList;
import java.util.Vector;
import org.apache.xerces.impl.dv.InvalidDatatypeFacetException;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAbstractTraverser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;

class XSDSimpleTypeTraverser
extends XSDAbstractTraverser {
    private boolean fIsBuiltIn = false;

    XSDSimpleTypeTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    XSSimpleType traverseGlobal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, true, xSDocumentInfo);
        String string = (String)arrobject[XSAttributeChecker.ATTIDX_NAME];
        if (string == null) {
            arrobject[XSAttributeChecker.ATTIDX_NAME] = "(no name)";
        }
        XSSimpleType xSSimpleType = this.traverseSimpleTypeDecl(element, arrobject, xSDocumentInfo, schemaGrammar);
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        if (string == null) {
            this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, SchemaSymbols.ATT_NAME}, element);
            xSSimpleType = null;
        }
        if (xSSimpleType != null) {
            if (schemaGrammar.getGlobalTypeDecl(xSSimpleType.getName()) == null) {
                schemaGrammar.addGlobalSimpleTypeDecl(xSSimpleType);
            }
            String string2 = this.fSchemaHandler.schemaDocument2SystemId(xSDocumentInfo);
            XSTypeDefinition xSTypeDefinition = schemaGrammar.getGlobalTypeDecl(xSSimpleType.getName(), string2);
            if (xSTypeDefinition == null) {
                schemaGrammar.addGlobalSimpleTypeDecl(xSSimpleType, string2);
            }
            if (this.fSchemaHandler.fTolerateDuplicates) {
                if (xSTypeDefinition != null && xSTypeDefinition instanceof XSSimpleType) {
                    xSSimpleType = (XSSimpleType)xSTypeDefinition;
                }
                this.fSchemaHandler.addGlobalTypeDecl(xSSimpleType);
            }
        }
        return xSSimpleType;
    }

    XSSimpleType traverseLocal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        String string = this.genAnonTypeName(element);
        XSSimpleType xSSimpleType = this.getSimpleType(string, element, arrobject, xSDocumentInfo, schemaGrammar);
        if (xSSimpleType instanceof XSSimpleTypeDecl) {
            ((XSSimpleTypeDecl)xSSimpleType).setAnonymous(true);
        }
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return xSSimpleType;
    }

    private XSSimpleType traverseSimpleTypeDecl(Element element, Object[] arrobject, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        String string = (String)arrobject[XSAttributeChecker.ATTIDX_NAME];
        return this.getSimpleType(string, element, arrobject, xSDocumentInfo, schemaGrammar);
    }

    private String genAnonTypeName(Element element) {
        StringBuffer stringBuffer = new StringBuffer("#AnonType_");
        Element element2 = DOMUtil.getParent(element);
        while (element2 != null && element2 != DOMUtil.getRoot(DOMUtil.getDocument(element2))) {
            stringBuffer.append(element2.getAttribute(SchemaSymbols.ATT_NAME));
            element2 = DOMUtil.getParent(element2);
        }
        return stringBuffer.toString();
    }

    private XSSimpleType getSimpleType(String string, Element element, Object[] arrobject, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object object;
        XSObjectList xSObjectList;
        Object object2;
        Object object3;
        int n2;
        ArrayList<Object> arrayList;
        XInt xInt = (XInt)arrobject[XSAttributeChecker.ATTIDX_FINAL];
        int n3 = xInt == null ? xSDocumentInfo.fFinalDefault : xInt.intValue();
        Element element2 = DOMUtil.getFirstChildElement(element);
        XSObject[] arrxSObject = null;
        if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
            object3 = this.traverseAnnotationDecl(element2, arrobject, false, xSDocumentInfo);
            if (object3 != null) {
                arrxSObject = new XSAnnotationImpl[]{object3};
            }
            element2 = DOMUtil.getNextSiblingElement(element2);
        } else {
            object3 = DOMUtil.getSyntheticAnnotation(element);
            if (object3 != null) {
                XSAnnotationImpl xSAnnotationImpl = this.traverseSyntheticAnnotation(element, (String)object3, arrobject, false, xSDocumentInfo);
                arrxSObject = new XSAnnotationImpl[]{xSAnnotationImpl};
            }
        }
        if (element2 == null) {
            this.reportSchemaError("s4s-elt-must-match.2", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))"}, element);
            return this.errorType(string, xSDocumentInfo.fTargetNamespace, 2);
        }
        object3 = DOMUtil.getLocalName(element2);
        short s2 = 2;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        if (object3.equals(SchemaSymbols.ELT_RESTRICTION)) {
            s2 = 2;
            bl = true;
        } else if (object3.equals(SchemaSymbols.ELT_LIST)) {
            s2 = 16;
            bl2 = true;
        } else if (object3.equals(SchemaSymbols.ELT_UNION)) {
            s2 = 8;
            bl3 = true;
        } else {
            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", object3}, element);
            return this.errorType(string, xSDocumentInfo.fTargetNamespace, 2);
        }
        Element element3 = DOMUtil.getNextSiblingElement(element2);
        if (element3 != null) {
            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", DOMUtil.getLocalName(element3)}, element3);
        }
        Object[] arrobject2 = this.fAttrChecker.checkAttributes(element2, false, xSDocumentInfo);
        QName qName = (QName)arrobject2[bl ? XSAttributeChecker.ATTIDX_BASE : XSAttributeChecker.ATTIDX_ITEMTYPE];
        Vector vector = (Vector)arrobject2[XSAttributeChecker.ATTIDX_MEMBERTYPES];
        Element element4 = DOMUtil.getFirstChildElement(element2);
        if (element4 != null && DOMUtil.getLocalName(element4).equals(SchemaSymbols.ELT_ANNOTATION)) {
            object2 = this.traverseAnnotationDecl(element4, arrobject2, false, xSDocumentInfo);
            if (object2 != null) {
                if (arrxSObject == null) {
                    arrxSObject = new XSAnnotationImpl[]{object2};
                } else {
                    arrayList = new XSAnnotationImpl[2];
                    arrayList[0] = arrxSObject[0];
                    arrxSObject = arrayList;
                    arrxSObject[1] = object2;
                }
            }
            element4 = DOMUtil.getNextSiblingElement(element4);
        } else {
            object2 = DOMUtil.getSyntheticAnnotation(element2);
            if (object2 != null) {
                arrayList = this.traverseSyntheticAnnotation(element2, (String)object2, arrobject2, false, xSDocumentInfo);
                if (arrxSObject == null) {
                    arrxSObject = new XSAnnotationImpl[]{arrayList};
                } else {
                    object = new XSAnnotationImpl[2];
                    object[0] = arrxSObject[0];
                    arrxSObject = object;
                    arrxSObject[1] = arrayList;
                }
            }
        }
        object2 = null;
        if ((bl || bl2) && qName != null && (object2 = this.findDTValidator(element2, string, qName, s2, xSDocumentInfo)) == null && this.fIsBuiltIn) {
            this.fIsBuiltIn = false;
            return null;
        }
        arrayList = null;
        object = null;
        if (bl3 && vector != null && vector.size() > 0) {
            n2 = vector.size();
            arrayList = new ArrayList(n2);
            int n4 = 0;
            while (n4 < n2) {
                object = this.findDTValidator(element2, string, (QName)vector.elementAt(n4), 8, xSDocumentInfo);
                if (object != null) {
                    if (object.getVariety() == 3) {
                        xSObjectList = object.getMemberTypes();
                        int n5 = 0;
                        while (n5 < xSObjectList.getLength()) {
                            arrayList.add(xSObjectList.item(n5));
                            ++n5;
                        }
                    } else {
                        arrayList.add(object);
                    }
                }
                ++n4;
            }
        }
        if (element4 != null && DOMUtil.getLocalName(element4).equals(SchemaSymbols.ELT_SIMPLETYPE)) {
            if (bl || bl2) {
                if (qName != null) {
                    this.reportSchemaError(bl2 ? "src-simple-type.3.a" : "src-simple-type.2.a", null, element4);
                }
                if (object2 == null) {
                    object2 = this.traverseLocal(element4, xSDocumentInfo, schemaGrammar);
                }
                element4 = DOMUtil.getNextSiblingElement(element4);
            } else if (bl3) {
                if (arrayList == null) {
                    arrayList = new ArrayList<Object>(2);
                }
                do {
                    if ((object = this.traverseLocal(element4, xSDocumentInfo, schemaGrammar)) == null) continue;
                    if (object.getVariety() == 3) {
                        xSObjectList = object.getMemberTypes();
                        n2 = 0;
                        while (n2 < xSObjectList.getLength()) {
                            arrayList.add(xSObjectList.item(n2));
                            ++n2;
                        }
                    } else {
                        arrayList.add(object);
                    }
                } while ((element4 = DOMUtil.getNextSiblingElement(element4)) != null && DOMUtil.getLocalName(element4).equals(SchemaSymbols.ELT_SIMPLETYPE));
            }
        } else if ((bl || bl2) && qName == null) {
            this.reportSchemaError(bl2 ? "src-simple-type.3.b" : "src-simple-type.2.b", null, element2);
        } else if (bl3 && (vector == null || vector.size() == 0)) {
            this.reportSchemaError("src-union-memberTypes-or-simpleTypes", null, element2);
        }
        if ((bl || bl2) && object2 == null) {
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            return this.errorType(string, xSDocumentInfo.fTargetNamespace, bl ? 2 : 16);
        }
        if (bl3 && (arrayList == null || arrayList.size() == 0)) {
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            return this.errorType(string, xSDocumentInfo.fTargetNamespace, 8);
        }
        if (bl2 && this.isListDatatype((XSSimpleType)object2)) {
            this.reportSchemaError("cos-st-restricts.2.1", new Object[]{string, object2.getName()}, element2);
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            return this.errorType(string, xSDocumentInfo.fTargetNamespace, 16);
        }
        XSSimpleType xSSimpleType = null;
        if (bl) {
            xSSimpleType = this.fSchemaHandler.fDVFactory.createTypeRestriction(string, xSDocumentInfo.fTargetNamespace, (short)n3, (XSSimpleType)object2, arrxSObject == null ? null : new XSObjectListImpl(arrxSObject, arrxSObject.length));
        } else if (bl2) {
            xSSimpleType = this.fSchemaHandler.fDVFactory.createTypeList(string, xSDocumentInfo.fTargetNamespace, (short)n3, (XSSimpleType)object2, arrxSObject == null ? null : new XSObjectListImpl(arrxSObject, arrxSObject.length));
        } else if (bl3) {
            XSSimpleType[] arrxSSimpleType = arrayList.toArray(new XSSimpleType[arrayList.size()]);
            xSSimpleType = this.fSchemaHandler.fDVFactory.createTypeUnion(string, xSDocumentInfo.fTargetNamespace, (short)n3, arrxSSimpleType, arrxSObject == null ? null : new XSObjectListImpl(arrxSObject, arrxSObject.length));
        }
        if (bl && element4 != null) {
            XSDAbstractTraverser.FacetInfo facetInfo = this.traverseFacets(element4, (XSSimpleType)object2, xSDocumentInfo);
            element4 = facetInfo.nodeAfterFacets;
            try {
                this.fValidationState.setNamespaceSupport(xSDocumentInfo.fNamespaceSupport);
                xSSimpleType.applyFacets(facetInfo.facetdata, facetInfo.fPresentFacets, facetInfo.fFixedFacets, this.fValidationState);
            }
            catch (InvalidDatatypeFacetException invalidDatatypeFacetException) {
                this.reportSchemaError(invalidDatatypeFacetException.getKey(), invalidDatatypeFacetException.getArgs(), element2);
                xSSimpleType = this.fSchemaHandler.fDVFactory.createTypeRestriction(string, xSDocumentInfo.fTargetNamespace, (short)n3, (XSSimpleType)object2, arrxSObject == null ? null : new XSObjectListImpl(arrxSObject, arrxSObject.length));
            }
        }
        if (element4 != null) {
            if (bl) {
                this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_RESTRICTION, "(annotation?, (simpleType?, (minExclusive | minInclusive | maxExclusive | maxInclusive | totalDigits | fractionDigits | length | minLength | maxLength | enumeration | whiteSpace | pattern)*))", DOMUtil.getLocalName(element4)}, element4);
            } else if (bl2) {
                this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_LIST, "(annotation?, (simpleType?))", DOMUtil.getLocalName(element4)}, element4);
            } else if (bl3) {
                this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_UNION, "(annotation?, (simpleType*))", DOMUtil.getLocalName(element4)}, element4);
            }
        }
        this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
        return xSSimpleType;
    }

    private XSSimpleType findDTValidator(Element element, String string, QName qName, short s2, XSDocumentInfo xSDocumentInfo) {
        if (qName == null) {
            return null;
        }
        XSTypeDefinition xSTypeDefinition = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 7, qName, element);
        if (xSTypeDefinition == null) {
            return null;
        }
        if (xSTypeDefinition.getTypeCategory() != 16) {
            this.reportSchemaError("cos-st-restricts.1.1", new Object[]{qName.rawname, string}, element);
            return null;
        }
        if (xSTypeDefinition == SchemaGrammar.fAnySimpleType && s2 == 2) {
            if (this.checkBuiltIn(string, xSDocumentInfo.fTargetNamespace)) {
                return null;
            }
            this.reportSchemaError("cos-st-restricts.1.1", new Object[]{qName.rawname, string}, element);
            return null;
        }
        if ((xSTypeDefinition.getFinal() & s2) != 0) {
            if (s2 == 2) {
                this.reportSchemaError("st-props-correct.3", new Object[]{string, qName.rawname}, element);
            } else if (s2 == 16) {
                this.reportSchemaError("cos-st-restricts.2.3.1.1", new Object[]{qName.rawname, string}, element);
            } else if (s2 == 8) {
                this.reportSchemaError("cos-st-restricts.3.3.1.1", new Object[]{qName.rawname, string}, element);
            }
            return null;
        }
        return (XSSimpleType)xSTypeDefinition;
    }

    private final boolean checkBuiltIn(String string, String string2) {
        if (string2 != SchemaSymbols.URI_SCHEMAFORSCHEMA) {
            return false;
        }
        if (SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(string) != null) {
            this.fIsBuiltIn = true;
        }
        return this.fIsBuiltIn;
    }

    private boolean isListDatatype(XSSimpleType xSSimpleType) {
        if (xSSimpleType.getVariety() == 2) {
            return true;
        }
        if (xSSimpleType.getVariety() == 3) {
            XSObjectList xSObjectList = xSSimpleType.getMemberTypes();
            int n2 = 0;
            while (n2 < xSObjectList.getLength()) {
                if (((XSSimpleType)xSObjectList.item(n2)).getVariety() == 2) {
                    return true;
                }
                ++n2;
            }
        }
        return false;
    }

    private XSSimpleType errorType(String string, String string2, short s2) {
        XSSimpleType xSSimpleType = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getTypeDefinition("string");
        switch (s2) {
            case 2: {
                return this.fSchemaHandler.fDVFactory.createTypeRestriction(string, string2, 0, xSSimpleType, null);
            }
            case 16: {
                return this.fSchemaHandler.fDVFactory.createTypeList(string, string2, 0, xSSimpleType, null);
            }
            case 8: {
                return this.fSchemaHandler.fDVFactory.createTypeUnion(string, string2, 0, new XSSimpleType[]{xSSimpleType}, null);
            }
        }
        return null;
    }
}

