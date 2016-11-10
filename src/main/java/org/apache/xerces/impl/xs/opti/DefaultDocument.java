/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.impl.xs.opti.NodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class DefaultDocument
extends NodeImpl
implements Document {
    private String fDocumentURI = null;

    public DefaultDocument() {
        this.nodeType = 9;
    }

    public String getNodeName() {
        return "#document";
    }

    public DocumentType getDoctype() {
        return null;
    }

    public DOMImplementation getImplementation() {
        return null;
    }

    public Element getDocumentElement() {
        return null;
    }

    public NodeList getElementsByTagName(String string) {
        return null;
    }

    public NodeList getElementsByTagNameNS(String string, String string2) {
        return null;
    }

    public Element getElementById(String string) {
        return null;
    }

    public Node importNode(Node node, boolean bl) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Element createElement(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public DocumentFragment createDocumentFragment() {
        return null;
    }

    public Text createTextNode(String string) {
        return null;
    }

    public Comment createComment(String string) {
        return null;
    }

    public CDATASection createCDATASection(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public ProcessingInstruction createProcessingInstruction(String string, String string2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Attr createAttribute(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public EntityReference createEntityReference(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Element createElementNS(String string, String string2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Attr createAttributeNS(String string, String string2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public String getInputEncoding() {
        return null;
    }

    public String getXmlEncoding() {
        return null;
    }

    public boolean getXmlStandalone() {
        throw new DOMException(9, "Method not supported");
    }

    public void setXmlStandalone(boolean bl) {
        throw new DOMException(9, "Method not supported");
    }

    public String getXmlVersion() {
        return null;
    }

    public void setXmlVersion(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public boolean getStrictErrorChecking() {
        return false;
    }

    public void setStrictErrorChecking(boolean bl) {
        throw new DOMException(9, "Method not supported");
    }

    public String getDocumentURI() {
        return this.fDocumentURI;
    }

    public void setDocumentURI(String string) {
        this.fDocumentURI = string;
    }

    public Node adoptNode(Node node) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void normalizeDocument() {
        throw new DOMException(9, "Method not supported");
    }

    public DOMConfiguration getDomConfig() {
        throw new DOMException(9, "Method not supported");
    }

    public Node renameNode(Node node, String string, String string2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }
}

