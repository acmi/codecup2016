/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import javax.xml.transform.Source;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBase;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLStringFactory;

public abstract class DTMDefaultBaseTraversers
extends DTMDefaultBase {
    public DTMDefaultBaseTraversers(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl) {
        super(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl);
    }

    public DTMDefaultBaseTraversers(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl, int n3, boolean bl2, boolean bl3) {
        super(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl, n3, bl2, bl3);
    }

    public DTMAxisTraverser getAxisTraverser(int n2) {
        DTMAxisTraverser dTMAxisTraverser;
        if (null == this.m_traversers) {
            this.m_traversers = new DTMAxisTraverser[Axis.getNamesLength()];
            dTMAxisTraverser = null;
        } else {
            dTMAxisTraverser = this.m_traversers[n2];
            if (dTMAxisTraverser != null) {
                return dTMAxisTraverser;
            }
        }
        switch (n2) {
            case 0: {
                dTMAxisTraverser = new AncestorTraverser(this, null);
                break;
            }
            case 1: {
                dTMAxisTraverser = new AncestorOrSelfTraverser(this, null);
                break;
            }
            case 2: {
                dTMAxisTraverser = new AttributeTraverser(this, null);
                break;
            }
            case 3: {
                dTMAxisTraverser = new ChildTraverser(this, null);
                break;
            }
            case 4: {
                dTMAxisTraverser = new DescendantTraverser(this, null);
                break;
            }
            case 5: {
                dTMAxisTraverser = new DescendantOrSelfTraverser(this, null);
                break;
            }
            case 6: {
                dTMAxisTraverser = new FollowingTraverser(this, null);
                break;
            }
            case 7: {
                dTMAxisTraverser = new FollowingSiblingTraverser(this, null);
                break;
            }
            case 9: {
                dTMAxisTraverser = new NamespaceTraverser(this, null);
                break;
            }
            case 8: {
                dTMAxisTraverser = new NamespaceDeclsTraverser(this, null);
                break;
            }
            case 10: {
                dTMAxisTraverser = new ParentTraverser(this, null);
                break;
            }
            case 11: {
                dTMAxisTraverser = new PrecedingTraverser(this, null);
                break;
            }
            case 12: {
                dTMAxisTraverser = new PrecedingSiblingTraverser(this, null);
                break;
            }
            case 13: {
                dTMAxisTraverser = new SelfTraverser(this, null);
                break;
            }
            case 16: {
                dTMAxisTraverser = new AllFromRootTraverser(this, null);
                break;
            }
            case 14: {
                dTMAxisTraverser = new AllFromNodeTraverser(this, null);
                break;
            }
            case 15: {
                dTMAxisTraverser = new PrecedingAndAncestorTraverser(this, null);
                break;
            }
            case 17: {
                dTMAxisTraverser = new DescendantFromRootTraverser(this, null);
                break;
            }
            case 18: {
                dTMAxisTraverser = new DescendantOrSelfFromRootTraverser(this, null);
                break;
            }
            case 19: {
                dTMAxisTraverser = new RootTraverser(this, null);
                break;
            }
            case 20: {
                return null;
            }
            default: {
                throw new DTMException(XMLMessages.createXMLMessage("ER_UNKNOWN_AXIS_TYPE", new Object[]{Integer.toString(n2)}));
            }
        }
        if (null == dTMAxisTraverser) {
            throw new DTMException(XMLMessages.createXMLMessage("ER_AXIS_TRAVERSER_NOT_SUPPORTED", new Object[]{Axis.getNames(n2)}));
        }
        this.m_traversers[n2] = dTMAxisTraverser;
        return dTMAxisTraverser;
    }

    static class 1 {
    }

    private class DescendantFromRootTraverser
    extends DescendantTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private DescendantFromRootTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            super(dTMDefaultBaseTraversers, null);
            this.this$0 = dTMDefaultBaseTraversers;
        }

        protected int getFirstPotential(int n2) {
            return this.this$0._firstch(0);
        }

        protected int getSubtreeRoot(int n2) {
            return 0;
        }

        public int first(int n2) {
            return this.this$0.makeNodeHandle(this.this$0._firstch(0));
        }

        public int first(int n2, int n3) {
            if (this.isIndexed(n3)) {
                int n4 = 0;
                int n5 = this.getFirstPotential(n4);
                return this.this$0.makeNodeHandle(this.getNextIndexed(n4, n5, n3));
            }
            int n6 = this.this$0.getDocumentRoot(n2);
            return this.next(n6, n6, n3);
        }

        DescendantFromRootTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class DescendantOrSelfFromRootTraverser
    extends DescendantTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private DescendantOrSelfFromRootTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            super(dTMDefaultBaseTraversers, null);
            this.this$0 = dTMDefaultBaseTraversers;
        }

        protected int getFirstPotential(int n2) {
            return n2;
        }

        protected int getSubtreeRoot(int n2) {
            return this.this$0.makeNodeIdentity(this.this$0.getDocument());
        }

        public int first(int n2) {
            return this.this$0.getDocumentRoot(n2);
        }

        public int first(int n2, int n3) {
            if (this.isIndexed(n3)) {
                int n4 = 0;
                int n5 = this.getFirstPotential(n4);
                return this.this$0.makeNodeHandle(this.getNextIndexed(n4, n5, n3));
            }
            int n6 = this.first(n2);
            return this.next(n6, n6, n3);
        }

        DescendantOrSelfFromRootTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class RootTraverser
    extends AllFromRootTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private RootTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            super(dTMDefaultBaseTraversers, null);
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int first(int n2, int n3) {
            int n4 = this.this$0.getDocumentRoot(n2);
            return this.this$0.getExpandedTypeID(n4) == n3 ? n4 : -1;
        }

        public int next(int n2, int n3) {
            return -1;
        }

        public int next(int n2, int n3, int n4) {
            return -1;
        }

        RootTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class AllFromRootTraverser
    extends AllFromNodeTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private AllFromRootTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            super(dTMDefaultBaseTraversers, null);
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int first(int n2) {
            return this.this$0.getDocumentRoot(n2);
        }

        public int first(int n2, int n3) {
            return this.this$0.getExpandedTypeID(this.this$0.getDocumentRoot(n2)) == n3 ? n2 : this.next(n2, n2, n3);
        }

        public int next(int n2, int n3) {
            int n4 = this.this$0.makeNodeIdentity(n2);
            short s2 = this.this$0._type(n3 = this.this$0.makeNodeIdentity(n3) + 1);
            if (s2 == -1) {
                return -1;
            }
            return this.this$0.makeNodeHandle(n3);
        }

        public int next(int n2, int n3, int n4) {
            int n5 = this.this$0.makeNodeIdentity(n2);
            n3 = this.this$0.makeNodeIdentity(n3) + 1;
            int n6;
            while ((n6 = this.this$0._exptype(n3)) != -1) {
                if (n6 == n4) {
                    return this.this$0.makeNodeHandle(n3);
                }
                ++n3;
            }
            return -1;
        }

        AllFromRootTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class SelfTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private SelfTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int first(int n2) {
            return n2;
        }

        public int first(int n2, int n3) {
            return this.this$0.getExpandedTypeID(n2) == n3 ? n2 : -1;
        }

        public int next(int n2, int n3) {
            return -1;
        }

        public int next(int n2, int n3, int n4) {
            return -1;
        }

        SelfTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class PrecedingSiblingTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private PrecedingSiblingTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int next(int n2, int n3) {
            return this.this$0.getPreviousSibling(n3);
        }

        public int next(int n2, int n3, int n4) {
            while (-1 != (n3 = this.this$0.getPreviousSibling(n3))) {
                if (this.this$0.getExpandedTypeID(n3) != n4) continue;
                return n3;
            }
            return -1;
        }

        PrecedingSiblingTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class PrecedingAndAncestorTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private PrecedingAndAncestorTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int next(int n2, int n3) {
            int n4 = this.this$0.makeNodeIdentity(n2);
            for (n3 = this.this$0.makeNodeIdentity((int)n3) - 1; n3 >= 0; --n3) {
                short s2 = this.this$0._type(n3);
                if (2 == s2 || 13 == s2) continue;
                return this.this$0.makeNodeHandle(n3);
            }
            return -1;
        }

        public int next(int n2, int n3, int n4) {
            int n5 = this.this$0.makeNodeIdentity(n2);
            for (n3 = this.this$0.makeNodeIdentity((int)n3) - 1; n3 >= 0; --n3) {
                int n6 = this.this$0.m_exptype.elementAt(n3);
                if (n6 != n4) continue;
                return this.this$0.makeNodeHandle(n3);
            }
            return -1;
        }

        PrecedingAndAncestorTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class PrecedingTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private PrecedingTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        protected boolean isAncestor(int n2, int n3) {
            n2 = this.this$0.m_parent.elementAt(n2);
            while (-1 != n2) {
                if (n2 == n3) {
                    return true;
                }
                n2 = this.this$0.m_parent.elementAt(n2);
            }
            return false;
        }

        public int next(int n2, int n3) {
            int n4 = this.this$0.makeNodeIdentity(n2);
            for (n3 = this.this$0.makeNodeIdentity((int)n3) - 1; n3 >= 0; --n3) {
                short s2 = this.this$0._type(n3);
                if (2 == s2 || 13 == s2 || this.isAncestor(n4, n3)) continue;
                return this.this$0.makeNodeHandle(n3);
            }
            return -1;
        }

        public int next(int n2, int n3, int n4) {
            int n5 = this.this$0.makeNodeIdentity(n2);
            for (n3 = this.this$0.makeNodeIdentity((int)n3) - 1; n3 >= 0; --n3) {
                int n6 = this.this$0.m_exptype.elementAt(n3);
                if (n6 != n4 || this.isAncestor(n5, n3)) continue;
                return this.this$0.makeNodeHandle(n3);
            }
            return -1;
        }

        PrecedingTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class ParentTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private ParentTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int first(int n2) {
            return this.this$0.getParent(n2);
        }

        public int first(int n2, int n3) {
            n2 = this.this$0.makeNodeIdentity(n2);
            while (-1 != (n2 = this.this$0.m_parent.elementAt(n2))) {
                if (this.this$0.m_exptype.elementAt(n2) != n3) continue;
                return this.this$0.makeNodeHandle(n2);
            }
            return -1;
        }

        public int next(int n2, int n3) {
            return -1;
        }

        public int next(int n2, int n3, int n4) {
            return -1;
        }

        ParentTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class NamespaceTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private NamespaceTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int next(int n2, int n3) {
            return n2 == n3 ? this.this$0.getFirstNamespaceNode(n2, true) : this.this$0.getNextNamespaceNode(n2, n3, true);
        }

        public int next(int n2, int n3, int n4) {
            int n5 = n3 = n2 == n3 ? this.this$0.getFirstNamespaceNode(n2, true) : this.this$0.getNextNamespaceNode(n2, n3, true);
            do {
                if (this.this$0.getExpandedTypeID(n3) != n4) continue;
                return n3;
            } while (-1 != (n3 = this.this$0.getNextNamespaceNode(n2, n3, true)));
            return -1;
        }

        NamespaceTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class NamespaceDeclsTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private NamespaceDeclsTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int next(int n2, int n3) {
            return n2 == n3 ? this.this$0.getFirstNamespaceNode(n2, false) : this.this$0.getNextNamespaceNode(n2, n3, false);
        }

        public int next(int n2, int n3, int n4) {
            int n5 = n3 = n2 == n3 ? this.this$0.getFirstNamespaceNode(n2, false) : this.this$0.getNextNamespaceNode(n2, n3, false);
            do {
                if (this.this$0.getExpandedTypeID(n3) != n4) continue;
                return n3;
            } while (-1 != (n3 = this.this$0.getNextNamespaceNode(n2, n3, false)));
            return -1;
        }

        NamespaceDeclsTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class FollowingSiblingTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private FollowingSiblingTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int next(int n2, int n3) {
            return this.this$0.getNextSibling(n3);
        }

        public int next(int n2, int n3, int n4) {
            while (-1 != (n3 = this.this$0.getNextSibling(n3))) {
                if (this.this$0.getExpandedTypeID(n3) != n4) continue;
                return n3;
            }
            return -1;
        }

        FollowingSiblingTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class FollowingTraverser
    extends DescendantTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private FollowingTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            super(dTMDefaultBaseTraversers, null);
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int first(int n2) {
            int n3;
            short s2 = this.this$0._type(n2 = this.this$0.makeNodeIdentity(n2));
            if ((2 == s2 || 13 == s2) && -1 != (n3 = this.this$0._firstch(n2 = this.this$0._parent(n2)))) {
                return this.this$0.makeNodeHandle(n3);
            }
            do {
                if (-1 != (n3 = this.this$0._nextsib(n2))) continue;
                n2 = this.this$0._parent(n2);
            } while (-1 == n3 && -1 != n2);
            return this.this$0.makeNodeHandle(n3);
        }

        public int first(int n2, int n3) {
            int n4;
            short s2 = this.this$0.getNodeType(n2);
            if ((2 == s2 || 13 == s2) && -1 != (n4 = this.this$0.getFirstChild(n2 = this.this$0.getParent(n2)))) {
                if (this.this$0.getExpandedTypeID(n4) == n3) {
                    return n4;
                }
                return this.next(n2, n4, n3);
            }
            do {
                if (-1 != (n4 = this.this$0.getNextSibling(n2))) {
                    if (this.this$0.getExpandedTypeID(n4) == n3) {
                        return n4;
                    }
                    return this.next(n2, n4, n3);
                }
                n2 = this.this$0.getParent(n2);
            } while (-1 == n4 && -1 != n2);
            return n4;
        }

        public int next(int n2, int n3) {
            short s2;
            n3 = this.this$0.makeNodeIdentity(n3);
            do {
                if (-1 != (s2 = this.this$0._type(++n3))) continue;
                return -1;
            } while (2 == s2 || 13 == s2);
            return this.this$0.makeNodeHandle(n3);
        }

        public int next(int n2, int n3, int n4) {
            int n5;
            n3 = this.this$0.makeNodeIdentity(n3);
            do {
                if (-1 != (n5 = this.this$0._exptype(++n3))) continue;
                return -1;
            } while (n5 != n4);
            return this.this$0.makeNodeHandle(n3);
        }

        FollowingTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class AllFromNodeTraverser
    extends DescendantOrSelfTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private AllFromNodeTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            super(dTMDefaultBaseTraversers, null);
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int next(int n2, int n3) {
            int n4 = this.this$0.makeNodeIdentity(n2);
            n3 = this.this$0.makeNodeIdentity(n3) + 1;
            this.this$0._exptype(n3);
            if (!this.isDescendant(n4, n3)) {
                return -1;
            }
            return this.this$0.makeNodeHandle(n3);
        }

        AllFromNodeTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class DescendantOrSelfTraverser
    extends DescendantTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private DescendantOrSelfTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            super(dTMDefaultBaseTraversers, null);
            this.this$0 = dTMDefaultBaseTraversers;
        }

        protected int getFirstPotential(int n2) {
            return n2;
        }

        public int first(int n2) {
            return n2;
        }

        DescendantOrSelfTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class DescendantTraverser
    extends IndexedDTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private DescendantTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            super(dTMDefaultBaseTraversers, null);
            this.this$0 = dTMDefaultBaseTraversers;
        }

        protected int getFirstPotential(int n2) {
            return n2 + 1;
        }

        protected boolean axisHasBeenProcessed(int n2) {
            return this.this$0.m_nextsib.elementAt(n2) != -2;
        }

        protected int getSubtreeRoot(int n2) {
            return this.this$0.makeNodeIdentity(n2);
        }

        protected boolean isDescendant(int n2, int n3) {
            return this.this$0._parent(n3) >= n2;
        }

        protected boolean isAfterAxis(int n2, int n3) {
            do {
                if (n3 != n2) continue;
                return false;
            } while ((n3 = this.this$0.m_parent.elementAt(n3)) >= n2);
            return true;
        }

        public int first(int n2, int n3) {
            if (this.isIndexed(n3)) {
                int n4 = this.getSubtreeRoot(n2);
                int n5 = this.getFirstPotential(n4);
                return this.this$0.makeNodeHandle(this.getNextIndexed(n4, n5, n3));
            }
            return this.next(n2, n2, n3);
        }

        public int next(int n2, int n3) {
            int n4 = this.getSubtreeRoot(n2);
            n3 = this.this$0.makeNodeIdentity(n3) + 1;
            do {
                short s2 = this.this$0._type(n3);
                if (!this.isDescendant(n4, n3)) {
                    return -1;
                }
                if (2 != s2 && 13 != s2) {
                    return this.this$0.makeNodeHandle(n3);
                }
                ++n3;
            } while (true);
        }

        public int next(int n2, int n3, int n4) {
            int n5 = this.getSubtreeRoot(n2);
            n3 = this.this$0.makeNodeIdentity(n3) + 1;
            if (this.isIndexed(n4)) {
                return this.this$0.makeNodeHandle(this.getNextIndexed(n5, n3, n4));
            }
            do {
                int n6 = this.this$0._exptype(n3);
                if (!this.isDescendant(n5, n3)) {
                    return -1;
                }
                if (n6 == n4) {
                    return this.this$0.makeNodeHandle(n3);
                }
                ++n3;
            } while (true);
        }

        DescendantTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private abstract class IndexedDTMAxisTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private IndexedDTMAxisTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        protected final boolean isIndexed(int n2) {
            return this.this$0.m_indexing && 1 == this.this$0.m_expandedNameTable.getType(n2);
        }

        protected abstract boolean isAfterAxis(int var1, int var2);

        protected abstract boolean axisHasBeenProcessed(int var1);

        protected int getNextIndexed(int n2, int n3, int n4) {
            int n5 = this.this$0.m_expandedNameTable.getNamespaceID(n4);
            int n6 = this.this$0.m_expandedNameTable.getLocalNameID(n4);
            do {
                int n7;
                if (-2 != (n7 = this.this$0.findElementFromIndex(n5, n6, n3))) {
                    if (this.isAfterAxis(n2, n7)) {
                        return -1;
                    }
                    return n7;
                }
                if (this.axisHasBeenProcessed(n2)) break;
                this.this$0.nextNode();
            } while (true);
            return -1;
        }

        IndexedDTMAxisTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class ChildTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private ChildTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        protected int getNextIndexed(int n2, int n3, int n4) {
            int n5 = this.this$0.m_expandedNameTable.getNamespaceID(n4);
            int n6 = this.this$0.m_expandedNameTable.getLocalNameID(n4);
            do {
                int n7;
                if (-2 != (n7 = this.this$0.findElementFromIndex(n5, n6, n3))) {
                    int n8 = this.this$0.m_parent.elementAt(n7);
                    if (n8 == n2) {
                        return n7;
                    }
                    if (n8 < n2) {
                        return -1;
                    }
                    do {
                        if ((n8 = this.this$0.m_parent.elementAt(n8)) >= n2) continue;
                        return -1;
                    } while (n8 > n2);
                    n3 = n7 + 1;
                    continue;
                }
                this.this$0.nextNode();
                if (this.this$0.m_nextsib.elementAt(n2) != -2) break;
            } while (true);
            return -1;
        }

        public int first(int n2) {
            return this.this$0.getFirstChild(n2);
        }

        public int first(int n2, int n3) {
            int n4 = this.this$0.makeNodeIdentity(n2);
            int n5 = this.getNextIndexed(n4, this.this$0._firstch(n4), n3);
            return this.this$0.makeNodeHandle(n5);
        }

        public int next(int n2, int n3) {
            return this.this$0.getNextSibling(n3);
        }

        public int next(int n2, int n3, int n4) {
            n3 = this.this$0._nextsib(this.this$0.makeNodeIdentity(n3));
            while (-1 != n3) {
                if (this.this$0.m_exptype.elementAt(n3) == n4) {
                    return this.this$0.makeNodeHandle(n3);
                }
                n3 = this.this$0._nextsib(n3);
            }
            return -1;
        }

        ChildTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class AttributeTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private AttributeTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int next(int n2, int n3) {
            return n2 == n3 ? this.this$0.getFirstAttribute(n2) : this.this$0.getNextAttribute(n3);
        }

        public int next(int n2, int n3, int n4) {
            int n5 = n3 = n2 == n3 ? this.this$0.getFirstAttribute(n2) : this.this$0.getNextAttribute(n3);
            do {
                if (this.this$0.getExpandedTypeID(n3) != n4) continue;
                return n3;
            } while (-1 != (n3 = this.this$0.getNextAttribute(n3)));
            return -1;
        }

        AttributeTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class AncestorOrSelfTraverser
    extends AncestorTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private AncestorOrSelfTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            super(dTMDefaultBaseTraversers, null);
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int first(int n2) {
            return n2;
        }

        public int first(int n2, int n3) {
            return this.this$0.getExpandedTypeID(n2) == n3 ? n2 : this.next(n2, n2, n3);
        }

        AncestorOrSelfTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

    private class AncestorTraverser
    extends DTMAxisTraverser {
        private final DTMDefaultBaseTraversers this$0;

        private AncestorTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers) {
            this.this$0 = dTMDefaultBaseTraversers;
        }

        public int next(int n2, int n3) {
            return this.this$0.getParent(n3);
        }

        public int next(int n2, int n3, int n4) {
            n3 = this.this$0.makeNodeIdentity(n3);
            while (-1 != (n3 = this.this$0.m_parent.elementAt(n3))) {
                if (this.this$0.m_exptype.elementAt(n3) != n4) continue;
                return this.this$0.makeNodeHandle(n3);
            }
            return -1;
        }

        AncestorTraverser(DTMDefaultBaseTraversers dTMDefaultBaseTraversers, 1 var2_2) {
            this(dTMDefaultBaseTraversers);
        }
    }

}

