/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAbstractTraverser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class XSDAttributeGroupTraverser
extends XSDAbstractTraverser {
    XSDAttributeGroupTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    XSAttributeGroupDecl traverseLocal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        QName qName = (QName)arrobject[XSAttributeChecker.ATTIDX_REF];
        XSAttributeGroupDecl xSAttributeGroupDecl = null;
        if (qName == null) {
            this.reportSchemaError("s4s-att-must-appear", new Object[]{"attributeGroup (local)", "ref"}, element);
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            return null;
        }
        xSAttributeGroupDecl = (XSAttributeGroupDecl)this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 2, qName, element);
        Element element2 = DOMUtil.getFirstChildElement(element);
        if (element2 != null) {
            Object[] arrobject2;
            String string = DOMUtil.getLocalName(element2);
            if (string.equals(SchemaSymbols.ELT_ANNOTATION)) {
                this.traverseAnnotationDecl(element2, arrobject, false, xSDocumentInfo);
                element2 = DOMUtil.getNextSiblingElement(element2);
            } else {
                arrobject2 = DOMUtil.getSyntheticAnnotation(element2);
                if (arrobject2 != null) {
                    this.traverseSyntheticAnnotation(element2, (String)arrobject2, arrobject, false, xSDocumentInfo);
                }
            }
            if (element2 != null) {
                arrobject2 = new Object[]{qName.rawname, "(annotation?)", DOMUtil.getLocalName(element2)};
                this.reportSchemaError("s4s-elt-must-match.1", arrobject2, element2);
            }
        }
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return xSAttributeGroupDecl;
    }

    XSAttributeGroupDecl traverseGlobal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object object;
        XSAttributeGroupDecl xSAttributeGroupDecl;
        Object object2;
        String string;
        Object object3;
        XSAttributeGroupDecl xSAttributeGroupDecl2 = new XSAttributeGroupDecl();
        Object[] arrobject = this.fAttrChecker.checkAttributes(element, true, xSDocumentInfo);
        String string2 = (String)arrobject[XSAttributeChecker.ATTIDX_NAME];
        if (string2 == null) {
            this.reportSchemaError("s4s-att-must-appear", new Object[]{"attributeGroup (global)", "name"}, element);
            string2 = "(no name)";
        }
        xSAttributeGroupDecl2.fName = string2;
        xSAttributeGroupDecl2.fTargetNamespace = xSDocumentInfo.fTargetNamespace;
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
        object2 = this.traverseAttrsAndAttrGrps(element2, xSAttributeGroupDecl2, xSDocumentInfo, schemaGrammar, null);
        if (object2 != null) {
            object = new Object[]{string2, "(annotation?, ((attribute | attributeGroup)*, anyAttribute?))", DOMUtil.getLocalName((Node)object2)};
            this.reportSchemaError("s4s-elt-must-match.1", (Object[])object, (Element)object2);
        }
        if (string2.equals("(no name)")) {
            this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
            return null;
        }
        xSAttributeGroupDecl2.removeProhibitedAttrs();
        object = (XSAttributeGroupDecl)this.fSchemaHandler.getGrpOrAttrGrpRedefinedByRestriction(2, new QName(XMLSymbols.EMPTY_STRING, string2, string2, xSDocumentInfo.fTargetNamespace), xSDocumentInfo, element);
        if (object != null && (object3 = xSAttributeGroupDecl2.validRestrictionOf(string2, (XSAttributeGroupDecl)object)) != null) {
            this.reportSchemaError((String)object3[object3.length - 1], (Object[])object3, element2);
            this.reportSchemaError("src-redefine.7.2.2", new Object[]{string2, object3[object3.length - 1]}, element2);
        }
        if (xSAnnotationImpl != null) {
            object3 = new XSObjectListImpl();
            ((XSObjectListImpl)object3).addXSObject(xSAnnotationImpl);
        } else {
            object3 = XSObjectListImpl.EMPTY_LIST;
        }
        xSAttributeGroupDecl2.fAnnotations = object3;
        if (schemaGrammar.getGlobalAttributeGroupDecl(xSAttributeGroupDecl2.fName) == null) {
            schemaGrammar.addGlobalAttributeGroupDecl(xSAttributeGroupDecl2);
        }
        if ((xSAttributeGroupDecl = schemaGrammar.getGlobalAttributeGroupDecl(xSAttributeGroupDecl2.fName, string = this.fSchemaHandler.schemaDocument2SystemId(xSDocumentInfo))) == null) {
            schemaGrammar.addGlobalAttributeGroupDecl(xSAttributeGroupDecl2, string);
        }
        if (this.fSchemaHandler.fTolerateDuplicates) {
            if (xSAttributeGroupDecl != null) {
                xSAttributeGroupDecl2 = xSAttributeGroupDecl;
            }
            this.fSchemaHandler.addGlobalAttributeGroupDecl(xSAttributeGroupDecl2);
        }
        this.fAttrChecker.returnAttrArray(arrobject, xSDocumentInfo);
        return xSAttributeGroupDecl2;
    }
}

