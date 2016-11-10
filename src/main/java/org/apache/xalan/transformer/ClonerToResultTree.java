/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.SerializerUtils;
import org.apache.xml.dtm.DTM;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XMLString;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class ClonerToResultTree {
    public static void cloneToResultTree(int n2, int n3, DTM dTM, SerializationHandler serializationHandler, boolean bl) throws TransformerException {
        try {
            switch (n3) {
                case 3: {
                    dTM.dispatchCharactersEvents(n2, serializationHandler, false);
                    break;
                }
                case 9: 
                case 11: {
                    break;
                }
                case 1: {
                    String string = dTM.getNamespaceURI(n2);
                    if (string == null) {
                        string = "";
                    }
                    String string2 = dTM.getLocalName(n2);
                    serializationHandler.startElement(string, string2, dTM.getNodeNameX(n2));
                    if (bl) {
                        SerializerUtils.addAttributes(serializationHandler, n2);
                        SerializerUtils.processNSDecls(serializationHandler, n2, n3, dTM);
                        break;
                    }
                    break;
                }
                case 4: {
                    serializationHandler.startCDATA();
                    dTM.dispatchCharactersEvents(n2, serializationHandler, false);
                    serializationHandler.endCDATA();
                    break;
                }
                case 2: {
                    SerializerUtils.addAttribute(serializationHandler, n2);
                    break;
                }
                case 13: {
                    SerializerUtils.processNSDecls(serializationHandler, n2, 13, dTM);
                    break;
                }
                case 8: {
                    XMLString xMLString = dTM.getStringValue(n2);
                    xMLString.dispatchAsComment(serializationHandler);
                    break;
                }
                case 5: {
                    serializationHandler.entityReference(dTM.getNodeNameX(n2));
                    break;
                }
                case 7: {
                    serializationHandler.processingInstruction(dTM.getNodeNameX(n2), dTM.getNodeValue(n2));
                    break;
                }
                default: {
                    throw new TransformerException("Can't clone node: " + dTM.getNodeName(n2));
                }
            }
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
    }
}

