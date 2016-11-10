/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAbstractTraverser;
import org.apache.xerces.impl.xs.traversers.XSDElementTraverser;
import org.apache.xerces.impl.xs.traversers.XSDGroupTraverser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDWildcardTraverser;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTerm;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

abstract class XSDAbstractParticleTraverser
extends XSDAbstractTraverser {
    ParticleArray fPArray = new ParticleArray();

    XSDAbstractParticleTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    XSParticleDecl traverseAll(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int n2, XSObject xSObject) {
        XSObjectListImpl xSObjectListImpl;
        String string;
        Object[] arrobject;
        XSParticleDecl xSParticleDecl;
        Object[] arrobject2 = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        Element element2 = DOMUtil.getFirstChildElement(element);
        XSAnnotationImpl xSAnnotationImpl = null;
        if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
            xSAnnotationImpl = this.traverseAnnotationDecl(element2, arrobject2, false, xSDocumentInfo);
            element2 = DOMUtil.getNextSiblingElement(element2);
        } else {
            string = DOMUtil.getSyntheticAnnotation(element);
            if (string != null) {
                xSAnnotationImpl = this.traverseSyntheticAnnotation(element, string, arrobject2, false, xSDocumentInfo);
            }
        }
        string = null;
        this.fPArray.pushContext();
        while (element2 != null) {
            xSParticleDecl = null;
            string = DOMUtil.getLocalName(element2);
            if (string.equals(SchemaSymbols.ELT_ELEMENT)) {
                xSParticleDecl = this.fSchemaHandler.fElementTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar, 1, xSObject);
            } else {
                arrobject = new Object[]{"all", "(annotation?, element*)", DOMUtil.getLocalName(element2)};
                this.reportSchemaError("s4s-elt-must-match.1", arrobject, element2);
            }
            if (xSParticleDecl != null) {
                this.fPArray.addParticle(xSParticleDecl);
            }
            element2 = DOMUtil.getNextSiblingElement(element2);
        }
        xSParticleDecl = null;
        arrobject = (Object[])arrobject2[XSAttributeChecker.ATTIDX_MINOCCURS];
        XInt xInt = (XInt)arrobject2[XSAttributeChecker.ATTIDX_MAXOCCURS];
        Long l2 = (Long)arrobject2[XSAttributeChecker.ATTIDX_FROMDEFAULT];
        XSModelGroupImpl xSModelGroupImpl = new XSModelGroupImpl();
        xSModelGroupImpl.fCompositor = 103;
        xSModelGroupImpl.fParticleCount = this.fPArray.getParticleCount();
        xSModelGroupImpl.fParticles = this.fPArray.popContext();
        if (xSAnnotationImpl != null) {
            xSObjectListImpl = new XSObjectListImpl();
            xSObjectListImpl.addXSObject(xSAnnotationImpl);
        } else {
            xSObjectListImpl = XSObjectListImpl.EMPTY_LIST;
        }
        xSModelGroupImpl.fAnnotations = xSObjectListImpl;
        xSParticleDecl = new XSParticleDecl();
        xSParticleDecl.fType = 3;
        xSParticleDecl.fMinOccurs = arrobject.intValue();
        xSParticleDecl.fMaxOccurs = xInt.intValue();
        xSParticleDecl.fValue = xSModelGroupImpl;
        xSParticleDecl.fAnnotations = xSObjectListImpl;
        xSParticleDecl = this.checkOccurrences(xSParticleDecl, SchemaSymbols.ELT_ALL, (Element)element.getParentNode(), n2, l2);
        this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
        return xSParticleDecl;
    }

    XSParticleDecl traverseSequence(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int n2, XSObject xSObject) {
        return this.traverseSeqChoice(element, xSDocumentInfo, schemaGrammar, n2, false, xSObject);
    }

    XSParticleDecl traverseChoice(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int n2, XSObject xSObject) {
        return this.traverseSeqChoice(element, xSDocumentInfo, schemaGrammar, n2, true, xSObject);
    }

    private XSParticleDecl traverseSeqChoice(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int n2, boolean bl, XSObject xSObject) {
        Object[] arrobject;
        XSParticleDecl xSParticleDecl;
        String string;
        XSObjectListImpl xSObjectListImpl;
        Object[] arrobject2 = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        Element element2 = DOMUtil.getFirstChildElement(element);
        XSAnnotationImpl xSAnnotationImpl = null;
        if (element2 != null && DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANNOTATION)) {
            xSAnnotationImpl = this.traverseAnnotationDecl(element2, arrobject2, false, xSDocumentInfo);
            element2 = DOMUtil.getNextSiblingElement(element2);
        } else {
            string = DOMUtil.getSyntheticAnnotation(element);
            if (string != null) {
                xSAnnotationImpl = this.traverseSyntheticAnnotation(element, string, arrobject2, false, xSDocumentInfo);
            }
        }
        string = null;
        this.fPArray.pushContext();
        while (element2 != null) {
            xSParticleDecl = null;
            string = DOMUtil.getLocalName(element2);
            if (string.equals(SchemaSymbols.ELT_ELEMENT)) {
                xSParticleDecl = this.fSchemaHandler.fElementTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar, 0, xSObject);
            } else if (string.equals(SchemaSymbols.ELT_GROUP)) {
                xSParticleDecl = this.fSchemaHandler.fGroupTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar);
                if (this.hasAllContent(xSParticleDecl)) {
                    xSParticleDecl = null;
                    this.reportSchemaError("cos-all-limited.1.2", null, element2);
                }
            } else if (string.equals(SchemaSymbols.ELT_CHOICE)) {
                xSParticleDecl = this.traverseChoice(element2, xSDocumentInfo, schemaGrammar, 0, xSObject);
            } else if (string.equals(SchemaSymbols.ELT_SEQUENCE)) {
                xSParticleDecl = this.traverseSequence(element2, xSDocumentInfo, schemaGrammar, 0, xSObject);
            } else if (string.equals(SchemaSymbols.ELT_ANY)) {
                xSParticleDecl = this.fSchemaHandler.fWildCardTraverser.traverseAny(element2, xSDocumentInfo, schemaGrammar);
            } else {
                arrobject = bl ? new Object[]{"choice", "(annotation?, (element | group | choice | sequence | any)*)", DOMUtil.getLocalName(element2)} : new Object[]{"sequence", "(annotation?, (element | group | choice | sequence | any)*)", DOMUtil.getLocalName(element2)};
                this.reportSchemaError("s4s-elt-must-match.1", arrobject, element2);
            }
            if (xSParticleDecl != null) {
                this.fPArray.addParticle(xSParticleDecl);
            }
            element2 = DOMUtil.getNextSiblingElement(element2);
        }
        xSParticleDecl = null;
        arrobject = (Object[])arrobject2[XSAttributeChecker.ATTIDX_MINOCCURS];
        XInt xInt = (XInt)arrobject2[XSAttributeChecker.ATTIDX_MAXOCCURS];
        Long l2 = (Long)arrobject2[XSAttributeChecker.ATTIDX_FROMDEFAULT];
        XSModelGroupImpl xSModelGroupImpl = new XSModelGroupImpl();
        xSModelGroupImpl.fCompositor = bl ? 101 : 102;
        xSModelGroupImpl.fParticleCount = this.fPArray.getParticleCount();
        xSModelGroupImpl.fParticles = this.fPArray.popContext();
        if (xSAnnotationImpl != null) {
            xSObjectListImpl = new XSObjectListImpl();
            xSObjectListImpl.addXSObject(xSAnnotationImpl);
        } else {
            xSObjectListImpl = XSObjectListImpl.EMPTY_LIST;
        }
        xSModelGroupImpl.fAnnotations = xSObjectListImpl;
        xSParticleDecl = new XSParticleDecl();
        xSParticleDecl.fType = 3;
        xSParticleDecl.fMinOccurs = arrobject.intValue();
        xSParticleDecl.fMaxOccurs = xInt.intValue();
        xSParticleDecl.fValue = xSModelGroupImpl;
        xSParticleDecl.fAnnotations = xSObjectListImpl;
        xSParticleDecl = this.checkOccurrences(xSParticleDecl, bl ? SchemaSymbols.ELT_CHOICE : SchemaSymbols.ELT_SEQUENCE, (Element)element.getParentNode(), n2, l2);
        this.fAttrChecker.returnAttrArray(arrobject2, xSDocumentInfo);
        return xSParticleDecl;
    }

    protected boolean hasAllContent(XSParticleDecl xSParticleDecl) {
        if (xSParticleDecl != null && xSParticleDecl.fType == 3) {
            return ((XSModelGroupImpl)xSParticleDecl.fValue).fCompositor == 103;
        }
        return false;
    }

    protected static class ParticleArray {
        XSParticleDecl[] fParticles = new XSParticleDecl[10];
        int[] fPos = new int[5];
        int fContextCount = 0;

        protected ParticleArray() {
        }

        void pushContext() {
            ++this.fContextCount;
            if (this.fContextCount == this.fPos.length) {
                int n2 = this.fContextCount * 2;
                int[] arrn = new int[n2];
                System.arraycopy(this.fPos, 0, arrn, 0, this.fContextCount);
                this.fPos = arrn;
            }
            this.fPos[this.fContextCount] = this.fPos[this.fContextCount - 1];
        }

        int getParticleCount() {
            return this.fPos[this.fContextCount] - this.fPos[this.fContextCount - 1];
        }

        void addParticle(XSParticleDecl xSParticleDecl) {
            if (this.fPos[this.fContextCount] == this.fParticles.length) {
                int n2 = this.fPos[this.fContextCount] * 2;
                XSParticleDecl[] arrxSParticleDecl = new XSParticleDecl[n2];
                System.arraycopy(this.fParticles, 0, arrxSParticleDecl, 0, this.fPos[this.fContextCount]);
                this.fParticles = arrxSParticleDecl;
            }
            int[] arrn = this.fPos;
            int n3 = this.fContextCount;
            int n4 = arrn[n3];
            arrn[n3] = n4 + 1;
            this.fParticles[n4] = xSParticleDecl;
        }

        XSParticleDecl[] popContext() {
            int n2 = this.fPos[this.fContextCount] - this.fPos[this.fContextCount - 1];
            XSParticleDecl[] arrxSParticleDecl = null;
            if (n2 != 0) {
                arrxSParticleDecl = new XSParticleDecl[n2];
                System.arraycopy(this.fParticles, this.fPos[this.fContextCount - 1], arrxSParticleDecl, 0, n2);
                int n3 = this.fPos[this.fContextCount - 1];
                while (n3 < this.fPos[this.fContextCount]) {
                    this.fParticles[n3] = null;
                    ++n3;
                }
            }
            --this.fContextCount;
            return arrxSParticleDecl;
        }
    }

}

