/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.CharacterDataImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.LCount;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.NodeIteratorImpl;
import org.apache.xerces.dom.RangeImpl;
import org.apache.xerces.dom.TreeWalkerImpl;
import org.apache.xerces.dom.events.EventImpl;
import org.apache.xerces.dom.events.MouseEventImpl;
import org.apache.xerces.dom.events.MutationEventImpl;
import org.apache.xerces.dom.events.UIEventImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.ranges.DocumentRange;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;

public class DocumentImpl
extends CoreDocumentImpl
implements DocumentEvent,
DocumentRange,
DocumentTraversal {
    static final long serialVersionUID = 515687835542616694L;
    protected transient List iterators;
    protected transient ReferenceQueue iteratorReferenceQueue;
    protected transient List ranges;
    protected transient ReferenceQueue rangeReferenceQueue;
    protected Hashtable eventListeners;
    protected boolean mutationEvents = false;
    EnclosingAttr savedEnclosingAttr;

    public DocumentImpl() {
    }

    public DocumentImpl(boolean bl) {
        super(bl);
    }

    public DocumentImpl(DocumentType documentType) {
        super(documentType);
    }

    public DocumentImpl(DocumentType documentType, boolean bl) {
        super(documentType, bl);
    }

    public Node cloneNode(boolean bl) {
        DocumentImpl documentImpl = new DocumentImpl();
        this.callUserDataHandlers(this, documentImpl, 1);
        this.cloneNode(documentImpl, bl);
        documentImpl.mutationEvents = this.mutationEvents;
        return documentImpl;
    }

    public DOMImplementation getImplementation() {
        return DOMImplementationImpl.getDOMImplementation();
    }

    public NodeIterator createNodeIterator(Node node, short s2, NodeFilter nodeFilter) {
        return this.createNodeIterator(node, s2, nodeFilter, true);
    }

    public NodeIterator createNodeIterator(Node node, int n2, NodeFilter nodeFilter, boolean bl) {
        if (node == null) {
            String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
            throw new DOMException(9, string);
        }
        NodeIteratorImpl nodeIteratorImpl = new NodeIteratorImpl(this, node, n2, nodeFilter, bl);
        if (this.iterators == null) {
            this.iterators = new LinkedList();
            this.iteratorReferenceQueue = new ReferenceQueue();
        }
        this.removeStaleIteratorReferences();
        this.iterators.add(new WeakReference<NodeIteratorImpl>(nodeIteratorImpl, this.iteratorReferenceQueue));
        return nodeIteratorImpl;
    }

    public TreeWalker createTreeWalker(Node node, short s2, NodeFilter nodeFilter) {
        return this.createTreeWalker(node, s2, nodeFilter, true);
    }

    public TreeWalker createTreeWalker(Node node, int n2, NodeFilter nodeFilter, boolean bl) {
        if (node == null) {
            String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
            throw new DOMException(9, string);
        }
        return new TreeWalkerImpl(node, n2, nodeFilter, bl);
    }

    void removeNodeIterator(NodeIterator nodeIterator) {
        if (nodeIterator == null) {
            return;
        }
        if (this.iterators == null) {
            return;
        }
        this.removeStaleIteratorReferences();
        Iterator iterator = this.iterators.iterator();
        while (iterator.hasNext()) {
            Object t2 = ((Reference)iterator.next()).get();
            if (t2 == nodeIterator) {
                iterator.remove();
                return;
            }
            if (t2 != null) continue;
            iterator.remove();
        }
    }

    private void removeStaleIteratorReferences() {
        this.removeStaleReferences(this.iteratorReferenceQueue, this.iterators);
    }

    private void removeStaleReferences(ReferenceQueue referenceQueue, List list) {
        Reference reference = referenceQueue.poll();
        int n2 = 0;
        while (reference != null) {
            ++n2;
            reference = referenceQueue.poll();
        }
        if (n2 > 0) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Object t2 = ((Reference)iterator.next()).get();
                if (t2 != null) continue;
                iterator.remove();
                if (--n2 > 0) continue;
                return;
            }
        }
    }

    public Range createRange() {
        if (this.ranges == null) {
            this.ranges = new LinkedList();
            this.rangeReferenceQueue = new ReferenceQueue();
        }
        RangeImpl rangeImpl = new RangeImpl(this);
        this.removeStaleRangeReferences();
        this.ranges.add(new WeakReference<RangeImpl>(rangeImpl, this.rangeReferenceQueue));
        return rangeImpl;
    }

    void removeRange(Range range) {
        if (range == null) {
            return;
        }
        if (this.ranges == null) {
            return;
        }
        this.removeStaleRangeReferences();
        Iterator iterator = this.ranges.iterator();
        while (iterator.hasNext()) {
            Object t2 = ((Reference)iterator.next()).get();
            if (t2 == range) {
                iterator.remove();
                return;
            }
            if (t2 != null) continue;
            iterator.remove();
        }
    }

    void replacedText(CharacterDataImpl characterDataImpl) {
        if (this.ranges != null) {
            this.notifyRangesReplacedText(characterDataImpl);
        }
    }

    private void notifyRangesReplacedText(CharacterDataImpl characterDataImpl) {
        this.removeStaleRangeReferences();
        Iterator iterator = this.ranges.iterator();
        while (iterator.hasNext()) {
            RangeImpl rangeImpl = (RangeImpl)((Reference)iterator.next()).get();
            if (rangeImpl != null) {
                rangeImpl.receiveReplacedText(characterDataImpl);
                continue;
            }
            iterator.remove();
        }
    }

    void deletedText(CharacterDataImpl characterDataImpl, int n2, int n3) {
        if (this.ranges != null) {
            this.notifyRangesDeletedText(characterDataImpl, n2, n3);
        }
    }

    private void notifyRangesDeletedText(CharacterDataImpl characterDataImpl, int n2, int n3) {
        this.removeStaleRangeReferences();
        Iterator iterator = this.ranges.iterator();
        while (iterator.hasNext()) {
            RangeImpl rangeImpl = (RangeImpl)((Reference)iterator.next()).get();
            if (rangeImpl != null) {
                rangeImpl.receiveDeletedText(characterDataImpl, n2, n3);
                continue;
            }
            iterator.remove();
        }
    }

    void insertedText(CharacterDataImpl characterDataImpl, int n2, int n3) {
        if (this.ranges != null) {
            this.notifyRangesInsertedText(characterDataImpl, n2, n3);
        }
    }

    private void notifyRangesInsertedText(CharacterDataImpl characterDataImpl, int n2, int n3) {
        this.removeStaleRangeReferences();
        Iterator iterator = this.ranges.iterator();
        while (iterator.hasNext()) {
            RangeImpl rangeImpl = (RangeImpl)((Reference)iterator.next()).get();
            if (rangeImpl != null) {
                rangeImpl.receiveInsertedText(characterDataImpl, n2, n3);
                continue;
            }
            iterator.remove();
        }
    }

    void splitData(Node node, Node node2, int n2) {
        if (this.ranges != null) {
            this.notifyRangesSplitData(node, node2, n2);
        }
    }

    private void notifyRangesSplitData(Node node, Node node2, int n2) {
        this.removeStaleRangeReferences();
        Iterator iterator = this.ranges.iterator();
        while (iterator.hasNext()) {
            RangeImpl rangeImpl = (RangeImpl)((Reference)iterator.next()).get();
            if (rangeImpl != null) {
                rangeImpl.receiveSplitData(node, node2, n2);
                continue;
            }
            iterator.remove();
        }
    }

    private void removeStaleRangeReferences() {
        this.removeStaleReferences(this.rangeReferenceQueue, this.ranges);
    }

    public Event createEvent(String string) throws DOMException {
        if (string.equalsIgnoreCase("Events") || "Event".equals(string)) {
            return new EventImpl();
        }
        if (string.equalsIgnoreCase("MutationEvents") || "MutationEvent".equals(string)) {
            return new MutationEventImpl();
        }
        if (string.equalsIgnoreCase("UIEvents") || "UIEvent".equals(string)) {
            return new UIEventImpl();
        }
        if (string.equalsIgnoreCase("MouseEvents") || "MouseEvent".equals(string)) {
            return new MouseEventImpl();
        }
        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string2);
    }

    void setMutationEvents(boolean bl) {
        this.mutationEvents = bl;
    }

    boolean getMutationEvents() {
        return this.mutationEvents;
    }

    protected void setEventListeners(NodeImpl nodeImpl, Vector vector) {
        if (this.eventListeners == null) {
            this.eventListeners = new Hashtable();
        }
        if (vector == null) {
            this.eventListeners.remove(nodeImpl);
            if (this.eventListeners.isEmpty()) {
                this.mutationEvents = false;
            }
        } else {
            this.eventListeners.put(nodeImpl, vector);
            this.mutationEvents = true;
        }
    }

    protected Vector getEventListeners(NodeImpl nodeImpl) {
        if (this.eventListeners == null) {
            return null;
        }
        return (Vector)this.eventListeners.get(nodeImpl);
    }

    protected void addEventListener(NodeImpl nodeImpl, String string, EventListener eventListener, boolean bl) {
        if (string == null || string.length() == 0 || eventListener == null) {
            return;
        }
        this.removeEventListener(nodeImpl, string, eventListener, bl);
        Vector<LEntry> vector = this.getEventListeners(nodeImpl);
        if (vector == null) {
            vector = new Vector<LEntry>();
            this.setEventListeners(nodeImpl, vector);
        }
        vector.addElement(new LEntry(this, string, eventListener, bl));
        LCount lCount = LCount.lookup(string);
        if (bl) {
            ++lCount.captures;
            ++lCount.total;
        } else {
            ++lCount.bubbles;
            ++lCount.total;
        }
    }

    protected void removeEventListener(NodeImpl nodeImpl, String string, EventListener eventListener, boolean bl) {
        if (string == null || string.length() == 0 || eventListener == null) {
            return;
        }
        Vector vector = this.getEventListeners(nodeImpl);
        if (vector == null) {
            return;
        }
        int n2 = vector.size() - 1;
        while (n2 >= 0) {
            LEntry lEntry = (LEntry)vector.elementAt(n2);
            if (lEntry.useCapture == bl && lEntry.listener == eventListener && lEntry.type.equals(string)) {
                vector.removeElementAt(n2);
                if (vector.size() == 0) {
                    this.setEventListeners(nodeImpl, null);
                }
                LCount lCount = LCount.lookup(string);
                if (bl) {
                    --lCount.captures;
                    --lCount.total;
                    break;
                }
                --lCount.bubbles;
                --lCount.total;
                break;
            }
            --n2;
        }
    }

    protected void copyEventListeners(NodeImpl nodeImpl, NodeImpl nodeImpl2) {
        Vector vector = this.getEventListeners(nodeImpl);
        if (vector == null) {
            return;
        }
        this.setEventListeners(nodeImpl2, (Vector)vector.clone());
    }

    protected boolean dispatchEvent(NodeImpl nodeImpl, Event event) {
        Vector vector;
        if (event == null) {
            return false;
        }
        EventImpl eventImpl = (EventImpl)event;
        if (!eventImpl.initialized || eventImpl.type == null || eventImpl.type.length() == 0) {
            String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "UNSPECIFIED_EVENT_TYPE_ERR", null);
            throw new EventException(0, string);
        }
        LCount lCount = LCount.lookup(eventImpl.getType());
        if (lCount.total == 0) {
            return eventImpl.preventDefault;
        }
        eventImpl.target = nodeImpl;
        eventImpl.stopPropagation = false;
        eventImpl.preventDefault = false;
        ArrayList<Node> arrayList = new ArrayList<Node>(10);
        Node node = nodeImpl;
        Node node2 = node.getParentNode();
        while (node2 != null) {
            arrayList.add(node2);
            node = node2;
            node2 = node2.getParentNode();
        }
        if (lCount.captures > 0) {
            eventImpl.eventPhase = 1;
            int n2 = arrayList.size() - 1;
            while (n2 >= 0) {
                if (eventImpl.stopPropagation) break;
                NodeImpl nodeImpl2 = (NodeImpl)arrayList.get(n2);
                eventImpl.currentTarget = nodeImpl2;
                Vector n8 = this.getEventListeners(nodeImpl2);
                if (n8 != null) {
                    vector = (Vector)n8.clone();
                    int vector4 = vector.size();
                    int exception = 0;
                    while (exception < vector4) {
                        LEntry lEntry = (LEntry)vector.elementAt(exception);
                        if (lEntry.useCapture && lEntry.type.equals(eventImpl.type) && n8.contains(lEntry)) {
                            try {
                                lEntry.listener.handleEvent(eventImpl);
                            }
                            catch (Exception exception2) {
                                // empty catch block
                            }
                        }
                        ++exception;
                    }
                }
                --n2;
            }
        }
        if (lCount.bubbles > 0) {
            eventImpl.eventPhase = 2;
            eventImpl.currentTarget = nodeImpl;
            Vector vector3 = this.getEventListeners(nodeImpl);
            if (!eventImpl.stopPropagation && vector3 != null) {
                Vector n7 = (Vector)vector3.clone();
                int n2 = n7.size();
                int n3 = 0;
                while (n3 < n2) {
                    LEntry lEntry = (LEntry)n7.elementAt(n3);
                    if (!lEntry.useCapture && lEntry.type.equals(eventImpl.type) && vector3.contains(lEntry)) {
                        try {
                            lEntry.listener.handleEvent(eventImpl);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                    ++n3;
                }
            }
            if (eventImpl.bubbles) {
                eventImpl.eventPhase = 3;
                int n4 = arrayList.size();
                int n5 = 0;
                while (n5 < n4) {
                    if (eventImpl.stopPropagation) break;
                    vector = (NodeImpl)arrayList.get(n5);
                    eventImpl.currentTarget = vector;
                    vector3 = this.getEventListeners((NodeImpl)((Object)vector));
                    if (vector3 != null) {
                        Vector vector2 = (Vector)vector3.clone();
                        int n6 = vector2.size();
                        int n7 = 0;
                        while (n7 < n6) {
                            LEntry lEntry = (LEntry)vector2.elementAt(n7);
                            if (!lEntry.useCapture && lEntry.type.equals(eventImpl.type) && vector3.contains(lEntry)) {
                                try {
                                    lEntry.listener.handleEvent(eventImpl);
                                }
                                catch (Exception exception) {
                                    // empty catch block
                                }
                            }
                            ++n7;
                        }
                    }
                    ++n5;
                }
            }
        }
        if (lCount.defaults <= 0 || !eventImpl.cancelable || !eventImpl.preventDefault) {
            // empty if block
        }
        return eventImpl.preventDefault;
    }

    protected void dispatchEventToSubtree(Node node, Event event) {
        ((NodeImpl)node).dispatchEvent(event);
        if (node.getNodeType() == 1) {
            NamedNodeMap namedNodeMap = node.getAttributes();
            int n2 = namedNodeMap.getLength() - 1;
            while (n2 >= 0) {
                this.dispatchingEventToSubtree(namedNodeMap.item(n2), event);
                --n2;
            }
        }
        this.dispatchingEventToSubtree(node.getFirstChild(), event);
    }

    protected void dispatchingEventToSubtree(Node node, Event event) {
        if (node == null) {
            return;
        }
        ((NodeImpl)node).dispatchEvent(event);
        if (node.getNodeType() == 1) {
            NamedNodeMap namedNodeMap = node.getAttributes();
            int n2 = namedNodeMap.getLength() - 1;
            while (n2 >= 0) {
                this.dispatchingEventToSubtree(namedNodeMap.item(n2), event);
                --n2;
            }
        }
        this.dispatchingEventToSubtree(node.getFirstChild(), event);
        this.dispatchingEventToSubtree(node.getNextSibling(), event);
    }

    protected void dispatchAggregateEvents(NodeImpl nodeImpl, EnclosingAttr enclosingAttr) {
        if (enclosingAttr != null) {
            this.dispatchAggregateEvents(nodeImpl, enclosingAttr.node, enclosingAttr.oldvalue, 1);
        } else {
            this.dispatchAggregateEvents(nodeImpl, null, null, 0);
        }
    }

    protected void dispatchAggregateEvents(NodeImpl nodeImpl, AttrImpl attrImpl, String string, short s2) {
        MutationEventImpl mutationEventImpl;
        LCount lCount;
        NodeImpl nodeImpl2 = null;
        if (attrImpl != null) {
            lCount = LCount.lookup("DOMAttrModified");
            nodeImpl2 = (NodeImpl)((Object)attrImpl.getOwnerElement());
            if (lCount.total > 0 && nodeImpl2 != null) {
                mutationEventImpl = new MutationEventImpl();
                mutationEventImpl.initMutationEvent("DOMAttrModified", true, false, attrImpl, string, attrImpl.getNodeValue(), attrImpl.getNodeName(), s2);
                nodeImpl2.dispatchEvent(mutationEventImpl);
            }
        }
        lCount = LCount.lookup("DOMSubtreeModified");
        if (lCount.total > 0) {
            mutationEventImpl = new MutationEventImpl();
            mutationEventImpl.initMutationEvent("DOMSubtreeModified", true, false, null, null, null, null, 0);
            if (attrImpl != null) {
                this.dispatchEvent(attrImpl, mutationEventImpl);
                if (nodeImpl2 != null) {
                    this.dispatchEvent(nodeImpl2, mutationEventImpl);
                }
            } else {
                this.dispatchEvent(nodeImpl, mutationEventImpl);
            }
        }
    }

    protected void saveEnclosingAttr(NodeImpl nodeImpl) {
        this.savedEnclosingAttr = null;
        LCount lCount = LCount.lookup("DOMAttrModified");
        if (lCount.total > 0) {
            NodeImpl nodeImpl2 = nodeImpl;
            do {
                if (nodeImpl2 == null) {
                    return;
                }
                short s2 = nodeImpl2.getNodeType();
                if (s2 == 2) {
                    EnclosingAttr enclosingAttr = new EnclosingAttr(this);
                    enclosingAttr.node = (AttrImpl)nodeImpl2;
                    enclosingAttr.oldvalue = enclosingAttr.node.getNodeValue();
                    this.savedEnclosingAttr = enclosingAttr;
                    return;
                }
                if (s2 == 5) {
                    nodeImpl2 = nodeImpl2.parentNode();
                    continue;
                }
                if (s2 != 3) break;
                nodeImpl2 = nodeImpl2.parentNode();
            } while (true);
            return;
        }
    }

    void modifyingCharacterData(NodeImpl nodeImpl, boolean bl) {
        if (this.mutationEvents && !bl) {
            this.saveEnclosingAttr(nodeImpl);
        }
    }

    void modifiedCharacterData(NodeImpl nodeImpl, String string, String string2, boolean bl) {
        if (this.mutationEvents) {
            this.mutationEventsModifiedCharacterData(nodeImpl, string, string2, bl);
        }
    }

    private void mutationEventsModifiedCharacterData(NodeImpl nodeImpl, String string, String string2, boolean bl) {
        if (!bl) {
            LCount lCount = LCount.lookup("DOMCharacterDataModified");
            if (lCount.total > 0) {
                MutationEventImpl mutationEventImpl = new MutationEventImpl();
                mutationEventImpl.initMutationEvent("DOMCharacterDataModified", true, false, null, string, string2, null, 0);
                this.dispatchEvent(nodeImpl, mutationEventImpl);
            }
            this.dispatchAggregateEvents(nodeImpl, this.savedEnclosingAttr);
        }
    }

    void replacedCharacterData(NodeImpl nodeImpl, String string, String string2) {
        this.modifiedCharacterData(nodeImpl, string, string2, false);
    }

    void insertingNode(NodeImpl nodeImpl, boolean bl) {
        if (this.mutationEvents && !bl) {
            this.saveEnclosingAttr(nodeImpl);
        }
    }

    void insertedNode(NodeImpl nodeImpl, NodeImpl nodeImpl2, boolean bl) {
        if (this.mutationEvents) {
            this.mutationEventsInsertedNode(nodeImpl, nodeImpl2, bl);
        }
        if (this.ranges != null) {
            this.notifyRangesInsertedNode(nodeImpl2);
        }
    }

    private void mutationEventsInsertedNode(NodeImpl nodeImpl, NodeImpl nodeImpl2, boolean bl) {
        Object object;
        LCount lCount = LCount.lookup("DOMNodeInserted");
        if (lCount.total > 0) {
            object = new MutationEventImpl();
            object.initMutationEvent("DOMNodeInserted", true, false, nodeImpl, null, null, null, 0);
            this.dispatchEvent(nodeImpl2, (Event)object);
        }
        lCount = LCount.lookup("DOMNodeInsertedIntoDocument");
        if (lCount.total > 0) {
            object = nodeImpl;
            if (this.savedEnclosingAttr != null) {
                object = (NodeImpl)((Object)this.savedEnclosingAttr.node.getOwnerElement());
            }
            if (object != null) {
                Object object2 = object;
                while (object2 != null) {
                    object = object2;
                    object2 = object2.getNodeType() == 2 ? (NodeImpl)((Object)((AttrImpl)object2).getOwnerElement()) : object2.parentNode();
                }
                if (object.getNodeType() == 9) {
                    MutationEventImpl mutationEventImpl = new MutationEventImpl();
                    mutationEventImpl.initMutationEvent("DOMNodeInsertedIntoDocument", false, false, null, null, null, null, 0);
                    this.dispatchEventToSubtree(nodeImpl2, mutationEventImpl);
                }
            }
        }
        if (!bl) {
            this.dispatchAggregateEvents(nodeImpl, this.savedEnclosingAttr);
        }
    }

    private void notifyRangesInsertedNode(NodeImpl nodeImpl) {
        this.removeStaleRangeReferences();
        Iterator iterator = this.ranges.iterator();
        while (iterator.hasNext()) {
            RangeImpl rangeImpl = (RangeImpl)((Reference)iterator.next()).get();
            if (rangeImpl != null) {
                rangeImpl.insertedNodeFromDOM(nodeImpl);
                continue;
            }
            iterator.remove();
        }
    }

    void removingNode(NodeImpl nodeImpl, NodeImpl nodeImpl2, boolean bl) {
        if (this.iterators != null) {
            this.notifyIteratorsRemovingNode(nodeImpl2);
        }
        if (this.ranges != null) {
            this.notifyRangesRemovingNode(nodeImpl2);
        }
        if (this.mutationEvents) {
            this.mutationEventsRemovingNode(nodeImpl, nodeImpl2, bl);
        }
    }

    private void notifyIteratorsRemovingNode(NodeImpl nodeImpl) {
        this.removeStaleIteratorReferences();
        Iterator iterator = this.iterators.iterator();
        while (iterator.hasNext()) {
            NodeIteratorImpl nodeIteratorImpl = (NodeIteratorImpl)((Reference)iterator.next()).get();
            if (nodeIteratorImpl != null) {
                nodeIteratorImpl.removeNode(nodeImpl);
                continue;
            }
            iterator.remove();
        }
    }

    private void notifyRangesRemovingNode(NodeImpl nodeImpl) {
        this.removeStaleRangeReferences();
        Iterator iterator = this.ranges.iterator();
        while (iterator.hasNext()) {
            RangeImpl rangeImpl = (RangeImpl)((Reference)iterator.next()).get();
            if (rangeImpl != null) {
                rangeImpl.removeNode(nodeImpl);
                continue;
            }
            iterator.remove();
        }
    }

    private void mutationEventsRemovingNode(NodeImpl nodeImpl, NodeImpl nodeImpl2, boolean bl) {
        Object object;
        if (!bl) {
            this.saveEnclosingAttr(nodeImpl);
        }
        LCount lCount = LCount.lookup("DOMNodeRemoved");
        if (lCount.total > 0) {
            object = new MutationEventImpl();
            object.initMutationEvent("DOMNodeRemoved", true, false, nodeImpl, null, null, null, 0);
            this.dispatchEvent(nodeImpl2, (Event)object);
        }
        lCount = LCount.lookup("DOMNodeRemovedFromDocument");
        if (lCount.total > 0) {
            object = this;
            if (this.savedEnclosingAttr != null) {
                object = (NodeImpl)((Object)this.savedEnclosingAttr.node.getOwnerElement());
            }
            if (object != null) {
                NodeImpl nodeImpl3 = object.parentNode();
                while (nodeImpl3 != null) {
                    object = nodeImpl3;
                    nodeImpl3 = nodeImpl3.parentNode();
                }
                if (object.getNodeType() == 9) {
                    MutationEventImpl mutationEventImpl = new MutationEventImpl();
                    mutationEventImpl.initMutationEvent("DOMNodeRemovedFromDocument", false, false, null, null, null, null, 0);
                    this.dispatchEventToSubtree(nodeImpl2, mutationEventImpl);
                }
            }
        }
    }

    void removedNode(NodeImpl nodeImpl, boolean bl) {
        if (this.mutationEvents && !bl) {
            this.dispatchAggregateEvents(nodeImpl, this.savedEnclosingAttr);
        }
    }

    void replacingNode(NodeImpl nodeImpl) {
        if (this.mutationEvents) {
            this.saveEnclosingAttr(nodeImpl);
        }
    }

    void replacingData(NodeImpl nodeImpl) {
        if (this.mutationEvents) {
            this.saveEnclosingAttr(nodeImpl);
        }
    }

    void replacedNode(NodeImpl nodeImpl) {
        if (this.mutationEvents) {
            this.dispatchAggregateEvents(nodeImpl, this.savedEnclosingAttr);
        }
    }

    void modifiedAttrValue(AttrImpl attrImpl, String string) {
        if (this.mutationEvents) {
            this.dispatchAggregateEvents(attrImpl, attrImpl, string, 1);
        }
    }

    void setAttrNode(AttrImpl attrImpl, AttrImpl attrImpl2) {
        if (this.mutationEvents) {
            if (attrImpl2 == null) {
                this.dispatchAggregateEvents(attrImpl.ownerNode, attrImpl, null, 2);
            } else {
                this.dispatchAggregateEvents(attrImpl.ownerNode, attrImpl, attrImpl2.getNodeValue(), 1);
            }
        }
    }

    void removedAttrNode(AttrImpl attrImpl, NodeImpl nodeImpl, String string) {
        if (this.mutationEvents) {
            this.mutationEventsRemovedAttrNode(attrImpl, nodeImpl, string);
        }
    }

    private void mutationEventsRemovedAttrNode(AttrImpl attrImpl, NodeImpl nodeImpl, String string) {
        LCount lCount = LCount.lookup("DOMAttrModified");
        if (lCount.total > 0) {
            MutationEventImpl mutationEventImpl = new MutationEventImpl();
            mutationEventImpl.initMutationEvent("DOMAttrModified", true, false, attrImpl, attrImpl.getNodeValue(), null, string, 3);
            this.dispatchEvent(nodeImpl, mutationEventImpl);
        }
        this.dispatchAggregateEvents(nodeImpl, null, null, 0);
    }

    void renamedAttrNode(Attr attr, Attr attr2) {
    }

    void renamedElement(Element element, Element element2) {
    }

    class EnclosingAttr
    implements Serializable {
        private static final long serialVersionUID = 5208387723391647216L;
        AttrImpl node;
        String oldvalue;
        private final DocumentImpl this$0;

        EnclosingAttr(DocumentImpl documentImpl) {
            this.this$0 = documentImpl;
        }
    }

    class LEntry
    implements Serializable {
        private static final long serialVersionUID = -8426757059492421631L;
        String type;
        EventListener listener;
        boolean useCapture;
        private final DocumentImpl this$0;

        LEntry(DocumentImpl documentImpl, String string, EventListener eventListener, boolean bl) {
            this.this$0 = documentImpl;
            this.type = string;
            this.listener = eventListener;
            this.useCapture = bl;
        }
    }

}

