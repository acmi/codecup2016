/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.serialize;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class SerializerUtils {
    public static void addAttribute(SerializationHandler serializationHandler, int n2) throws TransformerException {
        TransformerImpl transformerImpl = (TransformerImpl)serializationHandler.getTransformer();
        DTM dTM = transformerImpl.getXPathContext().getDTM(n2);
        if (SerializerUtils.isDefinedNSDecl(serializationHandler, n2, dTM)) {
            return;
        }
        String string = dTM.getNamespaceURI(n2);
        if (string == null) {
            string = "";
        }
        try {
            serializationHandler.addAttribute(string, dTM.getLocalName(n2), dTM.getNodeName(n2), "CDATA", dTM.getNodeValue(n2), false);
        }
        catch (SAXException sAXException) {
            // empty catch block
        }
    }

    public static void addAttributes(SerializationHandler serializationHandler, int n2) throws TransformerException {
        TransformerImpl transformerImpl = (TransformerImpl)serializationHandler.getTransformer();
        DTM dTM = transformerImpl.getXPathContext().getDTM(n2);
        int n3 = dTM.getFirstAttribute(n2);
        while (-1 != n3) {
            SerializerUtils.addAttribute(serializationHandler, n3);
            n3 = dTM.getNextAttribute(n3);
        }
    }

    public static void outputResultTreeFragment(SerializationHandler serializationHandler, XObject xObject, XPathContext xPathContext) throws SAXException {
        int n2 = xObject.rtf();
        DTM dTM = xPathContext.getDTM(n2);
        if (null != dTM) {
            int n3 = dTM.getFirstChild(n2);
            while (-1 != n3) {
                serializationHandler.flushPending();
                if (dTM.getNodeType(n3) == 1 && dTM.getNamespaceURI(n3) == null) {
                    serializationHandler.startPrefixMapping("", "");
                }
                dTM.dispatchToEvents(n3, serializationHandler);
                n3 = dTM.getNextSibling(n3);
            }
        }
    }

    public static void processNSDecls(SerializationHandler serializationHandler, int n2, int n3, DTM dTM) throws TransformerException {
        try {
            if (n3 == 1) {
                int n4 = dTM.getFirstNamespaceNode(n2, true);
                while (-1 != n4) {
                    String string = dTM.getNodeNameX(n4);
                    String string2 = serializationHandler.getNamespaceURIFromPrefix(string);
                    String string3 = dTM.getNodeValue(n4);
                    if (!string3.equalsIgnoreCase(string2)) {
                        serializationHandler.startPrefixMapping(string, string3, false);
                    }
                    n4 = dTM.getNextNamespaceNode(n2, n4, true);
                }
            } else if (n3 == 13) {
                String string = dTM.getNodeNameX(n2);
                String string4 = serializationHandler.getNamespaceURIFromPrefix(string);
                String string5 = dTM.getNodeValue(n2);
                if (!string5.equalsIgnoreCase(string4)) {
                    serializationHandler.startPrefixMapping(string, string5, false);
                }
            }
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
    }

    public static boolean isDefinedNSDecl(SerializationHandler serializationHandler, int n2, DTM dTM) {
        String string;
        String string2;
        if (13 == dTM.getNodeType(n2) && null != (string2 = serializationHandler.getNamespaceURIFromPrefix(string = dTM.getNodeNameX(n2))) && string2.equals(dTM.getStringValue(n2))) {
            return true;
        }
        return false;
    }

    public static void ensureNamespaceDeclDeclared(SerializationHandler serializationHandler, DTM dTM, int n2) throws SAXException {
        String string;
        NamespaceMappings namespaceMappings;
        String string2 = dTM.getNodeValue(n2);
        String string3 = dTM.getNodeNameX(n2);
        if (!(string2 == null || string2.length() <= 0 || null == string3 || (namespaceMappings = serializationHandler.getNamespaceMappings()) == null || null != (string = namespaceMappings.lookupNamespace(string3)) && string.equals(string2))) {
            serializationHandler.startPrefixMapping(string3, string2, false);
        }
    }
}

