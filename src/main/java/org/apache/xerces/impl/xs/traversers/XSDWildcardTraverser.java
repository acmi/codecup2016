/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAbstractTraverser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTerm;
import org.w3c.dom.Element;

class XSDWildcardTraverser
extends XSDAbstractTraverser {
    XSDWildcardTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    XSParticleDecl traverseAny(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        XSWildcardDecl xSWildcardDecl = this.traverseWildcardDecl(element, arrobject, xSDocumentInfo, schemaGrammar);
        XSParticleDecl xSParticleDecl = null;
        if (xSWildcardDecl != null) {
            int n2 = ((XInt)arrobject[XSAttributeChecker.ATTIDX_MINOCCURS]).intValue();
            int n3 = ((XInt)arrobject[XSAttributeChecker.ATTIDX_MAXOCCURS]).intValue();
            if (n3 != 0) {
                xSParticleDecl = this.fSchemaHandler.fDeclPool != null ? this.fSchemaHandler.fDeclPool.getParticleDecl() : new XSParticleDecl();
                xSParticleDecl.fType = 2;
                xSParticleDecl.fValue = xSWildcardDecl;
                xSParticleDecl.fMinOccurs = n2;
                xSParticleDecl.fMaxOccurs = n3;
                xSParticleDecl.fAnnotations = xSWildcardDecl.fAnnotations;
            }
        }
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return xSParticleDecl;
    }

    XSWildcardDecl traverseAnyAttribute(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        XSWildcardDecl xSWildcardDecl = this.traverseWildcardDecl(element, arrobject, xSDocumentInfo, schemaGrammar);
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return xSWildcardDecl;
    }

    XSWildcardDecl traverseWildcardDecl(Element element, Object[] arrobject, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object object;
        XSWildcardDecl xSWildcardDecl = new XSWildcardDecl();
        XInt xInt = (XInt)arrobject[XSAttributeChecker.ATTIDX_NAMESPACE];
        xSWildcardDecl.fType = xInt.shortValue();
        xSWildcardDecl.fNamespaceList = (String[])arrobject[XSAttributeChecker.ATTIDX_NAMESPACE_LIST];
        XInt xInt2 = (XInt)arrobject[XSAttributeChecker.ATTIDX_PROCESSCONTENTS];
        xSWildcardDecl.fProcessContents = xInt2.shortValue();
        Element element2 = DOMUtil.getFirstChildElement(element);
        XSAnnotationImpl xSAnnotationImpl = null;
        if (element2 != null) {
            if (DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                xSAnnotationImpl = this.traverseAnnotationDecl(element2, arrobject, false, xSDocumentInfo);
                element2 = DOMUtil.getNextSiblingElement(element2);
            } else {
                object = DOMUtil.getSyntheticAnnotation(element);
                if (object != null) {
                    xSAnnotationImpl = this.traverseSyntheticAnnotation(element, (String)object, arrobject, false, xSDocumentInfo);
                }
            }
            if (element2 != null) {
                this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"wildcard", "(annotation?)", DOMUtil.getLocalName(element2)}, element);
            }
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
        xSWildcardDecl.fAnnotations = object;
        return xSWildcardDecl;
    }
}

