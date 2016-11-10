/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XString;

public class XRTreeFragSelectWrapper
extends XRTreeFrag
implements Cloneable {
    public XRTreeFragSelectWrapper(Expression expression) {
        super(expression);
    }

    public void fixupVariables(Vector vector, int n2) {
        ((Expression)this.m_obj).fixupVariables(vector, n2);
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XObject xObject = ((Expression)this.m_obj).execute(xPathContext);
        xObject.allowDetachToRelease(this.m_allowRelease);
        if (xObject.getType() == 3) {
            return xObject;
        }
        return new XString(xObject.str());
    }

    public void detach() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_DETACH_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    public double num() throws TransformerException {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NUM_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    public XMLString xstr() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_XSTR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    public String str() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_STR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    public int getType() {
        return 3;
    }

    public int rtf() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }

    public DTMIterator asNodeIterator() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", null));
    }
}

