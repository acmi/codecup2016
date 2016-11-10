/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMConfigurationImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.PSVIAttrNSImpl;
import org.apache.xerces.dom.PSVIDOMImplementationImpl;
import org.apache.xerces.dom.PSVIElementNSImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PSVIDocumentImpl
extends DocumentImpl {
    static final long serialVersionUID = -8822220250676434522L;

    public PSVIDocumentImpl() {
    }

    public PSVIDocumentImpl(DocumentType documentType) {
        super(documentType);
    }

    public Node cloneNode(boolean bl) {
        PSVIDocumentImpl pSVIDocumentImpl = new PSVIDocumentImpl();
        this.callUserDataHandlers(this, pSVIDocumentImpl, 1);
        this.cloneNode(pSVIDocumentImpl, bl);
        pSVIDocumentImpl.mutationEvents = this.mutationEvents;
        return pSVIDocumentImpl;
    }

    public DOMImplementation getImplementation() {
        return PSVIDOMImplementationImpl.getDOMImplementation();
    }

    public Element createElementNS(String string, String string2) throws DOMException {
        return new PSVIElementNSImpl(this, string, string2);
    }

    public Element createElementNS(String string, String string2, String string3) throws DOMException {
        return new PSVIElementNSImpl(this, string, string2, string3);
    }

    public Attr createAttributeNS(String string, String string2) throws DOMException {
        return new PSVIAttrNSImpl(this, string, string2);
    }

    public Attr createAttributeNS(String string, String string2, String string3) throws DOMException {
        return new PSVIAttrNSImpl(this, string, string2, string3);
    }

    public DOMConfiguration getDomConfig() {
        super.getDomConfig();
        return this.fConfiguration;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        throw new NotSerializableException(this.getClass().getName());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        throw new NotSerializableException(this.getClass().getName());
    }
}

