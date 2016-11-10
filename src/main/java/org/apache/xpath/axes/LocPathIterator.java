/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.IteratorPool;
import org.apache.xpath.axes.PathComponent;
import org.apache.xpath.axes.PredicatedNodeTest;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class LocPathIterator
extends PredicatedNodeTest
implements Serializable,
Cloneable,
DTMIterator,
PathComponent {
    static final long serialVersionUID = -4602476357268405754L;
    protected boolean m_allowDetach = true;
    protected transient IteratorPool m_clones;
    protected transient DTM m_cdtm;
    transient int m_stackFrame;
    private boolean m_isTopLevel;
    public transient int m_lastFetched;
    protected transient int m_context;
    protected transient int m_currentContextNode;
    protected transient int m_pos;
    protected transient int m_length;
    private PrefixResolver m_prefixResolver;
    protected transient XPathContext m_execContext;

    protected LocPathIterator() {
        this.m_clones = new IteratorPool(this);
        this.m_stackFrame = -1;
        this.m_isTopLevel = false;
        this.m_lastFetched = -1;
        this.m_context = -1;
        this.m_currentContextNode = -1;
        this.m_pos = 0;
        this.m_length = -1;
    }

    protected LocPathIterator(PrefixResolver prefixResolver) {
        this.m_clones = new IteratorPool(this);
        this.m_stackFrame = -1;
        this.m_isTopLevel = false;
        this.m_lastFetched = -1;
        this.m_context = -1;
        this.m_currentContextNode = -1;
        this.m_pos = 0;
        this.m_length = -1;
        this.setLocPathIterator(this);
        this.m_prefixResolver = prefixResolver;
    }

    protected LocPathIterator(Compiler compiler, int n2, int n3) throws TransformerException {
        this(compiler, n2, n3, true);
    }

    protected LocPathIterator(Compiler compiler, int n2, int n3, boolean bl) throws TransformerException {
        this.m_clones = new IteratorPool(this);
        this.m_stackFrame = -1;
        this.m_isTopLevel = false;
        this.m_lastFetched = -1;
        this.m_context = -1;
        this.m_currentContextNode = -1;
        this.m_pos = 0;
        this.m_length = -1;
        this.setLocPathIterator(this);
    }

    public int getAnalysisBits() {
        int n2 = this.getAxis();
        int n3 = WalkerFactory.getAnalysisBitFromAxes(n2);
        return n3;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, TransformerException {
        try {
            objectInputStream.defaultReadObject();
            this.m_clones = new IteratorPool(this);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new TransformerException(classNotFoundException);
        }
    }

    public void setEnvironment(Object object) {
    }

    public DTM getDTM(int n2) {
        return this.m_execContext.getDTM(n2);
    }

    public DTMManager getDTMManager() {
        return this.m_execContext.getDTMManager();
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XNodeSet xNodeSet = new XNodeSet((LocPathIterator)this.m_clones.getInstance());
        xNodeSet.setRoot(xPathContext.getCurrentNode(), xPathContext);
        return xNodeSet;
    }

    public void executeCharsToContentHandler(XPathContext xPathContext, ContentHandler contentHandler) throws TransformerException, SAXException {
        LocPathIterator locPathIterator = (LocPathIterator)this.m_clones.getInstance();
        int n2 = xPathContext.getCurrentNode();
        locPathIterator.setRoot(n2, xPathContext);
        int n3 = locPathIterator.nextNode();
        DTM dTM = locPathIterator.getDTM(n3);
        locPathIterator.detach();
        if (n3 != -1) {
            dTM.dispatchCharactersEvents(n3, contentHandler, false);
        }
    }

    public DTMIterator asIterator(XPathContext xPathContext, int n2) throws TransformerException {
        XNodeSet xNodeSet = new XNodeSet((LocPathIterator)this.m_clones.getInstance());
        xNodeSet.setRoot(n2, xPathContext);
        return xNodeSet;
    }

    public boolean isNodesetExpr() {
        return true;
    }

    public int asNode(XPathContext xPathContext) throws TransformerException {
        DTMIterator dTMIterator = this.m_clones.getInstance();
        int n2 = xPathContext.getCurrentNode();
        dTMIterator.setRoot(n2, xPathContext);
        int n3 = dTMIterator.nextNode();
        dTMIterator.detach();
        return n3;
    }

    public boolean bool(XPathContext xPathContext) throws TransformerException {
        return this.asNode(xPathContext) != -1;
    }

    public void setIsTopLevel(boolean bl) {
        this.m_isTopLevel = bl;
    }

    public boolean getIsTopLevel() {
        return this.m_isTopLevel;
    }

    public void setRoot(int n2, Object object) {
        XPathContext xPathContext;
        this.m_context = n2;
        this.m_execContext = xPathContext = (XPathContext)object;
        this.m_cdtm = xPathContext.getDTM(n2);
        this.m_currentContextNode = n2;
        if (null == this.m_prefixResolver) {
            this.m_prefixResolver = xPathContext.getNamespaceContext();
        }
        this.m_lastFetched = -1;
        this.m_foundLast = false;
        this.m_pos = 0;
        this.m_length = -1;
        if (this.m_isTopLevel) {
            this.m_stackFrame = xPathContext.getVarStack().getStackFrame();
        }
    }

    protected void setNextPosition(int n2) {
        this.assertion(false, "setNextPosition not supported in this iterator!");
    }

    public final int getCurrentPos() {
        return this.m_pos;
    }

    public void setShouldCacheNodes(boolean bl) {
        this.assertion(false, "setShouldCacheNodes not supported by this iterater!");
    }

    public boolean isMutable() {
        return false;
    }

    public void setCurrentPos(int n2) {
        this.assertion(false, "setCurrentPos not supported by this iterator!");
    }

    public void incrementCurrentPos() {
        ++this.m_pos;
    }

    public int size() {
        this.assertion(false, "size() not supported by this iterator!");
        return 0;
    }

    public int item(int n2) {
        this.assertion(false, "item(int index) not supported by this iterator!");
        return 0;
    }

    public void setItem(int n2, int n3) {
        this.assertion(false, "setItem not supported by this iterator!");
    }

    public int getLength() {
        LocPathIterator locPathIterator;
        int n2;
        boolean bl = this == this.m_execContext.getSubContextList();
        int n3 = this.getPredicateCount();
        if (-1 != this.m_length && bl && this.m_predicateIndex < 1) {
            return this.m_length;
        }
        if (this.m_foundLast) {
            return this.m_pos;
        }
        int n4 = this.m_predicateIndex >= 0 ? this.getProximityPosition() : this.m_pos;
        try {
            locPathIterator = (LocPathIterator)this.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return -1;
        }
        if (n3 > 0 && bl) {
            locPathIterator.m_predCount = this.m_predicateIndex;
        }
        while (-1 != (n2 = locPathIterator.nextNode())) {
            ++n4;
        }
        if (bl && this.m_predicateIndex < 1) {
            this.m_length = n4;
        }
        return n4;
    }

    public boolean isFresh() {
        return this.m_pos == 0;
    }

    public int previousNode() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_ITERATE", null));
    }

    public int getWhatToShow() {
        return -17;
    }

    public DTMFilter getFilter() {
        return null;
    }

    public int getRoot() {
        return this.m_context;
    }

    public boolean getExpandEntityReferences() {
        return true;
    }

    public void allowDetachToRelease(boolean bl) {
        this.m_allowDetach = bl;
    }

    public void detach() {
        if (this.m_allowDetach) {
            this.m_execContext = null;
            this.m_cdtm = null;
            this.m_length = -1;
            this.m_pos = 0;
            this.m_lastFetched = -1;
            this.m_context = -1;
            this.m_currentContextNode = -1;
            this.m_clones.freeInstance(this);
        }
    }

    public void reset() {
        this.assertion(false, "This iterator can not reset!");
    }

    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        LocPathIterator locPathIterator = (LocPathIterator)this.m_clones.getInstanceOrThrow();
        locPathIterator.m_execContext = this.m_execContext;
        locPathIterator.m_cdtm = this.m_cdtm;
        locPathIterator.m_context = this.m_context;
        locPathIterator.m_currentContextNode = this.m_currentContextNode;
        locPathIterator.m_stackFrame = this.m_stackFrame;
        return locPathIterator;
    }

    public abstract int nextNode();

    protected int returnNextNode(int n2) {
        if (-1 != n2) {
            ++this.m_pos;
        }
        this.m_lastFetched = n2;
        if (-1 == n2) {
            this.m_foundLast = true;
        }
        return n2;
    }

    public int getCurrentNode() {
        return this.m_lastFetched;
    }

    public void runTo(int n2) {
        if (this.m_foundLast || n2 >= 0 && n2 <= this.getCurrentPos()) {
            return;
        }
        if (-1 == n2) {
            int n3;
            while (-1 != (n3 = this.nextNode())) {
            }
        } else {
            int n4;
            while (-1 != (n4 = this.nextNode()) && this.getCurrentPos() < n2) {
            }
        }
    }

    public final boolean getFoundLast() {
        return this.m_foundLast;
    }

    public final XPathContext getXPathContext() {
        return this.m_execContext;
    }

    public final int getContext() {
        return this.m_context;
    }

    public final int getCurrentContextNode() {
        return this.m_currentContextNode;
    }

    public final void setCurrentContextNode(int n2) {
        this.m_currentContextNode = n2;
    }

    public final PrefixResolver getPrefixResolver() {
        if (null == this.m_prefixResolver) {
            this.m_prefixResolver = (PrefixResolver)((Object)this.getExpressionOwner());
        }
        return this.m_prefixResolver;
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        if (xPathVisitor.visitLocationPath(expressionOwner, this)) {
            xPathVisitor.visitStep(expressionOwner, this);
            this.callPredicateVisitors(xPathVisitor);
        }
    }

    public boolean isDocOrdered() {
        return true;
    }

    public int getAxis() {
        return -1;
    }

    public int getLastPos(XPathContext xPathContext) {
        return this.getLength();
    }
}

