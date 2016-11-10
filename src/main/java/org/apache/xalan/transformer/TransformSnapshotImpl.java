/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.util.Stack;
import org.apache.xalan.transformer.CountersTable;
import org.apache.xalan.transformer.TransformSnapshot;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.ObjectStack;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;

class TransformSnapshotImpl
implements TransformSnapshot {
    private VariableStack m_variableStacks;
    private IntStack m_currentNodes;
    private IntStack m_currentExpressionNodes;
    private Stack m_contextNodeLists;
    private DTMIterator m_contextNodeList;
    private Stack m_axesIteratorStack;
    private BoolStack m_currentTemplateRuleIsNull;
    private ObjectStack m_currentTemplateElements;
    private Stack m_currentMatchTemplates;
    private NodeVector m_currentMatchNodes;
    private CountersTable m_countersTable;
    private Stack m_attrSetStack;
    boolean m_nsContextPushed;
    private NamespaceMappings m_nsSupport;

    TransformSnapshotImpl(TransformerImpl transformerImpl) {
        try {
            SerializationHandler serializationHandler = transformerImpl.getResultTreeHandler();
            this.m_nsSupport = (NamespaceMappings)serializationHandler.getNamespaceMappings().clone();
            XPathContext xPathContext = transformerImpl.getXPathContext();
            this.m_variableStacks = (VariableStack)xPathContext.getVarStack().clone();
            this.m_currentNodes = (IntStack)xPathContext.getCurrentNodeStack().clone();
            this.m_currentExpressionNodes = (IntStack)xPathContext.getCurrentExpressionNodeStack().clone();
            this.m_contextNodeLists = (Stack)xPathContext.getContextNodeListsStack().clone();
            if (!this.m_contextNodeLists.empty()) {
                this.m_contextNodeList = (DTMIterator)xPathContext.getContextNodeList().clone();
            }
            this.m_axesIteratorStack = (Stack)xPathContext.getAxesIteratorStackStacks().clone();
            this.m_currentTemplateRuleIsNull = (BoolStack)transformerImpl.m_currentTemplateRuleIsNull.clone();
            this.m_currentTemplateElements = (ObjectStack)transformerImpl.m_currentTemplateElements.clone();
            this.m_currentMatchTemplates = (Stack)transformerImpl.m_currentMatchTemplates.clone();
            this.m_currentMatchNodes = (NodeVector)transformerImpl.m_currentMatchedNodes.clone();
            this.m_countersTable = (CountersTable)transformerImpl.getCountersTable().clone();
            if (transformerImpl.m_attrSetStack != null) {
                this.m_attrSetStack = (Stack)transformerImpl.m_attrSetStack.clone();
            }
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new WrappedRuntimeException(cloneNotSupportedException);
        }
    }

    void apply(TransformerImpl transformerImpl) {
        try {
            SerializationHandler serializationHandler = transformerImpl.getResultTreeHandler();
            if (serializationHandler != null) {
                serializationHandler.setNamespaceMappings((NamespaceMappings)this.m_nsSupport.clone());
            }
            XPathContext xPathContext = transformerImpl.getXPathContext();
            xPathContext.setVarStack((VariableStack)this.m_variableStacks.clone());
            xPathContext.setCurrentNodeStack((IntStack)this.m_currentNodes.clone());
            xPathContext.setCurrentExpressionNodeStack((IntStack)this.m_currentExpressionNodes.clone());
            xPathContext.setContextNodeListsStack((Stack)this.m_contextNodeLists.clone());
            if (this.m_contextNodeList != null) {
                xPathContext.pushContextNodeList((DTMIterator)this.m_contextNodeList.clone());
            }
            xPathContext.setAxesIteratorStackStacks((Stack)this.m_axesIteratorStack.clone());
            transformerImpl.m_currentTemplateRuleIsNull = (BoolStack)this.m_currentTemplateRuleIsNull.clone();
            transformerImpl.m_currentTemplateElements = (ObjectStack)this.m_currentTemplateElements.clone();
            transformerImpl.m_currentMatchTemplates = (Stack)this.m_currentMatchTemplates.clone();
            transformerImpl.m_currentMatchedNodes = (NodeVector)this.m_currentMatchNodes.clone();
            transformerImpl.m_countersTable = (CountersTable)this.m_countersTable.clone();
            if (this.m_attrSetStack != null) {
                transformerImpl.m_attrSetStack = (Stack)this.m_attrSetStack.clone();
            }
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new WrappedRuntimeException(cloneNotSupportedException);
        }
    }
}

