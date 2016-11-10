/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.OneStepIteratorForward;

public class KeyIterator
extends OneStepIteratorForward {
    static final long serialVersionUID = -1349109910100249661L;
    private QName m_name;
    private Vector m_keyDeclarations;

    public QName getName() {
        return this.m_name;
    }

    public Vector getKeyDeclarations() {
        return this.m_keyDeclarations;
    }

    KeyIterator(QName qName, Vector vector) {
        super(16);
        this.m_keyDeclarations = vector;
        this.m_name = qName;
    }

    public short acceptNode(int n2) {
        boolean bl = false;
        KeyIterator keyIterator = (KeyIterator)this.m_lpi;
        XPathContext xPathContext = keyIterator.getXPathContext();
        Vector vector = keyIterator.getKeyDeclarations();
        QName qName = keyIterator.getName();
        try {
            int n3 = vector.size();
            for (int i2 = 0; i2 < n3; ++i2) {
                KeyDeclaration keyDeclaration = (KeyDeclaration)vector.elementAt(i2);
                if (!keyDeclaration.getName().equals(qName)) continue;
                bl = true;
                XPath xPath = keyDeclaration.getMatch();
                double d2 = xPath.getMatchScore(xPathContext, n2);
                keyDeclaration.getMatch();
                if (d2 == Double.NEGATIVE_INFINITY) continue;
                return 1;
            }
        }
        catch (TransformerException transformerException) {
            // empty catch block
        }
        if (!bl) {
            throw new RuntimeException(XSLMessages.createMessage("ER_NO_XSLKEY_DECLARATION", new Object[]{qName.getLocalName()}));
        }
        return 2;
    }
}

