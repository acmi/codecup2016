/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

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
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAbstractParticleTraverser;
import org.apache.xerces.impl.xs.traversers.XSDAbstractTraverser;
import org.apache.xerces.impl.xs.traversers.XSDGroupTraverser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDSimpleTypeTraverser;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class XSDComplexTypeTraverser
extends XSDAbstractParticleTraverser {
    private static final int GLOBAL_NUM = 11;
    private static XSParticleDecl fErrorContent = null;
    private static XSWildcardDecl fErrorWildcard = null;
    private String fName = null;
    private String fTargetNamespace = null;
    private short fDerivedBy = 2;
    private short fFinal = 0;
    private short fBlock = 0;
    private short fContentType = 0;
    private XSTypeDefinition fBaseType = null;
    private XSAttributeGroupDecl fAttrGrp = null;
    private XSSimpleType fXSSimpleType = null;
    private XSParticleDecl fParticle = null;
    private boolean fIsAbstract = false;
    private XSComplexTypeDecl fComplexTypeDecl = null;
    private XSAnnotationImpl[] fAnnotations = null;
    private Object[] fGlobalStore = null;
    private int fGlobalStorePos = 0;
    private static final boolean DEBUG = false;

    private static XSParticleDecl getErrorContent() {
        if (fErrorContent == null) {
            XSParticleDecl xSParticleDecl = new XSParticleDecl();
            xSParticleDecl.fType = 2;
            xSParticleDecl.fValue = XSDComplexTypeTraverser.getErrorWildcard();
            xSParticleDecl.fMinOccurs = 0;
            xSParticleDecl.fMaxOccurs = -1;
            XSModelGroupImpl xSModelGroupImpl = new XSModelGroupImpl();
            xSModelGroupImpl.fCompositor = 102;
            xSModelGroupImpl.fParticleCount = 1;
            xSModelGroupImpl.fParticles = new XSParticleDecl[1];
            xSModelGroupImpl.fParticles[0] = xSParticleDecl;
            XSParticleDecl xSParticleDecl2 = new XSParticleDecl();
            xSParticleDecl2.fType = 3;
            xSParticleDecl2.fValue = xSModelGroupImpl;
            fErrorContent = xSParticleDecl2;
        }
        return fErrorContent;
    }

    private static XSWildcardDecl getErrorWildcard() {
        if (fErrorWildcard == null) {
            XSWildcardDecl xSWildcardDecl = new XSWildcardDecl();
            xSWildcardDecl.fProcessContents = 2;
            fErrorWildcard = xSWildcardDecl;
        }
        return fErrorWildcard;
    }

    XSDComplexTypeTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    XSComplexTypeDecl traverseLocal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        String string = this.genAnonTypeName(element);
        this.contentBackup();
        XSComplexTypeDecl xSComplexTypeDecl = this.traverseComplexTypeDecl(element, string, arrobject, xSDocumentInfo, schemaGrammar);
        this.contentRestore();
        schemaGrammar.addComplexTypeDecl(xSComplexTypeDecl, this.fSchemaHandler.element2Locator(element));
        xSComplexTypeDecl.setIsAnonymous();
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return xSComplexTypeDecl;
    }

    XSComplexTypeDecl traverseGlobal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, true, xSDocumentInfo);
        String string = (String)arrobject[XSAttributeChecker.ATTIDX_NAME];
        this.contentBackup();
        XSComplexTypeDecl xSComplexTypeDecl = this.traverseComplexTypeDecl(element, string, arrobject, xSDocumentInfo, schemaGrammar);
        this.contentRestore();
        schemaGrammar.addComplexTypeDecl(xSComplexTypeDecl, this.fSchemaHandler.element2Locator(element));
        if (string == null) {
            this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_COMPLEXTYPE, SchemaSymbols.ATT_NAME}, element);
            xSComplexTypeDecl = null;
        } else {
            if (schemaGrammar.getGlobalTypeDecl(xSComplexTypeDecl.getName()) == null) {
                schemaGrammar.addGlobalComplexTypeDecl(xSComplexTypeDecl);
            }
            String string2 = this.fSchemaHandler.schemaDocument2SystemId(xSDocumentInfo);
            XSTypeDefinition xSTypeDefinition = schemaGrammar.getGlobalTypeDecl(xSComplexTypeDecl.getName(), string2);
            if (xSTypeDefinition == null) {
                schemaGrammar.addGlobalComplexTypeDecl(xSComplexTypeDecl, string2);
            }
            if (this.fSchemaHandler.fTolerateDuplicates) {
                if (xSTypeDefinition != null && xSTypeDefinition instanceof XSComplexTypeDecl) {
                    xSComplexTypeDecl = (XSComplexTypeDecl)xSTypeDefinition;
                }
                this.fSchemaHandler.addGlobalTypeDecl(xSComplexTypeDecl);
            }
        }
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return xSComplexTypeDecl;
    }

    private XSComplexTypeDecl traverseComplexTypeDecl(Element element, String string, Object[] arrobject, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        this.fComplexTypeDecl = new XSComplexTypeDecl();
        this.fAttrGrp = new XSAttributeGroupDecl();
        Boolean bl = (Boolean)arrobject[XSAttributeChecker.ATTIDX_ABSTRACT];
        XInt xInt = (XInt)arrobject[XSAttributeChecker.ATTIDX_BLOCK];
        Boolean bl2 = (Boolean)arrobject[XSAttributeChecker.ATTIDX_MIXED];
        XInt xInt2 = (XInt)arrobject[XSAttributeChecker.ATTIDX_FINAL];
        this.fName = string;
        this.fComplexTypeDecl.setName(this.fName);
        this.fTargetNamespace = xSDocumentInfo.fTargetNamespace;
        this.fBlock = xInt == null ? xSDocumentInfo.fBlockDefault : xInt.shortValue();
        this.fFinal = xInt2 == null ? xSDocumentInfo.fFinalDefault : xInt2.shortValue();
        this.fBlock = (short)(this.fBlock & 3);
        this.fFinal = (short)(this.fFinal & 3);
        this.fIsAbstract = bl != null && bl != false;
        this.fAnnotations = null;
        Element element2 = null;
        try {
            Object object;
            element2 = DOMUtil.getFirstChildElement(element);
            if (element2 != null) {
                if (DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    this.addAnnotation(this.traverseAnnotationDecl(element2, arrobject, false, xSDocumentInfo));
                    element2 = DOMUtil.getNextSiblingElement(element2);
                } else {
                    object = DOMUtil.getSyntheticAnnotation(element);
                    if (object != null) {
                        this.addAnnotation(this.traverseSyntheticAnnotation(element, (String)object, arrobject, false, xSDocumentInfo));
                    }
                }
                if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, SchemaSymbols.ELT_ANNOTATION}, element2);
                }
            } else {
                object = DOMUtil.getSyntheticAnnotation(element);
                if (object != null) {
                    this.addAnnotation(this.traverseSyntheticAnnotation(element, (String)object, arrobject, false, xSDocumentInfo));
                }
            }
            if (element2 == null) {
                this.fBaseType = SchemaGrammar.fAnyType;
                this.fDerivedBy = 2;
                this.processComplexContent(element2, bl2, false, xSDocumentInfo, schemaGrammar);
            } else if (DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_SIMPLECONTENT)) {
                this.traverseSimpleContent(element2, xSDocumentInfo, schemaGrammar);
                object = DOMUtil.getNextSiblingElement(element2);
                if (object != null) {
                    String string2 = DOMUtil.getLocalName((Node)object);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, string2}, (Element)object);
                }
            } else if (DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_COMPLEXCONTENT)) {
                this.traverseComplexContent(element2, bl2, xSDocumentInfo, schemaGrammar);
                object = DOMUtil.getNextSiblingElement(element2);
                if (object != null) {
                    String string3 = DOMUtil.getLocalName((Node)object);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, string3}, (Element)object);
                }
            } else {
                this.fBaseType = SchemaGrammar.fAnyType;
                this.fDerivedBy = 2;
                this.processComplexContent(element2, bl2, false, xSDocumentInfo, schemaGrammar);
            }
        }
        catch (ComplexTypeRecoverableError complexTypeRecoverableError) {
            this.handleComplexTypeError(complexTypeRecoverableError.getMessage(), complexTypeRecoverableError.errorSubstText, complexTypeRecoverableError.errorElem);
        }
        this.fComplexTypeDecl.setValues(this.fName, this.fTargetNamespace, this.fBaseType, this.fDerivedBy, this.fFinal, this.fBlock, this.fContentType, this.fIsAbstract, this.fAttrGrp, this.fXSSimpleType, this.fParticle, new XSObjectListImpl(this.fAnnotations, this.fAnnotations == null ? 0 : this.fAnnotations.length));
        return this.fComplexTypeDecl;
    }

    private void traverseSimpleContent(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) throws ComplexTypeRecoverableError {
        String string;
        Object object;
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        this.fContentType = 1;
        this.fParticle = null;
        Element element2 = DOMUtil.getFirstChildElement(element);
        if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
            this.addAnnotation(this.traverseAnnotationDecl(element2, arrobject, false, xSDocumentInfo));
            element2 = DOMUtil.getNextSiblingElement(element2);
        } else {
            string = DOMUtil.getSyntheticAnnotation(element);
            if (string != null) {
                this.addAnnotation(this.traverseSyntheticAnnotation(element, string, arrobject, false, xSDocumentInfo));
            }
        }
        if (element2 == null) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[]{this.fName, SchemaSymbols.ELT_SIMPLECONTENT}, element);
        }
        string = DOMUtil.getLocalName(element2);
        if (string.equals(SchemaSymbols.ELT_RESTRICTION)) {
            this.fDerivedBy = 2;
        } else if (string.equals(SchemaSymbols.ELT_EXTENSION)) {
            this.fDerivedBy = 1;
        } else {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, string}, element2);
        }
        Element element3 = DOMUtil.getNextSiblingElement(element2);
        if (element3 != null) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            String string2 = DOMUtil.getLocalName(element3);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, string2}, element3);
        }
        Object[] arrobject2 = this.fAttrChecker.checkAttributes(element2, false, xSDocumentInfo);
        QName qName = (QName)arrobject2[XSAttributeChecker.ATTIDX_BASE];
        if (qName == null) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[]{string, "base"}, element2);
        }
        XSTypeDefinition xSTypeDefinition = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 7, qName, element2);
        if (xSTypeDefinition == null) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            throw new ComplexTypeRecoverableError();
        }
        this.fBaseType = xSTypeDefinition;
        Object object2 = null;
        XSComplexTypeDecl xSComplexTypeDecl = null;
        short s2 = 0;
        if (xSTypeDefinition.getTypeCategory() == 15) {
            xSComplexTypeDecl = (XSComplexTypeDecl)xSTypeDefinition;
            s2 = xSComplexTypeDecl.getFinal();
            if (xSComplexTypeDecl.getContentType() == 1) {
                object2 = (XSSimpleType)xSComplexTypeDecl.getSimpleType();
            } else if (this.fDerivedBy != 2 || xSComplexTypeDecl.getContentType() != 3 || !((XSParticleDecl)xSComplexTypeDecl.getParticle()).emptiable()) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[]{this.fName, xSComplexTypeDecl.getName()}, element2);
            }
        } else {
            object2 = (XSSimpleType)xSTypeDefinition;
            if (this.fDerivedBy == 2) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[]{this.fName, object2.getName()}, element2);
            }
            s2 = object2.getFinal();
        }
        if ((s2 & this.fDerivedBy) != 0) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            String string3 = this.fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
            throw new ComplexTypeRecoverableError(string3, new Object[]{this.fName, this.fBaseType.getName()}, element2);
        }
        Element element4 = element2;
        if ((element2 = DOMUtil.getFirstChildElement(element2)) != null) {
            if (DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                this.addAnnotation(this.traverseAnnotationDecl(element2, arrobject2, false, xSDocumentInfo));
                element2 = DOMUtil.getNextSiblingElement(element2);
            } else {
                object = DOMUtil.getSyntheticAnnotation(element4);
                if (object != null) {
                    this.addAnnotation(this.traverseSyntheticAnnotation(element4, (String)object, arrobject2, false, xSDocumentInfo));
                }
            }
            if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, SchemaSymbols.ELT_ANNOTATION}, element2);
            }
        } else {
            object = DOMUtil.getSyntheticAnnotation(element4);
            if (object != null) {
                this.addAnnotation(this.traverseSyntheticAnnotation(element4, (String)object, arrobject2, false, xSDocumentInfo));
            }
        }
        if (this.fDerivedBy == 2) {
            Object[] arrobject3;
            Object object3;
            if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                object = this.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar);
                if (object == null) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw new ComplexTypeRecoverableError();
                }
                if (object2 != null && !XSConstraints.checkSimpleDerivationOk((XSSimpleType)object, (XSTypeDefinition)object2, object2.getFinal())) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.2.2.1", new Object[]{this.fName, object.getName(), object2.getName()}, element2);
                }
                object2 = object;
                element2 = DOMUtil.getNextSiblingElement(element2);
            }
            if (object2 == null) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw new ComplexTypeRecoverableError("src-ct.2.2", new Object[]{this.fName}, element2);
            }
            object = null;
            XSFacets xSFacets = null;
            short s3 = 0;
            short s4 = 0;
            if (element2 != null) {
                object3 = this.traverseFacets(element2, (XSSimpleType)object2, xSDocumentInfo);
                object = object3.nodeAfterFacets;
                xSFacets = object3.facetdata;
                s3 = object3.fPresentFacets;
                s4 = object3.fFixedFacets;
            }
            object3 = this.genAnonTypeName(element);
            this.fXSSimpleType = this.fSchemaHandler.fDVFactory.createTypeRestriction((String)object3, xSDocumentInfo.fTargetNamespace, 0, (XSSimpleType)object2, null);
            try {
                this.fValidationState.setNamespaceSupport(xSDocumentInfo.fNamespaceSupport);
                this.fXSSimpleType.applyFacets(xSFacets, s3, s4, this.fValidationState);
            }
            catch (InvalidDatatypeFacetException invalidDatatypeFacetException) {
                this.reportSchemaError(invalidDatatypeFacetException.getKey(), invalidDatatypeFacetException.getArgs(), element2);
                this.fXSSimpleType = this.fSchemaHandler.fDVFactory.createTypeRestriction((String)object3, xSDocumentInfo.fTargetNamespace, 0, (XSSimpleType)object2, null);
            }
            if (this.fXSSimpleType instanceof XSSimpleTypeDecl) {
                ((XSSimpleTypeDecl)this.fXSSimpleType).setAnonymous(true);
            }
            if (object != null) {
                if (!this.isAttrOrAttrGroup((Element)object)) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName((Node)object)}, (Element)object);
                }
                arrobject3 = this.traverseAttrsAndAttrGrps((Element)object, this.fAttrGrp, xSDocumentInfo, schemaGrammar, this.fComplexTypeDecl);
                if (arrobject3 != null) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName((Node)arrobject3)}, (Element)arrobject3);
                }
            }
            try {
                this.mergeAttributes(xSComplexTypeDecl.getAttrGrp(), this.fAttrGrp, this.fName, false, element);
            }
            catch (ComplexTypeRecoverableError complexTypeRecoverableError) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw complexTypeRecoverableError;
            }
            this.fAttrGrp.removeProhibitedAttrs();
            arrobject3 = this.fAttrGrp.validRestrictionOf(this.fName, xSComplexTypeDecl.getAttrGrp());
            if (arrobject3 != null) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw new ComplexTypeRecoverableError((String)arrobject3[arrobject3.length - 1], arrobject3, (Element)object);
            }
        } else {
            this.fXSSimpleType = object2;
            if (element2 != null) {
                object = element2;
                if (!this.isAttrOrAttrGroup((Element)object)) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName((Node)object)}, (Element)object);
                }
                Element element5 = this.traverseAttrsAndAttrGrps((Element)object, this.fAttrGrp, xSDocumentInfo, schemaGrammar, this.fComplexTypeDecl);
                if (element5 != null) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(element5)}, element5);
                }
                this.fAttrGrp.removeProhibitedAttrs();
            }
            if (xSComplexTypeDecl != null) {
                try {
                    this.mergeAttributes(xSComplexTypeDecl.getAttrGrp(), this.fAttrGrp, this.fName, true, element);
                }
                catch (ComplexTypeRecoverableError complexTypeRecoverableError) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw complexTypeRecoverableError;
                }
            }
        }
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
    }

    private void traverseComplexContent(Element element, boolean bl, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) throws ComplexTypeRecoverableError {
        String string;
        Object object;
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        boolean bl2 = bl;
        Boolean bl3 = (Boolean)arrobject[XSAttributeChecker.ATTIDX_MIXED];
        if (bl3 != null) {
            bl2 = bl3;
        }
        this.fXSSimpleType = null;
        Element element2 = DOMUtil.getFirstChildElement(element);
        if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
            this.addAnnotation(this.traverseAnnotationDecl(element2, arrobject, false, xSDocumentInfo));
            element2 = DOMUtil.getNextSiblingElement(element2);
        } else {
            string = DOMUtil.getSyntheticAnnotation(element);
            if (string != null) {
                this.addAnnotation(this.traverseSyntheticAnnotation(element, string, arrobject, false, xSDocumentInfo));
            }
        }
        if (element2 == null) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[]{this.fName, SchemaSymbols.ELT_COMPLEXCONTENT}, element);
        }
        string = DOMUtil.getLocalName(element2);
        if (string.equals(SchemaSymbols.ELT_RESTRICTION)) {
            this.fDerivedBy = 2;
        } else if (string.equals(SchemaSymbols.ELT_EXTENSION)) {
            this.fDerivedBy = 1;
        } else {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, string}, element2);
        }
        Element element3 = DOMUtil.getNextSiblingElement(element2);
        if (element3 != null) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            String string2 = DOMUtil.getLocalName(element3);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, string2}, element3);
        }
        Object[] arrobject2 = this.fAttrChecker.checkAttributes(element2, false, xSDocumentInfo);
        QName qName = (QName)arrobject2[XSAttributeChecker.ATTIDX_BASE];
        if (qName == null) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[]{string, "base"}, element2);
        }
        XSTypeDefinition xSTypeDefinition = (XSTypeDefinition)this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 7, qName, element2);
        if (xSTypeDefinition == null) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            throw new ComplexTypeRecoverableError();
        }
        if (!(xSTypeDefinition instanceof XSComplexTypeDecl)) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            throw new ComplexTypeRecoverableError("src-ct.1", new Object[]{this.fName, xSTypeDefinition.getName()}, element2);
        }
        XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl)xSTypeDefinition;
        this.fBaseType = xSComplexTypeDecl;
        if ((xSComplexTypeDecl.getFinal() & this.fDerivedBy) != 0) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            String string3 = this.fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
            throw new ComplexTypeRecoverableError(string3, new Object[]{this.fName, this.fBaseType.getName()}, element2);
        }
        if ((element2 = DOMUtil.getFirstChildElement(element2)) != null) {
            if (DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                this.addAnnotation(this.traverseAnnotationDecl(element2, arrobject2, false, xSDocumentInfo));
                element2 = DOMUtil.getNextSiblingElement(element2);
            } else {
                object = DOMUtil.getSyntheticAnnotation(element2);
                if (object != null) {
                    this.addAnnotation(this.traverseSyntheticAnnotation(element2, (String)object, arrobject2, false, xSDocumentInfo));
                }
            }
            if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, SchemaSymbols.ELT_ANNOTATION}, element2);
            }
        } else {
            object = DOMUtil.getSyntheticAnnotation(element2);
            if (object != null) {
                this.addAnnotation(this.traverseSyntheticAnnotation(element2, (String)object, arrobject2, false, xSDocumentInfo));
            }
        }
        try {
            this.processComplexContent(element2, bl2, true, xSDocumentInfo, schemaGrammar);
        }
        catch (ComplexTypeRecoverableError complexTypeRecoverableError) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            throw complexTypeRecoverableError;
        }
        object = (XSParticleDecl)xSComplexTypeDecl.getParticle();
        if (this.fDerivedBy == 2) {
            Object[] arrobject3;
            if (this.fContentType == 3 && xSComplexTypeDecl.getContentType() != 3) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.4.1.2", new Object[]{this.fName, xSComplexTypeDecl.getName()}, element2);
            }
            try {
                this.mergeAttributes(xSComplexTypeDecl.getAttrGrp(), this.fAttrGrp, this.fName, false, element2);
            }
            catch (ComplexTypeRecoverableError complexTypeRecoverableError) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw complexTypeRecoverableError;
            }
            this.fAttrGrp.removeProhibitedAttrs();
            if (xSComplexTypeDecl != SchemaGrammar.fAnyType && (arrobject3 = this.fAttrGrp.validRestrictionOf(this.fName, xSComplexTypeDecl.getAttrGrp())) != null) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw new ComplexTypeRecoverableError((String)arrobject3[arrobject3.length - 1], arrobject3, element2);
            }
        } else {
            if (this.fParticle == null) {
                this.fContentType = xSComplexTypeDecl.getContentType();
                this.fXSSimpleType = (XSSimpleType)xSComplexTypeDecl.getSimpleType();
                this.fParticle = object;
            } else if (xSComplexTypeDecl.getContentType() != 0) {
                if (this.fContentType == 2 && xSComplexTypeDecl.getContentType() != 2) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.a", new Object[]{this.fName}, element2);
                }
                if (this.fContentType == 3 && xSComplexTypeDecl.getContentType() != 3) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.b", new Object[]{this.fName}, element2);
                }
                if (this.fParticle.fType == 3 && ((XSModelGroupImpl)this.fParticle.fValue).fCompositor == 103 || ((XSParticleDecl)xSComplexTypeDecl.getParticle()).fType == 3 && ((XSModelGroupImpl)((XSParticleDecl)xSComplexTypeDecl.getParticle()).fValue).fCompositor == 103) {
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                    throw new ComplexTypeRecoverableError("cos-all-limited.1.2", new Object[0], element2);
                }
                XSModelGroupImpl xSModelGroupImpl = new XSModelGroupImpl();
                xSModelGroupImpl.fCompositor = 102;
                xSModelGroupImpl.fParticleCount = 2;
                xSModelGroupImpl.fParticles = new XSParticleDecl[2];
                xSModelGroupImpl.fParticles[0] = (XSParticleDecl)xSComplexTypeDecl.getParticle();
                xSModelGroupImpl.fParticles[1] = this.fParticle;
                xSModelGroupImpl.fAnnotations = XSObjectListImpl.EMPTY_LIST;
                XSParticleDecl xSParticleDecl = new XSParticleDecl();
                xSParticleDecl.fType = 3;
                xSParticleDecl.fValue = xSModelGroupImpl;
                xSParticleDecl.fAnnotations = XSObjectListImpl.EMPTY_LIST;
                this.fParticle = xSParticleDecl;
            }
            this.fAttrGrp.removeProhibitedAttrs();
            try {
                this.mergeAttributes(xSComplexTypeDecl.getAttrGrp(), this.fAttrGrp, this.fName, true, element2);
            }
            catch (ComplexTypeRecoverableError complexTypeRecoverableError) {
                this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
                throw complexTypeRecoverableError;
            }
        }
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
    }

    private void mergeAttributes(XSAttributeGroupDecl xSAttributeGroupDecl, XSAttributeGroupDecl xSAttributeGroupDecl2, String string, boolean bl, Element element) throws ComplexTypeRecoverableError {
        XSObjectList xSObjectList = xSAttributeGroupDecl.getAttributeUses();
        XSAttributeUseImpl xSAttributeUseImpl = null;
        int n2 = xSObjectList.getLength();
        int n3 = 0;
        while (n3 < n2) {
            xSAttributeUseImpl = (XSAttributeUseImpl)xSObjectList.item(n3);
            XSAttributeUse xSAttributeUse = xSAttributeGroupDecl2.getAttributeUse(xSAttributeUseImpl.fAttrDecl.getNamespace(), xSAttributeUseImpl.fAttrDecl.getName());
            if (xSAttributeUse == null) {
                String string2 = xSAttributeGroupDecl2.addAttributeUse(xSAttributeUseImpl);
                if (string2 != null) {
                    throw new ComplexTypeRecoverableError("ct-props-correct.5", new Object[]{string, string2, xSAttributeUseImpl.fAttrDecl.getName()}, element);
                }
            } else if (xSAttributeUse != xSAttributeUseImpl && bl) {
                this.reportSchemaError("ct-props-correct.4", new Object[]{string, xSAttributeUseImpl.fAttrDecl.getName()}, element);
                xSAttributeGroupDecl2.replaceAttributeUse(xSAttributeUse, xSAttributeUseImpl);
            }
            ++n3;
        }
        if (bl) {
            if (xSAttributeGroupDecl2.fAttributeWC == null) {
                xSAttributeGroupDecl2.fAttributeWC = xSAttributeGroupDecl.fAttributeWC;
            } else if (xSAttributeGroupDecl.fAttributeWC != null) {
                xSAttributeGroupDecl2.fAttributeWC = xSAttributeGroupDecl2.fAttributeWC.performUnionWith(xSAttributeGroupDecl.fAttributeWC, xSAttributeGroupDecl2.fAttributeWC.fProcessContents);
                if (xSAttributeGroupDecl2.fAttributeWC == null) {
                    throw new ComplexTypeRecoverableError("src-ct.5", new Object[]{string}, element);
                }
            }
        }
    }

    private void processComplexContent(Element element, boolean bl, boolean bl2, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) throws ComplexTypeRecoverableError {
        Object object;
        Element element2 = null;
        XSParticleDecl xSParticleDecl = null;
        boolean bl3 = false;
        if (element != null) {
            object = DOMUtil.getLocalName(element);
            if (object.equals(SchemaSymbols.ELT_GROUP)) {
                xSParticleDecl = this.fSchemaHandler.fGroupTraverser.traverseLocal(element, xSDocumentInfo, schemaGrammar);
                element2 = DOMUtil.getNextSiblingElement(element);
            } else if (object.equals(SchemaSymbols.ELT_SEQUENCE)) {
                xSParticleDecl = this.traverseSequence(element, xSDocumentInfo, schemaGrammar, 0, this.fComplexTypeDecl);
                if (xSParticleDecl != null) {
                    XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
                    if (xSModelGroupImpl.fParticleCount == 0) {
                        bl3 = true;
                    }
                }
                element2 = DOMUtil.getNextSiblingElement(element);
            } else if (object.equals(SchemaSymbols.ELT_CHOICE)) {
                xSParticleDecl = this.traverseChoice(element, xSDocumentInfo, schemaGrammar, 0, this.fComplexTypeDecl);
                if (xSParticleDecl != null && xSParticleDecl.fMinOccurs == 0) {
                    XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
                    if (xSModelGroupImpl.fParticleCount == 0) {
                        bl3 = true;
                    }
                }
                element2 = DOMUtil.getNextSiblingElement(element);
            } else if (object.equals(SchemaSymbols.ELT_ALL)) {
                xSParticleDecl = this.traverseAll(element, xSDocumentInfo, schemaGrammar, 8, this.fComplexTypeDecl);
                if (xSParticleDecl != null) {
                    XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl)xSParticleDecl.fValue;
                    if (xSModelGroupImpl.fParticleCount == 0) {
                        bl3 = true;
                    }
                }
                element2 = DOMUtil.getNextSiblingElement(element);
            } else {
                element2 = element;
            }
        }
        if (bl3) {
            object = DOMUtil.getFirstChildElement(element);
            if (object != null && DOMUtil.getLocalName((Node)object).equals(SchemaSymbols.ELT_ANNOTATION)) {
                object = DOMUtil.getNextSiblingElement((Node)object);
            }
            if (object == null) {
                xSParticleDecl = null;
            }
        }
        if (xSParticleDecl == null && bl) {
            xSParticleDecl = XSConstraints.getEmptySequence();
        }
        this.fParticle = xSParticleDecl;
        this.fContentType = this.fParticle == null ? 0 : (bl ? 3 : 2);
        if (element2 != null) {
            if (!this.isAttrOrAttrGroup(element2)) {
                throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(element2)}, element2);
            }
            object = this.traverseAttrsAndAttrGrps(element2, this.fAttrGrp, xSDocumentInfo, schemaGrammar, this.fComplexTypeDecl);
            if (object != null) {
                throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName((Node)object)}, (Element)object);
            }
            if (!bl2) {
                this.fAttrGrp.removeProhibitedAttrs();
            }
        }
    }

    private boolean isAttrOrAttrGroup(Element element) {
        String string = DOMUtil.getLocalName(element);
        if (string.equals(SchemaSymbols.ELT_ATTRIBUTE) || string.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) || string.equals(SchemaSymbols.ELT_ANYATTRIBUTE)) {
            return true;
        }
        return false;
    }

    private void traverseSimpleContentDecl(Element element) {
    }

    private void traverseComplexContentDecl(Element element, boolean bl) {
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

    private void handleComplexTypeError(String string, Object[] arrobject, Element element) {
        if (string != null) {
            this.reportSchemaError(string, arrobject, element);
        }
        this.fBaseType = SchemaGrammar.fAnyType;
        this.fContentType = 3;
        this.fXSSimpleType = null;
        this.fParticle = XSDComplexTypeTraverser.getErrorContent();
        this.fAttrGrp.fAttributeWC = XSDComplexTypeTraverser.getErrorWildcard();
    }

    private void contentBackup() {
        if (this.fGlobalStore == null) {
            this.fGlobalStore = new Object[11];
            this.fGlobalStorePos = 0;
        }
        if (this.fGlobalStorePos == this.fGlobalStore.length) {
            Object[] arrobject = new Object[this.fGlobalStorePos + 11];
            System.arraycopy(this.fGlobalStore, 0, arrobject, 0, this.fGlobalStorePos);
            this.fGlobalStore = arrobject;
        }
        this.fGlobalStore[this.fGlobalStorePos++] = this.fComplexTypeDecl;
        this.fGlobalStore[this.fGlobalStorePos++] = this.fIsAbstract ? Boolean.TRUE : Boolean.FALSE;
        this.fGlobalStore[this.fGlobalStorePos++] = this.fName;
        this.fGlobalStore[this.fGlobalStorePos++] = this.fTargetNamespace;
        this.fGlobalStore[this.fGlobalStorePos++] = new Integer((this.fDerivedBy << 16) + this.fFinal);
        this.fGlobalStore[this.fGlobalStorePos++] = new Integer((this.fBlock << 16) + this.fContentType);
        this.fGlobalStore[this.fGlobalStorePos++] = this.fBaseType;
        this.fGlobalStore[this.fGlobalStorePos++] = this.fAttrGrp;
        this.fGlobalStore[this.fGlobalStorePos++] = this.fParticle;
        this.fGlobalStore[this.fGlobalStorePos++] = this.fXSSimpleType;
        this.fGlobalStore[this.fGlobalStorePos++] = this.fAnnotations;
    }

    private void contentRestore() {
        this.fAnnotations = (XSAnnotationImpl[])this.fGlobalStore[--this.fGlobalStorePos];
        this.fXSSimpleType = (XSSimpleType)this.fGlobalStore[--this.fGlobalStorePos];
        this.fParticle = (XSParticleDecl)this.fGlobalStore[--this.fGlobalStorePos];
        this.fAttrGrp = (XSAttributeGroupDecl)this.fGlobalStore[--this.fGlobalStorePos];
        this.fBaseType = (XSTypeDefinition)this.fGlobalStore[--this.fGlobalStorePos];
        int n2 = (Integer)this.fGlobalStore[--this.fGlobalStorePos];
        this.fBlock = (short)(n2 >> 16);
        this.fContentType = (short)n2;
        n2 = (Integer)this.fGlobalStore[--this.fGlobalStorePos];
        this.fDerivedBy = (short)(n2 >> 16);
        this.fFinal = (short)n2;
        this.fTargetNamespace = (String)this.fGlobalStore[--this.fGlobalStorePos];
        this.fName = (String)this.fGlobalStore[--this.fGlobalStorePos];
        this.fIsAbstract = (Boolean)this.fGlobalStore[--this.fGlobalStorePos];
        this.fComplexTypeDecl = (XSComplexTypeDecl)this.fGlobalStore[--this.fGlobalStorePos];
    }

    private void addAnnotation(XSAnnotationImpl xSAnnotationImpl) {
        if (xSAnnotationImpl == null) {
            return;
        }
        if (this.fAnnotations == null) {
            this.fAnnotations = new XSAnnotationImpl[1];
        } else {
            XSAnnotationImpl[] arrxSAnnotationImpl = new XSAnnotationImpl[this.fAnnotations.length + 1];
            System.arraycopy(this.fAnnotations, 0, arrxSAnnotationImpl, 0, this.fAnnotations.length);
            this.fAnnotations = arrxSAnnotationImpl;
        }
        this.fAnnotations[this.fAnnotations.length - 1] = xSAnnotationImpl;
    }

    private static final class ComplexTypeRecoverableError
    extends Exception {
        private static final long serialVersionUID = 6802729912091130335L;
        Object[] errorSubstText = null;
        Element errorElem = null;

        ComplexTypeRecoverableError() {
        }

        ComplexTypeRecoverableError(String string, Object[] arrobject, Element element) {
            super(string);
            this.errorSubstText = arrobject;
            this.errorElem = element;
        }
    }

}

