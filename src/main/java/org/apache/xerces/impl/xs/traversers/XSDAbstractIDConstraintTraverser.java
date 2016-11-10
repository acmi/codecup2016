/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xpath.XPathException;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.identity.Field;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.identity.Selector;
import org.apache.xerces.impl.xs.traversers.XSAttributeChecker;
import org.apache.xerces.impl.xs.traversers.XSDAbstractTraverser;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.impl.xs.traversers.XSDocumentInfo;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.NamespaceContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class XSDAbstractIDConstraintTraverser
extends XSDAbstractTraverser {
    public XSDAbstractIDConstraintTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    boolean traverseIdentityConstraint(IdentityConstraint var1_1, Element var2_2, XSDocumentInfo var3_3, Object[] var4_4) {
        var5_5 = DOMUtil.getFirstChildElement(var2_2);
        if (var5_5 == null) {
            this.reportSchemaError("s4s-elt-must-match.2", new Object[]{"identity constraint", "(annotation?, selector, field+)"}, var2_2);
            return false;
        }
        if (DOMUtil.getLocalName(var5_5).equals(SchemaSymbols.ELT_ANNOTATION)) {
            var1_1.addAnnotation(this.traverseAnnotationDecl(var5_5, var4_4, false, var3_3));
            var5_5 = DOMUtil.getNextSiblingElement(var5_5);
            if (var5_5 == null) {
                this.reportSchemaError("s4s-elt-must-match.2", new Object[]{"identity constraint", "(annotation?, selector, field+)"}, var2_2);
                return false;
            }
        } else {
            var6_6 = DOMUtil.getSyntheticAnnotation(var2_2);
            if (var6_6 != null) {
                var1_1.addAnnotation(this.traverseSyntheticAnnotation(var2_2, (String)var6_6, var4_4, false, var3_3));
            }
        }
        if (!DOMUtil.getLocalName(var5_5).equals(SchemaSymbols.ELT_SELECTOR)) {
            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"identity constraint", "(annotation?, selector, field+)", SchemaSymbols.ELT_SELECTOR}, var5_5);
            return false;
        }
        var6_6 = this.fAttrChecker.checkAttributes(var5_5, false, var3_3);
        var7_7 = DOMUtil.getFirstChildElement(var5_5);
        if (var7_7 != null) {
            if (DOMUtil.getLocalName(var7_7).equals(SchemaSymbols.ELT_ANNOTATION)) {
                var1_1.addAnnotation(this.traverseAnnotationDecl(var7_7, var6_6, false, var3_3));
                var7_7 = DOMUtil.getNextSiblingElement(var7_7);
            } else {
                this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SELECTOR, "(annotation?)", DOMUtil.getLocalName(var7_7)}, var7_7);
            }
            if (var7_7 != null) {
                this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SELECTOR, "(annotation?)", DOMUtil.getLocalName(var7_7)}, var7_7);
            }
        } else {
            var8_8 = DOMUtil.getSyntheticAnnotation(var5_5);
            if (var8_8 != null) {
                var1_1.addAnnotation(this.traverseSyntheticAnnotation(var2_2, var8_8, var6_6, false, var3_3));
            }
        }
        if ((var8_8 = (String)var6_6[XSAttributeChecker.ATTIDX_XPATH]) == null) {
            this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_SELECTOR, SchemaSymbols.ATT_XPATH}, var5_5);
            return false;
        }
        var8_8 = XMLChar.trim(var8_8);
        var9_9 = null;
        try {
            var9_9 = new Selector.XPath(var8_8, this.fSymbolTable, var3_3.fNamespaceSupport);
            var10_10 = new Selector(var9_9, var1_1);
            var1_1.setSelector((Selector)var10_10);
        }
        catch (XPathException var10_11) {
            this.reportSchemaError(var10_11.getKey(), new Object[]{var8_8}, var5_5);
            this.fAttrChecker.returnAttrArray(var6_6, var3_3);
            return false;
        }
        this.fAttrChecker.returnAttrArray(var6_6, var3_3);
        var10_10 = DOMUtil.getNextSiblingElement(var5_5);
        if (var10_10 != null) ** GOTO lbl83
        this.reportSchemaError("s4s-elt-must-match.2", new Object[]{"identity constraint", "(annotation?, selector, field+)"}, var5_5);
        return false;
lbl-1000: // 1 sources:
        {
            if (!DOMUtil.getLocalName((Node)var10_10).equals(SchemaSymbols.ELT_FIELD)) {
                this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"identity constraint", "(annotation?, selector, field+)", SchemaSymbols.ELT_FIELD}, (Element)var10_10);
                var10_10 = DOMUtil.getNextSiblingElement((Node)var10_10);
                continue;
            }
            var6_6 = this.fAttrChecker.checkAttributes((Element)var10_10, false, var3_3);
            var11_12 = DOMUtil.getFirstChildElement((Node)var10_10);
            if (var11_12 != null && DOMUtil.getLocalName(var11_12).equals(SchemaSymbols.ELT_ANNOTATION)) {
                var1_1.addAnnotation(this.traverseAnnotationDecl(var11_12, var6_6, false, var3_3));
                var11_12 = DOMUtil.getNextSiblingElement(var11_12);
            }
            if (var11_12 != null) {
                this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_FIELD, "(annotation?)", DOMUtil.getLocalName(var11_12)}, var11_12);
            } else {
                var12_13 = DOMUtil.getSyntheticAnnotation((Node)var10_10);
                if (var12_13 != null) {
                    var1_1.addAnnotation(this.traverseSyntheticAnnotation(var2_2, var12_13, var6_6, false, var3_3));
                }
            }
            var12_13 = (String)var6_6[XSAttributeChecker.ATTIDX_XPATH];
            if (var12_13 == null) {
                this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_FIELD, SchemaSymbols.ATT_XPATH}, (Element)var10_10);
                this.fAttrChecker.returnAttrArray(var6_6, var3_3);
                return false;
            }
            var12_13 = XMLChar.trim(var12_13);
            try {
                var13_14 = new Field.XPath(var12_13, this.fSymbolTable, var3_3.fNamespaceSupport);
                var14_16 = new Field(var13_14, var1_1);
                var1_1.addField(var14_16);
            }
            catch (XPathException var13_15) {
                this.reportSchemaError(var13_15.getKey(), new Object[]{var12_13}, (Element)var10_10);
                this.fAttrChecker.returnAttrArray(var6_6, var3_3);
                return false;
            }
            var10_10 = DOMUtil.getNextSiblingElement((Node)var10_10);
            this.fAttrChecker.returnAttrArray(var6_6, var3_3);
lbl83: // 3 sources:
            ** while (var10_10 != null)
        }
lbl84: // 1 sources:
        if (var1_1.getFieldCount() <= 0) return false;
        return true;
    }
}

