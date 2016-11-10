/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import javax.xml.transform.Source;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;
import org.apache.xml.dtm.ref.DTMDefaultBaseTraversers;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.XMLStringFactory;

public abstract class DTMDefaultBaseIterators
extends DTMDefaultBaseTraversers {
    public DTMDefaultBaseIterators(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl) {
        super(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl);
    }

    public DTMDefaultBaseIterators(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl, int n3, boolean bl2, boolean bl3) {
        super(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl, n3, bl2, bl3);
    }

    public DTMAxisIterator getTypedAxisIterator(int n2, int n3) {
        DTMAxisIterator dTMAxisIterator = null;
        switch (n2) {
            case 13: {
                dTMAxisIterator = new TypedSingletonIterator(this, n3);
                break;
            }
            case 3: {
                dTMAxisIterator = new TypedChildrenIterator(this, n3);
                break;
            }
            case 10: {
                return new ParentIterator(this).setNodeType(n3);
            }
            case 0: {
                return new TypedAncestorIterator(this, n3);
            }
            case 1: {
                return new TypedAncestorIterator(this, n3).includeSelf();
            }
            case 2: {
                return new TypedAttributeIterator(this, n3);
            }
            case 4: {
                dTMAxisIterator = new TypedDescendantIterator(this, n3);
                break;
            }
            case 5: {
                dTMAxisIterator = new TypedDescendantIterator(this, n3).includeSelf();
                break;
            }
            case 6: {
                dTMAxisIterator = new TypedFollowingIterator(this, n3);
                break;
            }
            case 11: {
                dTMAxisIterator = new TypedPrecedingIterator(this, n3);
                break;
            }
            case 7: {
                dTMAxisIterator = new TypedFollowingSiblingIterator(this, n3);
                break;
            }
            case 12: {
                dTMAxisIterator = new TypedPrecedingSiblingIterator(this, n3);
                break;
            }
            case 9: {
                dTMAxisIterator = new TypedNamespaceIterator(this, n3);
                break;
            }
            case 19: {
                dTMAxisIterator = new TypedRootIterator(this, n3);
                break;
            }
            default: {
                throw new DTMException(XMLMessages.createXMLMessage("ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", new Object[]{Axis.getNames(n2)}));
            }
        }
        return dTMAxisIterator;
    }

    public DTMAxisIterator getAxisIterator(int n2) {
        DTMAxisIterator dTMAxisIterator = null;
        switch (n2) {
            case 13: {
                dTMAxisIterator = new SingletonIterator(this);
                break;
            }
            case 3: {
                dTMAxisIterator = new ChildrenIterator(this);
                break;
            }
            case 10: {
                return new ParentIterator(this);
            }
            case 0: {
                return new AncestorIterator(this);
            }
            case 1: {
                return new AncestorIterator(this).includeSelf();
            }
            case 2: {
                return new AttributeIterator(this);
            }
            case 4: {
                dTMAxisIterator = new DescendantIterator(this);
                break;
            }
            case 5: {
                dTMAxisIterator = new DescendantIterator(this).includeSelf();
                break;
            }
            case 6: {
                dTMAxisIterator = new FollowingIterator(this);
                break;
            }
            case 11: {
                dTMAxisIterator = new PrecedingIterator(this);
                break;
            }
            case 7: {
                dTMAxisIterator = new FollowingSiblingIterator(this);
                break;
            }
            case 12: {
                dTMAxisIterator = new PrecedingSiblingIterator(this);
                break;
            }
            case 9: {
                dTMAxisIterator = new NamespaceIterator(this);
                break;
            }
            case 19: {
                dTMAxisIterator = new RootIterator(this);
                break;
            }
            default: {
                throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_AXIS_NOT_IMPLEMENTED", new Object[]{Axis.getNames(n2)}));
            }
        }
        return dTMAxisIterator;
    }

    public final class TypedSingletonIterator
    extends SingletonIterator {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedSingletonIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public int next() {
            int n2 = this._currentNode;
            int n3 = this._nodeType;
            this._currentNode = -1;
            if (n3 >= 14 ? this.this$0.getExpandedTypeID(n2) == n3 : this.this$0.getNodeType(n2) == n3) {
                return this.returnNode(n2);
            }
            return -1;
        }
    }

    public class SingletonIterator
    extends InternalAxisIteratorBase {
        private boolean _isConstant;
        private final DTMDefaultBaseIterators this$0;

        public SingletonIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            this(dTMDefaultBaseIterators, Integer.MIN_VALUE, false);
        }

        public SingletonIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            this(dTMDefaultBaseIterators, n2, false);
        }

        public SingletonIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2, boolean bl) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._currentNode = this._startNode = n2;
            this._isConstant = bl;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isConstant) {
                this._currentNode = this._startNode;
                return this.resetPosition();
            }
            if (this._isRestartable) {
                this._currentNode = this._startNode = n2;
                return this.resetPosition();
            }
            return this;
        }

        public DTMAxisIterator reset() {
            if (this._isConstant) {
                this._currentNode = this._startNode;
                return this.resetPosition();
            }
            boolean bl = this._isRestartable;
            this._isRestartable = true;
            this.setStartNode(this._startNode);
            this._isRestartable = bl;
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            this._currentNode = -1;
            return this.returnNode(n2);
        }
    }

    public class NthDescendantIterator
    extends DescendantIterator {
        int _pos;
        private final DTMDefaultBaseIterators this$0;

        public NthDescendantIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._pos = n2;
        }

        public int next() {
            int n2;
            while ((n2 = super.next()) != -1) {
                n2 = this.this$0.makeNodeIdentity(n2);
                int n3 = this.this$0._parent(n2);
                int n4 = this.this$0._firstch(n3);
                int n5 = 0;
                do {
                    short s2;
                    if (1 != (s2 = this.this$0._type(n4))) continue;
                    ++n5;
                } while (n5 < this._pos && (n4 = this.this$0._nextsib(n4)) != -1);
                if (n2 != n4) continue;
                return n2;
            }
            return -1;
        }
    }

    public final class TypedDescendantIterator
    extends DescendantIterator {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedDescendantIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public int next() {
            short s2;
            if (this._startNode == -1) {
                return -1;
            }
            int n2 = this._currentNode;
            do {
                if (-1 != (s2 = this.this$0._type(++n2)) && this.isDescendant(n2)) continue;
                this._currentNode = -1;
                return -1;
            } while (s2 != this._nodeType && this.this$0._exptype(n2) != this._nodeType);
            this._currentNode = n2;
            return this.returnNode(this.this$0.makeNodeHandle(n2));
        }
    }

    public class DescendantIterator
    extends InternalAxisIteratorBase {
        private final DTMDefaultBaseIterators this$0;

        public DescendantIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                n2 = this.this$0.makeNodeIdentity(n2);
                this._startNode = n2--;
                if (this._includeSelf) {
                    // empty if block
                }
                this._currentNode = n2;
                return this.resetPosition();
            }
            return this;
        }

        protected boolean isDescendant(int n2) {
            return this.this$0._parent(n2) >= this._startNode || this._startNode == n2;
        }

        public int next() {
            short s2;
            if (this._startNode == -1) {
                return -1;
            }
            if (this._includeSelf && this._currentNode + 1 == this._startNode) {
                return this.returnNode(this.this$0.makeNodeHandle(++this._currentNode));
            }
            int n2 = this._currentNode;
            do {
                if (-1 != (s2 = this.this$0._type(++n2)) && this.isDescendant(n2)) continue;
                this._currentNode = -1;
                return -1;
            } while (2 == s2 || 3 == s2 || 13 == s2);
            this._currentNode = n2;
            return this.returnNode(this.this$0.makeNodeHandle(n2));
        }

        public DTMAxisIterator reset() {
            boolean bl = this._isRestartable;
            this._isRestartable = true;
            this.setStartNode(this.this$0.makeNodeHandle(this._startNode));
            this._isRestartable = bl;
            return this;
        }
    }

    public final class TypedAncestorIterator
    extends AncestorIterator {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedAncestorIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            this.m_realStartNode = n2;
            if (this._isRestartable) {
                int n3 = this.this$0.makeNodeIdentity(n2);
                int n4 = this._nodeType;
                if (!this._includeSelf && n2 != -1) {
                    n3 = this.this$0._parent(n3);
                }
                this._startNode = n2;
                if (n4 >= 14) {
                    while (n3 != -1) {
                        int n5 = this.this$0._exptype(n3);
                        if (n5 == n4) {
                            this.m_ancestors.addElement(this.this$0.makeNodeHandle(n3));
                        }
                        n3 = this.this$0._parent(n3);
                    }
                } else {
                    while (n3 != -1) {
                        int n6 = this.this$0._exptype(n3);
                        if (n6 >= 14 && this.this$0.m_expandedNameTable.getType(n6) == n4 || n6 < 14 && n6 == n4) {
                            this.m_ancestors.addElement(this.this$0.makeNodeHandle(n3));
                        }
                        n3 = this.this$0._parent(n3);
                    }
                }
                this.m_ancestorsPos = this.m_ancestors.size() - 1;
                this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
                return this.resetPosition();
            }
            return this;
        }
    }

    public class AncestorIterator
    extends InternalAxisIteratorBase {
        NodeVector m_ancestors;
        int m_ancestorsPos;
        int m_markedPos;
        int m_realStartNode;
        private final DTMDefaultBaseIterators this$0;

        public AncestorIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this.m_ancestors = new NodeVector();
        }

        public int getStartNode() {
            return this.m_realStartNode;
        }

        public final boolean isReverse() {
            return true;
        }

        public DTMAxisIterator cloneIterator() {
            this._isRestartable = false;
            try {
                AncestorIterator ancestorIterator = (AncestorIterator)Object.super.clone();
                ancestorIterator._startNode = this._startNode;
                return ancestorIterator;
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
            }
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            this.m_realStartNode = n2;
            if (this._isRestartable) {
                int n3 = this.this$0.makeNodeIdentity(n2);
                if (!this._includeSelf && n2 != -1) {
                    n3 = this.this$0._parent(n3);
                    n2 = this.this$0.makeNodeHandle(n3);
                }
                this._startNode = n2;
                while (n3 != -1) {
                    this.m_ancestors.addElement(n2);
                    n3 = this.this$0._parent(n3);
                    n2 = this.this$0.makeNodeHandle(n3);
                }
                this.m_ancestorsPos = this.m_ancestors.size() - 1;
                this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
                return this.resetPosition();
            }
            return this;
        }

        public DTMAxisIterator reset() {
            this.m_ancestorsPos = this.m_ancestors.size() - 1;
            this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
            return this.resetPosition();
        }

        public int next() {
            int n2;
            int n3 = this._currentNode;
            this._currentNode = (n2 = --this.m_ancestorsPos) >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
            return this.returnNode(n3);
        }

        public void setMark() {
            this.m_markedPos = this.m_ancestorsPos;
        }

        public void gotoMark() {
            this.m_ancestorsPos = this.m_markedPos;
            this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
        }
    }

    public final class TypedFollowingIterator
    extends FollowingIterator {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedFollowingIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public int next() {
            int n2;
            do {
                n2 = this._currentNode;
                this._currentNode = this.m_traverser.next(this._startNode, this._currentNode);
            } while (n2 != -1 && this.this$0.getExpandedTypeID(n2) != this._nodeType && this.this$0.getNodeType(n2) != this._nodeType);
            return n2 == -1 ? -1 : this.returnNode(n2);
        }
    }

    public class FollowingIterator
    extends InternalAxisIteratorBase {
        DTMAxisTraverser m_traverser;
        private final DTMDefaultBaseIterators this$0;

        public FollowingIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this.m_traverser = dTMDefaultBaseIterators.getAxisTraverser(6);
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = this.m_traverser.first(n2);
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            this._currentNode = this.m_traverser.next(this._startNode, this._currentNode);
            return this.returnNode(n2);
        }
    }

    public final class TypedPrecedingIterator
    extends PrecedingIterator {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedPrecedingIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public int next() {
            block5 : {
                var1_1 = this._currentNode;
                var2_2 = this._nodeType;
                if (var2_2 < 14) ** GOTO lbl15
                do lbl-1000: // 3 sources:
                {
                    ++var1_1;
                    if (this._sp < 0) {
                        var1_1 = -1;
                        break block5;
                    }
                    if (var1_1 < this._stack[this._sp]) continue;
                    if (--this._sp >= 0) ** GOTO lbl-1000
                    var1_1 = -1;
                    break block5;
                } while (this.this$0._exptype(var1_1) != var2_2);
                ** GOTO lbl25
lbl15: // 1 sources:
                do lbl-1000: // 3 sources:
                {
                    ++var1_1;
                    if (this._sp < 0) {
                        var1_1 = -1;
                        break;
                    }
                    if (var1_1 < this._stack[this._sp]) continue;
                    if (--this._sp >= 0) ** GOTO lbl-1000
                    var1_1 = -1;
                    break;
                } while (!((var3_3 = this.this$0._exptype(var1_1)) < 14 ? var3_3 == var2_2 : this.this$0.m_expandedNameTable.getType(var3_3) == var2_2));
            }
            this._currentNode = var1_1;
            if (var1_1 == -1) {
                return -1;
            }
            v0 = this.returnNode(this.this$0.makeNodeHandle(var1_1));
            return v0;
        }
    }

    public class PrecedingIterator
    extends InternalAxisIteratorBase {
        private final int _maxAncestors = 8;
        protected int[] _stack;
        protected int _sp;
        protected int _oldsp;
        protected int _markedsp;
        protected int _markedNode;
        protected int _markedDescendant;
        private final DTMDefaultBaseIterators this$0;

        public PrecedingIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._maxAncestors = 8;
            this._stack = new int[8];
        }

        public boolean isReverse() {
            return true;
        }

        public DTMAxisIterator cloneIterator() {
            this._isRestartable = false;
            try {
                PrecedingIterator precedingIterator = (PrecedingIterator)Object.super.clone();
                int[] arrn = new int[this._stack.length];
                System.arraycopy(this._stack, 0, arrn, 0, this._stack.length);
                precedingIterator._stack = arrn;
                return precedingIterator;
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
            }
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                if (this.this$0._type(n2 = this.this$0.makeNodeIdentity(n2)) == 2) {
                    n2 = this.this$0._parent(n2);
                }
                this._startNode = n2;
                int n3 = 0;
                this._stack[0] = n2;
                int n4 = n2;
                while ((n4 = this.this$0._parent(n4)) != -1) {
                    if (++n3 == this._stack.length) {
                        int[] arrn = new int[n3 + 4];
                        System.arraycopy(this._stack, 0, arrn, 0, n3);
                        this._stack = arrn;
                    }
                    this._stack[n3] = n4;
                }
                if (n3 > 0) {
                    --n3;
                }
                this._currentNode = this._stack[n3];
                this._oldsp = this._sp = n3;
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            ++this._currentNode;
            while (this._sp >= 0) {
                if (this._currentNode < this._stack[this._sp]) {
                    if (this.this$0._type(this._currentNode) != 2 && this.this$0._type(this._currentNode) != 13) {
                        return this.returnNode(this.this$0.makeNodeHandle(this._currentNode));
                    }
                } else {
                    --this._sp;
                }
                ++this._currentNode;
            }
            return -1;
        }

        public DTMAxisIterator reset() {
            this._sp = this._oldsp;
            return this.resetPosition();
        }

        public void setMark() {
            this._markedsp = this._sp;
            this._markedNode = this._currentNode;
            this._markedDescendant = this._stack[0];
        }

        public void gotoMark() {
            this._sp = this._markedsp;
            this._currentNode = this._markedNode;
        }
    }

    public final class TypedPrecedingSiblingIterator
    extends PrecedingSiblingIterator {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedPrecedingSiblingIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public int next() {
            int n2 = this._currentNode;
            int n3 = this._nodeType;
            int n4 = this._startNodeID;
            if (n3 >= 14) {
                while (n2 != -1 && n2 != n4 && this.this$0._exptype(n2) != n3) {
                    n2 = this.this$0._nextsib(n2);
                }
            } else {
                int n5;
                while (n2 != -1 && n2 != n4 && !((n5 = this.this$0._exptype(n2)) < 14 ? n5 == n3 : this.this$0.m_expandedNameTable.getType(n5) == n3)) {
                    n2 = this.this$0._nextsib(n2);
                }
            }
            if (n2 == -1 || n2 == this._startNodeID) {
                this._currentNode = -1;
                return -1;
            }
            this._currentNode = this.this$0._nextsib(n2);
            return this.returnNode(this.this$0.makeNodeHandle(n2));
        }
    }

    public class PrecedingSiblingIterator
    extends InternalAxisIteratorBase {
        protected int _startNodeID;
        private final DTMDefaultBaseIterators this$0;

        public PrecedingSiblingIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
        }

        public boolean isReverse() {
            return true;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._startNodeID = this.this$0.makeNodeIdentity(n2);
                if ((n2 = this._startNodeID) == -1) {
                    this._currentNode = n2;
                    return this.resetPosition();
                }
                short s2 = this.this$0.m_expandedNameTable.getType(this.this$0._exptype(n2));
                if (2 == s2 || 13 == s2) {
                    this._currentNode = n2;
                } else {
                    this._currentNode = this.this$0._parent(n2);
                    this._currentNode = -1 != this._currentNode ? this.this$0._firstch(this._currentNode) : n2;
                }
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            if (this._currentNode == this._startNodeID || this._currentNode == -1) {
                return -1;
            }
            int n2 = this._currentNode;
            this._currentNode = this.this$0._nextsib(n2);
            return this.returnNode(this.this$0.makeNodeHandle(n2));
        }
    }

    public final class TypedAttributeIterator
    extends InternalAxisIteratorBase {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedAttributeIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = this.this$0.getTypedAttribute(n2, this._nodeType);
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            this._currentNode = -1;
            return this.returnNode(n2);
        }
    }

    public final class AttributeIterator
    extends InternalAxisIteratorBase {
        private final DTMDefaultBaseIterators this$0;

        public AttributeIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = this.this$0.getFirstAttributeIdentity(this.this$0.makeNodeIdentity(n2));
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            if (n2 != -1) {
                this._currentNode = this.this$0.getNextAttributeIdentity(n2);
                return this.returnNode(this.this$0.makeNodeHandle(n2));
            }
            return -1;
        }
    }

    public final class TypedFollowingSiblingIterator
    extends FollowingSiblingIterator {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedFollowingSiblingIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public int next() {
            if (this._currentNode == -1) {
                return -1;
            }
            int n2 = this._currentNode;
            int n3 = this._nodeType;
            if (n3 >= 14) {
                while ((n2 = this.this$0._nextsib(n2)) != -1 && this.this$0._exptype(n2) != n3) {
                }
            } else {
                int n4;
                while ((n2 = this.this$0._nextsib(n2)) != -1 && !((n4 = this.this$0._exptype(n2)) < 14 ? n4 == n3 : this.this$0.m_expandedNameTable.getType(n4) == n3)) {
                }
            }
            this._currentNode = n2;
            return this._currentNode == -1 ? -1 : this.returnNode(this.this$0.makeNodeHandle(this._currentNode));
        }
    }

    public class FollowingSiblingIterator
    extends InternalAxisIteratorBase {
        private final DTMDefaultBaseIterators this$0;

        public FollowingSiblingIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = this.this$0.makeNodeIdentity(n2);
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            this._currentNode = this._currentNode == -1 ? -1 : this.this$0._nextsib(this._currentNode);
            return this.returnNode(this.this$0.makeNodeHandle(this._currentNode));
        }
    }

    public class TypedRootIterator
    extends RootIterator {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedRootIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public int next() {
            if (this._startNode == this._currentNode) {
                return -1;
            }
            int n2 = this._nodeType;
            int n3 = this._startNode;
            int n4 = this.this$0.getExpandedTypeID(n3);
            this._currentNode = n3;
            if (n2 >= 14 ? n2 == n4 : (n4 < 14 ? n4 == n2 : this.this$0.m_expandedNameTable.getType(n4) == n2)) {
                return this.returnNode(n3);
            }
            return -1;
        }
    }

    public class RootIterator
    extends InternalAxisIteratorBase {
        private final DTMDefaultBaseIterators this$0;

        public RootIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (this._isRestartable) {
                this._startNode = this.this$0.getDocumentRoot(n2);
                this._currentNode = -1;
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            if (this._startNode == this._currentNode) {
                return -1;
            }
            this._currentNode = this._startNode;
            return this.returnNode(this._startNode);
        }
    }

    public class TypedNamespaceIterator
    extends NamespaceIterator {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedNamespaceIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public int next() {
            int n2 = this._currentNode;
            while (n2 != -1) {
                if (this.this$0.getExpandedTypeID(n2) == this._nodeType || this.this$0.getNodeType(n2) == this._nodeType || this.this$0.getNamespaceType(n2) == this._nodeType) {
                    this._currentNode = n2;
                    return this.returnNode(n2);
                }
                n2 = this.this$0.getNextNamespaceNode(this._startNode, n2, true);
            }
            this._currentNode = -1;
            return -1;
        }
    }

    public class NamespaceIterator
    extends InternalAxisIteratorBase {
        private final DTMDefaultBaseIterators this$0;

        public NamespaceIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = this.this$0.getFirstNamespaceNode(n2, true);
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            if (-1 != n2) {
                this._currentNode = this.this$0.getNextNamespaceNode(this._startNode, n2, true);
            }
            return this.returnNode(n2);
        }
    }

    public final class TypedChildrenIterator
    extends InternalAxisIteratorBase {
        private final int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public TypedChildrenIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators, int n2) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = n2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = n2 == -1 ? -1 : this.this$0._firstch(this.this$0.makeNodeIdentity(this._startNode));
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            int n3 = this._nodeType;
            if (n3 >= 14) {
                while (n2 != -1 && this.this$0._exptype(n2) != n3) {
                    n2 = this.this$0._nextsib(n2);
                }
            } else {
                int n4;
                while (n2 != -1 && !((n4 = this.this$0._exptype(n2)) < 14 ? n4 == n3 : this.this$0.m_expandedNameTable.getType(n4) == n3)) {
                    n2 = this.this$0._nextsib(n2);
                }
            }
            if (n2 == -1) {
                this._currentNode = -1;
                return -1;
            }
            this._currentNode = this.this$0._nextsib(n2);
            return this.returnNode(this.this$0.makeNodeHandle(n2));
        }
    }

    public final class ParentIterator
    extends InternalAxisIteratorBase {
        private int _nodeType;
        private final DTMDefaultBaseIterators this$0;

        public ParentIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
            this._nodeType = -1;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = this.this$0.getParent(n2);
                return this.resetPosition();
            }
            return this;
        }

        public DTMAxisIterator setNodeType(int n2) {
            this._nodeType = n2;
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            if (this._nodeType >= 14) {
                if (this._nodeType != this.this$0.getExpandedTypeID(this._currentNode)) {
                    n2 = -1;
                }
            } else if (this._nodeType != -1 && this._nodeType != this.this$0.getNodeType(this._currentNode)) {
                n2 = -1;
            }
            this._currentNode = -1;
            return this.returnNode(n2);
        }
    }

    public final class ChildrenIterator
    extends InternalAxisIteratorBase {
        private final DTMDefaultBaseIterators this$0;

        public ChildrenIterator(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            super(dTMDefaultBaseIterators);
            this.this$0 = dTMDefaultBaseIterators;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = n2 == -1 ? -1 : this.this$0._firstch(this.this$0.makeNodeIdentity(n2));
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            if (this._currentNode != -1) {
                int n2 = this._currentNode;
                this._currentNode = this.this$0._nextsib(n2);
                return this.returnNode(this.this$0.makeNodeHandle(n2));
            }
            return -1;
        }
    }

    public abstract class InternalAxisIteratorBase
    extends DTMAxisIteratorBase {
        protected int _currentNode;
        private final DTMDefaultBaseIterators this$0;

        public InternalAxisIteratorBase(DTMDefaultBaseIterators dTMDefaultBaseIterators) {
            this.this$0 = dTMDefaultBaseIterators;
        }

        public void setMark() {
            this._markedNode = this._currentNode;
        }

        public void gotoMark() {
            this._currentNode = this._markedNode;
        }
    }

}

