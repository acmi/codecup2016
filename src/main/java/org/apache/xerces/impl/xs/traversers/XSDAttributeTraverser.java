/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAbstractTraverser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDSimpleTypeTraverser;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

class XSDAttributeTraverser
extends XSDAbstractTraverser {
    public XSDAttributeTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    protected XSAttributeUseImpl traverseLocal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, XSComplexTypeDecl xSComplexTypeDecl) {
        Object object;
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        String string = (String)arrobject[XSAttributeChecker.ATTIDX_DEFAULT];
        String string2 = (String)arrobject[XSAttributeChecker.ATTIDX_FIXED];
        String string3 = (String)arrobject[XSAttributeChecker.ATTIDX_NAME];
        QName qName = (QName)arrobject[XSAttributeChecker.ATTIDX_REF];
        XInt xInt = (XInt)arrobject[XSAttributeChecker.ATTIDX_USE];
        XSAttributeDecl xSAttributeDecl = null;
        XSAnnotationImpl xSAnnotationImpl = null;
        if (element.getAttributeNode(SchemaSymbols.ATT_REF) != null) {
            if (qName != null) {
                xSAttributeDecl = (XSAttributeDecl)this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 1, qName, element);
                Element element2 = DOMUtil.getFirstChildElement(element);
                if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    xSAnnotationImpl = this.traverseAnnotationDecl(element2, arrobject, false, xSDocumentInfo);
                    element2 = DOMUtil.getNextSiblingElement(element2);
                } else {
                    object = DOMUtil.getSyntheticAnnotation(element);
                    if (object != null) {
                        xSAnnotationImpl = this.traverseSyntheticAnnotation(element, (String)object, arrobject, false, xSDocumentInfo);
                    }
                }
                if (element2 != null) {
                    this.reportSchemaError("src-attribute.3.2", new Object[]{qName.rawname}, element2);
                }
                string3 = qName.localpart;
            } else {
                xSAttributeDecl = null;
            }
        } else {
            xSAttributeDecl = this.traverseNamedAttr(element, arrobject, xSDocumentInfo, schemaGrammar, false, xSComplexTypeDecl);
        }
        int n2 = 0;
        if (string != null) {
            n2 = 1;
        } else if (string2 != null) {
            n2 = 2;
            string = string2;
            string2 = null;
        }
        object = null;
        if (xSAttributeDecl != null) {
            object = this.fSchemaHandler.fDeclPool != null ? this.fSchemaHandler.fDeclPool.getAttributeUse() : new XSAttributeUseImpl();
            object.fAttrDecl = xSAttributeDecl;
            object.fUse = xInt.shortValue();
            object.fConstraintType = n2;
            if (string != null) {
                object.fDefault = new ValidatedInfo();
                object.fDefault.normalizedValue = string;
            }
            if (element.getAttributeNode(SchemaSymbols.ATT_REF) == null) {
                object.fAnnotations = xSAttributeDecl.getAnnotations();
            } else {
                XSObjectListImpl xSObjectListImpl;
                if (xSAnnotationImpl != null) {
                    xSObjectListImpl = new XSObjectListImpl();
                    xSObjectListImpl.addXSObject(xSAnnotationImpl);
                } else {
                    xSObjectListImpl = XSObjectListImpl.EMPTY_LIST;
                }
                object.fAnnotations = xSObjectListImpl;
            }
        }
        if (string != null && string2 != null) {
            this.reportSchemaError("src-attribute.1", new Object[]{string3}, element);
        }
        if (n2 == 1 && xInt != null && xInt.intValue() != 0) {
            this.reportSchemaError("src-attribute.2", new Object[]{string3}, element);
            object.fUse = 0;
        }
        if (string != null && object != null) {
            this.fValidationState.setNamespaceSupport(xSDocumentInfo.fNamespaceSupport);
            try {
                this.checkDefaultValid((XSAttributeUseImpl)object);
            }
            catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                this.reportSchemaError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs(), element);
                this.reportSchemaError("a-props-correct.2", new Object[]{string3, string}, element);
                object.fDefault = null;
                object.fConstraintType = 0;
            }
            if (((XSSimpleType)xSAttributeDecl.getTypeDefinition()).isIDType()) {
                this.reportSchemaError("a-props-correct.3", new Object[]{string3}, element);
                object.fDefault = null;
                object.fConstraintType = 0;
            }
            if (!(object.fAttrDecl.getConstraintType() != 2 || object.fConstraintType == 0 || object.fConstraintType == 2 && object.fAttrDecl.getValInfo().actualValue.equals(object.fDefault.actualValue))) {
                this.reportSchemaError("au-props-correct.2", new Object[]{string3, object.fAttrDecl.getValInfo().stringValue()}, element);
                object.fDefault = object.fAttrDecl.getValInfo();
                object.fConstraintType = 2;
            }
        }
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return object;
    }

    protected XSAttributeDecl traverseGlobal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, true, xSDocumentInfo);
        XSAttributeDecl xSAttributeDecl = this.traverseNamedAttr(element, arrobject, xSDocumentInfo, schemaGrammar, true, null);
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return xSAttributeDecl;
    }

    XSAttributeDecl traverseNamedAttr(Element element, Object[] arrobject, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, boolean bl, XSComplexTypeDecl xSComplexTypeDecl) {
        Object object;
        Object object2;
        String string = (String)arrobject[XSAttributeChecker.ATTIDX_DEFAULT];
        String string2 = (String)arrobject[XSAttributeChecker.ATTIDX_FIXED];
        XInt xInt = (XInt)arrobject[XSAttributeChecker.ATTIDX_FORM];
        String string3 = (String)arrobject[XSAttributeChecker.ATTIDX_NAME];
        QName qName = (QName)arrobject[XSAttributeChecker.ATTIDX_TYPE];
        XSAttributeDecl xSAttributeDecl = null;
        xSAttributeDecl = this.fSchemaHandler.fDeclPool != null ? this.fSchemaHandler.fDeclPool.getAttributeDecl() : new XSAttributeDecl();
        if (string3 != null) {
            string3 = this.fSymbolTable.addSymbol(string3);
        }
        String string4 = null;
        XSComplexTypeDecl xSComplexTypeDecl2 = null;
        short s2 = 0;
        if (bl) {
            string4 = xSDocumentInfo.fTargetNamespace;
            s2 = 1;
        } else {
            if (xSComplexTypeDecl != null) {
                xSComplexTypeDecl2 = xSComplexTypeDecl;
                s2 = 2;
            }
            if (xInt != null) {
                if (xInt.intValue() == 1) {
                    string4 = xSDocumentInfo.fTargetNamespace;
                }
            } else if (xSDocumentInfo.fAreLocalAttributesQualified) {
                string4 = xSDocumentInfo.fTargetNamespace;
            }
        }
        ValidatedInfo validatedInfo = null;
        short s3 = 0;
        if (bl) {
            if (string2 != null) {
                validatedInfo = new ValidatedInfo();
                validatedInfo.normalizedValue = string2;
                s3 = 2;
            } else if (string != null) {
                validatedInfo = new ValidatedInfo();
                validatedInfo.normalizedValue = string;
                s3 = 1;
            }
        }
        Element element2 = DOMUtil.getFirstChildElement(element);
        XSAnnotationImpl xSAnnotationImpl = null;
        if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
            xSAnnotationImpl = this.traverseAnnotationDecl(element2, arrobject, false, xSDocumentInfo);
            element2 = DOMUtil.getNextSiblingElement(element2);
        } else {
            object2 = DOMUtil.getSyntheticAnnotation(element);
            if (object2 != null) {
                xSAnnotationImpl = this.traverseSyntheticAnnotation(element, (String)object2, arrobject, false, xSDocumentInfo);
            }
        }
        object2 = null;
        boolean bl2 = false;
        if (element2 != null && (object = DOMUtil.getLocalName(element2)).equals(SchemaSymbols.ELT_SIMPLETYPE)) {
            object2 = this.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar);
            bl2 = true;
            element2 = DOMUtil.getNextSiblingElement(element2);
        }
        if (object2 == null && qName != null) {
            object = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 7, qName, element);
            if (object != null && object.getTypeCategory() == 16) {
                object2 = (XSSimpleType)object;
            } else {
                this.reportSchemaError("src-resolve", new Object[]{qName.rawname, "simpleType definition"}, element);
                if (object == null) {
                    xSAttributeDecl.fUnresolvedTypeName = qName;
                }
            }
        }
        if (object2 == null) {
            object2 = SchemaGrammar.fAnySimpleType;
        }
        if (xSAnnotationImpl != null) {
            object = new XSObjectListImpl();
            ((XSObjectListImpl)object).addXSObject(xSAnnotationImpl);
        } else {
            object = XSObjectListImpl.EMPTY_LIST;
        }
        xSAttributeDecl.setValues(string3, string4, (XSSimpleType)object2, s3, s2, validatedInfo, xSComplexTypeDecl2, (XSObjectList)object);
        if (string3 == null) {
            if (bl) {
                this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_ATTRIBUTE, SchemaSymbols.ATT_NAME}, element);
            } else {
                this.reportSchemaError("src-attribute.3.1", null, element);
            }
            string3 = "(no name)";
        }
        if (element2 != null) {
            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{string3, "(annotation?, (simpleType?))", DOMUtil.getLocalName(element2)}, element2);
        }
        if (string != null && string2 != null) {
            this.reportSchemaError("src-attribute.1", new Object[]{string3}, element);
        }
        if (bl2 && qName != null) {
            this.reportSchemaError("src-attribute.4", new Object[]{string3}, element);
        }
        this.checkNotationType(string3, (XSTypeDefinition)object2, element);
        if (validatedInfo != null) {
            this.fValidationState.setNamespaceSupport(xSDocumentInfo.fNamespaceSupport);
            try {
                this.checkDefaultValid(xSAttributeDecl);
            }
            catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                this.reportSchemaError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs(), element);
                this.reportSchemaError("a-props-correct.2", new Object[]{string3, validatedInfo.normalizedValue}, element);
                validatedInfo = null;
                s3 = 0;
                xSAttributeDecl.setValues(string3, string4, (XSSimpleType)object2, s3, s2, validatedInfo, xSComplexTypeDecl2, (XSObjectList)object);
            }
        }
        if (validatedInfo != null && object2.isIDType()) {
            this.reportSchemaError("a-props-correct.3", new Object[]{string3}, element);
            validatedInfo = null;
            s3 = 0;
            xSAttributeDecl.setValues(string3, string4, (XSSimpleType)object2, s3, s2, validatedInfo, xSComplexTypeDecl2, (XSObjectList)object);
        }
        if (string3 != null && string3.equals(XMLSymbols.PREFIX_XMLNS)) {
            this.reportSchemaError("no-xmlns", null, element);
            return null;
        }
        if (string4 != null && string4.equals(SchemaSymbols.URI_XSI)) {
            this.reportSchemaError("no-xsi", new Object[]{SchemaSymbols.URI_XSI}, element);
            return null;
        }
        if (string3.equals("(no name)")) {
            return null;
        }
        if (bl) {
            XSAttributeDecl xSAttributeDecl2;
            String string5;
            if (schemaGrammar.getGlobalAttributeDecl(string3) == null) {
                schemaGrammar.addGlobalAttributeDecl(xSAttributeDecl);
            }
            if ((xSAttributeDecl2 = schemaGrammar.getGlobalAttributeDecl(string3, string5 = this.fSchemaHandler.schemaDocument2SystemId(xSDocumentInfo))) == null) {
                schemaGrammar.addGlobalAttributeDecl(xSAttributeDecl, string5);
            }
            if (this.fSchemaHandler.fTolerateDuplicates) {
                if (xSAttributeDecl2 != null) {
                    xSAttributeDecl = xSAttributeDecl2;
                }
                this.fSchemaHandler.addGlobalAttributeDecl(xSAttributeDecl);
            }
        }
        return xSAttributeDecl;
    }

    void checkDefaultValid(XSAttributeDecl xSAttributeDecl) throws InvalidDatatypeValueException {
        ((XSSimpleType)xSAttributeDecl.getTypeDefinition()).validate(xSAttributeDecl.getValInfo().normalizedValue, (ValidationContext)this.fValidationState, xSAttributeDecl.getValInfo());
        ((XSSimpleType)xSAttributeDecl.getTypeDefinition()).validate(xSAttributeDecl.getValInfo().stringValue(), (ValidationContext)this.fValidationState, xSAttributeDecl.getValInfo());
    }

    void checkDefaultValid(XSAttributeUseImpl xSAttributeUseImpl) throws InvalidDatatypeValueException {
        ((XSSimpleType)xSAttributeUseImpl.fAttrDecl.getTypeDefinition()).validate(xSAttributeUseImpl.fDefault.normalizedValue, (ValidationContext)this.fValidationState, xSAttributeUseImpl.fDefault);
        ((XSSimpleType)xSAttributeUseImpl.fAttrDecl.getTypeDefinition()).validate(xSAttributeUseImpl.fDefault.stringValue(), (ValidationContext)this.fValidationState, xSAttributeUseImpl.fDefault);
    }
}

