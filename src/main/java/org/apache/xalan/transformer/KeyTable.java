/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xalan.transformer.KeyIterator;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class KeyTable {
    private int m_docKey;
    private Vector m_keyDeclarations;
    private Hashtable m_refsTable = null;
    private XNodeSet m_keyNodes;

    public int getDocKey() {
        return this.m_docKey;
    }

    KeyIterator getKeyIterator() {
        return (KeyIterator)this.m_keyNodes.getContainedIter();
    }

    public KeyTable(int n2, PrefixResolver prefixResolver, QName qName, Vector vector, XPathContext xPathContext) throws TransformerException {
        this.m_docKey = n2;
        this.m_keyDeclarations = vector;
        KeyIterator keyIterator = new KeyIterator(qName, vector);
        this.m_keyNodes = new XNodeSet(keyIterator);
        this.m_keyNodes.allowDetachToRelease(false);
        this.m_keyNodes.setRoot(n2, xPathContext);
    }

    public XNodeSet getNodeSetDTMByKey(QName qName, XMLString xMLString) {
        XNodeSet xNodeSet = (XNodeSet)this.getRefsTable().get(xMLString);
        try {
            if (xNodeSet != null) {
                xNodeSet = (XNodeSet)xNodeSet.cloneWithReset();
            }
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            xNodeSet = null;
        }
        if (xNodeSet == null) {
            KeyIterator keyIterator = (KeyIterator)this.m_keyNodes.getContainedIter();
            XPathContext xPathContext = keyIterator.getXPathContext();
            xNodeSet = new XNodeSet(this, xPathContext.getDTMManager()){
                private final KeyTable this$0;

                public void setRoot(int n2, Object object) {
                }
            };
            xNodeSet.reset();
        }
        return xNodeSet;
    }

    public QName getKeyTableName() {
        return this.getKeyIterator().getName();
    }

    private Vector getKeyDeclarations() {
        int n2 = this.m_keyDeclarations.size();
        Vector<KeyDeclaration> vector = new Vector<KeyDeclaration>(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            KeyDeclaration keyDeclaration = (KeyDeclaration)this.m_keyDeclarations.elementAt(i2);
            if (!keyDeclaration.getName().equals(this.getKeyTableName())) continue;
            vector.add(keyDeclaration);
        }
        return vector;
    }

    private Hashtable getRefsTable() {
        if (this.m_refsTable == null) {
            int n2;
            this.m_refsTable = new Hashtable(89);
            KeyIterator keyIterator = (KeyIterator)this.m_keyNodes.getContainedIter();
            XPathContext xPathContext = keyIterator.getXPathContext();
            Vector vector = this.getKeyDeclarations();
            int n3 = vector.size();
            this.m_keyNodes.reset();
            while (-1 != (n2 = this.m_keyNodes.nextNode())) {
                try {
                    for (int i2 = 0; i2 < n3; ++i2) {
                        int n4;
                        Object object;
                        KeyDeclaration keyDeclaration = (KeyDeclaration)vector.elementAt(i2);
                        XObject xObject = keyDeclaration.getUse().execute(xPathContext, n2, keyIterator.getPrefixResolver());
                        if (xObject.getType() != 4) {
                            object = xObject.xstr();
                            this.addValueInRefsTable(xPathContext, (XMLString)object, n2);
                            continue;
                        }
                        object = ((XNodeSet)xObject).iterRaw();
                        while (-1 != (n4 = object.nextNode())) {
                            DTM dTM = xPathContext.getDTM(n4);
                            XMLString xMLString = dTM.getStringValue(n4);
                            this.addValueInRefsTable(xPathContext, xMLString, n2);
                        }
                    }
                    continue;
                }
                catch (TransformerException transformerException) {
                    throw new WrappedRuntimeException(transformerException);
                }
            }
        }
        return this.m_refsTable;
    }

    private void addValueInRefsTable(XPathContext xPathContext, XMLString xMLString, int n2) {
        XNodeSet xNodeSet = (XNodeSet)this.m_refsTable.get(xMLString);
        if (xNodeSet == null) {
            xNodeSet = new XNodeSet(n2, xPathContext.getDTMManager());
            xNodeSet.nextNode();
            this.m_refsTable.put(xMLString, xNodeSet);
        } else if (xNodeSet.getCurrentNode() != n2) {
            xNodeSet.mutableNodeset().addNode(n2);
            xNodeSet.nextNode();
        }
    }

}

