/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import java.util.Locale;
import java.util.Vector;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.traversers.XSAnnotationInfo;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAttributeGroupTraverser;
import org.apache.xerces.impl.xs.traversers.XSDAttributeTraverser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDWildcardTraverser;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

abstract class XSDAbstractTraverser {
    protected static final String NO_NAME = "(no name)";
    protected static final int NOT_ALL_CONTEXT = 0;
    protected static final int PROCESSING_ALL_EL = 1;
    protected static final int GROUP_REF_WITH_ALL = 2;
    protected static final int CHILD_OF_GROUP = 4;
    protected static final int PROCESSING_ALL_GP = 8;
    protected XSDHandler fSchemaHandler = null;
    protected SymbolTable fSymbolTable = null;
    protected XSAttributeChecker fAttrChecker = null;
    protected boolean fValidateAnnotations = false;
    ValidationState fValidationState = new ValidationState();
    private static final XSSimpleType fQNameDV = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("QName");
    private StringBuffer fPattern = new StringBuffer();
    private final XSFacets xsFacets = new XSFacets();

    XSDAbstractTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        this.fSchemaHandler = xSDHandler;
        this.fAttrChecker = xSAttributeChecker;
    }

    void reset(SymbolTable symbolTable, boolean bl, Locale locale) {
        this.fSymbolTable = symbolTable;
        this.fValidateAnnotations = bl;
        this.fValidationState.setExtraChecking(false);
        this.fValidationState.setSymbolTable(symbolTable);
        this.fValidationState.setLocale(locale);
    }

    XSAnnotationImpl traverseAnnotationDecl(Element element, Object[] arrobject, boolean bl, XSDocumentInfo xSDocumentInfo) {
        Object object;
        Object[] arrobject2 = this.fAttrChecker.checkAttributes(element, bl, xSDocumentInfo);
        this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
        String string = DOMUtil.getAnnotation(element);
        Element element2 = DOMUtil.getFirstChildElement(element);
        if (element2 != null) {
            do {
                if (!(object = DOMUtil.getLocalName(element2)).equals(SchemaSymbols.ELT_APPINFO) && !object.equals(SchemaSymbols.ELT_DOCUMENTATION)) {
                    this.reportSchemaError("src-annotation", new Object[]{object}, element2);
                    continue;
                }
                arrobject2 = this.fAttrChecker.checkAttributes(element2, true, xSDocumentInfo);
                this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
            } while ((element2 = DOMUtil.getNextSiblingElement(element2)) != null);
        }
        if (string == null) {
            return null;
        }
        object = this.fSchemaHandler.getGrammar(xSDocumentInfo.fTargetNamespace);
        Vector vector = (Vector)arrobject[XSAttributeChecker.ATTIDX_NONSCHEMA];
        if (vector != null && !vector.isEmpty()) {
            String string2;
            int n2;
            String string3;
            StringBuffer stringBuffer = new StringBuffer(64);
            stringBuffer.append(" ");
            int n3 = 0;
            while (n3 < vector.size()) {
                String string4;
                if ((n2 = (string2 = (String)vector.elementAt(n3++)).indexOf(58)) == -1) {
                    string3 = "";
                    string4 = string2;
                } else {
                    string3 = string2.substring(0, n2);
                    string4 = string2.substring(n2 + 1);
                }
                String string5 = xSDocumentInfo.fNamespaceSupport.getURI(this.fSymbolTable.addSymbol(string3));
                if (element.getAttributeNS(string5, string4).length() != 0) {
                    ++n3;
                    continue;
                }
                stringBuffer.append(string2).append("=\"");
                String string6 = (String)vector.elementAt(n3++);
                string6 = XSDAbstractTraverser.processAttValue(string6);
                stringBuffer.append(string6).append("\" ");
            }
            string2 = new StringBuffer(string.length() + stringBuffer.length());
            n2 = string.indexOf(SchemaSymbols.ELT_ANNOTATION);
            if (n2 == -1) {
                return null;
            }
            string2.append(string.substring(0, n2 += SchemaSymbols.ELT_ANNOTATION.length()));
            string2.append(stringBuffer.toString());
            string2.append(string.substring(n2, string.length()));
            string3 = string2.toString();
            if (this.fValidateAnnotations) {
                xSDocumentInfo.addAnnotation(new XSAnnotationInfo(string3, element));
            }
            return new XSAnnotationImpl(string3, (SchemaGrammar)object);
        }
        if (this.fValidateAnnotations) {
            xSDocumentInfo.addAnnotation(new XSAnnotationInfo(string, element));
        }
        return new XSAnnotationImpl(string, (SchemaGrammar)object);
    }

    XSAnnotationImpl traverseSyntheticAnnotation(Element element, String string, Object[] arrobject, boolean bl, XSDocumentInfo xSDocumentInfo) {
        String string2 = string;
        SchemaGrammar schemaGrammar = this.fSchemaHandler.getGrammar(xSDocumentInfo.fTargetNamespace);
        Vector vector = (Vector)arrobject[XSAttributeChecker.ATTIDX_NONSCHEMA];
        if (vector != null && !vector.isEmpty()) {
            String string3;
            int n2;
            String string4;
            StringBuffer stringBuffer = new StringBuffer(64);
            stringBuffer.append(" ");
            int n3 = 0;
            while (n3 < vector.size()) {
                String string5;
                if ((n2 = (string4 = (String)vector.elementAt(n3++)).indexOf(58)) == -1) {
                    string3 = "";
                    string5 = string4;
                } else {
                    string3 = string4.substring(0, n2);
                    string5 = string4.substring(n2 + 1);
                }
                String string6 = xSDocumentInfo.fNamespaceSupport.getURI(this.fSymbolTable.addSymbol(string3));
                stringBuffer.append(string4).append("=\"");
                String string7 = (String)vector.elementAt(n3++);
                string7 = XSDAbstractTraverser.processAttValue(string7);
                stringBuffer.append(string7).append("\" ");
            }
            string4 = new StringBuffer(string2.length() + stringBuffer.length());
            n2 = string2.indexOf(SchemaSymbols.ELT_ANNOTATION);
            if (n2 == -1) {
                return null;
            }
            string4.append(string2.substring(0, n2 += SchemaSymbols.ELT_ANNOTATION.length()));
            string4.append(stringBuffer.toString());
            string4.append(string2.substring(n2, string2.length()));
            string3 = string4.toString();
            if (this.fValidateAnnotations) {
                xSDocumentInfo.addAnnotation(new XSAnnotationInfo(string3, element));
            }
            return new XSAnnotationImpl(string3, schemaGrammar);
        }
        if (this.fValidateAnnotations) {
            xSDocumentInfo.addAnnotation(new XSAnnotationInfo(string2, element));
        }
        return new XSAnnotationImpl(string2, schemaGrammar);
    }

    FacetInfo traverseFacets(Element element, XSSimpleType xSSimpleType, XSDocumentInfo xSDocumentInfo) {
        short s2 = 0;
        short s3 = 0;
        boolean bl = this.containsQName(xSSimpleType);
        Vector<String> vector = null;
        XSObjectListImpl xSObjectListImpl = null;
        XSObjectListImpl xSObjectListImpl2 = null;
        Vector<Object> vector2 = bl ? new Vector<Object>() : null;
        int n2 = 0;
        this.xsFacets.reset();
        while (element != null) {
            Object object;
            Object object2;
            Object object3;
            Object[] arrobject = null;
            String string = DOMUtil.getLocalName(element);
            if (string.equals(SchemaSymbols.ELT_ENUMERATION)) {
                Object object4;
                arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo, bl);
                object2 = (String)arrobject[XSAttributeChecker.ATTIDX_VALUE];
                if (object2 == null) {
                    this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_ENUMERATION, SchemaSymbols.ATT_VALUE}, element);
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    element = DOMUtil.getNextSiblingElement(element);
                    continue;
                }
                object3 = (NamespaceSupport)arrobject[XSAttributeChecker.ATTIDX_ENUMNSDECLS];
                if (xSSimpleType.getVariety() == 1 && xSSimpleType.getPrimitiveKind() == 20) {
                    xSDocumentInfo.fValidationContext.setNamespaceSupport((NamespaceContext)object3);
                    object = null;
                    try {
                        object4 = (QName)fQNameDV.validate((String)object2, (ValidationContext)xSDocumentInfo.fValidationContext, null);
                        object = this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 6, (QName)object4, element);
                    }
                    catch (InvalidDatatypeValueException invalidDatatypeValueException) {
                        this.reportSchemaError(invalidDatatypeValueException.getKey(), invalidDatatypeValueException.getArgs(), element);
                    }
                    if (object == null) {
                        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                        element = DOMUtil.getNextSiblingElement(element);
                        continue;
                    }
                    xSDocumentInfo.fValidationContext.setNamespaceSupport(xSDocumentInfo.fNamespaceSupport);
                }
                if (vector == null) {
                    vector = new Vector<String>();
                    xSObjectListImpl = new XSObjectListImpl();
                }
                vector.addElement((String)object2);
                xSObjectListImpl.addXSObject(null);
                if (bl) {
                    vector2.addElement(object3);
                }
                if ((object = DOMUtil.getFirstChildElement(element)) != null && DOMUtil.getLocalName((Node)object).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    xSObjectListImpl.addXSObject(xSObjectListImpl.getLength() - 1, this.traverseAnnotationDecl((Element)object, arrobject, false, xSDocumentInfo));
                    object = DOMUtil.getNextSiblingElement((Node)object);
                } else {
                    object4 = DOMUtil.getSyntheticAnnotation(element);
                    if (object4 != null) {
                        xSObjectListImpl.addXSObject(xSObjectListImpl.getLength() - 1, this.traverseSyntheticAnnotation(element, (String)object4, arrobject, false, xSDocumentInfo));
                    }
                }
                if (object != null) {
                    this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"enumeration", "(annotation?)", DOMUtil.getLocalName((Node)object)}, (Element)object);
                }
            } else if (string.equals(SchemaSymbols.ELT_PATTERN)) {
                arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
                object2 = (String)arrobject[XSAttributeChecker.ATTIDX_VALUE];
                if (object2 == null) {
                    this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_PATTERN, SchemaSymbols.ATT_VALUE}, element);
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    element = DOMUtil.getNextSiblingElement(element);
                    continue;
                }
                if (this.fPattern.length() == 0) {
                    this.fPattern.append((String)object2);
                } else {
                    this.fPattern.append("|");
                    this.fPattern.append((String)object2);
                }
                object3 = DOMUtil.getFirstChildElement(element);
                if (object3 != null && DOMUtil.getLocalName((Node)object3).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    if (xSObjectListImpl2 == null) {
                        xSObjectListImpl2 = new XSObjectListImpl();
                    }
                    xSObjectListImpl2.addXSObject(this.traverseAnnotationDecl((Element)object3, arrobject, false, xSDocumentInfo));
                    object3 = DOMUtil.getNextSiblingElement((Node)object3);
                } else {
                    object = DOMUtil.getSyntheticAnnotation(element);
                    if (object != null) {
                        if (xSObjectListImpl2 == null) {
                            xSObjectListImpl2 = new XSObjectListImpl();
                        }
                        xSObjectListImpl2.addXSObject(this.traverseSyntheticAnnotation(element, (String)object, arrobject, false, xSDocumentInfo));
                    }
                }
                if (object3 != null) {
                    this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"pattern", "(annotation?)", DOMUtil.getLocalName((Node)object3)}, (Element)object3);
                }
            } else {
                if (string.equals(SchemaSymbols.ELT_MINLENGTH)) {
                    n2 = 2;
                } else if (string.equals(SchemaSymbols.ELT_MAXLENGTH)) {
                    n2 = 4;
                } else if (string.equals(SchemaSymbols.ELT_MAXEXCLUSIVE)) {
                    n2 = 64;
                } else if (string.equals(SchemaSymbols.ELT_MAXINCLUSIVE)) {
                    n2 = 32;
                } else if (string.equals(SchemaSymbols.ELT_MINEXCLUSIVE)) {
                    n2 = 128;
                } else if (string.equals(SchemaSymbols.ELT_MININCLUSIVE)) {
                    n2 = 256;
                } else if (string.equals(SchemaSymbols.ELT_TOTALDIGITS)) {
                    n2 = 512;
                } else if (string.equals(SchemaSymbols.ELT_FRACTIONDIGITS)) {
                    n2 = 1024;
                } else if (string.equals(SchemaSymbols.ELT_WHITESPACE)) {
                    n2 = 16;
                } else {
                    if (!string.equals(SchemaSymbols.ELT_LENGTH)) break;
                    n2 = 1;
                }
                arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
                if ((s2 & n2) != 0) {
                    this.reportSchemaError("src-single-facet-value", new Object[]{string}, element);
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    element = DOMUtil.getNextSiblingElement(element);
                    continue;
                }
                if (arrobject[XSAttributeChecker.ATTIDX_VALUE] == null) {
                    if (element.getAttributeNodeNS(null, "value") == null) {
                        this.reportSchemaError("s4s-att-must-appear", new Object[]{element.getLocalName(), SchemaSymbols.ATT_VALUE}, element);
                    }
                    this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
                    element = DOMUtil.getNextSiblingElement(element);
                    continue;
                }
                s2 = (short)(s2 | n2);
                if (((Boolean)arrobject[XSAttributeChecker.ATTIDX_FIXED]).booleanValue()) {
                    s3 = (short)(s3 | n2);
                }
                switch (n2) {
                    case 2: {
                        this.xsFacets.minLength = ((XInt)arrobject[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                        break;
                    }
                    case 4: {
                        this.xsFacets.maxLength = ((XInt)arrobject[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                        break;
                    }
                    case 64: {
                        this.xsFacets.maxExclusive = (String)arrobject[XSAttributeChecker.ATTIDX_VALUE];
                        break;
                    }
                    case 32: {
                        this.xsFacets.maxInclusive = (String)arrobject[XSAttributeChecker.ATTIDX_VALUE];
                        break;
                    }
                    case 128: {
                        this.xsFacets.minExclusive = (String)arrobject[XSAttributeChecker.ATTIDX_VALUE];
                        break;
                    }
                    case 256: {
                        this.xsFacets.minInclusive = (String)arrobject[XSAttributeChecker.ATTIDX_VALUE];
                        break;
                    }
                    case 512: {
                        this.xsFacets.totalDigits = ((XInt)arrobject[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                        break;
                    }
                    case 1024: {
                        this.xsFacets.fractionDigits = ((XInt)arrobject[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                        break;
                    }
                    case 16: {
                        this.xsFacets.whiteSpace = ((XInt)arrobject[XSAttributeChecker.ATTIDX_VALUE]).shortValue();
                        break;
                    }
                    case 1: {
                        this.xsFacets.length = ((XInt)arrobject[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                    }
                }
                object2 = DOMUtil.getFirstChildElement(element);
                object3 = null;
                if (object2 != null && DOMUtil.getLocalName((Node)object2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    object3 = this.traverseAnnotationDecl((Element)object2, arrobject, false, xSDocumentInfo);
                    object2 = DOMUtil.getNextSiblingElement((Node)object2);
                } else {
                    object = DOMUtil.getSyntheticAnnotation(element);
                    if (object != null) {
                        object3 = this.traverseSyntheticAnnotation(element, (String)object, arrobject, false, xSDocumentInfo);
                    }
                }
                switch (n2) {
                    case 2: {
                        this.xsFacets.minLengthAnnotation = object3;
                        break;
                    }
                    case 4: {
                        this.xsFacets.maxLengthAnnotation = object3;
                        break;
                    }
                    case 64: {
                        this.xsFacets.maxExclusiveAnnotation = object3;
                        break;
                    }
                    case 32: {
                        this.xsFacets.maxInclusiveAnnotation = object3;
                        break;
                    }
                    case 128: {
                        this.xsFacets.minExclusiveAnnotation = object3;
                        break;
                    }
                    case 256: {
                        this.xsFacets.minInclusiveAnnotation = object3;
                        break;
                    }
                    case 512: {
                        this.xsFacets.totalDigitsAnnotation = object3;
                        break;
                    }
                    case 1024: {
                        this.xsFacets.fractionDigitsAnnotation = object3;
                        break;
                    }
                    case 16: {
                        this.xsFacets.whiteSpaceAnnotation = object3;
                        break;
                    }
                    case 1: {
                        this.xsFacets.lengthAnnotation = object3;
                    }
                }
                if (object2 != null) {
                    this.reportSchemaError("s4s-elt-must-match.1", new Object[]{string, "(annotation?)", DOMUtil.getLocalName((Node)object2)}, (Element)object2);
                }
            }
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            element = DOMUtil.getNextSiblingElement(element);
        }
        if (vector != null) {
            s2 = (short)(s2 | 2048);
            this.xsFacets.enumeration = vector;
            this.xsFacets.enumNSDecls = vector2;
            this.xsFacets.enumAnnotations = xSObjectListImpl;
        }
        if (this.fPattern.length() != 0) {
            s2 = (short)(s2 | 8);
            this.xsFacets.pattern = this.fPattern.toString();
            this.xsFacets.patternAnnotations = xSObjectListImpl2;
        }
        this.fPattern.setLength(0);
        return new FacetInfo(this.xsFacets, element, s2, s3);
    }

    private boolean containsQName(XSSimpleType xSSimpleType) {
        if (xSSimpleType.getVariety() == 1) {
            short s2 = xSSimpleType.getPrimitiveKind();
            return s2 == 18 || s2 == 20;
        }
        if (xSSimpleType.getVariety() == 2) {
            return this.containsQName((XSSimpleType)xSSimpleType.getItemType());
        }
        if (xSSimpleType.getVariety() == 3) {
            XSObjectList xSObjectList = xSSimpleType.getMemberTypes();
            int n2 = 0;
            while (n2 < xSObjectList.getLength()) {
                if (this.containsQName((XSSimpleType)xSObjectList.item(n2))) {
                    return true;
                }
                ++n2;
            }
        }
        return false;
    }

    Element traverseAttrsAndAttrGrps(Element element, XSAttributeGroupDecl xSAttributeGroupDecl, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, XSComplexTypeDecl xSComplexTypeDecl) {
        Object object;
        Object object2;
        String string;
        String string2;
        Element element2 = null;
        XSAttributeGroupDecl xSAttributeGroupDecl2 = null;
        XSAttributeUseImpl xSAttributeUseImpl = null;
        XSAttributeUse xSAttributeUse = null;
        element2 = element;
        while (element2 != null) {
            string2 = DOMUtil.getLocalName(element2);
            if (string2.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                xSAttributeUseImpl = this.fSchemaHandler.fAttributeTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar, xSComplexTypeDecl);
                if (xSAttributeUseImpl != null) {
                    if (xSAttributeUseImpl.fUse == 2) {
                        xSAttributeGroupDecl.addAttributeUse(xSAttributeUseImpl);
                    } else {
                        xSAttributeUse = xSAttributeGroupDecl.getAttributeUseNoProhibited(xSAttributeUseImpl.fAttrDecl.getNamespace(), xSAttributeUseImpl.fAttrDecl.getName());
                        if (xSAttributeUse == null) {
                            object2 = xSAttributeGroupDecl.addAttributeUse(xSAttributeUseImpl);
                            if (object2 != null) {
                                object = xSComplexTypeDecl == null ? "ag-props-correct.3" : "ct-props-correct.5";
                                string = xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName();
                                this.reportSchemaError((String)object, new Object[]{string, xSAttributeUseImpl.fAttrDecl.getName(), object2}, element2);
                            }
                        } else if (xSAttributeUse != xSAttributeUseImpl) {
                            object2 = xSComplexTypeDecl == null ? "ag-props-correct.2" : "ct-props-correct.4";
                            object = xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName();
                            this.reportSchemaError((String)object2, new Object[]{object, xSAttributeUseImpl.fAttrDecl.getName()}, element2);
                        }
                    }
                }
            } else {
                if (!string2.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) break;
                xSAttributeGroupDecl2 = this.fSchemaHandler.fAttributeGroupTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar);
                if (xSAttributeGroupDecl2 != null) {
                    String string3;
                    String string4;
                    object2 = xSAttributeGroupDecl2.getAttributeUses();
                    int n2 = object2.getLength();
                    int n3 = 0;
                    while (n3 < n2) {
                        object = (XSAttributeUseImpl)object2.item(n3);
                        if (object.fUse == 2) {
                            xSAttributeGroupDecl.addAttributeUse((XSAttributeUseImpl)object);
                        } else {
                            xSAttributeUse = xSAttributeGroupDecl.getAttributeUseNoProhibited(object.fAttrDecl.getNamespace(), object.fAttrDecl.getName());
                            if (xSAttributeUse == null) {
                                string3 = xSAttributeGroupDecl.addAttributeUse((XSAttributeUseImpl)object);
                                if (string3 != null) {
                                    string4 = xSComplexTypeDecl == null ? "ag-props-correct.3" : "ct-props-correct.5";
                                    String string5 = xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName();
                                    this.reportSchemaError(string4, new Object[]{string5, object.fAttrDecl.getName(), string3}, element2);
                                }
                            } else if (object != xSAttributeUse) {
                                string3 = xSComplexTypeDecl == null ? "ag-props-correct.2" : "ct-props-correct.4";
                                string4 = xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName();
                                this.reportSchemaError(string3, new Object[]{string4, object.fAttrDecl.getName()}, element2);
                            }
                        }
                        ++n3;
                    }
                    if (xSAttributeGroupDecl2.fAttributeWC != null) {
                        if (xSAttributeGroupDecl.fAttributeWC == null) {
                            xSAttributeGroupDecl.fAttributeWC = xSAttributeGroupDecl2.fAttributeWC;
                        } else {
                            xSAttributeGroupDecl.fAttributeWC = xSAttributeGroupDecl.fAttributeWC.performIntersectionWith(xSAttributeGroupDecl2.fAttributeWC, xSAttributeGroupDecl.fAttributeWC.fProcessContents);
                            if (xSAttributeGroupDecl.fAttributeWC == null) {
                                string3 = xSComplexTypeDecl == null ? "src-attribute_group.2" : "src-ct.4";
                                string4 = xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName();
                                this.reportSchemaError(string3, new Object[]{string4}, element2);
                            }
                        }
                    }
                }
            }
            element2 = DOMUtil.getNextSiblingElement(element2);
        }
        if (element2 != null && (string2 = DOMUtil.getLocalName(element2)).equals(SchemaSymbols.ELT_ANYATTRIBUTE)) {
            object2 = this.fSchemaHandler.fWildCardTraverser.traverseAnyAttribute(element2, xSDocumentInfo, schemaGrammar);
            if (xSAttributeGroupDecl.fAttributeWC == null) {
                xSAttributeGroupDecl.fAttributeWC = object2;
            } else {
                xSAttributeGroupDecl.fAttributeWC = object2.performIntersectionWith(xSAttributeGroupDecl.fAttributeWC, object2.fProcessContents);
                if (xSAttributeGroupDecl.fAttributeWC == null) {
                    object = xSComplexTypeDecl == null ? "src-attribute_group.2" : "src-ct.4";
                    string = xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName();
                    this.reportSchemaError((String)object, new Object[]{string}, element2);
                }
            }
            element2 = DOMUtil.getNextSiblingElement(element2);
        }
        return element2;
    }

    void reportSchemaError(String string, Object[] arrobject, Element element) {
        this.fSchemaHandler.reportSchemaError(string, arrobject, element);
    }

    void checkNotationType(String string, XSTypeDefinition xSTypeDefinition, Element element) {
        if (xSTypeDefinition.getTypeCategory() == 16 && ((XSSimpleType)xSTypeDefinition).getVariety() == 1 && ((XSSimpleType)xSTypeDefinition).getPrimitiveKind() == 20 && (((XSSimpleType)xSTypeDefinition).getDefinedFacets() & 2048) == 0) {
            this.reportSchemaError("enumeration-required-notation", new Object[]{xSTypeDefinition.getName(), string, DOMUtil.getLocalName(element)}, element);
        }
    }

    protected XSParticleDecl checkOccurrences(XSParticleDecl xSParticleDecl, String string, Element element, int n2, long l2) {
        boolean bl;
        int n3 = xSParticleDecl.fMinOccurs;
        int n4 = xSParticleDecl.fMaxOccurs;
        boolean bl2 = (l2 & (long)(1 << XSAttributeChecker.ATTIDX_MINOCCURS)) != 0;
        boolean bl3 = (l2 & (long)(1 << XSAttributeChecker.ATTIDX_MAXOCCURS)) != 0;
        boolean bl4 = (n2 & 1) != 0;
        boolean bl5 = (n2 & 8) != 0;
        boolean bl6 = (n2 & 2) != 0;
        boolean bl7 = bl = (n2 & 4) != 0;
        if (bl) {
            Object[] arrobject;
            if (!bl2) {
                arrobject = new Object[]{string, "minOccurs"};
                this.reportSchemaError("s4s-att-not-allowed", arrobject, element);
                n3 = 1;
            }
            if (!bl3) {
                arrobject = new Object[]{string, "maxOccurs"};
                this.reportSchemaError("s4s-att-not-allowed", arrobject, element);
                n4 = 1;
            }
        }
        if (n3 == 0 && n4 == 0) {
            xSParticleDecl.fType = 0;
            return null;
        }
        if (bl4) {
            if (n4 != 1) {
                Object[] arrobject = new Object[2];
                arrobject[0] = n4 == -1 ? "unbounded" : Integer.toString(n4);
                arrobject[1] = ((XSElementDecl)xSParticleDecl.fValue).getName();
                this.reportSchemaError("cos-all-limited.2", arrobject, element);
                n4 = 1;
                if (n3 > 1) {
                    n3 = 1;
                }
            }
        } else if ((bl5 || bl6) && n4 != 1) {
            this.reportSchemaError("cos-all-limited.1.2", null, element);
            if (n3 > 1) {
                n3 = 1;
            }
            n4 = 1;
        }
        xSParticleDecl.fMinOccurs = n3;
        xSParticleDecl.fMaxOccurs = n4;
        return xSParticleDecl;
    }

    private static String processAttValue(String string) {
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (c2 == '\"' || c2 == '<' || c2 == '&' || c2 == '\t' || c2 == '\n' || c2 == '\r') {
                return XSDAbstractTraverser.escapeAttValue(string, n3);
            }
            ++n3;
        }
        return string;
    }

    private static String escapeAttValue(String string, int n2) {
        int n3 = string.length();
        StringBuffer stringBuffer = new StringBuffer(n3);
        stringBuffer.append(string.substring(0, n2));
        int n4 = n2;
        while (n4 < n3) {
            char c2 = string.charAt(n4);
            if (c2 == '\"') {
                stringBuffer.append("&quot;");
            } else if (c2 == '<') {
                stringBuffer.append("&lt;");
            } else if (c2 == '&') {
                stringBuffer.append("&amp;");
            } else if (c2 == '\t') {
                stringBuffer.append("&#x9;");
            } else if (c2 == '\n') {
                stringBuffer.append("&#xA;");
            } else if (c2 == '\r') {
                stringBuffer.append("&#xD;");
            } else {
                stringBuffer.append(c2);
            }
            ++n4;
        }
        return stringBuffer.toString();
    }

    static final class FacetInfo {
        final XSFacets facetdata;
        final Element nodeAfterFacets;
        final short fPresentFacets;
        final short fFixedFacets;

        FacetInfo(XSFacets xSFacets, Element element, short s2, short s3) {
            this.facetdata = xSFacets;
            this.nodeAfterFacets = element;
            this.fPresentFacets = s2;
            this.fFixedFacets = s3;
        }
    }

}

