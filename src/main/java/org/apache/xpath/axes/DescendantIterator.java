/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.Expression;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public class DescendantIterator
extends LocPathIterator {
    protected transient DTMAxisTraverser m_traverser;
    protected int m_axis;
    protected int m_extendedTypeID;

    DescendantIterator(Compiler compiler, int n2, int n3) throws TransformerException {
        int n4;
        int n5;
        super(compiler, n2, n3, false);
        int n6 = OpMap.getFirstChildPos(n2);
        int n7 = compiler.getOp(n6);
        boolean bl = 42 == n7;
        boolean bl2 = false;
        if (48 == n7) {
            bl = true;
        } else if (50 == n7) {
            bl2 = true;
            n5 = compiler.getNextStepPos(n6);
            if (compiler.getOp(n5) == 42) {
                bl = true;
            }
        }
        n5 = n6;
        while ((n5 = compiler.getNextStepPos(n5)) > 0 && -1 != (n4 = compiler.getOp(n5))) {
            n6 = n5;
        }
        if ((n3 & 65536) != 0) {
            bl = false;
        }
        this.m_axis = bl2 ? (bl ? 18 : 17) : (bl ? 5 : 4);
        n4 = compiler.getWhatToShow(n6);
        if (0 == (n4 & 67) || n4 == -1) {
            this.initNodeTest(n4);
        } else {
            this.initNodeTest(n4, compiler.getStepNS(n6), compiler.getStepLocalName(n6));
        }
        this.initPredicateInfo(compiler, n6);
    }

    public DescendantIterator() {
        super(null);
        this.m_axis = 18;
        int n2 = -1;
        this.initNodeTest(n2);
    }

    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        DescendantIterator descendantIterator = (DescendantIterator)super.cloneWithReset();
        descendantIterator.m_traverser = this.m_traverser;
        descendantIterator.resetProximityPositions();
        return descendantIterator;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int nextNode() {
        VariableStack variableStack;
        int n2;
        block12 : {
            if (this.m_foundLast) {
                return -1;
            }
            if (-1 == this.m_lastFetched) {
                this.resetProximityPositions();
            }
            if (-1 != this.m_stackFrame) {
                variableStack = this.m_execContext.getVarStack();
                n2 = variableStack.getStackFrame();
                variableStack.setStackFrame(this.m_stackFrame);
            } else {
                variableStack = null;
                n2 = 0;
            }
            try {
                int n3;
                do {
                    if (0 == this.m_extendedTypeID) {
                        this.m_lastFetched = -1 == this.m_lastFetched ? this.m_traverser.first(this.m_context) : this.m_traverser.next(this.m_context, this.m_lastFetched);
                        n3 = this.m_lastFetched;
                        continue;
                    }
                    this.m_lastFetched = -1 == this.m_lastFetched ? this.m_traverser.first(this.m_context, this.m_extendedTypeID) : this.m_traverser.next(this.m_context, this.m_lastFetched, this.m_extendedTypeID);
                    n3 = this.m_lastFetched;
                } while (-1 != n3 && 1 != this.acceptNode(n3) && n3 != -1);
                if (-1 == n3) break block12;
                ++this.m_pos;
                int n4 = n3;
                Object var6_6 = null;
                if (-1 != this.m_stackFrame) {
                    variableStack.setStackFrame(n2);
                }
                return n4;
            }
            catch (Throwable throwable) {
                Object var6_8 = null;
                if (-1 != this.m_stackFrame) {
                    variableStack.setStackFrame(n2);
                }
                throw throwable;
            }
        }
        this.m_foundLast = true;
        int n5 = -1;
        Object var6_7 = null;
        if (-1 != this.m_stackFrame) {
            variableStack.setStackFrame(n2);
        }
        return n5;
    }

    public void setRoot(int n2, Object object) {
        super.setRoot(n2, object);
        this.m_traverser = this.m_cdtm.getAxisTraverser(this.m_axis);
        String string = this.getLocalName();
        String string2 = this.getNamespace();
        int n3 = this.m_whatToShow;
        if (-1 == n3 || "*".equals(string) || "*".equals(string2)) {
            this.m_extendedTypeID = 0;
        } else {
            int n4 = DescendantIterator.getNodeTypeTest(n3);
            this.m_extendedTypeID = this.m_cdtm.getExpandedTypeID(string2, string, n4);
        }
    }

    public int asNode(XPathContext xPathContext) throws TransformerException {
        if (this.getPredicateCount() > 0) {
            return super.asNode(xPathContext);
        }
        int n2 = xPathContext.getCurrentNode();
        DTM dTM = xPathContext.getDTM(n2);
        DTMAxisTraverser dTMAxisTraverser = dTM.getAxisTraverser(this.m_axis);
        String string = this.getLocalName();
        String string2 = this.getNamespace();
        int n3 = this.m_whatToShow;
        if (-1 == n3 || string == "*" || string2 == "*") {
            return dTMAxisTraverser.first(n2);
        }
        int n4 = DescendantIterator.getNodeTypeTest(n3);
        int n5 = dTM.getExpandedTypeID(string2, string, n4);
        return dTMAxisTraverser.first(n2, n5);
    }

    public void detach() {
        if (this.m_allowDetach) {
            this.m_traverser = null;
            this.m_extendedTypeID = 0;
            super.detach();
        }
    }

    public int getAxis() {
        return this.m_axis;
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        if (this.m_axis != ((DescendantIterator)expression).m_axis) {
            return false;
        }
        return true;
    }
}

