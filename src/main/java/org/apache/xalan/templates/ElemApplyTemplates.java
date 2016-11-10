/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemCallTemplate;
import org.apache.xalan.templates.ElemForEach;
import org.apache.xalan.templates.ElemParam;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemWithParam;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.TemplateList;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.StackGuard;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.QName;
import org.apache.xpath.Expression;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class ElemApplyTemplates
extends ElemCallTemplate {
    static final long serialVersionUID = 2903125371542621004L;
    private QName m_mode = null;
    private boolean m_isDefaultTemplate = false;

    public void setMode(QName qName) {
        this.m_mode = qName;
    }

    public QName getMode() {
        return this.m_mode;
    }

    public void setIsDefaultTemplate(boolean bl) {
        this.m_isDefaultTemplate = bl;
    }

    public int getXSLToken() {
        return 50;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
    }

    public String getNodeName() {
        return "apply-templates";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        transformerImpl.pushCurrentTemplateRuleIsNull(false);
        boolean bl = false;
        try {
            QName qName = transformerImpl.getMode();
            if (!this.m_isDefaultTemplate && (null == qName && null != this.m_mode || null != qName && !qName.equals(this.m_mode))) {
                bl = true;
                transformerImpl.pushMode(this.m_mode);
            }
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEvent(this);
            }
            this.transformSelectedNodes(transformerImpl);
        }
        finally {
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
            if (bl) {
                transformerImpl.popMode();
            }
            transformerImpl.popCurrentTemplateRuleIsNull();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void transformSelectedNodes(TransformerImpl var1_1) throws TransformerException {
        block36 : {
            block37 : {
                var2_2 = var1_1.getXPathContext();
                var3_3 = var2_2.getCurrentNode();
                var4_4 = this.m_selectExpression.asIterator(var2_2, var3_3);
                var5_5 = var2_2.getVarStack();
                var6_6 = this.getParamElemCount();
                var7_7 = var5_5.getStackFrame();
                var8_8 = var1_1.getStackGuard();
                var9_9 = var8_8.getRecursionLimit() > -1;
                var10_10 = false;
                try {
                    try {
                        var2_2.pushCurrentNode(-1);
                        var2_2.pushCurrentExpressionNode(-1);
                        var2_2.pushSAXLocatorNull();
                        var1_1.pushElemTemplateElement(null);
                        v0 = var11_11 = this.m_sortElems == null ? null : var1_1.processSortKeys(this, var3_3);
                        if (null != var11_11) {
                            var4_4 = this.sortNodes(var2_2, var11_11, var4_4);
                        }
                        if (var1_1.getDebug()) {
                            var1_1.getTraceManager().fireSelectedEvent(var3_3, this, "select", new XPath(this.m_selectExpression), new XNodeSet(var4_4));
                        }
                        var12_13 = var1_1.getSerializationHandler();
                        var13_14 = var1_1.getStylesheet();
                        var14_15 = var13_14.getTemplateListComposed();
                        var15_16 = var1_1.getQuietConflictWarnings();
                        var16_17 = var2_2.getDTM(var3_3);
                        var17_18 = -1;
                        if (var6_6 > 0) {
                            var17_18 = var5_5.link(var6_6);
                            var5_5.setStackFrame(var7_7);
                            for (var18_19 = 0; var18_19 < var6_6; ++var18_19) {
                                var19_21 = this.m_paramElems[var18_19];
                                if (var1_1.getDebug()) {
                                    var1_1.getTraceManager().fireTraceEvent((ElemTemplateElement)var19_21);
                                }
                                var20_22 = var19_21.getValue(var1_1, var3_3);
                                if (var1_1.getDebug()) {
                                    var1_1.getTraceManager().fireTraceEndEvent((ElemTemplateElement)var19_21);
                                }
                                var5_5.setLocalVariable(var18_19, var20_22, var17_18);
                            }
                            var5_5.setStackFrame(var17_18);
                        }
                        var2_2.pushContextNodeList(var4_4);
                        var10_10 = true;
                        var18_20 = var2_2.getCurrentNodeStack();
                        var19_21 = var2_2.getCurrentExpressionNodeStack();
lbl44: // 3 sources:
                        if (-1 == (var20_23 = var4_4.nextNode())) ** GOTO lbl84
                        var18_20.setTop(var20_23);
                        var19_21.setTop(var20_23);
                        if (var2_2.getDTM(var20_23) != var16_17) {
                            var16_17 = var2_2.getDTM(var20_23);
                        }
                        var21_24 = var16_17.getExpandedTypeID(var20_23);
                        var22_25 = var16_17.getNodeType(var20_23);
                        var23_26 = var1_1.getMode();
                        var24_27 = var14_15.getTemplateFast(var2_2, var20_23, var21_24, var23_26, -1, var15_16, var16_17);
                        if (null != var24_27) ** GOTO lbl71
                        switch (var22_25) {
                            case 1: 
                            case 11: {
                                var24_27 = var13_14.getDefaultRule();
                                ** GOTO lbl72
                            }
                            case 2: 
                            case 3: 
                            case 4: {
                                var1_1.pushPairCurrentMatched(var13_14.getDefaultTextRule(), var20_23);
                                var1_1.setCurrentElement(var13_14.getDefaultTextRule());
                                var16_17.dispatchCharactersEvents(var20_23, var12_13, false);
                                var1_1.popCurrentMatched();
                                ** break;
                            }
                            case 9: {
                                var24_27 = var13_14.getDefaultRootRule();
                                ** GOTO lbl72
                            }
                            default: {
                                ** break;
lbl69: // 2 sources:
                                break;
                            }
                        }
                        ** GOTO lbl44
lbl71: // 1 sources:
                        var1_1.setCurrentElement(var24_27);
lbl72: // 3 sources:
                        var1_1.pushPairCurrentMatched(var24_27, var20_23);
                        if (var9_9) {
                            var8_8.checkForInfinateLoop();
                        }
                        if (var24_27.m_frameSize > 0) {
                            var2_2.pushRTFContext();
                            var25_28 = var5_5.getStackFrame();
                            var5_5.link(var24_27.m_frameSize);
                            if (var24_27.m_inArgsSize <= 0) break block36;
                            var26_30 = 0;
                            break block37;
                        }
                        var25_28 = 0;
                        break block36;
lbl84: // 1 sources:
                        var35_38 = null;
                        if (var1_1.getDebug()) {
                            var1_1.getTraceManager().fireSelectedEndEvent(var3_3, this, "select", new XPath(this.m_selectExpression), new XNodeSet(var4_4));
                        }
                        if (var6_6 > 0) {
                            var5_5.unlink(var7_7);
                        }
                        var2_2.popSAXLocator();
                        if (var10_10) {
                            var2_2.popContextNodeList();
                        }
                        var1_1.popElemTemplateElement();
                        var2_2.popCurrentExpressionNode();
                        var2_2.popCurrentNode();
                        var4_4.detach();
                        return;
                    }
                    catch (SAXException var11_12) {
                        var1_1.getErrorListener().fatalError(new TransformerException(var11_12));
                        var35_39 = null;
                        if (var1_1.getDebug()) {
                            var1_1.getTraceManager().fireSelectedEndEvent(var3_3, this, "select", new XPath(this.m_selectExpression), new XNodeSet(var4_4));
                        }
                        if (var6_6 > 0) {
                            var5_5.unlink(var7_7);
                        }
                        var2_2.popSAXLocator();
                        if (var10_10) {
                            var2_2.popContextNodeList();
                        }
                        var1_1.popElemTemplateElement();
                        var2_2.popCurrentExpressionNode();
                        var2_2.popCurrentNode();
                        var4_4.detach();
                        return;
                    }
                }
                catch (Throwable var34_41) {
                    var35_40 = null;
                    if (var1_1.getDebug()) {
                        var1_1.getTraceManager().fireSelectedEndEvent(var3_3, this, "select", new XPath(this.m_selectExpression), new XNodeSet(var4_4));
                    }
                    if (var6_6 > 0) {
                        var5_5.unlink(var7_7);
                    }
                    var2_2.popSAXLocator();
                    if (var10_10) {
                        var2_2.popContextNodeList();
                    }
                    var1_1.popElemTemplateElement();
                    var2_2.popCurrentExpressionNode();
                    var2_2.popCurrentNode();
                    var4_4.detach();
                    throw var34_41;
                }
            }
            for (var27_31 = var24_27.getFirstChildElem(); null != var27_31 && 41 == var27_31.getXSLToken(); ++var26_30, var27_31 = var27_31.getNextSiblingElem()) {
                var28_32 = (ElemParam)var27_31;
                for (var29_33 = 0; var29_33 < var6_6; ++var29_33) {
                    var30_34 = this.m_paramElems[var29_33];
                    if (var30_34.m_qnameID != var28_32.m_qnameID) continue;
                    var31_35 = var5_5.getLocalVariable(var29_33, var17_18);
                    var5_5.setLocalVariable(var26_30, var31_35);
                    break;
                }
                if (var29_33 != var6_6) continue;
                var5_5.setLocalVariable(var26_30, null);
            }
        }
        if (var1_1.getDebug()) {
            var1_1.getTraceManager().fireTraceEvent(var24_27);
        }
        var26_29 = var24_27.m_firstChild;
        while (var26_29 != null) {
            var2_2.setSAXLocator(var26_29);
            try {
                var1_1.pushElemTemplateElement(var26_29);
                var26_29.execute(var1_1);
                var33_36 = null;
                var1_1.popElemTemplateElement();
            }
            catch (Throwable var32_37) {
                var33_36 = null;
                var1_1.popElemTemplateElement();
                throw var32_37;
            }
            var26_29 = var26_29.m_nextSibling;
        }
        if (var1_1.getDebug()) {
            var1_1.getTraceManager().fireTraceEndEvent(var24_27);
        }
        if (var24_27.m_frameSize > 0) {
            var5_5.unlink(var25_28);
            var2_2.popRTFContext();
        }
        var1_1.popCurrentMatched();
        ** GOTO lbl44
    }
}

