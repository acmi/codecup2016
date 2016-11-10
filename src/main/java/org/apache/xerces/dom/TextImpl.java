/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.CharacterDataImpl;
import org.apache.xerces.dom.ChildNode;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class TextImpl
extends CharacterDataImpl
implements CharacterData,
Text {
    static final long serialVersionUID = -5294980852957403469L;

    public TextImpl() {
    }

    public TextImpl(CoreDocumentImpl coreDocumentImpl, String string) {
        super(coreDocumentImpl, string);
    }

    public void setValues(CoreDocumentImpl coreDocumentImpl, String string) {
        this.flags = 0;
        this.nextSibling = null;
        this.previousSibling = null;
        this.setOwnerDocument(coreDocumentImpl);
        this.data = string;
    }

    public short getNodeType() {
        return 3;
    }

    public String getNodeName() {
        return "#text";
    }

    public void setIgnorableWhitespace(boolean bl) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.isIgnorableWhitespace(bl);
    }

    public boolean isElementContentWhitespace() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.internalIsIgnorableWhitespace();
    }

    public String getWholeText() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (this.data != null && this.data.length() != 0) {
            stringBuffer.append(this.data);
        }
        this.getWholeTextBackward(this.getPreviousSibling(), stringBuffer, this.getParentNode());
        String string = stringBuffer.toString();
        stringBuffer.setLength(0);
        this.getWholeTextForward(this.getNextSibling(), stringBuffer, this.getParentNode());
        return string + stringBuffer.toString();
    }

    protected void insertTextContent(StringBuffer stringBuffer) throws DOMException {
        String string = this.getNodeValue();
        if (string != null) {
            stringBuffer.insert(0, string);
        }
    }

    private boolean getWholeTextForward(Node node, StringBuffer stringBuffer, Node node2) {
        boolean bl = false;
        if (node2 != null) {
            bl = node2.getNodeType() == 5;
        }
        while (node != null) {
            short s2 = node.getNodeType();
            if (s2 == 5) {
                if (this.getWholeTextForward(node.getFirstChild(), stringBuffer, node)) {
                    return true;
                }
            } else if (s2 == 3 || s2 == 4) {
                ((NodeImpl)node).getTextContent(stringBuffer);
            } else {
                return true;
            }
            node = node.getNextSibling();
        }
        if (bl) {
            this.getWholeTextForward(node2.getNextSibling(), stringBuffer, node2.getParentNode());
            return true;
        }
        return false;
    }

    private boolean getWholeTextBackward(Node node, StringBuffer stringBuffer, Node node2) {
        boolean bl = false;
        if (node2 != null) {
            bl = node2.getNodeType() == 5;
        }
        while (node != null) {
            short s2 = node.getNodeType();
            if (s2 == 5) {
                if (this.getWholeTextBackward(node.getLastChild(), stringBuffer, node)) {
                    return true;
                }
            } else if (s2 == 3 || s2 == 4) {
                ((TextImpl)node).insertTextContent(stringBuffer);
            } else {
                return true;
            }
            node = node.getPreviousSibling();
        }
        if (bl) {
            this.getWholeTextBackward(node2.getPreviousSibling(), stringBuffer, node2.getParentNode());
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Text replaceWholeText(String string) throws DOMException {
        void var3_6;
        Node node;
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        Node node2 = this.getParentNode();
        if (string == null || string.length() == 0) {
            if (node2 == null) return null;
            node2.removeChild(this);
            return null;
        }
        if (this.ownerDocument().errorChecking) {
            if (!this.canModifyPrev(this)) {
                throw new DOMException(7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
            }
            if (!this.canModifyNext(this)) {
                throw new DOMException(7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
            }
        }
        java.lang.Object var3_3 = null;
        if (this.isReadOnly()) {
            node = this.ownerDocument().createTextNode(string);
            if (node2 == null) return node;
            node2.insertBefore(node, this);
            node2.removeChild(this);
            Node node3 = node;
        } else {
            this.setData(string);
            TextImpl textImpl = this;
        }
        node = var3_6.getPreviousSibling();
        while (node != null) {
            if (node.getNodeType() != 3 && node.getNodeType() != 4 && (node.getNodeType() != 5 || !this.hasTextOnlyChildren(node))) break;
            node2.removeChild(node);
            node = var3_6;
            node = node.getPreviousSibling();
        }
        Node node4 = var3_6.getNextSibling();
        while (node4 != null) {
            if (node4.getNodeType() != 3 && node4.getNodeType() != 4 && (node4.getNodeType() != 5 || !this.hasTextOnlyChildren(node4))) return var3_6;
            node2.removeChild(node4);
            node4 = var3_6;
            node4 = node4.getNextSibling();
        }
        return var3_6;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean canModifyPrev(Node var1_1) {
        var2_2 = false;
        var3_3 = var1_1.getPreviousSibling();
        while (var3_3 != null) {
            block8 : {
                var4_4 = var3_3.getNodeType();
                if (var4_4 != 5) ** GOTO lbl10
                var5_5 = var3_3.getLastChild();
                if (var5_5 == null) {
                    return false;
                }
                ** GOTO lbl24
lbl10: // 1 sources:
                if (var4_4 != 3) {
                    if (var4_4 != 4) return true;
                    break block8;
lbl-1000: // 1 sources:
                    {
                        var6_6 = var5_5.getNodeType();
                        if (var6_6 == 3 || var6_6 == 4) {
                            var2_2 = true;
                        } else {
                            if (var6_6 != 5) {
                                if (var2_2 == false) return true;
                                return false;
                            }
                            if (!this.canModifyPrev(var5_5)) {
                                return false;
                            }
                            var2_2 = true;
                        }
                        var5_5 = var5_5.getPreviousSibling();
lbl24: // 2 sources:
                        ** while (var5_5 != null)
                    }
                }
            }
            var3_3 = var3_3.getPreviousSibling();
        }
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean canModifyNext(Node var1_1) {
        var2_2 = false;
        var3_3 = var1_1.getNextSibling();
        while (var3_3 != null) {
            block8 : {
                var4_4 = var3_3.getNodeType();
                if (var4_4 != 5) ** GOTO lbl10
                var5_5 = var3_3.getFirstChild();
                if (var5_5 == null) {
                    return false;
                }
                ** GOTO lbl24
lbl10: // 1 sources:
                if (var4_4 != 3) {
                    if (var4_4 != 4) return true;
                    break block8;
lbl-1000: // 1 sources:
                    {
                        var6_6 = var5_5.getNodeType();
                        if (var6_6 == 3 || var6_6 == 4) {
                            var2_2 = true;
                        } else {
                            if (var6_6 != 5) {
                                if (var2_2 == false) return true;
                                return false;
                            }
                            if (!this.canModifyNext(var5_5)) {
                                return false;
                            }
                            var2_2 = true;
                        }
                        var5_5 = var5_5.getNextSibling();
lbl24: // 2 sources:
                        ** while (var5_5 != null)
                    }
                }
            }
            var3_3 = var3_3.getNextSibling();
        }
        return true;
    }

    private boolean hasTextOnlyChildren(Node node) {
        Node node2 = node;
        if (node2 == null) {
            return false;
        }
        node2 = node2.getFirstChild();
        while (node2 != null) {
            short s2 = node2.getNodeType();
            if (s2 == 5) {
                return this.hasTextOnlyChildren(node2);
            }
            if (s2 != 3 && s2 != 4 && s2 != 5) {
                return false;
            }
            node2 = node2.getNextSibling();
        }
        return true;
    }

    public boolean isIgnorableWhitespace() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.internalIsIgnorableWhitespace();
    }

    public Text splitText(int n2) throws DOMException {
        if (this.isReadOnly()) {
            throw new DOMException(7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
        }
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        if (n2 < 0 || n2 > this.data.length()) {
            throw new DOMException(1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null));
        }
        Text text = this.getOwnerDocument().createTextNode(this.data.substring(n2));
        this.setNodeValue(this.data.substring(0, n2));
        Node node = this.getParentNode();
        if (node != null) {
            node.insertBefore(text, this.nextSibling);
        }
        return text;
    }

    public void replaceData(String string) {
        this.data = string;
    }

    public String removeData() {
        String string = this.data;
        this.data = "";
        return string;
    }
}

