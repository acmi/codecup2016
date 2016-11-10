/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionDef1Arg;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class FuncNormalizeSpace
extends FunctionDef1Arg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XMLString xMLString = this.getArg0AsString(xPathContext);
        return (XString)xMLString.fixWhiteSpace(true, true, false);
    }

    public void executeCharsToContentHandler(XPathContext xPathContext, ContentHandler contentHandler) throws TransformerException, SAXException {
        if (this.Arg0IsNodesetExpr()) {
            int n2 = this.getArg0AsNode(xPathContext);
            if (-1 != n2) {
                DTM dTM = xPathContext.getDTM(n2);
                dTM.dispatchCharactersEvents(n2, contentHandler, true);
            }
        } else {
            XObject xObject = this.execute(xPathContext);
            xObject.dispatchCharactersEvents(contentHandler);
        }
    }
}

