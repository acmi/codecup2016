/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemElement;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.XML11Char;
import org.xml.sax.SAXException;

public class ElemAttribute
extends ElemElement {
    static final long serialVersionUID = 8817220961566919187L;

    public int getXSLToken() {
        return 48;
    }

    public String getNodeName() {
        return "attribute";
    }

    protected String resolvePrefix(SerializationHandler serializationHandler, String string, String string2) throws TransformerException {
        if (null != string && (string.length() == 0 || string.equals("xmlns")) && (null == (string = serializationHandler.getPrefix(string2)) || string.length() == 0 || string.equals("xmlns"))) {
            if (string2.length() > 0) {
                NamespaceMappings namespaceMappings = serializationHandler.getNamespaceMappings();
                string = namespaceMappings.generateNextPrefix();
            } else {
                string = "";
            }
        }
        return string;
    }

    protected boolean validateNodeName(String string) {
        if (null == string) {
            return false;
        }
        if (string.equals("xmlns")) {
            return false;
        }
        return XML11Char.isXML11ValidQName(string);
    }

    void constructNode(String string, String string2, String string3, TransformerImpl transformerImpl) throws TransformerException {
        if (null != string && string.length() > 0) {
            SerializationHandler serializationHandler = transformerImpl.getSerializationHandler();
            String string4 = transformerImpl.transformToString(this);
            try {
                String string5 = QName.getLocalPart(string);
                if (string2 != null && string2.length() > 0) {
                    serializationHandler.addAttribute(string3, string5, string, "CDATA", string4, true);
                } else {
                    serializationHandler.addAttribute("", string5, string, "CDATA", string4, true);
                }
            }
            catch (SAXException sAXException) {
                // empty catch block
            }
        }
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        switch (n2) {
            case 9: 
            case 17: 
            case 28: 
            case 30: 
            case 35: 
            case 36: 
            case 37: 
            case 42: 
            case 50: 
            case 72: 
            case 73: 
            case 74: 
            case 75: 
            case 78: {
                break;
            }
            default: {
                this.error("ER_CANNOT_ADD", new Object[]{elemTemplateElement.getNodeName(), this.getNodeName()});
            }
        }
        return super.appendChild(elemTemplateElement);
    }

    public void setName(AVT aVT) {
        if (aVT.isSimple() && aVT.getSimpleString().equals("xmlns")) {
            throw new IllegalArgumentException();
        }
        super.setName(aVT);
    }
}

