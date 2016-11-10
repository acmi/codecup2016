/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.PrintStream;
import java.util.Vector;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.AbsPathChecker;
import org.apache.xalan.templates.ElemForEach;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.ElemVariablePsuedo;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.VarNameCollector;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.AxesWalker;
import org.apache.xpath.axes.FilterExprIteratorSimple;
import org.apache.xpath.axes.FilterExprWalker;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.SelfIteratorNoPredicate;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.axes.WalkingIterator;
import org.apache.xpath.operations.Variable;
import org.apache.xpath.operations.VariableSafeAbsRef;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class RedundentExprEliminator
extends XSLTVisitor {
    Vector m_paths = null;
    Vector m_absPaths = new Vector();
    boolean m_isSameContext = true;
    AbsPathChecker m_absPathChecker = new AbsPathChecker();
    private static int m_uniquePseudoVarID = 1;
    static final String PSUEDOVARNAMESPACE = "http://xml.apache.org/xalan/psuedovar";
    public static final boolean DEBUG = false;
    public static final boolean DIAGNOSE_NUM_PATHS_REDUCED = false;
    public static final boolean DIAGNOSE_MULTISTEPLIST = false;
    VarNameCollector m_varNameCollector = new VarNameCollector();

    public void eleminateRedundentLocals(ElemTemplateElement elemTemplateElement) {
        this.eleminateRedundent(elemTemplateElement, this.m_paths);
    }

    public void eleminateRedundentGlobals(StylesheetRoot stylesheetRoot) {
        this.eleminateRedundent(stylesheetRoot, this.m_absPaths);
    }

    protected void eleminateRedundent(ElemTemplateElement elemTemplateElement, Vector vector) {
        int n2 = vector.size();
        int n3 = 0;
        int n4 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            ExpressionOwner expressionOwner = (ExpressionOwner)vector.elementAt(i2);
            if (null == expressionOwner) continue;
            int n5 = this.findAndEliminateRedundant(i2 + 1, i2, expressionOwner, elemTemplateElement, vector);
            if (n5 > 0) {
                ++n4;
            }
            n3 += n5;
        }
        this.eleminateSharedPartialPaths(elemTemplateElement, vector);
    }

    protected void eleminateSharedPartialPaths(ElemTemplateElement elemTemplateElement, Vector vector) {
        MultistepExprHolder multistepExprHolder = this.createMultistepExprList(vector);
        if (null != multistepExprHolder) {
            boolean bl = vector == this.m_absPaths;
            int n2 = multistepExprHolder.m_stepCount;
            for (int i2 = n2 - 1; i2 >= 1; --i2) {
                MultistepExprHolder multistepExprHolder2 = multistepExprHolder;
                while (null != multistepExprHolder2 && multistepExprHolder2.m_stepCount >= i2) {
                    multistepExprHolder = this.matchAndEliminatePartialPaths(multistepExprHolder2, multistepExprHolder, bl, i2, elemTemplateElement);
                    multistepExprHolder2 = multistepExprHolder2.m_next;
                }
            }
        }
    }

    protected MultistepExprHolder matchAndEliminatePartialPaths(MultistepExprHolder multistepExprHolder, MultistepExprHolder multistepExprHolder2, boolean bl, int n2, ElemTemplateElement elemTemplateElement) {
        if (null == multistepExprHolder.m_exprOwner) {
            return multistepExprHolder2;
        }
        WalkingIterator walkingIterator = (WalkingIterator)multistepExprHolder.m_exprOwner.getExpression();
        if (this.partialIsVariable(multistepExprHolder, n2)) {
            return multistepExprHolder2;
        }
        MultistepExprHolder multistepExprHolder3 = null;
        MultistepExprHolder multistepExprHolder4 = null;
        MultistepExprHolder multistepExprHolder5 = multistepExprHolder2;
        while (null != multistepExprHolder5) {
            WalkingIterator walkingIterator2;
            if (multistepExprHolder5 != multistepExprHolder && null != multistepExprHolder5.m_exprOwner && this.stepsEqual(walkingIterator, walkingIterator2 = (WalkingIterator)multistepExprHolder5.m_exprOwner.getExpression(), n2)) {
                if (null == multistepExprHolder3) {
                    try {
                        multistepExprHolder3 = (MultistepExprHolder)multistepExprHolder.clone();
                        multistepExprHolder.m_exprOwner = null;
                    }
                    catch (CloneNotSupportedException cloneNotSupportedException) {
                        // empty catch block
                    }
                    multistepExprHolder4 = multistepExprHolder3;
                    multistepExprHolder4.m_next = null;
                }
                try {
                    multistepExprHolder4.m_next = (MultistepExprHolder)multistepExprHolder5.clone();
                    multistepExprHolder5.m_exprOwner = null;
                }
                catch (CloneNotSupportedException cloneNotSupportedException) {
                    // empty catch block
                }
                multistepExprHolder4 = multistepExprHolder4.m_next;
                multistepExprHolder4.m_next = null;
            }
            multistepExprHolder5 = multistepExprHolder5.m_next;
        }
        boolean bl2 = false;
        if (null != multistepExprHolder3) {
            ElemTemplateElement elemTemplateElement2 = bl ? elemTemplateElement : this.findCommonAncestor(multistepExprHolder3);
            WalkingIterator walkingIterator3 = (WalkingIterator)multistepExprHolder3.m_exprOwner.getExpression();
            WalkingIterator walkingIterator4 = this.createIteratorFromSteps(walkingIterator3, n2);
            ElemVariable elemVariable = this.createPseudoVarDecl(elemTemplateElement2, walkingIterator4, bl);
            while (null != multistepExprHolder3) {
                ExpressionOwner expressionOwner = multistepExprHolder3.m_exprOwner;
                WalkingIterator walkingIterator5 = (WalkingIterator)expressionOwner.getExpression();
                LocPathIterator locPathIterator = this.changePartToRef(elemVariable.getName(), walkingIterator5, n2, bl);
                expressionOwner.setExpression(locPathIterator);
                multistepExprHolder3 = multistepExprHolder3.m_next;
            }
        }
        return multistepExprHolder2;
    }

    boolean partialIsVariable(MultistepExprHolder multistepExprHolder, int n2) {
        WalkingIterator walkingIterator;
        if (1 == n2 && (walkingIterator = (WalkingIterator)multistepExprHolder.m_exprOwner.getExpression()).getFirstWalker() instanceof FilterExprWalker) {
            return true;
        }
        return false;
    }

    protected void diagnoseLineNumber(Expression expression) {
        ElemTemplateElement elemTemplateElement = this.getElemFromExpression(expression);
        System.err.println("   " + elemTemplateElement.getSystemId() + " Line " + elemTemplateElement.getLineNumber());
    }

    protected ElemTemplateElement findCommonAncestor(MultistepExprHolder multistepExprHolder) {
        int n2;
        int n3;
        int n4 = multistepExprHolder.getLength();
        ElemTemplateElement[] arrelemTemplateElement = new ElemTemplateElement[n4];
        int[] arrn = new int[n4];
        MultistepExprHolder multistepExprHolder2 = multistepExprHolder;
        int n5 = 10000;
        for (n3 = 0; n3 < n4; ++n3) {
            ElemTemplateElement elemTemplateElement;
            arrelemTemplateElement[n3] = elemTemplateElement = this.getElemFromExpression(multistepExprHolder2.m_exprOwner.getExpression());
            arrn[n3] = n2 = this.countAncestors(elemTemplateElement);
            if (n2 < n5) {
                n5 = n2;
            }
            multistepExprHolder2 = multistepExprHolder2.m_next;
        }
        for (n3 = 0; n3 < n4; ++n3) {
            if (arrn[n3] <= n5) continue;
            int n6 = arrn[n3] - n5;
            for (n2 = 0; n2 < n6; ++n2) {
                arrelemTemplateElement[n3] = arrelemTemplateElement[n3].getParentElem();
            }
        }
        ElemTemplateElement elemTemplateElement = null;
        while (n5-- >= 0) {
            boolean bl = true;
            elemTemplateElement = arrelemTemplateElement[0];
            for (n2 = 1; n2 < n4; ++n2) {
                if (elemTemplateElement == arrelemTemplateElement[n2]) continue;
                bl = false;
                break;
            }
            if (bl && this.isNotSameAsOwner(multistepExprHolder, elemTemplateElement) && elemTemplateElement.canAcceptVariables()) {
                return elemTemplateElement;
            }
            for (n2 = 0; n2 < n4; ++n2) {
                arrelemTemplateElement[n2] = arrelemTemplateElement[n2].getParentElem();
            }
        }
        RedundentExprEliminator.assertion(false, "Could not find common ancestor!!!");
        return null;
    }

    protected boolean isNotSameAsOwner(MultistepExprHolder multistepExprHolder, ElemTemplateElement elemTemplateElement) {
        MultistepExprHolder multistepExprHolder2 = multistepExprHolder;
        while (null != multistepExprHolder2) {
            ElemTemplateElement elemTemplateElement2 = this.getElemFromExpression(multistepExprHolder2.m_exprOwner.getExpression());
            if (elemTemplateElement2 == elemTemplateElement) {
                return false;
            }
            multistepExprHolder2 = multistepExprHolder2.m_next;
        }
        return true;
    }

    protected int countAncestors(ElemTemplateElement elemTemplateElement) {
        int n2 = 0;
        while (null != elemTemplateElement) {
            ++n2;
            elemTemplateElement = elemTemplateElement.getParentElem();
        }
        return n2;
    }

    protected void diagnoseMultistepList(int n2, int n3, boolean bl) {
        if (n2 > 0) {
            System.err.print("Found multistep matches: " + n2 + ", " + n3 + " length");
            if (bl) {
                System.err.println(" (global)");
            } else {
                System.err.println();
            }
        }
    }

    protected LocPathIterator changePartToRef(QName qName, WalkingIterator walkingIterator, int n2, boolean bl) {
        AxesWalker axesWalker;
        Variable variable = new Variable();
        variable.setQName(qName);
        variable.setIsGlobal(bl);
        if (bl) {
            axesWalker = this.getElemFromExpression(walkingIterator);
            StylesheetRoot stylesheetRoot = axesWalker.getStylesheetRoot();
            Vector vector = stylesheetRoot.getVariablesAndParamsComposed();
            variable.setIndex(vector.size() - 1);
        }
        axesWalker = walkingIterator.getFirstWalker();
        for (int i2 = 0; i2 < n2; ++i2) {
            RedundentExprEliminator.assertion(null != axesWalker, "Walker should not be null!");
            axesWalker = axesWalker.getNextWalker();
        }
        if (null != axesWalker) {
            FilterExprWalker filterExprWalker = new FilterExprWalker(walkingIterator);
            filterExprWalker.setInnerExpression(variable);
            filterExprWalker.exprSetParent(walkingIterator);
            filterExprWalker.setNextWalker(axesWalker);
            axesWalker.setPrevWalker(filterExprWalker);
            walkingIterator.setFirstWalker(filterExprWalker);
            return walkingIterator;
        }
        FilterExprIteratorSimple filterExprIteratorSimple = new FilterExprIteratorSimple(variable);
        filterExprIteratorSimple.exprSetParent(walkingIterator.exprGetParent());
        return filterExprIteratorSimple;
    }

    protected WalkingIterator createIteratorFromSteps(WalkingIterator walkingIterator, int n2) {
        WalkingIterator walkingIterator2 = new WalkingIterator(walkingIterator.getPrefixResolver());
        try {
            AxesWalker axesWalker = (AxesWalker)walkingIterator.getFirstWalker().clone();
            walkingIterator2.setFirstWalker(axesWalker);
            axesWalker.setLocPathIterator(walkingIterator2);
            for (int i2 = 1; i2 < n2; ++i2) {
                AxesWalker axesWalker2 = (AxesWalker)axesWalker.getNextWalker().clone();
                axesWalker.setNextWalker(axesWalker2);
                axesWalker2.setLocPathIterator(walkingIterator2);
                axesWalker = axesWalker2;
            }
            axesWalker.setNextWalker(null);
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new WrappedRuntimeException(cloneNotSupportedException);
        }
        return walkingIterator2;
    }

    protected boolean stepsEqual(WalkingIterator walkingIterator, WalkingIterator walkingIterator2, int n2) {
        AxesWalker axesWalker = walkingIterator.getFirstWalker();
        AxesWalker axesWalker2 = walkingIterator2.getFirstWalker();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (null == axesWalker || null == axesWalker2) {
                return false;
            }
            if (!axesWalker.deepEquals(axesWalker2)) {
                return false;
            }
            axesWalker = axesWalker.getNextWalker();
            axesWalker2 = axesWalker2.getNextWalker();
        }
        RedundentExprEliminator.assertion(null != axesWalker || null != axesWalker2, "Total match is incorrect!");
        return true;
    }

    protected MultistepExprHolder createMultistepExprList(Vector vector) {
        MultistepExprHolder multistepExprHolder = null;
        int n2 = vector.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            LocPathIterator locPathIterator;
            int n3;
            ExpressionOwner expressionOwner = (ExpressionOwner)vector.elementAt(i2);
            if (null == expressionOwner || (n3 = this.countSteps(locPathIterator = (LocPathIterator)expressionOwner.getExpression())) <= 1) continue;
            multistepExprHolder = null == multistepExprHolder ? new MultistepExprHolder(this, expressionOwner, n3, null) : multistepExprHolder.addInSortedOrder(expressionOwner, n3);
        }
        if (null == multistepExprHolder || multistepExprHolder.getLength() <= 1) {
            return null;
        }
        return multistepExprHolder;
    }

    protected int findAndEliminateRedundant(int n2, int n3, ExpressionOwner expressionOwner, ElemTemplateElement elemTemplateElement, Vector vector) throws DOMException {
        ExpressionNode expressionNode;
        Object object;
        Object object2;
        MultistepExprHolder multistepExprHolder = null;
        MultistepExprHolder multistepExprHolder2 = null;
        int n4 = 0;
        int n5 = vector.size();
        Expression expression = expressionOwner.getExpression();
        boolean bl = vector == this.m_absPaths;
        LocPathIterator locPathIterator = (LocPathIterator)expression;
        int n6 = this.countSteps(locPathIterator);
        for (int i2 = n2; i2 < n5; ++i2) {
            boolean bl2;
            object2 = (ExpressionOwner)vector.elementAt(i2);
            if (null == object2 || !(bl2 = (expressionNode = object2.getExpression()).deepEquals(locPathIterator))) continue;
            object = (LocPathIterator)expressionNode;
            if (null == multistepExprHolder) {
                multistepExprHolder2 = multistepExprHolder = new MultistepExprHolder(this, expressionOwner, n6, null);
                ++n4;
            }
            multistepExprHolder2 = multistepExprHolder2.m_next = new MultistepExprHolder(this, (ExpressionOwner)object2, n6, null);
            vector.setElementAt(null, i2);
            ++n4;
        }
        if (0 == n4 && bl) {
            multistepExprHolder = new MultistepExprHolder(this, expressionOwner, n6, null);
            ++n4;
        }
        if (null != multistepExprHolder) {
            ElemTemplateElement elemTemplateElement2 = bl ? elemTemplateElement : this.findCommonAncestor(multistepExprHolder);
            object2 = (LocPathIterator)multistepExprHolder.m_exprOwner.getExpression();
            expressionNode = this.createPseudoVarDecl(elemTemplateElement2, (LocPathIterator)object2, bl);
            QName qName = expressionNode.getName();
            while (null != multistepExprHolder) {
                object = multistepExprHolder.m_exprOwner;
                this.changeToVarRef(qName, (ExpressionOwner)object, vector, elemTemplateElement2);
                multistepExprHolder = multistepExprHolder.m_next;
            }
            vector.setElementAt(expressionNode.getSelect(), n3);
        }
        return n4;
    }

    protected int oldFindAndEliminateRedundant(int n2, int n3, ExpressionOwner expressionOwner, ElemTemplateElement elemTemplateElement, Vector vector) throws DOMException {
        QName qName = null;
        boolean bl = false;
        int n4 = 0;
        int n5 = vector.size();
        Expression expression = expressionOwner.getExpression();
        boolean bl2 = vector == this.m_absPaths;
        LocPathIterator locPathIterator = (LocPathIterator)expression;
        for (int i2 = n2; i2 < n5; ++i2) {
            Expression expression2;
            boolean bl3;
            ExpressionOwner expressionOwner2 = (ExpressionOwner)vector.elementAt(i2);
            if (null == expressionOwner2 || !(bl3 = (expression2 = expressionOwner2.getExpression()).deepEquals(locPathIterator))) continue;
            LocPathIterator locPathIterator2 = (LocPathIterator)expression2;
            if (!bl) {
                bl = true;
                ElemVariable elemVariable = this.createPseudoVarDecl(elemTemplateElement, locPathIterator, bl2);
                if (null == elemVariable) {
                    return 0;
                }
                qName = elemVariable.getName();
                this.changeToVarRef(qName, expressionOwner, vector, elemTemplateElement);
                vector.setElementAt(elemVariable.getSelect(), n3);
                ++n4;
            }
            this.changeToVarRef(qName, expressionOwner2, vector, elemTemplateElement);
            vector.setElementAt(null, i2);
            ++n4;
        }
        if (0 == n4 && vector == this.m_absPaths) {
            ElemVariable elemVariable = this.createPseudoVarDecl(elemTemplateElement, locPathIterator, true);
            if (null == elemVariable) {
                return 0;
            }
            qName = elemVariable.getName();
            this.changeToVarRef(qName, expressionOwner, vector, elemTemplateElement);
            vector.setElementAt(elemVariable.getSelect(), n3);
            ++n4;
        }
        return n4;
    }

    protected int countSteps(LocPathIterator locPathIterator) {
        if (locPathIterator instanceof WalkingIterator) {
            WalkingIterator walkingIterator = (WalkingIterator)locPathIterator;
            int n2 = 0;
            for (AxesWalker axesWalker = walkingIterator.getFirstWalker(); null != axesWalker; axesWalker = axesWalker.getNextWalker()) {
                ++n2;
            }
            return n2;
        }
        return 1;
    }

    protected void changeToVarRef(QName qName, ExpressionOwner expressionOwner, Vector vector, ElemTemplateElement elemTemplateElement) {
        Variable variable = vector == this.m_absPaths ? new VariableSafeAbsRef() : new Variable();
        variable.setQName(qName);
        if (vector == this.m_absPaths) {
            StylesheetRoot stylesheetRoot = (StylesheetRoot)elemTemplateElement;
            Vector vector2 = stylesheetRoot.getVariablesAndParamsComposed();
            variable.setIndex(vector2.size() - 1);
            variable.setIsGlobal(true);
        }
        expressionOwner.setExpression(variable);
    }

    private static synchronized int getPseudoVarID() {
        return m_uniquePseudoVarID++;
    }

    protected ElemVariable createPseudoVarDecl(ElemTemplateElement elemTemplateElement, LocPathIterator locPathIterator, boolean bl) throws DOMException {
        QName qName = new QName("http://xml.apache.org/xalan/psuedovar", "#" + RedundentExprEliminator.getPseudoVarID());
        if (bl) {
            return this.createGlobalPseudoVarDecl(qName, (StylesheetRoot)elemTemplateElement, locPathIterator);
        }
        return this.createLocalPseudoVarDecl(qName, elemTemplateElement, locPathIterator);
    }

    protected ElemVariable createGlobalPseudoVarDecl(QName qName, StylesheetRoot stylesheetRoot, LocPathIterator locPathIterator) throws DOMException {
        ElemVariable elemVariable = new ElemVariable();
        elemVariable.setIsTopLevel(true);
        XPath xPath = new XPath(locPathIterator);
        elemVariable.setSelect(xPath);
        elemVariable.setName(qName);
        Vector vector = stylesheetRoot.getVariablesAndParamsComposed();
        elemVariable.setIndex(vector.size());
        vector.addElement(elemVariable);
        return elemVariable;
    }

    protected ElemVariable createLocalPseudoVarDecl(QName qName, ElemTemplateElement elemTemplateElement, LocPathIterator locPathIterator) throws DOMException {
        ElemVariablePsuedo elemVariablePsuedo = new ElemVariablePsuedo();
        XPath xPath = new XPath(locPathIterator);
        elemVariablePsuedo.setSelect(xPath);
        elemVariablePsuedo.setName(qName);
        ElemVariable elemVariable = this.addVarDeclToElem(elemTemplateElement, locPathIterator, elemVariablePsuedo);
        locPathIterator.exprSetParent(elemVariable);
        return elemVariable;
    }

    protected ElemVariable addVarDeclToElem(ElemTemplateElement elemTemplateElement, LocPathIterator locPathIterator, ElemVariable elemVariable) throws DOMException {
        ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getFirstChildElem();
        locPathIterator.callVisitors(null, this.m_varNameCollector);
        if (this.m_varNameCollector.getVarCount() > 0) {
            ElemTemplateElement elemTemplateElement3 = this.getElemFromExpression(locPathIterator);
            ElemVariable elemVariable2 = this.getPrevVariableElem(elemTemplateElement3);
            while (null != elemVariable2) {
                if (this.m_varNameCollector.doesOccur(elemVariable2.getName())) {
                    elemTemplateElement = elemVariable2.getParentElem();
                    elemTemplateElement2 = elemVariable2.getNextSiblingElem();
                    break;
                }
                elemVariable2 = this.getPrevVariableElem(elemVariable2);
            }
        }
        if (null != elemTemplateElement2 && 41 == elemTemplateElement2.getXSLToken()) {
            if (this.isParam(locPathIterator)) {
                return null;
            }
            while (null != elemTemplateElement2 && (null == (elemTemplateElement2 = elemTemplateElement2.getNextSiblingElem()) || 41 == elemTemplateElement2.getXSLToken())) {
            }
        }
        elemTemplateElement.insertBefore(elemVariable, elemTemplateElement2);
        this.m_varNameCollector.reset();
        return elemVariable;
    }

    protected boolean isParam(ExpressionNode expressionNode) {
        while (null != expressionNode && !(expressionNode instanceof ElemTemplateElement)) {
            expressionNode = expressionNode.exprGetParent();
        }
        if (null != expressionNode) {
            for (ElemTemplateElement elemTemplateElement = (ElemTemplateElement)expressionNode; null != elemTemplateElement; elemTemplateElement = elemTemplateElement.getParentElem()) {
                int n2 = elemTemplateElement.getXSLToken();
                switch (n2) {
                    case 41: {
                        return true;
                    }
                    case 19: 
                    case 25: {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    protected ElemVariable getPrevVariableElem(ElemTemplateElement elemTemplateElement) {
        while (null != (elemTemplateElement = this.getPrevElementWithinContext(elemTemplateElement))) {
            int n2 = elemTemplateElement.getXSLToken();
            if (73 != n2 && 41 != n2) continue;
            return (ElemVariable)elemTemplateElement;
        }
        return null;
    }

    protected ElemTemplateElement getPrevElementWithinContext(ElemTemplateElement elemTemplateElement) {
        int n2;
        ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getPreviousSiblingElem();
        if (null == elemTemplateElement2) {
            elemTemplateElement2 = elemTemplateElement.getParentElem();
        }
        if (null != elemTemplateElement2 && (28 == (n2 = elemTemplateElement2.getXSLToken()) || 19 == n2 || 25 == n2)) {
            elemTemplateElement2 = null;
        }
        return elemTemplateElement2;
    }

    protected ElemTemplateElement getElemFromExpression(Expression expression) {
        for (ExpressionNode expressionNode = expression.exprGetParent(); null != expressionNode; expressionNode = expressionNode.exprGetParent()) {
            if (!(expressionNode instanceof ElemTemplateElement)) continue;
            return (ElemTemplateElement)expressionNode;
        }
        throw new RuntimeException(XSLMessages.createMessage("ER_ASSERT_NO_TEMPLATE_PARENT", null));
    }

    public boolean isAbsolute(LocPathIterator locPathIterator) {
        boolean bl;
        int n2 = locPathIterator.getAnalysisBits();
        boolean bl2 = bl = WalkerFactory.isSet(n2, 134217728) || WalkerFactory.isSet(n2, 536870912);
        if (bl) {
            bl = this.m_absPathChecker.checkAbsolute(locPathIterator);
        }
        return bl;
    }

    public boolean visitLocationPath(ExpressionOwner expressionOwner, LocPathIterator locPathIterator) {
        FilterExprWalker filterExprWalker;
        AxesWalker axesWalker;
        Expression expression;
        WalkingIterator walkingIterator;
        if (locPathIterator instanceof SelfIteratorNoPredicate) {
            return true;
        }
        if (locPathIterator instanceof WalkingIterator && (axesWalker = (walkingIterator = (WalkingIterator)locPathIterator).getFirstWalker()) instanceof FilterExprWalker && null == axesWalker.getNextWalker() && (expression = (filterExprWalker = (FilterExprWalker)axesWalker).getInnerExpression()) instanceof Variable) {
            return true;
        }
        if (this.isAbsolute(locPathIterator) && null != this.m_absPaths) {
            this.m_absPaths.addElement(expressionOwner);
        } else if (this.m_isSameContext && null != this.m_paths) {
            this.m_paths.addElement(expressionOwner);
        }
        return true;
    }

    public boolean visitPredicate(ExpressionOwner expressionOwner, Expression expression) {
        boolean bl = this.m_isSameContext;
        this.m_isSameContext = false;
        expression.callVisitors(expressionOwner, this);
        this.m_isSameContext = bl;
        return false;
    }

    public boolean visitTopLevelInstruction(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        switch (n2) {
            case 19: {
                return this.visitInstruction(elemTemplateElement);
            }
        }
        return true;
    }

    public boolean visitInstruction(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        switch (n2) {
            case 17: 
            case 19: 
            case 28: {
                ElemForEach elemForEach;
                if (n2 == 28) {
                    elemForEach = (ElemForEach)elemTemplateElement;
                    Expression expression = elemForEach.getSelect();
                    expression.callVisitors(elemForEach, this);
                }
                elemForEach = this.m_paths;
                this.m_paths = new Vector();
                elemTemplateElement.callChildVisitors(this, false);
                this.eleminateRedundentLocals(elemTemplateElement);
                this.m_paths = elemForEach;
                return false;
            }
            case 35: 
            case 64: {
                boolean bl = this.m_isSameContext;
                this.m_isSameContext = false;
                elemTemplateElement.callChildVisitors(this);
                this.m_isSameContext = bl;
                return false;
            }
        }
        return true;
    }

    protected void diagnoseNumPaths(Vector vector, int n2, int n3) {
        if (n2 > 0) {
            if (vector == this.m_paths) {
                System.err.println("Eliminated " + n2 + " total paths!");
                System.err.println("Consolodated " + n3 + " redundent paths!");
            } else {
                System.err.println("Eliminated " + n2 + " total global paths!");
                System.err.println("Consolodated " + n3 + " redundent global paths!");
            }
        }
    }

    private final void assertIsLocPathIterator(Expression expression, ExpressionOwner expressionOwner) throws RuntimeException {
        if (!(expression instanceof LocPathIterator)) {
            String string = expression instanceof Variable ? "Programmer's assertion: expr1 not an iterator: " + ((Variable)expression).getQName() : "Programmer's assertion: expr1 not an iterator: " + expression.getClass().getName();
            throw new RuntimeException(string + ", " + expressionOwner.getClass().getName() + " " + expression.exprGetParent());
        }
    }

    private static void validateNewAddition(Vector vector, ExpressionOwner expressionOwner, LocPathIterator locPathIterator) throws RuntimeException {
        RedundentExprEliminator.assertion(expressionOwner.getExpression() == locPathIterator, "owner.getExpression() != path!!!");
        int n2 = vector.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            ExpressionOwner expressionOwner2 = (ExpressionOwner)vector.elementAt(i2);
            RedundentExprEliminator.assertion(expressionOwner2 != expressionOwner, "duplicate owner on the list!!!");
            RedundentExprEliminator.assertion(expressionOwner2.getExpression() != locPathIterator, "duplicate expression on the list!!!");
        }
    }

    protected static void assertion(boolean bl, String string) {
        if (!bl) {
            throw new RuntimeException(XSLMessages.createMessage("ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR", new Object[]{string}));
        }
    }

    class MultistepExprHolder
    implements Cloneable {
        ExpressionOwner m_exprOwner;
        final int m_stepCount;
        MultistepExprHolder m_next;
        private final RedundentExprEliminator this$0;

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        MultistepExprHolder(RedundentExprEliminator redundentExprEliminator, ExpressionOwner expressionOwner, int n2, MultistepExprHolder multistepExprHolder) {
            this.this$0 = redundentExprEliminator;
            this.m_exprOwner = expressionOwner;
            RedundentExprEliminator.assertion(null != this.m_exprOwner, "exprOwner can not be null!");
            this.m_stepCount = n2;
            this.m_next = multistepExprHolder;
        }

        MultistepExprHolder addInSortedOrder(ExpressionOwner expressionOwner, int n2) {
            MultistepExprHolder multistepExprHolder = this;
            MultistepExprHolder multistepExprHolder2 = this;
            MultistepExprHolder multistepExprHolder3 = null;
            while (null != multistepExprHolder2) {
                if (n2 >= multistepExprHolder2.m_stepCount) {
                    MultistepExprHolder multistepExprHolder4 = new MultistepExprHolder(this.this$0, expressionOwner, n2, multistepExprHolder2);
                    if (null == multistepExprHolder3) {
                        multistepExprHolder = multistepExprHolder4;
                    } else {
                        multistepExprHolder3.m_next = multistepExprHolder4;
                    }
                    return multistepExprHolder;
                }
                multistepExprHolder3 = multistepExprHolder2;
                multistepExprHolder2 = multistepExprHolder2.m_next;
            }
            multistepExprHolder3.m_next = new MultistepExprHolder(this.this$0, expressionOwner, n2, null);
            return multistepExprHolder;
        }

        MultistepExprHolder unlink(MultistepExprHolder multistepExprHolder) {
            MultistepExprHolder multistepExprHolder2 = this;
            MultistepExprHolder multistepExprHolder3 = this;
            MultistepExprHolder multistepExprHolder4 = null;
            while (null != multistepExprHolder3) {
                if (multistepExprHolder3 == multistepExprHolder) {
                    if (null == multistepExprHolder4) {
                        multistepExprHolder2 = multistepExprHolder3.m_next;
                    } else {
                        multistepExprHolder4.m_next = multistepExprHolder3.m_next;
                    }
                    multistepExprHolder3.m_next = null;
                    return multistepExprHolder2;
                }
                multistepExprHolder4 = multistepExprHolder3;
                multistepExprHolder3 = multistepExprHolder3.m_next;
            }
            RedundentExprEliminator.assertion(false, "unlink failed!!!");
            return null;
        }

        int getLength() {
            int n2 = 0;
            MultistepExprHolder multistepExprHolder = this;
            while (null != multistepExprHolder) {
                ++n2;
                multistepExprHolder = multistepExprHolder.m_next;
            }
            return n2;
        }

        protected void diagnose() {
            System.err.print("Found multistep iterators: " + this.getLength() + "  ");
            MultistepExprHolder multistepExprHolder = this;
            while (null != multistepExprHolder) {
                System.err.print("" + multistepExprHolder.m_stepCount);
                multistepExprHolder = multistepExprHolder.m_next;
                if (null == multistepExprHolder) continue;
                System.err.print(", ");
            }
            System.err.println();
        }
    }

}

