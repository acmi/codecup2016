/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.IOException;
import org.apache.xml.serialize.BaseMarkupSerializer;
import org.apache.xml.serialize.ElementState;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Printer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TextSerializer
extends BaseMarkupSerializer {
    public TextSerializer() {
        super(new OutputFormat("text", null, false));
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        this.startElement(string3 == null ? string2 : string3, null);
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        this.endElement(string3 == null ? string2 : string3);
    }

    public void startElement(String string, AttributeList attributeList) throws SAXException {
        try {
            ElementState elementState = this.getElementState();
            if (this.isDocumentState() && !this._started) {
                this.startDocument(string);
            }
            boolean bl = elementState.preserveSpace;
            elementState = this.enterElementState(null, null, string, bl);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void endElement(String string) throws SAXException {
        try {
            this.endElementIO(string);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void endElementIO(String string) throws IOException {
        ElementState elementState = this.getElementState();
        elementState = this.leaveElementState();
        elementState.afterElement = true;
        elementState.empty = false;
        if (this.isDocumentState()) {
            this._printer.flush();
        }
    }

    public void processingInstructionIO(String string, String string2) throws IOException {
    }

    public void comment(String string) {
    }

    public void comment(char[] arrc, int n2, int n3) {
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        try {
            ElementState elementState = this.content();
            elementState.inCData = false;
            elementState.doCData = false;
            this.printText(arrc, n2, n3, true, true);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    protected void characters(String string, boolean bl) throws IOException {
        ElementState elementState = this.content();
        elementState.inCData = false;
        elementState.doCData = false;
        this.printText(string, true, true);
    }

    protected void startDocument(String string) throws IOException {
        this._printer.leaveDTD();
        this._started = true;
        this.serializePreRoot();
    }

    protected void serializeElement(Element element) throws IOException {
        String string = element.getTagName();
        ElementState elementState = this.getElementState();
        if (this.isDocumentState() && !this._started) {
            this.startDocument(string);
        }
        boolean bl = elementState.preserveSpace;
        if (element.hasChildNodes()) {
            elementState = this.enterElementState(null, null, string, bl);
            Node node = element.getFirstChild();
            while (node != null) {
                this.serializeNode(node);
                node = node.getNextSibling();
            }
            this.endElementIO(string);
        } else if (!this.isDocumentState()) {
            elementState.afterElement = true;
            elementState.empty = false;
        }
    }

    protected void serializeNode(Node node) throws IOException {
        switch (node.getNodeType()) {
            case 3: {
                String string = node.getNodeValue();
                if (string == null) break;
                this.characters(node.getNodeValue(), true);
                break;
            }
            case 4: {
                String string = node.getNodeValue();
                if (string == null) break;
                this.characters(node.getNodeValue(), true);
                break;
            }
            case 8: {
                break;
            }
            case 5: {
                break;
            }
            case 7: {
                break;
            }
            case 1: {
                this.serializeElement((Element)node);
                break;
            }
            case 9: 
            case 11: {
                Node node2 = node.getFirstChild();
                while (node2 != null) {
                    this.serializeNode(node2);
                    node2 = node2.getNextSibling();
                }
                break block0;
            }
        }
    }

    protected ElementState content() {
        ElementState elementState = this.getElementState();
        if (!this.isDocumentState()) {
            if (elementState.empty) {
                elementState.empty = false;
            }
            elementState.afterElement = false;
        }
        return elementState;
    }

    protected String getEntityRef(int n2) {
        return null;
    }
}

