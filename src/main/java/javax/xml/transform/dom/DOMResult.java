/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.transform.dom;

import javax.xml.transform.Result;
import org.w3c.dom.Node;

public class DOMResult
implements Result {
    private Node node = null;
    private Node nextSibling = null;
    private String systemId = null;

    public DOMResult() {
        this.setNode(null);
        this.setNextSibling(null);
        this.setSystemId(null);
    }

    public DOMResult(Node node) {
        this.setNode(node);
        this.setNextSibling(null);
        this.setSystemId(null);
    }

    public void setNode(Node node) {
        if (this.nextSibling != null) {
            if (node == null) {
                throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
            }
            if ((node.compareDocumentPosition(this.nextSibling) & 16) == 0) {
                throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
            }
        }
        this.node = node;
    }

    public Node getNode() {
        return this.node;
    }

    public void setNextSibling(Node node) {
        if (node != null) {
            if (this.node == null) {
                throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
            }
            if ((this.node.compareDocumentPosition(node) & 16) == 0) {
                throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
            }
        }
        this.nextSibling = node;
    }

    public Node getNextSibling() {
        return this.nextSibling;
    }

    public void setSystemId(String string) {
        this.systemId = string;
    }

    public String getSystemId() {
        return this.systemId;
    }
}

