/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAbstractTraverser;
import org.apache.xerces.impl.xs.traversers.XSDComplexTypeTraverser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDSimpleTypeTraverser;
import org.apache.xerces.impl.xs.traversers.XSDUniqueOrKeyTraverser;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class XSDElementTraverser
extends XSDAbstractTraverser {
    protected final XSElementDecl fTempElementDecl = new XSElementDecl();
    boolean fDeferTraversingLocalElements;

    XSDElementTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    XSParticleDecl traverseLocal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int n2, XSObject xSObject) {
        XSParticleDecl xSParticleDecl = null;
        xSParticleDecl = this.fSchemaHandler.fDeclPool != null ? this.fSchemaHandler.fDeclPool.getParticleDecl() : new XSParticleDecl();
        if (this.fDeferTraversingLocalElements) {
            xSParticleDecl.fType = 1;
            Attr attr = element.getAttributeNode(SchemaSymbols.ATT_MINOCCURS);
            if (attr != null) {
                String string = attr.getValue();
                try {
                    int n3 = Integer.parseInt(XMLChar.trim(string));
                    if (n3 >= 0) {
                        xSParticleDecl.fMinOccurs = n3;
                    }
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
            this.fSchemaHandler.fillInLocalElemInfo(element, xSDocumentInfo, n2, xSObject, xSParticleDecl);
        } else {
            this.traverseLocal(xSParticleDecl, element, xSDocumentInfo, schemaGrammar, n2, xSObject, null);
            if (xSParticleDecl.fType == 0) {
                xSParticleDecl = null;
            }
        }
        return xSParticleDecl;
    }

    protected void traverseLocal(XSParticleDecl xSParticleDecl, Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int n2, XSObject xSObject, String[] arrstring) {
        Object object;
        if (arrstring != null) {
            xSDocumentInfo.fNamespaceSupport.setEffectiveContext(arrstring);
        }
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        QName qName = (QName)arrobject[XSAttributeChecker.ATTIDX_REF];
        XInt xInt = (XInt)arrobject[XSAttributeChecker.ATTIDX_MINOCCURS];
        XInt xInt2 = (XInt)arrobject[XSAttributeChecker.ATTIDX_MAXOCCURS];
        XSElementDecl xSElementDecl = null;
        XSAnnotationImpl xSAnnotationImpl = null;
        if (element.getAttributeNode(SchemaSymbols.ATT_REF) != null) {
            if (qName != null) {
                xSElementDecl = (XSElementDecl)this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 3, qName, element);
                object = DOMUtil.getFirstChildElement(element);
                if (object != null && DOMUtil.getLocalName((Node)object).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    xSAnnotationImpl = this.traverseAnnotationDecl((Element)object, arrobject, false, xSDocumentInfo);
                    object = DOMUtil.getNextSiblingElement((Node)object);
                } else {
                    String string = DOMUtil.getSyntheticAnnotation(element);
                    if (string != null) {
                        xSAnnotationImpl = this.traverseSyntheticAnnotation(element, string, arrobject, false, xSDocumentInfo);
                    }
                }
                if (object != null) {
                    this.reportSchemaError("src-element.2.2", new Object[]{qName.rawname, DOMUtil.getLocalName((Node)object)}, (Element)object);
                }
            } else {
                xSElementDecl = null;
            }
        } else {
            xSElementDecl = this.traverseNamedElement(element, arrobject, xSDocumentInfo, schemaGrammar, false, xSObject);
        }
        xSParticleDecl.fMinOccurs = xInt.intValue();
        xSParticleDecl.fMaxOccurs = xInt2.intValue();
        if (xSElementDecl != null) {
            xSParticleDecl.fType = 1;
            xSParticleDecl.fValue = xSElementDecl;
        } else {
            xSParticleDecl.fType = 0;
        }
        if (qName != null) {
            if (xSAnnotationImpl != null) {
                object = new XSObjectListImpl();
                ((XSObjectListImpl)object).addXSObject(xSAnnotationImpl);
            } else {
                object = XSObjectListImpl.EMPTY_LIST;
            }
            xSParticleDecl.fAnnotations = object;
        } else {
            xSParticleDecl.fAnnotations = xSElementDecl != null ? xSElementDecl.fAnnotations : XSObjectListImpl.EMPTY_LIST;
        }
        object = (Long)arrobject[XSAttributeChecker.ATTIDX_FROMDEFAULT];
        this.checkOccurrences(xSParticleDecl, SchemaSymbols.ELT_ELEMENT, (Element)element.getParentNode(), n2, object.longValue());
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
    }

    XSElementDecl traverseGlobal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, true, xSDocumentInfo);
        XSElementDecl xSElementDecl = this.traverseNamedElement(element, arrobject, xSDocumentInfo, schemaGrammar, true, null);
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return xSElementDecl;
    }

    XSElementDecl traverseNamedElement(Element element, Object[] arrobject, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, boolean bl, XSObject xSObject) {
        Object object;
        String string;
        Boolean bl2 = (Boolean)arrobject[XSAttributeChecker.ATTIDX_ABSTRACT];
        XInt xInt = (XInt)arrobject[XSAttributeChecker.ATTIDX_BLOCK];
        String string2 = (String)arrobject[XSAttributeChecker.ATTIDX_DEFAULT];
        XInt xInt2 = (XInt)arrobject[XSAttributeChecker.ATTIDX_FINAL];
        String string3 = (String)arrobject[XSAttributeChecker.ATTIDX_FIXED];
        XInt xInt3 = (XInt)arrobject[XSAttributeChecker.ATTIDX_FORM];
        String string4 = (String)arrobject[XSAttributeChecker.ATTIDX_NAME];
        Boolean bl3 = (Boolean)arrobject[XSAttributeChecker.ATTIDX_NILLABLE];
        QName qName = (QName)arrobject[XSAttributeChecker.ATTIDX_SUBSGROUP];
        QName qName2 = (QName)arrobject[XSAttributeChecker.ATTIDX_TYPE];
        XSElementDecl xSElementDecl = null;
        xSElementDecl = this.fSchemaHandler.fDeclPool != null ? this.fSchemaHandler.fDeclPool.getElementDecl() : new XSElementDecl();
        if (string4 != null) {
            xSElementDecl.fName = this.fSymbolTable.addSymbol(string4);
        }
        if (bl) {
            xSElementDecl.fTargetNamespace = xSDocumentInfo.fTargetNamespace;
            xSElementDecl.setIsGlobal();
        } else {
            if (xSObject instanceof XSComplexTypeDecl) {
                xSElementDecl.setIsLocal((XSComplexTypeDecl)xSObject);
            }
            xSElementDecl.fTargetNamespace = xInt3 != null ? (xInt3.intValue() == 1 ? xSDocumentInfo.fTargetNamespace : null) : (xSDocumentInfo.fAreLocalElementsQualified ? xSDocumentInfo.fTargetNamespace : null);
        }
        xSElementDecl.fBlock = xInt == null ? xSDocumentInfo.fBlockDefault : xInt.shortValue();
        xSElementDecl.fFinal = xInt2 == null ? xSDocumentInfo.fFinalDefault : xInt2.shortValue();
        xSElementDecl.fBlock = (short)(xSElementDecl.fBlock & 7);
        xSElementDecl.fFinal = (short)(xSElementDecl.fFinal & 3);
        if (bl3.booleanValue()) {
            xSElementDecl.setIsNillable();
        }
        if (bl2 != null && bl2.booleanValue()) {
            xSElementDecl.setIsAbstract();
        }
        if (string3 != null) {
            xSElementDecl.fDefault = new ValidatedInfo();
            xSElementDecl.fDefault.normalizedValue = string3;
            xSElementDecl.setConstraintType(2);
        } else if (string2 != null) {
            xSElementDecl.fDefault = new ValidatedInfo();
            xSElementDecl.fDefault.normalizedValue = string2;
            xSElementDecl.setConstraintType(1);
        } else {
            xSElementDecl.setConstraintType(0);
        }
        if (qName != null) {
            xSElementDecl.fSubGroup = (XSElementDecl)this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 3, qName, element);
        }
        Element element2 = DOMUtil.getFirstChildElement(element);
        XSAnnotationImpl xSAnnotationImpl = null;
        if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
            xSAnnotationImpl = this.traverseAnnotationDecl(element2, arrobject, false, xSDocumentInfo);
            element2 = DOMUtil.getNextSiblingElement(element2);
        } else {
            object = DOMUtil.getSyntheticAnnotation(element);
            if (object != null) {
                xSAnnotationImpl = this.traverseSyntheticAnnotation(element, (String)object, arrobject, false, xSDocumentInfo);
            }
        }
        if (xSAnnotationImpl != null) {
            object = new XSObjectListImpl();
            ((XSObjectListImpl)object).addXSObject(xSAnnotationImpl);
        } else {
            object = XSObjectListImpl.EMPTY_LIST;
        }
        xSElementDecl.fAnnotations = object;
        XSTypeDefinition xSTypeDefinition = null;
        boolean bl4 = false;
        if (element2 != null) {
            string = DOMUtil.getLocalName(element2);
            if (string.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                xSTypeDefinition = this.fSchemaHandler.fComplexTypeTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar);
                bl4 = true;
                element2 = DOMUtil.getNextSiblingElement(element2);
            } else if (string.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                xSTypeDefinition = this.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar);
                bl4 = true;
                element2 = DOMUtil.getNextSiblingElement(element2);
            }
        }
        if (xSTypeDefinition == null && qName2 != null && (xSTypeDefinition = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 7, qName2, element)) == null) {
            xSElementDecl.fUnresolvedTypeName = qName2;
        }
        if (xSTypeDefinition == null && xSElementDecl.fSubGroup != null) {
            xSTypeDefinition = xSElementDecl.fSubGroup.fType;
        }
        if (xSTypeDefinition == null) {
            xSTypeDefinition = SchemaGrammar.fAnyType;
        }
        xSElementDecl.fType = xSTypeDefinition;
        if (element2 != null) {
            string = DOMUtil.getLocalName(element2);
            while (element2 != null && (string.equals(SchemaSymbols.ELT_KEY) || string.equals(SchemaSymbols.ELT_KEYREF) || string.equals(SchemaSymbols.ELT_UNIQUE))) {
                if (string.equals(SchemaSymbols.ELT_KEY) || string.equals(SchemaSymbols.ELT_UNIQUE)) {
                    DOMUtil.setHidden(element2, this.fSchemaHandler.fHiddenNodes);
                    this.fSchemaHandler.fUniqueOrKeyTraverser.traverse(element2, xSElementDecl, xSDocumentInfo, schemaGrammar);
                    if (DOMUtil.getAttrValue(element2, SchemaSymbols.ATT_NAME).length() != 0) {
                        this.fSchemaHandler.checkForDuplicateNames(xSDocumentInfo.fTargetNamespace == null ? "," + DOMUtil.getAttrValue(element2, SchemaSymbols.ATT_NAME) : xSDocumentInfo.fTargetNamespace + "," + DOMUtil.getAttrValue(element2, SchemaSymbols.ATT_NAME), 1, this.fSchemaHandler.getIDRegistry(), this.fSchemaHandler.getIDRegistry_sub(), element2, xSDocumentInfo);
                    }
                } else if (string.equals(SchemaSymbols.ELT_KEYREF)) {
                    this.fSchemaHandler.storeKeyRef(element2, xSDocumentInfo, xSElementDecl);
                }
                if ((element2 = DOMUtil.getNextSiblingElement(element2)) == null) continue;
                string = DOMUtil.getLocalName(element2);
            }
        }
        if (string4 == null) {
            if (bl) {
                this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_ELEMENT, SchemaSymbols.ATT_NAME}, element);
            } else {
                this.reportSchemaError("src-element.2.1", null, element);
            }
            string4 = "(no name)";
        }
        if (element2 != null) {
            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{string4, "(annotation?, (simpleType | complexType)?, (unique | key | keyref)*))", DOMUtil.getLocalName(element2)}, element2);
        }
        if (string2 != null && string3 != null) {
            this.reportSchemaError("src-element.1", new Object[]{string4}, element);
        }
        if (bl4 && qName2 != null) {
            this.reportSchemaError("src-element.3", new Object[]{string4}, element);
        }
        this.checkNotationType(string4, xSTypeDefinition, element);
        if (xSElementDecl.fDefault != null) {
            this.fValidationState.setNamespaceSupport(xSDocumentInfo.fNamespaceSupport);
            if (XSConstraints.ElementDefaultValidImmediate(xSElementDecl.fType, xSElementDecl.fDefault.normalizedValue, this.fValidationState, xSElementDecl.fDefault) == null) {
                this.reportSchemaError("e-props-correct.2", new Object[]{string4, xSElementDecl.fDefault.normalizedValue}, element);
                xSElementDecl.fDefault = null;
                xSElementDecl.setConstraintType(0);
            }
        }
        if (xSElementDecl.fSubGroup != null && !XSConstraints.checkTypeDerivationOk(xSElementDecl.fType, xSElementDecl.fSubGroup.fType, xSElementDecl.fSubGroup.fFinal)) {
            this.reportSchemaError("e-props-correct.4", new Object[]{string4, qName.prefix + ":" + qName.localpart}, element);
            xSElementDecl.fSubGroup = null;
        }
        if (xSElementDecl.fDefault != null && (xSTypeDefinition.getTypeCategory() == 16 && ((XSSimpleType)xSTypeDefinition).isIDType() || xSTypeDefinition.getTypeCategory() == 15 && ((XSComplexTypeDecl)xSTypeDefinition).containsTypeID())) {
            this.reportSchemaError("e-props-correct.5", new Object[]{xSElementDecl.fName}, element);
            xSElementDecl.fDefault = null;
            xSElementDecl.setConstraintType(0);
        }
        if (xSElementDecl.fName == null) {
            return null;
        }
        if (bl) {
            XSElementDecl xSElementDecl2;
            schemaGrammar.addGlobalElementDeclAll(xSElementDecl);
            if (schemaGrammar.getGlobalElementDecl(xSElementDecl.fName) == null) {
                schemaGrammar.addGlobalElementDecl(xSElementDecl);
            }
            if ((xSElementDecl2 = schemaGrammar.getGlobalElementDecl(xSElementDecl.fName, string = this.fSchemaHandler.schemaDocument2SystemId(xSDocumentInfo))) == null) {
                schemaGrammar.addGlobalElementDecl(xSElementDecl, string);
            }
            if (this.fSchemaHandler.fTolerateDuplicates) {
                if (xSElementDecl2 != null) {
                    xSElementDecl = xSElementDecl2;
                }
                this.fSchemaHandler.addGlobalElementDecl(xSElementDecl);
            }
        }
        return xSElementDecl;
    }

    void reset(SymbolTable symbolTable, boolean bl, Locale locale) {
        super.reset(symbolTable, bl, locale);
        this.fDeferTraversingLocalElements = true;
    }
}

