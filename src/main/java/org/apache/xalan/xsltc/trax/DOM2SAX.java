/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

public class DOM2SAX
implements Locator,
XMLReader {
    private static final String EMPTYSTRING = "";
    private static final String XMLNS_PREFIX = "xmlns";
    private Node _dom = null;
    private ContentHandler _sax = null;
    private LexicalHandler _lex = null;
    private SAXImpl _saxImpl = null;
    private Hashtable _nsPrefixes = new Hashtable();

    public DOM2SAX(Node node) {
        this._dom = node;
    }

    public ContentHandler getContentHandler() {
        return this._sax;
    }

    public void setContentHandler(ContentHandler contentHandler) throws NullPointerException {
        this._sax = contentHandler;
        if (contentHandler instanceof LexicalHandler) {
            this._lex = (LexicalHandler)((Object)contentHandler);
        }
        if (contentHandler instanceof SAXImpl) {
            this._saxImpl = (SAXImpl)contentHandler;
        }
    }

    private boolean startPrefixMapping(String string, String string2) throws SAXException {
        boolean bl = true;
        Stack<String> stack = (Stack<String>)this._nsPrefixes.get(string);
        if (stack != null) {
            if (stack.isEmpty()) {
                this._sax.startPrefixMapping(string, string2);
                stack.push(string2);
            } else {
                String string3 = (String)stack.peek();
                if (!string3.equals(string2)) {
                    this._sax.startPrefixMapping(string, string2);
                    stack.push(string2);
                } else {
                    bl = false;
                }
            }
        } else {
            this._sax.startPrefixMapping(string, string2);
            stack = new Stack<String>();
            this._nsPrefixes.put(string, stack);
            stack.push(string2);
        }
        return bl;
    }

    private void endPrefixMapping(String string) throws SAXException {
        Stack stack = (Stack)this._nsPrefixes.get(string);
        if (stack != null) {
            this._sax.endPrefixMapping(string);
            stack.pop();
        }
    }

    private static String getLocalName(Node node) {
        String string = node.getLocalName();
        if (string == null) {
            String string2 = node.getNodeName();
            int n2 = string2.lastIndexOf(58);
            return n2 > 0 ? string2.substring(n2 + 1) : string2;
        }
        return string;
    }

    public void parse(InputSource inputSource) throws IOException, SAXException {
        this.parse(this._dom);
    }

    public void parse() throws IOException, SAXException {
        if (this._dom != null) {
            boolean bl;
            boolean bl2 = bl = this._dom.getNodeType() != 9;
            if (bl) {
                this._sax.startDocument();
                this.parse(this._dom);
                this._sax.endDocument();
            } else {
                this.parse(this._dom);
            }
        }
    }

    private void parse(Node node) throws IOException, SAXException {
        Object var2_2 = null;
        if (node == null) {
            return;
        }
        switch (node.getNodeType()) {
            case 2: 
            case 5: 
            case 6: 
            case 10: 
            case 11: 
            case 12: {
                break;
            }
            case 4: {
                String string = node.getNodeValue();
                if (this._lex != null) {
                    this._lex.startCDATA();
                    this._sax.characters(string.toCharArray(), 0, string.length());
                    this._lex.endCDATA();
                    break;
                }
                this._sax.characters(string.toCharArray(), 0, string.length());
                break;
            }
            case 8: {
                if (this._lex == null) break;
                String string = node.getNodeValue();
                this._lex.comment(string.toCharArray(), 0, string.length());
                break;
            }
            case 9: {
                this._sax.setDocumentLocator(this);
                this._sax.startDocument();
                for (Node node2 = node.getFirstChild(); node2 != null; node2 = node2.getNextSibling()) {
                    this.parse(node2);
                }
                this._sax.endDocument();
                break;
            }
            case 1: {
                String string2;
                int string;
                String string3;
                int n2;
                String string32 /* !! */ ;
                Object object;
                ArrayList<String> arrayList = new ArrayList<String>();
                AttributesImpl attributesImpl = new AttributesImpl();
                NamedNodeMap namedNodeMap = node.getAttributes();
                int n4 = namedNodeMap.getLength();
                for (n2 = 0; n2 < n4; ++n2) {
                    object = namedNodeMap.item(n2);
                    string3 = object.getNodeName();
                    if (!string3.startsWith("xmlns")) continue;
                    string32 /* !! */  = object.getNodeValue();
                    string = string3.lastIndexOf(58);
                    String string4 = string2 = string > 0 ? string3.substring(string + 1) : "";
                    if (!this.startPrefixMapping(string2, string32 /* !! */ )) continue;
                    arrayList.add(string2);
                }
                for (n2 = 0; n2 < n4; ++n2) {
                    object = namedNodeMap.item(n2);
                    string3 = object.getNodeName();
                    if (string3.startsWith("xmlns")) continue;
                    String string5 = object.getNamespaceURI();
                    String string6 = DOM2SAX.getLocalName((Node)object);
                    if (string5 != null) {
                        int n3 = string3.lastIndexOf(58);
                        String string7 = string2 = n3 > 0 ? string3.substring(0, n3) : "";
                        if (this.startPrefixMapping(string2, string5)) {
                            arrayList.add(string2);
                        }
                    }
                    attributesImpl.addAttribute(object.getNamespaceURI(), DOM2SAX.getLocalName((Node)object), string3, "CDATA", object.getNodeValue());
                }
                String string7 = node.getNodeName();
                object = node.getNamespaceURI();
                string3 = DOM2SAX.getLocalName(node);
                if (object != null) {
                    int n5 = string7.lastIndexOf(58);
                    String string8 = string2 = n5 > 0 ? string7.substring(0, n5) : "";
                    if (this.startPrefixMapping(string2, (String)object)) {
                        arrayList.add(string2);
                    }
                }
                if (this._saxImpl != null) {
                    this._saxImpl.startElement((String)object, string3, string7, attributesImpl, node);
                } else {
                    this._sax.startElement((String)object, string3, string7, attributesImpl);
                }
                for (Node node3 = node.getFirstChild(); node3 != null; node3 = node3.getNextSibling()) {
                    this.parse(node3);
                }
                this._sax.endElement((String)object, string3, string7);
                string32 /* !! */  = (String)arrayList.size();
                for (string = 0; string < string32 /* !! */ ; ++string) {
                    this.endPrefixMapping((String)arrayList.get(string));
                }
                break;
            }
            case 7: {
                this._sax.processingInstruction(node.getNodeName(), node.getNodeValue());
                break;
            }
            case 3: {
                String string = node.getNodeValue();
                this._sax.characters(string.toCharArray(), 0, string.length());
            }
        }
    }

    public DTDHandler getDTDHandler() {
        return null;
    }

    public ErrorHandler getErrorHandler() {
        return null;
    }

    public boolean getFeature(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    public void setFeature(String string, boolean bl) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    public void parse(String string) throws IOException, SAXException {
        throw new IOException("This method is not yet implemented.");
    }

    public void setDTDHandler(DTDHandler dTDHandler) throws NullPointerException {
    }

    public void setEntityResolver(EntityResolver entityResolver) throws NullPointerException {
    }

    public EntityResolver getEntityResolver() {
        return null;
    }

    public void setErrorHandler(ErrorHandler errorHandler) throws NullPointerException {
    }

    public void setProperty(String string, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    public Object getProperty(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        return null;
    }

    public int getColumnNumber() {
        return 0;
    }

    public int getLineNumber() {
        return 0;
    }

    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        return null;
    }

    private String getNodeTypeFromCode(short s2) {
        String string = null;
        switch (s2) {
            case 2: {
                string = "ATTRIBUTE_NODE";
                break;
            }
            case 4: {
                string = "CDATA_SECTION_NODE";
                break;
            }
            case 8: {
                string = "COMMENT_NODE";
                break;
            }
            case 11: {
                string = "DOCUMENT_FRAGMENT_NODE";
                break;
            }
            case 9: {
                string = "DOCUMENT_NODE";
                break;
            }
            case 10: {
                string = "DOCUMENT_TYPE_NODE";
                break;
            }
            case 1: {
                string = "ELEMENT_NODE";
                break;
            }
            case 6: {
                string = "ENTITY_NODE";
                break;
            }
            case 5: {
                string = "ENTITY_REFERENCE_NODE";
                break;
            }
            case 12: {
                string = "NOTATION_NODE";
                break;
            }
            case 7: {
                string = "PROCESSING_INSTRUCTION_NODE";
                break;
            }
            case 3: {
                string = "TEXT_NODE";
            }
        }
        return string;
    }
}

