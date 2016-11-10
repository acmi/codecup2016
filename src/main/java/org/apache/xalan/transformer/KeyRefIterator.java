/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xalan.transformer.KeyIterator;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.ChildTestIterator;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class KeyRefIterator
extends ChildTestIterator {
    static final long serialVersionUID = 3837456451659435102L;
    DTMIterator m_keysNodes;
    protected XMLString m_ref;
    protected QName m_name;
    protected Vector m_keyDeclarations;

    public KeyRefIterator(QName qName, XMLString xMLString, Vector vector, DTMIterator dTMIterator) {
        super(null);
        this.m_name = qName;
        this.m_ref = xMLString;
        this.m_keyDeclarations = vector;
        this.m_keysNodes = dTMIterator;
        this.setWhatToShow(-1);
    }

    protected int getNextNode() {
        int n2;
        while (-1 != (n2 = this.m_keysNodes.nextNode()) && 1 != this.filterNode(n2)) {
        }
        this.m_lastFetched = n2;
        return n2;
    }

    public short filterNode(int n2) {
        boolean bl = false;
        Vector vector = this.m_keyDeclarations;
        QName qName = this.m_name;
        KeyIterator keyIterator = (KeyIterator)((XNodeSet)this.m_keysNodes).getContainedIter();
        XPathContext xPathContext = keyIterator.getXPathContext();
        if (null == xPathContext) {
            this.assertion(false, "xctxt can not be null here!");
        }
        try {
            XMLString xMLString = this.m_ref;
            int n3 = vector.size();
            for (int i2 = 0; i2 < n3; ++i2) {
                Object object;
                int n4;
                KeyDeclaration keyDeclaration = (KeyDeclaration)vector.elementAt(i2);
                if (!keyDeclaration.getName().equals(qName)) continue;
                bl = true;
                XObject xObject = keyDeclaration.getUse().execute(xPathContext, n2, keyIterator.getPrefixResolver());
                if (xObject.getType() != 4) {
                    object = xObject.xstr();
                    if (!xMLString.equals((XMLString)object)) continue;
                    return 1;
                }
                object = ((XNodeSet)xObject).iterRaw();
                while (-1 != (n4 = object.nextNode())) {
                    DTM dTM = this.getDTM(n4);
                    XMLString xMLString2 = dTM.getStringValue(n4);
                    if (null == xMLString2 || !xMLString.equals(xMLString2)) continue;
                    return 1;
                }
            }
        }
        catch (TransformerException transformerException) {
            throw new WrappedRuntimeException(transformerException);
        }
        if (!bl) {
            throw new RuntimeException(XSLMessages.createMessage("ER_NO_XSLKEY_DECLARATION", new Object[]{qName.getLocalName()}));
        }
        return 2;
    }
}

