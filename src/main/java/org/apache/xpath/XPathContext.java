/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.sax2dtm.SAX2RTFDTM;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.ObjectStack;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.SourceTreeManager;
import org.apache.xpath.VariableStack;
import org.apache.xpath.axes.OneStepIteratorForward;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.objects.DTMXRTreeFrag;
import org.apache.xpath.objects.XMLStringFactoryImpl;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public class XPathContext
extends DTMManager {
    IntStack m_last_pushed_rtfdtm = new IntStack();
    private Vector m_rtfdtm_stack = null;
    private int m_which_rtfdtm = -1;
    private SAX2RTFDTM m_global_rtfdtm = null;
    private HashMap m_DTMXRTreeFrags = null;
    private boolean m_isSecureProcessing = false;
    protected DTMManager m_dtmManager = DTMManager.newInstance(XMLStringFactoryImpl.getFactory());
    ObjectStack m_saxLocations = new ObjectStack(4096);
    private Object m_owner;
    private Method m_ownerGetErrorListener;
    private VariableStack m_variableStacks;
    private SourceTreeManager m_sourceTreeManager = new SourceTreeManager();
    private ErrorListener m_errorListener;
    private ErrorListener m_defaultErrorListener;
    private Stack m_contextNodeLists = new Stack();
    private IntStack m_currentNodes = new IntStack(4096);
    private NodeVector m_iteratorRoots = new NodeVector();
    private NodeVector m_predicateRoots = new NodeVector();
    private IntStack m_currentExpressionNodes = new IntStack(4096);
    private IntStack m_predicatePos = new IntStack();
    private ObjectStack m_prefixResolvers = new ObjectStack(4096);
    private Stack m_axesIteratorStack = new Stack();
    XPathExpressionContext expressionContext;

    public DTMManager getDTMManager() {
        return this.m_dtmManager;
    }

    public void setSecureProcessing(boolean bl) {
        this.m_isSecureProcessing = bl;
    }

    public boolean isSecureProcessing() {
        return this.m_isSecureProcessing;
    }

    public DTM getDTM(Source source, boolean bl, DTMWSFilter dTMWSFilter, boolean bl2, boolean bl3) {
        return this.m_dtmManager.getDTM(source, bl, dTMWSFilter, bl2, bl3);
    }

    public DTM getDTM(int n2) {
        return this.m_dtmManager.getDTM(n2);
    }

    public int getDTMHandleFromNode(Node node) {
        return this.m_dtmManager.getDTMHandleFromNode(node);
    }

    public int getDTMIdentity(DTM dTM) {
        return this.m_dtmManager.getDTMIdentity(dTM);
    }

    public DTM createDocumentFragment() {
        return this.m_dtmManager.createDocumentFragment();
    }

    public boolean release(DTM dTM, boolean bl) {
        if (this.m_rtfdtm_stack != null && this.m_rtfdtm_stack.contains(dTM)) {
            return false;
        }
        return this.m_dtmManager.release(dTM, bl);
    }

    public DTMIterator createDTMIterator(Object object, int n2) {
        return this.m_dtmManager.createDTMIterator(object, n2);
    }

    public DTMIterator createDTMIterator(String string, PrefixResolver prefixResolver) {
        return this.m_dtmManager.createDTMIterator(string, prefixResolver);
    }

    public DTMIterator createDTMIterator(int n2, DTMFilter dTMFilter, boolean bl) {
        return this.m_dtmManager.createDTMIterator(n2, dTMFilter, bl);
    }

    public DTMIterator createDTMIterator(int n2) {
        OneStepIteratorForward oneStepIteratorForward = new OneStepIteratorForward(13);
        oneStepIteratorForward.setRoot(n2, this);
        return oneStepIteratorForward;
    }

    public XPathContext() {
        this(true);
    }

    public XPathContext(boolean bl) {
        this.expressionContext = new XPathExpressionContext(this);
        this.m_prefixResolvers.push(null);
        this.m_currentNodes.push(-1);
        this.m_currentExpressionNodes.push(-1);
        this.m_saxLocations.push(null);
        this.m_variableStacks = bl ? new VariableStack() : new VariableStack(1);
    }

    public XPathContext(Object object) {
        this(object, true);
    }

    public XPathContext(Object object, boolean bl) {
        this(bl);
        this.m_owner = object;
        try {
            this.m_ownerGetErrorListener = this.m_owner.getClass().getMethod("getErrorListener", new Class[0]);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
    }

    public void reset() {
        this.releaseDTMXRTreeFrags();
        if (this.m_rtfdtm_stack != null) {
            Enumeration enumeration = this.m_rtfdtm_stack.elements();
            while (enumeration.hasMoreElements()) {
                this.m_dtmManager.release((DTM)enumeration.nextElement(), true);
            }
        }
        this.m_rtfdtm_stack = null;
        this.m_which_rtfdtm = -1;
        if (this.m_global_rtfdtm != null) {
            this.m_dtmManager.release(this.m_global_rtfdtm, true);
        }
        this.m_global_rtfdtm = null;
        this.m_dtmManager = DTMManager.newInstance(XMLStringFactoryImpl.getFactory());
        this.m_saxLocations.removeAllElements();
        this.m_axesIteratorStack.removeAllElements();
        this.m_contextNodeLists.removeAllElements();
        this.m_currentExpressionNodes.removeAllElements();
        this.m_currentNodes.removeAllElements();
        this.m_iteratorRoots.RemoveAllNoClear();
        this.m_predicatePos.removeAllElements();
        this.m_predicateRoots.RemoveAllNoClear();
        this.m_prefixResolvers.removeAllElements();
        this.m_prefixResolvers.push(null);
        this.m_currentNodes.push(-1);
        this.m_currentExpressionNodes.push(-1);
        this.m_saxLocations.push(null);
    }

    public void setSAXLocator(SourceLocator sourceLocator) {
        this.m_saxLocations.setTop(sourceLocator);
    }

    public void pushSAXLocatorNull() {
        this.m_saxLocations.push(null);
    }

    public void popSAXLocator() {
        this.m_saxLocations.pop();
    }

    public SourceLocator getSAXLocator() {
        return (SourceLocator)this.m_saxLocations.peek();
    }

    public Object getOwnerObject() {
        return this.m_owner;
    }

    public final VariableStack getVarStack() {
        return this.m_variableStacks;
    }

    public final void setVarStack(VariableStack variableStack) {
        this.m_variableStacks = variableStack;
    }

    public final SourceTreeManager getSourceTreeManager() {
        return this.m_sourceTreeManager;
    }

    public final ErrorListener getErrorListener() {
        if (null != this.m_errorListener) {
            return this.m_errorListener;
        }
        ErrorListener errorListener = null;
        try {
            if (null != this.m_ownerGetErrorListener) {
                errorListener = (ErrorListener)this.m_ownerGetErrorListener.invoke(this.m_owner, new Object[0]);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (null == errorListener) {
            if (null == this.m_defaultErrorListener) {
                this.m_defaultErrorListener = new DefaultErrorHandler();
            }
            errorListener = this.m_defaultErrorListener;
        }
        return errorListener;
    }

    public Stack getContextNodeListsStack() {
        return this.m_contextNodeLists;
    }

    public void setContextNodeListsStack(Stack stack) {
        this.m_contextNodeLists = stack;
    }

    public final DTMIterator getContextNodeList() {
        if (this.m_contextNodeLists.size() > 0) {
            return (DTMIterator)this.m_contextNodeLists.peek();
        }
        return null;
    }

    public final void pushContextNodeList(DTMIterator dTMIterator) {
        this.m_contextNodeLists.push(dTMIterator);
    }

    public final void popContextNodeList() {
        if (this.m_contextNodeLists.isEmpty()) {
            System.err.println("Warning: popContextNodeList when stack is empty!");
        } else {
            this.m_contextNodeLists.pop();
        }
    }

    public IntStack getCurrentNodeStack() {
        return this.m_currentNodes;
    }

    public void setCurrentNodeStack(IntStack intStack) {
        this.m_currentNodes = intStack;
    }

    public final int getCurrentNode() {
        return this.m_currentNodes.peek();
    }

    public final void pushCurrentNodeAndExpression(int n2, int n3) {
        this.m_currentNodes.push(n2);
        this.m_currentExpressionNodes.push(n2);
    }

    public final void popCurrentNodeAndExpression() {
        this.m_currentNodes.quickPop(1);
        this.m_currentExpressionNodes.quickPop(1);
    }

    public final void pushCurrentNode(int n2) {
        this.m_currentNodes.push(n2);
    }

    public final void popCurrentNode() {
        this.m_currentNodes.quickPop(1);
    }

    public IntStack getCurrentExpressionNodeStack() {
        return this.m_currentExpressionNodes;
    }

    public void setCurrentExpressionNodeStack(IntStack intStack) {
        this.m_currentExpressionNodes = intStack;
    }

    public final int getPredicatePos() {
        return this.m_predicatePos.peek();
    }

    public final void pushPredicatePos(int n2) {
        this.m_predicatePos.push(n2);
    }

    public final void popPredicatePos() {
        this.m_predicatePos.pop();
    }

    public final void pushCurrentExpressionNode(int n2) {
        this.m_currentExpressionNodes.push(n2);
    }

    public final void popCurrentExpressionNode() {
        this.m_currentExpressionNodes.quickPop(1);
    }

    public final PrefixResolver getNamespaceContext() {
        return (PrefixResolver)this.m_prefixResolvers.peek();
    }

    public final void setNamespaceContext(PrefixResolver prefixResolver) {
        this.m_prefixResolvers.setTop(prefixResolver);
    }

    public final void pushNamespaceContext(PrefixResolver prefixResolver) {
        this.m_prefixResolvers.push(prefixResolver);
    }

    public final void pushNamespaceContextNull() {
        this.m_prefixResolvers.push(null);
    }

    public final void popNamespaceContext() {
        this.m_prefixResolvers.pop();
    }

    public Stack getAxesIteratorStackStacks() {
        return this.m_axesIteratorStack;
    }

    public void setAxesIteratorStackStacks(Stack stack) {
        this.m_axesIteratorStack = stack;
    }

    public final void pushSubContextList(SubContextList subContextList) {
        this.m_axesIteratorStack.push(subContextList);
    }

    public final void popSubContextList() {
        this.m_axesIteratorStack.pop();
    }

    public SubContextList getSubContextList() {
        return this.m_axesIteratorStack.isEmpty() ? null : (SubContextList)this.m_axesIteratorStack.peek();
    }

    public SubContextList getCurrentNodeList() {
        return this.m_axesIteratorStack.isEmpty() ? null : (SubContextList)this.m_axesIteratorStack.elementAt(0);
    }

    public final int getContextNode() {
        return this.getCurrentNode();
    }

    public ExpressionContext getExpressionContext() {
        return this.expressionContext;
    }

    public DTM getGlobalRTFDTM() {
        if (this.m_global_rtfdtm == null || this.m_global_rtfdtm.isTreeIncomplete()) {
            this.m_global_rtfdtm = (SAX2RTFDTM)this.m_dtmManager.getDTM(null, true, null, false, false);
        }
        return this.m_global_rtfdtm;
    }

    public DTM getRTFDTM() {
        SAX2RTFDTM sAX2RTFDTM;
        if (this.m_rtfdtm_stack == null) {
            this.m_rtfdtm_stack = new Vector();
            sAX2RTFDTM = (SAX2RTFDTM)this.m_dtmManager.getDTM(null, true, null, false, false);
            this.m_rtfdtm_stack.addElement(sAX2RTFDTM);
            ++this.m_which_rtfdtm;
        } else if (this.m_which_rtfdtm < 0) {
            sAX2RTFDTM = (SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(++this.m_which_rtfdtm);
        } else {
            sAX2RTFDTM = (SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm);
            if (sAX2RTFDTM.isTreeIncomplete()) {
                if (++this.m_which_rtfdtm < this.m_rtfdtm_stack.size()) {
                    sAX2RTFDTM = (SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm);
                } else {
                    sAX2RTFDTM = (SAX2RTFDTM)this.m_dtmManager.getDTM(null, true, null, false, false);
                    this.m_rtfdtm_stack.addElement(sAX2RTFDTM);
                }
            }
        }
        return sAX2RTFDTM;
    }

    public void pushRTFContext() {
        this.m_last_pushed_rtfdtm.push(this.m_which_rtfdtm);
        if (null != this.m_rtfdtm_stack) {
            ((SAX2RTFDTM)this.getRTFDTM()).pushRewindMark();
        }
    }

    public void popRTFContext() {
        int n2 = this.m_last_pushed_rtfdtm.pop();
        if (null == this.m_rtfdtm_stack) {
            return;
        }
        if (this.m_which_rtfdtm == n2) {
            if (n2 >= 0) {
                boolean bl = ((SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(n2)).popRewindMark();
            }
        } else {
            while (this.m_which_rtfdtm != n2) {
                boolean bl = ((SAX2RTFDTM)this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm)).popRewindMark();
                --this.m_which_rtfdtm;
            }
        }
    }

    public DTMXRTreeFrag getDTMXRTreeFrag(int n2) {
        if (this.m_DTMXRTreeFrags == null) {
            this.m_DTMXRTreeFrags = new HashMap();
        }
        if (this.m_DTMXRTreeFrags.containsKey(new Integer(n2))) {
            return (DTMXRTreeFrag)this.m_DTMXRTreeFrags.get(new Integer(n2));
        }
        DTMXRTreeFrag dTMXRTreeFrag = new DTMXRTreeFrag(n2, this);
        this.m_DTMXRTreeFrags.put(new Integer(n2), dTMXRTreeFrag);
        return dTMXRTreeFrag;
    }

    private final void releaseDTMXRTreeFrags() {
        if (this.m_DTMXRTreeFrags == null) {
            return;
        }
        Iterator iterator = this.m_DTMXRTreeFrags.values().iterator();
        while (iterator.hasNext()) {
            DTMXRTreeFrag dTMXRTreeFrag = (DTMXRTreeFrag)iterator.next();
            dTMXRTreeFrag.destruct();
            iterator.remove();
        }
        this.m_DTMXRTreeFrags = null;
    }

    static VariableStack access$000(XPathContext xPathContext) {
        return xPathContext.m_variableStacks;
    }

    public class XPathExpressionContext
    implements ExpressionContext {
        private final XPathContext this$0;

        public XPathExpressionContext(XPathContext xPathContext) {
            this.this$0 = xPathContext;
        }

        public XPathContext getXPathContext() {
            return this.this$0;
        }

        public DTMManager getDTMManager() {
            return this.this$0.m_dtmManager;
        }

        public Node getContextNode() {
            int n2 = this.this$0.getCurrentNode();
            return this.this$0.getDTM(n2).getNode(n2);
        }

        public NodeIterator getContextNodes() {
            return new DTMNodeIterator(this.this$0.getContextNodeList());
        }

        public ErrorListener getErrorListener() {
            return this.this$0.getErrorListener();
        }

        public double toNumber(Node node) {
            int n2 = this.this$0.getDTMHandleFromNode(node);
            DTM dTM = this.this$0.getDTM(n2);
            XString xString = (XString)dTM.getStringValue(n2);
            return xString.num();
        }

        public String toString(Node node) {
            int n2 = this.this$0.getDTMHandleFromNode(node);
            DTM dTM = this.this$0.getDTM(n2);
            XMLString xMLString = dTM.getStringValue(n2);
            return xMLString.toString();
        }

        public final XObject getVariableOrParam(QName qName) throws TransformerException {
            return XPathContext.access$000(this.this$0).getVariableOrParam(this.this$0, qName);
        }
    }

}

